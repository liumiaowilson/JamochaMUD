/**
 * Used to gather and report system information for JamochaMUD
 * $Id: SysInfo.java,v 1.6 2008/03/08 16:25:46 jeffnik Exp $
 */

/* JamochaMUD, a Muck/Mud client program
 * Copyright (C) 1998-2002 Jeff Robinson
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

import java.awt.Frame;

public class SysInfo {

    private static final String userDir = "user.dir";
    private static final boolean DEBUG = false;
    private transient JMConfig settings;

    /** Read in our system configuration (machine specific stuff)
     * and update any applicable settings */
//    public SysInfo(JMConfig mainSettings) {
    public SysInfo() {
        // this.settings = mainSettings;
        settings = JMConfig.getInstance();

        // We create a frame so that we can use it's getToolkit method.
        // Ugly, huh?  Yeah!
        final Frame dudFrame = new Frame();
        settings.setScreenSize(dudFrame.getToolkit().getScreenSize());

        // Set directory variables
        String userDir = "";
        String userPlugInDir = "";
        final java.util.Properties systemData = System.getProperties();
        // String osName = new String(systemData.getProperty("os.name"));

        final String pathSeparator = java.io.File.separator;
        final String sCurrentWorkingDir = new String(systemData.getProperty(userDir) + pathSeparator);

        // if (osName.toLowerCase().startsWith("windows") || osName.toLowerCase().startsWith("os/2") || osName.toLowerCase().startsWith("mac")) {
        // Trying to determine single-user environments from Java is pretty precarious
        // Instead, we'll default to a multi-user environment.  A single-user environment
        // can be specified by adding a -s argument when launching JamochaMUD
        // if (settings.isSingleUserMode()) {
        if (settings.getJMboolean(JMConfig.SINGLEUSERMODE)) {
            // This is a single user environment
            /*
            System.out.println("JamochaMUD, single user configuration.");
            Class testC = this.getClass();
            java.net.URL tempURL = testC.getResource("kehza.gif");
            java.io.InputStream tempStream = testC.getResourceAsStream("kehza.gif");
            java.io.File tempFile = new java.io.File(tempURL.getFile());

            System.out.println("Our class name: " + testC.getName());
            System.out.println("Our URL is: " + tempURL);
            System.out.println("Our InputStream is : " + tempStream);
            System.out.println("Our file is: " + tempFile);
            System.out.println("Our absolute path is: " + tempFile.getAbsolutePath());
            System.out.println("Our file's name: " + tempFile.getName());
            System.out.println("Our file's parent: " + tempFile.getParent());
            System.out.println("Our file's path: " + tempFile.getPath());
            try {
                System.out.println("Our canonical path is: " + tempFile.getCanonicalPath());
            } catch (Exception e) {
                System.out.println("Can't get canonical path.");
            }
            System.exit(0);

            // systemData.put(userDir, tempURL.getFile());
            */

            userDir = new String(systemData.getProperty(userDir) + pathSeparator);
            userPlugInDir = new String(systemData.getProperty(userDir) + pathSeparator + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("plugins"));
        } else {
            // This is a multi-user environment, put the
            // rc file in the user's home directory
            if (DEBUG) {
                System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("JamochaMUD..._going_multiuser"));
            }
            userDir = new String(systemData.getProperty("user.home") + pathSeparator + "JamochaMUD" + pathSeparator + "SavedData" + pathSeparator);
            userPlugInDir = userDir + "plugins";
        }

        if (DEBUG) {
            System.out.println("OS name: " + systemData.getProperty("os.name"));
            System.out.println("OS architecture: " + systemData.getProperty("os.arch"));
            System.out.println("OS version: " + systemData.getProperty("os.version"));
            System.out.println("Java version: " + systemData.getProperty("java.version"));
            System.out.println("User name: " + systemData.getProperty("user.name"));
            System.out.println("User home: " + systemData.getProperty("user.home"));
            System.out.println("User dir: " + systemData.getProperty(userDir));
            System.out.println("User PlugIn dir: " + userPlugInDir);
        }

        // Test for location of classes
        // Class testC = this.getClass();
        // System.out.println("Our class is: " + (testC.getResource("kehza.gif")).getFile());

        // Now stick all this data into the hashtable
        settings.setJMValue(JMConfig.PATHSEPARATOR, pathSeparator);
        settings.setJMValue(JMConfig.WORKINGDIRECTORY, sCurrentWorkingDir);
        settings.setJMValue(JMConfig.USERDIRECTORY, userDir);
        settings.setJMValue(JMConfig.OSNAME, systemData.getProperty("os.name"));        
        settings.setJMValue(JMConfig.USERPLUGINDIR, userPlugInDir);

        settings.setJMValue(JMConfig.COMMANDS_FILE, "~/CoffeeMUD/access_words.txt");
        
        settings.setJMValue(JMConfig.DICT_QUERY_URL, "open http://www.dictionary.com/browse/${query}");
    }
}
