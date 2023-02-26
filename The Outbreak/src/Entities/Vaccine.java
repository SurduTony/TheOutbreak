package Entities;

import java.awt.*;
import java.awt.image.BufferedImage;
import Graphics.*;

public class Vaccine extends GameObject {

    public static int nrVaccines = 0;

    private BufferedImage vaccine_image;

    public Vaccine(int x, int y, ID id, SpriteSheet ss) {
        super(x, y, id, ss);

        this.vaccine_image = ss.grabImage(9, 7, 128, 128);
        vaccine_image = SpriteSheet.resize(vaccine_image, 70, 70);

        nrVaccines++;
    }

    public void tick() {

    }

    public void render(Graphics g) {
        g.drawImage(vaccine_image, x, y, null);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 32, 32);
    }
}
