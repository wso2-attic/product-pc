/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.wso2.pc.integration.tests;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.wso2.carbon.automation.engine.context.TestUserMode;
import org.wso2.pc.integration.test.utils.base.PublisherTestBaseTest;

public class PublisherAddProcessTestCase extends PublisherTestBaseTest{

    String cookieHeader;
    private TestUserMode userMode;

    @BeforeTest(alwaysRun = true)
    public void init() throws Exception {
        super.init(userMode);

    }
    @Test(groups = {"org.wso2.pc"}, description = "Test case for adding process")
    public void addProcess(){

    }

    @DataProvider
    private static Object[][] userModeProvider(){
        return new TestUserMode[][]{
                new TestUserMode[]{TestUserMode.SUPER_TENANT_ADMIN}
        };
    }
}
