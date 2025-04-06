package org.example.prolabfx.Yolcular;

public abstract class Yolcu {
    private int baslangicZaman;
    private double lat;
    private double lon;
    private String hedef;
    private String type;





    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getBaslangicZaman() {
        return baslangicZaman;
    }

    public void setBaslangicZaman(int baslangicZaman) {
        this.baslangicZaman = baslangicZaman;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getHedef() {
        return hedef;
    }

    public void setHedef(String hedef) {
        this.hedef = hedef;
    }

    public abstract double ucretHesapla(double normalUcret);
}
