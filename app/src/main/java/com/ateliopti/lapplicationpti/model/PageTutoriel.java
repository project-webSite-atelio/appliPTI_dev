package com.ateliopti.lapplicationpti.model;

public class PageTutoriel {
   private int id;
   private String titre;
   private int resImage;
   private String contenu;

   public PageTutoriel(){

   }

   public PageTutoriel(int id, String titre, int resImage, String contenu) {
      this.id = id;
      this.titre = titre;
      this.resImage = resImage;
      this.contenu = contenu;
   }

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getTitre() {
      return titre;
   }

   public void setTitre(String titre) {
      this.titre = titre;
   }

   public int getResImage() {
      return resImage;
   }

   public void setResImage(int resImage) {
      this.resImage = resImage;
   }

   public String getContenu() {
      return contenu;
   }

   public void setContenu(String contenu) {
      this.contenu = contenu;
   }
}
