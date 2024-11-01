package main.command;

import main.Main;

public class StopCommand {
    public Main main;

    public StopCommand(Main main) {
        this.main = main;
    }

    public void run(String[] args) {
        main.running = false;
    }
}