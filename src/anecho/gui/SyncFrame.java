/**

 * The JamochaMUD Frame synchroniser:

 * SyncFrame.java handles the synchronising of

 * frame minimize/maximize, and potentially, movement events

 * $Id: SyncFrame.java,v 1.6 2008/09/27 16:33:48 jeffnik Exp $
 */



/* JamochaMUD, a Muck/Mud client program

 * Copyright (C) 1998-2005  Jeff Robinson

 *

 * This library is free software; you can redistribute it and/or

 * modify it under the terms of the GNU Lesser General Public

 * License as published by the Free Software Foundation; either

 * version 2.1 of the License, or (at your option) any later version.

 *

 * This library is distributed in the hope that it will be useful,

 * but WITHOUT ANY WARRANTY; without even the implied warranty of

 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU

 * Lesser General Public License for more details.

 *

 * You should have received a copy of the GNU Lesser General Public

 * License along with this library; if not, write to the Free Software

 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

 *

 */



package anecho.gui;



import java.awt.*;

import java.awt.event.*;



// import anecho.gui.SyncFrameGroup;



/**

 * The JamochaMUD Frame synchroniser:

 * SyncFrame.java handles the synchronising of

 * frame minimize/maximize, and potentially, movement events

 * @version $Id: SyncFrame.java,v 1.6 2008/09/27 16:33:48 jeffnik Exp $
 * @author Jeff Robinson

 */

public class SyncFrame extends Frame {

    

    // Variables for the frame

    private SyncFrameGroup frameGroup;		/* The group for this frame */

    private boolean inSync = false;		/* The frame's synchronisation status */

    private Point compPos;			/* A comparison location to judge

     *  movement of the frame */

    private boolean endProgram = false;		/* Closing of this frame terminates JVM */

    private boolean closeFrame = false;       /* Close the frame via the "close" icon */

    private boolean locked = false;		/* Lock status of this frame (for use

     * with SyncFrameGroup */

    private static final boolean DEBUG = false;

    

    /**

     * Creates a new SyncFrame.
     * @param title The title of our Frame.
     */

    public SyncFrame(String title) {

        this(title, null);

    }

    

    /**

     * Creates a new SyncFrame.
     * @param title The title of our Frame.
     * @param group The SyncFrameGroup that this SyncFrame will belong to.
     */

    public SyncFrame(String title, SyncFrameGroup group) {

        this(title, false, group);

    }

    

    /**

     * Creates a new SyncFrame.
     * @param title The title of our Frame.
     * @param sync true - This SyncFrame is the be synchronised with the other SyncFrames in the group
     * false - This SyncFrame is to not be synchronised with its given SyncFrameGroup.
     * @param group The SyncFrameGroup that this SyncFrame will belong to.
     */

    public SyncFrame(String title, boolean sync, SyncFrameGroup group) {

        super(title);

        if (group != null) {

            this.frameGroup = group;

            frameGroup.add(this);

            

            // group.add(this);

        }

        

        inSync = sync;

        compPos = new Point(0, 0);

        

        enableEvents(AWTEvent.WINDOW_EVENT_MASK);

        enableEvents(AWTEvent.COMPONENT_EVENT_MASK);

    }

    

    /**
     * Set the synchronisation of this frame
     * @param sync <CODE>true</CODE> - This frame is to be synchronised with its SyncFrameGroup.
     * <CODE>false</CODE> - This frame is not to be synchronised with its SyncFrameGroup.
     */

    public void setSync(final boolean sync) {

        inSync = sync;

    }

    

    /**
     * Set the synchronisation of the entire frame group
     * @param sync <CODE>true</CODE> - Set all frames in this group to be synchronized
     * <CODE>false</CODE> - Set all frames in this group to not be synchronized
     */

    public void setGroupSync(final boolean sync) {

        // Go through the Syncframes of this group and set their status

        // if (frameGroup == null) return;

        if (DEBUG) {

            System.err.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("SyncFrame.setGroupSync(boolean)_has_not_be_implimented"));

        }

    }

    

    /**

     * Get the synchronisation status of this frame

     * @return	returns the boolean state of this SyncFrame's

     *			synchronisation status

     * @see 	gui.SyncFrame#setSync

     */

    public boolean isSynced() {

        return inSync;

    }

    

    /**
     * Set the SyncFrameGroup for this frame
     * @param group The SyncFrameGroup that this SyncFrame is to become a member of.
     */

    public void setSyncFrameGroup(final SyncFrameGroup group) {

        frameGroup = group;

    }

    

    /** Set the "termination state" of this frame (whether it kills the JVM or not)

     *@param	state	A <code>true</code> state indicates that closing

     *				this SyncFrame will terminate the JVM.

     *			A <code>false</code> indicates this SyncFrame will

     *				simply terminate itself, leaving the JVM to run

     */

    public void setTerminationState(final boolean state) {

        endProgram = state;

    }

    

    /** Report the "termination state" of this frame

     * @return	A <code>true</code> state indicates that the closing

     *			of this frame will terminate the JVM,

     *			a <code>false</code> will simply close the frame

     */

    public boolean isTerminationState() {

        return endProgram;

    }

    

    /** Set the frame to close when the &quot;close" icon of the frame is activated

     * @param   state     A <code>true</code> closes the appropriate frame.

     *                    A <code>false</code> does not close the frame when activate

     */

    public void setCloseState(final boolean state) {

        closeFrame = state;

    }

    

    /**
     * Report the &quot;Close state" of this frame.
     * @return 
     */

    public boolean isCloseState() {

        return closeFrame;

    }

    

    // WINDOW EVENTS

    /**

     * 

     * @param event 

     */

    public void processWindowEvent(final WindowEvent event) {

        super.processWindowEvent(event); // Handle listeners

        

        if (event.getID() == WindowEvent.WINDOW_CLOSING) {

            super.processWindowEvent(event);

            if (endProgram) {

                // Now we kill the JVM

                System.exit(0);

            }

            

            if (closeFrame) {

                this.setVisible(false);

                this.dispose();

            }

        }

        

        if (event.getID() == WindowEvent.WINDOW_ICONIFIED) {

            hideComponents();

            

            if (frameGroup != null && this.isLocked()) {

                this.setLock(false);

                // return;

            }

            

            // hideComponents();

        }

        

        if (event.getID() == WindowEvent.WINDOW_DEICONIFIED) {

            showComponents();

            if (frameGroup != null && this.isLocked()) {

                this.setLock(false);

                // return;

            }

            

        }

        

//        if (event.getID() == WindowEvent.WINDOW_ACTIVATED) {

//        }

        

    }

    

    /**

     * 

     * @param event 

     */

    public void processComponentEvent(final ComponentEvent event) {

        super.processComponentEvent(event); // Handle listeners

        

        if (event.getID() == ComponentEvent.COMPONENT_MOVED) {

            moveComponents(event);

            if (frameGroup != null && this.isLocked()) {

                this.setLock(false);

                return;

            }

        }

        

        if (event.getID() == ComponentEvent.COMPONENT_RESIZED) {

            super.processComponentEvent(event); // Handle listeners

        }

        

    }

    

    /**

     * Handle the calculation of how far the active frame

     * has moved, and adjust the rest of the group accordingly

     */

    private void moveComponents(final ComponentEvent event) {

        // Our component has to be visible.  Newer versions of Java

        // get very cranky about this feature

        if (compPos == null || !this.isVisible() || !inSync) {

            // System.out.println("Checking for visibility (" + this.isVisible() + ") and compPos " + compPos);

            // System.out.println("We failed on " + this.getTitle());

            return;

        }

        

        // First, we check to see if this frame is sync'd.

        // If not, then we don't need to check the rest of this method

//        if (!inSync) {

//            return;

//        }

        

        // Original position of the component

        final int xOrg = compPos.x;

        final int yOrg = compPos.y;

        // New position of the component

        final int xNew = event.getComponent().getLocationOnScreen().x;

        final int yNew = event.getComponent().getLocationOnScreen().y;

        

        // Now we'll set the new comparison position

        compPos.setLocation(xNew, yNew);

        

        // Cycle through all the SyncFrames in this group and apply the position change

        if (frameGroup != null) {

            frameGroup.moveFrames(this, (xNew - xOrg), (yNew - yOrg));

        }

    }

    

    /** Hide all the associated frames if in sync */

    private void hideComponents() {

        // if (!inSync) return;

        if (inSync && frameGroup != null) {

            // if (frameGroup != null) {

            frameGroup.changeState(this, SyncFrameGroup.HIDE);

            // }

        }

    }

    

    /** Show all the associated frames if in sync */

    private void showComponents() {

        // if (!inSync) return;

        

        if (inSync && frameGroup != null) {

            //	if (frameGroup != null) {

            frameGroup.changeState(this, SyncFrameGroup.SHOW);

            //	}

        }

    }

    

    /** Set the state of all the frames in this group to be the same

     * @param state	If <code>true</code>, all the frames in

     *				the group will be synchronised

     *			If <code>false</code> the frames will act

     *				independantly of each other.

     */

    public void setAllStates(final boolean state) {

        inSync = state;

        

        if (frameGroup != null) {

            if (state) {

                frameGroup.changeState(this, SyncFrameGroup.SET_ALL_TRUE);

            } else {

                frameGroup.changeState(this, SyncFrameGroup.SET_ALL_FALSE);

            }

        }

    }

    

    // Overridden methods of Frame

    /**
     * This method is overridden to keep us up to date with the component's location
     * @param newX New X-axis position for this frame.
     * @param newY New Y-axis position for this frame.
     * @param width New width for this frame
     * @param height Newheight for this frame
     */

    public void setBounds(final int newX, final int newY, final int width, final int height) {

        // Grab the dimension information

        

        compPos.setLocation(newX, newY);

        super.setBounds(newX, newY, width, height);

    }

    

    /**
     * setBounds is overridden to keep us up to date with the component's location
     * @param bounds New position and size for this component
     */

    public void setBounds(final Rectangle bounds) {

        compPos.setLocation(bounds.x, bounds.y);

        super.setBounds(bounds);

    }

    

    /**
     * setLocation is overridden to keep us up to date with the component's location
     * @param newX New X-axis position for this frame.
     * @param newY New Y-axis position for this frame.
     */

    public void setLocation(final int newX, final int newY) {

        if (compPos == null) {

            return;

        }

        compPos.setLocation(newX, newY);

        super.setLocation(newX, newY);

    }

    

    /**
     * setBounds is overridden to keep us up to date with the component's location
     * @param newLoc New position for this component
     */

    public void setLocation(final Point newLoc) {

        if (compPos == null) {

            return;

        }

        compPos.setLocation(newLoc);

        super.setLocation(newLoc);

    }

    

    /** dispose of public class Window is handled to remove this frame from it's *

     * registered SyncFrameGroups first before letting the super-class handle the rest */

    public void dispose() {

        super.dispose();

        if (frameGroup != null) {

            frameGroup.remove(this);

        }

    }

    

    /** A paramString so that other classes can identify individual SyncFrames.

     * @return	The name of the class, plus the name of the class

     */

    public String paramString() {

        final String retName = getClass() + java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString(",_") + getName();

        // return new String(getClass() + ", " + getName());

        return retName;

    }

    

    /** Sets the "lock" status of this frame.  This is needed when dealing with

     * SyncFrameGroups to prevent "stuttering".  A SyncFrame becomes "locked"

     * <code>true</code> by default if another SyncFrame is moved, hidden, or shown.

     * Without the lock, certain OSes activate the other SyncFrame's events after the

     * original event has been completed, making terrible loops.  Once a locked frame has

     * received an event, it is unlocked (set <code>false</code>) again.

     * @see	gui.SyncFrameGroup

     * @param	status	the SyncFrame's lock status

     */

    public synchronized void setLock(final boolean status) {

        locked = status;

    }

    

    /** Returns the "lock" state of this frame.

     * @see 	gui.SyncFrame#setLock

     * @return	the state of the lock as a boolean variable

     */

    public synchronized boolean isLocked() {

        return locked;

    }

}

