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

    @ManyToOne
    private Wishlist wishlist;

    @ManyToOne
    private User reservedBy;

    public Long getId() { return id; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Wishlist getWishlist() { return wishlist; }
    public void setWishlist(Wishlist wishlist) { this.wishlist = wishlist; }

    public User getReservedBy() { return reservedBy; }
    public void setReservedBy(User reservedBy) { this.reservedBy = reservedBy; }

    public boolean isReserved() { return reservedBy != null; }
}