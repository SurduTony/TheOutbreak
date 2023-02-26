package Entities;

import java.awt.*;
import java.awt.image.BufferedImage;
import Graphics.*;

public class AmmoCrate extends GameObject {

    public static int nrAmmoCrates = 0;
    public static long timeOfLastSpawn;

    private BufferedImage ammo_crate_image;

    public AmmoCrate(int x, int y, ID id, SpriteSheet ss) {
        super(x, y, id, ss);

        this.ammo_crate_image = ss.grabImage(7, 7, 128, 128);
        ammo_crate_image = SpriteSheet.resize(ammo_crate_image, 70, 70);

        nrAmmoCrates++;
    }

    public void tick() {

    }

    public void render(Graphics g) {
        g.drawImage(ammo_crate_image, x, y, null);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 32, 32);
    }
}
