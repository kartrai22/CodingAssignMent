package Exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UIExceptions  extends FrameworkExceptions{
	
    private static final Logger logger = LogManager.getLogger(UIExceptions.class);

	
	 public UIExceptions(String message) {
	        super(message);
	        logger.error("UI Exception: " + message);
	    }

	    public UIExceptions(String message, Throwable cause) {
	        super(message, cause);
	        logger.error("UI Exception: " + message, cause);

	    }

}
