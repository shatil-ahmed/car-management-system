package carrental;

import util.DB;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController implements Initializable {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Any initialization if needed
    }

    @FXML
    private void handleMinimize(ActionEvent event) {
        Stage stage = (Stage) emailField.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void handleClose(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Please enter both email and password.");
            return;
        }

        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, password);  // Ideally passwords should be hashed & verified accordingly

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                showAlert(Alert.AlertType.INFORMATION, "Login successful!");
                // Load Dashboard scene
                Parent root = FXMLLoader.load(getClass().getResource("AdminDashboard.fxml"));
                Stage stage = (Stage) emailField.getScene().getWindow();
                stage.setScene(new Scene(root));
            } else {
                showAlert(Alert.AlertType.ERROR, "Invalid email or password.");
            }

        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error: " + ex.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Login");
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleSignUp(ActionEvent event) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Signup.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    } catch (IOException e) {
    }
    }
}
