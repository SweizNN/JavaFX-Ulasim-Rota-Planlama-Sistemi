package org.example.prolabfx.VeriYapilari;

import org.json.JSONArray;
import java.util.*;

public class RotaBulucu {
    private final Map<String, Duraklar> tumDuraklarMap = new HashMap<>();

    public RotaBulucu(JSONArray duraklar) {
        for (int i = 0; i < duraklar.length(); i++) {
            Duraklar durak = new Duraklar(duraklar.getJSONObject(i));
            tumDuraklarMap.put(durak.id, durak);
        }
    }

    public List<String> enKisaRotaBul(String baslangicDurakId, String hedefDurakId) {
        if (tumDuraklarMap.get(baslangicDurakId).sonDurak) {
            System.out.println("Seçilen başlangıç durağı bir son duraktır. Rota bulunamıyor.");
            return Collections.emptyList();
        }

        HashMap<String, Double> minSure = new HashMap<>();
        HashMap<String, String> oncekiDurak = new HashMap<>();
        HashSet<String> ziyaretEdildi = new HashSet<>();


        for (String durak : tumDuraklarMap.keySet()) {
            minSure.put(durak, Double.MAX_VALUE); // Tüm duraklar için başlangıçta süre sonsuz
        }
        minSure.put(baslangicDurakId, 0.0); // Başlangıç durağına 0 süre

        List<String> kuyruk = new ArrayList<>();
        kuyruk.add(baslangicDurakId);

        while (!kuyruk.isEmpty()) {
            // Kuyruktan en kısa süreye sahip olan durak seçilir
            String mevcutDurakId = bulEnKucukSureliDurak(kuyruk, minSure);
            kuyruk.remove(mevcutDurakId);

            if (ziyaretEdildi.contains(mevcutDurakId)) continue;
            ziyaretEdildi.add(mevcutDurakId);

            Duraklar mevcutDurak = tumDuraklarMap.get(mevcutDurakId);

            for (Hatlar hat : mevcutDurak.baglantilar) {
                double yeniSure = minSure.get(mevcutDurakId) + hat.sure;
                if (yeniSure < minSure.get(hat.hedefDurak)) {
                    minSure.put(hat.hedefDurak, yeniSure);
                    oncekiDurak.put(hat.hedefDurak, mevcutDurakId);
                    if (!ziyaretEdildi.contains(hat.hedefDurak)) {
                        kuyruk.add(hat.hedefDurak);
                    }
                }
            }

            for (Transferler transfer : mevcutDurak.aktarmalar) {
                if (!ziyaretEdildi.contains(transfer.transferId) && !mevcutDurakId.equals(baslangicDurakId)) {
                    double yeniSure = minSure.get(mevcutDurakId) + transfer.transferSure;
                    if (yeniSure < minSure.get(transfer.transferId)) {
                        minSure.put(transfer.transferId, yeniSure);
                        oncekiDurak.put(transfer.transferId, mevcutDurakId);
                        if (!ziyaretEdildi.contains(transfer.transferId)) {
                            kuyruk.add(transfer.transferId);
                        }
                    }
                }
            }
        }

        return returnRota(oncekiDurak, hedefDurakId);
    }

    private String bulEnKucukSureliDurak(List<String> kuyruk, Map<String, Double> minSure) {
        String enKucukSureliDurak = null;
        double enKucukSure = Double.MAX_VALUE;

        for (String durak : kuyruk) {
            double sure = minSure.get(durak);
            if (sure < enKucukSure) {
                enKucukSure = sure;
                enKucukSureliDurak = durak;
            }
        }

        return enKucukSureliDurak;
    }

    private List<String> returnRota(Map<String, String> oncekiDurak, String hedef) {
        List<String> rota = new ArrayList<>();
        String durak = hedef;

        // Hedeften başlangıca kadar gidip rota oluşturulur
        while (durak != null) {
            rota.add(durak);
            durak = oncekiDurak.get(durak);
        }

        Collections.reverse(rota);
        return rota;
    }

    public Duraklar durakGetir(String durakId) {
        if (!tumDuraklarMap.containsKey(durakId)) {
            System.out.println("UYARI: " + durakId + " ID'li durak bulunamadı!");
            return null;
        }
        return tumDuraklarMap.get(durakId);
    }
}
