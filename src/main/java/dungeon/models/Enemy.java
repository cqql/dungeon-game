package dungeon.models;

import dungeon.models.messages.Transform;

import java.awt.geom.Rectangle2D;

public class Enemy implements Spatial {
  public static final int SIZE = 1000;

  /**
   * How much damage the enemy inflics on the player.
   */
  private final int strength = 1;

  /**
   * The enemy's position in the room.
   */
  private final Position position;

  public Enemy (Position position) {
    this.position = position;
  }

  public int getStrength () {
    return this.strength;
  }

  public Position getPosition () {
    return this.position;
  }

  public Enemy apply (Transform transform) {
    return this;
  }

  @Override
  public Rectangle2D space () {
    return new Rectangle2D.Float(this.position.getX(), this.position.getY(), SIZE, SIZE);
  }
}
