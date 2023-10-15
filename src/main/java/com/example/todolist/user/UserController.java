package com.example.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

@RestController
@RequestMapping("/users")
public class UserController {
    // @Autowired: o objetivo principal é injetar dependências em um componente. As dependências podem ser outras classes, beans, serviços, repositórios, etc. Isso elimina a necessidade de criar manualmente instâncias das dependências e permite que o Spring cuide da gestão e criação dessas instâncias.

    // @RequestBody: indica que um método de controlador deve receber os dados da solicitação HTTP no corpo (body) da solicitação. 

    @Autowired
    private IUserRepository userRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody UserModel userModel) {

        var passwordHashred = BCrypt.withDefaults()
                .hashToString(12, userModel.getPassword()
                .toCharArray());

        userModel.setPassword(passwordHashred);

        // Verifica se o 'username' já existe no banco de dados. Se não existir retorna 'null'
        var user = this.userRepository.findByUsername(userModel.getUsername());

        if (user != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username já existe");
        }
        var userCreated = this.userRepository.save(userModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }
}
