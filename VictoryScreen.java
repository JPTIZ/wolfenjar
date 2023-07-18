//=============================================================================
// VictoryScreen
//-----------------------------------------------------------------------------
// Displays guava and bus options for player when he/she reaches R$1,50
//  (3x R$0,50 coin).
//=============================================================================

import java.awt.Font;
import java.awt.Color;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;
import java.awt.event.KeyListener;

public class VictoryScreen implements ScreenState, KeyListener {

    int index;
    StaticText[] options;
    StaticImage background, cursor;

    public VictoryScreen() {
        options = new StaticText[2];
        options[0] = new StaticText("Bus", Game.SCREEN_WIDTH/4, Game.SCREEN_HEIGHT - 120);
        options[0].setFont(new Font("Times New Roman", Font.BOLD, 48));
        options[0].setColor(Color.RED);
        options[0].align = TextAlign.CENTER;
        options[1] = new StaticText("Guava", 3*Game.SCREEN_WIDTH/4, Game.SCREEN_HEIGHT - 120);
        options[1].setFont(new Font("Times New Roman", Font.BOLD, 48));
        options[1].setColor(Color.RED);
        options[1].align = TextAlign.CENTER;
        background = new StaticImage("background.png", 0, 0);
        cursor = new StaticImage("cursor.png", Game.SCREEN_WIDTH/4 - 100, Game.SCREEN_HEIGHT - 120);
    }

    @Override
    public void initialize() {
        Game.renderer.createHUD();
        Game.renderer.getHUD().addElement(background);
        Game.renderer.getHUD().addElement(options[0]);
        Game.renderer.getHUD().addElement(options[1]);
        Game.renderer.getHUD().addElement(cursor);
        Game.player.lock();
        Game.window.addKeyListener(this);
        Game.renderer.repaint();
    }

    @Override
    public void update() {
        
    }

    @Override
    public void terminate() {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int oldIndex = this.index;
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                AudioPlayer.playSFX("shot.wav");
                this.index--;
                break;
            case KeyEvent.VK_RIGHT:
                AudioPlayer.playSFX("shot.wav");
                this.index++;
                break;
            case KeyEvent.VK_ENTER:
                switch (this.index) {
                    case 0:
                        JOptionPane.showMessageDialog(null, "The bus drivers are on strike. Please select another option.");
                        break;
                    case 1:
                        AudioPlayer.pauseBGM();
                        AudioPlayer.loopSFX("guavasong.wav");
                        break;
                }
                break;
        }
        if (oldIndex != index) {
            this.index = index < 0 ? options.length-1 : index >= options.length ? 0 : index;
            this.cursor.x = this.options[this.index].x - this.options[this.index].getWidth()/2 - 60;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}