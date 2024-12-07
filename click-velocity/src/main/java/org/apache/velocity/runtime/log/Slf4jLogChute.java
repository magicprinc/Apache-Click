package org.apache.velocity.runtime.log;

import org.apache.velocity.runtime.RuntimeServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of a simple log4j system that will either latch onto
 * an existing category, or just do a simple rolling file log.
 *
 * @author <a href="mailto:geirm@apache.org>Geir Magnusson Jr.</a>
 * @author <a href="mailto:dlr@finemaltcoding.com>Daniel L. Rall</a>
 * @author <a href="mailto:nbubna@apache.org>Nathan Bubna</a>
 * @version $Id: Log4JLogChute.java 730039 2008-12-30 03:53:19Z byron $
 * @since Velocity 1.5
 @see org.slf4j.Logger
 @see LogChute
 */
@Deprecated
public class Slf4jLogChute implements LogChute {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void init (RuntimeServices rs) throws Exception {
		//rsvc = rs; logger =
	}

	@Override
	public void log (int level, String message) {
		switch (level) {
		case LogChute.ERROR_ID: logger.error(message); break;
		case LogChute.WARN_ID: logger.warn(message); break;
		case LogChute.INFO_ID: logger.info(message); break;
		case LogChute.TRACE_ID: logger.trace(message); break;
		case LogChute.DEBUG_ID:
		default:
			logger.debug(message);
			break;
		}
	}

	@Override
	public void log (int level, String message, Throwable t) {
		switch (level){
		case LogChute.ERROR_ID: logger.error(message, t); break;
		case LogChute.WARN_ID: logger.warn(message, t); break;
		case LogChute.INFO_ID: logger.info(message, t); break;
		case LogChute.TRACE_ID: logger.trace(message, t); break;
		case LogChute.DEBUG_ID:
		default:
			logger.debug(message, t);
			break;
		}
	}

	@Override
	public boolean isLevelEnabled (int level) {
		return switch (level){
			case LogChute.ERROR_ID -> logger.isErrorEnabled();
			case LogChute.WARN_ID -> logger.isWarnEnabled();
			case LogChute.INFO_ID -> logger.isInfoEnabled();
			case LogChute.DEBUG_ID -> logger.isDebugEnabled();
			case LogChute.TRACE_ID -> logger.isTraceEnabled();
			default -> true;
		};
	}
}