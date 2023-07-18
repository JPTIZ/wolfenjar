//=============================================================================
// Renderer
//-----------------------------------------------------------------------------
// Manages displaying processes
//=============================================================================

import java.awt.Image;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import java.util.ArrayList;
import java.awt.image.ColorModel;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class Renderer extends JPanel {

    private static boolean  AUTORENDER_LUMINOSITY = true,
                            RENDER_RAYCASTING = true;

    // Heads-Up Display
    private HUD hud;

    private double  bobbingY = 0.0,
                    bobbingHeight = 12;
    private int bobbingTime = 0;
    private boolean bobbing = false;

    Renderer() {
        super();
        this.hud = new HUD();
    }

    public void startBobbing() {
        bobbing = true;
    }

    public void stopBobbing() {
        bobbing = false;
    }

    public void updateBobbing() {
        if (bobbing || (!bobbing && !(bobbingY*10 >= -2 && bobbingY*10 <= 2))) {
            bobbingTime+=10;
            bobbingY = Math.sin(Math.toRadians(bobbingTime));
        } else {
            bobbingY = 0.0;
            bobbingTime = 0;
        }
    }

    //-------------------------------------------------------------------------
    // Painting everything assuming it all have been already calculated.
    //-------------------------------------------------------------------------
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (Renderer.RENDER_RAYCASTING) {
            drawBackground(g);
            drawWalls(g);
        }
        this.hud.draw(g, this);
    }

    public void drawKey(Graphics g, Key key, Image img, int sourceX) {
        double w = (((double)key.endX - (double)key.startX) / (double)key.image.getWidth(null));
        int dx1 = (int)key.startX * this.getWidth() / Game.SCREEN_WIDTH,  // (key.startX + ((sourceX) * w)),
            dx2 = (int)key.endX * this.getWidth() / Game.SCREEN_WIDTH,    // (key.startX + ((sourceX + 1) * w)),
            dy1 = (int)(300.0) * this.getHeight() / Game.SCREEN_HEIGHT,   // 300 = altura máxima da chave na tela (prop 1:1)
            dy2 = (int)(300.0+key.height) * this.getHeight() / Game.SCREEN_HEIGHT;
        g.drawImage(img,
                    dx1, dy1+(int)(bobbingY*bobbingHeight), dx2, dy2+(int)(bobbingY*bobbingHeight),
                    (int)0,0,(int)(key.image.getWidth(null)),(int)(key.image.getHeight(null))
                    , null);
    }

    //-------------------------------------------------------------------------
    // Get previously calculated walls height
    //-------------------------------------------------------------------------
    private Wall[] getWallsHeight() {
        Raycaster raycaster = new Raycaster((int)this.getWidth(), (int)this.getHeight(), (int)120, (int)10);
        int[][] swalls = raycaster.getFrameWalls(Game.player.getX(), Game.player.getY(), Game.player.dir);
        Wall[] walls = new Wall[swalls.length];
        for (int i = 0; i < swalls.length; i++) {
          int[] wall = swalls[i];
          walls[i] = new Wall(wall[0], wall[1]);
        }
        return walls;
    }

    //-------------------------------------------------------------------------
    // Draw Gray->"Alaranjadinho claro quase amarelo" background.
    //-------------------------------------------------------------------------
    private void drawBackground(Graphics g) {
        double  w_prop = (double)this.getWidth() / (double)Game.SCREEN_WIDTH,
                h_prop = (double)this.getHeight() / (double)Game.SCREEN_HEIGHT,
                x = 0,
                y = h_prop*Game.SCREEN_HEIGHT/2.0,
                w = Game.SCREEN_WIDTH*w_prop,
                h = Game.SCREEN_HEIGHT*h_prop/2.0;
        double black_height = 25 * h_prop;
        double grad_height = (this.getHeight() - black_height)/2; // 2 pieces, then it's x/2.
        if (AUTORENDER_LUMINOSITY) {
            int resolution = 100;
            double piece = grad_height / resolution;
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, (int)w, (int)this.getHeight());
            for (int i = 0; i < resolution; i++) {
                int gray = 192 - (192 * i/resolution);
                g.setColor(new Color(gray, gray, gray));
                g.fillRect(
                    (int)x,
                    (int)(Math.floor(piece)*i),
                    (int)w,
                    (int)(Math.ceil(piece))
                );
                int step = i/resolution;
                g.setColor(new Color(255 * i/resolution, 192 * i/resolution, 128 * i/resolution));
                g.fillRect(
                    (int)x,
                    (int)((this.getHeight())/2+((i+12)*piece) + (bobbingY*bobbingHeight)), // +12 just to make screen darker than it would normally be.
                    (int)w,
                    (int)(Math.ceil(h/resolution))
                );
            }
        } else {
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(0, (int)(bobbingY*bobbingHeight*h_prop), (int)w, (int)(this.getHeight()/2));
            g.setColor(new Color(255, 192, 128));
            g.fillRect(0, (int)((bobbingY*bobbingHeight*h_prop)+this.getHeight()/2), (int)w, (int)(this.getHeight()/2));
        }
    }

    //-------------------------------------------------------------------------
    // Draw walls with their heights
    //-------------------------------------------------------------------------
    private void drawWalls(Graphics g) {
        if (getWallsHeight() == null) return;
        double  w_prop = (double)this.getWidth() / (double)Game.SCREEN_WIDTH,
                h_prop = (double)this.getHeight() / (double)Game.SCREEN_HEIGHT;
        Wall[] walls = getWallsHeight();
        // Check if colors were schanged
        boolean[] changedKeys = new boolean[Levels.keys.length];
        int[] lastKeyX = new int[Levels.keys.length];
        for (boolean b : changedKeys) b = false;
        Image[] imgs = new Image[changedKeys.length];
        for (int i = 0; i < imgs.length; i++) {
          imgs[i] = Levels.keys[i].image;
        }
        // ---
        for (int i = 0; i < walls.length; i++) {
            int wallHeight = (20000 / (int)walls[i].getHeight());
            Color c = Levels.palette[walls[i].getIndex()-1];
            if (AUTORENDER_LUMINOSITY) {
                float hsbVals[] = Color.RGBtoHSB( c.getRed(),
                                                  c.getGreen(),
                                                  c.getBlue(), null );
                float lumLevel = (float)wallHeight;
                float tolerance = 38.0f;
                float dis = 800.0f;
                lumLevel = lumLevel < tolerance ? 50f : lumLevel > dis ? 2*dis : lumLevel;
                lumLevel -= tolerance;
                float luminosity = (lumLevel / dis);
                c = Color.getHSBColor( hsbVals[0], hsbVals[1], luminosity * (hsbVals[2]));
            }

            g.setColor(c);
            double  h1 = h_prop*(Game.SCREEN_HEIGHT - wallHeight)/2;
            w_prop = (double)this.getWidth() / (double)walls.length;
            g.drawRect((int)(w_prop*i), (int)h1 + (int)(bobbingHeight*bobbingY*h_prop), (int)(w_prop), (int)(h_prop*wallHeight));
            int j = 0;
            for (Key key : Levels.keys) {
                if (!key.pickedUp()) {
                  if (walls[i].getHeight() > (Game.DISTANCE_PROPORTION*key.distance) && (key.distance < 8.0f)) {
                        if (i >= key.startX && i <= key.endX) {
                            // If hasn't schanged color...
                            if (!changedKeys[j] && AUTORENDER_LUMINOSITY) {
                                BufferedImage img = deepCopy((BufferedImage)imgs[j]);
                                for (int x = 0; x < img.getWidth(null); x++) {
                                    for (int y = 0; y < img.getHeight(null); y++) {
                                        Color c1 = new Color(img.getRGB(x, y));
                                        if (img.getRGB(x, y) == 0) continue;
                                        float hsbVals[] = Color.RGBtoHSB( c1.getRed(),
                                                                c1.getGreen(),
                                                                c1.getBlue(), null );
                                        float lumLevel = 1.0f/(float)key.distance * 1000.0f;
                                        float tolerance = 18.0f;
                                        lumLevel = lumLevel < tolerance ? 0f : lumLevel > 600.0f ? 600.0f : lumLevel;
                                        lumLevel -= tolerance;
                                        float luminosity = (lumLevel / 600.0f);
                                        c1 = Color.getHSBColor( hsbVals[0], hsbVals[1], luminosity * (hsbVals[2]));
                                        img.setRGB(x, y, c1.getRGB() | (255 << 24));
                                    }
                                }
                                imgs[j] = img;
                                changedKeys[j] = true;
                            }
                            // ---
                            lastKeyX[j] = i;
                            // drawKey(g, key, imgs[j], i - key.startX);
                        } else {
                            if (i == lastKeyX[j]+1) {
                                drawKey(g, key, imgs[j], i - key.startX);
                            }
                        }
                    }
                }
                j++;
            }
        }
    }

    // Makes Copy of Buffered Image instead of passing it by reference and changing original image
    static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    public static void toggleLuminosity() {
        AUTORENDER_LUMINOSITY = !AUTORENDER_LUMINOSITY;
    }

    public static void toggleRayCasting() {
        RENDER_RAYCASTING = !RENDER_RAYCASTING;
    }

    public static boolean isShadingEnabled() {
        return AUTORENDER_LUMINOSITY;
    }

    public static void enableShading(boolean state) {
        Renderer.AUTORENDER_LUMINOSITY = state;
    }

    public static boolean isRaycastingEnabled() {
        return RENDER_RAYCASTING;
    }

    public static void enableRaycasting(boolean state) {
        Renderer.RENDER_RAYCASTING = state;
    }

    public void createHUD() {
        this.hud = new HUD();
    }

    public HUD getHUD() {
        return this.hud;
    }

}
