//=============================================================================
// HUD
//-------------------------------------------------------------------------
// Handles all HUD (Heads-Up Display).
//	* HUD is every top-layer view for displaying useful information for user
//		atop of map view.
//=============================================================================

import java.awt.Graphics;
import java.util.ArrayList;

public class HUD {

	private ArrayList<HUDElement> hudElements;

	public HUD() {
		this.hudElements = new ArrayList<>();
	}

	public void addElement(HUDElement element) {
		this.hudElements.add(element);
	}

	public void removeElement(HUDElement element) {
		this.hudElements.remove(element);
	}

	public void draw(Graphics g, Renderer renderer) {
		for (HUDElement element : hudElements) {
			element.draw(g, renderer);
		}
	}
}