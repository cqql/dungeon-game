package dungeon.models;

/**
 * A dropped item that is lying around somewhere in a room.
 *
 * It can be either an amount of money (item == null) or an item (money == 0).
 */
public class Drop {
  public static final int SIZE = 500;

  private final Position position;

  private final Item item;

  private final int money;

  public Drop (Position position, Item item, int money) {
    this.position = position;
    this.item = item;
    this.money = money;
  }

  public Position getPosition () {
    return this.position;
  }

  public Item getItem () {
    return this.item;
  }

  /**
   * @return true if the drop is a money drop.
   */
  public boolean isMoney () {
    return money != 0;
  }

  public int getMoney () {
    return this.money;
  }
}
