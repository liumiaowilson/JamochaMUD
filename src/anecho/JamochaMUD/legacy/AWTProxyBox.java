/**
 * AWTProxyBox for JamochaMUD dialogue for configuring... yes!  Proxies!
 * $Id: AWTProxyBox.java,v 1.1 2009/06/28 04:06:27 jeffnik Exp $
 */

/* JamochaMUD, a Muck/Mud client program
 * Copyright (C) 1998-2000 Jeff Robinson
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. *
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

package anecho.JamochaMUD.legacy;

import anecho.JamochaMUD.*;
import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

// import java.util.Properties;

// import anecho.gui.ResReader;


	/**
	 * AWTProxyBox for JamochaMUD dialogue for configuring... yes!  Proxies!
         * @version $Id: AWTProxyBox.java,v 1.1 2009/06/28 04:06:27 jeffnik Exp $
        * @author Jeff Robinson
	 */
public class AWTProxyBox extends Dialog implements ActionListener, ItemListener {

    // private static Button okayButton, cancelButton;
    // private static Checkbox enableProxyCB;
    // private static TextField proxyAddress, proxyPort;
    // private static Label addressLabel, portLabel;
    private transient final Button okayButton, cancelButton;
    private transient final Checkbox enableProxyCB;
    private transient final TextField proxyAddress, proxyPort;
    private transient final Label addressLabel, portLabel;
    private transient final JMConfig settings;
    /**
     * Enables or disables debugging output
     */
    private static final boolean DEBUG = false;
    
    /**
    * 
    * @param parent 
    */
//    public AWTProxyBox(Frame parent, JMConfig mainSettings){
//     public AWTProxyBox(Frame parent, JMConfig mainSettings){
    public AWTProxyBox(Frame parent) {
        super(parent, java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("configureProxies"), true);

        // this.settings = mainSettings;
        settings = JMConfig.getInstance();

        // Design look for gridbag layout
		final GridBagLayout dLayout = new GridBagLayout();
		final GridBagConstraints constraints = new GridBagConstraints();
		this.setLayout(dLayout);

		// Add the components to the dialog
		enableProxyCB = new Checkbox(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("enableProxy"), false);
		enableProxyCB.addItemListener(this);
		constraints.insets = new Insets(7, 7, 2, 7);
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.gridheight = 1;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.WEST;
		dLayout.setConstraints(enableProxyCB, constraints);
		add(enableProxyCB);

		addressLabel = new Label(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("proxyIP"));
		constraints.insets = new Insets(2, 7, 2, 7);
		constraints.gridwidth = GridBagConstraints.RELATIVE;
		constraints.gridheight = 1;
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.CENTER;
		dLayout.setConstraints(addressLabel, constraints);
		// addressLabel.setEnabled(false);
		add(addressLabel);

		portLabel = new Label(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("proxyPort"));
		constraints.insets = new Insets(2, 7, 2, 2);
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.gridheight = 1;
		constraints.gridx = 3;
		constraints.gridy = 1;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.CENTER;
		dLayout.setConstraints(portLabel, constraints);
		// portLabel.setEnabled(false);
		add(portLabel);

		proxyAddress = new TextField("");
		constraints.insets = new Insets(2, 7, 2, 2);
		constraints.gridwidth = GridBagConstraints.RELATIVE;
		constraints.gridheight = GridBagConstraints.RELATIVE;
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.CENTER;
		dLayout.setConstraints(proxyAddress, constraints);
		// proxyAddress.setEnabled(false);
		add(proxyAddress);

		proxyPort = new TextField("");
		constraints.insets = new Insets(2, 2, 2, 7);
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.gridheight = GridBagConstraints.RELATIVE;
		constraints.gridx = 3;
		constraints.gridy = 2;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.CENTER;
		dLayout.setConstraints(proxyPort, constraints);
		// proxyPort.setEnabled(false);
		add(proxyPort);

		okayButton = new Button(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("okay"));
		okayButton.setActionCommand(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Okay"));
		constraints.insets = new Insets(2, 7, 7, 2);
		constraints.gridwidth = GridBagConstraints.RELATIVE;
		constraints.gridheight = GridBagConstraints.REMAINDER;
		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.CENTER;
		dLayout.setConstraints(okayButton, constraints);
		okayButton.addActionListener(this);
		add(okayButton);

		cancelButton = new Button(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("cancel"));
		cancelButton.setActionCommand(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Cancel"));
		constraints.insets = new Insets(2, 2, 7, 7);
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.gridheight = GridBagConstraints.REMAINDER;
		constraints.gridx = 3;
		constraints.gridy = 3;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.CENTER;
		dLayout.setConstraints(cancelButton, constraints);
		cancelButton.addActionListener(this);
		add(cancelButton);

		// Check the proxy's current status
                // ProxyStatus();
                // Our initial settings for the proxy
                // try {
                // if (sP.get("proxySet").equals("true")) {
                // if (settings.getProxy()) {
                if (settings.getJMboolean(JMConfig.PROXY)) {
                    enableProxyCB.setState(true);
                    enableProxy();
                } else {
                    disableProxy();
                    enableProxyCB.setState(false);
                }
                // Regardless of if the proxy is enabled or not,
                // we want the name to show up
                // proxyAddress.setText(settings.getProxyHost());
                proxyAddress.setText(settings.getJMString(JMConfig.PROXYHOST));
                // proxyPort.setText(settings.getProxyPort() + "");
                // proxyPort.setText(settings.getJMint(JMConfig.PROXYPORT) + "");
               proxyPort.setText(Integer.toString(settings.getJMint(JMConfig.PROXYPORT)));

		this.pack();
	}

//	public static void Content(Frame parent){
//		// Create the dialogue box
//		AWTProxyBox proxyDialogue = new AWTProxyBox(parent);
//
//		// Now set the size and location of the dialogue
//		proxyDialogue.setSize(400, 200);
//		proxyDialogue.show();
//	}

	/**
	 * This is a generic bit to access the
	 * ResReader.class, for localization
	 * (Multi-language support)
         */
//    private static String RB(String itemTarget) {
//        ResReader reader = new ResReader();
//        // return reader.langString("JamochaMUDBundle", itemTarget);
//        return reader.langString(JMConfig.BUNDLEBASE, itemTarget);
//    }

	/**
	 * Watch to see if the checkbox has changed, so that we
	 * can enable/disable the appropriate fields
	 * @param event 
	 */
	public void itemStateChanged(final ItemEvent event) {
		// The checkbox has changed, so we can enable/disable the TextFields
		if (enableProxyCB.getState()) {
			enableProxy();
		} else {
			disableProxy();
		}
	}

	/**
	 * We'll enable the proxy
	 */
	private void enableProxy() {
		// Here we'll enable the TextFields when the checkbox is 'true'
		addressLabel.setEnabled(true);
		portLabel.setEnabled(true);

		proxyAddress.setEnabled(true);
		proxyPort.setEnabled(true);
	}

	/**
	 * Proxy support is disabled, and the address/port sections are greyed out
	 */
	private void disableProxy() {
		// "Grey out" the proxy address and port (more as a visual indicator)
		addressLabel.setEnabled(false);
		portLabel.setEnabled(false);

		proxyAddress.setEnabled(false);
		proxyPort.setEnabled(false);
	}

	/**
	 * Set the initial status of the proxy IP and port
	 * which we'll derive from the JVM
	 * @param event 
	 */
    /*
	private void ProxyStatus() {
		// Read in the proxy information
		Properties sP = System.getProperties();

		// Now that we have the properties, set them to the dialogue
		try {
			if (sP.get("proxySet").equals("true")) {
				enableProxyCB.setState(true);
				EnableProxy();
			}
			// Regardless of if the proxy is enabled or not,
			// we want the name to show up
			proxyAddress.setText((String)sP.get("proxyHost"));
			proxyPort.setText((String)sP.get("proxyPort"));
			
		} catch (Exception e) {
			// Most likely there is no proxy configured
				enableProxyCB.setState(false);
				DisableProxy();
		}			
		
                }
                */


	public void actionPerformed(final ActionEvent event){
            // Either Cancel or Okay...
            // final String cmd = event.getActionCommand();
            // if (cmd.toLowerCase().equals(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("okay"))) {
            if (event.getSource() == okayButton) {
                if (DEBUG) {
                    System.err.println("ProxyBox.actionPerformed calling setOkayProxy()");
                }
                
                setOkayProxy();
                
                //			// User chose 'okay', so apply these new settings
                //        		try {
                //				// Properties pS = System.getProperties();
                //				if (enableProxyCB.getState()) {
                //					System.getProperties().put("proxySet", "true");
                //				} else {
                //					System.getProperties().put("proxySet", "false");
                //				}
                //
                //				if (proxyAddress.getText().trim().equals("")) {
                //					System.getProperties().put("proxySet", "false");
                //				} else {
                //					System.getProperties().put("proxyHost", proxyAddress.getText().trim());
                //					System.getProperties().put("proxyPort", proxyPort.getText().trim());
                //				}
                //			} catch (Exception e) {
                //				System.out.println("AWTProxyBox OKAY error " + e);
                //			}
            }

            // By default, the user has chosen 'cancel'
            // hide();
            setVisible(false);
            dispose();
        }

    /** Put our new settings into our configuration file */
    private void setOkayProxy() {
        if (DEBUG) {
            System.err.println("ProxyBox.setOkayProxy: " + enableProxyCB.getState() + " " + proxyAddress.getText() + ":" + proxyPort.getName());
        }
        settings.setJMValue(JMConfig.PROXY, enableProxyCB.getState());
        settings.setJMValue(JMConfig.PROXYHOST, proxyAddress.getText().trim());
        settings.setJMValue(JMConfig.PROXYPORT, proxyPort.getText().trim());
    }

}

