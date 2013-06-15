package dungeon.models;

/**
 * A dropped item that is lying around somewhere in a room.
 *
 * It can be either an amount of money (item == null) or an item (money == 0).
 */
public class Drop {
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

  public int getMoney () {
    return this.money;
  }
}
