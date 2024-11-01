package main.menu;

import javafx.scene.paint.Stop;
import main.Main;
import main.command.StopCommand;

public class HistoryMenu implements MenuInterface {
    private StopCommand stopCommand;

    public HistoryMenu(Main main) {
        stopCommand = new StopCommand(main);
    }

    @Override
    public void displayHelp() {
            System.out.println("Type 'today' to sort by today's transactions.");
            System.out.println("Type 'yesterday' to sort by transactions from the last day.");
            System.out.println("Type 'week' to sort by transactions from the last week.");
            System.out.println("Type 'month' to sort by transactions from the last month.");
            System.out.println("Type 'year' to sort by transactions from the last year.");
            System.out.println("Type 'all' to show all transactions.");
            System.out.println("Type 'remove' to remove a transaction from your history.");
            System.out.println("Type 'return' to return to the previous menu.");
            System.out.println("stop - quit the application.");
    }

    @Override
    public void stopHandler() {
        stopCommand.run();
    }
}
