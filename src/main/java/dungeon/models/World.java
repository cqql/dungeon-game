package dungeon.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class World {
  public final List<Room> rooms;

  public final Player player;

  public World (List<Room> rooms, Player player) {
    this.rooms = Collections.unmodifiableList(new ArrayList<Room>(rooms));
    this.player = player;
  }
}
