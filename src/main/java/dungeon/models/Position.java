package dungeon.models;

public class Position {
  private final float x;

  private final float y;

  public Position (float x, float y) {
    this.x = x;
    this.y = y;
  }

  public float getX () {
    return this.x;
  }

  public float getY () {
    return this.y;
  }
}
