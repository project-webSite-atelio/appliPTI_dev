package com.ateliopti.lapplicationpti.model;

public class AlarmeAgression {
    private int id;
    private boolean aAlarmeAgression; // true si le mode homme-mort est activé
    private int agressionDureeDetection; // durée entre chaque sollicitation (en minute(s))
    private int agressionDureeNotification; // durée de la préalarme
    private String agressionTypeNotif; // type de notification de la préalarme
    private String agressionCodeAnnulation; // code pin de la préalarme

    /**
     * @param id
     * @param aAlarmeAgression
     * @param agressionDureeDetection
     * @param agressionDureeNotification
     * @param agressionTypeNotif
     * @param agressionCodeAnnulation
     */
    public AlarmeAgression(int id, boolean aAlarmeAgression, int agressionDureeDetection, int agressionDureeNotification,
                           String agressionTypeNotif, String agressionCodeAnnulation) {
        this.id = id;
        this.aAlarmeAgression = aAlarmeAgression;
        this.agressionDureeDetection = agressionDureeDetection;
        this.agressionDureeNotification = agressionDureeNotification;
        this.agressionTypeNotif = agressionTypeNotif;
        this.agressionCodeAnnulation = agressionCodeAnnulation;
    }

    public AlarmeAgression() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isaAlarmeAgression() {
        return aAlarmeAgression;
    }

    public void setaAlarmeAgression(boolean aAlarmeAgression) {
        this.aAlarmeAgression = aAlarmeAgression;
    }

    public int getAgressionDureeDetection() {
        return agressionDureeDetection;
    }

    public void setAgressionDureeDetection(int agressionDureeDetection) {
        this.agressionDureeDetection = agressionDureeDetection;
    }

    public int getAgressionDureeNotification() {
        return agressionDureeNotification;
    }

    public void setAgressionDureeNotification(int agressionDureeNotification) {
        this.agressionDureeNotification = agressionDureeNotification;
    }

    public String getAgressionTypeNotif() {
        return agressionTypeNotif;
    }

    public void setAgressionTypeNotif(String agressionTypeNotif) {
        this.agressionTypeNotif = agressionTypeNotif;
    }

    public String getAgressionCodeAnnulation() {
        return agressionCodeAnnulation;
    }

    public void setAgressionCodeAnnulation(String agressionCodeAnnulation) {
        this.agressionCodeAnnulation = agressionCodeAnnulation;
    }

    public boolean compares(AlarmeAgression alarmeAgression) {
        return this.aAlarmeAgression == alarmeAgression.aAlarmeAgression &&
                this.agressionDureeDetection == alarmeAgression.agressionDureeDetection &&
                this.agressionDureeNotification == alarmeAgression.agressionDureeNotification &&
                this.agressionTypeNotif.equals(alarmeAgression.agressionTypeNotif) &&
                this.agressionCodeAnnulation.equals(alarmeAgression.agressionCodeAnnulation);
    }

    @Override
    public String toString() {
        return "AlarmeAgression{" +
                "id=" + id +
                ", aAlarmeAgression=" + aAlarmeAgression +
                ", agressionDureeDetection=" + agressionDureeDetection +
                ", agressionDureeNotification=" + agressionDureeNotification +
                ", agressionTypeNotif='" + agressionTypeNotif + '\'' +
                ", agressionCodeAnnulation='" + agressionCodeAnnulation + '\'' +
                '}';
    }

}
