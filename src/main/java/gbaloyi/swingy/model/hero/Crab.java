package gbaloyi.swingy.model.hero;


public class Crab extends Hero {

    public Crab() {
        super(); 
    }

    public Crab(String name) {
        super(name);
        this.type = "Crab";
        this.attack += 6;
        this.defense += 1;
        this.hitPoints += 25;
    }
}