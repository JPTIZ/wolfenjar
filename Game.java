//=============================================================================
// Game
//-----------------------------------------------------------------------------
// Contains static fields for general use and default configurations in whole
// game. Also contains methods to ensure game to run well and enables a solid
// communication within objects.
//=============================================================================

import java.awt.Point;
import javax.swing.JFrame;
import java.util.ArrayList;
import java.io.PrintStream;
import java.awt.image.BufferedImage;

public class Game {

    // All-Game Constants
    public static final String 	TITLE = "WolfenJar - Java RayCaster Wolfenstein3D";
    public static final int 	SCREEN_WIDTH  = 800,
                    			SCREEN_HEIGHT = 600,
                    			DISTANCE_PROPORTION = 40;
    public static final boolean DEBUG = false; 	// Set to 'true' whenever you have to
                        	            		// test non-release code.

    // Variables controlled directly trough other objects
    public static boolean NO_CLIPPING = false,
    					  SHOW_MINIMAP = true,
    					  SHOW_INVENTORY = true;

    // Variables controlled through message communications
    private static boolean ENABLE_SOUND = true;

    // Window, renderer and current state. Not too much to say.
    public static JFrame window;
    public static Renderer renderer;
    private static ScreenState screenState;

    // Loggers for messages output
    private static ArrayList<PrintStream> loggers;

    // Used on MapScreen
    public static Player player;
    public static MiniMap miniMap;

    // Game initialization
    public static void initialize() {
      	// Initializes windows
      	Game.window = new JFrame(Game.TITLE);
        Game.window.setContentPane(renderer = new Renderer());
        Game.window.getContentPane().setPreferredSize(new java.awt.Dimension(800, 600));
        Game.window.pack();
        Game.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Game.window.setLocationRelativeTo(null);
        Game.window.setCursor(Game.window.getToolkit().createCustomCursor(
                  new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB), new Point(0, 0),
                  "null"));

        // Adds listeners
        Game.miniMap = new MiniMap();
        Game.player = new Player(5, 5, 1.5, 120);
        Game.window.addKeyListener(Game.player);
        Game.window.addMouseMotionListener(Game.player);

        // Adds logger
        Game.loggers = new ArrayList<>();
        Game.loggers.add(System.out);
        Game.log(System.out);

        // Initializes game itself
        screenState = new TitleScreen();
        screenState.initialize();
        Game.window.setVisible(true);
    }

    //-------------------------------------------------------------------------
    // State events
    //-------------------------------------------------------------------------
    public static void changeState(ScreenState newState) {
        Game.screenState.terminate();
        Game.screenState = null;
        Game.screenState = newState;
        Game.screenState.initialize();
    }

    public static ScreenState getCurrentState() {
        return Game.screenState;
    }

    public static void save() {
        ArrayList<Object> data = new ArrayList<>();
        data.add(Game.player);
        data.add(Game.NO_CLIPPING);
        data.add(Game.SHOW_MINIMAP);
        data.add(Game.SHOW_INVENTORY);
        data.add(Game.renderer.isShadingEnabled());
        data.add(Game.renderer.isRaycastingEnabled());
        data.add(Game.ENABLE_SOUND);
        data.add(Levels.keys);
        GameSerializer.serialize("savegame.wsg", data); // .wsg = WolfenJar SaveGame
    }

    public static void load() {
        ArrayList<Object> data = GameSerializer.load("savegame.wsg");
        Player p = null;
        p = (Player) data.get(0);
        Game.player.cloneFrom(p);
        Game.NO_CLIPPING = (boolean) data.get(1);
        Game.SHOW_MINIMAP = (boolean) data.get(2);
        Game.SHOW_INVENTORY = (boolean) data.get(3);
        Game.renderer.enableShading((boolean) data.get(4));
        Game.renderer.enableRaycasting((boolean) data.get(5));
        Game.ENABLE_SOUND = (boolean) data.get(6);
        Levels.keys = (Key[]) data.get(7);
    }

    public static void exit() {
        System.exit(0);
    }

    //-------------------------------------------------------------------------
    // Console events
    //-------------------------------------------------------------------------

    // No Clip
    public static void toggleNoClip() {
        Game.NO_CLIPPING = !Game.NO_CLIPPING;
    }

    // MiniMap
    public static void toggleMiniMap() {
        Game.SHOW_MINIMAP = !Game.SHOW_MINIMAP;
        Game.miniMap.setVisible(Game.SHOW_MINIMAP);
    }

    // Inventory
    public static void toggleInventory() {
        Game.SHOW_INVENTORY = !Game.SHOW_INVENTORY;
        Game.player.getInventory().setVisible(Game.SHOW_INVENTORY);
    }

    // Shading
    public static void toggleShading() {
        Game.renderer.toggleLuminosity();
    }

    // Ray Casting
    public static void toggleRayCasting() {
        Game.renderer.toggleRayCasting();
    }

    // Mute Control
    public static void toggleMute() {
      	Game.ENABLE_SOUND = !Game.ENABLE_SOUND;
      	if (Game.ENABLE_SOUND) AudioPlayer.continueBGM();
      	else AudioPlayer.pauseBGM();
    }

    //
    public static boolean isMuted() {
    	 return !Game.ENABLE_SOUND;
    }

    // Print Message
    public static void addLogger(PrintStream ps) {
        loggers.add(ps);
    }

    public static void log(Object o) {
        for (PrintStream ps : loggers) {
            ps.println(o);
        }
    }

}
