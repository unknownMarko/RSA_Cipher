package com.rsacipher.rsa_cipher;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class RSA_Cipher extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RSA_Cipher.class.getResource("rsa_cipher_frontend.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 730, 720);
        stage.setTitle("RSA Cipher");
        stage.setResizable(false);
        stage.setScene(scene);

        File iconFile = new File("src/main/resources/icon/icon_200.png");
        if (iconFile.exists()) {
            stage.getIcons().add(new Image(iconFile.toURI().toString()));
        } else {
            System.err.println("Could not find icon!");
        }

        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}