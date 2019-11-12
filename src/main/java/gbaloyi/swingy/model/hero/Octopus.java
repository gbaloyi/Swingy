package gbaloyi.swingy.model.hero;


public class Octopus extends Hero {

    public Octopus() {

        super();
    }
    
    public Octopus(String name) {
        super(name);
        this.type = "Octopus";
        this.attack += 10;
        this.defense += 3;
        this.hitPoints += 75;
    }
}