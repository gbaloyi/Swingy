package gbaloyi.swingy.model.hero;


public class Jellyfish extends Enemy {
    
    public Jellyfish(int level) {
        super(level);
        this.name = "Blue Jellyfish";
        this.type = "Jellyfish";
        this.attack = level + 5;
        this.defense = level + 2;
        this.hitPoints = level + 12;
    }
}