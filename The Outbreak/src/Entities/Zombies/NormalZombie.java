package Entities.Zombies;

import Graphics.SpriteSheet;
import Main.Game;
import Entities.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;

public class NormalZombie extends Zombie {

    final private static BufferedImage[] moving_images = new BufferedImage[17];
    final private static BufferedImage[] attack_images = new BufferedImage[9];

    final private static int size = 145;

    public NormalZombie(int x, int y, ID id, Handler handler, Game game, SpriteSheet ss) {
        super(x, y, id, handler, game, ss);

        setStats();

        hitBoxSize = 100;

        // create animations
        moving_anim = new Animation(speed, moving_images);
        attack_anim = new Animation(attackSpeed*5, attack_images);

        // random speed
        speed = ThreadLocalRandom.current().nextInt(2, 4);
    }

    public void setStats() {
        hp = 100;
        speed = 2;
        dmg = 5;
        attackSpeed = 1;
    }

    public void tick() {
        super.tick();
    }

    public void render(Graphics g) {
        super.render(g);

        // draw hp bar
        g.setColor(Color.red);
        g.fillRect(x-5, y-5, (int)(hp*0.7), 10);
        g.setColor(Color.black);
        g.drawRect(x-5, y-5, 70, 10);
    }

    // loads all images for normal zombie
    public static void loadImages(SpriteSheet ss, int spriteSize) {
        // get all moving images for zombie
        for (int i = 0; i < 17; i++) {
            moving_images[i] = ss.grabImage(i+1, 6, spriteSize, spriteSize);
            moving_images[i] = SpriteSheet.resize(moving_images[i], size, size);
        }

        // get all attack images for zombie
        for (int i = 0; i < 9; i++) {
            attack_images[i] = ss.grabImage(i+1, 5, spriteSize, spriteSize);
            attack_images[i] = SpriteSheet.resize(attack_images[i], size, size);
        }
    }
}
