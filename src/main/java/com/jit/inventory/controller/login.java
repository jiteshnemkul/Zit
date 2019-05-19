package com.jit.inventory.controller;


import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.jit.inventory.Entities.Employee;
import com.jit.inventory.Repository.EmployeeRepo;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class login implements Initializable {

@Autowired
private EmployeeRepo emp;

    @FXML
    private TextField usernameField, passwordField;
    @FXML
    private Label errorLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
      /*  model = new Employee();*/
        enterPressed();
    }

    private void enterPressed() {

        usernameField.setOnKeyPressed((KeyEvent ke) -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                try {
                    authenticate(ke);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });

        passwordField.setOnKeyPressed((KeyEvent ke) -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                try {
                    authenticate(ke);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });
    }

    @FXML
    public void loginAction(ActionEvent event) throws Exception {

        authenticate(event);
    }

    private void authenticate(Event event) throws Exception {
        if (validateInput()) {

            String username = usernameField.getText().trim();
            String password = DigestUtils.sha1Hex((passwordField.getText().trim()));

            Employee ee= emp.findByUsername(username);
            if (ee.getUsername().equalsIgnoreCase(username)) {

                if (ee.getPassword().equalsIgnoreCase(password)) {

                    ((Node) (event.getSource())).getScene().getWindow().hide();

                    String type = ee.getType();

                    switch (type) {
                        case "admin":
                            windows("/fxml/Admin.fxml", "Admin Panel");
                            break;

                        case "employee":
                            windows("/fxml/Pos.fxml", "Point of Sales");
                            break;
                    }
                } else {
                    passwordField.setText("");
                    errorLabel.setText("Wrong Password!");
                }
            } else {
                resetFields();
                errorLabel.setText("User doesn't exist!");
            }
        }
    }

    private void windows(String path, String title) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource(path));
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setTitle(title);
        stage.getIcons().add(new Image("/images/logo.png"));
        stage.setScene(scene);
        stage.show();
    }

    private void resetFields() {
        usernameField.setText("");
        passwordField.setText("");
    }

    @FXML
    public void cancelAction(ActionEvent event) {
        resetFields();
    }

    @FXML
    public void closeAction(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    public void minusAction(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    private boolean validateInput() {

        String errorMessage = "";

        if (usernameField.getText() == null || passwordField.getText().length() == 0) {
            errorMessage += "Please enter credentials!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            errorLabel.setText(errorMessage);
            return false;
        }
    }


}
