package dungeon.util;

/**
 * An immutable 2D-vector.
 */
public class Vector {
  /**
   * How close a vector has to be to zero, to be considered the null vector.
   */
  private static final double ZERO_PRECISION = 0.00000000001;

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
   * @throws ArithmeticException if the vector is the null vector
   */
  public Vector normalize () {
    if (this.isZero()) {
      throw new ArithmeticException("Cannot normalize the null vector");
    }

    return this.times(1 / this.length());
  }

  public double length () {
    return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
  }

  /**
   * @return true if the vector is the null vector
   */
  public boolean isZero () {
    return Math.abs(this.x) < ZERO_PRECISION && Math.abs(this.y) < ZERO_PRECISION;
  }

  public double getX () {
    return x;
  }

  public double getY () {
    return y;
  }
}
