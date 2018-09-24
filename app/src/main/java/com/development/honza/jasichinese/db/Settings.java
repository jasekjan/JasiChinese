package com.development.honza.jasichinese.db;

import java.util.Set;

public class Settings {
    private float speed;

    public Settings() {};

    public Settings(float speed) {
        this.speed = speed;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
