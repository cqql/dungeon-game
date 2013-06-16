package dungeon.models;

import dungeon.util.Vector;

public class Projectile {
  public static final int SIZE = 100;

  private final int id;

  private final Position position;

  private final Vector direction;

  private final int damage;

  public Projectile (int id, Position position, Vector direction, int damage) {
    this.id = id;
    this.position = position;
    this.direction = direction;
    this.damage = damage;
  }

  public int getId () {
    return this.id;
  }

  public Position getPosition () {
    return this.position;
  }

  public Vector getDirection () {
    return this.direction;
  }

  public int getDamage () {
    return this.damage;
  }
}
