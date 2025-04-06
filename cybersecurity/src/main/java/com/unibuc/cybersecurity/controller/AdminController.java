package com.unibuc.cybersecurity.controller;

import com.unibuc.cybersecurity.entity.Role;
import com.unibuc.cybersecurity.entity.Task;
import com.unibuc.cybersecurity.entity.User;
import com.unibuc.cybersecurity.repository.TaskRepository;
import com.unibuc.cybersecurity.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class AdminController {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping({"/list", "/", "/list-tasks"})
    public ModelAndView showUsersAndTasks(HttpSession session) {
        ModelAndView mav = new ModelAndView("list-users-task");
        List<User> users = userRepository.findAll();
        List<Task> tasks = taskRepository.findAll();
        mav.addObject("users", users);
        mav.addObject("tasks", tasks);
        return mav;
    }

    @GetMapping("/addUserForm")
    public ModelAndView addUserForm(HttpSession session) {
        ModelAndView mav = new ModelAndView("add-user-form");
        User user = new User();
        mav.addObject("user", user);
        return mav;
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute User user, HttpSession session) {
        userRepository.save(user);
        return "redirect:/list";
    }

    @GetMapping("/showUpdateForm")
    public ModelAndView showUpdateForm(@RequestParam Long userID, HttpSession session) {
        ModelAndView mav = new ModelAndView("add-user-form");
        User user = userRepository.findById(userID).orElse(null);
        mav.addObject("user", user);
        return mav;
    }

    @GetMapping("/deleteUser")
    public String deleteUser(@RequestParam Long userID, HttpSession session) {
        userRepository.deleteById(userID);
        return "redirect:/list";
    }

    @GetMapping("/add-task-form")
    public ModelAndView addTaskForm() {
        ModelAndView mav = new ModelAndView("add-task-form");
        Task task = new Task();
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        mav.addObject("task", task);
        mav.addObject("users", userRepository.findAll());
        return mav;
    }

    @PostMapping("/saveTask")
    public String saveTask(@ModelAttribute Task task) {
        System.out.println(task.getName());
        taskRepository.save(task);
        return "redirect:/list";
    }

    @GetMapping("/showUpdateTaskForm")
    public ModelAndView showUpdateTaskForm(@RequestParam Long taskID) {
        ModelAndView mav = new ModelAndView("add-task-form");
        Task task = taskRepository.findById(taskID).orElse(null);
        mav.addObject("task", task);
        mav.addObject("users", userRepository.findAll());
        return mav;
    }

    @GetMapping("/deleteTask")
    public String deleteTask(@RequestParam Long taskID) {
        taskRepository.deleteById(taskID);
        return "redirect:/list";
    }
}
