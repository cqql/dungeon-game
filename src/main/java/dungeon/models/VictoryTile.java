package dungeon.models;

import dungeon.models.messages.Transform;

public class VictoryTile extends Tile {
  public VictoryTile (Position position) {
    super(true, position);
  }

  public VictoryTile apply (Transform transform) {
    return this;
  }
}
