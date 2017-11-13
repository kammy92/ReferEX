package com.referex.model;

/**
 * Created by Rahul jain on 21-07-2017.
 */

public class JobDescription {
    int id;
    String job_title, job_description, min_experience, max_experience, skill, company_name, location, min_salary, max_salary, posted_at, job_type, job_expires;
    boolean is_bookmarked;
    
    
    public JobDescription (int id, String job_title, String job_description, String min_experience, String max_experience, String skill, String company_name, String location, String min_salary, String max_salary, String posted_at, String job_type, String job_expires, boolean is_bookmarked) {
        this.id = id;
        this.job_title = job_title;
        this.job_description = job_description;
        this.min_experience = min_experience;
        this.max_experience = max_experience;
        this.skill = skill;
        this.company_name = company_name;
        this.location = location;
        this.min_salary = min_salary;
        this.max_salary = max_salary;
        this.posted_at = posted_at;
        this.job_type = job_type;
        this.job_expires = job_expires;
        this.is_bookmarked = is_bookmarked;
    }
    
    public String getMin_experience () {
        return min_experience;
    }
    
    public void setMin_experience (String min_experience) {
        this.min_experience = min_experience;
    }
    
    public String getMax_experience () {
        return max_experience;
    }
    
    public void setMax_experience (String max_experience) {
        this.max_experience = max_experience;
    }
    
    public String getMin_salary () {
        return min_salary;
    }
    
    public void setMin_salary (String min_salary) {
        this.min_salary = min_salary;
    }
    
    public String getMax_salary () {
        return max_salary;
    }
    
    public void setMax_salary (String max_salary) {
        this.max_salary = max_salary;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getJob_title () {
        return job_title;
    }
    
    public void setJob_title (String job_title) {
        this.job_title = job_title;
    }
    
    public String getJob_description () {
        return job_description;
    }
    
    public void setJob_description (String job_description) {
        this.job_description = job_description;
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
    
    public String getPosted_at () {
        return posted_at;
    }
    
    public void setPosted_at (String posted_at) {
        this.posted_at = posted_at;
    }
    
    public String getJob_type () {
        return job_type;
    }
    
    public void setJob_type (String job_type) {
        this.job_type = job_type;
    }
    
    public String getJob_expires () {
        return job_expires;
    }
    
    public void setJob_expires (String job_expires) {
        this.job_expires = job_expires;
    }
    
    public boolean is_bookmarked () {
        return is_bookmarked;
    }
    
    public void setIs_bookmarked (boolean is_bookmarked) {
        this.is_bookmarked = is_bookmarked;
    }
}