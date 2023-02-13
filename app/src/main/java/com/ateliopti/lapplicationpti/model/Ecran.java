package com.ateliopti.lapplicationpti.model;


public class Ecran {
    private int id;
    private boolean estActif;

    public Ecran() {

    }

    public Ecran(int id, boolean estActif) {
        this.id = id;
        this.estActif = estActif;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isEstActif() {
        return estActif;
    }

    public void setEstActif(boolean estActif) {
        this.estActif = estActif;
    }

    @Override
    public String toString() {
        return "Ecran{" +
                "id=" + id +
                ", estActif=" + estActif +
                '}';
    }

}
