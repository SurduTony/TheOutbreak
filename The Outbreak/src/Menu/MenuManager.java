package Menu;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import Main.*;
import Entities.*;
import Graphics.*;
import other.LevelLoader;

public class MenuManager {
    final private Game game;
    final private Soldier soldier;

    final private int width, height;

    private BufferedImage menu_image;

    private MenuState state;

    private int select = 0; // option selected in current menu

    public MenuManager(Game game) {
        this.game = game;
        this.soldier = game.soldier;

        width = game.width;
        height = game.height;

        // menu image
        BufferedImageLoader loader = new BufferedImageLoader();
        menu_image = loader.loadImage("/menu/menu.png");
        menu_image = SpriteSheet.resize(menu_image, width, height);

        state = MenuState.mainMenu;
    }

    public void render(Graphics g) {
        switch (state) {
            case mainMenu -> renderMainMenu(g);
            case pauseMenu -> renderPauseMenu(g);
            case aboutPage -> renderAboutPage(g);
            case levelFinished -> renderLevelFinished(g);
            case gameFinished -> renderGameFinished(g);
            case settingsMenu -> renderSettingsMenu(g);
        }
    }

    public void input(int key) {
        switch (state) {
            case mainMenu -> mainMenuInput(key);
            case pauseMenu -> pauseMenuInput(key);
            case aboutPage -> aboutPageInput(key);
            case levelFinished -> levelFinishedInput(key);
            case gameFinished -> gameFinishedInput(key);
            case settingsMenu -> settingsMenuInput(key);
        }
    }

    public void setState(MenuState s) {
        state = s;
        select = 0;
    }

    public MenuState getState() {
        return state;
    }

    // render functions
    private void renderMainMenu(Graphics g) {
        g.drawImage(menu_image, 0, 0, null);

        g.setFont(new Font("Ink Free", Font.BOLD, 70));
        g.setColor(Color.white);
        g.drawString("The Outbreak", width / 2 - 200, 150);

        String[] options = {
            "Start game",
            "Load game",
            "Settings",
            "About",
            "Exit"
        };

        g.setFont(new Font("Ink Free", Font.BOLD, 30));
        for (int i = 0; i < options.length; i++) {
            g.setColor(select == i ? Color.red : Color.white);
            g.drawString(options[i], width / 2 - 100, 300 + i*70);
        }
    }

    private void renderPauseMenu(Graphics g) {
        g.drawImage(menu_image, 0, 0, null);

        g.setFont(new Font("Ink Free", Font.BOLD, 70));
        g.setColor(Color.white);
        g.drawString("Pause", width/2-100 , 150);

        String[] options = {
                "Restart level",
                "Save game",
                "Back to main menu",
                "Exit"
        };

        g.setFont(new Font("Ink Free", Font.BOLD, 30));
        for (int i = 0; i < options.length; i++) {
            g.setColor(select == i ? Color.red : Color.white);
            g.drawString(options[i], width / 2 - 100, 300 + i*70);
        }
    }

    private void renderAboutPage(Graphics g) {
        g.drawImage(menu_image, 0, 0, null);

        g.setFont(new Font("Ink Free", Font.BOLD, 70));
        g.setColor(Color.white);
        g.drawString("About page", width / 2 - 200, 150);

        g.setFont(new Font("Ink Free", Font.BOLD, 20));
        g.drawString("           In this game you are a soldier during a zombie apocalypse. You have to recover the vaccine prototypes", 0, 250);
        g.drawString("     from different locations and save the world. Collect all vaccines from every level and survive until the", 0, 300);
        g.drawString("     helicopter comes to your rescue.", 0, 350);

        g.setFont(new Font("Ink Free", Font.BOLD, 30));
        g.drawString("    Controls", width/2-70, 400);

        g.setFont(new Font("Ink Free", Font.BOLD, 20));
        g.drawString("    W, A, S, D - movement", width/2-100, 450);
        g.drawString("    LMB - fire", width/2-100, 500);
        g.drawString("    R - reload", width/2-100, 550);
        g.drawString("    Q - change gun", width/2-100, 600);
    }

    private void renderLevelFinished(Graphics g) {
        if (game.won)
            renderLevelPassed(g);
        else
            renderLevelLost(g);
    }

    private void renderGameFinished(Graphics g) {
        g.drawImage(menu_image, 0, 0, null);

        g.setColor(Color.yellow);
        g.setFont(new Font("Ink Free", Font.BOLD, 60));
        g.drawString("Congratulations!", width/2-200, 100);
        g.drawString("You finished the game!", width/2-250, 200);

        g.setColor(Color.white);
        g.setFont(new Font("Ink Free", Font.BOLD, 50));
        g.drawString("Deaths: " + soldier.nrDeaths, width/2-150, 350);
        g.drawString("Total kills: " + soldier.totalKills, width/2-150, 450);

        g.setColor(Color.white);
        g.setFont(new Font("Ink Free", Font.BOLD, 30));
        g.drawString("Press space to return to main menu", width/3-80, height-80);
    }

    private void renderSettingsMenu(Graphics g) {
        g.drawImage(menu_image, 0, 0, null);

        g.setFont(new Font("Ink Free", Font.BOLD, 70));
        g.setColor(Color.white);
        g.drawString("Settings", width / 2 - 120, 150);

        g.setFont(new Font("Ink Free", Font.BOLD, 40));

        g.setColor(select == 0 ? Color.red : Color.white);
        g.drawString("Difficulty: " + (game.difficulty == 0 ? "easy" : (game.difficulty == 1) ? "normal" : "hard"), 100, 300);

        g.setColor(select == 1 ? Color.red : Color.white);
        g.drawString("Music: " + (game.musicOn ? "on" : "off"), 100, 400);

        g.setColor(select == 2 ? Color.red : Color.white);
        g.drawString("Sound: " + (game.soundOn ? "on" : "off"), 100, 500);
    }

    // input functions
    private void mainMenuInput(int key) {
        if (key == KeyEvent.VK_ENTER) {
            switch (select) {
                case 0:     // start game
                    game.levelIndex = 1;
                    game.levelLoader.load(game.levelIndex);
                    game.inMenu = false;
                    break;

                case 1:     // load game
                    game.loadGame();
                    break;

                case 2:     // settings
                    setState(MenuState.settingsMenu);
                    break;

                case 3:     // about
                    setState(MenuState.aboutPage);
                    break;

                case 4:     // exit
                    System.exit(0);
            }
        }

        menuNavigation(key, 5);
    }

    private void pauseMenuInput(int key) {
        if (key == KeyEvent.VK_ESCAPE) {
            game.inMenu = false;
            game.unmuteMusic();
            game.countDownTimer.start();
        }

        if (key == KeyEvent.VK_ENTER) {
            switch(select) {
                case 0:     // restart level
                    game.levelLoader.load(game.levelIndex);
                    game.inMenu = false;
                    break;

                case 1:     // save game
                    game.saveGame();
                    break;

                case 2:     // return to main menu
                    setState(MenuState.mainMenu);

                    game.countDownTimer.stop();

                    game.playMusic(0);  // plays menu music
                    break;

                case 3:     // exit game
                    System.exit(0);
                    break;
            }
        }

        menuNavigation(key, 4);
    }

    private void aboutPageInput(int key) {
        if (key == KeyEvent.VK_ESCAPE) {
            setState(MenuState.mainMenu);
        }
    }

    private void levelFinishedInput(int key) {
        if (key == KeyEvent.VK_SPACE) {
            // go to next level only if you win
            if (game.won) {
                game.levelIndex++;
            }

            // continue
            if (game.levelIndex < LevelLoader.nrLevels + 1) {
                game.levelLoader.load(game.levelIndex);
                game.inMenu = false;
            }
            else {
                setState(MenuState.gameFinished);
                game.playMusic(19);
            }
        }
    }

    private void gameFinishedInput(int key) {
        if (key == KeyEvent.VK_SPACE) {
            setState(MenuState.mainMenu);

            game.playMusic(0);  // play menu music
        }
    }

    private void settingsMenuInput(int key) {
        if (key == KeyEvent.VK_ESCAPE) {
            setState(MenuState.mainMenu);
        }

        if (key == KeyEvent.VK_ENTER) {
            switch (select) {
                case 0:     // Difficulty
                    game.difficulty++;
                    if (game.difficulty > 2) {
                        game.difficulty = 0;
                    }
                    break;

                case 1:     // Music
                    game.musicOn = !game.musicOn;
                    if (game.musicOn)
                        game.playMusic(0);
                    else
                        game.stopMusic();
                    break;

                case 2:     // Sound
                    game.soundOn = !game.soundOn;
                    break;
            }
        }

        menuNavigation(key, 3);
    }

    // other
    private void menuNavigation(int key, int nrOptions) {
        if ((key == KeyEvent.VK_W || key == KeyEvent.VK_UP) && select > 0) {
            select--;
            game.playSoundEffect(9);
        }

        if ((key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) && select < nrOptions-1) {
            select++;
            game.playSoundEffect(9);
        }
    }

    private void renderLevelPassed(Graphics g) {
        g.setColor(Color.black);
        //g.fillRect(0, 0, 1900, 800);
        g.drawImage(menu_image, 0, 0, null);

        g.setColor(Color.ORANGE);
        g.setFont(new Font("Ink Free", Font.BOLD, 60));
        g.drawString("You passed the Level!", width / 4, height / 4);

        // kills
        g.setColor(Color.red);
        g.drawString("Kills: " + soldier.kills, width / 2 - 60, height/4+200);

        // vaccines collected
        g.setColor(Color.red);
        g.drawString("Vaccines: " + soldier.vaccinesCollected + " / " + soldier.vaccinesToCollect, width / 2 - 120, height/4+300);

        // rifle unlock
        if (game.levelIndex == 1) {
            g.setColor(Color.orange);
            g.setFont(new Font("Ink Free", Font.BOLD, 30));
            g.drawString("You unlocked the rifle!", width / 3 + 50, height/4 + 100);
        }

        g.setColor(Color.white);
        g.setFont(new Font("Ink Free", Font.BOLD, 30));
        g.drawString("Press space to continue", width/3 + 40, height-80);
    }

    private void renderLevelLost(Graphics g) {
        g.drawImage(menu_image, 0, 0, null);

        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 60));
        g.drawString("You Lost!", width / 2 - 100, height / 4);

        // kills
        g.drawString("Kills: " + soldier.kills, width / 2 - 60, height/4+200);

        // vaccines collected
        g.setColor(Color.red);
        g.drawString("Vaccines: " + soldier.vaccinesCollected + " / " + soldier.vaccinesToCollect, width / 2 - 120, height/4+300);

        g.setColor(Color.white);
        g.setFont(new Font("Ink Free", Font.BOLD, 30));
        g.drawString("Press space to restart", width/3 + 40, height-80);
    }
}
