package automation.listener;

/**
 * Report Verbose Level
 *
 * @author Vincent-Li
 */
public enum LogLevel {
    // @formatter:off
    FATAL(1),
    ERROR(2),
    WARN(3),
    DEBUG(4),
    INFO(0),
    UNKNOWN(0);
    // @formatter:on

    int m_val;

    LogLevel(int val) {
        m_val = val;
    }

    public int val() {
        return m_val;
    }

    public static LogLevel get(int result) {
        for (LogLevel res : values()) {
            if (res.val() == result) {
                return res;
            }
        }
        return UNKNOWN;
    }
}
