package com.udacity.and.popularmovies.models;

public class Video {

    private String id;
    private String key;
    private String name;
    private String type;

    public Video(String id, String key, String name, String type) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
