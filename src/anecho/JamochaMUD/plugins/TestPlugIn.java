/* This is a test plug-in.

 * It mainly contains stubs for the

 * functions the plug-ins look for.

 */



package anecho.JamochaMUD.plugins;



import anecho.JamochaMUD.JMConfig;

import anecho.JamochaMUD.MuSocket;



import java.util.*;



public class TestPlugIn implements PlugInterface {



    // private transient JMConfig settings;

    /** This constructor is here for the sake of "proper" coding
     */
    public TestPlugIn() {
        
    }

    /**

     * 

     * @param mainSettings 

     */

    public void setSettings(final JMConfig mainSettings) {

        // this.settings = mainSettings;

    }



    /**

     * This method returns the name of the plug-in

     * @return 

     */

    public String plugInName() {

        // Nothin'

        return java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Test_Plug-in");

    }



    /**

     * A basic description of the plug-in that is shown in

     * the JamochaMUD &quot;Manage Plugins" dialogue box

     * @return 

     */

    public String plugInDescription() {

        return java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("TestPlugIn_is_a_sample_plug-in_used_to_show_developers_how_to_create_their_own_plug-ins.");

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

        return java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("TestPlugIn_->_PlugInType");

    }



    /**

     * This is the main method of the plug-in.  It is passed both

     * a string for processing as well as and associated MU* that

     * the message is coming from/going to.

     * @param jamochaString 

     * @param mSock 

     * @return 

     */

    public String plugMain(final String jamochaString, final MuSocket mSock) {

        System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("TestPlugIn_received:_") + jamochaString);

        return jamochaString;

    }



    /** This method is called by the &quot;Properties" option in
     * the JamochaMUD &quot;Manage plugins" dialogue box.
     * Any settings that you wish the user to have access to
     * can be configured through this method
     */
    public void plugInProperties() {

    }



    /** This method is called as soon as the plugin is first loaded.
     * It is useful for loading or configuring anything needed by
     * the plug-in at start-up (such as loading settings).
     * This method is only called when JamochaMUD first loads the
     * plug-in, even if the plug-in is not currently enabled.
     */
    public void initialiseAtLoad(){

    }



    /** The method is automatically called when JamochaMUD is quit
     * by using the File -> Exit menu or the close window icon.
     * This method will not be called if JamochaMUD is &quot;killed"
     * or crashes.
     * This section is useful for writing any settings back to disk.
     */
    public void setAtHalt(){

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

        return false;

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

        return false;

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

    }



    /** This function is automatically called by JamochaMUD when the
     * plug-in is set inactive from the JamochaMUD &quot;Manage Plug-Ins"
     * dialogue.  With this method, you can easily deactive items that may have
     * initially been setup by the <tt>Activate</tt> method (such as removing
     * listeners, etc.).
     */
    public void deactivate() {

    }



    /**
     * 
     * @return 
     */

    public boolean isActive() {

        return false;

    }

}

