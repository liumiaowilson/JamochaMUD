/*
 * TextUtils.java
 *
 * Created on October 28, 2005, 10:33 PM
 *
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 */
package anecho.gui;

/**
 * This class contains methods to do some common string requirements
 * such as stripping escape characters.
 * @author Jeff Robinson
 */
public final class TextUtils {

    /**
     * Character representing the ANSI "bell"
     */
    private static final char BELL = '7';
    /**
     * Enables and disables debugging output
     */
    private static final boolean DEBUG = false;

    /** Creates a new instance of TextUtils */
    private TextUtils() {
    }

    /**
     * This method returns the value of a string
     * once it has had any escape characters stripped
     * from it.
     * @param token The string that will be analysed for escape characters.
     * @param useBell <CODE>true</CODE> - trigger system "bell" if a bell sequence is stripped.
     * <CODE>false</CODE> - do not trigger the system "bell" if a bell sequence is stripped.
     * @return Returns the initial string with escapes removed.
     */
    public static synchronized String stripEscapes(final String token, final boolean useBell) {
        final StringBuffer workString = new StringBuffer("");
        boolean loop = true;
        int start = 0;
        int end = 0;
        final int tLen = token.length();

        do {
            // Look for the next escape character
            end = token.indexOf('\u001b', start);

            // if (end < start && start < 0) {
            if (start < 0) {
                // There are no escapes left
                System.out.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("We_break_to_avoid_an_index_error."));
                break;
            }

            if (end < start) {
                workString.append(token.substring(start));
                loop = false;
                break;
            }

            // if (end > 0 && start >= 0) {
            if (end > 0) {
                workString.append(token.substring(start, end));
            }

            // Now set the new 'start'
            start = token.indexOf('m', end) + 1;

            if (start <= end) {
                loop = false;
                if (start < 0) {
                    System.out.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("We_don't_have_an_'m'_to_end_our_ANSI_escape.__We_may_not_have_received_it_yet."));
                } else {
                    // Check for BELL character (this is probably an ugly place to stick this Fix Me XXX)
                    if (token.charAt(1) == BELL && useBell) {
                        java.awt.Toolkit.getDefaultToolkit().beep();
                        if (DEBUG) {
                            System.out.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("Start:_") + start + java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("_end:_") + end);
                        }
                        start = 2;
                        if (end == 0 && tLen > 2) {
                            // There are no more escapes, so we'll just add the remainder of the string
                            workString.append(token.substring(2));
                        }

                        break;
                    } else {
                        if (DEBUG) {
                            System.out.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("Not_a_proper_ANSI_escape"));

                            for (int i = 0; i < tLen; i++) {
                                System.out.print(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("*") + token.charAt(i) + java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("*"));
                            }
                        }
                        break;
                    }
                }

            }

            // Check to see if we've reached the end of our token.  If this is
            // the case, then we can end the loop and return what we've got so far.
            if (start >= tLen) {
                loop = false;
                break;
            }
        } while (loop);

        return workString.toString();
    }

    /**
     * This method is used to remove extra characters from a URL.
     * Thoug not entirely scientific, it will get rid of things like trailing
     * quotation marks
     * @param input
     * @return
     */
    public static synchronized String cleanURL(final String input) {
        // Remove quotation marks
        String output = input.replace("\"", "");

        // convert spaces to %22
        output = output.replace(" ", "%22");

        return output;
    }

    /**
     * This method converts the numeric portion of a String to an Int.
     * For example, having a Java version of 1.6.0_01 will return 1.6.0
     * (discarding everything after the first non-numeric character)
     * @param input A mix string of numbers and characters
     * @return An integer based on the intial string
     */
    public static double stringToDouble(final String input) {
        double retDouble = -1;
        boolean firstDecimal = false;
        final StringBuffer endStr = new StringBuffer();

        // This is going to be simple-stupid and ugly.  Start at the first
        // character and discard everything that isn't a number (with the exception)
        // of the first decimal
        final int len = input.length();
        char testChar;
        boolean loopCheck;

        for (int i = 0; i < len; i++) {
            loopCheck = false;
            testChar = input.charAt(i);

            if (Character.isDigit(testChar)) {
                loopCheck = true;
            } else {
                if (testChar == '.' && !firstDecimal) {
                    // This is the first decimal we've reached, so we'll add it
                    loopCheck = true;
                    firstDecimal = true;
                }
            }

            if (loopCheck) {
                if (DEBUG) {
                    System.err.println("TextUtils.StringToDouble Adding: " + testChar);
                }

                endStr.append(testChar);
            }
        }

        final String newNum = endStr.toString();

        try {
            // retDouble = Integer.parseInt(newNum);
            retDouble = Double.parseDouble(newNum);
        } catch (Exception parseExc) {
            if (DEBUG) {
                System.err.println("Parsing exception: " + parseExc);
            }
        }

        return retDouble;
    }

    /**
     * This method splits the given string into an array of strings separated
     * @param inStr The given string to split
     * @param splitStr The string to base the splitting on
     * @return An array of strings containing the split text
     */
    public static String[] splitter(final String inStr, final String splitStr) {
        // Get count of how many escapes there are and create a string array 1 larger
        if (DEBUG) {
            System.err.println("TextUtils.splitter entered with: " + inStr);
            System.err.println("Split string is: *" + splitStr + "*");
        }

        final int splits = TextUtils.countSplits(inStr, splitStr);
        int nextIndex = 0;
        int prevIndex = 0;
        int count = 0;

        if (DEBUG) {
            System.err.println("TextUtils.splitter has found " + splits + " splits.");
        }

        String retStr[] = new String[splits + 1];
        boolean running = true;

        while (running) {

            nextIndex = inStr.indexOf(splitStr, nextIndex);

            if (nextIndex == 0) {
                if (DEBUG) {
                    System.err.println("TextUtils.splitter index is 0.  Advancing to next token.");
                }
                nextIndex = inStr.indexOf(splitStr, 1);
            }

            if (nextIndex == -1) {
                // Grab the remainder of the string
                if (DEBUG) {
                    System.err.println("Grabbing the remainder of the string starting at " + prevIndex);
                    System.err.println("Substring is " + inStr.substring(prevIndex));
                    System.err.println("prevIndex:" + prevIndex + " inStr.length-1: " + (inStr.length() - 1));
                }

                // if (prevIndex < inStr.length() - 1) {
                if (prevIndex < inStr.length()) {
                    retStr[count] = inStr.substring(prevIndex);
                }

                count++;

                running = false;
            } else {

                if (DEBUG) {
                    System.err.println("TextUtils.splitter start " + prevIndex + " end: " + nextIndex);
                }

                retStr[count] = inStr.substring(prevIndex, nextIndex);
                prevIndex = nextIndex;
                nextIndex++;
                count++;

            }

            if (DEBUG) {
                System.err.println(count + ") " + retStr[count - 1]);
            }

        }


        return retStr;
    }

    /**
     * Counts the number of times the matching string appears in the given string
     * An instance of the split as the first substring does not count as a match,
     * as a real "split" cannot occur if there is nothing infront of it.
     * @param inStr The main string to compare against
     * @param splitStr The matching portions to find
     * @return A count of the number of times splitStr appears within inStr
     */
    public static int countSplits(final String inStr, final String splitStr) {
        int lastIndex = 0;
        int count = 0;

        if (DEBUG) {
            System.err.println("TextUtils.countSplits string: " + inStr);
            System.err.println("TextUtils.countSplits splitter: " + splitStr);
        }

        if (inStr.length() > 0) {
            while (lastIndex != -1) {

                if (DEBUG) {
                    System.err.println("TextUtils.countSplits last index " + lastIndex);
                }

                lastIndex = inStr.indexOf(splitStr, lastIndex);

                if (lastIndex != -1) {
                    if (lastIndex != 0) {
                        // Escapes happening at the beginning of the string don't count!
                        count++;
                    }
                    
                    lastIndex++;
                }
            }
        }

        if (DEBUG) {
            System.err.println("TextUtils.countSplits total: " + count);
        }

        return count;
    }
}
