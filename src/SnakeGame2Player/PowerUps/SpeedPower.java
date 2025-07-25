package SnakeGame2Player.PowerUps;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import SnakeGame2Player.*;
import java.awt.Color;

/**
 *
 * @author marvi
 */
public class SpeedPower extends PowerUp {
    
    public SpeedPower(int x, int y, Handler handler) {
        super(x, y, ID.SpeedPower, handler);
        
        color = Color.ORANGE;
    }
    
}
