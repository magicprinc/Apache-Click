package template.page;

import org.apache.click.extras.control.Menu;
import org.apache.click.extras.control.MenuFactory;

import java.io.Serial;

public class BorderPage extends BasePage {

    @Serial private static final long serialVersionUID = 1L;

    private Menu rootMenu;

    public BorderPage() {
        MenuFactory menuFactory = new MenuFactory();
        rootMenu = menuFactory.getRootMenu();
        addControl(rootMenu);
    }

    /**
     * @see #getTemplate()
     */
    @Override public String getTemplate() {
        return "/border-template.htm";
    }

}