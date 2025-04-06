package org.example.prolabfx.OdemeYontem;

public class Nakit extends ÖdemeYontem {
    private double bakiye=2;
    public Nakit(double bakiye) {
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
