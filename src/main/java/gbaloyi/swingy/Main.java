package gbaloyi.swingy;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import gbaloyi.swingy.view.console.ConsoleView;

import static gbaloyi.swingy.StaticGlobal.*;


public class Main {

    public static ValidatorFactory validatorFactory;

    public static void main(String[] args) {
        try {
            if (args[0].equals("console")) {
                validatorFactory = Validation.buildDefaultValidatorFactory();
                CONSOLE_MODE = true;
                ConsoleView.run();
            } else if (args[0].equals("gui")) {
        
                CONSOLE_MODE = false;
                System.out.print("Augment not available try Console\n");
              
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}