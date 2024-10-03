package com.softserve.itacademy.controller;

import com.softserve.itacademy.model.Priority;
import com.softserve.itacademy.model.Task;
import com.softserve.itacademy.repository.TaskRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@WebServlet("/create-task")
public class CreateTaskServlet extends HttpServlet {

    private TaskRepository taskRepository;

    @Override
    public void init() {
        taskRepository = TaskRepository.getTaskRepository();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/pages/create-task.jsp").forward(request, response);

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Task task = new Task(
                    request.getParameter("title"),
                    Priority.valueOf(request.getParameter("priority"))
            );
            boolean ifTitleExists = taskRepository
                    .all()
                    .stream()
                    .anyMatch(t -> Objects.equals(t.getTitle(), task.getTitle()));
            if (ifTitleExists) throw new IllegalArgumentException("Title already exists");
            taskRepository.create(task);
        } catch (IllegalArgumentException e) {
            request.setAttribute("errors", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/pages/create-task.jsp").forward(request, response);
            return;
        }
        response.sendRedirect("/tasks-list");
    }
}
