package dungeon.models;

import dungeon.util.Vector;

import java.awt.geom.Rectangle2D;
import java.io.Serializable;

public class NPC implements Spatial, Serializable {
  public static final int SIZE = 1000;

  private final int id;

  private final Position position;

  private final String name;

  private final String saying;

  /**
   * If this is not null, the player will get this quest when talking to the NPC.
   */
  private final Quest quest;

  public NPC (int id, Position position, String name, String saying, Quest quest) {
    this.id = id;
    this.position = position;
    this.name = name;
    this.saying = saying;
    this.quest = quest;
  }

  public int getId () {
    return this.id;
  }

  public Position getPosition () {
    return this.position;
  }

  public String getName () {
    return this.name;
  }

  public String getSaying () {
    return this.saying;
  }

  public Quest getQuest () {
    return this.quest;
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
