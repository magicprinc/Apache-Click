package org.apache.click.examples.util;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.access.DataDomain;
import org.apache.cayenne.access.DataNode;
import org.apache.cayenne.access.DbGenerator;
import org.apache.cayenne.conf.Configuration;
import org.apache.cayenne.conf.ServletUtil;
import org.apache.cayenne.map.DataMap;
import org.apache.cayenne.query.SelectQuery;
import org.apache.click.examples.domain.Course;
import org.apache.click.examples.domain.Customer;
import org.apache.click.examples.domain.PostCode;
import org.apache.click.examples.domain.StudentHouse;
import org.apache.click.examples.domain.SystemCode;
import org.apache.click.examples.domain.User;
import org.apache.click.util.ClickUtils;
import org.apache.commons.lang.WordUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Provides a database initialization servlet context listener. This listener
 * creates an example database schema using the Cayenne {@link DbGenerator}
 * utility class, and loads data files into the database. This class also
 * adds an ExampleJob to the Quartz Scheduler.
 * <p/>
 * This listener also provides a customer reloading task which runs every 15 minutes.
 */
@Slf4j
public class DatabaseInitListener implements ServletContextListener {
  private static final long RELOAD_TIMER_INTERVAL = 1000 * 60 * 5;

  private final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");

  private final Timer reloadTimer = new Timer(true);


  /**
   * @see ServletContextListener#contextInitialized(ServletContextEvent)
   */
  @Override
	public void contextInitialized(ServletContextEvent sce) {
    ServletContext servletContext = sce.getServletContext();
    ServletUtil.initializeSharedConfiguration(servletContext);
    try {
      DataDomain cayenneDomain = Configuration.getSharedConfiguration().getDomain();
      DataMap dataMap = cayenneDomain.getMap("cayenneMap");
      DataNode dataNode = cayenneDomain.getNode("cayenneNode");

      initDatabaseSchema(dataNode, dataMap);

      loadDatabase();
    } catch (Exception e){
      throw new RuntimeException("Error creating database", e);
    }
  }

  /**
   * @see ServletContextListener#contextDestroyed(ServletContextEvent)
   */
  @Override public void contextDestroyed(ServletContextEvent sce) {
    reloadTimer.cancel();
  }


  /**
   * Create the demonstration database schema using the given Cayenne
   * DataNode and DataMap.
   *
   * @param dataNode the Cayenne DataNode
   * @param dataMap the Cayenne DataMap
   * @throws Exception
   */
  private void initDatabaseSchema(DataNode dataNode, DataMap dataMap) throws Exception {
    val generator = new DbGenerator(dataNode.getAdapter(), dataMap);
    generator.setShouldCreateFKConstraints(true);
    generator.setShouldCreatePKSupport(true);
    generator.setShouldCreateTables(true);
    generator.setShouldDropPKSupport(false);
    generator.setShouldDropTables(false);

    generator.runGenerator(dataNode.getDataSource());
  }

  /**
   * Load data files into the database
   *
   * @throws IOException if an I/O error occurs
   */
  private void loadDatabase() throws IOException {
    final DataContext dataContext = DataContext.createDataContext();

    // Load users data file
    loadUsers(dataContext);

    // Load customers data file
    loadCustomers(dataContext);

    // Load customers data file
    loadSystemCodes(dataContext);

    // Load post codes data file
    loadPostCodes(dataContext);

    // Load course data file
    loadCourses(dataContext);

    // Load student houses data file
    loadStudentHouses(dataContext);

    dataContext.commitChanges();
  }

  private static void loadFile(String filename, DataContext dataContext,
      LineProcessor lineProcessor) throws IOException {

    InputStream is = ClickUtils.getResourceAsStream(filename, DatabaseInitListener.class);

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
          lineProcessor.processLine(line, dataContext);
        }

        line = reader.readLine();
      }
    } finally {
      ClickUtils.close(reader);
    }
  }

  private void loadUsers(final DataContext dataContext) throws IOException {
    loadFile("users.txt", dataContext, (line, context)->{
      val tokenizer = new StringTokenizer(line, ",");

      User user = new User();

      user.setUsername(next(tokenizer));
      user.setPassword(next(tokenizer));
      user.setFullname(next(tokenizer));
      user.setEmail(next(tokenizer));

      context.registerNewObject(user);
    });
  }

  private void loadCustomers(final DataContext dataContext) throws IOException {
    // Load customers data file
    loadFile("customers.txt", dataContext, (line, context)->{
      val tokenizer = new StringTokenizer(line, ",");

      Customer customer = new Customer();
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

      dataContext.registerNewObject(customer);
    });
  }

  private void loadSystemCodes(final DataContext dataContext) throws IOException {
    loadFile("system-codes.txt", dataContext, (line, context)->{
      StringTokenizer tokenizer = new StringTokenizer(line, ",");

      SystemCode systemCode = new SystemCode();
      systemCode.setName(next(tokenizer));
      systemCode.setValue(next(tokenizer));
      systemCode.setLabel(next(tokenizer));
      systemCode.setOrderBy(Integer.valueOf((next(tokenizer))));

      dataContext.registerNewObject(systemCode);
    });
  }

  private void loadPostCodes(final DataContext dataContext) throws IOException {
    loadFile("post-codes-australian.csv", dataContext, (line, context)->{
      if (!line.startsWith("Pcode")) {
        StringTokenizer tokenizer = new StringTokenizer(line, ",");

        String postcode = next(tokenizer);
        String locality = WordUtils.capitalizeFully(next(tokenizer));
        String state = next(tokenizer);

        PostCode postCode = new PostCode();
        postCode.setPostCode(postcode);
        postCode.setLocality(locality);
        postCode.setState(state);

        dataContext.registerNewObject(postCode);
      }
    });
  }

  private void loadCourses(final DataContext dataContext) throws IOException {
    loadFile("courses.txt", dataContext, (line, context)->{
      StringTokenizer tokenizer = new StringTokenizer(line, ",");

      Course course = new Course();

      course.setName(next(tokenizer));

      context.registerNewObject(course);
    });
  }

  private void loadStudentHouses(final DataContext dataContext) throws IOException {
    loadFile("student-houses.txt", dataContext, (line, context)->{
      StringTokenizer tokenizer = new StringTokenizer(line, ",");

      StudentHouse studentHouse = new StudentHouse();

      studentHouse.setName(next(tokenizer));

      context.registerNewObject(studentHouse);
    });
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

  private Date createDate(String pattern) {
    try {
      return FORMAT.parse(pattern);
    } catch (ParseException pe) {
      return null;
    }
  }

  @FunctionalInterface
	private interface LineProcessor {
    void processLine(String line, DataContext dataContext);
  }
  @AllArgsConstructor
  private static class ReloadTask extends TimerTask {
    private final DatabaseInitListener databaseInitListener;

    @Override @SuppressWarnings("unchecked")
    public void run() {
      DataContext dataContext = null;
      try {
        dataContext = DataContext.createDataContext();

        SelectQuery query = new SelectQuery(Customer.class);
        List<Customer> list = dataContext.performQuery(query);

        if (list.size() < 60) {
          dataContext.deleteObjects(list);

          databaseInitListener.loadCustomers(dataContext);

          dataContext.commitChanges();
        }

      } catch (Throwable t) {
        log.warn("failed", t);

        if (dataContext != null) {
          dataContext.rollbackChanges();
        }
      }
    }
  }//ReloadTask
}