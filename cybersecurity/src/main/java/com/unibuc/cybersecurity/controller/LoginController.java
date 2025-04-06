package com.unibuc.cybersecurity.controller;

import com.unibuc.cybersecurity.entity.Role;
import com.unibuc.cybersecurity.service.UserService;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
public class LoginController {
    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String email,
                               @RequestParam String password,
                               HttpSession session,
                               Model model) {
        System.out.println("Received Email: " + email);
        System.out.println("Received Password: " + password);

        return userService.authenticate(email, password)
                .map(user -> {
                    session.setAttribute("loggedInUser", user);

                    if (user.getRole().equals(Role.ADMIN)) {
                        return "redirect:/list";
                    } else {
                        return "redirect:/my-tasks";
                    }
                })
                .orElseGet(() -> {
                    model.addAttribute("error", "Invalid credentials");
                    return "login";
                });
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
