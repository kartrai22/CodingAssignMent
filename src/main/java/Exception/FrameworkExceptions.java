package Exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FrameworkExceptions extends RuntimeException {

	private static final Logger logger = LogManager.getLogger(FrameworkExceptions.class);

	public FrameworkExceptions(String message) {
		super(message);
		logger.error(message);

	}

	public FrameworkExceptions(String message, Throwable cause) {
		super(message, cause);
		logger.error(message);

	}
}
