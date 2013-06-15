package dungeon.models;

import java.awt.geom.Rectangle2D;

/**
 * An object that occupies space.
 */
public interface Spatial {
  /**
   * @return the space occupied by this object.
   */
  public Rectangle2D space ();
}
