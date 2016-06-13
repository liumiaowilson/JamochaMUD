/**
 * The JamochaMUD Frame synchroniser:
 * SyncFrame.java handles the synchronising of
 * frame minimize/maximize, and potentially, movement events
 * $Id: JSyncFrame.java,v 1.4 2008/07/26 19:21:03 jeffnik Exp $
 */

/* JamochaMUD, a Muck/Mud client program
 * Copyright (C) 1998-2004  Jeff Robinson
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

import javax.swing.JFrame;

// import anecho.gui.SyncFrameGroup;
/**
 * The JamochaMUD Frame synchroniser:
 * SyncFrame.java handles the synchronising of
 * frame minimize/maximize, and potentially, movement events
 * @version $Id: JSyncFrame.java,v 1.4 2008/07/26 19:21:03 jeffnik Exp $
 * @author Jeff Robinson
 */
// public class SyncFrame extends Frame {
public class JSyncFrame extends JFrame {

    /** The group for this frame */
    private transient SyncFrameGroup frameGroup;
    /** The frame's synchronisation status */
    private transient boolean inSync = false;
    /** A comparison location to judge movement of the frame */
    private transient Point compPos;
    /** Closing of this frame terminates JVM */
    private transient boolean endProgram = false;
    /** Close the frame via the "close" icon */
    private transient boolean closeFrame = false;
    /** Lock status of this frame (for use with SyncFrameGroup) */
    private transient boolean locked = false;
    private static final boolean DEBUG = false;

    /**
     * The JamochaMUD Frame synchroniser:
     * JSyncFrame.java handles the synchronising of
     * Swing frame minimize/maximize, and potentially, movement events
     * @param title The title to be displayed on this frame component
     */
    public JSyncFrame(String title) {
        this(title, null);
    }

    /**
     *  The JamochaMUD Frame synchroniser:
     * SyncFrame.java handles the synchronising of
     * frame minimize/maximize, and potentially, movement events
     * @param title The title to be displayed on this frame component.
     * @param group The group that this JSyncFrame is to be part of.
     */
    public JSyncFrame(String title, SyncFrameGroup group) {
        this(title, false, group);
    }

    /**
     *   The JamochaMUD Frame synchroniser:
     * SyncFrame.java handles the synchronising of
     * frame minimize/maximize, and potentially, movement events
     * @param title The title to be displayed on this frame component
     * @param sync Indicates whether this frame is to sync with other frames in this group.
     * <CODE>true</CODE> - indicates synchronisation should occur
     * <CODE>false</CODE> - indicates this frame acts independently of other frames in the SyncGroup
     * @param group The SyncGroup this frame belongs to
     */
    public JSyncFrame(String title, boolean sync, SyncFrameGroup group) {
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
     *  The JamochaMUD Frame synchroniser:
     * SyncFrame.java handles the synchronising of
     * frame minimize/maximize, and potentially, movement events
     * @param sync Indicates whether this frame is to sync with other frames in this group.
     * <CODE>true</CODE> - indicates synchronisation should occur
     * <CODE>false</CODE> - indicates this frame acts independently of other frames in the SyncGroup
     */
    public void setSync(final boolean sync) {
        if (DEBUG) {
            System.err.println(this.getTitle() + java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("_frame_has_been_setSync:_") + sync);
        }

        inSync = sync;
    }

    /**
     * Set the synchronisation of the entire frame group.  Not finished.  Fix Me XXX
     * @param sync <CODE>true</CODE> - Synchronise this frame with the rest of the group
     * <CODE>false</CODE> - this frame should act independently of the other frames
     */
    public void setGroupSync(final boolean sync) {
        // Go through the Syncframes of this group and set their status
        if (frameGroup == null) {
            return;
        }
    }

    public boolean isGroupSync() {
        // Not finished.  Fix Me XXX
        return false;
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
     * @param group This is the group this frame should belong to.
     */
    public void setSyncFrameGroup(final SyncFrameGroup group) {
        frameGroup = group;
    }

    public SyncFrameGroup getSyncFrameGroup() {
        return frameGroup;
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
    public boolean isTerminated() {
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
     * Report the "Close state" of this frame.
     * @return <CODE>true</CODE> - This frame should close when it is "x"'d out
     * <CODE>false</CODE> - This frame should not close when "x"'d out
     */
    public boolean isCloseState() {
        return closeFrame;
    }

    // WINDOW EVENTS
    /**
     * This method handles the closing and iconifying of the frame, calling
     * other frames in this group if necessary.
     * @param event Our WindowEvent
     */
    @Override
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
                return;
            }

        // hideComponents();
        }

        if (event.getID() == WindowEvent.WINDOW_DEICONIFIED) {
            showComponents();
            if (frameGroup != null && this.isLocked()) {
                this.setLock(false);
                return;
            }

        }

//            if (event.getID() == WindowEvent.WINDOW_ACTIVATED) {
//            }

    }

    /**
     * This method processes Component Events, letting other frames in the
     * group know if they have changed size or moved.
     * @param event Our ComponentEvent.
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
//            if (!inSync) {
//                return;
//            }

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

        // if (frameGroup != null) {
        if (inSync && frameGroup != null) {
            frameGroup.changeState(this, SyncFrameGroup.HIDE);
        }
    }

    /** Show all the associated frames if in sync */
    private void showComponents() {
        // if (!inSync) return;

        //if (frameGroup != null) {
        if (inSync && frameGroup != null) {
            frameGroup.changeState(this, SyncFrameGroup.SHOW);
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
     * This method is overridden to keep us up to date with the component's
     * location
     * @param newX New X-axis position for this frame.
     * @param newY New Y-axis position for this frame.
     * @param width Width of the component
     * @param height Height of this component
     */
    public void setBounds(final int newX, final int newY, final int width, final int height) {
        // Grab the dimension information

        compPos.setLocation(newX, newY);
        super.setBounds(newX, newY, width, height);
    }

    /**
     * setBounds is overridden to keep us up to date with the component's 
     * location
     * @param bounds The position and size of this frame
     */
    public void setBounds(final Rectangle bounds) {
        compPos.setLocation(bounds.x, bounds.y);
        super.setBounds(bounds);
    }

    /**
     * setLocation is overridden to keep us up to date with the component's 
     * location
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
     * setBounds is overridden to keep us up to date with the component's 
     * location
     * @param newLoc New position for this frame.
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
        final String retStr = getClass() + java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString(",_") + getName();
        return retStr;
    // return new String(getClass() + ", " + getName());
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
