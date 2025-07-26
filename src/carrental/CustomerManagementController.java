package carrental;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import util.DB;

import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CustomerManagementController implements Initializable {

    @FXML
    private TableView<Customer> customerTable;

    @FXML
    private TableColumn<Customer, Integer> colId;

    @FXML
    private TableColumn<Customer, String> colName;

    @FXML
    private TableColumn<Customer, String> colEmail;

    @FXML
    private TableColumn<Customer, String> colPhone;

    private ObservableList<Customer> customerList = FXCollections.observableArrayList();
    @FXML
    private Button buttonBack;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));

        loadCustomerData();
    }

    private void loadCustomerData() {
        customerList.clear();
        try (Connection conn = DB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, name, email, phone FROM Customers")) {

            while (rs.next()) {
                customerList.add(new Customer(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error loading customers: " + e.getMessage());
        }

        customerTable.setItems(customerList);
    }

    @FXML
    private void handleAddCustomer(ActionEvent event) {
        Dialog<Customer> dialog = new Dialog<>();
        dialog.setTitle("Add New Customer");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone");

        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(new Label("Name:"), nameField,
                new Label("Email:"), emailField,
                new Label("Phone:"), phoneField);

        dialog.getDialogPane().setContent(vbox);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return new Customer(0, nameField.getText(), emailField.getText(), phoneField.getText());
            }
            return null;
        });

        Optional<Customer> result = dialog.showAndWait();

        result.ifPresent(customer -> {
            if (customer.getName().isEmpty() || customer.getEmail().isEmpty() || customer.getPhone().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "All fields are required.");
                return;
            }

            try (Connection conn = DB.getConnection();
                 PreparedStatement ps = conn.prepareStatement(
                         "INSERT INTO Customers (name, email, phone) VALUES (?, ?, ?)")) {

                ps.setString(1, customer.getName());
                ps.setString(2, customer.getEmail());
                ps.setString(3, customer.getPhone());
                ps.executeUpdate();

                showAlert(Alert.AlertType.INFORMATION, "Customer added successfully!");
                loadCustomerData();

            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error adding customer: " + e.getMessage());
            }
        });
    }

    @FXML
    private void handleEditCustomer(ActionEvent event) {
        Customer selected = customerTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Please select a customer to edit.");
            return;
        }

        Dialog<Customer> dialog = new Dialog<>();
        dialog.setTitle("Edit Customer");

        ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        TextField nameField = new TextField(selected.getName());
        TextField emailField = new TextField(selected.getEmail());
        TextField phoneField = new TextField(selected.getPhone());

        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(new Label("Name:"), nameField,
                new Label("Email:"), emailField,
                new Label("Phone:"), phoneField);

        dialog.getDialogPane().setContent(vbox);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButtonType) {
                return new Customer(selected.getId(), nameField.getText(), emailField.getText(), phoneField.getText());
            }
            return null;
        });

        Optional<Customer> result = dialog.showAndWait();

        result.ifPresent(customer -> {
            if (customer.getName().isEmpty() || customer.getEmail().isEmpty() || customer.getPhone().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "All fields are required.");
                return;
            }

            try (Connection conn = DB.getConnection();
                 PreparedStatement ps = conn.prepareStatement(
                         "UPDATE Customers SET name = ?, email = ?, phone = ? WHERE id = ?")) {

                ps.setString(1, customer.getName());
                ps.setString(2, customer.getEmail());
                ps.setString(3, customer.getPhone());
                ps.setInt(4, customer.getId());
                ps.executeUpdate();

                showAlert(Alert.AlertType.INFORMATION, "Customer updated successfully!");
                loadCustomerData();

            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error updating customer: " + e.getMessage());
            }
        });
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void back(ActionEvent event) {
    try {
        Parent root = FXMLLoader.load(getClass().getResource("AdminDashboard.fxml")); // change to your target FXML
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    } catch (IOException e) {
    }
    }
}
