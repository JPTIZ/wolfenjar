//=============================================================================
// StaticImage
//-----------------------------------------------------------------------------
// Generic HUD Element for static image display.
//=============================================================================

import java.awt.Image;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.Graphics;

public class StaticImage implements HUDElement {

    public int x, y;
    private String filename;
    private Image image;
    private boolean visible;

    public StaticImage(String filename, int x, int y) {
        this.x = x;
        this.y = y;
        this.filename = filename;
        this.setImage(filename);
        this.visible = true;
    }

    public void setImage(String filename) {
        this.filename = filename;
        if (!(filename.contains(".jpg") || filename.contains(".png"))) {
            filename.replace(".png", "");
            filename += ".png";
        }
        try {
            this.image = ImageIO.read(new File("img/"+filename));
        } catch (Exception e) {
            Game.log("Failed to load image \""+filename+"\": "+e.getMessage());
        }
    }

    public void hide() {
        this.visible = false;
    }

    public void show() {
        this.visible = true;
    }

    public String getFilename() {
        return this.filename;
    }

    public int getWidth() {
        return this.image.getWidth(null);
    }

    public int getHeight() {
        return this.image.getHeight(null);
    }

    @Override
    public void draw(Graphics g, Renderer renderer) {
        if (!this.visible) return;
        double  w_prop = (double)renderer.getWidth() / (double)Game.SCREEN_WIDTH,
                h_prop = (double)renderer.getHeight() / (double)Game.SCREEN_HEIGHT;
        g.drawImage(image,  (int)(this.x * w_prop), (int)(this.y * h_prop),
                            (int)(this.getWidth() * w_prop), (int)(this.getHeight() * h_prop), null);
    }
}