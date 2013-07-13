package dungeon.models;

import dungeon.models.messages.Transform;

import java.io.Serializable;
import java.util.*;

public class World implements Serializable {
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

  public List<Player> getPlayersInRoom (Room room) {
    return this.getPlayersInRoom(room.getId());
  }

  public List<Player> getPlayersInRoom (String roomId) {
    List<Player> players = new ArrayList<>();

    for (Player player : this.players) {
      if (player.getRoomId().equals(roomId)) {
        players.add(player);
      }
    }

    return players;
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

  public List<Room> getCurrentRooms () {
    List<Room> rooms = new ArrayList<>();

    for (Player player : this.players) {
      rooms.add(this.getCurrentRoom(player));
    }

    return rooms;
  }

  public Player getPlayer (int id) {
    Player player = null;

    for (Player a : this.players) {
      if (a.getId() == id) {
        player = a;
      }
    }

    return player;
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
      if (!(transform instanceof RemovePlayerTransform) || player.getId() != ((RemovePlayerTransform)transform).playerId) {
        players.add(player.apply(transform));
      }
    }

    Set<Level> currentLevels = this.getCurrentLevels();

    for (Level level : this.levels) {
      if (currentLevels.contains(level)) {
        levels.add(level.apply(transform));
      } else {
        levels.add(level);
      }
    }

    if (transform instanceof AddPlayerTransform) {
      players.add(((AddPlayerTransform)transform).player);
    }

    return new World(levels, players);
  }

  public static class AddPlayerTransform implements Transform, Serializable {
    public final Player player;

    public AddPlayerTransform (Player player) {
      this.player = player;
    }
  }

  public static class RemovePlayerTransform implements Transform, Serializable {
    public final int playerId;

    public RemovePlayerTransform (int playerId) {
      this.playerId = playerId;
    }
  }
}
