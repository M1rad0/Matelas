package entities.base;

import baseconfig.annotations.Column;
import baseconfig.annotations.DefaultValue;
import baseconfig.annotations.Table;

@Table(name="transformation")
public class Transformation {
    @DefaultValue
    @Column(name="id_transformation")
    String idTransformation;
    @Column(name="id_usuel")
    String idUsuel;
    @Column(name="id_bloc")
    String idBloc;
    @Column(name="quantite")
    int quantite;
    
    // Getters and Setters

    public String getIdTransformation() {
        return idTransformation;
    }
    public void setIdTransformation(String idTransformation) {
        this.idTransformation = idTransformation;
    }
    public String getIdUsuel() {
        return idUsuel;
    }
    public void setIdUsuel(String idUsuel) {
        this.idUsuel = idUsuel;
    }
    public int getQuantite() {
        return quantite;
    }
    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }    
    public String getIdBloc() {
        return idBloc;
    }
    public void setIdBloc(String idBloc) {
        this.idBloc = idBloc;
    }
    // Constructors
    public Transformation(String idTransformation, String idBloc, String idUsuel, int quantite) {
        setIdTransformation(idTransformation);
        setIdBloc(idBloc);
        setIdUsuel(idUsuel);
        setQuantite(quantite);
    }

    public Transformation(){

    }    
}