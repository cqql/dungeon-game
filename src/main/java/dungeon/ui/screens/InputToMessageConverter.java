package dungeon.ui.screens;

import dungeon.messages.Mailman;
import dungeon.ui.messages.*;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * This converts input events like key presses in internal messages that can then be interpreted by the other modules.
 *
 * WARNING: For this class to be thread safe, handleMessage() has to be called on the EDT.
 */
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
    Command command = this.commandForKey(keyEvent.getKeyChar());

    if (command != null) {
      this.mailman.send(new StartCommand(command));
    }
  }

  @Override
  public void keyReleased (KeyEvent keyEvent) {
    Command command = this.commandForKey(keyEvent.getKeyChar());

    if (command != null) {
      this.mailman.send(new EndCommand(command));
    }
  }

  /**
   * @return The command that is associated with the #key.
   */
  private Command commandForKey (Character key) {
    switch (key) {
      case 'w':
        return MoveCommand.UP;
      case 'a':
        return MoveCommand.LEFT;
      case 's':
        return MoveCommand.DOWN;
      case 'd':
        return MoveCommand.RIGHT;
      case 'j':
        return new AttackCommand();
      case 'h':
        return new HealthPotionCommand();
      default:
        return null;
    }
  }
}
