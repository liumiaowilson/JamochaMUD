/**
 * SplashScreen creates easy (AND FUN!) splash screens for programs
 * $Id: SplashScreen.java,v 1.5 2009/11/23 01:36:30 jeffnik Exp $
 */

/* JamochaMUD, a Muck/Mud client program
 * Copyright (C) 1998-2009  Jeff Robinson
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
*/

package anecho.gui;

import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.JWindow;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * SplashScreen creates easy (AND FUN!) splash screens for programs
 */
public class SplashScreen extends JWindow {

    /**
     * This label will display messages to the user
     */
    private transient JLabel messageLabel;

    /**
    * Create a blank SplashScreen
    * @param parent The parent component of this SplashScreen window.
    */
    public SplashScreen(final Frame parent) {
        this(parent, java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString(""), (ImageIcon)null);
    }

    /**
    * Create a SplashScreen with an initial message
    * @param parent The parent component of this SplashScreen window.
    * @param message The initial message to be displayed on our SplashScreen.
    */
    public SplashScreen(final Frame parent, final String message) {
        this(parent, message, (ImageIcon)null);
    }

    /**
    * Create a SplashScreen with an initial message and an imageFile
    * @param parent The parent component of this SplashScreen window.
    * @param message The initial message to be displayed on our SplashScreen.
    * @param imageFile An image to be shown along with any message on the SplashScreen.
    */
    public SplashScreen(final Frame parent, final String message, final ImageIcon imageFile) {
        super(parent);

        Dimension labelSize = new Dimension(2, 2);

        if (imageFile != null) {
            final JLabel picture = new JLabel(imageFile);
            getContentPane().add(picture, BorderLayout.CENTER);
            picture.getAccessibleContext().setAccessibleDescription("Loading...");
            labelSize = picture.getPreferredSize();
        }

        if (message != null && !message.equals(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString(""))) {
            messageLabel = new JLabel(message);
            messageLabel.getAccessibleContext().setAccessibleDescription(message); // For accessibility
            getContentPane().add(messageLabel, BorderLayout.SOUTH);
        }

        pack();

        final Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screenSize.width/2 - (labelSize.width/2),
                    screenSize.height/2 - (labelSize.height/2));
    }

    /**
    * Update an existing message on the splash screen
    * @param message The message to be shown on our SplashScreen.
    */
    public synchronized void updateMessage(final String message) {
        if (messageLabel != null) {
            messageLabel.setText(message);
            messageLabel.getAccessibleContext().setAccessibleDescription(message); // For accessibility
        }
    }
}