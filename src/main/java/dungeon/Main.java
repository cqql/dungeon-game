package dungeon;

import dungeon.ui.GUI;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.logging.Logger;

public class Main {
  private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

  public static void main (String[] args) {
    GUI gui = null;

    try {
      gui = new GUI();
    } catch (UnsupportedAudioFileException e) {
      LOGGER.warning("Could not load audio files");
    } catch (IOException e) {
      LOGGER.warning("Could not load audio files");
    }

    if (gui != null) {
      gui.run();
    }
  }
}
