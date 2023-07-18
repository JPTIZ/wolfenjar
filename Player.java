//=============================================================================
// Player
//-----------------------------------------------------------------------------
// Manages player movement/behaviour, to be used with Renderer and Raycaster
// implementations.
//=============================================================================

import java.lang.Math;
import javax.swing.Timer;
import java.util.ArrayList;
import java.io.Serializable;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Player extends Levels implements KeyListener, MouseMotionListener, Serializable {

    private static final long serialVersionUID = 6938029690972740389L;

    private double x;
    private double y;
    public  double dir;  // Angle mod 360

    private double strafe_speed;
    private double turn_speed;

    // New Stuff
    private boolean locked; // When true, prevent player from moving
    private Inventory inventory;
    private int lastX = -1, lastY = -1;

    private ArrayList<Integer> pressedKeys;

    Player(double x, double y, double strafe_speed, double turn_speed) {
        this.x = x;
        this.y = y;

        // Initialize with Given Values
        this.strafe_speed = (double) 0.5;  // Blocks per Second
        this.turn_speed   = (double) Raycaster.ANGULO6/2;  // Degrees per Second
        this.dir          = (double) 0;

        this.pressedKeys = new ArrayList<>();

        Timer timer = new Timer(17, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                update();
            }
        });
        timer.start();

        this.inventory = new Inventory();
    }

    public void cloneFrom(Player player) {
        this.x = player.getX();
        this.y = player.getY();
        this.dir = player.dir;
        if (player.isLocked()) this.lock(); else this.unlock();
        this.inventory = player.inventory;
        this.lastX = (int)player.getX();
        this.lastY = (int)player.getY();
    }


    // Times are in seconds
    public void strafe(double delta, int keycode) {
        if (keycode == KeyEvent.VK_W) Game.log("should move");
        if (this.isLocked()) return;
        double novoX = getX();
        double novoY = getY();
        if (pressedKeys.contains(KeyEvent.VK_W)) {
            novoX += Math.cos(Raycaster.toRad((int)dir)) * (strafe_speed * delta);
            novoY += Math.sin(Raycaster.toRad((int)dir)) * (strafe_speed * delta);
            Game.renderer.startBobbing();
        } else {
            Game.renderer.stopBobbing();
        }
        Game.renderer.updateBobbing();
        if (pressedKeys.contains(KeyEvent.VK_S)) {
            novoX -= Math.cos(Raycaster.toRad((int)dir)) * (strafe_speed * delta);
            novoY -= Math.sin(Raycaster.toRad((int)dir)) * (strafe_speed * delta);
        }
        if (pressedKeys.contains(KeyEvent.VK_A)) {
            novoX += Math.sin(Raycaster.toRad((int)dir+90)) * (strafe_speed * delta);
            novoY -= Math.cos(Raycaster.toRad((int)dir+90)) * (strafe_speed * delta);
        }
        if (pressedKeys.contains(KeyEvent.VK_D)) {
            novoX -= Math.sin(Raycaster.toRad((int)dir+90)) * (strafe_speed * delta);
            novoY += Math.cos(Raycaster.toRad((int)dir+90)) * (strafe_speed * delta);
        }
        if (Game.NO_CLIPPING) {
            this.x = novoX;
            this.y = novoY;
        } else {
            double t = 0.1;
            // Verifica trocas em X
            if (!(Levels.map[(int)(this.getY())][(int)(novoX-t)] > 0 || Levels.map[(int)(this.getY())][(int)(novoX+t)] > 0)) {
                this.x = novoX;
            }
            // Verifica trocas em Y
            if (!(Levels.map[(int)(novoY-t)][(int)(this.getX())] > 0 || Levels.map[(int)(novoY+t)][(int)(this.getX())] > 0)) {
                this.y = novoY;
            }
        }
        for (Key key : Levels.keys) {
            if (!key.pickedUp()) {
                key.update(this);
                boolean col0 = (Math.abs(this.x) >= (double)key.x-0.25);
                boolean col1 = (Math.abs(this.x) <= (double)key.x+0.25);
                boolean col2 = (Math.abs(this.y) >= (double)key.y-0.25);
                boolean col3 = (Math.abs(this.y) <= (double)key.y+0.25);
                if (col0 && col1 && col2 && col3) {
                    key.pickUp();
                    try {
                        this.inventory.addItem(key);
                        if (this.inventory.count() == 1) Game.changeState(new VictoryScreen());
                    } catch (FullInventoryException fie) {
                        key.drop();
                        Game.log(fie.getMessage());
                    }
                }
            }
        }
        Game.renderer.repaint();
    }

    // Delta in degrees
    public void turn(double delta, boolean right) {
        if (this.isLocked()) return;
        if (right)  {dir += (delta * turn_speed);}
        else        {dir -= (delta * turn_speed);}
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void lock() {
        this.locked = true;
    }

    public void unlock() {
        this.locked = false;
    }

    public boolean isLocked() {
        return this.locked;
    }

    public void update() {
        this.strafe(0.1, 0);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!pressedKeys.contains(e.getKeyCode())) pressedKeys.add(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {};

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(new Integer(e.getKeyCode()));
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int newX = e.getX() + (int)Game.window.getLocationOnScreen().getX();
        int newY = e.getX() + (int)Game.window.getLocationOnScreen().getY();
        if (lastX < 0 || lastY < 0) {
            lastX = newX;
            lastY = newY;
        } else {
            int moveX = newX - lastX;
            moveX /= (double)(Game.renderer.getWidth() / (double)Game.SCREEN_WIDTH);
            moveX *= 2;
            if (moveX > 0) {
                this.turnRight();
            } else if (moveX < 0) {
                this.turnLeft();
            }
            int whx = (int)Math.floor((double)Game.window.getLocationOnScreen().getX() + (double)(Game.window.getWidth()/2));
            int why = (int)Math.floor((double)Game.window.getLocationOnScreen().getY() + (double)(Game.window.getHeight()/2));
            try {
                new java.awt.Robot().mouseMove(whx, why);
            } catch (java.awt.AWTException ex) {

            }
            lastX = whx;
            lastY = why;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    public void turnLeft() {
        if (this.isLocked()) return;
        this.turn(1, false);
        for (Key key : Levels.keys) key.update(this);
        Game.renderer.repaint();
    }

    public void turnRight() {
        if (this.isLocked()) return;
        this.turn(1, true);
        for (Key key : Levels.keys) key.update(this);
        Game.renderer.repaint();
    }

    public Inventory getInventory() {
        return this.inventory;
    }
}
