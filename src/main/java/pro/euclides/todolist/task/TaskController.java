package pro.euclides.todolist.task;

import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import pro.euclides.todolist.utils.Utils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private ITaskRepository taskRepository;

    @SuppressWarnings("rawtypes")
    @PostMapping("/")
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
        UUID userId = (UUID) request.getAttribute("userId");
        taskModel.setUserId(userId);

        var currentDate = LocalDateTime.now();
        if (currentDate.isAfter(taskModel.getStartAt()) || currentDate.isAfter(taskModel.getEndAt())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST).body("Start and End date must be in the future");
        }

        if (taskModel.getStartAt().isAfter(taskModel.getEndAt())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST).body("Start date must be before End date");
        }

        var task = this.taskRepository.save(taskModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @GetMapping("/")
    public List<TaskModel> list(HttpServletRequest request) {

        UUID userId = (UUID) request.getAttribute("userId");
        var tasks = this.taskRepository.findByUserId(userId);

        return tasks;
    }

    @SuppressWarnings("rawtypes")
    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody TaskModel taskModel, @PathVariable UUID id, HttpServletRequest request) {

        var task = this.taskRepository.findById(id).orElse(null);

        if (task == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Task not found");
        }

        var userId = (UUID) request.getAttribute("userId");

        if (!task.getUserId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You can't update this task");
        }

        Utils.copyNonNullProperties(taskModel, task);
        var updatedTask = this.taskRepository.save(task);

        return ResponseEntity.ok().body(updatedTask);
    }
}
