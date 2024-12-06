package org.apache.click.examples.page.general;

import lombok.val;
import org.apache.click.Page;
import org.apache.click.util.ClickUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serial;
import java.io.UncheckedIOException;
import java.nio.file.Paths;
import java.util.Map;

import static java.nio.charset.StandardCharsets.*;

/**
 * Provides an example direct <tt>HttpServletResponse</tt> handling.
 */
public class DirectPage extends Page {
  @Serial private static final long serialVersionUID = 1L;

  /**
   * Render the Java source file as "text/plain".
   *
   * @see Page#onGet()
   */
  @Override
	public void onGet () {
    val fileName1 = "/readme.txt";// getClass().getName().replace('.', '/');
    val fileName2 = "/click-page.properties";
    val fileName3 = "/META-INF/MANIFEST.MF";

    HttpServletResponse response = getContext().getResponse();

    response.setContentType("text/plain");
    response.setCharacterEncoding("UTF-8");
    response.setHeader("Pragma", "no-cache");

    ServletContext context = getContext().getServletContext();

    try (PrintWriter writer = response.getWriter()){
      downloadFileWithLineNumber(context, fileName1, writer);
      downloadFileWithLineNumber(context, fileName2, writer);
      downloadFileWithLineNumber(context, fileName3, writer);

      //!!! Set page path to null to signal to ClickServlet that rendering has been completed
      setPath(null);

      if (getContext().hasRequestParameter("more")){
				writer.println("#".repeat(160));
        writer.println("##### More #####\n\n");
        writer.println(System.getProperty("user.dir"));// ~ C:\opt\github\click
        writer.println(Paths.get(".").toAbsolutePath());// ~ C:\Users\magic\.gradle\daemon\7.6\.

        System.getProperties().entrySet().stream()
            .map(ClickUtils::<Map.Entry<String,String>>castUnsafe)
            .sorted(Map.Entry.comparingByKey())
            .forEach(e -> writer.println(e.getKey()+" = "+e.getValue()));

				writer.println("-".repeat(160));
				val servletConfig = getContext().getServletConfig();
				servletConfig.getInitParameterNames().asIterator().forEachRemaining(paramName->
						writer.println(paramName+" = "+servletConfig.getInitParameter(paramName))
				);
				writer.println("-".repeat(160));
				context.getInitParameterNames().asIterator().forEachRemaining(paramName->
						writer.println(paramName+" = "+context.getInitParameter(paramName))
				);
      }
    } catch (IOException ioe){
      throw new UncheckedIOException(ioe);
    }
  }

  void downloadFileWithLineNumber (ServletContext context, String fileName, PrintWriter writer) throws IOException {
		InputStream resourceAsStream = context.getResourceAsStream(fileName);
		if (resourceAsStream == null){// значит он не в META-INF/resources, а просто resources
			resourceAsStream = getClass().getResourceAsStream(fileName);
		}
		try (val reader = new BufferedReader(new InputStreamReader(resourceAsStream, UTF_8)) ){
      String line;  int i = 0;

      writer.println("# ");
      writer.println("# "+fileName + "\t→\t"+context.getRealPath(fileName));
      writer.println("# ");

      while (( line = reader.readLine() ) != null){
        writer.println(i++ + "\t" + line);
      }
    }
  }
}