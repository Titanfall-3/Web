package de.presti.titanfall.backend.repository;

import de.presti.titanfall.backend.entities.Session;
import de.presti.titanfall.backend.entities.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface SessionRepository extends ReactiveCrudRepository<Session, String> {

    @Query("SELECT * FROM session WHERE user_id = :userId")
    Flux<Session> findByUser(long userId);

    @Query("DELETE FROM session WHERE user_id = :userId")
    Mono<Session> deleteAllByUser(long userId);
}
