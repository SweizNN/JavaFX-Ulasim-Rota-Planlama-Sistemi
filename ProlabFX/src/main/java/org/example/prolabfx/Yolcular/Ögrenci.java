package org.example.prolabfx.Yolcular;

public class Ögrenci extends Yolcu {
    @Override
    public double ucretHesapla(double normalUcret) {
        return normalUcret * 0.5; // Öğrenciler %50 indirimli
    }
}
