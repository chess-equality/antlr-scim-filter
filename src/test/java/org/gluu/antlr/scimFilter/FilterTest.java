/*
 * antlr-scim-filter is available under the MIT License (2008). See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (c) 2016, Gluu
 */
package org.gluu.antlr.scimFilter;

import org.antlr.v4.runtime.*;
import org.gluu.antlr.scimFilter.exception.ScimFilterErrorHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertNull;

/**
 * @author Val Pecaoco
 */
@RunWith(Parameterized.class)
public class FilterTest {

    @Parameterized.Parameters
    public static Collection<Object[]> testData() {

        return Arrays.asList(new Object[][] {
            // Valid
            { true, "userType eq \"employee\"" },
            { true, "userType1234 ne \"Employee\" and email.type eq \"work\"" },
            { true, "userType eq \"employee\" and (email.type eq \"Work\" or email-type eq \"personal\")" },
            { true, "userType pr and ((type eq \"work\" or type eq \"personal\") and user_Type eq \"employee\")" },
            { true, "userType pr"},
            { true, "userType eq \"employee\" and userType pr"},
            { true, "userType pr and (userType eq \"employee\")"},
            { true, "userType pr and not (userType eq \"employee\")"},
            { true, "(userType pr) and ((type eq \"work\" or type eq \"personal\") and userType eq \"employee\")" },
            { true, "userType pr and (type eq \"work\" or type eq \"personal\") and userType eq \"employee\"" }
            // Invalid
            /*
            { true, "pr userType" },
            { true, "pr userType pr" },
            { true, "not userType" },
            { true, "not userType not" }
            */
        });
    }

    private final boolean isValid;
    private final String testFilter;

    public FilterTest(boolean isValid, String testFilter) {
        this.isValid = isValid;
        this.testFilter = testFilter;
    }

    @Test
    public void testFilter() throws Exception {

        ANTLRInputStream input = new ANTLRInputStream(this.testFilter);
        ScimFilterLexer lexer = new ScimFilterLexer(input);
        TokenStream tokens = new CommonTokenStream(lexer);

        ScimFilterParser parser = new ScimFilterParser(tokens);

        parser.removeErrorListeners();
        parser.setErrorHandler(new ScimFilterErrorHandler());

        assert(this.isValid);

        ParserRuleContext ruleContext = parser.scimFilter();
        assertNull(ruleContext.exception);
    }
}