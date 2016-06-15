/**
 * JMParseRC replaces the deprecated ParseINI file Reads in the settings for the
 * user's environment $Id: JMParseRC.java,v 1.18 2012/02/25 02:42:40 jeffnik Exp
 * $
 */
/* JamochaMUD, a Muck/Mud client program
 * Copyright (C) 1998-2015  Jeff Robinson
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

import java.awt.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;

import java.util.Vector;

import anecho.gui.OKBox;
import net.sf.wraplog.SystemLogger;
import net.sf.wraplog.AbstractLogger;
import net.sf.wraplog.NoneLogger;

public class JMParseRC {

    /**
     * Read .jamocha.rc and make an array of available MU*s, window positions,
     * etc.
     */
    private transient boolean stillLines;
    private transient RandomAccessFile file;
    private transient String temp1;
    private transient String temp2;
    private transient String temp3;
    private transient String temp4;

    /**
     * Enable or disables debug information
     */
    private static final boolean DEBUG = false;
    private transient String fileLines[];
    public transient JMConfig settings;

    final private transient Vector muckList = new Vector(0, 1);
    private final AbstractLogger logger;

    final private transient Vector muckName = new Vector(0, 1);
    final private transient Vector muckAddy = new Vector(0, 1);
    final private transient Vector muckPort = new Vector(0, 1);

    private transient Object splashGraphic = new Object();

    /**
     * Create a new JMParseRC instance for parsing settings files
     */
    public JMParseRC() {
        this(new Object());
    }

    /**
     * Create a new JMParseRC instance for parsing settings files. Any updates
     * while reading the .rc file will be displayed on the splash screen
     *
     * @param splash The splash screen to display information on
     */
    public JMParseRC(Object splash) {
        this.settings = JMConfig.getInstance();
        if (DEBUG) {
            logger = new SystemLogger();
        } else {
            logger = new NoneLogger();
        }

        // Initialise other variables
        // Open the file to allow subroutine to read
        temp1 = "";
        temp2 = "";
        temp3 = "";
        temp4 = "";

        // First, set 'stillLines' to True.
        // When 'stillLines' is false, end reading the file
        stillLines = true;
        final Vector tempLines = new Vector();

        if (splash != null) {
            splashGraphic = splash;
        }

        // Open the file to allow subroutine to read
        try {
            // file = new RandomAccessFile(settings.getJMString(JMConfig.USERDIRECTORY) + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString(".jamocha.rc"), java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("r"));
            String settingsFile = settings.getJMString(JMConfig.USERDIRECTORY) + ".jamocha.rc";
            // file = new RandomAccessFile(settings.getJMString(JMConfig.USERDIRECTORY) + ".jamocha.rc", "r");
            file = new RandomAccessFile(settingsFile, "r");
            fileLines = new String[0];

            logger.debug("JMParseRC: reading configuration file" + settingsFile);
            
            String str; // Temporary working String
            String readStr;

            while (stillLines) {
                // str = (file.readLine()).trim();
                readStr = file.readLine();

                // Check for relevant information
                if (readStr == null) {
                    // We've run out of lines to read
                    stillLines = false;
                    logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("NULL_in_.rc_file."));

                } else {
                    str = readStr.trim();
                    tempLines.addElement(str);
                    logger.debug("JMParseRC adding line: " + str);
                }
            }
        } catch (IOException e) {
            // No .jamocha.rc available, create a new one
            logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("JMParseRC_creating_new_.jamocha.rc_file."));
            // createRC(splash);
            createRC();
            // } catch (NullPointerException e) {
            // Run out of file to parse
        } catch (Exception e) {
            // This is the catchall exception, just to end JMParseRC
            logger.debug("JMParseRC Exception ", e);
            // should really work on a better mechanism to end this, huh?
            logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("JMParseRC_hit_an_error:_") + e);
        }

        // Set all the elements in our Settings class to the default
        // Any settings we find here will then override them
        // These are simple one-line configuration options
        final String[] options = {
            JMConfig.AUTOLOGGING,
            JMConfig.LOCALECHO,
            JMConfig.SYNCWINDOWS,
            JMConfig.USEUNICODE,
            JMConfig.TFKEYEMU,
            JMConfig.PROXY,
            JMConfig.DOUBLEBUFFER,
            JMConfig.SPLITVIEW,
            JMConfig.RELEASEPAUSE,
            JMConfig.ALTFOCUS,
            JMConfig.LOGFILENAMEFORMAT,
            JMConfig.LOGPATH,
            JMConfig.LOWCOLOUR,
            JMConfig.DIVIDERLOCATION,
            JMConfig.AUTOLOGPATH,
            JMConfig.LOGFILENAMEFORMAT,
            JMConfig.CUSTOMPALETTE + '0',    
            JMConfig.CUSTOMPALETTE + '1',
            JMConfig.CUSTOMPALETTE + '2',
            JMConfig.CUSTOMPALETTE + '3',
            JMConfig.CUSTOMPALETTE + '4',
            JMConfig.CUSTOMPALETTE + '5',
            JMConfig.CUSTOMPALETTE + '6',
            JMConfig.CUSTOMPALETTE + '7',
            JMConfig.CUSTOMPALETTE + '8',
            JMConfig.CUSTOMPALETTE + '9',
            JMConfig.CUSTOMPALETTE + "10",
            JMConfig.CUSTOMPALETTE + "11",
            JMConfig.CUSTOMPALETTE + "12",
            JMConfig.CUSTOMPALETTE + "13",
            JMConfig.CUSTOMPALETTE + "14",
            JMConfig.CUSTOMPALETTE + "15"
        };

        final int optLen = options.length;

        // Loop through all the lines of the file and separate it into its
        // proper "components"
        String tmpString;

        if (tempLines.isEmpty()) {
            // There are no lines in the file, so nothing to read in
            logger.debug("JMParseRC(Object) tempLines is empty.");
            return;
        } else {
            logger.debug("tempLines has " + tempLines.size() + " lines");
        }
        fileLines = new String[tempLines.size()];

        for (int i = 0; i < tempLines.size(); i++) {
            logger.debug("JMParseRC(Object) counting temp line: " + i);
            fileLines[i] = tempLines.elementAt(i).toString();
        }

        for (int i = 0; i < fileLines.length; i++) {

            try {

                tmpString = fileLines[i].toLowerCase();

                if (tmpString.equalsIgnoreCase(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("[worlds]"))) {
                    // if (fileLines[0].toLowerCase().equalsIgnoreCase(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("#_jamochamud_ini_file_version_1.0"))) {
                    if (fileLines[0].equalsIgnoreCase(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("#_jamochamud_ini_file_version_1.0"))) {
                        // Old style file... must be converted!!
                        logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Worlds_in_old_format,_converting..."));
                        convertMuckWorld(i);
                        // System.exit(0);
                    } else {
                        muckWorld(i);
                    }
                }

                if (tmpString.equalsIgnoreCase('[' + JMConfig.WORLD + ']')) {
                    muckWorld(i);
                }

                if (tmpString.equalsIgnoreCase('[' + JMConfig.FOREGROUNDCOLOUR + ']')) {
                    setColours(i, true);
                }

                if (tmpString.equalsIgnoreCase('[' + JMConfig.BACKGROUNDCOLOUR + ']')) {
                    setColours(i, false);
                }

                if (tmpString.equalsIgnoreCase('[' + JMConfig.MAINWINDOW + ']')) {
                    mainWindow(i, true);
                }

                if (tmpString.equalsIgnoreCase('[' + JMConfig.DATABAR + ']')) {
                    mainWindow(i, false);
                }

                if (tmpString.equalsIgnoreCase('[' + JMConfig.MACROFRAME + ']')) {
                    macroWindow(i);
                }

                if (tmpString.equalsIgnoreCase('[' + JMConfig.FONTFACE + ']')) {
                    fontFace(i);
                }

                /*
                 if (tmpString.equalsIgnoreCase("[" + JMConfig.MACROS + "]")) {
                 }
                 */
                if (tmpString.equalsIgnoreCase('[' + JMConfig.BROWSER1 + ']')) {
                    browser(i, true);
                }

                if (tmpString.equalsIgnoreCase('[' + JMConfig.BROWSER2 + ']')) {
                    browser(i, false);
                }

                if (tmpString.equalsIgnoreCase('[' + JMConfig.FTPCLIENT + ']')) {
                    fTPClient(i);
                }

                if (tmpString.equalsIgnoreCase('[' + JMConfig.EMAILCLIENT + ']')) {
                    eMailClient(i);
                }

                if (tmpString.equalsIgnoreCase('[' + JMConfig.TIMERS + ']')) {
                    jMTimers(i);
                }

                for (int optLoop = 0; optLoop < optLen; optLoop++) {
                    if (tmpString.equalsIgnoreCase('[' + options[optLoop] + ']')) {
                        logger.debug("JMParseRC: optLoop " + optLoop + " adding " + tmpString);
                        this.setConfigVariable(options[optLoop], i);
                    }
                }

                if (tmpString.equalsIgnoreCase('[' + JMConfig.PROXY + ']')) {
                    jMSetProxy(i);
                }

                if (tmpString.startsWith(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("/def"))) {
                    jMAddDefinition(i);
                }

                if (tmpString.startsWith(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("/set"))) {
                    jMAddVariable(i);
                }
                
                if (tmpString.equalsIgnoreCase('[' + JMConfig.COMMANDS_FILE + ']')) {
                    JMConfig.getInstance().setJMValue(JMConfig.COMMANDS_FILE, fileLines[i+1].trim());
                }
                
                if (tmpString.equalsIgnoreCase('[' + JMConfig.DICT_QUERY_URL + ']')) {
                    JMConfig.getInstance().setJMValue(JMConfig.DICT_QUERY_URL, fileLines[i+1].trim());
                }
                
                if (tmpString.equalsIgnoreCase('[' + JMConfig.PROMPT_STR + ']')) {
                    JMConfig.getInstance().setJMValue(JMConfig.PROMPT_STR, fileLines[i+1].trim());
                }

//                if (tmpString.startsWith('[' + JMConfig.CUSTOMPALETTE)) {
//                    logger.debug("JMParseRC: checking custom palette: " + tmpString);
//                    int numStart = JMConfig.CUSTOMPALETTE.length() + 1;
//                    int numEnd = tmpString.length() - 1;
//                    String palNum = tmpString.substring(numStart, numEnd);
//                    
//                    logger.debug("JMParseRC adding custom palette colour " + palNum);
//                }
            } catch (Exception le) {
                logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Loop_exception_") + le);
                le.printStackTrace();

            }

        }

        // Add the worlds to the hashtable if we have any
        // This will need to be updated for new method.  Fix Me XXX
        if (!muckList.isEmpty()) {
            // New since 2006-01-05
            settings.setJMValue(JMConfig.MUCKLIST, muckList);
        }

        // Do a sanity-check on colours
        Color tempColour1 = settings.getJMColor(JMConfig.FOREGROUNDCOLOUR);
        final Color tempColour2 = settings.getJMColor(JMConfig.BACKGROUNDCOLOUR);

        // Make certain our two colours are not the same!
        if (tempColour1.equals(tempColour2)) {
            int red, green, blue;
            red = tempColour1.getRed();
            blue = tempColour1.getBlue();
            green = tempColour1.getGreen();
            red = red + 128;
            if (red > 255) {
                red = red - 255;
            }
            green = green + 128;
            if (green > 255) {
                green = green - 255;
            }

            blue = blue + 128;
            if (blue > 255) {
                blue = blue - 255;
            }
            tempColour1 = new Color(red, green, blue);
            settings.setJMValue(JMConfig.FOREGROUNDCOLOUR, tempColour1);
        }

        // Get any Java2 specific settings
        if (settings.getJMboolean(JMConfig.USESWING)) {
            // Get the custom palette
            final java.util.prefs.Preferences prefs = java.util.prefs.Preferences.userNodeForPackage(this.getClass());

            // logger.debug("JMParseRC_getting_custom_palette_from_" + prefs);

            int tmpCol = -1;
            Color[] newPal = new Color[16];

            logger.debug("JMParseRC verifying custom palette.");

            try {
                for (int i = 0; i < 16; i++) {
                    tmpCol = settings.getJMint(JMConfig.CUSTOMPALETTE + i);
                    
                    logger.debug("JMParseRC palette column[" + i + "]: " + tmpCol);

                    if (tmpCol >= 0) {
                        newPal[i] = new Color(tmpCol);
                        logger.debug("Color " + i + " is " + newPal[i] + " (tmpCol " + tmpCol + ")");
                        logger.debug("Red: " + newPal[i].getRed());
                        logger.debug("Green: " + newPal[i].getGreen());
                        logger.debug("Blue: " + newPal[i].getBlue());
                    }
                }

                // settings.setJMObject(JMConfig.CUSTOMPALETTE, newPal);
                settings.setJMValue(JMConfig.CUSTOMPALETTE, newPal);
            } catch (Exception exc) {
                logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("JMParseRC_custom_error_getting_custom_palette") + exc);
            }

            // Check to see if we show the What's New dialogue
            checkWhatsNew(prefs);
        }

    }

    /**
     *
     * and if so, sets the variables to show the update information.
     *
     * @param prefs
     */
    private void checkWhatsNew(final java.util.prefs.Preferences prefs) {
        final String checkVer = prefs.get("LastVersion", "");
        final boolean showNew = prefs.getBoolean(JMConfig.SHOWNEW, true);
        boolean isNew = false;

        final String thisVersion = AboutBox.FULLVERNUM;

        settings.setJMboolean(JMConfig.SHOWNEW, String.valueOf(showNew));

        if (!thisVersion.equals(checkVer)) {
            // We are running a different version than before.
            isNew = true;
        }

        logger.debug("JMParseRC: checkWhatsNew:");
        logger.debug("Last version: " + checkVer);
        logger.debug("This version: " + thisVersion);
        logger.debug("ShowNew: " + showNew);

        logger.debug("JMParseRC: checkWhatsNew:");
        logger.debug("Last version: " + checkVer);
        logger.debug("This version: " + thisVersion);
        logger.debug("ShowNew: " + showNew);
        logger.debug("IsNew: " + isNew);
    }

    /**
     * Section to read line and parse out remarks and/or blank lines
     *
     * @return
     */
    public String readParse() {
        boolean readLoop = true;

        String str = "";

        while (readLoop) {
            try {
                // Read in the line from the file, and trim extra characters
                str = (file.readLine()).trim();

                // Check for relevant information
                if (str == null) {
                    // We've run out of lines to read
                    readLoop = false;
                    stillLines = false;
                }

                if (!"".equals(str) && str.charAt(0) != '#') {
                    readLoop = false; // This terminates the loop
                }
                // } catch (NullPointerException ex) {
                // // End of the file
                // readLoop = false;
                // stillLines = false;
                // // } catch (StringIndexOutOfBoundsException ex) {
                // // Hmmm...
            } catch (Exception otherExcp) {
                logger.debug("JMParseRC.readParse exception: " + otherExcp);
                readLoop = false;
                stillLines = false;
            }
        }

        return str;

    }

    /**
     *
     * Read in the names of the 'worlds'
     *
     */
    private void muckWorld(final int index) {
        // Parse the remaining 2 required muckWorld lines, checking for section flags
        muckName.addElement(fileLines[index + 1]);
        muckAddy.addElement(fileLines[index + 2]);
        muckPort.addElement(fileLines[index + 3]);

        final MuckInfo tempMU = new MuckInfo();
        tempMU.setName(fileLines[index + 1]);
        tempMU.setAddress(fileLines[index + 2]);

        try {
            final int tempPort = Integer.parseInt(fileLines[index + 3]);
            tempMU.setPort(tempPort);
            // Experimental check for SSL information
            final String tempBool = fileLines[index + 4];
            if (tempBool.equalsIgnoreCase(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("true"))) {
                tempMU.setSSL(true);
            } else {
                tempMU.setSSL(false);
            }

        } catch (NumberFormatException exc) {

            logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("JMParseRC_error_reading_port_for_") + fileLines[index + 1]);

        }
        // Insert tempMU into our running list
        muckList.addElement(tempMU);

    }

    private void convertMuckWorld(final int origIndex) {

        int index = origIndex;

        boolean stillWorlds = true;

        while (stillWorlds) {
            index++;

            if (fileLines[index].startsWith("[") || fileLines[index].startsWith(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("#"))) {
                stillWorlds = false;
            } else {

                if (!fileLines[index].trim().equals("")) {
                    muckName.addElement(fileLines[index]);
                    muckAddy.addElement(fileLines[index + 1]);
                    muckPort.addElement(fileLines[index + 2]);
                    index = index + 2;
                }

            }

        }
    }

    /**
     *
     * Set the foreground and background colours for JamochaMUD
     *
     */
    private void setColours(final int index, final boolean foreground) {

        // Set either the foreground or the background colours
        try {
            final int tempInt1 = Integer.parseInt(fileLines[index + 1]);
            final int tempInt2 = Integer.parseInt(fileLines[index + 2]);
            final int tempInt3 = Integer.parseInt(fileLines[index + 3]);

            final Color tColour = new Color(tempInt1, tempInt2, tempInt3);

            if (foreground) {
                // settings.setForegroundColour(tColour);
                settings.setJMValue(JMConfig.FOREGROUNDCOLOUR, tColour);
            } else {
                // settings.setBackgroundColour(tColour);
                settings.setJMValue(JMConfig.BACKGROUNDCOLOUR, tColour);
            }

        } catch (NumberFormatException e) {

            logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Your_colours_are_buggered.") + e);

        }

    }

    /**
     *
     * Set location of the Main Output window
     *
     * and Data Bar (input window) location and size
     *
     */
    private void mainWindow(final int index, final boolean mainWindow) {

        // Set the location of the main text window
        try {
            temp1 = fileLines[index + 1];
            temp2 = fileLines[index + 2];
            temp3 = fileLines[index + 3];
            temp4 = fileLines[index + 4];

            final Rectangle tempRect = new Rectangle(Integer.parseInt(temp1.trim()),
                    Integer.parseInt(temp2.trim()),
                    Integer.parseInt(temp3.trim()),
                    Integer.parseInt(temp4.trim()));

            // Check to see if any of of these values contain flags
            if (mainWindow) {
                // Set the variables for the databar's
                // initial position and size

                settings.setJMValue(JMConfig.MAINWINDOW, tempRect);

            } else {

                settings.setJMValue(JMConfig.DATABAR, tempRect);

            }

        } catch (NumberFormatException mwe) {

            logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Main_window_error") + mwe);

        }

    }

    /**
     *
     * Read in Macro Window's position and size
     *
     */
    private void macroWindow(final int index) {

        // Determine the size of the macro-window, and if visible.
        try {

            final Rectangle tempRect = new Rectangle(Integer.parseInt(fileLines[index + 1].trim()),
                    Integer.parseInt(fileLines[index + 2].trim()),
                    Integer.parseInt(fileLines[index + 3].trim()),
                    Integer.parseInt(fileLines[index + 4].trim()));

            settings.setJMValue(JMConfig.MACROFRAME, tempRect);

        } catch (NumberFormatException e) {

            logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Error_parsing_MacroWindow_from_.rc_file"));

        }

    }

    /**
     * Set the Font Face for both the input and output windows
     *
     * @param index
     */
    // public void fontFace(String temp1) {
    public void fontFace(final int index) {

        // Try and cast the second two variables into ints
        // temp2 and temp3 are font style and font size respectively
        try {
            final int tempInt2 = Integer.parseInt(fileLines[index + 2]);
            final int tempInt3 = Integer.parseInt(fileLines[index + 3]);

            // Transfer this information into the font
            // settings.setFontFace(new Font(fileLines[index + 1], tempInt2, tempInt3));
            settings.setJMValue(JMConfig.FONTFACE, new Font(fileLines[index + 1], tempInt2, tempInt3));

        } catch (NumberFormatException e) {

            // The font section is corrupt
            logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Font_section_corrupt_") + e);

            // This section will be expanded for multiple exception types
        }

    }

    /**
     * Read in the selected external browers/viewers to deal with special file
     * formats, etc.
     */
    private void browser(final int index, final boolean primary) {
        // Assign the browser
        // This section does not check to see if the location is valid or not
        String status = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("false");
        final String bLine = fileLines[index + 1];

        if (bLine.equals(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("empty"))) {
            temp1 = "";
        } else {
            // final int ast = bLine.indexOf(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("*"));
            final int ast = bLine.indexOf('*');

            if (ast >= 0) {
                temp1 = bLine.substring(0, ast);
                if (bLine.endsWith(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("true"))) {
                    // status = new String("true");
                    status = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("true");
                }
            } else {
                temp1 = "";
            }
        }

        if (primary) {
            settings.setJMValue(JMConfig.BROWSER1, temp1);
            settings.setJMValue(JMConfig.BROWSERINSTANCE1, status);
        } else {
            settings.setJMValue(JMConfig.BROWSER2, temp1);
            settings.setJMValue(JMConfig.BROWSERINSTANCE2, status);
        }
    }

    /**
     * Write the existing FTP client into the setting file in memory
     *
     * @param index
     */
    private void fTPClient(final int index) {
        // Set the FTP client location

        if (fileLines[index + 1].equals(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("empty"))) {

            settings.setJMValue(JMConfig.FTPCLIENT, fileLines[index + 1]);

        } else {

            settings.setJMValue(JMConfig.FTPCLIENT, fileLines[index + 1]);

        }

    }

    /**
     * Write the current e-mail client into the settings file in memory
     *
     * @param index
     */
    private void eMailClient(final int index) {
        // Set E-Mail Client location

        if (fileLines[index + 1].equals(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("empty"))) {
            // settings.setBrowser(JMConfig.EMAILCLIENT, "");
            settings.setJMValue(JMConfig.EMAILCLIENT, "");
        } else {
            // settings.setBrowser(JMConfig.EMAILCLIENT, fileLines[index + 1]);
            settings.setJMValue(JMConfig.EMAILCLIENT, fileLines[index + 1]);
        }
    }

    /**
     * Set the Timers key
     */
    /**
     * This was all wrong! We need the size of the timer window!!!
     */
    private void jMTimers(final int index) {

        try {

            // We will only get here if there is indeed a 'Timers' section
            settings.setJMValue(JMConfig.TIMERS, new Rectangle(Integer.parseInt(fileLines[index + 1]),
                    Integer.parseInt(fileLines[index + 2]),
                    Integer.parseInt(fileLines[index + 3]),
                    Integer.parseInt(fileLines[index + 4])));

            // settings.setTimersVisible(temp1.toLowerCase());
            settings.setJMValue(JMConfig.TIMERSVISIBLE, temp1.toLowerCase());

        } catch (NumberFormatException e) {

            logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Timer_exception_in_.rc_file."));

        }

    }

    /**
     * This is a reusable method to set configuration variables in memory
     *
     * @param name The name of the variable to set
     * @param the line index to start reading the variable from
     */
    private void setConfigVariable(final String name, final int index) {
        settings.setJMValue(name, fileLines[index + 1]);

        logger.debug("JMParseRC setting " + name + " to " + fileLines[index + 1]);

    }

    /**
     *
     * Read in and configure proxy support for JamochaMUD
     *
     */
    private void jMSetProxy(final int index) {

        // settings.setProxy(fileLines[index + 1].trim());
        settings.setJMValue(JMConfig.PROXY, fileLines[index + 1].trim());

        if (fileLines[index + 2].equals(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("null"))) {
            // settings.setProxy(false);
            settings.setJMValue(JMConfig.PROXY, false);
        } else {
            // settings.setProxyHost(fileLines[index + 2]);
            // settings.setProxyPort(fileLines[index + 3]);
            settings.setJMValue(JMConfig.PROXYHOST, fileLines[index + 2]);
            settings.setJMValue(JMConfig.PROXYPORT, fileLines[index + 3]);
        }

    }

    /**
     * Add a definition to the setting file in memory
     *
     * @param index
     */
    private void jMAddDefinition(final int index) {
        String temp, name, value;
        int eqSign;

        temp = (fileLines[index].substring(4)).trim();

        // eqSign = temp.indexOf(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("="));
        eqSign = temp.indexOf('=');
        name = temp.substring(0, eqSign).trim();
        value = temp.substring(eqSign + 1);

        settings.addDefinition(name, value);
    }

    /**
     * Add a variable to the settings file in memory
     *
     * @param index
     */
    private void jMAddVariable(final int index) {
        String temp, name, value;
        int eqSign;

        temp = (fileLines[index].substring(4)).trim();

        // eqSign = temp.indexOf(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("="));
        eqSign = temp.indexOf('=');
        name = temp.substring(0, eqSign).trim();
        value = temp.substring(eqSign + 1);

        settings.addVariable(name, value);
    }

    public void setSplashGraphic(Object splash) {
        if (splash != null) {
            splashGraphic = splash;
        }
    }

    /**
     * Section to create .jamocha.rc if one doesn't exist. Prompt user to make
     * sure this is okay, if not program will end (can't think of anything else
     * to do, otherwise, hee!)
     */
    // public void createRC(final Object splash) {
    public void createRC() {
        // Prompt user that a .jamocha.rc file will be created

        boolean createFile = false;
        boolean diskless = false;

        // This is a hack to give the dialogue a "frame" to float above
        final Frame workingFrame = new Frame();

        // final String message = anecho.gui.AbstractMessageFormat.wrap(bundle.langString("createSettingsFile"));
        final String message = anecho.gui.AbstractMessageFormat.wrap(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("createSettingsFile"), 50);

        if (settings.getJMboolean(JMConfig.QUIETRC)) {
            createFile = true;
        } else {

            Object[] options = {"Create file",
                "Don't save settings",
                "Quit JamochaMUD"};

            if (settings.getJMboolean(JMConfig.USESWING)) {
                // Check to see if the splash-screen is present. If so, we need to hide it temporarily!
                if (splashGraphic != null) {
                    ((anecho.gui.SplashScreen) splashGraphic).setVisible(false);
                }

                // final int confirm = javax.swing.JOptionPane.showConfirmDialog(workingFrame,
                // message,
                // java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("createSettingsFileTitle"),
                // javax.swing.JOptionPane.OK_CANCEL_OPTION,
                // javax.swing.JOptionPane.INFORMATION_MESSAGE);
                final int confirm = javax.swing.JOptionPane.showOptionDialog(workingFrame,
                        message,
                        java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("createSettingsFileTitle"),
                        javax.swing.JOptionPane.YES_NO_CANCEL_OPTION,
                        javax.swing.JOptionPane.INFORMATION_MESSAGE,
                        null,
                        options,
                        options[0]);

                if (confirm == javax.swing.JOptionPane.OK_OPTION) {
                    createFile = true;
                }

                if (confirm == javax.swing.JOptionPane.NO_OPTION) {
                    diskless = true;
                }

                // Return the splash screen if it was present before
                if (splashGraphic != null) {
                    ((anecho.gui.SplashScreen) splashGraphic).setVisible(true);
                }

            } else {
                final OKBox check = new OKBox(workingFrame, java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("createSettingsFileTitle"), true);
                // Add in some other settings here

                check.append(message);

                check.addButton(OKBox.OKAY);
                check.addButton(OKBox.CANCEL);

                check.setVisible(true);
                if (check.getResult().equals(OKBox.OKAY)) {
                    createFile = true;
                }
            }
        }

        if (diskless) {
            // Set JamochaMUD so that it doesn't use a .jamocha.rc file
            logger.debug("JMParseRC.createRC: settings JamochaMUD for diskless operation.");
            settings.setJMValue(JMConfig.DISKLESSCONFIG, diskless);
        } else {
            // if (check.getResult().equals(check.OKAY)) {
            if (createFile) {
                // File rcFile = new File(settings.getUserDirectory());
                final File rcFile = new File(settings.getJMString(JMConfig.USERDIRECTORY));

                // We'll use this opportunity to set a few defaults, just for the road!
                Rectangle tempRect = new Rectangle(20, 20, 500, 400);
                settings.setJMValue(JMConfig.MAINWINDOW, tempRect);
                tempRect = new Rectangle(20, 440, 500, 100);
                settings.setJMValue(JMConfig.DATABAR, tempRect);

                // Local Echo should be on by default (http://www.cryosphere.org/mud-protocol.html)
                settings.setJMValue(JMConfig.LOCALECHO, true);
                // Colours should start as white type on black background (some MU*'s make this assumption)
                settings.setJMValue(JMConfig.BACKGROUNDCOLOUR, new Color(0, 0, 0));
                settings.setJMValue(JMConfig.FOREGROUNDCOLOUR, new Color(255, 255, 255));

                logger.debug("JMParseRC - set standard foreground and background colours");

                // For convenience, we'll turn on the TinyFugue short-cut keys, too
                settings.setJMValue(JMConfig.TFKEYEMU, true);

                // Another nicety is having auto-focus to input enabled by default
                settings.setJMValue(JMConfig.AUTOFOCUSINPUT, true);
                // First, we see if the directory exists
                if (!rcFile.exists()) {
                    // Create a new user directory
                    logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Creating_directory(s):_") + rcFile);
                    rcFile.mkdirs();
                }
            } else {
                System.exit(0);
                // This may not be nice, but it appears to be a requirement to get JUnit tests
                // to complete. System.exit() causes JUnit to fail.
                throw new RuntimeException();
            }
        }
    }

    /**
     * Add a new Macros section to .jamocha.rc
     */
    public void newMacros() {

        String mLabel[] = new String[8];
        String mDef[] = new String[8];

        for (int i = 0; i < 8; i++) {
            mLabel[i] = (java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Macro") + (i + 1));
            mDef[i] = ("");
        }

        settings.setJMValue(JMConfig.MACROLABELS, mLabel);
        settings.setJMValue(JMConfig.MACRODEFS, mDef);

        // As of 12/09/98, this method has been altered to serialise
        // macros in the .macros.rc file, instead of placing them in
        // the basic .jamocha.rc file
        try {
            // FileOutputStream outputFile = new FileOutputStream(settings.getUserDirectory() + ".macros.rc");
            final FileOutputStream outputFile = new FileOutputStream(settings.getJMString(JMConfig.USERDIRECTORY) + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString(".macros.rc"));
            final ObjectOutputStream serializeStream = new ObjectOutputStream(outputFile);
            serializeStream.writeObject(mLabel);
            serializeStream.writeObject(mDef);
            serializeStream.flush();
        } catch (IOException e) {
            logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Macro_serialization_error_") + e);

        }
    }

    /**
     * Read in the serialised macros. This method, as of 98/12/09 replaces the
     * older method of storing macros in the .jamocha.rc file
     */
    public void readSerialisedMacros() {

        try {
            // FileInputStream inputFile = new FileInputStream(settings.getUserDirectory() + ".macros.rc");
            final FileInputStream inputFile = new FileInputStream(settings.getJMString(JMConfig.USERDIRECTORY) + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString(".macros.rc"));
            final ObjectInputStream serializeStream = new ObjectInputStream(inputFile);

            // settings.setMacroLabels((String[]) serializeStream.readObject());
            // settings.setMacroDefs((String[]) serializeStream.readObject());
            settings.setJMValue(JMConfig.MACROLABELS, (String[]) serializeStream.readObject());
            settings.setJMValue(JMConfig.MACRODEFS, (String[]) serializeStream.readObject());

        } catch (FileNotFoundException e) {
            // The file does not exist, but will be written later
            // This is not a bad thing
            // We'll create the info needed now:
            newMacros();
        } catch (IOException | ClassNotFoundException e) {
            logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Error_during_serialization_") + e);
            // The file probably does not exist
            // We'll create the info needed now:
            newMacros();
        }

    }
}
