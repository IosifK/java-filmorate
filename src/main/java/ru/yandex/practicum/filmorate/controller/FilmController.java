package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;


import java.util.concurrent.atomic.AtomicInteger;


@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);

    @PostMapping
    public ResponseEntity<Film> addFilm(@Valid @RequestBody Film film) {
        log.info("пришел Post запрос /films с телом: {}", film);
        film.setId(idCounter.getAndIncrement());
        films.put(film.getId(), film);
        log.info("отправлен ответ Post /films с телом: {}", film);
        return new ResponseEntity<>(film, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film film) {
        log.info("пришел Put запрос /films с телом: {}", film);

        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("отправлен ответ Put /films с телом: {}", film);
            return new ResponseEntity<>(film, HttpStatus.OK);
        } else {
            String errorMessage = String.format("Фильм с id %d не найден", film.getId());
            log.warn(errorMessage);
            return new ResponseEntity<>(film, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public List<Film> getAllFilms() {
        List<Film> filmList = new ArrayList<>(films.values());
        log.info("Запрос Get /films, ответ: {}", filmList);
        return filmList;
    }
}




