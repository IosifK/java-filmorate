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
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        log.info("пришёл POST запрос /users с телом: {}", user);
        User createdUser = userService.createUser(user);
        log.info("отправлен ответ POST /users с телом: {}", createdUser);
        return ResponseEntity.ok(createdUser);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@Valid @RequestBody User user) {
        log.info("пришёл PUT запрос /users с телом: {}", user);
        User updatedUser = userService.updateUser(user);
        log.info("отправлен ответ PUT /users с телом: {}", updatedUser);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        log.info("Запрос GET /users, ответ: {}", users);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        log.info("Запрос GET /users/{}", id);
        User user = userService.getUserById(id);
        log.info("отправлен ответ GET /users/{} с телом: {}", id, user);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<User> addFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Пользователь {} добавляет в друзья пользователя {}", id, friendId);
        userService.addFriend(id, friendId);
        User updatedUser = userService.getUserById(id);
        log.info("отправлен ответ PUT /users/{}/friends/{} с телом: {}", id, friendId, updatedUser);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<User> removeFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Пользователь {} удаляет из друзей пользователя {}", id, friendId);
        userService.removeFriend(id, friendId);
        User updatedUser = userService.getUserById(id);
        log.info("отправлен ответ DELETE /users/{}/friends/{} с телом: {}", id, friendId, updatedUser);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<List<User>> getFriends(@PathVariable int id) {
        log.info("Запрос GET /users/{}/friends", id);
        List<User> friends = userService.getFriends(id);
        log.info("отправлен ответ GET /users/{}/friends с телом: {}", id, friends);
        return ResponseEntity.ok(friends);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public ResponseEntity<List<User>> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        log.info("Запрос GET /users/{}/friends/common/{}", id, otherId);
        List<User> commonFriends = userService.getCommonFriends(id, otherId);
        log.info("отправлен ответ GET /users/{}/friends/common/{} с телом: {}", id, otherId, commonFriends);
        return ResponseEntity.ok(commonFriends);
    }
}




