package com.example.todo.service;

import com.example.todo.model.User;
import com.example.todo.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
import java.util.Optional;

/**
 * UserService contains business logic for registration and login.
 * No SQL here. Delegates persistence to UserRepository.
 */
public class UserService {
    private final UserRepository userRepository;

    public UserService() {
        this.userRepository = new UserRepository();
    }

    public User register(String username, String plainPassword) throws SQLException {
        validateUsername(username);
        validatePassword(plainPassword);
        Optional<User> existing = userRepository.findByUsername(username);
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Username already taken");
        }
        String hash = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
        return userRepository.createUser(username, hash);
    }

    public User login(String username, String plainPassword) throws SQLException {
        validateUsername(username);
        validatePassword(plainPassword);
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            return null;
        }
        User user = userOpt.get();
        return BCrypt.checkpw(plainPassword, user.getPasswordHash()) ? user : null;
    }

    private void validateUsername(String username) {
        if (username == null || username.trim().length() < 3) {
            throw new IllegalArgumentException("Username must be at least 3 characters");
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }
    }
}
