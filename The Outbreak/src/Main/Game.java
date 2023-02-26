package Main;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import Entities.*;
import Entities.Zombies.*;
import Exceptions.InvalidAudioException;
import Graphics.*;
import Inputs.*;
import Menu.*;
import Sound.*;
import other.*;

public class Game extends Canvas implements Runnable {
    // game status
    private boolean isRunning = false;
    public boolean won = false;              // level won

    private Thread thread;
    final private Handler handler;
    final private Camera camera;
    final private SpriteSheet ss;
    final private Sound sound;

    public int width = 1000;
    public int height = 700;

    // sound
    public boolean soundOn = true, musicOn = true;

    public int spriteSize = 128;

    public BufferedImage map = null;

    // frames per second
    private int fps;

    // timer
    public CountDownTimer countDownTimer;

    // spawner
    final private ObjectSpawner objectSpawner;

    // level loader
    public LevelLoader levelLoader;
    public int levelIndex;

    final private HUD hud;

    public Soldier soldier;

    public MenuManager menuManager;
    public boolean inMenu = true;

    final private SaveManager saveManager;

    // 0 = easy, 1 = normal, 2 = hard
    public int difficulty = 1;

    // ======================================
    public static void main(String[] args) {
        new Game();
    }

    public Game() {
        new Window(width, height, "The Outbreak", this);

        start();

        sound = new Sound();
        playSoundEffect(9);

        handler = new Handler();

        camera = new Camera(0, 0, this);

        // sprite sheet
        BufferedImageLoader loader = new BufferedImageLoader();
        BufferedImage sprite_sheet = loader.loadImage("/sprites/sprite_sheet.png");
        ss = new SpriteSheet(sprite_sheet, spriteSize);

        // load images for soldier and zombie class
        NormalZombie.loadImages(ss, spriteSize);
        BabyZombie.loadImages(ss, spriteSize);
        Soldier.loadImages(ss, spriteSize);

        // add soldier
        handler.addSoldier(Soldier.getInstance(ID.Player, handler, this, ss, camera));
        soldier = handler.getSoldier();

        // HUD
        hud = HUD.getInstance(this, ss, soldier);

        menuManager = new MenuManager(this);

        saveManager = new SaveManager(this, handler);

        this.addKeyListener(new KeyInput(this, soldier));
        this.addMouseListener(new MouseInput(this, soldier));

        countDownTimer = new CountDownTimer(this, soldier);

        levelLoader = new LevelLoader(this, handler, ss);

        objectSpawner = new ObjectSpawner(this, handler, camera, ss);

        playMusic(0);
    }

    private void start() {
        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    private void stop() {
        isRunning = false;
        try {
            thread.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Game loop
    public void run() {
        this.requestFocus();
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;

        while (isRunning) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                if (!inMenu) {
                    tick();
                }
                frames++;
                delta--;
            }
            render();

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                fps = frames;
                frames = 0;
            }
        }
        stop();
    }

    // updates everything in the game
    public void tick() {

        // spawners based on the level
        if (objectSpawner != null) {
            if (levelIndex != LevelLoader.nrLevels)
                objectSpawner.spawnZombie(1.1 - difficulty/0.1, levelIndex * ((difficulty*2)+1));

            objectSpawner.spawnAmmoCrate(10 - levelIndex*2, levelIndex);
            objectSpawner.spawnHealthCrate(10 - levelIndex*2, levelIndex);
        }

        // makes camera follow the player
        camera.tick(soldier);

        // updates all GameObjects
        handler.tick();
    }

    // renders everything in the game
    public void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }

        //============= Area for rendering to screen =================================
        Graphics g = bs.getDrawGraphics();
        if (menuManager == null) {
            renderLoadingScreen(g);
        }
        else if (inMenu) {
            menuManager.render(g);
        }
        else {  // if in game
            renderGame(g);
        }
        //============= Exit area for rendering to screen ===============================

        g.dispose();
        bs.show();
    }

    public void saveGame() {
        saveManager.save();
    }

    public void loadGame() {
        saveManager.load();
    }

    public void playMusic(int i) {
        try {
            stopMusic();
            if (musicOn)
                sound.playMusic(i); // plays selected music
        }
        catch (InvalidAudioException e) {
            System.out.println(e);
        }
    }

    public void stopMusic() {
        sound.stopMusic();  // stop all music
    }

    public void muteMusic() {
        if (musicOn)
            sound.music.stop();
    }

    public void unmuteMusic() {
        if (musicOn)
            sound.music.start();
    }

    public void playSoundEffect(int i) {
        try {
            if (soundOn)
                sound.playSoundEffect(i);
        }
        catch (InvalidAudioException e) {
            System.out.println(e);
        }
    }

    // Render functions ===========================

    public void renderGame(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.translate(-camera.getX(), -camera.getY());

        // background
        g.drawImage(map, 0, 0, null);

        // render all GameObjects
        handler.render(g);

        g2d.translate(camera.getX(), camera.getY());

        // show fps
        g.setColor(Color.black);
        g.setFont(new Font("Ink Free", Font.BOLD, 10));
        g.drawString("fps: " + fps, width - 100, 20);

        hud.render(g);
    }

    public void renderLoadingScreen(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, 1000, 1000);
        g.setFont(new Font("Ink Free", Font.BOLD, 70));
        g.setColor(Color.white);
        g.drawString("Loading ...", width/2-150, height/2);
    }
}