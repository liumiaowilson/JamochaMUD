/* MusicBox.java
 * Reads specified files written by music playing software
 * which can then be put directly into JamochaMUD
 */

/* MusicBox Copyright (C) 2004 Jeff Robinson
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. *
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
package anecho.JamochaMUD.plugins;

/** Creates an interface used by all JamochaMUD plugins
 * $Id: MusicBox.java,v 1.7 2010/01/18 03:52:21 jeffnik Exp $
 * @author Jeff Robinson
 */
import anecho.JamochaMUD.JMConfig;
import anecho.JamochaMUD.MuSocket;

// import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MusicBox implements PlugInterface, KeyListener {

    private transient boolean active = false;
    // private JMConfig settings;
    private transient String playFile;    // The file containing the playing music info
    private transient String dispLine;    // String describing how to display playing information
    private transient boolean useCTRL = false;
    private transient boolean useALT = true;
    private transient boolean useSHIFT = false;
    private transient char ctrlKey = 'm';
    // private java.awt.AWTKeyStroke kStroke;
    private static final boolean DEBUG = false;

    /**
     * Collect the settings file information that we might need.
     * @param settings Our JamochaMUD settings file
     */
    public void setSettings(JMConfig settings) {
    // this.settings = settings;
    }

    /**
     * Returns the plugin's proper name
     * @return The plug-in's name to be displayed by JamochaMUD.
     */
    public String plugInName() {
        return java.util.ResourceBundle.getBundle("anecho/JamochaMUD/plugins/MusicBoxDir/MusicBoxBundle").getString("MusicBox");
    }

    /**
     * Returns a description, eg. author, date, build...
     * @return Returns a human-readable description of the plug-in used by JamochaMUD.
     */
    public String plugInDescription() {
        final String retStr = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/plugins/MusicBoxDir/MusicBoxBundle").getString("MusicBoxDescription");
        return retStr;

    }

    /**
     * Returns a type of either input, output, or other
     * @return Returns our plug-in type to JamochaMUD.
     */
    public String plugInType() {
        return java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("other");
    }

    /**
     * the core of the plugin
     * @param jamochaString Original string supplied to our plug-in by JamochaMUD.  Not used in this
     * instance.
     * @param mSock The mSock* socket that our input would come from.  Not used in this plug-in.
     * @return Returns an altered string to JamochaMUD.  Not used in this plug-in.
     */
    public String plugMain(final String jamochaString, final MuSocket mSock) {
        return jamochaString;
    }

    /** Any user configurable options for the plugin */
    public void plugInProperties() {
        final JMConfig settings = JMConfig.getInstance();
        final java.awt.Frame mainFrame = anecho.JamochaMUD.MuckMain.getInstance().getMainFrame();
        final boolean USESWING = settings.getJMboolean(JMConfig.USESWING);

        if (playFile == null) {
            playFile = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("");
        }

        if (dispLine == null || dispLine.equals(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString(""))) {
            dispLine = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("%2%_-_%4%");
        }

        if (USESWING) {
            final anecho.JamochaMUD.plugins.MusicBoxDir.MusicBoxGUI mbg = new anecho.JamochaMUD.plugins.MusicBoxDir.MusicBoxGUI(mainFrame, true);
            mbg.setPlayFile(playFile);
            mbg.setDispLine(dispLine);
            mbg.setCTRLKey(ctrlKey);
            mbg.setUseCTRL(useCTRL);
            mbg.setUseSHIFT(useSHIFT);
            mbg.setUseALT(useALT);

            mbg.setVisible(true);

            if (mbg.isOkay()) {
                playFile = mbg.getPlayFile();
                dispLine = mbg.getDispLine();
                useCTRL = mbg.useCTRL();
                useSHIFT = mbg.useSHIFT();
                useALT = mbg.useALT();
                ctrlKey = mbg.getCTRLKey();

                saveSettings();
            }

        } else {
            final anecho.JamochaMUD.plugins.MusicBoxDir.MusicBoxAWTGUI mbg = new anecho.JamochaMUD.plugins.MusicBoxDir.MusicBoxAWTGUI(mainFrame, true);
            mbg.setPlayFile(playFile);
            mbg.setDispLine(dispLine);
            mbg.setCTRLKey(ctrlKey);
            mbg.setUseCTRL(useCTRL);
            mbg.setUseSHIFT(useSHIFT);
            mbg.setUseALT(useALT);

            mbg.setVisible(true);

            if (mbg.isOkay()) {
                playFile = mbg.getPlayFile();
                dispLine = mbg.getDispLine();
                useCTRL = mbg.useCTRL();
                useSHIFT = mbg.useSHIFT();
                useALT = mbg.useALT();
                ctrlKey = mbg.getCTRLKey();

                saveSettings();
            }
        }

    // Check to see if we need to save our new settings
        /*
    if (result) {
    if (DEBUG) {
    System.err.println("We received an 'okay'");
    }
    if (USESWING) {
    // grab our settings
    playFile = mbg.getPlayFile();
    dispLine = mbg.getDispLine();
    useCTRL = mbg.useCTRL();
    useSHIFT = mbg.useSHIFT();
    useALT = mbg.useALT();
    ctrlKey = mbg.getCTRLKey();
    } else {
    }
    // Save the settings to disk
    saveSettings();
    }
     */
    }

    /**
     * Check to see if the plug-in has configurable properties
     * @returns <pre>true</pre> - plug-in can be configured
     * <pre>false</pre> - plug-in cannot be configured
     * @return This plug-in returns true as it can be configured.
     */
    public boolean hasProperties() {
        return true;
    }

    /** This function is called at load-up, in case
     * properties are needed... eg. lists, settings */
    public void initialiseAtLoad() {
        // Load our settings
        readSettings();
    }

    /** This function tells the plug-in that it should be functioning.
     * See Deactive() for the inverse function.  Activate may be called
     * more than once.
     */
    public void activate() {
        // Add a listener to our DataIn so we can catch our cue!
        final JMConfig settings = JMConfig.getInstance();

        // Put this is a try-block just in the event that our components do not exist.
        try {
            final java.awt.Component lTarget = (settings.getDataInVariable()).getTextVariable();
            lTarget.addKeyListener(this);
            active = true;
        } catch (Exception exc) {
            active = false;
        }

    }

    /** This function tells the plug-in that it should not be active.
     * Deactivate may be called more than once.
     * Formerly this function was handled by SetAtHalt.
     */
    public void deactivate() {
        // Remove our listener from DataIn, effectively disabling this plugin
        final JMConfig settings = JMConfig.getInstance();

        try {
            final java.awt.Component lTarget = (settings.getDataInVariable()).getTextVariable();
            lTarget.removeKeyListener(this);
        } catch (Exception exc) {
            if (DEBUG) {
                System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("MusicBox.Deactivate()_exception_trying_to_remove_listener_") + exc);
            }
        }

        active = false;
    }

    /* Returns a result of true if the plug-in is active, or false if it is not
     */
    /**
     * Returns to JamochaMUD whether our plug-in is currently active or not.
     * @return Returns <PRE>true</PRE> if the plug-in is currently active and
     * <PRE>false</PRE> if inactive.
     */
    public boolean isActive() {
        return active;
    }

    /** Previously this function did most of the functions of Deactivate()
     * but now this is reserved solely for shutting down the plug-in
     * and doing any necessary clean-up.
     */
    public void setAtHalt() {
        saveSettings();
    }

    /**
     * This function indicates whether a directory
     * is required to hold settings for our plug-in.
     * It is recommended for consistency to let JamochaMUD
     * create and handle any plug-in directories.  Plus,
     * it means you can use less code in your plug-in!
     * @return <pre>true</pre> - create a settings directory
     * <pre>false</pre> - no directory required
     */
    public boolean haveConfig() {
        return true;
    }

    /**
     * This method processes the keyTyped event out-side of the event processing code.
     * In this way the method can be tested with-out having to generate a KeyEvent
     * @param key
     * @param shiftKey
     * @param controlKey
     */
    public void processKeyTyped(char key, boolean isShift, boolean isControl, boolean isAlt) {

        if (DEBUG) {
            System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("plugins.MusicBox.keyTyped:_") + key);
            // System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Keycode_is_:_") + evtKey.getKeyCode());
            // System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Key_Text_is_:_") + KeyEvent.getKeyText(evtKey.getKeyCode()));
            // System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Key_Modifiers_Text_is_:_") + KeyEvent.getKeyModifiersText(evtKey.getModifiers()));
            // System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Key_character_is_:_") + evtKey.getKeyChar());
        }

        // Check for our modifiers first
        if (useCTRL && !isControl) {
            return;
        }

        if (DEBUG) {
            System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Passed_Control_test."));
        }

        if (useSHIFT && !isShift) {
            return;
        }

        if (DEBUG) {
            System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Passed_Shift_test."));
        }

        if (useALT && !isAlt) {
            return;
        }

        if (DEBUG) {
            System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Passed_Alt_test."));
        }

        if (key != ctrlKey) {
            if (DEBUG) {
                System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Our_keyEvent_") + key + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("_doesn't_match_our_ctrlKey_") + ctrlKey);
            }

            return;
        }

        if (DEBUG) {
            System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("We've_got_the_key_we're_looking_for!"));
        }

        // Process request for playing.txt
        // Do our sanity check first
        if (playFile != null && dispLine != null && !playFile.equals(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("")) && !dispLine.equals(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString(""))) {

            final java.io.File info = new java.io.File(playFile);

            java.io.RandomAccessFile reader;
            final java.util.Vector rules = new java.util.Vector();

            boolean loop = true;

            if (DEBUG) {
                System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Attempting_to_read_file_:_") + info.getAbsolutePath());
            }

            try {
                reader = new java.io.RandomAccessFile(info.getAbsolutePath(), java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("r"));


                String line;
                // StringBuffer fullLine = new StringBuffer("");

                while (loop) {
                    if (DEBUG) {
                        System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Loop..."));
                    }

                    try {
                        line = reader.readLine();
                    } catch (Exception e) {
                        // We're all out of lines
                        if (DEBUG) {
                            System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("We_ran_out_of_lines"));
                        }
                        break;
                    }

                    // if (line == null || line.trim().equals("")) {
                    if (line == null) {
                        loop = false;
                        break;
                    }

                    line.trim();
                    // fullLine.append(line);

                    if (DEBUG) {
                        System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Read_rule:_") + line);
                    }

                    rules.addElement(line);
                }

                reader.close();

            } catch (Exception exc) {
                // We can't find our playing file.
                if (DEBUG) {
                    System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("MusicBox_plugin_could_not_access_") + info + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString(",_exception_") + exc);
                    exc.printStackTrace();
                }
            }

            final StringBuffer retStr = new StringBuffer();

            int start = -1;
            // int end = -1;
            final int dispLen = dispLine.length();
            char testC;

            // Read through the characters one by one and look for our
            // %number% format
            for (int i = 0; i < dispLen; i++) {
                testC = dispLine.charAt(i);

                if (testC == '%') {
                    if (start < 0) {
                        start = i;
                    } else {
                        // We've found and end marker
                        final String item = dispLine.substring(start + 1, i);
                        if (DEBUG) {
                            System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Testing_%_string:_") + item);
                        }

                        try {
                            final int lineNum = Integer.parseInt(item);

                            retStr.append(rules.elementAt(lineNum - 1).toString());
                            start = -1;

                        } catch (Exception exc) {
                            // The isn't a number, so we should just append what we have
                            if (DEBUG) {
                                System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Exception_append:_") + dispLine.substring(start, i));
                            }
                            retStr.append(dispLine.substring(start, i));
                            start = -1;
                        }

                    }
                } else {
                    if (start < 0) {
                        retStr.append(testC);
                    }
                }
            }

            // Clean up any stragglers that we may have
            if (start > -1) {
                retStr.append(dispLine.substring(start));
            }

            if (DEBUG) {
                System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Our_final_string_from_MusicBox_is:_") + retStr.toString());
            }

            final JMConfig settings = JMConfig.getInstance();
            settings.getDataInVariable().append(retStr.toString());
        // Parse our lines

        }

    }
    
    /**
     * 
     * @param evtKey 
     */
    public void keyTyped(java.awt.event.KeyEvent evtKey) {

        final char key = evtKey.getKeyChar();
        boolean isShift = evtKey.isShiftDown();
        boolean isControl = evtKey.isControlDown();
        boolean isAlt = evtKey.isAltDown();
        
        processKeyTyped(key, isShift, isControl, isAlt);
        
//        if (DEBUG) {
//            System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("plugins.MusicBox.keyTyped:_") + evtKey);
//            System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Keycode_is_:_") + evtKey.getKeyCode());
//            System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Key_Text_is_:_") + KeyEvent.getKeyText(evtKey.getKeyCode()));
//            System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Key_Modifiers_Text_is_:_") + KeyEvent.getKeyModifiersText(evtKey.getModifiers()));
//            System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Key_character_is_:_") + evtKey.getKeyChar());
//        }
//
//        // Check for our modifiers first
//        if (useCTRL && !evtKey.isControlDown()) {
//            return;
//        }
//
//        if (DEBUG) {
//            System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Passed_Control_test."));
//        }
//
//        if (useSHIFT && !evtKey.isShiftDown()) {
//            return;
//        }
//
//        if (DEBUG) {
//            System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Passed_Shift_test."));
//        }
//
//        if (useALT && !evtKey.isAltDown()) {
//            return;
//        }
//
//        if (DEBUG) {
//            System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Passed_Alt_test."));
//        }
//
//        if (key != ctrlKey) {
//            if (DEBUG) {
//                System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Our_keyEvent_") + key + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("_doesn't_match_our_ctrlKey_") + ctrlKey);
//            }
//
//            return;
//        }
//
//        if (DEBUG) {
//            System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("We've_got_the_key_we're_looking_for!"));
//        }
//
//        // Process request for playing.txt
//        // Do our sanity check first
//        if (playFile != null && dispLine != null && !playFile.equals(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("")) && !dispLine.equals(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString(""))) {
//
//            final java.io.File info = new java.io.File(playFile);
//
//            java.io.RandomAccessFile reader;
//            final java.util.Vector rules = new java.util.Vector();
//
//            boolean loop = true;
//
//            if (DEBUG) {
//                System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Attempting_to_read_file_:_") + info.getAbsolutePath());
//            }
//
//            try {
//                reader = new java.io.RandomAccessFile(info.getAbsolutePath(), java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("r"));
//
//
//                String line;
//                // StringBuffer fullLine = new StringBuffer("");
//
//                while (loop) {
//                    if (DEBUG) {
//                        System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Loop..."));
//                    }
//
//                    try {
//                        line = reader.readLine();
//                    } catch (Exception e) {
//                        // We're all out of lines
//                        if (DEBUG) {
//                            System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("We_ran_out_of_lines"));
//                        }
//                        break;
//                    }
//
//                    // if (line == null || line.trim().equals("")) {
//                    if (line == null) {
//                        loop = false;
//                        break;
//                    }
//
//                    line.trim();
//                    // fullLine.append(line);
//
//                    if (DEBUG) {
//                        System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Read_rule:_") + line);
//                    }
//
//                    rules.addElement(line);
//                }
//
//                reader.close();
//
//            } catch (Exception exc) {
//                // We can't find our playing file.
//                if (DEBUG) {
//                    System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("MusicBox_plugin_could_not_access_") + info + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString(",_exception_") + exc);
//                    exc.printStackTrace();
//                }
//            }
//
//            final StringBuffer retStr = new StringBuffer();
//
//            int start = -1;
//            // int end = -1;
//            final int dispLen = dispLine.length();
//            char testC;
//
//            // Read through the characters one by one and look for our
//            // %number% format
//            for (int i = 0; i < dispLen; i++) {
//                testC = dispLine.charAt(i);
//
//                if (testC == '%') {
//                    if (start < 0) {
//                        start = i;
//                    } else {
//                        // We've found and end marker
//                        final String item = dispLine.substring(start + 1, i);
//                        if (DEBUG) {
//                            System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Testing_%_string:_") + item);
//                        }
//
//                        try {
//                            final int lineNum = Integer.parseInt(item);
//
//                            retStr.append(rules.elementAt(lineNum - 1).toString());
//                            start = -1;
//
//                        } catch (Exception exc) {
//                            // The isn't a number, so we should just append what we have
//                            if (DEBUG) {
//                                System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Exception_append:_") + dispLine.substring(start, i));
//                            }
//                            retStr.append(dispLine.substring(start, i));
//                            start = -1;
//                        }
//
//                    }
//                } else {
//                    if (start < 0) {
//                        retStr.append(testC);
//                    }
//                }
//            }
//
//            // Clean up any stragglers that we may have
//            if (start > -1) {
//                retStr.append(dispLine.substring(start));
//            }
//
//            if (DEBUG) {
//                System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Our_final_string_from_MusicBox_is:_") + retStr.toString());
//            }
//
//            final JMConfig settings = JMConfig.getInstance();
//            settings.getDataInVariable().append(retStr.toString());
//        // Parse our lines
//
//        }
    }

    /**
     * 
     * @param key 
     */
    public void keyPressed(final java.awt.event.KeyEvent key) {
    }

    /**
     * 
     * @param key 
     */
    public void keyReleased(final java.awt.event.KeyEvent key) {
    }

    /**
     * This method saves the current MusicBox settings to a file.
     */
    protected void saveSettings() {

        if (DEBUG) {
            System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Saving_settings..."));
        }

        final JMConfig settings = JMConfig.getInstance();
        final String pathSep = java.io.File.separator;
        final String plugIns = settings.getJMString(anecho.JamochaMUD.JMConfig.USERPLUGINDIR);
        final String musicBoxDir = plugIns + pathSep + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("MusicBoxDir");
        final java.io.File info = new java.io.File(musicBoxDir + pathSep + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString(".musicbox.rc"));

        if (info.exists()) {
            info.delete();
        }

        try {
            final java.io.FileOutputStream outputFile = new java.io.FileOutputStream(info);
            final java.io.ObjectOutputStream sStream = new java.io.ObjectOutputStream(outputFile);

            sStream.writeObject(playFile);
            sStream.writeObject(dispLine);
            sStream.writeBoolean(useCTRL);
            sStream.writeBoolean(useALT);
            sStream.writeBoolean(useSHIFT);
            sStream.writeChar(ctrlKey);

            sStream.flush();
        } catch (Exception exc) {
            System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("MusicBox.saveSettings()_Macro_serialization_error_") + exc);
        }

        if (DEBUG) {
            System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Finished_saving_file."));
        }
    }

    /**
     * This method reads the MusicBox settings from a file.
     */
    protected void readSettings() {

        if (DEBUG) {
            System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Reading_settings..."));
        }

        final JMConfig settings = JMConfig.getInstance();

        final String pathSep = java.io.File.separator;
        final String plugIns = settings.getJMString(anecho.JamochaMUD.JMConfig.USERPLUGINDIR);
        final String musicBoxDir = plugIns + pathSep + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("MusicBoxDir");
        final java.io.File info = new java.io.File(musicBoxDir + pathSep + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString(".musicbox.rc"));

        try {
            final java.io.FileInputStream inputFile = new java.io.FileInputStream(info);
            final java.io.ObjectInputStream serializeStream = new java.io.ObjectInputStream(inputFile);

            playFile = serializeStream.readObject().toString();
            dispLine = serializeStream.readObject().toString();
            useCTRL = serializeStream.readBoolean();
            useALT = serializeStream.readBoolean();
            useSHIFT = serializeStream.readBoolean();
            ctrlKey = serializeStream.readChar();

        } catch (Exception e) {
            if (DEBUG) {
                System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Error_during_serialization_") + e);
                System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("MusicBox_needs_to_know_how_to_create_a_new_file."));
            }
        }

        if (DEBUG) {
            System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Finished_reading_file."));
        }

    }
}
