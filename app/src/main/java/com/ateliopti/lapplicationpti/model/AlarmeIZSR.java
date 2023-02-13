package com.ateliopti.lapplicationpti.model;

public class AlarmeIZSR {
    private int id;
    private int izsrDureeNotification; // durée de la préalarme
    private String izsrTypeNotif; // type de notification

    public AlarmeIZSR(int id, int izsrDureeNotification, String izsrTypeNotif) {
        this.id = id;
        this.izsrDureeNotification = izsrDureeNotification;
        this.izsrTypeNotif = izsrTypeNotif;
    }

    public AlarmeIZSR() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIzsrDureeNotification() {
        return izsrDureeNotification;
    }

    public void setIzsrDureeNotification(int izsrDureeNotification) {
        this.izsrDureeNotification = izsrDureeNotification;
    }

    public String getIzsrTypeNotif() {
        return izsrTypeNotif;
    }

    public void setIzsrTypeNotif(String izsrTypeNotif) {
        this.izsrTypeNotif = izsrTypeNotif;
    }

    public boolean compares(AlarmeIZSR alarmeIZSR) {
        return this.izsrDureeNotification == alarmeIZSR.izsrDureeNotification &&
                this.izsrTypeNotif.equals(alarmeIZSR.izsrTypeNotif);
    }


    @Override
    public String toString() {
        return "AlarmeIZSR{" +
                "id=" + id +
                ", izsrDureeNotification=" + izsrDureeNotification +
                ", izsrTypeNotif='" + izsrTypeNotif + '\'' +
                '}';
    }

}
