package com.example.cazapatos.models;

public class User {
    private String nick;
    private int animal;

    public User() {
    }

    public User(String nick, int animal) {
        this.nick = nick;
        this.animal = animal;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public int getAnimal() {
        return animal;
    }

    public void setAnimal(int animal) {
        this.animal = animal;
    }
}
