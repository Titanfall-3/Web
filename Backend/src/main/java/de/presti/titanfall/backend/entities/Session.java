package de.presti.titanfall.backend.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import java.util.Date;

@RequiredArgsConstructor
@Getter(AccessLevel.PUBLIC)
public class Session {
    @Id
    private String token;

    private long userId;

    private boolean admin;

    @Column("created_at")
    private Date created;

    @Column("last_login")
    private Date login;
}
