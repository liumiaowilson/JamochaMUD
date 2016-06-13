/**
 * AboutBox for JamochaMUD (to display license, thanks, etc...) 
 * $Id: AboutBox.java,v 1.64 2015/08/30 22:43:32 jeffnik Exp $
 */

/*
 * JamochaMUD, a Muck/Mud client program Copyright (C) 1998-2015 Jeff Robinson
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2, as published by the
 * Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */
package anecho.JamochaMUD;

import anecho.gui.JMText;
import anecho.gui.PosTools;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * A Dialogue box to display licensing information and credits/thanks for the
 * development of JamochaMUD.
 *
 * @version $Id: AboutBox.java,v 1.64 2015/08/30 22:43:32 jeffnik Exp $
 * @author Jeff Robinson
 */
public class AboutBox extends Dialog implements ActionListener, KeyListener {

    /**
     * This button is used by the user to toggle between the credits and license
     * of JamochaMUD.
     */
    private transient final Button okayButton;
    /**
     * This button is used by the user to dismiss the "About JamochaMUD"
     * dialogue box.
     */
    private transient final Button cancelButton;
    /**
     * Current version number of JamochaMUD
     */
    public static final String VERNUM = "5.11";
    /**
     * Build number of this version of JamochaMUD
     */
    public static final String BUILDNUM = "2015-08-30";
    /**
     * Complete version of JamochaMUD made of version number and build number
     */
    public static final String FULLVERNUM = VERNUM + " build " + BUILDNUM;
    /**
     * The JMText item used to display either credits or licensing information.
     */
    private transient final JMText TEXT;
    private static transient final String RESET = "[0m\n";
    private static final String LANGBUNDLE = "anecho/JamochaMUD/JamochaMUDBundle";

    /**
     * The constructor that allows up to show the JamochaMUD AboutBox.
     * @param parentFrame The parent frame for our about-box.
     */
    public AboutBox(final Frame parentFrame) {
        super(parentFrame, "About JamochaMUD (Version: " + FULLVERNUM + ")", true);
        final JMConfig settings = JMConfig.getInstance();

        // The "About Box" for JamochaMUD information

        // Design look for gridbag layout
        final GridBagLayout dLayout = new GridBagLayout();
        final GridBagConstraints constraints = new GridBagConstraints();
        this.setLayout(dLayout);

        // Add the components to the dialog

        TEXT = new JMText("", 80, 10, JMText.SCROLLBARS_VERTICAL_ONLY);
        TEXT.setMaxRows(50);

        TEXT.setEditable(false);
        TEXT.addKeyListener(this);
        constraints.insets = new Insets(7, 7, 2, 7);
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.gridheight = GridBagConstraints.RELATIVE;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        dLayout.setConstraints(TEXT, constraints);
        add(TEXT);

        okayButton = new Button(java.util.ResourceBundle.getBundle(LANGBUNDLE).getString("credits"));
        constraints.insets = new Insets(2, 7, 7, 2);
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.gridheight = 1;
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = 0;
        constraints.weighty = 0;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.WEST;
        dLayout.setConstraints(okayButton, constraints);
        okayButton.addActionListener(this);
        add(okayButton);

        cancelButton = new Button(java.util.ResourceBundle.getBundle(LANGBUNDLE).getString("thanksalot!"));
        constraints.insets = new Insets(2, 2, 7, 7);
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.gridheight = 1;
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.weightx = 0;
        constraints.weighty = 0;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.EAST;
        dLayout.setConstraints(cancelButton, constraints);
        cancelButton.addActionListener(this);
        add(cancelButton);

        this.pack();

        TEXT.setFont(settings.getJMFont(JMConfig.FONTFACE));
        TEXT.setBackground(settings.getJMColor(JMConfig.BACKGROUNDCOLOUR));
        TEXT.setForeground(settings.getJMColor(JMConfig.FOREGROUNDCOLOUR));

        final Point centre = PosTools.findCenter();
        setLocation(centre.x - 237, centre.y - 185);
        setSize(475, 370);

        displayLicense();


    }

    /**
     * Information pertaining to the GPL and copyright
     */
    private void displayLicense() {
        TEXT.setText("");

        // Write the GPL information
        TEXT.append('\u001b' + "[32mJamochaMUD - a (Java) MUD/MUCK client\n");
        TEXT.append("Copyright (C) 1998-2015  Jeff Robinson (jeffnik@anecho.mb.ca)\n");
        TEXT.append('\u001b' + "[0m\n");
        TEXT.append("This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or any later version.\n\n");
        // TEXT.append("\n");
        TEXT.append("This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.\n\n");
        // TEXT.append("\n");
        TEXT.append("You should have received a copy of the GNU General Public License  along with this program; if not, write to the\n");
        TEXT.append("Free Software Foundation, Inc., 59 Temple Place - Suite 330,\n");
        TEXT.append("Boston, MA  02111-1307, USA.\n");
        okayButton.setLabel(java.util.ResourceBundle.getBundle(LANGBUNDLE).getString("credits"));
    }

    /**
     * Changes the TextArea to show the credits for JamochaMUD
     */
    private void showCredits() {
        // First, empty the TEXT area
        TEXT.setText("");

        // Write the credits
        // In the future, perhaps this will be on a Canvas instead
//        TEXT.append("Dedicated to the memory of F. Ross Browne; uncle, instigator, inspiration, and curmudgeon.\n\n");
//        TEXT.append('\u001b' + "[1mJeff Robinson - High(ly caffeinated) Muckymuck (jeffnik@anecho.mb.ca)\n" + '\u001b' + "[0m\n");
//        TEXT.append("Jason Holmgren - Annoying Vermin\n");
//        TEXT.append("Sean Simpson - Head Haranguer (Strnig)\n");
//        TEXT.append("Sara Palmer - Chief of Wahness\n");
//        TEXT.append("Sandi Wilkinson - Wizzywizz\n");
//        TEXT.append("Andrea Adams - Clue-by-four handler\n");
//        TEXT.append("Bjoern Weber - Socks(5) for fox!\n");
//        TEXT.append("Leonid Konkov - Language logistics\n");
//        TEXT.append("Stephane Boisjoli - Text-chopping magic\n\n");
//        TEXT.append("Translations:\n");
//        TEXT.append("Miguel Estrugo - Man of many words (Spanish and Italian)\n");
//        TEXT.append("Mark Straver - Dutch (No, Dutch is *NOT* baby-German!) \n");
//        TEXT.append("Andr� Schieleit - German!  Yay!\n\n");
//        TEXT.append("Sloggers:\n");
//        TEXT.append("Rocko - Official JMUD-mangler\n");
//        TEXT.append("Caroline Blight - Chat-a-cat\n");
//        TEXT.append("PaulTB - Japanese is a difficult language?!\n");
//        TEXT.append("Thufir - Work properly?  Not unreasonable, I guess.\n");
//        TEXT.append("Lucy Handfield - My God, it's (not) full of colours!\n");
//        TEXT.append("Kiv - Sure, not being able to start the program may slow some folks down...\n");
//        TEXT.append("\n");
//        TEXT.append("A great deal of thanks to Matthias L. Jugel and Marcus Mei�ner for their Java telnet app (http://www.first.gmd.de/persons/leo/java/Telnet).\n");
//        TEXT.append("\n");
//        TEXT.append('\u001b' + "[1mCrystal Icon Theme" + '\u001b' + "[0m\n");
//        TEXT.append("Created by Everaldo Coelho for KDE.  http://www.everaldo.com\n");
        TEXT.append("Dedicated to the memory of F. Ross Browne; uncle, instigator, inspiration, and curmudgeon.\n\n");
        TEXT.append('\u001b' + "[1mJeff Robinson - High(ly caffeinated) Muckymuck (jeffnik@anecho.mb.ca)\n" + '\u001b' + RESET);
        TEXT.append("Jason Holmgren - Annoying Vermin\n");
        TEXT.append("Sean Simpson - Head Haranguer (Strnig)\n");
        TEXT.append("Sara Palmer - Chief of Wahness\n");
        TEXT.append("Sandi Wilkinson - Wizzywizz\n");
        TEXT.append("Andrea Adams - Clue-by-four handler\n");
        TEXT.append("Bjoern Weber - Socks(5) for fox!\n");
        TEXT.append("Leonid Konkov - Language logistics\n");
        TEXT.append("Stephane Boisjoli - Text-chopping magic\n\n");

        TEXT.append('\u001b' + "1[Plug-ins:" + '\u001b' + RESET);
        TEXT.append("Ben Dehner - b.dehner@cox.net - PathWalker plug-in\n\n");

        TEXT.append('\u001b' + "[1mTranslations:" + '\u001b' + RESET);
        TEXT.append("Miguel Estrugo - Man of many words (Spanish and Italian)\n");
        TEXT.append("Mark Straver - Dutch (No, Dutch is *NOT* baby-German!)\n");
        TEXT.append("Andr� Schieleit - German!  Yay!\n\n");

        TEXT.append('\u001b' + "[1mSloggers:" + '\u001b' + RESET);
        TEXT.append("Rocko - Official JMUD-mangler\n");
        TEXT.append("Caroline Blight - Chat-a-cat\n");
        TEXT.append("PaulTB - Japanese is a difficult language?!\n");
        TEXT.append("Thufir - Work properly?  Not unreasonable, I guess.\n");
        TEXT.append("Lucy Handfield - My God, it's (not) full of colours!\n");
        TEXT.append("\n");
        TEXT.append("A great deal of thanks to Matthias L. Jugel and Marcus Mei�ner for their Java telnet app\n");
        TEXT.append("( http://javassh.org ).\n");
        TEXT.append("\n");
        TEXT.append('\u001b' + "[1mCrystal Icon Theme" + '\u001b' + RESET);
        TEXT.append("Created by Everaldo Coelho for KDE.  http://www.everaldo.com\n");


        // Change the button to 'About'
        okayButton.setLabel(java.util.ResourceBundle.getBundle(LANGBUNDLE).getString("aboutJamochaMUD"));
    }

    /**
     * Check for button presses
     * @param event The event used to determine the button pressed.
     */
    public void actionPerformed(final ActionEvent event) {
        final String arg = event.getActionCommand();

        if (arg.equals(cancelButton.getLabel())) {
            // dismiss the dialogue
            dispose();
        }

        if (arg.equals(okayButton.getLabel())) {
            // Check to see which function to call
            if (arg.equals(java.util.ResourceBundle.getBundle(LANGBUNDLE).getString("credits"))) {
                // Change to display the credits
                showCredits();
            } else {
                // Change to display the license
                displayLicense();
            }

            // reflow the layout of the button, incase the size of
            // the writing is different.
            okayButton.invalidate();
            okayButton.getParent().validate();

            // Make certain we start displaying the first line
            // of the TEXT and not the last
            TEXT.setVerticalScrollbarPos(0);

        }

    }

    /**
     * Consume any keystrokes that are in the TextArea
     * @param event Any keypresses received.
     */
    public void keyPressed(final KeyEvent event) {
        event.consume();
    }

    /**
     * 
     * @param event 
     */
    @Override
    public void keyTyped(final KeyEvent event) {
    }

    /**
     * 
     * @param event 
     */
    @Override
    public void keyReleased(final KeyEvent event) {
    }
}
