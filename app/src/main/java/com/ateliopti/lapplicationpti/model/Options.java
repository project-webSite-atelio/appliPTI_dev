package com.ateliopti.lapplicationpti.model;

public class Options {
    private int id;
    private boolean aTrajet; // true si le mode extérieur est activé
    private boolean aBatiment; // true si le mode intérieur est activé
    private boolean aIzsr; // true si le mode zone sans réseau est activé
    private int puissanceSonore; // niveau de volume sonore de la préalarme
    private boolean localisationSonore; // true si la localisation sonore post alarme est activée
    private String logo; // nom du fournisseur pour le logo
    private boolean activationPTIAutomatique;
    private boolean sollicitation;
    private boolean historiqueSupervision;
    private boolean aTutoriel;
    private int scenarioExceptionnel;
    private boolean localisationAudio;
    private boolean aRappelUtilisation;
    private int dureeRappelUtilisation;
    private boolean aNotation;
    private boolean aBroadcastInstavox;
    private boolean aInterfaceAlternative; // si true, nouvelle interface
    private boolean aScenarioJourNuit;
    private String heureDebutScenarioJourNuit;
    private String heureFinScenarioJourNuit;

    public Options(int id, boolean aTrajet, boolean aBatiment, boolean aIzsr, int puissanceSonore, boolean localisationSonore, String logo,
                   boolean activationPTIAutomatique, boolean sollicitation, boolean historiqueSupervision, boolean aTutoriel, int scenarioExceptionnel,
                   boolean localisationAudio, boolean aRappelUtilisation, int dureeRappelUtilisation, boolean aNotation, boolean aBroadcastInstavox, boolean aInterfaceAlternative, boolean aScenarioJourNuit, String heureDebutScenarioJourNuit, String heureFinScenarioJourNuit) {
        this.id = id;
        this.aTrajet = aTrajet;
        this.aBatiment = aBatiment;
        this.aIzsr = aIzsr;
        this.puissanceSonore = puissanceSonore;
        this.localisationSonore = localisationSonore;
        this.logo = logo;
        this.activationPTIAutomatique = activationPTIAutomatique;
        this.sollicitation = sollicitation;
        this.historiqueSupervision = historiqueSupervision;
        this.aTutoriel = aTutoriel;
        this.scenarioExceptionnel = scenarioExceptionnel;
        this.localisationAudio = localisationAudio;
        this.aRappelUtilisation = aRappelUtilisation;
        this.dureeRappelUtilisation = dureeRappelUtilisation;
        this.aNotation = aNotation;
        this.aBroadcastInstavox = aBroadcastInstavox;
        this.aInterfaceAlternative = aInterfaceAlternative;
        this.aScenarioJourNuit = aScenarioJourNuit;
        this.heureDebutScenarioJourNuit = heureDebutScenarioJourNuit;
        this.heureFinScenarioJourNuit = heureFinScenarioJourNuit;
    }

    public Options() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isaTrajet() {
        return aTrajet;
    }

    public void setaTrajet(boolean aTrajet) {
        this.aTrajet = aTrajet;
    }

    public boolean isaBatiment() {
        return aBatiment;
    }

    public void setaBatiment(boolean aBatiment) {
        this.aBatiment = aBatiment;
    }

    public boolean isaIzsr() {
        return aIzsr;
    }

    public void setaIzsr(boolean aIzsr) {
        this.aIzsr = aIzsr;
    }

    public int getPuissanceSonore() {
        return puissanceSonore;
    }

    public void setPuissanceSonore(int puissanceSonore) {
        this.puissanceSonore = puissanceSonore;
    }

    public boolean isLocalisationSonore() {
        return localisationSonore;
    }

    public void setLocalisationSonore(boolean localisationSonore) {
        this.localisationSonore = localisationSonore;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public boolean isActivationPTIAutomatique() {
        return activationPTIAutomatique;
    }

    public void setActivationPTIAutomatique(boolean activationPTIAutomatique) {
        this.activationPTIAutomatique = activationPTIAutomatique;
    }

    public boolean isSollicitation() {
        return sollicitation;
    }

    public void setSollicitation(boolean sollicitation) {
        this.sollicitation = sollicitation;
    }

    public boolean isHistoriqueSupervision() {
        return historiqueSupervision;
    }

    public void setHistoriqueSupervision(boolean historiqueSupervision) {
        this.historiqueSupervision = historiqueSupervision;
    }

    public boolean isaTutoriel() {
        return aTutoriel;
    }

    public void setaTutoriel(boolean aTutoriel) {
        this.aTutoriel = aTutoriel;
    }

    public int getScenarioExceptionnel() {
        return scenarioExceptionnel;
    }

    public void setScenarioExceptionnel(int scenarioExceptionnel) {
        this.scenarioExceptionnel = scenarioExceptionnel;
    }

    public boolean isLocalisationAudio() {
        return localisationAudio;
    }

    public void setLocalisationAudio(boolean localisationAudio) {
        this.localisationAudio = localisationAudio;
    }

    public boolean isaRappelUtilisation() {
        return aRappelUtilisation;
    }

    public void setaRappelUtilisation(boolean aRappelUtilisation) {
        this.aRappelUtilisation = aRappelUtilisation;
    }

    public int getDureeRappelUtilisation() {
        return dureeRappelUtilisation;
    }

    public void setDureeRappelUtilisation(int dureeRappelUtilisation) {
        this.dureeRappelUtilisation = dureeRappelUtilisation;
    }

    public boolean isaNotation() {
        return aNotation;
    }

    public void setaNotation(boolean aNotation) {
        this.aNotation = aNotation;
    }

    public boolean isaBroadcastInstavox() {
        return aBroadcastInstavox;
    }

    public void setaBroadcastInstavox(boolean aBroadcastInstavox) {
        this.aBroadcastInstavox = aBroadcastInstavox;
    }

    public boolean isaInterfaceAlternative() {
        return aInterfaceAlternative;
    }

    public void setaInterfaceAlternative(boolean aInterfaceAlternative) {
        this.aInterfaceAlternative = aInterfaceAlternative;
    }

    public boolean isaScenarioJourNuit() {
        return aScenarioJourNuit;
    }

    public void setaScenarioJourNuit(boolean aScenarioJourNuit) {
        this.aScenarioJourNuit = aScenarioJourNuit;
    }

    public String getHeureDebutScenarioJourNuit() {
        return heureDebutScenarioJourNuit;
    }

    public void setHeureDebutScenarioJourNuit(String heureDebutScenarioJourNuit) {
        this.heureDebutScenarioJourNuit = heureDebutScenarioJourNuit;
    }

    public String getHeureFinScenarioJourNuit() {
        return heureFinScenarioJourNuit;
    }

    public void setHeureFinScenarioJourNuit(String heureFinScenarioJourNuit) {
        this.heureFinScenarioJourNuit = heureFinScenarioJourNuit;
    }

    public boolean compares(Options options) {

      //  boolean comparaisonJourNuit;
        boolean comparaison = (this.aTrajet ? 1 : 0) == (options.aTrajet ? 1 : 0) &&
                this.aBatiment == options.aBatiment &&
                this.aIzsr == options.aIzsr &&
                this.puissanceSonore == options.puissanceSonore &&
                this.localisationSonore == options.localisationSonore &&
                this.logo.equals(options.logo) &&
                this.activationPTIAutomatique == options.activationPTIAutomatique &&
                this.sollicitation == options.sollicitation &&
                this.historiqueSupervision == options.historiqueSupervision &&
                this.aTutoriel == options.aTutoriel &&
                this.scenarioExceptionnel == options.scenarioExceptionnel &&
                //    this.localisationAudio == options.localisationAudio && (interface alternatif + super)
                this.aRappelUtilisation == options.aRappelUtilisation &&
                this.dureeRappelUtilisation == options.dureeRappelUtilisation &&
                this.aNotation == options.aNotation &&
                this.aBroadcastInstavox == options.aBroadcastInstavox &&
                this.aInterfaceAlternative == options.aInterfaceAlternative &&
                this.aScenarioJourNuit == options.aScenarioJourNuit;

     //   comparaisonJourNuit = comparaison;

        if (this.heureDebutScenarioJourNuit != null && this.heureFinScenarioJourNuit != null) {
            comparaison = comparaison && this.heureDebutScenarioJourNuit.equals(options.heureDebutScenarioJourNuit) &&
                    this.heureFinScenarioJourNuit.equals(options.heureFinScenarioJourNuit);

        }

        /*&&
                this.heureDebutScenarioJourNuit.equals(options.heureDebutScenarioJourNuit) &&
                this.heureFinScenarioJourNuit.equals(options.heureFinScenarioJourNuit);

                */


        // Interface normale
        // Compare uniquement si c'est une interface alternative
        if (!options.isaInterfaceAlternative()) {
            comparaison = comparaison && this.localisationAudio == options.localisationAudio;
        }

        return comparaison;
    }


    @Override
    public String toString() {
        return "Options{" +
                "id=" + id +
                ", aTrajet=" + aTrajet +
                ", aBatiment=" + aBatiment +
                ", aIzsr=" + aIzsr +
                ", puissanceSonore=" + puissanceSonore +
                ", localisationSonore=" + localisationSonore +
                ", logo='" + logo + '\'' +
                ", activationPTIAutomatique=" + activationPTIAutomatique +
                ", sollicitation=" + sollicitation +
                ", historiqueSupervision=" + historiqueSupervision +
                ", aTutoriel=" + aTutoriel +
                ", scenarioExceptionnel=" + scenarioExceptionnel +
                ", localisationAudio=" + localisationAudio +
                ", aRappelUtilisation=" + aRappelUtilisation +
                ", dureeRappelUtilisation=" + dureeRappelUtilisation +
                ", aNotation=" + aNotation +
                ", aBroadcastInstavox=" + aBroadcastInstavox +
                ", aInterfaceAlternative=" + aInterfaceAlternative +
                ", aScenarioJourNuit=" + aScenarioJourNuit +
                ", heureDebutScenarioJourNuit='" + heureDebutScenarioJourNuit + '\'' +
                ", heureFinScenarioJourNuit='" + heureFinScenarioJourNuit + '\'' +
                '}';
    }

}
