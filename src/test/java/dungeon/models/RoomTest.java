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
        new Tile(false, new Position(0, 1)),
        new Tile(false, new Position(3, 5))
      )
    );
  }

  @Test
  public void longestXSpanIsXSize () {
    assertEquals(4, room.getXSize(), 0.000001);
  }

  @Test
  public void longestYSpanIsYSize () {
    assertEquals(6, room.getYSize(), 0.000001);
  }
}
