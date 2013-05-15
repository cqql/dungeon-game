package dungeon.ui.events;

import dungeon.messages.Event;

/**
 * Bewegungsbefehle, die der Spieler sendet.
 */
public enum MoveCommand implements Event {
  LEFT, RIGHT, UP, DOWN
}
