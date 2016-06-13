/**
 * JMSwingTextPane, a Swing TextPane that has been extended to
 * allow programmers to turn antialiasing on and off easily.
 * $Id: JMSwingTextPane.java,v 1.12 2013/09/16 01:40:49 jeffnik Exp $
 */

/* JMSwingTextPane, a antialiasing TextPane
 * Copyright (C) 2004 - 2008 Jeff Robinson
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
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


import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JTextPane;
import javax.swing.text.StyledDocument;
import javax.swing.text.html.HTMLDocument;

/**
 * JMSwingTextPane, a antialiasing TextPane
 */
public class JMSwingTextPane extends JTextPane {

    /**
     * The current state of antialiasing for this component.
     */
    transient boolean antiAlias = false;
    /**
     * Enables and disables debugging output
     */
    private static final boolean DEBUG = false;

    /**
     * JMSwingTextPane, a antialiasing TextPane
     */
    public JMSwingTextPane() {
        super();
    }

    /**
     * JMSwingTextPane, a antialiasing TextPane
     * @param doc The document used by our parent class
     */
    public JMSwingTextPane(StyledDocument doc) {
        super(doc);
    }

    /**
     * JMSwingTextPane, an antialiasing TextPane using an HTMLDocument for display
     * @param doc The document used by our parent class
     */
    public JMSwingTextPane(HTMLDocument doc) {
        super(doc);
    }
    
    /**
     * Set the status of our AntiAliasing to either be enabled or
     * disable in this component.
     * @param status <CODE>true</CODE> - enable antialiasing
     * <CODE>false</CODE> - disable antialiasing
     */
    public synchronized void setAntiAliasing(final boolean status) {
        antiAlias = status;

        if (DEBUG) {
            System.err.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("JMSwingTextPane.setAntiAliasing()_Setting_antiAliasing_to:_") + status);
        }

        // Try to ensure that our display gets updated to show the new look
        invalidate();
        validate();
        repaint();

    }

    /**
     * Returns whether font anti-aliasing is activated
     * @return <code>true</code> Anti-aliasing is active. <code>false</code> anti-aliasing is not active
     */
    public boolean isAntiAliasing() {
        return antiAlias;
    }

    /**
     * Over-ridden paintComponent procedure to set the Rendering Hints
     * @param graphItem 
     */
    public void paintComponent(final Graphics graphItem) {

        final Graphics2D graph2D = (Graphics2D) graphItem;

        if (graphItem == null) {
            return;
        }

        if (antiAlias) {
            if (DEBUG) {
                System.err.print(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("+"));
            }
            graph2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        } else {
            if (DEBUG) {
                System.err.print(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("-"));
            }
            graph2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
            graph2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        }

        super.paintComponent(graphItem);
    }
//    public int getVisibleRows() {
//        int visRows = super.getVisibleRows();
//        
//        return visRows;
//    }

//    public int getRows() {
//        // int visRows = super.getRows();
//        int totalHeight = super.getVisibleRect();
//        int lineHeight = super.getFont().getSize();
//        
//        return visRows;
//        
//    }
}
