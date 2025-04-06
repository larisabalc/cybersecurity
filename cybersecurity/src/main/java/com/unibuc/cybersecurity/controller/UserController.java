package com.unibuc.cybersecurity.controller;

import com.unibuc.cybersecurity.entity.Task;
import com.unibuc.cybersecurity.entity.TaskStatus;
import com.unibuc.cybersecurity.entity.User;
import com.unibuc.cybersecurity.repository.TaskRepository;
import com.unibuc.cybersecurity.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    private static final String UPLOAD_DIR = System.getProperty("user.dir") + File.separator + "uploads";

    @GetMapping("/my-tasks")
    public ModelAndView showUserTasks(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        List<Task> tasks = taskRepository.findByAssignee(loggedInUser);
        ModelAndView mav = new ModelAndView("user-tasks");
        mav.addObject("tasks", tasks);
        return mav;
    }

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam Long taskId, @RequestParam MultipartFile file) throws IOException {
        Task task = taskRepository.findById(taskId).orElse(null);

        if (task != null && file != null && !file.isEmpty()) {
            String fileName = file.getOriginalFilename();

            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            File destination = new File(UPLOAD_DIR + fileName);
            file.transferTo(destination);

            task.setFileName(fileName);
            taskRepository.save(task);
        }

        return "redirect:/my-tasks";
    }

    @PostMapping("/changeTaskStatus")
    public String changeTaskStatus(@RequestParam Long taskId) {
        Task task = taskRepository.findById(taskId).orElse(null);
        task.setUpdatedAt(LocalDateTime.now());
        if (task != null) {
            if (task.getStatus() == TaskStatus.PENDING) {
                task.setStatus(TaskStatus.IN_PROGRESS);
            } else if (task.getStatus() == TaskStatus.IN_PROGRESS) {
                task.setStatus(TaskStatus.COMPLETED);
            } else {
                task.setStatus(TaskStatus.PENDING);
            }
            taskRepository.save(task);
        }
        return "redirect:/my-tasks";
    }

    @GetMapping("/assignTaskForm")
    public ModelAndView showAssignTaskForm(@RequestParam Long userID) {
        ModelAndView mav = new ModelAndView("assign-task-form");
        User user = userRepository.findById(userID).orElse(null);
        List<Task> tasks = taskRepository.findAll();
        mav.addObject("user", user);
        mav.addObject("tasks", tasks);
        return mav;
    }

    @PostMapping("/assignTask")
    public String assignTaskToUser(@RequestParam Long userId, @RequestParam Long taskId) {
        User user = userRepository.findById(userId).orElse(null);
        Task task = taskRepository.findById(taskId).orElse(null);
        if (user != null && task != null) {
            task.setAssignee(user);
            taskRepository.save(task);
        }
        return "redirect:/list";
    }
}
