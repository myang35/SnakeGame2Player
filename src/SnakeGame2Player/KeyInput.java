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
        int sleepTime = 70;

        for (int i = 0; i < handler.object.size(); i++) {
            GameObject tempObject = handler.object.get(i);

            if (tempObject.getId() == ID.Head) {
                Head headObject = (Head) tempObject;
                // key events for player 1
                if (headObject.getPlayer() == Player.BLUE) {
                    int vel = headObject.getVelocity();
                    try {
                        if (key == KeyEvent.VK_W && headObject.getHeading() != Heading.SOUTH) {
                            headObject.setHeading(Heading.NORTH);
                            headObject.setVelY(-vel);
                            headObject.setVelX(0);
                            Game.playing = true;
                            Thread.sleep(sleepTime);
                        }
                        if (key == KeyEvent.VK_S && headObject.getHeading() != Heading.NORTH) {
                            headObject.setHeading(Heading.SOUTH);
                            headObject.setVelY(vel);
                            headObject.setVelX(0);
                            Game.playing = true;
                            Thread.sleep(sleepTime);
                        }
                        if (key == KeyEvent.VK_A && headObject.getHeading() != Heading.EAST) {
                            headObject.setHeading(Heading.WEST);
                            headObject.setVelX(-vel);
                            headObject.setVelY(0);
                            Game.playing = true;
                            Thread.sleep(sleepTime);
                        }
                        if (key == KeyEvent.VK_D && headObject.getHeading() != Heading.WEST) {
                            headObject.setHeading(Heading.EAST);
                            headObject.setVelX(vel);
                            headObject.setVelY(0);
                            Game.playing = true;
                            Thread.sleep(sleepTime);
                        }
                    } catch (InterruptedException ie) {
                        System.out.println(ie);
                    }
                }
                // key events for player 2
                if (headObject.getPlayer() == Player.RED) {
                    int vel = headObject.getVelocity();
                    try {
                        if (key == KeyEvent.VK_UP && headObject.getHeading() != Heading.SOUTH) {
                            headObject.setHeading(Heading.NORTH);
                            headObject.setVelY(-vel);
                            headObject.setVelX(0);
                            Game.playing = true;
                            Thread.sleep(sleepTime);
                        }
                        if (key == KeyEvent.VK_DOWN && headObject.getHeading() != Heading.NORTH) {
                            headObject.setHeading(Heading.SOUTH);
                            headObject.setVelY(vel);
                            headObject.setVelX(0);
                            Game.playing = true;
                            Thread.sleep(sleepTime);
                        }
                        if (key == KeyEvent.VK_LEFT && headObject.getHeading() != Heading.EAST) {
                            headObject.setHeading(Heading.WEST);
                            headObject.setVelX(-vel);
                            headObject.setVelY(0);
                            Game.playing = true;
                            Thread.sleep(sleepTime);
                        }
                        if (key == KeyEvent.VK_RIGHT && headObject.getHeading() != Heading.WEST) {
                            headObject.setHeading(Heading.EAST);
                            headObject.setVelX(vel);
                            headObject.setVelY(0);
                            Game.playing = true;
                            Thread.sleep(sleepTime);
                        }
                    } catch (InterruptedException ie) {
                        System.out.println(ie);
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
        Game.blueWin = false;
        Game.redWin = false;
        Game.tie = false;
        
        Game.playing = false;
    }
}
