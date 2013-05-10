package dungeon.models;

public class Enemy {
  public final int strength = 1;

  public final Position startPosition;

  public Enemy (Position startPosition) {
    this.startPosition = startPosition;
  }

  public static class Position {
    public final float x;

    public final float y;

    public Position (float x, float y) {
      this.x = x;
      this.y = y;
    }
  }
}
