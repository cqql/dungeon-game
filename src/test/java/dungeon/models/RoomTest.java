package dungeon.models;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class RoomTest {
  private Room room;

  @Before
  public void setUp () {
    this.room = new Room(
      "test-room",
      new ArrayList<Enemy>(),
      Arrays.asList(
        new Tile(false, new Position(0, 1000)),
        new Tile(false, new Position(3000, 5000))
      )
    );
  }

  @Test
  public void longestXSpanIsXSize () {
    assertEquals(3000 + Tile.SIZE, room.getXSize());
  }

  @Test
  public void longestYSpanIsYSize () {
    assertEquals(5000 + Tile.SIZE, room.getYSize());
  }
}
