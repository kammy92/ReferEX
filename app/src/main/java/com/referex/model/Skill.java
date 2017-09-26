package com.referex.model;

/**
 * Created by l on 26/09/2017.
 */

public class Skill {
    int id, parent_id;
    String skill_name;
    boolean parent_status;


    public Skill(String skill_name, int id, int parent_id, boolean parent_status) {
        this.id = id;
        this.parent_id = parent_id;
        this.skill_name = skill_name;
        this.parent_status = parent_status;
    }

    public Skill() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public String getSkill_name() {
        return skill_name;
    }

    public void setSkill_name(String skill_name) {
        this.skill_name = skill_name;
    }

    public boolean isParent_status() {
        return parent_status;
    }

    public void setParent_status(boolean parent_status) {
        this.parent_status = parent_status;
    }
}
