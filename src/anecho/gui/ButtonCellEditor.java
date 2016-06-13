/*
 * ColourCellEditor.java
 *
 * Created on November 27, 2005, 9:58 PM
 * $Id: ButtonCellEditor.java,v 1.3 2014/05/21 00:42:33 jeffnik Exp $
 */
package anecho.gui;

import java.awt.Component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.table.TableCellEditor;
import javax.swing.JTable;

public class ButtonCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener, MouseListener {

    private transient javax.swing.JButton tableButton;
    private static final boolean DEBUG = false;

    /**
     * 
     */
    public ButtonCellEditor() {
        this(null);
    }

    public ButtonCellEditor(JButton tempButton) {

        if (tempButton != null) {
            tableButton = tempButton;
            tableButton.addMouseListener(this);
        }

    }

    /**
     * 
     * @param mEvent 
     */
    public void mouseExited(final MouseEvent mEvent) {
    }

    /**
     * 
     * @param mEvent 
     */
    public void mouseEntered(final MouseEvent mEvent) {
    }

    /**
     * 
     * @param mEvent 
     */
    public void mouseReleased(final MouseEvent mEvent) {
        if (DEBUG) {
            System.err.println("ButtonCellEditor.mouseReleased: We received a mouserelease " + mEvent);
        }
        // buttonPressed(mEvent);

    }

    /**
     * 
     * @param mEvent 
     */
    public void mousePressed(final MouseEvent mEvent) {
        if (DEBUG) {
            System.err.println("ButtonCellEditor.mousePressed: We received a mousepress " + mEvent);
        }
        // buttonPressed(mEvent);
    }

    /**
     * 
     * @param mEvent 
     */
    public void mouseClicked(final MouseEvent mEvent) {
        if (DEBUG) {
            System.err.println("ButtonCellEditor.mouseClicked: We received a mouseclick " + mEvent);
        }
        
        // buttonPressed(mEvent);
        // chooseColour();
    }

    /**
     * Determine if a new colour has been selected and set it if so
     * @param evt 
     */
    public void actionPerformed(final ActionEvent evt) {
        if (DEBUG) {
            System.err.println("We received this action event:" + evt);
        }
        // curColour = jcc.getColor();
        buttonPressed(evt);
    }

    /**
     * 
     * @return 
     */
    public Object getCellEditorValue() {
        return tableButton;
    }

    /**
     * 
     * @param table 
     * @param value 
     * @param isSelected 
     * @param row 
     * @param column 
     * @return 
     */
    public Component getTableCellEditorComponent(final JTable table, final Object value, final boolean isSelected, final int row, final int column) {
        return tableButton;
    }

    public void buttonPressed(ActionEvent evt) {
        // subclass this method for your own nefarious purposes!
        if (DEBUG) {
            System.err.println("ButtonCellEditor.buttonPressed: " + evt);
        }
    }
    
    protected JButton getButton() {
        return tableButton;
    }
    
}
