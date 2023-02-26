package Entities;

import java.awt.*;
import java.awt.image.BufferedImage;
import Graphics.*;

public class HealthCrate extends GameObject {

    public static int nrHealthCrates;
    public static long timeOfLastSpawn;

    private BufferedImage health_crate_image;

    public HealthCrate(int x, int y, ID id, SpriteSheet ss) {
        super(x, y, id, ss);

        health_crate_image = ss.grabImage(5, 7, 128, 128);
        health_crate_image = SpriteSheet.resize(health_crate_image, 70, 70);

        nrHealthCrates++;
    }

    public void tick() {

    }

    public void render(Graphics g) {
        g.drawImage(health_crate_image, x, y, null);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 32, 32);
    }
}
