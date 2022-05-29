package ru.yandex.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.zip.DataFormatException;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    @DisplayName("Получить время окончания эпика")
    void getEndTimeEpicTest() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yy, HH:mm");

        Subtask subtaskForEpic1 = new Subtask("Подзадача 1", 0,
                "Описание подзадачи 1 эпика 1", Status.NEW, 0,
                TypeTask.SUBTASK, 30, LocalDateTime.parse("17.06.22, 16:00", format));

        Subtask subtaskForEpic2 = new Subtask("Подзадача 2", 0,
                "Описание подзадачи 1 эпика 2", Status.IN_PROGRESS, 0,
                TypeTask.SUBTASK, 30, LocalDateTime.parse("30.05.22, 10:30", format));

        Epic epic = new Epic("Эпик 1", 0, "Описание эпика 1",
                Status.NO, TypeTask.EPIC, 0, subtaskForEpic2.getStartTime());
        epic.putSubtask(subtaskForEpic1);
        epic.putSubtask(subtaskForEpic2);

        LocalDateTime epicTime = epic.getEndTimeEpic();

        assertEquals(LocalDateTime.of(2022,05, 30, 11, 30), epicTime);
    }
}