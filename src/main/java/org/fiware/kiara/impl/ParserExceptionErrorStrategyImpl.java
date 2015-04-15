package org.fiware.kiara.impl;

import org.antlr.v4.runtime.DefaultErrorStrategy;
import org.antlr.v4.runtime.InputMismatchException;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.IntervalSet;

public class ParserExceptionErrorStrategyImpl extends DefaultErrorStrategy {

    @Override
    public void recover(Parser recognizer, RecognitionException e) {
        throw e;
    }

    @Override
    public void reportInputMismatch(Parser recognizer, InputMismatchException e) throws RecognitionException {
        String msg = "";
        msg += "In file " + recognizer.getSourceName() + " at line " + recognizer.getContext().start.getLine() + ": ";
        msg += "Mismatched input " + getTokenErrorDisplay(e.getOffendingToken());
        msg += " expecting one of "+e.getExpectedTokens().toString(recognizer.getTokenNames()) + "\n";
        msg += "Line Number " + recognizer.getContext().start.getLine() + ", Column " + recognizer.getContext().start.getCharPositionInLine() + ";";
        RecognitionException ex = new RecognitionException(msg, recognizer, recognizer.getInputStream(), recognizer.getContext());
        ex.initCause(e);
        throw ex;
    }

    @Override
    public void reportMissingToken(Parser recognizer) {
        beginErrorCondition(recognizer);
        Token t = recognizer.getCurrentToken();
        IntervalSet expecting = getExpectedTokens(recognizer);
        String msg = "";
        msg += "In file " + recognizer.getSourceName() + " at line " + recognizer.getContext().start.getLine() + ": ";
        msg += "Missing "+expecting.toString(recognizer.getTokenNames()) + " at " + getTokenErrorDisplay(t) + ";";
        //msg += "Line Number " + recognizer.getContext().start.getLine() + ", Column " + recognizer.getContext().start.getCharPositionInLine() + ";";
        throw new RecognitionException(msg, recognizer, recognizer.getInputStream(), recognizer.getContext());
    }
    
}
