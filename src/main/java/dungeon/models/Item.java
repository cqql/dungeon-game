package dungeon.models;

import dungeon.game.Transaction;

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

  /**
   * Returns the worth of the item in coins.
   */
  public int getValue () {
    return this.type.getValue();
  }

  public void use (Transaction transaction, Player player) {
    this.type.use(transaction, player);
  }

  @Override
  public String toString () {
    return "Item#" + this.id + " " + this.type;
  }

  @Override
  public boolean equals (Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Item item = (Item)o;

    if (this.id != item.id) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode () {
    return this.id;
  }
}
