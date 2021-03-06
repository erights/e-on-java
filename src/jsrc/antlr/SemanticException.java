package antlr;

/* ANTLR Translator Generator
 * Project led by Terence Parr at http://www.jGuru.com
 * Software rights: http://www.antlr.org/license.html
 *
 * $Id: //depot/code/org.antlr/release/antlr-2.7.5/antlr/SemanticException.java#1 $
 */

public class SemanticException extends RecognitionException {

    static private final long serialVersionUID = -5795236298214066300L;

    public SemanticException(String s) {
        super(s);
    }

    /**
     * @deprecated As of ANTLR 2.7.2 use 
     *             {@link #SemanticException(String,String,int,int) }
     */
    public SemanticException(String s, String fileName, int line) {
        this(s, fileName, line, -1);
    }

    public SemanticException(String s, String fileName, int line, int column) {
        super(s, fileName, line, column);
    }
}
