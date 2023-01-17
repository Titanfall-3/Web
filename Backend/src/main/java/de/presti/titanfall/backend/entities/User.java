package de.presti.titanfall.backend.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class User {
    @Id
    private long id;

    @Setter(AccessLevel.PUBLIC)
    private String username;

    @Setter(AccessLevel.PUBLIC)
    private String password;

    @Setter(AccessLevel.PUBLIC)
    private String email;

    @Setter(AccessLevel.PUBLIC)
    public boolean admin;

    @Column("created_at")
    LocalDateTime created;

    @Column("updated_at")
    LocalDateTime updated;

    public User(String username, String password, String email, boolean admin, LocalDateTime created, LocalDateTime updated) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.admin = admin;
        this.created = created;
        this.updated = updated;
    }
}
