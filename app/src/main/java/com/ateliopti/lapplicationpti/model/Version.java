package com.ateliopti.lapplicationpti.model;

public class Version {
    private int id;
    private int version; // numéro de la version de l'application (défini sur strings.xml)
    private boolean obsolete; // true si la version n'est pas à jour à la version définie par le serveur
    private boolean rappelNotation; // si true, à la fin de la surveillance, proposition de notation


    public Version() {

    }

    public Version(int id, int version, boolean obsolete, boolean rappelNotation) {
        this.id = id;
        this.version = version;
        this.obsolete = obsolete;
        this.rappelNotation = rappelNotation;
    }

    // Getter / setter (id)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter / setter (version)
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    // Getter / setter (obsolete)
    public boolean isObsolete() {
        return obsolete;
    }

    public void setObsolete(boolean obsolete) {
        this.obsolete = obsolete;
    }

    // Getter / setter (rappelNotation)
    public boolean isRappelNotation() {
        return rappelNotation;
    }

    public void setRappelNotation(boolean rappelNotation) {
        this.rappelNotation = rappelNotation;
    }


    @Override
    public String toString() {
        return "Version{" +
                "id=" + id +
                ", version=" + version +
                ", obsolete=" + obsolete +
                ", rappelNotation=" + rappelNotation +

                '}';
    }

}
