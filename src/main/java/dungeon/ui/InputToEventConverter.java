package dungeon.ui;

import dungeon.MoveEvent;
import dungeon.events.EventHost;

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
        this.eventHost.publish(MoveEvent.UP);
        break;
      case 'a':
        this.eventHost.publish(MoveEvent.LEFT);
        break;
      case 's':
        this.eventHost.publish(MoveEvent.DOWN);
        break;
      case 'd':
        this.eventHost.publish(MoveEvent.RIGHT);
        break;
      default:
    }
  }

  @Override
  public void keyReleased (KeyEvent keyEvent) {

  }
}
