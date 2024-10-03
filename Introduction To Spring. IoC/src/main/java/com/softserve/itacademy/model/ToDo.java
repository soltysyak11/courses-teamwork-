package com.softserve.itacademy.model;

import java.time.LocalDateTime;
import java.util.List;

public class ToDo {

    private String title;

    private LocalDateTime createdAt;

    private User owner;

    private List<Task> tasks;

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    // Constructor(s), getters, setters, hashCode, equals, etc.

}
