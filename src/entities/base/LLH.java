package entities.base;

import baseconfig.annotations.Column;

public class LLH {
    @Column(name="longueur")
    double longueur;
    @Column(name="largeur")
    double largeur;
    @Column(name="hauteur")
    double hauteur;

    /*Getters and Setters */
    public double getLongueur() {
        return longueur;
    }
    public void setLongueur(double longueur) {
        this.longueur = longueur;
    }
    public double getLargeur() {
        return largeur;
    }
    public void setLargeur(double largeur) {
        this.largeur = largeur;
    }
    public double getHauteur() {
        return hauteur;
    }
    public void setHauteur(double hauteur) {
        this.hauteur = hauteur;
    }

    // Constructeurs
    public LLH(double longueur, double largeur, double hauteur) {
        setLongueur(longueur);
        setLargeur(largeur);
        setHauteur(hauteur);
    }
    public LLH() {
    } 

    public double getVolume() {
        return largeur*hauteur*longueur;
    }
}