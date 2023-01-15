package de.presti.titanfall.backend.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import java.util.Date;

@Getter(AccessLevel.PUBLIC)
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
    Date created;

    @Column("updated_at")
    Date updated;

    public User(String username, String password, String email, boolean admin, Date created, Date updated) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.admin = admin;
        this.created = created;
        this.updated = updated;
    }
}
