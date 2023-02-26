package Inputs;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import Main.*;
import Menu.*;
import Entities.*;

public class KeyInput extends KeyAdapter {

    Game game;
    Soldier soldier;

    public KeyInput(Game game, Soldier soldier) {
        this.game = game;
        this.soldier = soldier;
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (!game.inMenu && key == KeyEvent.VK_ESCAPE) {    // pause game
            game.inMenu = true;
            game.menuManager.setState(MenuState.pauseMenu);
            game.countDownTimer.stop();
            game.muteMusic();
        }
        else if (game.inMenu)
            game.menuManager.input(key);
        else
            gameRunningInput(key);
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_W) soldier.dirUp = false;
        if (key == KeyEvent.VK_S) soldier.dirDown = false;
        if (key == KeyEvent.VK_A) soldier.dirLeft = false;
        if (key == KeyEvent.VK_D) soldier.dirRight = false;
    }

    public void gameRunningInput(int key) {
        // change gun
        if (soldier.rifleUnlocked && key == KeyEvent.VK_Q) {
            soldier.changeGun(soldier.gunIndex == 1 ? 2 : 1);
        }
        else if (key == KeyEvent.VK_1) {
            soldier.changeGun(1);
        }
        else if (soldier.rifleUnlocked && key == KeyEvent.VK_2) {
            soldier.changeGun(2);
        }

        // movement
        if (key == KeyEvent.VK_W) soldier.dirUp = true;
        if (key == KeyEvent.VK_S) soldier.dirDown = true;
        if (key == KeyEvent.VK_A) soldier.dirLeft = true;
        if (key == KeyEvent.VK_D) soldier.dirRight = true;

        if (key == KeyEvent.VK_R && !game.inMenu && !soldier.isReloading && soldier.currentGun.getAmmo() < soldier.currentGun.getCapacity() && soldier.currentGun.totalAmmo > 0) {
            game.playSoundEffect(4);    // play reload sound
            soldier.isReloading = true;
        }
    }
}
