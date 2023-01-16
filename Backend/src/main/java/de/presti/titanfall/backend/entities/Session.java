package de.presti.titanfall.backend.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import java.util.Date;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter(AccessLevel.PUBLIC)
public class Session {
    @Id
    private String token;

    private long userId;

    @Column("created_at")
    private Date created;

    @Setter(AccessLevel.PUBLIC)
    @Column("last_login")
    private Date login;
}
