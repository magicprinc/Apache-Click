package org.apache.click.utilinvokelistener;

import junit.framework.TestCase;
import org.apache.click.util.ActionListenerAdaptor;
import org.apache.click.util.ClickUtils;

/**
 * Tests ClickUtils.invokeLister(). This is in a separate package
 * because otherwise the protected, package-private restrictions
 * would have no meaning.
 */
public class ClickUtilsInvokeListenerTest extends TestCase {

  /**
   * Test whether invoke listener functions correctly.
   */
  public void testInvokeListener() {
    ListenerMock lm = new ListenerMock();

    assertTrue(ClickUtils.invokeListener(lm, "onClickTrue"));
    assertEquals(1,lm.called);
    lm.called = 0;

    assertFalse(ClickUtils.invokeListener(lm, "onClickFalse"));
    assertEquals(1,lm.called);
    lm.called = 0;


    assertTrue(new ActionListenerAdaptor(lm, "onClickTrue").onAction(null));
    assertEquals(1,lm.called);
    lm.called = 0;

    assertFalse(new ActionListenerAdaptor(lm, "onClickFalse").onAction(null));
    assertEquals(1,lm.called);
    lm.called = 0;


    try{
      ClickUtils.invokeListener(lm,"noReturn");
      fail();
    }catch(Exception e){
      assertTrue(e.toString().startsWith("java.lang.IllegalArgumentException: Invalid listener method, missing boolean return type: public void org.apache.click.utilinvokelistener.ClickUtilsInvokeListenerTest$ListenerMock.noReturn()"));
    }

    try{
      ClickUtils.invokeListener(lm,"privateMethod");
      fail();
    }catch(Exception e){
      assertTrue(e.toString().startsWith("java.lang.IllegalArgumentException: Can't find public method: privateMethod in org.apache.click.utilinvokelistener.ClickUtilsInvokeListenerTest$ListenerMock@"));
    }

    try{
      ClickUtils.invokeListener(lm,"protectedMethod");
      fail();
    }catch(Exception e){
      assertTrue(e.toString().startsWith("java.lang.IllegalArgumentException: Can't find public method: protectedMethod in org.apache.click.utilinvokelistener.ClickUtilsInvokeListenerTest$ListenerMock@"));
    }

    try{
      ClickUtils.invokeListener(lm,"packagePrivateMethod");
      fail();
    }catch(Exception e){
      assertTrue(e.toString().startsWith("java.lang.IllegalArgumentException: Can't find public method: packagePrivateMethod in org.apache.click.utilinvokelistener.ClickUtilsInvokeListenerTest$ListenerMock@"));
    }

    PrivListenerMock pM = new PrivListenerMock();
    try{
      ClickUtils.invokeListener(pM,"onClick");
      fail();
    }catch(Exception e){
      assertTrue(e.toString().startsWith("java.lang.IllegalStateException: Exception occurred invoking public method: public boolean org.apache.click.utilinvokelistener.ClickUtilsInvokeListenerTest$PrivListenerMock.onClick() on org.apache.click.utilinvokelistener."));
    }

    //the anonymous inner class
    Object anon = new Object() {
      @SuppressWarnings("unused")
      public boolean onClick(){
        return true;
      }
      @SuppressWarnings("unused")
      private boolean privateMethod(){
        return true;
      }
      @SuppressWarnings({"unused", "ProtectedMemberInFinalClass"})
      protected boolean protectedMethod() {
        return true;
      }
      @SuppressWarnings("unused")
      boolean packagePrivateMethod(){
        return false;
      }
    };

    assertTrue(ClickUtils.invokeListener(anon, "onClick"));

    try{
      ClickUtils.invokeListener(anon,"noMethod");
      fail();
    }catch(Exception e){
      assertTrue(e.toString().startsWith("java.lang.IllegalArgumentException: Can't find public method: noMethod in org.apache.click.utilinvokelistener.ClickUtilsInvokeListenerTest$"));
    }

    try{
      ClickUtils.invokeListener(anon,"privateMethod");
      fail();
    }catch(Exception e){
      assertTrue(e.toString().startsWith("java.lang.IllegalArgumentException: Can't find public method: privateMethod in org.apache.click.utilinvokelistener.ClickUtilsInvokeListenerTest$"));
    }

    try{
      ClickUtils.invokeListener(anon,"protectedMethod");
      fail();
    }catch(Exception e){
      assertTrue(e.toString().startsWith("java.lang.IllegalArgumentException: Can't find public method: protectedMethod in org.apache.click.utilinvokelistener.ClickUtilsInvokeListenerTest$"));
    }

    try{
      ClickUtils.invokeListener(anon,"packagePrivateMethod");
      fail();
    }catch(Exception e){
      assertTrue(e.toString().startsWith("java.lang.IllegalArgumentException: Can't find public method: packagePrivateMethod in org.apache.click.utilinvokelistener.ClickUtilsInvokeListenerTest$"));
    }
  }

  /**
   * Public mock class which listens to events.
   */
  public static class ListenerMock {
    /** Counts the amount of times listeners are fired. */
    int called = 0;

    /**
     * onClickTrue event handler.
     *
     * @return true if processing should continue
     */
    public boolean onClickTrue(){
      called++;
      return true;
    }

    /**
     * onClickFalse event handler.
     *
     * @return true if processing should continue
     */
    public boolean onClickFalse() {
      called++;
      return false;
    }

    /**
     * An event handler.
     */
    public void noReturn() {
    }

    /**
     * An event handler.
     *
     * @return true if processing should continue
     */
    @SuppressWarnings("unused")
    private boolean privateMethod(){
      return true;
    }

    /**
     * An event handler.
     *
     * @return true if processing should continue
     */
    protected boolean protectedMethod() {
      return true;
    }

    /**
     * An event handler.
     *
     * @return true if processing should continue
     */
    boolean packagePrivateMethod(){
      return true;
    }
  }

  /**
   * Private mock class which listens to events.
   */
  private static class PrivListenerMock {

    /**
     * onClick event handler.
     *
     * @return true if processing should continue
     */
    @SuppressWarnings("unused")
    public boolean onClick(){
      return true;
    }
  }
}