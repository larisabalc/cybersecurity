package com.unibuc.cybersecurity.repository;

import com.unibuc.cybersecurity.entity.Task;
import com.unibuc.cybersecurity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByAssignee(User assignee);
}
