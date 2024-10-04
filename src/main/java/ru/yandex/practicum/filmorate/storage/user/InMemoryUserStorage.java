package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);

    @Override
    public User createUser(User user) {
        user.setId(idCounter.getAndIncrement());
        user.setFriends(new HashSet<>());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUserById(int id) {
        return users.get(id);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void addFriend(int userId, int friendId) {
        User user = users.get(userId);
        User friend = users.get(friendId);

        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    @Override
    public void removeFriend(int userId, int friendId) {
        User user = users.get(userId);
        User friend = users.get(friendId);

        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
    }

    @Override
    public List<User> getFriends(int userId) {
        User user = users.get(userId);
        Set<Integer> friendsIds = user.getFriends();
        List<User> friends = new ArrayList<>();
        for (Integer id : friendsIds) {
            friends.add(users.get(id));
        }
        return friends;
    }
}

