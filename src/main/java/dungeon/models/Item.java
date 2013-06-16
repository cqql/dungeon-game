package dungeon.models;

/**
 * This can be any item, that could exist - A sword, a shield, a potion, etc.
 */
public class Item {
  private final int id;

  private final ItemType type;

  public Item (int id, ItemType type) {
    this.id = id;
    this.type = type;
  }

  public int getId () {
    return this.id;
  }

  public ItemType getType () {
    return this.type;
  }

  @Override
  public String toString () {
    return "Item#" + this.id + " " + this.type;
  }
}
