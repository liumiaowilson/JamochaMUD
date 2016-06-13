/*
 * This class subverts a JLabel to act like a hyperlink in a JMSwingText component
 * $Id: Hyperlink.java,v 1.7 2013/09/16 01:40:49 jeffnik Exp $
 */

/* JamochaMUD, a Muck/Mud client program
 * Copyright (C) 1998-2013 Jeff Robinson
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

package anecho.gui;

import java.awt.Color;
import javax.swing.JLabel;

/**
 *
 * @author jeffnik
 */
public class Hyperlink extends JLabel {
// public class Hyperlink extends JButton {

    private static final boolean DEBUG = false;
    /** This array holds additional information if provided */
    private String[] extraInfo;

    public Hyperlink() {
        this("");
    }

    /**
     *
     * @param address The address to navigate to when this link is activated
     */
    public Hyperlink(final String address) {
        this(address, address);
    }

    /**
     *
     * @param address The address to navigate to when this link is activated
     * @param text The text that will appear to the user.  If this is not provided Hyperlink will use the provided address instead
     */
    public Hyperlink(final String address, final String text) {
        this(address, text, null);
    }

    /**
     *
     * @param address The address to navigate to when this link is activated
     * @param text The text that will appear to the user.  If this is not provided Hyperlink will use the provided address instead
     * @param hint A hint is shown to the user when the mouse pointer hovers over the link
     */
    public Hyperlink(final String address, final String text, final String hint) {
        this(address, text, hint, null);
    }

    /**
     *
     * @param address The address to navigate to when this link is activated
     * @param text The text that will appear to the user.  If this is not provided Hyperlink will use the provided address instead
     * @param hint A hint is shown to the user when the mouse pointer hovers over the link
     * @param linkName
     */
    public Hyperlink(final String address, final String text, final String hint, final String linkName) {
        // super(address);
        super();

        if (DEBUG) {
            System.err.println("----Hyperlink.hyperlink initial received information:----");
            System.err.println("Address: " + address);
            System.err.println("Text: " + text);
            System.err.println("Hint: " + hint);
            System.err.println("Link Name: " + linkName);
            System.err.println("----End initial information----");
        }

        this.address = address;

        if (text == null) {
            super.setText("<html><u>" + address + "</u></html>");
            if (DEBUG) {
                System.err.println("Hyperlink setting label to text: " + text);
            }
        } else {

            super.setText("<html><u>" + text + "</u></html>");

            if (DEBUG) {
                System.err.println("Hyperlink setting label to address: " + address);
            }
        }

        if (hint != null) {
            setToolTipText(hint);
        }

        if (linkName != null) {
            this.linkName = linkName;
        }

        setForeground(Color.BLUE);


        if (DEBUG) {
            setOpaque(true);
            setBackground(Color.RED);
            System.err.println("Hyperlink.hyperlink created with:");
            System.err.println("Address: " + address);
            System.err.println("Text: " + text);
            System.err.println("Hint: " + hint);
            System.err.println("Link Name: " + linkName);
        }
    }

    /**
     * Returns the address associated with this link
     * @return
     */
    public String getAddress() {
        return address;
    }

    /**
     * Set the address for this link
     * @param address
     */
    public void setAddress(final String address) {
        this.address = address;
    }

    /**
     *
     * @return
     */
    public String getLinkName() {
        return linkName;
    }

    /**
     *
     * @param linkName
     */
    public void setLinkName(final String linkName) {
        this.linkName = linkName;
    }

    @Override
    public void setText(final String linkText) {
        super.setText("<html><u>" + linkText + "</u></html>");
    }

    /**
     * 
     * @return
     */
    @Override
    public String getText() {
        final String origText = super.getText();

        int start, end;

        if (origText.startsWith("<html><u>")) {
            start = 9;
        } else {
            start = 0;
        }

        if (origText.indexOf("</u></html>") > -1) {
            end = origText.indexOf("</u>");
        } else {
            end = origText.length();
        }

        return origText.substring(start, end);
    }

    public String[] getExtraInfo() {
        return extraInfo;
    }

    /** Extra info can be used to store information that might not
     * be a standard part of a link, but can be used by classes that extend this one
     * @param extraInfo
     */
    public void setExtraInfo(final String[] extraInfo) {
        if (extraInfo != null && extraInfo.length > 0) {
        this.extraInfo = extraInfo;
        }
    }
    
    /** Cause the link to "react" with a colour change.  
     * This can be used when clicked to give feedback to the user.
     */
    public void react() {
        Color orig = this.getForeground();
        this.setForeground(Color.WHITE);
        this.setForeground(orig);
    }

//    public void getText() {
//        String retText;
//        String orgText = super.getText();
//
//        int start = orgText.indexOf("<html><u>");
//        int end;
//
//        if (start > -1) {
//            retText = orgText;
//        } else {
//            retText = orgText;
//        }
//
//    }


    private String address;
    private String linkName;

}
