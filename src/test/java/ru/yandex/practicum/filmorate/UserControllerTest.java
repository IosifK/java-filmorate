package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1);
        user.setName("Test User");
        user.setLogin("testLogin");
        user.setEmail("test@mail.com");
        user.setBirthday(LocalDate.of(1990, 1, 1));
    }

    @Test
    public void testGetAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(Collections.singletonList(user));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Test User"));
    }

    @Test
    public void testGetUserById() throws Exception {
        when(userService.getUserById(1)).thenReturn(user);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test User"));
    }

    @Test
    public void testCreateUser() throws Exception {
        when(userService.createUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test User\", \"email\":\"test@mail.com\", \"login\":\"testLogin\", \"birthday\":\"1990-01-01\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.email").value("test@mail.com"))
                .andExpect(jsonPath("$.login").value("testLogin"));
    }

    @Test
    public void testUpdateUser() throws Exception {
        user.setName("Updated User");
        user.setEmail("updated@mail.com");
        user.setLogin("updatedLogin");

        when(userService.updateUser(any(User.class))).thenReturn(user);

        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1, \"name\":\"Updated User\", \"email\":\"updated@mail.com\", \"login\":\"updatedLogin\", \"birthday\":\"1990-01-01\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated User"))
                .andExpect(jsonPath("$.email").value("updated@mail.com"))
                .andExpect(jsonPath("$.login").value("updatedLogin"));
    }

    @Test
    public void testAddFriend() throws Exception {
        when(userService.getUserById(1)).thenReturn(user);

        mockMvc.perform(put("/users/1/friends/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test User"));
    }

    @Test
    public void testRemoveFriend() throws Exception {
        when(userService.getUserById(1)).thenReturn(user);

        mockMvc.perform(delete("/users/1/friends/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test User"));
    }

    @Test
    public void testGetFriends() throws Exception {
        User friend1 = new User();
        friend1.setId(2);
        friend1.setName("Friend 1");
        friend1.setLogin("friend1");
        friend1.setEmail("friend1@mail.com");
        friend1.setBirthday(LocalDate.of(1990, 1, 1));

        User friend2 = new User();
        friend2.setId(3);
        friend2.setName("Friend 2");
        friend2.setLogin("friend2");
        friend2.setEmail("friend2@mail.com");
        friend2.setBirthday(LocalDate.of(1991, 1, 1));

        List<User> friends = Arrays.asList(friend1, friend2);
        when(userService.getFriends(1)).thenReturn(friends);

        mockMvc.perform(get("/users/1/friends"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].name").value("Friend 1"))
                .andExpect(jsonPath("$[1].id").value(3))
                .andExpect(jsonPath("$[1].name").value("Friend 2"));
    }


    @Test
    public void testGetCommonFriends() throws Exception {
        User commonFriend = new User();
        commonFriend.setId(3);
        commonFriend.setName("Common Friend");
        commonFriend.setLogin("commonFriend");
        commonFriend.setEmail("common@mail.com");
        commonFriend.setBirthday(LocalDate.of(1990, 1, 1));

        List<User> commonFriends = Collections.singletonList(commonFriend);
        when(userService.getCommonFriends(1, 2)).thenReturn(commonFriends);

        mockMvc.perform(get("/users/1/friends/common/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(3))
                .andExpect(jsonPath("$[0].name").value("Common Friend"));
    }

}
