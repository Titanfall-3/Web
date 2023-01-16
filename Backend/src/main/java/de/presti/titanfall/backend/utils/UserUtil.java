package de.presti.titanfall.backend.utils;

import de.presti.titanfall.backend.entities.User;

public class UserUtil {

    public static User createCleanUser(User user) {
        return new User(user.getUsername(), null, user.getEmail(), user.isAdmin(), user.getCreated(), user.getUpdated());
    }
}
