package gbaloyi.swingy.controller;

import gbaloyi.swingy.model.hero.Hero;
import gbaloyi.swingy.model.hero.Crab;
import gbaloyi.swingy.model.hero.Octopus;
import gbaloyi.swingy.model.hero.HeroEnum;
import gbaloyi.swingy.model.hero.Horsefish;
import gbaloyi.swingy.model.hero.Jellyfish;
import gbaloyi.swingy.model.hero.Cuttlefish;


public abstract class HeroFactory {

    private static Hero newHero;
    private static Hero newEnemy;

    public static Hero newHero(String name, HeroEnum type) {
        switch (type) {
            case CRAB:
                newHero = new Crab(name);
                break;
            case CUTTLEFISH:
                newHero = new Cuttlefish(name);
                break;
            case OCTOPUS:
                newHero = new Octopus(name);
                break;
            default:
                break;
        }
        return newHero;
    }

    public static Hero newEnemy(Hero hero, HeroEnum type) {
        switch (type) {
            case HORSEFISH:
                newEnemy = new Horsefish(hero.getLevel());
                break;
            case JELLYFISH:
                newEnemy = new Jellyfish(hero.getLevel());
                break;
            default:
                break;
        }
        return newEnemy;
    }
}