package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public ResponseEntity<Film> addFilm(@Valid @RequestBody Film film) {
        log.info("пришёл POST запрос /films с телом: {}", film);
        Film createdFilm = filmService.addFilm(film);
        log.info("отправлен ответ POST /films с телом: {}", createdFilm);
        return ResponseEntity.ok(createdFilm);
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film film) {
        log.info("пришёл PUT запрос /films с телом: {}", film);
        Film updatedFilm = filmService.updateFilm(film);
        log.info("отправлен ответ PUT /films с телом: {}", updatedFilm);
        return ResponseEntity.ok(updatedFilm);
    }

    @GetMapping
    public ResponseEntity<List<Film>> getAllFilms() {
        List<Film> films = filmService.getAllFilms();
        log.info("Запрос GET /films, ответ: {}", films);
        return ResponseEntity.ok(films);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Film> getFilmById(@PathVariable int id) {
        log.info("Запрос GET /films/{}", id);
        Film film = filmService.getFilmById(id);
        log.info("отправлен ответ GET /films/{} с телом: {}", id, film);
        return ResponseEntity.ok(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<Film> addLike(@PathVariable int id, @PathVariable int userId) {
        log.info("Пользователь {} ставит лайк фильму {}", userId, id);
        filmService.addLike(id, userId);
        Film updatedFilm = filmService.getFilmById(id);
        log.info("отправлен ответ PUT /films/{}/like/{} с телом: {}", id, userId, updatedFilm);
        return ResponseEntity.ok(updatedFilm);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<Film> removeLike(@PathVariable int id, @PathVariable int userId) {
        log.info("Пользователь {} удаляет лайк с фильма {}", userId, id);
        filmService.removeLike(id, userId);
        Film updatedFilm = filmService.getFilmById(id);
        log.info("отправлен ответ DELETE /films/{}/like/{} с телом: {}", id, userId, updatedFilm);
        return ResponseEntity.ok(updatedFilm);
    }

    @GetMapping("/popular")
    public ResponseEntity<List<Film>> getMostPopularFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("Запрос GET /films/popular с параметром count={}", count);
        List<Film> popularFilms = filmService.getMostPopularFilms(count);
        log.info("отправлен ответ GET /films/popular с телом: {}", popularFilms);
        return ResponseEntity.ok(popularFilms);
    }
}





