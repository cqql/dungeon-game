package dungeon.models;

import dungeon.models.events.Transform;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class World {
  private final List<Room> rooms;

  private final Player player;

  public World (List<Room> rooms, Player player) {
    this.rooms = Collections.unmodifiableList(new ArrayList<>(rooms));
    this.player = player;
  }

  public List<Room> getRooms () {
    return this.rooms;
  }

  public Player getPlayer () {
    return this.player;
  }

  public Room getCurrentRoom () {
    for (Room room : this.rooms) {
      if (room.getId() == this.player.getRoomId()) {
        return room;
      }
    }

    return null;
  }

  public World apply (Transform transform) {
    return this;
  }
}
