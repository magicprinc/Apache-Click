package org.apache.click.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class ActionListenerAdaptorTest {

  int cnt;
  boolean returnValue = true;
  public boolean fake () {
    cnt++;
    return returnValue;
  }

  @Test
  public void basic () {
    ActionListenerAdaptor listener = new ActionListenerAdaptor(this, "fake");
    returnValue = true;
    assertTrue(listener.onAction(null));
    returnValue = false;
    assertFalse(listener.onAction(null));
    assertEquals(2, cnt);

    assertThrows(IllegalArgumentException.class, ()->new ActionListenerAdaptor(this, null));
    assertThrows(IllegalArgumentException.class, ()->new ActionListenerAdaptor(null, "fake"));
    assertThrows(IllegalArgumentException.class, ()->new ActionListenerAdaptor(this, ""));
  }

}