/*
 * ColourCellRenderer.java
 *
 * Created on November 27, 2005, 9:57 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package anecho.JamochaMUD.PrefPanels.CustomColours;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;
import java.awt.Color;
import java.awt.Component;

public class ColourCellRenderer extends JLabel implements TableCellRenderer {
    private transient Border cellBorder = null;
    // final private boolean isBordered = true;

    public ColourCellRenderer() {
        super();
        setOpaque(true);
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
    public Component getTableCellRendererComponent(final JTable curTable, final Object colour, final boolean isSelected, final boolean hasFocus, final int row, final int column) {
        final Color newColour = (Color)colour;
        
        cellBorder = BorderFactory.createMatteBorder(2,5,2,5, curTable.getBackground());
        setBorder(cellBorder);
        setBackground(newColour);

        return this;
    }
}
