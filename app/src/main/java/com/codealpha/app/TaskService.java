package com.codealpha.app;

import java.util.ArrayList;
import java.util.List;

public class TaskService {
    private final List<Task> tasks = new ArrayList<>();
    private int nextId = 1;

    public synchronized Task addTask(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title must not be empty");
        }
        Task task = new Task(nextId++, title.trim(), false);
        tasks.add(task);
        return task;
    }

    public synchronized List<Task> getAllTasks() {
        return List.copyOf(tasks);
    }
}