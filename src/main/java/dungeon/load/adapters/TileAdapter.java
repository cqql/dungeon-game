package dungeon.load.adapters;

import dungeon.models.Position;
import dungeon.models.TeleporterTile;
import dungeon.models.Tile;
import dungeon.models.VictoryTile;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

public class TileAdapter extends XmlAdapter<TileAdapter, Tile> {
  @XmlAttribute
  public boolean blocking;

  @XmlJavaTypeAdapter(PositionAdapter.class)
  public Position position;

  @Override
  public Tile unmarshal (TileAdapter adapter) throws Exception {
    if (adapter instanceof TeleporterTileAdapter) {
      return new TeleporterTile(((TeleporterTileAdapter)adapter).position, ((TeleporterTileAdapter)adapter).target);
    } else if (adapter instanceof VictoryTileAdapter) {
      return new VictoryTile(adapter.position);
    } else {
      return new Tile(adapter.blocking, adapter.position);
    }
  }

  @Override
  public TileAdapter marshal (Tile tile) throws Exception {
    if (tile instanceof TeleporterTile) {
      TeleporterTileAdapter adapter = new TeleporterTileAdapter();
      adapter.blocking = tile.isBlocking();
      adapter.position = tile.getPosition();
      adapter.target = ((TeleporterTile)tile).getTarget();

      return adapter;
    } else if (tile instanceof VictoryTile) {
      VictoryTileAdapter adapter = new VictoryTileAdapter();
      adapter.blocking = tile.isBlocking();
      adapter.position = tile.getPosition();

      return adapter;
    } else {
      TileAdapter adapter = new TileAdapter();
      adapter.blocking = tile.isBlocking();
      adapter.position = tile.getPosition();

      return adapter;
    }
  }
}
