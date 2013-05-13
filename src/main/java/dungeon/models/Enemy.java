package dungeon.models;

import dungeon.models.events.Transform;

public class Enemy {
  private final int strength = 1;

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
}
