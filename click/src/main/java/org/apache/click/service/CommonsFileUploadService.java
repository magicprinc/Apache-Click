package org.apache.click.service;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.click.util.ClickUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.security.AccessControlException;
import java.util.List;

import static org.apache.click.util.ClickUtils.trim;

/**
 * Provides an Apache Commons FileUploadService class.
 * <p/>
 * To prevent users from uploading exceedingly large files you can configure
 * CommonsFileUploadService through the properties {@link #sizeMax} and
 * {@link #fileSizeMax}.
 * <p/>
 * For example:
 * <pre class="prettyprint">
 * &lt;file-upload-service&gt;
 *	 &lt;!-- Set the total request maximum size to 10mb (10 x 1024 x 1024 = 10485760). --&gt;
 *	 &lt;property name="sizeMax" value="10485760"/&gt;
 *
 *	 &lt;!-- Set the maximum individual file size to 2mb (2 x 1024 x 1024 = 2097152). --&gt;
 *	 &lt;property name="fileSizeMax" value="2097152"/&gt;
 * &lt;/file-upload-service&gt; </pre>
 *
 * Note that this is a global configuration and applies to the all file uploads
 * of the application.
 * <p/>
 * If you would like to specify a custom FileUploadService implementation
 * use the <tt>classname</tt> attribute:
 * <pre class="prettyprint">
 * &lt;file-upload-service classname="com.mycorp.util.CustomFileUploadService"&gt;
 *	 &lt;property name="customProperty" value="customValue"/&gt;
 * &lt;/file-upload-service&gt; </pre>
 */
@Getter @Setter  @ToString @Slf4j
public class CommonsFileUploadService implements FileUploadService {
	/** The total request maximum size in bytes. By default there is no limit. */
	protected long sizeMax;

	/** The maximum individual file size in bytes. By default there is no limit. */
	protected long fileSizeMax;

	/**
	 * @see FileUploadService#onInit(ServletContext)
	 * @param servletContext the application servlet context
	 * @throws Exception if an error occurs initializing the FileUploadService
	 */
	@Override
	public void onInit (ServletContext servletContext) throws Exception {
		//ConfigService configService = ClickUtils.getConfigService(servletContext);
		String s = trim(servletContext.getInitParameter("file-upload-service.sizeMax"));
		if (!s.isEmpty()){
			sizeMax = Long.parseLong(s);
		}
		s = trim(servletContext.getInitParameter("file-upload-service.fileSizeMax"));
		if (!s.isEmpty()){
			fileSizeMax = Long.parseLong(s);
		}
		boolean logWarning = false;
		boolean restrictedEnvironment = false;

		// Uploaded files are saved to java.io.tmpdir. Here we check if this directory exists, if it doesn't, log a warning
		String tmpdir = System.getProperty("java.io.tmpdir");
		try {
			if (tmpdir == null){
				logWarning = true;

			} else {
				File tmpfile = new File(tmpdir);
				if (!tmpfile.exists()){
					logWarning = true;
				}
			}

			if (!ClickUtils.isResourcesDeployable(servletContext)){
				restrictedEnvironment = true;
			}

		} catch (AccessControlException exception) {
			if (ClickUtils.isResourcesDeployable(servletContext)){
				throw exception;// If resources are deployable, throw exception
			} else {
				restrictedEnvironment = true;
			}
		}

		if (logWarning){
			log.warn(
				"The java.io.tmpdir dir '{}', does not exist. This can cause file uploading to fail as uploaded files are saved to the directory specified by the 'java.io.tmpdir' property.",
				tmpdir
			);
		}
		if (restrictedEnvironment){
			// Probably deploying on Google App Engine which throws Security Exception if accessing temp folder
			log.warn("If you are deploying to Google App Engine, please note that Click's default 'org.apache.click.service.CommonsFileUploadService'"
				+ " does not work with Google App Engine. Instead use 'org.apache.click.extras.gae.MemoryFileUploadService'.");
		}
	}

	/** @see FileUploadService#onDestroy() */
	@Override public void onDestroy (){}

	/**
	 * @see FileUploadService#parseRequest(HttpServletRequest)
	 *
	 * @param request the servlet request
	 * @return the list of FileItem instances parsed from the request
	 * @throws FileUploadException if request cannot be parsed
	 */
	@Override
	public List<FileItem> parseRequest (@NonNull HttpServletRequest request) throws FileUploadException {
		FileItemFactory fileItemFactory = createFileItemFactory(request);

		FileUploadBase fileUpload = new ServletFileUpload();
		fileUpload.setFileItemFactory(fileItemFactory);

		if (fileSizeMax > 0) {
			fileUpload.setFileSizeMax(fileSizeMax);
		}
		if (sizeMax > 0) {
			fileUpload.setSizeMax(sizeMax);
		}
		val requestContext = new ServletRequestContext(request);

		return fileUpload.parseRequest(requestContext);
	}

	/**
	 * Create and return a new Commons Upload FileItemFactory instance.
	 *
	 * @param request the servlet request
	 * @return a new Commons FileUpload FileItemFactory instance
	 */
	public FileItemFactory createFileItemFactory (HttpServletRequest request) {
		return new DiskFileItemFactory();
	}
}