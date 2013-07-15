package dungeon.game;

import dungeon.game.messages.TalkToNpc;
import dungeon.game.messages.TradeWithMerchant;
import dungeon.models.*;
import dungeon.models.messages.IdentityTransform;
import dungeon.models.messages.Transform;
import dungeon.ui.messages.MoveCommand;
import dungeon.util.Vector;

import java.awt.geom.Rectangle2D;
import java.util.*;
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

  private static final int SPEED = 3000;

  private static final int MANA_DELAY = 2000;

  /**
   * The next available ID.
   *
   * IDs in maps may only be between 0 and 999999.
   *
   * @see #nextId()
   */
  private int nextId = 1000000;

  private final List<Player> joiningPlayers = new ArrayList<>();

  private final List<Integer> leavingPlayerIds = new ArrayList<>();

  private Map<Integer, PlayerState> playerStates = new HashMap<>();

  private GameState gameState = GameState.PLAYING;

  private World world;

  public GameLogic (World world) {
    this.world = world;
  }

  /**
   * Injects a given world into the game logic.
   *
   * This is used to load save files.
   */
  public void setWorld (World world) {
    this.world = world;
    this.playerStates = new HashMap<>();

    for (Player player : this.world.getPlayers()) {
      this.playerStates.put(player.getId(), new PlayerState());
    }
  }

  /**
   * Set a move flag.
   */
  public void activateMoveDirection (int playerId, MoveCommand command) {
    PlayerState state = this.getPlayerState(playerId);

    state.activeMoveDirections.add(command.getDirection());
    state.viewingDirection = command.getDirection();
  }

  /**
   * Reset a move flag.
   */
  public void deactivateMoveDirection (int playerId, MoveCommand command) {
    PlayerState state = this.getPlayerState(playerId);

    state.activeMoveDirections.remove(command.getDirection());
  }

  /**
   * Set the attacking flag.
   */
  public void activateAttack (int playerId) {
    PlayerState state = this.getPlayerState(playerId);

    state.attacking = true;
  }

  /**
   * Reset the attacking flag.
   */
  public void deactivateAttack (int playerId) {
    PlayerState state = this.getPlayerState(playerId);

    state.attacking = false;
  }

  /**
   * Use a health potion during the next pulse.
   */
  public void useHealthPotion (int playerId) {
    PlayerState state = this.getPlayerState(playerId);

    state.useHealthPotion = true;
  }

  /**
   * Use a mana potion during the next pulse.
   */
  public void useManaPotion (int playerId) {
    PlayerState state = this.getPlayerState(playerId);

    state.useManaPotion = true;
  }

  /**
   * Use {@code item} during the next pulse.
   */
  public void useItem (int playerId, Item item) {
    PlayerState state = this.getPlayerState(playerId);

    state.useItems.add(item);
  }

  /**
   * Equip {@code item} during the next pulse.
   */
  public void equipWeapon (int playerId, Item item) {
    PlayerState state = this.getPlayerState(playerId);

    state.equipWeapon = item;
  }

  /**
   * Cast spell of given type.
   */
  public void activateSpell (int playerId, DamageType damageType) {
    PlayerState state = this.getPlayerState(playerId);

    state.castSpell = damageType;
  }

  /**
   * Stop casting spells.
   */
  public void deactivateSpell (int playerId) {
    PlayerState state = this.getPlayerState(playerId);

    state.castSpell = null;
  }

  /**
   * Sell item on next pulse.
   */
  public void sellItem (int playerId, Merchant merchant, Item item) {
    PlayerState state = this.getPlayerState(playerId);

    List<Item> items = state.sellItems.get(merchant);

    if (items == null) {
      items = new ArrayList<>();
    }

    items.add(item);

    state.sellItems.put(merchant, items);
  }

  /**
   * Buy item on next pulse.
   */
  public void buyItem (int playerId, Merchant merchant, Item item) {
    PlayerState state = this.getPlayerState(playerId);

    List<Item> items = state.buyItems.get(merchant);

    if (items == null) {
      items = new ArrayList<>();
    }

    items.add(item);

    state.buyItems.put(merchant, items);
  }

  /**
   * Interact with a nearby NPC on next pulse.
   */
  public void interact (int playerId) {
    PlayerState state = this.getPlayerState(playerId);

    state.interact = true;
  }

  /**
   * Create a new player state, when a player joins the game.
   */
  public void addPlayer (Player player) {
    this.joiningPlayers.add(player);
  }

  /**
   * Removes the player from the world.
   */
  public void removePlayer (int playerId) {
    this.leavingPlayerIds.add(playerId);
  }

  /**
   * Returns the current game state.
   *
   * You can use this to check, if the player has died, won, etc.
   */
  public GameState getGameState () {
    return this.gameState;
  }

  public World getWorld () {
    return this.world;
  }

  /**
   * Compute all changes, that have happened in the last #delta seconds.
   *
   * @return A transaction of all changes that have happened
   */
  public Transaction pulse (double delta) {
    Transaction transaction = new Transaction(this.world);

    this.handleHealthPotion(transaction);
    this.handleManaPotion(transaction);
    this.useItems(transaction);
    this.equipWeapon(transaction);
    this.sellItems(transaction);
    this.buyItems(transaction);
    this.handleMovement(transaction, delta);
    this.handleProjectiles(transaction, delta);
    this.updateViewingDirection(transaction);
    this.handleDrops(transaction);
    this.moveEnemies(transaction, delta);
    this.handleEnemies(transaction);
    this.handleEnemyLives(transaction);
    this.handleTeleporters(transaction);
    this.handleCheckpoint(transaction);
    this.handleRespawn(transaction);
    this.handleMana(transaction);
    this.handleAttack(transaction);
    this.handleSpellCasting(transaction);
    this.handleInteractionWithNpcs(transaction);
    this.joinPlayers(transaction);
    this.removePlayers(transaction);
    this.handleQuests(transaction);

    this.world = transaction.getWorld();

    this.handleDefeat();

    return transaction;
  }

  /**
   * Use a health potion if the player has one.
   */
  private void handleHealthPotion (Transaction transaction) {
    for (Player player : transaction.getWorld().getPlayers()) {
      PlayerState state = this.getPlayerState(player);

      if (state.useHealthPotion) {
        state.useHealthPotion = false;

        List<Item> healthPotions = player.getHealthPotions();

        if (healthPotions.size() > 0) {
          Item healthPotion = healthPotions.get(0);

          healthPotion.use(transaction, player);
          transaction.pushAndCommit(new Player.RemoveItemTransform(player, healthPotion));
        }
      }
    }
  }

  /**
   * Use a mana potion if the player has one.
   */
  private void handleManaPotion (Transaction transaction) {
    for (Player player : transaction.getWorld().getPlayers()) {
      PlayerState state = this.getPlayerState(player);

      if (state.useManaPotion) {
        state.useManaPotion = false;

        List<Item> manaPotions = player.getManaPotions();

        if (manaPotions.size() > 0) {
          Item manaPotion = manaPotions.get(0);

          manaPotion.use(transaction, player);
          transaction.pushAndCommit(new Player.RemoveItemTransform(player, manaPotion));
        }
      }
    }
  }

  /**
   * Use the items, that have been requested.
   */
  private void useItems (Transaction transaction) {
    for (Player player : transaction.getWorld().getPlayers()) {
      PlayerState state = this.getPlayerState(player);

      for (Item item : state.useItems) {
        LOGGER.info("Use item " + item);

        item.use(transaction, player);

        transaction.pushAndCommit(new Player.RemoveItemTransform(player, item));
      }

      state.useItems.clear();
    }
  }

  private void equipWeapon (Transaction transaction) {
    for (Player player : transaction.getWorld().getPlayers()) {
      PlayerState state = this.getPlayerState(player);

      if (state.equipWeapon != null && state.equipWeapon.getType().isEquipable()) {
        LOGGER.info("Equip weapon " + state.equipWeapon);

        transaction.pushAndCommit(new Player.EquipWeaponTransform(player, state.equipWeapon.getId()));

        state.equipWeapon = null;
      }
    }
  }

  private void sellItems (Transaction transaction) {
    for (Player player : transaction.getWorld().getPlayers()) {
      PlayerState state = this.getPlayerState(player);

      for (Map.Entry<Merchant, List<Item>> entry : state.sellItems.entrySet()) {
        for (Item item : entry.getValue()) {
          Merchant merchant = transaction.getWorld().getCurrentRoom(player).findMerchant(entry.getKey());

          if (merchant.getMoney() >= item.getValue()) {
            transaction.pushAndCommit(new Merchant.BuyItemTransform(merchant, item));
            transaction.pushAndCommit(new Player.MoneyTransform(player, item.getValue()));
            transaction.pushAndCommit(new Player.RemoveItemTransform(player, item));
          }
        }
      }

      state.sellItems.clear();
    }
  }

  private void buyItems (Transaction transaction) {
    for (Player player : transaction.getWorld().getPlayers()) {
      PlayerState state = this.getPlayerState(player);

      for (Map.Entry<Merchant, List<Item>> entry : state.buyItems.entrySet()) {
        for (Item item : entry.getValue()) {
          Merchant merchant = transaction.getWorld().getCurrentRoom(player).findMerchant(entry.getKey());

          if (player.getMoney() >= item.getValue()) {
            transaction.pushAndCommit(new Merchant.SellItemTransform(merchant, item));
            transaction.pushAndCommit(new Player.MoneyTransform(player, -item.getValue()));
            transaction.pushAndCommit(new Player.AddItemTransform(player, item));
          }
        }
      }

      state.buyItems.clear();
    }
  }

  private void handleMovement (Transaction transaction, double delta) {
    for (Player player : transaction.getWorld().getPlayers()) {
      Transform movementTransform = moveTransform(player, delta);
      movementTransform = filterWalls(player, movementTransform);
      Player movedPlayer = player.apply(movementTransform);

      if (!this.outOfBorders(movedPlayer, transaction.getWorld().getCurrentRoom(player))) {
        transaction.pushAndCommit(movementTransform);
      }
    }
  }

  /**
   * Create the appropriate MoveTransform with respect to the currently active directions.
   */
  private Transform moveTransform (Player player, double delta) {
    PlayerState state = this.getPlayerState(player);

    Vector finalDirection = new Vector(0, 0);

    for (Direction direction : state.activeMoveDirections) {
      finalDirection = finalDirection.plus(direction.getVector());
    }

    if (finalDirection.isZero()) {
      return new IdentityTransform();
    } else {
      finalDirection = finalDirection.normalize();
      finalDirection = finalDirection.times(SPEED * delta);

      return new Player.MoveTransform(player, finalDirection);
    }
  }

  /**
   * Prevent movement if the player would walk on a wall.
   */
  private Transform filterWalls (Player player, Transform transform) {
    Player movedPlayer = player.apply(transform);

    for (Tile wall : this.world.getCurrentRoom(player).getWalls()) {
      if (this.touch(movedPlayer, wall)) {
        return new IdentityTransform();
      }
    }

    return transform;
  }

  /**
   * Move the projectiles and collide it with walls and borders.
   */
  private void handleProjectiles (Transaction transaction, double delta) {
    Set<Room> rooms = transaction.getWorld().getCurrentRooms();

    for (Room room : rooms) {
      for (Projectile projectile : room.getProjectiles()) {
        transaction.pushAndCommit(new Projectile.MoveTransform(projectile.getId(), new Position(projectile.getPosition().getVector().plus(projectile.getVelocity().times(delta)))));

        for (Tile wall : room.getWalls()) {
          if (this.touch(wall, projectile)) {
            transaction.pushAndCommit(new Room.RemoveProjectileTransform(room.getId(), projectile));
            break;
          }
        }

        if (this.outOfBorders(projectile, room)) {
          transaction.pushAndCommit(new Room.RemoveProjectileTransform(room.getId(), projectile));
          break;
        }

        for (Player player : transaction.getWorld().getPlayers()) {
          if (this.touch(player, projectile) && !player.equals(projectile.getSource())) {
            this.damagePlayer(transaction, player, projectile.getDamage());
            transaction.pushAndCommit(new Room.RemoveProjectileTransform(room.getId(), projectile));
            break;
          }
        }

        for (Enemy enemy : room.getEnemies()) {
          if (this.touch(enemy, projectile) && !enemy.equals(projectile.getSource())) {
            this.hitEnemy(transaction, enemy, projectile);
            transaction.pushAndCommit(new Enemy.MoveTransform(enemy, projectile.getVelocity().normalize().times(100)));
            transaction.pushAndCommit(new Room.RemoveProjectileTransform(room.getId(), projectile));
            break;
          }
        }
      }
    }
  }

  private void moveEnemies (Transaction transaction, double delta) {
    for (Room room : transaction.getWorld().getCurrentRooms()) {
      for (Enemy enemy : room.getEnemies()) {
        enemy.getMoveStrategy().move(transaction, room, enemy, delta);
      }
    }
  }

  /**
   * Update the direction the players are currently facing.
   */
  private void updateViewingDirection (Transaction transaction) {
    for (Player player : transaction.getWorld().getPlayers()) {
      PlayerState state = this.getPlayerState(player);

      if (player.getViewingDirection() != state.viewingDirection) {
        transaction.pushAndCommit(new Player.ViewingDirectionTransform(player, state.viewingDirection));
      }
    }
  }

  /**
   * Pickup drops that the player is touching.
   */
  private void handleDrops (Transaction transaction) {
    for (Player player : transaction.getWorld().getPlayers()) {
      Room currentRoom = transaction.getWorld().getCurrentRoom(player);

      for (Drop drop : currentRoom.getDrops()) {
        if (this.touch(player, drop)) {
          LOGGER.info("Pick up " + drop);

          transaction.push(new Room.RemoveDropTransform(currentRoom, drop));

          if (drop.isMoney()) {
            transaction.push(new Player.MoneyTransform(player, drop.getMoney()));
          } else {
            transaction.push(new Player.AddItemTransform(player, drop.getItem()));
          }

          transaction.commit();
        }
      }
    }
  }

  /**
   * Damage player on enemy contact.
   */
  private void handleEnemies (Transaction transaction) {
    for (Player player : transaction.getWorld().getPlayers()) {
      for (Enemy enemy : transaction.getWorld().getCurrentRoom(player).getEnemies()) {
        if (this.touch(player, enemy)) {
          this.damagePlayer(transaction, player, enemy.getStrength());
        }
      }
    }
  }

  /**
   * Destroy enemies when their hit points drop below 0.
   *
   * Enemies leave a random item.
   */
  private void handleEnemyLives (Transaction transaction) {
    for (Room room : transaction.getWorld().getCurrentRooms()) {
      for (Enemy enemy : room.getEnemies()) {
        if (enemy.getHitPoints() <= 0) {
          transaction.pushAndCommit(new Room.RemoveEnemyTransform(room, enemy));

          ItemType[] itemTypes = ItemType.values();
          ItemType itemType = itemTypes[(new Random()).nextInt(itemTypes.length)];

          transaction.pushAndCommit(
            new Room.AddDropTransform(
              room.getId(),
              new Drop(
                this.nextId(),
                enemy.getPosition(),
                new Item(this.nextId(), itemType),
                0
              )
            )
          );

          if (enemy.getOnDeath() != null) {
            if ("VICTORY".equals(enemy.getOnDeath())) {
              this.gameState = GameState.VICTORY;
            } else {
              String[] parts = enemy.getOnDeath().split("#");
              String levelId = parts[0];
              String roomId = parts[1];

              transaction.pushAndCommit(new Player.AdvanceLevelTransform(levelId, roomId));
            }
          }
        }
      }
    }
  }

  /**
   * Create a teleport transform if the player touches a teleporter.
   */
  private void handleTeleporters (Transaction transaction) {
    for (Player player : transaction.getWorld().getPlayers()) {
      for (TeleporterTile teleporter : transaction.getWorld().getCurrentRoom(player).getTeleporters()) {
        if (this.touch(player, teleporter)) {
          TeleporterTile.Target target = teleporter.getTarget();

          transaction.pushAndCommit(new Player.TeleportTransform(player, target.getRoomId(), target.getPosition()));

          // Next player
          break;
        }
      }
    }
  }

  /**
   * Create a savepoint transform if the player touches a savepoint
   */
  private void handleCheckpoint (Transaction transaction) {
    for (Player player : transaction.getWorld().getPlayers()) {
      for (SavePoint savePoint : transaction.getWorld().getCurrentRoom(player).getSavePoints()) {
        if (this.touch(player, savePoint)) {
          transaction.pushAndCommit(
            new Player.SavePointTransform(player.getRoomId(), player.getPosition())
          );

          return;
        }
      }
    }
  }

  /**
   * Reset HP when players loses a life and respawn the player if he dies
   */
  private void handleRespawn (Transaction transaction) {
    for (Player player : transaction.getWorld().getPlayers()) {
      if (player.getHitPoints() == 0) {
        transaction.pushAndCommit(new Player.RespawnTransform(player));
      }
    }
  }

  /**
   * Restore mana every 5 seconds by 1
   */
  private void handleMana (Transaction transaction) {
    for (Player player : transaction.getWorld().getPlayers()) {
      PlayerState state = this.getPlayerState(player);

      if (player.getMana() < player.getMaxMana()
        && System.currentTimeMillis() - state.lastManaRestoreTime > MANA_DELAY
        && System.currentTimeMillis() - state.lastManaUsedTime > MANA_DELAY) {
        state.lastManaRestoreTime = System.currentTimeMillis();

        transaction.pushAndCommit(new Player.ManaTransform(player, 1));
      }
    }
  }

  /**
   * Set the game state to DEFEAT when any player's lives drop to 0.
   */
  private void handleDefeat () {
    for (Player player : this.world.getPlayers()) {
      if (player.getLives() == 0) {
        this.gameState = GameState.DEFEAT;
      }
    }
  }

  /**
   * Create a new projectile if the player is attacking.
   */
  private void handleAttack (Transaction transaction) {
    for (Player player : transaction.getWorld().getPlayers()) {
      PlayerState state = this.getPlayerState(player);

      if (state.attacking && System.currentTimeMillis() - state.lastAttackTime > 200) {
        state.lastAttackTime = System.currentTimeMillis();

        Projectile projectile = player.attack(this.nextId());
        Room currentRoom = transaction.getWorld().getCurrentRoom(player);

        transaction.pushAndCommit(new Room.AddProjectileTransform(currentRoom.getId(), projectile));
      }
    }
  }

  /**
   * Create projectiles of activated type.
   */
  private void handleSpellCasting (Transaction transaction) {
    for (Player player : transaction.getWorld().getPlayers()) {
      PlayerState state = this.getPlayerState(player);

      if (state.castSpell != null && player.getMana() > 0) {
        state.lastManaUsedTime = System.currentTimeMillis();

        Projectile projectile = player.castSpell(this.nextId(), state.castSpell);
        Room currentRoom = transaction.getWorld().getCurrentRoom(player);

        transaction.pushAndCommit(new Player.ManaTransform(player, -1));
        transaction.pushAndCommit(new Room.AddProjectileTransform(currentRoom.getId(), projectile));

        state.castSpell = null;
      }
    }
  }

  /**
   * Interact with a nearby NPC and merchants.
   */
  private void handleInteractionWithNpcs (Transaction transaction) {
    for (Player player : transaction.getWorld().getPlayers()) {
      PlayerState state = this.getPlayerState(player);

      if (!state.interact) {
        continue;
      }

      state.interact = false;

      Vector playerPosition = player.getCenter().getVector();
      Room currentRoom = transaction.getWorld().getCurrentRoom(player);

      for (NPC npc : currentRoom.getNpcs()) {
        Vector npcPosition = npc.getCenter().getVector();

        double distance = npcPosition.minus(playerPosition).length();

        if (distance < (NPC.SIZE + Player.SIZE) * Math.sqrt(2) / 2) {
          transaction.pushAndCommit(new TalkToNpc(player, npc));
          Quest quest = npc.getQuest();

          if (quest != null && !player.hasQuest(quest)) {
            transaction.pushAndCommit(new Player.AddQuestTransform(player, quest));
          }
          break;
        }
      }

      for (Merchant merchant : currentRoom.getMerchants()) {
        Vector merchantPosition = merchant.getCenter().getVector();

        double distance = merchantPosition.minus(playerPosition).length();

        if (distance < (Merchant.SIZE + Player.SIZE) * Math.sqrt(2) / 2) {
          transaction.pushAndCommit(new TradeWithMerchant(player, merchant));
          break;
        }
      }
    }
  }

  private void joinPlayers (Transaction transaction) {
    for (Player player : this.joiningPlayers) {
      LOGGER.info("Player joined " + player);

      transaction.pushAndCommit(new World.AddPlayerTransform(player));

      this.playerStates.put(player.getId(), new PlayerState());
    }

    this.joiningPlayers.clear();
  }

  private void removePlayers (Transaction transaction) {
    for (int id : this.leavingPlayerIds) {
      LOGGER.info("Player left " + id);

      transaction.pushAndCommit(new World.RemovePlayerTransform(id));

      this.playerStates.remove(id);
    }

    this.leavingPlayerIds.clear();
  }

  private void handleQuests (Transaction transaction) {
    for (Player player : this.world.getPlayers()) {
      for (Quest quest : player.getOpenQuests()) {
        if (quest.isSolved(transaction.getWorld())) {
          transaction.pushAndCommit(new Player.SolveQuestTransform(player, quest));
          quest.giveReward(transaction, player);
        }
      }
    }
  }

  private PlayerState getPlayerState (int playerId) {
    return this.playerStates.get(playerId);
  }

  private PlayerState getPlayerState (Player player) {
    return this.playerStates.get(player.getId());
  }

  /**
   * Returns a free ID.
   */
  public int nextId () {
    return this.nextId++;
  }

  /**
   * Inflict {@code amount} damage on player if he has not suffered any damage lately.
   */
  private void damagePlayer (Transaction transaction, Player player, int amount) {
    PlayerState state = this.getPlayerState(player);

    if (System.currentTimeMillis() - state.lastDamageTime > 1000) {
      state.lastDamageTime = System.currentTimeMillis();

      transaction.pushAndCommit(new Player.HitPointTransform(player, -amount));
    }
  }

  /**
   * Collides an {@code enemy} with a {@code projectile}.
   */
  private void hitEnemy (Transaction transaction, Enemy enemy, Projectile projectile) {
    if (projectile.getType() == DamageType.NORMAL) {
      transaction.pushAndCommit(new Enemy.HitPointTransform(enemy, -projectile.getDamage()));
    } else {
      if (projectile.getType().isStrongAgainst(enemy.getType())) {
        transaction.pushAndCommit(new Enemy.HitPointTransform(enemy, -(projectile.getDamage() + 2)));
      } else if (enemy.getType().isStrongAgainst(projectile.getType())) {
        transaction.pushAndCommit(new Enemy.HitPointTransform(enemy, +2));
      } else {
        transaction.pushAndCommit(new Enemy.HitPointTransform(enemy, -1));
      }
    }
  }

  /**
   * Helper to check if two spatial objects touch.
   */
  private boolean touch (Spatial a, Spatial b) {
    return a.space().intersects(b.space());
  }

  /**
   * Helper to check if a spatial object is out of the playing field.
   */
  private boolean outOfBorders (Spatial object, Room room) {
    Rectangle2D space = object.space();

    return space.getMinX() < 0 || space.getMinY() < 0 || space.getMaxX() > room.getXSize() || space.getMaxY() > room.getYSize();
  }
}
