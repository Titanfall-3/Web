package de.presti.titanfall.backend.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class CaptchaUtil {

    final CaptchaSettings captchaSettings;

    @Autowired
    public CaptchaUtil(CaptchaSettings captchaSettings) {
        this.captchaSettings = captchaSettings;
    }

    public HttpClient httpClient = HttpClient.newHttpClient();

    public Mono<Boolean> validCaptcha(String gRecaptchaToken) {
        if (gRecaptchaToken == null) return Mono.just(false);

        if (true)return Mono.just(true);

        String secretKey = captchaSettings.getSecret(); //use your secret key
        float expectedScore = 0.9f; //set the expectedScore as per your requirement

        return Mono.just(gRecaptchaToken).flatMap(token -> {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://hcaptcha.com/siteverify"))
                    .POST(HttpRequest.BodyPublishers.ofString("secret=" + secretKey + "&response=" + gRecaptchaToken))
                    .header("User-Agent", "Titanfall-Backend/1.0")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .build();;

            return Mono.just(httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(HttpResponse::body));
        }).flatMap(body -> {
            JsonObject jsonObject = null;
            try {
                jsonObject = JsonParser.parseString(body.get()).getAsJsonObject();
            } catch (Exception e) {
                Mono.just(false);
            }

            float scoreFromResponse = jsonObject.has("score") ? Float.parseFloat(jsonObject.get("score").toString()) : 1.0F;
            return Mono.just(jsonObject.get("success").getAsBoolean() && scoreFromResponse >= expectedScore);
        });
    }

}