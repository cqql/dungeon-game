package dungeon.game;

import dungeon.game.messages.DefeatEvent;
import dungeon.game.messages.WinEvent;
import dungeon.load.messages.LevelLoadedEvent;
import dungeon.messages.Mailman;
import dungeon.messages.Message;
import dungeon.messages.MessageHandler;
import dungeon.models.messages.Transform;
import dungeon.pulse.Pulse;
import dungeon.ui.messages.Command;
import dungeon.ui.messages.EndCommand;
import dungeon.ui.messages.MoveCommand;
import dungeon.ui.messages.StartCommand;

import java.util.List;

/**
 * Handles the game logic.
 */
public class LogicHandler implements MessageHandler {
  private static final double MS_PER_SECOND = 1000;

  private final Mailman mailman;

  /**
   * Time when the last pulse came in in milliseconds.
   */
  private long lastPulse;

  /**
   * Time delta since the last pulse came in in milliseconds.
   */
  private int pulseDelta;

  private GameLogic logic;

  public LogicHandler (Mailman mailman) {
    this.mailman = mailman;
  }

  @Override
  public void handleMessage (Message message) {
    if (message instanceof LevelLoadedEvent) {
      this.logic = new GameLogic(((LevelLoadedEvent)message).getWorld());
    } else if (message instanceof StartCommand) {
      this.startCommand((StartCommand)message);
    } else if (message instanceof EndCommand) {
      this.endCommand((EndCommand)message);
    } else if (message instanceof Pulse) {
      this.pulse();
    }
  }

  private void startCommand (StartCommand message) {
    if (this.logic == null) {
      return;
    }

    Command command = message.getCommand();

    if (command instanceof MoveCommand) {
      this.logic.activateMoveDirection((MoveCommand)command);
    }
  }

  private void endCommand (EndCommand message) {
    if (this.logic == null) {
      return;
    }

    Command command = message.getCommand();

    if (command instanceof MoveCommand) {
      this.logic.deactivateMoveDirection((MoveCommand)command);
    }
  }

  /**
   * Apply all the changes to the world, that have happened since the last pulse.
   */
  private void pulse () {
    if (this.logic == null) {
      return;
    }

    this.updatePulseDelta();

    List<Transform> transforms = this.logic.pulse(this.getPulseDelta());

    for (Transform transform : transforms) {
      this.mailman.send(transform);
    }

    switch (this.logic.getGameState()) {
      case VICTORY:
        this.mailman.send(new WinEvent());

        this.reset();
        break;
      case DEFEAT:
        this.mailman.send(new DefeatEvent());

        this.reset();
        break;
      default:
    }
  }

  /**
   * Reset this handler.
   *
   * This is necessary, because otherwise the handler would continually send win or defeat events.
   */
  private void reset () {
    this.logic = null;
    this.lastPulse = 0;
    this.pulseDelta = 0;
  }

  /**
   * Recalculate the time since the last pulse.
   */
  private void updatePulseDelta () {
    long now = System.currentTimeMillis();

    // If there are more than 2^32 seconds between two pulses, we are already extinct.
    this.pulseDelta = (int)(now - this.lastPulse);
    this.lastPulse = now;
  }

  /**
   * @return the time since the last pulse came in in milliseconds
   */
  private double getPulseDelta () {
    return this.pulseDelta / MS_PER_SECOND;
  }
}
