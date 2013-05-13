package dungeon.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class World {
  public final List<Room> rooms;

  public World (List<Room> rooms) {
    this.rooms = Collections.unmodifiableList(new ArrayList<Room>(rooms));
  }
}
