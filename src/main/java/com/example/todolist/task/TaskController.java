package com.example.todolist.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.todolist.utils.Utils;

import jakarta.servlet.http.HttpServletRequest;

// TaskController lida com as operações relacionadas às tarefas(tasks).

@RestController
@RequestMapping("/tasks")

public class TaskController {
    @Autowired
    private ITaskRepository taskRepository;

    /**
     * Rota do tipo "POST" para cadastro de usuários.
     * @param taskModel Objeto do tipo 'TaskModel' a ser criado;
     * @param request Objeto do tipo 'HttpServletRequest' contendo informações da solicitação;
     *  @return Um 'ResponseEntity' com a resposta da operação, que pode conter um objeto do tipo 'TaskModel' ou uma mensagem de erro.
    */ 
    @PostMapping("/")
    public ResponseEntity<Object> createNewTask(@RequestBody TaskModel taskModel, HttpServletRequest request) {
        var idUser = request.getAttribute("idUser");
        taskModel.setIdUser((UUID)idUser);

        var currentDate = LocalDateTime.now();
        if(currentDate.isAfter(taskModel.getStartAt()) || 
            currentDate.isAfter(taskModel.getEndAt())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("A data de início/data de término - devem ser maior do que a data atual");
        }

        if(taskModel.getStartAt().isAfter(taskModel.getEndAt())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("A data de início deve ser menor do que a data de término");
        }

        var task = this.taskRepository.save(taskModel);
        return ResponseEntity.status(HttpStatus.OK).body(task);
    }


    /**
     * Rota do tipo "GET" para obter a lista de tarefas associadas a um usuário.
     * @param request Objeto do tipo 'HttpServletRequest' contendo informações da solicitação;
     * @return Uma lista de objetos do tipo 'TaskModel'.
    */
    @GetMapping("/")
    public List<TaskModel> getTaskByUserId(HttpServletRequest request) {
        var idUser = request.getAttribute("idUser");
        var tasks = this.taskRepository.findTasksByIdUser((UUID)idUser);
        return tasks;
    }


    /**
     * Rota do tipo "PUT" para atualizar informações em uma tarefa existente com base em seu ID.
     *
     * @param taskModel Objeto do tipo 'TaskModel' com os dados atualizados;
     * @param id        O ID da tarefa a ser atualizada;
     * @param request   Objeto do tipo 'HttpServletRequest' contendo informações da solicitação;
     * @return Um 'ResponseEntity' com a resposta da operação, que pode conter um objeto TaskModel atualizado ou uma mensagem de erro.
    */
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTaskById(
        @RequestBody TaskModel taskModel,
        @PathVariable UUID id,
        HttpServletRequest request) {
            var task = this.taskRepository.findById(id).orElse(null);
            if(task == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Tarefa não encontrada");
            }
            var idUser = request.getAttribute("idUser");

            if(!task.getIdUser().equals(idUser)){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Este usuário não tem permissão para alterar essa tarefa");
            }
            Utils.copyNonNullProperties(taskModel, task);
            
            var taskUpdated = this.taskRepository.save(task);
            return ResponseEntity.status(HttpStatus.OK).body(taskUpdated);
        }
}
