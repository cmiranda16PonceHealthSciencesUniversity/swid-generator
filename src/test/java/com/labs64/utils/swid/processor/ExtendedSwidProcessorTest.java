/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.labs64.utils.swid.processor;

import java.util.Date;

import org.iso.standards.iso._19770.__2._2009.schema.ObjectFactory;
import org.iso.standards.iso._19770.__2._2009.schema.SoftwareIdentificationTagComplexType;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.labs64.utils.swid.exception.SwidException;
import com.labs64.utils.swid.support.JAXBUtils;
import com.labs64.utils.swid.support.SwidUtils;

import static org.junit.Assert.assertNotNull;

/**
 */
public class ExtendedSwidProcessorTest {

    private static ObjectFactory objectFactory;

    private ExtendedSwidProcessor underTest;

    @BeforeClass
    public static void setup() {
        objectFactory = new ObjectFactory();
    }

    @Before
    public void setUp() {
        underTest = new ExtendedSwidProcessor();
    }

    @Test(expected = SwidException.class)
    public void testProcessorEmpty() {
        underTest.process();
    }

    @Test(expected = SwidException.class)
    public void testProcessorIncomplete() {
        underTest.setEntitlementRequiredIndicator(true)
                .setProductTitle("NetLicensing");
        underTest.process();
    }

    @Test
    public void testProcessorFull() {
        final String regid = SwidUtils.generateRegId("2010-04", "com.labs64");
        // set mandatory properties (see also com.labs64.utils.swid.processor.DefaultSwidProcessor)
        underTest.setEntitlementRequiredIndicator(true)
                .setProductTitle("NetLicensing")
                .setProductVersion("2.2.0", 2, 2, 0, 0)
                .setSoftwareCreator("Labs64", regid)
                .setSoftwareLicensor("Labs64", regid)
                .setSoftwareId("NLIC", regid)
                .setTagCreator("Labs64", regid);
        // set extended optional properties
        underTest.setKeywords("TryAndBuy", "Subscription", "Rental", "Floating", "etc.")
                .setAbstract("Innovative License Management Solution", "Application Licensing for Professionals")
                .setDataSource("https://netlicensing.labs64.com/")
                .setProductFamily("Online License Management")
                .setProductId("NLIC", "LMB")
                .setReleaseDate(new Date())
                .setReleaseId("2.2.0-Final")
                .setSerialNumber("SN-0123456789")
                .setSku("SKU-0123456789")
                .setSupportedLanguages("en", "de", "ru");

        SoftwareIdentificationTagComplexType swidElement = underTest.process();
        assertNotNull(swidElement);
        final String out = JAXBUtils.writeObjectToString(objectFactory.createSoftwareIdentificationTag(swidElement));
        System.out.println(out);
    }

}