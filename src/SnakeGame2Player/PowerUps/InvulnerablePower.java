/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SnakeGame2Player.PowerUps;

import SnakeGame2Player.*;
import java.awt.Color;

/**
 *
 * @author marvi
 */
public class InvulnerablePower extends PowerUp {
    
    public InvulnerablePower(int x, int y, Handler handler) {
        super(x, y, ID.InvulnerablePower, handler);
        
        color = Color.GREEN;
    }
    
}
