package automation.config;

public enum Env {
    // @formatter:off
    // Env("url" or "ip")
    TEST("test"),
    PRE("pre-c"),
    SIT("pre-u"),
    UAT("tun-c");
    // @formatter:on

    private String env;

    private Env(String env) {
        this.env = env;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }
}
