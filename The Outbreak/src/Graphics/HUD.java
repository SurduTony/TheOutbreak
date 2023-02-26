package Graphics;

import java.awt.*;
import java.awt.image.BufferedImage;
import Main.*;
import Entities.*;

public class HUD {
    private static HUD instance = null;

    final private Game game;
    final private SpriteSheet ss;
    final private Soldier soldier;

    private BufferedImage health_icon, ammo_icon, skull_icon, vaccine_icon;
    final private int spriteSize;

    private HUD(Game game, SpriteSheet ss, Soldier soldier) {
        this.game = game;
        this.ss = ss;
        this.soldier = soldier;

        this.spriteSize = game.spriteSize;

        loadImagesHUD();
    }

    public static HUD getInstance(Game game, SpriteSheet ss, Soldier soldier) {
        if (instance == null) {
            instance = new HUD(game, ss, soldier);
        }
        return instance;
    }

    public void render(Graphics g) {
        // show hp
        g.setColor(Color.red);
        g.fillRect(40, 25, soldier.hp * 3, 25);
        g.setColor(Color.black);
        g.drawRect(40, 25, 300, 25);

        g.setFont(new Font("Ink Free", Font.BOLD, 20));
        g.drawImage(health_icon, 10, 15, null);

        // last level is drawing ammo and kills with white (to see them)
        if (game.levelIndex >= 4) {
            g.setColor(Color.white);
        }

        // Show Timer in a box
        //g.setColor(Color.black);
        //g.fillRect(380, 0, 280, 80);

        // show timer
        g.setFont(new Font("Ink Free", Font.BOLD, 50));
        if (game.countDownTimer != null)
            g.drawString("      " + game.countDownTimer.getTimerString(), 400, 50);

        // show current level
        g.setFont(new Font("Ink Free", Font.BOLD, 30));
        if (game.levelIndex == 5)
            g.drawString("Boss", 510, 90);
        else
            g.drawString("Level " + game.levelIndex, 510, 90);

        // show ammo
        g.setFont(new Font("Ink Free", Font.BOLD, 30));
        g.drawImage(ammo_icon, 10, 80, null);
        g.drawString(soldier.currentGun.getAmmo() + " / " + soldier.currentGun.totalAmmo, 70, 115);

        if (soldier.currentGun.getAmmo() == 0) {
            g.setFont(new Font("Ink Free", Font.BOLD, 20));
            g.drawString("Press R to reload", 20, 150);
        }

        /*
        // show number of zombies
        g.setFont(new Font("Ink Free", Font.BOLD, 20));
        g.drawString("Zombies: " + Zombie.nrZombies, 800, 40);
        */

        // show number of kills
        g.setFont(new Font("Ink Free", Font.BOLD, 30));
        g.drawImage(skull_icon, 800, 15, null);
        g.drawString("" + soldier.kills, 860, 50);

        // show number of vaccines
        g.setFont(new Font("Ink Free", Font.BOLD, 30));
        g.drawImage(vaccine_icon, 800, 70, null);
        g.drawString(soldier.vaccinesCollected + " / " + soldier.vaccinesToCollect, 860, 110);

        // show "Press ESC to pause the game"
        g.setFont(new Font("Ink Free", Font.BOLD, 20));
        g.drawString("Press ESC to pause the game", 10, game.height - 70);
    }

    public void loadImagesHUD() {
        // get icons
        health_icon = ss.grabImage(2, 7, spriteSize, spriteSize);
        ammo_icon = ss.grabImage(8, 7, spriteSize, spriteSize);
        skull_icon = ss.grabImage(4, 7, spriteSize, spriteSize);
        vaccine_icon = ss.grabImage(9, 7, spriteSize, spriteSize);

        // resize icons
        health_icon = SpriteSheet.resize(health_icon, 50, 50);
        ammo_icon = SpriteSheet.resize(ammo_icon, 50, 50);
        skull_icon = SpriteSheet.resize(skull_icon, 50, 50);
        vaccine_icon = SpriteSheet.resize(vaccine_icon, 50, 50);
    }
}
