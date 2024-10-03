package com.softserve.itacademy.repository;

import com.softserve.itacademy.model.Priority;
import com.softserve.itacademy.model.Task;
import com.softserve.itacademy.model.ToDo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class TaskRepositoryTest {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ToDoRepository toDoRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @Transactional
    public void newTaskTest() {
        ToDo toDo = toDoRepository.getOne(10L);
        Task task = new Task();
        task.setName("Test");
        task.setPriority(Priority.LOW);
        task.setTodo(toDo);
        taskRepository.save(task);
        Task actual = taskRepository.getByTodoId(toDo.getId()).get(0);
        Assertions.assertEquals("Test", actual.getName());
    }
}

