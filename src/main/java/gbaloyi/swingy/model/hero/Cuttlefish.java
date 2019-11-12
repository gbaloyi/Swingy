package gbaloyi.swingy.model.hero;

public class Cuttlefish extends Hero {

    public Cuttlefish() {
        super();
    }
    
    public Cuttlefish(String name) {
        super(name);
        this.type = "Cuttlefish";
        this.attack += 8;
        this.defense += 2;
        this.hitPoints += 50;
    }
}