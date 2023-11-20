package com.NEU23FallGroup7.jobtrackpro;

import java.sql.Time;

public class Application {
    String job_id;
    String title;
    String company;
    String city;
    String state;
    String resume_id;
    String resume_name;
    Time applied_date;
    String status;

    public Application(String job_id, String title, String company, String city, String state, String resume_id, String resume_name, Time applied_date, String status) {
        this.job_id = job_id;
        this.title = title;
        this.company = company;
        this.city = city;
        this.state = state;
        this.resume_id = resume_id;
        this.resume_name = resume_name;
        this.applied_date = applied_date;
        this.status = status;

    }

    public String getJob_id() {
        return job_id;
    }

    public String getTitle() {
        return title;
    }

    public String getCompany() {
        return company;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getResume_id() {
        return resume_id;
    }

    public String getResume_name() {
        return resume_name;
    }

    public Time getApplied_date() {
        return applied_date;
    }

    public String getStatus() {
        return status;
    }


    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setResume_id(String resume_id) {
        this.resume_id = resume_id;
    }

    public void setResume_name(String resume_name) {
        this.resume_name = resume_name;
    }

    public void setApplied_date(Time applied_date) {
        this.applied_date = applied_date;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
