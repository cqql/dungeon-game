package dungeon.game;

import dungeon.models.*;
import dungeon.models.messages.IdentityTransform;
import dungeon.models.messages.Transform;
import dungeon.ui.messages.MoveCommand;
import dungeon.util.Vector;

import java.util.EnumSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * The game logic.
 *
 * It works a bit like a state machine. Every time you call {@link GameLogic#pulse(double)}, it evaluates
 * what happened in the amount of time, that you pass to {@link GameLogic#pulse(double)}, with respect to the flags,
 * that are set.
 *
 * Think of commands, that the player can send (like walking up), as flags. Let's say for example the UP and LEFT flag
 * are set and you call {@code pulse(150)}. This will return transforms that correspond to moving in up-left direction
 * for 150ms.
 */
public class GameLogic {
  private static final Logger LOGGER = Logger.getLogger(GameLogic.class.getName());

  private static final int SPEED = 1000;

  /**
   * The next available ID.
   *
   * @see #nextId()
   */
  private int nextId;

  private long lastDamageTime;

  private final Set<Direction> activeMoveDirections = EnumSet.noneOf(Direction.class);

  private Direction viewingDirection = Direction.RIGHT;

  private boolean attacking;

  private long lastAttackTime;

  private GameState gameState = GameState.PLAYING;

  private World world;

  public GameLogic (World world) {
    this.world = world;
  }

  /**
   * Set a move flag.
   */
  public void activateMoveDirection (MoveCommand command) {
    this.activeMoveDirections.add(command.getDirection());

    this.viewingDirection = command.getDirection();
  }

  /**
   * Reset a move flag.
   */
  public void deactivateMoveDirection (MoveCommand command) {
    this.activeMoveDirections.remove(command.getDirection());
  }

  /**
   * Set the attacking flag.
   */
  public void activateAttack () {
    this.attacking = true;
  }

  /**
   * Reset the attacking flag.
   */
  public void deactivateAttack () {
    this.attacking = false;
  }

  /**
   * Returns the current game state.
   *
   * You can use this to check, if the player has died, won, etc.
   */
  public GameState getGameState () {
    return this.gameState;
  }

  /**
   * Compute all changes, that have happened in the last #delta seconds.
   *
   * @return A transaction of all changes that have happened
   */
  public Transaction pulse (double delta) {
    Transaction transaction = new Transaction(this.world);

    this.handleMovement(transaction, delta);
    this.handleProjectiles(transaction, delta);
    this.updateViewingDirection(transaction);
    this.handleDrops(transaction);
    this.handleEnemies(transaction);
    this.handleTeleporters(transaction);
    this.handleCheckpoint(transaction);
    this.handleRespawn(transaction);
    this.handleAttacking(transaction);

    this.world = transaction.getWorld();

    this.handleDefeat();
    this.handleWin();

    return transaction;
  }

  private void handleMovement (Transaction transaction, double delta) {
    Transform movementTransform = moveTransform(delta);
    movementTransform = filterWalls(movementTransform);

    transaction.pushAndCommit(filterBorders(movementTransform));
  }

  /**
   * Create the appropriate MoveTransform with respect to the currently active directions.
   */
  private Transform moveTransform (double delta) {
    Vector finalDirection = new Vector(0, 0);

    for (Direction direction : this.activeMoveDirections) {
      finalDirection = finalDirection.plus(direction.getVector());
    }

    if (finalDirection.isZero()) {
      return new IdentityTransform();
    } else {
      finalDirection = finalDirection.normalize();
      finalDirection = finalDirection.times(SPEED * delta);

      return new Player.MoveTransform((int)finalDirection.getX(), (int)finalDirection.getY());
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
   * Move the projectiles.
   */
  private void handleProjectiles (Transaction transaction, double delta) {
    for (Projectile projectile : transaction.getWorld().getCurrentRoom().getProjectiles()) {
      transaction.pushAndCommit(new Projectile.MoveTransform(projectile.getId(), new Position(projectile.getPosition().getVector().plus(projectile.getVelocity().times(delta)))));
    }
  }

  /**
   * Update the direction the player is currently facing.
   */
  private void updateViewingDirection (Transaction transaction) {
    if (transaction.getWorld().getPlayer().getViewingDirection() != this.viewingDirection) {
      transaction.pushAndCommit(new Player.ViewingDirectionTransform(this.viewingDirection));
    }
  }

  /**
   * Pickup drops that the player is touching.
   */
  private void handleDrops (Transaction transaction) {
    for (Drop drop : transaction.getWorld().getCurrentRoom().getDrops()) {
      if (this.world.getPlayer().touches(drop)) {
        LOGGER.info("Pick up " + drop);

        transaction.push(new Room.RemoveDropTransform(drop.getId()));

        if (drop.isMoney()) {
          transaction.push(new Player.MoneyTransform(drop.getMoney()));
        } else {
          transaction.push(new Player.AddItemTransform(drop.getItem()));
        }

        transaction.commit();
      }
    }
  }

  /**
   * Translate enemy contact into a transform.
   */
  private void handleEnemies (Transaction transaction) {
    for (Enemy enemy : this.world.getCurrentRoom().getEnemies()) {
      if (System.currentTimeMillis() - this.lastDamageTime > 1000 && this.world.getPlayer().touches(enemy)) {
        this.lastDamageTime = System.currentTimeMillis();

        transaction.pushAndCommit(new Player.HitpointTransform(-enemy.getStrength()));
      }
    }
  }

  /**
   * Create a teleport transform if the player touches a teleporter.
   */
  private void handleTeleporters (Transaction transaction) {
    for (TeleporterTile teleporter : transaction.getWorld().getCurrentRoom().getTeleporters()) {
      if (this.world.getPlayer().touches(teleporter)) {
        TeleporterTile.Target target = teleporter.getTarget();

        transaction.pushAndCommit(new Player.TeleportTransform(target.getRoomId(), target.getPosition()));

        return;
      }
    }
  }

  /**
   * Create a savepoint transform if the player touches a savepoint
   */
  private void handleCheckpoint (Transaction transaction) {
    for (SavePoint savePoint : this.world.getCurrentRoom().getSavePoints()) {
      if (this.world.getPlayer().touches(savePoint)) {
        transaction.pushAndCommit(
          new Player.SavePointTransform(this.world.getPlayer().getRoomId(), this.world.getPlayer().getPosition()));

        return;
      }
    }
  }

  /**
   * Reset HP when players loses a life and respawn the player if he dies
   */
  private void handleRespawn (Transaction transaction) {
    if (this.world.getPlayer().getHitPoints() == 0) {
      transaction.pushAndCommit(new Player.LivesTransform(-1));
      transaction.pushAndCommit(new Player.HitpointTransform(this.world.getPlayer().getMaxHitPoints()));
      transaction.pushAndCommit(new Player.TeleportTransform(this.world.getPlayer().getSavePointRoomId(), this.world.getPlayer().getSavePointPosition()));
    }
  }

  /**
   * Set the game state to DEFEAT when the player's hit points drop to 0.
   */
  private void handleDefeat () {
    if (this.world.getPlayer().getLives() == 0) {
      this.gameState = GameState.DEFEAT;
    }
  }

  /**
   * Set the game state to VICTORY when the player touches a victory tile.
   */
  private void handleWin () {
    for (VictoryTile tile : this.world.getCurrentRoom().getVictoryTiles()) {
      if (this.world.getPlayer().touches(tile)) {
        this.gameState = GameState.VICTORY;
      }
    }
  }

  /**
   * Create a new projectile if the player is attacking.
   */
  private void handleAttacking (Transaction transaction) {
    if (this.attacking && System.currentTimeMillis() - this.lastAttackTime > 200) {
      this.lastAttackTime = System.currentTimeMillis();

      Projectile projectile = new Projectile(this.nextId(), transaction.getWorld().getPlayer().getPosition(), Direction.DOWN.getVector().times(5000), 1);

      transaction.pushAndCommit(new Room.AddProjectileTransform(transaction.getWorld().getCurrentRoom().getId(), projectile));
    }
  }

  /**
   * Returns a free ID.
   */
  private int nextId () {
    return this.nextId++;
  }
}
