package dungeon.models;

public enum ItemType {
  HEALTH_POTION(true, false, 5);

  private final boolean useable;

  private final boolean equipable;

  private final int hitPointDelta;

  private ItemType (boolean useable, boolean equipable, int hitPointDelta) {
    this.useable = useable;
    this.equipable = equipable;
    this.hitPointDelta = hitPointDelta;
  }

  public boolean isUseable () {
    return useable;
  }

  public boolean isEquipable () {
    return equipable;
  }

  public int getHitPointDelta () {
    return hitPointDelta;
  }

  @Override
  public String toString () {
    return "[" + this.name() + " useable=" + this.useable + ", equipable=" + this.equipable + ", hpDelta=" + this.hitPointDelta + "]";
  }
}
