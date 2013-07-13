package dungeon.ui.screens;

import dungeon.client.Client;
import dungeon.ui.messages.*;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * This converts input events like key presses in internal messages that can then be interpreted by the other modules.
 *
 * WARNING: For this class to be thread safe, handleMessage() has to be called on the EDT.
 */
public class InputToMessageConverter implements KeyListener {
  private final Client client;

  public InputToMessageConverter (Client client) {
    this.client = client;
  }

  @Override
  public void keyTyped (KeyEvent keyEvent) {

  }

  @Override
  public void keyPressed (KeyEvent keyEvent) {
    Command command = this.commandForKey(keyEvent.getKeyChar());

    if (command != null) {
      this.client.send(new StartCommand(this.client.getPlayerId(), command));
    }  else if (keyEvent.getKeyChar() == 'i') {
      this.client.send(new ShowInventory(this.client.getPlayerId()));
    } else if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
      this.sendChatMessage();
    }
  }

  @Override
  public void keyReleased (KeyEvent keyEvent) {
    Command command = this.commandForKey(keyEvent.getKeyChar());

    if (command != null) {
      this.client.send(new EndCommand(this.client.getPlayerId(), command));
    }
  }

  /**
   * @return The command that is associated with the {@code key}.
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
      case 'k':
        return new IceBoltAttackCommand();
      case 'h':
        return new HealthPotionCommand();
      case 'm':
        return new ManaPotionCommand();
      case 'z':
        return new InteractCommand();
      default:
        return null;
    }
  }

  private void sendChatMessage () {
    String message = JOptionPane.showInputDialog("Nachricht");

    if (message != null) {

    }
  }
}
