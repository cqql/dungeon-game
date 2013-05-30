package dungeon.ui.messages;

import dungeon.util.Vector;

/**
 * Bewegungsbefehle, die der Spieler geben kann.
 */
public enum MoveCommand implements Command {
  LEFT(new Vector(-1, 0)),
  RIGHT(new Vector(1, 0)),
  UP(new Vector(0, -1)),
  DOWN(new Vector(0, 1));

  private final Vector direction;

  private MoveCommand (Vector direction) {
    this.direction = direction;
  }

  public Vector getDirection () {
    return direction;
  }
}
