package dungeon;

/**
 * Log messages to STDOUT.
 */
public class Log {
  public static void notice (String message) {
    notice(message, null);
  }

  public static void notice (String message, Exception exception) {
    System.out.println(message);

    if (exception != null) {
      exception.printStackTrace();
    }
  }
}
