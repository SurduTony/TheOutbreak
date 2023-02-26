package other;

import Main.*;
import Entities.*;

public class Camera {
    private float x, y;
    Game game;

    public Camera(float x, float y, Game game) {
        this.x = x;
        this.y = y;
        this.game = game;
    }

    public void tick(GameObject object) {
        x += ((object.getX() - x) - game.width/2.0) * 0.05f;
        y += ((object.getY() - y) - game.height/2.0) * 0.05f;

        if (x <= 0) x = 0;
        if (x >= game.map.getWidth()-game.width) x = game.map.getWidth()-game.width;
        if (y <= 0) y = 0;
        if (y >= game.map.getHeight()-game.height) y = game.map.getHeight()-game.height;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
