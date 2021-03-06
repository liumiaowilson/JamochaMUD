/*
 * MainSwingFrame.java
 *
 * This class moves the Swing GUI for the main window out of the MuckMain class.
 * Created on June 16, 2005, 7:53 PM
 */

/* JamochaMUD, a Muck/Mud client program
 * Copyright (C) 1998-2005  Jeff Robinson
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
package anecho.JamochaMUD;

import anecho.gui.*;
import net.sf.wraplog.AbstractLogger;
import net.sf.wraplog.NoneLogger;
import net.sf.wraplog.SystemLogger;
// import java.awt.Image;
// import java.awt.Toolkit;

/**
 *
 * @author jeffnik
 */
public class MainSwingFrame extends JSyncFrame {

    private final AbstractLogger logger;

    /**
     * Creates new form MainSwingFrame
     */
    public MainSwingFrame() {
        this("");
    }

    /**
     *
     * @param title
     */
    public MainSwingFrame(String title) {
        this(title, null);
    }

    /**
     *
     * @param title
     * @param group
     */
    public MainSwingFrame(String title, SyncFrameGroup group) {
        this(title, false, group);

    }

    /**
     *
     * @param title
     * @param sync
     * @param group
     */
    public MainSwingFrame(String title, boolean sync, SyncFrameGroup group) {
        super(title);
        
        if (DEBUG) {
            logger = new SystemLogger();
        } else {
            logger = new NoneLogger();
        }


        initComponents();

    }

    /**
     *
     * @return
     */
    public int getDividerLocation() {
        final int retVal = masterPane.getDividerLocation();

        return retVal;
    }

    /**
     *
     * @param loc
     */
    public void setDividerLocation(final int loc) {
        if (DEBUG) {
            System.err.println("Setting divider location " + loc);
        }
        masterPane.setDividerLocation(loc);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        masterPane = new javax.swing.JSplitPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        masterPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        masterPane.setResizeWeight(1.0);
        getContentPane().add(masterPane, java.awt.BorderLayout.CENTER);

        pack();
    }
    // </editor-fold>//GEN-END:initComponents

    /**
     *
     * @param comp
     * @param pos
     */
    public void setPaneComponent(final java.awt.Component comp, final int pos) {

        logger.debug("MainSwingFrame.setPaneComponent using" + comp);
        
        if (pos == TOP) {
            masterPane.setTopComponent(comp);
            // comp.addComponentListener(this);
        } else {
            masterPane.setBottomComponent(comp);
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSplitPane masterPane;
    // End of variables declaration//GEN-END:variables
    public final static int TOP = 0;
    public final static int BOTTOM = 1;
    private static final boolean DEBUG = false;
}
