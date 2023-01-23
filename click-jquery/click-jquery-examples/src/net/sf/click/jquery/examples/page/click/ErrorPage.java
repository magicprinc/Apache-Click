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
package net.sf.click.jquery.examples.page.click;

import net.sf.click.jquery.examples.control.DesktopMenu;
import org.apache.click.extras.control.Menu;
import org.apache.click.extras.control.MenuFactory;

public class ErrorPage extends org.apache.click.util.ErrorPage {

	  private static final long serialVersionUID = 1L;

    public String title = "Error Page";

    private Menu rootMenu = new MenuFactory().getRootMenu(DesktopMenu.class);

    public ErrorPage() {
        addControl(rootMenu);
    }

    @Override
    public void onDestroy() {
        Throwable error = getError();

        // TODO: Log error to Log4J or Commons Logger
        getContext().getServletContext().log(error.toString(), error);
    }

}
