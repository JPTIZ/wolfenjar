//-------------------------------------------------------------------------
// Data Structures
//-------------------------------------------------------------------------
// Should work like a C/C++ struct

public class Wall {
  
  private double height;
  private int index; // a.k.a. "n"

  public Wall(double height, int index) {
    this.height = height;
    this.index = index;
  }

  public double getHeight() {
    return this.height;
  }

  public int getIndex() {
    return this.index;
  }
}
