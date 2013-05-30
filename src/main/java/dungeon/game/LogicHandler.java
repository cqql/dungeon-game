package dungeon.game;

import dungeon.game.messages.DefeatEvent;
import dungeon.game.messages.WinEvent;
import dungeon.load.messages.LevelLoadedEvent;
import dungeon.messages.Mailman;
import dungeon.messages.Message;
import dungeon.messages.MessageHandler;
import dungeon.models.*;
import dungeon.models.messages.IdentityTransform;
import dungeon.models.messages.Transform;
import dungeon.pulse.Pulse;
import dungeon.ui.messages.Command;
import dungeon.ui.messages.EndCommand;
import dungeon.ui.messages.MoveCommand;
import dungeon.ui.messages.StartCommand;
import dungeon.util.Vector;

import java.util.EnumSet;
import java.util.Set;

/**
 * Handles the game logic.
 */
public class LogicHandler implements MessageHandler {
  private static final int SPEED = 1000;

  private final Mailman mailman;

  private final Set<MoveCommand> activeMoveDirections = EnumSet.noneOf(MoveCommand.class);

  private long lastPulse = 0;
  private int pulseDelta = 0;

  private World world;

  public LogicHandler (Mailman mailman) {
    this.mailman = mailman;
  }

  @Override
  public void handleMessage (Message message) {
    if (message instanceof LevelLoadedEvent) {
      this.world = ((LevelLoadedEvent)message).getWorld();

      this.activeMoveDirections.clear();
    } else if (message instanceof StartCommand) {
      Command command = ((StartCommand)message).getCommand();

      if (command instanceof MoveCommand) {
        this.activeMoveDirections.add((MoveCommand)command);
      }
    } else if (message instanceof EndCommand) {
      Command command = ((EndCommand)message).getCommand();

      if (command instanceof MoveCommand) {
        this.activeMoveDirections.remove(command);
      }
    } else if (message instanceof Pulse) {
      this.pulse();
    }
  }

  private void pulse () {
    if (this.world == null) {
      return;
    }

    this.updatePulseDelta();

    this.applyTransform(this.handleMovement());
    this.applyTransform(this.handleEnemies());
    this.applyTransform(this.handleTeleporters());

    this.handleDefeat();
    this.handleWin();
  }

  private void updatePulseDelta () {
    long now = System.currentTimeMillis();

    // If there are more than 2^32 seconds between two pulses, we are already extinct.
    this.pulseDelta = (int)(now - this.lastPulse);
    this.lastPulse = now;
  }

  private double getPulseDelta () {
    return this.pulseDelta / 1000.0;
  }

  /**
   * Applies a transform to the internal World object and sends it to the mailman.
   */
  private void applyTransform (Transform transform) {
    this.world = this.world.apply(transform);
    this.mailman.send(transform);
  }

  private Transform handleMovement () {
    Transform movementTransform = moveTransform();
    movementTransform = filterWalls(movementTransform);

    return filterBorders(movementTransform);
  }

  /**
   * Create the appropriate MoveTransform with respect to the currently active directions.
   */
  private Transform moveTransform () {
    Vector direction = new Vector(0, 0);

    for (MoveCommand moveCommand : this.activeMoveDirections) {
      direction = direction.plus(moveCommand.getDirection());
    }

    if (direction.isZero()) {
      return new IdentityTransform();
    } else {
      direction = direction.normalize();
      direction = direction.times(SPEED * this.getPulseDelta());

      return new Player.MoveTransform((int)direction.getX(), (int)direction.getY());
    }
  }

  /**
   * Prevent movement if the player would walk on a wall.
   */
  private Transform filterWalls (Transform transform) {
    Player movedPlayer = this.world.getPlayer().apply(transform);

    for (Tile wall : this.world.getCurrentRoom().getWalls()) {
      if (movedPlayer.touches(wall)) {
        return new IdentityTransform();
      }
    }

    return transform;
  }

  /**
   * Prevent movement if the player would leave the playing field.
   */
  private Transform filterBorders (Transform transform) {
    Player movedPlayer = this.world.getPlayer().apply(transform);

    if (movedPlayer.getPosition().getY() < 0
      || movedPlayer.getPosition().getY() + Player.SIZE > this.world.getCurrentRoom().getYSize()
      || movedPlayer.getPosition().getX() < 0
      || movedPlayer.getPosition().getX() + Player.SIZE > this.world.getCurrentRoom().getXSize()) {
      return new IdentityTransform();
    } else {
      return transform;
    }
  }

  /**
   * Translate enemy contact into a transform.
   */
  private Transform handleEnemies () {
    for (Enemy enemy : this.world.getCurrentRoom().getEnemies()) {
      if (this.world.getPlayer().touches(enemy)) {
        return new Player.HitpointTransform(-1);
      }
    }

    return new IdentityTransform();
  }

  /**
   * Create a teleport transform it the player touches a teleporter.
   */
  private Transform handleTeleporters () {
    for (TeleporterTile teleporter : this.world.getCurrentRoom().getTeleporters()) {
      if (this.world.getPlayer().touches(teleporter)) {
        TeleporterTile.Target target = teleporter.getTarget();

        return new Player.TeleportTransform(target.getRoomId(), target.getX(), target.getY());
      }
    }

    return new IdentityTransform();
  }

  private void handleDefeat () {
    if (this.world.getPlayer().getHitPoints() == 0) {
      this.mailman.send(new DefeatEvent());
    }
  }

  private void handleWin () {
    for (VictoryTile tile : this.world.getCurrentRoom().getVictoryTiles()) {
      if (this.world.getPlayer().touches(tile)) {
        this.mailman.send(new WinEvent());
      }
    }
  }
}
