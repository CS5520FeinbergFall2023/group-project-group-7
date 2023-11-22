package com.NEU23FallGroup7.jobtrackpro.Models;

public class Category {
    public String tag;
    public String label;

    public Category(String tag, String label) {
        this.tag = tag;
        this.label = label;
    }

    public String getTag() {
        return tag;
    }

    public String getLabel() {
        return label;
    }
}
