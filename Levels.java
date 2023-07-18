//=============================================================================
// Levels
//-----------------------------------------------------------------------------
// Map matrix, wall palettes and keys location static class.
//=============================================================================

import java.awt.Color;

public class Levels {

    public static final Color[] palette = new Color[] {
      new Color(255, 255, 255), // 1 = WHITE
      new Color(255, 255, 0),   // 2 = YELLOW
      new Color(255, 0, 0),     // 3 = RED
      new Color(0, 255, 0),     // 4 = GREEN
      new Color(0, 0, 255)      // 5 = BLUE
    };

    public static Key[] keys = new Key[] {
        new Key(2, 2),
        new Key(12, 12),
        new Key(22, 22)
    };

    public static final int[][] map = {
        {3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4},
        {1, 0, 0, 0, 0, 0, 0, 5, 0, 0, 3, 0, 5, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 4},
        {1, 0, 0, 0, 0, 0, 0, 5, 0, 3, 3, 0, 5, 0, 5, 0, 5, 0, 0, 0, 0, 0, 0, 4},
        {1, 0, 0, 0, 0, 0, 0, 5, 0, 0, 3, 0, 0, 0, 5, 0, 5, 0, 0, 0, 0, 0, 0, 4},
        {1, 0, 0, 0, 0, 0, 0, 5, 3, 0, 3, 0, 5, 0, 5, 0, 5, 0, 0, 0, 0, 0, 0, 4},
        {1, 0, 0, 0, 0, 0, 0, 5, 3, 3, 3, 0, 5, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 4},
        {1, 0, 0, 0, 0, 0, 0, 5, 3, 0, 3, 0, 5, 0, 5, 5, 5, 0, 0, 0, 0, 0, 0, 4},
        {1, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 5, 0, 5, 0, 5, 0, 0, 0, 0, 0, 0, 4},
        {1, 0, 0, 0, 0, 0, 0, 5, 3, 0, 3, 0, 5, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 4},
        {1, 2, 2, 2, 2, 2, 0, 5, 2, 2, 2, 0, 5, 2, 0, 2, 2, 0, 0, 0, 0, 0, 0, 4},
        {1, 0, 0, 0, 0, 3, 0, 0, 4, 0, 0, 0, 5, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4},
        {1, 0, 0, 0, 0, 3, 0, 0, 4, 0, 4, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4},
        {1, 0, 0, 0, 0, 0, 0, 0, 2, 0, 4, 4, 0, 5, 2, 0, 5, 0, 0, 0, 0, 0, 0, 4},
        {1, 2, 2, 2, 2, 2, 0, 0, 2, 0, 4, 0, 5, 5, 5, 0, 0, 0, 0, 0, 0, 0, 0, 4},
        {1, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4},
        {1, 0, 0, 0, 0, 0, 0, 0, 2, 0, 4, 4, 4, 4, 4, 0, 0, 0, 0, 0, 0, 0, 0, 4},
        {1, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4},
        {1, 0, 0, 0, 0, 0, 0, 0, 2, 4, 4, 4, 4, 4, 4, 0, 0, 0, 0, 0, 0, 0, 0, 4},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4},
        {3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4}
    };
}
