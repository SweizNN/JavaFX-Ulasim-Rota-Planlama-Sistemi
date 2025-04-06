package org.example.prolabfx.OdemeYontem;

public class KentKart extends ÖdemeYontem {
    private double bakiye=20;
    public KentKart(double bakiye) {
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
