package com.development.honza.jasichinese.db;

import java.util.Set;

public class Settings {
    private float speed;
    private String path;
    private Integer id;

    public Settings() {};

    public Settings(Integer id, float speed, String path) {
        this.id = id;
        this.speed = speed;
        this.path = path;
    }

    public float getSpeed() {
        return speed;
    }

    public String getPath() {
        return path;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setPath(String path) { this.path = path;}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
