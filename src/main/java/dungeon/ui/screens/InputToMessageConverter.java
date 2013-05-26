package dungeon.ui.screens;

import dungeon.load.messages.LevelLoadedEvent;
import dungeon.messages.Mailman;
import dungeon.messages.Message;
import dungeon.messages.MessageHandler;
import dungeon.ui.messages.MoveCommand;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

/**
 * This converts input events like key presses in internal messages that can then be interpreted by the other modules.
 *
 * To support multiple keys pressed at the same time, it manages a set of currently pressed keys.
 *
 * WARNING: For this class to be thread safe, handleMessage() has to be called on the EDT.
 */
public class InputToMessageConverter implements KeyListener, MessageHandler {
  private final Mailman mailman;

  private final Set<Character> activeKeys;

  public InputToMessageConverter (Mailman mailman) {
    this.mailman = mailman;

    this.activeKeys = new HashSet<>();
  }

  /**
   * Reset active keys when the game is restarted.
   */
  @Override
  public void handleMessage (Message message) {
    if (message instanceof LevelLoadedEvent) {
      this.activeKeys.clear();
    }
  }

  @Override
  public void keyTyped (KeyEvent keyEvent) {

  }

  @Override
  public void keyPressed (KeyEvent keyEvent) {
    this.activeKeys.add(keyEvent.getKeyChar());

    for (Character key : this.activeKeys) {
      this.sendCommand(key);
    }
  }

  @Override
  public void keyReleased (KeyEvent keyEvent) {
    this.activeKeys.remove(keyEvent.getKeyChar());
  }

  /**
   * Send the command that is associated with the #key.
   */
  private void sendCommand (Character key) {
    switch (key) {
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
}
