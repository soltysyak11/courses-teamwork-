package com.softserve.itacademy;

import com.softserve.itacademy.model.Priority;
import com.softserve.itacademy.model.Task;
import com.softserve.itacademy.model.ToDo;
import com.softserve.itacademy.model.User;
import com.softserve.itacademy.service.TaskService;
import com.softserve.itacademy.service.ToDoService;
import com.softserve.itacademy.service.UserService;
import com.softserve.itacademy.service.impl.TaskServiceImpl;
import com.softserve.itacademy.service.impl.ToDoServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.List;

@RunWith(JUnitPlatform.class)
public class TaskServiceTest {
    private static UserService userService;
    private TaskService taskService;
    private ToDoService toDoService;

    @BeforeAll
    public static void setupBeforeClass() throws Exception {
        AnnotationConfigApplicationContext annotationConfigContext = new AnnotationConfigApplicationContext(Config.class);
        userService = annotationConfigContext.getBean(UserService.class);
        annotationConfigContext.close();
    }

    @BeforeEach
    public void setup() {
        toDoService = new ToDoServiceImpl(userService);
        taskService = new TaskServiceImpl(toDoService);
    }

    @Test
    public void checkAddTask() {
        Task task = new Task("Test Task", Priority.MEDIUM);
        ToDo todo = new ToDo();
        todo.setTasks(new ArrayList<>());

        Task addedTask = taskService.addTask(task, todo);
        Assertions.assertNotNull(addedTask, "Task should not be null");
        Assertions.assertEquals("Test Task", addedTask.getName(), "Task name should be 'Test Task'");
        Assertions.assertEquals(Priority.MEDIUM, addedTask.getPriority(), "Task priority should be 'MEDIUM'");
    }

    @Test
    public void checkUpdateTask() {
        User user = new User("FirstName", "LastName", "Email", "1111", new ArrayList<>());
        userService.addUser(user);

        Task task = new Task("Old Task", Priority.LOW);
        ToDo todo = new ToDo();
        todo.setTasks(new ArrayList<>(List.of(task)));
        user.getMyTodos().add(todo);
        toDoService.addTodo(todo, user);

        Task updatedTask = new Task("Old Task", Priority.HIGH);
        Task result = taskService.updateTask(updatedTask);

        Assertions.assertNotNull(result, "Updated task should not be null");
        Assertions.assertEquals("Old Task", result.getName(), "Task name should be 'Old Task'");
        Assertions.assertEquals(Priority.HIGH, result.getPriority(), "Task priority should be updated to HIGH");

        ToDo updatedToDo = toDoService.getAll().get(0);
        Assertions.assertTrue(updatedToDo.getTasks().contains(result), "ToDo should contain the updated task");
        Assertions.assertEquals(Priority.HIGH, updatedToDo.getTasks().get(0).getPriority(), "Task priority should be updated in the ToDo");
        userService.deleteUser(user);
    }


    @Test
    public void checkDeleteTask() {
        Task task = new Task("Task to Delete", Priority.MEDIUM);
        ToDo todo = new ToDo();
        todo.setTasks(new ArrayList<>(List.of(task)));
        User user = new User("FirstName", "LastName", "Email", "1111", new ArrayList<>());
        user.setMyTodos(new ArrayList<>(List.of(todo)));
        userService.addUser(user);
        toDoService.addTodo(todo, user);

        taskService.deleteTask(task);

        Assertions.assertTrue(todo.getTasks().isEmpty(), "ToDo's task list should be empty after deletion");
        userService.deleteUser(user);
    }

    @Test
    public void checkGetAll() {
        ToDo todo1 = new ToDo();
        ToDo todo2 = new ToDo();

        Task task1 = new Task("Task 1", Priority.LOW);
        Task task2 = new Task("Task 2", Priority.HIGH);

        todo1.setTasks(new ArrayList<>(List.of(task1)));
        todo2.setTasks(new ArrayList<>(List.of(task2)));

        User user = new User("FirstName", "LastName", "Email", "1111", new ArrayList<>());
        user.setMyTodos(new ArrayList<>(List.of(todo1, todo2)));
        userService.addUser(user);

        toDoService.addTodo(todo1, user);
        toDoService.addTodo(todo2, user);

        List<Task> allTasks = taskService.getAll();

        Assertions.assertEquals(2, allTasks.size(), "There should be 2 tasks in total");
        Assertions.assertTrue(allTasks.contains(task1), "Task 1 should be present in the list");
        Assertions.assertTrue(allTasks.contains(task2), "Task 2 should be present in the list");

        userService.deleteUser(user);
    }

    @Test
    public void checkGetByToDo() {
        ToDo todo = new ToDo();
        Task task = new Task("Task for ToDo", Priority.MEDIUM);
        todo.setTasks(new ArrayList<>(List.of(task)));

        List<Task> tasks = taskService.getByToDo(todo);

        Assertions.assertEquals(1, tasks.size(), "There should be one task");
        Assertions.assertEquals(task, tasks.get(0), "The task should match the one in ToDo");
    }

    @Test
    public void checkGetByToDoName() {
        Task task = new Task("Unique Task", Priority.HIGH);

        ToDo todo = new ToDo();
        todo.setTasks(new ArrayList<>(List.of(task)));

        Task foundTask = taskService.getByToDoName(todo, "Unique Task");

        Assertions.assertNotNull(foundTask, "Task should be found");
        Assertions.assertEquals("Unique Task", foundTask.getName(), "Task name should match");
    }

    @Test
    public void checkGetByUserName() {
        User user = new User("FirstName", "LastName", "Email", "1111", new ArrayList<>());
        Task task = new Task("User Task", Priority.LOW);

        ToDo todo = new ToDo();
        todo.setTasks(new ArrayList<>(List.of(task)));
        user.setMyTodos(new ArrayList<>(List.of(todo)));
        userService.addUser(user);

        toDoService.addTodo(todo, user);

        Task foundTask = taskService.getByUserName(user, "User Task");

        Assertions.assertNotNull(foundTask, "Task should be found for the user");
        Assertions.assertEquals("User Task", foundTask.getName(), "Task name should match");

        userService.deleteUser(user);
    }
}
