package net.sf.click.ajax4click.examples.page;

import org.apache.click.Context;
import org.apache.click.Page;
import org.apache.click.extras.control.Menu;
import org.apache.click.extras.control.MenuFactory;

public class BorderPage extends Page {

    private static final long serialVersionUID = 1L;

    private Menu rootMenu;

    public BorderPage() {
        MenuFactory menuFactory = new MenuFactory();
        rootMenu = menuFactory.getRootMenu();
        addControl(rootMenu);

        String className = getClass().getName();
        final String srcPath = className.replace('.', '/') + ".java";
        addModel("srcPath", srcPath);
        addSourceCodeMenus(rootMenu, srcPath);
    }

    /**
     * @see #getTemplate()
     */
    @Override
    public String getTemplate() {
        return "border-template.htm";
    }


    // Private Methods --------------------------------------------------------

    private void addSourceCodeMenus(final Menu rootMenu, final String srcPath) {

    	Context context = getContext();

        // Add menu for Java Source code
        Menu pageJavaMenu = new Menu("pageJava");
        pageJavaMenu.setLabel( "Page Java");
        pageJavaMenu.setPath("source-viewer.htm?filename=WEB-INF/classes/" + srcPath);
        pageJavaMenu.setAccessController(rootMenu.getAccessController());

        if (!rootMenu.contains(pageJavaMenu)) {
            pageJavaMenu.setImageSrc("/assets/images/lightbulb1.png");
            pageJavaMenu.setTitle("Page Java source");
            pageJavaMenu.setTarget("_blank");
            rootMenu.add(pageJavaMenu);
        }

        // Add menu for Html Source code
        Menu pageHtmlMenu = new Menu("pageHtml");
        pageHtmlMenu.setLabel(" Page HTML");
        pageHtmlMenu.setPath("source-viewer.htm?filename=" + context.getPagePath(getClass()));
        pageHtmlMenu.setAccessController(rootMenu.getAccessController());

        if (!rootMenu.contains(pageHtmlMenu)) {
            pageHtmlMenu.setTitle("Page Content source");
            pageHtmlMenu.setTarget("_blank");
            pageHtmlMenu.setImageSrc("/assets/images/lightbulb2.png");
            rootMenu.add(pageHtmlMenu);
        }
    }
}
