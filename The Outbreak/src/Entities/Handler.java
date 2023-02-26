package Entities;

import java.awt.*;
import java.util.LinkedList;

public class Handler {
    public LinkedList<GameObject> object = new LinkedList<GameObject>();
    Soldier soldier;

    public void tick() {
        soldier.tick();

        for (int i = 0; i < object.size(); i++) {
            object.get(i).tick();
        }
    }

    public void render(Graphics g) {
        soldier.render(g);

        for (int i = 0; i < object.size(); i++) {
            object.get(i).render(g);
        }
    }

    public void addObject(GameObject tempObject) {
        object.add(tempObject);
    }

    public void removeObject(GameObject tempObject) {
        object.remove(tempObject);
    }

    public void reset() {
        object.clear();
    }

    public void addSoldier(Soldier soldier) {
        this.soldier = soldier;
    }

    public Soldier getSoldier() {
        return soldier;
    }
}
