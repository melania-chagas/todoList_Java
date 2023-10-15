package com.example.todolist.user;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

// A principal finalidade do JpaRepository é simplificar o desenvolvimento de camadas de acesso a dados em aplicativos Spring, especialmente em aplicativos Spring Boot. Ele faz isso fornecendo uma série de métodos predefinidos para realizar operações comuns em um banco de dados, como criar, ler, atualizar e excluir (CRUD). Esses métodos incluem coisas como save, findById, delete, findAll, e muitos outros.

/* 
    JpaRepository<T, id>
    onde:
        T = classe que esse repositório está representando;
        id = tipo de id.
*/ 


public interface IUserRepository extends JpaRepository<UserModel, UUID > {
    /* 
    "UserModel findByUsername(String username);"
    onde:
        "UserModel" é o tipo
    */ 
    UserModel findByUsername(String username);
    
}
