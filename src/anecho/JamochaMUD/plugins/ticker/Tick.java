/* JamochaMUD, a Muck/Mud client program
 * Copyright (C) 1998-2008  Jeff Robinson
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
package anecho.JamochaMUD.plugins.ticker;

import anecho.JamochaMUD.MuSocket;

/**
 *
 * This class maintains all the tick information for a specific MU*.
 * A tick class is assigned to each MU* to keep track of warnings, ticksizes, etc.
 * 
 * @author Jeff Robinson
 * @version $Id: Tick.java,v 1.9 2010/03/22 02:48:23 jeffnik Exp $
 */
public class Tick extends Thread {

    /**
     * Sets the MuSocket that this tick is associated with
     * @param inMuck The MuSocket associated with this Tick
     */
    public void setMU(final MuSocket inMuck) {
        muck = inMuck;
    }

    /**
     * 
     * @return
     */
    public MuSocket getMU() {
        return muck;
    }

    /**
     * 
     * @param startTime
     */
    public void setStart(final long startTime) {
        final long tickStart = startTime;
        nextTick = tickStart + (tickSize * 1000);
        setTickWarn();  // Set the time for the next warning
    }

    /**
     * 
     */
    public void setStartNow() {
        setStart(System.currentTimeMillis());
    }

    /**
     * Set the size (duration) of the tick in seconds
     * @param size Duration of the tick in seconds.
     */
    public void setSize(final int size) {
        tickSize = size;
        if (DEBUG) {
            System.err.println("Tick.setSize set to " + size);
        }
    }

    /**
     * This method should "safely" end the thread by changing our running
     * variable to false.
     */
    public void endThread() {
        running = false;
        live = false;
    }

    /**
     * Returns the size (duration) of the tick
     * @return Size of tick in seconds
     */
    public int getTickSize() {
        // int retSize = -1;
        int retSize;
        
        if (live) {
            retSize = tickSize;
        } else {
            retSize = -1;
        }

        return retSize;
    }

    /**
     * This method is for returning the tick size of a dead thread.
     * Thie method should only be used for getting the size of the previous thread.
     * @return The tick size of the previously running thread.
     */
    public int getDeadTickSize() {
        return tickSize;
    }

    /**
     * Set next tick based on last tick
     */
    private synchronized void setNextTick() {
        if (DEBUG) {
            System.err.println("Tick.setNextTick()");
        }

        if (tickSize < 1) {
            if (DEBUG) {
                System.err.println("Tick.setNextTick() tickSize is a negative number.  Stopping.");
            }

            // We can't increment the next tick, so we'll set that we've stopped
            running = false;
            return;
        }

        final long tempTime = System.currentTimeMillis();

        while (nextTick < tempTime) {
            nextTick = nextTick + (tickSize * 1000);
        }

//        // Set-up the new tick warning time and reset the tickWarnSent
//        if (tickSize >= 20) {
//            tickWarn = nextTick - 10000;
//            tickWarnSent = false;
//        } else {
//            tickWarnSent = true;
//        }

        setTickWarn();
        
        if (DEBUG) {
            System.err.println("Tick.setNextTick() exiting.");
        }

    }

    /**
     * Sets the time when the tick warning is set to the user.
     * If the ticksize is less than 20 then a tick warning is not issued
     */
    private void setTickWarn() {
            // Set-up the new tick warning time and reset the tickWarnSent
        if (tickSize >= 20) {
            tickWarn = nextTick - 10000;
            tickWarnSent = false;
        } else {
            tickWarnSent = true;
        }
    
    }
    
    /** Send the tick message to the output owned by the MU*
     * 
     */
    private void sendTickMessage() {
        if (DEBUG) {
            System.err.println("Tick.sendTickMessage sending visual tick reminder: " + TICKMSG + " to " + muck.getMUName());
        }
        if (muck != null) {
        muck.write(TICKMSG);
        // CHandler.getInstance().sendText(TICKMSG, muck);
        }
        
        if (DEBUG) {
            System.err.println("Tick.sendTickMessage has been sent.");
        }
    }

    /**
     * Send the tick warning message to the output owned by the MU*
     */
    private void sendTickWarningMessage() {
        if (muck != null) {
            muck.write(TICKWARNMSG);
        }
    }

    /**
     * Returns the number of seconds remaining before the next tick
     * @return Number of seconds
     */
    public int getTimeRemaining() {
        if (nextTick < 1) {
            setNextTick();
        }
        
        /* Set the return value for the full tick-size in the event the ticker
         * isn't running */
        // int retVal = tickSize;
        int retVal;
        
        if (running) {
            final long longRemain = nextTick - System.currentTimeMillis();
            retVal = (int) (longRemain / 1000);
        } else {
            retVal = tickSize;
        }
       
        return retVal;
    }

    /**
     * Indicates whether this thread is live or not.  A thread may be live but
     * not running if the thread has been created but not yet started.
     * @return <code>true</code> - this thread is alive
     * <code>false</code> - this thread is no long alive
     */
    public boolean isLive() {
        return live;
    }

    /** This is our main method that handles the ticking
     * 
     */
    public void run() {

        if (running) {
            // Our thread is already running
            if (DEBUG) {
                System.err.println("Tick.run - thread is already running.");
            }
            return;
        }

        running = true;
        long checkTime;

        while (running) {
            if (tickSize < 1) {
                if (DEBUG) {
                    System.err.println("Tick.run exiting 'while' loop due to tickSize < 1.");
                }
                // We don't have a tick-size so we can't start.  We should
                // probably give some feedback to the user.  Fix Me XXX
                running = false;
                break;
            }

            // Check for ticks
            checkTime = System.currentTimeMillis();

            // Check for 10 second warning.
            if (!tickWarnSent && checkTime >= tickWarn) {
                sendTickWarningMessage();
                tickWarnSent = true;
            }

            // Check for real tick
            if (checkTime >= nextTick) {
                setNextTick();

                sendTickMessage();

            }

            try {
                if (DEBUG) {
                    System.err.println("Tick.run sleeping for 500 ms.");
                }

                sleep(500);
            } catch (Exception exc) {
                if (DEBUG) {
                    System.err.println("Tick.run error " + exc);
                }

                running = false;
                live = false;
            }
        }
        
        // We have fallen through.  Ensure everything is "turned off"
        running = false;
        live = false;
    }
    
    /** The MU* that this tick belongs to */
    private transient MuSocket muck;
    /** The start time of this tick */
    // private long tickStart = 0;
    /** The time of the next tick */
    private transient long nextTick = 0;
    /** The 10 second warning before the tick.  This will be calculated at each new tick */
    private transient long tickWarn = 0;
    /** Indicates if a tickWarn message should be sent out */
    private transient boolean tickWarnSent = false;
    /** The duration of each tick.  75 seconds is the default tick */
    private transient int tickSize = 75;
    // private int tickSize = 10;
    /** Whether this thread is running or not */
    private transient boolean running = false;
    /** The tick warning message */
    private static final transient String TICKWARNMSG = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/plugins/ticker/Bundle").getString("Tick_in_10_seconds");
    /** The tick message */
    private static final transient String TICKMSG = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/plugins/ticker/Bundle").getString("*TICK*");
    /** Enables and disables debugging information */
    private static final boolean DEBUG = false;
    /** Used to monitor if the thread is alive.
     * A thread can be new but not running, 
     * so live tracks true state */
    private transient boolean live = true;
}
