package main.menu;

import main.Main;
import main.command.StopCommand;

public class AccountMenu implements MenuInterface {
    private StopCommand stopCommand;

    public AccountMenu(Main main) {
        stopCommand = new StopCommand(main);
    }

    @Override
    public void displayHelp() {
        System.out.println("Type 'remove' to delete your account.");
        System.out.println("Type 'return' to return to the previous menu.");
        System.out.println("stop - quit the application.");
    }

    @Override
    public void stopHandler() {
        stopCommand.run();
    }


}
