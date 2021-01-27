package SnakeGame2Player;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

/**
 *
 * @author marvi
 */
public class Head extends GameObject {

    private Random r = new Random();
    private Handler handler;
    private Heading heading;
    private int vel;
    private Player player;
    private LinkedList<Integer> prevX = new LinkedList<>();
    private LinkedList<Integer> prevY = new LinkedList<>();
    
    private int bodyNum;
    private ID powerType;
    private boolean poweredUp;
    private int numTicks;
    private boolean alive;
    private boolean invulnerable;

    public Head(int x, int y, Player player, Handler handler) {
        super(x, y, ID.Head);
        this.handler = handler;
        this.vel = 4;
        this.bodyNum = 0;
        this.alive = true;
        this.player = player;

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
        prevX.add(x);
        prevY.add(y);

        x += velX;
        y += velY;
        
        if (x > Game.WIDTH - 11) {
            x = -11;
        }
        if (x < -11) {
            x = Game.WIDTH - 11;
        }
        if (y > Game.HEIGHT - 43) {
            y = -11;
        }
        if (y < -11) {
            y = Game.HEIGHT - 43;
        }

        collision();
        powerEffect(powerType);
    }
    
    public void render(Graphics g) {
        g.setColor(color);

        if (vel == 0) {
            g.setFont(new Font("ARIEL", 0, 10));
            g.drawRect(x, y, 16, 16);
        } else {
            g.fillRect(x, y, 16, 16);
        }

    }

    private void collision() {
        for (int i = 0; i < handler.object.size(); i++) {

            GameObject tempObject = handler.object.get(i);

            // create new body when head eats food
            if (tempObject.getId() == ID.Food) {
                if (getBounds().intersects(tempObject.getBounds())) {
                    bodyNum++;
                    handler.addObject(new Body(prevX, prevY, bodyNum, player, handler));
                }
            }

            // stop moving when head hits body
            if (tempObject.getId() == ID.Body) {
                if (getBounds().intersects(tempObject.getBounds()) && !invulnerable) {
                    velX = 0;
                    velY = 0;
                    vel = 0;
                    alive = false;
                }
            }
            
            if (tempObject.getId() == ID.SpeedPower) {
                if (getBounds().intersects(tempObject.getBounds())) {
                    powerType = ID.SpeedPower;
                    poweredUp = true;
                    numTicks = 0;
                }
            }
            
            if (tempObject.getId() == ID.InvulnerablePower) {
                if (getBounds().intersects(tempObject.getBounds())) {
                    powerType = ID.InvulnerablePower;
                    poweredUp = true;
                    numTicks = 0;
                }
            }
        }
    }

    private void powerEffect(ID power) {
        if (poweredUp && alive) {
            switch(power) {
                case SpeedPower:
                    // disable other power ups
                    disableInvulnerablePower();
                    
                    // implement power up
                    if (heading == Heading.NORTH) velY = -6;
                    if (heading == Heading.SOUTH) velY = 6;
                    if (heading == Heading.EAST) velX = 6;
                    if (heading == Heading.WEST) velX = -6;
                    vel = 6;
                    break;
                case InvulnerablePower:
                    // disable other power ups
                    disableSpeedPower();
                    
                    // implement power up
                    invulnerable = true;
                    break;
                default:
                    System.out.println("Error: invalid power ID");
            }
            
            // disable power up after 500 ticks (approximately 10 seconds)
            if (numTicks == 500) {
                disableSpeedPower();
                disableInvulnerablePower();
                poweredUp = false;
                numTicks = 0;
            }
            numTicks++;
            
        }
    }
    
    private void disableSpeedPower() {
        if (heading == Heading.NORTH) velY = -4;
        if (heading == Heading.SOUTH) velY = 4;
        if (heading == Heading.EAST) velX = 4;
        if (heading == Heading.WEST) velX = -4;
        vel = 4;
    }
    
    private void disableInvulnerablePower() {
        invulnerable = false;
    }
    
    public void setHeading(Heading heading) {
        this.heading = heading;
    }
    
    public Heading getHeading() {
        return heading;
    }
    
    public void setVelocity(int vel) {
        this.vel = vel;
    }
    
    public int getVelocity() {
        return vel;
    }

    public LinkedList<Integer> getPrevX() {
        return prevX;
    }

    public LinkedList<Integer> getPrevY() {
        return prevY;
    }

    public int getVel() {
        return vel;
    }

    public void setVel(int vel) {
        this.vel = vel;
    }

    public boolean isPoweredUp() {
        return poweredUp;
    }

    public void setPoweredUp(boolean poweredUp) {
        this.poweredUp = poweredUp;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public int getNumBody() {
        return bodyNum;
    }

    public int getNumTicks() {
        return numTicks;
    }

    public ID getPowerType() {
        return powerType;
    }

    public Player getPlayer() {
        return player;
    }
    
    
}