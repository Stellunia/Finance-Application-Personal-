package main.database;

import java.sql.*;
import java.util.Arrays;
import java.util.stream.Collectors;

public class DatabaseManager {
    public static Connection connection;

    public static void connectToDatabase() throws SQLException {
        String connectionString = "jdbc:postgresql://localhost/userdb?user=postgres&password=password";
        connection = DriverManager.getConnection(connectionString);
        establishDatabaseTables();
    }

    public UsersDTO getUserByID (String idInput){
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE id::text = ?;")) {
            statement.setString(1, idInput);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    String id = (result.getObject("id", java.util.UUID.class)).toString(); //TODO: Application doesn't like the fact that it's a String
                    /*Error at getUserByID, DatabaseManager: ERROR: operator does not exist: uuid = character varying
                    Hint: No operator matches the given name and argument types. You might need to add explicit type casts.
                    Position: 30*/
                    String username = result.getString("username");
                    String password = result.getString("password");
                    double balance = result.getDouble("balance");
                    return new UsersDTO(id, username, password, balance);
                }
            } catch (SQLException e) {
                System.out.println("Error at getUserByID, DatabaseManager: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Error at: " + e.getMessage());
        }
        return null;
    }

    public static boolean authenticateUser(String usernameInput) {
        Statement authUser = null;
        try {
            authUser = connection.createStatement();
            authUser.execute("SELECT * FROM users WHERE username ='" + usernameInput + "';");
            return true;
        } catch (SQLException e) {
            System.out.println("Error at: " + e.getMessage());
        }
        return false;
    }

    public static String authenticate(String usernameInput, String passwordInput) {
        Statement authUser = null;
        try (PreparedStatement loadUser = connection.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?")) {
            loadUser.setString(1, usernameInput);
            loadUser.setString(2, passwordInput);
            try (ResultSet result = loadUser.executeQuery()) {
                if (result.next()) {
                    return result.getString("id");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error at: " + e.getMessage());
        }
        return null;
    }

    public static void establishDatabaseTables() throws SQLException {
        Statement createTables = null;
        try {
            createTables = connection.createStatement();
            createTables.execute("CREATE EXTENSION IF NOT EXISTS \"uuid-ossp\";");
            createTables.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "id uuid DEFAULT uuid_generate_v4()," +
                    "username TEXT NOT NULL," +
                    "password TEXT NOT NULL," +
                    "balance FLOAT NOT NULL," +
                    "PRIMARY KEY (id))"
            );

            createTables.execute("CREATE TABLE IF NOT EXISTS transactionHistory (" +
                    "id SERIAL PRIMARY KEY," +
                    "userid uuid," +
                    "title TEXT NOT NULL," +
                    "message TEXT NOT NULL," +
                    "amount double NOT NULL," +
                    "type TEXT NOT NULL," +
                    "date DATE NOT NULL," +
                    "PRIMARY KEY (id)," +
                    "CONSTRAINT fk_user FOREIGN KEY (userid) REFERENCES users(id))");
        } catch (SQLException e) {
            System.out.println("Error at creating tables with error message: " + e.getMessage());
            String errorMessages = Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.joining("\n"));
            System.out.println(errorMessages);
            throw e;
        } finally {
            try {
                if (createTables != null) {
                    createTables.close();
                }
            } catch (SQLException ignored) {}
        }
    }

    public static void initializeDatabaseLoader() {
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM users");
            while (rs.next()) {
                System.out.println("Username: " + rs.getString("username"));
                System.out.println("Password: " + rs.getString("password"));
                System.out.println("Balance: " + rs.getDouble("balance"));
            }

            rs.close();
            st.close();

            connection.close();
        } catch (SQLException e) {
            System.out.println("Error at " + e.getMessage());
        }


    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException ignored) {
        }
    }
}
