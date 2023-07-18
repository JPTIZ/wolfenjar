//=============================================================================
// Key
//-----------------------------------------------------------------------------
// Manages key aspects, such as distance from player, image, position, etc...
//=============================================================================

import java.io.File;
import java.awt.Image;
import java.io.Serializable;
import javax.imageio.ImageIO;

class Key implements Serializable {

    private static final long serialVersionUID = -5362404591073428715L;

    public int x, y;
    public transient Image image;
    public double distance;
    public int width, height;
    public int startX, endX;
    private boolean pickedUp;

    public Key(int x, int y) {
        this.x = x;
        this.y = y;
        this.loadImage();
    }

    public void loadImage() {
        try {
            this.image = ImageIO.read(new File("img/cursor.png"));
        } catch (Exception e) {
            Game.log("Failed to load image \"key.png\": "+e.getMessage());
        }
    }

    public int getWidth() {
        if (this.image == null) loadImage();
        return this.image.getWidth(null);
    }

    public int getHeight() {
        if (this.image == null) loadImage();
        return this.image.getHeight(null);
    }

    public Image getImage() {
        if (this.image == null) loadImage();
        return this.image;
    }

    public boolean pickedUp() {
        return this.pickedUp;
    }

    public void pickUp() {
        AudioPlayer.playSFX("coin-04.wav");
        this.pickedUp = true;
    }

    public void drop() {
        this.pickedUp = false;
    }

    public void update(Player player) {
        if (this.image == null) loadImage();
        double x = this.x;
        double y = this.x;
        x -= player.getX();
        y -= player.getY();
        distance = Math.sqrt(x*x + y*y);
        double thetatemp = Math.atan2(y, x);
        double angle = player.dir % Raycaster.ANGULO360;
        angle = Math.abs(angle);
        thetatemp = Math.toDegrees(thetatemp);
        while (thetatemp < 0) thetatemp += 360;
        while (thetatemp > 360) thetatemp += 360;

        double theta = (angle-Raycaster.ANGULO30)/(4800.0/360.0);
        double ytmp = thetatemp-(theta);
        if (thetatemp > 270 && theta < 90) ytmp = (theta)-thetatemp + 360;
        if (theta > 270 && thetatemp < 90) ytmp = (theta)-thetatemp - 360;

        double xtmp = ((double)ytmp * (double)800 / 60.0);

        x = xtmp;
        y = ytmp;
        xtmp = Math.abs(xtmp);
        width = this.image.getWidth(null);
        height = this.image.getHeight(null);

        width *= 4;
        height *= 4;
        width /= Math.abs(distance);
        height /= Math.abs(distance);

        startX = (int)x;
        endX = (int)(x+width);
    }

}
