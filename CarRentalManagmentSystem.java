package car.rental.managment.system;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CarRentalManagmentSystem extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/car/rental/managment/system/signup.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("Car Rental - Sign Up");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
