package dungeon.util;

/**
 * An immutable 2D-vector.
 */
public class Vector {
  private final double x;

  private final double y;

  public Vector (double x, double y) {
    this.x = x;
    this.y = y;
  }

  public Vector plus (Vector a) {
    return new Vector(this.x + a.x, this.y + a.y);
  }

  public Vector times (double scalar) {
    return new Vector(this.x * scalar, this.y * scalar);
  }

  /**
   * @return This vector normalized to length 1
   */
  public Vector normalize () {
    return this.times(1 / this.length());
  }

  public double length () {
    return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
  }

  public double getX () {
    return x;
  }

  public double getY () {
    return y;
  }
}
