package Entities.Projectiles;

import java.awt.*;
import Main.*;
import Graphics.*;
import Entities.*;

public class Bullet extends Projectile {

    public Bullet(int x, int y, ID id, Handler handler, int destX, int destY, Game game, SpriteSheet ss) {
        super(x, y, id, handler, game, ss);

        speed = 20;
        calculateVel(destX, destY);
    }

    public void tick() {
        super.tick();
    }

    public void render(Graphics g) {
        g.setColor(Color.black);
        g.fillOval(x, y, 8, 8);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 8, 8);
    }
}
