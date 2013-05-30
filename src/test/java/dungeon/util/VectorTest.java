package dungeon.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class VectorTest {
  @Test
  public void twoVectorsShouldBeAddable () {
    Vector a = new Vector(2, 1);
    Vector b = new Vector(-5, 2);

    Vector result = a.plus(b);

    assertEquals(-3, result.getX(), 0.00001);
    assertEquals(3, result.getY(), 0.00001);
  }

  @Test
  public void vectorShouldBeMultiplyableWithAScalar () {
    Vector a = new Vector(5, 2);

    Vector result = a.times(2.5);

    assertEquals(12.5, result.getX(), 0.00001);
    assertEquals(5, result.getY(), 0.00001);
  }

  @Test
  public void vectorShouldBeNormalizable () {
    Vector a = new Vector(3, 4);

    Vector result = a.normalize();

    assertEquals(0.6, result.getX(), 0.00001);
    assertEquals(0.8, result.getY(), 0.00001);
  }

  @Test
  public void vectorShouldHaveALength () {
    Vector a = new Vector(3, 4);

    assertEquals(5, a.length(), 0.00001);
  }
}
