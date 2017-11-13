package com.referex.model;

/**
 * Created by l on 10/11/2017.
 */

public class FilterSkills {
    
    int id;
    String skill;
    
    public FilterSkills (int id, String skill) {
        this.id = id;
        this.skill = skill;
    }
    
    public int getId () {
        return id;
    }
    
    public void setId (int id) {
        this.id = id;
    }
    
    public String getSkill () {
        return skill;
    }
    
    public void setSkill (String skill) {
        this.skill = skill;
    }
}
