package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        if (!filmExists(film.getId())) {
            throw new NoSuchElementException("Фильм с id " + film.getId() + " не найден");
        }
        return filmStorage.updateFilm(film);
    }

    public Film getFilmById(int id) {
        if (!filmExists(id)) {
            throw new NoSuchElementException("Фильм с id " + id + " не найден");
        }
        return filmStorage.getFilmById(id);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }


    public void addLike(int filmId, int userId) {
        if (userExists(userId)) {
            throw new NoSuchElementException("Пользователь с id " + userId + " не найден");
        }
        Film film = getFilmById(filmId);
        if (film.getLikes().contains(userId)) {
            throw new IllegalArgumentException("Пользователь с id " + userId + " уже поставил лайк этому фильму.");
        }
        filmStorage.addLike(filmId, userId);
    }

    public void removeLike(int filmId, int userId) {
        if (userExists(userId)) {
            throw new NoSuchElementException("Пользователь с id " + userId + " не найден");
        }
        Film film = getFilmById(filmId);
        if (!film.getLikes().contains(userId)) {
            throw new NoSuchElementException("Лайк от пользователя с id " + userId + " не найден для фильма с id " + filmId);
        }
        filmStorage.removeLike(filmId, userId);
    }

    public List<Film> getMostPopularFilms(int count) {
        return filmStorage.getMostPopularFilms(count);
    }

    private boolean filmExists(int filmId) {
        return filmStorage.getFilmById(filmId) != null;
    }

    private boolean userExists(int userId) {
        User user = userStorage.getUserById(userId);
        if (user == null) {
            return true;
        } else {
            return false;
        }
    }

}



