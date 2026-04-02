package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        TreeMap<TimeOfDay, List<TrainingSession>> mondaySchedule = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        Assertions.assertEquals(1, mondaySchedule.size());

        TreeMap<TimeOfDay, List<TrainingSession>> tuesdaySchedule = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        Assertions.assertTrue(tuesdaySchedule.isEmpty());
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

        TreeMap<TimeOfDay, List<TrainingSession>> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        Assertions.assertEquals(1, mondaySessions.size());

        TreeMap<TimeOfDay, List<TrainingSession>> thursdaySchedule = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        Assertions.assertEquals(2, thursdaySchedule.size());

        List<TrainingSession> thursday13Sessions = thursdaySchedule.get(new TimeOfDay(13, 0));
        List<TrainingSession> thursday20Sessions = thursdaySchedule.get(new TimeOfDay(20, 0));

        Assertions.assertNotNull(thursday13Sessions);
        Assertions.assertNotNull(thursday20Sessions);
        Assertions.assertEquals(1, thursday13Sessions.size());
        Assertions.assertEquals(1, thursday20Sessions.size());

        TreeMap<TimeOfDay, List<TrainingSession>> tuesdaySchedule = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        Assertions.assertTrue(tuesdaySchedule.isEmpty());
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

        TreeMap<TimeOfDay, List<TrainingSession>> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
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

        List<CounterForCoach> result = timetable.getCountByCoaches();
        Assertions.assertEquals(1, result.size());

        CounterForCoach counter = result.get(0);
        Assertions.assertEquals(coach, counter.getCoach());
        Assertions.assertEquals(2, counter.getCount());
    }

    @Test
    void testGetCountByCoaches_MultipleCoaches() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика", Age.CHILD, 60);
        Coach coach1 = new Coach("Иванов", "Иван", "Иванович");
        Coach coach2 = new Coach("Петров", "Пётр", "Петрович");
        Coach coach3 = new Coach("Сидоров", "Сергей", "Сергеевич");

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

        timetable.addNewTrainingSession(new TrainingSession(group, coach3, DayOfWeek.SATURDAY,
                new TimeOfDay(15, 0)));

        List<CounterForCoach> result = timetable.getCountByCoaches();
        Assertions.assertEquals(3, result.size());

        CounterForCoach first = result.get(0);
        CounterForCoach second = result.get(1);
        CounterForCoach third = result.get(2);

        Assertions.assertEquals(coach1, first.getCoach());
        Assertions.assertEquals(3, first.getCount());

        Assertions.assertEquals(coach2, second.getCoach());
        Assertions.assertEquals(2, second.getCount());

        Assertions.assertEquals(coach3, third.getCoach());
        Assertions.assertEquals(1, third.getCount());
    }

    @Test
    void testGetCountByCoaches_EmptyTimetable() {
        Timetable timetable = new Timetable();
        List<CounterForCoach> result = timetable.getCountByCoaches();
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void testAddNewTrainingSession_MultipleGroupsSameTime() {
        Timetable timetable = new Timetable();

        Group childGroup = new Group("Детская акробатика", Age.CHILD, 60);
        Group adultGroup = new Group("Взрослая акробатика", Age.ADULT, 90);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        TrainingSession childSession = new TrainingSession(childGroup, coach, DayOfWeek.MONDAY,
                new TimeOfDay(18, 0));
        TrainingSession adultSession = new TrainingSession(adultGroup, coach, DayOfWeek.MONDAY,
                new TimeOfDay(18, 0));

        timetable.addNewTrainingSession(childSession);
        timetable.addNewTrainingSession(adultSession);

        TreeMap<TimeOfDay, List<TrainingSession>> mondaySchedule = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        List<TrainingSession> sessionsAt18 = mondaySchedule.get(new TimeOfDay(18, 0));

        Assertions.assertNotNull(sessionsAt18);
        Assertions.assertEquals(2, sessionsAt18.size());
        Assertions.assertTrue(sessionsAt18.contains(childSession));
        Assertions.assertTrue(sessionsAt18.contains(adultSession));
    }

    @Test
    void testGetTrainingSessionsForDay_EmptyDay() {
        Timetable timetable = new Timetable();

        Group group = new Group("Растяжка", Age.ADULT, 45);
        Coach coach = new Coach("Смирнов", "Андрей", "Андреевич");

        timetable.addNewTrainingSession(new TrainingSession(group, coach, DayOfWeek.FRIDAY,
                new TimeOfDay(19, 0)));

        TreeMap<TimeOfDay, List<TrainingSession>> sundaySchedule = timetable.getTrainingSessionsForDay(DayOfWeek.SUNDAY);
        Assertions.assertTrue(sundaySchedule.isEmpty());

        TreeMap<TimeOfDay, List<TrainingSession>> fridaySchedule = timetable.getTrainingSessionsForDay(DayOfWeek.FRIDAY);
        Assertions.assertFalse(fridaySchedule.isEmpty());
        Assertions.assertEquals(1, fridaySchedule.size());
    }
}
