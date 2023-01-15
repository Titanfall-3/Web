package de.presti.titanfall.backend.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

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

    public boolean validCaptcha(String gRecaptchaToken) {
        if (gRecaptchaToken == null) return false;
        String secretKey = captchaSettings.getSecret(); //use your secret key
        float expectedScore = 0.9f, scoreFromResponse = 0; //set the expectedScore as per your requirement

        HttpRequest httpRequest;
        try {
            httpRequest = HttpRequest.newBuilder()
                    .uri(new URI("https://hcaptcha.com/siteverify"))
                    .POST(HttpRequest.BodyPublishers.ofString("secret=" + secretKey + "&response=" + gRecaptchaToken))
                    .header("User-Agent", "Titanfall-Backend/1.0")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .build();
        } catch (Exception ignore) {
            return false;
        }

        HttpResponse<String> httpResponse;
        try {
            httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (Exception ignore) {
            return false;
        }

        JsonObject jsonObject = JsonParser.parseString(httpResponse.body()).getAsJsonObject();

        scoreFromResponse = jsonObject.has("score") ? Float.parseFloat(jsonObject.get("score").toString()) : 1.0F;
        return jsonObject.get("success").getAsBoolean() && scoreFromResponse >= expectedScore;
    }

}