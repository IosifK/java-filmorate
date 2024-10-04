package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);

    @Override
    public Film addFilm(Film film) {
        film.setId(idCounter.getAndIncrement());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film getFilmById(int id) {
        return films.get(id);
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public void addLike(int filmId, int userId) {
        Film film = getFilmById(filmId);
        film.getLikes().add(userId);
    }

    @Override
    public void removeLike(int filmId, int userId) {
        Film film = getFilmById(filmId);
        film.getLikes().remove(userId);
    }

    @Override
    public List<Film> getMostPopularFilms(int count) {
        return films.values().stream()
                .sorted((f1, f2) -> Integer.compare(f2.getLikes().size(), f1.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }
}
