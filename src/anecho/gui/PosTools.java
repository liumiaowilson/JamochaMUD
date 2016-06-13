/**

 * PosTools, a JamochaMUD class to determine the centre "Point"

 * of the desktop or passed object

 * $Id: PosTools.java,v 1.4 2006/11/17 06:24:35 jeffnik Exp $
 */



/* JamochaMUD, a Muck/Mud client program

 * Copyright (C) 1998-2005 Jeff Robinson

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



import java.awt.Component;

import java.awt.Dimension;

import java.awt.Point;



/** A set of tools to help ease finding the centre and corner points

 * of existing objects and/or the screen.

 * @version $Id: PosTools.java,v 1.4 2006/11/17 06:24:35 jeffnik Exp $
 * @author Jeff Robinson

 */



public class PosTools {


    // Static variables

    private final static int NORTHWEST = 0;	// NorthWest corner of object

    private final static int NORTHEAST = 1;	// NorthEast corner of object

    private final static int SOUTHWEST = 3;	// SouthWest corner of object

    private final static int SOUTHEAST = 4;	// SouthEast corner of object

    private final static int CENTRE = 5;	// Centre of the object (default)

    private final static int CENTER = 5;    // for all the people that spell like this!

    private static final boolean DEBUG = false;

    // other variables

    private static Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize(); // Screen size



    /**

     * Without arguements, we will return the centre of the 'desktop'

     * @deprecated use findCenter() instead.  English spelling confuses people

     * @return Returns a point indicating the centre of the user's "desktop"

     */

    public static Point findCentre() {



        // Take half of the width and half of the height of the screen

        // int xCentre = (int)screen.width / 2;

        // int yCentre = (int)screen.height / 2;



        // return new Point(xCentre, yCentre);

        return findCenter();

    }



    /**

     * Without arguements, we will return the centre of the 'desktop'

     * @return Return a point indicating the centre of the 'desktop'

     */

    public static Point findCenter() {



        // Take half of the width and half of the height of the screen

        final int xCentre = (int)screen.width / 2;

        final int yCentre = (int)screen.height / 2;



        return new Point(xCentre, yCentre);

    }



    /**
     * Find the centre of the object that we have been passed
     * @param comp The component that we are to find the centre of.
     * @return A Point containing the relative coordinates of the 
     * centre of our component.
     */

    public static Point findCenter(final Component comp) {

        return findCenter(comp, CENTER);

    }



    /**
     * Find the centre of the object that we have been passed
     * @param comp The component that we are to find the centre of.
     * @return A point representing the centre of the given component.
     * @deprecated use findCenter(Component c) instead
     */

    public static Point findCentre(final Component comp) {

        // this.findCentre(c, CENTRE);

        return findCentre(comp, CENTRE);

    }



    /**
     * Returns the specified corner of the object which we have been passed
     * @param comp The component that we are to find the centre of.
     * @param corner 
     * @return A point representing the centre of the given component.
     * @deprecated use findCenter(Component c, int corner) instead
     */

    public static Point findCentre(final Component comp, final int corner) {

        return findCenter(comp, corner);

    }



    /**
     * Returns the specified corner of the object which we have been passed
     * @param comp The component that we are to find the centre of.
     * @param corner The name of the component's corner-coordinates that we want returned
     * @return A point representing the centre of the given component.
     */

    public static Point findCenter(final Component comp, final int corner) {

        // First, determine information about the object

        int cXPos = 0;		// X axis of component

        int cYPos = 0;		// Y axis of component



        try {

            cXPos = comp.getLocationOnScreen().x;

            cYPos = comp.getLocationOnScreen().y;

        } catch (Exception e) {

            if (DEBUG) {

                 System.out.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("PosTools.findCentre(Component,_int)_") + comp + java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("_") + e);

            }

        }



        final int cWidth = comp.getSize().width;		// Width of the component

        final int cHeight = comp.getSize().height;		// Height of the component

        final int cXCentre = cWidth / 2;	// Horizontal centre of the component (relative)

        final int cYCentre = cHeight / 2;	// Vertical centre of the component (relative)



        final Point centre = new Point(0, 0);	// create a Point to contain the return info



        // Now do the calculations based on the 'corner' chosen

        switch (corner) {

        case NORTHWEST:

            centre.setLocation(cXPos, cYPos);

            break;

        case NORTHEAST:

            centre.setLocation(cXPos + cWidth, cYPos);

            break;

        case SOUTHWEST:

            centre.setLocation(cXPos, cYPos + cHeight);

            break;

        case SOUTHEAST:

            centre.setLocation(cXPos + cWidth, cYPos + cHeight);

            break;

        default:

            centre.setLocation(cXPos + cXCentre, cYPos + cYCentre);

        }



        return centre;

    }



    /**
     * return the point to centre the child component on the parent component
     * @param parent The parent component that will be used to centre the child
     * @param child 
     * @return A point representing the centre of the given component.
     * @deprecated use findCenter(Component parent, Component child)
     */

    public static Point findCentre(final Component parent, final Component child) {

        return findCenter(parent, child);

    }



    /**
     * return the point to centre the child component on the parent component
     * @param parent The parent component that will be used to centre the child
     * @param child The child component to be centred on the parent
     * @return A point representing the centre of the given component.
     */

    public static Point findCenter(final Component parent, final Component child) {

        return findCentre(parent, CENTRE, child);

    }



    /**
     * return the Point to centre the child component over the given axis of the parent
     * @param parent The parent component that will be used to centre the child
     * @param corner 
     * @param child 
     * @return A point representing the centre of the given component.
     */

    public static Point findCentre(final Component parent, final int corner, final Component child) {

        // get the "centre" of the parent

        Point centre = findCentre(parent, corner);

        final int parentX = centre.x;

        final int parentY = centre.y;

        final int childWidth = (int)child.getSize().width / 2;

        final int childHeight = (int)child.getSize().height / 2;



        centre = new Point(parentX - childWidth, parentY - childHeight);



        return centre;



    }

}

