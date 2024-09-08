package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserValidationTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testUserWithInvalidEmail() {
        User user = new User();
        user.setEmail("email");
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        var violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

}
