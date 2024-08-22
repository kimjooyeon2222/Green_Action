package com.example.green_action.Community;

public class User {
    private String id;
    private String pw;
    private String username;
    private String contact;
    private String gender;

    // 기본 생성자 (Firebase에서 데이터 변환 시 필요)
    public User() {
    }

    public User(String id, String pw, String username, String contact, String gender) {
        this.id = id;
        this.pw = pw;
        this.username = username;
        this.contact = contact;
        this.gender = gender;

    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
