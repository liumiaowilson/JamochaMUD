/* JamochaMUD, a Muck/Mud client program
 * Copyright (C) 1998-2007 Jeff Robinson
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

import anecho.JamochaMUD.JMConfig;
import anecho.JamochaMUD.MuSocket;

import java.util.*;
import java.text.SimpleDateFormat;

/**
 * TimeStamp is a class that allows the user to put a time stamp
 * at the beginning of lines of text received in the output window.
 * The format of the time is user-definable.
 */
public class TimeStamp implements PlugInterface {
    
    
    
//    /** This constructor is here for the sake of "proper" coding
//     */
//    public TimeStamp() {
//        // If you got here, don't expect anything!
//    }
    
    /**
     *
     * @param mainSettings
     */
    public void setSettings(final JMConfig mainSettings) {
        
        // this.settings = mainSettings;
        
    }
    
    
    
    /**
     * This method returns the name of the plug-in
     * @return A string containing the human readable name of this plug-in.
     */
    
    public String plugInName() {
        
        // Nothin'
        
        return "Time stamp";
        
    }
    
    
    
    /**
     * A basic description of the plug-in that is shown in
     * the JamochaMUD &quot;Manage Plugins" dialogue box
     * @return A human-readable String with a brief description of
     * what this plug-in does.
     */
    
    public String plugInDescription() {
        
        return "This plug-in will place a user-configured time stamp at the beginning of every line out output.";
        
    }
    
    
    
    /**
     * This indicates what type of plugin this is, ie: input or output.
     * This setting will affect what information JamochaMUD tries to
     * process with this plug-in.
     * If the plug-in is set to a setting that JamochaMUD does not
     * understand than the plugin will be ignored.
     * @return 
     */
    
    public String plugInType() {
        
        return "Output";
        
    }
    
    
    
    /**
     * This is the main method of the plug-in.  It is passed both
     * a string for processing as well as and associated MU* that
     * the message is coming from/going to.
     * @param jamochaString The string received from the MU* and to be processed.
     * @param mSock The connection that the given string belongs to.
     * @return This returns the processed version of the originally
     * received string.
     */
    
    public String plugMain(final String jamochaString, final MuSocket mSock) {

        if (sdf == null) {
            setStandardFormat();
        }
        
        final Calendar cal = Calendar.getInstance();
        final String timeStr = sdf.format(cal.getTime());
        
        final String retStr = timeStr + ' ' + jamochaString;
        
        if (DEBUG) {
            System.out.println(retStr);
        }
        
        
        // return "(" + timeStr + ") " + jamochaString;
        // return "foobar";
        return retStr;
        
    }
    
    /** This method is called by the &quot;Properties" option in
     * the JamochaMUD &quot;Manage plugins" dialogue box.
     * Any settings that you wish the user to have access to
     * can be configured through this method
     */
    public void plugInProperties() {
        final anecho.JamochaMUD.plugins.TimeStampDir.TimeStampGUI plugGUI = anecho.JamochaMUD.plugins.TimeStampDir.TimeStampGUI.getInstance(this);
        
        plugGUI.setTimeStamp(timeFormat);
        plugGUI.setVisible(true);
        
        // Get the new time-stamp
        final String tempTimeFormat = plugGUI.getTimeStamp();
        
        // A null time format means that it didn't change
        // if (!tempTimeFormat.equals("")) {
        if (!"".equals(tempTimeFormat)) {
            timeFormat = tempTimeFormat;
            // sdf = new SimpleDateFormat(timeFormat);
            setStandardFormat();
        }
        
        
    }
    
    
    
    /** This method is called as soon as the plugin is first loaded.
     * It is useful for loading or configuring anything needed by
     * the plug-in at start-up (such as loading settings).
     * This method is only called when JamochaMUD first loads the
     * plug-in, even if the plug-in is not currently enabled.
     */
    public void initialiseAtLoad(){
        // Nothing here needs to be initialised!
    }
    
    
    
    /** The method is automatically called when JamochaMUD is quit
     * by using the File -> Exit menu or the close window icon.
     * This method will not be called if JamochaMUD is &quot;killed"
     * or crashes.
     * This section is useful for writing any settings back to disk.
     */
    public void setAtHalt(){
        this.saveSettings();
    }
    
    
    
    /**
     * This method tells JamochaMUD whether this plugin should have
     * its own person configuration directory where it can keep its
     * settings.
     * A return of false means that the plugin has no settings that
     * need to be saved to disk.
     * A return of true tells JamochaMUD to create a settings directory
     * for this plugin (if one does not already exist).
     * @return 
     */
    
    public boolean haveConfig() {
        return true;
    }
    
    /**
     * If a plug-in has properties that can be configured via a GUI,
     * this method should return <tt>true</tt>, otherwise it should
     * return false.
     * When settings this result to <tt>true</tt>, it indicates to
     * JamochaMUD that the PlugInProperties method may be called.
     * @return
     */
    
    public boolean hasProperties() {
        
        return true;
        
    }
    
    
    
    /** This function is automatically called by JamochaMUD when the
     * plug-in is first activated (at each JamochaMUD start-up).  This
     * may be called if the plug-in was active the last time JamochaMUD
     * was stopped, or when it is set active from the &quot;Manage Plug-Ins"
     * dialogue.
     * It can be useful for setting up items that you did not want setup
     * during the normal initialisation of the plug-in.
     */
    public void activate() {
        
        if (DEBUG) {
            System.err.println("TimeStamp.activate");
        }
        
        this.readSettings();
        
        plugActive = true;
        
        // cal = Calendar.getInstance();
        // sdf = new SimpleDateFormat(timeFormat);
        setStandardFormat();
        
        
    }
    
    
    
    /** This function is automatically called by JamochaMUD when the
     * plug-in is set inactive from the JamochaMUD &quot;Manage Plug-Ins"
     * dialogue.  With this method, you can easily deactive items that may have
     * initially been setup by the <tt>Activate</tt> method (such as removing
     * listeners, etc.).
     */
    public void deactivate() {
        if (DEBUG) {
            System.err.println("TimeStamp.deactivate");
        }
        
        plugActive = false;
        
    }
    
    
    
    /**
     *
     * @return
     */
    
    public boolean isActive() {
        // return false;
        return plugActive;
    }
    
    /**
     * This method will read in time format settings from a configuration file
     * if it exists.  If the file does not exist, a standard format is used.
     */
    private void readSettings() {
        if (DEBUG) {
            System.err.println("TimeStamp reading settings...");
        }
        
        if (settings == null) {
            settings = JMConfig.getInstance();
        }
        
        final String pathSep = java.io.File.separator;
        final String plugIns = settings.getJMString(anecho.JamochaMUD.JMConfig.USERPLUGINDIR);
        final String timeStampDir = plugIns + pathSep + "TimeStampDir";
        final java.io.File info = new java.io.File(timeStampDir + pathSep +".timestamp.rc");
        
        try {
            final java.io.FileInputStream inputFile = new java.io.FileInputStream(info);
            final java.io.ObjectInputStream serializeStream = new java.io.ObjectInputStream(inputFile);
            
            timeFormat = serializeStream.readObject().toString();
            
        } catch (Exception e) {
            if (DEBUG) {
                System.out.println("TimeStamp.readSettings() Error during serialization " + e);
                System.err.println("TimeStamp needs to know how to create a new file.");
            }
        }
        
        if (DEBUG) {
            System.err.println("Finished reading file.");
        }
        
//        if (timeFormat == null || timeFormat.equals("")) {
//            if (DEBUG) {
//                System.err.println("There has been an error reading the time format, so we'll use a default");
//            }
//            
//            timeFormat = "(HH:mm:ss)";
//        }
        
        // sdf = new SimpleDateFormat(timeFormat);
        setStandardFormat();
    }
    
    /**
     * This method saves the current MusicBox settings to a file.
     */
    protected void saveSettings() {
        
        if (DEBUG) {
            System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Saving_settings..."));
        }
        
        if (settings == null) {
            settings = JMConfig.getInstance();
        }
        
        final String pathSep = java.io.File.separator;
        final String plugIns = settings.getJMString(anecho.JamochaMUD.JMConfig.USERPLUGINDIR);
        final String musicBoxDir = plugIns + pathSep + "TimeStampDir";
        final java.io.File info = new java.io.File(musicBoxDir + pathSep + ".timestamp.rc");
        
        if (info.exists()) {
            info.delete();
        }
        
        try {
            final java.io.FileOutputStream outputFile = new java.io.FileOutputStream(info);
            final java.io.ObjectOutputStream sStream = new java.io.ObjectOutputStream(outputFile);
            
            sStream.writeObject(timeFormat);
            
            sStream.flush();
        } catch (Exception exc) {
            System.out.println("TimeStamp.saveSettings() Macro serialization error " + exc);
        }
        
        if (DEBUG) {
            System.err.println("TimeStamp.saveSettings() Finished saving file.");
        }
    }
    
    /** Set the standard time format
     * 
     */
    private void setStandardFormat() {
        if (timeFormat == null || timeFormat.equals("")) {
            timeFormat = "HH:mm:ss";
        }
        
        sdf = new SimpleDateFormat(timeFormat);
    }
    
    /** A variable tracking whether this plug-in is active or not */
    transient private boolean plugActive = false;
    transient private String timeFormat;
    transient private SimpleDateFormat sdf;
    /** Used to enable or disable debugging feedback */
    private static final boolean DEBUG = false;
    /** Consider removing this variable and just using JMConfig.getInstance()
     * Fix Me XXX
     */
    transient private JMConfig settings;
    
}

