package main.account;

import main.ApplicationManager;
import main.database.DatabaseManager;
import main.database.UsersDTO;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AccountRemover {

    // Insert account removal entirely into this file whenever possible.

    // TODO: Change to Database-variant: By method of getting the userid and then removing the row from the database.
    // Handles removal of user files, need to move this to "AccountRemover" for proper organisation
    public void removeAccount(String userid) {
        try (PreparedStatement statement = DatabaseManager.connection.prepareStatement("DELETE FROM users WHERE id = '" + userid + "';")) {

            if (statement.executeUpdate() == 0) {
                System.out.println("There is no todo with id " + statement);
            }
        } catch (SQLException e) {
            System.out.println("Failed to remove user: " + e.getMessage());
        }
        ApplicationManager.loginCheck = true;
    }

    public void handleAccountRemoval(String userid) {
        System.out.println("Are you sure you'd like to delete your account?");
        System.out.println("Enter 'yes' to continue, otherwise 'return' to go back.");
        String confirmation = ApplicationManager.commandScanner.nextLine();
        if (confirmation.equalsIgnoreCase("yes")) {
            removeAccount(userid);
        }
    }
}
