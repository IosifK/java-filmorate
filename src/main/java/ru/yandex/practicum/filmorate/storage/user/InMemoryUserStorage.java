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
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        user.setId(idCounter.getAndIncrement());
        user.setFriends(new HashSet<>());
        users.put(user.getId(), user);
        return user;
    }


    @Override
    public User updateUser(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            return user;
        } else {
            throw new NoSuchElementException("Пользователь с id " + user.getId() + " не найден");
        }
    }


    @Override
    public User getUserById(int id) {
        if (!users.containsKey(id)) {
            throw new NoSuchElementException("Пользователь с id " + id + " не найден");
        }
        return users.get(id);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }


    @Override
    public void addFriend(int userId, int friendId) {
        if (!users.containsKey(userId)) {
            throw new NoSuchElementException("Пользователь с id " + userId + " не найден");
        }
        if (!users.containsKey(friendId)) {
            throw new NoSuchElementException("Пользователь с id " + friendId + " не найден");
        }

        User user = getUserById(userId);
        User friend = getUserById(friendId);

        if (user.getFriends().contains(friendId)) {
            throw new IllegalArgumentException("Пользователь с id " + friendId + " уже добавлен в друзья");
        }

        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    @Override
    public void removeFriend(int userId, int friendId) {
        if (!users.containsKey(userId)) {
            throw new NoSuchElementException("Пользователь с id " + userId + " не найден");
        }
        if (!users.containsKey(friendId)) {
            throw new NoSuchElementException("Пользователь с id " + friendId + " не найден");
        }

        User user = getUserById(userId);
        User friend = getUserById(friendId);

        if (!user.getFriends().contains(friendId)) {
            throw new NoSuchElementException("Пользователь с id " + friendId + " не найден в списке друзей пользователя с id " + userId);
        }

        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
    }


    @Override
    public List<User> getFriends(int userId) {
        User user = getUserById(userId);
        Set<Integer> friendsIds = user.getFriends();
        List<User> friends = new ArrayList<>();
        for (Integer id : friendsIds) {
            friends.add(getUserById(id));
        }
        return friends;
    }

    @Override
    public List<User> getCommonFriends(int userId, int otherUserId) {
        User user = getUserById(userId);
        User otherUser = getUserById(otherUserId);
        Set<Integer> commonFriendsIds = new HashSet<>(user.getFriends());
        commonFriendsIds.retainAll(otherUser.getFriends());
        List<User> commonFriends = new ArrayList<>();
        for (Integer id : commonFriendsIds) {
            commonFriends.add(getUserById(id));
        }
        return commonFriends;
    }
}
