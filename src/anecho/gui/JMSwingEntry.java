/* JMSwingEntry, a Swing-based text entry area with anti-aliasing effects
 * Copyright (C) 2005 Jeff Robinson
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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * JMSwingEntry is a Swing-based area used for inputting text.
 */
public class JMSwingEntry extends javax.swing.JTextPane implements java.awt.event.MouseListener {

    /**
     * Indicates whether text antialiasing is enabled (true) or disabled (false)
     */
    private boolean antiAlias = false;
    private Style docStyle;
    private StyledDocument doc;
    /**
     * Enables and disables debugging output
     */
    private static final boolean DEBUG = false;

    /**
     * JMSwingEntry is a Swing based component that allows a user to
     * input text.
     */
    public JMSwingEntry() {
        super();
        addMouseListener(this);

        setDoc();
    }

    /**
     * JMSwingEntry is a Swing based component that allows a user to
     * input text.
     * @param doc Our document (used by the parent class)
     */
    // public JMSwingEntry(javax.swing.text.Document doc)  {
    public JMSwingEntry(javax.swing.text.StyledDocument doc) {
        super(doc);
        addMouseListener(this);

        setDoc();
    }

    /**
     * JMSwingEntry is a Swing based component that allows a user to
     * input text.
     * @param doc Our document, used by the parent class.
     * @param text Text to be initially shown after our object is created
     * @param rows Number of rows for our component to display
     * @param columns Number of columns for our component to display.
     */
//    public JMSwingEntry(javax.swing.text.Document doc, String text, int rows, int columns) {
//        super(doc, text, rows, columns);
//        addMouseListener(this);
//    }
    /**
     * JMSwingEntry is a Swing-based area used for inputting text.
     * @param rows Number of rows to show in our component
     * @param columns Number of columns to show in our component
     */
//    public JMSwingEntry(int rows, int columns) {
//        super(rows, columns);
//        addMouseListener(this);
//    }
    /**
     * JMSwingEntry is a Swing-based area used for inputting text.
     * @param text Initial text to be displayed after our component is created
     */
//    public JMSwingEntry(String text) {
//        super(text);
//        addMouseListener(this);
//    }
    /**
     * JMSwingEntry is a Swing-based area used for inputting text.
     * @param text The initial text to be displayed after our component is created.
     * @param rows Number of rows for our component to show.
     * @param columns Number of columns for our component to show.
     */
//    public JMSwingEntry(String text, int rows, int columns) {
//        super(text, rows, columns);
//        addMouseListener(this);
//    }
    /**
     * This method allows anti-aliasing of text to be enabled or disabled
     * @param state true - Enable anti-aliasing on text (smooth characters)
     * false - Disable anti-aliasing on text
     */
    public void setAntiAliasing(final boolean state) {
        antiAlias = state;
    }

    /**
     * Returns whether antialiasing is active on this component
     * @return <code>true</code> - Antialiasing is enabled <code>false</code> - Antialiasing is not enabled
     */
    public boolean isAntiAliasing() {
        return antiAlias;
    }

    /**
     * Over-ridden paintComponent proceedure to set the Rendering Hints
     * @param graphItem The Graphic object that we will render to.
     */
    public void paintComponent(final Graphics graphItem) {

        if (graphItem != null) {
            final Graphics2D graph2D = (Graphics2D) graphItem;

            if (antiAlias) {
                graph2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            } else {
                graph2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
                graph2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            }

            super.paintComponent(graphItem);
        }
    }

    /**
     * Subclassed for proper cursor appearance
     * @param evt Our mouse event
     */
    public void mousePressed(final java.awt.event.MouseEvent evt) {
        this.setCaretColor(this.getForeground());
    }

    /**
     * Empty event
     * @param evt Mouse Event
     */
    public void mouseReleased(final java.awt.event.MouseEvent evt) {

    }

    /**
     * Empty event
     * @param evt mouse event
     */
    public void mouseEntered(final java.awt.event.MouseEvent evt) {

    }

    /**
     * Empty event
     * @param evt Mouse Event
     */
    public void mouseExited(final java.awt.event.MouseEvent evt) {

    }

    /**
     * Empty event
     * @param evt Mouse Event
     */
    public void mouseClicked(final java.awt.event.MouseEvent evt) {

    }

    /**
     * Append text to the end of the existing text in this component.
     * @param text The text to be appended.
     */
    public void append(final String text) {
        // StyledDocument doc = (StyledDocument)this.getDocument();
        try {
            doc.insertString(doc.getLength(), text, docStyle);
        } catch (Exception exc) {
            if (DEBUG) {
                System.err.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("JMSwingEntry_exception_at_append_") + exc);
            }
        }
    }

    private void setDoc() {
        doc = (StyledDocument) this.getDocument();
        docStyle = doc.addStyle(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("StyleName"), null);


    }

    /**
     * This method should return the number of columns in this component.
     * Fix Me XXX - This method is not yet supported
     * @return An integer representing the number of columns showing.
     */
    public int getColumns() {
        final int retVal = -1;

        return retVal;
    }

    /**
     * This method should set the number of columns in this component.
     * Fix Me XXX - This method is not yet supported
     */
    public void setColumns(int cols) {
        
    }

    /**
     * This method sets the number of rows in this component.
     * Fix Me XXX
     * @param rows The number of rows that this component should have.
     */
    public void setRows(final int rows) {

    }

    /** Because we are using a styled document, we can't directly call
     * setForeground.
     */
    public void setForeground(Color fgc) {
        super.setForeground(fgc);

        if (DEBUG) {
            System.err.println("JMSwingEntry setting foreground colour");
        }
        // doc = (StyledDocument)this.getDocument();
        // docStyle = doc.addStyle(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("StyleName"), null);
        MutableAttributeSet attrs = this.getInputAttributes();
        StyleConstants.setForeground(attrs, fgc);

        // Make certain that the caret is also set to the proper colour
        this.setCaretColor(fgc);
    }
}

