package org.example.prolabfx.OdemeYontem;

public class KrediKart extends ÖdemeYontem {
    private double bakiye=100;
    public KrediKart(double bakiye) {
        super(bakiye);
    }





    @Override
    public double getBakiye() {
        return bakiye;
    }

    @Override
    public void setBakiye(double bakiye) {
        this.bakiye = bakiye;
    }
}
