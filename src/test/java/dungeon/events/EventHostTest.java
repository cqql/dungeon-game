package dungeon.events;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class EventHostTest {
  private static class TestEvent implements Event {

  }

  private EventHost eventHost;

  @Before
  public void setUp () {
    eventHost = new EventHost(Executors.newSingleThreadExecutor());
  }

  @Test(timeout = 100)
  public void itReturnsOnShutdownEvent () {
    eventHost.publish(LifecycleEvents.SHUTDOWN);

    eventHost.run();
  }

  @Test
  public void itPublishesEventsToListeners () {
    final AtomicBoolean eventPublished = new AtomicBoolean(false);
    final Event testEvent = new TestEvent();

    eventHost.addListener(new AbstractEventListener() {
      @Override
      public void onEvent (Event event) {
        if (event == testEvent) {
          eventPublished.set(true);
        }
      }
    });

    eventHost.publish(testEvent);
    eventHost.publish(LifecycleEvents.SHUTDOWN);
    eventHost.run();

    Assert.assertTrue(eventPublished.get());
  }

  @Test
  public void itSendsAnInitializeEvent () {
    final AtomicBoolean eventPublished = new AtomicBoolean(false);

    eventHost.addListener(new AbstractEventListener() {
      @Override
      public void onEvent (Event event) {
        if (event == LifecycleEvents.INITIALIZE) {
          eventPublished.set(true);
        }
      }
    });

    eventHost.publish(LifecycleEvents.SHUTDOWN);
    eventHost.run();

    Assert.assertTrue(eventPublished.get());
  }
}
