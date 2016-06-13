/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package anecho.JamochaMUD.plugins.TriggerDir;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import net.sf.wraplog.AbstractLogger;
import net.sf.wraplog.NoneLogger;
import net.sf.wraplog.SystemLogger;

/**
 *
 * @author jeffnik
 */
public class DeleteCellEditor extends anecho.gui.ButtonCellEditor {

    private static final boolean DEBUG = false;
    private java.awt.Dimension coordinate;
    AbstractLogger logger;

    DeleteCellEditor(JButton delButton) {
        super(delButton);

        if (DEBUG) {
            logger = new SystemLogger();
        } else {
            logger = new NoneLogger();
        }
        
    }

    @Override
    public Component getTableCellEditorComponent(final JTable table, final Object value, final boolean isSelected, final int row, final int column) {
        logger.debug("DeleteCellEditor.getTableCellEditorComponent row " + row + " column: " + column);
        JButton tempButton = this.getButton();
        
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.removeRow(row);
        
        return tempButton;
    }

    public void mouseClicked(final MouseEvent mEvent) {

        if (DEBUG) {
            System.err.println("DeleteCellEditor.mouseClicked()");
        }

    }

    public Dimension getSelectedCoordinate() {
        return new Dimension(0, 0);
    }
}
