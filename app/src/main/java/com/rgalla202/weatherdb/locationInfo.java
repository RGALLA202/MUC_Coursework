package com.rgalla202.weatherdb;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rgall on 27/11/2016.
 */

public class locationInfo implements Serializable{
    // *********************************************
// Declare variables etc.
// *********************************************

    private int locationid;
    private String place;
    private String url;

    private static final long serialVersionUID = 0L;



// *********************************************
// Declare getters and setters etc.
// *********************************************


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getLocationid() {
        return locationid;
    }

    public void setLocationid(int locationid) {
        this.locationid = locationid;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
