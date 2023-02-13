package com.ateliopti.lapplicationpti.model;

public class AlarmeAM {
    private int id;
    private boolean aAlarmeAm; // true si le mode absence de mouvement est actif
    private int amDureeDetection; // durée de la détection d'absence de mouvement
    private int amDureeNotification; // durée de la préalarme
    private String amTypeNotif; // type de notification de la préalarme
    private boolean amAnnulation; // true si l'annulation de la préalarme par mouvement est disponible

    public AlarmeAM(int id, boolean aAlarmeAm, int amDureeDetection, int amDureeNotification, String amTypeNotif, boolean amAnnulation) {
        this.id = id;
        this.aAlarmeAm = aAlarmeAm;
        this.amDureeDetection = amDureeDetection;
        this.amDureeNotification = amDureeNotification;
        this.amTypeNotif = amTypeNotif;
        this.amAnnulation = amAnnulation;
    }

    public AlarmeAM() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isaAlarmeAm() {
        return aAlarmeAm;
    }

    public void setaAlarmeAm(boolean aAlarmeAm) {
        this.aAlarmeAm = aAlarmeAm;
    }

    public int getAmDureeDetection() {
        return amDureeDetection;
    }

    public void setAmDureeDetection(int amDureeDetection) {
        this.amDureeDetection = amDureeDetection;
    }

    public int getAmDureeNotification() {
        return amDureeNotification;
    }

    public void setAmDureeNotification(int amDureeNotification) {
        this.amDureeNotification = amDureeNotification;
    }

    public String getAmTypeNotif() {
        return amTypeNotif;
    }

    public void setAmTypeNotif(String amTypeNotif) {
        this.amTypeNotif = amTypeNotif;
    }

    public boolean isAmAnnulation() {
        return amAnnulation;
    }

    public void setAmAnnulation(boolean amAnnulation) {
        this.amAnnulation = amAnnulation;
    }

    public boolean compares(AlarmeAM alarmeAM) {
        return this.aAlarmeAm == alarmeAM.aAlarmeAm &&
                this.amDureeDetection == alarmeAM.amDureeDetection &&
                this.amDureeNotification == alarmeAM.amDureeNotification &&
                this.amTypeNotif.equals(alarmeAM.amTypeNotif) &&
                this.amAnnulation == alarmeAM.amAnnulation;
    }

    @Override
    public String toString() {
        return "AlarmeAM{" +
                "id=" + id +
                ", aAlarmeAm=" + aAlarmeAm +
                ", amDureeDetection=" + amDureeDetection +
                ", amDureeNotification=" + amDureeNotification +
                ", amTypeNotif='" + amTypeNotif + '\'' +
                ", amAnnulation=" + amAnnulation +
                '}';
    }

}
