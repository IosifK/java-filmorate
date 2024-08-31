package ru.yandex.practicum.filmorate.controller;


import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        log.info("пришел Post запрос /users с телом: {}", user);
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        user.setId(idCounter.getAndIncrement());
        users.put(user.getId(), user);
        log.info("отправлен ответ Post /users с телом: {}", user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }


    @PutMapping
    public ResponseEntity<User> updateUser(@Valid @RequestBody User user) {
        log.info("пришел Put запрос /users с телом: {}", user);
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("отправлен ответ Put /users с телом: {}", user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            String errorMessage = String.format("пользователь с id %d не найден", user.getId());
            log.warn(errorMessage);
            return new ResponseEntity<>(user, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>(users.values());
        log.info("Запрос Get /users, ответ: {}", userList);
        return userList;
    }
}
