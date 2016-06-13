/**
 * The Reusable dialogue box: OKBox.java displays program messages to the user
 * with the possibility of including icons, text, and/or response buttons $Id:
 * OKBox.java,v 1.9 2010/05/10 02:37:44 jeffnik Exp $
 */

/* JamochaMUD, a Muck/Mud client program
 * Copyright (C) 1998-2004  Jeff Robinson
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

import java.util.StringTokenizer;
import java.util.Vector;

/*
 * The Reusable dialogue box:
 * OKBox.java displays program messages to the user with the possibility
 * of including icons, text, and/or response buttons
 * @version $Id: OKBox.java,v 1.10 2014/05/20 02:18:31 jeffnik Exp $
 * @author Jeff Robinson
 */
/**
 * The Reusable dialogue box: OKBox.java displays program messages to the user
 * with the possibility of including icons, text, and/or response buttons
 */
public class OKBox extends Dialog implements ActionListener {

    // Variables
    /**
     * The icon to be displayed in this dialogue.
     */
    Image icon;		// The icon for the OKBox (if applicable)
    String buttonChoice;	// The label of button that was chosen by the user

    // Vector buttonLabels;	// List of the labels to be applied to the buttons
    /**
     * Do not show an icon in the message box
     */
    public static final int NO_ICON = 0;
    /**
     * Show a custom icon in the dialogue box.
     */
    public static final int CUSTOM_ICON = 1;
    /**
     * Show a custom icon in the message box
     */
    public static final int QUESTION_ICON = 2;
    /**
     * Show an "Alert" icon in the message box.
     */
    public static final int ALERT_ICON = 3;
    /**
     * Show an "information" icon in the dialogue box.
     */
    public static final int INFORMATION_ICON = 4;
    // A couple generic defaults for folks
    /**
     * Okay button
     */
    public static final String OKAY = java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("Okay");
    /**
     * Cancel Button
     */
    public static final String CANCEL = java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("Cancel");
    // private int charWidth = 40;     // Average number of characters per message line
    final private BorderLayout layout = new BorderLayout(5, 5); // Our layout manager for the OKBox
    private Canvas iconCanvas;	// The canvas that will contain our icon (if applicable)
    private Canvas textCanvas;	// The canvas that will contain our user-message
    private Panel buttonPanel;	// This panel will contain the buttons in a flow-layout 
    // type of fashion
    // private TextArea messageText = new TextArea(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString(""), 1, 1, TextArea.SCROLLBARS_NONE);   // a text area to contain the user-message
    private TextArea messageText; // a text area to contain the user-message
    private Vector userMessage = new Vector(0, 1);	// Message before preparation for output
    private Vector buttonLabels = new Vector(0, 1);      // Vector of the button labels
    private Button tempButton, okayButton;
    private String finalChoice;
    private Frame parentFrame;
    private static final boolean DEBUG = false;

    // Constructor(s)
    /**
     * The Reusable dialogue box: OKBox.java displays program messages to the
     * user with the possibility of including icons, text, and/or response
     * buttons
     *
     * @param parent The parent frame for this dialogue
     */
    public OKBox(Frame parent) {

        this(parent, java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString(""));

    }

    /**
     * The Reusable dialogue box: OKBox.java displays program messages to the
     * user with the possibility of including icons, text, and/or response
     * buttons
     *
     * @param parent The parent frame for this dialogue
     * @param modalBox Indicate the modal status of this dialogue
     * <CODE>true</CODE> - Dialogue is modal <CODE>false</CODE> - Dialogue is
     * non-modal
     */
    public OKBox(Frame parent, boolean modalBox) {

        this(parent, java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString(""), modalBox);

    }

    /**
     * The Reusable dialogue box: OKBox.java displays program messages to the
     * user with the possibility of including icons, text, and/or response
     * buttons
     *
     * @param title Title to be used for this dialogue
     * @param parent The parent frame for this dialogue
     */
    public OKBox(Frame parent, String title) {

        this(parent, title, false);

    }

    /**
     * The Reusable dialogue box: OKBox.java displays program messages to the
     * user with the possibility of including icons, text, and/or response
     * buttons
     *
     * @param parent The parent frame of this dialogue
     * @param title The title for this dialog
     * @param modalBox Indicate the modal status of this dialogue
     * <CODE>true</CODE> - Dialogue is modal <CODE>false</CODE> - Dialogue is
     * non-modal
     */
    public OKBox(Frame parent, String title, boolean modalBox) {

        this(parent, title, modalBox, java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString(""));

    }

    /**
     * The Reusable dialogue box: OKBox.java displays program messages to the
     * user with the possibility of including icons, text, and/or response
     * buttons
     *
     * @param title Title of this dialogue
     * @param boxMessage Message to be displayed in this dialogue
     * @param parent The parent frame for this dialogue
     * @param modalBox Indicate the modal status of this dialogue
     * <CODE>true</CODE> - Dialogue is modal <CODE>false</CODE> - Dialogue is
     * non-modal
     */
    public OKBox(Frame parent, String title, boolean modalBox, String boxMessage) {

        this(parent, title, modalBox, boxMessage, NO_ICON);

    }

    /**
     * The Reusable dialogue box: OKBox.java displays program messages to the
     * user with the possibility of including icons, text, and/or response
     * buttons
     *
     * @param title Title of this dialogue
     * @param boxMessage Message to be displayed by this dialogue
     * @param iconType The type of icon to be displayed by this dialogue
     * @param parent The parent frame for this dialogue
     * @param modalBox Indicate the modal status of this dialogue
     * <CODE>true</CODE> - Dialogue is modal <CODE>false</CODE> - Dialogue is
     * non-modal
     */
    public OKBox(Frame parent, String title, boolean modalBox, String boxMessage, int iconType) {

        this(parent, title, modalBox, boxMessage, iconType, java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString(""));

    }

    /**
     * The Reusable dialogue box: OKBox.java displays program messages to the
     * user with the possibility of including icons, text, and/or response
     * buttons
     *
     * @param title The title to be displayed by this dialogue
     * @param boxMessage The message to be displayed by this dialogue
     * @param buttons The buttons to be displayed on this dialogue box.
     * @param parent The parent frame for this dialogue
     * @param modalBox Indicate the modal status of this dialogue
     * <CODE>true</CODE> - Dialogue is modal <CODE>false</CODE> - Dialogue is
     * non-modal
     */
    public OKBox(Frame parent, String title, boolean modalBox, String boxMessage, String buttons) {

        this(parent, title, modalBox, boxMessage, NO_ICON, buttons);

    }

    /**
     * The Reusable dialogue box: OKBox.java displays program messages to the
     * user with the possibility of including icons, text, and/or response
     * buttons
     *
     * @param title The title to be used by this dialogue box
     * @param boxMessage The message to be displayed by this dialogue box.
     * @param iconType The icon type to be used by this dialogue
     * @param buttons The buttons to be used by this dialogue
     * @param parent The parent frame for this dialogue
     * @param modalBox Indicate the modal status of this dialogue
     * <CODE>true</CODE> - Dialogue is modal <CODE>false</CODE> - Dialogue is
     * non-modal
     */
    public OKBox(Frame parent, String title, boolean modalBox, String boxMessage, int iconType, String buttons) {

        super(parent, title, modalBox);

        // Because Dialog does not have a parameter-less constructor,
        // we have to call its basic constructor ourselves, or we get
        // a compiling error of &quot;No constructor matching Dialog()"
        this.parentFrame = parent;

        // messageText = new TextArea(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString(""), 1, 1, TextArea.SCROLLBARS_NONE);
        messageText = new TextArea(boxMessage);

        // Time to set up our new OKBox
        this.setLayout(layout);

        iconCanvas = new Canvas();

        Panel iconPanel = new Panel();

        textCanvas = new Canvas();

        // Panel textPanel = new Panel();
        // Panel buttonPanel = new Panel();
        buttonPanel = new Panel();

        add(iconPanel, BorderLayout.WEST);

        // To get things up and running we'll just use a TextArea for now.
        // Fix this XXX
        // add(textPanel, BorderLayout.CENTER);
        add(messageText, BorderLayout.CENTER);

        add(buttonPanel, BorderLayout.SOUTH);

        if (iconType > 0) {

            // We'll nab the icon and use it to help use determine the default size
            icon = getBoxIcon(iconType);

        }

        // Add any text if applicable
        if (boxMessage != null && !boxMessage.equals("")) {

            append(boxMessage);

        }

        // Layout text if we have any
        layoutText();

        // Now pack everything nice and snug!
        pack();

        // We'll set the default location as being centred over the parent window,
        // and if the programmer doesn't like it they can always change it with setLocation!
        // setLocation(PosTools.findCentre(parent,this));
        Point screenPos = PosTools.findCenter(parent, this);

        if (screenPos.x < 0) {

            screenPos.x = 0;

        }

        if (screenPos.y < 0) {

            screenPos.y = 0;

        }

        setLocation(screenPos);

    }

    // Handle the events
    /**
     * This method captures the button that was pressed by the user and records
     * the information internally. The dialogue is the set to be hidden.
     *
     * @param event
     */
    public void actionPerformed(final ActionEvent event) {

        if (event != null) {
            actionPerformedEvents(event);
        }
//        setResult(event.getActionCommand());
//
//        this.setVisible(false);

    }

    private void actionPerformedEvents(final ActionEvent event) {
        // We record the button that is pressed and then dismiss this dialogue
        setResult(event.getActionCommand());

        this.setVisible(false);

    }

    /**
     * Returns a string representing the current dialogue message
     *
     * @return
     */
    String getText() {
        // Convert user message to text and return to user

        StringBuffer retText;
        retText = new StringBuffer();

        if (!userMessage.isEmpty()) {
            int count;
            count = userMessage.size();

            for (int i = 0; i < count; i++) {
                retText.append(userMessage.elementAt(i));
            }
        }

        return retText.toString();
    }

    /**
     * We'll pull the icon (from wherever it is supposed to be) and write it
     * onto the correct canvas (also keeping a copy in memory)
     */
    private synchronized Image getBoxIcon(final int imageType) {

        // In the future we'll determine the icon type from 'imageType'
        final Image image = Toolkit.getDefaultToolkit().getImage(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("kehza.gif"));

        return image;

    }

    // Draw the contents to the screen
    /**
     * Paint a given icon to the dialogue
     *
     * @param iconGraph The graphic area we are working with.
     */
    public void paint(final Graphics iconGraph) {

        if (icon != null) {

            iconGraph.drawImage(icon, 5, 25, this);

        }

    }

    /**
     * We'll do a virtual run-through of the text available so that we'll be
     * able to size the window before we try to print to it. By doing this we
     * can set up our vector so it neatly parses the message
     */
    private void formatText() {

        if (userMessage.size() < 1) {

            return;

        }

        int mChars = 0;

        // int x = 60;
        // int y = 0;
        String tempString;

        messageText.setText(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString(""));

        for (int i = 0; i < userMessage.size(); i++) {

            tempString = (String) userMessage.elementAt(i);

            messageText.append(tempString + '\n');

            // mChars += tempString.length();
            if (tempString.length() > mChars) {

                mChars = tempString.length();

            }

            if (DEBUG) {

                System.err.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("Writing:_") + tempString + "\n");

            }

        }

        messageText.setEditable(false);

        /*
         if (mChars > charWidth) {
         y = (int)(mChars / charWidth);
         } else {
         charWidth = mChars;
         y = 1;
         }
         */
        if (DEBUG) {

            System.err.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("OKBox.formatText()_believes_our_maximum_column_size_to_be:_") + mChars);

        }

        // messageText.setColumns(charWidth);
        messageText.setColumns(mChars);

        // messageText.setRows(y);
        // System.out.println("OKBox set for " + mChars + " lines");
        // System.out.println("y = " + y);
        // messageText.setMaxRows(mChars);
        // messageText.setMaxRows(y * 3);
        // messageText.setMaxRows(y + 1);
        if (DEBUG) {

            System.err.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("OKBox.formatText()_setting_number_of_rows_to:_") + (userMessage.size() + 1));

        }

        // messageText.setRows(y + 1);
        messageText.setRows(userMessage.size() + 1);

        messageText.setCaretPosition(0);

    }

    /**
     * Set the text for the message (erasing any previous contents)
     *
     * @param text The text to display in our dialogue box.
     */
    public synchronized void setText(final String text) {

        // if (userMessage.size() > 0) {
        if (!userMessage.isEmpty()) {
            userMessage.removeAllElements();

        }

        append(text);

    }

    /**
     * Format the buttons for display
     */
    private void formatButtons() {

        // Clear the button panel
        if (buttonPanel != null) {

            buttonPanel.removeAll();

        }

        // if (buttons.equals("")) {
        if (buttonLabels.size() < 1) {

            // We weren't passed a string of buttons, so we'll default to 'OK'
            buttonLabels.addElement(OKAY);

        }

        for (int i = 0; i < buttonLabels.size(); i++) {

            if (buttonLabels.elementAt(i).equals(OKAY)) {

                // We create this button separately so we can give
                // it focus when we show the dialogue to the users
                okayButton = new Button(OKAY);

                buttonPanel.add(okayButton);

                okayButton.addActionListener(this);

            } else {

                tempButton = new Button((buttonLabels.elementAt(i)).toString());

                buttonPanel.add(tempButton);

                tempButton.addActionListener(this);

            }

        }

    }

    /**
     * Add a button to the list of buttons to be displayed
     *
     * @param name Name of the button to add to our dialogue
     */
    public void addButton(final String name) {

        if (!name.equals(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString(""))) {

            buttonLabels.addElement(name);

        }

    }

    /**
     * Append a line of text to a message, or create a new message if one does
     * not exist
     *
     * @param text Text to be appended to the dialogue
     */
    public synchronized void append(final String text) {

        if (DEBUG) {
            System.err.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("We_received_the_message_as_so:") + text);
        }

        // First do a check to see if the message already contains
        // any \n characters... we'll split them ourselves
        // final StringTokenizer mTok = new StringTokenizer(text, "\n", false);
        StringTokenizer mTok = new StringTokenizer(text, "\n", true);

        while (mTok.hasMoreTokens()) {
            // userMessage.addElement(text);
            userMessage.addElement(mTok.nextToken());
            if (DEBUG) {
                System.err.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("->") + userMessage.elementAt(userMessage.size() - 1) + java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("<-"));
            }
        }
    }

    private synchronized void layoutText() {

    }

    /**
     * Return the user's selection. This may be a little tricky as We are not
     * necessarily confined to simply having "Okay" and "Cancel" buttons. This
     * dialogue is extensible!
     *
     * @return A string indicated the user's button choice.
     */
    public String getResult() {
        return finalChoice;
    }

    /**
     * Override Dialog's 'show' command so that we can format text first!
     *
     * @deprecated This method is deprecated
     */
    @Override
    public void show() {
        setupDialogue();
        super.show();
        okayButton.requestFocus();
    }

    /**
     * Allow the dialogue box to be shown or hidden
     *
     * @param status <CODE>true</CODE> - make this dialogue visible
     * <CODE>false</CODE> - hide this dialogue
     */
    @Override
    public void setVisible(final boolean status) {

        if (status) {

            this.show();

        } else {

            super.setVisible(false);

        }

    }

    /**
     * This functions the same as the show command except for the fact that it
     * automatically centres the dialogue over its parent
     *
     * @deprecated use showCentered() instead
     */
    public void showCentred() {

        showCentered();

    }

    /**
     * This functions the same as the show command except for the fact that it
     * automatically centres the dialogue over its parent
     */
    public void showCentered() {

        setupDialogue();

        setLocation(PosTools.findCenter(parentFrame, this));

        super.show();

    }

    /**
     * Set up the dialogue by formating the text and buttons and then packing
     * everything nice and snug
     */
    private void setupDialogue() {

        formatText();

        formatButtons();

        pack();

    }

    /**
     *
     * @param choice
     */
    private void setResult(final String choice) {

        finalChoice = choice;

    }
}
