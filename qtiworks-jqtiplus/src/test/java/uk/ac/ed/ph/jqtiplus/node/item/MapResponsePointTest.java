/* Copyright (c) 2012, University of Edinburgh.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice, this
 *   list of conditions and the following disclaimer in the documentation and/or
 *   other materials provided with the distribution.
 *
 * * Neither the name of the University of Edinburgh nor the names of its
 *   contributors may be used to endorse or promote products derived from this
 *   software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *
 * This software is derived from (and contains code from) QTItools and MathAssessEngine.
 * QTItools is (c) 2008, University of Southampton.
 * MathAssessEngine is (c) 2010, University of Edinburgh.
 */
package uk.ac.ed.ph.jqtiplus.node.item;

import static org.junit.Assert.assertEquals;

import uk.ac.ed.ph.jqtiplus.running.ItemSessionController;
import uk.ac.ed.ph.jqtiplus.state.ItemSessionState;
import uk.ac.ed.ph.jqtiplus.testutils.UnitTestHelper;
import uk.ac.ed.ph.jqtiplus.value.FloatValue;
import uk.ac.ed.ph.jqtiplus.value.MultipleValue;
import uk.ac.ed.ph.jqtiplus.value.PointValue;
import uk.ac.ed.ph.jqtiplus.value.SingleValue;
import uk.ac.ed.ph.jqtiplus.value.Value;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class MapResponsePointTest {

    /**
     * Creates test data for this test.
     * 
     * @return test data for this test
     */
    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { "MapResponsePoint-Single.xml", new String[] { "1 1" }, 1.0 }, 
                { "MapResponsePoint-Single.xml", new String[] { "1 3" }, 2.0 },
                { "MapResponsePoint-Single.xml", new String[] { "3 1" }, 3.0 },
                { "MapResponsePoint-Single.xml", new String[] { "3 3" }, 4.0 },
                { "MapResponsePoint-Single.xml", new String[] { "10 10" }, -1.0 },
                { "MapResponsePoint-Multiple.xml", new String[] { "1 1" }, 1.0 },
                { "MapResponsePoint-Multiple.xml", new String[] { "1 1", "1 1" }, 1.0 },
                { "MapResponsePoint-Multiple.xml", new String[] { "1 1", "1 3" }, 3.0 },
                { "MapResponsePoint-Multiple.xml", new String[] { "1 1", "1 3", "3 1" }, 6.0 },
                { "MapResponsePoint-Multiple.xml", new String[] { "1 1", "1 3", "3 1", "3 3" }, 10.0 },
                { "MapResponsePoint-Multiple.xml", new String[] { "1 1", "1 3", "3 1", "3 3", "3 3", "3 1" }, 10.0 },
        });
    }

    private final String fileName;
    private Value response;
    private final double expectedOutcome;

    public MapResponsePointTest(String fileName, String[] responses, double expectedOutcome) {
        this.fileName = fileName;
        this.expectedOutcome = expectedOutcome;

        if (responses.length == 1) {
            response = new PointValue(responses[0]);
        }
        else {
            response = new MultipleValue();
            for (final String s : responses) {
                ((MultipleValue) response).add(new PointValue(s));
            }
        }
    }

    @Test
    public void test() throws Exception {
        final ItemSessionController itemSessionController = UnitTestHelper.loadUnitTestAssessmentItemForControl(fileName, MapResponseTest.class);
        itemSessionController.initialize();
        
        ItemSessionState itemSessionState = itemSessionController.getItemSessionState();
        AssessmentItem item = itemSessionController.getItem();

        if (item.getResponseDeclaration("RESPONSE").getCardinality().isMultiple() && response.getCardinality().isSingle()) {
            response = new MultipleValue((SingleValue) response);
        }

        itemSessionState.setResponseValue("RESPONSE", response);
        itemSessionController.processResponses();

        assertEquals(expectedOutcome, ((FloatValue) itemSessionState.getOutcomeValue("OUTCOME")).doubleValue(), 0.1);
    }
}