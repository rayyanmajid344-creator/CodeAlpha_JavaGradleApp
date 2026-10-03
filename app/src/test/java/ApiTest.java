package com.codealpha.app;

import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class ApiTest {

    @Test
    void healthEndpointReportsUp() {
        JavalinTest.test(App.createApp(new TaskService()), (server, client) -> {
            var response = client.get("/health");
            assertEquals(200, response.code());
            assertTrue(response.body().string().contains("UP"));
        });
    }

    @Test
    void homePageIsServed() {
        JavalinTest.test(App.createApp(new TaskService()), (server, client) -> {
            var response = client.get("/");
            assertEquals(200, response.code());
            assertTrue(response.body().string().contains("<h1>Tasks</h1>"));
        });
    }

    @Test
    void createdTaskAppearsInList() {
        JavalinTest.test(App.createApp(new TaskService()), (server, client) -> {
            var created = client.post("/api/tasks", Map.of("title", "Write API tests"));
            assertEquals(201, created.code());
            var list = client.get("/api/tasks");
            assertTrue(list.body().string().contains("Write API tests"));
        });
    }

    @Test
    void blankTitleReturns400() {
        JavalinTest.test(App.createApp(new TaskService()), (server, client) -> {
            var response = client.post("/api/tasks", Map.of("title", ""));
            assertEquals(400, response.code());
        });
    }

    @Test
    void patchMarksTaskDone() {
        TaskService service = new TaskService();
        Task task = service.addTask("Ship it");
        JavalinTest.test(App.createApp(service), (server, client) -> {
            var response = client.patch("/api/tasks/" + task.id(), Map.of("done", true));
            assertEquals(200, response.code());
            assertTrue(service.getAllTasks().get(0).done());
        });
    }

    @Test
    void deleteRemovesTask() {
        TaskService service = new TaskService();
        Task task = service.addTask("Temporary");
        JavalinTest.test(App.createApp(service), (server, client) -> {
            var response = client.delete("/api/tasks/" + task.id());
            assertEquals(204, response.code());
            assertTrue(service.getAllTasks().isEmpty());
        });
    }

    @Test
    void unknownTaskReturns404() {
        JavalinTest.test(App.createApp(new TaskService()), (server, client) -> {
            assertEquals(404, client.delete("/api/tasks/999").code());
        });
    }

    @Test
    void nonNumericIdReturns400() {
        JavalinTest.test(App.createApp(new TaskService()), (server, client) -> {
            assertEquals(400, client.delete("/api/tasks/abc").code());
        });
    }
}