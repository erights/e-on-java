// $ANTLR : "tokdef.g" -> "ANTLRTokdefParser.java"$
package antlr;

import antlr.collections.impl.BitSet;

/**
 * Simple lexer/parser for reading token definition files in support of the
 * import/export vocab option for grammars.
 */
public class ANTLRTokdefParser extends antlr.LLkParser
  implements ANTLRTokdefParserTokenTypes {

    // This chunk of error reporting code provided by Brian Smith

    private antlr.Tool antlrTool;

    /**
     * In order to make it so existing subclasses don't break, we won't require
     * that the antlr.Tool instance be passed as a constructor element.
     * Instead, the antlr.Tool instance should register itself via {@link
     * #setTool(antlr.Tool)}
     *
     * @throws IllegalStateException if a tool has already been registered
     * @since 2.7.2
     */
    public void setTool(antlr.Tool tool) {
        if (antlrTool == null) {
            antlrTool = tool;
        } else {
            throw new IllegalStateException("antlr.Tool already registered");
        }
    }

    /**
     * @since 2.7.2
     */
    protected antlr.Tool getTool() {
        return antlrTool;
    }

    /**
     * Delegates the error message to the tool if any was registered via {@link
     * #setTool(antlr.Tool)}
     *
     * @since 2.7.2
     */
    public void reportError(String s) {
        if (getTool() != null) {
            getTool().error(s, getFilename(), -1, -1);
        } else {
            super.reportError(s);
        }
    }

    /**
     * Delegates the error message to the tool if any was registered via {@link
     * #setTool(antlr.Tool)}
     *
     * @since 2.7.2
     */
    public void reportError(RecognitionException e) {
        if (getTool() != null) {
            getTool().error(e.getMessage(),
                            e.getFilename(),
                            e.getLine(),
                            e.getColumn());
        } else {
            super.reportError(e);
        }
    }

    /**
     * Delegates the warning message to the tool if any was registered via
     * {@link #setTool(antlr.Tool)}
     *
     * @since 2.7.2
     */
    public void reportWarning(String s) {
        if (getTool() != null) {
            getTool().warning(s, getFilename(), -1, -1);
        } else {
            super.reportWarning(s);
        }
    }

    protected ANTLRTokdefParser(TokenBuffer tokenBuf, int k) {
        super(tokenBuf, k);
        tokenNames = _tokenNames;
    }

    public ANTLRTokdefParser(TokenBuffer tokenBuf) {
        this(tokenBuf, 3);
    }

    protected ANTLRTokdefParser(TokenStream lexer, int k) {
        super(lexer, k);
        tokenNames = _tokenNames;
    }

    public ANTLRTokdefParser(TokenStream lexer) {
        this(lexer, 3);
    }

    public ANTLRTokdefParser(ParserSharedInputState state) {
        super(state, 3);
        tokenNames = _tokenNames;
    }

    public final void file(ImportVocabTokenManager tm)
      throws RecognitionException, TokenStreamException {

        Token name = null;

        try {      // for error handling
            name = LT(1);
            match(ID);
            {
                _loop225:
                do {
                    if ((ID == LA(1) || STRING == LA(1))) {
                        line(tm);
                    } else {
                        break _loop225;
                    }

                } while (true);
            }
        } catch (RecognitionException ex) {
            reportError(ex);
            consume();
            consumeUntil(_tokenSet_0);
        }
    }

    public final void line(ImportVocabTokenManager tm)
      throws RecognitionException, TokenStreamException {

        Token s1 = null;
        Token lab = null;
        Token s2 = null;
        Token id = null;
        Token para = null;
        Token id2 = null;
        Token i = null;
        Token t = null;
        Token s = null;

        try {      // for error handling
            {
                if ((STRING == LA(1))) {
                    s1 = LT(1);
                    match(STRING);
                    s = s1;
                } else
                if ((ID == LA(1)) && (ASSIGN == LA(2)) && (STRING == LA(3))) {
                    lab = LT(1);
                    match(ID);
                    t = lab;
                    match(ASSIGN);
                    s2 = LT(1);
                    match(STRING);
                    s = s2;
                } else if ((ID == LA(1)) && (LPAREN == LA(2))) {
                    id = LT(1);
                    match(ID);
                    t = id;
                    match(LPAREN);
                    para = LT(1);
                    match(STRING);
                    match(RPAREN);
                } else
                if ((ID == LA(1)) && (ASSIGN == LA(2)) && (INT == LA(3))) {
                    id2 = LT(1);
                    match(ID);
                    t = id2;
                } else {
                    throw new NoViableAltException(LT(1), getFilename());
                }

            }
            match(ASSIGN);
            i = LT(1);
            match(INT);

            Integer value = Integer.valueOf(i.getText());
            // if literal found, define as a string literal
            if (s != null) {
                tm.define(s.getText(), value.intValue());
                // if label, then label the string and map label to token symbol also
                if (t != null) {
                    StringLiteralSymbol sl =
                      (StringLiteralSymbol)tm.getTokenSymbol(s.getText());
                    sl.setLabel(t.getText());
                    tm.mapToTokenSymbol(t.getText(), sl);
                }
            }
            // define token (not a literal)
            else if (t != null) {
                tm.define(t.getText(), value.intValue());
                if (para != null) {
                    TokenSymbol ts = tm.getTokenSymbol(t.getText());
                    ts.setParaphrase(para.getText());
                }
            }

        } catch (RecognitionException ex) {
            reportError(ex);
            consume();
            consumeUntil(_tokenSet_1);
        }
    }


    static public final String[] _tokenNames = {"<0>",
      "EOF",
      "<2>",
      "NULL_TREE_LOOKAHEAD",
      "ID",
      "STRING",
      "ASSIGN",
      "LPAREN",
      "RPAREN",
      "INT",
      "WS",
      "SL_COMMENT",
      "ML_COMMENT",
      "ESC",
      "DIGIT",
      "XDIGIT"};

    static private long[] mk_tokenSet_0() {
        long[] data = {2L, 0L};
        return data;
    }

    static public final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());

    static private long[] mk_tokenSet_1() {
        long[] data = {50L, 0L};
        return data;
    }

    static public final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());

}
