/*
 * EasyIconTreeNode.java
 *
 * Created on July 25, 2006, 2:34 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package anecho.gui;

import javax.swing.Icon;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * The class is an extension of the DefaultMutableTreeNode that allows
 * the programmer to easily assign an icon to it.
 * @author jeffnik
 */
public class EasyIconTreeNode extends DefaultMutableTreeNode {
   
    /**
     * Set the icon for this node.
     * @param iconName The icon file to be used for this node.
     */
    public void setIcon(final Icon iconName) {
        nodeIcon = iconName;
    }
    
    /**
     * Returns the icon assigned to this node.
     * @return The icon assigned to this node.  This value may be null
     * if an icon has not yet been assigned.
     */
    public Icon getIcon() {
        return nodeIcon;
    }
    
    private Icon nodeIcon;
}
