package com.ateliopti.lapplicationpti.model;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class Configuration {

    private int id;
    private final int idGroup;

    @SerializedName("autorisation")
    private final boolean autorisation; // Selon configuration TLG PTI - ne pas autoriser l'activation si le nombre permis d'appareil est dépassé

    @SerializedName("derniereVersion")
    private final int derniereVersion;

    @SerializedName("aTrajet")
    private final boolean aTrajet;

    @SerializedName("aBatiment")
    private final boolean aBatiment;

    @SerializedName("aIzsr")
    private final boolean aIzsr;

    @SerializedName("dureeRechercheGps")
    private final int dureeRechercheGps;

    @SerializedName("dureePauseGps")
    private final int dureePauseGps;

    @SerializedName("precisionGps")
    private final int precisionGps;

    @SerializedName("sosDeclenchementMouvement")
    private final String sosDeclenchementMouvement;

    @SerializedName("sosDeclenchementBluetooth")
    private final String sosDeclenchementBluetooth;

    @SerializedName("sosDureeNotification")
    private final int sosDureeNotification;

    @SerializedName("sosTypeNotif")
    private final String sosTypeNotif;

    @SerializedName("sosCodeAnnulation")
    private final String sosCodeAnnulation;

    @SerializedName("aAlarmePv")
    private final boolean aAlarmePv;

    @SerializedName("pvDureeDetection")
    private final int pvDureeDetection;

    @SerializedName("pvAngleDetection")
    private final int pvAngleDetection;

    @SerializedName("pvDureeNotification")
    private final int pvDureeNotification;

    @SerializedName("pvTypeNotif")
    private final String pvTypeNotif;

    @SerializedName("pvAnnulation")
    private final boolean pvAnnulation;

    @SerializedName("aAlarmeAm")
    private final boolean aAlarmeAm;

    @SerializedName("amDureeDetection")
    private final int amDureeDetection;

    @SerializedName("amDureeNotification")
    private final int amDureeNotification;

    @SerializedName("amTypeNotif")
    private final String amTypeNotif;

    @SerializedName("amAnnulation")
    private final boolean amAnnulation;

    @SerializedName("aAlarmeAgression")
    private final boolean aAlarmeAgression;

    @SerializedName("agressionDureeDetection")
    private final int agressionDureeDetection;

    @SerializedName("agressionDureeNotification")
    private final int agressionDureeNotification;

    @SerializedName("agressionTypeNotif")
    private final String agressionTypeNotif;

    @SerializedName("agressionCodeAnnulation")
    private final String agressionCodeAnnulation;

    @SerializedName("izsrDureeNotification")
    private final int izsrDureeNotification;

    @SerializedName("izsrTypeNotif")
    private final String izsrTypeNotif;

    @SerializedName("puissanceSonore")
    private final int puissanceSonore;

    @SerializedName("localisationSonore")
    private final boolean localisationSonore;

    @SerializedName("logo")
    private final String logo;

    @SerializedName("activationPTIAutomatique")
    private final boolean activationPTIAutomatique;

    @SerializedName("sollicitation")
    private final boolean sollicitation;

    @SerializedName("historiqueSupervision")
    private final boolean historiqueSupervision;

    @SerializedName("aTutoriel")
    private final boolean aTutoriel;

    @SerializedName("scenarioExceptionnel")
    private final int scenarioExceptionnel;

    @SerializedName("localisationAudio")
    private final boolean localisationAudio;

    @SerializedName("aRappelUtilisation")
    private final boolean aRappelUtilisation;

    @SerializedName("dureeRappelUtilisation")
    private final int dureeRappelUtilisation;

    private boolean aNotation;

    @SerializedName("aBroadcastInstavox")
    private boolean aBroadcastInstavox;

    @SerializedName("aInterfaceAlternative")
    private boolean aInterfaceAlternative;

    @SerializedName("aScenarioJourNuit")
    private boolean aScenarioJourNuit;

    @SerializedName("heureDebutScenarioJourNuit")
    private String heureDebutScenarioJourNuit;

    @SerializedName("heureFinScenarioJourNuit")
    private String heureFinScenarioJourNuit;

    @SerializedName("localisations")
    private final String[] localisations;

    public Configuration(int id, int idGroup, boolean autorisation, int derniereVersion, boolean aTrajet, boolean aBatiment, boolean aIzsr, int dureeRechercheGps,
                         int dureePauseGps, int precisionGps, String sosDeclenchementMouvement, String sosDeclenchementBluetooth, int sosDureeNotification,
                         String sosTypeNotif, String sosCodeAnnulation, boolean aAlarmePv, int pvDureeDetection, int pvAngleDetection, int pvDureeNotification,
                         String pvTypeNotif, boolean pvAnnulation, boolean aAlarmeAm, int amDureeDetection, int amDureeNotification, String amTypeNotif,
                         boolean amAnnulation, boolean aAlarmeAgression, int agressionDureeDetection, int agressionDureeNotification, String agressionTypeNotif,
                         String agressionCodeAnnulation, int izsrDureeNotification, String izsrTypeNotif, int puissanceSonore, boolean localisationSonore,
                         String logo, boolean activationPTIAutomatique, boolean sollicitation, boolean historiqueSupervision, boolean aTutoriel,
                         int scenarioExceptionnel, boolean localisationAudio, boolean aRappelUtilisation, int dureeRappelUtilisation,
                         boolean aBroadcastInstavox, boolean aInterfaceAlternative, boolean aScenarioJourNuit, String heureDebutScenarioJourNuit, String heureFinScenarioJourNuit, String[] localisations) {
        this.id = id;
        this.idGroup = idGroup;
        this.autorisation = autorisation;
        this.derniereVersion = derniereVersion;
        this.aTrajet = aTrajet;
        this.aBatiment = aBatiment;
        this.aIzsr = aIzsr;
        this.dureeRechercheGps = dureeRechercheGps;
        this.dureePauseGps = dureePauseGps;
        this.precisionGps = precisionGps;
        this.sosDeclenchementMouvement = sosDeclenchementMouvement;
        this.sosDeclenchementBluetooth = sosDeclenchementBluetooth;
        this.sosDureeNotification = sosDureeNotification;
        this.sosTypeNotif = sosTypeNotif;
        this.sosCodeAnnulation = sosCodeAnnulation;
        this.aAlarmePv = aAlarmePv;
        this.pvDureeDetection = pvDureeDetection;
        this.pvAngleDetection = pvAngleDetection;
        this.pvDureeNotification = pvDureeNotification;
        this.pvTypeNotif = pvTypeNotif;
        this.pvAnnulation = pvAnnulation;
        this.aAlarmeAm = aAlarmeAm;
        this.amDureeDetection = amDureeDetection;
        this.amDureeNotification = amDureeNotification;
        this.amTypeNotif = amTypeNotif;
        this.amAnnulation = amAnnulation;
        this.aAlarmeAgression = aAlarmeAgression;
        this.agressionDureeDetection = agressionDureeDetection;
        this.agressionDureeNotification = agressionDureeNotification;
        this.agressionTypeNotif = agressionTypeNotif;
        this.agressionCodeAnnulation = agressionCodeAnnulation;
        this.izsrDureeNotification = izsrDureeNotification;
        this.izsrTypeNotif = izsrTypeNotif;
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
        this.aBroadcastInstavox = aBroadcastInstavox;
        this.aInterfaceAlternative = aInterfaceAlternative;
        this.aScenarioJourNuit = aScenarioJourNuit;
        this.heureDebutScenarioJourNuit = heureDebutScenarioJourNuit;
        this.heureFinScenarioJourNuit = heureFinScenarioJourNuit;
        this.localisations = localisations;
    }

    public int getId() {
        return id;
    }

    public boolean isAutorisation() {
        return autorisation;
    }

    public int getDerniereVersion() {
        return derniereVersion;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isaTrajet() {
        return aTrajet;
    }

    public boolean isaBatiment() {
        return aBatiment;
    }

    public boolean isaIzsr() {
        return aIzsr;
    }

    public int getDureeRechercheGps() {
        return dureeRechercheGps;
    }

    public int getDureePauseGps() {
        return dureePauseGps;
    }

    public int getPrecisionGps() {
        return precisionGps;
    }

    public String getSosDeclenchementMouvement() {
        return sosDeclenchementMouvement;
    }

    public String getSosDeclenchementBluetooth() {
        return sosDeclenchementBluetooth;
    }

    public int getSosDureeNotification() {
        return sosDureeNotification;
    }

    public String getSosTypeNotif() {
        return sosTypeNotif;
    }

    public String getSosCodeAnnulation() {
        return sosCodeAnnulation;
    }

    public boolean isaAlarmePv() {
        return aAlarmePv;
    }

    public int getPvDureeDetection() {
        return pvDureeDetection;
    }

    public int getPvAngleDetection() {
        return pvAngleDetection;
    }

    public int getPvDureeNotification() {
        return pvDureeNotification;
    }

    public String getPvTypeNotif() {
        return pvTypeNotif;
    }

    public boolean isPvAnnulation() {
        return pvAnnulation;
    }

    public boolean isaAlarmeAm() {
        return aAlarmeAm;
    }

    public int getAmDureeDetection() {
        return amDureeDetection;
    }

    public int getAmDureeNotification() {
        return amDureeNotification;
    }

    public String getAmTypeNotif() {
        return amTypeNotif;
    }

    public boolean isAmAnnulation() {
        return amAnnulation;
    }

    public boolean isaAlarmeAgression() {
        return aAlarmeAgression;
    }

    public int getAgressionDureeDetection() {
        return agressionDureeDetection;
    }

    public int getAgressionDureeNotification() {
        return agressionDureeNotification;
    }

    public String getAgressionTypeNotif() {
        return agressionTypeNotif;
    }

    public String getAgressionCodeAnnulation() {
        return agressionCodeAnnulation;
    }

    public int getIzsrDureeNotification() {
        return izsrDureeNotification;
    }

    public String getIzsrTypeNotif() {
        return izsrTypeNotif;
    }

    public int getPuissanceSonore() {
        return puissanceSonore;
    }

    public boolean isLocalisationSonore() {
        return localisationSonore;
    }

    public String getLogo() {
        return logo;
    }

    public boolean isActivationPTIAutomatique() {
        return activationPTIAutomatique;
    }

    public boolean isSollicitation() {
        return sollicitation;
    }

    public boolean isHistoriqueSupervision() {
        return historiqueSupervision;
    }

    public boolean isaTutoriel() {
        return aTutoriel;
    }

    public int getScenarioExceptionnel() {
        return scenarioExceptionnel;
    }

    public boolean isLocalisationAudio() {
        return localisationAudio;
    }

    public boolean isaRappelUtilisation() {
        return aRappelUtilisation;
    }

    public int getDureeRappelUtilisation() {
        return dureeRappelUtilisation;
    }

    public boolean isaNotation() {
        return aNotation;
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

    public String[] getLocalisations() {
        return localisations;
    }


    @Override
    public String toString() {
        return "Configuration{" +
                "id=" + id +
                ", idGroup=" + idGroup +
                ", autorisation=" + autorisation +
                ", derniereVersion=" + derniereVersion +
                ", aTrajet=" + aTrajet +
                ", aBatiment=" + aBatiment +
                ", aIzsr=" + aIzsr +
                ", dureeRechercheGps=" + dureeRechercheGps +
                ", dureePauseGps=" + dureePauseGps +
                ", precisionGps=" + precisionGps +
                ", sosDeclenchementMouvement='" + sosDeclenchementMouvement + '\'' +
                ", sosDeclenchementBluetooth='" + sosDeclenchementBluetooth + '\'' +
                ", sosDureeNotification=" + sosDureeNotification +
                ", sosTypeNotif='" + sosTypeNotif + '\'' +
                ", sosCodeAnnulation='" + sosCodeAnnulation + '\'' +
                ", aAlarmePv=" + aAlarmePv +
                ", pvDureeDetection=" + pvDureeDetection +
                ", pvAngleDetection=" + pvAngleDetection +
                ", pvDureeNotification=" + pvDureeNotification +
                ", pvTypeNotif='" + pvTypeNotif + '\'' +
                ", pvAnnulation=" + pvAnnulation +
                ", aAlarmeAm=" + aAlarmeAm +
                ", amDureeDetection=" + amDureeDetection +
                ", amDureeNotification=" + amDureeNotification +
                ", amTypeNotif='" + amTypeNotif + '\'' +
                ", amAnnulation=" + amAnnulation +
                ", aAlarmeAgression=" + aAlarmeAgression +
                ", agressionDureeDetection=" + agressionDureeDetection +
                ", agressionDureeNotification=" + agressionDureeNotification +
                ", agressionTypeNotif='" + agressionTypeNotif + '\'' +
                ", agressionCodeAnnulation='" + agressionCodeAnnulation + '\'' +
                ", izsrDureeNotification=" + izsrDureeNotification +
                ", izsrTypeNotif='" + izsrTypeNotif + '\'' +
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
                ", aBroadcastInstavox=" + aBroadcastInstavox +
                ", aInterfaceAlternative=" + aInterfaceAlternative +
                ", aScenarioJourNuit=" + aScenarioJourNuit +
                ", heureDebutScenarioJourNuit=" + heureDebutScenarioJourNuit +
                ", heureFinScenarioJourNuit=" + heureFinScenarioJourNuit +

                ", localisations=" + Arrays.toString(localisations) +
                '}';
    }
}
