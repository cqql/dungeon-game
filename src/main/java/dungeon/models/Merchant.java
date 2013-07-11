package dungeon.models;

import dungeon.models.messages.Transform;
import dungeon.util.Vector;

import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Merchant implements Spatial, Serializable {
  public static final int SIZE = 1000;

  private final int id;

  private final Position position;

  private final int money;

  private final List<Item> items;

  public Merchant (int id, Position position, int money, List<Item> items) {
    this.id = id;
    this.position = position;
    this.money = money;
    this.items = Collections.unmodifiableList(new ArrayList<>(items));
  }

  public int getId () {
    return this.id;
  }

  public Position getPosition () {
    return this.position;
  }

  public int getMoney () {
    return this.money;
  }

  public List<Item> getItems () {
    return this.items;
  }

  @Override
  public Rectangle2D space () {
    return new Rectangle2D.Float(this.position.getX(), this.position.getY(), SIZE, SIZE);
  }

  @Override
  public Position getCenter () {
    return new Position(this.position.getVector().plus(new Vector(SIZE / 2, SIZE / 2)));
  }

  public Merchant apply (Transform transform) {
    int id = this.id;
    Position position = this.position;
    int money = this.money;
    List<Item> items = this.items;

    if (transform instanceof BuyItemTransform && this.equals(((BuyItemTransform)transform).merchant)) {
      money -= ((BuyItemTransform)transform).item.getValue();

      items = new ArrayList<>(items);
      items.add(((BuyItemTransform)transform).item);
    } else if (transform instanceof SellItemTransform && this.equals(((SellItemTransform)transform).merchant)) {
      money += ((SellItemTransform)transform).item.getValue();

      items = new ArrayList<>();

      for (Item item : this.items) {
        if (!item.equals(((SellItemTransform)transform).item)) {
          items.add(item);
        }
      }
    }

    return new Merchant(id, position, money, items);
  }

  @Override
  public boolean equals (Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Merchant merchant = (Merchant)o;

    if (this.id != merchant.id) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode () {
    return this.id;
  }

  public static class BuyItemTransform implements Transform, Serializable {
    private final Merchant merchant;

    private final Item item;

    public BuyItemTransform (Merchant merchant, Item item) {
      this.merchant = merchant;
      this.item = item;
    }
  }

  public static class SellItemTransform implements Transform, Serializable {
    private final Merchant merchant;

    private final Item item;

    public SellItemTransform (Merchant merchant, Item item) {
      this.merchant = merchant;
      this.item = item;
    }
  }
}
