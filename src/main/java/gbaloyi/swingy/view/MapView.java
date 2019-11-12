package gbaloyi.swingy.view;

import lombok.Getter;

import java.util.Random;

import gbaloyi.swingy.tools.Colors;
import gbaloyi.swingy.model.hero.Hero;

import static gbaloyi.swingy.StaticGlobal.*;


@Getter
public class MapView {

    private Hero  heroObj;
    private char[][] map;
    private int size;
    private int[] previousPosition = new int[] {-1, -1};

   
    public MapView(int size) {
        this.size = size;
        this.map = new char[size][size]; 
    }

    public void registerHero(Hero hero) {

        this.heroObj = hero;
        this.heroObj.register(this);
        this.heroObj.setXCoordinate(size / 2);
        this.heroObj.setYCoordinate(size / 2);

        previousPosition[0] = this.heroObj.getXCoordinate();
        previousPosition[1] = this.heroObj.getYCoordinate();
        this.map[size / 2][size / 2] = 'H';
    }

   
    public void updateHeroPosition() {
        this.map[previousPosition[0]][previousPosition[1]] = '.';
        previousPosition[0] = hero.getXCoordinate();
        previousPosition[1] = hero.getYCoordinate();

        if (this.map[hero.getXCoordinate()][hero.getYCoordinate()] == 'E') {
            this.map[hero.getXCoordinate()][hero.getYCoordinate()] = 'X';
        } else {
            this.map[hero.getXCoordinate()][hero.getYCoordinate()] = 'H';
        }
        if (CONSOLE_MODE == true) {
            displayMap();
        } 
    }

  
    public void spreadEnemies() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (map[i][j] != 'H') {
                    int random = new Random().nextInt(3);
                    if (random == 0) {
                        map[i][j] = 'E';
                    }
                }
            }
        }
        if (CONSOLE_MODE == true) {
            displayMap();
        }
    }

    
    public void displayMap() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                switch (map[i][j]) {
                    case 'H':
                        System.out.print(Colors.ANSI_GREEN + map[i][j] + "  " + Colors.ANSI_RESET);
                        break;
                    case 'E':
                        System.out.print(Colors.ANSI_RED + map[i][j] + "  " + Colors.ANSI_RESET);
                        break;
                    case 'X':
                        System.out.print(Colors.ANSI_PURPLE + map[i][j] + "  " + Colors.ANSI_RESET);
                        break;
                    default:
                        System.out.print(Colors.ANSI_YELLOW + ".  " + Colors.ANSI_RESET);
                        break;
                }
            }
            System.out.println("\n");
        }
    }
}