package dungeon.ui.screens;

import dungeon.client.Client;
import dungeon.load.WorldLoader;
import dungeon.models.World;
import dungeon.ui.messages.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

/**
 * This converts input events like key presses in internal messages that can then be interpreted by the other modules.
 *
 * WARNING: For this class to be thread safe, handleMessage() has to be called on the EDT.
 */
public class InputToMessageConverter implements KeyListener {
  private final Client client;

  private final Canvas canvas;

  public InputToMessageConverter (Client client, Canvas canvas) {
    this.client = client;
    this.canvas = canvas;
  }

  @Override
  public void keyTyped (KeyEvent keyEvent) {

  }

  @Override
  public void keyPressed (KeyEvent keyEvent) {
    if (keyEvent.getKeyChar() == 'i') {
      this.client.send(new ShowInventory(this.client.getPlayerId()));
    } else if (keyEvent.getKeyChar() == 't') {
      this.saveGame();
    } else if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
      this.sendChatMessage();
    } else {
      Command command = this.commandForKey(keyEvent.getKeyChar());

      if (command != null) {
        this.client.send(new StartCommand(this.client.getPlayerId(), command));
      }
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

  /**
   * Lets the user enter a chat message and sends it.
   */
  private void sendChatMessage () {
    String message = JOptionPane.showInputDialog(this.canvas, "Nachricht");

    if (message != null) {
      this.client.sendChatMessage(message);
    }
  }

  private void saveGame () {
    World world = this.client.getWorld();

    if (world.getPlayers().size() > 1) {
      JOptionPane.showMessageDialog(this.canvas, "Man kann im Mehrspielermodus nicht speichern.");
      return;
    }

    JFileChooser fileChooser = new JFileChooser();
    fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Dungeon Game Speicherstand", "dungeon"));

    int button = fileChooser.showSaveDialog(this.canvas);

    if (button == JFileChooser.APPROVE_OPTION) {
      File selectedFile = fileChooser.getSelectedFile();

      new WorldLoader().saveToFile(this.client.getWorld(), selectedFile);

      JOptionPane.showMessageDialog(this.canvas, "Gespeichert");
    }
  }
}
