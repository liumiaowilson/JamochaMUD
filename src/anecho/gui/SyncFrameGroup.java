/**
 *
 * The JamochaMUD Frame synchroniser:
 * SyncFrameGroup.java manages (obviously) groups of SyncFrames
 * $Id: SyncFrameGroup.java,v 1.7 2014/05/20 02:18:31 jeffnik Exp $
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

// import java.awt.Component;
import java.awt.Frame;
import java.awt.Point;
import java.util.Vector;

// import anecho.gui.SyncFrame;
/**
 *
 * The JamochaMUD Frame Group handler:
 * SyncFrame.java handles the synchronising of
 * frame minimize/maximize, and potentially, movement events.
 * This class coordinates the SyncFrames in each group.
 * @version $Id: SyncFrameGroup.java,v 1.7 2014/05/20 02:18:31 jeffnik Exp $
 * @author Jeff Robinson
 *
 */
public class SyncFrameGroup {

	// VARIABLES
    final private transient Vector groupFrames = new Vector(0, 1);	// The vector of all the frames in this group
    /**
     * A variable representing the "Show" option for the frame state.
     */
    public static final int SHOW = 0;

    /**
     * A variable representing the "hide" option for the frame state.
     */
    public static final int HIDE = 1;

    public static final int SET_ALL_TRUE = 2;

    public static final int SET_ALL_FALSE = 3;

	// private boolean locked = false;	// Set the lock false.  When true, this group
        // of frames will not respond to movement, etc.
    private static final boolean DEBUG = false;

	// CONSTRUCTORS
    /**
     * Create a SyncFrameGroup
     */
    public SyncFrameGroup() {
    }

	// METHODS
    /**
     * Add a new SyncFrame to the group
     *
     * @param newFrame A SyncFrame to be added to the SyncFrame group.
     */
    public void add(final Frame newFrame) {

        groupFrames.addElement(newFrame);

		// groupFrames.addElement(newFrame.paramString());
    }

    /**
     * Remove a SyncFrame from the group
     *
     * @param origin The given SyncFrame to be removed from the SyncFrame group.
     */
        // public void remove(SyncFrame origin) {
    public void remove(final Frame origin) {

        try {

            groupFrames.removeElement(origin);

        } catch (Exception e) {

            System.out.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("SyncFrameGroup.remove_error_") + e);

        }

    }

    /**
     * Move all the frames in this group with the exception of the "origin"
     * frame
     *
     * @param origin The frame that originated the move request.
     * @param xMove The relative number of pixels to move the frames
     * horizontally.
     * @param yMove The relative number of pixels to move the frames vertically.
     */
    public void moveFrames(final Frame origin, final int xMove, final int yMove) {

        String tempName;

        final int length = groupFrames.size();

        for (int i = 0; i < length; i++) {

                // tempName = new String(((SyncFrame)groupFrames.elementAt(i)).paramString());
            tempName = groupFrames.elementAt(i).toString();

                // System.out.println("Attempt to move " + tempName);
                // if (!origin.paramString().equals(tempName) && ((SyncFrame)groupFrames.elementAt(i)).isSynced()) {
            if (!origin.toString().equals(tempName) && ((SyncFrame) groupFrames.elementAt(i)).isSynced()) {

                try {

                        // We'll move this component
                    final Point pos = ((SyncFrame) groupFrames.elementAt(i)).getLocationOnScreen();

                    ((SyncFrame) groupFrames.elementAt(i)).setLocation(pos.x + xMove, pos.y + yMove);

                    ((SyncFrame) groupFrames.elementAt(i)).setLock(true);

                        // System.out.println("Move a success for " + tempName);
                } catch (Exception e) {

                    if (DEBUG) {

                        System.out.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("FrameGroup_exception_in_moving_") + e);

                    }

                }

            }

        }

    }

    /**
     * A common loop using switches
     *
     * @param origin The frame that originated the change state request.
     * @param action The action to be performed by the frames, such as HIDE.
     */
    public synchronized void changeState(final Frame origin, final int action) {

        String tempName;

        final int length = groupFrames.size();

        for (int i = 0; i < length; i++) {

            tempName = groupFrames.elementAt(i).toString();

            if (!origin.toString().equals(tempName)) {

                try {

                    ((SyncFrame) groupFrames.elementAt(i)).setLock(true);

                    switch (action) {

                        case SHOW: {

                            if (((SyncFrame) groupFrames.elementAt(i)).isSynced()) {

                                if (DEBUG) {
                                    System.err.println(groupFrames.elementAt(i) + java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("_frame_is_synced_and_will_be_made_visible."));
                                }

                                ((SyncFrame) groupFrames.elementAt(i)).setVisible(true);

                            }

                            break;

                        }

                        case HIDE: {

                            if (((SyncFrame) groupFrames.elementAt(i)).isSynced()) {

                                ((SyncFrame) groupFrames.elementAt(i)).setVisible(false);

                            }

                            break;

                        }

                        case SET_ALL_TRUE: {

                            ((SyncFrame) groupFrames.elementAt(i)).setSync(true);

                            break;

                        }

                        case SET_ALL_FALSE: {

                            ((SyncFrame) groupFrames.elementAt(i)).setSync(false);

                            break;

                        }

                        default: {

                            if (DEBUG) {

                                System.err.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("SyncFrameGroup_->_We_fell_through_to_the_default."));

                            }

                        }

                    } // end "switch"

                } catch (Exception e) {

                    System.out.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("Errant_class..._must...._destroy...!") + e);

                }

            } // end "if"

        } // end "for" loop

        clearLocks();

    }

    /**
     * This clears the 'lock' status from all our frames, but to my way of
     * thinking, this method should be removed in favour of actually finding the
     * 'lock-bug'
     */
    // private synchronized void clearLocks() {
    private void clearLocks() {
        // String tempName;

        final int length = groupFrames.size();

        for (int i = 0; i < length; i++) {

                    //tempName = new String(((Frame)groupFrames.elementAt(i)).paramString());
            ((SyncFrame) groupFrames.elementAt(i)).setLock(false);

        }

    }

}
