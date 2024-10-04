package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User createUser(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        if (!userExists(user.getId())) {
            throw new NoSuchElementException("Пользователь с id " + user.getId() + " не найден");
        }
        return userStorage.updateUser(user);
    }

    public User getUserById(int id) {
        if (!userExists(id)) {
            throw new NoSuchElementException("Пользователь с id " + id + " не найден");
        }
        return userStorage.getUserById(id);
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public void addFriend(int userId, int friendId) {
        validateUserExists(userId);
        validateUserExists(friendId);

        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);

        if (user.getFriends().contains(friendId)) {
            throw new IllegalArgumentException("Пользователь с id " + friendId + " уже добавлен в друзья");
        }

        userStorage.addFriend(userId, friendId);
    }

    public void removeFriend(int userId, int friendId) {
        validateUserExists(userId);
        validateUserExists(friendId);

        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);

        if (!user.getFriends().contains(friendId)) {
            return;
        }

        userStorage.removeFriend(userId, friendId);
    }

    public List<User> getFriends(int userId) {
        validateUserExists(userId);
        return userStorage.getFriends(userId);
    }

    public List<User> getCommonFriends(int userId, int otherUserId) {
        validateUserExists(userId);
        validateUserExists(otherUserId);

        User user = userStorage.getUserById(userId);
        User otherUser = userStorage.getUserById(otherUserId);

        Set<Integer> commonFriendsIds = new HashSet<>(user.getFriends());
        commonFriendsIds.retainAll(otherUser.getFriends());

        List<User> commonFriends = new ArrayList<>();
        for (Integer id : commonFriendsIds) {
            commonFriends.add(userStorage.getUserById(id));
        }

        return commonFriends;
    }

    private boolean userExists(int id) {
        return userStorage.getAllUsers().stream().anyMatch(user -> user.getId() == id);
    }

    private void validateUserExists(int id) {
        if (!userExists(id)) {
            throw new NoSuchElementException("Пользователь с id " + id + " не найден");
        }
    }
}
