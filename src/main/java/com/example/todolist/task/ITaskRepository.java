package com.example.todolist.task;

import java.util.UUID;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITaskRepository extends JpaRepository<TaskModel, UUID> {
    /* 
    ... extends JpaRepository<T, id>
    onde:
        T = classe que esse repositório está representando;
        id = tipo de id.
    */ 
    List<TaskModel> findTasksByIdUser(UUID idUser);
}
