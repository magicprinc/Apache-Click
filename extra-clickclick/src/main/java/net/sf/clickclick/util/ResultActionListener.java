package net.sf.clickclick.util;

import org.apache.click.ActionListener;
import org.apache.click.ActionResult;
import org.apache.click.Context;
import org.apache.click.Control;
import org.apache.click.Page;
import org.apache.click.util.ClickUtils;
import org.apache.commons.lang3.ClassUtils;

/**
 * Provides an action listener that returns an ActionResult instance instead of a
 * boolean. If a valid ActionResult is returned it will be rendered and further Page
 * processing will be stopped. If the listener returns null, Page processing
 * continues normally.
 * <p/>
 * <b>Please note:</b> if the Page forward or redirect property was set during
 * the action listener, page processing is also stopped whether a valid ActionResult
 * was returned nor not.
 */
public abstract class ResultActionListener implements ActionListener {

    @Override public final boolean onAction(Control source) {
        Page page = ClickUtils.getParentPage(source);
        if (page == null) {
            String controlClassName = ClassUtils.getShortClassName(source.getClass());
            throw new IllegalStateException("control '" + source.getName() + "' "
                + controlClassName + " parent page is not set");
        }

        ActionResult actionResult = onResultAction(source);
        if (actionResult != null) {
            actionResult.render(Context.getThreadLocalContext());
            return false;
        } else {
            // Check if forward or redirect was set in the action
            if (page.getForward() == null && page.getRedirect() == null) {
                return true;
            } else {
                // If redirect or forward was set skip further processing
                return false;
            }
        }
    }

    public abstract ActionResult onResultAction(Control source);
}