package com.ateliopti.lapplicationpti.model;

public class Trajet {
    private int id;
    private int dureeRechercheGps; // durée de recherche active d'une géolocalisation
    private int dureePauseGps; // durée d'arrêt de recherche GPS
    private int precisionGps; // précision minimale

    public Trajet(int id, int dureeRechercheGps, int dureePauseGps, int precisionGps) {
        this.id = id;
        this.dureeRechercheGps = dureeRechercheGps;
        this.dureePauseGps = dureePauseGps;
        this.precisionGps = precisionGps;
    }

    public Trajet() {

    }

    // Getter / setter (id)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter / setter (dureeRechercheGps)
    public int getDureeRechercheGps() {
        return dureeRechercheGps;
    }

    public void setDureeRechercheGps(int dureeRechercheGps) {
        this.dureeRechercheGps = dureeRechercheGps;
    }

    // Getter / setter (dureePauseGps)
    public int getDureePauseGps() {
        return dureePauseGps;
    }

    public void setDureePauseGps(int dureePauseGps) {
        this.dureePauseGps = dureePauseGps;
    }

    // Getter / setter (precisionGps)
    public int getPrecisionGps() {
        return precisionGps;
    }

    public void setPrecisionGps(int precisionGps) {
        this.precisionGps = precisionGps;
    }

    // Comparaison des valeurs actuelles à celles du serveur afin de déterminer si c'est une nouvelle configuration
    public boolean compares(Trajet trajet) {
        return this.dureeRechercheGps == trajet.dureeRechercheGps &&
                this.dureePauseGps == trajet.dureePauseGps &&
                this.precisionGps == trajet.precisionGps;
    }

    @Override
    public String toString() {
        return "Trajet{" +
                "id=" + id +
                ", dureeRechercheGps=" + dureeRechercheGps +
                ", dureePauseGps=" + dureePauseGps +
                ", precisionGps=" + precisionGps +
                '}';
    }
}
