import java.util.ArrayList;
import java.util.List;

import baseconfig.utils.DatabaseConnection;
import baseconfig.utils.GeneralDB;
import entities.base.Bloc;
import entities.base.Usuel;
import entities.middle.Transformateur;

public class App {
    public static void main(String[] args) throws Exception {
        GeneralDB<Usuel> forUsuel=new GeneralDB<Usuel>(Usuel.class);
        List<Usuel> usuels=forUsuel.findAll(DatabaseConnection.getConnection());

        GeneralDB<Bloc> forBloc=new GeneralDB<Bloc>(Bloc.class);
        Bloc initial=forBloc.findAll(DatabaseConnection.getConnection()).get(0);

        List<String> idUsuels=new ArrayList<String>();
        idUsuels.add(usuels.get(0).getIdUsuel());
        idUsuels.add(usuels.get(1).getIdUsuel());
        idUsuels.add(usuels.get(2).getIdUsuel());

        List<Integer> quantites=new ArrayList<Integer>();
        quantites.add(100);
        quantites.add(100);
        quantites.add(100);

        Bloc reste=new Bloc(null,50,10,20,0);

        new Transformateur(initial, reste, usuels, idUsuels, quantites, 2);
    }
}