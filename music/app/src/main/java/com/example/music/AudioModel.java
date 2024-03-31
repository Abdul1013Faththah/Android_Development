package com.example.music;

import java.io.Serializable;

public class AudioModel implements Serializable {
    String path;
    String tittle;
    String duration;

    public AudioModel(String path, String tittle, String duration) {
        this.path = path;
        this.tittle = tittle;
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

}
