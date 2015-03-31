/* KIARA - Middleware for efficient and QoS/Security-aware invocation of services and exchange of messages
 *
 * Copyright (C) 2015 German Research Center for Artificial Intelligence (DFKI)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fiware.kiara.impl;

import com.eprosima.idl.parser.grammar.KIARAIDLLexer;
import com.eprosima.idl.parser.grammar.KIARAIDLParser;
import com.eprosima.idl.parser.tree.Specification;
import com.eprosima.idl.util.Util;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.fiware.kiara.exceptions.IDLParseException;

/**
 *
 * @author Dmitri Rubinstein {@literal <dmitri.rubinstein@dfki.de>}
 */
public class IDLUtils {

    public static ParserContextImpl loadIDL(InputStream stream, String fileName) throws IOException {
        return loadIDL(new ANTLRInputStream(stream), fileName);
    }

    public static ParserContextImpl loadIDL(String idlContents, String fileName) throws IDLParseException {
        return loadIDL(new ANTLRInputStream(idlContents), fileName);
    }

    public static ParserContextImpl loadIDL(ANTLRInputStream input, String fileName) throws IDLParseException {
        ParserContextImpl ctx = new ParserContextImpl(Util.getIDLFileNameOnly(fileName), fileName, new ArrayList<String>());

        KIARAIDLLexer lexer = new KIARAIDLLexer(input);
        lexer.setContext(ctx);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        KIARAIDLParser parser = new KIARAIDLParser(tokens);
        // Select handling strategy for errors
        parser.setErrorHandler(new ParserExceptionErrorStrategyImpl());
        // Pass the finelame without the extension
        try {
            Specification specification = parser.specification(ctx, null, null).spec;
        } catch (RecognitionException ex) {
            throw new IDLParseException(ex);
        }

        return ctx;
    }

}
