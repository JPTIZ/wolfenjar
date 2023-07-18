//=============================================================================
// StaticImage
//-----------------------------------------------------------------------------
// Generic HUD Element for static image display.
//=============================================================================

import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class StaticText implements HUDElement {

    // Public Attributes
    public  int x, y;
    public TextAlign align;

    // Private Attributes
    private Font font;
    private Color color;
    private String text;
    private boolean visible;
    private boolean needUpdate;
    private BufferedImage image;

    // Implementations
    public StaticText(String text, int x, int y) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.font = null;    // "null" means font will not be changed when drawing text
        this.color = null;   // "null" means color will not be changed when drawing text
        this.align = TextAlign.LEFT;
        this.visible = true;
        this.needUpdate = true;
    }

    public void hide() {
        this.visible = false;
    }

    public void show() {
        this.visible = true;
    }

    public void setFont(Font font) {
        this.font = font;
        this.needUpdate = true;
    }

    public void setColor(Color color) {
        this.color = color;
        this.needUpdate = true;
    }

    public void setText(String text) {
        this.text = text;
        this.needUpdate = true;
    }

    public String getText() {
        return this.text;
    }

    public int getWidth() {
        return (int)this.image.getWidth();
    }

    public int getHeight() {
        return (int)this.image.getHeight();
    }

    private void updateImage(Graphics g) {
        if (this.font == null) this.font = g.getFont();
        Rectangle2D textRect = this.font.getStringBounds(this.text, ((Graphics2D)g).getFontRenderContext());
        BufferedImage buffer = new BufferedImage((int)textRect.getWidth()+3, (int)textRect.getWidth()+3, BufferedImage.TYPE_INT_ARGB);
        Graphics bufferGraphics = buffer.getGraphics();
        if (this.font != null) bufferGraphics.setFont(this.font);
        bufferGraphics.setColor(Color.BLACK);
        bufferGraphics.drawString(text, 0, 0+this.font.getSize());
        bufferGraphics.drawString(text, 2, 0+this.font.getSize());
        bufferGraphics.drawString(text, 0, 2+this.font.getSize());
        bufferGraphics.drawString(text, 2, 2+this.font.getSize());
        if (this.color != null) bufferGraphics.setColor(this.color); else bufferGraphics.setColor(Color.BLACK);
        bufferGraphics.drawString(text, 1, 1+this.font.getSize());
        this.image = buffer;
        this.needUpdate = false;
    }

    @Override
    public void draw(Graphics g, Renderer renderer) {
        if (!this.visible) return;
        if (this.needUpdate) this.updateImage(g);
        double  w_prop = (double)renderer.getWidth() / (double)Game.SCREEN_WIDTH,
                h_prop = (double)renderer.getHeight() / (double)Game.SCREEN_HEIGHT;
        int x = this.x - (align.get() * this.getWidth()/2), y = this.y - 15;
        g.drawImage(this.image,
                    (int)(x*w_prop), (int)(y*h_prop),
                    (int)((this.image.getWidth()+x)*w_prop), (int)((this.image.getHeight()+y)*h_prop),
                    0, 0, (int)this.image.getWidth(), (int)this.image.getHeight(),
                    null
                    );
    }
}