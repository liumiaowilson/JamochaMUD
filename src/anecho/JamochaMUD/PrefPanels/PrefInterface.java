/*
 * PrefInterface.java
 *
 * Created on December 10, 2005, 3:12 AM
 *
 * Part of JamochaMUD, this interface helps define new "Panels" to be shown
 * in the JamochaMUD preferences dialogue.  This way, panels may be loaded
 * in a lazy fashion, giving an apparent speed-boost to the Prefs dialogue.
 */

/* JamochaMUD, a Muck/Mud client program
 * Copyright (C) 1998-2008 Jeff Robinson
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

package anecho.JamochaMUD.PrefPanels;

import javax.swing.JPanel;

/**
 * This interface defines methods to be used by other "PrefPanel" classes.
 * These classes appear as separate items in the JamochaMUD preferences
 * dialogue allowing the user to customise how JamochaMUD operates.
 * @author Jeff Robinson
 */
public interface PrefInterface {
// public abstract class PrefInterface extends javax.swing.JPanel {

    /**
     * The human-readable name for our module
     * @return This string represents the human-readable name of the given module.
     */
    String getModuleName();
    
    /**
     * 
     * @return 
     */
    JPanel loadPanel();
    
    void applySettings();

    /**
     * This interface allows preference panels to tell JamochaMUD whether
     * the system meets all requirements to run it (Java version, etc).
     * @return
     */
    boolean checkVersion();
    
    /**
     * This method allows the preference panel to supply the user with feedback
     * in the event that the checkVersion fails.  This is called when the
     * panel is shown.
     */
    void versionWarning();
    
//    public String getModuleName() {
//        return "Module Name";
//    }
//    
//    public JPanel loadPanel() {
//        return new JPanel();
//        
//    }
//    
//    public void applySettings() {
//        // La-dee-dah!
//    }
    
}
