package net.sf.click.jquery.examples.util;

import net.sf.click.jquery.examples.domain.Customer;
import net.sf.click.jquery.examples.domain.PostCode;
import org.apache.click.util.ClickUtils;
import org.apache.commons.lang.WordUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Initializes application data upon startup.
 */
public class StartupListener implements ServletContextListener {

  private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");

  private long id = 0;

  public static final List<Customer> CUSTOMERS = new ArrayList<>();

  public static final List<PostCode> POST_CODES = new ArrayList<>();


  /**
   * @see ServletContextListener#contextInitialized(ServletContextEvent)
   */
  @Override public void contextInitialized (ServletContextEvent sce){
    try {
      loadCustomers();
      loadPostCodes();

    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Error creating database", e);
    }
  }

  /**
   * @see ServletContextListener#contextDestroyed(ServletContextEvent)
   */
  @Override public void contextDestroyed (ServletContextEvent sce){
  }


  private void loadCustomers() throws IOException {
    // Load customers data file
    loadFile("customers.txt", line->{
      StringTokenizer tokenizer = new StringTokenizer(line, ",");

      Customer customer = new Customer();
      customer.setId(nextId());

      customer.setName(next(tokenizer));
      if (tokenizer.hasMoreTokens()) {
        customer.setEmail(next(tokenizer));
      }
      if (tokenizer.hasMoreTokens()) {
        customer.setAge(Integer.valueOf(next(tokenizer)));
      }
      if (tokenizer.hasMoreTokens()) {
        customer.setInvestments(next(tokenizer));
      }
      if (tokenizer.hasMoreTokens()) {
        customer.setHoldings(Double.valueOf(next(tokenizer)));
      }
      if (tokenizer.hasMoreTokens()) {
        customer.setDateJoined(createDate(next(tokenizer)));
      }
      if (tokenizer.hasMoreTokens()) {
        customer.setActive(Boolean.valueOf(next(tokenizer)));
      }

      CUSTOMERS.add(customer);
    });
  }

  private void loadPostCodes() throws IOException {
    loadFile("post-codes-australian.csv", line->{
      if (!line.startsWith("Pcode")) {
        StringTokenizer tokenizer = new StringTokenizer(line, ",");

        String postcode = next(tokenizer);
        String locality = WordUtils.capitalizeFully(next(tokenizer));
        String state = next(tokenizer);

        PostCode postCode = new PostCode();
        postCode.setPostCode(postcode);
        postCode.setLocality(locality);
        postCode.setState(state);

        POST_CODES.add(postCode);
      }
    });
  }

  private static void loadFile(String filename, LineProcessor lineProcessor)
      throws IOException {

    InputStream is = ClickUtils.getResourceAsStream(filename, StartupListener.class);

    if (is == null) {
      throw new RuntimeException("classpath file not found: " + filename);
    }

    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new InputStreamReader(is));

      String line = reader.readLine();
      while (line != null) {
        line = line.trim();

        if (line.length() > 0 && !line.startsWith("#")) {
          lineProcessor.processLine(line);
        }

        line = reader.readLine();
      }
    } finally {
      ClickUtils.close(reader);
    }
  }

  private synchronized long nextId() {
    return id++;
  }

  private static String next(StringTokenizer tokenizer) {
    String token = tokenizer.nextToken().trim();
    if (token.startsWith("\"")) {
      token = token.substring(1);
    }
    if (token.endsWith("\"")) {
      token = token.substring(0, token.length() - 1);
    }
    return token;
  }

  private static Date createDate(String pattern) {
    try {
      return FORMAT.parse(pattern);
    } catch (ParseException pe) {
      return null;
    }
  }


  private interface LineProcessor {
    void processLine(String line);
  }
}