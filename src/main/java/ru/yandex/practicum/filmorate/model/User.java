package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User {
    private Set<Integer> friends = new HashSet<>();
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
