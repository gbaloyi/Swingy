package gbaloyi.swingy.controller;

import java.util.Scanner;
import java.io.IOException;
import java.sql.SQLException;

import gbaloyi.swingy.model.hero.Hero;
import gbaloyi.swingy.model.hero.HeroEnum;
import gbaloyi.swingy.database.DatabaseHandler;
import gbaloyi.swingy.view.console.ConsoleView;

import static gbaloyi.swingy.tools.Log.*;
import static gbaloyi.swingy.StaticGlobal.*;
import static gbaloyi.swingy.tools.Colors.*;


public class ConsoleController {

  
    public static void chooseHeroType() {
        Scanner scanner = new Scanner(System.in);

        log(ANSI_YELLOW + "::: SELECT A HERO TYPE" + ANSI_RESET);
        ConsoleView.displayHeroTypes();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.equals("1") || line.equals("2") || line.equals("3")) {
                Integer choice = Integer.parseInt(line);
                switch (choice) {
                case 1:
                    createHero(HeroEnum.CRAB);
                    break;
                case 2:
                    createHero(HeroEnum.CUTTLEFISH);
                    break;
                case 3:
                    createHero(HeroEnum.OCTOPUS);
                    break;
                }
                break;
            } else {
                log(ANSI_RED + ">>> Hero Type Does Not Exist, Try Again!" + ANSI_RESET);
            }
        }
        ConsoleView.displayMenuChoices();
    }

   
    public static void createHero(HeroEnum type) {
        Scanner scanner = new Scanner(System.in);

        log(ANSI_YELLOW + "::: Enter The Name Of The Hero");
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

           
            if (line.length() >= 2 && line.length() < 26) {
                try {
                 
                   if (!DatabaseHandler.getInstance().heroExists(line)) {
                       DatabaseHandler.getInstance().insertHero((Hero) HeroFactory.newHero(line.trim(), type));
                       log(ANSI_CYAN + "Created Hero Named: " + ANSI_YELLOW + line);
                    } else {
                        log(ANSI_RED + " >>> " + line + " Hero Exists, Try again!" + ANSI_RESET);
                    }
                } catch (SQLException | IOException | ClassNotFoundException exception) {
                    exception.printStackTrace();
                }
                break;
            } else if (line.length() < 3 || line.length() > 25) {
                log(
                        ANSI_RED + ">>> Name Must Have 2 - 25 Characters, Try Again:" + ANSI_RESET);
            }
        }
    }

   
    public static void selectExistingHero() {
        Scanner scanner = new Scanner(System.in);

        try {
           
            if (DatabaseHandler.getInstance().numberOfHeroes() > 0) {
                if (DatabaseHandler.getInstance().numberOfHeroes() == 1) {
                    log(ANSI_YELLOW + DatabaseHandler.getInstance().numberOfHeroes()
                            + " Hero Available, Choose: " + ANSI_RESET);
                } else {
                    log(ANSI_YELLOW + DatabaseHandler.getInstance().numberOfHeroes()
                            + " Heroes Available: " + ANSI_RESET);
                }
              
                DatabaseHandler.getInstance().retrieveAllHeroes();
            } else {
                log(ANSI_RED + ">>> No Heroes Available!" + ANSI_RESET);
                ConsoleView.displayMenuChoices();
            }
        } catch (IOException | ClassNotFoundException | SQLException exception) {
            exception.printStackTrace();
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            try {
               
                if (DatabaseHandler.getInstance().heroExists(line)) {
                    hero = DatabaseHandler.getInstance().retrieveHeroData(line.trim());
                    map = MapFactory.generateMap(hero);
                    if (CONSOLE_MODE == true) {
                        directions();
                    }
                } else {
                    log(ANSI_RED + ">>> Hero Does not Exist, Try Again!" + ANSI_RESET);
                }
            } catch (ClassNotFoundException | SQLException | IOException exception) {
                exception.printStackTrace();
            }
        }
    }

  
    public static void directions() {
        Scanner scanner = new Scanner(System.in);

        ConsoleView.displayMoveList();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Integer direction = Integer.parseInt(line);
            if (direction == 1 || direction == 2 ||
                direction == 3 || direction == 4) {
                    GameController.moveHero(direction);
                    GameController.goal();
            } else {
                log(ANSI_RED + ">>> Incorrect Choice, Try Again!" + ANSI_RESET);
            }
            ConsoleView.displayMoveList();
        }
        scanner.close();
    }
}