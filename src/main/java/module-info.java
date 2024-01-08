module it.unimol.diffusiontool {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.swing;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires com.google.common;

    opens it.unimol.diffusiontool to javafx.fxml;
    exports it.unimol.diffusiontool;
    exports it.unimol.diffusiontool.application;
    exports it.unimol.diffusiontool.controller;
    exports it.unimol.diffusiontool.entities;
    exports it.unimol.diffusiontool.exceptions;
    opens it.unimol.diffusiontool.controller to javafx.fxml;
}