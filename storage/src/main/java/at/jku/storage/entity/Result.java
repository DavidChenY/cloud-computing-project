package at.jku.storage.entity;

import java.util.concurrent.atomic.AtomicInteger;

public class Result {

    private static final AtomicInteger _ID = new AtomicInteger(0);
    private int id;
    private int first;
    private int second;
    private int result;

    protected Result() {}

    public Result(int first, int second, int result) {
        this.id = _ID.incrementAndGet();
        this.first = first;
        this.second = second;
        this.result = result;
    }

    @Override
    public String toString() {
        return "Result{" +
                "first=" + first +
                ", second=" + second +
                ", result=" + result +
                '}';
    }

    public int getId() {
        return id;
    }

    public int getFirst() {
        return first;
    }

    public int getSecond() {
        return second;
    }

    public int getResult() {
        return result;
    }
}
