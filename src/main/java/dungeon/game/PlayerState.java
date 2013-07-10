package dungeon.game;

import dungeon.models.Direction;
import dungeon.models.Item;
import dungeon.models.Merchant;

import java.util.*;

/**
 * Saves all the state belonging to a single player, e.g. the currently pressed motion keys.
 */
public class PlayerState {
  long lastDamageTime;

  final Set<Direction> activeMoveDirections = EnumSet.noneOf(Direction.class);

  Direction viewingDirection = Direction.RIGHT;

  boolean attacking;

  boolean useIceBolt;

  boolean useHealthPotion;

  boolean useManaPotion;

  boolean interact;

  final List<Item> useItems = new ArrayList<>();

  /**
   * Which weapon to equip on next pulse.
   */
  Item equipWeapon;

  /**
   * Which items to sell to which merchant on next pulse.
   */
  final Map<Merchant, List<Item>> sellItems = new LinkedHashMap<>();

  /**
   * Which items to buy from which merchant on next pulse.
   */
  final Map<Merchant, List<Item>> buyItems = new LinkedHashMap<>();

  long lastAttackTime;

  long lastManaUsedTime;

  long lastManaRestoreTime;
}
