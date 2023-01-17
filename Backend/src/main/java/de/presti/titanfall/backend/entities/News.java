package de.presti.titanfall.backend.entities;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Data
@RequiredArgsConstructor
public class News {

    @Id
    long id;

    @Setter(AccessLevel.PUBLIC)
    String title;

    @Setter(AccessLevel.PUBLIC)
    String content;

    @Setter(AccessLevel.PUBLIC)
    String thumbnail;

    public News(String title, String content, String thumbnail) {
        this.title = title;
        this.content = content;
        this.thumbnail = thumbnail;
    }
}
