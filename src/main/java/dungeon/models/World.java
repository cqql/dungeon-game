package dungeon.models;

import dungeon.models.messages.Transform;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class World {
  private final List<Level> levels;

  private final Player player;

  public World (List<Level> levels, Player player) {
    this.levels = Collections.unmodifiableList(new ArrayList<>(levels));
    this.player = player;
  }

  public List<Level> getLevels () {
    return this.levels;
  }

  public Player getPlayer () {
    return this.player;
  }

  public Room getCurrentRoom () {
    Level currentLevel = this.getCurrentLevel();

    if (currentLevel == null) {
      return null;
    }

    for (Room room : currentLevel.getRooms()) {
      if (room.getId().equals(this.player.getRoomId())) {
        return room;
      }
    }

    return null;
  }

  private Level getCurrentLevel () {
    for (Level level : this.levels) {
      if (!level.getId().equals(this.player.getLevelId())) {
        continue;
      }

      return level;
    }

    return null;
  }

  public World apply (Transform transform) {
    List<Level> levels = new ArrayList<>(this.levels.size());

    for (Level level : this.levels) {
      levels.add(level.apply(transform));
    }

    return new World(levels, this.player.apply(transform));
  }
}
