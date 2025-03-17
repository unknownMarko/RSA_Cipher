package com.rsacipher.rsa_cipher;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Controller {

    @FXML
    private TextArea area_calculations;

    @FXML
    private TextArea area_input;

    @FXML
    private TextArea area_input_numeric_values;

    @FXML
    private TextArea area_output;

    @FXML
    private TextArea area_priv_key_d;

    @FXML
    private TextArea area_priv_key_n;

    @FXML
    private TextArea area_pub_key_e;

    @FXML
    private TextArea area_pub_key_n;

    @FXML
    private Button btn_decrypt;

    @FXML
    private Button btn_encrypt;

    @FXML
    private Button btn_generate_values;

    RSALogic logic = new RSALogic();
    List<BigInteger> encrypted;

    boolean nPrivOK = false;
    boolean nPubOK = false;
    boolean eOK = false;
    boolean dOK = false;

    @FXML
    void initialize(){
        area_output.setWrapText(true);
        area_priv_key_n.setWrapText(true);
        area_priv_key_d.setWrapText(true);
        area_pub_key_e.setWrapText(true);
        area_pub_key_n.setWrapText(true);
        area_input.setWrapText(true);
        area_input_numeric_values.setWrapText(true);
        btn_encrypt.setDisable(true);
        btn_decrypt.setDisable(true);
        checkInputs();
    }

    void updateCalArea() {
        StringBuilder calculation_text = new StringBuilder();

        calculation_text.append("P\n").append(logic.p != null ? logic.p.toString() : "NULL").append("\n");
        calculation_text.append("Q\n").append(logic.q != null ? logic.q.toString() : "NULL").append("\n");
        calculation_text.append("N = P * Q\n").append(logic.n != null ? logic.n.toString() : "NULL").append("\n");
        calculation_text.append("φ(N) = (P − 1)(Q − 1)\n").append(logic.phi != null ? logic.phi.toString() : "NULL").append("\n");
        calculation_text.append("1 < E < φ(N)\n").append(logic.e != null ? logic.e.toString() : "NULL").append("\n");
        calculation_text.append("D\n").append(logic.d != null ? logic.d.toString() : "NULL");

        area_calculations.setText(calculation_text.toString());
    }

    void setAreas() {
        area_pub_key_n.setText(logic.n.toString());
        area_pub_key_e.setText(logic.e.toString());
        area_priv_key_n.setText(logic.n.toString());
        area_priv_key_d.setText(logic.d.toString());
    }

    @FXML
    void handleGenerateValues(ActionEvent event) {
        logic.generate();
        nPrivOK = true;
        nPubOK = true;
        eOK = true;
        dOK = true;
        updateCalArea();
        setAreas();
        checkInputs();
    }

    @FXML
    void handleEncrypt(ActionEvent event) {
        encrypted = logic.encryptMessage(area_input.getText());

        System.out.println("P = " + logic.p);
        System.out.println("Q = " + logic.q);
        System.out.println("N = " + logic.n);
        System.out.println("φ = " + logic.phi);
        System.out.println("E = " + logic.e);
        System.out.println("D = " + logic.d);

        area_output.setText(encrypted.toString());
        area_input_numeric_values.setText(logic.convertTextToHexPairs(area_input.getText()));
    }

    @FXML
    void handleDecrypt(ActionEvent event) {
        System.out.println("P = " + logic.p);
        System.out.println("Q = " + logic.q);
        System.out.println("N = " + logic.n);
        System.out.println("φ = " + logic.phi);
        System.out.println("E = " + logic.e);
        System.out.println("D = " + logic.d);

        String decrypted = logic.decryptMessage(
                Arrays.stream(
                    area_input.getText().substring(1, area_input.getLength() - 1).split(","))
                        .map(String::trim)
                        .map(BigInteger::new)
                        .collect(Collectors.toList()));

        area_output.setText(decrypted);

        area_input_numeric_values.setText(logic.convertTextToHexPairs(decrypted));
    }

    boolean isNumber(String input){
        return input.matches("\\d+");
    }

    @FXML
    void handleInput(KeyEvent event) {
        checkInputs();
    }

    void checkInputs() {

        System.out.println("nPubOK = " + nPubOK);
        System.out.println("eOK = " + eOK);
        System.out.println("nPrivOK = " + nPrivOK);
        System.out.println("dOK = " + dOK);
        System.out.println();

        if (!area_input.getText().isEmpty()) {
            //Encrypt
            if (nPubOK && eOK) {
                btn_encrypt.setDisable(false);
            } else {
                btn_encrypt.setDisable(true);
            }

            //Decrypt
            if (nPrivOK && dOK) {
                btn_decrypt.setDisable(false);
            } else {
                btn_decrypt.setDisable(true);
            }
        }
    }

    @FXML
    void handlePrivD(KeyEvent event) {
        if (isNumber(area_priv_key_d.getText())) {
            BigInteger temp  = new BigInteger(area_priv_key_d.getText());
            if (logic.n != null) {
                if (temp.compareTo(BigInteger.ONE) > 0 && temp.compareTo(logic.n) < 0) {
                    System.out.println("Hodnota D JE vacsia ako 1 a mensia ako n..");
                    logic.d = temp;
                    dOK = true;
                    updateCalArea();
                } else {
                    System.out.println("Hodnota D NIE JE vacsia ako 1 a mensia ako n..");
                    area_calculations.setText("");
                    logic.d = null;
                    dOK = false;
                }
            }
        } else {
            System.out.println("Hodnota D NIE JE cislo..");
            area_calculations.setText("");
            logic.d = null;
            dOK = false;
        }
        checkInputs();
    }

    @FXML
    void handlePrivN(KeyEvent event) {
        area_pub_key_n.setText(area_priv_key_n.getText());

        BigInteger temp = new BigInteger(area_priv_key_n.getText());

        if (temp.toString().length() >= 25 && isNumber(area_priv_key_n.getText()) && logic.isPrime(temp)) {
            System.out.println("Hodnota PrivN JE cislo, alebo JE prvocislo..");
            logic.n = temp;
            updateCalArea();
            nPrivOK = true;
            nPubOK = true;
        } else {
            System.out.println("Hodnota PrivN NIE JE cislo, alebo NIE JE prvocislo, alebo NEMA dlzku min 25..");
            area_calculations.setText("");
            logic.n = null;
            nPrivOK = false;
            nPubOK = false;
        }
        checkInputs();
    }

    @FXML
    void handlePubN(KeyEvent event) {
        area_priv_key_n.setText(area_pub_key_n.getText());

        BigInteger temp = new BigInteger(area_pub_key_n.getText());

        if (temp.toString().length() >= 25 && isNumber(area_pub_key_n.getText()) && logic.isPrime(temp)) {
            System.out.println("Hodnota PubN JE cislo, alebo JE prvocislo..");
            logic.n = temp;
            updateCalArea();
            nPubOK = true;
            nPrivOK = true;
        } else {
            System.out.println("Hodnota PubN NIE JE cislo, alebo NIE JE prvocislo, alebo NEMA dlzku min 25..");
            area_calculations.setText("");
            logic.n = null;
            nPubOK = false;
            nPrivOK = false;
        }
        checkInputs();
    }

    @FXML
    void handlePubE(KeyEvent event) {
        if (isNumber(area_pub_key_e.getText())) {
            BigInteger temp = new BigInteger(area_pub_key_e.getText());
            if (logic.n != null) {
                if (temp.compareTo(BigInteger.ONE) > 0 && temp.compareTo(logic.n) < 0) {
                    System.out.println("Hodnota E JE vacsia ako 1 a mensia ako N");
                    logic.e = temp;
                    logic.d = logic.e.modInverse(logic.phi);
                    eOK = true;
                    updateCalArea();
                } else {
                    System.out.println("Hodnota E NIE JE vacsia ako 1 a mensia ako N");
                    area_calculations.setText("");
                    logic.e = null;
                    eOK = false;
                }
            }
        } else {
            System.out.println("Hodnota E NIE JE cislo..");
            area_calculations.setText("");
            logic.e = null;
            eOK = false;
        }
        checkInputs();
    }
}
