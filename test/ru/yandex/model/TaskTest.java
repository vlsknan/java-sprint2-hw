package ru.yandex.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    @DisplayName("Получить конечное время")
    void getEndTimeTest() {
        Task task = new Task("Задача 1", 0, "Описание задачи 1", Status.NEW,
                TypeTask.TASK, 30, LocalDateTime.of(2022, 05, 25, 12, 30));
        LocalDateTime endTime = task.getEndTime();

        assertEquals(LocalDateTime.of(2022,05, 25, 13, 00), endTime);
    }
}