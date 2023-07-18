//=============================================================================
// HUDElement
//-----------------------------------------------------------------------------
// Base Interface for every HUD Element implementation.
//=============================================================================

import java.awt.Graphics;

public interface HUDElement {
	public void draw(Graphics g, Renderer renderer);
}