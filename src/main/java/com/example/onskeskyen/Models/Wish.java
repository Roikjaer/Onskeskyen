package com.example.onskeskyen.Models;

import jakarta.persistence.*;

@Entity
public class Wish {

    @Id
    @GeneratedValue
    private Long id;

    private String text;

    @ManyToOne
    private User user;

    public Long getId() { return id; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}