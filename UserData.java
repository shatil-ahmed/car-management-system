package car.rental.managment.system;

import java.util.HashMap;
import java.util.Map;

public class UserData {

    private static final Map<String, String> users = new HashMap<>();

    // Add a new user. Returns false if username already exists.
    public static boolean addUser(String username, String password) {
        if (users.containsKey(username)) {
            return false; // user exists
        }
        users.put(username, password);
        return true;
    }

    // Authenticate username and password
    public static boolean authenticate(String username, String password) {
        return users.containsKey(username) && users.get(username).equals(password);
    }
}
