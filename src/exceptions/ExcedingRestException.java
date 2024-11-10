package exceptions;

public class ExcedingRestException extends Exception{
    double volumeResteReel;
    double volumeResteSupposé;

    public double getVolumeResteReel() {
        return volumeResteReel;
    }

    public void setVolumeResteReel(double volumeResteReel) {
        this.volumeResteReel = volumeResteReel;
    }

    public double getVolumeResteSupposé() {
        return volumeResteSupposé;
    }

    public void setVolumeResteSupposé(double volumeResteSupposé) {
        this.volumeResteSupposé = volumeResteSupposé;
    }

    public ExcedingRestException(double volumeResteReel, double volumeResteSupposé) throws ExcedingRestException{
        super(String.format("Le volume du reste réel de ?mcube est impossible car la valeur maximale possible est de ?mcube",volumeResteReel,volumeResteSupposé));
        this.volumeResteReel = volumeResteReel;
        this.volumeResteSupposé = volumeResteSupposé;

        if(volumeResteReel>volumeResteSupposé){
            throw this;
        }
    }
}