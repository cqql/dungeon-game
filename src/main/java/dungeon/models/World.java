package dungeon.models;

import dungeon.models.messages.Transform;

import java.util.*;

public class World {
  private final List<Level> levels;

  private final List<Player> players;

  public World (List<Level> levels, List<Player> players) {
    this.levels = Collections.unmodifiableList(new ArrayList<>(levels));
    this.players = Collections.unmodifiableList(new ArrayList<>(players));
  }

  public List<Level> getLevels () {
    return this.levels;
  }

  public List<Player> getPlayers () {
    return this.players;
  }

  public Room getCurrentRoom (Player player) {
    Level currentLevel = this.getCurrentLevel(player);

    if (currentLevel == null) {
      return null;
    }

    for (Room room : currentLevel.getRooms()) {
      if (room.getId().equals(player.getRoomId())) {
        return room;
      }
    }

    return null;
  }

  private Level getCurrentLevel (Player player) {
    for (Level level : this.levels) {
      if (!level.getId().equals(player.getLevelId())) {
        continue;
      }

      return level;
    }

    return null;
  }

  private Set<Level> getCurrentLevels () {
    Set<Level> levels = new HashSet<>();

    for (Player player : this.players) {
      levels.add(this.getCurrentLevel(player));
    }

    return levels;
  }

  /**
   * Applies transforms only to the current levels for performance reasons.
   */
  public World apply (Transform transform) {
    List<Level> levels = new ArrayList<>();
    List<Player> players = new ArrayList<>();

    for (Player player : this.players) {
      players.add(player.apply(transform));
    }

    Set<Level> currentLevels = this.getCurrentLevels();

    for (Level level : this.levels) {
      if (currentLevels.contains(level)) {
        levels.add(level.apply(transform));
      } else {
        levels.add(level);
      }
    }

    return new World(levels, players);
  }
}
