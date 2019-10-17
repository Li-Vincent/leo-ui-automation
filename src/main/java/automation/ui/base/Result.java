package automation.ui.base;

public enum Result {
    // @formatter:off
    PASS(1),
    FAIL(2),
    SKIP(3),
    NOTRUN(0),
    UNKNOWN(0);
    // @formatter:on

    int m_val;

    Result(int val) {
        m_val = val;
    }

    public int val() {
        return m_val;
    }

    public boolean passed() {
        return m_val == 1;
    }

    public boolean failed() {
        return m_val != 1;
    }

    public boolean skipped() {
        return m_val == 3;
    }

    public static Result get(int result) {
        for (Result res : values()) {
            if (res.val() == result) {
                return res;
            }
        }
        return UNKNOWN;
    }
}
