package org.apache.click.service;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import lombok.val;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GroovyTest {

  static boolean done;

  private static final class Foo {
    private static int bar;

    private static void test(){
      //System.out.println("Hello Groovy!");
      done = true;
    }
  }

  /** Groovy 1-3 can read/write private fields and call private methods, even in private class:
   Super Power for UnitTests and our field bindings */
  @Test public void groovyPrivate () {
    Binding b = new Binding();
    GroovyShell sh = new GroovyShell(b);
    val o = sh.evaluate("org.apache.click.service.GroovyTest.Foo.bar=42");
    assertTrue(o instanceof Integer);
    assertEquals(42, o);
    assertEquals(42, sh.evaluate("org.apache.click.service.GroovyTest.Foo.bar"));

    assertEquals("org.apache.click.service.GroovyTest$Foo", Foo.class.getName());

    assertFalse(done);
    sh.evaluate(Foo.class.getName()+".test()");
    assertTrue(done);
  }


  @SuppressWarnings("unchecked") @Test
  public void testScriptInterface () {
    Binding b = new Binding();
    GroovyShell sh = new GroovyShell(b);
    // Script + Function
    val script = sh.parse("static int calc (int a) { return a*2;}");

    assertEquals(84, script.invokeMethod("calc", 42));

    assertTrue(script.getMetaClass().getMethods().toString().contains("public static int Script1.calc(int)"));
  }
}