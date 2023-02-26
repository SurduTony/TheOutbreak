package Entities.Projectiles;

import Main.Game;
import Graphics.*;
import Entities.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class FireBall extends Projectile {

    private BufferedImage image;

    final private int size = 40;

    public FireBall(int x, int y, ID id, Handler handler, int destX, int destY, Game game, SpriteSheet ss) {
        super(x, y, id, handler, game, ss);

        speed = 10;
        calculateVel(destX, destY);

        image = ss.grabImage(13, 7, game.spriteSize, game.spriteSize);
        image = SpriteSheet.resize(image, size*2, size*2);
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(image, x, y, null);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, size, size);
    }
}
