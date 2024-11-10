package entities.middle;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import baseconfig.utils.DatabaseConnection;
import baseconfig.utils.GeneralDB;
import entities.base.Bloc;
import entities.base.Transformation;
import entities.base.Usuel;
import exceptions.ExcedingLossException;
import exceptions.ExcedingRestException;
import exceptions.ExcedingTransfoException;

public class Transformateur {
    Bloc blocTransforme;
    Bloc resteReel;
    ArrayList<Transformation_Usuel> transformations;
    double volumeResteSupposé;
    
    //Getters and setters
    public void setResteReel(Bloc resteReel) {
        this.resteReel = resteReel;
    }
    public double getVolumeResteSupposé() {
        return volumeResteSupposé;
    }
    public void setVolumeResteSupposé(double volumeResteSupposé) throws ExcedingTransfoException {
        new ExcedingTransfoException(volumeResteSupposé); /*Toutes les exceptions sont testées automatiquement à la construction */
        this.volumeResteSupposé = volumeResteSupposé;
    }    
    public Bloc getBlocTransforme() {
        return blocTransforme;
    }
    private void setBlocTransforme(Bloc blocTransforme) {
        this.blocTransforme = blocTransforme;
        this.volumeResteSupposé=blocTransforme.getVolume();
    }
    public Bloc getResteReel() {
        return resteReel;
    }
    private void setResteReel(Bloc resteReel, double tolérancePerte) throws ExcedingLossException,ExcedingRestException{
        double volumeResteRéel=resteReel.getVolume();
        new ExcedingRestException(volumeResteRéel,volumeResteSupposé);
        new ExcedingLossException(volumeResteRéel, volumeResteSupposé, tolérancePerte);
        this.resteReel = resteReel;
    }
    public ArrayList<Transformation_Usuel> getTransformations() {
        return transformations;
    }
    private void setTransformations(ArrayList<Transformation_Usuel> transformations) {
        this.transformations = transformations;
    }

    private void addTransformation(Transformation_Usuel transfo) throws ExcedingLossException,ExcedingTransfoException{
        getTransformations().add(transfo);
        setVolumeResteSupposé(volumeResteSupposé-transfo.getUsuel().getVolume()*transfo.getQuantite());
    }

    //Constructeur
    public Transformateur(Bloc bloc, Bloc reste, List<Usuel> usuels, List<String> idUsuels, List<Integer> quantities, double tolérancePerte) throws Exception{
        setBlocTransforme(bloc);
        setTransformations(new ArrayList<Transformation_Usuel>());
        
        int index=0;
        for (String idUsuel : idUsuels) {
            addTransformation(new Transformation_Usuel(null,bloc.getIdBloc(),idUsuel,quantities.get(index),usuels));
            index++;
        }

        setResteReel(reste,tolérancePerte);
        reste.setAsDecoupe(bloc);
    }

    /*Insert dans la base */
    public void insert() throws Exception{
        try(Connection conn=DatabaseConnection.getConnection()){
            try{
                conn.setAutoCommit(false);
                /*Insertion du reste */
                GeneralDB<Bloc> blocDB=new GeneralDB<Bloc>(Bloc.class);
                blocDB.create(conn, resteReel);
                
                GeneralDB<Transformation> transfoDB=new GeneralDB<Transformation>(Transformation.class);
                for (Transformation_Usuel transformation : transformations) {
                    Transformation register=transformation;
                    transfoDB.create(conn, register);
                }
            }
            catch(Exception e){
                System.out.println("RollBacked");
                conn.rollback();
                throw e;
            }
        }
    }
}