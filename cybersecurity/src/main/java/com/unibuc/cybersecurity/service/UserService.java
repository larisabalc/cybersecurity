package com.unibuc.cybersecurity.service;

import com.unibuc.cybersecurity.entity.User;
import com.unibuc.cybersecurity.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    public UserService(UserRepository userRepository) {
    }

    @PersistenceContext
    private EntityManager entityManager;

    public Optional<User> authenticate(String email, String password) {
        String query = "SELECT * FROM users WHERE email = '" + email + "' AND password = '" + password + "'";

        try {
            List<User> users = entityManager.createNativeQuery(query, User.class).getResultList();

            if (!users.isEmpty()) {
                return Optional.of(users.get(0));
            }

            return Optional.empty();
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
