package Entities.Zombies;

import Graphics.SpriteSheet;
import Main.Game;
import Entities.*;
import Entities.Projectiles.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;

public class Boss extends Zombie {

    final private static BufferedImage[] moving_images = new BufferedImage[17];
    final private static BufferedImage[] attack_images = new BufferedImage[9];

    final private static int size = 250;
    private int offset = 50;

    private boolean fireMode = false;

    private long babyZombieLastSpawn;
    private long timeOfLastCharge, timeOfChargeStart, timeOfLastShot;

    public Boss(int x, int y, ID id, Handler handler, Game game, SpriteSheet ss) {
        super(x, y, id, handler, game, ss);

        setStats();

        hitBoxSize = 150;

        // create animations
        moving_anim = new Animation(speed, moving_images);
        attack_anim = new Animation(attackSpeed*5, attack_images);

        // random speed
        speed = ThreadLocalRandom.current().nextInt(2, 4);

        babyZombieLastSpawn = System.currentTimeMillis();
        timeOfLastCharge = System.currentTimeMillis();
        timeOfLastShot = System.currentTimeMillis();
    }

    public void setStats() {
        hp = 5000;
        speed = 2;
        dmg = 20;
        attackSpeed = 1;
    }

    public void tick() {
        super.tick();

        // spawn baby zombie
        if ((System.currentTimeMillis() - babyZombieLastSpawn)/1000.0 > 1) {
            handler.addObject(new BabyZombie(x+size/2, y+size/2, ID.Enemy, handler, game, ss));
            babyZombieLastSpawn = System.currentTimeMillis();
        }

        // charge into player
        if ((System.currentTimeMillis()-timeOfLastCharge)/1000.0 > 14) {
            speed = 5;
            timeOfLastCharge = System.currentTimeMillis();
            timeOfChargeStart = System.currentTimeMillis();
        }
        // stop charge
        if ((System.currentTimeMillis()-timeOfChargeStart)/1000.0 > 4) {
            speed = 2;
        }

        // shoot
        if ((System.currentTimeMillis()-timeOfLastShot)/1000.0 > 2) {
            shootFireBalls();
            timeOfLastShot = System.currentTimeMillis();
        }
    }

    public void render(Graphics g) {
        // hit box
        // g.drawRect(x+offset, y+offset, hitBoxSize, hitBoxSize);

        // rotate to player and draw    ==========================================
        double rotation = Math.atan2((y - soldier.getY()), (x - soldier.getX()));
        Graphics2D g2d = (Graphics2D) g;

        // rotate zombie to player
        g2d.rotate(rotation+3.1415, x + size/2.0, y + size/2.0);

        // draw animation frame
        if (isAttacking)
            attack_anim.drawAnimation(g2d, x, y, 0);
        else
            moving_anim.drawAnimation(g2d, x, y, 0);

        g2d.rotate(-rotation-3.1415, x + size/2.0, y + size/2.0);

        // draw hp bar
        g.setColor(Color.red);
        g.fillRect(x-5, y-5, (int)(hp*0.02), 10);
        g.setColor(Color.black);
        g.drawRect(x-5, y-5, 100, 10);
    }

    public void shootFireBalls() {
        int centerX = x + size/3;
        int centerY = y + size/3;

        if (fireMode) {
            handler.addObject(new FireBall(centerX, centerY, ID.FireBall, handler, centerX + 1, centerY + 1, game, ss));
            handler.addObject(new FireBall(centerX, centerY, ID.FireBall, handler, centerX + 1, centerY - 1, game, ss));
            handler.addObject(new FireBall(centerX, centerY, ID.FireBall, handler, centerX - 1, centerY + 1, game, ss));
            handler.addObject(new FireBall(centerX, centerY, ID.FireBall, handler, centerX - 1, centerY - 1, game, ss));
        }
        else {
            handler.addObject(new FireBall(centerX, centerY, ID.FireBall, handler, centerX + 0, centerY + 1, game, ss));
            handler.addObject(new FireBall(centerX, centerY, ID.FireBall, handler, centerX + 0, centerY - 1, game, ss));
            handler.addObject(new FireBall(centerX, centerY, ID.FireBall, handler, centerX - 1, centerY + 0, game, ss));
            handler.addObject(new FireBall(centerX, centerY, ID.FireBall, handler, centerX + 1, centerY - 0, game, ss));
        }

        fireMode = !fireMode;
    }

    @Override
    public void death() {
        if (hp <= 0) {
            handler.removeObject(this);

            // drop vaccine
            handler.addObject(new Vaccine(x, y, ID.Vaccine, ss));

            game.countDownTimer.set(0, 11);
        }
    }

    // loads all images for boss
    public static void loadImages(SpriteSheet ss, int spriteSize) {
        // get all moving images for boss
        for (int i = 0; i < 17; i++) {
            moving_images[i] = ss.grabImage(i+1, 6, spriteSize, spriteSize);
            moving_images[i] = SpriteSheet.resize(moving_images[i], size, size);
        }

        // get all attack images for boss
        for (int i = 0; i < 9; i++) {
            attack_images[i] = ss.grabImage(i+1, 5, spriteSize, spriteSize);
            attack_images[i] = SpriteSheet.resize(attack_images[i], size, size);
        }
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x+offset, y+offset, hitBoxSize, hitBoxSize);
    }
}

