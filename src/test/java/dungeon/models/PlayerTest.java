package dungeon.models;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerTest {
  private Player player;

  @Before
  public void setUp () {
    this.player = new Player("player", 5, "room", new Position(2000, 2000));
  }

  @Test
  public void moveTransformUpdatesPosition () {
    Player transformed = this.player.apply(new Player.MoveTransform(1200, 2000));

    assertEquals(3200, transformed.getPosition().getX());
    assertEquals(4000, transformed.getPosition().getY());
  }

  @Test
  public void hitpointTransformUpdatesHitpoints () {
    Player transformed = this.player.apply(new Player.HitpointTransform(-1));

    assertEquals(4, transformed.getHitPoints());
  }

  @Test
  public void touchesOverlappingEnemy () {
    Enemy enemy = new Enemy(new Position(1500, 1500));

    assertTrue(player.touches(enemy));
  }

  @Test
  public void doesNotTouchDistantEnemy () {
    Enemy enemy = new Enemy(new Position(0, 0));

    assertFalse(player.touches(enemy));
  }

  @Test
  public void touchesOverlappingTile () {
    assertTrue(player.touches(new Tile(false, new Position(1500, 1500))));
  }

  @Test
  public void doesNotTouchDistantTile () {
    assertFalse(player.touches(new Tile(false, new Position(1500, 3500))));
  }
}
