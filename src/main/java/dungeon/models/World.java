package dungeon.models;

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
    return rooms;
  }

  public Player getPlayer () {
    return player;
  }
}
