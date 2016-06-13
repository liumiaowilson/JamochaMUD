/**
 * JMSwingText, a Swing-based text area for ANSI colours (originally developed
 * for JamochaMUD) $Id: JMSwingText.java,v 1.37 2013/09/16 01:40:49 jeffnik Exp $
 */

/*
 * JMSwingText, a Swing-based text area for ANSI colours Copyright (C) 2003-2010
 * Jeff Robinson
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 *
 */
package anecho.gui;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Enumeration;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.*;
import javax.swing.text.StyleContext.SmallAttributeSet;

/**
 * JMSwingText, a Swing-based text area for ANSI colours (originally developed
 * for JamochaMUD)
 */
public class JMSwingText extends JScrollPane implements MouseListener, KeyListener, HyperlinkListener {

    final transient private JMSwingTextPane tPane;
    /**
     * Standard document format
     */
    final transient private DefaultStyledDocument textDoc;
    // final private HTMLDocument textDoc; // Experimental HTML document format.  Fix Me XXX
    /**
     * The attribute set of our text area
     */
    final transient private SimpleAttributeSet attr;
    /**
     * A temporary colour variable
     */
    private transient Color tempColour;
    /**
     * The foreground colour of our text area
     */
    private transient Color fgColour;
    /**
     * The background colour of our text area
     */
    private transient Color bgColour;
    /**
     * Our basic font style plain SanSerif font at 16 points
     */
    private transient Font standardFont = new Font("SanSerif", Font.PLAIN, 16);
    /**
     * The text area'linkStyle mouse listener
     */
    private transient MouseListener mListener;	// A mouselistener
    /**
     * The text area'linkStyle key listener
     */
    private transient KeyListener keyL;
    /**
     * The status of our background painting. OS/2 JVM 1.1.8 + Swing screws up
     * background painting
     */
    private transient boolean paintBG = true;
    /**
     * A variable that enables or disabled debugging output
     */
    private static final boolean DEBUG = false;
    /**
     * A variable that determines if the raw output from the MU* should be shown
     */
    private static final boolean RAWLOG = false;
    /**
     * The total length of the current text area
     */
    // private int totalLength;
    /**
     * A variable to keep track of the "bold" state of our letters
     */
    private transient boolean isBold = false;
    /**
     * A variable tracking whether to use the 8x2- or 16-colour palette
     */
    private boolean boldNotBright = false;
    /**
     * An array that contains the colours of our private palette
     */
    private transient Color[] privPal;
    /**
     * This variable indicates moving the scrollbars one "page" up
     */
    public static final int PAGEUP = 0;
    /**
     * This variable indicates moving the scrollbars one "page" down.
     */
    public static final int PAGEDOWN = 1;
    /**
     * This variable is to determine whether URLs should be underlined
     */
    private transient boolean markURLs = true;
    /**
     * The escape character
     */
    private static final char ESCAPECHAR = '\u001b';
    /**
     * The escape character as a String
     */
    private static final String ESCAPESTR = Character.toString(ESCAPECHAR);
    /**
     * Tracks whether we are buffering escape characters before outputting
     */
    private transient boolean bufferingEscape = false;
    /**
     * This contains any buffered escape text
     */
    final transient private StringBuffer bufferedText = new StringBuffer();
    /**
     * This enables and disables destructive resizing of text
     */
    private boolean destructiveResize = false;

    /** The constructor for JMSwingText.
     * There is no guarantee that this component will be fully initialised
     * before a program calls some of its methods.  Therefore, we will
     * check our colours, fonts, etc to see if they have already been
     * set.  If not, we will set them to our defaults
     */
    public JMSwingText() {

        super();

        textDoc = new DefaultStyledDocument();

        // Experimental HTML support
        // javax.swing.text.html.HTMLEditorKit kit = new javax.swing.text.html.HTMLEditorKit();
        // textDoc = (javax.swing.text.html.HTMLDocument) (kit.createDefaultDocument());

        attr = new SimpleAttributeSet();
        tPane = new JMSwingTextPane(textDoc);

        tPane.addMouseListener(this);
        tPane.addKeyListener(this);
        tPane.addHyperlinkListener(this);
        this.setViewportView(tPane);

        // tPane.setEditorKit(kit);
        // tPane.setContentType("text/html");

        if (DEBUG) {
            System.err.println("JMSwingText.JMSwingText() has created tPane: " + tPane);
            System.err.println("Our text area is using the EditorKit: " + tPane.getEditorKit());
        }

        // Set the standard font
        StyleConstants.setFontFamily(attr, "SansSerif");
        StyleConstants.setFontSize(attr, 16);

        // Background color
        if (bgColour == null) {
            bgColour = new Color(SystemColor.text.getRGB());
        }

        // Foreground color
        if (fgColour == null) {
            fgColour = new Color(SystemColor.textText.getRGB());
        }

        tPane.setBackground(bgColour);
        tPane.setForeground(fgColour);
        StyleConstants.setForeground(attr, fgColour);

        if (DEBUG) {
            System.err.println("JMSwingText constructor: Background painting set to: " + paintBG);
        }

        if (paintBG) {
            StyleConstants.setBackground(attr, bgColour);
        }

        // This should ensure that our palettes are set-up properly.
        setBoldNotBright(false);
    }

    /**
     * Append text to our JMSwingText.
     * Revised method supplied by Stephane Boisjoli 2004-11-26
     * 
     * 2008-08-09 Changed this method to private writeOut for support
     * of the new ANSI parsing engine
     * @param rawInput The input to be added to the current JMSwingText area.
     */
    private synchronized void writeOut(final String rawInput) {

        if (rawInput == null) {
            return;
        }

        String input;

        // Check to see if URLs must be marked first
        // This May be moved for new link support.  Fix Me XXX 2010-04-19
        if (markURLs) {
            input = applyLinkMark(rawInput);
        } else {
            input = rawInput;
        }

        if (RAWLOG) {
            System.err.print(input);
        }

        final int inputLen = input.length();
        int totalLength = textDoc.getLength();

        try {

            int doneTo = 0, escPos;
            String subText;

            while ((escPos = input.indexOf('\u001b', doneTo)) > - 1) {

                // There are special escape chars that need to be processed.
                // First ditch any non-special stuff
                if (escPos - doneTo > 0) {
                    if (DEBUG) {
                        System.err.println("JMSwingText.append string: " + input);
                        System.err.println("Document total length: " + totalLength);
                        System.err.println("doneTo: " + doneTo + " escPos: " + escPos);
                        System.err.println("*" + input.substring(doneTo, escPos) + "*");
                        System.err.println("totalLength is: " + totalLength);
                    }

                    subText = input.substring(doneTo, escPos);
                    textDoc.insertString(totalLength, subText, attr);

                    // textDoc.insertString(totalLength, input.substring(doneTo, escPos) , attr);
                    totalLength += escPos - doneTo;

                }

                // Now process the Escape.
                doneTo = checkEscape(input, escPos) + 1;

            }

            // If anything remains it is just icing, I mean, regular text
            if (doneTo < inputLen) {

                // We check the length again in the event that there has been a change
                // due to a clear screen
                totalLength = textDoc.getLength();

                if (DEBUG) {
                    System.err.println("Length: " + inputLen);
                    System.err.println("doneTo: " + doneTo);
                    System.err.println("TotalLength: " + totalLength);
                    System.err.println("Current totalLength: " + textDoc.getLength());

                }

                subText = input.substring(doneTo);
                textDoc.insertString(totalLength, subText, attr);
                // textDoc.insertString(totalLength, input.substring(doneTo) , attr);

                totalLength += inputLen - doneTo;

                if (DEBUG) {
                    System.err.println(input.substring(doneTo));
                    System.err.println("totalLength is: " + totalLength);
                }

            }



            tPane.setCaretPosition(totalLength);

        } catch (Exception e) {

            if (DEBUG) {
                System.err.println("JMSwingText.append(): Error inserting text. " + e);
                e.printStackTrace();
            }

        }

    }

    /**
     *
     * This sets the text area editable or non-editable.
     * @param state <CODE>true</CODE> sets the text area editable.
     * <CODE>false</CODE> sets the text area non-editable.
     *
     */
    public synchronized void setEditable(final boolean state) {

        tPane.setEditable(state);

    }

    /**
     * This returns whether the user can edit the JMSwingText area.
     * @return
     * <code>true</code>  The JMSwingText component is editable
     * <code>false</code> The JMSwingText component is read-only
     */
    public synchronized boolean isEditable() {
        return tPane.isEditable();
    }

    /**
     *
     * @param token
     * @param startPos
     * @return
     */
    public int checkEscape(final String token, final int startPos) {


        final int linkCheck = token.indexOf(ESCAPESTR + "[!");
        // int end = token.indexOf('m', startPos);	// look for the closing 'm' from the offset of 'startPos'
        int end;
        // int spaceCheck = token.indexOf(' ', startPos);  // look for the next space character from the offset of 'startPos'
        int spaceCheck;
        String endChar;
        String spaceChar;

        if (DEBUG) {
            System.err.println("JMSwingText.checkEscape initial text is: " + token);
            System.err.println("linkCheck of returns as: " + linkCheck);
        }

        // This does not quite work, as if there is a colour code and then a link
        // the colour code "eats" the link code as well.
        if (linkCheck > -1) {
            // We actually have a web-link so we can't use a plain "m" to find the
            // end, so we'll use some hokery-trickery!  Fix Me XXX
            int testM = token.indexOf('m', startPos);

            if (testM > -1 && testM < linkCheck) {
                endChar = "m";
                spaceChar = " ";
            } else {
                endChar = "]";
                spaceChar = "]";
            }
        } else {
            endChar = "m";
            spaceChar = " ";
        }

        end = token.indexOf(endChar, startPos);	// look for the closing 'm' from the offset of 'startPos'
        spaceCheck = token.indexOf(spaceChar, startPos);

        if (DEBUG) {
            System.err.println("JMSwingText.checkEscape endChar " + endChar + " located at " + end);
            System.err.println("JMSwingText.checkEscape spaceCheck is " + spaceCheck);
        }

        // Either a space occurs before the first "m" or there is no "m"
        // This can happen if we encounter a "simple escape""
        if (end < 0 || (spaceCheck < end && spaceCheck > -1)) {

            if (DEBUG) {
                System.err.println("JMSwingText.checkEscape() checking for simple escape.");

            }

            final int newEnd = checkSimpleEscape(token, startPos);

            // return startPos + 2;       // Could be a simple escape like "BELL"
            return newEnd;

            // Simple escapes:
            // BELL
            // ESC2J - CLEAR SCREEN

            // return i; 	// This token doesn't qualify as a colour
        }

        final String escTokens[] = this.escapeTokens(token.substring(startPos, end), endChar);

        final int tokenCount = escTokens.length;

        if (DEBUG) {
            System.err.println("JMSwingText.checkEscape has counted " + tokenCount + " escape tokens");
        }

        for (int i = 0; i < tokenCount; i++) {
            // Go through each token
            if (DEBUG) {
                System.err.println("Token " + i + ": " + escTokens[i]);
            }

            if (escTokens[i].startsWith("#")) {
                // Process a colour escape
                if (DEBUG) {
                    System.err.println("JMSwing.checkEscape processing colour rule.");
                }

                final Color tempColor = hexToColour(escTokens[i]);
                if (i == 0) {
                    StyleConstants.setForeground(attr, tempColor);
                } else {
                    StyleConstants.setBackground(attr, tempColor);
                }

            } else if (escTokens[i].startsWith("!")) {
                // Process as a portion of a link
                if (DEBUG) {
                    System.err.println("JMSwingText.checkEscape() We think we have a portion of a URL: " + escTokens[i]);
                }

                linkFromArray(escTokens);
                // We've created a link so we'll break out of this loop

                // We may need to supply a new "end" here to cut out extra spaces.
                // Fix Me XXX

                break;
                // }
            } else {

                if (!escTokens[i].equals("")) {
                    // Process a "standard" ANSI escape
                    final int stdToken = Integer.parseInt(escTokens[i]);
                    buildColourAttr(stdToken);

                    if (DEBUG) {
                        System.err.println("JMSwingText.checkEscape() standard Token " + stdToken);
                    }
                }
            }
        }

        return end;

    }

    /** Check and act upon simple escape codes
     * 
     */
    private int checkSimpleEscape(final String token, final int startPos) {
        // Immediately set the new Ending Position to the start+2,
        // if we don't recognize the escape, this will allow us to at least
        // get past it.
        if (DEBUG) {
            System.err.println("JMSwingText.checkSimpleEscape entered with token: " + token);
        }

        int newEndPos = startPos + 2;

        if (newEndPos > token.length()) {
            return token.length();
        }

        if (DEBUG) {
            System.err.println("JMSwingText.checkSimpleEscape is checking " + token + " starting at postition " + startPos);
        }
        // Check for BELL


        // Check for CLEAR SCREEN
        // if ((token.substring(startPos + 1, startPos + 4)).equalsIgnoreCase("[JK")) {
        // if ((token.substring(startPos, startPos + 6)).equalsIgnoreCase('\u001b' + "[H" + '\u001b' + "[J")) {
        if (DEBUG) {
            System.err.println("JMSwingText.checkSimpleEscape checking token: " + token.substring(startPos));
        }
        if (startPos + 3 > token.length()) {

            // if ((token.substring(startPos, startPos + 3)).equalsIgnoreCase('\u001b' + "[J") || (token.substring(startPos, startPos + 6)).equalsIgnoreCase('\u001b' + "[H" + '\u001b' + "[J")) {
            if ((token.substring(startPos, startPos + 3)).equalsIgnoreCase('\u001b' + "[J")) {
                if (DEBUG) {
                    System.err.println("We have received a CLEAR SCREEN signal");
                }

                clearScreen();

                // newEndPos = token.indexOf("J", startPos);
                newEndPos = token.indexOf('J', startPos);

                if (DEBUG) {
                    System.err.println("JMSwingText.checkSimpleEscape() newEndPos " + newEndPos);
                }

                // newEndPos = startPos + 5;

            } else {
                if (DEBUG) {
                    System.err.println("JMSwingText.checkSimpleEscape determined that " + token.substring(startPos, startPos + 6) + " was not a clear screen.");
                }
            }
        } else {
            // Need to set a newEndPos here?
        }

        return newEndPos;
    }

    /**
     * Reset all the font styles to normal (no bold, no underline, etc.)
     */
    private void normalText() {
        StyleConstants.setFontFamily(attr, standardFont.getName());
        StyleConstants.setFontSize(attr, standardFont.getSize());

        StyleConstants.setBold(attr, standardFont.isBold());
        StyleConstants.setItalic(attr, standardFont.isItalic());
        StyleConstants.setUnderline(attr, false);
        StyleConstants.setStrikeThrough(attr, false);

        StyleConstants.setForeground(attr, fgColour);

        if (paintBG) {
            StyleConstants.setBackground(attr, bgColour);
        }

        if (DEBUG) {
            System.err.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("Reset_styles"));
        }

    }

    /**
     * Setting the text style to bold, which uses an 16-colour palette varying between
     * 8 colours with two brightness levels instead of 16 distinct colours
     */
    private void boldTextStyle(final boolean state) {
        if (boldNotBright) {
            // StyleConstants.setBold(attr, true);
            StyleConstants.setBold(attr, state);
            if (DEBUG) {
                System.err.println("Setting isBold true.");
            }
        } else {
            // This must be reworked (after inclusion of MXP).  Fix Me XXX
            if (DEBUG) {
                System.err.println("We're setting isBold true, but boldNotBright is set " + boldNotBright);
            }

            // Due to some misplanning, we have to reverse engineer the existing colour!
            // StyleConstants.setForeground(attr, intTempForeColour);
            if (DEBUG) {
                System.err.println("Trying to determine old colour");
            }
            final Color oldColour = StyleConstants.getForeground(attr);
            for (int i = 0; i < 8; i++) {
                if (oldColour == privPal[i]) {
                    if (DEBUG) {
                        System.err.println("Old colour is #" + i);
                    }
                    StyleConstants.setForeground(attr, privPal[i + 8]);
                }
            }
        }

        // isBold = true;
        isBold = state;
    }

    /**
     *
     * A method to sift through ANSI colour numbers and assign
     * them to the proper "area" (foreground, background, etc...)
     * @param colAt The ANSI colour code to be processed by this method
     *
     */
    public synchronized void buildColourAttr(final int colAt) {

        if (colAt == 0) {
            // Reset the text to normal attributess
            normalText();
        }

        if (colAt < 30) {
            switch (colAt) {
                case -9:
                    // This is an "illegal" JMSwingText-only option to remove strike-throughs
                    // with-out affecting the rest of the text
                    StyleConstants.setStrikeThrough(attr, false);
                    break;
                case -4:
                    // This is an "illegal" JMSwingText-only option to remove underlines
                    // with-out affecting the rest of the text
                    StyleConstants.setUnderline(attr, false);
                    break;
                case -3:
                    StyleConstants.setItalic(attr, false);
                    break;
                case -1:
                    boldTextStyle(false);
                    break;
                case 0:
                    isBold = false;
                    if (DEBUG) {
                        System.err.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("Resetting_isBold_to_false."));
                    }
                    break;
                case 1:
                    boldTextStyle(true);
//                    if (boldNotBright) {
//                        StyleConstants.setBold(attr, true);
//                        if (DEBUG) {
//                            System.err.println("Setting isBold true.");
//                        }
//                    } else {
//                        if (DEBUG) {
//                            System.err.println("We're setting isBold true, but boldNotBright is set " + boldNotBright);
//                        }
//
//                        // Due to some misplanning, we have to reverse engineer the existing colour!
//                        // StyleConstants.setForeground(attr, intTempForeColour);
//                        if (DEBUG) {
//                            System.err.println("Trying to determine old colour");
//                        }
//                        final Color oldColour = StyleConstants.getForeground(attr);
//                        for (int i = 0; i < 8; i++) {
//                            if (oldColour == privPal[i]) {
//                                if (DEBUG) {
//                                    System.err.println("Old colour is #" + i);
//                                }
//                                StyleConstants.setForeground(attr, privPal[i + 8]);
//                            }
//                        }
//                    }

//                    isBold = true;

                    break;
                case 3:
                    StyleConstants.setItalic(attr, true);
                    break;
                case 4:
                    if (DEBUG) {
                        System.err.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("Set_Underline."));
                    }
                    StyleConstants.setUnderline(attr, true);
                    break;
                case 9:
                    StyleConstants.setStrikeThrough(attr, true);
                    break;
                case 22:
                    if (DEBUG) {
                        System.err.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("Setting_BOLD_false."));
                    }
                    StyleConstants.setBold(attr, false);
                    isBold = false;
                    break;
                case 24:
                    StyleConstants.setUnderline(attr, false);
                    break;
                default:

                /*
                 * Nothing
                 */

            }

        }


        /*
         * Real colour codes (JamochaMUD sort've fudges them right now Atributes
         * 0 Normal text 1 Bold (This gives the color higher intensity) 4
         * Underline 5 Blinking 7 Reverse (Foreground -> Background, Background
         * -> Foreground) 8 Hidden
         *
         * Foreground color 30 Black 31 Red 32 Green 33 Yellow 34 Blue 35
         * Magenta 36 Cyan 37 White
         *
         * Background color 39 Reset background colour 40 Black 41 Red 42 Green
         * 43 Yellow 44 Blue 45 Magenta 46 Cyan 47 White
         */

        if (DEBUG) {
            System.err.println("Our current colAt is " + colAt);
        }

        if (colAt > 29 && colAt < 50) {

            if (DEBUG) {
                System.err.println("JMSwingText.buildColourAttr colAt is " + colAt);
            }

            int cInt;

            if (colAt > 29) {
                if (colAt > 39) {
                    cInt = colAt - 40;
                } else {
                    cInt = colAt - 30;
                }
            } else {
                cInt = colAt;
            }

            if (DEBUG) {
                System.err.println("JMSwingText.buildColourAttr() reports cInt being " + cInt);
            }

            tempColour = privPal[cInt];

            if (DEBUG) {
                System.err.println("JMSwingText.BuildColourAttr colour is " + tempColour);
            }

            // Make colour brighter if it is supposed to be bold

            if (isBold) {
                if (cInt == 0) {
                    // This is suspect.  Fix Me XXX
                    if (colAt > 39) {
                        // If for a background colour, change to black
                        tempColour = Color.black;
                    } else {
                        // If for a foreground colour, use grey.
                        tempColour = Color.gray;
                    }

                } else {
                    tempColour = privPal[cInt + 8];

                    if (DEBUG) {
                        System.err.println("Changing regular colour (" + cInt + ") " + privPal[cInt] + " to (" + (cInt + 8) + ") " + privPal[cInt + 8]);
                    }
                }
            }



            if (colAt > 29 && colAt < 38) {
                if (DEBUG) {
                    System.err.println("JMSwingText.buildColourAttr setting Foreground colour to " + colAt);
                }
                StyleConstants.setForeground(attr, tempColour);
            }

            if (colAt > 39 && colAt < 48 && paintBG) {

                if (DEBUG) {
                    System.err.println("JMSwingText.buildColourAttr background colour should be " + colAt);
                }

                StyleConstants.setBackground(attr, tempColour);

            }

            if (colAt == 39 && paintBG) {
                StyleConstants.setBackground(attr, bgColour);
            }

        }

    }

    /**
     *
     * Tell us whether it is alright to paint the background colour or not.
     * Some combinations of Java 1.1.x and Swing seem to have trouble with
     * background painting and just paint a solid coloured square instead of
     * text!!
     * @param state <CODE>true</CODE> indicates we should handle painting the background colour
     * <CODE>false</CODE> indicates we should not paint the background colour ourselves
     *
     */
    public void setPaintBackground(final boolean state) {

        paintBG = state;

        if (DEBUG) {
            System.err.println("JMSwingText.setPaintBackground now set to: " + paintBG);
        }

    }

    /**
     * Returns whether background painting is enabled.  Background painting allows
     * the program to change the colour of the background.  Otherwise the background
     * will always be the user-defined colour
     * @return 
     * <code>true</code> Background painting is enabled 
     * <code>false</code> Background painting is not enabled
     */
    public boolean isPaintBackground() {
        return paintBG;
    }

    /**
     *
     * Set the foreground (text) colour of our display
     *
     * @param newColour The colour to set our foreground (text)
     *
     */
    @Override
    public void setForeground(final Color newColour) {

        fgColour = newColour;

        if (tPane != null) {

            tPane.setForeground(fgColour);

            StyleConstants.setForeground(attr, fgColour);

        }

    }

    @Override
    public Color getForeground() {
        Color retCol = Color.BLACK;

        if (tPane != null) {
            retCol = tPane.getForeground();
        }

        return retCol;
    }

    /**
     *
     * Set the background colour of our text area
     *
     * @param newColour The colour to set our background
     *
     */
    @Override
    public void setBackground(final Color newColour) {

        bgColour = newColour;

        if (tPane != null) {

            tPane.setBackground(bgColour);

            if (paintBG) {

                StyleConstants.setBackground(attr, bgColour);

            }

        }

    }

    @Override
    public Color getBackground() {
        Color retCol = Color.WHITE;

        if (tPane != null) {
            retCol = tPane.getBackground();
        }

        return retCol;

    }

    @Override
    public Font getFont() {
        Font retFont;

        if (tPane == null) {
            if (standardFont == null) {
                // This is ugly.  Hopefully it doesn't happen.  Fix Me XXX
                retFont = new Font("SanSerif", Font.PLAIN, 16);
                standardFont = retFont;
            } else {
                retFont = standardFont;
            }
            if (DEBUG) {
                System.err.println("JMSwingText.getFont returns standardFont " + retFont);
                System.err.println("JMSwingText.getFont which should be the same as " + standardFont);
            }
        } else {
            retFont = tPane.getFont();
//            if (DEBUG) {
//                System.err.println("JMSwingText.getFont return tPane font " + retFont);
//            }
        }


        return retFont;
    }

    /**
     * Set the font to be used in our display.
     * @param newFont The java.awt.Font to use for our display
     */
    @Override
    public void setFont(final Font newFont) {

        if (newFont == null) {
            if (DEBUG) {
                System.err.println("JMSwingText.setFont() tried to set font to null");
            }
            return;

        }

        standardFont = newFont;

        if (DEBUG) {
            System.err.println("JMSwingText.setFont() set to " + newFont);
            System.err.println("JMSwingText standardFont set to " + standardFont);
        }

        if (tPane == null) {
            if (DEBUG) {
                System.err.println("JMSwingText.setFont() reports tPane being null");
            }
        } else {

            tPane.setFont(newFont);
            StyleConstants.setFontFamily(attr, standardFont.getName());
            StyleConstants.setFontSize(attr, standardFont.getSize());

            if (standardFont.isBold()) {
                StyleConstants.setBold(attr, true);
            }

            if (standardFont.isItalic()) {
                StyleConstants.setItalic(attr, true);
            }

        }

        if (destructiveResize) {
            // Set standard for all existing type
            // This changes the font but destroys all other colours and formatting
            // final SimpleAttributeSet fontAttr = attr;
//            StyleConstants.setFontSize(fontAttr, standardFont.getSize());
//            textDoc.setCharacterAttributes(0, textDoc.getLength(), fontAttr, false);

            // DefaultStyledDocument doc = (DefaultStyledDocument) textPane.getDocument();
//            Enumeration stylEnum = textDoc.getStyleNames();
//            while (stylEnum.hasMoreElements()) {
//                String styleName = (String) stylEnum.nextElement();
//
//                // Get style object
//                Style style = textDoc.getStyle(styleName);
//                System.err.println("JMSwingText.setFont() style name: " + styleName + " and style: " + style);
//            }

            // Get section element
            Element sectionElem = textDoc.getDefaultRootElement();

            // Get number of paragraphs.
            int paraCount = sectionElem.getElementCount();

            for (int i = 0; i < paraCount; i++) {
                Element paraElem = sectionElem.getElement(i);
                AttributeSet attrib = paraElem.getAttributes();

                // Get the name of the style applied to this paragraph element; may be null

                String sn = (String) attrib.getAttribute(StyleConstants.NameAttribute);

                // Check if style name match
                // if (style.getName().equals(sn)) {
                // Reapply the paragraph style
                int rangeStart = paraElem.getStartOffset();
                int rangeEnd = paraElem.getEndOffset();

                Style curStyle = textDoc.getStyle(sn);

                // SmallAttributeSet tempSet = (SmallAttributeSet) attrib.copyAttributes();
                
                // MutableAttributeSet mutSet = new MutableAttributeSet(tempSet);
                MutableAttributeSet mutSet = (MutableAttributeSet) attrib.copyAttributes();
                
                mutSet.removeAttribute(StyleConstants.FontSize);
                mutSet.addAttribute(StyleConstants.FontSize, standardFont.getSize());

                // c.getStyledDocument().setParagraphAttributes(rangeStart, rangeEnd-rangeStart, style, true);
                // tPane.getStyledDocument().setParagraphAttributes(rangeStart, rangeEnd - rangeStart, mutSet, true);
                // }

                // Enumerate the content elements
//        for (int j=0; j<paraElem.getElementCount(); j++) {
//            Element contentElem = paraElem.getElement(j);
//            attrib = contentElem.getAttributes();
//
//                        
//            // Get the name of the style applied to this content element; may be null
//            sn = (String)attrib.getAttribute(StyleConstants.NameAttribute);
//
//            // Check if style name match
//            if (style.getName().equals(sn)) {
//                // Reapply the content style
//                int rangeStart = contentElem.getStartOffset();
//                int rangeEnd = contentElem.getEndOffset();
//                c.getStyledDocument().setCharacterAttributes(
//                    rangeStart, rangeEnd-rangeStart, style, true);
//            }
//        }
            }
        }

    }

    /**
     * Return all of our text
     * @return Return all the text current in our text widget.
     */
    public String getText() {

        return tPane.getText();

    }

    /**
     * Sets the text to be displayed.  If the display aready contains text
     * it is removed first.
     * @param initialText
     */
    public void setText(final String initialText) {
        this.clearScreen();
        this.append(initialText);
    }

    /**
     * Return any texted that is currently selected
     * @return returns any text currently selected in our text widget
     */
    public String getSelectedText() {
        if (DEBUG) {
            System.err.println("JMSwingText.getSelectedText() returning text: " + tPane.getSelectedText());
            System.err.println("getSelectedText start: " + tPane.getSelectionStart());
            System.err.println("getSelectedText end: " + tPane.getSelectionEnd());
        }
        return tPane.getSelectedText();
    }

    /**
     * Select text marked by the start and end integers
     * @param start The starting point of our selection.  This number
     * is the count of characters from the beginning of our
     * entire text.
     * @param end The end point of our selection.  This number is the
     * count of characters from the beginning of our text.
     */
    public void select(final int start, final int end) {
        // final int caretPos = tPane.getCaretPosition();
        tPane.select(start, end);

        // This was removed because it caused a problem with actual selection length
        // Does this affect other methods?  Fix Me XXX
        // tPane.setCaretPosition(caretPos);

        if (DEBUG) {
            System.err.println("JMSwingText.select set selection : " + start + " " + end);
            System.err.println("Component reports start: " + tPane.getSelectionStart() + " end " + tPane.getSelectionEnd());
        }
    }

    /**
     * We need to make our own mouse-listener that'll report back
     * to any listeners that may've registered to this component.  Or
     * something.  This may not make a lotta sense, I'm tired.
     * @param mouse The mouselistener for this widget
     */
    @Override
    public synchronized void addMouseListener(final MouseListener mouse) {
        mListener = AWTEventMulticaster.add(mListener, mouse);
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
    }

    /**
     * Remove given mouse listener from this widget
     * @param mouse The mouselistener to remove
     */
    @Override
    public synchronized void removeMouseListener(final MouseListener mouse) {
        if (mListener != null) {
            mListener = AWTEventMulticaster.remove(mListener, mouse);
        }
    }

// Mouse events
    /**
     * Capture any mouse clicks so that we can snoop them if need be.
     * @param event The captured mouse event
     */
    public void mouseClicked(final MouseEvent event) {

        // Fix me!  This has to be replaced to avoid a Null Pointer Exception
        // if (event.getClickCount() >= 2) {
        if (event == null) {
            return;
        }

//        try {
//            anecho.gui.Hyperlink testLink = (anecho.gui.Hyperlink)event.getComponent();
//
//            // We've got a link and should launch it!
//            if (DEBUG) {
//                System.err.println("JMSwingText.mouseClicked() has a link: " + testLink.getAddress());
//            }
//        } catch (Exception notLink) {
//            if (DEBUG) {
//                System.err.println("Not a link, so we'll continue on.");
//            }
//        }

        if (event.getClickCount() == 2) {
            if (DEBUG) {
                System.err.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("JMSwingText.mouseClicked()_(twice)_event_") + event);
            }

            final java.awt.Point newPos = new java.awt.Point(event.getX(), event.getY());
            final int pos = tPane.viewToModel(newPos);

            if (DEBUG) {
                System.err.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("Our_position_is:_") + pos);
            }

            // javax.swing.text.Element elem = textDoc.getCharacterElement(pos);
            // String word;

            try {
                // String word = textDoc.getText(elem.getStartOffset(), elem.getEndOffset() - elem.getStartOffset());
                final int start = javax.swing.text.Utilities.getWordStart(tPane, pos);
                final int end = javax.swing.text.Utilities.getWordEnd(tPane, pos);

                if (DEBUG) {
                    System.err.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("JMSwingText_attempting_to_set_selection:_") + start + java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("_to_") + (end - start));
                }

                // Our selection doesn't seem to work if we set it manually, but can we
                // depend on other operating systems to highlight the word?!
                // Fix Me XXX!
                // select(start, end - start);

                if (DEBUG) {
                    System.err.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("Selection_complete."));
                }
                // word = textDoc.getText(start, end - start);

                //if (DEBUG) {
                // System.err.println("Our word is: " + word);
                // }
            } catch (Exception exc) {
                System.err.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("Exception_trying_to_get_word_in_JMSwingText."));
            }

        }

        if (mListener != null) {
            mListener.mouseClicked(event);
        }

    }

    /**
     *
     * Empty event
     *
     * @param event Mouse Event
     *
     */
    public void mouseEntered(final MouseEvent event) {

        /*
         * // Change the mouse cursor to an I-beam setCursor(new
         * Cursor(Cursor.TEXT_CURSOR));
         */
    }

    /**
     *
     * Empty Event
     *
     * @param event Mouse Event
     *
     */
    public void mouseExited(final MouseEvent event) {

        /*
         * // Return mouse cursor back to standard pointer setCursor(new
         * Cursor(Cursor.DEFAULT_CURSOR));
         */
    }

    /**
     *
     * Empty Event
     *
     * @param event Mouse Event
     *
     */
    public void mousePressed(final MouseEvent event) {

        /*
         * // The mouse has been pressed, we will record its array position
         * textBegin = pointToArray(new Point(event.getX(), event.getY()));
         * textEnd = new Point(textBegin.x, textBegin.y); // Check to see if we
         * have to deselect any old selections if (selectBegin.x > 0 ||
         * selectBegin.y > 0 || selectEnd.x > 0 || selectEnd.y > 0) {
         * paintSelection(selectBegin, selectEnd, drawText.getBackground()); } //
         * And now we'll record the 'corrected position'; the character'linkStyle
         * position selectBegin = arrayToPoint(textBegin); selectEnd = new
         * Point(selectBegin.x, selectBegin.y);
         */
    }

    /**
     *
     * Empty Event
     *
     * @param event Mouse Event
     *
     */
    public void mouseReleased(final MouseEvent event) {

        /*
         * if(selectBegin.x == selectEnd.x && selectBegin.y == selectEnd.y) {
         * repaint(); // No need for selection... start and end points are the
         * same return; }
         */
    }

    /**
     *
     * Empty Event
     *
     * @param event Mouse Event
     *
     */
    public void mouseDragged(final MouseEvent event) {

        /*
         * // First we'll nab our selected character location Point tempPoint =
         * pointToArray(new Point(event.getX(), event.getY())); int
         * selectedColumn = tempPoint.x; int selectedRow = tempPoint.y; // First
         * we'll grab our FontMetrics // FontMetrics fMetric =
         * getFontMetrics(getFont()); int oldx = selectEnd.x, oldy = selectEnd.y;
         * // Figure out the coordinates of this selection tempPoint =
         * arrayToPoint(new Point(selectedColumn, selectedRow)); int x =
         * tempPoint.x; int y = tempPoint.y; if((x < selectBegin.x && y <
         * selectBegin.y) && (x < selectEnd.x && y < selectEnd.y)) {
         * selectBegin.x = x; selectBegin.y = y; textBegin.x = selectedColumn;
         * textBegin.y = selectedRow; } else { selectEnd.x = x; selectEnd.y = y;
         * textEnd.x = selectedColumn; textEnd.y = selectedRow; } if(oldx != x ||
         * oldy != y) { // erase our old selection first
         * paintSelection(selectBegin, new Point(oldx, oldy),
         * drawText.getBackground()); // Then draw our new selection
         * paintSelection(selectBegin, selectEnd, drawText.getBackground()); }
         */
    }

    /**
     *
     * Required event for mouseMoved
     * @param evt Mouse Event
     *
     */
    public void mouseMoved(final MouseEvent evt) {
    }

// KeyListener events
    /**
     *
     * Allow KeyListeners to be added to our JMSwingText.
     *
     * @param listener The listener to be added
     *
     */
    @Override
    public void addKeyListener(final KeyListener listener) {

        keyL = AWTEventMulticaster.add(keyL, listener);

        enableEvents(AWTEvent.KEY_EVENT_MASK);

    }

    /**
     *
     * Remove registered KeyListeners from our JMSwingText class
     *
     * @param listener The KeyListener to be removed
     *
     */
    @Override
    public void removeKeyListener(final KeyListener listener) {

        keyL = AWTEventMulticaster.remove(keyL, listener);

    }

    /**
     *
     * Send out any key events to our listeners
     *
     * @param event The received keyPressed event
     *
     */
    public void keyPressed(final KeyEvent event) {

        if (keyL != null) {

            keyL.keyPressed(event);

        }

    }

    /**
     *
     * Related our keyReleased event to our registered KeyListener
     *
     * @param event received KeyEvent
     *
     */
    public void keyReleased(final KeyEvent event) {

        if (keyL != null) {

            keyL.keyReleased(event);

        }

    }

    /**
     *
     * Pass our keyTyped events to our KeyListener
     *
     * @param event Our KeyEvent
     *
     */
    public void keyTyped(final KeyEvent event) {

        if (DEBUG) {

            System.err.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("keyTyped_on_JMSwingText."));

        }

        if (keyL != null) {

            keyL.keyTyped(event);

        }

    }

    /**
     *
     * Methods for handling hyper-links in text.  To be completed.  Fix Me XXX.
     *
     * @param evt Hyperlink event
     *
     */
    public void hyperlinkUpdate(final HyperlinkEvent evt) {
        // If we don't check this in versions of Java later than 1.2 the link
        // will be activated every time the mouse moves over it!
        // Fix this XXX
//        if (evt.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
//
//            if (DEBUG) {
//                System.err.println("JMSwingText.hyperlinkUpdated(): Received a HyperlinkEvent " + evt);
//            }
//        }
    }

    /** Convert the given hex value into a Java Color object */
    private Color hexToColour(final String initHexVal) {

        // private Color hexToColour(String hexVal) {
        if (DEBUG) {
            System.err.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("Entering_hexToColour_with:_") + initHexVal);
        }

        String hexVal;
        int red, green, blue;

        // if (hexVal.startsWith("#")) {
        // if (initHexVal.startsWith("#")) {
        final int hashPoint = initHexVal.indexOf('#');
        if (hashPoint > -1) {
            // strip the proceeding # symbol
            // hexVal = new String(hexVal.substring(1, 7));
            // hexVal = new String(initHexVal.substring(1, 7));
            hexVal = new String(initHexVal.substring(hashPoint + 1, 7));
        } else {
            hexVal = initHexVal;
        }

        if (DEBUG) {
            System.err.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("JMSwingText_hexVal:_") + hexVal);
        }

        try {
            red = Integer.parseInt(hexVal.substring(0, 2), 16);
            green = Integer.parseInt(hexVal.substring(2, 4), 16);
            blue = Integer.parseInt(hexVal.substring(4, 6), 16);
        } catch (Exception colourErr) {
            // Not a valid number
            if (DEBUG) {
                System.err.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("JMSwingText,_not_a_valid_colour_number,_setting_to_white."));
            }

            red = 255;
            green = 255;
            blue = 255;
        }

        return new Color(red, green, blue);

    }

    /**
     * Turn anti-aliasing of our letters on or off
     * @param status The variable that tells us to either turn on
     * anti-aliasing with <CODE>true</CODE>, or turn off anti-aliasing
     * with <CODE>false</CODE>.
     */
    public synchronized void setAntiAliasing(final boolean status) {

        tPane.setAntiAliasing(status);

        if (DEBUG) {
            System.err.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("JMSwingText.setAntiAliasing()_setting_antialiasing_to_") + status);
        }

    }

    /**
     * Return whether the current view has font anti-aliasing enabled or disabled
     * @return <code>true</code> anti-aliasing is enabled 
     * <code>false</code> anti-aliasing is disabled
     */
    public boolean isAntiAliasing() {
        return tPane.isAntiAliasing();
    }

    /** Copy any selected text to the system clipboard */
    public void copy() {
        tPane.copy();
    }

    /**
     * There are two different ways to interpret the "bold" feature on MU*'linkStyle.
     *  The first technique takes the "bold" command and makes the text bold.
     *  The second technique uses a palette at about half-brightness, and when
     *  the "bold" command is received, it turns the brightness up to full.
     * @param state <CODE>true</CODE> - Create the characters as bold, using a limited palette
     * <CODE>false</CODE> - Use the 16 colour palatte instead of bolding characters
     */
    public void setBoldNotBright(final boolean state) {
        if (DEBUG) {
            System.err.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("setBoldNotBright_set_to:_") + state);
        }

        boldNotBright = state;
        // Color tempCol;
        privPal = new Color[16];

        // Remap our palette of colours
        if (boldNotBright) {
            if (DEBUG) {
                System.err.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("Set_to_8_colour_palette"));
            }
            setStandardPalette();

        } else {
            if (DEBUG) {
                System.err.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("Colours_set_to_fancy_palette"));
            }
            // We will use the fancy palette technique
            setFancyPalette();

        }
    }

    /**
     * This returns the state of our colour rendering, if we are using the
     * 8 colour (bold) palette, or the 16 colour (bright) palette.
     * @return <CODE>true</CODE> - We are using the 8 colour (bold) palette
     * <CODE>false</CODE> - We are using the 16 colour (bright) palette
     */
    public boolean isBoldNotBright() {
        return boldNotBright;
    }

    /** This method enables "destructive" resizing of fonts.
     * By enabling this feature the JMSwingText panel will update the size
     * of the font of all lettering currently on-screen, but at the cost of
     * removing all other formating and colour.
     * 
     * If this method is not enabled lettering size change will be visible only
     * when new text is printed.
     * 
     * @param state <code>true</code> Enable destructive resizing
     * <code>false</code> Disable destructive resizing
     */
    public void setDestructiveResize(final boolean state) {
        destructiveResize = state;
    }

    /**
     * Returns whether destructive resizing is enabled
     * @return <code>true</code> destructive resizing is enabled
     * <code>false</code> destructive resizing is not enabled
     */
    public boolean isDestructiveResize() {
        return destructiveResize;
    }

    /**
     *
     * Move the scrollbars either one page up or one page down
     *
     * @param direction <CODE>PAGEDOWN</CODE> - move the scrollbars one page down
     *
     * <CODE>PAGEUP</CODE> - move the scrollbars one page up
     *
     */
    public void movePage(final int direction) {

        // Get a handle on our scrollbar

        // int pos = tPane.get

        final javax.swing.JScrollBar tempScroll = this.getVerticalScrollBar();

        // Get the unit to move our scrollbar

        final int inc = tempScroll.getBlockIncrement(javax.swing.JScrollBar.VERTICAL);

        final int pos = tempScroll.getValue();

        int newPos = 0;

        // Move the durn thing!

        if (direction == anecho.gui.JMSwingText.PAGEDOWN) {

            if (DEBUG) {

                System.err.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("JMSwingText.java:_PageDown."));

            }

            newPos = pos + inc;

        } else {

            if (DEBUG) {

                System.err.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("JMSwingText.java:_PageUp."));

            }

            newPos = pos - inc;

        }

        if (DEBUG) {

            System.err.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("Our_increment_is_") + inc);

            System.err.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("Our_position_is_") + pos);

            System.err.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("Our_new_position_should_be_") + newPos);

        }

        tempScroll.setValue(newPos);

    }

    /**
     *
     * Return an array of colours representing our current palette
     *
     * @return
     *
     */
    public Color[] getCurrentPalette() {

        if (privPal == null) {

            setStandardPalette();

        }

        final Color[] retPal = new Color[16];
        System.arraycopy(privPal, 0, retPal, 0, 16);

        // return privPal;
        // Return a copy of the array instead of exposing an internal array.
        return retPal;

    }

    /**
     *
     * Sets an entirely new colour palette based on the array given.  This array
     * must contain 16 colours, preferrably 8 "normal" colours and 8 "bold" colours.
     *
     * @param newPal An array of colours to be used.
     *
     */
//     public void setPalette(final Color[] newPal) throws Exception {
    public void setPalette(final Color[] newPal) {

        // Storing an array directly is just looking for the big hurt, so lets
        // do some parsing.

        if (newPal != null) {

            if (newPal.length == 16) {

                // privPal = newPal;
                System.arraycopy(newPal, 0, privPal, 0, 16);

            } else {

                if (DEBUG) {
                    System.err.println("JMSwingText.setPalette error.  The new palette must");
                    System.err.println("Contain 16 colours.  This palette contains " + newPal.length);
                }

                throw new ArrayIndexOutOfBoundsException();

            }

        }

    }

    /**
     * Sets colours in the output window to the ones listed in the getStandardPalette method
     */
    public void setStandardPalette() {
        // private void setStandardPalette() {
        privPal = new Color[16];

        if (DEBUG) {

            System.err.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("Standard_palette_set_via_setStandardPalette()"));

        }

        privPal = this.getStandardPalette();

    }

    /**
     * Sets the current output palette to the colours in the "getFancyPalette" method
     */
    private void setFancyPalette() {

        privPal = new Color[16];

        if (DEBUG) {

            System.err.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("Fancy_palette_set_via_setFancyPalette()"));

        }

        // We will use the fancy palette technique
        privPal = this.getFancyPalette();
    }

    /**
     *
     * Returns an array representing the "Standard" colours to be
     * used.
     *
     * @return An array of Color objects, each representing one
     * of the 16 colours of the colour palette.
     *
     */
    public Color[] getStandardColours() {

        // Color[] retPal = new Color[16];

        Color[] retPal;

        if (boldNotBright) {

            retPal = getStandardPalette();

        } else {

            retPal = getFancyPalette();

        }

        return retPal;

    }

    /**
     * This method returns a palette of 16 "standard" colours.
     * @return an array of 16 "standard" colours
     */
    private Color[] getStandardPalette() {
        Color[] stdPal = new Color[16];
        if (DEBUG) {
            System.err.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("Standard_palette_set_via_setStandardPalette()"));
        }
        stdPal[0] = Color.black;
        stdPal[1] = Color.red;
        stdPal[2] = Color.green;
        stdPal[3] = Color.yellow;
        stdPal[4] = Color.blue;
        stdPal[5] = Color.magenta;
        stdPal[6] = Color.cyan;
        stdPal[7] = Color.white;
        // upper colours
        stdPal[8] = Color.gray;
        stdPal[9] = Color.red;
        stdPal[10] = Color.green;
        stdPal[11] = Color.yellow;
        stdPal[12] = Color.blue;
        stdPal[13] = Color.magenta;
        stdPal[14] = Color.cyan;
        stdPal[15] = Color.white;

        return stdPal;
    }

    /**
     * Returns a palette of 16 colours, 8 "low colour" and 8 "high colour"
     * @return an array of 16 different "standard" colours
     */
    private Color[] getFancyPalette() {
        Color[] fancyPal = new Color[16];
        if (DEBUG) {
            System.err.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("Fancy_palette_set_via_setFancyPalette()"));
        }
        // We will use the fancy palette technique
        fancyPal[0] = Color.black;
        fancyPal[1] = new Color(205, 0, 0);
        fancyPal[2] = new Color(110, 139, 61);
        fancyPal[3] = new Color(238, 238, 0);
        fancyPal[4] = new Color(0, 0, 129);
        fancyPal[5] = new Color(205, 10, 76);
        fancyPal[6] = new Color(122, 197, 205);
        fancyPal[7] = new Color(169, 169, 169);

        // upper colours
        fancyPal[8] = Color.gray;
        fancyPal[9] = Color.red;
        fancyPal[10] = Color.green;
        fancyPal[11] = Color.yellow;
        fancyPal[12] = Color.blue;
        fancyPal[13] = Color.magenta;
        fancyPal[14] = Color.cyan;
        fancyPal[15] = Color.white;

        return fancyPal;
    }

    /**
     * Return the index of the beginning of the selected text
     * @return the index of the beginning of the selected text
     */
    public int getSelectionStart() {
        return tPane.getSelectionStart();
    }

    /**
     * Return the index of the end of the selected text
     * @return The index of the end of the selected text
     */
    public int getSelectionEnd() {
        return tPane.getSelectionEnd();
    }

    /**
     * Sets the index for the beginning of a selected area
     * @param start index to begin the selection
     */
    public void setSelectionStart(final int start) {
        tPane.setSelectionStart(start);
    }

    /**
     * This method sets the end point of a selection
     * @param end the index to end the current selection
     */
    public void setSelectionEnd(final int end) {
        tPane.setSelectionEnd(end);
    }

    /**
     * Clear the text from the document.
     * This actually clears all text from the existing document.  We should
     * ensure this is the best solution.
     * Fix Me XXX
     */
    public synchronized void clearScreen() {
        // import javax.swing.text.Document;
        try {
            final javax.swing.text.Document tempDoc = tPane.getDocument();
            tempDoc.remove(0, tempDoc.getLength());
        } catch (Exception clearExc) {
            if (DEBUG) {
                System.err.println("JMSwingText.clearScreen error clearning document " + clearExc);
            }
        }


        // final int visRows = tPane.getVisibleRows();
//        final int visRows = tPane.getRows();
//
//
//
//        final StringBuffer enterStr = new StringBuffer();
//
//        for (int i = 0; i < visRows; i++) {
//            enterStr.append('\n');
//        }
//
//        this.append(enterStr.toString());
    }

    /**
     * This method enables the marking of web links, usually with an underline
     * @param status <code>true</code> Enable link marking (default)
     * <code>false</code> disable link marking
     */
    public void setMarkLinks(final boolean status) {
        markURLs = status;
    }

    /**
     * This method returns whether URL marking has been enabled or not.
     * @return <code>true</code> link marking is enabled
     * <code>false</code> link marking is disabled
     */
    public boolean isMarkLinks() {
        return markURLs;
    }

    /** 
     * old: This method checks the given input and will add underline tags if required
     * As of 2010-04-15 this method now converts the link to Hyperlink object
     * @param input The input string
     * @return The modified string containing any required underlining marks
     */
    private String applyLinkMark(final String input) {

        final StringBuffer retStr = new StringBuffer(input);
        int startPoint = 0;

//        while (retStr.indexOf("http", startPoint) > -1) {
//            final int linkStart = retStr.indexOf("http", startPoint);
//            retStr.insert(linkStart, ESCAPESTR + "[!");
//
//            int linkEnd = retStr.indexOf(" ", linkStart);
//
//            if (linkEnd == -1) {
//
//            }
//        }

        // If underlining is already occuring, then we don't need to stop it after
        if (!StyleConstants.isUnderline(attr)) {

            // Continue looping as long as web link indicators still exist
            while (retStr.indexOf("http", startPoint) > -1) {
                // Mark start of underline
                final int linkStart = retStr.indexOf("http", startPoint);

                // if (linkStart > 1 && retStr.charAt(linkStart - 3) == ESCAPECHAR) {
                if (linkStart > 2 && retStr.charAt(linkStart - 3) == ESCAPECHAR) {
                    // This link is already marked.  Advance one character
                    startPoint++;
                } else {
                    // Add the beginning of the link
                    retStr.insert(linkStart, '\u001b' + "[!");

                    // Mark end of underline (with-out cancelling out other colour marks
                    int linkEnd = retStr.indexOf(" ", linkStart);

                    if (linkEnd == -1) {
                        // We can't find the next space in the line, so simply underline
                        // to the end of the input.
                        if (DEBUG) {
                            System.err.println("JMSwingText.applyLinkMark() can't find the ending space.");
                        }
                        linkEnd = retStr.length();
                    }

                    // We'll have to create our own code here to only cancel the underline
                    // retStr.insert(linkEnd, '\u001b' + "[-4m");
                    retStr.insert(linkEnd, ']');

                    if (DEBUG) {
                        System.err.println("JMSwingText.applyLinkMark has added: " + retStr.substring(linkStart, linkEnd));
                    }

                    startPoint = linkEnd;

                }

            }
        }

        return retStr.toString();
    }

    /**
     * This method checks to see if the given string contains ESCAPE character
     * indicating an escape string that should be fully parsed before displaying 
     * @param inStr The string to check for escape characters
     * @return <code>true</code> - The given string does contain an escape sequence. 
     * <code>false</code> - The given string does not contain an escape sequence.
     */
    public boolean containsEscape(final String inStr) {
        boolean retVal = false;

        // String.contains is Java 1.5 ad negates this class being used by OS/2
        // if (inStr != null && inStr.contains(ESCAPESTR)) {
        if (inStr != null && inStr.indexOf(ESCAPESTR) > -1) {
            retVal = true;
        }

        return retVal;
    }

//    /**
//     * This method writes out any buffered text and resets the
//     * buffering state
//     */
//    private synchronized void displayBufferedEscape() {
//        if (DEBUG) {
//            System.err.println("JMSwingText.displayBufferedEscape entered.");
//        }
//
//        // Write out the buffer
//        writeOut(bufferedText.toString());
//        // Empty the buffer text
//        bufferedText.setLength(0);
//        // Reset the buffer state
//        bufferingEscape = false;
//    }
    /**
     * Returns the string that is currently held in the "escape" buffer
     * @return A string of the escape-buffer contents
     */
    private synchronized String getBufferedEscape() {
        final String buffText = bufferedText.toString();
        // Empty the buffer text
        bufferedText.setLength(0);
        // Reset the buffer state
        bufferingEscape = false;

        return buffText;
    }

    /**
     * Incoming text will be passed through this method to check to see if it
     * should be buffered or displayed
     * @param inStr
     */
    public synchronized void append(String inStr) {

        if (bufferingEscape) {
            if (DEBUG) {
                System.err.println("JMSwingText.append() already has buffered text, so we'll see how to deal with it.");
            }

            if (inStr.indexOf('m') == -1) {
                // No closing escape yet, so buffer this?
                simpleBufferString(inStr);
                return;
            } else {
                // We have a closing escape, so we'll write out first
                inStr = getBufferedEscape() + inStr;
                if (DEBUG) {
                    System.err.println("JMSwingText.append() adding buffered escape to incoming string.");
                }
            }
        }

        final String chunks[] = TextUtils.splitter(inStr, ESCAPESTR);
        final int len = chunks.length;
        final int shortLen = len - 1;

        if (DEBUG) {
            System.err.println("JMSwingText.append() has " + shortLen + " shortLen chunks and " + len + " standard chunks.");
        }

        for (int i = 0; i < shortLen; i++) {
            if (DEBUG) {
                System.err.println("->" + chunks[i] + "<-");
            }

            writeOut(chunks[i]);
        }

        // We only need to check the last chunk
        if (containsEscape(chunks[shortLen])) {
            // Check for a complete escape
            if (DEBUG) {
                System.err.println("JMSwingText.append() Going to parse: " + chunks[shortLen]);
            }

            if (containsCompleteEscape(chunks[shortLen])) {
                // This chunk has a valid escape, so we can write it out
                if (DEBUG) {
                    System.err.println("JMSwingText.append() thinks we have a valid string.");
                }
                writeOut(chunks[shortLen]);
            } else {
                if (DEBUG) {
                    System.err.println("JMSwingText.append() going to buffer output.");
                }
                // Buffer the incomplete escape
                simpleBufferString(chunks[shortLen]);

            }
        } else {
            writeOut(chunks[shortLen]);
        }

    }

    /** This method parses apart the given string, deciding if any can be returned
     * as "clean" and the remainder will be buffered in hopes of receiving 
     * an end-of-escape character
     * @param inStr
     * @return
     */
    private String simpleBufferString(final String inStr) {
        String retStr;

        final int escLoc = inStr.indexOf(ESCAPECHAR);

        if (escLoc > 0) {
            // Return the string before 
            retStr = inStr.substring(0, escLoc);
            bufferedText.append(inStr.substring(escLoc));
        } else {
            retStr = "";
            bufferedText.append(inStr);
        }


        if (DEBUG) {
            System.err.println("JMSwingText.simpleBufferString:");
            System.err.println("  retStr: " + retStr);
            if (escLoc > 0) {
                System.err.println("  Remainder to buffer: " + inStr.substring(escLoc));
            } else {
                System.err.println("  Total string to buffer: " + inStr);
            }
        }

        bufferingEscape = true;

        return retStr;
    }

    /**
     * This method checks to see if the given String contains a complete escape.
     * If the String is not long enough for a complete escape, or does not
     * contain an entire escape but no other characters it will be returned false.
     * Otherwise it will return true.  An invalid escape will also be considered "complete"
     * and be passed directly to output for parsing.
     * @param inStr The String to be checked for complete escapes
     * @return <true>The given String has a complete or invalid escape 
     * <false>The escape is not complete but it is also the end of the String
     */
    private boolean containsCompleteEscape(final String inStr) {
        boolean retVal = true;
        final String lowerStr = inStr.toLowerCase();

        final int startSpot = inStr.indexOf(ESCAPECHAR);
        final int endSpot = lowerStr.indexOf('m', startSpot);

        if (endSpot == -1) {
            // This may be a short escape, so do some additional checks
            if (startSpot > -1) {
                if (!inStr.substring(startSpot).startsWith(ESCAPECHAR + "[J")) {
                    retVal = false;
                }
            } else {
                retVal = false;
            }
        }

        // Search string between start and end looking for illegal characters
        for (int i = startSpot; i < endSpot; i++) {
        }

        if (DEBUG) {
            System.err.println("JMSwingText.containsCompleteEscape returns " + retVal);
        }

        return retVal;
    }

    /** This method will return all the escape tokens and strip off the leading
     * escape characters and the ending string provided.
     *
     * @param orig The token string to be parsed
     * @param endStr
     * @return An array of Strings containing the escape tokens
     */
    public String[] escapeTokens(final String orig, final String endStr) {
        // Clean up the input

        int start;
        int end;

        // Remove the leading escape if present
        if (orig.contains(ESCAPESTR)) {
            start = orig.indexOf(ESCAPECHAR) + 2;
        } else {
            start = 0;
        }

        // Remove the trailing endStr is present
        if (orig.contains(endStr)) {
            end = orig.indexOf(endStr);
        } else {
            end = orig.length();
        }

        final String cleanStr = orig.substring(start, end);

        if (DEBUG) {
            System.err.println("JMSwingText.escapeTokens cleaned text: " + cleanStr);
        }

        final String[] retTokens = cleanStr.split(";");

        if (DEBUG) {
            System.err.println("JMSwingText.escapeTokens count: " + retTokens.length);
        }

        return retTokens;
    }

    private void createLink(final String address, final String text, final String hint, final String linkName) {
        final anecho.gui.Hyperlink link = new anecho.gui.Hyperlink(address, text, hint, linkName);

        // Generate a uniqe style name for this item
        // String styleName = "Unique placeholder name";
        String styleName;

        if (linkName == null) {
            styleName = address;
        } else {
            styleName = linkName;
        }

        // We create a link as a style and then add it to our document
        final Style linkStyle = textDoc.addStyle(styleName, tPane.getLogicalStyle());
        StyleConstants.setUnderline(linkStyle, true);
        StyleConstants.setItalic(linkStyle, true);
        StyleConstants.setUnderline(linkStyle, true);
        StyleConstants.setForeground(linkStyle, new Color(0, 0, 255));

        link.setFont(new Font(StyleConstants.getFontFamily(linkStyle), Font.PLAIN, StyleConstants.getFontSize(linkStyle)));

        link.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));

        link.addMouseListener(this);

        StyleConstants.setComponent(linkStyle, link);

        final int totalLength = textDoc.getLength();

        try {
            textDoc.insertString(totalLength, address, linkStyle);

            if (DEBUG) {
                System.err.println("JMSwingText.createLink() insertString:");
                System.err.println("totalLength: " + totalLength);
                System.err.println("address: " + address);
                System.err.println("Style: " + linkStyle);
            }
        } catch (Exception exc) {
            if (DEBUG) {
                System.err.println("JMSwingText.createLink() exception " + exc);
            }
        }

    }

    /** Create a link based on the supplied tokens
     * 
     * @param tokens
     */
    // public String[] linkFromArray(String[] tokens) {
    public void linkFromArray(String[] tokens) {
        String linkName = null;
        String linkAddress = null;
        String linkHint = null;
        String linkText = null;

        final int total = tokens.length;

        // Clean up the tokens first
        for (int i = 0; i < total; i++) {
            if (tokens[i].startsWith("!")) {
                tokens[i] = tokens[i].substring(1);
            }
        }

        if (DEBUG) {
            System.err.println("JMSwingText.linkFromArray has " + total + " tokens");
        }

        if (total > 0) {
            // Assign the address
            linkAddress = tokens[0];
        }

        if (total > 1) {
            linkText = tokens[1];
        }

        if (total > 2) {
            linkHint = tokens[2];
        }

        if (total > 3) {
            linkName = tokens[3];
        }

        int extraLen = 0;

        if (total > 4) {
            extraLen = total - 4;
        }

        final String[] extraInfo = new String[extraLen];

        if (total > 4) {
            // Save the rest as extra info as well, just in case
            System.arraycopy(tokens, 4, extraInfo, 0, extraLen);
        }

        if (DEBUG) {
            System.err.println("JMSwingText.linkFromArray has come up with:");
            System.err.println("Address: " + linkAddress);
            System.err.println("Text: " + linkText);
            System.err.println("Hint: " + linkHint);
            System.err.println("linkName: " + linkName);
        }

        createLink(linkAddress, linkText, linkHint, linkName);
        // return new String[]{linkAddress, linkText, linkHint, linkName};
    }
}
