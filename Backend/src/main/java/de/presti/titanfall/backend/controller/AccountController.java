package de.presti.titanfall.backend.controller;

import com.google.gson.JsonObject;
import de.presti.titanfall.backend.entities.User;
import de.presti.titanfall.backend.repository.UserRepository;
import de.presti.titanfall.backend.utils.CaptchaUtil;
import de.presti.titanfall.backend.utils.HashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final UserRepository userRepository;
    private final CaptchaUtil captchaUtil;

    @Autowired
    public AccountController(UserRepository userRepository, CaptchaUtil captchaUtil) {
        this.userRepository = userRepository;
        this.captchaUtil = captchaUtil;
    }

    //region Auth

    @CrossOrigin
    @PostMapping("/auth")
    public Mono<AuthResponse> auth(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(JSONObject.class).flatMap(jsonObject -> {
            String username, password, version;
            try {
                username = jsonObject.getString("username");
                password = jsonObject.getString("password");
                version = jsonObject.getString("version");
            } catch (JSONException e) {
                return Mono.just(new AuthResponse(3, null, "Invalid JSON"));
            }

            // TODO:: add future version check

            return userRepository.findByName(username).elementAt(0).onErrorReturn(new User("", "", "", false, new Date(), new Date())).flatMap(user -> {
                if (HashUtil.encoder().matches(password, user.getPassword())) {
                    return Mono.just(new AuthResponse(1, user, "Login successful!"));
                } else {
                    return Mono.just(new AuthResponse(2, null, "Invalid password"));
                }
            });
        });
    }

    public record AuthResponse(@NonNull int code, @Nullable User user, @Nullable String message) {

        @Override
        public String toString() {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("ResultCode", code);
            if (user != null) {
                jsonObject.addProperty("UserId", user.getId());
                jsonObject.addProperty("Nickname", user.getUsername());
            }

            if (message != null) jsonObject.addProperty("message", message);
            return jsonObject.toString();
        }
    }

    //endregion

    //region Login

    @CrossOrigin
    @PostMapping("/login")
    public Mono<LoginResponse> login(ServerRequest serverRequest, @RequestBody LoginForm loginForm) {

        Optional<Object> optionalCaptcha = serverRequest.attribute("h-captcha-response");
        String usedCaptchaSolve = loginForm.captchaKey == null ?
                (String) optionalCaptcha.orElse(null) :
                loginForm.captchaKey;

        if (loginForm.username == null || loginForm.password == null || loginForm.captchaKey == null) {
            return Mono.just(new LoginResponse(false, null, "Invalid post data"));
        }

        return userRepository.findByName(loginForm.username).elementAt(0).onErrorReturn(new User("", "", "", false, new Date(), new Date())).flatMap(user -> {
            if (HashUtil.encoder().matches(loginForm.password, user.getPassword())) {
                // TODO:: create Session and give it to user.

                return Mono.just(new LoginResponse(true, "", "Login successful!"));
            } else {
                return Mono.just(new LoginResponse(false, "", "Invalid password"));
            }
        });
    }

    public record LoginResponse(boolean success, String data, String message) {
    }

    public record LoginForm(String username, String password, String captchaKey) {
    }

    //endregion

    //region Register

    //endregion
}
