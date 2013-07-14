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
    if (transform instanceof MerchantTransform) {
      return ((MerchantTransform)transform).apply(this);
    } else {
      return this;
    }
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

  private static class MerchantTransform implements Transform {
    private final Merchant merchant;

    private MerchantTransform (Merchant merchant) {
      this.merchant = merchant;
    }

    public Merchant apply (Merchant merchant) {
      if (this.merchant.equals(merchant)) {
        return new Merchant(
          this.id(merchant),
          this.position(merchant),
          this.money(merchant),
          this.items(merchant)
        );
      } else {
        return merchant;
      }
    }

    protected int id (Merchant merchant) {
      return merchant.id;
    }

    protected Position position (Merchant merchant) {
      return merchant.position;
    }

    protected int money (Merchant merchant) {
      return merchant.money;
    }

    protected List<Item> items (Merchant merchant) {
      return merchant.items;
    }
  }

  public static class BuyItemTransform extends MerchantTransform {
    private final Item item;

    public BuyItemTransform (Merchant merchant, Item item) {
      super(merchant);

      this.item = item;
    }

    @Override
    protected int money (Merchant merchant) {
      return merchant.money - this.item.getValue();
    }

    @Override
    protected List<Item> items (Merchant merchant) {
      List<Item> items = new ArrayList<>(merchant.items);
      items.add(this.item);
      return items;
    }
  }

  public static class SellItemTransform extends MerchantTransform {
    private final Item item;

    public SellItemTransform (Merchant merchant, Item item) {
      super(merchant);

      this.item = item;
    }

    @Override
    protected int money (Merchant merchant) {
      return merchant.money + this.item.getValue();
    }

    @Override
    protected List<Item> items (Merchant merchant) {
      List<Item> items = new ArrayList<>();

      for (Item item : merchant.items) {
        if (!item.equals(this.item)) {
          items.add(item);
        }
      }

      return items;
    }
  }
}
