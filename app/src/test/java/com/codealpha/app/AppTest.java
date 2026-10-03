package com.codealpha.app;

import org.junit.jupiter.api.Test;
import java.util.NoSuchElementException;
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

    @Test
    void tooLongTitleIsRejected() {
        TaskService service = new TaskService();
        assertThrows(IllegalArgumentException.class, () -> service.addTask("a".repeat(201)));
    }

    @Test
    void setDoneMarksTaskAsDone() {
        TaskService service = new TaskService();
        Task task = service.addTask("Finish polish");
        assertTrue(service.setDone(task.id(), true).done());
        assertTrue(service.getAllTasks().get(0).done());
    }

    @Test
    void deleteRemovesTask() {
        TaskService service = new TaskService();
        Task task = service.addTask("Temporary");
        service.deleteTask(task.id());
        assertTrue(service.getAllTasks().isEmpty());
    }

    @Test
    void unknownIdThrowsNotFound() {
        TaskService service = new TaskService();
        assertThrows(NoSuchElementException.class, () -> service.setDone(99, true));
        assertThrows(NoSuchElementException.class, () -> service.deleteTask(99));
    }
}