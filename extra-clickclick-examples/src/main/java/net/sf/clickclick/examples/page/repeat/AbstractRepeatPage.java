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
package net.sf.clickclick.examples.page.repeat;

import net.sf.clickclick.control.repeater.Repeater;
import net.sf.clickclick.examples.page.BorderPage;
import org.apache.click.Control;
import org.apache.click.control.AbstractLink;
import org.apache.click.control.Container;
import org.apache.click.control.Field;
import org.apache.click.util.ContainerUtils;

public class AbstractRepeatPage extends BorderPage {

    protected Repeater repeater;

    // ------------------------------------------------------ Protected Methods

    protected void toggleLinks(int modelSize) {
        if (modelSize == 0) {
            return;
        }

        if (modelSize == 1) {
            Container row = (Container) repeater.getControls().get(0);
            Control control = ContainerUtils.findControlByName(row, "down");
            toggleControl(control, true);
            control = ContainerUtils.findControlByName(row, "up");
            toggleControl(control, true);
        } else if (modelSize == 2) {
            toggleLinksTopAndBottom();
        } else if (modelSize > 2) {
            toggleLinksTopAndBottom();
            toggleLinksSecondFromTopAndBottom();
        }
    }

    // -------------------------------------------------------- Private Methods

    private void toggleControl(Control control, boolean value) {
        if (control instanceof Field) {
            ((Field) control).setDisabled(value);
        } else if (control instanceof AbstractLink) {
            ((AbstractLink) control).setDisabled(value);
        }
    }

    private void toggleLinksTopAndBottom() {
        Container row = (Container) repeater.getControls().get(0);
        Control control = ContainerUtils.findControlByName(row, "down");
        toggleControl(control, false);
        control = ContainerUtils.findControlByName(row, "up");
        toggleControl(control, true);

        row = (Container) repeater.getControls().get(repeater.getControls().size() - 1);
        control = ContainerUtils.findControlByName(row, "down");
        toggleControl(control, true);
        control = ContainerUtils.findControlByName(row, "up");
        toggleControl(control, false);
    }

    private void toggleLinksSecondFromTopAndBottom() {
        Container row = (Container) repeater.getControls().get(1);
        Control control = ContainerUtils.findControlByName(row, "down");
        toggleControl(control, false);
        control = ContainerUtils.findControlByName(row, "up");
        toggleControl(control, false);

        row = (Container) repeater.getControls().get(repeater.getControls().size() - 2);
        control = ContainerUtils.findControlByName(row, "down");
        toggleControl(control, false);
        control = ContainerUtils.findControlByName(row, "up");
        toggleControl(control, false);
    }
}