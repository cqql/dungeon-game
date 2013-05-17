package dungeon.ui;

import dungeon.ui.events.MoveCommand;
import dungeon.messages.Mailman;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputToMessageConverter implements KeyListener {
  private final Mailman mailman;

  public InputToMessageConverter (Mailman mailman) {
    this.mailman = mailman;
  }

  @Override
  public void keyTyped (KeyEvent keyEvent) {

  }

  @Override
  public void keyPressed (KeyEvent keyEvent) {
    switch (keyEvent.getKeyCode()) {
      case KeyEvent.VK_W: case KeyEvent.VK_UP:
        this.mailman.send(MoveCommand.UP);
        break;
      case KeyEvent.VK_A: case KeyEvent.VK_LEFT:
        this.mailman.send(MoveCommand.LEFT);
        break;
      case KeyEvent.VK_S:case KeyEvent.VK_DOWN:
        this.mailman.send(MoveCommand.DOWN);
        break;
      case KeyEvent.VK_D: case KeyEvent.VK_RIGHT:
        this.mailman.send(MoveCommand.RIGHT);
        break;
      default:
    }
  }

  @Override
  public void keyReleased (KeyEvent keyEvent) {

  }
}
