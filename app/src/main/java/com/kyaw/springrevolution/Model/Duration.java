package com.kyaw.springrevolution.Model;

public class Duration {
    int minute,second;

    public Duration(int minute, int second) {
        this.minute = minute;
        this.second = second;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }
}
