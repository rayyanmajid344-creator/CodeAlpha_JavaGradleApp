package com.codealpha.app;

import io.javalin.Javalin;
import java.util.Map;

public class App {

    public record NewTask(String title) {}

    public static void main(String[] args) {
        TaskService service = new TaskService();
        service.addTask("Learn Gradle");
        service.addTask("Set up CI/CD");
        createApp(service).start(7070);
    }

    static Javalin createApp(TaskService service) {
        return Javalin.create()
            .get("/", ctx -> ctx.result("CodeAlpha Task Manager API is running"))
            .get("/health", ctx -> ctx.json(Map.of("status", "UP")))
            .get("/tasks", ctx -> ctx.json(service.getAllTasks()))
            .post("/tasks", ctx -> {
                NewTask body = ctx.bodyAsClass(NewTask.class);
                ctx.status(201).json(service.addTask(body.title()));
            })
            .exception(IllegalArgumentException.class, (e, ctx) ->
                ctx.status(400).json(Map.of("error", e.getMessage())));
    }
}