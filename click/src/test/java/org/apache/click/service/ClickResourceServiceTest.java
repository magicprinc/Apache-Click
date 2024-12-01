package org.apache.click.service;

import lombok.val;
import org.apache.click.MockContext;
import org.apache.click.servlet.MockRequest;
import org.apache.click.servlet.MockResponse;
import org.junit.Test;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ClickResourceServiceTest {
  @Test
	public void testResourceLoading () throws IOException {
    val clickResourceService = new ClickResourceService();
    val context = MockContext.initContext(Map.of("mode", "profile"));
    clickResourceService.onInit(context.getServletContext());

    val req = new MockRequest();
    val resp = new MockResponse();

    req.setPathInfo("/table.css");
    assertTrue(clickResourceService.isResourceRequest(req));

    clickResourceService.renderResource(req, resp);
    assertEquals(HttpServletResponse.SC_NOT_FOUND, resp.getCode());

    resp.reset();
    req.setPathInfo("/click/table.css");
    assertTrue(clickResourceService.isResourceRequest(req));

    clickResourceService.renderResource(req, resp);
    assertEquals(HttpServletResponse.SC_OK, resp.getCode());
    var binaryContent = resp.getBinaryContent();
    var tableCss = new String(binaryContent, StandardCharsets.UTF_8);
    assertTrue(tableCss.contains("th.ascending a, th.descending a, th.sortable a {"));
    assertTrue(tableCss.contains(" * http://displaytag.sourceforge.net"));

    assertEquals(1, clickResourceService.resourceCache.size());
    assertTrue(clickResourceService.resourceCache.containsKey("/click/table.css"));
    assertArrayEquals(binaryContent, clickResourceService.resourceCache.get("/click/table.css"));

    clickResourceService.onDestroy();
  }
}