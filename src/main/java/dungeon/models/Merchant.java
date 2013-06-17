package dungeon.models;

import dungeon.util.Vector;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Merchant implements Spatial {
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
}
