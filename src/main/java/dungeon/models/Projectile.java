package dungeon.models;

import dungeon.models.messages.Transform;
import dungeon.util.Vector;

public class Projectile {
  public static final int SIZE = 100;

  private final int id;

  private final Position position;

  private final Vector velocity;

  private final int damage;

  public Projectile (int id, Position position, Vector velocity, int damage) {
    this.id = id;
    this.position = position;
    this.velocity = velocity;
    this.damage = damage;
  }

  public int getId () {
    return this.id;
  }

  public Position getPosition () {
    return this.position;
  }

  public Vector getVelocity () {
    return this.velocity;
  }

  public int getDamage () {
    return this.damage;
  }

  public Projectile apply (Transform transform) {
    if (transform instanceof MoveTransform && ((MoveTransform)transform).projectileId == this.id) {
      return new Projectile(this.id, ((MoveTransform)transform).position, this.velocity, this.damage);
    } else {
      return this;
    }
  }

  public static class MoveTransform implements Transform {
    private final int projectileId;

    private final Position position;

    public MoveTransform (int projectileId, Position position) {
      this.projectileId = projectileId;
      this.position = position;
    }
  }
}
