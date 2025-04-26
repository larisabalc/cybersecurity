package com.unibuc.cybersecurity.repository;

import com.unibuc.cybersecurity.entity.Task;
import com.unibuc.cybersecurity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}
