package com.codealpha.app;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AppTest {

    @Test
    void addTaskStoresTheTask() {
        TaskService service = new TaskService();
        service.addTask("Write tests");
        assertEquals(1, service.getAllTasks().size());
        assertEquals("Write tests", service.getAllTasks().get(0).title());
    }

    @Test
    void idsIncreaseForEachTask() {
        TaskService service = new TaskService();
        Task first = service.addTask("One");
        Task second = service.addTask("Two");
        assertEquals(first.id() + 1, second.id());
    }

    @Test
    void blankTitleIsRejected() {
        TaskService service = new TaskService();
        assertThrows(IllegalArgumentException.class, () -> service.addTask("   "));
    }

    @Test
    void titleIsTrimmed() {
        TaskService service = new TaskService();
        assertEquals("Buy milk", service.addTask("  Buy milk  ").title());
    }
}