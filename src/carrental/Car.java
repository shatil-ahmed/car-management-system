package carrental;

public class Car {
    private int carId;
    private String model;
    private String make;
    private int year;

    public Car(int carId, String model, String make, int year) {
        this.carId = carId;
        this.model = model;
        this.make = make;
        this.year = year;
    }

    public int getCarId() {
        return carId;
    }

    public String getModel() {
        return model;
    }

    public String getMake() {
        return make;
    }

    public int getYear() {
        return year;
    }
}
