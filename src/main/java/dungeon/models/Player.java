package dungeon.models;

public class Player {
  private final String name;

  private final int hitPoints;

  private final String roomId;

  private final Position position;

  public Player (String name, int hitPoints, String roomId, Position position) {
    this.name = name;
    this.hitPoints = hitPoints;
    this.roomId = roomId;
    this.position = position;
  }

  public String getName () {
    return this.name;
  }

  public int getHitPoints () {
    return this.hitPoints;
  }

  public String getRoomId () {
    return this.roomId;
  }

  public Position getPosition () {
    return this.position;
  }
}
