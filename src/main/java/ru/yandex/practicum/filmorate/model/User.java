package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.time.LocalDate;


@Data
public class User {
    private int id;


    @Email(message = "некорректный формат email.")
    @NotBlank(message = "email не может быть пустым.")
    private String email;

    @NotBlank(message = "логин не может быть пустым.")
    @NotNull
    private String login;


    private String name;

    @Past(message = "дата рождения не может быть в будущем.")
    private LocalDate birthday;

}
