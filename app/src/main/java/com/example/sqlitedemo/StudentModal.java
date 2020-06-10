package com.example.sqlitedemo;

public class StudentModal {

    private int id;
    private String name;
    private String course;
    private boolean isDone;

    public StudentModal(int id, String name, String course, boolean isDone) {
        this.id = id;
        this.name = name;
        this.course = course;
        this.isDone = isDone;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCourse() {
        return course;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    @Override
    public String toString() {
        return "ID: " + id + " ,NAME: " + name + " ,COURSE: " + course + " ,DONE: "+ isDone;
    }
}
