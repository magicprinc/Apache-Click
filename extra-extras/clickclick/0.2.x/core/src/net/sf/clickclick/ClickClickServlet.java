/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.clickclick;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.click.*;
import org.apache.click.util.ErrorPage;
import org.apache.click.util.PageImports;

/**
 * Provides extra functionality not available in ClickServlet.
 * <p/>
 * Some of the features provided here are:
 * <ul>
 *   <li>Full Ajax support</li>
 * </ul>
 */
public class ClickClickServlet extends ClickServlet {

    ClickClickHelper helper;

    @Override
    public void init() throws ServletException {
        super.init();
        helper = new ClickClickHelper(logger);
    }

    // ------------------------------------------------------ Protected Methods

    /**
     * Creates and returns a new ActionEventDispatcher instance.
     *
     * @return the new ControlRegistry instance
     */
    @Override
    protected ActionEventDispatcher createActionEventDispatcher() {
        return new AjaxControlRegistry();
    }

    @Override
    protected void processPage(Page page) throws Exception {

        final Context context = page.getContext();

        AjaxControlRegistry controlRegistry = AjaxControlRegistry.getThreadLocalRegistry();

        PageImports pageImports = createPageImports(page);
        page.setPageImports(pageImports);

        // Support direct access of click-error.htm
        if (page instanceof ErrorPage) {
            ErrorPage errorPage = (ErrorPage) page;
            errorPage.setMode(configService.getApplicationMode());

            controlRegistry.errorOccurred(errorPage.getError());
        }

        boolean continueProcessing = performOnSecurityCheck(page, context);

        if (continueProcessing) {
            performOnInit(page, context);

            // Check if processing should continue after Ajax processing is
            // performed.
            if (!helper.performAjaxProcessing(page, context, controlRegistry)) {
                return;
            }

            continueProcessing = performOnProcess(page, context, controlRegistry);

            if (continueProcessing) {
                performOnPostOrGet(page, context, context.isPost());

                performOnRender(page, context);
            }
        } else {
            // If security check fails for an Ajax request, Click returns without
            // any rendering. It is up to the user to render a Partial response
            // in the onSecurityCheck event
            if (context.isAjaxRequest()) {
                return;
            }
        }

        controlRegistry.fireActionEvents(context, AjaxControlRegistry.POST_ON_RENDER_EVENT);

        performRender(page, context);
    }


    /**
     * Extends the application exception handling to cater for Ajax requests.
     * When an exception occurs during an Ajax request, the exception
     * stack trace is streamed back to the browser.
     *
     * @see org.apache.click.ClickServlet#handleException(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, boolean, java.lang.Throwable, java.lang.Class)
     *
     * @param request the servlet request with the associated error
     * @param response the servlet response
     * @param isPost boolean flag denoting the request method is "POST"
     * @param exception the error causing exception
     * @param pageClass the page class with the error
     */
    @Override
    protected void handleException(HttpServletRequest request,
        HttpServletResponse response, boolean isPost, Throwable exception,
        Class pageClass) {

        boolean continueProcessing = helper.handleException(request, response, isPost, exception, pageClass);

        if (continueProcessing) {
            super.handleException(request, response, isPost, exception, pageClass);
        }
    }
}
