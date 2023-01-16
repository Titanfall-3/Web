package de.presti.titanfall.backend.controller;

import de.presti.titanfall.backend.entities.Invite;
import de.presti.titanfall.backend.repository.InviteRepository;
import de.presti.titanfall.backend.repository.UserRepository;
import de.presti.titanfall.backend.services.SessionServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/invite")
public class InviteController {

    private final UserRepository userRepository;
    private final InviteRepository inviteRepository;
    private final SessionServices sessionServices;

    @Autowired
    public InviteController(UserRepository userRepository, InviteRepository inviteRepository, SessionServices sessionServices) {
        this.userRepository = userRepository;
        this.inviteRepository = inviteRepository;
        this.sessionServices = sessionServices;
    }

    @CrossOrigin
    @PostMapping("/create")
    public Mono<InviteResponse> create(@RequestBody InviteCreateForm inviteCreateForm) {
        return sessionServices.checkAndRefreshSession(inviteCreateForm.sessionToken)
                .flatMap(session -> {
                    if (session == null) return Mono.error(new Exception("Invalid Session."));

                    return userRepository.findById(session.getUserId());
                }).flatMap(user -> {
                    if (user == null) return Mono.error(new Exception("Invalid Session"));

                    if (!user.isAdmin()) {
                        return Mono.error(new Exception("No Admin"));
                    } else {
                        Invite invite = new Invite(UUID.randomUUID().toString(), user.getId(), false, LocalDateTime.now());
                        return inviteRepository.save(invite);
                    }
                }).map(invite -> {
                    return new InviteResponse(true, invite.getCode());
                })
                .onErrorResume(Exception.class, e -> Mono.just(new InviteResponse(false, e.getMessage())))
                .onErrorReturn(new InviteResponse(false, "Server Error!"));
    }

    public record InviteCreateForm(String sessionToken) {}

    public record InviteResponse(boolean success, String message) {}

}
