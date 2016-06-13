/**
 * JMText, a multi-colour text-area for Java 1.1 (originally developed for JamochaMUD)
 * $Id: JMText.java,v 1.10 2014/05/20 02:18:31 jeffnik Exp $
 */

/* JMText, a multi-colour TextArea for Java 1.1
 * Copyright (C) 1999-2010 Jeff Robinson
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
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

import java.awt.*;
import java.awt.event.*;

// import java.lang.Integer;

import java.util.Vector;
import java.util.StringTokenizer;
// import javax.swing.text.SimpleAttributeSet;

/** A widget to display multicoloured text in a Java 1.1 environment
 * with an effort made to clone the TextArea API as close as possible.
 * @version $Id: JMText.java,v 1.10 2014/05/20 02:18:31 jeffnik Exp $
 * @author Jeff Robinson
 */
public class JMText extends Container implements AdjustmentListener, KeyListener, MouseListener, MouseMotionListener, ComponentListener, FocusListener {
// public class JMText extends javax.swing.JComponent implements AdjustmentListener, KeyListener, MouseListener, MouseMotionListener, ComponentListener, FocusListener {
    
    // Variables
    /**
     * JMSwingTextPane, a antialiasing TextPane
     */
    public static final int SCROLLBARS_BOTH = 0;
    
    /**
     * Show only vertical scroll bars
     */
    public static final int SCROLLBARS_VERTICAL_ONLY = 1;
    
    /**
     * Do not show scroll bars
     */
    public static final int SCROLLBARS_HORIZONTAL_ONLY = 2;
    
    /**
     * Show only horizontal scroll bars
     */
    public static final int SCROLLBARS_NONE = 3;
    
    // Testing new methods
    private static final boolean SMART_ARRAY = true;
    private static final boolean SMART_MARK = false;
    
    private static final String FGMARK = java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("fg:");
    
    private LittleCanvas drawText;
    // private Graphics graphObj;             // The "graphics" used to display the characters
    private Graphics display;             // The "graphics" used to display the characters
    private Image offScreenImg;     // Our off-screen image for double-buffering
    private Color bgColour, fgColour;  // colours we'll use for our foreground/background
    private char[][] textChars;	// The actual text (characters) received
    private String[][] markings;	// The mark-ups for the associated characters in 'text'
    private boolean minimised = true;	// Whether this component is minimized or not
    // Starting "true" forces us to calculate the size
    private boolean softReturn = false;
    private StringTokenizer workString;       // for parsing of tokens in append(str)
    private Dimension widgetSize = new Dimension(0, 0);	// A size to be used when minimized
    private boolean notified = false;         // Help to determine if we have an 'object' that
    // we can query for information
    // private boolean sizeSet = false;
    private boolean doubleBuffer = true;      // Double-buffer output graphics (smoother
    // draw but slower)
    private int columns, rows;	// The number of rows and columns in the JMText area
    private int prevCols = 0;       // Used for tracking changes against new columns
    private int prevRows = 0;       // Used for tracking changes against new rows
    private int maxRows = 6000;	// The maximum number of rows available at once
    private int halfMax = 2500;	// The amount of rows we send to history at once
    private Scrollbar vertScroll;	// Vertical scrollbar
    // private AdjustmentListener vertListener;	// Vertical scrollbar Listener
    private Scrollbar horzScroll;	// Horizontal scrollbar
    private BorderLayout layout;	// The layout manager for this component
    // private int viewBase;		// The 'base' line that is viewable to the user
    private transient Vector pendingText = new Vector(0, 1);	// a vector for text that can't be
    // presented at the time of receipt
    private transient final Vector historyText = new Vector(0, 1);	// This vector will keep all text
    // that is 'spooled off' the active
    // text Array.
    private transient String currentAttrib = java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("");	// Keeps track of the "active" character attributes
    private transient Point cursorPos;		// The current cursor position
    private static Point selectBegin = new Point(0, 0);	// Starting mouse position (for selections)
    private static Point selectEnd = new Point(0, 0);	// End point for mouse selections
    private transient Point textBegin = new Point(0, 0);	// Actual text selected
    private transient Point textEnd = new Point(0, 0);	// End point of actual text
    
    // private static final char BELL = '7';
    private static final String RESET = java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("reset");    // Reset display colours
    private static final boolean DEBUG = false;  // Enable or disable debugging features
    
    private transient KeyListener keyL;		// A keylistener that can 'talk' to other classes
    private transient TextListener textL;		// A textlistener
    private transient MouseListener mListener;	// A mouselistener
    
    // private boolean paintCall = false;
    // private JMTextEffects textEffect;
    // private FocusListener fListener;
    
    // Constructor Methods
    /**
     * JMText, a multi-colour text-area for Java 1.1
     * (originally developed for JamochaMUD)
     */
    public JMText() {
        this(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString(""), -1, -1);
    }
    
    /**
     * JMText, a multi-colour text-area for Java 1.1
     * (originally developed for JamochaMUD)
     * @param text Text to be displayed after intial component creation
     */
    public JMText(String text) {
        this(text, -1, -1);
    }
    
    /**
     * JMText, a multi-colour text-area for Java 1.1
     * (originally developed for JamochaMUD)
     * @param rows Number of rows to be visible in the component
     * @param columns Number of columns to be visible in the component
     */
    public JMText(int rows, int columns) {
        this(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString(""), rows, columns);
    }
    
    /**
     * JMText, a multi-colour text-area for Java 1.1
     * (originally developed for JamochaMUD)
     * @param text Text to initially be displayed when this component is created.
     * @param rows Number of rows to be visible in the component
     * @param columns Number of columns to be visible in the component
     */
    public JMText(String text, int rows, int columns) {
        this(text, rows, columns, SCROLLBARS_NONE);
    }
    
    /**
     * JMText, a multi-colour text-area for Java 1.1
     * (originally developed for JamochaMUD)
     * @param text Text to initially be displayed after creation of this component
     * @param rows Number of rows in this component
     * @param columns Number of columns in this component
     * @param scrollbars Indicate whether scrollbars should be visible in this component
     */
    public JMText(String text, int rows, int columns, int scrollbars) {
        // set up the variables and then add the components
        layout = new BorderLayout();
        setLayout(layout);
        
        drawText = new LittleCanvas();
        // drawText = new LittleCanvas(this);
        add(drawText, BorderLayout.CENTER);
        drawText.addKeyListener(this);
        drawText.addComponentListener(this);		// This listener catches updates
        drawText.addFocusListener(this);
        drawText.addMouseListener(this);
        drawText.addMouseMotionListener(this);
        
        // Check to see if scrollbars are needed
        switch (scrollbars) {
            case SCROLLBARS_VERTICAL_ONLY: {
                vertScroll = new Scrollbar(Scrollbar.VERTICAL);
                vertScroll.addAdjustmentListener(this);
                add(vertScroll, BorderLayout.EAST);
                break;
            }
            case SCROLLBARS_HORIZONTAL_ONLY: {
                horzScroll = new Scrollbar(Scrollbar.HORIZONTAL);
                add(horzScroll, BorderLayout.SOUTH);
                break;
            }
            case SCROLLBARS_BOTH: {
                vertScroll = new Scrollbar(Scrollbar.VERTICAL);
                add(vertScroll, BorderLayout.EAST);
                horzScroll = new Scrollbar(Scrollbar.HORIZONTAL);
                add(horzScroll, BorderLayout.SOUTH);
                break;
            }
            default: {
                // We have no scrollbars or we don't know what we're being asked for!
                break;
            }
            
        }
        
        // We'll store the rows and columns for now.  If the user hasn't specified
        // their size, then we can't calculate it until AddNotify is called
        this.rows = rows;
        this.columns = columns;
        
        if (text != null && !text.equals(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString(""))) {
            // System.out.println("JMText constructor pendingText adds: " + text);
            pendingText.addElement(text);
        }
        
        // textEffect = JMTextEffects.getInstance();
    }
    
    /**

     * Set the maximum number of rows to be shown in the component.

     * @param setMax Number of rows to be shown.

     */

    public synchronized void setMaxRows(final int setMax) {

        if (!notified && setMax > 0) {

            maxRows = setMax;

            // System.out.println("maxRows set to " + setMax);

        }

    }

    /** This method is used to initialise all the variables in JMText

     * that could not be done when it was initially created.

     * This method is also helpful when an existing JMText area needs

     * to be cleared for use again.

     */

    private synchronized void initializeJMText() {

        

        // System.out.println("JMText.initializeJMText() calling checkNewRowsSame()");

        // if (checkNewRowsSame()) {

        // return;

        // }

        if (!checkNewRowsSame()) {

            

            // System.out.println("JMText.initializeJMText()... minimised: " + minimised);

            // We check to see if JMText is minimised after we call "checkNewRowsSame" as

            // the previous check can actually reset out minimised variable.  Minimised

            // May sometimes mistakenly be set to false by system calls that make it appear

            // that the JMText widget has been made visible.

            if (minimised) {

                // System.out.println("initializeJMText() called, but we're minimized.");

                return;

            }

            

            // System.out.println("JMText.initializeJMText() set notified = true");

            notified = true;	// We don't want to accidentally re-initialise the variables

            currentAttrib = java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("");     // make sure no attributes were left over from a previous session

            

            // Set up our character arrays

            // System.out.println("setCharacterArrays()");

            setCharacterArrays();

            

            // Set up the scrollbars

            // System.out.println("resetScrollbars()");

            resetScrollbars();

            

            cursorPos = new Point(0, 0);		// set the cursor to its starting position

            

            // If there is any text pending, we will write it out now

            // System.out.println("Write out pending text.");

            if (!pendingText.isEmpty()) {

                // System.out.println("JMText.initializeJMText Our pending text is " + pendingText.size() + " lines.");

                final int pTextSize = pendingText.size();

                // for (int index = 0; index < pendingText.size(); index++) {

                for (int i = 0; i < pTextSize; i++) {

                    // System.out.println(index + "/" + pendingText.size() + " " + pendingText.elementAt(index).toString());

                    append(pendingText.elementAt(i).toString());

                }

                // System.out.println("Clear the pending text Vector.");

                pendingText.removeAllElements();	// Make sure the vector is empty

            }

            

            // System.out.println("Remove history text.");

            if (historyText != null) {

                historyText.removeAllElements();	// Empty the history text vector

            }

        }

    }

    

    /** Set the proper rows and columns for this page.  Admittedly, this is ugly. */

    private synchronized void getRowsAndColumns() {
        Dimension size = getSize();
        final String test =java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("l");
        int tempRows, tempCols;
        int total = 0;
        int fontWidth = 0;
        final FontMetrics fMetric = getFontMetrics(getFont());
        
        try {
            total = fMetric.stringWidth(test);
        } catch (Exception exc) {
            System.out.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("JMText_exception_getting_average_character_width"));
        }
        
        fontWidth = (int)(total /= test.length());
        
        if (rows > 0 && size.width == 0 && size.height == 0) {
            // Set the size and width via the rows and columns supplied
            setSize(columns * fontWidth, fMetric.getHeight() * rows);
        } else {
            if (rows < 1 && size.width == 0 && size.height == 0) {
                size = this.getParent().getSize();
            }
            tempRows = (int)size.height / fMetric.getHeight();
            tempCols = (int)size.width / fontWidth;
            
            // System.out.println("tempRows: " + tempRows + " tempColumns: " + tempCols);
            
            if (tempRows <= 1 || tempCols <= 1) {
                // For some reason our rows and columns don't stack up
                // We're going to assume that we're minimised
                // System.out.println("JMText.getRowsAndColumns() set minimised = true");
                minimised = true;
                return;
            }
            
            prevRows = rows;
            prevCols = columns;
            // rows = tempRows;
            rows = tempRows - 1;
            columns = tempCols;
            
        }
        
        // Now we can set up the character arrays
        widgetSize.setSize(this.getSize());
    }

    

    /** Append the String to the JMText,

     * @param   str       The string to be added to the JMText

     */

    public synchronized void append(final String str) {

        append(str, true);

    }

    

    /** Append to the String to the JMText.  This method is considered

     * <tt>private</tt> as only JMText should really know best when

     * new information should be displayed.  Of course, if I'm wrong then

     * this is open source and someone can go make this method <tt>public</tt>!

     * @param   str       The string to be added to the JMText

     * @param   refresh   redraw the screen content

     */

    private synchronized void append(final String str, final boolean refresh) {

        if (str == null || str.equals(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString(""))) {

            return;

        }

        

        if (DEBUG) {

            System.out.print(str);

        }

        

        getRowsAndColumns();  // Experimentally moved from the later 'if' statement XXX

        

        if (!notified || minimised) {

            // This object has no size right now, so save this String

            pendingText.addElement(str);

            return;

        }

        

        // Experimental line-wrap code ***WARNING***

        // First, break the string into tokens

        workString = new StringTokenizer(str, java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("_"), true);

        

        final FontMetrics fontWidth = getFontMetrics(drawText.getFont());

        int displayWidth = fontWidth.charsWidth(textChars[cursorPos.y], 0, cursorPos.x);

        final int appendWidth = getSize().width;

        int tokenLength;

        int pelWidth = 0;

        int len = 0;

        // boolean loop = true;

        String token, strippedToken;

        

        while (workString.hasMoreTokens()) {

            token = workString.nextToken();

            // strippedToken = stripEscapes(token);

            strippedToken = anecho.gui.TextUtils.stripEscapes(token, true);

            

            // We now have the token we want to send out...

            len = strippedToken.length();

            pelWidth = fontWidth.stringWidth(strippedToken.trim() + java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("_"));

            

            // now evaluate the length of the token, and see if it

            // will fit on the current line.  If not, send a 'new-line'

            // if ((displayWidth + pelWidth) > appendWidth || (len + cursorPos.x) > columns - 2) {

            if (((displayWidth + pelWidth) > appendWidth || (len + cursorPos.x) > columns - 2) && pelWidth < appendWidth) {

                // Check to see if the token is too large to fit on a line.

                // If it is, we'll let it slip through to be broken up.

                // if (pelWidth < appendWidth) {

                cursorPos.setLocation(0, cursorPos.y + 1);	// Go to next line

                scrollDown();

                displayWidth = 0;

                // }

            }

            

            displayWidth += pelWidth;

            tokenLength = token.length();

            // tokenLength = strippedToken.length();

            // for(int index = 0; index < token.length(); index++) {

            // for(int index = 0; index < tokenLength; index++) {

            for(int i = 0; i < tokenLength; i++) {

                // Check to see if this is an ANSI escape

                while (token.charAt(i) == '\u001b') {

                    i = checkEscape(token, i);

                    // if ( index >= token.length()) {

                    if ( i >= tokenLength) {

                        break;

                    }

                }

                

                // write the token to the screen

                // if (index < token.length()) {

                if (i < tokenLength) {

                    // This "try" statement was looking for null characters

                    // but we shouldn't have to do this anymore, as we do some

                    // checking prior to this

                    appendChar(token.charAt(i), currentAttrib);

                    //appendChar(strippedToken.charAt(index), currentAttrib);

                }

                

            }

        }

        

        // Refresh only if required... this is helpful if we have to do a lot of consecutive 'appends'

        if (refresh) {

            repaint();

        }

        

    }

    

    /** Append characters to JMText, one at a time, including the current attribute

     * @param jchar       the single <tt>char</tt> to be added

     * @param attrib      a &quot;description" of the attributes of the character

     */

    public void appendChar(final char jchar, final String attrib) {

        final FontMetrics fMetric = getFontMetrics(getFont());

        int width = fMetric.charsWidth(textChars[cursorPos.y], 0, cursorPos.x);

        width += fMetric.charWidth(jchar);

        

        if (width >= getSize().width || cursorPos.x >= columns) {

            cursorPos.setLocation(0, cursorPos.y + 1);

            softReturn = true;

            scrollDown();

            

            if (cursorPos.x >= columns) {

                System.out.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("Return:_Too_wide_or_ran_out_of_columns_at:_") + jchar);

            }

            // else {

            // System.out.println("Return: Too wide for screen at: " + jchar);

            // }

        }

        

        textChars[cursorPos.y][cursorPos.x] = jchar;

        markings[cursorPos.y][cursorPos.x] = attrib;

        

        if (jchar == '\n') {

            if (!softReturn) {

                cursorPos.setLocation(0, cursorPos.y + 1);

                scrollDown();	// Check to see if we need to scroll down

                // No need to update the screen on a newline

            }

            return;

        }

        

        cursorPos.setLocation(cursorPos.x + 1, cursorPos.y);

        

        // Notify our text-listener that something has happened

        fauxTextListener();

        softReturn = false;

        

    }

    

    /**

     * The paint method will be over-ridden to give us complete

     * control over this event

     * @param graphicItem The Graphic object that we will draw on.

     */

    public void paint(final Graphics graphicItem) {

        // drawTextArea(graphicItem);

        // System.out.println("JMText.paint");

        // super.paint(graphicItem);

        // }

        

        // public void paintComponent(Graphics graphicItem) {

        // System.out.println("JMText.paintComponent");

        if (DEBUG) {

            System.err.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("JMText.paint()_notified:_") + notified);

        }

        

        if (!notified) {

            // System.out.println("Not notified yet.");

            return;

        }

        

        // if (!this.isVisible()) {

        // System.out.println("This component isn't visible right now, sorry");

        // }

        

        // System.out.println("jMComponentResized() called from paint()");

        jMComponentResized();

        

        final int tWidth = drawText.getSize().width;

        final int tHeight = drawText.getSize().height;

        if (tWidth < 1 || tHeight < 1) {

            return;          // we're not visible!

        }

        

        bgColour = getBackground();

        fgColour = getForeground();

        offScreenImg = createImage(tWidth, tHeight);

        

        if (doubleBuffer) {

            // We're using double-buffering.  It removes the "flicker" but

            // redraws somewhat slower

            display = offScreenImg.getGraphics();

            display.setFont(getFont());

            display.setColor(bgColour);

            display.fillRect(0, 0, tWidth, tHeight);

        } else {

            // No double-buffering.  The display "flickers" more

            // but is redrawn faster than the double-buffered type

            display = drawText.getGraphics();

        }

        

        int lineWidth;		// Keep track of the line width instead of recalculating

        int lineHeight;					// Height of the line

        final FontMetrics fMetric = getFontMetrics(getFont());	// Fontmetrics for this character

        final int charHeight = fMetric.getHeight();		// Height of this character

        final int charDescent = fMetric.getDescent();

        

        // Only display the lines which may be visible for faster refreshes

        int baseLine = 0;	// the 'top' line of text to view

        if (vertScroll != null && vertScroll.getValue() >= 0) {

            // baseLine = vertScroll.getValue();

            baseLine = vertScroll.getValue();

        }

        

        int endRead = 0;

        // if (cursorPos.clickY + 1 < rows) {

        if (cursorPos.y < rows) {

            endRead = cursorPos.y;

        } else {

            endRead = baseLine + rows;

        }

        

        // Kludgily, we loop through what *should* be visible

        char tChar;   // the variable we'll be checking

        String tLine; // Our entire line of text

        String tMarks; // Formatting/colour information for our current character

        String lineMarks[];

        String oldMarks = java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("");  // Formatting/colour information for our previous character

        int tLength;

        int fgPos;

        boolean style = false;  // Keeps track of our "styles" such as bold, italic, etc.

        boolean bold = false;

        Color restoreColour = fgColour;     // If we need to alter the colour, this maintains the original value

        final Font oldFont = display.getFont();



        if (DEBUG) {

            System.err.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("JMText.paint():"));

            System.err.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("baseLine:_") + baseLine);

            System.err.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("endRead:_") + endRead);

        }

        

        for (int y = baseLine; y <= endRead; y++) {

            // By dumping the multi-dimensional array to a string

            // we can save a lot of overhead 'cause we don't have

            // to keep checking the array.

            tLine = new String(textChars[y]);  // By not having to check an array

            // tLine = textChars[y].toString();

            tLength = tLine.length();

            lineMarks = markings[y];

            

            lineWidth = 0;

            lineHeight = (y - baseLine) * charHeight;

            

            // Clear the entire line

            display.setColor(bgColour);

            display.fillRect(0, lineHeight + charDescent, tWidth, charHeight);

            

            // Change our writing colour back to the foreground colour by default

            display.setColor(fgColour);

            

            for (int x = 0; x < tLength; x++) {

                tChar = tLine.charAt(x);

                // tMarks = markings[clickY][clickX];

                tMarks = lineMarks[x];

                

                if (tMarks == null) {

                    break;	// No characters here!

                }

                

                if (tChar == '\n') {

                    break;

                }

                // Now draw the character

                fgPos = tMarks.indexOf(FGMARK);

                

                // Only calculate new colours if we have to, such as if we

                // start a new line or if the current attributes are different

                // than the character previous

                // if (clickX == 0 || !tMarks.equals(markings[clickY][clickX - 1])) {

                if (!tMarks.equals(oldMarks) || x == 0) {

                    if (fgPos >= 0) {

                        // We'll set the colour from the attrib

                        display.setColor(calculateColour(tMarks, fgPos));

                    } else {

                        display.setColor(fgColour);

                    }

                }

                

                if (tMarks.indexOf(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("<bold>")) > -1) {

                    if (oldFont.getStyle() == Font.BOLD || bgColour.equals(display.getColor())) {

                        /* Highlight the colour if the font is already bold */

                        restoreColour = display.getColor();

                        display.setColor(brighter(restoreColour));

                        style = true;

                    }

                    bold = true;

                    display.setFont(new Font(oldFont.getName(), Font.BOLD, oldFont.getSize()));

                }

                

                if (tMarks.indexOf(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("<i>")) > -1) {

                    if (oldFont.getStyle() == Font.ITALIC) {

                        /* Highlight the colour if the font is already italicized */

                        restoreColour = display.getColor();

                        display.setColor(brighter(restoreColour));

                        style = true;

                    }

                    bold = true;

                    display.setFont(new Font(oldFont.getName(), Font.ITALIC, oldFont.getSize()));

                }

                

                if (tMarks.indexOf(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("<faint>")) > -1) {

                    restoreColour = display.getColor();

                    display.setColor(darker(restoreColour));

                    style = true;

                }

                

                if (tMarks.indexOf(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("<negative>")) > -1) {

                    restoreColour = display.getColor();

                    // display.setColor(background());

                    display.fillRect(lineWidth,

                            lineHeight + charDescent,

                            fMetric.charWidth(tChar),

                            charHeight);

                    display.setColor(getBackground());

                    style = true;

                }

                

                display.drawString(tChar + java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString(""), lineWidth, lineHeight + charHeight);

        

                if (tMarks.indexOf(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("<u>")) > -1) {

                    // Underline the current character

                    display.drawLine(lineWidth, lineHeight + charHeight + 2, lineWidth + fMetric.charWidth(tChar), lineHeight + charHeight + 2);

                }

                

                if (tMarks.indexOf(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("<s>")) > -1) {

                    // Strike-through the character

                    display.drawLine(lineWidth, lineHeight + ((charHeight + charDescent) / 2), lineWidth + fMetric.charWidth(tChar), lineHeight + ((charHeight + charDescent) / 2));

                }

                

                // Try the singleton method

                // textEffect.drawLines(tChar, tMarks, display, lineWidth, lineHeight, fMetric.charWidth(tChar), charHeight, charDescent);

                

                lineWidth += fMetric.charWidth(tChar);

                

                oldMarks = tMarks;

                

                if (style) {

                    display.setColor(restoreColour);

                    style = false;

                    

                }

                

                if (bold) {

                    bold = false;

                    display.setFont(oldFont);

                }

            }

            

            // Now we clear the remainder of the line

            // if (cursorPos.clickY - baseLine < rows) {

            // display.setColor(bgColour);

            // }

            

        }

        

        // Now we'll redraw any selections if they exist

        if (selectBegin.x != selectEnd.x && selectBegin.y != selectEnd.y) {

            paintSelection(selectBegin, selectEnd, bgColour);

        }

        

        if (doubleBuffer) {

            // Double buffer must draw the graphics onto the active area now

            final Graphics onScreen = drawText.getGraphics();

            onScreen.drawImage(offScreenImg, 0, 0, null);

        }

        

        super.paint(graphicItem);

        

    }

    

    /**
     * This method passes to call to update the graphics on to our super-class.
     * @param graphItem 
     */

    public void update(final Graphics graphItem) {

        System.out.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("update()_->_paint()_"));

        paint(graphItem);

    }

    

    // Handle scrollbar methods

    /**
     * Monitor the scroll-bar adjustment values to see if "scrolling" of
     * the text is required
     * @param event 
     */

    public void adjustmentValueChanged(final AdjustmentEvent event) {

        // Scrollbar has been adjusted

        // System.out.print("adjustmentValueChanged() -> paint() ");

        repaint();

    }

    

    /** Bump the scrollbar down one notch if necessary (scrolling the 'screen' up) */

    private void scrollDown() {

        // Now we'll also check to see if we're getting too much text for this array.

        // If this is the case, we'll write some of the lines to historyText and chop

        // them off the top of the array.  They may appear to be 'gone' from the viewer

        // but if a text-dump is done, they will still be included.

        if (cursorPos.y > maxRows - 2) {

            // Write the strings out to the historyText vector

            String work;

            // StringBuffer tempString = new StringBuffer("");

            // int returnColumns = textChars[0].length;

            

            

            for (int y = 0; y < halfMax; y++) {

                work = java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("");

                try {

                    work = textChars[y] + java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("");

                } catch (Exception e) {

                    System.out.println(textChars);

                    System.out.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("Error_in_JMText.ScrollDown_historyText_") + e);

                    // System.exit(0);

                    return;

                }

                try {

                    historyText.addElement(work.substring(0, work.lastIndexOf('\n') + 1));

                } catch (Exception e) {

                    System.out.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("Error_in_Scrolldown_") + e);

                }

            }

            

            // We'll copy these into temporary arrays before erasing the old

            final char tempChars[][] = new char[cursorPos.y + 1][columns];

            System.arraycopy(textChars, halfMax, tempChars, 0, cursorPos.y - halfMax);

            textChars = new char[maxRows][columns];

            System.arraycopy(tempChars, 0, textChars, 0, cursorPos.y - halfMax);

            

            // copy the markings

            final String tempMarkings[][] = new String[cursorPos.y + 1][columns];

            System.arraycopy(markings, halfMax, tempMarkings, 0, cursorPos.y - halfMax);

            markings = new String[maxRows][columns];

            System.arraycopy(tempMarkings, 0, markings, 0, cursorPos.y - halfMax);

            

            // Set the new cursor position and scrollbar information

            cursorPos.setLocation(cursorPos.x, cursorPos.y - halfMax);

            

        }

        

        if (vertScroll != null && cursorPos.y + 1> rows) {

            if (cursorPos.y + 1 > rows) {

                vertScroll.setMaximum((cursorPos.y + 1) - rows);

                vertScroll.setValue(vertScroll.getMaximum());

            }

            // System.out.println("Scrolldown() -> paint()");

            repaint();

        }

        

    }

    

    /** Set the text in JMText to whatever we are sent in the string,

     * erasing everything else

     * @param str         Text to set in the cleared JMText field.

     * A <tt>null</tt> will leave the JMText area blank.

     */

    public synchronized void setText(final String str) {

        // first, check it the JMText area is valid yet

        if (!notified) {

            // We don't have an actual JMText are yet, so we'll erase

            // any previously stored lines, and set this one as the new one

            pendingText.removeAllElements();

            // System.out.println("JMText.setText adding pending: " + str);

            pendingText.addElement(str);

            

            return;		// we can't really do any more right now

        }

        

        // now we'll erase anything that may be on the widget's text area right now

        final Graphics graphObj = drawText.getGraphics();

        graphObj.setColor(drawText.getBackground());

        graphObj.fillRect(0, 0, drawText.getSize().width, drawText.getSize().height);

        

        // Set up our character arrays

        setCharacterArrays();

        

        // Set up the scrollbars

        resetScrollbars();

        

        cursorPos = new Point(0, 0);		// set the cursor to its starting position

        

        if (str !=null && !str.equals(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString(""))) {

            // System.out.println("JMText.setText appends: " + str);

            append(str);		// Now add the new string that we were passed

        }

        

    }

    

    /**

     * This method allows setting of the cursor's position based on the

     * count of characters from the beginning of the component.

     * @param position An integer-based position of the character counting from the

     * first character of the component (not the first visible character)

     */

    public void setCaretPosition(final int position) {

        // System.out.println("setCaretPosition of JMText was called, but it is still just a stub.");

        // Fix me XXX

    }

    

    /** Return a string of the text-content of JMText (does not return the markings)

     * @return A string containing the entire text of the JMText including the &quot;history"

     */

    public synchronized String getText() {

        String work;

        final StringBuffer tempString = new StringBuffer(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString(""));

        final int returnRows = cursorPos.y;

        // int returnColumns = textChars[0].length;

        

        // add any historyText, if there is

        if (historyText != null) {

            final int hTextSize = historyText.size();

            

            // for (int index = 0; index < historyText.size(); index++) {

            for (int i = 0; i < hTextSize; i++) {

                tempString.append(historyText.elementAt(i));

            }

        }

        

        for (int y = 0; y < returnRows; y++) {

            work = java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("");

            try {

                work = textChars[y] + java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("");

            } catch (Exception e) {

                System.out.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("Error_in_JMText.getText_") + e);

            }

            

            // Smack any 'null's off the end *WHACK WHACK WHACK!*

            if (work.indexOf('\u0000') == 0) {

                tempString.append('\n');

            } else {

                if (work.indexOf('\u0000') > 0) {

                    tempString.append(work.substring(0, work.indexOf('\u0000')));

                    if (work.indexOf('\n') < 1) {

                        // There is no newline here, we'll add one

                        tempString.append('\n');

                    }

                } else {

                    tempString.append(work);

                }

            }

        }

        

        return tempString.toString().trim();

        

    }

    

    /** Set our redraw status to use double buffering for our display redraws.

     * Double buffering is a technique of &quot;drawing" to a screen not visible

     * to the user and then replacing the visible screen with the new one.  When

     * double buffering is not used the user can witness new information being

     * written to the JMText area (if visible)

     * @param status <tt>true</tt> enables double buffering while <tt>false</tt> disables it

     */

    public synchronized void setDoubleBuffer(final boolean status) {

        doubleBuffer = status;

    }

    

    /** Report if we use double buffering for our display redraws

     * @return <tt>true</tt> indicates double buffering is enabled, <tt>false</tt> indicates

     * double buffering is not enabled.

     */

    public synchronized boolean isDoubleBuffer() {

        return doubleBuffer;

    }

    

    /** Select a given area of text which will be indicated by highlighting

     * @param selectionStart        The character number (from the beginning) to start the selection

     * @param selectionEnd          The number of characters from <tt>selectionStart</tt>

     */

    public synchronized void select(final int selectionStart, final int selectionEnd) {

        if (selectionStart == 0 && selectionEnd == 0) {

            // Deselect any selections

            selectBegin.x = 0;

            selectBegin.y = 0;

            selectEnd.x = 0;

            selectEnd.y = 0;

        }

        // System.out.println("Stub: JMText.select(int, int) was called.");

        // Fix me XXX

        

        // Refresh the screen with out changes

        repaint();

        // return;

    }

    

    /**

     * Returns an integer value of where a selection ends.

     * @return Return an integer representing the count from the

     * beginning of the component.

     * <CODE>0</CODE> is returned if there is not a selection present.

     */

    public synchronized int getSelectionEnd() {

        return translateSelect(textBegin);

    }

    

    /**

     *  Returns the beginning of a selection in an integer format based on

     * the count of characters from the beginning of the text component.

     * @return The integer value representing the beginning of the selection.

     * <CODE>0</CODE> will be returned if there is no selection.

     */

    public synchronized int getSelectionStart() {

        return translateSelect(textEnd);

    }

    

    /**

     * Translate the selected location into a integer representing

     * the number of characters from the beginning of the JMText

     * to the selected point

     * @param location    An clickX,clickY position of a character in the JMText

     * @return This integer is the position of the <tt>location</tt> character counting

     * from the first character of the JMText area.

     */

    private int translateSelect(final Point location) {

        int total = 0;

        

        if (location.y > 0) {

            final int yLoc = location.y;

            

            // for (int index = 0; index < location.clickY; index++) {

            for (int i = 0; i < yLoc; i++) {

                total += textChars[i].toString().length();

            }

        }

        

        total += location.x;

        

        return total;

    }

    

    /** return a String of the text currently selected in the JMText area

     * @return Returns a <tt>String</tt> of the selected text, if any.

     */

    public synchronized String getSelectedText() {

        final StringBuffer totalString = new StringBuffer(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString(""));

        String workString = java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("");

        

        int baseLine = 0;	// the 'top' line of text to view

        if (vertScroll != null) {

            baseLine = vertScroll.getValue();

        }

        

        testSelection();

        

        // First, we'll nab the start and end points based on the selection

        

        final int tArea = textEnd.y - textBegin.y;

        

        // for (int index = 0; index <= (textEnd.clickY - textBegin.clickY); index++) {

        for (int i = 0; i <= tArea; i++) {

            // First we grab the entire, upper-most line

            workString = textChars[textBegin.y + i + baseLine] + java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("");

            

            // We'll make sure we're not containing any 'newline' characters

            // The first one we encounter, that is the *end* of our line

            if (workString.indexOf('\n') > 0) {

                workString = workString.substring(0, workString.indexOf('\n'));

            } else {

                if (workString.indexOf('\u0000') > 0) {

                    workString = workString.substring(0, workString.indexOf('\u0000'));

                }

            }

            

            // Now we'll trim down the workString if it happens to be the first

            // or last row (this can be "cumulative")

            if (i == 0) {

                workString = workString.substring(textBegin.x);

            }

            if (i == (textEnd.y - textBegin.y)) {

                if (textEnd.y == textBegin.y) {

                    // Beginning and end are on the same line

                    int end = textEnd.x - textBegin.x;

                    if (workString.length() < end || end < 0) {

                        end = workString.length();

                    }

                    workString = workString.substring(0, end);

                } else {

                    int end = textEnd.x;

                    if (workString.length() < end || end < 0) {

                        end = workString.length();

                    }

                    workString = workString.substring(0, end);

                }

            }

            totalString.append(workString);

        }

        

        final String finalString = totalString.toString();

        return finalString;

    }

    

    /** Set the JMText area to be editable or not.  Currently this is only a &quot;stub" call

     * @param edit <tt>true</tt> indicates this area is editable, <tt>false</tt> disables editing

     */

    public synchronized void setEditable(final boolean edit) {

        // System.out.println("Stub: JMText.setEditable(boolean) was called.  Currently, JMText does not have the capacity to be editable.");

        // Fix me XXX

    }

    

    /**

     * Allow a TextListener to be added to this component.

     * @param listener The listener to be added

     */

    public void addTextListener(final TextListener listener) {

        textL = AWTEventMulticaster.add(textL, listener);

    }

    

    // KeyListener events

    /**

     * Allow a KeyListener to be added to this component.

     * @param listener The KeyListener to be added.

     */

    public void addKeyListener(final KeyListener listener) {

        keyL = AWTEventMulticaster.add(keyL, listener);

        enableEvents(AWTEvent.KEY_EVENT_MASK);

    }

    

    /**

     *  Allow the removal of the given KeyListener from our component.

     * @param listener The listener to be removed.

     */

    public void removeKeyListener(final KeyListener listener) {

        keyL = AWTEventMulticaster.remove(keyL, listener);

    }

    

    /**

     * The method to handle any KeyPresses.

     * @param event The event we are listening for.

     */

    public void keyPressed(final KeyEvent event) {

        if(keyL != null) {

            keyL.keyPressed(event);

        }

    }

    

    /**

     * A method to handle a key being released

     * @param event The Key Released event we are waiting for.

     */

    public void keyReleased(final KeyEvent event) {

        if(keyL != null) {

            keyL.keyReleased(event);

        }

    }

    

    /**

     * A method to handle a key being typed.

     * @param event The event we are listening for.

     */

    public void keyTyped(final KeyEvent event) {

        if(keyL != null) {

            keyL.keyTyped(event);

        }

    }

    

    /**

     * If our component is hidden this method called, and internally

     * recorded as being "minimised".

     * @param event The event we are listening for.

     */

    public synchronized void componentHidden(final ComponentEvent event) {

        // System.out.println("JMText.componentHidden minimised = true");

        minimised = true;

    }

    

    /**
     * An empty method
     * @param event Empty event
     */
    public void componentMoved(final ComponentEvent event) {

    }

    

    /**

     * A method used to monitor if this component has been resized. This method

     * is important for handling the number of columns/rows available.

     * @param event Our Component Resized Event

     */

    public synchronized void componentResized(final ComponentEvent event) {

        if (notified) {

            return;

        }

        

        // if (this.getSize().width < 1 || this.getSize().height < 1) {

        if (this.getSize().width < 1 || this.getSize().height < 1) {

            // This item has a negative dimension, which we'll

            // take as being the same a "minimized"

            // System.out.println("JMText.componentResized() set minimised = true");

            minimised = true;

            return;

        } else {

            if ((widgetSize.height != drawText.getSize().height || widgetSize.width != drawText.getSize().width) && getSize().height > 0) {

                // System.out.println("JMText.componentResized() set minimised = false");

                minimised = false;

                // System.out.println("JMText.ComponentResized() discrepency with recorded size.");

                if (!notified) {

                    // System.out.println("JMText.ComponentResized() initialising JMText.");

                    initializeJMText();

                    // System.out.println("JMText is initialised.");

                }

            }

            

            // this item has a positive size, so we'll save its dimensions

            widgetSize.setSize(getSize());

            // System.out.println("Widget-size: " + widgetSize);

            // System.out.println("Minimum size: " + getMinimumSize());

            // System.out.println("JMText.ComponentResized() called, would spool text.");

            // spoolText();

        }

    }

    

    private void jMComponentResized() {

        

        if (this.getSize().width < 1 || this.getSize().height < 1) {

            

            // This item has a negative dimension, which we'll

            // take as being the same a "minimized"

            // System.out.println("JMText.jMComponentResized() set minimised = true");

            minimised = true;

            return;

        } else {

            // System.out.println("----");

            // System.out.println("this.getSize() : " + this.getSize());

            // System.out.println("widgetSize : " + widgetSize);

            /// System.out.println("drawText.getSize() : " + drawText.getSize());

            if ((widgetSize.height != drawText.getSize().height || widgetSize.width != drawText.getSize().width) && getSize().height > 0) {

                // System.out.println("JMText.jMComponentResized() set minimised = false");

                minimised = false;

                if (!notified) {

                    initializeJMText();

                }

                

                // Check to see if it affects the columns & rows before calling a resize

                if (checkNewRowsSame()) {

                    return;

                }

                

                // Calculate new columns & rows

                resizeDisplay();

                

                // this item has a positive size, so we'll save its dimensions

                // System.out.println("resizeDisplay() setting widgetSize to current size:");

                // System.out.println(widgetSize + " -> " + drawText.getSize());

                // widgetSize.setSize(getSize());  // This returns a slightly different value than drawText

                widgetSize.setSize(drawText.getSize());

                spoolText();

                // System.out.println("JMText.jMComponentResized() called, would spool text.");

            }

            

        }

    }

    

    /**

     * This method is called when our component is shown and internally

     * records the new size of our componenet and spools out any text

     * that may have been held while it was minimised.

     * @param event Our component event.

     */

    public synchronized void componentShown(final ComponentEvent event) {

        // System.out.println("JMText.componentShown() set minimised = false");

        minimised = false;

        widgetSize.setSize(getSize());

        spoolText();

    }

    

    /**

     * We need to make our own mouse-listener that'll report back

     * to any listeners that may've registered to this component.  Or

     * something.  This may not make a lotta sense, I'm tired.

     * @param mouse The mouseListener to add to this componenet.

     */

    public synchronized void addMouseListener(final MouseListener mouse) {

        mListener = AWTEventMulticaster.add(mListener, mouse);

        enableEvents(AWTEvent.MOUSE_EVENT_MASK);

    }

    

    /**

     * All registered MouseListeners to be removed from this component.

     * @param mouse The MouseListener to be removed.

     */

    public synchronized void removeMouseListener(final MouseListener mouse) {

        if (mListener != null) {

            mListener = AWTEventMulticaster.remove(mListener, mouse);

        }

    }

    

    // Mouse events

    /**

     * This method handles mouse clicks for this component

     * @param event Our Mouse Event

     */

    public void mouseClicked(final MouseEvent event) {

        if (event.getClickCount() >= 2) {

            // Select the "word" that the mouse was double-clicked on

            doubleClickSelect(event.getX(), event.getY());

        }

        

        if (mListener != null) {

            mListener.mouseClicked(event);

        }

    }

    

    /**

     * This method changes the cursor to the I-beam when it enters the text area.

     * @param event Our event

     */

    public void mouseEntered(final MouseEvent event) {

        // Change the mouse cursor to an I-beam

        setCursor(new Cursor(Cursor.TEXT_CURSOR));

    }

    

    /**

     * This method changes our cursor back to the default cursor (from the I-beam)

     * when it leaves our component area.

     * @param event Our mouse event.

     */

    public void mouseExited(final MouseEvent event) {

        // Return mouse cursor back to standard pointer

        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

    }

    

    /**

     * This method tracks when the mouse button is pressed... used mostly when

     * the user selects text.

     * @param event Our mouse event

     */

    public void mousePressed(final MouseEvent event) {

        // The mouse has been pressed, we will record its array position

        textBegin = pointToArray(new Point(event.getX(), event.getY()));

        textEnd = new Point(textBegin.x, textBegin.y);

        

        // Check to see if we have to deselect any old selections

        if (selectBegin.x > 0 || selectBegin.y > 0 || selectEnd.x > 0 || selectEnd.y > 0) {

            paintSelection(selectBegin, selectEnd, drawText.getBackground());

        }

        

        // And now we'll record the 'corrected position'; the character's position

        selectBegin = arrayToPoint(textBegin);

        selectEnd = new Point(selectBegin.x, selectBegin.y);

    }

    

    /**

     * This method watches for when the mouse is released and repaints our

     * component.  This is to ensure that selections are properly painted.

     * @param event Our mouse event.

     */

    public void mouseReleased(final MouseEvent event) {

        if(selectBegin.x == selectEnd.x && selectBegin.y == selectEnd.y) {

            repaint();

            // No need for selection... start and end points are the same

            return;

        }

    }

    

    /**

     * The method is used to track mouse drags and properly support text

     * selection via the mouse.

     * @param event Our mouse listener.

     */

    public void mouseDragged(final MouseEvent event) {

        // First we'll nab our selected character location

        Point tempPoint = pointToArray(new Point(event.getX(), event.getY()));

        final int selectedColumn = tempPoint.x;

        final int selectedRow = tempPoint.y;

        

        // First we'll grab our FontMetrics

        // FontMetrics fMetric = getFontMetrics(getFont());

        

        final int oldx = selectEnd.x, oldy = selectEnd.y;

        // Figure out the coordinates of this selection

        

        tempPoint = arrayToPoint(new Point(selectedColumn, selectedRow));

        final int xPos = tempPoint.x;

        final int yPos = tempPoint.y;

        

        if((xPos < selectBegin.x && yPos < selectBegin.y) &&

                (xPos < selectEnd.x && yPos < selectEnd.y)) {

            

            selectBegin.x = xPos;

            selectBegin.y = yPos;

            textBegin.x = selectedColumn;

            textBegin.y = selectedRow;

        } else {

            selectEnd.x = xPos;

            selectEnd.y = yPos;

            textEnd.x = selectedColumn;

            textEnd.y = selectedRow;

        }

        

        if(oldx != xPos || oldy != yPos) {

            // erase our old selection first

            paintSelection(selectBegin, new Point(oldx, oldy), drawText.getBackground());

            // Then draw our new selection

            paintSelection(selectBegin, selectEnd, drawText.getBackground());

        }

    }

    

    /**
     * Empty method.
     * @param event 
     */
    public void mouseMoved(final MouseEvent event) {

    }

    

    /**

     * This method watches for the component to gain focus.  If the component

     * has not already been initialised, that method is called here.

     * @param event Our focus event

     */

    public void focusGained(final FocusEvent event) {

        if (!notified) {

            initializeJMText();

        }

    }

    

    /**
     * Empty method.
     * @param event Our focus event.
     */
    public void focusLost(final FocusEvent event) {

    }

    

    /**

     * Here, we check for an ANSI colour escape in the 'token', starting at position 'index'.

     * If this is indeed a real colour escape, we will return index as the end of the escape,

     * modifying the 'currentAttrib' and "eating" the escape

     * @param token is a string that is to be checked to see if it is an ANSI colour escape

     * @param index is the starting position of the token, incase the token is more than an &quot;escape"

     * @return This integer represents to end of the &quot;escape" if there is one.

     */

    private int checkEscape(final String token, final int index) {

        int end = 0;

        int split = token.indexOf('\u001b') + 2;

        int newSplit = 0;

        int token1 = -1;

        // int token2 = -1;

        boolean loop = true;

        

        end = token.indexOf('m', index);	// look for the closing 'm' from the offset of 'index'

        

        // if (end == 0) {

        if (end < 0) {

            return index + 2;       // Could be a simple escape like "BELL"

            // return index; 	// This token doesn't qualify as a colour

        }

        final int mPlace = token.indexOf('m', index);

        if (mPlace < 0) {

            return token.length();

        }

        final String lilToken = token.substring(index, mPlace + 1);

        

        // This should continue a loop until our last element, which does

        // not end with a ';' character.  We'll grab that last.

        try {

            while (loop) {

                newSplit = lilToken.indexOf(';', split);

                

                if (newSplit > end || newSplit < 1 || split >= newSplit) {

                    loop = false;

                    break;

                }

                

                token1 = Integer.parseInt(lilToken.substring(split, newSplit));

                

                buildColourAttr(token1);

                split = lilToken.indexOf(';', split);

                split++;

            }

        } catch (Exception e) {

            System.out.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("Token-loop_caught:_") + e);

            System.out.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("Start_(split)_") + split + java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("_end_split_(newSplit)_") + newSplit);

            loop = false;

        }

        

        // if (lilToken.indexOf(';') > 0) {

        if (lilToken.indexOf(';') > -1) {

            final int endStart = lilToken.lastIndexOf(';') + 1;

            final int endEnd = lilToken.lastIndexOf('m');

            try {

                if (endStart > 0) {

                    token1 = Integer.parseInt(lilToken.substring(endStart, endEnd));

                } else {

                    // This is just a single colour token

                    token1 = Integer.parseInt(token.substring(index + 2, end));

                    System.out.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("We_think_we've_just_got_a_little_token."));

                }

                

                // if (token1 == 0) token1 = 10;  What did this do?  Fix this XXX

            } catch (NumberFormatException nfe) {

                System.out.println(nfe);

                System.out.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("lilToken_") + lilToken);

                System.out.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("Token_") + token);

                // System.exit(0);

                return (endEnd + 1);

            } catch (Exception e) {

                System.out.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("Error_trying_to_get_closing_token.") + e);

                // This seems to happen if we're missing a final 'm'

                System.out.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("Our_entire_token_was:_") + token);

                System.out.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("The_lilToken_was:_") + lilToken);

                final int start = lilToken.lastIndexOf(';');

                final int stop = lilToken.indexOf('m', start);

                token1 = Integer.parseInt(lilToken.substring(start + 1, stop));

            }

        } else {

            // System.out.println("Our token: " + token.substring(index + 2, end));

            try {

                token1 = Integer.parseInt(token.substring(index + 2, end));

            } catch (Exception e) {

                if (token.trim().length() >= 7 && token.trim().startsWith(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("["))) {

                    final String hexToken = token.trim().substring(1, 8);

                    changeAttrib(FGMARK + hexToken);

                    return (end + 1);

                } else {

                    System.out.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("Token_isn't_6_long?_*") + token.trim() + java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("*"));

                }

            }

        }

        buildColourAttr(token1);

        

        return (end + 1);

    }

    

    /** A method to sift through ANSI colour numbers and assign

     * them to the proper "area" (foreground, background, etc...)

     */

    private synchronized void buildColourAttr(final int colourAttr) {

        if (colourAttr == 0) {

            // changeAttrib("reset");

            changeAttrib(JMText.RESET);

            // return;

        }

        

        

        if (colourAttr < 30) {

            if (colourAttr == 1) {

                changeAttrib(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("<bold>"));

            }

            if (colourAttr == 2) {

                changeAttrib(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("<faint>"));

            }

            if (colourAttr == 3) {

                changeAttrib(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("<i>"));

            }

            if (colourAttr == 4) {

                changeAttrib(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("<u>"));

            }

            if (colourAttr == 7) {

                changeAttrib(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("<negative>"));

            }

            if (colourAttr == 9) {

                changeAttrib(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("<s>"));

            }

            if (colourAttr == 22) {

                changeAttrib(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("</bold>"));

            }

            if (colourAttr == 24) {

                changeAttrib(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("</u>"));

            }

            // return;

        }

        

        // Foreground colours

        if (colourAttr > 29 && colourAttr < 40) {

            changeAttrib(FGMARK + colourAttr);

            // return;

        }

        

        // Background colours

        if (colourAttr > 39 && colourAttr < 50) {

            changeAttrib(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("bg:") + colourAttr);

            // return;

        }

        

        // return;

    }

    

    /** We'll use this method to 'intelligently' add and remove attributes */

    // private void changeAttrib(String attrib) {

    private void changeAttrib(final String initialAttrib) {

        // attrib = attrib.toLowerCase();

        String attrib = initialAttrib.toLowerCase();

        

        // first, if we just need to reset the values, we don't need to continue

        // if (attrib.equals("reset")) {

        if (attrib.equals(JMText.RESET)) {

            // reset attributes

            currentAttrib = java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("");

            return;

        }

        

        // Break up the currentAttribs into a vector for ease of manipulation

        final Vector tokens = new Vector(0, 1);

        final StringTokenizer workString = new StringTokenizer(currentAttrib, java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("|"), false);

        

        int workTokens = workString.countTokens();

        

        // for (int index = 0; index < workString.countTokens(); index++) {

        for (int i = 0; i < workTokens; i++) {

            tokens.addElement(workString.nextToken());

        }

        

        workTokens = tokens.size();

        

        // It is okay to have "nested" attributes, except for foreground and

        // background colours, which we will deal with here.

        if (attrib.startsWith(FGMARK)) {

            // Change the foreground colour

            

            // for (int index = 0; index < tokens.size(); index++) {

            for (int i = 0; i < workTokens; i++) {

                if (((String)tokens.elementAt(i)).startsWith(FGMARK)) {

                    tokens.removeElementAt(i);

                    break;

                }

            }

            // Now add the new foreground colour to the end of the vector

        }

        

        if (attrib.startsWith(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("bg:"))) {

            // Change the background colour

            

            // for (int index = 0; index < tokens.size(); index++) {

            for (int i = 0; i < workTokens; i++) {

                if (((String)tokens.elementAt(i)).startsWith(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("bg:"))) {

                    tokens.removeElementAt(i);

                    break;

                }

            }

            // Now add the new background colour to the end of the vector

        }

        

        if (attrib.indexOf(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("</")) >= 0) {

            final String rep = java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("<") + attrib.substring(2);

            for (int i = 0; i < workTokens; i++) {

                // if (((String)tokens.elementAt(index)).equals("<u>")) {

                if (((String)tokens.elementAt(i)).equals(rep)) {

                    tokens.removeElementAt(i);

                    attrib = java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("");

                    break;

                }

            }

        }

        

        // Now we reassemble the attributes

        final StringBuffer newAttribs = new StringBuffer(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString(""));

        

        workTokens = tokens.size();

        

        // for (int index = 0; index < tokens.size(); index++) {

        for (int i = 0; i < workTokens; i++) {

            newAttribs.append((String)tokens.elementAt(i) + java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("|"));

        }

        

        if (!attrib.equals(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString(""))) {

            newAttribs.append(attrib + java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("|"));	// Now add the newest attribute

        }

        

        currentAttrib = newAttribs.toString();

        

    }

    

    /** Calculate the proper colour to return from either ANSI or Hex codes

     * @param markings    contains the colour information in a String

     * @param offset      the location in the <tt>markings</tt> to start parsing

     * @return Returns a <tt>Color</tt> to be used to colour the characters

     */

    private Color calculateColour(final String markings, final int offset) {

        final String colourCode = markings.substring(offset + 3, markings.indexOf('|', offset));

        Color retColour = getForeground();	// Just in case

        

        // if (colourCode.length() == 6) {

        // if (colourCode.startsWith("#")) {

        if (colourCode.charAt(0) == '#') {

            retColour = hexToColour(colourCode);

        }

        

            /* Real colour codes (JamochaMUD sort've fudges them right now

             * Atributes

             * 0  Normal text

             * 1  Bold   (This gives the color higher intensity)

             * 4  Underline

             * 5  Blinking

             * 7  Revers (Foreground -> Background, Background -> Foreground)

             * 8  Hidden

             *

             * Foreground color

             * 30 Black

             * 31 Red

             * 32 Green

             * 33 Yellow

             * 34 Blue

             * 35 Magenta

             * 36 Cyan

             * 37 White

             *

             * Background color

             * 39 Reset background colour

             * 40 Black

             * 41 Red

             * 42 Green

             * 43 Yellow

             * 44 Blue

             * 45 Magenta

             * 46 Cyan

             * 47 White

             */

        

        // Make the definitions below match the table above!  Fix this XXX

        // Now determine what the colour is

        // 0 indicates normal text colour

        // if (colourCode.equals("0")) retColour =

        if (colourCode.equals(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("1"))) {

            retColour = retColour.brighter();

        }

        if (colourCode.equals(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("2"))) {

            retColour = retColour.darker();

        }

        if (colourCode.equals(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("3"))) {

            retColour = Color.yellow;       // XXX should be italicized

        }

        if (colourCode.equals(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("4"))) {

            retColour = Color.blue; // XXX should be underlined

        }

        if (colourCode.equals(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("5"))) {

            retColour = Color.magenta;      // should be slowly blinking

        }

        if (colourCode.equals(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("6"))) {

            retColour = Color.cyan; // XXX should be rapidly blinking

        }

        if (colourCode.equals(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("7"))) {

            retColour = Color.white;        // XXX negative image

        }

        if (colourCode.equals(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("8"))) {

            retColour = Color.black;        // XXX should be concealed characters

        }

        if (colourCode.equals(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("9"))) {

            retColour = Color.white;        // XXX should be crossed-out characters

        }

        if (colourCode.equals(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("10"))) {

            retColour = Color.lightGray;   // XXX should be default font

        }

        if (colourCode.equals(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("30")) || colourCode.equals(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("40"))) {

            retColour = Color.black;

        }

        if (colourCode.equals(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("31")) || colourCode.equals(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("41"))) {

            retColour = Color.red;

        }

        if (colourCode.equals(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("32")) || colourCode.equals(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("42"))) {

            retColour = Color.green;

        }

        if (colourCode.equals(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("33")) || colourCode.equals(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("43"))) {

            retColour = Color.yellow;

        }

        if (colourCode.equals(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("34")) || colourCode.equals(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("44"))) {

            retColour = Color.blue;

        }

        if (colourCode.equals(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("35")) || colourCode.equals(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("45"))) {

            retColour = Color.magenta;

        }

        if (colourCode.equals(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("36")) || colourCode.equals(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("46"))) {

            retColour = Color.cyan;

        }

        if (colourCode.equals(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("37")) || colourCode.equals(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("47"))) {

            retColour = Color.white;

        }

        if (colourCode.equals(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("39"))) {

            retColour = bgColour;

        }

        

        return retColour;

    }

    

    /** This method returns "feedback" to our registered TextListener.

     * Right now this is very simplified, simply alerting the Listener that

     * the text value has changed @see java.awt.event.TextEvent

     * In the future this method may be expanded upon.

     */

    private void fauxTextListener() {

        // Here, we'll process our FauxText Event and package it up to the listener

        if (textL != null) {

            textL.textValueChanged(new TextEvent(this, TextEvent.TEXT_VALUE_CHANGED));

        }

        

    }

    

    /**

     * We override 'getSize()' to ensure that we always send a valid

     * size back to the component.  (This compensates for weirdness

     * when the component is minimised and stuff)

     * @return Returns a Dimension of the current size of the component.

     */

//    public Dimension getSize() {
//
  //      return super.getSize();
//
//    }

    

    /** With the displayed area being resized (on the screen),

     * we will have to handle copying our arrays to a temporary

     * array, resizing the original array and then put the info back

     */

    // We'll temporarily name this method "oldresizeDisplay" to try

    // a new method of resizing the Display (just below this one)

    private synchronized void resizeDisplay() {

        if (!notified || cursorPos == null) {

            return;

        }

        

        if (checkNewColumnsSame()) {

            // Only the height has changed, so we do not need to reformat

            // Adjust the scrollbars to their proper maximum

            resizeScrollbars();

            return;

        }

        

        if (rows < 1 || columns < 1) {

            minimised = true;

            return;

        }

        

//        if (cursorPos == null) {

//            return;  // If there is no cursor, we don't do this

//        }

        

        final char[][] tempChars = new char[cursorPos.y + 1][columns];

        final String[][] tempMarkings = new String[cursorPos.y + 1][columns];

        final int tempRows = cursorPos.y;		// The bottom of the array

        // int tempColumns = columns;		// The "right edge" of the array

        

        // Copy the array into a new temporary array

        try {

            System.arraycopy(textChars, 0, tempChars, 0, cursorPos.y + 1);

            System.arraycopy(markings, 0, tempMarkings, 0, cursorPos.y + 1);

        } catch (Exception e) {

            System.out.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("Trouble_copying_arrays_in_resizeDisplay._") + e);

            System.out.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("Current_cursor_position:_") + cursorPos.y);

            return;

        }

        

        // Clear the old array

        setCharacterArrays();

        

        // Reset the attributes

        initializeJMText();

        

        if (!SMART_ARRAY) {

            // Now we'll (in an ugly way) loop through the array and re-write
            // the old information into the format of the new array

            StringBuffer chopString;
            String curAt = java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString(""); // Current attribute for our purposes here
            String workString = java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("");
            String markLine[];

            int atLoc; // cummulative length of attributes added to the line
            int tLength;
            int lastNewLine;

            // This initial loop goes through the array line by line,
            // converting each line to a string (with proper termination)
            // These strings are then "append()"ed back to the window.  This
            // is a pretty ugly way of doing things.

            for (int i = 0; i <= tempRows; i++) {
                atLoc = 0;
                // workString = new String(tempChars[index]);
                workString = tempChars[i].toString();
                lastNewLine = workString.lastIndexOf('\n');
                lastNewLine++;  // This gives us the number of characters till the last new line

                if (lastNewLine > 0) {
                    chopString = new StringBuffer(workString.substring(0, lastNewLine));
                    tLength = lastNewLine;
                } else {
                    // Find the first null.  This will be the length of our line.

                    tLength = workString.indexOf(0);

                    

                    chopString = new StringBuffer(workString.substring(0, tLength));

                    if (tLength < chopString.length()) {

                        tLength = chopString.length();

                    }

                }

                

                if (!SMART_MARK) {

                    markLine = tempMarkings[i];

                    for (int j = 0; j < tLength; j++) {

//                            if (markLine[j] != null) {

//                                if (!curAt.equals(markLine[j])) {

                        if (markLine[j] != null && !curAt.equals(markLine[j])) {

                            curAt = markLine[j];

                            if (curAt.equals(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString(""))) {

                                // Add a tag to reset colouring

                                chopString.insert(j + atLoc, '\u001b' + java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("[0m"));

                                atLoc += 4;

                            } else {

                                // Insert a tag to start colouring of proper attributes

                                chopString.insert(j + atLoc, '\u001b' + java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("[") + curAt.substring(3, curAt.length() - 1) + java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("m"));

                                atLoc += curAt.length() - 1;

                            }

                            // }

                        }

                    }

                }

                workString = chopString.toString();

                append(workString, false);

            }

            

            if (SMART_MARK) {

                // An alternative form of marking.  This method goes through

                // the new array after it has been created and has the characters

                // assigned.  It then adds the markings in the appropriate locations

                int newX = 0;

                int newY = 0;

                

                final int tCPos = cursorPos.y;

                

                // for (int index = 0; index < cursorPos.clickY; index++) {

                for (int i = 0; i < tCPos; i++) {

                    // tLength = (new String(textChars[index])).length();

                    tLength = (textChars[i].toString()).length();

                    // System.out.println("Line: " + index + " length: " + tLength);

                    for (int j = 0; j < (tLength - 1); j++){

                        if (tempMarkings[newY][newX] != null) {

                            // System.out.println("New line..." + newY);

                            newX=0;

                            newY++;

                        }

                        

                        // Add the proper markings to the array

                        markings[i][j] = tempMarkings[newY][newX];

                        newX++;

                    }

                }

            }

            

            // Point tempCursor = new Point(0, 0); // To keep track of where we are...

            

        } else {

            /** This is the SMART_ARRAY method... still in working on it! */

            

                /* This is the new method for marking and resizing the array!

                 * If this proves successful we will move it to a standard part

                 * of the code instead of making it an if (SMART_ARRAY)

                 */

            int newX = 0;

            int newY = 0;

            char workChar;

            String workString = java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("");

            int lastNewLine, lineMeasure;

            final int workRows = tempRows - 1;

            int modifier = 0;       // this keeps track of any breaks in mid-String

            // String lastMarks;       // The last successful set of markups we have

            

            // Get the FontMetrics

            final FontMetrics fMetric = getFontMetrics(getFont());	// Fontmetrics for this character

            final int viewWidth = getSize().width - 10; // width of the visual area

            

                /* We have two different parameters we have to watch:

                 * 1. The width of the array

                 * 2. The width of the visible screen

                 * We cannot exceed the first or we'll get errors;

                 * exceeding the second means JMText would be unusable to the user

                 */

            // for (int index = 0; index < (tempRows - 1); index++) {

            for (int i = 0; i <= workRows; i++) {

                lineMeasure = 0;    // Keep track of our visual lenght (for the line)

                modifier = 0;

                

                // Determine how long the line in our array is

                // workString = new String(tempChars[index]);

                workString = tempChars[i].toString();

                

                lastNewLine = workString.lastIndexOf(0);

                

                for (int j = 0; j < lastNewLine; j++) {

                    if (tempChars[i][j] != 0) {

                        workChar = tempChars[i][j];

                        textChars[newY][newX] = workChar;

                        markings[newY][newX] = tempMarkings[i][j];

                        lineMeasure = lineMeasure + fMetric.charWidth(workChar);

                        

                        // System.out.print(workChar);

                        

                        newX++;

                        // if (textChars[newY][newX] == '\n' || (newX + 1) == columns || lineMeasure > viewWidth) {

                        if (newX >= columns || lineMeasure > viewWidth) {

                            // This was an EOL character.  Start a new row.

                            newX = 0;

                            newY++;

                            lineMeasure = 0;

                            modifier = j;

                            // System.out.println("<br>");

                        }

                        

                        if (workChar == '\n' || workChar == '\r') {

                            newX = 0;

                            newY++;

                            lineMeasure = 0;

                            modifier = j;

                        }

                        

                        if (workChar == ' ') {

                            // This is a space, check to see if the next space will fit on this line of the array

                            final int charIndex = workString.indexOf(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("_"), j + 1);

                            if (charIndex > 0) {

                                

                                // System.out.println("j = " + j);

                                // System.out.println("charIndex = " + charIndex);

                                // System.out.println("lastNewLine = " + lastNewLine);

                                // int tmp = fMetric.charsWidth(tempChars[index], j + 1, workString.indexOf(" ", j + 1) - j);

                                final int tmp = fMetric.charsWidth(tempChars[i], j + 1, charIndex - j);

                                

                                // if (workString.indexOf(" ", j + 1) - modifier > columns || tmp + lineMeasure > viewWidth) {

                                if (charIndex - modifier > columns || tmp + lineMeasure > viewWidth) {

                                    newX = 0;

                                    newY++;

                                    lineMeasure = 0;

                                    modifier = j;

                                    // System.out.println("<space-br>" + workString.indexOf(" ", j));

                                }

                            }

                        }

                        

                    }

                }

            }

            

            // Point tempCursor = new Point(0, newY); // To keep track of where we are...

            cursorPos.setLocation(newX, newY);

            // vertScroll.setMaximum(newY);

            // vertScroll.setValue(newY);

            scrollDown();

            

        }

        

    }

    

    /** With the displayed area being resized (on the screen),

     * we will have to handle copying our arrays to a temporary

     * array, resizing the original array and then put the info back

     */

    // This is the new experimental-type version of resizeDisplay

    

    /** Paint the selected area appropriate (if there is one)

     * @param begin       The beginning of the selection

     * @param end         The end of the selection

     * @param XORColour   The colour used to highlight the selection

     */

    private void paintSelection(final Point begin, final Point end, final Color XORColour) {

        // Here, we keep the "selection" updated for the paint method

        final FontMetrics fMetric = getFontMetrics(getFont());

        final int height = fMetric.getHeight();

        final int descent = fMetric.getMaxDescent();

        

        final Graphics tempGraph = drawText.getGraphics();

        

        if (begin.y == end.y) {

            tempGraph.setXORMode(XORColour);

            tempGraph.fillRect(begin.x,

                    begin.y + descent,

                    end.x - begin.x,

                    end.y - begin.y + height);

        }

        

        if (begin.y < end.y) {

            tempGraph.setXORMode(XORColour);

            tempGraph.fillRect(begin.x,

                    begin.y + descent,

                    getSize().width - begin.x,

                    height);

            tempGraph.fillRect(0,

                    begin.y + height + descent,

                    getSize().width,

                    end.y - (begin.y + height));

            tempGraph.fillRect(0,

                    end.y + descent,

                    end.x,

                    height);

        }

        

    }

    

    /**

     * This translates a point on the screen into a location

     * on our array of characters !  (This method is usually used

     * in conjunction with marking text)

     * @param pos The coordinates of the mouse pointer in our component.

     * @return This returns a Dimension indicating the column and row

     * that coordinate with the mouse position on our component.

     */

    public Point pointToArray(final Point pos) {

        // First we'll grab our FontMetrics

        final FontMetrics fMetric = getFontMetrics(getFont());

        final int height = fMetric.getHeight();

        final int descent = fMetric.getDescent();

        

        // Figure out where we're dragging right now

        int selectedColumn = -1;

        int selectedRow = (int)((pos.y - descent) / height);

        if ((pos.y - descent) / height > selectedRow) {

            // We'll need to round this up by one

            selectedRow++;

        }

        

        // int clickX = 0;

        // Figure out the coordinates of this selection

        int count = 0;

        int baseLine = 0;	// the 'top' line of text to view

        

        if (vertScroll != null) {

            baseLine = vertScroll.getValue();

        }

        

        for (int i = 0; i < columns; i++) {

            count += fMetric.charWidth(textChars[selectedRow + baseLine][i]);

            if (count > pos.x || (textChars[selectedRow + baseLine][i] == '\u0000')) {

                selectedColumn = i;

                break;

            }

        }

        if (selectedColumn > columns) {

            selectedColumn = columns;

        }

        

        return new Point(selectedColumn, selectedRow);

    }

    

    private Point arrayToPoint(final Point pos) {

        // First we'll grab our FontMetrics

        final FontMetrics fMetric = getFontMetrics(getFont());

        

        // Figure out where we're dragging right now

        final int selectedColumn = pos.x;

        final int selectedRow = pos.y;

        

        // int clickX = 0;

        // Figure out the coordinates of this selection

        int count = 0;

        int baseLine = 0;	// the 'top' line of text to view

        

        if (vertScroll != null) {

            baseLine = vertScroll.getValue();

        }

        

        for (int i = 0; i < selectedColumn; i++) {

            count += fMetric.charWidth(textChars[selectedRow + baseLine][i]);

        }

        

        

        final int yPos = selectedRow * fMetric.getHeight();

        return new Point(count, yPos);

    }

    

    /** This method returns the value of a string

     * once it has had any escape characters stripped

     * from it.

     */

//    private String stripEscapes(final String token) {

//        final StringBuffer workString = new StringBuffer("");

//        boolean loop = true;

//        int start = 0;

//        int end = 0;

//        

//        do {

//            // Look for the next escape character

//            end = token.indexOf('\u001b', start);

//            

//            if (end < start) {

//                // There are no escapes left

//                if (start < 0) {

//                    System.out.println("We break to avoid an index error.");

//                    break;

//                }

//                workString.append(token.substring(start));

//                loop = false;

//                break;

//            }

//            

//            if (end > 0 && start >= 0) {

//                workString.append(token.substring(start, end));

//            }

//            

//            // Now set the new 'start'

//            start = token.indexOf('m', end) + 1;

//            

//            if (start <= end) {

//                loop = false;

//                if (start < 0) {

//                    System.out.println("We don't have an 'm' to end our ANSI escape.  We may not have received it yet.");

//                } else {

//                    // Check for BELL character (this is probably an ugly place to stick this Fix Me XXX)

//                    if (token.charAt(1) == BELL) {

//                        java.awt.Toolkit.getDefaultToolkit().beep();

//                        // System.out.println(BELL);

//                        // loop = true;

//                        System.out.println("Start: " + start + " end: " + end);

//                        start = 2;

//                        if (end == 0 && token.length() > 2) {

//                            // There are no more escapes, so we'll just add the remainder of the string

//                            workString.append(token.substring(2));

//                        }

//                        

//                        break;

//                    } else {

//                        if (DEBUG) {

//                            System.out.println("Not a proper ANSI escape");

//                            

//                            for (int i = 0; i < token.length(); i++) {

//                                System.out.print("*" + token.charAt(i) + "*");

//                            }

//                        }

//                        break;

//                    }

//                }

//                

//            }

//            

//            // Check to see if we've reached the end of our token.  If this is

//            // the case, then we can end the loop and return what we've got so far.

//            if (start >= token.length()) {

//                loop = false;

//                break;

//            }

//        } while (loop);

//        

//        return workString.toString();

//    }

    

    /**

     * Select the 'token' that has been double-clicked,

     * based on the clickX and clickY position of the cursor

     */

    private void doubleClickSelect(final int clickX, final int clickY) {
        // First, determine the start-point...
        // we want the 'point' translated to a location in our array
        Point cursor;
        cursor = pointToArray(new Point(clickX, clickY));
        int startX, startY;	// The 'start points'
        int endX, endY;	// The 'end points'
        int baseLine = 0;	// The 'baseline' to start reading from
        if (vertScroll != null) {
            baseLine = vertScroll.getValue();
        }
        startX = cursor.x;
        startY = cursor.y + baseLine;
        
        // Now we'll read backwards to find the beginning of the token
        while (startY > -1 && textChars[startY][startX] !=' ') {
            startX--;
            if (startX < 0) {
                startY--;
                startX = (textChars[startY] + java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("")).length() - 1;
                if (textChars[startY][startX] == '\n' || textChars[startY][startX] == '\r' || textChars[startY][startX] == ' ') {
                    startX = 0;
                    startY++;
                    break;
                }
            }
        }
        startX++;	// This moves us off the 'space' character
        
        // And forwards to the end of the token.
        endX = startX;
        endY = startY;
        
        while (endY < cursorPos.y && textChars[endY][endX] != ' ') {
            if (endY == cursorPos.y && endX >= (cursorPos.x - 1)) {
                break;
            }
            endX++;
            if (endX >= (textChars[endY] + java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("")).length() || textChars[endY][endX] == '\u0000' || textChars[endY][endX] == '\n' || textChars[endY][endX] == '\r') {
                if (endY < cursorPos.y - 1) {
                    endY++;
                    endX = 0;
                } else {
                    break;
                }
            }
            
            if (textChars[endY][endX] == ' ') {
                // Normal termination check here
                break;
            }
            
        }
        
        // We now have our selection!
        textBegin = new Point(startX, startY - baseLine);
        textEnd = new Point(endX, endY - baseLine);
    }
    
    /** Test the selected area to make sure parameters don't fall
     * into illegal bounds, as it were.  Y'know, like -1 and such */
    private void testSelection() {

        final int length = (textChars[textBegin.y] + java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("")).length() - 1;

        if (textBegin.x < 0) {

            textBegin.x = 0;

        }

        if (textEnd.x > length) {

            textBegin.x = length;

        }

        if (textBegin.y < 0) {

            textBegin.y = 0;

        }

        // There should be a test for the end of 'clickY', but I digress

    }

    

    /**

     * This method notifies our component that it has been added to a new object.

     */

    public void addNotify() {

        super.addNotify();

        

        if (!notified && this.getParent().isVisible()) {

            // Dimension test = new Dimension(this.getParent().getSize());

            if (getSize().width == 0 || getSize().height == 0) {

                setSize(getPreferredSize());

            }

            this.validate();

        }

    }

    

    /**
     * Over-ride the setFont so that we can readjust our display
     * for the new font-metrics
     * @param newFont The font to use in the current component.
     */

    public synchronized void setFont(final Font newFont) {

        super.setFont(newFont);

        resizeDisplay();

    }

    

    /** This tests the current view for the number of rows and columns.

     * If the columns and rows are the same, <pre>true</pre> is

     * returned.  Otherwise, we receive a <pre>false</pre>

     */

    private boolean checkNewRowsSame() {

        // Check to see if the change in size is suitable

        boolean retVal = false;

        

        getRowsAndColumns();

        if (prevRows == rows && prevCols == columns) {

            // return true;

            retVal = true;

        } else {

            

            // Our size has changed, so make certain that our scrollbars keep up!

            resizeScrollbars();

        }

        

        // return false;

        return retVal;

    }

    

    /** Much like checkNewRowsSame(), though this function only checks to

     * see if when resized we still maintain the same number of columns.

     * This returns <pre>true</pre> if the number of hasn't changed, or

     * <pre>false</pre> if they have.  A function like this is useful as

     * reformating text due to a width-change is very time consuming; but

     * if only the height need be adjusted, it is a trivial matter of setting

     * a new &quot;base-line" for the window.

     */

    private boolean checkNewColumnsSame() {

        boolean retVal = false;

        

        if (prevCols == columns) {

            // The number of columns haven't changed

            // return true;

            retVal = true;

        }

        

        // Yes, our number of columns have changed.  You poor CPU, you.

        // return false;

        return retVal;

    }

    

    private void setCharacterArrays() {

        // Now we can set up the character arrays

        textChars = new char[maxRows][columns];

        markings = new String[maxRows][columns];

        cursorPos = new Point(0, 0);

    }

    

    /** Set the initial values for any scrollbars we have */

    private void resetScrollbars() {

        // Set up the scrollbars

        if (vertScroll != null) {

            vertScroll.setMaximum(0);

        }

        

        if (horzScroll != null) {

            horzScroll.setMaximum(0);

        }

    }

    

    /** This method calculates the new size of the scrollbars;

     * handy incase we change the size of our display

     */

    private void resizeScrollbars() {

        if (vertScroll != null && cursorPos != null) {

            // Fix this XXX, right now we just hammer things to the bottom of the screen

            final int max = (cursorPos.y + 1) - rows;

//                if (vertScroll.getMaximum() != max); {

            if (vertScroll.getMaximum() != max) {

                vertScroll.setMaximum(max);

                vertScroll.setValue(max);

            }

        }

    }

    

    /** Spool out any text that may be pending.  Most likely this would be

     * called from an event that displays our JMText after being minimised

     * or hidden. */

    private synchronized void spoolText() {

        // if (pendingText.size() == 0) {
        if (pendingText.isEmpty()) {

            // There is no pending text

            return;

        }

        

        final int pTextSize = pendingText.size();

        

        // for (int index = 0; index < pendingText.size(); index++) {

        for (int i = 0; i < pTextSize; i++) {

            append(pendingText.elementAt(i).toString());

        }

        

        pendingText.removeAllElements();	// Make sure the vector is empty

        

    }

    

    /** Convert the given hex value into a Java Color object */

    private Color hexToColour(final String initialHexVal) {

        // private Color hexToColour(String hexVal) {

        String hexVal;

        int red, green, blue;

        

        // if (hexVal.startsWith("#")) {

        // if (initialHexVal.startsWith("#")) {

        if (initialHexVal.charAt(0) == '#') {

            // strip the proceeding # symbol

            // hexVal = new String(hexVal.substring(1, 7));

            hexVal = new String(initialHexVal.substring(1, 7));

        } else {

            hexVal = initialHexVal;

        }

        

        try {

            red = Integer.parseInt(hexVal.substring(0, 2), 16);

            green = Integer.parseInt(hexVal.substring(2, 4), 16);

            blue = Integer.parseInt(hexVal.substring(4, 6), 16);

        } catch (Exception colourErr) {

            // Not a valid number

            red = 255;

            green = 255;

            blue = 255;

        }

        

        return new Color(red, green, blue);

        

    }

    

    /**

     * Set the position of our vertical scrollbar

     * @param pos The position of our vertical scrollbar.

     */

    public void setVerticalScrollbarPos(final int pos) {

        if (vertScroll != null) {

            if (pos < 0 || pos > vertScroll.getMaximum()) {

                return;

            }

            

            vertScroll.setValue(pos);

        }

    }

    

    /**

     * get the position of our vertical scrollbar

     * @return Returns an integer representation of the location of our scrollbar

     */

    public int getVerticalScrollbarPos() {

        int retVal = -1;

        

        if (vertScroll != null) {

            // return vertScroll.getValue();

            retVal = vertScroll.getValue();

        }

        

        // return -1;

        return retVal;

    }

    

    /**
     * Set the column-width of our magic widget! (That's the JMText widget,
     * Y'know)
     * @param newCols The number of columns this component should contain.
     */

    public void setColumns(final int newCols) {

        columns = newCols;

    }

    

    /**

     * Calculate a preferred size from the text we have

     * @return Returns a dimension representative of the preferred size of our component.

     */

    public Dimension getPreferredSize() {
        
        // This is ripped directly from getRowsAndColumns()
        // Dimension size = getSize();
        final String test =java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("the_quick_brown_fox_jumps_over_the_lazy_dog");
        // int tempRows, tempCols;
        int total = 0;
        final FontMetrics fMetric = getFontMetrics(getFont());
        int fontWidth = 0;
        int tempWidth = 0;
        
        // We'll set a tempHeight of of the size of our pending text first
        int tempHeight = (pendingText.size() + 2) * (fMetric.getHeight() + fMetric.getLeading());
        
        if (cursorPos != null) {
            // System.out.println("Size determined from cursorPos.");
            tempHeight = (cursorPos.y + 1) * (fMetric.getHeight() + fMetric.getLeading());
        }
        
        try {
            total = fMetric.stringWidth(test);
        } catch (Exception e) {
            System.out.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("JMText_exception_getting_average_character_width"));
        }
        
        fontWidth = (int)(total /= test.length());
        if (!pendingText.isEmpty()) {
            int longest = 0;
            String testString;
            
            final int pTextSize = pendingText.size();
            
            // for (int index = 0; index < pendingText.size(); index++) {
            for (int i = 0; i < pTextSize; i++) {
                testString = pendingText.elementAt(i).toString();
                
                if (testString.length() > longest) {
                    longest = testString.length();
                }
            }
            
            // System.out.println("longest from pendingText: " + longest);
            tempWidth = longest;
        }
        
        if (pendingText.isEmpty() && notified) {
            // We can base this one on the actual long of our string!
            int longestLine = 0;
            String testString;
            final int tCPos = cursorPos.y;
            
            // for (int index = 0; index < cursorPos.clickY; index++) {
            for (int i = 0; i < tCPos; i++) {
                // testString = new String(textChars[index]);
                testString = textChars[i].toString();
                if (testString.trim().length() > longestLine) {
                    longestLine = i;
                }
            }
            fontWidth = 1;  // We do this because we don't need the formula!
            testString = (new String(textChars[longestLine])).trim();
            tempWidth = fMetric.stringWidth(testString) * 3;
        }
        
        return new Dimension((tempWidth * fontWidth), tempHeight);
    }
    
    /** Make the given colour brighter */

    private Color brighter(final Color start) {

        int red = start.getRed();

        int green = start.getGreen();

        int blue = start.getBlue();

        

        red = (int)((255 - red) / 2) + red;

        green = (int)((255 - green) / 2) + green;

        blue = (int)((255 - blue) / 2) + blue;

        

        return new Color(red, green, blue);

    }

    

    /** Make the given colour darker */

    private Color darker(final Color start) {

        int red = start.getRed();

        int green = start.getGreen();

        int blue = start.getBlue();

        

        red = (int)(red / 2);

        green = (int)(green / 2);

        blue = (int)(blue / 2);

        

        return new Color(red, green, blue);

    }

    

}



/** An inner class simply to subclass the canvas (to catch paint() events) */

class LittleCanvas extends Canvas {

// class LittleCanvas extends javax.swing.JPanel {

    

//    public LittleCanvas() {

//    }

    

    /**
     * 
     * @param lcDisplay 
     */
    public void paint(final Graphics lcDisplay) {

        this.getParent().paint(lcDisplay);

    }

}



