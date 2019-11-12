package gbaloyi.swingy.model.hero;


public class Horsefish extends Enemy {

    public Horsefish(int level) {
        super(level);
        this.name = "Big Belly Seahorse";
        this.type = "Horsefish";
        this.attack = level + 6;
        this.defense = level + 2;
        this.hitPoints = level + 15; 
    }
}