package carrental;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class AdminDashboardController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Button manageCarsBtn;

    @FXML
    private Button manageCustomersBtn;

    @FXML
    private Button rentalHistoryBtn;

    @FXML
    private Button assignCarBtn;

    @FXML
    private Button logoutBtn;
    @FXML
    private Button buttonBack;

    @FXML
    private void handleManageCars(ActionEvent event) {
        loadScene(event, "CarManagement.fxml");
    }

    @FXML
    private void handleManageCustomers(ActionEvent event) {
        loadScene(event, "CustomerManagement.fxml");
    }

    @FXML
    private void handleRentalHistory(ActionEvent event) {
        loadScene(event, "RentalHistory.fxml");
    }

    @FXML
    private void handleAssignCar(ActionEvent event) {
        loadScene(event, "AssignCar.fxml");
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        loadScene(event, "Login.fxml");
    }

    private void loadScene(ActionEvent event, String fxmlFile) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void back(ActionEvent event) {
        try {
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml")); // change to your target FXML
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    } catch (IOException e) {
    }
    }
}