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
                var authorizationFiltred = authorization.substring("Basic".length()).trim();
    
                // Decodificar a authorizationFiltred, pois vem no formato array de bytes[B@311d1c02
                byte[] decodedAuthorization = Base64.getDecoder().decode(authorizationFiltred);
                
                // Transformar o decodedAuthorization (que é um array de bytes) para string. Após transformado, decodedAuthorizationString virá no formato "username:password". Exemplo: "melania_chagas:12345"
                var decodedAuthorizationString = new String(decodedAuthorization);
    
                // É necessário pegar as informações "username" e "password" separadamente. Primeiramente é feito um "split(":") que irá gerar um array nesse formato: [username, password]"
                String[] credentials = decodedAuthorizationString.split(":");
    
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
                    var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                    // a função 'verified' retorna true ou false.
                    if(passwordVerify.verified) {
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
