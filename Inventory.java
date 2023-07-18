//=============================================================================
// Inventory
//-----------------------------------------------------------------------------
// HUD Element for player inventory drawing.
//=============================================================================

import java.io.File;
import java.awt.Image;
import java.awt.Graphics;
import java.io.Serializable;
import javax.imageio.ImageIO;

public class Inventory implements HUDElement, Serializable {

	private static final long serialVersionUID = -693859497961203884L;

	Key[] items;
	int itemCount;
	boolean visible;

	public Inventory() {
		this.items = new Key[8];
		this.itemCount = 0;
		this.visible = true;
	}

	public void addItem(Key item) throws FullInventoryException {
		if (itemCount == 8) {
			throw new FullInventoryException();
		}
		items[itemCount] = item;
		itemCount++;
	}

	public void setVisible(boolean state) {
		this.visible = state;
	}

	public int count() {
		return itemCount;
	}

	@Override
	public void draw(Graphics g, Renderer renderer) {
		if (!this.visible) return;
		double 	x_prop = renderer.getWidth() / Game.SCREEN_WIDTH,
				y_prop = renderer.getHeight() / Game.SCREEN_HEIGHT;
		for (int i = 0; i < itemCount; i++) {
			int w = items[i].getWidth(),
				h = items[i].getHeight();
			g.drawImage(items[i].getImage(),
				112 + (40 * i), 4,
				112 + (40 * i) + w, 4 + h,
				0, 0, w, h, null
				);
		}
	}
}
