/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SnakeGame2Player.PowerUps;

import SnakeGame2Player.Handler;
import SnakeGame2Player.ID;
import SnakeGame2Player.PowerUp;
import java.awt.Color;

/**
 *
 * @author myang
 */
public class GrowPower extends PowerUp {
    
    public GrowPower(int x, int y, Handler handler) {
        super(x, y, ID.GrowPower, handler);
        
        color = Color.MAGENTA;
    }
}
