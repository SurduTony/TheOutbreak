package Inputs;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import Main.*;
import Entities.*;

public class MouseInput extends MouseAdapter {

    final private Game game;
    final private Soldier soldier;

    public MouseInput(Game game, Soldier soldier) {
        this.game = game;
        this.soldier = soldier;
    }

    public void mousePressed(MouseEvent e) {
        if (!game.inMenu && !soldier.isReloading && !soldier.isShooting) {
            soldier.isShooting = true;
        }
    }

    public void mouseReleased(MouseEvent e) {
        soldier.isShooting = false;
    }
}
