package com.example.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import at.favre.lib.crypto.bcrypt.BCrypt;

/**
 * UserController lida com as operações relacionadas a usuários(users).
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserRepository userRepository;

    /**
     * Rota do tipo "POST" para criar um novo usuário.
     * @param userModel Objeto do tipo 'UserModel' a ser criado como novo usuário;
     * @return Um 'ResponseEntity' com a resposta da operação, que pode conter o usuário criado ou uma mensagem de erro.
     */
    @PostMapping("/")
    public ResponseEntity<Object> create(@RequestBody UserModel userModel) {

        var passwordHashed = BCrypt.withDefaults()
                .hashToString(12, userModel.getPassword()
                .toCharArray());

        userModel.setPassword(passwordHashed);

        var user = this.userRepository.findByUsername(userModel.getUsername());

        if (user != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username já cadastrado");
        }
        var userCreated = this.userRepository.save(userModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }
}
