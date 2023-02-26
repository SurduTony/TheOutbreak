package other;

import java.util.concurrent.ThreadLocalRandom;
import Main.*;
import Entities.*;
import Entities.Zombies.*;
import Graphics.*;

public class ObjectSpawner {
    Game game;
    Handler handler;
    Camera camera;
    SpriteSheet ss;

    public ObjectSpawner(Game game, Handler handler, Camera camera, SpriteSheet ss) {
        this.game = game;
        this.handler = handler;
        this.camera = camera;
        this.ss = ss;

        Zombie.timeOfLastSpawn = System.currentTimeMillis();      // for zombie spawner
        AmmoCrate.timeOfLastSpawn = System.currentTimeMillis();   // for ammo crate spawner
        HealthCrate.timeOfLastSpawn = System.currentTimeMillis(); // for health crate spawner
    }

    public void spawnZombie(double timeInterval, int maxSpawns) {
        // spawns zombie at 'timeInterval' seconds
        if (Zombie.nrZombies < maxSpawns && System.currentTimeMillis()-Zombie.timeOfLastSpawn > timeInterval*1000) {
            // get coordinates out of screen
            int randX = (int)camera.getX()+1, randY = (int)camera.getY()+1;
            while (randX >= camera.getX() && randX <= camera.getX()+game.width) {
                randX = ThreadLocalRandom.current().nextInt(-10, game.map.getWidth() + 10);
                randY = ThreadLocalRandom.current().nextInt(-10, game.map.getHeight() + 10);
            }

            // spawn zombie
            if (game.soldier.kills % 3 == 1) {
                handler.addObject(new BabyZombie(randX, randY, ID.Enemy, handler, game, ss));
            }
            else {
                handler.addObject(new NormalZombie(randX, randY, ID.Enemy, handler, game, ss));
            }

            Zombie.timeOfLastSpawn = System.currentTimeMillis();
        }
    }

    public void spawnAmmoCrate(int timeInterval, int maxSpawns) {
        // spawns ammo crate at 'timeInterval' seconds
        if (AmmoCrate.nrAmmoCrates < maxSpawns && System.currentTimeMillis() - AmmoCrate.timeOfLastSpawn > timeInterval * 1000.0) {
            // get coordinates out of screen
            int randX = (int) camera.getX() + 1, randY = (int) camera.getY() + 1;
            while (randX >= camera.getX() && randX <= camera.getX() + game.width) {
                randX = ThreadLocalRandom.current().nextInt(100, game.map.getWidth() - 100);
                randY = ThreadLocalRandom.current().nextInt(100, game.map.getHeight() - 100);
            }

            // spawn ammo crate
            handler.addObject(new AmmoCrate(randX, randY, ID.AmmoCrate, ss));
            AmmoCrate.timeOfLastSpawn = System.currentTimeMillis();
        }
    }

    public void spawnHealthCrate(int timeInterval, int maxSpawns) {
        // spawns health crate at 'timeInterval' seconds
        if (HealthCrate.nrHealthCrates < maxSpawns && System.currentTimeMillis() - HealthCrate.timeOfLastSpawn > timeInterval * 1000.0) {
            // get coordinates out of screen
            int randX = (int) camera.getX() + 1, randY = (int) camera.getY() + 1;
            while (randX >= camera.getX() && randX <= camera.getX() + game.width) {
                randX = ThreadLocalRandom.current().nextInt(100, game.map.getWidth() - 100);
                randY = ThreadLocalRandom.current().nextInt(100, game.map.getHeight() - 100);
            }

            // spawn health crate
            handler.addObject(new HealthCrate(randX, randY, ID.HealthCrate, ss));
            HealthCrate.timeOfLastSpawn = System.currentTimeMillis();
        }
    }
}
