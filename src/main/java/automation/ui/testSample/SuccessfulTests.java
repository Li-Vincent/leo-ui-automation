// =============================================================================
// Copyright 2006-2013 Daniel W. Dyer
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// =============================================================================
package automation.ui.testSample;

import automation.listener.ReportLogger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Test;

/**
 * Some successful tests to be included in the sample output.
 *
 * @author Daniel Dyer
 */
public class SuccessfulTests {
    private ReportLogger LOGGER = new ReportLogger(this.getClass());

    @Test
    public void test1() {
        assert true;
    }

    @Test(description = "This is a test description")
    public void testWithDescription1() {
        assert true;
    }

    @Test
    public void testWithOutput1() {
        LOGGER.info("Here is some output from a successful test.");
        assert true;
    }

    @Test
    public void testWithMultiLineOutput1() {
        LOGGER.log("This is level log.");
        LOGGER.info("This is level info.");
        LOGGER.debug("This is level debug.");
        LOGGER.warn("This is level warn.");
        LOGGER.error("This is level error.");
        LOGGER.fatal("This is level fatal.");
        // MongoCollection<Document> collection = MongoConn.getConnect(Env.TEST).getCollection("testColl");
        // collection.find().forEach(printBlock);
        assert true;
    }
    //
    // Block<Document> printBlock = new Block<Document>() {
    // public void apply(final Document document) {
    // System.out.println(document.toJson());
    // }
    // };

    @AfterMethod
    public void afterMethod() {
        // This is here to detect any problems processing config
        // methods.
    }

    @AfterClass
    public void afterClass() {
        // This is here to detect any problems processing config
        // methods.
    }

    @AfterSuite
    public void afterSuite() {
        // This is here to detect any problems processing config
        // methods.
    }
}
