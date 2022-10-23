package org.apache.click.service;

import org.apache.click.MockContainer;
import org.apache.click.MockContext;
import org.apache.click.Page;
import org.apache.click.util.ClickUtils;
import org.junit.Test;

import java.io.File;
import java.io.PrintStream;
import java.io.StringWriter;
import java.util.Collections;

import static org.apache.click.util.ClickTestUtils.deleteDir;
import static org.apache.click.util.ClickTestUtils.makeTmpDir;
import static org.apache.click.util.ClickTestUtils.makeXmlStream;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class VelocityTemplateServiceTest {

  @Test public void testTemplateService() throws Exception {
    File tmpdir = makeTmpDir();

    PrintStream pstr = makeXmlStream(tmpdir, "WEB-INF/click.xml");
    pstr.println("<click-app>");
    pstr.println(" <pages/>");
    pstr.println(" <template-service classname='org.apache.click.service.VelocityTemplateService'/>");
    //pstr.println(" <template-service classname='org.apache.click.service.MVELTemplateService'/>");
    pstr.println("</click-app>");
    pstr.close();

    MockContainer container = new MockContainer(tmpdir.getAbsolutePath());
    container.start();

    ConfigService config = ClickUtils.getConfigService(container.getServletContext());

    assertTrue(config.getTemplateService() instanceof VelocityTemplateService);

    container.stop();
    deleteDir(tmpdir);
  }

  @Test public void basic () throws Exception {
    MockContext ctx = MockContext.initContext();

    TemplateService vt = new VelocityTemplateService();
    vt.onInit(ctx.getServletContext());

    StringWriter w = new StringWriter();
    vt.renderTemplate("test.txt", Collections.emptyMap(), w);
    assertEquals("Hello there!", w.toString());

    Page page = new Page();
    page.setTemplate("test.txt");
    w = new StringWriter();
    vt.renderTemplate(page, Collections.emptyMap(), w);
    assertEquals("Hello there!", w.toString());

    // ClickUtils.getConfigService(ctx.getServletContext())
    ConfigService clickConfigService = mock(ConfigService.class);
    when(clickConfigService.getCharset()).thenReturn("KAKA");
    ctx.getServletContext().setAttribute(ConfigService.CONTEXT_NAME, clickConfigService);
    w = new StringWriter();
    vt.renderTemplate(page, Collections.emptyMap(), w);
    assertEquals("Hello there!", w.toString());

    vt.onDestroy();
  }

  @Test public void basicErr () throws Exception {
    MockContext ctx = MockContext.initContext();

    // ClickUtils.getConfigService(ctx.getServletContext())
    ConfigService clickConfigService = mock(ConfigService.class);
    when(clickConfigService.getApplicationMode()).thenReturn("debug");
    when(clickConfigService.getCharset()).thenReturn("KAKA");
    when(clickConfigService.getServletContext()).thenReturn(ctx.getServletContext());
    ctx.getServletContext().setAttribute(ConfigService.CONTEXT_NAME, clickConfigService);

    TemplateService vt = new VelocityTemplateService();
    vt.onInit(ctx.getServletContext());

    StringWriter w = new StringWriter();
    assertThrows(TemplateException.class, ()->vt.renderTemplate("test.txt", Collections.emptyMap(), w));
    assertEquals("", w.toString());

    vt.onDestroy();
  }

}