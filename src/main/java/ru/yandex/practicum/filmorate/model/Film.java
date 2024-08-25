package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

/**
 * Film.
 */
@Data
public class Film {
    private int id;

    @NotBlank(message = "название не может быть пустым")
    private String name;

    @Size(max = 200, message = "максимальная длина описания — 200 символов")
    private String description;

    @NotNull(message = "дата релиза не может быть пустой.")
    @PastOrPresent(message = "дата релиза не может быть в будущем.")
    @ReleaseDateConstraint
    private LocalDate releaseDate;

    @Positive(message = "продолжительность фильма должна быть положительным числом.")
    private int duration;


}
