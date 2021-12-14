/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SnakeGame2Player;

import static SnakeGame2Player.Game.HEIGHT;
import static SnakeGame2Player.Game.WIDTH;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marvi
 */
public class KeyInput extends KeyAdapter {

    private Handler handler;
    private Random r = new Random();

    public KeyInput(Handler handler) {
        this.handler = handler;
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        for (int i = 0; i < handler.object.size(); i++) {
            GameObject tempObject = handler.object.get(i);

            if (tempObject.getId() == ID.Head) {
                Head headObject = (Head) tempObject;
                // key events for player 1
                if (headObject.getPlayer() == Player.BLUE) {
                    switch (key) {
                        case KeyEvent.VK_W:
                            headObject.goUp();
                            break;
                        case KeyEvent.VK_S:
                            headObject.goDown();
                            break;
                        case KeyEvent.VK_A:
                            headObject.goLeft();
                            break;
                        case KeyEvent.VK_D:
                            headObject.goRight();
                            break;
                    }
                }
                // key events for player 2
                if (headObject.getPlayer() == Player.RED) {
                    switch (key) {
                        case KeyEvent.VK_UP:
                            headObject.goUp();
                            break;
                        case KeyEvent.VK_DOWN:
                            headObject.goDown();
                            break;
                        case KeyEvent.VK_LEFT:
                            headObject.goLeft();
                            break;
                        case KeyEvent.VK_RIGHT:
                            headObject.goRight();
                            break;
                    }
                }
            }
        }

        if (key == KeyEvent.VK_P) {
            restart();
        }
        if (key == KeyEvent.VK_ESCAPE) {
            System.exit(1);
        }
    }

    private void setPlayerDirection(Head player, Heading direction) {
        player.changeDirection(direction);
        if (Game.gameState == GameState.IDLE) {
            Game.gameState = GameState.PLAYING;
        }
        try {
            Thread.sleep(70);
        } catch (InterruptedException ex) {
            Logger.getLogger(KeyInput.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void restart() {
        handler.clearObject();
        Game.playerBlue = new Head(50, Game.HEIGHT / 2, Player.BLUE, handler);
        handler.addObject(Game.playerBlue);
        Game.playerBlue.setAlive(true);

        Game.playerRed = new Head(Game.WIDTH - 50, Game.HEIGHT / 2, Player.RED, handler);
        handler.addObject(Game.playerRed);
        Game.playerRed.setAlive(true);

        Game.food = new Food(r.nextInt(WIDTH - 22), r.nextInt(HEIGHT - 54), handler);
        handler.addObject(Game.food);

        Game.ticksAfterDeath = 0;

        Game.gameState = GameState.IDLE;
    }
}
