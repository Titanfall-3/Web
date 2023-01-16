package de.presti.titanfall.backend.entities;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter(AccessLevel.PUBLIC)
public class Invite {

    @Id
    private long id;

    @Column("code")
    private String code;

    @Column("user_id")
    private long userId;

    private boolean admin;

    @Column("created_at")
    private LocalDateTime created;

    public Invite(String code, long userId, boolean admin, LocalDateTime created) {
        this.code = code;
        this.userId = userId;
        this.admin = admin;
        this.created = created;
    }
}
