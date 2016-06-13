package anecho.JamochaMUD.plugins;

import anecho.JamochaMUD.CHandler;
import anecho.JamochaMUD.MuSocket;

import anecho.JamochaMUD.plugins.ticker.Tick;
import java.util.*;

/**
 * This plug-in recreates the ticker function of TinyFugue.
 * 
 * This plug-in will implement the commands:
 * /tick - This command displays the number of seconds before the next tick.
 * /tickoff - Turns off the timer
 * /tickon - Turn on the timer and resets the tickcount.
 * /tickset - Resets the tickcounter.
 * /ticksize n - Sets a new tick size of "n" seconds.
 *
 * Each of these settings will be specific to the connected MU*
 * 
 * @author Jeff Robinson
 * @version $Id: Ticker.java,v 1.7 2009/06/17 02:39:59 jeffnik Exp $
 */
public class Ticker implements PlugInterface {

    /** 
     * This method is used for setting to tick size (in seconds)
     * @param size - The length of the tick in seconds
     */
    public void setTickSize(final int size) {
        Tick workTick = getTickHandle();

        if (workTick == null) {
            workTick = createNewTicker();
        }

        if (DEBUG) {
            System.err.println("Ticker.setTickSize setting new size.");
        }

        workTick.setSize(size);

        if (DEBUG) {
            System.err.println("Ticker.setTickSize set new size successfully.");
        }
    }

    /** 
     * This method returns the tick size
     * @return The length of the tick in seconds
     */
    public int getTickSize() {
        // return tickSize;
        int retSize = 0;
        final Tick workTick = getTickHandle();

        if (workTick != null) {
            retSize = workTick.getTickSize();
        }

        return retSize;
    }

    /**
     * Returns the handle of the Tick for the current MU*
     * 
     * @return Returns the handle for the appropriate tick
     */
    private Tick getTickHandle() {
        if (DEBUG) {
            System.err.println("Ticker.getTickHandler entered.");
        }

        final MuSocket muck = CHandler.getInstance().getActiveMUHandle();

        // final boolean hasMU = false;
        final int totalTicks = ticks.size();
        Tick tempTick = null;

        if (totalTicks > 0) {
            for (int i = 0; i < totalTicks; i++) {
                tempTick = (Tick) ticks.elementAt(i);

                if (tempTick.getMU() == muck) {
                    // We have the tick we are looking for, let's break out of this loop
                    break;
                }
            }
        }

        if (DEBUG) {
            System.err.println("Ticker.getTickHandle returns " + tempTick);
        }

        if (DEBUG) {
            System.err.println("Ticker.getTickHandler exited.");
        }

        if (null == tempTick) {
            // We don't have a Tick, so we'll set one up.
            createNewTicker();
        }

        return tempTick;

    }

    /**
     * This method returns the name of the plug-in
     * @return Human-readable name of plug-in
     */
    public String plugInName() {
        return "Ticker";
    }

    /**
     * A basic description of the plug-in that is shown in
     * the JamochaMUD &quot;Manage Plugins" dialogue box
     * @return 
     */
    public String plugInDescription() {
        return "The Ticker plug-in implement dikumud tick counting, similar to tintin.\n" +
                "/tick - This command displays the number of seconds before the next tick.\n" +
                "/tickoff - Turns off the timer\n" +
                "/tickon - Turn on the timer and resets the tickcount.\n" +
                "/tickset - Resets the tickcounter.\n" +
                "/ticksize n - Sets a new tick size of \"n\" seconds.\n";
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
        return anecho.JamochaMUD.EnumPlugIns.INPUT;
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
        /* All that this method will do is look for appropriate /tick commands
         * if we don't receive one then we'll just return the whole string */
        String retStr;
        final String testString = jamochaString.toLowerCase();

        if (DEBUG) {
            System.err.println("Ticker.plugMain received (toLowerCase): " + testString);
        }
        if (testString.startsWith("/tick")) {
            retStr = processTickCommand(jamochaString);
        } else {
            // This doesn't qualify as a tick command, so we'll return the string
            retStr = jamochaString;
        }

        return retStr;
    }

    /** This method will process the tick command and will return the String
     * if it does not qualify as a proper tick command
     * 
     * @param inStr
     * @return
     */
    private String processTickCommand(final String inStr) {
        boolean validCommand = false;
        final String lowerCmd = inStr.toLowerCase();

        if (DEBUG) {
            System.err.println("Ticker.processTickCommand received: " + inStr);
        }

        // String retStr = inStr;
        String retStr;

        // if (inStr.equalsIgnoreCase("/tick")) {
        // if (lowerCmd.equals("/tick")) {
        if ("/tick".equals(lowerCmd)) {
            showTimeRemaining();
            validCommand = true;
        }

        // if (inStr.equalsIgnoreCase("/tickon") || inStr.equalsIgnoreCase("/tickset")) {
        // if (lowerCmd.equals("/tickon") || lowerCmd.equals("/tickset")) {
        if ("/tickon".equals(lowerCmd) || "/tickset".equals(lowerCmd)) {
            // Start the current ticker
            startTicker();
            validCommand = true;
        }

        // if (inStr.equalsIgnoreCase("/tickoff")) {
        // if (lowerCmd.equals("/tickoff")) {
        if ("/tickoff".equals(lowerCmd)) {
            stopTicker();

            validCommand = true;
        }

        if (lowerCmd.startsWith("/ticksize")) {
            validCommand = processTickSizeCmd(inStr);
//            // This should be moved into its own method
//            final int space = lowerCmd.indexOf(' ');
//            if (DEBUG) {
//                System.err.println("Ticker.processTickCommand ticksize space at location " + space);
//            }
//
//            try {
//                final int value = Integer.parseInt(inStr.substring(space).trim());
//                if (DEBUG) {
//                    System.err.println("Size to set: " + value);
//                }
//                setTickSize(value);
//                validCommand = true;
//                final MuSocket muck = CHandler.getInstance().getActiveMUHandle();
//                if (DEBUG) {
//                    System.err.println("Writing out to MU*.");
//                }
//
//                if (value < 1) {
//                    stopTicker();
//                }
//
//                if (muck != null) {
//                    muck.write("Ticker now set to " + value + " second intervals.\n");
//                    //CHandler.getInstance().sendText("Ticker now set to " + value + " second intervals.\n", muck);
//                }
//            } catch (Exception exc) {
//                if (DEBUG) {
//                    System.err.println("Error parsing the integer provided " + exc);
//                }
//                final MuSocket muck = CHandler.getInstance().getActiveMUHandle();
//                muck.write("Ticker does not understand the size \"" + inStr.substring(space).trim() + "\".\n");
//                // CHandler.getInstance().sendText("Ticker does not understand the size \"" + inStr.substring(space).trim() + "\".\n", muck);
//            }

        }

        if (validCommand) {
            retStr = null;
        } else {
            retStr = inStr;
        }

        if (DEBUG) {
            System.err.println("Ticker.processTickCommand is valid command? " + validCommand);
            System.err.println("Returning string: " + retStr);
        }

        return retStr;

    }

    /**
     * This method attempts to process the size of the tick supplied by the
     * user via the /ticksize command.
     * @param inStr The entire /ticksize string including command
     * @return <code>true</code> - This is a valid tick size
     *         <code>false</code> - This is not a valid tick size and is ignored
     */
    private boolean processTickSizeCmd(final String inStr) {
        boolean validCommand = false;

        final int space = inStr.indexOf(' ');

        if (DEBUG) {
            System.err.println("Ticker.processTickCommand ticksize space at location " + space);
        }

        try {
            final int value = Integer.parseInt(inStr.substring(space).trim());
            if (DEBUG) {
                System.err.println("Size to set: " + value);
            }
            setTickSize(value);
            validCommand = true;
            final MuSocket muck = CHandler.getInstance().getActiveMUHandle();
            if (DEBUG) {
                System.err.println("Writing out to MU*.");
            }

            if (value < 1) {
                stopTicker();
            }

            if (muck != null) {
                muck.write("Ticker now set to " + value + " second intervals.\n");
            //CHandler.getInstance().sendText("Ticker now set to " + value + " second intervals.\n", muck);
            }
        } catch (Exception exc) {
            if (DEBUG) {
                System.err.println("Error parsing the integer provided " + exc);
            }
            final MuSocket muck = CHandler.getInstance().getActiveMUHandle();
            muck.write("Ticker does not understand the size \"" + inStr.substring(space).trim() + "\".\n");
        // CHandler.getInstance().sendText("Ticker does not understand the size \"" + inStr.substring(space).trim() + "\".\n", muck);
        }

        return validCommand;
    }

    /**
     * Start the ticker and give feedback to the user
     */
    private void startTicker() {
        if (DEBUG) {
            System.err.println("Ticker.startTicker.");
        }

        Tick tempTick = getTickHandle();
        // final MuSocket muck = CHandler.getInstance().getActiveMUHandle();

        if (tempTick == null) {
            tempTick = createNewTicker();
        }

        if (DEBUG) {
            System.err.println("Ticker.startTicker setting start time.");
        }

        tempTick.setStartNow();
        if (DEBUG) {
            System.err.println("Ticker.startTicker starting ticker.");
        }

        startTick(tempTick);

    }

    /**
     * Start the thread, checking for instances of dead threads first
     * @param tempTick The tick to start
     */
    private void startTick(Tick tempTick) {

        // Check to see if this thread is still alive
        if (!tempTick.isLive()) {
            if (DEBUG) {
                System.err.println("Ticker.startTick indicates existing thread is dead.  We'll create a new one.");
            }
            /* This thread is dead.
             * We'll get rid of the old thread and create a new one
             * with the same settings
             */
            final int interval = tempTick.getDeadTickSize();
            ticks.removeElement(tempTick);
            tempTick = createNewTicker();
            tempTick.setSize(interval);
        }

        // Set the start time to now
        tempTick.setStartNow();
        // Start the timer.
        tempTick.start();

        final MuSocket muck = CHandler.getInstance().getActiveMUHandle();
        muck.write("Ticker started with a " + tempTick.getTickSize() + " second interval.\n");

    }

    /**
     * Create a new tick
     * @return The new tick created
     */
    private Tick createNewTicker() {
        final MuSocket muck = CHandler.getInstance().getActiveMUHandle();
        if (DEBUG) {
            System.err.println("Ticker.startTicker creating new ticker.");
        }
        // We do not have a ticker for this connect yet.  Set one up.
        final Tick tempTick = new Tick();
        tempTick.setMU(muck);
        ticks.addElement(tempTick);

        return tempTick;
    }

    /**
     * Stop the ticker and give feedback to the user
     */
    private void stopTicker() {
        if (DEBUG) {
            System.err.println("Ticker.stopTicker.");
        }

        final Tick tempTick = getTickHandle();

        tempTick.endThread();

        final MuSocket muck = CHandler.getInstance().getActiveMUHandle();
        muck.write("Ticker stopped.\n");

    }

    /**
     * Report the time remaining until the next tick to the user
     */
    public void showTimeRemaining() {

        // Show the tick time remaining for the currently active MU*
        final MuSocket muck = CHandler.getInstance().getActiveMUHandle();
        final Tick tempTick = getTickHandle();

        if (muck == null) {
            // We don't have anything to write messages out to!
            return;
        }

        // This is how far in we are
        if (null == tempTick || !activated || tempTick.getTickSize() < 1) {
            if (DEBUG) {
                System.err.println("Ticker.showTimeRemaining not active.");
                if (null == muck) {
                    System.err.println("Muck is returned as null");
                } else {
                    System.err.println("Our active MU* is " + muck);
                }
            }

            muck.write("The ticker is currently not active.");

            if (null == tempTick) {
                // muck.write("  " + tempTick.getDeadTickSize() + " second intervals currently set.\n");
                // Supply feedback on this?  Fix Me XXX
                muck.write("  Standard interval is 75 seconds.\n");


            } else {
                muck.write("  " + tempTick.getDeadTickSize() + " second intervals currently set.\n");
            }

            if (DEBUG) {
                System.err.println("Wrote not active output.");

            }
        } else {
            if (DEBUG) {
                System.err.println("Ticker.showTimeRemaining calling getTimeRemaining.");
            }

            final int remaining = tempTick.getTimeRemaining();

            muck.write(remaining + " seconds until next tick.\n");
            if (DEBUG) {
                System.err.println("Ticker.showTimeRemaining wrote time.");
            }

        }

    }

    /** This method is called by the &quot;Properties" option in
     * the JamochaMUD &quot;Manage plugins" dialogue box.
     * Any settings that you wish the user to have access to
     * can be configured through this method
     */
    public void plugInProperties() {
    // This plug-in has no properties that can be defined by GUI (yet).
    }

    /** This method is called as soon as the plugin is first loaded.
     * It is useful for loading or configuring anything needed by
     * the plug-in at start-up (such as loading settings).
     * This method is only called when JamochaMUD first loads the
     * plug-in, even if the plug-in is not currently enabled.
     */
    public void initialiseAtLoad() {
    // This program has no settings to initialise
    }

    /** The method is automatically called when JamochaMUD is quit
     * by using the File -> Exit menu or the close window icon.
     * This method will not be called if JamochaMUD is &quot;killed"
     * or crashes.
     * This section is useful for writing any settings back to disk.
     */
    public void setAtHalt() {
    // No clean-up is required for this plug-in
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
        activated = true;

    // Start the counting thread.  Fix Me XXX

    }

    /** This function is automatically called by JamochaMUD when the
     * plug-in is set inactive from the JamochaMUD &quot;Manage Plug-Ins"
     * dialogue.  With this method, you can easily deactive items that may have
     * initially been setup by the <tt>Activate</tt> method (such as removing
     * listeners, etc.).
     */
    public void deactivate() {

        activated = false;

        final int tickCount = ticks.size();
        Tick tempTick;

        // Stop every tick in our list
        for (int i = 0; i < tickCount; i++) {
            tempTick = (Tick) ticks.elementAt(i);
            tempTick.endThread();

        }

        ticks.removeAllElements();

    }

    /**
     * 
     * @return 
     */
    public boolean isActive() {
        return activated;
    }
    /** Monitor's this plug-ins activated state */
    private transient boolean activated;
    /** The vector containing the ticks for each connection */
    private transient final Vector ticks = new Vector();
    /** Enable and disable debugging output */
    private static final boolean DEBUG = false;
}
