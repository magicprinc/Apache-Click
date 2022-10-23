package org.apache.click.util;

import lombok.NonNull;
import org.junit.Test;

import static org.junit.Assert.*;

public class ActionListenerAdaptorTest {

  int cnt;
  boolean returnValue = true;
  public boolean fake () {
    cnt++;
    return returnValue;
  }

  static void usedToTest (@NonNull Object value) {}

  static Class<Throwable> detectNonNull () {
    try {
      usedToTest(null);
    } catch (Throwable e) {
      return ClickUtils.castUnsafe(e.getClass());
    }
    return null;
  }

  @Test
  public void basic () {
    ActionListenerAdaptor listener = new ActionListenerAdaptor(this, "fake");
    returnValue = true;
    assertTrue(listener.onAction(null));
    returnValue = false;
    assertFalse(listener.onAction(null));
    assertEquals(2, cnt);

    Class<Throwable> ct = detectNonNull();
    assertTrue(ct.equals(IllegalArgumentException.class) || ct.equals(NullPointerException.class));

    assertThrows(ct, ()->new ActionListenerAdaptor(this, null));
    assertThrows(ct, ()->new ActionListenerAdaptor(null, "fake"));
    assertThrows(IllegalArgumentException.class, ()->new ActionListenerAdaptor(this, "x"));
  }

}