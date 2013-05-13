package dungeon.models;

public class Tile {
  /**
   * true, wenn die Platte nicht begehbar ist; false, wenn die Platte begehbar ist
   */
  private final boolean blocking;

  public Tile (boolean blocking) {
    this.blocking = blocking;
  }

  public boolean isBlocking () {
    return this.blocking;
  }
}
