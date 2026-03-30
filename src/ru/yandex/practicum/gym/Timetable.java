package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {
    private final Map<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable;

    public Timetable() {
        this.timetable = new HashMap<>();
        for (DayOfWeek day : DayOfWeek.values()) {
            timetable.put(day, new TreeMap<>());
        }
    }

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek day = trainingSession.getDayOfWeek();
        TimeOfDay time = trainingSession.getTimeOfDay();

        TreeMap<TimeOfDay, List<TrainingSession>> dayMap = timetable.get(day);
        List<TrainingSession> listSession = dayMap.get(time);

        if (listSession == null) {
            listSession = new ArrayList<>();
            dayMap.put(time, listSession);
        }

        if (!listSession.contains(trainingSession)) {
            listSession.add(trainingSession);
        }

    }

    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        TreeMap<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(dayOfWeek);
        if (daySchedule == null) return Collections.emptyList();
        List<TrainingSession> result = new ArrayList<>();
        for (List<TrainingSession> sessions : daySchedule.values()) {
            result.addAll(sessions);
        }
        return result;
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        TreeMap<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(dayOfWeek);
        if (daySchedule == null) {
            return Collections.emptyList();
        }

        List<TrainingSession> sessions = daySchedule.get(timeOfDay);
        return sessions != null ? sessions : Collections.emptyList();
    }

    public Map<Coach, Integer> getCountByCoaches() {
        Map<Coach, Integer> coachCount = new HashMap<>();

        for (TreeMap<TimeOfDay, List<TrainingSession>> daySchedule : timetable.values()) {
            for (List<TrainingSession> sessions : daySchedule.values()) {
                for (TrainingSession session : sessions) {
                    Coach coach = session.getCoach();
                    if (coachCount.containsKey(coach)) {
                        coachCount.put(coach, coachCount.get(coach) + 1);
                    } else {
                        coachCount.put(coach, 1);
                    }
                }
            }
        }

        List<Map.Entry<Coach, Integer>> sortedEntries = new ArrayList<>(coachCount.entrySet());
        sortedEntries.sort(Map.Entry.<Coach, Integer>comparingByValue().reversed());

        Map<Coach, Integer> result = new LinkedHashMap<>();
        for (Map.Entry<Coach, Integer> entry : sortedEntries) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
}

