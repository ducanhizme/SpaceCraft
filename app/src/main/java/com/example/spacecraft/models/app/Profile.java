package com.example.spacecraft.models.app;

import java.io.Serializable;

public class Profile implements Serializable {
    private long id;
    private String username;
    private int highestScore;
    private long timestamp;
    private String description;
    private int quanity;

    public Profile() {
    }

    public Profile( String username, int highestScore) {
        this.username = username;
        this.highestScore = highestScore;
    }

    public Profile(long id, String username, int highestScore, long timestamp, String description, int quanity) {
        this.id = id;
        this.username = username;
        this.highestScore = highestScore;
        this.timestamp = timestamp;
        this.description = description;
        this.quanity = quanity;
    }



    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuanity() {
        return quanity;
    }

    public void setQuanity(int quanity) {
        this.quanity = quanity;
    }

    public Profile(long id, String username, int highestScore) {
        this.id = id;
        this.username = username;
        this.highestScore = highestScore;
    }

    public long getIdLocal() {
        return id;
    }

    public String getId() {
        return String.valueOf(id);
    }

    public void setId(String id) {
        this.id = Long.parseLong(id);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getHighestScore() {
        return highestScore;
    }

    public void setHighestScore(int highestScore) {
        this.highestScore = highestScore;
    }
}
