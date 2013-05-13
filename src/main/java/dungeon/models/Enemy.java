package dungeon.models;

public class Enemy {
  public final int strength = 1;

  public final Position startPosition;

  public Enemy (Position startPosition) {
    this.startPosition = startPosition;
  }
}
