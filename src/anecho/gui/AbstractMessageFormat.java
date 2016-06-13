/**
 * Message Format takes in a String message and reformats it to certain line
 * lengths (using the \n character). This can be quite helpful in use with
 * JOptionPane messages and the like 
 * $Id: AbstractMessageFormat.java,v 1.2
 * 2006/11/12 07:42:26 jeffnik Exp $
 */
package anecho.gui;

import java.util.StringTokenizer;

import net.sf.wraplog.AbstractLogger;
import net.sf.wraplog.NoneLogger;
import net.sf.wraplog.SystemLogger;

/**
 * Simple line formatting methods
 */
public abstract class AbstractMessageFormat {

    private static final int standWrap = 40; // Standard number of characters to wrap if none is supplied

    private static final boolean DEBUG = false;

    /**
     * An empty constructor!
     */
    private AbstractMessageFormat() {

    }

    /**
     * Wrap the input we're given at the interval suggested by standWrap
     *
     * @param input The string to be modified by the line wrap.
     * @return The new string that contains the extra new-line escapes.
     */
    public static String wrap(final String input) {
        return wrap(input, standWrap);
    }

    /**
     * Take the input we're given and wrap at the user-defined intervals
     *
     * @param input The string to be modified by the line wrap.
     * @param wrapLen The maximum number of characters per line
     * @return The new string that contains the extra new-line escapes.
     */
    public static String wrap(final String input, final int wrapLen) {

        AbstractLogger logger;

        if (DEBUG) {

            logger = new SystemLogger();
        } else {
            logger = new NoneLogger();
        }

        final StringBuffer output;
        output = new StringBuffer();

        // Break the input into tokens based on existing new-lines so that we
        // can honour those
        final StringTokenizer newlineTok;
        newlineTok = new StringTokenizer(input, "\n", true);

        logger.debug("AbstractMessageFormat.wrap: based on new-lines built into the text, we have " + newlineTok.countTokens() + " lines.");

        // String temp;
        while (newlineTok.hasMoreTokens()) {

            String temp;
            StringBuffer workBuf;
            
            workBuf = new StringBuffer();

            // final StringTokenizer strTok = new StringTokenizer(input, " ", true);
            final StringTokenizer strTok;
            strTok = new StringTokenizer(newlineTok.nextToken(), " ", true);

            while (strTok.hasMoreTokens()) {
                temp = strTok.nextToken();

                if ((workBuf.length() + temp.length()) >= wrapLen) {
                    output.append(workBuf.toString());
                    output.append('\n');
                    logger.debug("AbstractMessageFormat.wrap: Sentence tokenizer inserted newline.");
                    workBuf = new StringBuffer();

                    // Just to make things look a little nicer, we'll see if this element
                    // starts with a space and lop it off if so.
                    if (temp.charAt(0) == ' ') {

                        logger.debug("Truncating token: " + temp);

                        final int tempLen = temp.length();

                        if (tempLen > 1) {
                            temp = temp.substring(1, temp.length() - 1);
                        } else {
                            temp = "";
                        }
                    }
                }

                workBuf.append(temp);

            }

            // Now catch the last little bit of our work buffer
            output.append(workBuf.toString());

        }

        logger.debug("Our final string is: " + output.toString());

        return output.toString();
    }
}
