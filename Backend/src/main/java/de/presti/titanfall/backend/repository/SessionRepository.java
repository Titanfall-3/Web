package de.presti.titanfall.backend.repository;

import de.presti.titanfall.backend.entities.Session;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface SessionRepository extends ReactiveCrudRepository<Session, String> {

    @Query("SELECT * FROM session WHERE user_id = :userId")
    Flux<Session> findByUser(long userId);

    @Query("SELECT * FROM session WHERE token = :token")
    Flux<Session> findByToken(String token);

    @Query("DELETE FROM session WHERE user_id = :userId")
    Mono<Void> deleteAllByUser(long userId);
}
