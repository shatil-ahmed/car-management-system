package carrental;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class AssignCarController implements Initializable {

    @FXML
    private ComboBox<String> carComboBox;

    @FXML
    private ComboBox<String> customerComboBox;

    @FXML
    private Label statusLabel;
    @FXML
    private Button buttonBack;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Populate with sample data - replace with DB calls
        carComboBox.getItems().addAll("Car A", "Car B", "Car C");
        customerComboBox.getItems().addAll("Customer X", "Customer Y", "Customer Z");

        statusLabel.setText("");  // Clear status on init
    }

    @FXML
    private void handleAssign(ActionEvent event) {
        String selectedCar = carComboBox.getValue();
        String selectedCustomer = customerComboBox.getValue();

        if (selectedCar == null || selectedCustomer == null) {
            statusLabel.setText("Please select both a car and a customer.");
            return;
        }

        // TODO: Add DB logic to assign car to customer here

        statusLabel.setStyle("-fx-text-fill: green;");
        statusLabel.setText("Car '" + selectedCar + "' assigned to '" + selectedCustomer + "' successfully.");
    }

    @FXML
    private void handleAddCar(ActionEvent event) {
        statusLabel.setStyle("-fx-text-fill: black;");
        statusLabel.setText("Add Car clicked. Implement add car logic.");
        // TODO: Open Add Car dialog or scene
    }

    @FXML
    private void handleEditCar(ActionEvent event) {
        String selectedCar = carComboBox.getValue();

        if (selectedCar == null) {
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setText("Please select a car to edit.");
            return;
        }

        statusLabel.setStyle("-fx-text-fill: black;");
        statusLabel.setText("Edit Car clicked for: " + selectedCar);
        // TODO: Open Edit Car dialog or scene with selectedCar data
    }

    @FXML
    private void back(ActionEvent event) {
        try {
        // Load the previous scene (change "Dashboard.fxml" to your target FXML file)
        Parent root = FXMLLoader.load(getClass().getResource("AdminDashboard.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
    }
    }
}