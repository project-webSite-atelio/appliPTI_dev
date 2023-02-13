package com.ateliopti.lapplicationpti.model;

public class AlarmeSOS {
    private int id;
    private String sosDeclenchementMouvement; // nom du déclenchement spécial renseigné
    private String sosDeclenchementBluetooth; // nom de l'appareil Bluetooth déclencheur d'alerte
    private int sosDureeNotification; // durée de la préalarme
    private String sosTypeNotif; // type de notification de la préalarme
    private String sosCodeAnnulation; // code pin de la préalarme

    public AlarmeSOS(int id, String sosDeclenchementMouvement, String sosDeclenchementBluetooth, int sosDureeNotification, String sosTypeNotif, String sosCodeAnnulation) {
        this.id = id;
        this.sosDeclenchementMouvement = sosDeclenchementMouvement;
        this.sosDeclenchementBluetooth = sosDeclenchementBluetooth;
        this.sosDureeNotification = sosDureeNotification;
        this.sosTypeNotif = sosTypeNotif;
        this.sosCodeAnnulation = sosCodeAnnulation;
    }

    public AlarmeSOS() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSosDeclenchementMouvement() {
        return sosDeclenchementMouvement;
    }

    public void setSosDeclenchementMouvement(String sosDeclenchementMouvement) {
        this.sosDeclenchementMouvement = sosDeclenchementMouvement;
    }

    public String getSosDeclenchementBluetooth() {
        return sosDeclenchementBluetooth;
    }

    public void setSosDeclenchementBluetooth(String sosDeclenchementBluetooth) {
        this.sosDeclenchementBluetooth = sosDeclenchementBluetooth;
    }

    public int getSosDureeNotification() {
        return sosDureeNotification;
    }

    public void setSosDureeNotification(int sosDureeNotification) {
        this.sosDureeNotification = sosDureeNotification;
    }

    public String getSosTypeNotif() {
        return sosTypeNotif;
    }

    public void setSosTypeNotif(String sosTypeNotif) {
        this.sosTypeNotif = sosTypeNotif;
    }

    public String getSosCodeAnnulation() {
        return sosCodeAnnulation;
    }

    public void setSosCodeAnnulation(String sosCodeAnnulation) {
        this.sosCodeAnnulation = sosCodeAnnulation;
    }

    public boolean compares(AlarmeSOS alarmeSOS) {
        return this.sosDeclenchementMouvement.equals(alarmeSOS.sosDeclenchementMouvement) &&
                this.sosDeclenchementBluetooth.equals(alarmeSOS.sosDeclenchementBluetooth) &&
                this.sosDureeNotification == alarmeSOS.sosDureeNotification &&
                this.sosTypeNotif.equals(alarmeSOS.sosTypeNotif) &&
                this.sosCodeAnnulation.equals(alarmeSOS.sosCodeAnnulation);
    }

    @Override
    public String toString() {
        return "AlarmeSOS{" +
                "id=" + id +
                ", sosDeclenchementMouvement='" + sosDeclenchementMouvement + '\'' +
                ", sosDeclenchementBluetooth='" + sosDeclenchementBluetooth + '\'' +
                ", sosDureeNotification=" + sosDureeNotification +
                ", sosTypeNotif='" + sosTypeNotif + '\'' +
                ", sosCodeAnnulation='" + sosCodeAnnulation + '\'' +
                '}';
    }

}
