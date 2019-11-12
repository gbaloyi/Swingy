package gbaloyi.swingy.controller;

import gbaloyi.swingy.view.MapView;
import gbaloyi.swingy.model.hero.Hero;

import static gbaloyi.swingy.tools.Log.*;

public class MapFactory {

    public static MapView generateMap(Hero hero) {
        int mapSize = (hero.getLevel() - 1) * 5 + 10 - (hero.getLevel() % 2);
        
        if (mapSize >= 25) {
            mapSize = 25;
        }
        MapView map = new MapView(mapSize);
        map.registerHero(hero);
        map.spreadEnemies();
        log("Level: " + hero.getLevel() + "; Mapsize: " + mapSize);
        return (map);
    }
}