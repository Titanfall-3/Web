package de.presti.titanfall.backend.controller;

import com.google.gson.JsonObject;
import de.presti.titanfall.backend.entities.User;
import de.presti.titanfall.backend.repository.UserRepository;
import de.presti.titanfall.backend.utils.HashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.Date;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final UserRepository userRepository;

    @Autowired
    public AccountController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public Mono<LoginResponse> login(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(JSONObject.class)
                .flatMap(jsonObject -> {
                    String username, password, version;
                    try {
                        username = jsonObject.getString("username");
                        password = jsonObject.getString("password");
                        version = jsonObject.getString("version");
                    } catch (JSONException e) {
                        return Mono.just(new LoginResponse(3, null,"Invalid JSON"));
                    }

                    // TODO:: add future version check

                    return userRepository.findByName(username).elementAt(0)
                            .onErrorReturn(new User("", "", "", false, new Date(), new Date()))
                            .flatMap(user -> {
                        if (HashUtil.encoder().matches(password, user.getPassword())) {
                            return Mono.just(new LoginResponse(2, null, "Invalid password"));
                        } else {
                            return Mono.just(new LoginResponse(1, user, "Login successful!"));
                        }
                    });
                });
    }

    public record LoginResponse(@NonNull int code, @Nullable User user, @Nullable String message) {

        @Override
        public String toString() {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("ResultCode", code);
            if (user != null) {
                jsonObject.addProperty("UserId", user.getId());
                jsonObject.addProperty("Nickname", user.getUsername());
            }

            if (message != null)
                jsonObject.addProperty("message", message);
            return jsonObject.toString();
        }
    }
}
