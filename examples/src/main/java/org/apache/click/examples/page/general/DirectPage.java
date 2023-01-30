package org.apache.click.examples.page.general;

import lombok.val;
import org.apache.click.Page;
import org.apache.click.util.ClickUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serial;
import java.io.UncheckedIOException;
import java.nio.file.Path;
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
  @Override public void onGet () {
    val fileName1 = "/readme.txt";// getClass().getName().replace('.', '/');
    val fileName2 = "/WEB-INF/classes/click-page.properties";
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
        writer.println("#\n# More:\n#\n");
        writer.println(System.getProperty("user.dir"));// ~ C:\opt\github\click
        writer.println(Path.of(".").toAbsolutePath());// ~ C:\Users\magic\.gradle\daemon\7.6\.

        System.getProperties().entrySet().stream()
            .map(ClickUtils::<Map.Entry<String,String>>castUnsafe)
            .sorted(Map.Entry.comparingByKey())
            .forEach(e -> writer.println(e.getKey()+" = "+e.getValue()));
      }

    } catch (IOException ioe){
      throw new UncheckedIOException(ioe);
    }
  }

  void downloadFileWithLineNumber (ServletContext context, String fileName, PrintWriter writer) throws IOException {
    try (val reader = new BufferedReader(new InputStreamReader( context.getResourceAsStream(fileName), UTF_8)) ){
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