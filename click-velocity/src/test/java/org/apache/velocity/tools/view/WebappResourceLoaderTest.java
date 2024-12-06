package org.apache.velocity.tools.view;

import lombok.val;
import org.apache.click.MockContext;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.util.ExtProperties;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/** @see WebappResourceLoader */
public class WebappResourceLoaderTest {
  @Test
  public void basic () throws IOException {
    val ctx = MockContext.initContext();
    val wrl = new WebappResourceLoader();

    //ExtendedProperties p = mock(ExtendedProperties.class);
    RuntimeServices rsvc = mock(RuntimeServices.class);
		var ep = mock(ExtProperties.class);

		// eq(ServletContext.class.getName())
    when(rsvc.getApplicationAttribute(any())).thenReturn(ctx.getServletContext());

    wrl.commonInit(rsvc, ep);
    wrl.init(ep);
    val is = wrl.getResourceReader("test.txt", "UTF-8");
    assertEquals("Hello there!", IOUtils.toString(is));
    is.close();

    var e1 = assertThrows(ResourceNotFoundException.class, ()->wrl.getResourceReader("not_found!!!", "Utf-8"));
    assertEquals("org.apache.velocity.exception.ResourceNotFoundException: WebappResourceLoader: Resource 'not_found!!!' not found!", e1.toString());

    var e2 = assertThrows(ResourceNotFoundException.class, ()->wrl.getResourceReader("::/\\", "Utf-8"));
    assertEquals("org.apache.velocity.exception.ResourceNotFoundException: WebappResourceLoader: Resource '::/\\' not found!", e2.toString());

    // file:////test.txt (windows) -or- file:/test.txt (github: linux?)
//    assertEquals("file:/test.txt", wrl.getCachedFile("/", "test.txt").toURI().toString().replace("////", "/"));
//    assertEquals("/test.txt", wrl.getCachedFile("", "test.txt").toString().replace('\\','/'));

    Resource fileResource = mock(Resource.class);
    when(fileResource.getName()).thenReturn("test.txt");
    var msc = ctx.getServletContext();
    assertNull(msc.getWebappPath());
    assertNull(msc.getWebappRoot());
    msc.setWebappPath("src/test/resources/");
    assertEquals("src/test/resources/", msc.getWebappPath());
    assertTrue(msc.getWebappRoot().toURL().toString().endsWith("/click-velocity/src/test/resources/"));
    long lastModified = wrl.getLastModified(fileResource);
    // Sun Oct 23 03:45:39 MSK 2022 = 1666485939958
    assertTrue(new Date(lastModified)+" = "+lastModified+", instead of "+new Date(1666485938000L)+" = "+1666485938000L,
        lastModified>=1666485938000L);

    assertTrue(wrl.isSourceModified(fileResource));
    when(fileResource.getLastModified()).thenReturn(lastModified);
    assertFalse(wrl.isSourceModified(fileResource));
  }

//  @Test
//  public void utils () {
//    assertEquals("x", WebappResourceLoader.cutLeadingSlash(" ////\\\\ x\t\n"));
//  }
}