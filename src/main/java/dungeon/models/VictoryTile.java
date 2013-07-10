package dungeon.models;

import dungeon.models.messages.Transform;

import java.io.Serializable;

public class VictoryTile extends Tile implements Serializable {
  public VictoryTile (Position position) {
    super(false, position);
  }

  public VictoryTile apply (Transform transform) {
    return this;
  }
}
