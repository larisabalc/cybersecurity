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
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PersistenceContext
    private EntityManager entityManager;

    public Optional<User> authenticate(String email, String password) {
        // Build the query by directly concatenating user input (dangerous practice)
        String query = "SELECT * FROM users WHERE email = '" + email + "' AND password = '" + password + "'";

        // Log the query to see the final SQL being executed
        System.out.println("Executing Query: " + query);

        try {
            // Use getResultList() instead of getSingleResult() to handle the possibility of multiple results
            List<User> users = entityManager.createNativeQuery(query, User.class).getResultList();

            // If the list is not empty, return the first user as the authenticated user (vulnerable approach)
            if (!users.isEmpty()) {
                return Optional.of(users.get(0)); // Return the first result, assuming it's valid
            }

            return Optional.empty(); // No users found
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
