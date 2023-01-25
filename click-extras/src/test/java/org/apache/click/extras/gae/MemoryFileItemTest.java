package org.apache.click.extras.gae;

import junit.framework.TestCase;
import org.apache.click.MockContainer;
import org.apache.click.control.FileField;
import org.apache.click.control.Form;
import org.apache.click.extras.pages.MemoryFileItemUploadPage;
import org.apache.click.servlet.MockRequest;

import java.io.File;

/**
 * Provides tests for the Google App Engine MemoryFileItem.
 */
public class MemoryFileItemTest extends TestCase {

  /**
   * Test Google App Engine (GAE) MemoryFileItem support.
   */
  public void testMemoryFileItem() {
    MockContainer container = new MockContainer("web");
    container.start();

    String firstname = "Steve";
    String lastname = "Jones";

    // Lookup path to files
    String file1Path = container.getServletContext().getRealPath("upload1.txt");
    String file2Path = container.getServletContext().getRealPath("upload2.txt");

    File upload1 = new File(file1Path);
    File upload2 = new File(file2Path);
    MockRequest request = container.getRequest();

    // Add couple of files to be uploaded
    request.addFile("upload1", upload1, "utf-8");
    request.addFile("upload2", upload2, "utf-8");

    // Set firstname and lastname request parameters
    request.setParameter("firstname", firstname);
    request.setParameter("lastname", lastname);

    // Set FORM_NAME parameter to ensure Form is submitted
    request.setParameter(Form.FORM_NAME, "form");

    MemoryFileItemUploadPage page = container.testPage(MemoryFileItemUploadPage.class);

    // Check that field parameters were processed
    assertEquals(firstname, page.getForm().getFieldValue("firstname"));
    assertEquals(lastname, page.getForm().getFieldValue("lastname"));

    // Check that file field parameters were processed
    FileField fileField1 = (FileField) page.getForm().getField("upload1");
    FileField fileField2 = (FileField) page.getForm().getField("upload2");
    assertEquals("upload1 success", fileField1.getFileItem().getString());
    assertEquals("upload2 success", fileField2.getFileItem().getString());

    container.stop();
  }
}