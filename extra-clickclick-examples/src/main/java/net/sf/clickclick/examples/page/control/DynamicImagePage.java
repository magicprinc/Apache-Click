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
package net.sf.clickclick.examples.page.control;

import net.sf.clickclick.control.DynamicImage;
import net.sf.clickclick.examples.page.BorderPage;
import org.apache.click.ActionResult;
import org.apache.click.util.ClickUtils;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class DynamicImagePage extends BorderPage {

    public DynamicImagePage() {
        DynamicImage dynamicImage = new DynamicImage("dynamicImage", "renderChart");
        addControl(dynamicImage);
    }

    public ActionResult renderChart() {
        ActionResult actionResult = new ActionResult();
        String contentType = ClickUtils.getMimeType(".png");
        byte[] imageData = generateChart();
        actionResult.setBytes(imageData, contentType);
        return actionResult;
    }

    private byte[] generateChart() {
        // This method returns a byte array that can be dynamically retrieved
        // from a database or generated on the fly. For simplicity sake a static
        // image is looked up

        try {
            InputStream is = getContext().getServletContext().getResourceAsStream("/assets/images/chart.png");
            return IOUtils.toByteArray(is);

        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
}