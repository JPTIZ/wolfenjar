//=============================================================================
// MiniMap
//-------------------------------------------------------------------------
// HUD Element that draws Minimap based on player's position.
//=============================================================================

import java.awt.Color;
import java.awt.Graphics;

public class MiniMap implements HUDElement {

	private boolean visible;

	public MiniMap() {
		this.visible = true;
	}

	public void setVisible(boolean state) {this.visible = state;}

	public boolean isVisible() {return this.visible;}

	@Override
	public void draw(Graphics g, Renderer renderer) {
		if (!this.isVisible()) return;
      	int x = 0;
		int y = 0;

		Color color;
        g.setColor(new Color(0, 0, 255, 128));

        for (int[] row : Levels.map) {
            for (int tile : row) {
			  	if (tile == 0) {color = new Color(128, 128, 128, 128);}
                else {color = Levels.palette[tile - 1];}

		  		g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 128));
		  		g.fillRect(x * 4, y * 4, 4, 4);
		  		x++;
			}
			y++;
            x = 0;
		}

		g.setColor(new Color(0, 255, 255, 128));
		g.drawRect((int) (Game.player.getX()*4.0 - 2), (int) (Game.player.getY()*4.0 - 2), 4, 4);
	}
}
