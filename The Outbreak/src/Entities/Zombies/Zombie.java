package Entities.Zombies;

import java.awt.*;
import Main.*;
import Graphics.*;
import Entities.*;

public abstract class Zombie extends GameObject {

    Handler handler;
    Game game;

    Animation moving_anim, attack_anim;

    protected int hp;
    protected int speed;
    protected int dmg;
    protected int attackSpeed; // how many times a second he can attack
    protected int hitBoxSize;

    public static int nrZombies = 0;
    public static long timeOfLastSpawn;

    private long timeOfLastAttack;
    protected Boolean isAttacking;

    protected Soldier soldier;

    public Zombie(int x, int y, ID id, Handler handler, Game game, SpriteSheet ss) {
        super(x, y, id, ss);
        this.handler = handler;
        this.game = game;

        Init();
    }

    public void Init() {
        // get soldier from object list
        soldier = handler.getSoldier();

        nrZombies++;
        timeOfLastAttack = System.currentTimeMillis();
        isAttacking = false;
    }

    public void tick() {
        if (!isAttacking)
            chaseSoldier();

        x += velX;
        y += velY;

        // checks if zombie is currently attacking
        if ((System.currentTimeMillis() - timeOfLastAttack)/1000.0 >= 1.0/attackSpeed)
            isAttacking = false;

        // run animations
        if (isAttacking)
            attack_anim.runAnimation();
        else
            moving_anim.runAnimation();

        collision();

        death();
    }

    public void death() {
        if (hp <= 0) {
            nrZombies--;
            soldier.kills++;
            game.playSoundEffect(7);    // play zombie sound

            // drops vaccine
            vaccineDrop();

            handler.removeObject(this);
        }
    }

    public void vaccineDrop() {
        if (soldier.kills%10 == 0 && Vaccine.nrVaccines + soldier.vaccinesCollected < soldier.vaccinesToCollect) {
            handler.addObject(new Vaccine(x, y, ID.Vaccine, ss));
        }
    }

    public void render(Graphics g) {
        // hit box
        // g.drawRect(x, y, hitBoxSize, hitBoxSize);

        // rotate to player and draw    ==========================================
        double rotation = Math.atan2((y - soldier.getY()), (x - soldier.getX()));
        Graphics2D g2d = (Graphics2D) g;

        // rotate zombie to player
        g2d.rotate(rotation+3.1415, x + game.spriteSize/2.0, y + game.spriteSize/2.0);

        // draw animation frame
        if (isAttacking)
            attack_anim.drawAnimation(g2d, x, y, 0);
        else
            moving_anim.drawAnimation(g2d, x, y, 0);

        g2d.rotate(-rotation-3.1415, x + game.spriteSize/2.0, y + game.spriteSize/2.0);
    }

    public void collision() {
        // collision with soldier
        if (getBounds().intersects(soldier.getBounds())) {
            // stops moving to attack
            velX = 0;
            velY = 0;

            // attack speed
            if (!isAttacking) {
                isAttacking = true;
                game.playSoundEffect(5);    // play zombie sound
                soldier.hp -= dmg;

                timeOfLastAttack = System.currentTimeMillis();
            }
        }

        for (int i = 0; i < handler.object.size(); i++) {
            GameObject tempObject = handler.object.get(i);

            if (tempObject.getId() == ID.Bullet) {
                if (getBounds().intersects(tempObject.getBounds())) {
                    hp -= soldier.currentGun.getDamage();
                    handler.removeObject(tempObject);
                }
            }
        }
    }

    public void chaseSoldier() {
        int playerX = soldier.getX();
        int playerY = soldier.getY();
        float distance = (float) Math.sqrt(Math.pow(playerX - x, 2) + Math.pow(playerY - y, 2));

        setVelX(((playerX - x) / distance) * speed);
        setVelY(((playerY - y) / distance) * speed);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, hitBoxSize, hitBoxSize);
    }
}
