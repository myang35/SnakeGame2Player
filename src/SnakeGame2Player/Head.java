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
    private int size;
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
    private int ticksSinceLastTurn;
    private Heading pendingAction;

    public Head(int x, int y, Player player, Handler handler) {
        super(x, y, ID.Head);
        this.handler = handler;
        this.size = 16;
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
        return new Rectangle(x, y, size, size);
    }

    public void tick() {
        prevX.add(x);
        prevY.add(y);
        
        if (!alive) {
            prevX.remove();
            prevY.remove();
            return;
        }
        
        if (prevX.size() > (bodyNum + 1) * (size/vel + 3)) {
            prevX.remove();
        }
        if (prevY.size() > (bodyNum + 1) * (size/vel + 3)) {
            prevY.remove();
        }
        
        if (pendingAction != null && canTurn()) {
            goDirection(pendingAction);
        }

        x += velX;
        y += velY;
        
        if (x > Game.WIDTH - size/2 - 16) {
            x = -size/2;
        }
        if (x < -size/2) {
            x = Game.WIDTH - size/2 - 16;
        }
        if (y > Game.HEIGHT - size/2 - 39) {
            y = -size/2;
        }
        if (y < -size/2) {
            y = Game.HEIGHT - size/2 - 39;
        }

        collision();
        powerEffect(powerType);
        
        this.ticksSinceLastTurn++;
    }
    
    public void render(Graphics g) {
        g.setColor(color);

        if (vel == 0) {
            g.setFont(new Font("ARIEL", 0, 10));
            g.drawRect(x, y, size, size);
        } else {
            g.fillRect(x, y, size, size);
        }

    }
    
    public void goDirection(Heading heading) {
        switch (heading) {
            case NORTH:
                goUp();
                break;
            case SOUTH:
                goDown();
                break;
            case WEST:
                goLeft();
                break;
            case EAST:
                goRight();
                break;
        }
    }
    
    public void goUp() {
        if (heading == Heading.SOUTH) {
            return;
        }
        
        if (pendingAction == Heading.NORTH || (pendingAction == null && canTurn())) {
            heading = Heading.NORTH;
            velY = -vel;
            velX = 0;
            ticksSinceLastTurn = 0;
            pendingAction = null;
            return;
        }
        
        if (pendingAction == null && !canTurn() && heading != Heading.NORTH) {
            pendingAction = Heading.NORTH;
            return;
        }
    }
    
    public void goDown() {
        if (heading == Heading.NORTH) {
            return;
        }
        
        if (pendingAction == Heading.SOUTH || (pendingAction == null && canTurn())) {
            heading = Heading.SOUTH;
            velY = vel;
            velX = 0;
            ticksSinceLastTurn = 0;
            pendingAction = null;
            return;
        }
        
        if (pendingAction == null && !canTurn() && heading != Heading.SOUTH) {
            pendingAction = Heading.SOUTH;
        }
    }
    
    public void goLeft() {
        if (heading == Heading.EAST) {
            return;
        }
        
        if (pendingAction == Heading.WEST || (pendingAction == null && canTurn())) {
            heading = Heading.WEST;
            velX = -vel;
            velY = 0;
            ticksSinceLastTurn = 0;
            pendingAction = null;
            return;
        }
        
        if (pendingAction == null && !canTurn() && heading != Heading.WEST) {
            pendingAction = Heading.WEST;
            return;
        }
    }
    
    public void goRight() {
        if (heading == Heading.WEST) {
            return;
        }
        
        if (pendingAction == Heading.EAST || (pendingAction == null && canTurn())) {
            heading = Heading.EAST;
            velX = vel;
            velY = 0;
            ticksSinceLastTurn = 0;
            pendingAction = null;
            return;
        }
        
        if (pendingAction == null && !canTurn() && heading != Heading.EAST) {
            pendingAction = Heading.EAST;
            return;
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
    
    public void changeDirection() {
        switch (heading) {
            case NORTH:
                velX = 0;
                velY = -vel;
                break;
            case SOUTH:
                velX = 0;
                velY = vel;
                break;
            case WEST:
                velX = -vel;
                velY = 0;
                break;
            case EAST:
                velX = vel;
                velY = 0;
                break;
            default:
                System.out.println("Error: invalid heading");
        }
    }
    
    public void changeDirection(Heading heading) {
        this.heading = heading;
        changeDirection();
    }
    
    private boolean canTurn() {
        return ticksSinceLastTurn * vel >= size;
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