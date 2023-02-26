package Entities;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Animation {
    final private int speed;
    final private int frames;
    private int index = 0;
    private int count = 0;
    private int loopCount = 0;

    final private BufferedImage[] img = new BufferedImage[20];
    private BufferedImage currentImg;

    public Animation(int speed, BufferedImage[] img) {
        this.speed = speed;

        for (int i = 0; i < img.length; i++) {
            this.img[i] = img[i];
        }
        frames = img.length;

        nextFrame();
    }

    public void runAnimation() {
        index++;
        if (index > speed) {
            index = 0;
            nextFrame();
        }
    }

    public void nextFrame() {
        if (count > frames-1) {
            count = 0;
            loopCount++;
        }

        currentImg = img[count];
        count++;
    }

    public void drawAnimation(Graphics g, double x, double y, int offset) {
        g.drawImage(currentImg, (int)x - offset, (int)y, null);
    }

    public void setFrame(int i) {
        count = i;
    }

    public void setLoopCount(int loopCount) {
        this.loopCount = loopCount;
    }

    public int getLoopCount() {
        return loopCount;
    }
}
