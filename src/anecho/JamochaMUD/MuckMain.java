/**
 * Main Muck Output Window $Id: MuckMain.java,v 1.35 2012/03/11 03:44:51 jeffnik
 * Exp $
 */

/* JamochaMUD, a Muck/Mud client program
 * Copyright (C) 1998-2010  Jeff Robinson
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

import anecho.JamochaMUD.legacy.AWTProxyBox;
import anecho.JamochaMUD.legacy.FontFace;
import anecho.JamochaMUD.TinyFugue.JMTFKeys;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import anecho.gui.SyncFrame;
import anecho.gui.JMText;
import anecho.gui.PosTools;

import anecho.JamochaMUD.plugins.PlugInterface;
import net.sf.wraplog.AbstractLogger;
import net.sf.wraplog.NoneLogger;
import net.sf.wraplog.SystemLogger;

/**
 *
 * Main Muck Output Window, heart of the program, containing most of the menus
 * and logistics of JamochaMUD
 *
 * @version $Id: MuckMain.java,v 1.36 2014/06/08 01:31:40 jeffnik Exp $
 * @author Jeff Robinson
 *
 */
public final class MuckMain implements ActionListener, ComponentListener, KeyListener, ItemListener, WindowListener {

    /**
     * This variable monitors the split frame menu item
     */
    // private transient CheckboxMenuItem splitFramesItem;
    private transient CheckboxMenuItem tWAltFocus;
    private transient Frame muckMainFrame;
    /**
     * Combined frame style of presentation
     */
    public static final int COMBINED = 0;     // Combined frame style
    /**
     * Split frame style of presentation
     */
    public static final int SPLIT = 1;        // Split frame style
    /**
     * Indicates whether JamochaMUD is operating as separate windows or one
     * combined window
     */
    private transient int viewStyle = COMBINED;         // the current style of our display
    /**
     * Variable representing the NEXT when navigating through MU*s
     */
    public static final int NEXT = 1;
    /**
     * Variable representing the PREVIOUS when navigating through MU*s
     */
    public static final int PREVIOUS = 0;
    // private static ResReader reader;
    /**
     * Have we set the listener for JTabbedPane if necessary
     */
    private transient boolean listenerSet = false;
    /**
     * Whether the interface is swing-based or not
     */
    private transient boolean useSwing = true;
    /**
     * Right now we have to use the heavyweight AWT menu bar
     */
    private transient boolean useSwingMenu = false;
    /**
     * The component that is either a JMFancyTabbedPane or JMTabPanel
     */
    private transient Component textPanel;
    /**
     * Enables or disables the printing of debugging information
     */
    private static final boolean DEBUG = false;
    /**
     * The instance of this class (for use as a singleton)
     */
    private static MuckMain ourMuckMain;
    /**
     * A string used to identify the language translation bundle
     */
    private static final transient String BUNDLENAME = "anecho/JamochaMUD/JamochaMUDBundle";

    private final AbstractLogger logger;

    /**
     * This method returns the running instance of MuckMain, or creates it if we
     * do not yet have one.
     *
     * @return The current instance of MuckMain
     */
    public synchronized static MuckMain getInstance() {

        if (ourMuckMain == null) {
            // ourMuckMain = new MuckMain(settings);
            ourMuckMain = new MuckMain();
        }

        return ourMuckMain;

    }

    /**
     * This is the private constructor for MuckMain, which is run the first time
     * MuckMain.getInstance() is called.
     *
     */
    private MuckMain() {

   
        if (DEBUG) {
            logger = new SystemLogger();
        } else {
            logger = new NoneLogger();
        }

        logger.debug("MuckMain.MuckMain starting.");
        
        final JMConfig settings = JMConfig.getInstance();

        if (settings.getJMboolean(JMConfig.USESWING)) {
            muckMainFrame = (Frame) new anecho.JamochaMUD.MainSwingFrame(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("JamochaMUD"));
            useSwing = true;
            useSwingMenu = true;
            final javax.swing.ImageIcon jMUDImage = new javax.swing.ImageIcon(getClass().getResource(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("/anecho/JamochaMUD/JamochaMUD.png")));
            muckMainFrame.setIconImage(jMUDImage.getImage());
            final JMainMenu swingMenu = JMainMenu.getInstance();        // Trying out the singleton
            swingMenu.buildMenu((javax.swing.JFrame) muckMainFrame, this);
            textPanel = new anecho.gui.JMFancyTabbedPane();

            // This is a direct copy/paste from setMainLayout() and should be fixed.  Fix Me XXX
            ((anecho.gui.JMFancyTabbedPane) textPanel).addChangeListener(new javax.swing.event.ChangeListener() {

                public void stateChanged(javax.swing.event.ChangeEvent change) {

                    panelTabChanged((Object) change);

                }
            });

        } else {
            useSwing = false;
            useSwingMenu = false;
            muckMainFrame = (Frame) new SyncFrame(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("JamochaMUD"));
            final Image jMUDImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("/anecho/JamochaMUD/kehza.gif")));
            muckMainFrame.setIconImage(jMUDImage);
            final anecho.JamochaMUD.legacy.MainMenu mainMenu = anecho.JamochaMUD.legacy.MainMenu.getInstance();
            mainMenu.buildMenu(muckMainFrame, this, settings);
            textPanel = new anecho.JamochaMUD.legacy.JMTabPanel();
        }

        // Create the windows
        muckMainFrame.addComponentListener(this);
        muckMainFrame.addWindowListener(this);
        muckMainFrame.addKeyListener(this);

        logger.debug("MuckMain.MuckMain completed.");
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        if (event != null) {
            privateActionPerformed(event);
        }
    }

    /**
     *
     *
     *
     * @param event
     *
     */
    private void privateActionPerformed(final ActionEvent event) {

        final String arg = event.getActionCommand();

        if (arg.equals(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("nextMU"))) {
            advanceMU(NEXT);
        }

        if (arg.equals(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("previousMU"))) {
            advanceMU(PREVIOUS);
        }

        // Arguments for the ***FILE**** menu
        // Check for 'Dump Output'
        if (arg.equals(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("dumpOutput"))) {
            dumpOutput();
        }

        if (arg.equals(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("export"))) {
            final ImportExport impExp = ImportExport.getInstance();
            impExp.exportSettings();
        }

        if (arg.equals(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("import"))) {

            final ImportExport impExp = ImportExport.getInstance();

            impExp.importSettings();

        }

        if (arg.equals(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("quit"))) {
            // Do the proper exit proceedure
            quitJamochaMUD();

            // Close the JVM
            System.exit(1);  // Probably an ugly exit
        }

        // Arguments for the **EDIT** menu
        /**
         * Copy
         */
        if (arg.equals(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("copyFromMainWindow"))) {
            if (DEBUG) {
                System.err.println("MuckMain.actionPerformed has received the command to copyToClipboard");
            }
            copyToClipboard();
        }

        /**
         * Paste
         */
        if (arg.equals(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("paste"))) {
            pasteFromClipboard();
        }

        /**
         * Find
         */
        // Arguments for the **CONNECTION**** menu
        /**
         * Connect to MU*
         */
        if (arg.equals(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("connectToMU"))) {
            jmConnectToMU();
        }

        /**
         * Reconnect to MU*
         */
        if (arg.equals(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("reconnectToMU"))) {
            jmReconnectToMU();
        }

        /**
         * Disconnect from MU*
         */
        if (arg.equals(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("disconnectFromMU"))) {
            jmDisconnectFromMU();
        }

        /**
         * Close the visible MU "window"
         */
        if (arg.equals(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("closeThisView"))) {
            closeActiveWindow();
        }

        if (arg.equals(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("addToMUConnector"))) {
            addToConnector();
        }

        // Arguments for ****OPTIONS**** menu
        if (arg.equals(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("Configure_JamochaMUD"))) {
            configureJamochaMUD();
        }

        if (arg.equals(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("fontsAndColours"))) {
            setColours();
        }

        if (arg.equals(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("externalPrograms"))) {
            showExtProgDialogue();
        }

        if (arg.equals(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("serverOptions"))) {
            showProxyDialogue();
        }

        if (arg.equals(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("installPlugin"))) {
            installPlugIn();
        }

        // Arguments for ****HELP**** menu
        if (arg.equals(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("contents"))) {

//            // Launch the web browser to show the JamochaMUD home page
//            final StringBuffer tentativeURL = new StringBuffer(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("jamochaMUDPages"));
//
//            final BrowserWrapper wrapper = BrowserWrapper.getInstance();
//            wrapper.showURL(tentativeURL.toString());
            showHelp();
        }

        if (arg.equals(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("tinyFugueKeyCodes"))) {
            JMTFKeys.showCommands(muckMainFrame, useSwing);
        }

        if (arg.equals(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("aboutJamochaMUD"))) {
            showAboutBox();
        }

        if (arg.equals("What's new?")) {
            showWhatsNew(true);
        }

        if (arg.equals(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("reportABug"))) {

//            final String message = anecho.gui.AbstractMessageFormat.wrap(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("reportABugMessage"));
//            
//            
//            
//            if (useSwing) {
//                
//                javax.swing.JOptionPane.showMessageDialog(muckMainFrame,
//                        
//                        message,
//                        
//                        java.util.ResourceBundle.getBundle(BUNDLENAME).getString("reportABug"),
//                        
//                        javax.swing.JOptionPane.INFORMATION_MESSAGE);
//                
//            } else {
//                
//                final anecho.gui.OKBox shoot = new anecho.gui.OKBox(muckMainFrame, java.util.ResourceBundle.getBundle(BUNDLENAME).getString("reportABug"), true);
//                
//                shoot.append(message);
//                
//                
//                
//                shoot.show();
//                
//            }
//            // Take the user directly to the mantis bug-tracker
//            // the project_id=2 directs the user straigh to the JamochaMUD bugs
//            final String bugTracker = "http://www.anecho.mb.ca/mantis/set_project.php?project_id=2";
//
//            final BrowserWrapper wrapper = BrowserWrapper.getInstance();
//            wrapper.showURL(bugTracker);
            showReportBug();

        }

        // Check to see if we've had a request on the plugins menu
        if (DEBUG) {
            System.err.println("MuckMain.actionPerformed: " + arg);
        }

        if (arg.indexOf(':') > -1) {

            managePlugInMenuStuff(arg);
        }

        if (event.getActionCommand().startsWith(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("ChangeMU:"))) {
            changeMenu(event.getActionCommand());
        }

    }

    /**
     * Take the user directly to the mantis bug-tracker the project_id=2 directs
     * the user straigh to the JamochaMUD bugs
     */
    private void showReportBug() {

        final String bugTracker = "http://www.anecho.mb.ca/mantis/set_project.php?project_id=2";

        final BrowserWrapper wrapper = BrowserWrapper.getInstance();
        wrapper.showURL(bugTracker);

    }

    /**
     * This method is used to update JamochaMUD when the user switches MU*s
     * based on a choice from a menu
     *
     * @param aCmd
     */
    private void changeMenu(final String aCmd) {
        // Make sure we tell the ConnHandler we're changing MU*s
        setActiveMU(aCmd);

        // This will show our active MU*
        setVisibleMU();

        // Update our connection menu
        updateConnectionMenu();

        // Update the Frame's title
        setWindowTitle();

        // Make sure the info shows up!
        // validate();
        muckMainFrame.validate();

    }

    /**
     * Show the help URL
     */
    private void showHelp() {
        // Launch the web browser to show the JamochaMUD home page
        final StringBuffer tentativeURL = new StringBuffer(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("jamochaMUDPages"));

        final BrowserWrapper wrapper = BrowserWrapper.getInstance();
        wrapper.showURL(tentativeURL.toString());
    }

    /**
     * This method handles events on the plug-in menu such as showing
     * properties, enabling, disabling, and showing descriptions
     *
     * @param eventArg
     */
    private void managePlugInMenuStuff(final String eventArg) {
        if (DEBUG) {
            System.err.println("MuckMain showing plug-in description");
        }

        final int col = eventArg.indexOf(':');
        final String testName = eventArg.substring(col + 1);

        // a request for the properties of a plugin has been made
        try {
            final int selected = EnumPlugIns.plugInName.indexOf(testName);
            final Object plugClass = EnumPlugIns.plugInClass.elementAt(selected);
            final String name = ((PlugInterface) plugClass).plugInName();

            // Use of strings like "plugin:" is fragile.  Use static variables instead.
            // Fix Me XXX
            if (eventArg.startsWith("plugin:")) {
                ((PlugInterface) plugClass).plugInProperties();
            }

            if (eventArg.startsWith("description:")) {

                if (DEBUG) {
                    System.err.println(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("Calling_description"));
                }

                final String desc = anecho.gui.AbstractMessageFormat.wrap(((PlugInterface) plugClass).plugInDescription());

                if (useSwing) {
                    javax.swing.JOptionPane.showMessageDialog(muckMainFrame, desc, name, javax.swing.JOptionPane.INFORMATION_MESSAGE);
                } else {
                    final anecho.gui.OKBox message = new anecho.gui.OKBox(muckMainFrame, desc);

                    message.setTitle(name);

                    message.showCentered();
                }

            }

            if (eventArg.startsWith(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("enable:"))) {
                if (DEBUG) {
                    System.err.println(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("Calling_enable"));
                }

                final ManagePlugins manplug = new ManagePlugins();
                manplug.changePlugInState(name, true);
            }

            if (eventArg.startsWith("disable:")) {
                if (DEBUG) {
                    System.err.println(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("Calling_disable"));
                }
                final ManagePlugins manplug = new ManagePlugins();
                manplug.changePlugInState(name, false);
            }
        } catch (ArrayIndexOutOfBoundsException except) {
            if (DEBUG) {
                System.err.println(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("Error_trying_to_get_plug-in_options."));
                System.err.println(except);
                except.printStackTrace();

            }

            if (useSwing) {
                javax.swing.JOptionPane.showMessageDialog(muckMainFrame, java.util.ResourceBundle.getBundle(BUNDLENAME).getString("noSettingsAvailable"));
            } else {
                final anecho.gui.OKBox message = new anecho.gui.OKBox(muckMainFrame, java.util.ResourceBundle.getBundle(BUNDLENAME).getString("noSettingsAvailable"), true, java.util.ResourceBundle.getBundle(BUNDLENAME).getString("noPlugInSettings"));
                message.showCentered();
            }

        }

    }

    /**
     *
     * Show a dialogue box to allow users to select a file to dump all of the
     * output to for logging/archiving, etc...
     *
     */
    private void dumpOutput() {

        // Show the system dump dialogue
        // This will dump *all* the output to the selected file
        final String prevPath = JMConfig.getInstance().getJMString(JMConfig.LOGPATH);

        final FileDialog dumpDialogue = new FileDialog(muckMainFrame, java.util.ResourceBundle.getBundle(BUNDLENAME).getString("Dump_Output_to_file"), 1);
        if (prevPath != null && !prevPath.equals("")) {
            dumpDialogue.setDirectory(prevPath);
        }
        dumpDialogue.show();

        try {
            final String fileName = dumpDialogue.getFile();

            // File has been selected
            // now write the output contents to it
            if (fileName != null) {
                final String filePath = dumpDialogue.getDirectory();
                final FileOutputStream outFile = new FileOutputStream((filePath + fileName), false);
                final PrintWriter out = new PrintWriter(outFile, true);
                final CHandler connHandler = CHandler.getInstance();

                // Strip the text so that only plain text is written
                if (useSwing) {
                    final anecho.gui.JMSwingText activeMU = connHandler.getActiveMUDSwingText();
                    out.println(anecho.gui.TextUtils.stripEscapes(activeMU.getText(), false));
                } else {
                    final JMText activeMU = connHandler.getActiveMUDText();

                    out.println(anecho.gui.TextUtils.stripEscapes(activeMU.getText(), false));
                }
                out.flush();
                outFile.close();
                JMConfig.getInstance().setJMValue(JMConfig.LOGPATH, filePath);
            }
        } catch (IOException except) {
            // This occurs when there is a 'cancel' instead of a file selection
            // We'll just gracefully fall through
            dumpDialogue.dispose();
        }

    }

    public void keyPressed(final KeyEvent event) {
        if (event != null) {
            privateKeyPressed(event);
        }
    }

    /**
     *
     *
     *
     * @param event
     *
     */
    private void privateKeyPressed(final KeyEvent event) {
        boolean autoFocus = false;

        if (useSwingMenu) {
            final JMainMenu swingMenu = JMainMenu.getInstance();        // Trying out the singleton
            autoFocus = swingMenu.isAutoFocus();
        } else {
            final anecho.JamochaMUD.legacy.MainMenu mainMenu = anecho.JamochaMUD.legacy.MainMenu.getInstance();
            autoFocus = mainMenu.isAutoFocus();
        }

        if (!event.isAltDown() && !event.isControlDown()) {
            // Check to see if the user wants AutoFocus shift
            if (autoFocus) {
                transferFocus(event);
            } else {
                event.consume();
            }
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
    public void keyTyped(final KeyEvent event) {
    }

    /**
     *
     *
     *
     * @param event
     *
     */
    public void keyReleased(final KeyEvent event) {
    }

    public void itemStateChanged(final ItemEvent event) {
        if (event != null) {
            privateItemStateChanged(event);
        }
    }

    /**
     *
     *
     *
     * @param event
     *
     */
    // public void itemStateChanged(final ItemEvent event) {
    private void privateItemStateChanged(final ItemEvent event) {
        String arg = "";
        boolean itemState = false;

        if (useSwingMenu) {
            final Object target = event.getItem();
            arg = ((javax.swing.JCheckBoxMenuItem) target).getText();
        } else {
            arg = (String) event.getItem();
        }

        // Determine if our item is now enabled or disabled
        if (event.getStateChange() == ItemEvent.SELECTED) {
            itemState = true;
        }

        if (arg.equals(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("autoFocusInput"))) {
            JMConfig.getInstance().setJMValue(JMConfig.AUTOFOCUSINPUT, itemState);
        }

        if (arg.equals(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("autoLogging"))) {
            JMConfig.getInstance().setJMValue(JMConfig.AUTOLOGGING, itemState);

            // Tell our different MU*s to start/stop logging
            final CHandler connHandler = CHandler.getInstance();
            connHandler.setLogging(itemState);
        }

        if (arg.equals(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("altFocus"))) {
            JMConfig.getInstance().setJMValue(JMConfig.ALTFOCUS, itemState);
        }

        if (arg.equals(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("localEcho"))) {
            JMConfig.getInstance().setJMValue(JMConfig.LOCALECHO, itemState);
        }

        if (arg.equals(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("doubleBuffer"))) {
            setDoubleBuffer(itemState);
        }

        if (arg.equals(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("antiAlias"))) {
            setAntiAliasing(itemState);
        }

        if (arg.equals(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("Low_colour_display"))) {
            setLowColour(itemState);
        }

        if (arg.equals(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("splitFrames"))) {
//            JMConfig.getInstance().setJMValue(JMConfig.SPLITVIEW, itemState);
//            // Toggle our layout
//            if (viewStyle == SPLIT) {
//                viewStyle = COMBINED;
//                splitFramesItem.setState(false);
//                tWAltFocus.setEnabled(false);
//            } else {
//                viewStyle = SPLIT;
//                splitFramesItem.setState(true);
//                tWAltFocus.setEnabled(true);
//            }
//
//            // Call for new layout
//            setMainLayout();

            setSplitFrames(itemState);
        }

        if (arg.equals(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("syncWindows"))) {

//            // If the user is not using split frames then we can record this choice,
//            // but we should not affect the true nature of the frames
//            JMConfig.getInstance().setJMValue(JMConfig.SYNCWINDOWS, itemState);
//
//            if (splitFramesItem.getState()) {
//                // The frames are split, so we can go to town!
//                setAllSync(itemState);
//            }
            setSyncWindows(itemState);
        }

        if (arg.equals(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("showTimers"))) {
            // settings.setTimersVisible(tWTimers.getState());
            // settings.setJMValue(JMConfig.TIMERSVISIBLE, tWTimers.getState());
            JMConfig.getInstance().setJMValue(JMConfig.TIMERSVISIBLE, itemState);
            // Changed the status of the timers
            // Fix this XXX
            // if (tWTimers.getState()) {
            // The timers have been enabled, start the timer thread
            // timerThread.setActiveState(true);
            // } else {
            // timerThread.setActiveState(false);
            // }
        }

        if (arg.equals(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("useUnicode"))) {
            // Changed the Unicode option
            JMConfig.getInstance().setJMValue(JMConfig.USEUNICODE, itemState);
        }

        if (arg.equals(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("releasePause"))) {
            JMConfig.getInstance().setJMValue(JMConfig.RELEASEPAUSE, itemState);
        }

        if (arg.equals(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("tinyFugueKeys"))) {
            JMConfig.getInstance().setJMValue(JMConfig.TFKEYEMU, itemState);
        }
    }

    /**
     * Sets whether windows are synchronized or not
     *
     * @param itemState
     */
    private void setSyncWindows(final boolean itemState) {
        // If the user is not using split frames then we can record this choice,
        // but we should not affect the true nature of the frames
        JMConfig.getInstance().setJMValue(JMConfig.SYNCWINDOWS, itemState);

        boolean splitState = false;

        if (useSwingMenu) {
            final JMainMenu swingMenu = JMainMenu.getInstance();
            splitState = swingMenu.isSyncWindows();
        } else {
            // AWT Stuff goes here.  Fix Me XXX
        }

        // if (splitFramesItem.getState()) {
        if (splitState) {
            // The frames are split, so we can go to town!
            setAllSync(itemState);
        }

    }

    /**
     * Sets the frames to be either split of combined
     */
    private void setSplitFrames(final boolean itemState) {
        JMConfig.getInstance().setJMValue(JMConfig.SPLITVIEW, itemState);
        boolean isSplit = false;
        // Toggle our layout
        if (viewStyle == SPLIT) {
            viewStyle = COMBINED;
        } else {
            viewStyle = SPLIT;
            isSplit = true;
        }

        if (useSwingMenu) {
            final JMainMenu tempMenu = JMainMenu.getInstance();
            tempMenu.setSyncWindows(isSplit);
        } else {
            // This is for AWT stuff.  Fix Me XXX
        }

        tWAltFocus.setEnabled(isSplit);

        // Call for new layout
        setMainLayout();

    }

    /**
     *
     *
     *
     * @param event
     *
     */
    @Override
    public void windowActivated(final WindowEvent event) {
        // Restore the original title to the window
        // settings.setMainWindowIconified(false);
        JMConfig instance = JMConfig.getInstance();
        instance.setJMValue(JMConfig.MAINWINDOWICONIFIED, false);
        setWindowTitle();
    }

    /**
     *
     *
     *
     * @param event
     *
     */
    @Override
    public void windowClosed(final WindowEvent event) {
        System.err.println("MuckMain.windowClosed() called.");
    }

    /**
     *
     *
     *
     * @param event
     *
     */
    public void windowClosing(final WindowEvent event) {
        // Do a proper shutdown proceedure
        quitJamochaMUD();
    }

    /**
     *
     *
     *
     * @param event
     *
     */
    public void windowDeactivated(final WindowEvent event) {
    }

    /**
     *
     *
     *
     * @param event
     *
     */
    @Override
    public void windowDeiconified(final WindowEvent event) {
        callSpoolText();

        // The text window is visible
        // textWindowStatus = true;
        JMConfig.getInstance().setJMValue(JMConfig.MAINWINDOWICONIFIED, false);
        setWindowTitle();
    }

    /**
     *
     *
     *
     * @param event
     *
     */
    public void windowIconified(final WindowEvent event) {
        final JMConfig settings = JMConfig.getInstance();
        // Write the frame to a the Hashtable

        settings.setJMValue(JMConfig.MAINWINDOWICONIFIED, true);
        settings.setJMValue(JMConfig.MAINWINDOW, muckMainFrame.getBounds());

        if (DEBUG) {
            System.err.println(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("MuckMain.windowIconified_set_bounds_to:_") + muckMainFrame.getBounds());
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
    public void windowOpened(final WindowEvent event) {
    }

    // We'll track the movements of our window for when we have to
    // write it out to our .rc file  Each window for himself!!
    /**
     *
     *
     *
     * @param event
     *
     */
    public void componentHidden(final ComponentEvent event) {
    }

    /**
     *
     *
     *
     * @param event
     *
     */
    @Override
    public void componentResized(final ComponentEvent event) {
    }

    /**
     *
     *
     *
     * @param event
     *
     */
    public void componentMoved(final ComponentEvent event) {
        JMConfig.getInstance().setJMValue(JMConfig.MAINWINDOW, muckMainFrame.getBounds());
    }

    /**
     *
     *
     *
     * @param event
     *
     */
    public void componentShown(final ComponentEvent event) {
    }

    /**
     * This method sets the title for the main JamochaMUD frame. If the client
     * is connected to a MU, the title will be "JamochaMUD (MU* Name)". If the
     * client is connected but output is paused, the title will read "JamochaMUD
     * (Output Paused)" If the client is not connected, it will read "JamochaMUD
     * (Not Connected)".
     *
     */
    public synchronized void setWindowTitle() {

        // This resets the window's title, depending if the
        // client is connected to a MU* or not
        final CHandler connection = CHandler.getInstance();

        boolean active = false;
        try {
            active = connection.isActiveMUDConnected();
        } catch (Exception exc) {
            if (DEBUG) {
                System.err.println("MuckMain.setWindowTitle() error: " + exc);
            }
        }

        if (active) {
            // Now test to see if this MU* output is paused or not
            final MuSocket mSock = connection.getActiveMUHandle();

            if (mSock.isPaused()) {
                // This format is nice, but too long
                muckMainFrame.setTitle(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("outputPaused"));
            } else {
                muckMainFrame.setTitle(connection.getActiveTitle());
            }
        } else {
            // Connection is inactive, just have program's title
            muckMainFrame.setTitle(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("JamochaMUD_-_Not_connected"));
        }

        checkCloseMenuState();

    }

    /**
     * A specific instance of setting the MU* title if the MU* is minimised
     *
     * @param title
     */
    public synchronized void setWindowTitle(final String title) {
        muckMainFrame.setTitle(title);
    }

    /**
     * Return the current title of this main frame
     *
     * @return
     */
    public String getWindowTitle() {
        return muckMainFrame.getTitle();
    }

    /**
     * Methods called when disconnected from the MUD/MUCK Here, we reset some of
     * the menu flags to the appropriate status and give the user visual
     * notification that they have been disconnected (This is not always called
     * just by the user's 'Disconnect' action)
     */
    public void disconnectMenu() {
        logger.debug("MuckMain.disconnectMenu() called.");

        // This changes the flags on the MuckMain menu to 'disconnected'
        // Connect to MU*
        if (useSwingMenu) {
            final JMainMenu swingMenu = JMainMenu.getInstance();
            swingMenu.setConnected(false);
        } else {
            // muckMainFrame.setMenuBar(tWMenuBar);
            final anecho.JamochaMUD.legacy.MainMenu mainMenu = anecho.JamochaMUD.legacy.MainMenu.getInstance();
            mainMenu.setConnected(false);
        }
    }

    /**
     * We've received notification from one of our connections that it has
     * either terminated. We'll query the muck* and see if it is the active one.
     * If so, we'll update our connection menu.
     */
    public void checkDisconnectMenu() {
        final CHandler connHandler = CHandler.getInstance();
        // MuSocket muck = connHandler.getActiveMUHandle();

        // If our active MU* is not connected, change the connection menu
        boolean active = false;
        try {
            active = connHandler.isActiveMUDConnected();
        } catch (Exception exc) {
            System.err.println("MuckMain.checkDisconnectMenu() error " + exc);
        }

        if (connHandler != null && !active) {
            disconnectMenu();
        }
    }

    /**
     * The user has chosen to connect to a MUD/MUCK, so we set the menu items as
     * appropriate, and give the a visual identifier that we are actually trying
     * to make the connection
     */
    public void connectMenu() {
        if (useSwingMenu) {
            final JMainMenu swingMenu = JMainMenu.getInstance();
            swingMenu.setConnected(true);
        } else {
            final anecho.JamochaMUD.legacy.MainMenu mainMenu = anecho.JamochaMUD.legacy.MainMenu.getInstance();
            mainMenu.setConnected(true);
        }

    }

    /**
     *
     * Set the 'pause' status on the Main window. Usually done by a single
     * mouse-click, it will stop the text from scrolling, and set the DataBar's
     * title to alert the user that the text is paused
     *
     */
    public void pauseText() {
        // A single click signals a pause
        // This will pause the output window,
        // the incoming lines held in queue
        // pauseStatus = true;
    }

    /**
     *
     * The user has chosen to connect to a MU*, so we hide all the
     *
     * active windows and show the MuckConn again
     *
     */
    private void jmConnectToMU() {
        final CHandler connHandler = CHandler.getInstance();
        if (DEBUG) {
            System.err.println(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("MuckMain.JMConnectToMU_calling_connHandler.connectToNewMU()"));
        }

        connHandler.connectToNewMU();

        if (DEBUG) {
            System.err.println(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("MuckMain.JMConnectToMU_complete."));
        }
    }

    /**
     *
     * Disconnect from the MU, closing the socket and stopping
     *
     * the thread that listens to that IP address
     *
     */
    private void jmDisconnectFromMU() {
        final CHandler connHandler = CHandler.getInstance();
        // System.err.println("MuckMain.JMDisconnectFromMU calls connHandler.getActiveMUHandle.");
        final MuSocket muSock = connHandler.getActiveMUHandle();
        // final String stamp = muSock.getTimeStamp() + "";
        final String stamp = String.valueOf(muSock.getTimeStamp());
        connHandler.closeActiveMU(stamp);

        // Explicitly set the state of our menus
        setConnectionMenu();

    }

    /**
     * Reconnect to the MU* we were just connected to!
     */
    private synchronized void jmReconnectToMU() {
        // We'll remove the current MU* after we grab the name and address
        // It's important to recycle
        final CHandler connHandler = CHandler.getInstance();

        if (DEBUG) {
            System.err.println(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("MuckMain.JMReconnectToMU_calls_CHandler.reconnectToMU()"));
        }

        connHandler.reconnectToMU();

        if (DEBUG) {
            System.err.println(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("MuckMain.JMReconnectToMU_complete"));
        }

        // Reset titles and stuff
        updateStates();

    }

    /**
     * Change the shown MU* to our new selection
     */
    public synchronized void setVisibleMU() {

        // Update the menu to show the appropriate MU* as being active
        updateConnectionMenu();

        // Make certain that our connection menu is up to date for this MU*
        setConnectionMenu();

        // Change the title on our data-entry window
        final DataIn inWindow = JMConfig.getInstance().getDataInVariable();

        if (inWindow != null) {
            inWindow.setWindowTitle();
        }
    }

    /**
     * Set the colouring for our components
     */
    private void setColours() {
        final JMConfig settings = JMConfig.getInstance();

        // Grab our settings for the current MU*
        Color fgColor = settings.getJMColor(JMConfig.FOREGROUNDCOLOUR);
        Color bgColor = settings.getJMColor(JMConfig.BACKGROUNDCOLOUR);
        final Font ourFont = settings.getJMFont(JMConfig.FONTFACE);

        final Font newStyle;

        if (this.useSwing) {
            final anecho.gui.JMFontDialog fFace = new anecho.gui.JMFontDialog(muckMainFrame, fgColor, bgColor, ourFont);
            fFace.setLocationRelativeTo(muckMainFrame);
            fFace.setResizable(false);
            fFace.setVisible(true);

            newStyle = fFace.getFontStyle();
            fgColor = fFace.getForegroundColour();
            bgColor = fFace.getBackgroundColour();

        } else {
            // Set up our dialogue and display it
            final FontFace fFace = new FontFace(muckMainFrame, fgColor, bgColor, ourFont);
            fFace.setLocation(anecho.gui.PosTools.findCenter(muckMainFrame, fFace));
            fFace.setResizable(false);
            fFace.setVisible(true);

            // Now querry our dialogue and get our new settings
            newStyle = fFace.getFontStyle();
            fgColor = fFace.getForegroundColour();
            bgColor = fFace.getBackgroundColour();

        }

        if (newStyle == null) {
            // The user cancelled, no changes made
            return;
        }

        // Set up the other components with the proper colour
        // Change the colour/style in our settings
        settings.setJMValue(JMConfig.FONTFACE, newStyle);
        settings.setJMValue(JMConfig.FOREGROUNDCOLOUR, fgColor);
        settings.setJMValue(JMConfig.BACKGROUNDCOLOUR, bgColor);

        // Change the colours/styles of our inputBuffer window
        final JMUD core = settings.getJMCore();
        core.setAllFonts(newStyle);
        core.setAllColours(fgColor, bgColor);
    }

    /**
     * This method creates a dialogue box to show the JamochaMUD credits and
     * license information.
     */
    private void showAboutBox() {
        if (JMConfig.getInstance().getJMboolean(JMConfig.USESWING)) {
            final AboutBox2 temp = new AboutBox2(muckMainFrame);
            temp.setVisible(true);
        } else {
            final AboutBox temp = new AboutBox(muckMainFrame);
            temp.setVisible(true);
        }
    }

    /**
     * This method shows the dialogue for configuring external programs.
     *
     * @deprecated JamochaMUD doesn't work well with other programs, so this
     * method should be avoided at the moment.
     */
    private void showExtProgDialogue() {
        final ExternalProgs extP = new ExternalProgs(muckMainFrame);
        extP.setVisible(true);
    }

    /**
     * This method shows a dialogue box that may be used to configure proxies
     * (such as Socks servers)
     */
    private void showProxyDialogue() {
        // final AWTProxyBox pSettings = new AWTProxyBox(muckMainFrame, settings);
        final AWTProxyBox pSettings = new AWTProxyBox(muckMainFrame);
        pSettings.pack();
        pSettings.setLocation(PosTools.findCenter(muckMainFrame, pSettings));
        pSettings.show();
    }

    /**
     * Perform all the necessary steps to shut down JamochaMUD, most importantly
     * being to save the settings from this session
     */
    private synchronized void quitJamochaMUD() {
        // "Shut down" and save the details of our other components
        final JMConfig settings = JMConfig.getInstance();

        if (useSwing) {
            // Do we need to find a better location for this?  Fix Me XXX
            final int divLoc = ((MainSwingFrame) muckMainFrame).getDividerLocation();

            settings.setJMValue(JMConfig.DIVIDERLOCATION, divLoc);
        }

        final JMUD master = settings.getJMCore();

        if (master != null) {
            master.quitJamochaMUD();
        }
    }

    /**
     * Set the sync status for all JamochaMUD windows
     *
     * @param state <code>true</code> synchronize windows <code>false</code>
     * windows not to be sycnchronized
     */
    private void setAllSync(final boolean state) {
        final JMUD core = JMConfig.getInstance().getJMCore();
        core.setAllSync(state);
    }

    /**
     * transfer focus to our data entry window, taking into consideration an
     * special events that may also need to be passed along.
     */
    private void transferFocus(final KeyEvent event) {
        // System.err.println("Attempting to write to DataIn: " + event);
        final DataIn target = JMConfig.getInstance().getDataInVariable();

        if (event.getKeyCode() == KeyEvent.VK_ENTER) {
            // The user hit 'ENTER', send the text out
            target.jMSendText();
        }

        target.jmGainFocus();

        if (event.getKeyCode() == KeyEvent.VK_ENTER) {
            target.jMSendText();
            event.consume();
            return;
        }

        if (event.getKeyCode() != KeyEvent.VK_SHIFT && event.getKeyCode() != KeyEvent.VK_CONTROL) {
            if (!event.isControlDown()) {
                // target.append(event.getKeyChar() + "");
                target.append(String.valueOf(event.getKeyChar()));
                // Using keyTyped instead of keyPressed checks our "spool" condition
                target.keyTyped(event);
            }
            event.consume();
        }
    }

    /**
     * Copy the selected text from our active MU
     *
     * to the system clipboard
     */
    private void copyToClipboard() {
        if (DEBUG) {
            System.err.println("MuckMain.copyToClipboard: entered method");
        }
        final Clipboard clip = muckMainFrame.getToolkit().getSystemClipboard();
        boolean releasePause = false;

        try {
            StringSelection selection = new StringSelection("");
            String str = "";
            final CHandler connHandler = CHandler.getInstance();

            if (useSwing) {
                final anecho.gui.JMSwingText text = connHandler.getActiveMUDSwingText();
                str = text.getSelectedText();

                // This sends the display up to position 0.  This is very annoying
                //text.select(0, 0);
                final int finalPos = text.getSelectionEnd();
                text.select(finalPos, finalPos);

                final JMainMenu swingMenu = JMainMenu.getInstance();        // Trying out the JMTFKeys singleton
                releasePause = swingMenu.isReleasePause();
            } else {
                final JMText text = connHandler.getActiveMUDText();
                str = text.getSelectedText();

                // After a copy we should make certain to "unhighlight" our copied area
                // This sends the display up to position 0.  This is very annoying
                // text.select(0, 0);
                final int finalPos = text.getSelectionEnd();
                text.select(finalPos, finalPos);

                final anecho.JamochaMUD.legacy.MainMenu mainMenu = anecho.JamochaMUD.legacy.MainMenu.getInstance();
                releasePause = mainMenu.isReleasePause();
            }

            selection = new StringSelection(str);
            clip.setContents(selection, selection);
            if (DEBUG) {
                System.err.println("MuckMain.copyToClipboard has set contents to: " + selection);
            }

            if (releasePause) {
                // pauseStatus = false;
                callSpoolText();
            }
        } catch (Exception except) {
            if (DEBUG) {
                System.err.println(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("Copy_exception_in_MuckMain.") + except);
                except.printStackTrace();
            }
        }

    }

    /**
     * Paste the contents of our system clipboard
     *
     * into the data-entry window
     */
    private void pasteFromClipboard() {

        final Clipboard clip = muckMainFrame.getToolkit().getSystemClipboard();
        try {
            final Transferable contents = clip.getContents(this);
            final String str = (String) contents.getTransferData(DataFlavor.stringFlavor);

            // Now we'll stick it into the dataText-thing
            final DataIn dataBox = JMConfig.getInstance().getDataInVariable();
            final StringBuffer buff = new StringBuffer(dataBox.getText());
            final int caretPos = dataBox.getCaretPosition();

            buff.insert(caretPos, str);
            dataBox.setText(buff.toString());

        } catch (Exception except) {
            if (DEBUG) {
                System.err.println(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("Paste_exception_in_MuckMain_") + except);
            }
        }
    }

    /**
     * Update the connection menu with the proper number of MU's, indicating
     * connected and disconnected status in addition to which is our active MU*
     */
    private void updateConnectionMenu() {

        if (useSwingMenu) {
            final JMainMenu swingMenu = JMainMenu.getInstance();
            swingMenu.updateConnectionMenu(this);
        } else {
            // muckMainFrame.setMenuBar(tWMenuBar);
            final anecho.JamochaMUD.legacy.MainMenu mainMenu = anecho.JamochaMUD.legacy.MainMenu.getInstance();
            mainMenu.updateConnectionMenu();
        }

    }

    /**
     * Set the state of our connection menu based on our active MU*'s condition
     */
    public void setConnectionMenu() {
        final CHandler connection = CHandler.getInstance();
        boolean active = false;
        try {
            active = connection.isActiveMUDConnected();
        } catch (Exception exc) {
            System.err.println("MuckMain.setConnectionMenu() exception " + exc);
        }
        // if (connection.isActiveMUDConnected()) {
        if (active) {
            connectMenu();
        } else {
            disconnectMenu();
        }

        // Update the list of active/inactive MU*s
        updateConnectionMenu();
    }

    /**
     * Clear all the plugins from our current menu
     *
     */
    public void removeAllPlugins() {

        if (useSwingMenu) {
            final JMainMenu swingMenu = JMainMenu.getInstance();        // Trying out the singleton
            swingMenu.removeAllPlugins();
        } else {
            final anecho.JamochaMUD.legacy.MainMenu mainMenu = anecho.JamochaMUD.legacy.MainMenu.getInstance();
            mainMenu.removeAllPlugins();
        }

    }

    /**
     *
     * Add a new plugin to our plugins menu
     *
     * @param plug
     *
     */
    public void addPlugin(final PlugInterface plug) {

        if (useSwingMenu) {
            final JMainMenu swingMenu = JMainMenu.getInstance();        // Trying out the singleton
            // swingMenu.addPlugin(name, state);
            // swingMenu.addPlugin(itemName, state);
            swingMenu.addPlugin(plug);
        } else {
            final anecho.JamochaMUD.legacy.MainMenu mainMenu = anecho.JamochaMUD.legacy.MainMenu.getInstance();
            // mainMenu.addPlugin(name, state);
            // mainMenu.addPlugin(itemName, state);
            mainMenu.addPlugin(plug);
        }

    }

    /**
     *
     * This method is used to make sure all the classes
     *
     * properly identify the active MU*
     *
     * @param muName
     *
     */
    public void setActiveMU(final String muName) {

        if (muName != null && !muName.equals("")) {
            // First we take apart our String to get the MU number
            int muNum, split;
            split = muName.indexOf(':');
            split++;
            muNum = Integer.parseInt(muName.substring(split));

            final CHandler connHandler = CHandler.getInstance();
            connHandler.setActiveMU(muNum);
        }

        checkCloseMenuState();
    }

    /**
     * This method sets up the lay-out of the main JamochaMUD window
     */
    public void setMainLayout() {
        if (DEBUG) {
            System.err.println("MuckMain.setMainLayout: entering method");
        }

        final JMConfig settings = JMConfig.getInstance();

        // To test we're going to avoid the rest of the method altogether!
        // Fix Me XXX
        if (useSwing) {
            // Add the text entry area into our Split-pane
            final DataIn input = settings.getDataInVariable();
            final javax.swing.JScrollPane tempText = (javax.swing.JScrollPane) (input.exportText());
            ((MainSwingFrame) muckMainFrame).setPaneComponent(tempText, MainSwingFrame.BOTTOM);
            // Add the main tabbed panel to our Split-pane
            anecho.gui.JMFancyTabbedPane tempPane = this.getFancyTextPanel();

            // We'll do the lazy initialisation here, as we can't guarantee that we'll have
            // a usable JTabbedPane by the time we get here.
            if (tempPane == null) {

                // Should the be allocated to the constructor?  Fix Me XXX
                textPanel = new anecho.gui.JMFancyTabbedPane();
                tempPane = this.getFancyTextPanel();

                // Add listener
                //if (!listenerSet) {
                if (DEBUG) {
                    System.err.println("MuckMain.setMainLayout added changeListener to pane.");
                }
                tempPane.addChangeListener(new javax.swing.event.ChangeListener() {

                    @Override
                    public void stateChanged(javax.swing.event.ChangeEvent change) {

                        panelTabChanged(change);

                    }
                });

                listenerSet = true;

                // }
            }

            ((MainSwingFrame) muckMainFrame).setPaneComponent(tempPane, MainSwingFrame.TOP);

            int divLoc = settings.getJMint(JMConfig.DIVIDERLOCATION);

            if (divLoc < 0) {

                if (DEBUG) {
                    System.err.println(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("The_height_of_our_frame_is_") + ((MainSwingFrame) muckMainFrame).getHeight());
                }

                divLoc = ((MainSwingFrame) muckMainFrame).getHeight() - 120;

                // As this value doesn't already exist, we'll set it in our configuration now
                settings.setJMValue(JMConfig.DIVIDERLOCATION, divLoc);

            }

            ((MainSwingFrame) muckMainFrame).setDividerLocation(divLoc);

        } else {
            // Start of OLD METHOD FIX ME XXX
            this.doOldLayout();
        }

    }

    /**
     * Remove the active view from our list of MU*s. Since some Operating
     * Systems seem to synchronize things in ways that I hadn't foresee
     * previously, we'll have to do everything by the time-stamp of the MU, so
     * we don't mess things up!
     */
    private void closeActiveWindow() {
        // remove the MU from our Cardlayout
        final CHandler connHandler = CHandler.getInstance();
        final MuSocket muSock = connHandler.getActiveMUHandle();

        closeWindow(muSock);
    }

    /**
     * Close a window based on the passed MuSocket
     */
    private void closeWindow(final MuSocket muSock) {

        final CHandler connHandler = CHandler.getInstance();
        connHandler.removeMU(muSock);  // Causes layout problems somewhere down the line XXX
        updateStates();
    }

    /**
     * This method will add the currently displayed MU* to the World Connector
     *
     */
    private void addToConnector() {

        logger.debug("MuckMain.addToConnector() called.");
        final AddEditWorld tempConnector = new AddEditWorld();
        final CHandler handler = CHandler.getInstance();
        final MuSocket muck = handler.getActiveMUHandle();

        final String muName = muck.getMUName();
        final String muAddress = muck.getAddress();
        final int muPort = muck.getPort();
        final boolean useSSL = muck.isSSL();

        tempConnector.checkWorld(muName, muAddress, muPort, useSSL);

        logger.debug("MuckMain.addToConnector() finished.");
    }

    /**
     * Update the window title, set the visible MU* and make certain that our
     * connection menu is showing the proper states
     */
    private void updateStates() {
        setWindowTitle();
        setVisibleMU();

        checkCloseMenuState();
    }

    /**
     * This method checks to see if the &quot;Close current view" Menu item
     * should be active or not
     */
    private void checkCloseMenuState() {

        final CHandler connHandler = CHandler.getInstance();

        int totalConn = 0;
        boolean closeEnabled = false;
        boolean connectEnabled = false;

        totalConn = connHandler.totalConnections();

        boolean active = false;
        try {
            active = connHandler.isActiveMUDConnected();
        } catch (Exception exc) {
            if (DEBUG) {
                System.err.println("MuckMain.checkCloseMenuState() exception " + exc);
            }
        }

        if (active) {
            connectEnabled = true;
        }

        if (useSwingMenu) {

            final JMainMenu swingMenu = JMainMenu.getInstance();

            if (totalConn > 1) {
                closeEnabled = true;
            }
            swingMenu.setCloseMUEnabled(closeEnabled);
            swingMenu.setConnected(connectEnabled);

        } else {
            final anecho.JamochaMUD.legacy.MainMenu mainMenu = anecho.JamochaMUD.legacy.MainMenu.getInstance();

            if (totalConn > 1) {
                closeEnabled = true;
            } else {
                if (DEBUG) {
                    System.err.println(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("MuckMain.checkCloseMenuState()_total_connections_1_or_less."));
                }
            }

            mainMenu.setCloseMUEnabled(closeEnabled);

            mainMenu.setConnected(connectEnabled);
        }

    }

    /**
     * display the next or previous MU, depending on the direction given
     *
     * @param direction
     */
    public void advanceMU(final int direction) {
        final CHandler handler = CHandler.getInstance();

        // change the active MU*
        if (direction == NEXT) {
            handler.nextMU();
        } else {
            handler.previousMU();
        }

        // now update our display
        setWindowTitle();
        setVisibleMU();
    }

    /**
     * This method enables or disables double-buffering of the output display.
     * Enabling double-buffering creates a smoother scrolling effect for output.
     * Disabling double-buffering creates a "flashing" effect, but can be faster
     * on older machines.
     *
     * @param state <code>true</code> enable double-buffering <code>false</code>
     * disable double-buffering
     */
    private void setDoubleBuffer(final boolean state) {
        JMConfig.getInstance().setJMValue(JMConfig.DOUBLEBUFFER, state);
        final CHandler handler = CHandler.getInstance();
        handler.setDoubleBuffer(state);
    }

    /**
     * Allows the enabling and disabling of font anti-aliasing in the input and
     * output windows
     *
     * @param state <code>true</code> enable anti-aliasing <code>false</code>
     * disable anti-aliasing
     */
    private void setAntiAliasing(final boolean state) {
        if (DEBUG) {
            System.err.println(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("MuckMain.setAntiAliasing_to_:_") + state);
        }
        final JMConfig settings = JMConfig.getInstance();
        settings.setJMValue(JMConfig.ANTIALIAS, state);
        final CHandler handler = CHandler.getInstance();
        handler.setAntiAliasing(state);

        // Change our text entry area
        settings.getDataInVariable().setAntiAliasing(state);
    }

    /**
     *
     * @param state
     */
    private void setLowColour(final boolean state) {
        if (DEBUG) {
            System.err.println(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("MuckMain_settings_LOWCOLOUR_to:_") + state);
        }
        final JMConfig settings = JMConfig.getInstance();
        settings.setJMValue(JMConfig.LOWCOLOUR, state);

        final CHandler handler = CHandler.getInstance();
        handler.setLowColour(state);
    }

    /**
     * Returns the "generic" Component version of TextPanel
     *
     * @return
     */
    public Component getTextPanel() {
        return textPanel;
    }

    /**
     * Returns the textPanel pre-cast to a JMFancyTabbedPane
     *
     * @return
     */
    public anecho.gui.JMFancyTabbedPane getFancyTextPanel() {
        return (anecho.gui.JMFancyTabbedPane) textPanel;
    }

    /**
     * Returns the textPanel pre-cast to the older legacy JMTabPanel
     *
     * @return
     */
    public anecho.JamochaMUD.legacy.JMTabPanel getLegacyTextPanel() {
        final anecho.JamochaMUD.legacy.JMTabPanel tempPane = (anecho.JamochaMUD.legacy.JMTabPanel) textPanel;

        return tempPane;

    }

    /**
     * Call the SpoolText proceedure that belongs to the correct JMText
     */
    private void callSpoolText() {

        final CHandler target = CHandler.getInstance();
        // Check to see if we actually have any connections before we try to spool text

        if (DEBUG) {
            System.err.println(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("MuckMain.callSpoolText_checking_total_connections."));
        }

        if (target.totalConnections() > 0) {

            final MuSocket mSock = target.getActiveMUHandle();
            if (mSock.isPaused()) {
                // This spools out any paused text
                mSock.spoolText();
            }
        }

        if (DEBUG) {
            System.err.println(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("MuckMain.callSpoolText_leaving_method."));
        }
    }

    /**
     * This method calls EnumPlugIns to create a dialogue for the installation
     * of a new plug-in
     */
    private void installPlugIn() {

        final EnumPlugIns plugEnum = EnumPlugIns.getInstance();

        plugEnum.addNewPlugIn();

    }

    /**
     * Rebuild the plug-in menu to reflect its current state.
     *
     * This is usually called after activating/deactivating plug-ins
     *
     */
    public void rebuildPlugInMenu() {
        if (DEBUG) {
            System.err.println(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("MuckMain.rebuildPlugInMenu()_called."));
        }

        // Clear out all the old entries
        removeAllPlugins();

        // Cycle through our list of plug-ins and add them back to the menu
        final Vector pClasses = EnumPlugIns.plugInClass;

        final int pcs = pClasses.size();

        for (int i = 0; i < pcs; i++) {
            addPlugin((PlugInterface) pClasses.elementAt(i));
        }

    }

    /**
     * Bring up the configuration dialogue box (Java2 only)
     */
    private void configureJamochaMUD() {
        final anecho.JamochaMUD.PrefDialogue prefD = new anecho.JamochaMUD.PrefDialogue(muckMainFrame, true);
        prefD.setVisible(true);
    }

    /**
     * Do the text output layout with AWT components
     */
    private void oldAWTLayout() {
        final JMConfig settings = JMConfig.getInstance();

        muckMainFrame.setLayout(null);

        final int style = viewStyle;
        final DataIn input = settings.getDataInVariable();
        GridBagConstraints constraints;
        final GridBagLayout mainBagLayout = new GridBagLayout();
        constraints = new GridBagConstraints();

        muckMainFrame.setLayout(mainBagLayout);

        if (style == COMBINED) {
            // Set the style for the combined window
            constraints.gridwidth = GridBagConstraints.REMAINDER;
            constraints.gridheight = 5;
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.weightx = 1;
            constraints.weighty = 1;
            constraints.insets = new Insets(0, 0, 2, 0);
            constraints.fill = GridBagConstraints.BOTH;
            constraints.anchor = GridBagConstraints.CENTER;

            anecho.JamochaMUD.legacy.JMTabPanel tempPane = this.getLegacyTextPanel();

            if (tempPane == null) {
                textPanel = new anecho.JamochaMUD.legacy.JMTabPanel();
                tempPane = this.getLegacyTextPanel();

                mainBagLayout.setConstraints(tempPane, constraints);  // Ugly code Fix me XXX
                muckMainFrame.add(tempPane);
            }

            constraints.gridwidth = GridBagConstraints.REMAINDER;
            constraints.gridheight = GridBagConstraints.REMAINDER;
            constraints.gridx = 0;
            constraints.gridy = 5;
            constraints.weighty = 0;
            constraints.insets = new Insets(0, 0, 0, 0);
            constraints.fill = GridBagConstraints.BOTH;
            constraints.anchor = GridBagConstraints.CENTER;

            // This repeats in 2 methods.  Fix Me XXX!!!
            if (settings.getJMboolean(JMConfig.USESWINGENTRY)) {
                final javax.swing.JScrollPane tempText = (javax.swing.JScrollPane) (input.exportText());
                mainBagLayout.setConstraints(tempText, constraints);
                ((javax.swing.JFrame) muckMainFrame).getContentPane().add(tempText);
            } else {
                final TextArea tempText = (TextArea) (input.exportText());
                mainBagLayout.setConstraints(tempText, constraints);
                if (useSwing) {
                    ((javax.swing.JFrame) muckMainFrame).getContentPane().add(tempText);
                } else {
                    muckMainFrame.add(tempText);
                }
            }

        } else {
            constraints.gridwidth = GridBagConstraints.REMAINDER;
            constraints.gridheight = GridBagConstraints.REMAINDER;
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.weightx = 1;
            constraints.weighty = 1;
            constraints.insets = new Insets(0, 0, 0, 0);
            constraints.fill = GridBagConstraints.BOTH;
            constraints.anchor = GridBagConstraints.CENTER;
            mainBagLayout.setConstraints(this.getTextPanel(), constraints);

            muckMainFrame.add(this.getLegacyTextPanel());

            // Now make our inputBuffer bar visible again
            input.restoreText();

        }

        mainBagLayout.invalidateLayout(muckMainFrame);
        mainBagLayout.layoutContainer(muckMainFrame);
        muckMainFrame.invalidate();
        muckMainFrame.validate();
        muckMainFrame.doLayout();

        // Should we try and absolute size the DataIn?
        // Fix this XXX - in the future we want the user to decide how many
        // lines tall our DataIn textarea is!
        input.setRows(3);

        viewStyle = style;

        updateConnectionMenu();
        settings.setJMValue(JMConfig.MAINLAYOUTVALID, true);

    }

    /**
     * Do the text output layout using the new Swing components
     */
    private void oldSwingLayout() {
        final JMConfig settings = JMConfig.getInstance();

        ((javax.swing.JFrame) muckMainFrame).getContentPane().setLayout(null);

        final int style = viewStyle;
        final DataIn input = settings.getDataInVariable();
        GridBagConstraints constraints;
        final GridBagLayout mainBagLayout = new GridBagLayout();
        constraints = new GridBagConstraints();

        ((javax.swing.JFrame) muckMainFrame).getContentPane().setLayout(mainBagLayout);

        if (style == COMBINED) {
            // Set the style for the combined window
            constraints.gridwidth = GridBagConstraints.REMAINDER;
            constraints.gridheight = 5;
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.weightx = 1;
            constraints.weighty = 1;
            constraints.insets = new Insets(0, 0, 2, 0);
            constraints.fill = GridBagConstraints.BOTH;
            constraints.anchor = GridBagConstraints.CENTER;

            anecho.gui.JMFancyTabbedPane tempPane = this.getFancyTextPanel();

            // We'll do the lazy initialisation here, as we can't guarantee that we'll have
            // a usable JTabbedPane by the time we get here.
            if (tempPane == null) {
                if (DEBUG) {
                    System.err.println(java.util.ResourceBundle.getBundle(BUNDLENAME).getString("MuckMain.setMainLayout()_doing_lazy_initialisation_of_JTabbedPane"));
                }

                textPanel = new anecho.gui.JMFancyTabbedPane();
                tempPane = this.getFancyTextPanel();

            }

            mainBagLayout.setConstraints(tempPane, constraints);  // Ugly code Fix me XXX
            ((javax.swing.JFrame) muckMainFrame).getContentPane().add(tempPane);

            if (!listenerSet) {
                tempPane.addChangeListener(new javax.swing.event.ChangeListener() {

                    @Override
                    public void stateChanged(final javax.swing.event.ChangeEvent change) {

                        checkCloseMenuState();

                        updateConnectionMenu();

                        // Should change Main Title here, too.  Fix Me XXX
                        final anecho.gui.JMFancyTabbedPane src = (anecho.gui.JMFancyTabbedPane) (change.getSource());

                        if (src.isValid()) {
                            // We don't want to try and set the title if it
                            // isn't yet a valid object
                            setWindowTitle();
                        }

                    }
                });

                listenerSet = true;
            }

            constraints.gridwidth = GridBagConstraints.REMAINDER;
            constraints.gridheight = GridBagConstraints.REMAINDER;
            constraints.gridx = 0;
            constraints.gridy = 5;
            constraints.weighty = 0;
            constraints.insets = new Insets(0, 0, 0, 0);
            constraints.fill = GridBagConstraints.BOTH;
            constraints.anchor = GridBagConstraints.CENTER;
            // TextArea tempText = in.exportText();

            // This repeats in 2 methods.  Fix Me XXX!!
            if (settings.getJMboolean(JMConfig.USESWINGENTRY)) {
                final javax.swing.JScrollPane tempText = (javax.swing.JScrollPane) (input.exportText());
                mainBagLayout.setConstraints(tempText, constraints);
                ((javax.swing.JFrame) muckMainFrame).getContentPane().add(tempText);
            } else {
                final TextArea tempText = (TextArea) (input.exportText());
                mainBagLayout.setConstraints(tempText, constraints);
                if (useSwing) {
                    ((javax.swing.JFrame) muckMainFrame).getContentPane().add(tempText);
                } else {
                    muckMainFrame.add(tempText);
                }
            }

        } else {
            constraints.gridwidth = GridBagConstraints.REMAINDER;
            constraints.gridheight = GridBagConstraints.REMAINDER;
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.weightx = 1;
            constraints.weighty = 1;
            constraints.insets = new Insets(0, 0, 0, 0);
            constraints.fill = GridBagConstraints.BOTH;
            constraints.anchor = GridBagConstraints.CENTER;
            mainBagLayout.setConstraints(this.getTextPanel(), constraints);

            muckMainFrame.add(this.getLegacyTextPanel());

            // Now make our inputBuffer bar visible again
            input.restoreText();

        }

        mainBagLayout.invalidateLayout(muckMainFrame);
        mainBagLayout.layoutContainer(muckMainFrame);
        muckMainFrame.invalidate();
        muckMainFrame.validate();
        muckMainFrame.doLayout();

        // Should we try and absolute size the DataIn?
        // Fix this XXX - in the future we want the user to decide how many
        // lines tall our DataIn textarea is!
        input.setRows(3);

        viewStyle = style;

        updateConnectionMenu();
        settings.setJMValue(JMConfig.MAINLAYOUTVALID, true);

    }

    /**
     * Set-up the old layout.
     *
     * @param connHandler
     */
    private void doOldLayout() {
        if (useSwing) {
            oldSwingLayout();
        } else {
            oldAWTLayout();
        }

    }

    /**
     * Return the main frame to the calling method
     *
     * @return
     */
    public Frame getMainFrame() {
        return muckMainFrame;
    }

    /**
     * Show a dialogue box showing the latest changes/fixes in JamochaMUD
     *
     * @param modal Sets the Whats New dialogue as modal or not
     */
    public void showWhatsNew(boolean modal) {
        final WhatsNew whatsBox = new WhatsNew(muckMainFrame, modal);
        whatsBox.setLocationRelativeTo(muckMainFrame);
        whatsBox.setVisible(true);
    }

    /**
     * This method handles all the details of a panel change event when using
     * the Swing toolkit.
     *
     * @param objChange An object that is later cast into a ChangeEvent for
     * processing
     */
    private void panelTabChanged(final Object objChange) {
        final javax.swing.event.ChangeEvent change = (javax.swing.event.ChangeEvent) objChange;
        if (DEBUG) {
            System.err.println("MuckMain.panelTabChanged() entered stateChanged code." + change);
        }
        checkCloseMenuState();
        updateConnectionMenu();

        // Should change Main Title here, too.  Fix Me XXX
        final anecho.gui.JMFancyTabbedPane src = (anecho.gui.JMFancyTabbedPane) (change.getSource());

        if (DEBUG) {
            System.err.println("MuckMain.panelTabChanged() checking to see if source is valid.");
            System.err.println("MuckMain.panelTabChanged() Change source: " + change.getSource());
        }

        // if (src.isValid()) {
        // if (src != null) {
        if (src == null) {
            if (DEBUG) {
                System.err.println("MuckMain.panelTabChanged() doesn't have valid target.");
            }

        } else {
            if (DEBUG) {
                System.err.println("MuckMain.panelTabChanged() source is valid.");
            }
            // We don't want to try and set the title if it
            // isn't yet a valid object

            setWindowTitle();

            if (DEBUG) {
                System.err.println("MuckMain.panelTabChanged() setting active MU.");
            }
            final CHandler handler = CHandler.getInstance();
            // handler.setActiveMUStatus(src.getSelectedComponent());
            handler.setActiveMU(src.getSelectedIndex());
            // reset colours on active tab
            src.flashTab(src.getSelectedIndex(), false);
        }
    }
}
