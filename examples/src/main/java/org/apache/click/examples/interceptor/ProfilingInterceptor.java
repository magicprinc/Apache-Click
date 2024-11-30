package org.apache.click.examples.interceptor;

import org.apache.click.Context;
import org.apache.click.Page;
import org.apache.click.PageInterceptor;
import org.apache.click.service.LogService;
import org.apache.click.util.ClickUtils;

import javax.servlet.ServletContext;

/**
 * Provides a basic page profiling PageInterceptor example.
 * <p/>
 * This interceptor must be configured with "request" scope as it not thread
 * safe.
 */
public class ProfilingInterceptor implements PageInterceptor {
	private String pageName;
	private long startTime;
	private long checkpointTime;
	private long createDuration;
	private long processDuration;
	private long renderDuration;
	private long totalDuration;

	@Override public void onInit (ServletContext servletContext){}

	/**
	 * @see PageInterceptor#preCreate(Class, Context)
	 */
	@Override
	public boolean preCreate(Class<? extends Page> pageClass, Context context) {
		checkpointTime = System.currentTimeMillis();
		startTime = checkpointTime;
		pageName = pageClass.getSimpleName();
		return true;
	}

	/**
	 * @see PageInterceptor#postCreate(Page)
	 */
	@Override public boolean postCreate(Page page) {
		createDuration = System.currentTimeMillis() - checkpointTime;
		checkpointTime = System.currentTimeMillis();
		return true;
	}

	/**
	 * @see PageInterceptor#preResponse(Page)
	 */
	@Override public boolean preResponse(Page page) {
		processDuration = System.currentTimeMillis() - checkpointTime;
		checkpointTime = System.currentTimeMillis();
		return true;
	}

	/**
	 * @see PageInterceptor#postDestroy(Page)
	 */
	@Override
	public void postDestroy(Page page) {
		renderDuration = System.currentTimeMillis() - checkpointTime;
		totalDuration = System.currentTimeMillis() - startTime;

		LogService logService = ClickUtils.getLogService();
		if (logService.isInfoEnabled()){
			logService.info(this);
		}
	}

	/**
	 * @see Object#toString()
	 */
	@Override public String toString() {
		return getClass().getSimpleName()
			+ "[page=" + pageName
			+ ", createDuration=" + createDuration
			+ ", processDuration=" + processDuration
			+ ", renderDuration=" + renderDuration
			+ ", totalDuration=" + totalDuration
			+ "]";
	}
}