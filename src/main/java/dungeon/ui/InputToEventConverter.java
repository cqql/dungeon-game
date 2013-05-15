package dungeon.ui;

import dungeon.ui.events.MoveCommand;
import dungeon.messages.Mailman;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputToEventConverter implements KeyListener {
  private final Mailman mailman;

  public InputToEventConverter (Mailman mailman) {
    this.mailman = mailman;
  }

  @Override
  public void keyTyped (KeyEvent keyEvent) {

  }

  @Override
  public void keyPressed (KeyEvent keyEvent) {
    switch (keyEvent.getKeyChar()) {
      case 'w':
        this.mailman.send(MoveCommand.UP);
        break;
      case 'a':
        this.mailman.send(MoveCommand.LEFT);
        break;
      case 's':
        this.mailman.send(MoveCommand.DOWN);
        break;
      case 'd':
        this.mailman.send(MoveCommand.RIGHT);
        break;
      default:
    }
  }

  @Override
  public void keyReleased (KeyEvent keyEvent) {

  }
}
