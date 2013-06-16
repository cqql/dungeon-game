package dungeon.models;

import dungeon.game.Transaction;

public enum ItemType {
  HEALTH_POTION("Heiltrank", true, false, 5) {
    @Override
    public String getDescription () {
      return "Heilt " + this.getHitPointDelta() + " HP";
    }

    @Override
    public void use (Transaction transaction) {
      transaction.pushAndCommit(new Player.HitpointTransform(this.getHitPointDelta()));
    }
  };

  private final String name;

  private final boolean useable;

  private final boolean equipable;

  private final int hitPointDelta;

  private ItemType (String name, boolean useable, boolean equipable, int hitPointDelta) {
    this.name = name;
    this.useable = useable;
    this.equipable = equipable;
    this.hitPointDelta = hitPointDelta;
  }

  public String getName () {
    return this.name;
  }

  public abstract String getDescription ();

  public boolean isUseable () {
    return this.useable;
  }

  public boolean isEquipable () {
    return this.equipable;
  }

  public int getHitPointDelta () {
    return this.hitPointDelta;
  }

  /**
   * Use the item, e.g. apply the appropriate transforms.
   */
  public void use (Transaction transaction) {

  }

  @Override
  public String toString () {
    return "[" + this.name() + " useable=" + this.useable + ", equipable=" + this.equipable + ", hpDelta=" + this.hitPointDelta + "]";
  }
}
