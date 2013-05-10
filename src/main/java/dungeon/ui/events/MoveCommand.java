package dungeon.ui.events;

import dungeon.events.Event;

/**
 * Bewegungsbefehle, die der Spieler sendet.
 */
public enum MoveCommand implements Event {
  LEFT, RIGHT, UP, DOWN
}
