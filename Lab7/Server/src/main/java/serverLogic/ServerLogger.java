package serverLogic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ServerLogger {
    private static final Logger LOGGER = LogManager.getLogger(ServerLogger.class);

    private ServerLogger() {
    }

    public static void logInfoMessage(String format, Object... arg) {
        LOGGER.info(format, arg);
    }

    public static void logDebugMessage(String format, Object... arg) {
        LOGGER.debug(format, arg);
    }

    public static void logErrorMessage(String format, Object... arg) {
        LOGGER.error(format, arg);
    }
}