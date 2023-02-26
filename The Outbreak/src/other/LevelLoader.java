package other;

import Main.*;
import Entities.*;
import Entities.Zombies.*;
import Graphics.*;

public class LevelLoader {

    Game game;
    SpriteSheet ss;
    Handler handler;
    BufferedImageLoader loader;

    Soldier soldier;

    public static int nrLevels = 5;

    public LevelLoader(Game game, Handler handler, SpriteSheet ss) {
        this.game = game;
        this.ss = ss;
        this.handler = handler;

        loader = new BufferedImageLoader();

        soldier = handler.getSoldier();
    }

    public void load(int level) {
        // delete all objects
        handler.reset();

        // choose level base on index
        switch (level) {
            case 1 -> level_1();
            case 2 -> level_2();
            case 3 -> level_3();
            case 4 -> level_4();
            case 5 -> boss_fight();
        }

        soldier.resetStats();

        // reset number of objects
        Zombie.nrZombies = 0;
        HealthCrate.nrHealthCrates = 0;
        AmmoCrate.nrAmmoCrates = 0;
        Vaccine.nrVaccines = 0;

        // music
        game.playMusic(level + 13);
    }

    private void level_1() {
        // load map
        game.map = loader.loadImage("/maps/map1.png");

        // vaccines to collect
        soldier.vaccinesToCollect = game.difficulty+1;

        // gun
        soldier.rifleUnlocked = false;
        soldier.changeGun(1);

        // set timer
        game.countDownTimer.set(1, 0);
        game.countDownTimer.start();
    }

    private void level_2() {
        // load map
        game.map = loader.loadImage("/maps/map2.png");

        // vaccines to collect
        soldier.vaccinesToCollect = game.difficulty+3;

        // gun
        soldier.rifleUnlocked = true;
        soldier.changeGun(2);

        // set timer
        game.countDownTimer.set(1, 30);
        game.countDownTimer.start();
    }

    private void level_3() {
        // load map
        game.map = loader.loadImage("/maps/map3.png");

        // vaccines to collect
        soldier.vaccinesToCollect = game.difficulty+4;

        // gun
        soldier.rifleUnlocked = true;
        soldier.changeGun(2);

        // set timer
        game.countDownTimer.set(2, 0);
        game.countDownTimer.start();
    }

    private void level_4() {
        // load map
        game.map = loader.loadImage("/maps/map4.png");

        // vaccines to collect
        soldier.vaccinesToCollect = game.difficulty+8;

        // gun
        soldier.rifleUnlocked = true;
        soldier.changeGun(2);

        // set timer
        game.countDownTimer.set(2, 30);
        game.countDownTimer.start();
    }

    private void boss_fight() {
        // load map
        game.map = loader.loadImage("/maps/map4.png");

        // vaccines to collect
        soldier.vaccinesToCollect = 1;

        // gun
        soldier.rifleUnlocked = true;
        soldier.changeGun(2);

        // set timer
        game.countDownTimer.set(1, 30);
        game.countDownTimer.start();

        // boss
        Boss.loadImages(ss, game.spriteSize);
        handler.addObject(new Boss(0, 0, ID.Enemy, handler, game, ss));
    }
}
