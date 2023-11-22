package com.NEU23FallGroup7.jobtrackpro.Models;

import java.util.ArrayList;

public class Location {
    public ArrayList<String> area;
    public String display_name;

    public Location(ArrayList<String> area, String display_name) {
        this.area = area;
        this.display_name = display_name;
    }
}
