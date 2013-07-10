package dungeon.game;

import dungeon.game.messages.DefeatEvent;
import dungeon.game.messages.WinEvent;
import dungeon.messages.LifecycleEvent;
import dungeon.messages.Mailman;
import dungeon.messages.Message;
import dungeon.messages.MessageHandler;
import dungeon.models.World;
import dungeon.pulse.Pulse;
import dungeon.ui.messages.*;

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

  /**
   * Is the game paused?
   */
  private boolean paused = true;

  public LogicHandler (Mailman mailman, World world) {
    this.mailman = mailman;
    this.logic = new GameLogic(world);
  }

  /**
   * Returns the current state of the world.
   */
  public World getWorld () {
    return this.logic.getWorld();
  }

  @Override
  public void handleMessage (Message message) {
    if (message instanceof Pulse && !this.paused) {
      this.pulse();
    } else if (message instanceof PlayerMessage) {
      int playerId = ((PlayerMessage)message).getPlayerId();

      if (message instanceof StartCommand) {
        this.startCommand((StartCommand)message);
      } else if (message instanceof EndCommand) {
        this.endCommand((EndCommand)message);
      } else if (message instanceof UseItemCommand) {
        this.logic.useItem(playerId, ((UseItemCommand)message).getItem());
      } else if (message instanceof EquipWeaponCommand) {
        this.logic.equipWeapon(playerId, ((EquipWeaponCommand)message).getWeapon());
      } else if (message instanceof BuyCommand) {
        this.logic.buyItem(playerId, ((BuyCommand)message).getMerchant(), ((BuyCommand)message).getItem());
      } else if (message instanceof SellCommand) {
        this.logic.sellItem(playerId, ((SellCommand)message).getMerchant(), ((SellCommand)message).getItem());
      }
    } else if (message instanceof World.AddPlayerTransform) {
      this.logic.addPlayer((World.AddPlayerTransform)message);
    } else if (message == MenuCommand.START_GAME) {
      this.paused = false;
    } else if (message == LifecycleEvent.INITIALIZE) {
      // Initialize the pulse delta. If you don't the first pulse will be from the beginning of the unix epoch until today.
      this.updatePulseDelta();
    }
  }

  private void startCommand (StartCommand message) {
    if (this.logic == null) {
      return;
    }

    int playerId = message.getPlayerId();
    Command command = message.getCommand();

    if (command instanceof MoveCommand) {
      this.logic.activateMoveDirection(playerId, (MoveCommand)command);
    } else if (command instanceof AttackCommand) {
      this.logic.activateAttack(playerId);
    } else if (command instanceof IceBoltAttackCommand) {
      this.logic.activateIceBolt(playerId);
    } else if (command instanceof HealthPotionCommand) {
      this.logic.useHealthPotion(playerId);
    } else if (command instanceof ManaPotionCommand) {
      this.logic.useManaPotion(playerId);
    } else if (command instanceof InteractCommand) {
      this.logic.interact(playerId);
    }
  }

  private void endCommand (EndCommand message) {
    if (this.logic == null) {
      return;
    }

    int playerId = message.getPlayerId();
    Command command = message.getCommand();

    if (command instanceof MoveCommand) {
      this.logic.deactivateMoveDirection(playerId, (MoveCommand)command);
    } else if (command instanceof AttackCommand) {
      this.logic.deactivateAttack(playerId);
    } else if (command instanceof IceBoltAttackCommand) {
      this.logic.deactivateIceBoltAttack(playerId);
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

    Transaction transaction = this.logic.pulse(this.getPulseDelta());

    for (Message message : transaction.getMessages()) {
      this.mailman.send(message);
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
