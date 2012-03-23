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
package org.qtitools.qti.node.test.flow;

import static org.junit.Assert.assertEquals;

import uk.ac.ed.ph.jqtiplus.node.test.AssessmentItemRef;
import uk.ac.ed.ph.jqtiplus.value.Value;

import java.util.Map;


class Command {

    private final Action action;

    private final Object param;

    private final Object result;

    public Command(Action action) {
        this(action, null, null);
    }

    public Command(Action action, Object result) {
        this(action, null, result);
    }

    public Command(Action action, Object param, Object result) {
        this.action = action;
        this.param = param;
        this.result = result;
    }

    @SuppressWarnings("unchecked")
    public void exec(ItemFlow flow) {
        switch (action) {
            case NONE: {
                // Do nothing.
                break;
            }

            case NEXT: {
                final AssessmentItemRef itemRef = flow.getNextItemRef(param != null ? (Boolean) param : false);
                assertEquals(result, itemRef != null ? itemRef.getIdentifier() : null);
                break;
            }

            case SUBMIT: {
                final AssessmentItemRef itemRef = flow.getCurrentItemRef();
                itemRef.setOutcomes((Map<String, Value>) param);
                break;
            }

            case SKIP: {
                final AssessmentItemRef itemRef = flow.getCurrentItemRef();
                itemRef.skip();
                break;
            }

            default:
                throw new AssertionError("Unsupported action: " + action);
        }
    }
}