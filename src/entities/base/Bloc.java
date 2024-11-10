package entities.base;

import baseconfig.annotations.Column;
import baseconfig.annotations.DefaultValue;
import baseconfig.annotations.Table;

@Table(name="Bloc")
public class Bloc extends LLH {
    @DefaultValue
    @Column(name="id_bloc")
    String idBloc;
    @Column(name="prix_production")
    double prix_production;
    @Column(name="id_bloc_decoupe")
    String idBlocOrigine=null;
    @Column(name="is_decoupe")
    boolean decoupe=false;

    /*Getters and Setters */
    public String getIdBloc() {
        return idBloc;
    }
    public void setIdBloc(String idBloc) {
        this.idBloc = idBloc;
    }
    public double getPrix_production() {
        return prix_production;
    }
    public void setPrix_production(double prix_production) {
        this.prix_production = prix_production;
    }

    public void setIdBlocOrigine(String idBlocOrigine) {
        this.idBlocOrigine = idBlocOrigine;
    }

    public String getIdBlocOrigine() {
        return idBlocOrigine;
    }
    public boolean isDecoupe() {
        return decoupe;
    }
    public void setDecoupe(boolean isDecoupe) {
        this.decoupe = isDecoupe;
    }
    // Constructeurs
    public Bloc() {
    }

    public Bloc(String idBloc, double longueur, double largeur, double hauteur, double prix_production){
        super(longueur,largeur,hauteur);
        setIdBloc(idBloc);
        setPrix_production(prix_production);
        setIdBlocOrigine(null);
    }

    public void setAsDecoupe(Bloc origine){
        double m3_origine=origine.getPrix_production()/getVolume();
        this.setPrix_production(m3_origine*this.getVolume());
        this.setIdBlocOrigine(origine.getIdBloc());
    }
}
