package com.example.lupl.Models;

public class User {
    private int id;
    private String username;
    private String mail;
    private String password;
    private int xp;
    private int hp;
    private int coin;

    public int getXp() { return xp; }

    public void setXp(int xp) { this.xp = xp; }

    public int getHp() { return hp; }

    public void setHp(int hp) { this.hp = hp; }

    public int getCoin() { return coin; }

    public void setCoin(int coin) { this.coin = coin; }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
