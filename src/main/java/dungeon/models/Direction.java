package dungeon.models;

import dungeon.util.Vector;

import java.io.Serializable;

/**
 * The four basic directions that one can move and look.
 */
public enum Direction implements Serializable {
  LEFT(new Vector(-1, 0)),
  RIGHT(new Vector(1, 0)),
  UP(new Vector(0, -1)),
  DOWN(new Vector(0, 1));

  private final Vector direction;

  private Direction (Vector direction) {
    this.direction = direction;
  }

  public Vector getVector () {
    return this.direction;
  }
}
