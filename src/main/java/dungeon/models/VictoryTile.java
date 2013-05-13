package dungeon.models;

import dungeon.models.events.Transform;

public class VictoryTile extends Tile {
  public VictoryTile () {
    super(false);
  }

  public VictoryTile apply (Transform transform) {
    return this;
  }
}
