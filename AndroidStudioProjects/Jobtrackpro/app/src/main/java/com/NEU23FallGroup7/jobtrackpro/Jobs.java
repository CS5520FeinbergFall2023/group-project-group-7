package com.NEU23FallGroup7.jobtrackpro;

import java.sql.Time;

public class Jobs {
    String job_id;
    String title;
    String company;
    String city;
    String state;
    String country;
    Time date;
    String salary;
    String currency;
    String description;
    String other;
    String url;
    String remote;
    String full_time;


    public Jobs(String job_id, String title, String company, String city, String state, String country, Time date, String salary, String currency, String description, String remote, String full_time, String url) {
        this.job_id = job_id;
        this.title = title;
        this.company = company;
        this.city = city;
        this.state = state;
        this.country = country;
        this.date = date;
        this.salary = salary;
        this.currency = currency;
        this.description = description;
        this.url = url;
        this.remote = remote;
        this.full_time = full_time;
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

    public String getCountry() {
        return country;
    }

    public Time getDate() {
        return date;
    }

    public String getSalary() {
        return salary;
    }

    public String getCurrency() {
        return currency;
    }

    public String getDescription() {
        return description;
    }

    public String getOther() {
        return other;
    }

    public String getUrl() {
        return url;
    }
    public String getRemote() {
        return remote;
    }
    public String getFull_time() {
        return full_time;
    }
}



