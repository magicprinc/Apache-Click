package org.apache.click.examples.page.general;

import org.apache.click.ActionResult;
import org.apache.click.Context;
import org.apache.click.examples.page.BorderPage;
import org.apache.click.util.ClickUtils;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serial;


/**
 * This demo shows to invoke a PAGE_ACTION using an HTML <img> tag. The pageAction
 * will render the image data to the browser.
 */
public class PageActionImage extends BorderPage {
	@Serial private static final long serialVersionUID = 1L;

	/**
	 * This page method is invoked from the <img> element and returns an ActionResult
	 * instance containing the static image data.
	 */
	public ActionResult getStaticImageData() {
		// Load the static image 'click-icon-blue-32.png'
		byte[] imageData = loadImageData("click-icon-blue-32.png");

		// Lookup the contentType for a PNG image
		String contentType = ClickUtils.getMimeType("png");

		// Return an ActionResult containing the image data
		return new ActionResult(imageData, contentType);
	}

	/**
	 * This page method is invoked from the <img> element and returns an ActionResult
	 * instance containing the image data specified by the imageName parameter.
	 */
	public ActionResult getDynamicImageData () {
		Context context = getContext();

		// Retrieve the image name parameter from the request
		String imageName = context.getRequestParameter("imageName");

		// Load the static image 'click-icon-blue-32.png'
		byte[] imageData = loadImageData(imageName);

		// Lookup the contentType for a PNG image
		String contentType = ClickUtils.getMimeType("png");

		// Return an ActionResult containing the image data
		return new ActionResult(imageData, contentType);
	}

	private byte[] loadImageData (String imageName) {
		InputStream is = null;
		try {
			ServletContext servletContext = getContext().getServletContext();
			is = servletContext.getResourceAsStream("/assets/images/"+ imageName);
			if (is == null){
				is = getClass().getResourceAsStream("/static/assets/images/"+ imageName);
			}
			//byte[] imageData = IOUtils.toByteArray(is);
			return is.readAllBytes();
		} catch (IOException ioe){
			throw new RuntimeException(ioe);
		} finally {
			ClickUtils.close(is);
		}
	}
}