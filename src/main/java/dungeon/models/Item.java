package dungeon.models;

/**
 * This can be any item, that could exist - A sword, a shield, a potion, etc.
 */
public class Item {
  private final int id;

  public Item (int id) {
    this.id = id;
  }

  public int getId () {
    return this.id;
  }
}
