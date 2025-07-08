package car.rental.managment.system;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

public class SignUpController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField emailField;  // Optional, for UI only
    @FXML private Label statusLabel;

    @FXML
    private void handleSignUp() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Please fill all fields.");
            return;
        }

        boolean added = UserData.addUser(username, password);
        if (!added) {
            statusLabel.setText("Username already taken. Try another.");
            return;
        }

        statusLabel.setText("Sign up successful! Redirecting to login...");

        try {
            Parent loginRoot = FXMLLoader.load(getClass().getResource("/car/rental/managment/system/login.fxml"));
            Scene loginScene = new Scene(loginRoot);
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(loginScene);
            stage.setTitle("Car Rental - Login");
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Failed to load login screen.");
        }
    }
}
