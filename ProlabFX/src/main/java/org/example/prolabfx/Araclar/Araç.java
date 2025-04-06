package org.example.prolabfx.Araclar;

public abstract class Araç {
    private String type;

    public Araç(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
