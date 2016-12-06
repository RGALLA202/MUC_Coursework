package com.rgalla202.weatherdb;

import android.media.Image;
import android.widget.ImageView;

/**
 * Created by rgall on 09/11/2016.
 */

public class RSSItem {
    String title;
    String link;
    String description;
    String pubDate;
    String geoRSS;

    //the following variables are substrings of title
    String day;
    String weather;
    String minTemp;
    String MaxTemp;

    //image ResID
    int resID;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getGeoRSS() {
        return geoRSS;
    }

    public void setGeoRSS(String geoRSS) {
        this.geoRSS = geoRSS;
    }
    //=========================================================
    //below are the getters and setters for substrings of title
    //=========================================================
    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(String minTemp) {
        this.minTemp = minTemp;
    }

    public String getMaxTemp() {
        return MaxTemp;
    }

    public void setMaxTemp(String maxTemp) {
        MaxTemp = maxTemp;
    }
//====================================

    public int getResID() {
        return resID;
    }

    public void setResID(int resID) {
        this.resID = resID;
    }
}

