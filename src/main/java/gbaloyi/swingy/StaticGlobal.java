package gbaloyi.swingy;

import gbaloyi.swingy.view.MapView;
import gbaloyi.swingy.model.hero.Hero;
import gbaloyi.swingy.model.hero.Enemy;

import javax.swing.JTextArea;

import gbaloyi.swingy.model.artifact.Artifact;


public class StaticGlobal {

    public static Hero hero;
    public static MapView map;
    public static Enemy enemy;
    public static Artifact artifact;
    
    public static JTextArea logTextArea;
    public static JTextArea displayTextArea;

    public static boolean HERO_RAN = false;
    public static boolean DISPLAY_LOGO = true;
    public static boolean CONSOLE_MODE = false;
    public static boolean ARTIFACT_DROPPED = false;
    public static boolean GOAL_REACHED = false;
}