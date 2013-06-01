package dungeon.models;

import dungeon.models.messages.Transform;

import java.util.List;

/**
 * A level is a collection of rooms.
 */
public class Level {
  private final String id;

  private final List<Room> rooms;

  public Level (String id, List<Room> rooms) {
    this.id = id;
    this.rooms = rooms;
  }

  public String getId () {
    return this.id;
  }

  public List<Room> getRooms () {
    return this.rooms;
  }

  public Level apply (Transform transform) {
    return this;
  }
}
