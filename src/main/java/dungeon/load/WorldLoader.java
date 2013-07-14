package dungeon.load;

import dungeon.load.adapters.WorldAdapter;
import dungeon.models.World;

import javax.xml.bind.JAXB;
import java.io.File;

/**
 * Loads the world from an XML file.
 */
public class WorldLoader {
  private final WorldAdapter worldAdapter = new WorldAdapter();

  public void saveToFile (World world, File file) {
    JAXB.marshal(this.worldAdapter.marshal(world), file);
  }

  public World load () throws Exception {
    World world = this.worldAdapter.unmarshal(JAXB.unmarshal(getClass().getClassLoader().getResourceAsStream("world.xml"), WorldAdapter.class));

    return world;
  }
}
