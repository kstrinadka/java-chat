module com.kstrinadka.chat {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.kstrinadka.chat to javafx.fxml;
    exports com.kstrinadka.chat;
}