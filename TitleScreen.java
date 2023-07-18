//=============================================================================
// TitleScreen
//-----------------------------------------------------------------------------
// First screen user views when game is opened, displaying initial menu options
//  such as:
//  *   New Game
//  *   Load Game
//  *   Options
//  *   Exit Game
//=============================================================================

import java.awt.Font;
import java.awt.Color;
import javax.swing.Timer;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

enum SubState {
    MAIN_MENU,
    LOAD_MENU,
    OPTIONS
}

public class TitleScreen implements ScreenState, KeyListener {

	private StaticImage imgLogo,
                        imgCursor;
    private StaticImage[] menuOptions;
    private StaticText[] options;
    private Timer timer;

    private SubState subState;

    private int index, optIndex;

    public TitleScreen() {
        this.menuOptions    = new StaticImage[4];
        this.imgLogo        = new StaticImage("logo", 100, 50);
        String[] opStrs     = new String[] { "newGameSelected", "loadGame", "options", "exitGame" };
        for (int i = 0; i < this.menuOptions.length; i++) {
            menuOptions[i] = new StaticImage(opStrs[i], 400, 300 + 72*i);
            menuOptions[i].x -= menuOptions[i].getWidth()/2;
        }

        String[] optsStrs   = new String[] {
            "No Clip", "OFF",
            "Show MiniMap", "ON",
            "Show Inventory", "ON",
            "Enable Sound", "ON",
            "Shading", "ON",
            "RayCasting", "ON",
            "Back"
        };

        this.options = new StaticText[optsStrs.length];
        for (int i = 0; i < this.options.length; i++) {
            options[i] = new StaticText(optsStrs[i], 240 + ((i % 2)*250), 250 + ((i/2)*48));
            options[i].setFont(new Font("Verdana", Font.BOLD, 18));
            options[i].setColor(i%2 == 0 ? Color.RED : Color.WHITE);
            options[i].hide();
        }

        this.imgCursor  = new StaticImage("cursor", 260, 308);
        this.subState   = SubState.MAIN_MENU;
        this.index      = 0;
        this.optIndex   = 0;
	}

    private void goToState(SubState newState) {
        this.subState = newState;
        switch (this.subState) {
            case MAIN_MENU:
                for (StaticImage si : menuOptions) {
                    si.show();
                }
                for (StaticText st : options) {
                    st.hide();
                }
                this.imgCursor.x = 260;
                this.imgCursor.y = 308 + this.index * 72;
                break;
            case OPTIONS:
                for (StaticImage si : menuOptions) {
                    si.hide();
                }
                for (StaticText st : options) {
                    st.show();
                }
                this.imgCursor.x = 200;
                this.imgCursor.y = 230 + this.optIndex * 48;
                break;
        }
        Game.renderer.repaint();
    }

    @Override
    public void initialize() {
        AudioPlayer.playBGM("get_them.mid");
        Game.renderer.createHUD();
        Game.renderer.getHUD().addElement(imgLogo);
        for (StaticImage si : menuOptions) Game.renderer.getHUD().addElement(si);
        for (StaticText st : options) Game.renderer.getHUD().addElement(st);
        Game.renderer.getHUD().addElement(imgCursor);
        Game.window.addKeyListener(this);
        Game.player.lock();
        timer = new Timer(17, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!(Game.getCurrentState() instanceof TitleScreen)) {
                    timer.stop();
                }
                update();
            }
        });
        timer.start();
    }

    @Override
    public void update() {
        Game.player.dir += 5;
        Game.renderer.repaint();
    }

    @Override
    public void terminate() {
        Game.window.removeKeyListener(this);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (this.subState) {
            case MAIN_MENU:
                int oldIndex = 0+this.index;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        this.index--;
                        break;
                    case KeyEvent.VK_DOWN:
                        this.index++;
                        break;
                }
                if (oldIndex != this.index) {
                    if (this.index < 0) this.index = 3;
                    if (this.index > 3) this.index = 0;
                    AudioPlayer.playSFX("dsswtchn.wav");
                    menuOptions[oldIndex].setImage(menuOptions[oldIndex].getFilename().replace("Selected",""));
                    menuOptions[this.index].setImage(menuOptions[this.index].getFilename()+"Selected");
                    this.imgCursor.y = 308 + 72*this.index;
                }
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    AudioPlayer.playSFX("shot.wav");
                    switch (this.index) {
                        case 0:
                            Game.log("Get Psyched!");
                            Game.changeState(new MapScreen());
                            break;
                        case 1:
                            Game.load();
                            Game.player.unlock();
                            Game.changeState(new MapScreen());
                            // goToState(SubState.LOAD_MENU);
                            break;
                        case 2:
                            goToState(SubState.OPTIONS);
                            break;
                        case 3:
                            Game.exit();
                            break;
                    }
                }
                break;
            case OPTIONS:
                int oldOptIndex = this.optIndex;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        this.optIndex--;
                        break;
                    case KeyEvent.VK_DOWN:
                        this.optIndex++;
                        break;
                }
                if (oldOptIndex != this.optIndex) {
                    AudioPlayer.playSFX("dsswtchn.wav");
                    if (this.optIndex < 0) this.optIndex = this.options.length/2;
                    if (this.optIndex > this.options.length/2) this.optIndex = 0;
                    this.imgCursor.y = 230 + this.optIndex * 48;
                }
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    AudioPlayer.playSFX("shot.wav");
                    switch (this.optIndex) {
                        case 0: Game.toggleNoClip(); break;
                        case 1: Game.toggleMiniMap(); break;
                        case 2: Game.toggleInventory(); break;
                        case 3: Game.toggleMute(); break;
                        case 4: Game.toggleShading(); break;
                        case 5: Game.toggleRayCasting(); break;
                        case 6: goToState(SubState.MAIN_MENU); break;
                    }
                    if (this.optIndex < 6) {
                        this.options[(this.optIndex*2)+1].setText(this.options[(this.optIndex*2)+1].getText().equals("ON") ? "OFF" : "ON");
                    }
                }
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}