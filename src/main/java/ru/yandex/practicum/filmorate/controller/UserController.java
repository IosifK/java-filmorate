package ru.yandex.practicum.filmorate.controller;


import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.*;


@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info("пришёл POST запрос /users с телом: {}", user);
        User createdUser = userService.createUser(user);
        log.info("отправлен ответ POST /users с телом: {}", createdUser);
        return createdUser;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("пришёл PUT запрос /users с телом: {}", user);
        User updatedUser = userService.updateUser(user);
        log.info("отправлен ответ PUT /users с телом: {}", updatedUser);
        return updatedUser;
    }

    @GetMapping
    public List<User> getAllUsers() {
        List<User> users = userService.getAllUsers();
        log.info("Запрос GET /users, ответ: {}", users);
        return users;
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        log.info("Запрос GET /users/{}", id);
        return userService.getUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<User> addFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Пользователь {} добавляет в друзья пользователя {}", id, friendId);
        userService.addFriend(id, friendId);
        User updatedUser = userService.getUserById(id);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<User> removeFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Пользователь {} удаляет из друзей пользователя {}", id, friendId);
        userService.removeFriend(id, friendId);
        User updatedUser = userService.getUserById(id);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable int id) {
        log.info("Запрос GET /users/{}/friends", id);
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        log.info("Запрос GET /users/{}/friends/common/{}", id, otherId);
        return userService.getCommonFriends(id, otherId);
    }
}




