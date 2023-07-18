//=============================================================================
// Console
//-----------------------------------------------------------------------------
// Console HUD Element for in-game command insertions
//=============================================================================

import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.PrintStream;
import java.io.ByteArrayOutputStream;
import javax.swing.Timer;
import java.util.ArrayList;

public class Console extends PrintStream implements KeyListener, HUDElement {

	private String text;
	private boolean active;
	private int currentCommandHistory;
	private ArrayList<String> commandHistory;
	private ArrayList<Object> logMessages;

	public Console() {
		super(new ByteArrayOutputStream());
		this.active = false;
		this.text = "";
		this.commandHistory = new ArrayList<>();
		this.logMessages = new ArrayList<>();
		this.currentCommandHistory = 0;
	}

	private class TimedActionListener implements ActionListener {

		private Object message;
		private Timer timer;
		private ArrayList<Object> logger;

		public TimedActionListener(Timer timer, Object message, ArrayList<Object> logger) {
			this.timer = timer;
			this.message = message;
			this.logger = logger;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			logger.remove(message);
			timer.stop();
			return;
		}
	}

	public void println(Object obj) {
		int i = this.logMessages.size();
		this.logMessages.add(obj.toString());
		Timer timer = null;
		timer = new Timer(3000, null);
		timer.addActionListener(new TimedActionListener(timer, obj, logMessages));
		timer.start();
	}

	public void activate() {
		this.active = true;
		this.text = "";
		this.currentCommandHistory = this.commandHistory.size();
		Game.player.lock();
	}

	public void deactivate() {
		this.active = false;
		this.text = "";
		this.currentCommandHistory = this.commandHistory.size();
		Game.player.unlock();
	}

	private void validate() {
		String[] texts = this.text.toLowerCase().split(" ");
		switch (texts[0]) {
			case "noclip":
				Game.toggleNoClip();
				Game.log("No Clip "+(Game.NO_CLIPPING ? "ON" : "OFF"));
				break;
			case "noshade":
				Renderer.toggleLuminosity();
				break;
			case "nomap":
				Game.toggleMiniMap();
				break;
			case "noinventory":
				Game.toggleInventory();
				break;
			case "mute":
				Game.toggleMute();
				break;
			case "exit":
				Game.exit();
				break;
			case "music":
				AudioPlayer.playBGM(texts[1]);
				break;
			default:
				Game.log("Command \""+texts[0]+"\" not recognized");
				break;
		}
      	commandHistory.add(this.text);
      	currentCommandHistory++;
	}

	//-------------------------------------------------------------------------
	// Override KeyListener methods
	//-------------------------------------------------------------------------
	@Override
	public void keyPressed(KeyEvent e) {
		// Verifies key only if active and activates if T is pressed while console is inactive
		if (!this.isActive()) {
			// If pressed "T", enables console
			if (e.getKeyCode() == KeyEvent.VK_T) {
				activate();
				Game.renderer.repaint();
			}
			return;
		}
		// Key verification
		switch (e.getKeyCode()) {
			case KeyEvent.VK_ENTER:
				validate();
				deactivate();
				break;
			case KeyEvent.VK_BACK_SPACE:
				if (text.length() > 0) {
	            	text = text.substring(0, text.length()-1);	// Deletes last character from string
	        	}
	        	break;
	        case KeyEvent.VK_UP:
	        	// Goes to previous command history
	        	if (currentCommandHistory > 0) {
	        		currentCommandHistory--;
	        		text = commandHistory.get(currentCommandHistory);
	        	}
	        	break;
	        case KeyEvent.VK_DOWN:
	        	// Goes to next command history
        		if (currentCommandHistory < commandHistory.size()) currentCommandHistory++;
	        	if (currentCommandHistory < commandHistory.size()) {
	        		text = commandHistory.get(currentCommandHistory);
	        	} else {
	        		text = "";
	        	}
	        	break;
	        case KeyEvent.VK_SPACE:
	        	text += " ";
	        	break;
	        case KeyEvent.VK_MINUS:
	        	if (e.isShiftDown()) {
	        		text += "_";
	        	} else {
	        		text += "-";
	        	}
	        	break;
	        case KeyEvent.VK_PERIOD:
	        	text += ".";
	        	break;
	        case KeyEvent.VK_ESCAPE:
	        	deactivate();
	        	break;
	        default:
	        	if (KeyEvent.getKeyText(e.getKeyCode()).length() == 1)
	        		text += e.getKeyChar();
	        	break;
		}
		Game.renderer.repaint();
	}

	@Override
	public void keyTyped(KeyEvent e) {};

	@Override
	public void keyReleased(KeyEvent e) {};

	//-------------------------------------------------------------------------
	// Override HUDElement methods
	//-------------------------------------------------------------------------
	@Override
	public void draw(Graphics g, Renderer renderer) {
        g.setFont(new Font("Times New Roman", Font.BOLD, 12));
		for (int i = 0; i < logMessages.size(); i++) {
			Object log = logMessages.get(logMessages.size()-1-i);
			g.setColor(Color.BLACK);
			g.drawString(log.toString(), 5, renderer.getHeight() - 28 - (14*i));
			g.setColor(Color.WHITE);
			g.drawString(log.toString(), 4, renderer.getHeight() - 29 - (14*i));
		}
		if (this.isActive()) {
	        g.setColor(new Color(0, 0, 0, 192));
	        g.fillRect(0, renderer.getHeight()-25, renderer.getWidth(), 25);
	        g.setColor(Color.BLACK);
	        g.drawString("Command:/ "+text, 5, renderer.getHeight()-6);
	        g.setColor(Color.WHITE);
	        g.drawString("Command:/ "+text, 4, renderer.getHeight()-7);
	      }
	}

	public void setActive(boolean state) {
		this.active = state;
	}

	public boolean isActive() {
		return this.active;
	}
}