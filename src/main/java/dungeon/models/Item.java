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

  public void use (Transaction transaction) {
    this.type.use(transaction);
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
