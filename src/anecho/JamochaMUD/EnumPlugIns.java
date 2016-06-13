/**
 * EnumPlugIns.java enumerates plugins at program start-up, and contains
 * functions for managing them once the program is loaded $Id:
 * EnumPlugIns.java,v 1.20 2012/03/11 14:57:19 jeffnik Exp $
 *
 * JamochaMUD, a Muck/Mud client program Copyright (C) 1998-2015 Jeff Robinson
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version. *
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;

import java.lang.reflect.*;

import java.util.Vector;

import anecho.JamochaMUD.plugins.PlugInterface;
import java.io.IOException;
import net.sf.wraplog.AbstractLogger;
import net.sf.wraplog.NoneLogger;
import net.sf.wraplog.SystemLogger;

/**
 * EnumPlugIns.java enumerates plugins at program start-up, and contains
 * functions for managing them once the program is loaded
 *
 * @version $Id: EnumPlugIns.java,v 1.23 2015/08/30 22:43:32 jeffnik Exp $
 * @author Jeff Robinson
 */
public class EnumPlugIns {

    /**
     * The path separator used by this operating system
     */
    private transient String pathSeparator;
    /**
     * A vector containing all the plug-in classes
     */
    public static Vector plugInClass = new Vector(0, 1);
    /**
     * A vector containing all the names of the plug-ins.
     */
    public static Vector plugInName = new Vector(0, 1);
    /**
     * A Vector containing the type of all the plug-ins.
     */
    public static Vector plugInType = new Vector(0, 1);
    /**
     * A vector containing the status of all the plug-ins.
     */
    public static Vector plugInStatus = new Vector(0, 1);
    /**
     * Enable and disables debugging output
     */
    private static final boolean DEBUG = false;
    /**
     * The global JamochaMUD settings information
     */
    private final transient JMConfig settings;
    private static final String PLUGINSTR = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("plugins");
    /**
     * The instance of this class for use as a singleton
     */
    private static EnumPlugIns _EnumInstance;
    private static final String LASTINTERFACE = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("isActive"); // last interface added to PlugInterface
    /**
     * Output type of plug-in, used for identifying type of commands this
     * plug-in watches
     */
    public static final String INPUT = "input";
    /**
     * Output type of plug-in, used for identifying type of commands this
     * plug-in watches
     */
    public static final String OUTPUT = "output";
    private final AbstractLogger logger;

    /**
     * This is the startup class to enumerate all the plugins found in the
     * plug-in directory
     */
    private EnumPlugIns() {
        settings = JMConfig.getInstance();

        if (DEBUG) {
            logger = new SystemLogger();
        } else {
            logger = new NoneLogger();
        }

    }

    /**
     * Get the instance of our singleton.
     *
     * @return Returns the singleton instance of our EnumPlugins class
     */
//    public static EnumPlugIns getInstance(final JMConfig mainSettings) {
    public static EnumPlugIns getInstance() {
        if (_EnumInstance == null) {
//            _EnumInstance = new EnumPlugIns(mainSettings);
            _EnumInstance = new EnumPlugIns();
        }

        return _EnumInstance;
    }

    /**
     * Load the JamochaMUD plug-ins. If a splash-screen component is used the
     * splash-screen will display the names of the plug-ins as they are loaded.
     *
     * @param splash The anecho.gui.SplashScreen object (if used).
     */
    public void loadPlugIns(final Object splash) {
        boolean isJar = false;
        plugInClass.removeAllElements();
        plugInName.removeAllElements();
        plugInType.removeAllElements();
        plugInStatus.removeAllElements();
        // temp.removeAllElements();

        pathSeparator = settings.getJMString(JMConfig.PATHSEPARATOR);

        // Now set the plug-ins directory
        final String userDir = settings.getJMString(JMConfig.USERDIRECTORY);
        final File plugIns = new File(userDir + PLUGINSTR);

        // check and see if there is a 'plugins' directory
        if (!plugIns.exists()) {
            // There is no plugins directory, so we'll create one
            plugIns.mkdir();
        }

        // Now, let's reset the menu on the main window
        // final MuckMain tempMain = settings.getMainWindowVariable();
        final MuckMain tempMain = MuckMain.getInstance();
        tempMain.removeAllPlugins();

        // Get a list of the files in that directory
        // discarding those that aren't plug-ins.
        String fileList[];

        // If we are set for a multi-user environment, we'll also need to
        // read the list of plug-ins from the "master" plug-ins directory
        if (!settings.getJMboolean(JMConfig.SINGLEUSERMODE)) {
            String workingDirectory = settings.getJMString(JMConfig.WORKINGDIRECTORY);

            // This is really really fugly!  By throwing an intentional error here
            // we can get the real path that we were looking for!
            try {
                final Class testC = this.getClass();
                /* This line is an ugly kludge made to intentionally throw an error
                 * which will allow some systems to correctly identify JamochaMUD's own
                 * byte-code directory!! */
                // final java.io.InputStream tempStream = testC.getResourceAsStream(PLUGINSTR);
                testC.getResourceAsStream(PLUGINSTR);

                /* End of kludge-line */
                // If the first command doesn't throw an exception then we can try the following:
                final java.net.URL tempURL = testC.getResource(PLUGINSTR);
                workingDirectory = tempURL.getFile();

                // We may have to do some clean-up on this to change things back into a file
                // path instead of a URL.  We'll do this "scientifically" by checking to see if
                // the third character is a : (colon) and if the first character is a slash that
                // doesn't match our default separator
                if (workingDirectory.charAt(2) == ':' && !workingDirectory.startsWith(pathSeparator)) {

                    workingDirectory = workingDirectory.substring(1);

                    if (DEBUG) {
                        System.err.println("EnumPlugins workingDirectory has : at position 2.");
                        System.err.println("Changing workingDirectory to: " + workingDirectory);
                    }
                }

                // Now trim out any characters converted to web-safe things like %20
                int textIndex, end; // size, begin;

                while (workingDirectory.contains("%20")) {
                    textIndex = workingDirectory.indexOf(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("%20"));
                    end = workingDirectory.length();

                    if (textIndex == 0) {
                        workingDirectory = workingDirectory.substring(3);
                        if (DEBUG) {
                            System.err.println("Found %20, changing workingDirectory to: " + workingDirectory);
                        }
                    } else {
                        if (textIndex == (end - 3)) {
                            workingDirectory = workingDirectory.substring(0, end - 3);
                            if (DEBUG) {
                                System.err.println("Found %20 and end-3, returning workDirectory of " + workingDirectory);
                            }
                        } else {
                            workingDirectory = workingDirectory.substring(0, textIndex) + " " + workingDirectory.substring(textIndex + 3, end);
                            if (DEBUG) {
                                System.err.println("Replacing %20 with space and returning " + workingDirectory);
                            }
                        }
                    }

                }

                final String className = this.getClass().getName().replace('.', '/');
                final String classJar = this.getClass().getResource(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("/") + className + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString(".class")).toString();

                if (classJar.startsWith(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("jar:"))) {
                    if (DEBUG) {
                        System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("***_running_from_jar!"));
                    }

                    isJar = true;

                } else {

                    logger.debug("EnumPlugIns... Not running from a jarfile.");

                }

                logger.debug("Raw: " + this.getClass().getName());
                logger.debug("ClassJar: " + classJar);

                // This method seems to break.  Fix Me XXX
                logger.debug("UM: Master plug-in directory: " + workingDirectory);

            } catch (Exception exc) {

                workingDirectory = exc.getMessage();

                workingDirectory.trim();

                logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("EM:_Master_plug-in_directory:_") + workingDirectory);

            }

            settings.setJMValue(JMConfig.MASTERPLUGINDIR, workingDirectory);

            final File mainPlugs = new File(workingDirectory);

            String tempList[], oldList[];
            tempList = mainPlugs.list();
            oldList = plugIns.list();

            if (DEBUG) {

                System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("EnumPlugins.LoadPlugIns()_mainPlugs:_"));

                if (tempList != null) {
                    for (int i = 0; i < tempList.length; i++) {

                        System.err.println(tempList[i] + "");

                    }
                } else {
                    System.err.println("EnumPlugIns templist is null.");
                }

                System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("EnumPlugins.LoadPlugIns()_userPlugs:_"));

                if (DEBUG) {
                    if (oldList != null) {
                        for (int i = 0; i < oldList.length; i++) {

                            System.err.println(oldList[i] + "");
                        }
                    } else {
                        System.err.println("EnumPlugIns.loadPlugIns does not have an old list.  It is null.");

                    }
                }

            }

            // Copy the fileList into the oldList;
            int first = 0;

            if (oldList != null) {
                first = oldList.length;
            }

            int second, third;

            if (tempList == null) {

                second = 0;

            } else {

                second = tempList.length;

                // We want to do a quick comparison of the first list to the second
                // and cancel out any duplicates from the second list
                for (int ul = 0; ul < second; ul++) {

                    for (int ml = 0; ml < first; ml++) {

                        if (oldList[ml].equals(tempList[ul])) {

                            // Put a dud character in which should stop loading.
                            // Fix me XXX!!
                            logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("EnumPlugins.LoadPlugIns()_cancelling_out_main_plug-in_") + oldList[ml]);

                            oldList[ml] = "";

                            // tempList[ul] = "";
                        }

                    }

                }

            }

            if (isJar) {
                third = 4;
            } else {

                third = 0;

            }

            final int listTotal = first + second + third;

            fileList = new String[listTotal];

            if (oldList != null) {
                System.arraycopy(oldList, 0, fileList, 0, oldList.length);

                if (second > 0) {

                    System.arraycopy(tempList, 0, fileList, oldList.length, tempList.length);

                }
            }

            /* If JamochaMUD is being run from a Jarfile we will manually load
             * the plug-ins from there.  This should be reasonably "safe" in
             * that only the "primary" plug-ins should be bundled with JamochaMUD.
             * But this still warrants a bit FIX ME XXX!!!
             */
            if (isJar) {

                logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Manually_inserting_jarfile_plug-ins."));

                fileList = getJarPlugs();

            }

        } else {

            fileList = plugIns.list();

        }

        // Now read this to an array, deleting any
        // non-'class' files.  This could be done
        // with a FilenameFilter, but we're going
        // to do this quick and dirty.
        boolean newEnough;  // This ensures that our plug-in has the newest Interface.

        // Otherwise, the plug-in can hang JamochaMUD during loading
        try {

            // This may cause an error if the file doesn't exist
            // Specifically, this occurs on *nix-type systems
            for (int i = 0; i < fileList.length; i++) {

                if (fileList[i].toLowerCase().endsWith(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("class"))) {

                    fileList[i] = fileList[i].substring(0, (fileList[i].length()) - 6);

                    // This one has made the temp list!
                    // Now see if it conforms to the
                    // Plug-In 'standard'
                    // This must be done through 'type-casting',
                    // for the plug-ins don't exist at compile time
                    try {
                        // First, capture the class
                        final Object plugClass = Class.forName(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("anecho.JamochaMUD.plugins.") + fileList[i]).newInstance();

                        final PlugInterface plug = (PlugInterface) plugClass;

                        // Check to see if this class is at the proper level for the installed
                        // level of JamochaMUD (different Interfaces, etc).
                        final Class tempClass = plugClass.getClass();
                        final Method[] theMethods = tempClass.getMethods();

                        newEnough = false;

                        for (int fl = 0; fl < theMethods.length; fl++) {

                            final String methodString = theMethods[fl].getName();

                            // String returnString = theMethods[fl].getReturnType().getName();
                            if (methodString.equals(LASTINTERFACE)) {
                                newEnough = true;

                                if (DEBUG) {
                                    System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("We_have_determined_that_") + fileList[i] + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("_is_new_enough."));
                                    System.err.println(fl + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString(")_Method:_") + methodString + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("_matches_Last_Interface:_") + LASTINTERFACE);
                                }
                            }

                        }

                        // Querry to see if it's a plugin or not
                        if (newEnough) {

                            try {

                                if (splash != null) {

                                    ((anecho.gui.SplashScreen) splash).updateMessage(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Loading_plug-in_") + plug.plugInName() + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("..."));

                                }

                                plugInClass.addElement(plugClass);

                                plugInName.addElement(plug.plugInName());

                                plugInType.addElement(plug.plugInType());

                                plugInStatus.addElement(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("false"));

                                deactivate(plugInStatus.size() - 1);

                                // tell the plug-in the variable for our 'settings'
                                // plug.setSettings(settings);
                                // New as of 2002, create a settings directory automatically
                                // This should help promote good form and consistency.
                                if (plug.haveConfig()) {

                                    createSettingsDirectory(fileList[i]);

                                }

                                plug.initialiseAtLoad();

                            } catch (Exception notPlugIn) {

                                // Disregarded
                                logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Name_failure..._") + notPlugIn + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("_from_") + plugClass);

                                notPlugIn.printStackTrace();

                                logger.debug(fileList[i] + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("_not_loaded_as_plug-in."));

                            }

                        } else {

                            logger.debug(fileList[i] + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("_does_not_conform_to_the_latest_PlugInterface_and_will_be_ignored."));

                        }

                    } catch (InstantiationException iexc) {

                        // This is a standard error, from trying
                        // to instantiate the 'interface'
                        logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Instantiation_Exception_") + iexc);

                    } catch (ClassNotFoundException cnfe) {

                        logger.debug(fileList[i] + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("_does_not_conform_to_current_JamochaMUD_plug-in_requirements."));

                    } catch (IllegalAccessException | SecurityException exc) {

                        logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Plugin_exception:_") + exc);

                        exc.printStackTrace();

                    }

                }

            }

        } catch (Exception err) {

            // This is from the 'outside' loop
            logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("EnumPlugins:_Non-fatal_error_") + err);

        }

        // Write the names and status to the hashtable
        settings.setJMValue(JMConfig.PLUGINNAME, plugInName);

        settings.setJMValue(JMConfig.PLUGINSTATUS, plugInStatus);

        logger.debug("EnumPlugIns.loadPlugIns: calling resetPlugInStatus");
        resetPlugInStatus();
        logger.debug("EnumPlugIns.loadPlugIns: finished calling resetPlugInStatus");

    }

    /**
     * Analyse the input/output and cycle through the list of plugins, applying
     * any where the input/output meets the right criteria
     *
     * @param target The initial string that the plug-in will operate on.
     * @param plugType A string representing the type of plug-in currently being
     * processed
     * @param mSock The MuSock that this plug-in is being called by.This ensures
     * that the input/output is being processed for the proper MU.
     * @return The string represents the final output from the current plug-in ,
     * which may or may not have changed depending on the plug-in's function
     */
    public static String callPlugin(String target, final String plugType, final MuSocket mSock) {

        // String targetString = new String();  // This has to be instantiated so that we can return it (null)
        String targetString = null;  // This has to be instantiated so that we can return it (null)

        boolean anyActive = false;  // If we have no active plug-ins, we have to copy the target string to targetString

        // or we'll end up getting a null back, which really fouls things up!
        PlugInterface plug;

        // String plugName;
        String type;

        if (plugInName.size() < 1) {
            // return target;
            targetString = target;

            if (DEBUG) {
                System.err.println("EnumPlugIns.callPlugin plugInName.size is less than 1, returning intial string.");
            }
        } else {
            // String temp2;
            // String plugName;

            // This is a public routine called to parse the plugins
            // String temp1;
            for (int i = 0; i < plugInName.size(); i++) {
                // Cycle through the list of plugins
                // calling only the type appropriate (input/output/other)
                // temp1 = (String) plugInType.elementAt(i);
                // temp2 = (String)plugInStatus.elementAt(i);

                plug = (PlugInterface) (plugInClass.elementAt(i));
                // plugName = plug.plugInName();
                type = plug.plugInType();

                // if (plugType.toLowerCase().equals(type.toLowerCase()) && plug.isActive()) {
                if (plugType.equalsIgnoreCase(type) && plug.isActive()) {
                    // This is what we want, call the plugin
                    anyActive = true;
                    try {
                        targetString = plug.plugMain(target, mSock);
                        if (DEBUG) {
                            System.err.println("EnumPlugIns.callPlugin -> " + plug.plugInName());
                            System.err.println("Start: " + target);
                            System.err.println("Finished: " + targetString);
                        }
                        // If we don't update this we won't catch other plug-ins that modify the text!
                        // This is nasty.  Fix Me XXX
                        target = targetString;
                    } catch (Exception e) {
                        if (DEBUG) {
                            System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Plugin_Exception_") + e);
                            e.printStackTrace();
                        }
                    }
                }
            }

            // We had no active plug-ins, so our string should pass through unchanged
            if (!anyActive) {
                targetString = target;

                if (DEBUG) {
                    System.err.println("EnumPlugIns.callPlugin - didn't find any active plug-ins.");
                }
            }
        }

        return targetString;

    }

    /**
     *
     * Set the status of the plugins, whether they are active or inactive
     *
     */
    // public static void ResetPlugInStatus() {
    public void resetPlugInStatus() {
        // Read in the .plugins.rc file, to set the state of the plugins

        Vector stateList;

        try {
            final String userDir = settings.getJMString(JMConfig.USERDIRECTORY);
            if (DEBUG) {
                System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Attempting_to_get_input_file_") + userDir + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString(".plugins.rc"));
            }

            final FileInputStream inputFile = new FileInputStream(userDir + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString(".plugins.rc"));
            final ObjectInputStream serializeStream = new ObjectInputStream(inputFile);
            stateList = (Vector) serializeStream.readObject();

            // Now that we have the vector, loop through and change
            // any needed plugins to 'true' (active)
            for (int j = 0; j < plugInName.size(); j++) {
                for (int i = 0; i < stateList.size(); i++) {

                    // Can this be done in one line with = ! ?  Fix Me XXX
                    if (plugInName.elementAt(j).equals(stateList.elementAt(i))) {
                        // This matches, so we set it true
                        plugInStatus.setElementAt(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("true"), j);
                        activate(j);

                    }
                }
            }

        } catch (FileNotFoundException e) {
            // The file does not exist, but will be written later
            // This is not a bad thing.  We'll just keep the error quiet as
            // Otherwise folks found it disturbing
            if (DEBUG) {
                System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString(".plugins.rc_file_was_not_found._Don't_worry,_we'll_create_one_later_on."));
            }

            // return;
        } catch (IOException | ClassNotFoundException e) {

            System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Error_during_serialization_") + e);

            // The file probably does not exist
            // return;
        }

        // Now add the plugInStatus to the hashtable
        settings.setJMValue(JMConfig.PLUGINNAME, plugInName);

        settings.setJMValue(JMConfig.PLUGINSTATUS, plugInStatus);

        // rebuild the Plug-in menu with currect information
        // final MuckMain tempMain = settings.getMainWindowVariable();
        final MuckMain tempMain = MuckMain.getInstance();

        if (DEBUG) {

            System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("EnumPlugIns_rebuilding_plugin_menu."));

        }

        tempMain.rebuildPlugInMenu();

        if (DEBUG) {
            System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("EnumPlugIns_plugin_menu_rebuilt."));
        }

    }

    /**
     * This method returns the user-readable description of the given plug-ins
     * function.
     *
     * @param targetName Name of the plug-in to examine.
     * @return A user-readable text description of the plug-in's features.
     */
    public static String description(final String targetName) {

        String targetString = "";
        String temp1;

        // This is a public routine called to parse the plugins
        for (int i = 0; i < plugInName.size(); i++) {

            // Cycle through the list of plugins
            // calling only the type appropriate (input/output/other)
            temp1 = (String) plugInName.elementAt(i);

            // if (targetName.toLowerCase().equals(temp1.toLowerCase())) {
            if (targetName.equalsIgnoreCase(temp1)) {
                // }catch (IOException | ClassNotFoundException e) {
                Object plugClass;

                try {
                    plugClass = plugInClass.elementAt(i);
                    targetString = ((PlugInterface) plugClass).plugInDescription();
                } catch (Exception e) {
                    if (DEBUG) {
                        System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Plugin_Description_Exception_") + e);
                    }
                }

            }

        }

        return targetString;

    }

    /**
     *
     * Return the plugin object for the name given
     *
     * @param targetName
     *
     * @return
     *
     */
    public static Object classByName(final String targetName) {

        // This is a public routine called to parse the plugins
        String temp1;
        Object plugClass = new Object();

        for (int i = 0; i < plugInName.size(); i++) {
            // Cycle through the list of plugins
            // calling only the type appropriate (input/output/other)
            temp1 = (String) plugInName.elementAt(i);

            // if (targetName.toLowerCase().equals(temp1.toLowerCase())) {
            if (targetName.equalsIgnoreCase(temp1)) {
                // This is what we want, call the plugin

                try {
                    plugClass = plugInClass.elementAt(i);
                } catch (Exception e) {
                    if (DEBUG) {
                        System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Plugin_Description_Exception_") + e);
                    }
                }

                // targetString = new String(((PlugInterface)plugClass).plugInDescription());
            }

        }

        return plugClass;

    }

    /**
     *
     * This is a static method that we'll
     *
     * allow our plugins to call via
     *
     * the "loader" in our PlugInterface.
     *
     * This basically allows our plug-ins to send
     *
     * responses back to the connected MUD/Muck
     *
     * @param textToMU
     *
     */
//    public static void sendToMU(final String textToMU) {
    // Since we have this function nicely mapped out already, we'll
    // just call it from here
    // DataIn.JMSendText(textToMU);
    // Fix this XXX
    // MuckConn.typeHere.JMSendText(textToMU);
//    }
    /**
     *
     * A static method to allow a plug-in to write output to the screen. There
     * may need to be more done than what can simply be done at the time of
     * &quot;processing" Should the CHandler method be moved here? Fix Me XXX
     *
     * @param text The text to be written out to the user's display.
     *
     */
    public static void write(final String text) {
        // Fix this XXX
        // MuckMain.mainText.append(text);
    }

    /**
     * Check to see if this plug-in has a settings directory. If one isn't found
     * the we'll create it
     */
    private void createSettingsDirectory(final String name) {

        // check to see if the directory already exists
        final String userDir = settings.getJMString(JMConfig.USERDIRECTORY);
        final String fileName = userDir + PLUGINSTR + pathSeparator + name + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Dir");
        final File settingsDir = new File(fileName);

        // check and see if there is a 'plugins' directory
        if (!settingsDir.exists()) {
            // There is no plugins directory, so we'll create one
            settingsDir.mkdir();
        }

    }

    /**
     *
     * Update out internal list and activate the plug-in
     *
     * @param plugNum The index number of the plug-in we wish to activate
     *
     */
    public void activate(final int plugNum) {
        Object plugClass;
        plugInStatus.setElementAt(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("true"), plugNum);

        try {
            plugClass = plugInClass.elementAt(plugNum);

            if (DEBUG) {
                System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("EnumPlugIns_activating_") + ((PlugInterface) plugClass).plugInName());
            }

            ((PlugInterface) plugClass).activate();

        } catch (Exception e) {
            if (DEBUG) {
                System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Plugin_activate_Exception_") + e);
            }
        }

    }

    /**
     *
     * Update out internal list and deactivate the plug-in
     *
     * @param plugNum The index number of the plug-in we wish to deactivate
     *
     */
    public void deactivate(final int plugNum) {
        Object plugClass;

        if (plugNum < plugInStatus.size()) {

            plugInStatus.setElementAt(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("false"), plugNum);

            try {
                plugClass = plugInClass.elementAt(plugNum);

                if (DEBUG) {
                    System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("EnumPlugIns_deactivating_") + ((PlugInterface) plugClass).plugInName());
                }

                ((PlugInterface) plugClass).deactivate();
            } catch (Exception e) {
                if (DEBUG) {
                    System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Plugin_activate_Exception_") + e);
                }
            }
        }

    }

    /**
     * Loop through the plugins and tell them all to &quot;halt"
     */
    public void haltPlugIns() {
        if (DEBUG) {
            System.err.println("EnumPlugins.haltPlugIns() called");
        }

        Object plugClass;

        // String targetString = "";
        // String temp1;
        // This is a public routine called to parse the plugins
        for (int i = 0; i < plugInName.size(); i++) {
            // Cycle through the list of plugins
            // calling only the type appropriate (input/output/other)
            // temp1 = (String) plugInName.elementAt(i);

            // if (targetName.toLowerCase().equals(temp1.toLowerCase())) {
            // This is what we want, call the plugin
            try {
                plugClass = plugInClass.elementAt(i);
                ((PlugInterface) plugClass).setAtHalt();
                // }
            } catch (Exception e) {
                if (DEBUG) {
                    System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Plugin_HaltPlugIns_Exception_") + e);
                }
            }

        }

    }

    /* This method calls the plug-in installer, allowing the user

     * to easily install newly down-loaded plug-ins

     */
    /**
     *
     * Update out internal list and deactivate the plug-in
     *
     */
    public void addNewPlugIn() {

//        final PlugInInstaller loadPlug = new PlugInInstaller(settings);
        final PlugInInstaller loadPlug = new PlugInInstaller();

        loadPlug.install();

    }

    /**
     * Remove (erase) a plug-in that is currently installed with JamochaMUD.
     * This is currently an empty method. Fix Me XXX.
     */
    public void removePlugIn() {
    }

    /**
     * This method retrieves a list of plug-ins from with the JamochaMUD
     * jarfile.
     *
     * @return An array of plug-in class names
     */
    private String[] getJarPlugs() {

        if (DEBUG) {
            System.err.println("EnumPlugIns.getJarPlugs reading plug-ins from Jarfile.");
        }

        String[] fileList;
        int listTotal;

        try {
            final java.util.jar.JarFile tmpJar = new java.util.jar.JarFile("JamochaMUD.jar");

            final java.util.Enumeration fileEnum = tmpJar.entries();

            String tmpStr;
            String tmpPlugName;
            final String plugPath = "anecho/JamochaMUD/plugins/";
            final int lastSlash = plugPath.lastIndexOf('/') + 1;
            final Vector tmpPlugList = new Vector();

            while (fileEnum.hasMoreElements()) {

                tmpStr = fileEnum.nextElement().toString();

//                if (DEBUG) {
//                    System.err.println("jarfile: " + tmpStr);
//                }
                if (tmpStr.startsWith(plugPath) && tmpStr.indexOf('/', lastSlash) < 0 && tmpStr.endsWith(".class")) {
                    tmpPlugName = tmpStr.substring(lastSlash);

                    if (!tmpPlugName.contains("PlugInterface.class")) {
                        tmpPlugList.addElement(tmpPlugName);
                    }

                    if (DEBUG) {
                        System.err.println("EnumPlugIns.getJarPlugs candidate: " + tmpPlugName);
                    }
                }
            }

            listTotal = tmpPlugList.size();
            fileList = new String[listTotal];

            for (int i = 0; i < listTotal; i++) {
                fileList[i] = tmpPlugList.elementAt(i).toString();
            }

        } catch (IOException exc) {
            // We couldn't get a proper list, so we'll fall back on the classics!
            listTotal = 5;
            fileList = new String[listTotal];
            fileList[listTotal - 5] = "PathWalker.class";
            fileList[listTotal - 4] = "Ticker.class";
            fileList[listTotal - 3] = "TimeStamp.class";
            fileList[listTotal - 2] = "FBLocationPlugIn.class";
            fileList[listTotal - 1] = "Trigger.class";
        }

        return fileList;
    }
}
// } catch (IOException exc) {
