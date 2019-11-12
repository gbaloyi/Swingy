package gbaloyi.swingy.controller;

import java.util.Random;
import java.util.Scanner;

import java.io.IOException;
import java.sql.SQLException;

import gbaloyi.swingy.model.hero.Enemy;
import gbaloyi.swingy.model.artifact.Helm;
import gbaloyi.swingy.model.hero.HeroEnum;
import gbaloyi.swingy.model.artifact.Armor;
import gbaloyi.swingy.model.artifact.Weapon;
import gbaloyi.swingy.database.DatabaseHandler;
import gbaloyi.swingy.view.console.ConsoleView;

import static gbaloyi.swingy.tools.Log.*;
import static gbaloyi.swingy.tools.Colors.*;
import static gbaloyi.swingy.StaticGlobal.*;


public class GameController {

    private static int[] previousPosition = new int[2];

    
    public static void moveHero(int direction) {
        switch (direction) {
        case 1:
            hero.setPosition(-1, 0);
            previousPosition[0] = -1;
            previousPosition[1] = 0;
            break;
        case 2:
            hero.setPosition(0, 1);
            previousPosition[0] = -1;
            previousPosition[1] = 0;
            break;
        case 3:
            hero.setPosition(1, 0);
            previousPosition[0] = -1;
            previousPosition[1] = 0;
            break;
        case 4:
            hero.setPosition(0, -1);
            previousPosition[0] = -1;
            previousPosition[1] = 0;
            break;
        }
        if (map.getMap()[hero.getXCoordinate()][hero.getYCoordinate()] == 'X') {
            int random = new Random().nextInt(3);
            if (random == 2) {
                enemy = (Enemy) HeroFactory.newEnemy(hero, HeroEnum.HORSEFISH);
            } else {
                enemy = (Enemy) HeroFactory.newEnemy(hero, HeroEnum.JELLYFISH);
            }
            if (CONSOLE_MODE == true) {
                action();
            }
        }
    }

    public static void action() {
        Scanner scanner = new Scanner(System.in);

        log(ANSI_YELLOW + "::: You Are Facing: " + enemy.getName() + ANSI_RESET);
        ConsoleView.displayActions();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Integer choice = Integer.parseInt(line);

            if (choice == 1 || choice == 2) {
                switch (choice) {
                case 1:
                    fight();
                    return;
                case 2:
                    run();
                    return;
                default:
                    break;
                }
            } else {
                log("Try again!");
                ConsoleView.displayActions();
            }
        }

    }

    public static void fight() {
        if (HERO_RAN == false) {
            while (hero.getHitPoints() > 0 && enemy.getHitPoints() > 0) {
                hero.attack(enemy);
                if (enemy.getHitPoints() > 0) {
                    enemy.attack(hero);
                }
            }
        } else if (HERO_RAN == true) {
            while (hero.getHitPoints() > 0 && enemy.getHitPoints() > 0) {
                enemy.attack(hero);
                if (hero.getHitPoints() > 0) {
                    hero.attack(enemy);
                }
            }
        }
        if (hero.getHitPoints() <= 0) {
            if (CONSOLE_MODE == true) {
                log(ANSI_RED + ">>> You Lost, Game Over!");
                ConsoleView.run();
            }
        } else {
            try {
                DatabaseHandler.getInstance().updateHero(hero);
                hero.setPosition(0, 0);
                battleGains();
                log(ANSI_CYAN + "::: Congratulations, You Won The Battle!");
            } catch (ClassNotFoundException | SQLException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void run() {
        int chance = new Random().nextInt(2);

        if (chance == 1) {
            log(ANSI_PURPLE + ">>> Hahaha, You Can't Run My Friend, We Gonna Fight!" + ANSI_RESET);
            HERO_RAN = true;
            fight();
        } else {
            HERO_RAN = false;
            log(ANSI_RED + ">>> Coward! You Ran Away!" + ANSI_RESET);
            hero.setPosition(previousPosition[0] * -1, previousPosition[1] * -1);
        }
    }

    private static void battleGains() {
        int drop = new Random().nextInt(2);
        boolean artifactIsDropped = drop == 1 ? true : false;

        if (artifactIsDropped == true) {
            ARTIFACT_DROPPED = true;
            try {
                log(ANSI_YELLOW + "::: Artifact is Dropped!");
                String[] artifacts = {"ARMOR", "HELM", "WEAPON", "EXPERIENCE"};
                String artifactType = artifacts[new Random().nextInt(4)];
                int variety = hero.getLevel() + 1;
    
                switch (artifactType) {
                    case "ARMOR":
                        artifact = new Armor("Dropped Armor", variety);
                        int gainedDefense = (((Armor) artifact).getDefense() - hero.getArmor().getDefense());
                        log(ANSI_CYAN + "::: If You Keep This Artifact Your Defense Increases by " + gainedDefense + ".");
                        break;
                    case "HELM":
                        artifact = new Helm("Dropped Helmet", variety);
                        int gainedHitPoints = (((Helm) artifact).getHitPoints() - hero.getHelm().getHitPoints());
                        log(ANSI_CYAN + "::: If You Keep This Artifact Your Hit Point(s) Increase by " + gainedHitPoints + ".");
                        break;
                    case "WEAPON":
                        artifact = new Weapon("Dropped Weapon", variety);
                        int gainedAttack = (((Weapon) artifact).getAttack() - hero.getWeapon().getAttack());
                        log(ANSI_CYAN + "::: If You Keep This Artifact Your Attack Increases by " + gainedAttack + ".");
                        break;
                    case "EXPERIENCE":
                        hero.setHitPoints(hero.getHitPoints() + variety);
                        log(ANSI_YELLOW +"::: Healed Up, Current Health: " + hero.getHitPoints());
                        return;
                }
                // Equip the hero.
                equip(artifactType);
            } catch (Exception exception) {
                exception.printStackTrace();
            } 
        } else if (artifactIsDropped == false) {
            log( ANSI_RED + ">>> Sorry, No Artifact Dropped!");
        }     
    }

    private static void equip(String artifactType) {
        if (CONSOLE_MODE == true) {
            Scanner scanner = new Scanner(System.in);
            log(ANSI_YELLOW + "::: Do You Wanna Keep The Artifact?\n1. YES!\n2. NO!" + ANSI_RESET);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.equals("1") || line.equals("2")) {
                    Integer choice = Integer.parseInt(line.trim());
                    if (choice == 1) {
                        hero.equipHero(artifact, artifact.getType());
                        log(ANSI_PURPLE +"::: " + hero.getName() + " Is Equipped With " + artifactType);
                        break;
                    } else if (choice == 2) {
                        break;
                    }   
                } else {
                    log(ANSI_RED + ">>> Incorrect Choice, Try Again!" + ANSI_RESET);
                }
            }
        }
    }

    public static void goal() {
        if (hero.getXCoordinate() == map.getSize() - 1 ||
            hero.getYCoordinate() == map.getSize() - 1 ||
            hero.getXCoordinate() == 0 ||
            hero.getYCoordinate() == 0) {
                log(ANSI_CYAN + "::: Congratutations, You Reached Your Goal!" + ANSI_RESET);
                map = MapFactory.generateMap(hero);
                GOAL_REACHED = true;
        } else {
            GOAL_REACHED = false;
        }
    }
}