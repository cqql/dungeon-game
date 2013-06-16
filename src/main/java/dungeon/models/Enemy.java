package dungeon.models;

import dungeon.models.messages.Transform;

import java.awt.geom.Rectangle2D;

public class Enemy implements Spatial, Identifiable {
  public static final int SIZE = 1000;

  private final int id;

  private final int hitPoints;

  /**
   * How much damage the enemy inflicts on the player.
   */
  private final int strength = 1;

  /**
   * The enemy's position in the room.
   */
  private final Position position;

  public Enemy (int id, int hitPoints, Position position) {
    this.id = id;
    this.hitPoints = hitPoints;
    this.position = position;
  }

  public int getId () {
    return this.id;
  }

  public int getHitPoints () {
    return this.hitPoints;
  }

  public int getStrength () {
    return this.strength;
  }

  public Position getPosition () {
    return this.position;
  }

  public Enemy apply (Transform transform) {
    if (transform instanceof HitPointTransform && this.equals(((HitPointTransform)transform).enemy)) {
      return new Enemy(this.id, this.hitPoints + ((HitPointTransform)transform).delta, this.position);
    } else {
      return this;
    }
  }

  @Override
  public Rectangle2D space () {
    return new Rectangle2D.Float(this.position.getX(), this.position.getY(), SIZE, SIZE);
  }

  @Override
  public boolean equals (Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Enemy enemy = (Enemy)o;

    if (this.id != enemy.id) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode () {
    return this.id;
  }

  public static class HitPointTransform implements Transform {
    private final Enemy enemy;

    public final int delta;

    public HitPointTransform (Enemy enemy, int delta) {
      this.enemy = enemy;
      this.delta = delta;
    }
  }
}
