package de.presti.titanfall.backend.repository;

import de.presti.titanfall.backend.entities.News;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface NewsRepository extends ReactiveCrudRepository<News, Long> {
}
