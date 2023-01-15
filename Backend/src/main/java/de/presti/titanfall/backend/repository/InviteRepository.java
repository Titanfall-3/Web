package de.presti.titanfall.backend.repository;

import de.presti.titanfall.backend.entities.Invite;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface InviteRepository extends ReactiveCrudRepository<Invite, String> {

    @Query("SELECT * FROM invite WHERE userId = :userid")
    Flux<Invite> findAllByUser(long userId);

    @Query("SELECT * FROM invite WHERE admin = true")
    Flux<Invite> findAllAdmin();
}
