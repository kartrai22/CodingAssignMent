package Exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Helper.APIHelper;

public class APIExceptions extends FrameworkExceptions {
	private static final Logger logger = LoggerFactory.getLogger(APIHelper.class);

    public APIExceptions(String message) {
        super(message);
        logger.error("API Exception: " + message);
    }

    public APIExceptions(String message, Throwable cause) {
        super(message, cause);
        logger.error("API Exception: " + message, cause);
    }
}
