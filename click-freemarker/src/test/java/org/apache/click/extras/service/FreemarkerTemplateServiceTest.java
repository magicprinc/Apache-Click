package org.apache.click.extras.service;

import org.apache.click.MockContext;
import org.junit.Test;

import java.io.StringWriter;
import java.util.Collections;

import static org.junit.Assert.*;

public class FreemarkerTemplateServiceTest {

  @Test
  public void basic () throws Exception {
//    MockServletContext ctx = new MockServletContext(".", "./src/test/resources/");
//    MockServletConfig cfg= new MockServletConfig("FakeName", ctx);
    MockContext mockContext = MockContext.initContext(".");

    FreemarkerTemplateService t = new FreemarkerTemplateService();
    t.onInit(mockContext.getServletContext());

    StringWriter w = new StringWriter();

    t.renderTemplate("test.txt", Collections.emptyMap(), w);

    assertEquals("Hello World!", w.toString());
  }


}