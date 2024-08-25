package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.List;


import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;


@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final List<Film> films = new ArrayList<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);

    @PostMapping
    public ResponseEntity<Film> addFilm(@Valid @RequestBody Film film) {
        film.setId(idCounter.getAndIncrement());
        films.add(film);
        log.info("Добавлен фильм: {}", film);
        return new ResponseEntity<>(film, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film film) {
        Optional<Film> existingFilm = films.stream()
                .filter(f -> f.getId() == film.getId())
                .findFirst();

        if (existingFilm.isPresent()) {
            Film updatedFilm = existingFilm.get();
            updatedFilm.setName(film.getName());
            updatedFilm.setDescription(film.getDescription());
            updatedFilm.setReleaseDate(film.getReleaseDate());
            updatedFilm.setDuration(film.getDuration());

            log.info("Обновлен фильм: {}", updatedFilm);
            return new ResponseEntity<>(updatedFilm, HttpStatus.OK);
        } else {
            String errorMessage = String.format("Фильм с id %d не найден", film.getId());
            log.warn(errorMessage);
            return new ResponseEntity<>(film, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return films;
    }
}



