package dungeon.ui.messages;

import dungeon.models.Direction;

/**
 * Bewegungsbefehle, die der Spieler geben kann.
 */
public enum MoveCommand implements Command {
  LEFT(Direction.LEFT),
  RIGHT(Direction.RIGHT),
  UP(Direction.UP),
  DOWN(Direction.DOWN);

  private final Direction direction;

  private MoveCommand (Direction direction) {
    this.direction = direction;
  }

  public Direction getDirection () {
    return this.direction;
  }
}
