package com.example.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.todolist.user.IUserRepository;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter{
    @Autowired
    private IUserRepository iUserRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

            var servletPath = request.getServletPath();
            if(servletPath.startsWith("/tasks/")) {
                System.out.println("chegou no filter");

                // Pegar a autenticação
                var authorization = request.getHeader("Authorization");
    
                /* 
                authorization vem nesse formato: "Basic bWVsYW5pYV9jaGFnYXM6MTIzNDU="
                "Basic" é o tipo de autenticação selecionada no "Auth" da requisição.
                remover o "Basic" e o espaço em branco da sequencia de caracteres:
                */ 
                var authorization_filtred = authorization.substring("Basic".length()).trim();
    
                // Decodificar a authorization_filtred, pois vem no formato array de bytes:           "[B@311d1c02"
                byte[] decoded_authorization = Base64.getDecoder().decode(authorization_filtred);
                
                // Transformar o decoded_authorization (que é um array de bytes) para string. Após transformado, decoded_authorization_string virá no formato "username:password". Exemplo: "melania_chagas:12345"
                var decoded_authorization_string = new String(decoded_authorization);
    
                // É necessário pegar as informações "username" e "password" separadamente. Primeiramente é feito um "split(":") que irá gerar um array nesse formato: [username, password]"
                String[] credentials = decoded_authorization_string.split(":");
    
                // Agora é possível pegar as informações "username" e "password" separadamente:
                String username = credentials[0];
                String password = credentials[1];
    
                // Validar se o usuário existe no banco de dados:
                var user = this.iUserRepository.findByUsername(username);
                if(user == null) {
                    response.sendError(401, "Usuário não autorizado");
                }
                else {
                    // Validar senha
                    // Transforma o 'password' em 'toCharArray()' pois é o formato que a função 'verify' espera.
                    var password_verify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                    // a função 'verified' retorna true ou false.
                    if(password_verify.verified) {
                        request.setAttribute("idUser", user.getId());
                        filterChain.doFilter(request, response);
                    } else {
                        response.sendError(401);
                    }
                }
            }else {
                filterChain.doFilter(request, response);
            }

        }
}
