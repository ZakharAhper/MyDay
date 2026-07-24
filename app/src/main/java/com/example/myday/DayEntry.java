package com.example.myday;

public class DayEntry {

    private int id;
    private int score;
    private String mood;
    private String comment;
    private String date;

    public DayEntry(int id, int score, String mood, String comment, String date) {
        this.id = id;
        this.score = score;
        this.mood = mood;
        this.comment = comment;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public int getScore() {
        return score;
    }

    public String getMood() {
        return mood;
    }

    public String getComment() {
        return comment;
    }

    public String getDate() {
        return date;
    }
}