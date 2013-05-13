package dungeon.models;

public class Player {
  public final String name;

  public final int hitPoints;

  public final String roomId;

  public final Position position;

  public Player (String name, int hitPoints, String roomId, Position position) {
    this.name = name;
    this.hitPoints = hitPoints;
    this.roomId = roomId;
    this.position = position;
  }
}
