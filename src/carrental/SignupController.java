package carrental;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class SignupController implements Initializable {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private TextField addressField;
    @FXML private TextField licenceField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;

    private final String DB_URL = "jdbc:mysql://localhost:3306/carrental";
    private final String DB_USER = "root";
    private final String DB_PASSWORD = ""; // leave empty if no password

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Optional: Add focus styling or input masks
    }

    @FXML
    private void handleMinimize(ActionEvent event) {
        Stage stage = (Stage) firstNameField.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void handleClose(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    private void handleSignup(ActionEvent event) {
        String fname = firstNameField.getText().trim();
        String lname = lastNameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String address = addressField.getText().trim();
        String licence = licenceField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Basic validation
        if (fname.isEmpty() || lname.isEmpty() || phone.isEmpty() || email.isEmpty() ||
            address.isEmpty() || licence.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "All fields are required.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Passwords do not match.");
            return;
        }

        // Insert into database
        String insertSQL = "INSERT INTO users (first_name, last_name, phone, email, address, licence, password) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(insertSQL)) {

            stmt.setString(1, fname);
            stmt.setString(2, lname);
            stmt.setString(3, phone);
            stmt.setString(4, email);
            stmt.setString(5, address);
            stmt.setString(6, licence);
            stmt.setString(7, password); // You can hash it in real applications

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Registration successful! You can now log in.");
                clearFields();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database error: " + e.getMessage());
        }
    }

    @FXML
    private void goToLogin(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
            Stage stage = (Stage) firstNameField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Signup");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        firstNameField.clear();
        lastNameField.clear();
        phoneField.clear();
        emailField.clear();
        addressField.clear();
        licenceField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
    }
}
