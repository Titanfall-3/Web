package de.presti.titanfall.backend.services;

import de.presti.titanfall.backend.entities.Session;
import de.presti.titanfall.backend.repository.SessionRepository;
import de.presti.titanfall.backend.repository.UserRepository;
import de.presti.titanfall.backend.utils.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class SessionServices {

    // Repository for accessing sessions
    private final SessionRepository sessionRepository;

    // Repository for getting account data
    private final UserRepository userRepository;

    @Autowired
    public SessionServices(SessionRepository sessionRepository, UserRepository userRepository) {
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
    }

    public Mono<Session> generateSession(long userID) {

        // Generate a unique session identifier
        AtomicReference<String> token = new AtomicReference<>(RandomUtils.getRandomBase64String(64));

        // Get all sessions of the user
        return sessionRepository.findByUser(userID).collectList().flatMap(list -> {

            // Sessions to delete from the database
            ArrayList<Session> toDelete = new ArrayList<>();

            // Remove old sessions
            for(Session session : list) {
                if(session.getCreated().toInstant().toEpochMilli() + TimeUnit.DAYS.toMillis(3) < System.currentTimeMillis()) {
                    toDelete.add(session);
                }
            }

            // Check if user has too many sessions
            if (list.size() - toDelete.size() >= 5) {
                return Mono.error(new Exception("Too many sessions"));
            }

            // Check if session token is already used
            return Mono.zip(
                    // Delete all tfa sessions
                    sessionRepository.deleteAllByUser(userID).thenReturn("deleted"),
                    sessionRepository.save(new Session(token.get(), userID, new Date(), new Date())),
                    sessionRepository.deleteAll(toDelete).thenReturn(new Session("d", -1, new Date(), new Date())));
        }).map(Tuple2::getT2);
    }

    public Mono<Session> checkAndRefreshSession(String token) {

        // Check if session exists
        return sessionRepository.findAllById(Collections.singleton(token)).collectList().flatMap(sessions -> {

            // Check if there are two sessions with the same token (really unlikely)
            if(sessions.size() > 1) {
                return Mono.error(new Exception("Session expired"));
            }

            if(sessions.isEmpty()) {
                return Mono.error(new Exception("Session expired"));
            }

            // Check if session is timed out
            if(sessions.get(0).getLogin().toInstant().toEpochMilli() + TimeUnit.DAYS.toMillis(3) < System.currentTimeMillis()) {

                // Delete session
                return sessionRepository.delete(sessions.get(0)).thenReturn("delete");
            }

            // Refresh session
            sessions.get(0).setLogin(new Date());

            // Save session
            return sessionRepository.save(sessions.get(0));
        }).flatMap(obj -> {

            // Check if session has been saved
            if(obj instanceof Session) {
                return Mono.just((Session) obj);
            }

            return Mono.just(null);
        });
    }

}
