package com.ateliopti.lapplicationpti.model;

public class AlarmePV {
    private int id;
    private boolean aAlarmePv; // true si la perte de verticalité est disponible
    private int pvDureeDetection; // durée de la détection de perte de verticalité
    private int pvAngleDetection; // angle de détection
    private int pvDureeNotification; // durée de la préalarme
    private String pvTypeNotif; // type de notification de la préalarme
    private boolean pvAnnulation; // true si l'annulation de la préalarme par mouvement est disponible

    public AlarmePV(int id, boolean aAlarmePv, int pvDureeDetection, int pvAngleDetection, int pvDureeNotification, String pvTypeNotif, boolean pvAnnulation) {
        this.id = id;
        this.aAlarmePv = aAlarmePv;
        this.pvDureeDetection = pvDureeDetection;
        this.pvAngleDetection = pvAngleDetection;
        this.pvDureeNotification = pvDureeNotification;
        this.pvTypeNotif = pvTypeNotif;
        this.pvAnnulation = pvAnnulation;
    }

    public AlarmePV() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isaAlarmePv() {
        return aAlarmePv;
    }

    public void setaAlarmePv(boolean aAlarmePv) {
        this.aAlarmePv = aAlarmePv;
    }

    public int getPvDureeDetection() {
        return pvDureeDetection;
    }

    public void setPvDureeDetection(int pvDureeDetection) {
        this.pvDureeDetection = pvDureeDetection;
    }

    public int getPvAngleDetection() {
        return pvAngleDetection;
    }

    public void setPvAngleDetection(int pvAngleDetection) {
        this.pvAngleDetection = pvAngleDetection;
    }

    public int getPvDureeNotification() {
        return pvDureeNotification;
    }

    public void setPvDureeNotification(int pvDureeNotification) {
        this.pvDureeNotification = pvDureeNotification;
    }

    public String getPvTypeNotif() {
        return pvTypeNotif;
    }

    public void setPvTypeNotif(String pvTypeNotif) {
        this.pvTypeNotif = pvTypeNotif;
    }

    public boolean isPvAnnulation() {
        return pvAnnulation;
    }

    public void setPvAnnulation(boolean pvAnnulation) {
        this.pvAnnulation = pvAnnulation;
    }

    public boolean compares(AlarmePV alarmePV) {
        return this.aAlarmePv == alarmePV.aAlarmePv &&
                this.pvDureeDetection == alarmePV.pvDureeDetection &&
                this.pvAngleDetection == alarmePV.pvAngleDetection &&
                this.pvDureeNotification == alarmePV.pvDureeNotification &&
                this.pvTypeNotif.equals(alarmePV.pvTypeNotif) &&
                this.pvAnnulation == alarmePV.pvAnnulation;
    }


    @Override
    public String toString() {
        return "AlarmePV{" +
                "id=" + id +
                ", aAlarmePv=" + aAlarmePv +
                ", pvDureeDetection=" + pvDureeDetection +
                ", pvAngleDetection=" + pvAngleDetection +
                ", pvDureeNotification=" + pvDureeNotification +
                ", pvTypeNotif='" + pvTypeNotif + '\'' +
                ", pvAnnulation=" + pvAnnulation +
                '}';
    }
}
