/**
 * DataIn, text input window $Id: DataIn.java,v 1.37 2012/03/11 03:44:51 jeffnik
 * Exp $
 */

/* JamochaMUD, a Muck/Mud client program
 * Copyright (C) 1998-2008  Jeff Robinson
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * version 2, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package anecho.JamochaMUD;

import anecho.JamochaMUD.TinyFugue.JMTFKeys;
import anecho.extranet.event.TelnetEvent;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import anecho.extranet.event.TelnetEventListener;

import anecho.gui.SyncFrame;
import net.sf.wraplog.AbstractLogger;
import net.sf.wraplog.NoneLogger;
import net.sf.wraplog.SystemLogger;
// import java.io.File;

/**
 * This is the input window where the user may type to send info to the
 * MUCK/MUD. In addition, if features scrolling of long messages and a
 * variable-length command 'recall' feature implemented by a right mouse-click.
 *
 * @version $Id: DataIn.java,v 1.39 2015/08/30 22:43:32 jeffnik Exp $
 * @author Jeff Robinson
 */
public class DataIn extends SyncFrame implements ActionListener, KeyListener, MouseListener, WindowListener, TelnetEventListener {

    /**
     * This variable is for the pop-up menu for the command history
     */
    private transient final PopupMenu backTrack;
    // private String backComm[];
    /**
     * This is the component that contains the input text object
     */
    private transient final Component dataText;
    /**
     * This is the pane used by the text object
     */
    private transient Component dataInPane;
    /**
     * Our JMConfig file
     */
    private transient final JMConfig settings;
    /**
     * The connection handler
     */
    private transient CHandler connHandler;
    /**
     * Number of history entries allowed
     */
    private transient int limit;
    /**
     * A quick-check variable used to determine if the interface is AWT or Swing
     */
    private transient final boolean useSwing;
    /**
     * Allows for the enabling and disabling of debugging output
     */
    private static final boolean DEBUG = false;
    /**
     * The listener used for the AWT Popup Menu
     */
    private transient ActionListener pml; // Pop-up menu listener
    /**
     * The transient history variable contains position -1 in our command
     * history
     */
    private transient String transHistory = "";
    /**
     * A vector containing the output history
     */
    private transient final Vector historyV;
    /**
     * An object used by the spellchecker
     */
    private transient Object spellCheckObj;   // used specifically for our spell-checker
    /**
     * This variable tracks our location in the command history. A position of
     * -1 indicates that we are sitting at the current input line, which will be
     * stored in a String if we move off of it.
     */
    private transient int historyLoc = -1;
    /**
     * A variable representing the use of ASPELL-style dictionaries
     */
    private static final int ASPELL = 0;
    /**
     * A variable representing the use of MYSPELL-style dictionaries
     */
    public static final int MYSPELL = 1; // OpenOffice .zip dictionaries
    /**
     * A variable indicating the style of dictionary to use (currently myspell).
     */
    private static final int DICTTYPE = MYSPELL;   // Type of dictionary to use
    /**
     * The up direction
     */
    public static final int UP = 0;
    /**
     * The down direction
     */
    public static final int DOWN = 1;
    
    private String lastProposal = null;
    
    private String initialInput = null;
    
    private java.util.List<String> commandList = null;

    private final AbstractLogger logger;

    /**
     *
     * The constructor for DataIn, producing a new data input area.
     *
     */
    public DataIn() {

        super(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("JAMOCHAMUD"));

        settings = JMConfig.getInstance();

        // this.connHandler = settings.getConnectionHandler();
        connHandler = CHandler.getInstance();

        if (DEBUG) {
            logger = new SystemLogger();
        } else {
            logger = new NoneLogger();
        }

        historyV = new Vector();
        
        commandList = new ArrayList<String>();
        String commandsFile = settings.getJMString(JMConfig.COMMANDS_FILE);
        String userHome = System.getProperty("user.home");
        commandsFile = commandsFile.replace("~", userHome);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(commandsFile));
            while(true) {
                String line = br.readLine();
                if(line == null) {
                    break;
                }
                commandList.add(line);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // tfCommand = new JMTFCommands();
        // final JMTFCommands tfCommand = JMTFCommands.getInstance();
        // JMTFKeys tfKeys = JMTFKeys.getInstance();        // Trying out the JMTFKeys singleton
        addWindowListener(this);

        // Set our limit from our configuration
        limit = settings.getJMint(JMConfig.HISTORYLENGTH);

        // We don't have a previous limit the integer returned will
        // be -1, which we'll happily change to 10.
        // If the previous limit was 0, that means the user has disabled the
        // history intentionally and we'll leave it alone.
        if (limit < 0) {
            limit = 10;
            settings.setJMValue(JMConfig.HISTORYLENGTH, limit);
        }

        // Set Gridbag layout
        final GridBagConstraints constraints = new GridBagConstraints();

        backTrack = new PopupMenu();

        // Add the text field to the box.
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.BOTH;

        useSwing = settings.getJMboolean(JMConfig.USESWINGENTRY);

        if (useSwing) {
            logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Swing-based_Text_entry_initialising..."));

            // Use a Swing component instead
            dataText = new anecho.gui.JMSwingEntry();
            // ((anecho.gui.JMSwingEntry)dataText).setLineWrap(true);

            // Old JTextArea JMSwingEntry
//            dataText = new anecho.gui.JMSwingEntry("", 70, 3);
//            ((anecho.gui.JMSwingEntry)dataText).setLineWrap(true);
            ((anecho.gui.JMSwingEntry) dataText).addKeyListener(this);
            ((anecho.gui.JMSwingEntry) dataText).addMouseListener(this);	// Adds MouseListener to data entry area
            ((anecho.gui.JMSwingEntry) dataText).setRequestFocusEnabled(true);
            ((anecho.gui.JMSwingEntry) dataText).setAntiAliasing(settings.getJMboolean(JMConfig.ANTIALIAS));

            dataInPane = new javax.swing.JScrollPane((anecho.gui.JMSwingEntry) dataText);
            ((javax.swing.JScrollPane) dataInPane).setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            ((javax.swing.JScrollPane) dataInPane).setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            add((javax.swing.JScrollPane) dataInPane);

            // Determine if spellcheck is enabled
            // Find the proper settings file Fix Me XXX
            final java.util.prefs.Preferences prefs = java.util.prefs.Preferences.userNodeForPackage(this.getClass());

            final boolean spellEnabled = prefs.getBoolean(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("SpellCheckEnabled"), false);

            logger.debug("Attempting to call setSpellCheck(" + spellEnabled + ")");

            if (spellEnabled) {
                setSpellCheck(spellEnabled);
            }

            logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Done."));

        } else {

            // Standard AWT TextArea
            dataText = (Component) new TextArea("", 3, 70, TextArea.SCROLLBARS_VERTICAL_ONLY);
            ((TextArea) dataText).addKeyListener(this);
            ((TextArea) dataText).add(backTrack); 		// Adds PopupMenu to data entry area
            ((TextArea) dataText).addMouseListener(this);	// Adds MouseListener to data entry area
            add((TextArea) dataText);

        }

        backTrack.addActionListener(this);

        pack();

        // Display macroBar
        logger.debug("DataIn - settings foreground and background colours");

        dataText.setForeground(settings.getJMColor(JMConfig.FOREGROUNDCOLOUR));
        dataText.setBackground(settings.getJMColor(JMConfig.BACKGROUNDCOLOUR));
        dataText.setFont(settings.getJMFont(JMConfig.FONTFACE));

        if (DEBUG) {
            System.err.println("Foreground colours: ");
            System.err.println(dataText.getForeground().getRed());
            System.err.println(dataText.getForeground().getGreen());
            System.err.println(dataText.getForeground().getBlue());
            System.err.println("Background colours: ");
            System.err.println(dataText.getBackground().getRed());
            System.err.println(dataText.getBackground().getGreen());
            System.err.println(dataText.getBackground().getBlue());
        }
    }

    /**
     * Sends the text currently in the data entry window to the currently view
     * MU*
     */
    private void sendCurrentText() {

        if (useSwing) {
            jMSendText(((anecho.gui.JMSwingEntry) dataText).getText());
        } else {
            jMSendText(((TextArea) dataText).getText());
        }

    }

    /**
     * Scroll the MU* output one "page" in the given direction
     *
     * @param direction
     */
    private void scrollPage(final int direction) {
        // Scroll the main window up one "page"

        logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("DataIn.java:_Scroll_main_window_up_one_page."));

        if (useSwing) {
            final anecho.gui.JMSwingText text = connHandler.getActiveMUDSwingText();
            if (direction == UP) {
                text.movePage(anecho.gui.JMSwingText.PAGEUP);
            } else {
                text.movePage(anecho.gui.JMSwingText.PAGEDOWN);
            }
        } else {
            logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("A_scrolling_method_has_not_yet_been_implemented_for_AWT"));

            // Fix Me XXX
            // Add method for handling AWT event
        }

    }

    /**
     *
     *
     *
     * @param event
     *
     */
    @Override
    public void keyPressed(final KeyEvent event) {

        if (event != null) {
            this.keyPressedEvents(event);
        }

    }
    
    private void doCompletion() {
        if(this.useSwing) {
            anecho.gui.JMSwingEntry entry = (anecho.gui.JMSwingEntry)dataText;
            String text = entry.getText();
            int pos = text.lastIndexOf(" ");
            String prefix = null;
            String suffix = null;
            if(pos >= 0) {
                prefix = text.substring(0, pos + 1);
                suffix = text.substring(pos+1);
            }
            else {
                prefix = "";
                suffix = text;
            }
            
            if(initialInput != null) {
                suffix = initialInput;
            }
            
            java.util.List<String> matched = new ArrayList<String>();
            for(int i = 0; i < historyV.size(); i++) {
                String item = (String) historyV.get(i);
                if(item.startsWith(suffix)) {
                    if(!matched.contains(item)) {
                        matched.add(item);
                    }
                }
            }
            
            for(int i = 0; i < commandList.size(); i++) {
                String item = (String) commandList.get(i);
                if(item.startsWith(suffix)) {
                    if(!matched.contains(item)) {
                        matched.add(item);
                    }
                }
            }
            
            if(matched.size() == 0) {
            }
            else if(matched.size() == 1) {
                suffix = matched.get(0);
            }
            else {
                for(String m : matched) {
                    if(!m.equals(lastProposal)) {
                        initialInput = suffix;
                        suffix = m;
                        lastProposal = m;
                        break;
                    }
                }
            }
            
            text = prefix + suffix;
            entry.setText(text);
        }
    }

    /**
     * This method handles the processing of key events
     *
     * @param event
     */
    private void keyPressedEvents(final KeyEvent event) {
        final int arg = event.getKeyCode();

        if (arg == KeyEvent.VK_ENTER) {

            sendCurrentText();
            event.consume();
        }

        if (arg == KeyEvent.VK_ALT && settings.getJMboolean(JMConfig.ALTFOCUS) && settings.getJMboolean(JMConfig.SPLITVIEW)) {
            // Check to see if the user wants to have to ALT key focus on the menu
            // if (settings.getJMboolean(JMConfig.ALTFOCUS) && settings.getJMboolean(JMConfig.SPLITVIEW)) {

            MuckMain.getInstance().getMainFrame().requestFocus();

        }
        
        if (arg == KeyEvent.VK_TAB) {
            doCompletion();
            
            event.consume();
        }
        else {
            initialInput = null;
        }

        if (event.isShiftDown()) {

            if (shiftEvent(event)) {
                event.consume();
            }
//            if (arg == KeyEvent.VK_PAGE_UP) {
//                this.scrollPage(UP);
//                event.consume();
//            }
//
//            if (arg == KeyEvent.VK_PAGE_DOWN) {
//                this.scrollPage(DOWN);
//                event.consume();
//            }
//
//            if (arg == KeyEvent.VK_UP) {
//                // Scroll the command history up one entry
//                scrollHistory(UP);
//            }
//
//            if (arg == KeyEvent.VK_DOWN) {
//                // Scroll the command history down one entry
//                scrollHistory(DOWN);
//            }

        }

        if (event.isControlDown()) {

            if (controlEvent(event)) {
                event.consume();
            }

//            if (arg == KeyEvent.VK_X) {
//                // 'Supervisor' key was called.
//                // Minimize all the windows
//                hideFramesQuick();
//            }
//
//            if (arg == KeyEvent.VK_Z) {
//                // restore minimized windows
//                restoreMinimizedFrames();
//            }
//
//            if (arg != KeyEvent.VK_LEFT && arg != KeyEvent.VK_RIGHT) {
//
//                // First, check to see if this is for TinyFugue emulation
//                if (settings.getJMboolean(JMConfig.TFKEYEMU) && JMTFKeys.jmTFKeyStroke(event.getKeyCode())) {
//                    event.consume();
//                }
//
//                // We'll send this to MuckMain, to see if
//                // it is supposed to be a menu shortcut
//                // MuckMain tempMain = settings.getMainWindowVariable();
//                if (settings.getJMboolean(JMConfig.ALTFOCUS) && settings.getJMboolean(JMConfig.SPLITVIEW)) {
//                    // final Frame tempMain = settings.getJMFrame(JMConfig.MUCKMAINFRAME);
//                    final Frame tempMain = MuckMain.getInstance().getMainFrame();
//                    tempMain.dispatchEvent(event);
//                }
//
//            }
        }

    }

    /**
     * Data input that has been modified by the shift key to produce non-text
     * results
     *
     * @param event
     * @return <code>true</code> consume the event <code>false</code> do not
     * consume the event
     */
    private boolean shiftEvent(KeyEvent event) {
        final int arg = event.getKeyCode();

        boolean consumed = false;

        if (arg == KeyEvent.VK_PAGE_UP) {
            this.scrollPage(UP);
            // event.consume();
            consumed = true;
        }

        if (arg == KeyEvent.VK_PAGE_DOWN) {
            this.scrollPage(DOWN);
            // event.consume();
            consumed = true;
        }

        if (arg == KeyEvent.VK_UP) {
            // Scroll the command history up one entry
            scrollHistory(UP);
        }

        if (arg == KeyEvent.VK_DOWN) {
            // Scroll the command history down one entry
            scrollHistory(DOWN);
        }

        return consumed;
    }

    /**
     * Data input that has been modified by the Control key to produce non-text
     * results
     *
     * @param event
     * @return <code>true</code> consume the event <code>false</code> do not
     * consume the event
     */
    private boolean controlEvent(KeyEvent event) {
        final int arg = event.getKeyCode();

        boolean consumed = false;

        if (arg == KeyEvent.VK_X) {
            // 'Supervisor' key was called.
            // Minimize all the windows
            hideFramesQuick();
        }

        if (arg == KeyEvent.VK_Z) {
            // restore minimized windows
            restoreMinimizedFrames();
        }

        if (arg != KeyEvent.VK_LEFT && arg != KeyEvent.VK_RIGHT) {

            // First, check to see if this is for TinyFugue emulation
            if (settings.getJMboolean(JMConfig.TFKEYEMU) && JMTFKeys.jmTFKeyStroke(event.getKeyCode())) {
                // event.consume();
                consumed = true;
            }

            // We'll send this to MuckMain, to see if
            // it is supposed to be a menu shortcut
            // MuckMain tempMain = settings.getMainWindowVariable();
            if (settings.getJMboolean(JMConfig.ALTFOCUS) && settings.getJMboolean(JMConfig.SPLITVIEW)) {
                // final Frame tempMain = settings.getJMFrame(JMConfig.MUCKMAINFRAME);
                final Frame tempMain = MuckMain.getInstance().getMainFrame();
                tempMain.dispatchEvent(event);
            }

        }
        return consumed;
    }

    /**
     *
     *
     *
     * @param event
     *
     */
    @Override
    public void keyReleased(final KeyEvent event
    ) {
    }

    /**
     *
     *
     *
     * @param event
     *
     */
    @Override
    public void keyTyped(final KeyEvent event) {

        // We must make one exception, if the user is using TinyFugue emulation
        // so that we do not spool out text if they're releasing the 'pause text' key
        // if (settings.getTFKeyEmu() && event.isControlDown()) {
        if (settings.getJMboolean(JMConfig.TFKEYEMU) && event.isControlDown()) {
            event.consume();
        } else {
            checkPause();
        }

    }

    /**
     *
     *
     *
     * @param event
     *
     */
    @Override
    public void actionPerformed(final ActionEvent event) {

        if (event != null) {
            actionPerformedEvents(event);
        }

//        if (DEBUG) {
//
//            System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("DataIn_received_ActionEvent_") + event);
//
//        }
//
//
//
//        // String arg = event.getActionCommand();
//
//
//
//        final String cmd = event.getActionCommand();
//
//
//
//        int cmdInt = 0;
//
//
//
//        try {
//
//            cmdInt = Integer.parseInt(cmd);
//
//            if (DEBUG) {
//
//                System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Chosen_action_command_is_") + cmd);
//
//            }
//
//            setFromHistory(cmdInt);
//
//        } catch (Exception e) {
//
//            if (DEBUG) {
//
//                System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Problem_getting_action_command_value."));
//
//            }
//
//        }
    }

    private void actionPerformedEvents(final ActionEvent event) {
        logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("DataIn_received_ActionEvent_") + event);

        // String arg = event.getActionCommand();
        final String cmd = event.getActionCommand();

        // int cmdInt = 0;

        try {
            int cmdInt;
            cmdInt = Integer.parseInt(cmd);
            logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Chosen_action_command_is_") + cmd);

            setFromHistory(cmdInt);
        } catch (Exception e) {
            logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Problem_getting_action_command_value."));

        }
    }

// Check for mouse actions
    /**
     *
     *
     *
     * @param evt
     *
     */
    @Override
    public void mousePressed(final MouseEvent evt) {

        checkPopup(evt);

    }

    /**
     *
     *
     *
     * @param evt
     *
     */
    @Override
    public void mouseReleased(final MouseEvent evt) {

        checkPopup(evt);

    }

    /**
     *
     *
     *
     * @param evt
     *
     */
    @Override
    public void mouseClicked(final MouseEvent evt) {

        // This spools out any paused text
        checkPause();

    }

    /**
     *
     *
     *
     * @param evt
     *
     */
    @Override
    public void mouseEntered(final MouseEvent evt) {

    }

    /**
     *
     *
     *
     * @param evt
     *
     */
    @Override
    public void mouseExited(final MouseEvent evt) {

    }

    /**
     * Check for PopupTrigger to display command history
     *
     * @param mouse
     */
    public void checkPopup(final MouseEvent mouse) {

        if (mouse != null) {
            checkPopupEvent(mouse);
        }

    }

    private void checkPopupEvent(final MouseEvent mouse) {
        if (mouse.isPopupTrigger()) {

            // showPopup(this, mouse.getX(), mouse.getY());
            // showPopup(dataText, mouse.getX(), mouse.getY());
            logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Our_original_component_is_") + mouse.getComponent());

            showPopup(mouse.getComponent(), mouse.getX(), mouse.getY());

            mouse.consume();

        }

    }

    /**
     * Display the pop-up menu
     *
     * @param origin The Component that originally received the mouse click.
     * @param xPos The x-position of the mouse within the origin component
     * @param yPos The y-position of the mouse within the origin component
     */
    public void showPopup(final Component origin,
            final int xPos, final int yPos) {
        Component objOrg = origin;
        int xaxis = xPos;
        int yaxis = yPos;

        logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("DataIn.ShowPopup:_origin_object:_") + objOrg);

// if xPos and yPos come in as -1, then we set the popup location from just
// the frame as this method was probably called by an outside class
        if (origin == null && xaxis == -1 && yaxis == -1) {
            logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("DataIn.ShowPopup:_Our_axi_are_both_-1."));

// objOrg = this;
            objOrg = dataText;
            xaxis
                    = (int) (this.getSize().width / 2);
            yaxis
                    = (int) (this.getSize().height / 2);

            logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Our_new_objOrg_is_") + objOrg);
            logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("xaxis:_") + xaxis);
            logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("yaxis:_") + yaxis);

        }

        if (useSwing) {
//            javax.swing.JMenuItem tempItem;
//            final javax.swing.JPopupMenu jpm = new javax.swing.JPopupMenu(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Command_history"));
//            String tempName;
//
//            for (int i = 0; i < historyV.size(); i++) {
//                tempName = historyV.elementAt(i).toString();
//                tempItem = new javax.swing.JMenuItem(tempName);
//                // tempItem.addActionListener(pml);
//                tempItem.addActionListener(this);
//                // tempItem.setActionCommand(i + "");
//                tempItem.setActionCommand(Integer.toString(i));
//                jpm.add(tempItem);
//            }

            final javax.swing.JPopupMenu jpm = buildSwingPopupMenu();

            jpm.show(objOrg, xaxis, yaxis);

        } else {
//            backTrack.removeAll();
//
//            String tempName;
//            java.awt.MenuItem tempItem;
//
//            for (int i = 0; i < historyV.size(); i++) {
//                tempName = historyV.elementAt(i).toString();
//                tempItem = new java.awt.MenuItem(tempName);
//                tempItem.addActionListener(pml);
//                // tempItem.setActionCommand(i + "");
//                tempItem.setActionCommand(Integer.toString(i));
//                backTrack.add(tempItem);
//            }

//            PopupMenu backTrack = buildAWTPopupMenu();
//            backTrack.show(objOrg, xaxis, yaxis);
            buildAWTPopupMenu(objOrg, xaxis, yaxis);
        }

    }

    /**
     * Compiles an AWT PopupMenu based on the command history
     *
     * @param objOrg The component this object will be centred on
     * @param xaxis The x-position of the mouse-click
     * @param yaxis The y-position of the mouse-click
     */
    private void buildAWTPopupMenu(final Component objOrg,
            final int xaxis, final int yaxis) {
        // It seems (currently) that we have to have the backTrack
        // variable defined so that it can be added to the text area
        backTrack.removeAll();

        String tempName;

        java.awt.MenuItem tempItem;
        // backTrack = new PopupMenu();

        for (int i = 0; i < historyV.size(); i++) {
            tempName = historyV.elementAt(i).toString();
            tempItem = new java.awt.MenuItem(tempName);
            tempItem.addActionListener(pml);
            tempItem.setActionCommand(Integer.toString(i));
            backTrack.add(tempItem);
        }

        if (objOrg != null) {
            backTrack.show(objOrg, xaxis, yaxis);
        }

        // return backTrack;
    }

    /**
     * Compiles a JPopupMenu from the command history
     *
     * @return a JPopupMenu with command history entries
     */
    private javax.swing.JPopupMenu buildSwingPopupMenu() {
        javax.swing.JMenuItem tempItem;
        final javax.swing.JPopupMenu jpm = new javax.swing.JPopupMenu(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Command_history"));
        String tempName;

        for (int i = 0; i
                < historyV.size(); i++) {
            tempName = historyV.elementAt(i).toString();
            tempItem = new javax.swing.JMenuItem(tempName);
            // tempItem.addActionListener(pml);
            tempItem.addActionListener(this);
            // tempItem.setActionCommand(i + "");
            tempItem.setActionCommand(Integer.toString(i));
            jpm.add(tempItem);
        }

        return jpm;
    }

    /**
     * Bring the frame into focus so the user may type
     *
     * @param event
     */
    @Override
    public void windowActivated(final WindowEvent event) {
        // First we insert a 6 millisecond kludge to make
        // up for the requestFocus timing problem in Java 1.1.7+
        final long startTime = java.lang.System.currentTimeMillis();
        long currentTime = java.lang.System.currentTimeMillis();

        while ((currentTime - startTime) < 7) {
            // Just an empty loop
            currentTime = java.lang.System.currentTimeMillis();
        }

// Now request focus
        if (useSwing) {
            logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("We_asked_for_a_requestFocus"));
            ((anecho.gui.JMSwingEntry) dataText).requestFocus();
        } else {
            ((TextArea) dataText).requestFocus();
        }

    }

    /**
     *
     * @param event
     */
    @Override
    public void windowClosed(final WindowEvent event) {
    }

    /**
     *
     * @param event
     */
    @Override
    public void windowClosing(final WindowEvent event) {
    }

    /**
     *
     * @param event
     */
    @Override
    public void windowDeactivated(final WindowEvent event) {
    }

    /**
     *
     * @param event
     */
    @Override
    public void windowDeiconified(final WindowEvent event) {
    }

    /**
     *
     * @param event
     */
    @Override
    public void windowIconified(final WindowEvent event) {
        // record the frame into the hashtable
        settings.setJMValue(JMConfig.DATABAR, getBounds());
    }

    /**
     *
     *
     *
     * @param event
     *
     */
    @Override
    public void windowOpened(final WindowEvent event) {
    }

    // Our custom TelnetEventListener
    /**
     *
     * Currently not used.
     *
     * @param event A telnet event.
     *
     */
    @Override
    public void telnetMessageReceived(final TelnetEvent event) {

//        // System.out.println("Confirming that DataIn has received: " + event);
//        // System.out.println("With our message being: " + event.getMessage());
//        // String message = event.getMessage();
//        // if (message.equals("IAC DO TELOPT_ECHO")) {
//        // dataText.setEchoChar('*');
//        // }
//
//        // if (message.equals("IAC DONT TELOPT_ECHO")) {
//        // dataText.setEchoChar('\u0000');
//        // }
    }

    /**
     *
     * This is a generic bit to access the
     *
     * ResReader.class, for localization
     *
     * (Multi-language support)
     *
     */
//    private String resReader(final String itemTarget) {
//        final ResReader reader = new ResReader();
//        // return reader.langString("JamochaMUDBundle", itemTarget);
//        return reader.langString(JMConfig.BUNDLEBASE, itemTarget);
//    }
    /**
     *
     * Method to hide all the frames. The 'supervisor' function
     *
     */
    public void hideFramesQuick() {

        // Hide all the frames,
        // the supervisor's here!
        setTitle(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("."));

        toBack();

        // We'll unsync the databar, otherwise bringing it up to the
        // front may result in all the other windows showing up again.  Eeek!
        setSync(false);

    }

    /**
     *
     * Restore frames hidden by the 'Supervisor' function
     *
     */
    public void restoreMinimizedFrames() {

        // Restore the frames that
        // were minimized by the 'supervisor' key
        // Fix this XXX
        // MuckMain.textWindow.setVisible(true);
        // Restore frame icons
        // dataBar.setIconImage(MuckConn.jMUDImage);
        // MuckMain.textWindow.setIconImage(MuckConn.jMUDImage);
        // Querry the MuckMain **** menu, to
        // see if we should set the Macros visible
        // Fix this XXX
        // if (MuckMain.tWMacro.getState()) {
        // tWMacro is set to 'true', so show the Macro Frame
        // MuckConn.jmMacros.setVisible(true);
        //}
        // Fix this XXX
        // if (MuckMain.tWSyncWindowsItem.getState()) {
        // setSync(true);
        // }
        // now reset the title
        // resetTitles();
    }

    /**
     * Add a new item to the history vector
     */
    private void appendToHistory(final String str) {

        // Add the string to our history vector
        historyV.addElement(str);

        if (historyV.size() > limit) {
            historyV.removeElementAt(0);

            // we have to bump our history marker to keep in sync with
            // losing an entry
            if (historyLoc > -1) {
                historyLoc--;
            }

        }

        logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Successfully_reached_the_end_of_updating_history."));

    }

    /**
     *
     * This is an &quot;empty" method that allows other classes to request that
     * we send our existing text to the MU*. This doesn't happen often, but can
     * be a handy ability to have
     *
     * @deprecated The SendText method in CHandler should now be used
     */
    public void jMSendText() {

        if (useSwing) {

            jMSendText(((anecho.gui.JMSwingEntry) dataText).getText());

        } else {

            jMSendText(((TextArea) dataText).getText());

        }

    }

    /**
     * Package up the data to be sent to the MU*, parsing off any yucky
     * characters such as 'new lines', etc., also determining via
     * user-preference whether they should be sent in Unicode or standard ASCII
     * format
     *
     * NOTE: This method has been made <code>private</code> as of 2008-03-09.
     * Any methods that send text to the connections should do it via the
     * SendText method of CHandler.
     *
     * @param outGoing The text to be sent to the currently active MU*.
     */
    private void jMSendText(final String outGoing) {

        final String sendStr = outGoing;

        // Safety first!
        if (connHandler == null) {
            connHandler = CHandler.getInstance();
        }

        logger.debug("DataIn.jmSendText: Sending text: " + outGoing);

// Now add the command to the Popupmenu
// addPopUpEntry(sendStr);
        if (sendStr.length() > 0) {

            appendToHistory(sendStr);

        } // Added as part of moving most of this method into the CHandler class

// Send through any output plug-ins
// final String prePlugText = EnumPlugIns.callPlugin(sendStr, EnumPlugIns.OUTPUT, connHandler.getActiveMUHandle());
        final String prePlugText = EnumPlugIns.callPlugin(sendStr, EnumPlugIns.INPUT, connHandler.getActiveMUHandle());
        // final String plugText = "";

        if (prePlugText == null) {
            // The plug-in has used the text and we don't need to pass it on.
            logger.debug("DataIn.jMSendText prePlugText is null.  Returning.");

// This still needs to be sent to localEcho.  Fix Me XXX
            clearTextArea();
            sendLocalEcho(outGoing);
            return;
        }

// Send the text to the CHandler for the current visible MU*
// connHandler.sendText(sendStr);
        connHandler.sendText(prePlugText);

        sendLocalEcho(prePlugText);

//        // If local echo is active, append it to the user's screen as well
//        // if (settings.isLocalEchoEnabled()) {
//        if (settings.getJMboolean(JMConfig.LOCALECHO) && connHandler.isActiveMUEchoState()) {
//
//            // First run our localecho through our list of plug-ins in the event
//            // that we need to modify the text.
//            // This may need to be moved in the future. XXX
////            final String prePlugText = EnumPlugIns.callPlugin(sendStr, EnumPlugIns.OUTPUT, connHandler.getActiveMUHandle());
////            String plugText = "";
////
////            if (prePlugText == null) {
////                // The plug-in has used the text and we don't need to pass it on.
////                if (DEBUG) {
////                    System.err.println("DataIn.jMSendText prePlugText is null.  Returning.");
////                }
////                return;
////            }
//
//            if (!prePlugText.equals("")) {
//                plugText = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("localEchoPrepend") + prePlugText;
//            }
//
//            if (DEBUG) {
//                System.err.println("DataIn.jMSendText returning: " + plugText);
//            }
//
//            // if (connHandler.isActiveMUEchoState()) {
//            if (settings.getJMboolean(JMConfig.USESWING)) {
//                anecho.gui.JMSwingText text;
//                text = connHandler.getActiveMUDSwingText();
//                // text.append(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("localEchoPrepend"));
//                // text.append(sendStr + '\n');
//                text.append(plugText + '\n');
//            } else {
//                anecho.gui.JMText text;
//                text = connHandler.getActiveMUDText();
//                // text.append(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("localEchoPrepend"));
//                // text.append(sendStr + '\n');
//                text.append(plugText + '\n');
//            }
//        // }
//
//        }
        clearTextArea();
    }

    /**
     * Send the string for local echo. This method will determine if it should
     * be displayed to the user based on local echo settings
     *
     * @param echoStr The string to potentially output
     */
    private void sendLocalEcho(final String echoStr) {

        String plugText;

        // If local echo is active, append it to the user's screen as well
        // if (settings.isLocalEchoEnabled()) {
        if (settings.getJMboolean(JMConfig.LOCALECHO) && connHandler.isActiveMUEchoState() && !echoStr.equals("")) {

            // First run our localecho through our list of plug-ins in the event
            // that we need to modify the text.
            // This may need to be moved in the future. XXX
            // if (!echoStr.equals("")) {
            plugText = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("localEchoPrepend") + echoStr;

            logger.debug("DataIn.sendLocalEcho returning: " + plugText);

            if (settings.getJMboolean(JMConfig.USESWING)) {
                anecho.gui.JMSwingText text;
                text
                        = connHandler.getActiveMUDSwingText();
                text.append(plugText + '\n');
            } else {
                anecho.gui.JMText text;
                text
                        = connHandler.getActiveMUDText();
                text.append(plugText + '\n');
            }

// }
        }
    }

    /**
     * Clear the text area and move the cursor back to the beginning.
     */
    private void clearTextArea() {
        // return the cursor to the starting position and empty the text area
        if (useSwing) {
            ((anecho.gui.JMSwingEntry) dataText).setCaretPosition(0);
            ((anecho.gui.JMSwingEntry) dataText).setText("");
        } else {
            ((TextArea) dataText).setCaretPosition(0);
            ((TextArea) dataText).setText("");
        }

    }

    /**
     *
     * Set the current text from the scrollback menu, the item indicated by int
     * pos
     *
     * @param currentPos The index of the command history to use as the text
     *
     * for the DataIn text area.
     *
     */
    public void setFromHistory(final int currentPos) {

        logger.debug("DataIn.setFromHistory starting with position:" + currentPos);

        int pos = currentPos;

        // If the integer comes in as '-1', that means to use the last string
        if (pos == -1) {
            pos = historyV.size() - 1;
        }

        if (pos >= historyV.size() || pos < -1) {
            logger.debug("Our position is larger than our historyV but less than 0");

            return;
        }

        final String text;

        if (pos == -1) {
            text = transHistory;
        } else {
            text = historyV.elementAt(pos).toString();
        }

        if (useSwing) {
            logger.debug("DataIn.setFromHistory setting text to: " + text);

            ((anecho.gui.JMSwingEntry) dataText).setText(text);
            ((anecho.gui.JMSwingEntry) dataText).setCaretPosition(text.length());
        } else {
            // Now we'll set the text from the menu item
            ((TextArea) dataText).setText(text);

            // Explicitly set the cursor location, as this is
            // not automatic on some operating systems.
            ((TextArea) dataText).setCaretPosition(text.length());

        }

    }

    /**
     *
     * Set the text visible in the DataIn window. This method will remove any
     * text that may have previously been showing.
     *
     * @param text The text to display in the DataIn window.
     *
     */
    public void setText(final String text) {

        if (useSwing) {

            ((anecho.gui.JMSwingEntry) dataText).setText(text);

        } else {

            ((TextArea) dataText).setText(text);

        }

    }

    /**
     * Set the font to be used for the DataIn window.
     *
     * @param newFont The Font to use.
     */
    @Override
    public void setFont(final Font newFont) {
        dataText.setFont(newFont);
    }

    @Override
    public Font getFont() {
        return dataText.getFont();
    }

    /**
     *
     * Append the given string to whatever text exists in the DataIn
     *
     * window.
     *
     * @param text The text to be appended.
     *
     */
    public void append(final String text) {

        if (useSwing) {

            ((anecho.gui.JMSwingEntry) dataText).append(text);

        } else {

            ((TextArea) dataText).append(text);

        }

    }

    /**
     *
     * Get all the text currently in the DataIn window.
     *
     * @return A String representing all the text currently in the DataIn area.
     *
     */
    public String getText() {

        // String retText = "";
        String retText;

        if (useSwing) {
            retText = ((anecho.gui.JMSwingEntry) dataText).getText();
        } else {
            retText = ((TextArea) dataText).getText();
        }

        return retText;

    }

    /**
     *
     * Set the caret's location based on the given index from the
     *
     * first character in the text area.
     *
     * @param pos Location of the caret
     *
     */
    public void setCaretPosition(final int pos) {

        if (useSwing) {

            ((anecho.gui.JMSwingEntry) dataText).setCaretPosition(pos);

        } else {

            ((TextArea) dataText).setCaretPosition(pos);

        }

    }

    /**
     *
     * Get the location of our caret based on the number of characters
     *
     * from the beginning of the text area.
     *
     * @return Index of the caret's location.
     *
     */
    public int getCaretPosition() {

        // int retInt = 0;
        int retInt;

        if (useSwing) {
            retInt = ((anecho.gui.JMSwingEntry) dataText).getCaretPosition();
        } else {
            retInt = ((TextArea) dataText).getCaretPosition();
        }

// return ((TextArea)dataText).getCaretPosition();
        return retInt;

    }

    /**
     * Get the number of columns visible in our DataIn display
     *
     * @return Number of columns visible.
     */
    public int getColumns() {
        // int retCol = 0;
        int retCol;

        if (useSwing) {
            retCol = ((anecho.gui.JMSwingEntry) dataText).getColumns();
        } else {
            retCol = ((TextArea) dataText).getColumns();
        }

        return retCol;

    }

    /**
     * Sets the number of columns to be visible
     *
     * @param cols Number of columns to be visible
     */
    public void setColumns(final int cols) {
        if (useSwing) {
            ((anecho.gui.JMSwingEntry) dataText).setColumns(cols);
        } else {
            ((TextArea) dataText).setColumns(cols);
        }

    }

    /**
     * Sets the foreground colour for the DataIn area.
     *
     * @param fgColour The foreground colour to use.
     *
     */
    public void setForegroundColour(final Color fgColour) {
        dataText.setForeground(fgColour);
    }

    public Color getForegroundColour() {
        return dataText.getForeground();
    }

    /**
     * Set the background colour for the DataIn window.
     *
     * @param bgColour The background colour.
     */
    public void setBackgroundColour(final Color bgColour) {
        dataText.setBackground(bgColour);
    }

    public Color getBackgroundColour() {
        return dataText.getBackground();
    }

    /**
     * Set the title of this frame on the basis of if the active MU* (the one
     * visible at the moment) is connected or not. This method differs from the
     * main window's setWindowTitle() method in that it takes the text being
     * paused into consideration as well
     */
    public synchronized void setWindowTitle() {

        // This resets the window's title, depending if the setWindowTitle();
        // client is connected to a MU* or not
        // final CHandler connection = settings.getConnectionHandler();
        final CHandler connection = CHandler.getInstance();

        boolean active = false;

        try {
            active = connection.isActiveMUDConnected();
        } catch (Exception exc) {
            logger.debug("DataIn.setWindowTitle() error: " + exc);
        }

        if (active) {

            final MuSocket mSock = connection.getActiveMUHandle();

            if (mSock.isPaused()) {
                this.setTitle(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("outputPaused"));
            } else {
                this.setTitle(connection.getActiveTitle());
            }

        } else {

            // Connection is inactive, just have program's title
            this.setTitle(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("notConnected"));

        }

    }

    /**
     *
     * We export our TextArea for use in a &quot;combined" JamochaMUD
     * configuration, where both the input and output are shown in the same
     * frame.
     *
     * @return A reference to the text area of our DataIn object.
     *
     */
    public Component exportText() {

        Component retComp;

        this.setVisible(false);

        this.setSync(false);

        if (useSwing) {
            retComp = dataInPane;
        } else {
            retComp = dataText;
        }

// We should also remove this frame from the sync-group?
        return retComp;

    }

    /**
     * We are having our TextArea &quot;returned" to us from the main window.
     * We'll add it back to our frame and then set everything visible again.
     */
    public void restoreText() {

        if (useSwing) {

            this.add((anecho.gui.JMSwingEntry) dataText);

        } else {

            this.add((TextArea) dataText);

        }

        this.pack();

        // Check for sync value to restore us to our previous glory
        if (settings.getJMboolean(JMConfig.SYNCWINDOWS)) {

            this.setSync(true);

        }

        this.setVisible(true);

    }

    /**
     *
     * Return the variable representing our DataIn area.
     *
     * @return Variable representing our DataIn area.
     *
     */
    public Component getTextVariable() {

        Component retComp;

        if (useSwing) {

            retComp = dataInPane;

        } else {

            retComp = dataText;

        }

// We should also remove this frame from the sync-group?
        return retComp;

    }

    /**
     * Remove the paused status from JamochaMUD because of some
     *
     * action that we have received
     *
     */
    private void checkPause() {

        // final CHandler target = settings.getConnectionHandler();
        final CHandler target = CHandler.getInstance();

        final MuSocket mSock = target.getActiveMUHandle();

        if (mSock != null && mSock.isPaused()) {

            // This spools out any paused text
            mSock.spoolText();

        }

    }

    /**
     *
     * Set the number of rows available in our DataIn area. This is
     *
     * only useful if the DataIn frame is being viewed as a separate
     *
     * frame.
     *
     * @param rows The number of rows to set for the DataIn window.
     *
     */
    public void setRows(final int rows) {

        if (useSwing) {

            ((anecho.gui.JMSwingEntry) dataText).setRows(rows);

        } else {

            ((TextArea) dataText).setRows(rows);

        }

    }

    // This method has been moved to CHandler.java
//    /** Do any required macro replacement in our string */
//    
//    private String translateMacros(final String macroIn) {
//        
//        String input = macroIn;
//        
//        boolean process = true;
//        
//        int start, end;
//        
//        String command, sString, eString, macro;
//        
//        
//        
//        /** Create a loop to continuously process any (recursive) macros we find */
//        
//        // We need to be able to locate endless loops!  Fix this XXX
//        
//        while (process) {
//            
//            start = input.indexOf(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("${"));
//            
//            
//            
//            if (start > -1) {
//                
//                
//                
//                end = input.indexOf('}', start);
//                
//                if (end > -1 && end > start) {
//                    
//                    command = input.substring(start + 2, end);
//                    
//                    
//                    
//                    if (start > 0) {
//                        
//                        sString = input.substring(0, start);
//                        
//                    } else {
//                        
//                        sString = "";
//                        
//                    }
//                    
//                    if (end + 1 < input.length()) {
//                        
//                        eString = input.substring(end + 1);
//                        
//                    } else {
//                        
//                        eString = "";
//                        
//                    }
//                    
//                    
//                    
//                    macro = settings.getVariable(command);
//                    
//                    
//                    
//                    if (macro == null) {
//                        
//                        input = sString + command + eString;
//                        
//                    } else {
//                        
//                        input = sString + macro + eString;
//                        
//                    }
//                    
//                    
//                    
//                }
//                
//                
//                
//            } else {
//                
//                // No more macros to process
//                
//                process = false;
//                
//            }
//            
//        }
//        
//        
//        
//        return input;
//        
//    }
    /**
     * This lets our other classes bring us into focus
     */
    public void jmGainFocus() {

        if (useSwing) {

            logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("JMGainFocus()"));

            ((anecho.gui.JMSwingEntry) dataText).requestFocus();

            ((anecho.gui.JMSwingEntry) dataText).grabFocus();

        } else {

            ((TextArea) dataText).requestFocus();

        }

    }

    /**
     * This can probably be removed XXX
     *
     * @deprecated This method may no longer be required by this class.
     */
//    public synchronized void validate() {
//
//        dataText.invalidate();
//
//    }
    /**
     *
     * Return the last word typed
     *
     * @return String representing the last "word" typed by the user.
     *
     */
    public String getLastWord() {

        // String retString = "";
        String retString;

        // String inputText = "";
        String inputText;

// int cursorPos = 0;
        int cursorPos;

        if (useSwing) {
            inputText = ((anecho.gui.JMSwingEntry) dataText).getText();
            cursorPos
                    = ((anecho.gui.JMSwingEntry) dataText).getCaretPosition();
        } else {
            inputText = ((TextArea) dataText).getText();
            cursorPos
                    = ((TextArea) dataText).getCaretPosition();
        }

        if (cursorPos > 1) {
            // We use cursorPos - 2 as by the time we check, the space is already in place!
            // int start = inputText.lastIndexOf(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("_"), cursorPos - 2);
            int start = inputText.lastIndexOf(' ', cursorPos - 2);
            if (start < 0) {
                start = 0;
            }

            retString = inputText.substring(start, cursorPos);
        } else {
            retString = "";
        }

        return retString.trim();
    }

    /**
     * Enable or disable anti-aliasing
     *
     * @param state <CODE>true</CODE> - enable anti-aliased text
     * <CODE>false</CODE> - disable anti-aliased text
     */
    public void setAntiAliasing(final boolean state) {

        if (useSwing) {
            ((anecho.gui.JMSwingEntry) dataText).setAntiAliasing(state);
        }

    }

    /**
     * Returns whether antialiasing is active on this component
     *
     * @return <code>true</code> - Antialiasing is enabled <code>false</code> -
     * Antialiasing is not enabled
     */
    public boolean isAntiAliasing() {
        // boolean result = false;
        boolean result;

        if (useSwing) {
            result = ((anecho.gui.JMSwingEntry) dataText).isAntiAliasing();
        } else {
            result = false;
        }

        return result;
    }

    /**
     * This method sets the maximum number of entries that will be saved in the
     * command history pop-up.
     *
     * @param newLim The maximum number of lines to save for command history.
     */
    public void setLimit(final int newLim) {

        limit = newLim;

        while (historyV.size() > limit) {
            historyV.removeElementAt(0);
        }

    }

    public int getLimit() {
        return limit;
    }

    /**
     * Scroll to the first item in the history
     */
    public void historyBegin() {
        historyLoc = 1;
        scrollHistory(UP);
    }

    /**
     * Scroll to the last item in the history
     */
    public void historyEnd() {
        historyLoc = 0;
        scrollHistory(UP);
    }

    /**
     * This "scrolls" the history shown in the input window (not the pop-up
     * menu) one step in the direction given.
     *
     * @param direction The direction to move the position pointer
     */
    public void scrollHistory(final int direction) {
        if (historyV.isEmpty()) {
            // The history vector is empty, so we can't continue
            // Check to see if the history is disabled, and if so warn the user

            if (limit < 1) {
                final String messageTitle = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("COMMAND HISTORY DISABLED");
                final String messageText = "The command history is currently disabled.\n"
                        + "You can enable it via the \n"
                        + "Options -> Configure JamochaMUD dialogue.";

                if (useSwing) {
                    javax.swing.JOptionPane.showMessageDialog(this, messageText, messageTitle, javax.swing.JOptionPane.INFORMATION_MESSAGE);
                } else {
                    final anecho.gui.OKBox tempBox = new anecho.gui.OKBox(this, messageTitle);
                    tempBox.setText(messageText);
                    tempBox.setVisible(true);
                }

            }

            return;
        }

        final int histSize = historyV.size();

        if (historyLoc == -1) {
            // If we are moving off -1 position then we should save our current
            // text into our transient history
            transHistory = this.getText();
        }

        if (direction == UP) {
            historyLoc--;
        } else {
            historyLoc++;
        }

// Do a sanity check on our pointer
// Going off either end of the list should put a blank line, and then we'd
// continue next time.
        if (historyLoc < -1) {
            historyLoc = historyV.size() - 1;
            // historyLoc = histSize;
        }

// if (historyLoc >= historyV.size()) {
        if (historyLoc >= histSize) {
            historyLoc = -1;
        }

        logger.debug("DataIn.scrollHistory: New history location: " + historyLoc);

// Now show the history in the input window
        if (historyLoc == -1) {
            // Set the text to from our transient history variable
            setText(transHistory);
        } else {
            setFromHistory(historyLoc);
        }

    }

    /**
     * This method returns whether spell-checking is currently active
     *
     * @return <code>true</code> The spell-checker is active <code>false</code>
     * The spell-check is not active
     */
    public boolean isSpellCheck() {
        // boolean result = false;
        boolean result;

        if (useSwing) {
            result = JMConfig.getInstance().getJMboolean(JMConfig.SPELLCHECK);
        } else {
            // If we are not using Swing then the answer is always false
            result = false;
        }

        return result;
    }

    /**
     * Allow the auto spellcheck feature to be enabled / disabled.
     *
     * This only works with a Swing interface
     *
     * @param state <CODE>true</CODE> - enable in-line spell checking
     * <CODE>false</CODE> - disable in-line spell checking
     */
    public void setSpellCheck(final boolean state) {
        if (!useSwing) {
            // Do not allow this to continue if we are not using Swing.
            return;
        }

        // Check to see if Java is version 1.5 or higher.  If not, we
        // cannot use myspell libraries due to the use of generics which do not
        // existing in version 1.4 and below.
        final java.util.Properties systemData = System.getProperties();
        final double ver = anecho.gui.TextUtils.stringToDouble(systemData.getProperty("java.version"));

        // Test to make certain the spelling libraries are present
        boolean validSpell;

        try {
            Class.forName("org.dts.spell.dictionary.SpellDictionary");
            logger.debug("The spell-checking libraries do exist.");

            validSpell = true;
        } catch (Exception except) {
            validSpell = false;
        }

        if (ver < 1.5 || !validSpell) {
            // We can't use myspell as generics are not handled until Java 1.5.
            // An error message should probably be given to the user and spell-check
            // should be disabled
            javax.swing.JOptionPane.showMessageDialog(this, "Due to updates in the spell-checking\n"
                    + "libraries JamochaMUD uses, Java\n"
                    + "versions older than 1.5 can no longer be used.\n"
                    + "Spell-checking has been disabled.\n"
                    + "Your java version is reported as:\n"
                    + systemData.getProperty("java.version"),
                    "Java version for Spellcheck",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
            settings.setJMboolean(JMConfig.SPELLCHECK, "false");
        } else {

            final SpellCheck checker = new SpellCheck();
            spellCheckObj = checker.setSpellCheck(DICTTYPE, state, dataText, spellCheckObj);

        }
    }
}
