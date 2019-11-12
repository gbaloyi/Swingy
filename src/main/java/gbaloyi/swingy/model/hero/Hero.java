package gbaloyi.swingy.model.hero;

import lombok.Getter;
import lombok.Setter;

import gbaloyi.swingy.view.MapView;
import gbaloyi.swingy.model.artifact.Helm;
import gbaloyi.swingy.model.artifact.Armor;
import gbaloyi.swingy.model.artifact.Weapon;
import gbaloyi.swingy.model.artifact.Artifact;
import gbaloyi.swingy.model.artifact.ArtifactEnum;

import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;

import static gbaloyi.swingy.tools.Log.*;
import static gbaloyi.swingy.tools.Colors.*;


@Getter
@Setter
public abstract class Hero {

    @NotNull
    @Size(min = 2, max = 30)
    protected String name;

    protected int level;
    protected int attack;
    protected int defense;
    protected String type;
    protected int hitPoints;
    protected int experience;
    protected int xCoordinate;
    protected int yCoordinate;
    protected Armor armor;
    protected Helm helm;
    protected Weapon weapon;
    private MapView observer;

    Hero() {

    }

    Hero(String name) {
        this.name = name;
        this.level = 1;
        this.experience = 0;

        Armor defaultArmor = new  Armor("Default Armor", 1);
        Helm defaultHelm = new Helm("Default Helm", 1);
        Weapon defaultWeapon = new Weapon("Default Weapon", 1);

        equipHero(defaultArmor, ArtifactEnum.ARMOR);
        equipHero(defaultHelm, ArtifactEnum.HELM);
        equipHero(defaultWeapon, ArtifactEnum.WEAPON);
    }

	public void register(MapView map) {
       observer = map; 
    }
    
    private void updateMap() {
        observer.updateHeroPosition();
    }

    public void setPosition(int x, int y) {
        this.xCoordinate += x;
        this.yCoordinate += y;
        updateMap();
    }

    public void attack(Hero enemy) {
        int earnedExperience = 0;

        enemy.defend(this.attack);
        if (enemy.getHitPoints() <= 0) {
            if (enemy.getType().equals("Horsefish")) {
                earnedExperience = (int) (Math.ceil((float)this.level / 2) * 750);
                this.experience += earnedExperience;
            } else if (enemy.getType().equals("Jellyfish")) {
                earnedExperience = (int) (Math.ceil((float)this.level / 2) * 500);
                this.experience += earnedExperience;
            }
            log(ANSI_CYAN + " >>> Well Done, Your Earned " + earnedExperience + "XP" + ANSI_RESET);
            if (this.experience >= (this.level * 1000 + Math.pow(this.level - 1, 2) * 450)) {
                levelUp();
            }
        }
    }


    public void defend(int enemyDamage) {
        int damage = enemyDamage - this.defense;

        if (damage <= 0) {
            damage = 1;
        }
        if (damage > 0) {
            this.hitPoints -= damage;
        }
    }

    private void levelUp() {
        int levelUp;
 
        this.level++;
        levelUp = this.level;
        this.attack += levelUp;
        this.hitPoints += levelUp;
        this.defense += 1;
    }

	public void equipHero(Artifact artifact, ArtifactEnum type) {
        switch (type) {
            case ARMOR:
                if (armor != null) {
                    defense -= armor.getDefense(); 
                } else {
                    armor = (Armor)artifact;
                    defense += armor.getDefense();
                }
                break;
            case HELM:
                if (helm != null) {
                    hitPoints -= helm.getHitPoints();
                } else {
                    helm = (Helm)artifact;
                    hitPoints += helm.getHitPoints();
                }
                break;
            case WEAPON:
                if (weapon != null) {
                    attack -= weapon.getAttack();
                } else {
                    weapon = (Weapon)artifact;
                    attack += weapon.getAttack();
                }
                break;
        }
    }
}
