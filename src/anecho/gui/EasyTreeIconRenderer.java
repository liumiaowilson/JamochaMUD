/*
 * EasyTreeIconRenderer.java
 *
 * Created on July 24, 2006, 8:37 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package anecho.gui;

import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * This class is used to display custom icons for individual nodes
 * in a JTree.  It works in conjunction with the EasyIconTreeNode
 * class, but can be used by any tree node.
 * @author jeffnik
 */
public class EasyTreeIconRenderer extends DefaultTreeCellRenderer {
    
    /**
     * This method over-rides the super-class getTreeCellRenderer method
     * allowing us to set our own icon.
     * @param tree 
     * @param value 
     * @param sel 
     * @param expanded 
     * @param leaf 
     * @param row 
     * @param hasFocus 
     * @return Returns the TreeCellRenderer
     */
    public Component getTreeCellRendererComponent(final JTree tree, final Object value,
            final boolean sel, final boolean expanded, final boolean leaf, final int row,
            final boolean hasFocus) {
        
        
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
                row, hasFocus);
        
        if (DEBUG) {
            System.err.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("EasyTreeIconRenderer_node_value:_") + value);
        }
        
        // Try to set the icon via the value
        final DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)value;

        // Fix ME XXX
        // This block throws an exception if we execute it, but it seems to do
        // what we want.  If it is commented out, the icons do not change.
        // It's really weird.
        try {
            final EasyIconTreeNode tempNode = (EasyIconTreeNode)dmtn;
            final Icon tempIcon = tempNode.getIcon();
            setIcon(tempIcon);
        } catch (Exception exc) {
            if (DEBUG) {
                System.err.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("Exception_with_cast_") + exc);
                exc.printStackTrace();
            }
        }
        
        return this;
    }
    
    private static final boolean DEBUG = false;
}

