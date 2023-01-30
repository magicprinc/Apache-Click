package org.apache.click.pages;

import org.apache.click.Context;
import org.apache.click.Page;

import java.io.IOException;
import java.io.Serial;

/**
 * Page that renders binary content.
 */
public class BinaryPage extends Page {
  @Serial private static final long serialVersionUID = -2139736468025117293L;

  @Override public void onInit() {
    try {
      Context context = getContext();

      // Retrieve the response outputStream. The servlet container will
      // throw an exception if Click tries to retrieve the response writer.
      // CLK-644 fixes the problem by using the response outputStream if the writer cannot be used
      context.getResponse().getOutputStream();

    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
  }
}