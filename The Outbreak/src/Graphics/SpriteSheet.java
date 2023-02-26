package Graphics;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SpriteSheet {

    final private BufferedImage image;
    final private int size;

    public SpriteSheet(BufferedImage image, int size) {
        this.image = image;
        this.size = size;
    }

    public BufferedImage grabImage(int col, int row, int width, int height) {
        return image.getSubimage((col*size)-size, (row*size)-size, width, height);
    }

    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }
}
