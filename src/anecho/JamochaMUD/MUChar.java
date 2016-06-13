/*
 * MUChar.java
 *
 * Created on September 24, 2006, 7:30 PM
 *
 * $Id: MUChar.java,v 1.5 2008/06/21 02:01:46 jeffnik Exp $
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package anecho.JamochaMUD;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author jeffnik
 */
public class MUChar extends DefaultMutableTreeNode {
    
    /**
     * Default constructor
     */
    public MUChar() {
            super();
        }
        
    /**
     * 
     * @param name 
     */
        public MUChar(String name) {
            super();
            charName = name;
        }
        
    /**
     * 
     * @param name 
     */
        public void setName(final String name) {
            charName = name;
        }
        
    /**
     * 
     * @return 
     */
        public String getName() {
            return charName;
        }
        
    /**
     * 
     * @param password 
     */
        public void setPassword(final String password) {
            charPassword = password;
        }
        
    /**
     * 
     * @return 
     */
        public String getPassword() {
            return charPassword;
        }
        
    /**
     * 
     * @param connString 
     */
        public void setConnString(final String connString) {
            charConn = connString;
        }
        
    /**
     * 
     * @return 
     */
        public String getConnString() {
            return charConn;
        }
        
    /**
     * 
     * @param notes 
     */
        public void setNotes(final String notes) {
            charNotes = notes;
        }
        
    /**
     * 
     * @return 
     */
        public String getNotes() {
            return charNotes;
        }
        
    /**
     * 
     * @param worldID 
     */
        public void setWorldID(final long worldID) {
            charWorldID = worldID;
        }
        
    /**
     * 
     * @return 
     */
        public long getWorldID() {
            return charWorldID;
        }
        
    /**
     * 
     * @return 
     */
        public Icon getIcon() {
            final Icon retIcon = new ImageIcon(MUChar.class.getResource(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("icons/22/personal.png")));
            
            return retIcon;
        }
 
    /**
     * 
     * @param state 
     */
        public void setAutoLogin(final boolean state) {
            autoLogin = state;
        }
        
    /**
     * 
     * @return 
     */
        public boolean isAutoLogin() {
            return autoLogin;
        }
        
    /**
     * 
     * @param style 
     */
        public void setLoginStyle(final String style) {
            loginStyle = style;
        }
        
    /**
     * 
     * @return 
     */
        public String getLoginStyle() {
            return loginStyle;
        }
        
        /**
     * Return a string listing the name, address, and port
     * @return 
     */
    @Override
        public String toString() {
            final StringBuffer retBuff = new StringBuffer();
            retBuff.append(this.getName());
            
            return retBuff.toString();
        }

        /**
         * Get the type of character this entry is
         * @return
         */
        public int getCharType() {
        return charType;
    }

        /**
         * Set the type of "character" this entry is to be
         * @param charType
         * 0 - Character
         * 1 - Puppet
         */
        public void setCharType(final int charType) {
        this.charType = charType;
    }
        
        
        private transient String charName;
        private transient String charPassword;
        private transient String charConn;
        private transient String charNotes;
        private transient long charWorldID;
        private static final boolean DEBUG = false;
        private boolean autoLogin = false;
        private String loginStyle;
        /** The character type that this entry represents (character, puppet, etc.) */
        private int charType;
}
