package com.codealpha.app;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class TaskService {
    private final List<Task> tasks = new ArrayList<>();
    private int nextId = 1;

    public synchronized Task addTask(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title must not be empty");
        }
        String clean = title.trim();
        if (clean.length() > 200) {
            throw new IllegalArgumentException("Title must be 200 characters or fewer");
        }
        Task task = new Task(nextId++, clean, false);
        tasks.add(task);
        return task;
    }

    public synchronized List<Task> getAllTasks() {
        return List.copyOf(tasks);
    }

    public synchronized Task setDone(int id, boolean done) {
        int index = indexOf(id);
        Task updated = new Task(id, tasks.get(index).title(), done);
        tasks.set(index, updated);
        return updated;
    }

    public synchronized void deleteTask(int id) {
        tasks.remove(indexOf(id));
    }

    private int indexOf(int id) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).id() == id) {
                return i;
            }
        }
        throw new NoSuchElementException("Task " + id + " not found");
    }
}