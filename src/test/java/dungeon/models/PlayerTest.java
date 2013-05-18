package dungeon.models;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PlayerTest {
  private Player player;

  @Before
  public void setUp () {
    this.player = new Player("player", 5, "room", new Position(2, 2));
  }

  @Test
  public void moveTransformUpdatesPosition () {
    Player transformed = this.player.apply(new Player.MoveTransform(1.2f, 2));

    Assert.assertEquals(3.2f, transformed.getPosition().getX(), 0000000.1);
    Assert.assertEquals(4, transformed.getPosition().getY(), 0000000.1);
  }

  @Test
  public void hitpointTransformUpdatesHitpoints () {
    Player transformed = this.player.apply(new Player.HitpointTransform(-1));

    Assert.assertEquals(4, transformed.getHitPoints());
  }
}
