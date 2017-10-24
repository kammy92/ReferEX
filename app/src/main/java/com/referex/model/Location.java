package com.referex.model;

/**
 * Created by l on 26/09/2017.
 */

public class Location {
    int id;
    String name;
    
    public Location (int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public int getId () {
        return id;
    }
    
    public void setId (int id) {
        this.id = id;
    }
    
    public String getName () {
        return name;
    }
    
    public void setName (String name) {
        this.name = name;
    }
}
