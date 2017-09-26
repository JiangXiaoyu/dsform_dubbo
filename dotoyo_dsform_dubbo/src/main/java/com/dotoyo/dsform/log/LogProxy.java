
package com.dotoyo.dsform.log;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dotoyo.ims.dsform.allin.IFrameLog;

public class LogProxy implements IFrameLog {
	private Log log = null;

	public LogProxy(Log log) {

		this.log = log;
	}

	public void debug(Object obj) {
		if (log != null) {
			try {
				log.debug(obj);
			} catch (Throwable e) {

			}
		}
	}

	public void debug(Object obj, Throwable e) {
		if (log != null) {
			try {
				log.debug(obj, e);
			} catch (Throwable e1) {

			}
		}
	}

	public void error(Object obj) {
		if (log != null) {
			try {
				log.error(obj);
			} catch (Throwable e1) {

			}
		}

	}

	public void error(Object obj, Throwable e) {
		if (log != null) {
			try {
				log.error(obj, e);
			} catch (Throwable e1) {

			}
		}

	}

	public void fatal(Object obj) {
		if (log != null) {
			try {
				log.fatal(obj);
			} catch (Throwable e1) {

			}
		}

	}

	public void fatal(Object obj, Throwable e) {
		if (log != null) {
			try {
				log.fatal(obj, e);
			} catch (Throwable e1) {

			}
		}

	}

	public void info(Object obj) {
		if (log != null) {
			try {
				log.info(obj);
			} catch (Throwable e1) {

			}
		}

	}

	public void info(Object obj, Throwable e) {
		if (log != null) {
			try {
				log.info(obj, e);
			} catch (Throwable e1) {

			}
		}

	}

	public boolean isDebugEnabled() {
		if (log != null) {
			try {
				return log.isDebugEnabled();
			} catch (Throwable e1) {

			}
		}
		return false;
	}

	public boolean isErrorEnabled() {
		if (log != null) {
			try {
				return log.isErrorEnabled();
			} catch (Throwable e1) {

			}
		}
		return false;
	}

	public boolean isFatalEnabled() {
		if (log != null) {
			try {
				return log.isFatalEnabled();
			} catch (Throwable e1) {

			}
		}
		return false;
	}

	public boolean isInfoEnabled() {
		if (log != null) {
			try {
				return log.isInfoEnabled();
			} catch (Throwable e1) {

			}
		}
		return false;
	}

	public boolean isTraceEnabled() {
		if (log != null) {
			try {
				return log.isTraceEnabled();
			} catch (Throwable e1) {

			}
		}
		return false;
	}

	public boolean isWarnEnabled() {
		if (log != null) {
			try {
				return log.isWarnEnabled();
			} catch (Throwable e1) {

			}
		}
		return false;
	}

	public void trace(Object obj) {
		if (log != null) {
			try {
				log.trace(obj);
			} catch (Throwable e1) {

			}
		}

	}

	public void trace(Object obj, Throwable e) {
		if (log != null) {
			try {
				log.trace(obj, e);
			} catch (Throwable e1) {

			}
		}

	}

	public void warn(Object obj) {
		if (log != null) {
			try {
				log.warn(obj);
			} catch (Throwable e1) {

			}
		}

	}

	public void warn(Object obj, Throwable e) {
		if (log != null) {
			try {
				log.warn(obj, e);
			} catch (Throwable e1) {

			}
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LogProxy log = new LogProxy(LogFactory.getLog(LogProxy.class));
		log.info("aaa");
	}
}
