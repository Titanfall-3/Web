package de.presti.titanfall.backend.controller;

import de.presti.titanfall.backend.entities.Invite;
import de.presti.titanfall.backend.entities.User;
import de.presti.titanfall.backend.repository.InviteRepository;
import de.presti.titanfall.backend.repository.UserRepository;
import de.presti.titanfall.backend.services.SessionServices;
import de.presti.titanfall.backend.utils.CaptchaUtil;
import de.presti.titanfall.backend.utils.HashUtil;
import de.presti.titanfall.backend.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final UserRepository userRepository;
    private final InviteRepository inviteRepository;
    private final SessionServices sessionServices;
    private final CaptchaUtil captchaUtil;

    @Autowired
    public AccountController(UserRepository userRepository, InviteRepository inviteRepository, SessionServices sessionServices, CaptchaUtil captchaUtil) {
        this.userRepository = userRepository;
        this.inviteRepository = inviteRepository;
        this.sessionServices = sessionServices;
        this.captchaUtil = captchaUtil;
    }

    //region Auth

    @CrossOrigin
    @PostMapping(value = "/auth", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<AuthResponse> auth(@RequestBody AuthForm authForm, @RequestParam String secret) {
        if (!secret.equals(HashUtil.authSecret)) return Mono.just(new AuthResponse(2, 0, null, "Invalid secret"));
        if (authForm.username == null || authForm.password == null || authForm.version == null) {
            return Mono.just(new AuthResponse(2, 0, null, "Invalid post data"));
        }

        return userRepository.findByName(authForm.username).elementAt(0).onErrorReturn(new User("", "", "", false,
                LocalDateTime.now(), LocalDateTime.now())).flatMap(user -> {
            if (user.getUsername().isBlank()) {
                return Mono.just(new AuthResponse(2, 0, null, "User not found"));
            }

            if (HashUtil.encoder().matches(authForm.password, user.getPassword())) {
                return Mono.just(new AuthResponse(1, user.getId(), user.getUsername(), "Login successful!"));
            } else {
                return Mono.just(new AuthResponse(2, 0, null, "Invalid password"));
            }
        });
    }

    public record AuthForm(String username, String password, String version) {
    }

    public record AuthResponse(@NonNull int ResultCode, @NonNull long UserId, @Nullable String Nickname, @Nullable String message) {
    }

    //endregion

    //region Refresh

    @CrossOrigin
    @PostMapping(value = "/refresh", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<RefreshResponse> refresh(@RequestBody RefreshForm refreshForm) {
        return sessionServices.checkAndRefreshSession(refreshForm.token)
                .flatMap(session -> {
                    if (session == null) return Mono.error(new Exception("Invalid Session."));

                    return Mono.zip(Mono.just(session), userRepository.findById(session.getUserId()));
                }).flatMap(tuple -> {
                    if (tuple.getT2() == null) return Mono.error(new Exception("Invalid Session"));

                    return Mono.just(new RefreshResponse(true, UserUtil.createCleanUser(tuple.getT2()), tuple.getT1().getToken()));
                })
                .onErrorResume(Exception.class, e -> Mono.just(new RefreshResponse(false, null, e.getMessage())))
                .onErrorReturn(new RefreshResponse(false, null, "Server Error!"));
    }


    public record RefreshResponse(boolean success, User user, String message) {
    }

    public record RefreshForm(String token) {
    }


    //endregion

    //region Logout

    @CrossOrigin
    @PostMapping(value = "/invalidate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<LogoutResponse> logout(@RequestBody LogoutForm logoutForm) {
        if (logoutForm.token == null) return Mono.just(new LogoutResponse(false, "No valid session"));

        return sessionServices.invalidateSession(logoutForm.token).flatMap(v -> {
                    return Mono.just(new LogoutResponse(true, "Deleted"));
                })
                .onErrorResume(Exception.class, e -> Mono.just(new LogoutResponse(false, e.getMessage())))
                .onErrorReturn(new LogoutResponse(false, "Invalid Session"));
    }

    public record LogoutForm(String token) {
    }

    public record LogoutResponse(boolean success, String message) {
    }

    //endregion

    //region Login

    @CrossOrigin
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<LoginResponse> login(@RequestBody LoginForm loginForm) {

        //// Optional<Object> optionalCaptcha = serverRequest.attribute("h-captcha-response");
        //// String usedCaptchaSolve = loginForm.captchaKey == null ? (String) optionalCaptcha.orElse(null) : loginForm.captchaKey;
        String usedCaptchaSolve = loginForm.captchaKey;

        if (loginForm.username == null || loginForm.password == null || usedCaptchaSolve == null) {
            return Mono.just(new LoginResponse(false, null, null, "Invalid post data"));
        }

        return captchaUtil.validCaptcha(usedCaptchaSolve).flatMap(value -> {
            if (!value) return Mono.just(new LoginResponse(false, null, null, "Invalid captcha"));

            return userRepository.findByName(loginForm.username)
                    .elementAt(0)
                    .onErrorReturn(new User("", "", "", false, LocalDateTime.now(), LocalDateTime.now()))
                    .flatMap(user -> {

                        if (user.getUsername().isBlank()) {
                            return Mono.just(new LoginResponse(false, null, null, "User not found"));
                        }

                        if (HashUtil.encoder().matches(loginForm.password, user.getPassword())) {
                            return sessionServices.generateSession(user.getId())
                                    .flatMap(session -> Mono.just(new LoginResponse(true, session.getToken(), UserUtil.createCleanUser(user), "Login successful!")));
                        } else {
                            return Mono.just(new LoginResponse(false, null, null, "Invalid password"));
                        }
                    });
        });
    }

    public record LoginResponse(boolean success, String data, User user, String message) {
    }

    public record LoginForm(String username, String password, String captchaKey) {
    }

    //endregion

    //region Register

    @CrossOrigin
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<LoginResponse> register(@RequestBody RegisterForm registerForm) {

        //// Optional<Object> optionalCaptcha = serverRequest.attribute("h-captcha-response");
        //// String usedCaptchaSolve = loginForm.captchaKey == null ? (String) optionalCaptcha.orElse(null) : loginForm.captchaKey;
        String usedCaptchaSolve = registerForm.captchaKey;

        if (registerForm.username == null || registerForm.password == null || registerForm.email == null || registerForm.invite == null || usedCaptchaSolve == null) {
            return Mono.just(new LoginResponse(false, null, null, "Invalid post data"));
        }

        return captchaUtil.validCaptcha(usedCaptchaSolve).flatMap(value -> {
            if (!value) return Mono.just(new LoginResponse(false, null, null, "Invalid captcha"));

            return inviteRepository.findByCode(registerForm.invite)
                    .defaultIfEmpty(new Invite("", 0, false, LocalDateTime.now())).flatMap(invite -> {

                        if (invite.getCode().isBlank())
                            return Mono.just(new LoginResponse(false, null, null, "Invalid invite code"));

                        return inviteRepository.delete(invite).thenReturn("Deleted invite").flatMap(s -> {
                            User newUser = new User(registerForm.username, HashUtil.encoder().encode(registerForm.password),
                                    registerForm.email, invite.isAdmin(), LocalDateTime.now(), LocalDateTime.now());
                            return userRepository.save(newUser)
                                    .flatMap(user -> sessionServices.generateSession(user.getId())
                                            .flatMap(session -> Mono.just(new LoginResponse(true, session.getToken(), UserUtil.createCleanUser(user), "Register successful!"))));
                        });
                    });
        });
    }

    public record RegisterForm(String username, String password, String email, String invite, String captchaKey) {
    }

    //endregion
}
