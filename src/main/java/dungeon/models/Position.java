package dungeon.models;

import dungeon.util.Vector;

import java.io.Serializable;

public class Position implements Serializable {
  private final int x;

  private final int y;

  public Position (int x, int y) {
    this.x = x;
    this.y = y;
  }

  public Position (Vector vector) {
    this.x = (int)vector.getX();
    this.y = (int)vector.getY();
  }

  public int getX () {
    return this.x;
  }

  public int getY () {
    return this.y;
  }

  public Vector getVector () {
    return new Vector(this.x, this.y);
  }
}
