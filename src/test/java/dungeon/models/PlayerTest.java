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

  @Test
  public void touchesOverlappingEnemy () {
    Enemy enemy = new Enemy(new Position(1.5f, 1.5f));

    Assert.assertTrue(player.touches(enemy));
  }

  @Test
  public void doesNotTouchDistantEnemy () {
    Enemy enemy = new Enemy(new Position(0, 0));

    Assert.assertFalse(player.touches(enemy));
  }

  @Test
  public void touchesOverlappingTile () {
    Assert.assertTrue(player.touches(new Tile(false, new Position(1.5f, 1.5f))));
  }

  @Test
  public void doesNotTouchDistantTile () {
    Assert.assertFalse(player.touches(new Tile(false, new Position(1.5f, 3.5f))));
  }
}
