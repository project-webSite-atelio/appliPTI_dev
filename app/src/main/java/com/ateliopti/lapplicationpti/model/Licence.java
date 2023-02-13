package com.ateliopti.lapplicationpti.model;

public class Licence {
    private int id;
    private boolean premiereConfiguration;

    public Licence(int id, boolean premiereConfiguration){
        this.id = id;
        this.premiereConfiguration = premiereConfiguration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isPremiereConfiguration() {
        return premiereConfiguration;
    }

    public void setPremiereConfiguration(boolean premiereConfiguration) {
        this.premiereConfiguration = premiereConfiguration;
    }

    @Override
    public String toString() {
        return "Licence{" +
                "id=" + id +
                ", premiereConfiguration=" + premiereConfiguration +
                '}';
    }

}
