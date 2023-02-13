package com.ateliopti.lapplicationpti.model;

public class TempLocalisation {
    private int id;
    private String date;
    private String latitude;
    private String longitude;
    private String site;

    public TempLocalisation(){

    }

    public TempLocalisation(int id, String date, String latitude, String longitude, String site) {
        this.id = id;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.site = site;
    }



    public TempLocalisation(String date, String latitude, String longitude, String site) {
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.site = site;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    @Override
    public String toString() {
        return "TempLocalisation{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", site='" + site + '\'' +
                '}';
    }

}
