package de.presti.titanfall.backend.repository;

import de.presti.titanfall.backend.entities.Invite;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface InviteRepository extends ReactiveCrudRepository<Invite, String> {

    @Query("SELECT * FROM invite WHERE user_id = :userid")
    Flux<Invite> findAllByUser(long userid);

    @Query("SELECT * FROM invite WHERE code = :code")
    Mono<Invite> findByCode(String code);

    @Query("SELECT * FROM invite WHERE admin = 1")
    Flux<Invite> findAllAdmin();
}
