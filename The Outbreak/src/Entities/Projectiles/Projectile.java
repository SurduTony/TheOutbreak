package Entities.Projectiles;

import Graphics.SpriteSheet;
import Main.Game;
import Entities.*;

import java.awt.*;

public abstract class Projectile extends GameObject {
    final private Handler handler;
    Game game;
    protected int speed;

    public Projectile(int x, int y, ID id, Handler handler, Game game, SpriteSheet ss) {
        super(x, y, id, ss);
        this.handler = handler;
        this.game = game;
    }

    public void calculateVel(int destX, int destY) {
        float distance = (float) Math.sqrt(Math.pow(destX - x, 2) + Math.pow(destY - y, 2));
        velX = ((destX - x) / distance) * speed;
        velY = ((destY - y) / distance) * speed;
    }

    public void tick() {
        x += velX;
        y += velY;

        // if the projectile gets out of the level, delete it
        if (outOfBounds()) {
            handler.removeObject(this);
        }
    }

    public abstract void render(Graphics g);

    // checks if the projectile is out of the level
    public Boolean outOfBounds() {
        return x < 0 || y < 0 || x > game.map.getWidth() || y > game.map.getHeight();
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 8, 8);
    }
}
