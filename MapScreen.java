//=============================================================================
// MapScreen
//-----------------------------------------------------------------------------
// Displays map and enables player interactions. Also adds Console, MiniMap and
// Inventory HUDs.
//=============================================================================

import java.awt.Font;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MapScreen implements ScreenState, KeyListener {

    private Console console;
    private int index;
    private boolean gamePaused;
    private StaticText menuText;
    private StaticText[] options;
    private StaticImage cursor;
    
    public MapScreen() {
        console = new Console();
        Game.miniMap = new MiniMap();
        //
        menuText = new StaticText("Game Paused", Game.SCREEN_WIDTH/2, Game.SCREEN_HEIGHT/2 - 150);
        menuText.align = TextAlign.CENTER;
        menuText.setColor(Color.RED);
        menuText.setFont(new Font("Times New Roman", Font.BOLD, 48));
        menuText.hide();
        //
        String[] optionsStrs = { "Return to Game", "Save Game", "Exit Game" };
        options = new StaticText[optionsStrs.length];
        for (int i = 0; i < optionsStrs.length; i++) {
            options[i] = new StaticText(optionsStrs[i], Game.SCREEN_WIDTH/2, Game.SCREEN_HEIGHT/2 + 48 * i);
            options[i].align = TextAlign.CENTER;
            options[i].setColor(Color.RED);
            options[i].setFont(new Font("Times New Roman", Font.BOLD, 18));
            options[i].hide();
        }
        //
        cursor = new StaticImage("cursor", options[0].x - 124, Game.SCREEN_HEIGHT/2 - 16);
        cursor.hide();
    }

    @Override
    public void initialize() {
        AudioPlayer.playBGM("d_e1m1.mid");
        Game.renderer.createHUD();
        Game.renderer.getHUD().addElement(console);
        Game.renderer.getHUD().addElement(Game.miniMap);
        Game.renderer.getHUD().addElement(Game.player.getInventory());
        Game.renderer.getHUD().addElement(menuText);
        for (StaticText st : options) Game.renderer.getHUD().addElement(st);
        Game.renderer.getHUD().addElement(cursor);
        Game.window.addKeyListener(console);
        Game.window.addKeyListener(Game.player);
        Game.window.addKeyListener(this);
        Game.addLogger(console);
        Game.player.unlock();
    }

    @Override
    public void update() {}

    @Override
    public void terminate() {}

    private void toggleMenu() {
        this.gamePaused = !this.gamePaused;
        if (this.gamePaused) {
            Game.player.lock();
            for (StaticText st : options) {
                st.show();
            }
            menuText.show();
            cursor.show();
        } else {
            Game.player.unlock();
            for (StaticText st : options) {
                st.hide();
            }
            menuText.hide();
            cursor.hide();
        }
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
            AudioPlayer.playSFX("shot.wav");
            this.toggleMenu();
        } else if (this.gamePaused) {
            switch (ke.getKeyCode()) {
                case KeyEvent.VK_UP:
                    AudioPlayer.playSFX("shot.wav");
                    this.index--;
                    break;
                case KeyEvent.VK_DOWN:
                    AudioPlayer.playSFX("shot.wav");
                    this.index++;
                    break;
                case KeyEvent.VK_ENTER:
                    AudioPlayer.playSFX("shot.wav");
                    switch (this.index) {
                        case 0: // Return to Game
                            this.toggleMenu();
                            break;
                        case 1: // Save Game
                            Game.save();
                            break;
                        case 2: // Exit Game
                            Game.exit();
                            break;
                    }
                    break;
            }
            if (this.index > this.options.length-1) this.index = 0;
            if (this.index < 0) this.index = this.options.length-1;
            this.cursor.x = this.options[this.index].x - 124;
            this.cursor.y = this.options[this.index].y - 16;
        }
        Game.renderer.repaint();
    }

    @Override
    public void keyTyped(KeyEvent ke) {}

    @Override
    public void keyReleased(KeyEvent ke) {

    }
}
