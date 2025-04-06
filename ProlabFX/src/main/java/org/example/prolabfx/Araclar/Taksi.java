package org.example.prolabfx.Araclar;

public class Taksi extends Araç {
    private static double openingFee;
    private static double costPerKm;

    public Taksi(double openingFee, double costPerKm) {
        super("Taksi");
        Taksi.openingFee = openingFee;
        Taksi.costPerKm = costPerKm;
    }



    public static double hesaplaUcret(double mesafe) {
        return openingFee + (costPerKm * mesafe);
    }









    public double getOpeningFee() {
        return openingFee;
    }

    public void setOpeningFee(double openingFee) {
        this.openingFee = openingFee;
    }

    public double getCostPerKm() {
        return costPerKm;
    }

    public void setCostPerKm(double costPerKm) {
        this.costPerKm = costPerKm;
    }

    @Override
    public String toString() {
        return "Taksi{" +
                "Açılış Ücreti=" + openingFee +
                ", Km Başına Ücret=" + costPerKm +
                '}';
    }
}
