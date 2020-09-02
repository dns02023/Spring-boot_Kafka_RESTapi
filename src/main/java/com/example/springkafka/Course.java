package com.example.springkafka;

public class Course {
    int id;
    String name;
    String professor;

    // constructor
    public Course(int id, String name, String professor){
        this.id = id;
        this.name = name;
        this.professor = professor;

    }

    //getter and setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }
}
