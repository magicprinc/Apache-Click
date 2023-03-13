package template.page.click;

import lombok.extern.slf4j.Slf4j;
import org.apache.click.extras.control.Menu;
import org.apache.click.extras.control.MenuFactory;



@Slf4j
public class ErrorPage extends org.apache.click.util.ErrorPage {
  private static final long serialVersionUID = 2323819756383921509L;

  public String title = "Error Page";

  private final Menu rootMenu;

  public ErrorPage() {
    MenuFactory menuFactory = new MenuFactory();
    rootMenu = menuFactory.getRootMenu();
    addControl(rootMenu);
  }

  @Override public void onDestroy() {
    super.onDestroy();

    Throwable error = getError();


    //getContext().getServletContext().log(error.toString(), error);
    log.warn("?", error);
  }

}