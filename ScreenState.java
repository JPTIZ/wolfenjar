//=============================================================================
// ScreenState
//-----------------------------------------------------------------------------
// Basic Interface for in-game screens implementation.
//=============================================================================

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public interface ScreenState {
    public void initialize();
	public void update();
    public void terminate();
}