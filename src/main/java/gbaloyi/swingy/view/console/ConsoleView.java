package gbaloyi.swingy.view.console;

import java.util.Scanner;

import gbaloyi.swingy.controller.ConsoleController;
import gbaloyi.swingy.view.gui.GameWindow;

import static gbaloyi.swingy.tools.Log.*;
import static gbaloyi.swingy.tools.Colors.*;
import static gbaloyi.swingy.StaticGlobal.*;

public class ConsoleView {

    public static void displayMenuChoices() {
        log(ANSI_GREEN + "1." + ANSI_YELLOW + " Create A New Hero." + ANSI_RESET);
        log(ANSI_GREEN + "2." + ANSI_YELLOW + " Select A Hero." + ANSI_RESET);
    }

    public static void displayHeroTypes() {

        log(ANSI_GREEN + "1." + ANSI_CYAN + " Crab" + ANSI_RESET);
        log(ANSI_GREEN + "2." + ANSI_CYAN + " Cuttlefish" + ANSI_RESET);
        log(ANSI_GREEN + "3." + ANSI_CYAN + " Octopus" + ANSI_RESET);
    }

    public static void menu() {
        if (DISPLAY_LOGO == true) {
            Logo.displayLogo();   
        }
        log("");
        log(ANSI_YELLOW + "::: SELECT YOUR CHOICE");
        displayMenuChoices();
        DISPLAY_LOGO = false;
    }

    public static void displayMoveList() {
        log(ANSI_YELLOW + "::: Move");
        log(ANSI_RED + "1." + ANSI_CYAN + " North" + ANSI_RESET);
        log(ANSI_RED + "2." + ANSI_CYAN + " East" + ANSI_RESET);
        log(ANSI_RED + "3." + ANSI_CYAN + " South" + ANSI_RESET);
        log(ANSI_RED + "4." + ANSI_CYAN + " West" + ANSI_RESET);
    }

    public static void displayActions() {
        log(ANSI_YELLOW + "::: Action");
        log(ANSI_RED + "1." + ANSI_CYAN + " Fight" + ANSI_RESET);
        log(ANSI_RED + "2." + ANSI_CYAN + " Run" + ANSI_RESET);
    }

    public static void run() {
        menu();
        Scanner scanner = new Scanner(System.in);

        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.equals("1") || line.equals("2")
                || line.equals("3")) {
                Integer choice = Integer.parseInt(line);
                switch (choice) {
                    case 1:
                        ConsoleController.chooseHeroType();
                        break;
                    case 2:
                        ConsoleController.selectExistingHero();
                        break;
                    case 3:
                        GameWindow.run();
                        break;
                }
            } else {
                log(ANSI_RED + " >>> Incorrect Choice, Try Again!");
            }
        }
    }
}