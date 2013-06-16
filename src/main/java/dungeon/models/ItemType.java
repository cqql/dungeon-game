package dungeon.models;

public enum ItemType {
  HEALTH_POTION("Heiltrank", true, false, 5) {
    @Override
    public String getDescription () {
      return "Heilt " + this.getHitPointDelta() + " HP";
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

  @Override
  public String toString () {
    return "[" + this.name() + " useable=" + this.useable + ", equipable=" + this.equipable + ", hpDelta=" + this.hitPointDelta + "]";
  }
}
