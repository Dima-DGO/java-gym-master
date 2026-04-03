package ru.yandex.practicum.gym;

public class CounterForCoach implements Comparable<CounterForCoach> {
    private final Coach coach;
    private final int count;

    public CounterForCoach(Coach coach, int count) {
        this.coach = coach;
        this.count = count;
    }

    public Coach getCoach() {
        return coach;
    }

    public int getCount() {
        return count;
    }

    @Override
    public int compareTo(CounterForCoach other) {

        return Integer.compare(other.count, this.count);
    }

    @Override
    public String toString() {
        return "CounterForCoach{" +
                "coach=" + coach +
                ", count=" + count +
                '}';
    }
}
