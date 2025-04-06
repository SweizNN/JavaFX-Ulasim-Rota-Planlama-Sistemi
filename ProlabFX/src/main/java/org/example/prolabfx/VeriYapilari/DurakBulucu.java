package org.example.prolabfx.VeriYapilari;

import org.json.JSONArray;
import org.json.JSONObject;

public class DurakBulucu {

    public static JSONObject enYakinDurak(double userLat, double userLon, JSONArray duraklar) {
        JSONObject enYakinDurak = null;
        double minMesafe = Double.MAX_VALUE;

        for (int i = 0; i < duraklar.length(); i++) {
            JSONObject durak = duraklar.getJSONObject(i);
            double durakLat = durak.getDouble("lat");
            double durakLon = durak.getDouble("lon");
            double mesafe = haversine(userLat, userLon, durakLat, durakLon);

            if (mesafe < minMesafe) {
                minMesafe = mesafe;
                enYakinDurak = durak;
            }
        }
        if (enYakinDurak != null) {
            enYakinDurak.put("mesafe", minMesafe);
        }
        return enYakinDurak;
    }

    private static double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int dunyaYaricap = 6371; // Dünya'nın yarıçapı (km)
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return dunyaYaricap * c;
    }
}
