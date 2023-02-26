package Entities.Zombies;

import Graphics.SpriteSheet;
import Main.Game;
import Entities.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;

public class BabyZombie extends Zombie {

    final private static BufferedImage[] moving_images = new BufferedImage[17];
    final private static BufferedImage[] attack_images = new BufferedImage[9];

    final private static int size = 120;

    public BabyZombie(int x, int y, ID id, Handler handler, Game game, SpriteSheet ss) {
        super(x, y, id, handler, game, ss);

        setStats();

        hitBoxSize = 80;

        // create animations
        moving_anim = new Animation(1/speed, moving_images);
        attack_anim = new Animation(2/attackSpeed, attack_images);

        // random speed
        speed = ThreadLocalRandom.current().nextInt(4, 6);
    }

    public void setStats() {
        hp = 50;
        speed = 4;
        dmg = 3;
        attackSpeed = 2;
    }

    public void tick() {
        super.tick();
    }

    public void render(Graphics g) {
        super.render(g);

        // draw hp bar
        g.setColor(Color.red);
        g.fillRect(x, y, (int)(hp*1.2), 10);
        g.setColor(Color.black);
        g.drawRect(x, y, 60, 10);
    }

    @Override
    public void vaccineDrop() {
        if (game.levelIndex != 5 && soldier.kills%10 == 0 && Vaccine.nrVaccines + soldier.vaccinesCollected < soldier.vaccinesToCollect) {
            handler.addObject(new Vaccine(x, y, ID.Vaccine, ss));
        }
    }

    // loads all images for baby zombie
    public static void loadImages(SpriteSheet ss, int spriteSize) {
        for (int i = 0; i < 17; i++) {
            moving_images[i] = ss.grabImage(i+1, 6, spriteSize, spriteSize);
            moving_images[i] = SpriteSheet.resize(moving_images[i], size, size);
        }

        for (int i = 0; i < 9; i++) {
            attack_images[i] = ss.grabImage(i+1, 5, spriteSize, spriteSize);
            attack_images[i] = SpriteSheet.resize(attack_images[i], size, size);
        }
    }
}
