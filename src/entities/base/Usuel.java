package entities.base;

import baseconfig.annotations.Column;
import baseconfig.annotations.DefaultValue;
import baseconfig.annotations.Table;

@Table(name="usuel")
public class Usuel extends LLH{
    @DefaultValue
    @Column(name="id_usuel")
    String idUsuel;
    @Column(name="prix_vente")
    double prixVente;
    @Column(name="nom_usuel")
    String nomUsuel;

    /*Getters and setters */
    public String getIdUsuel() {
        return idUsuel;
    }
    public void setIdUsuel(String idUsuel) {
        this.idUsuel = idUsuel;
    }
    public double getPrixVente() {
        return prixVente;
    }
    public void setPrixVente(double prixVente) {
        this.prixVente = prixVente;
    }

    public String getNomUsuel() {
        return nomUsuel;
    }

    public void setNomUsuel(String nomUsuel) {
        this.nomUsuel = nomUsuel;
    }

    public Usuel(){

    }
    public Usuel(double longueur, double largeur, double hauteur, String idUsuel, double prixVente, String nomUsuel) {
        super(longueur, largeur, hauteur);
        this.idUsuel = idUsuel;
        this.prixVente = prixVente;
        this.setNomUsuel(nomUsuel);
    }    
}