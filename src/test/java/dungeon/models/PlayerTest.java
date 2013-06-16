package dungeon.models;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class PlayerTest {
  private static final String ROOM_ID = "room";

  private Player player;

  @Before
  public void setUp () {
    this.player = new Player(0, "player", 1, 5, 5, 0, new ArrayList<Item>(), "level", ROOM_ID, new Position(2000, 2000), Direction.RIGHT, ROOM_ID, new Position(0, 0));
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
  public void teleportTransformUpdatesPosition () {
    Player teleported = this.player.apply(new Player.TeleportTransform("another-room", new Position(5, 1)));

    assertEquals("another-room", teleported.getRoomId());
    assertEquals(5, teleported.getPosition().getX());
    assertEquals(1, teleported.getPosition().getY());
  }
}
