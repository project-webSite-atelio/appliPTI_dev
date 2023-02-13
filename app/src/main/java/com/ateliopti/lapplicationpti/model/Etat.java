package com.ateliopti.lapplicationpti.model;

public class Etat {
    private int id;
    private boolean activation; // Activation du PTI
    private String derniereDesactivation; // Si plus de réseau
    private boolean finCharge = false;

    public Etat(){

    }

    public Etat(int id, boolean activation, String derniereDesactivation, boolean finCharge) {
        this.id = id;
        this.activation = activation;
        this.derniereDesactivation = derniereDesactivation;
        this.finCharge = finCharge;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isActivation() {
        return activation;
    }

    public void setActivation(boolean activation) {
        this.activation = activation;
    }

    public String getDerniereDesactivation() {
        return derniereDesactivation;
    }

    public void setDerniereDesactivation(String derniereDesactivation) {
        this.derniereDesactivation = derniereDesactivation;
    }

    public boolean isFinCharge() {
        return finCharge;
    }

    public void setFinCharge(boolean finCharge) {
        this.finCharge = finCharge;
    }


    @Override
    public String toString() {
        return "Etat{" +
                "id=" + id +
                ", activation=" + activation +
                ", derniereDesactivation='" + derniereDesactivation + '\'' +
                ", finCharge=" + finCharge +
                '}';
    }

}
