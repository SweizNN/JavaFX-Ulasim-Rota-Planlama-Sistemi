package org.example.prolabfx.VeriYapilari;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Duraklar {
    String id;
    String name;
    double lat, lon;
    boolean sonDurak;
    public List<Hatlar> baglantilar = new ArrayList<>();
    public ArrayList<Transferler> aktarmalar=new ArrayList<>();

    public Duraklar(JSONObject json) {
        this.id = json.getString("id");
        this.name = json.getString("name");
        this.lat = json.getDouble("lat");
        this.lon = json.getDouble("lon");
        this.sonDurak = json.getBoolean("sonDurak");

        JSONArray nextStops = json.getJSONArray("nextStops");
        for (int i = 0; i < nextStops.length(); i++) {
            JSONObject stop = nextStops.getJSONObject(i);
            baglantilar.add(new Hatlar(stop.getString("stopId"), stop.getDouble("mesafe"), stop.getInt("sure"), stop.getDouble("ucret")));
        }

        JSONObject transferObj = json.optJSONObject("transfer");
        if (transferObj != null) {
            aktarmalar.add(new Transferler(transferObj.getString("transferStopId"), transferObj.getDouble("transferSure"), transferObj.getDouble("transferUcret")));
        }

    }
}
