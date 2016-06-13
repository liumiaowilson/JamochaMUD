/*
 * ColourCellEditor.java
 *
 * Created on November 27, 2005, 9:58 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package anecho.JamochaMUD.PrefPanels.CustomColours;

import java.awt.Color;
import java.awt.Component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractCellEditor;
import javax.swing.table.TableCellEditor;
import javax.swing.JDialog;
import javax.swing.JTable;
import anecho.JamochaMUD.*;

public class ColourCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener, MouseListener {
    /**
     * The colour currently displayed by our component.
     */
    private transient Color curColour;
    /**
     * This panel is used to display our given colour.
     */
    final private transient javax.swing.JPanel colPanel;
    /**
     * Our colour chooser
     */
    private transient javax.swing.JColorChooser jcc;
    /**
     * Dialogue to contain the colour chooser
     */
    // private JDialog dialog;
    private static final boolean DEBUG = false;
    
    /**
     * 
     */
    public ColourCellEditor() {
        super();
        colPanel = new javax.swing.JPanel();
        colPanel.addMouseListener(this);
    }
    
    /**
     * 
     * @param mEvent 
     */
    @Override
    public void mouseExited(final MouseEvent mEvent) {
        
    }
    
    /**
     * 
     * @param mEvent 
     */
    @Override
    public void mouseEntered(final MouseEvent mEvent) {
        
    }
    
    /**
     * 
     * @param mEvent 
     */
    @Override
    public void mouseReleased(final MouseEvent mEvent) {
        
    }
    
    /**
     * 
     * @param mEvent 
     */
    @Override
    public void mousePressed(final MouseEvent mEvent) {
    }
    
    /**
     * 
     * @param mEvent 
     */
    @Override
    public void mouseClicked(final MouseEvent mEvent) {
        if (DEBUG) {
            System.err.println("ColorEditor: We received a mouseclick " + mEvent);
        }
        chooseColour();
    }
    
    
    /** Create a dialogue to let the user choose a new colour */
    private void chooseColour() {
        // final JMConfig settings = JMConfig.getInstance();
        // final javax.swing.JFrame mainFrame = (javax.swing.JFrame)settings.getJMFrame(JMConfig.MUCKMAINFRAME);
        final javax.swing.JFrame mainFrame = (javax.swing.JFrame)MuckMain.getInstance().getMainFrame();
        
        jcc = new javax.swing.JColorChooser();
        final JDialog colDialogue = javax.swing.JColorChooser.createDialog((java.awt.Component)mainFrame, java.util.ResourceBundle.getBundle("anecho/JamochaMUD/PrefPanels/CustomColours/ColourBundle").getString("Choose_a_new_colour"), true, jcc, this, null);
        colPanel.setBackground(curColour);
        jcc.setColor(curColour);
        colDialogue.setVisible(true);
        fireEditingStopped();

    }

    /**
     * Determine if a new colour has been selected and set it if so
     * @param evt 
     */
    @Override
    public void actionPerformed(final ActionEvent evt) {
        if (DEBUG) {
            System.err.println("We received this action event:" + evt);
        }
        curColour = jcc.getColor();
    }

    /**
     * 
     * @return 
     */
    @Override
    public Object getCellEditorValue() {
        return curColour;
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
    @Override
    public Component getTableCellEditorComponent(final JTable table, final Object value, final boolean isSelected, final int row, final int column) {
        curColour = (Color)value;
        return colPanel;
    }
}

