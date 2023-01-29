package template.page;

import lombok.extern.slf4j.Slf4j;
import org.apache.click.Page;
import org.slf4j.Logger;

import java.io.Serial;

/**
 *  Provides the base page with include business services, which
 *  other application pages should extend.
 */
@Slf4j
public class BasePage extends Page {
  @Serial private static final long serialVersionUID = -119652952210110698L;


  /**
   * Return the class logger.
   *
   * @return the class logger
   */
  public Logger getLogger() { return log;}

}