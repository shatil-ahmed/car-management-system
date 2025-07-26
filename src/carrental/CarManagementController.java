package carrental;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import util.DB;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CarManagementController implements Initializable {

    @FXML
    private TableView<Car> carTable;

    @FXML
    private TableColumn<Car, Integer> colCarId;

    @FXML
    private TableColumn<Car, String> colCarModel;

    @FXML
    private TableColumn<Car, String> colCarMake;

    @FXML
    private TableColumn<Car, Integer> colYear;

    @FXML
    private Button buuttonBack;

    @FXML
    private Button buttonInsert;

    @FXML
    private Button buttonUpdate;

    @FXML
    private Button buttonDelete;

    @FXML
    private Label lableActionInfoDisplayMessage;

    private ObservableList<Car> carsList = FXCollections.observableArrayList();

    @FXML
    private TextField tfID;
    @FXML
    private TextField tfModel;
    @FXML
    private TextField tfMake;
    @FXML
    private TextField tfYear;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colCarId.setCellValueFactory(new PropertyValueFactory<>("carId"));
        colCarModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        colCarMake.setCellValueFactory(new PropertyValueFactory<>("make"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("year"));

        loadCarData();
    }

    private void loadCarData() {
        carsList.clear();
        try (Connection conn = DB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT car_id, model, make, year FROM cars")) {

            while (rs.next()) {
                int id = rs.getInt("car_id");
                String model = rs.getString("model");
                String make = rs.getString("make");
                int year = rs.getInt("year");

                carsList.add(new Car(id, model, make, year));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        carTable.setItems(carsList);
    }

    @FXML
    private void back(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("AdminDashboard.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            lableActionInfoDisplayMessage.setText("Failed to go back to dashboard.");
        }
    }

    @FXML
    private void insert(ActionEvent event) {
        String model = tfModel.getText();
        String make = tfMake.getText();
        String yearStr = tfYear.getText();

        if (model.isEmpty() || make.isEmpty() || yearStr.isEmpty()) {
            lableActionInfoDisplayMessage.setText("Please fill in all fields.");
            return;
        }

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO cars (model, make, year) VALUES (?, ?, ?)")) {

            stmt.setString(1, model);
            stmt.setString(2, make);
            stmt.setInt(3, Integer.parseInt(yearStr));
            stmt.executeUpdate();

            lableActionInfoDisplayMessage.setText("Car inserted successfully.");
            clearFields();
            loadCarData();

        } catch (Exception e) {
            e.printStackTrace();
            lableActionInfoDisplayMessage.setText("Error inserting car.");
        }
    }

    @FXML
    private void update(ActionEvent event) {
        String idStr = tfID.getText();
        String model = tfModel.getText();
        String make = tfMake.getText();
        String yearStr = tfYear.getText();

        if (idStr.isEmpty() || model.isEmpty() || make.isEmpty() || yearStr.isEmpty()) {
            lableActionInfoDisplayMessage.setText("Please fill in all fields.");
            return;
        }

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE cars SET model=?, make=?, year=? WHERE car_id=?")) {

            stmt.setString(1, model);
            stmt.setString(2, make);
            stmt.setInt(3, Integer.parseInt(yearStr));
            stmt.setInt(4, Integer.parseInt(idStr));
            int rows = stmt.executeUpdate();

            if (rows > 0) {
                lableActionInfoDisplayMessage.setText("Car updated successfully.");
                clearFields();
                loadCarData();
            } else {
                lableActionInfoDisplayMessage.setText("No car found with that ID.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            lableActionInfoDisplayMessage.setText("Error updating car.");
        }
    }

    @FXML
    private void delete(ActionEvent event) {
        String idStr = tfID.getText();

        if (idStr.isEmpty()) {
            lableActionInfoDisplayMessage.setText("Please enter the Car ID.");
            return;
        }

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM cars WHERE car_id=?")) {

            stmt.setInt(1, Integer.parseInt(idStr));
            int rows = stmt.executeUpdate();

            if (rows > 0) {
                lableActionInfoDisplayMessage.setText("Car deleted successfully.");
                clearFields();
                loadCarData();
            } else {
                lableActionInfoDisplayMessage.setText("No car found with that ID.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            lableActionInfoDisplayMessage.setText("Error deleting car.");
        }
    }

    private void clearFields() {
        tfID.clear();
        tfModel.clear();
        tfMake.clear();
        tfYear.clear();
    }
}
