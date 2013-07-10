package dungeon.models;

import dungeon.models.messages.Transform;
import dungeon.util.Vector;

import java.awt.geom.Rectangle2D;
import java.io.Serializable;

public class Tile implements Spatial, Serializable {
  public static final int SIZE = 1000;

  /**
   * true, wenn die Platte nicht begehbar ist; false, wenn die Platte begehbar ist
   */
  private final boolean blocking;

  private final Position position;

  public Tile (boolean blocking, Position position) {
    this.blocking = blocking;
    this.position = position;
  }

  public boolean isBlocking () {
    return this.blocking;
  }

  public Position getPosition () {
    return this.position;
  }

  public Tile apply (Transform transform) {
    return this;
  }

  @Override
  public Rectangle2D space () {
    return new Rectangle2D.Float(this.position.getX(), this.position.getY(), SIZE, SIZE);
  }

  @Override
  public Position getCenter () {
    return new Position(this.position.getVector().plus(new Vector(SIZE / 2, SIZE / 2)));
  }
}
