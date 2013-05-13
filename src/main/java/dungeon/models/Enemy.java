package dungeon.models;

public class Enemy {
  private final int strength = 1;

  private final Position startPosition;

  public Enemy (Position startPosition) {
    this.startPosition = startPosition;
  }

  public int getStrength () {
    return this.strength;
  }

  public Position getStartPosition () {
    return this.startPosition;
  }
}
