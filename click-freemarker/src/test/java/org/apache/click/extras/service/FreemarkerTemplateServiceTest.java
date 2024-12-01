package org.apache.click.extras.service;

import lombok.val;
import org.apache.click.MockContainer;
import org.apache.click.MockContext;
import org.apache.click.service.ConfigService;
import org.apache.click.util.ClickUtils;
import org.junit.Test;

import java.io.File;
import java.io.StringWriter;
import java.util.Collections;

import static org.apache.click.util.ClickTestUtils.deleteDir;
import static org.apache.click.util.ClickTestUtils.makeTmpDir;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

  @Test
  public void testTemplateService() throws Exception {
    File tmpdir = makeTmpDir();
//    PrintStream pstr = makeXmlStream(tmpdir, "WEB-INF/click.xml");
//    pstr.println("<click-app>");
//    pstr.println(" <pages/>");
//    //pstr.println(" <template-service classname='org.apache.click.service.VelocityTemplateService'/>");
//    //pstr.println(" <template-service classname='org.apache.click.service.MVELTemplateService'/>");
//    pstr.println(" <template-service classname='org.apache.click.extras.service.FreemarkerTemplateService'/>");
//    pstr.println("</click-app>");
//    pstr.close();

    val container = new MockContainer(tmpdir.getAbsolutePath());
		container.getServletContext().addInitParameter("template-service", "org.apache.click.extras.service.FreemarkerTemplateService");
		container.getServletContext().addInitParameter("pages", "mock");
    container.start();

    ConfigService config = ClickUtils.getConfigService(container.getServletContext());

    assertTrue(config.getTemplateService() instanceof FreemarkerTemplateService);

    container.stop();
    deleteDir(tmpdir);
  }
}