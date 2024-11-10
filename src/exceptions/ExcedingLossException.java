package exceptions;

public class ExcedingLossException extends Exception{
    double volumeResteRéel;
    double volumeResteSupposé;
    double tolérancePerte;

    public double getVolumeResteRéel() {
        return volumeResteRéel;
    }

    public void setVolumeResteRéel(double volumeResteRéel) {
        this.volumeResteRéel = volumeResteRéel;
    }

    public double getVolumeResteSupposé() {
        return volumeResteSupposé;
    }

    public void setVolumeResteSupposé(double volumeResteSupposé) {
        this.volumeResteSupposé = volumeResteSupposé;
    }

    public double getTolérancePerte() {
        return tolérancePerte;
    }

    public void setTolérancePerte(double tolérancePerte) {
        this.tolérancePerte = tolérancePerte;
    }

    public ExcedingLossException(double volumeResteRéel, double volumeResteSupposé, double tolérancePerte) throws ExcedingLossException {
        super("La perte est trop grande et ne peut être acceptée.");
        this.volumeResteRéel = volumeResteRéel;
        this.volumeResteSupposé = volumeResteSupposé;
        this.tolérancePerte = tolérancePerte;

        double pctdifference=(1-(volumeResteRéel/volumeResteSupposé))*100;

        if(pctdifference>tolérancePerte){
            throw this;
        }
    }
}