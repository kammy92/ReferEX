package com.referex.model;

/**
 * Created by l on 10/11/2017.
 */

public class FilterLocation {
    int id;
    String location;
    
    public FilterLocation (int id, String location) {
        this.id = id;
        this.location = location;
    }
    
    
    public int getId () {
        return id;
    }
    
    public void setId (int id) {
        this.id = id;
    }
    
    public String getLocation () {
        return location;
    }
    
    public void setLocation (String location) {
        this.location = location;
    }
}
