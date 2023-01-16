package de.presti.titanfall.backend.repository;

import de.presti.titanfall.backend.entities.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Long> {

    @Query("SELECT * FROM user WHERE username = :username")
    Mono<User> findByName(String username);

    @Query("SELECT * FROM user WHERE email = :email")
    Flux<User> findByEmail(String email);

    @Query("SELECT * FROM user WHERE admin = :admin")
    Flux<User> findAdmin(boolean admin);
}
