/* PlugInterface.java
 * Creates an interface that all plug-ins declare
 * to interact with JamochaMUD
 */

/* JamochaMUD, a Muck/Mud client program
 * Copyright (C) 1998-2000 Jeff Robinson
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
 * @version $Id: PlugInterface.java,v 1.6 2008/12/30 19:56:23 jeffnik Exp $
 * @author Jeff Robinson
 */

// import anecho.JamochaMUD.JMConfig;
import anecho.JamochaMUD.MuSocket;

/**
 * This is the standard interface that must be used by all
 * JamochaMUD plug-ins.  It outlines the standard commands
 * required by JamochaMUD to qualify as a plug-in, such as
 * starting and stopping the plug-in, as well as registering
 * the plug-in with the main program.
 */
public interface PlugInterface {
    // This lists the methods that can be called by JamochaMUD
    /**
     * Set the loader of the module. This is necessary to know if you want to
     * contact the modules parent.
     * 
     * This method may now need to be deprecated since the changing
     * of JMConfig to a Singleton.  We no longer need to pass the
     * settings variable to each plug-in.
     * 
     * Fix Me XXX
     * @param settings The main JamochaMUD settings variable
     */
    // void setSettings(JMConfig settings);

    /**
     * Returns the plugin's proper name
     * @return Returns a human-readable string of the plug-in's name.
     */
    String plugInName();

    /**
     * Returns a description, eg. author, date, build...
     * @return Returns a human-readable description of what
     * the plug-in does, as well as any other information
     * such as author, date, version... what-ever the
     * author deems important.
     */
    String plugInDescription();

    /**
     * Returns a type of either input, output, or other
     * @return 
     */
    String plugInType();

    /**
     * the core of the plugin
     * @param jamochaString 
     * @param mSock 
     * @return 
     */
    String plugMain(String jamochaString, MuSocket mSock);

    /** Any user configurable options for the plugin */
    void plugInProperties();

    /**
     * Check to see if the plug-in has configurable properties
     * @return <pre>true</pre> - plug-in can be configured
     * <pre>false</pre> - plug-in cannot be configured
     */
    boolean hasProperties();

    /** This function is called at load-up, in case
     * properties are needed... eg. lists, settings */
    void initialiseAtLoad();

    /**
     * This function tells the plug-in that it should be functioning.
     * See Deactive() for the inverse function.  activate may be called
     * more than once.
     */
    void activate();

    /**
     * This function tells the plug-in that it should not be active.
     * deactivate may be called more than once.
     * Formerly this function was handled by setAtHalt.
     */
    void deactivate();

    /**
     * Allows JamochaMUD to check whether this plug-in is active
     * and should be used.
     * @return <CODE>true</CODE> - this plug-in is active
     * <CODE>false</CODE> - this plug-in is not active
     */
    boolean isActive();

    /**
     * Previously this function did most of the functions of deactivate()
     * but now this is reserved solely for shutting down the plug-in
     * and doing any necessary clean-up.
     */
    void setAtHalt();

    /**
     * This function indicates whether a directory
     * is required to hold settings for our plug-in.
     * It is recommended for consistency to let JamochaMUD
     * create and handle any plug-in directories.  Plus,
     * it means you can use less code in your plug-in!
     * @return <pre>true</pre> - create a settings directory
     * <pre>false</pre> - no directory required
     */
    boolean haveConfig();
}
