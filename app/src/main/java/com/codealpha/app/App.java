package com.codealpha.app;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import java.util.Map;
import java.util.NoSuchElementException;

public class App {

    public record NewTask(String title) {}
    public record UpdateTask(Boolean done) {}

    public static void main(String[] args) {
        TaskService service = new TaskService();
        service.addTask("Learn Gradle");
        service.addTask("Set up CI/CD");
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "7070"));
        createApp(service).start(port);
    }

    static Javalin createApp(TaskService service) {
        return Javalin.create(config -> config.staticFiles.add("/public", Location.CLASSPATH))
            .get("/health", ctx -> ctx.json(Map.of("status", "UP")))
            .get("/api/tasks", ctx -> ctx.json(service.getAllTasks()))
            .post("/api/tasks", ctx -> {
                NewTask body = ctx.bodyAsClass(NewTask.class);
                ctx.status(201).json(service.addTask(body.title()));
            })
            .patch("/api/tasks/{id}", ctx -> {
                int id = ctx.pathParamAsClass("id", Integer.class).get();
                UpdateTask body = ctx.bodyAsClass(UpdateTask.class);
                if (body.done() == null) {
                    throw new IllegalArgumentException("Field 'done' is required");
                }
                ctx.json(service.setDone(id, body.done()));
            })
            .delete("/api/tasks/{id}", ctx -> {
                int id = ctx.pathParamAsClass("id", Integer.class).get();
                service.deleteTask(id);
                ctx.status(204);
            })
            .exception(IllegalArgumentException.class, (e, ctx) ->
                ctx.status(400).json(Map.of("error", e.getMessage())))
            .exception(NoSuchElementException.class, (e, ctx) ->
                ctx.status(404).json(Map.of("error", e.getMessage())));
    }
}