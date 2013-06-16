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
    return Double.compare(this.x, 0) == 0 && Double.compare(this.y, 0) == 0;
  }

  public double getX () {
    return this.x;
  }

  public double getY () {
    return this.y;
  }

  @Override
  public boolean equals (Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Vector vector = (Vector)o;

    if (Double.compare(vector.x, this.x) != 0) {
      return false;
    }

    if (Double.compare(vector.y, this.y) != 0) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode () {
    int result;
    long temp;
    temp = Double.doubleToLongBits(this.x);
    result = (int)(temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(this.y);
    result = 31 * result + (int)(temp ^ (temp >>> 32));
    return result;
  }
}
