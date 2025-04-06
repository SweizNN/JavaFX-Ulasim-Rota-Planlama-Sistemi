package org.example.prolabfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import org.example.prolabfx.Araclar.Taksi;
import org.example.prolabfx.OdemeYontem.KentKart;
import org.example.prolabfx.OdemeYontem.KrediKart;
import org.example.prolabfx.OdemeYontem.Nakit;
import org.example.prolabfx.OdemeYontem.ÖdemeYontem;
import org.example.prolabfx.VeriYapilari.*;
import org.example.prolabfx.Yolcular.Genel;
import org.example.prolabfx.Yolcular.Yasli;
import org.example.prolabfx.Yolcular.Yolcu;
import org.example.prolabfx.Yolcular.Ögrenci;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Giris {
    @FXML
    private TextField girisBasZam;

    @FXML
    private TextField girisLat;

    @FXML
    private TextField girisLon;

    @FXML
    private TextField girisSonDurak;

    @FXML
    private ChoiceBox<String> girisYolcuTur;

    @FXML
    private Label yazdirLabel;

    private JSONArray duraklar;

    @FXML
    private ChoiceBox<String> girisOdemeYontem;

    @FXML
    private Label odemeBilgiler;

    @FXML
    private WebView webView;

    @FXML
    public void initialize() throws IOException {
        yukleHarita(40.76429, 29.94135);
        ObservableList<String> yolcuTurleri = FXCollections.observableArrayList("Öğrenci", "Yaşlı", "Genel");
        girisYolcuTur.setItems(yolcuTurleri);
        girisYolcuTur.setValue("Genel");
        String data = new String(Files.readAllBytes(Paths.get("src/main/resources/org/example/prolabfx/Veri.json")));
        JSONObject jsonObject = new JSONObject(data);
        duraklar = jsonObject.getJSONArray("duraklar");

        JSONObject taxiJson = jsonObject.getJSONObject("taxi");
        double openingFee = taxiJson.getDouble("openingFee");
        double costPerKm = taxiJson.getDouble("costPerKm");
        Taksi taksi =new Taksi(openingFee, costPerKm);

        ObservableList<String> odemeYontemleri = FXCollections.observableArrayList("KentKart", "Kredi Kartı", "Nakit");
        girisOdemeYontem.setItems(odemeYontemleri);
        girisOdemeYontem.setValue("KentKart");


    }

    public void bilgiGir() {
        int basZaman = Integer.parseInt(girisBasZam.getText());
        double lat = Double.parseDouble(girisLat.getText());
        double lon = Double.parseDouble(girisLon.getText());
        String sonDurak = girisSonDurak.getText();
        String yolcuTur = girisYolcuTur.getValue();
        String odemeYontemi  = girisOdemeYontem.getValue();

        // Yolcu oluştur
        Yolcu yolcu = switch (yolcuTur) {
            case "Öğrenci" -> new Ögrenci();
            case "Yaşlı" -> new Yasli();
            default -> new Genel();
        };

        yolcu.setLat(lat);
        yolcu.setLon(lon);
        yolcu.setHedef(sonDurak);
        yolcu.setBaslangicZaman(basZaman);
        yukleHarita(lat, lon);

        // En yakın durağı bul
        JSONObject enYakinDurak = DurakBulucu.enYakinDurak(lat, lon, duraklar);
        String durakAdi = enYakinDurak.getString("id");
        double mesafe = enYakinDurak.getDouble("mesafe");


        double taksiUcreti = 0;
        String ulasimSekli;

        if (mesafe > 3) {
            taksiUcreti = Taksi.hesaplaUcret(mesafe);
            ulasimSekli = "🚖 Taksi ile gidilecek = " + String.format("%.2f TL", taksiUcreti);
        } else {
            ulasimSekli = "🚶‍♂️ Yürüyerek gidilecek = 0 TL";
        }


        ÖdemeYontem odeme = switch (odemeYontemi) {
            case "KentKart" -> new KentKart(50.0);
            case "Kredi Kartı" -> new KrediKart(100.0);
            case "Nakit" -> new Nakit(2.0);
            default -> new KentKart(20.0);
        };


        // Rota hesapla
        RotaBulucu rotaBulucu = new RotaBulucu(duraklar);
        List<String> rota = rotaBulucu.enKisaRotaBul(durakAdi, sonDurak);

        bilgiYazdir(rotaBulucu, rota, durakAdi, mesafe, ulasimSekli, yolcu, taksiUcreti, odeme);

    }





    private void bilgiYazdir(RotaBulucu rotaBulucu, List<String> rota, String durakAdi, double mesafe, String ulasimSekli, Yolcu yolcu, double taksiUcreti, ÖdemeYontem odeme) {
        double toplamMaliyet = 0;
        double toplamSure = 0;
        double toplamMesafe = 0;
        StringBuilder rotaDetay = new StringBuilder();

        rotaDetay.append("📍 En Yakın Durak: ").append(durakAdi).append(" (")
                .append(String.format("%.2f km", mesafe)).append(") → ")
                .append("\n").append(ulasimSekli).append("\n\n");
        System.out.println(rota);

        for (int i = 0; i < rota.size() - 1; i++) {
            String mevcutDurak = rota.get(i);
            String sonrakiDurak = rota.get(i + 1);
            Duraklar durakBilgi = rotaBulucu.durakGetir(mevcutDurak);

            boolean aktarmaVar = false;


            for (Hatlar hat : durakBilgi.baglantilar) {
                if (hat.hedefDurak.equals(sonrakiDurak)) {
                    int sure = hat.sure;
                    double mesafeDurak = hat.mesafe;
                    double ucret = yolcu.ucretHesapla(hat.ucret);

                    toplamSure += sure;
                    toplamMaliyet += ucret;
                    toplamMesafe += mesafeDurak;

                    rotaDetay.append(i + 1).append(". ").append(mevcutDurak).append(" → ").append(sonrakiDurak).append("\n")
                            .append("   ⏳ Süre: ").append(sure).append(" dk\n")
                            .append("   📏 Mesafe: ").append(String.format("%.2f km", mesafeDurak)).append("\n")
                            .append("   💰 Ücret: ").append(String.format("%.2f TL", ucret)).append("\n");

                    aktarmaVar = true;


                    break;
                }
            }
            if (!aktarmaVar) {
                for (Transferler transfer : durakBilgi.aktarmalar) {
                    if (transfer.transferId.equals(sonrakiDurak)) {  // transfer.hedefDurak yerine transferId kullanıldı
                        double aktarmaSure =  transfer.transferSure;  // transfer.sure yerine transferSure
                        double aktarmaUcret = yolcu.ucretHesapla(transfer.transferUcret);  // transfer.ucret yerine transferUcret

                        toplamSure += aktarmaSure;
                        toplamMaliyet += aktarmaUcret;

                        rotaDetay.append("🔄 *Aktarma Yapıldı!*\n").append(mevcutDurak).append(" -> ").append(" 📍 Yeni Durak: ").append(sonrakiDurak).append("\n")
                                .append("   ⏳ Aktarma Süresi: ").append(aktarmaSure).append(" dk\n")
                                .append("   💰 Aktarma Ücreti: ").append(String.format("%.2f TL", aktarmaUcret)).append("\n\n");
                        break;
                    }
                }
            }

            rotaDetay.append("\n");
        }

        // Ödeme işlemi
        double toplamOdeme = toplamMaliyet + taksiUcreti;
        double yeniBakiye = odeme.getBakiye() - toplamOdeme;
        StringBuilder odemeBilgi = new StringBuilder();

        if (yeniBakiye < 0) {
            odemeBilgi.append("❌ Bakiyeniz yetersiz! (Gerekli: ").append(String.format("%.2f TL", Math.abs(yeniBakiye))).append(", Mevcut: ").append(odeme.getBakiye()).append(" TL)\n");
        } else {
            odeme.setBakiye(yeniBakiye);
            odemeBilgi.append("✅ Ödeme başarılı!\n")
                    .append("   💰 Kalan Bakiye: ").append(yeniBakiye).append(" TL\n");
        }
        odemeBilgiler.setText(odemeBilgi.toString());

        // Toplam bilgiler
        rotaDetay.append("🚀 **Toplam:**\n")
                .append("   💰 Ücret: ").append(String.format("%.2f TL", toplamMaliyet+taksiUcreti)).append("\n")
                .append("   ⏳ Süre: ").append(toplamSure).append(" dk\n")
                .append("   📏 Mesafe: ").append(String.format("%.2f km", toplamMesafe)).append("\n");

        yazdirLabel.setText(rotaDetay.toString());
    }

    private void yukleHarita(double lat, double lon) {
        String mapHtml = """
            <!DOCTYPE html>
            <html>
            <head>
            <title>OpenStreetMap</title>
            <meta name='viewport' content='width=device-width, initial-scale=1.0' />
            <link rel='stylesheet' href='https://unpkg.com/leaflet/dist/leaflet.css' />
            <script src='https://unpkg.com/leaflet/dist/leaflet.js'></script>
            </head>
            <body>
            <div id='map' style='width: 100%; height: 100vh;'></div>
            <script>
            var map = L.map('map').setView([""" + lat + """
                ,""" + lon + """
            ], 13);
            L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                attribution: '&copy; OpenStreetMap contributors'
            }).addTo(map);

            // Kullanıcının işaretlediği noktayı göster
            L.marker([""" + lat + """
                ,""" + lon + """
                ]).addTo(map)
                .bindPopup('Seçilen Konum')
                .openPopup();
            </script>
            </body>
            </html>
            """;

        webView.getEngine().loadContent(mapHtml);
    }





}