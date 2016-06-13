/**
 * JMConfig.java, an object to keep and share JamochaMUD settings between
 * classes $Id: JMConfig.java,v 1.25 2015/06/11 01:55:57 jeffnik Exp $ /*
 * JamochaMUD, a Muck/Mud client program Copyright (C) 1998-2014 Jeff Robinson
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License Copyright (C) 1998-2014 Jeff
 * Robinson
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */
package anecho.JamochaMUD;

import java.awt.*;
import java.util.Hashtable;
import java.util.Vector;
import net.sf.wraplog.AbstractLogger;
import net.sf.wraplog.NoneLogger;
import net.sf.wraplog.SystemLogger;

/**
 * This class is used for easy access to user variables and settings
 *
 * @author jeffnik
 */
final public class JMConfig {

    /**
     * This hashtable will contain all the values we should need
     */
    private transient Hashtable vars;
    /**
     * variables defined by the user
     */
    final private transient Hashtable userVariables;
    /**
     * Definitions defined by the user
     */
    final private transient Hashtable userDefs;
    /**
     * Enables and disables debugging output
     */
    private static final boolean DEBUG = false;

    private final AbstractLogger logger;

    public static final String AUTOFOCUSINPUT = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("AutoFocusInput");
    public static final String AUTOLOGGING = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("AutomaticLogging");
    public static final String ALTFOCUS = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("AltFocus");
    public static final String ANTIALIAS = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("AntiAliasing");
    public static final String BACKGROUNDCOLOUR = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("BGColour");
    public static final String BGPAINT = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("BackgroundPaint");
    public static final String BROWSER1 = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Browser1");
    public static final String BROWSERINSTANCE1 = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("BrowserInstance1");
    public static final String BROWSER2 = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Browser2");
    public static final String BROWSERINSTANCE2 = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("BrowserInstance2");
    public static final String CONNECTIONHANDLER = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("ConnectionHandler");
    public static final String CONNMUCK = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("ConnMuck");
    public static final String CONNPORT = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("ConnPort");
    public static final String CONNSSL = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("ConnSSL");
    public static final String CUSTOMPALETTE = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("CustomPalette");
    public static final String DATABAR = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("DataBar");
    public static final String DATAINVARIABLE = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("DataInVariable");
    public static final String DIVIDERLOCATION = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("DividerLocation");
    public static final String DOIMPORT = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("DoImport");
    public static final String DOUBLEBUFFER = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("DoubleBuffer");
    public static final String HISTORYLENGTH = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("HistoryLength");
    public static final String EMAILCLIENT = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("EMailClient");
    public static final String ENUMERATOR = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Enumerator");
    /**
     * The address to be used for loading external site mudlists
     */
    public static final String EXTMUDLIST = "ExternalMUDList";
    /**
     * The remote image to load when JamochaMUD is co-branded
     */
    public static final String EXTMUDIMAGE = "ExternalMUDImage";
    /**
     * Allows the user to define the MUD XML file type based on existing types
     */
    public static final String MUDLISTTYPE = "MUDListType";
    /**
     * This variable allows the user to manually pass the tags used to parse a
     * MUD XML file
     */
    public static final String MANUALEXTLIST = "ManualExtListTags";
    public static final String FONTFACE = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("FontFace");
    public static final String FOREGROUNDCOLOUR = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("FGColour");
    public static final String FTPCLIENT = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("FTPClient");
    public static final String ICONIMAGE = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("IconImage");
    /**
     * Identifies whether the current version of JamochaMUD is newer than the
     * previously run version
     */
    public static final String ISNEW = "IsNew";
    public static final String JMUDCORE = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("JamochaMUDCore");
    public static final String LASTMU = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("LastConnectedMU");
    /**
     * The path to where dumped logfiles are to be saved (not to be confused
     * with automatic log files)
     */
    public static final String LOGPATH = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("LogPath");
    /**
     * The path to where automatic logfiles are to be saved (not to be confused
     * with dumped log files)
     */
    public static final String AUTOLOGPATH = "AutoLogPath";
    /**
     * The format used for the continuous logging logfile
     */
    public static final String LOGFILENAMEFORMAT = "LogFileNameFormat";
    public static final String LOCALECHO = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("LocalEcho");
    public static final String LOWCOLOUR = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("LowColour");
    public static final String MACROFRAME = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("MacroFrame");
    public static final String MACRODEFS = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("MacroDefs");
    public static final String MACROLABELS = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("MacroLabels");
    public static final String MACROVISIBLE = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("MacroVisible");
    public static final String MAINLAYOUTVALID = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("MainLayoutValid");
    public static final String MAINWINDOW = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("MainWindow");
    public static final String MAINWINDOWICONIFIED = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("MainWindowIconified");
    public static final String MAINWINDOWVARIABLE = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("MainWindowVariable");
    public static final String MASTERPLUGINDIR = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("MasterPlugInDirectory");
    public static final String MUCKCONNVISIBLE = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("MuckConnVisible");
    public static final String MUCKLIST = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("MuckList");
    public static final String MUCKMAINTITLE = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("MuckMainTitle");
    /**
     * Whether to use the MudConnector.com MU* list
     */
    public static final String MUDCONNECTORLIST = "MudConnectorList";
    public static final String OSNAME = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("OSName");
    public static final String PATHSEPARATOR = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("PathSeparator");
    public static final String PLUGENUMERATOR = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("PluginEnumerator");
    public static final String PLUGINNAME = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("PlugInName");
    public static final String PLUGINSTATUS = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("PlugInStatus");
    public static final String DISKLESSCONFIG = "DisklessConfig";
    public static final String PROXY = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("ProxyEnabled");
    public static final String PROXYHOST = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("ProxyHost");
    public static final String PROXYPORT = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("ProxyPort");
    public static final String QUIETRC = "QuietRC";
    public static final String RELEASEPAUSE = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("ReleasePause");
    public static final String SCREENSIZE = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("ScreenSize");
    /**
     * Identifies whether the user wants to show JamochaMUD update information
     * or not
     */
    public static final String SHOWNEW = "ShowNew";
    public static final String SINGLEUSERMODE = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Single_User_Mode");
    public static final String SPLITVIEW = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("SplitView");
    public static final String SYNCWINDOWS = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("SyncWindows");
    public static final String SPELLCHECK = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("SpellCheck");
    // public static final String TEXTPANEL = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("TextPanel");
    public static final String TFKEYEMU = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("TFKeyEmu");
    public static final String TIMERS = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Timers");
    public static final String TIMERSVISIBLE = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("TimersVisible");
    public static final String USESWING = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("UseSwing");
    public static final String USESWINGENTRY = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("UseSwingEntry");
    public static final String USEUNICODE = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Unicode");
    public static final String USERDIRECTORY = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("UserDirectory");
    /**
     * The directory where plug-ins should be installed by the JamochaMUD
     * plug-in installer. It is also the base dir for all plug-in settings
     */
    public static final String USERPLUGINDIR = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("UserPlugInDir");
    public static final String WORKINGDIRECTORY = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("WorkingDirectory");
    public static final String WORLD = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("World");
    public static final String BUNDLEBASE = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("anecho.JamochaMUD.JamochaMUDBundle");

    public static final String COMMANDS_FILE = "CommandsFile";
    
    /**
     * Our constructor is empty, and we like it that way! Well, actually, all
     * our variables should have some base value straight &quot;out of the box".
     * There would be far too many to do in the constructor... without falling
     * asleep.
     *
     * Individual "get" statements will return a default value if one does not
     * exist. This should avoid problems with a damaged .jamocha.rc, missing
     * information when writing out .jamocha.rc or like scenarios.
     */
    private static JMConfig _instance;

    private JMConfig() {
        vars = new Hashtable(101, .75f);
        userVariables = new Hashtable(5, .75f);
        userDefs = new Hashtable(5, .75f);

        if (DEBUG) {
            logger = new SystemLogger();
        } else {
            // retCode = vars.containsKey(key);
            logger = new NoneLogger();
        }
    }

    /**
     * @return
     */
    public static JMConfig getInstance() {

        if (_instance == null) {
            _instance = new JMConfig();
        }

        return _instance;
    }

    /**
     * Check our hashtable for the existance of key &quot;String"
     */
    private boolean checkKey(final String key) {

        logger.debug("JMConfig.checkKey looking for value " + key);
        logger.debug("JMConfig.checkKey has vars hashtable as " + vars);
        boolean retCode;

        if (key != null && !key.isEmpty()) {
            retCode = vars.containsKey(key);
        } else {
            retCode = false;
        }
        return retCode;

    }

    /**
     * Convert a string value to a boolean This method looks awful. Fix Me XXX
     */
    private boolean stringToboolean(final String key) {

        final String workString = (String) vars.get(key);

        boolean retCode;

        // if (workString.toLowerCase().equals(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("true"))) {
        if (workString.equalsIgnoreCase(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("true"))) {
            retCode = true;
        } else {
            retCode = false;
        }

        return retCode;

    }

    /**
     * Set a value to our configuration containing a boolean value
     *
     * @param objName A string defining the setting's name
     * @param setting The boolean value to be attributed to our
     * <CODE>objName</CODE>
     */
    public synchronized void setJMValue(final String objName, final boolean setting) {

        // String value = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("false");
        String value;

        if (setting) {
            value = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("true");
        } else {
            value = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("false");
        }

        logger.debug("JMConfig.setJMValue(" + objName + ", " + value);
        logger.debug("JMConfig.setJMValue vars is set to: " + vars);

        // vars.put(objName, value);
        hashPut(objName, value);

    }

    /**
     * This method sets an Integer value to a given key stored in the user
     * variables
     *
     * @param objName The key to use
     * @param setting The Integer to be stored with the given key
     */
    public synchronized void setJMValue(final String objName, final int setting) {

        // vars.put(objName, setting + "");
        vars.put(objName, Integer.toString(setting));

    }

    /**
     * This method sets an Object to a given key stored in the user variables
     *
     * @param objName The key to use
     * @param value The object to be stored with the given key
     */
    public synchronized void setJMValue(final String objName, final Object value) {
        vars.put(objName, value);
    }

    /**
     * This method sets a String to a given key stored in the user variables
     *
     * @param objName The key to use
     * @param setting The String to be stored with the given key
     * @deprecated <this method mirrors setJMValue which should be used instead>
     */
    public void setJMboolean(final String objName, final String setting) {

        // String value = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("false");
        String value;

        // if (setting.toLowerCase().equals(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("true"))) {
        if (setting.equalsIgnoreCase(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("true"))) {
            value = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("true");
        } else {
            value = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("false");
        }

        vars.put(objName, value);
    }

    /**
     * Sets an key and value pair in the vars Hashtable
     * @param objName
     * @param value The Object to be stored with the given key
     * @deprecated <this method mirrors setJMValue which should be used instead>
     */
    public synchronized void setJMObject(final String objName, final Object value) {
        logger.debug("JMConfig.setJMObject received: " + objName + " with Object: " + value);
        vars.put(objName, value);
    }

    /**
     *
     * @param item
     * @return
     */
    public synchronized Object getJMObject(final String item) {

        Object retVal = (Object) null;

        if (checkKey(item)) {
            retVal = (Object) vars.get(item);
        }

        return retVal;

    }

    /**
     * Returns the Frame object for the specified variable
     *
     * @param item The frame object to be returned
     * @return 
     */
    public synchronized Frame getJMFrame(final String item) {

        // Frame retVal = new Frame();
        Frame retVal;

        if (checkKey(item)) {
            retVal = (Frame) vars.get(item);
        } else {
            retVal = new Frame();
        }

        return retVal;

    }

    /**
     *
     *
     *
     *
     * @return
     *
     */
    public synchronized Rectangle getJMRectangle(final String item) {

        // Rectangle retVal = new Rectangle(0, 0, 0, 0);
        Rectangle retVal;

        if (checkKey(item)) {
            retVal = (Rectangle) vars.get(item);
        } else {
            retVal = new Rectangle(0, 0, 0, 0);
        }

        return retVal;

    }

    /**
     * Returns a String from the JamochaMUD variables
     *
     * @param item
     * @return
     */
    public synchronized String getJMString(final String item) {

        // String retVal = "";
        String retVal;

        if (checkKey(item)) {
            retVal = (String) vars.get(item);
        } else {
            retVal = "";
        }

        return retVal;

    }

    /**
     * Returns a string array based on the provided item.
     * If the @item is not in the configuration an empty array will be returned.
     *
     * @param item
     * @return
     */
    public synchronized String[] getJMStringArray(final String item) {

        // String[] retVal = new String[0];
        String[] retVal;

        if (checkKey(item)) {
            retVal = (String[]) vars.get(item);
        } else {
            retVal = new String[0];
        }

        return retVal;
    }

    /**
     * Returns a boolean from the variable hashtable
     *
     * @param item
     * @return
     */
    public synchronized boolean getJMboolean(final String item) {

        // boolean retVal = false;
        boolean retVal;

        if (checkKey(item)) {
            retVal = stringToboolean(item);
        } else {
            retVal = false;
        }

        return retVal;

    }

    /**
     * Returns a colour from the hashtable
     *
     * @param item
     * @return
     */
    public synchronized Color getJMColor(final String item) {

        // Color retVal = new Color(0, 0, 0);
        Color retVal;

        if (checkKey(item)) {
            retVal = (Color) vars.get(item);
        } else {
            retVal = new Color(0, 0, 0);
        }

        return retVal;

    }

    /**
     * Returns an integer from the JamochaMUD variable hashtable
     *
     * @param item The key to use to look up the Integer
     * @return An integer represented by the given key. Keys that don't exist
     * returns -1
     */
    public synchronized int getJMint(final String item) {

        int retVal = -1;

        try {

            if (checkKey(item)) {
                retVal = Integer.parseInt((String) vars.get(item));
            }

        } catch (NumberFormatException exc) {

            logger.debug("JMConfig.getJMint error:", exc);

        } catch (NullPointerException npe) {

            logger.debug("JMConfig.getJMint null pointer exception:", npe);

        }

        return retVal;

    }

    /**
     * This method returns the Font represented by the given key
     *
     * @param item The key to use to look up the font
     * @return The font represented by the objName key
     */
    public synchronized Font getJMFont(final String item) {

        // Font retVal = new Font(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Dialog"), Font.PLAIN, 12);
        // Font retVal = new Font("Monospaced", Font.PLAIN, 12);
        Font retVal;

        if (checkKey(item)) {
            try {
                retVal = (Font) vars.get(item);
            } catch (Exception exc) {
                logger.debug("JMConfig.getJMFont has exception", exc);
                retVal = new Font(Font.MONOSPACED, Font.PLAIN, 12);
            }
        } else {
            retVal = new Font(Font.MONOSPACED, Font.PLAIN, 12);
        }

        return retVal;

    }

    /**
     * Returns a vector from the JamochaMUD configuration specified by the 
     * @item.  If the item is not in the configuration an empty Vector is returned.
     * in the configuration an empty Vector will be returned.
     * @param item The item to look up
     * @return 
     */
    public synchronized Vector getJMVector(final String item) {

        // Vector retVal = new Vector(); 
        Vector retVal;

        if (checkKey(item)) {
            retVal = (Vector) vars.get(item);
        } else {
            retVal = new Vector();
        }

        return retVal;

    }

    /* Specialised methods that are a little more tricky to deal with
     */
    /**
     *
     * @param icon
     */
    public synchronized void setIconImage(final Image icon) {
        vars.put(ICONIMAGE, icon);
    }

    /**
     *
     *
     *
     * @return
     *
     */
    public synchronized Image getIconImage() {

        return (Image) getJMObject(ICONIMAGE);

    }

    /**
     *
     *
     *
     * @param screen
     *
     */
    public synchronized void setScreenSize(final Dimension screen) {
        hashPut(SCREENSIZE, screen);
        // vars.put(SCREENSIZE, screen);

    }

    /**
     *
     *
     *
     * @return
     *
     */
    public synchronized Dimension getScreenSize() {

        return (Dimension) getJMObject(SCREENSIZE);

    }

    /**
     *
     * @param connection
     * @deprecated the ConnectionHandler will be turned into a singleton
     */
    public synchronized void setConnectionHandler(final CHandler connection) {

        if (DEBUG) {

            System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Connection_Handler_set."));

        }

        vars.put(CONNECTIONHANDLER, connection);

    }

    /**
     *
     * @return @deprecated The CHandler class will be turned into a singleton.
     * Use CHandler.getInstance() instead.
     */
    public synchronized CHandler getConnectionHandler() {

        // To make this compatible with the singleton we will now return the
        // actual instance instead of our stored variable
        // return (CHandler)(getJMObject(CONNECTIONHANDLER));
        return CHandler.getInstance();

    }

    /**
     *
     *
     *
     * @param core
     *
     */
    public synchronized void setJMCore(final JMUD core) {

        vars.put(JMUDCORE, core);

    }

    /**
     *
     *
     *
     * @return
     *
     */
    public synchronized JMUD getJMCore() {

        return (JMUD) getJMObject(JMUDCORE);

    }

    /**
     *
     *
     *
     * @param variable
     *
     */
    public synchronized void setDataInVariable(final DataIn variable) {

        vars.put(DATAINVARIABLE, variable);

    }

    /**
     *
     *
     *
     * @return
     *
     */
    public synchronized DataIn getDataInVariable() {

        return (DataIn) getJMObject(DATAINVARIABLE);

    }

    /**
     *
     * @deprecated MuckMain is now a singleton, information may be retrieved via
     * MuckMain.getInstance()
     *
     * @param variable
     *
     */
    public synchronized void setMainWindowVariable(final MuckMain variable) {

        vars.put(MAINWINDOWVARIABLE, variable);

    }

    /**
     *
     * @deprecated MuckMain is now a singleton, information may be retrieved via
     * MuckMain.getInstance()
     *
     * @return
     *
     */
    public synchronized MuckMain getMainWindowVariable() {

        return (MuckMain) getJMObject(MAINWINDOWVARIABLE);

    }

    /**
     *
     *
     *
     * @param plugEnum
     *
     */
    public synchronized void setPlugEnumerator(final EnumPlugIns plugEnum) {

        vars.put(PLUGENUMERATOR, plugEnum);

    }

    /**
     *
     * This returns the base path to our language resource bundles
     *
     * @return *
     */
    public synchronized String getBundleBase() {

        return java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("anecho.JamochaMUD.JamochaMUDBundle");

    }

    /**
     *
     * Add a new user-variable or change the definition of an existing one
     *
     * @param name
     *
     * @param value *
     */
    public synchronized void addVariable(final String name, final String value) {

        if (userVariables.containsKey(name)) {

            userVariables.remove(name);

        }

        userVariables.put(name, value);

    }

    /**
     *
     * Get the value of the given user-variable name
     *
     * @param name
     *
     * @return *
     */
    public synchronized String getVariable(final String name) {

        String retVal;
        // String retVal = (String)null;

        if (userVariables.containsKey(name)) {
            retVal = (String) userVariables.get(name);
        } else {
            retVal = (String) null;
        }

        return retVal;

    }

    /**
     * Remove the selected variable from the list of user-variables
     *
     * @param name The name of the user defined variable to remove
     * @return <code>true</code>The variable was successfully unset
     * <code>false</code> The variable does not exist
     */
    public synchronized boolean removeVariable(final String name) {

        boolean retVal;

        if (userVariables.containsKey(name)) {

            userVariables.remove(name);
            retVal = true;

        } else {
            retVal = false;
        }

        return retVal;

    }

    /**
     *
     *
     *
     * @return
     *
     */
    public synchronized Hashtable getAllVariables() {

        return userVariables;

    }

    /**
     * Store a user-defined definition
     *
     *
     * @param name the name of the definition to be stored
     *
     * @param value the value of the definition to be stored
     *
     */
    public synchronized void addDefinition(final String name, final String value) {

        if (userDefs.containsKey(name)) {
            userDefs.remove(name);
        }

        userDefs.put(name, value);

    }

    /**
     *
     * @param name
     * @return
     */
    public synchronized String getDefinition(final String name) {

        String retVal = (String) null;

        if (userDefs.containsKey(name)) {
            retVal = (String) userDefs.get(name);
        }

        return retVal;

    }

    /**
     *
     * Remove the given definition from the Hashtable
     * @param name
     *
     * @return
     *
     */
    public synchronized boolean removeDefinition(final String name) {

        boolean retVal;

        if (userDefs.containsKey(name)) {

            userDefs.remove(name);
            retVal = true;

        } else {
            retVal = false;
        }

        return retVal;

    }

    /**
     * This method returns a hashtable defining all the user-defined definitions
     *
     * @return All user-defined definitions
     */
    public synchronized Hashtable getAllDefinitions() {
        return userDefs;
    }

    /**
     * This method is most commonly called when one wants to back-up the current
     * JamochaMUD settings. This will return the hashtable with all the standard
     * settings, excluding the definitions and variables
     *
     * @return Returns a complete hashtable of all JamochaMUD settings
     */
    public synchronized Hashtable getAllSettings() {
        return vars;
    }

    /**
     * Change all our settings to that of the new Hashtable. This should be done
     * somewhere between the user termination of JamochaMUD and the closing of
     * the JVM. We don't want old settings to over-write what has just been
     * imported during shut-down.
     *
     * @param newVars A hashtable to replace all existing user variables
     */
    // public synchronized void setAllSettings(final Hashtable newVars) {
    public void setAllSettings(final Hashtable newVars) {
        vars = newVars;
    }

    /**
     * Put the new value into our settings Hashtable. If the value passed for
     * the key is null then we'll erase the existing value.
     *
     * @param name
     * @param value
     */
    private void hashPut(String name, Object value) {
        if (value == null) {
            // putting "null" into the hashtable just seems like bad business.
            // we'll use this as a hint to just remove the key instead
            if (vars.containsKey(name)) {
                logger.debug("JMConfig.hasPut removing " + name + " from Hashtable.");
                boolean result = removeDefinition(name);
                logger.debug("JMConfig.hashPut result: " + result);
            }
        } else {
            // If the name is null then obviously we don't have a key and
            // can't anything into the Hashtable
            if (name != null) {
                logger.debug("JMConfig.hashPut adding key: " + name + " with value: " + value);

                Object put;
                
                if (vars == null) {
                    logger.debug("JMConfig.hashPut has a null hashtable.");
                }
                put = vars.put(name, value);
            }
        }
    }
}
