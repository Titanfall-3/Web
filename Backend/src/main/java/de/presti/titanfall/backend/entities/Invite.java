package de.presti.titanfall.backend.entities;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import java.util.Date;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter(AccessLevel.PUBLIC)
public class Invite {

    @Id
    private String code;

    private long userId;

    private boolean admin;

    @Column("created_at")
    private Date created;
}
