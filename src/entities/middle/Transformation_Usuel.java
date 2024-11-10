package entities.middle;

import java.util.List;

import entities.base.Transformation;
import entities.base.Usuel;

/*Classe qui contient l'usuel utilisé par la transformation car utile dans certains cas */
public class Transformation_Usuel extends Transformation{
    Usuel usuel;

    public Usuel getUsuel() {
        return usuel;
    }

    public void setUsuel(Usuel usuel) {
        this.usuel = usuel;
    }

    public Transformation_Usuel(String idTransformation, String idBloc, String idUsuel, int quantite, List<Usuel> lsUsuels /*Pour des raisons de performances et éviter de faire des get bases isakn mihetsika */) throws Exception {
        super(idTransformation, idBloc, idUsuel, quantite);
        for (Usuel usu : lsUsuels) {
            if(usu.getIdUsuel()==idUsuel){
                setUsuel(usu);
            }   
        }
    }

    public Transformation_Usuel() {
        
    }
}