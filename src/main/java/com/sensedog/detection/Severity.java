package com.sensedog.detection;

public enum Severity {

    SUSPICIOUS(0),
    WARNING(1),
    CRITICAL(2);

    private final int grade;

    Severity(int grade) {
        this.grade = grade;
    }

    public boolean isAbove(Severity severity) {
        return grade > severity.grade;
    }

    public boolean isAboveOrEqual(Severity severity) {
        return grade >= severity.grade;
    }
}
