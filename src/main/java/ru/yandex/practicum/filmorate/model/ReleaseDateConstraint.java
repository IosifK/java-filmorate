package ru.yandex.practicum.filmorate.model;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Constraint(validatedBy = ReleaseDateValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ReleaseDateConstraint {
    String message() default "дата релиза не может быть ранее 28 декабря 1895 года.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
