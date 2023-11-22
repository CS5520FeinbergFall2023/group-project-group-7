package com.NEU23FallGroup7.jobtrackpro.Models;

import java.util.Date;

public class Jobs {
    private String description;
    private String salary_is_predicted;
    private String contract_type;
    private Date created;
    private String id;
    private String redirect_url;
    private Location location;
    private Category category;
    private String title;
    private Company company;
    private String contract_time;
    private int salary_min;
    private int salary_max;
    private double longitude;
    private double latitude;
    private boolean favourite;

    public Jobs(  String description, String salary_is_predicted, String contract_type, Date created, String id, String redirect_url, Location location, Category category, String title, Company company, String contract_time, int salary_min, int salary_max, double longitude, double latitude, boolean favourite) {

        this.description = description;
        this.salary_is_predicted = salary_is_predicted;
        this.contract_type = contract_type;
        this.created = created;
        this.id = id;
        this.redirect_url = redirect_url;
        this.location = location;
        this.category = category;
        this.title = title;
        this.company = company;
        this.contract_time = contract_time;
        this.salary_min = salary_min;
        this.salary_max = salary_max;
        this.longitude = longitude;
        this.latitude = latitude;
        this.favourite = favourite;
    }

    public String getDescription() {
        return description;
    }

    public String getSalary_is_predicted() {
        return salary_is_predicted;
    }

    public String getContract_type() {
        return contract_type;
    }

    public Date getCreated() {
        return created;
    }

    public String getId() {
        return id;
    }

    public String getRedirect_url() {
        return redirect_url;
    }

    public Location getLocation() {
        return location;
    }

    public Category getCategory() {
        return category;
    }

    public String getTitle() {
        return title;
    }

    public Company getCompany() {
        return company;
    }

    public String getContract_time() {
        return contract_time;
    }

    public int getSalary_min() {
        return salary_min;
    }

    public int getSalary_max() {
        return salary_max;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public boolean isFavourite() {
        return favourite;
    }


    public void setFavourite(boolean favourite){
        this.favourite = favourite;
    }
}



