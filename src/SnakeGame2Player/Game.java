package SnakeGame2Player;

import SnakeGame2Player.PowerUps.*;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.Random;

/**
 *
 * @author marvi
 */
public class Game extends Canvas implements Runnable {

    public static final int WIDTH = 640, HEIGHT = WIDTH;

    private Thread thread;
    private boolean running = false;
    private int numTicks;
    public static int ticksAfterDeath;
    public static GameState gameState = GameState.IDLE;

    private Random r = new Random();
    private Handler handler;
    public static Head playerBlue;
    public static Head playerRed;
    public static Food food;

    public Game() {
        handler = new Handler();

        playerBlue = new Head(50, HEIGHT / 2, Player.BLUE, handler);
        handler.addObject(playerBlue);

        playerRed = new Head(WIDTH - 50, HEIGHT / 2, Player.RED, handler);
        handler.addObject(playerRed);

        food = new Food(handler);
        handler.addObject(food);

        this.addKeyListener(new KeyInput(handler));

        new Window(WIDTH, HEIGHT, "Snake Game", this);
    }

    public synchronized void start() {
        thread = new Thread(this);
        thread.start();
        running = true;
    }

    public synchronized void stop() {
        try {
            thread.join();
            running = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        this.requestFocus();
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                tick();
                delta--;
            }
            if (running) {
                render();
            }
            frames++;

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                System.out.println("FPS: " + frames);
                frames = 0;
            }
        }
        stop();
    }

    private void tick() {
        handler.tick();
        createPowerUp();
    }

    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        // background
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // show blue score
        g.setFont(new Font("ARIEL", 0, 20));
        int bluePoints = playerBlue.getNumBody();
        g.setColor(Color.BLUE);
        g.drawString("pts: " + bluePoints, 10, 25);

        // show red score
        // when number gets bigger, shift to the left a little
        int redPoints = playerRed.getNumBody();
        g.setColor(Color.RED);
        if (redPoints < 10) {
            g.drawString("pts: " + redPoints, WIDTH - 65, 25);
        } else if (redPoints < 100) {
            g.drawString("pts: " + redPoints, WIDTH - 75, 25);
        } else {
            g.drawString("pts: " + redPoints, WIDTH - 85, 25);
        }
        
        // show timer for red player's powerUp
        if (playerBlue.isPoweredUp()) {
            int timer = 10 - playerBlue.getNumTicks() / 50;
            
            switch (playerBlue.getPowerType()) {
                case SpeedPower:
                    g.setColor(Color.RED);
                    break;
                case InvulnerablePower:
                    g.setColor(Color.GREEN);
                    break;
                default:
                    System.out.println("Error: invalid powerType");
            }
            g.setFont(new Font("ARIEL", 0, 25));
            g.drawString(""+timer, 100, 25);
        }
        
        // show timer for red player's powerUp
        if (playerRed.isPoweredUp()) {
            int timer = 10 - playerRed.getNumTicks() / 50;
            
            switch (playerRed.getPowerType()) {
                case SpeedPower:
                    g.setColor(Color.RED);
                    break;
                case InvulnerablePower:
                    g.setColor(Color.GREEN);
                    break;
                default:
                    System.out.println("Error: invalid powerType");
            }
            g.setFont(new Font("ARIEL", 0, 25));
            g.drawString(""+timer, WIDTH - 125, 25);
        }
        
        // if only red player dies
        if (playerBlue.isAlive() && !playerRed.isAlive() || gameState == GameState.BLUEWIN) {
            gameState = GameState.BLUEWIN;
            
            if (ticksAfterDeath > 50) {
                g.setFont(new Font("ARIEL", 0, 50));
                g.setColor(Color.BLUE);
                g.drawString("Blue player wins!", WIDTH / 2 - 190, HEIGHT / 2 - 30);
                g.setFont(new Font("ARIEL", 0, 25));
                g.setColor(Color.WHITE);
                g.drawString("Press p to play again", WIDTH / 2 - 115, HEIGHT / 2 + 30);
            }
            ticksAfterDeath++;
        }

        // if only blue player dies
        if (!playerBlue.isAlive() && playerRed.isAlive() || gameState == GameState.REDWIN) {
            gameState = GameState.REDWIN;
            
            if (ticksAfterDeath > 50) {
                g.setFont(new Font("ARIEL", 0, 50));
                g.setColor(Color.RED);
                g.drawString("Red player wins!", WIDTH / 2 - 190, HEIGHT / 2 - 30);
                g.setFont(new Font("ARIEL", 0, 25));
                g.setColor(Color.WHITE);
                g.drawString("Press p to play again", WIDTH / 2 - 115, HEIGHT / 2 + 30);
            }
            ticksAfterDeath++;
        }

        // if both players die
        if (!playerBlue.isAlive() && !playerRed.isAlive()) {
            if (ticksAfterDeath <= 50 || gameState == GameState.TIE) {
                gameState = GameState.TIE;
                g.setFont(new Font("ARIEL", 0, 50));
                g.setColor(Color.WHITE);
                g.drawString("TIED!", WIDTH / 2 - 60, HEIGHT / 2 - 30);
                g.setFont(new Font("ARIEL", 0, 25));
                g.drawString("Press p to play again", WIDTH / 2 - 115, HEIGHT / 2 + 30);
            }
        }

        handler.render(g);

        g.dispose();
        bs.show();
    }
    
    private void createPowerUp() {
        if (gameState != GameState.PLAYING) {
            numTicks = 0;
        } else {
            if (numTicks == 300) {
                PowerUp powerUp;
                int randNum = r.nextInt(2);
                switch (randNum) {
                    case 0:
                        powerUp = new SpeedPower(r.nextInt(WIDTH-22), r.nextInt(HEIGHT-54), handler);
                        break;
                    case 1:
                        powerUp = new InvulnerablePower(r.nextInt(WIDTH-22), r.nextInt(HEIGHT-54), handler);
                        break;
                    default:
                        System.out.println("Error: invalid random number (" + randNum + ")");
                        powerUp = new SpeedPower(r.nextInt(WIDTH-22), r.nextInt(HEIGHT-54), handler);
                }
                handler.addObject(powerUp);
                numTicks = 0;
            }
            numTicks++;
        }
    }

    public static void main(String args[]) {
        new Game();
    }
}
