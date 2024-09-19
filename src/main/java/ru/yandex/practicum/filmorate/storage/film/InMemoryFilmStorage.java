package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);

    private final UserStorage userStorage;

    @Autowired
    public InMemoryFilmStorage(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public Film addFilm(Film film) {
        film.setId(idCounter.getAndIncrement());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            return film;
        } else {
            throw new NoSuchElementException("Фильм с id " + film.getId() + " не найден");
        }
    }

    @Override
    public Film getFilmById(int id) {
        if (films.containsKey(id)) {
            return films.get(id);
        } else {
            throw new NoSuchElementException("Фильм с id " + id + " не найден");
        }
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }



    @Override
    public void addLike(int filmId, int userId) {
        if (!userExist(userId)) {
            throw new NoSuchElementException("Пользователь с id " + userId + " не найден");
        }

        Film film = getFilmById(filmId);
        if (film.getLikes().contains(userId)) {
            throw new IllegalArgumentException("Пользовaтель с id " + userId + " уже поставил лайк этому фильму.");
        }
        film.getLikes().add(userId);
    }


    @Override
    public void removeLike(int filmId, int userId) {
        if (!userExist(userId)) {
            throw new NoSuchElementException("Пользователь с id " + userId + " не найден");
        }

        Film film = getFilmById(filmId);
        if (!film.getLikes().contains(userId)) {
            throw new NoSuchElementException("Лайк от пользователя с id" + userId + " не найден для фильма с id " + filmId);
        }
        film.getLikes().remove(userId);
    }


    private boolean userExist(int userId) {
        try {
            userStorage.getUserById(userId);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }


    @Override
    public List<Film> getMostPopularFilms(int count) {
        return films.values().stream()
                .sorted((f1, f2) -> Integer.compare(f2.getLikes().size(), f1.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }


}
