module org.example.prolabfx {
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires org.json;
    requires javafx.web;

    opens org.example.prolabfx to javafx.fxml;
    exports org.example.prolabfx;
    exports org.example.prolabfx.VeriYapilari;
    opens org.example.prolabfx.VeriYapilari to javafx.fxml;

}