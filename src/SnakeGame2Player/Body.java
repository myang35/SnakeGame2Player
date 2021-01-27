/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SnakeGame2Player;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.Random;

/**
 *
 * @author marvi
 */
public class Body extends GameObject {

    private Random r = new Random();
    private Handler handler;
    private Player player;
    int bodyNum;
    private LinkedList<Integer> prevX, prevY;

    public Body(LinkedList<Integer> prevX, LinkedList<Integer> prevY, int bodyNum, Player player, Handler handler) {
        super(prevX.get(prevX.size() - 7), prevY.get(prevY.size() - 7), ID.Body);
        this.handler = handler;
        this.bodyNum = bodyNum;
        this.player = player;
        this.prevX = prevX;
        this.prevY = prevY;

        if (player == Player.BLUE) {
            color = Color.BLUE;
        } else {
            color = Color.RED;
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 16, 16);
    }

    public void tick() {
        x = prevX.get(prevX.size() - 6 * bodyNum);
        y = prevY.get(prevY.size() - 6 * bodyNum);
    }

    public void render(Graphics g) {
        g.setColor(color);
        g.setFont(new Font("ARIEL", 0, 10));

        if (player == Player.BLUE && Game.playerBlue.getVelocity() == 0
                || player == Player.RED && Game.playerRed.getVelocity() == 0) {
            g.drawRect(x, y, 16, 16);
        } else {
            g.fillRect(x, y, 16, 16);
        }
    }

}
