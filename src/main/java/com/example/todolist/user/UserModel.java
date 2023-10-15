package com.example.todolist.user;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

// @Data é usada para gerar automaticamente uma série de métodos comuns em uma classe, como métodos getters, setters, equals, hashCode e toString, sem a necessidade de escrevê-los manualmente.
@Data

@Entity(name="table_users")
public class UserModel {
    // @Id indica que 'id' é a chave primária do banco de dados;
    // @GeneratedValue() irá gerar automáticamente os id's;
    // UUID, que significa "Universally Unique Identifier," é um identificador único universal. É uma sequência de caracteres alfanuméricos que é usada para identificar informações de forma exclusiva.
    // @CreationTimestamp: o Lombork (ORM) automaticamente definirá o valor do campo 'createdAt' para a data e hora exatas em que o objeto foi inserido no banco de dados.

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    private String username;
    private String name;
    private String password;
    @CreationTimestamp
    private LocalDateTime createdAt;
}
