package com.samla.sdk.analytics.funnel;

public class WayPoint {
    private int invocations;
    private int number;
    private int timestamp;

    WayPoint (int waypoint) {
        this.number = waypoint;
    }

    public int getInvocations() {
        return invocations;
    }

    public void setInvocations(int invocations) {
        this.invocations = invocations;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }
}
