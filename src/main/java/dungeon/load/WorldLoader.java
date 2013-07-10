package dungeon.load;

import dungeon.load.adapters.WorldAdapter;
import dungeon.models.World;

import javax.xml.bind.JAXB;

/**
 * Loads the world from an XML file.
 */
public class WorldLoader {
  public World load () throws Exception {
    WorldAdapter worldAdapter = new WorldAdapter();

    World world = worldAdapter.unmarshal(JAXB.unmarshal(getClass().getClassLoader().getResourceAsStream("world.xml"), WorldAdapter.class));

    return world;
  }
}
