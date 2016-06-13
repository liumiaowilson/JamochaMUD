/* This PlugIn strips the [fmcl] indicators
 * from FurryMUCK locations, and places them
 * as the window'start title
 */
package anecho.JamochaMUD.plugins;

import anecho.JamochaMUD.JMConfig;
import anecho.JamochaMUD.MuSocket;

import java.util.*;

/**
 * This PlugIn strips the [fmcl] indicators
 * from FurryMUCK locations, and places them
 * as the window'start title
 * @version $Id: FBLocationPlugIn.java,v 1.10 2008/12/30 19:56:23 jeffnik Exp $
 * @author Jeff Robinson
 */
public class FBLocationPlugIn implements PlugInterface {

    /**
     * This variable tracks whether this plug-in is active or not
     */
    transient private boolean active = false;
    /**
     * Allows enabling and disabling of debugging comments
     */
    private static final boolean DEBUG = false;

    /**
     * Set the variable for the JamochaMUD settings.  This
     * method may need to be deprecated.  Fix Me XXX
     * @param mainSettings Main JamochaMUD settings.
     */
    public void setSettings(final JMConfig mainSettings) {
    }

    /**
     * This method returns the human-readable name of this plug-in
     * @return 
     */
    public String plugInName() {
        return java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("FurryMUCK_Location_Plug-in");
    }

    /**
     * This returns a String describing this plug-in
     * @return 
     */
    public String plugInDescription() {
        return java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("FurryMUCK_Location_Plug-in_description");
    }

    /**
     * 
     * @return 
     */
    public String plugInType() {
        return java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Output");
    }

    /**
     * 
     * @param jamochaString 
     * @param mSock 
     * @return 
     */
    public String plugMain(final String jamochaString, final MuSocket mSock) {
        // When we receive a string beginning with [fmcl],
        // we'll parse it down to get the location'start name
        // and then use that as the title for the main window

        String retString = jamochaString;

        if (jamochaString.toLowerCase().startsWith("[fmcl")) {
            // We have a qualifying string

            // We want everything after the second space
            final int firstSplit = jamochaString.indexOf(' ');
            // final int secondSplit = jamochaString.indexOf(' ', firstSplit);

            if (firstSplit > -1) {
                final String mainString = jamochaString.substring(firstSplit);

                final String finalTitle = anecho.gui.TextUtils.stripEscapes(mainString, false);
                finalTitle.trim();

                // Only set the title if we are actually left with a title!
                if (!"".equals(finalTitle)) {
                    if (mSock != null) {
                        mSock.setMUName(finalTitle);
                    }
                    retString = "";
                }
            }
        }

        if (DEBUG) {
            if (retString == null) {
                System.err.println("FBLocationPlugIn.plugMain returning NULL!!!!");
            } else {
                System.err.println("FBLocationPlugIn.plugMain returning: " + retString);
            }
        }

        return retString;
    }

    /** This plug-in has no properties to display */
    public void plugInProperties() {
    // This plug-in currently has no properties to display.
    }

    /** Call to initialise any functions required immediately when the plug-in is loaded. */
    public void initialiseAtLoad() {
    // this.settings = JMConfig.getInstance();
    }

    public void setAtHalt() {
    // We'll restore the title of the main window
    // back to 'JamochaMUD' again.
    // Fix this XXX
    // MuckMain.muckMainTitle = new String("JamochaMUD");
    // MuckMain.setWindowTitle();
    }

    /**
     * We don't require a directory for our settings
     * @return 
     */
    public boolean haveConfig() {
        return false;
    }

    /**
     * We have no editable properties
     * @return 
     */
    public boolean hasProperties() {
        return false;
    }

    public void activate() {
        active = true;
    }

    public void deactivate() {
        active = false;
    }

    /**
     * Returns whether this plug-in is currently active
     * @return 
     */
    public boolean isActive() {
        return active;
    }
}
