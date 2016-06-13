/*
 * ColourCellRenderer.java
 *
 * Created on November 27, 2005, 9:57 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package anecho.gui;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;
import java.awt.Component;

public class ButtonCellRenderer extends JButton implements TableCellRenderer {
    private final transient Border cellBorder = null;
    // final private boolean isBordered = true;
    /** Enable or disable debugging output */
    private static final boolean DEBUG = false;
    private JButton cellButton;
    
    public ButtonCellRenderer() {
        setOpaque(true);
        cellButton = new JButton();
    }

    public ButtonCellRenderer(JButton tempButton) {
        this();
        cellButton = tempButton;
    }
    
    /**
     * 
     * @param curTable 
     * @param colour 
     * @param isSelected 
     * @param hasFocus 
     * @param row 
     * @param column 
     * @return 
     */
    @Override
    public Component getTableCellRendererComponent(final JTable curTable, final Object colour, final boolean isSelected, final boolean hasFocus, final int row, final int column) {
        // final Color newColour = (Color)colour;
        // final JButton newButton = (JButton)colour;
        
        // cellBorder = BorderFactory.createMatteBorder(2,5,2,5, curTable.getBackground());
        // setBorder(cellBorder);
        // setBackground(newButton);
        // setText("Delete!");
        buttonPressed(row, column);
                
        // return this;
        return cellButton;
    }
    
    /** This method must be modified to pass relevant information
     * @param row
     * @param column */
    public void buttonPressed(int row, int column) {
        if (DEBUG) {
            System.err.println("anecho.gui.ButtonCellRenderer.buttonPressed()");
        }
    }
}
