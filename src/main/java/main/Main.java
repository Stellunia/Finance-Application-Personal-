package main;

public class Main {
    public boolean running = true;
    public ApplicationManager applicationManager = new ApplicationManager(this);

    public static void main(String[] args) {
        Main main = new Main();
        System.out.println("Welcome to the Finance application.");


        while (main.running) {
            try {
                main.applicationManager.readCommand();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
