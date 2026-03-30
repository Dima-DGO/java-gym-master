package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        Assertions.assertEquals(1, mondaySessions.size());
        Assertions.assertTrue(mondaySessions.contains(singleTrainingSession));

        List<TrainingSession> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        Assertions.assertTrue(tuesdaySessions.isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        Assertions.assertEquals(1, mondaySessions.size());
        Assertions.assertTrue(mondaySessions.contains(mondayChildTrainingSession));

        List<TrainingSession> thursdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        Assertions.assertEquals(2, thursdaySessions.size());

        Assertions.assertEquals(new TimeOfDay(13, 0), thursdaySessions.get(0).getTimeOfDay());
        Assertions.assertEquals(new TimeOfDay(20, 0), thursdaySessions.get(1).getTimeOfDay());

        List<TrainingSession> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        Assertions.assertTrue(tuesdaySessions.isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        List<TrainingSession> sessionsAt13 = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY,
                new TimeOfDay(13, 0));
        Assertions.assertEquals(1, sessionsAt13.size());
        Assertions.assertTrue(sessionsAt13.contains(singleTrainingSession));

        List<TrainingSession> sessionsAt14 = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY,
                new TimeOfDay(14, 0));
        Assertions.assertTrue(sessionsAt14.isEmpty());
    }

    @Test
    void testAddDuplicateTrainingSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика", Age.CHILD, 60);
        Coach coach = new Coach("Иванов", "Иван", "Иванович");
        TrainingSession session = new TrainingSession(group, coach, DayOfWeek.MONDAY,
                new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(session);
        timetable.addNewTrainingSession(session); // попытка добавить дубликат

        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        Assertions.assertEquals(1, mondaySessions.size()); // должна быть только одна тренировка
    }

    @Test
    void testGetCountByCoaches_SingleCoach() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика", Age.CHILD, 60);
        Coach coach = new Coach("Иванов", "Иван", "Иванович");

        TrainingSession session1 = new TrainingSession(group, coach, DayOfWeek.MONDAY,
                new TimeOfDay(10, 0));
        TrainingSession session2 = new TrainingSession(group, coach, DayOfWeek.TUESDAY,
                new TimeOfDay(11, 0));

        timetable.addNewTrainingSession(session1);
        timetable.addNewTrainingSession(session2);

        Map<Coach, Integer> result = timetable.getCountByCoaches();
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(2, result.get(coach));
    }

    @Test
    void testGetCountByCoaches_MultipleCoaches() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика", Age.CHILD, 60);
        Coach coach1 = new Coach("Иванов", "Иван", "Иванович");
        Coach coach2 = new Coach("Петров", "Пётр", "Петрович");

        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.MONDAY,
                new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.TUESDAY,
                new TimeOfDay(11, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.WEDNESDAY,
                new TimeOfDay(12, 0)));

        timetable.addNewTrainingSession(new TrainingSession(group, coach2, DayOfWeek.THURSDAY,
                new TimeOfDay(13, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach2, DayOfWeek.FRIDAY,
                new TimeOfDay(14, 0)));

        Map<Coach, Integer> result = timetable.getCountByCoaches();

        List<Coach> coaches = new ArrayList<>(result.keySet());
        Assertions.assertEquals(coach1, coaches.get(0));
        Assertions.assertEquals(coach2, coaches.get(1));
    }

    @Test
    void testGetCountByCoaches_EmptyTimetable() {
        Timetable timetable = new Timetable();
        Map<Coach, Integer> result = timetable.getCountByCoaches();
        Assertions.assertTrue(result.isEmpty());
    }
}
