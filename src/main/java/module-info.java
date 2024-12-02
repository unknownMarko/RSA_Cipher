module com.rsacipher.rsa_cipher {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.rsacipher.rsa_cipher to javafx.fxml;
    exports com.rsacipher.rsa_cipher;
}