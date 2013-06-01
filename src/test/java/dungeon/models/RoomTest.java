package dungeon.models;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RoomTest {
  private Room room;

  @Before
  public void setUp () {
    this.room = new Room(
      "test-room",
      new ArrayList<Enemy>(),
      new ArrayList<SavePoint>(),
      Arrays.asList(
        new Tile(false, new Position(0, 1000)),
        new Tile(false, new Position(3000, 5000)),
        new VictoryTile(new Position(1000, 2000)),
        new Tile(true, new Position(0, 0)),
        new Tile(true, new Position(3000, 0)),
        new TeleporterTile(new Position(0, 4000), new TeleporterTile.Target("test-room", 0, 0))
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

  @Test
  public void getVictoryTilesReturnsAllVictoryTiles () {
    List<VictoryTile> tiles = this.room.getVictoryTiles();

    assertEquals(1, tiles.size());
  }

  @Test
  public void getWallsReturnsAllBlockingTiles () {
    List<Tile> walls = this.room.getWalls();

    assertEquals(2, walls.size());
  }

  @Test
  public void getTeleportersReturnsAllTeleporters () {
    List<TeleporterTile> teleporters = this.room.getTeleporters();

    assertEquals(1, teleporters.size());
  }
}
