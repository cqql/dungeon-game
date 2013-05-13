package dungeon.models.events;

import dungeon.events.Event;

public class TransformEvent implements Event {
  private final Transform transform;

  public TransformEvent (Transform transform) {
    this.transform = transform;
  }

  public Transform getTransform () {
    return transform;
  }
}
