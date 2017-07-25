package com.referex.model;

import org.json.JSONObject;

/**
 * Created by Rahul jain on 21-07-2017.
 */

public class JobDescription {
    int id;
    String job_name,experience,skill,company_name,location;
    boolean is_favorite,is_refer;



    public JobDescription(int id,String job_name,String company_name,String experience,String location,String skill) {
        this.id=id;
        this.job_name=job_name;
        this.experience=experience;
        this.skill=skill;
        this.company_name=company_name;
        this.location=location;

    }

    public JobDescription() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJob_name() {
        return job_name;
    }

    public void setJob_name(String job_name) {
        this.job_name = job_name;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}