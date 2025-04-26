package com.unibuc.cybersecurity.service;

import com.unibuc.cybersecurity.entity.Task;
import com.unibuc.cybersecurity.repository.TaskRepository;
import com.unibuc.cybersecurity.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    public TaskService(TaskRepository taskRepository) {
    }

    @PersistenceContext
    private EntityManager entityManager;

    public List<Task> findTasksByAssignee(Long assigneeUserId) {
        String sql = "SELECT * FROM task WHERE assignee_user_id = " + assigneeUserId;
        return entityManager.createNativeQuery(sql, Task.class)
                .getResultList();
    }
}
