package com.NEU23FallGroup7.jobtrackpro;

import java.util.Date;

public class Resumes {
    String name, url, date;

    public Resumes(String name, String url, String date) {
        this.name = name;
        this.url = url;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Resumes() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
