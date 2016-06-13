/**
 * ColourPicker is a reusable dialogue to allow RGB colour selection
 * $Id: ColourPicker.java,v 1.8 2014/05/20 02:18:31 jeffnik Exp $
 */

/* JamochaMUD, a Muck/Mud client program
 * Copyright (C) 1998-2014  Jeff Robinson
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * version 2, as published by the Free Software Foundation.
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

import java.awt.*;
import java.awt.event.*;

/**
 * A resuable class to allow user selection of RBG colours
 * via three slides, each for their own colour.  (plus visual sample)
 * This dialog is for use with Java 1.1
 * @version $Id: ColourPicker.java,v 1.8 2014/05/20 02:18:31 jeffnik Exp $
 * @author Jeff Robinson
 */
public class ColourPicker extends Dialog implements ActionListener, AdjustmentListener, KeyListener {

    private Button cancelButton,  okayButton;
    private Color newColour;
    final private Frame parentFrame;
    private GridBagLayout nicePanel;
    private GridBagConstraints constraints;
    private Scrollbar redBar,  greenBar,  blueBar;
    private TextField redText,  greenText,  blueText;
    private int newRed,  newGreen,  newBlue;

    /**
     * A reusable class to allow user selection of RBG colours
     * via three slides, each for their own colour.  (plus visual sample)
     * @param frameParent The parent frame of this dialogue
     * @param titleName Title for the dialogue to be displayed to user.
     * @param currentColour Initial colour to show as selected.
     */
    public ColourPicker(Frame frameParent, String titleName, Color currentColour) {

        super(frameParent, titleName, true);
        parentFrame = frameParent;
        buildPicker(titleName, currentColour);
    }
    
    private void buildPicker(String titleName, Color currentColour) {
        // parentFrame = frameParent;

        redText = new TextField(Integer.toString(currentColour.getRed()), 3);
        greenText = new TextField(Integer.toString(currentColour.getGreen()), 3);
        blueText = new TextField(Integer.toString(currentColour.getBlue()), 3);

        // Create a GridBagLayout for this dialog
        nicePanel = new GridBagLayout();
        constraints = new GridBagConstraints();
        setLayout(nicePanel);  // Set the nicePanel layout to the dialog

        Label cPLabel = new Label(resBundle(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("red")));
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 1;
        constraints.insets = new Insets(7, 7, 2, 2);
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.CENTER;
        nicePanel.setConstraints(cPLabel, constraints);
        add(cPLabel);

        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.weightx = 4;
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.insets = new Insets(7, 2, 2, 2);
        redBar = new Scrollbar(Scrollbar.HORIZONTAL);
        redBar.setMaximum(265);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.CENTER;
        nicePanel.setConstraints(redBar, constraints);
        redBar.setValue(currentColour.getRed());
        redBar.addAdjustmentListener(this);
        add(redBar);

        constraints.gridx = 5;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.insets = new Insets(7, 2, 2, 7);
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.CENTER;
        nicePanel.setConstraints(redText, constraints);
        add(redText);
        redText.setColumns(3);
        redText.addKeyListener(this);

        cPLabel = new Label(resBundle(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("green")));
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.weightx = 1;
        constraints.insets = new Insets(2, 7, 2, 2);
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.CENTER;
        nicePanel.setConstraints(cPLabel, constraints);
        add(cPLabel);

        constraints.gridx = 1;
        constraints.gridy = 1;
        greenBar = new Scrollbar(Scrollbar.HORIZONTAL);
        greenBar.setMaximum(265);
        constraints.weightx = 4;
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.insets = new Insets(2, 2, 2, 2);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.CENTER;
        nicePanel.setConstraints(greenBar, constraints);
        greenBar.setValue(currentColour.getGreen());
        greenBar.addAdjustmentListener(this);
        add(greenBar);

        constraints.gridx = 5;
        constraints.gridy = 1;
        constraints.weightx = 1;
        constraints.insets = new Insets(2, 2, 2, 7);
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.CENTER;
        nicePanel.setConstraints(greenText, constraints);
        add(greenText);
        greenText.addKeyListener(this);

        cPLabel = new Label(resBundle(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("blue")));
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.weightx = 1;
        constraints.gridwidth = 1;
        constraints.insets = new Insets(2, 7, 3, 2);
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.CENTER;
        nicePanel.setConstraints(cPLabel, constraints);
        add(cPLabel);

        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.weightx = 4;
        blueBar = new Scrollbar(Scrollbar.HORIZONTAL);
        blueBar.setMaximum(265);
        constraints.insets = new Insets(2, 2, 3, 2);
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.CENTER;
        nicePanel.setConstraints(blueBar, constraints);
        blueBar.setValue(currentColour.getBlue());
        blueBar.addAdjustmentListener(this);
        add(blueBar);

        constraints.gridx = 5;
        constraints.gridy = 2;
        constraints.weightx = 1;
        constraints.insets = new Insets(2, 2, 3, 7);
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.NONE;
        nicePanel.setConstraints(blueText, constraints);
        add(blueText);
        blueText.addKeyListener(this);

        okayButton = new Button(resBundle(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("okay")));
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.weightx = 1;
        constraints.insets = new Insets(3, 7, 7, 2);
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.NONE;
        nicePanel.setConstraints(okayButton, constraints);
        add(okayButton);
        okayButton.addActionListener(this);

        cancelButton = new Button(resBundle(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("cancel")));
        constraints.gridx = 3;
        constraints.gridy = 3;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.weightx = 1;
        constraints.insets = new Insets(3, 2, 7, 7);
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.NONE;
        nicePanel.setConstraints(cancelButton, constraints);
        add(cancelButton);
        cancelButton.addActionListener(this);

        pack();
        this.setSize(300, 220);
    }

    /**
     * This is a generic bit to access the
     * ResReader.class, for localization
     * (Multi-language support)
     */
    private static String resBundle(final String itemTarget) {
        final ResReader reader = new ResReader();
        // return reader.langString("JamochaMUDBundle", itemTarget);
        // return reader.langString(JMConfig.BUNDLEBASE, itemTarget);
        return reader.langString(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("anecho.gui.guiBundle"), itemTarget);
    }

    /**
     * This method listens to the buttons, waiting for the user to click
     * Okay or Cancel
     * @param event Our MouseEvent
     */
    @Override
    public void actionPerformed(final ActionEvent event) {
        if (event != null) {
            this.checkActionPerformed(event.getActionCommand());
        }
//        final String arg = event.getActionCommand();
//
//        if (arg.equals(okayButton.getLabel())) {
//            newRed = redBar.getValue();
//            newGreen = greenBar.getValue();
//            newBlue = blueBar.getValue();
//            newColour = new Color(newRed, newGreen, newBlue);
//            setVisible(false);
//            dispose();
//        }
//
//        if (arg.equals(cancelButton.getLabel())) {
//            // newColour = null;
//            setVisible(false);
//            dispose();
//        }
    }

    private void checkActionPerformed(String arg) {
        if (arg.equals(okayButton.getLabel())) {
            newRed = redBar.getValue();
            newGreen = greenBar.getValue();
            newBlue = blueBar.getValue();
            newColour = new Color(newRed, newGreen, newBlue);
            setVisible(false);
            dispose();
        }

        if (arg.equals(cancelButton.getLabel())) {
            // newColour = null;
            setVisible(false);
            dispose();
        }

    }
    // This block will process the KeyEvents
    /**
     * This method response to the key being released and updates the colour
     * sample as the user types.
     * @param event Our KeyEvent
     */
    @Override
    public void keyReleased(final KeyEvent event) {
        // This will update the colour sample when the box-values change
        int value = 0;

        for (int i = 0; i <= 2; i++) {
            if (i == 0 && !redText.getText().equals(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString(""))) {
                value = Integer.parseInt(redText.getText());
            }
            if (i == 1 && !greenText.getText().equals(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString(""))) {
                value = Integer.parseInt(greenText.getText());
            }
            if (i == 2 && !blueText.getText().equals(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString(""))) {
                value = Integer.parseInt(blueText.getText());
            }

            // determine if number falls within the 0 - 255 parameters
            if (value >= 256 || value <= -1) {
                break;  // Exit loop.  Illegal number
            }

            // Since this number passed the test,
            // set the slider to the correct position

            if (i == 0) {
                redBar.setValue(value);
            }
            if (i == 1) {
                greenBar.setValue(value);
            }
            if (i == 2) {
                blueBar.setValue(value);
            }
            // Now update the colour in the sample box
            repaint();
        }

    }

    /**
     * This method response to the key being released and updates the colour
     * sample as the user types.
     * @param event Our KeyEvent
     */
    @Override
    public void keyTyped(final KeyEvent event) {
    }

    /**
     * Empty event
     * @param event Our event
     */
    @Override
    public void keyPressed(final KeyEvent event) {
    }

    // Respond to the sliders
    /**
     * This method listens to our sliders and will automatically update
     * the shown colour as the sliders move.
     * @param adj Our AdjustmentEvent
     */
    @Override
    public void adjustmentValueChanged(final AdjustmentEvent adj) {
        redText.setText(String.valueOf(redBar.getValue()));
        greenText.setText(String.valueOf(greenBar.getValue()));
        blueText.setText(String.valueOf(blueBar.getValue()));
        repaint();
    }

    /**
     * Over-ride the paint call to colour our sample box.
     * @param thing The graphics object
     */
    @Override
    public void paint(final Graphics thing) {
        thing.setColor(new Color(redBar.getValue(), greenBar.getValue(), blueBar.getValue()));
        thing.fill3DRect(25, 180, 250, 30, true);
    }

    /**
     * Set the colour pick visible or hiden.
     * @param vis <CODE>true</CODE> - Set the component visible
     * <CODE>false</CODE> - set the component hidden
     */
    @Override
    public void setVisible(final boolean vis) {
        // We'll set the location on the screen first
        this.setLocation(PosTools.findCenter(parentFrame));
        this.setResizable(false);
        super.setVisible(vis);

    }

    /**
     * Returns the currently chosen colour.
     * @return The currently chosen colour as a Java Color object.
     */
    public Color getColour() {
        return newColour;
    }

    /**
     * Return our colour as a Hex-based String
     * @return Returns a string representing the colour in the HTML-style hex mode.
     */
    public String getHexColour() {
        final java.lang.StringBuffer colour = new java.lang.StringBuffer();
        colour.append(translateHexColour(newRed));
        colour.append(translateHexColour(newGreen));
        colour.append(translateHexColour(newBlue));

        return colour.toString();
    }

    /**
     * This method translates a number to the hexadecimal format of the same colour
     * @param colour The interger from 0 to 255 representing the brightness of a colour
     * @return A String representing the hex value of the colour
     */
    private String translateHexColour(final int colour) {
        String retCol = java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("");
        try {
            if (colour > 9) {
                // retCol = new String(Integer.toHexString(colour));
                retCol = Integer.toHexString(colour);
            } else {
                // retCol = new String("0" + colour);
                retCol = java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("0") + colour;
            }
        } catch (Exception exc) {
            System.out.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("translateHexColour_error."));
        }

        if (retCol.length() == 1) {
            // Append a leading "0" to single digit numbers
            retCol = '0' + retCol;
        }
        
        return retCol;

    }
}

