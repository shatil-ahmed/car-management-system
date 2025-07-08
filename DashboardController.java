package car.rental.managment.system;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;

public class DashboardController {

    public static class CarRental {
        private final StringProperty carName = new SimpleStringProperty();
        private final IntegerProperty pricePerDay = new SimpleIntegerProperty();
        private final IntegerProperty days = new SimpleIntegerProperty(0);
        private final IntegerProperty totalPrice = new SimpleIntegerProperty(0);

        public CarRental(String carName, int pricePerDay) {
            this.carName.set(carName);
            this.pricePerDay.set(pricePerDay);

            // Update totalPrice when days changes
            days.addListener((obs, oldVal, newVal) -> {
                int total = pricePerDay * newVal.intValue();
                totalPrice.set(total);
            });
        }

        public String getCarName() { return carName.get(); }
        public void setCarName(String value) { carName.set(value); }
        public StringProperty carNameProperty() { return carName; }

        public int getPricePerDay() { return pricePerDay.get(); }
        public void setPricePerDay(int value) { pricePerDay.set(value); }
        public IntegerProperty pricePerDayProperty() { return pricePerDay; }

        public int getDays() { return days.get(); }
        public void setDays(int value) { days.set(value); }
        public IntegerProperty daysProperty() { return days; }

        public int getTotalPrice() { return totalPrice.get(); }
        public IntegerProperty totalPriceProperty() { return totalPrice; }
    }

    @FXML private TableView<CarRental> carTable;
    @FXML private TableColumn<CarRental, String> carNameCol;
    @FXML private TableColumn<CarRental, Number> priceCol;
    @FXML private TableColumn<CarRental, Number> daysCol;
    @FXML private TableColumn<CarRental, Number> totalCol;
    @FXML private Label statusLabel;

    private final ObservableList<CarRental> carList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Setup columns
        carNameCol.setCellValueFactory(data -> data.getValue().carNameProperty());
        priceCol.setCellValueFactory(data -> data.getValue().pricePerDayProperty());
        totalCol.setCellValueFactory(data -> data.getValue().totalPriceProperty());

        // Custom StringConverter that safely parses integer or resets to 0
        StringConverter<Number> safeIntegerConverter = new StringConverter<>() {
            @Override
            public String toString(Number object) {
                return object == null ? "0" : object.toString();
            }

            @Override
            public Number fromString(String string) {
                try {
                    int val = Integer.parseInt(string);
                    return val < 0 ? 0 : val;
                } catch (NumberFormatException e) {
                    return 0;
                }
            }
        };

        // Editable Days column with safe integer input
        daysCol.setCellValueFactory(data -> data.getValue().daysProperty());
        daysCol.setCellFactory(tc -> {
            TextFieldTableCell<CarRental, Number> cell = new TextFieldTableCell<>(safeIntegerConverter);
            cell.setEditable(true);
            return cell;
        });

        daysCol.setOnEditCommit(event -> {
            int newDays = event.getNewValue().intValue();
            if (newDays < 0) newDays = 0;  // no negative days allowed
            event.getRowValue().setDays(newDays);
            carTable.refresh();
        });

        carTable.setItems(carList);
        carTable.setEditable(true);

        // Add sample cars
        carList.addAll(
            new CarRental("Toyota Corolla", 3000),
            new CarRental("Honda Civic", 3500),
            new CarRental("BMW X5", 8000),
            new CarRental("Nissan Sunny", 2500),
            new CarRental("Tesla Model 3", 10000)
        );
    }

    @FXML
    private void handleSubmit() {
        int totalCost = 0;
        StringBuilder rentedCars = new StringBuilder();

        for (CarRental car : carList) {
            if (car.getDays() > 0) {
                totalCost += car.getTotalPrice();
                rentedCars.append(car.getCarName())
                          .append(" for ")
                          .append(car.getDays())
                          .append(" days = ৳")
                          .append(car.getTotalPrice())
                          .append("\n");
            }
        }

        if (totalCost > 0) {
            statusLabel.setText("You rented:\n" + rentedCars.toString() + "Total: ৳" + totalCost);
        } else {
            statusLabel.setText("Please enter rental days for at least one car.");
        }
    }
}
