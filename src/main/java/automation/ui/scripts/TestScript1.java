package automation.ui.scripts;

import org.testng.annotations.Test;

import automation.ui.base.BaseTest;

public class TestScript1 extends BaseTest {

    @Test
    public void test() {
        this.prepareTest();
        driver.get("https://www.baidu.com");
    }
}
