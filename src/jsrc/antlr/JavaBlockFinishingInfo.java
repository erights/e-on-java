package antlr;

/* ANTLR Translator Generator
 * Project led by Terence Parr at http://www.jGuru.com
 * Software rights: http://www.antlr.org/license.html
 *
 * $Id: //depot/code/org.antlr/release/antlr-2.7.5/antlr/JavaBlockFinishingInfo.java#1 $
 */

class JavaBlockFinishingInfo {

    String postscript;                // what to generate to terminate block
    boolean generatedSwitch;// did block finish with "default:" of switch?
    boolean generatedAnIf;

    /**
     * When generating an if or switch, end-of-token lookahead sets will become
     * the else or default clause, don't generate an error clause in this
     * case.
     */
    boolean needAnErrorClause;


    JavaBlockFinishingInfo() {
        postscript = null;
//        generatedSwitch = generatedSwitch = false;
        needAnErrorClause = true;
    }

    JavaBlockFinishingInfo(String ps,
                                  boolean genS,
                                  boolean generatedAnIf,
                                  boolean n) {
        postscript = ps;
        generatedSwitch = genS;
        this.generatedAnIf = generatedAnIf;
        needAnErrorClause = n;
    }
}
