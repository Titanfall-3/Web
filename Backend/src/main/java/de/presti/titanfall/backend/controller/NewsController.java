package de.presti.titanfall.backend.controller;

import de.presti.titanfall.backend.entities.News;
import de.presti.titanfall.backend.repository.NewsRepository;
import de.presti.titanfall.backend.repository.UserRepository;
import de.presti.titanfall.backend.services.SessionServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    private final UserRepository userRepository;
    private final NewsRepository newsRepository;
    private final SessionServices sessionServices;

    @Autowired
    public NewsController(UserRepository userRepository, NewsRepository newsRepository, SessionServices sessionServices) {
        this.userRepository = userRepository;
        this.newsRepository = newsRepository;
        this.sessionServices = sessionServices;
    }

    @CrossOrigin
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<NewsCreateResponse> create(@RequestBody NewsCreateForm newsCreateForm) {
        return sessionServices.checkAndRefreshSession(newsCreateForm.token)
                .flatMap(session -> {
                    if (session == null) return Mono.error(new Exception("Invalid Session."));

                    return userRepository.findById(session.getUserId());
                }).flatMap(user -> {
                    if (user == null) return Mono.error(new Exception("Invalid Session"));

                    if (!user.isAdmin()) {
                        return Mono.error(new Exception("No Admin"));
                    } else {
                        News news = new News(newsCreateForm.title, newsCreateForm.content, newsCreateForm.thumbnail);
                        return newsRepository.save(news);
                    }
                }).map(invite -> {
                    return new NewsCreateResponse(true, "Created");
                })
                .onErrorResume(Exception.class, e -> Mono.just(new NewsCreateResponse(false, e.getMessage())))
                .onErrorReturn(new NewsCreateResponse(false, "Server Error!"));
    }

    public record NewsCreateForm(String token, String title, String thumbnail, String content) {}

    public record NewsCreateResponse(boolean success, String message) {}

    @CrossOrigin
    @PostMapping(value ="/get", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<NewsResponse> get(@RequestBody NewsRetrieveForm newsRetrieveForm) {
        return sessionServices.checkAndRefreshSession(newsRetrieveForm.token)
                .flatMap(session -> {
                    if (session == null) return Mono.error(new Exception("Invalid Session."));

                    return userRepository.findById(session.getUserId());
                }).flatMap(user -> {
                    if (user == null) return Mono.error(new Exception("Invalid Session"));

                    return newsRepository.findAll().limitRate(3).collectList().map(news -> {
                        return new NewsResponse(true, news, "Worked!");
                    });
                }).onErrorResume(Exception.class, e -> Mono.just(new NewsResponse(false, null, e.getMessage())))
                .onErrorReturn(new NewsResponse(false, null, "Server Error!"));
    }

    public record NewsRetrieveForm(String token) {}

    public record NewsResponse(boolean success, List<News> data, String message) {}

}
