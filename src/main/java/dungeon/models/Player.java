package dungeon.models;

import dungeon.models.messages.Transform;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player implements Spatial {
  public static final int SIZE = 900;

  private final String name;

  private final int lives;

  private final int hitPoints;

  private final int maxHitPoints;

  /**
   * The amount of money the player has.
   */
  private final int money;

  /**
   * The items in the player's bag.
   */
  private final List<Item> items;

  /**
   * Which level is the player currently in?
   */
  private final String levelId;

  /**
   * Which room is the player currently in?
   */
  private final String roomId;

  /**
   * His position in the room.
   */
  private final Position position;

  /**
   * In which direction is the player looking?
   */
  private final Direction viewingDirection;

  /**
   * In which room was the player, when he activated the current save point?
   *
   * Whenever the player enters a new level, this should be reset to the starting room's id.
   */
  private final String savePointRoomId;

  /**
   * At which position was the player, when he activated the current save point?
   *
   * Whenever the player enters a new level, this should be reset to the player's position in the starting room.
   */
  private final Position savePointPosition;

  public Player (String name, int lives, int hitPoints, int maxHitPoints, int money, List<Item> items, String levelId, String roomId, Position position, Direction viewingDirection, String savePointRoomId, Position savePointPosition) {
    this.name = name;
    this.lives = lives;
    this.hitPoints = hitPoints;
    this.maxHitPoints = maxHitPoints;
    this.money = money;
    this.items = Collections.unmodifiableList(new ArrayList<>(items));
    this.levelId = levelId;
    this.roomId = roomId;
    this.position = position;
    this.viewingDirection = viewingDirection;
    this.savePointRoomId = savePointRoomId;
    this.savePointPosition = savePointPosition;
  }

  public String getName () {
    return this.name;
  }

  public int getLives () {
    return this.lives;
  }

  public int getHitPoints () {
    return this.hitPoints;
  }

  public int getMaxHitPoints () {
    return this.maxHitPoints;
  }

  public int getMoney () {
    return this.money;
  }

  public List<Item> getItems () {
    return items;
  }

  public String getLevelId () {
    return this.levelId;
  }

  public String getRoomId () {
    return this.roomId;
  }

  public Position getPosition () {
    return this.position;
  }

  public Direction getViewingDirection () {
    return this.viewingDirection;
  }

  public String getSavePointRoomId () {
    return this.savePointRoomId;
  }

  public Position getSavePointPosition () {
    return this.savePointPosition;
  }

  /**
   * @return true if the player touches the object.
   */
  public boolean touches (Spatial object) {
    return this.space().intersects(object.space());
  }

  public boolean touches (SavePoint savePoint) {
    Rectangle2D savePointSpace = new Rectangle2D.Float(savePoint.getPosition().getX(), savePoint.getPosition().getY(), SavePoint.SIZE, SavePoint.SIZE);

    return this.playerSpace().intersects(savePointSpace);
  }

  /**
   * Returns a rectangle that represents the space occupied by the player.
   */
  private Rectangle2D playerSpace () {
    return new Rectangle2D.Float(this.getPosition().getX(), this.getPosition().getY(), Player.SIZE, Player.SIZE);
  }

  public Rectangle2D space () {
    return new Rectangle2D.Float(this.position.getX(), this.position.getY(), SIZE, SIZE);

  }

  public Player apply (Transform transform) {
    String name = this.name;
    int lives = this.lives;
    int hitPoints = this.hitPoints;
    int maxHitPoints = this.maxHitPoints;
    int money = this.money;
    List<Item> items = this.items;
    String levelId = this.levelId;
    String roomId = this.roomId;
    Position position = this.position;
    Direction viewingDirection = this.viewingDirection;
    String savePointRoomId = this.savePointRoomId;
    Position savePointPosition = this.savePointPosition;

    if (transform instanceof MoveTransform) {
      MoveTransform move = (MoveTransform)transform;

      position = new Position(this.position.getX() + move.xDelta, this.position.getY() + move.yDelta);
    } else if (transform instanceof ViewingDirectionTransform) {
      viewingDirection = ((ViewingDirectionTransform)transform).direction;
    } else if (transform instanceof HitpointTransform) {
      HitpointTransform hpTransform = (HitpointTransform)transform;

      hitPoints += hpTransform.delta;
    } else if (transform instanceof LivesTransform) {
      LivesTransform livesTransform = (LivesTransform)transform;

      lives += livesTransform.delta;
    } else if (transform instanceof TeleportTransform) {
      TeleportTransform teleportTransform = (TeleportTransform)transform;

      roomId = teleportTransform.roomId;
      position = teleportTransform.position;
    } else if (transform instanceof SavePointTransform) {
      SavePointTransform savePointTransform = (Player.SavePointTransform)transform;

      savePointRoomId = savePointTransform.roomId;
      savePointPosition = savePointTransform.position;
    } else if (transform instanceof MoneyTransform) {
      money += ((MoneyTransform)transform).delta;
    } else if (transform instanceof AddItemTransform) {
      items = new ArrayList<>(items);
      items.add(((AddItemTransform)transform).item);
    }

    return new Player(name, lives, hitPoints, maxHitPoints, money, items, levelId, roomId, position, viewingDirection, savePointRoomId, savePointPosition);
  }

  public static class MoveTransform implements Transform {
    private final int xDelta;

    private final int yDelta;

    public MoveTransform (int xDelta, int yDelta) {
      this.xDelta = xDelta;
      this.yDelta = yDelta;
    }
  }

  public static class HitpointTransform implements Transform {
    private final int delta;

    public HitpointTransform (int delta) {
      this.delta = delta;
    }
  }

  public static class LivesTransform implements Transform {
    private final int delta;

    public LivesTransform (int delta) {
      this.delta = delta;
    }
  }

  public static class TeleportTransform implements Transform {
    private final String roomId;

    private final Position position;

    public TeleportTransform (String roomId, Position position) {
      this.roomId = roomId;
      this.position = position;
    }
  }

  public static class SavePointTransform implements Transform {
    private final String roomId;

    private final Position position;

    public SavePointTransform (String roomId, Position position) {
      this.roomId = roomId;
      this.position = position;
    }
  }

  public static class MoneyTransform implements Transform {
    private final int delta;

    public MoneyTransform (int delta) {
      this.delta = delta;
    }
  }

  public static class AddItemTransform implements Transform {
    private final Item item;

    public AddItemTransform (Item item) {
      this.item = item;
    }
  }

  public static class ViewingDirectionTransform implements Transform {
    private final Direction direction;

    public ViewingDirectionTransform (Direction direction) {
      this.direction = direction;
    }
  }
}