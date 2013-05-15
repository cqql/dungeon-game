package dungeon.ui.events;

import dungeon.messages.Message;

/**
 * Bewegungsbefehle, die der Spieler sendet.
 */
public enum MoveCommand implements Message {
  LEFT, RIGHT, UP, DOWN
}
