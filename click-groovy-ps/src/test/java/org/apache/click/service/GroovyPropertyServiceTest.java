package org.apache.click.service;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import lombok.val;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class GroovyPropertyServiceTest {

  static boolean done;

  private static final class Foo {
    private static int bar;

    private static void test(){
      //System.out.println("Hello Groovy!");
      done = true;
    }
  }

  @Test public void groovyPrivate () {
    Binding b = new Binding();
    GroovyShell sh = new GroovyShell(b);
    val o = sh.evaluate("org.apache.click.service.GroovyPropertyServiceTest.Foo.bar=42");
    assertTrue(o instanceof Integer);
    assertEquals(42, o);
    assertEquals(42, sh.evaluate("org.apache.click.service.GroovyPropertyServiceTest.Foo.bar"));

    assertFalse(done);
    sh.evaluate("org.apache.click.service.GroovyPropertyServiceTest$Foo.test()");
    assertTrue(done);
  }
}