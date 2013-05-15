package dungeon.ui;

import dungeon.ui.events.MoveCommand;
import dungeon.messages.EventHost;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputToEventConverter implements KeyListener {
  private final EventHost eventHost;

  public InputToEventConverter (EventHost eventHost) {
    this.eventHost = eventHost;
  }

  @Override
  public void keyTyped (KeyEvent keyEvent) {

  }

  @Override
  public void keyPressed (KeyEvent keyEvent) {
    switch (keyEvent.getKeyChar()) {
      case 'w':
        this.eventHost.publish(MoveCommand.UP);
        break;
      case 'a':
        this.eventHost.publish(MoveCommand.LEFT);
        break;
      case 's':
        this.eventHost.publish(MoveCommand.DOWN);
        break;
      case 'd':
        this.eventHost.publish(MoveCommand.RIGHT);
        break;
      default:
    }
  }

  @Override
  public void keyReleased (KeyEvent keyEvent) {

  }
}
