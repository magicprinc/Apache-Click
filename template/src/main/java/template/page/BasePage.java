package template.page;

import lombok.extern.slf4j.Slf4j;
import org.apache.click.Page;
import org.slf4j.Logger;

/**
 *  Provides the base page with include business services, which
 *  other application pages should extend.
 */
@Slf4j
public class BasePage extends Page {


  /**
   * Return the class logger.
   *
   * @return the class logger
   */
  public Logger getLogger() { return log;}

}