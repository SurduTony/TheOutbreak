package Entities;

import java.awt.*;
import java.awt.image.BufferedImage;
import Main.*;
import Graphics.*;
import Menu.*;
import Entities.Projectiles.*;
import other.*;

public class Soldier extends GameObject {
    private static Soldier instance = null;

    final private Handler handler;
    final private Game game;
    final private Camera camera;
    final private int spriteSize;

    final private static BufferedImage[] pistol_shooting = new BufferedImage[3];
    final private static BufferedImage[] pistol_reloading = new BufferedImage[15];

    final private static BufferedImage[] rifle_shooting = new BufferedImage[3];
    final private static BufferedImage[] rifle_reloading = new BufferedImage[20];

    private Animation pistol_shooting_anim, pistol_reloading_anim;
    private Animation rifle_shooting_anim, rifle_reloading_anim;
    private Animation shooting_anim, reloading_anim;

    public boolean isShooting = false, isReloading = false;

    private long timeOfLastShot;

    public boolean dirUp = false, dirDown = false, dirRight = false, dirLeft = false;  // directions

    // soldier stats
    public int hp;
    public int kills;
    public int speed = 3;
    public int vaccinesCollected = 0, vaccinesToCollect = 0;
    public int nrDeaths = 0, totalKills = 0;

    public int gunIndex = 1; // 1 = pistol, 2 = rifle
    public Gun pistol, rifle, currentGun;
    public boolean rifleUnlocked = false;

    private int mx, my; // mouse coordinates
    private double rotation;

    private int hitBoxSize = 100;

    private Soldier(ID id, Handler handler, Game game, SpriteSheet ss, Camera camera) {
        super(0, 0, id, ss);
        this.handler = handler;
        this.game = game;
        this.camera = camera;
        this.spriteSize = game.spriteSize;

        createAnimations();

        pistol = new Gun(15, 20, 1);
        rifle = new Gun(30, 25, 2);
        currentGun = pistol;

        timeOfLastShot = System.currentTimeMillis();
    }

    public static Soldier getInstance(ID id, Handler handler, Game game, SpriteSheet ss, Camera camera) {
        if (instance == null) {
            instance = new Soldier(id, handler, game, ss, camera);
        }
        return instance;
    }

    public void tick() {
        x += velX;
        y += velY;

        // shooting
        if (isShooting && !isReloading) {
            if (currentGun.getAmmo() > 0) {
                if ((System.currentTimeMillis()-timeOfLastShot)/1000.0 > 0.1) {
                    currentGun.ammo--;

                    handler.addObject(new Bullet(x + game.spriteSize / 2, y + game.spriteSize/2, ID.Bullet, handler, mx, my, game, ss));

                    game.playSoundEffect(1);    // gun shot sound
                    timeOfLastShot = System.currentTimeMillis();
                }
            } else {
                game.playSoundEffect(2);    // out of ammo sound
                isShooting = false;
            }
        }

        // reloading
        if (isReloading) {
            currentGun.reload();

            // reload animation
            reloading_anim.runAnimation();
            if (reloading_anim.getLoopCount() >= 1) {
                reloading_anim.setLoopCount(0);
                isReloading = false;
            }
        }

        // shooting animation
        if (isShooting) {
            shooting_anim.runAnimation();

            // pistol stop after one shot
            if (gunIndex == 1 && shooting_anim.getLoopCount() >= 1) {
                shooting_anim.setLoopCount(0);
                isShooting = false;
            }
        }

        collision();

        // movement
        if (dirUp) velY = -speed;
        else if (!dirDown) velY = 0;

        if (dirDown) velY = speed;
        else if (!dirUp) velY = 0;

        if (dirRight) velX = speed;
        else if (!dirLeft) velX = 0;

        if (dirLeft) velX = -speed;
        else if (!dirRight) velX = 0;

        // out of bounds
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (x > game.map.getWidth() - spriteSize) x = game.map.getWidth() - spriteSize;
        if (y > game.map.getHeight() - spriteSize)  y = game.map.getHeight() - spriteSize;

        // death
        if (hp <= 0) {
            nrDeaths++;
            hp = 0;

            game.playSoundEffect(11);
            game.countDownTimer.stop();

            game.inMenu = true;
            game.won = false;
            game.menuManager.setState(MenuState.levelFinished);
        }

        // calculate mouse coordinates
        mx = (int) (MouseInfo.getPointerInfo().getLocation().getX() - game.getLocationOnScreen().getX() + camera.getX());
        my = (int) (MouseInfo.getPointerInfo().getLocation().getY() - game.getLocationOnScreen().getY() + camera.getY());

        // calculate rotation with mouse coordinates
        rotation = Math.atan2((y+spriteSize/2.0 - my), (x+spriteSize/2.0 - mx));
    }

    public void render(Graphics g) {
        // draw hit box
        // g.drawRect(x, y, hitBoxSize, hitBoxSize);

        // sprite rotation
        Graphics2D g2d = (Graphics2D) g;
        g2d.rotate(rotation+3.1415, x + spriteSize/2.0, y + spriteSize/2.0);

        if (isReloading) {
            reloading_anim.drawAnimation(g2d, x, y, 0);
        }
        else {  // if is not reloading
            if (isShooting) {
                shooting_anim.drawAnimation(g2d, x, y, 0);
            } else {
                g2d.drawImage(gunIndex==1 ? pistol_shooting[0] : rifle_shooting[0], x, y, null);
            }
        }

        g2d.rotate(-rotation-3.1415, x + spriteSize/2.0, y + spriteSize/2.0);

        //g2d.dispose();
    }

    public void collision() {
        for (int i = 0; i < handler.object.size(); i++) {
            GameObject tempObject = handler.object.get(i);

            if (getBounds().intersects(tempObject.getBounds())) {

                if (tempObject.getId() == ID.Vaccine) {
                    vaccinesCollected++;
                    Vaccine.nrVaccines--;

                    handler.removeObject(tempObject);
                    game.playSoundEffect(12);    // play vaccine pickup sound
                }

                if (tempObject.getId() == ID.AmmoCrate) {
                    AmmoCrate.nrAmmoCrates--;

                    pistol.addAmmo(20);
                    rifle.addAmmo(50);

                    handler.removeObject(tempObject);
                    game.playSoundEffect(3);    // play ammo pickup sound
                }

                if (tempObject.getId() == ID.HealthCrate) {
                    HealthCrate.nrHealthCrates--;

                    hp += 20;
                    handler.removeObject(tempObject);

                    if (hp > 100) hp = 100;

                    game.playSoundEffect(8);
                }

                // fireball is thrown by the boss
                if (tempObject.getId() == ID.FireBall) {
                    handler.removeObject(tempObject);
                    hp -= 10;
                }
            }
        }
    }

    public void resetStats() {
        x = game.map.getWidth()/2;
        y = game.map.getHeight()/2;

        hp = 100;
        kills = 0;
        currentGun.totalAmmo = 100;

        pistol.ammo = pistol.getCapacity();
        rifle.ammo = rifle.getCapacity();

        vaccinesCollected = 0;
    }

    public void changeGun(int i) {
        isReloading = false;
        isShooting = false;

        gunIndex = i;

        switch (gunIndex) {
            case 1:
                shooting_anim = pistol_shooting_anim;
                reloading_anim = pistol_reloading_anim;
                currentGun = pistol;
                break;

            case 2:
                shooting_anim = rifle_shooting_anim;
                reloading_anim = rifle_reloading_anim;
                currentGun = rifle;
                break;
        }

        reloading_anim.setFrame(1);
    }

    public void createAnimations() {
        pistol_shooting_anim = new Animation(1, pistol_shooting);

        pistol_reloading_anim = new Animation(3, pistol_reloading);

        rifle_shooting_anim = new Animation(1, rifle_shooting);

        rifle_reloading_anim = new Animation(3, rifle_reloading);
    }

    // loads all images for soldier; used in Game class
    public static void loadImages(SpriteSheet ss, int spriteSize) {
        // ========================= Pistol ==============================
        // get all images for reloading
        for (int i = 0; i < 15; i++) {
            pistol_reloading[i] = ss.grabImage(i+1, 1, spriteSize, spriteSize);
        }

        // get all images for shooting
        for (int i = 0; i < 3; i++) {
            pistol_shooting[i] = ss.grabImage(i+1, 2, spriteSize, spriteSize);
        }

        // ========================= AK-47 ===============================
        // get all images for reloading
        for (int i = 0; i < 20; i++) {
            rifle_reloading[i] = ss.grabImage(i+1, 3, spriteSize, spriteSize);
            rifle_reloading[i] = SpriteSheet.resize(rifle_reloading[i], 160, 128);
        }

        // get all images for shooting
        for (int i = 0; i < 3; i++) {
            rifle_shooting[i] = ss.grabImage(i+1, 4, spriteSize, spriteSize);
            rifle_shooting[i] = SpriteSheet.resize(rifle_shooting[i], 160, 128);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, hitBoxSize, hitBoxSize);
    }
}
