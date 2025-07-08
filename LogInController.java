package car.rental.managment.system;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

public class LogInController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Please enter username and password.");
            return;
        }

        boolean valid = UserData.authenticate(username, password);
        if (valid) {
            statusLabel.setText("Login successful! Redirecting to dashboard...");

            try {
                Parent dashboardRoot = FXMLLoader.load(getClass().getResource("/car/rental/managment/system/dashboard.fxml"));
                Scene dashboardScene = new Scene(dashboardRoot);
                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setScene(dashboardScene);
                stage.setTitle("Car Rental - Dashboard");
            } catch (Exception e) {
                e.printStackTrace();
                statusLabel.setText("Failed to load dashboard.");
            }
        } else {
            statusLabel.setText("Invalid username or password.");
        }
    }
}
