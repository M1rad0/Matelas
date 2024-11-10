package exceptions;

public class ExcedingTransfoException extends Exception{
    double volumeResteSupposé;

    public double getVolumeResteSupposé() {
        return volumeResteSupposé;
    }

    public void setVolumeResteSupposé(double volumeResteSupposé) {
        this.volumeResteSupposé = volumeResteSupposé;
    }

    public ExcedingTransfoException(double volumeResteSupposé) throws ExcedingTransfoException{
        this.volumeResteSupposé = volumeResteSupposé;
        if(volumeResteSupposé<0){
            throw this;
        }
    }
}