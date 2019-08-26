//=============================================================================
// Copyright 2006-2013 Daniel W. Dyer
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//=============================================================================
package automation.ui.testSample;

import org.testng.annotations.Test;

/**
 * These tests are skipped because they depend on failed tests in
 * another class.
 * @author Daniel Dyer
 */
@Test(groups = "should-skip")
public class SkippedTests
{
    @Test(dependsOnGroups = "should-fail")
    public void skippedDueToDependentGroup()
    {
        assert false : "This method is supposed to be skipped.";
    }


    @Test(dependsOnMethods = "skippedDueToDependentGroup")
    public void skippedDueToDependentMethod()
    {
        assert false : "This method is supposed to be skipped.";
    }
}
