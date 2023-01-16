package de.presti.titanfall.backend.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter(AccessLevel.PUBLIC)
public class Session {
    @Id
    private long id;

    @Column("token")
    private String token;

    @Column("user_id")
    private long userId;

    @Column("created_at")
    private LocalDateTime created;

    @Setter(AccessLevel.PUBLIC)
    @Column("last_login")
    private LocalDateTime login;

    public Session(String token, long userId, LocalDateTime created, LocalDateTime login) {
        this.token = token;
        this.userId = userId;
        this.created = created;
        this.login = login;
    }
}
