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
    private int size;

    public Body(LinkedList<Integer> prevX, LinkedList<Integer> prevY, int bodyNum, Player player, Handler handler) {
        super(prevX.get(prevX.size() - 7), prevY.get(prevY.size() - 7), ID.Body);
        this.handler = handler;
        this.bodyNum = bodyNum;
        this.player = player;
        this.prevX = prevX;
        this.prevY = prevY;
        this.size = 16;

        if (player == Player.BLUE) {
            color = Color.BLUE;
        } else {
            color = Color.RED;
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, size, size);
    }

    public void tick() {
        final int GROW_MULT = 2;
        
        if (hasGrowPower()) {
            size = 16 * GROW_MULT;
        } else {
            size = 16;
        }
        
        int xPos = prevX.size() - 6 * bodyNum * (hasGrowPower() ? GROW_MULT : 1);
        x = xPos >= 0 ? prevX.get(xPos) : prevX.getFirst();
        
        int yPos = prevY.size() - 6 * bodyNum * (hasGrowPower() ? GROW_MULT : 1);
        y = yPos >= 0 ? prevY.get(yPos) : prevY.getFirst();
    }

    public void render(Graphics g) {
        g.setColor(color);
        g.setFont(new Font("ARIEL", 0, 10));

        if (player == Player.BLUE && Game.playerBlue.getVelocity() == 0
                || player == Player.RED && Game.playerRed.getVelocity() == 0) {
            g.drawRect(x, y, size, size);
        } else {
            g.fillRect(x, y, size, size);
        }
    }
    
    private boolean hasGrowPower() {
        return player == Player.BLUE && Game.playerBlue.getPowerType() == ID.GrowPower
                || player == Player.RED && Game.playerRed.getPowerType() == ID.GrowPower;
    }

}
