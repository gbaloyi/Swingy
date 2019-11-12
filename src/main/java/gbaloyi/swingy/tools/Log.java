package gbaloyi.swingy.tools;

import static gbaloyi.swingy.StaticGlobal.*;

public class Log {

    public static void log(String message) {
        if (CONSOLE_MODE == true) {
            System.out.println(message);   
        }
    }
}