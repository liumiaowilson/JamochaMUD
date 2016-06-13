/** * ManagePlugins.java - A dialogue for managing plugins
 * $Id: ManagePlugins.java,v 1.8 2015/08/30 22:43:32 jeffnik Exp $
 */

/* JamochaMUD, a Muck/Mud client program
 * Copyright (C) 1998-2007  Jeff Robinson
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
package anecho.JamochaMUD;

import java.awt.*;
import java.awt.event.*;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import java.util.Vector;

// import anecho.gui.PosTools;
// import anecho.gui.ResReader;

import anecho.JamochaMUD.plugins.PlugInterface;

/**
 * ManagePlugins.java - A dialogue for managing plugins
 * @version $Id: ManagePlugins.java,v 1.8 2015/08/30 22:43:32 jeffnik Exp $
 * @author Jeff Robinson
 */
class ManagePlugins extends Dialog implements ActionListener, ItemListener {

    private Button gButton, propertiesB;
    private GridBagLayout mpLayout;
    private GridBagConstraints constraints;
    private Label gLabel;
    // private static List activeList, inactiveList, fullList;
    private List activeList, inactiveList;
    private TextArea desc;
    // private Dialog mPDialog;
    // private static ManagePlugins managePluginsFrame;
    // private ManagePlugins mpFrame;
    private JMConfig settings;
    // private EnumPlugIns enum;       // Our "private" plug-in enumerator
    private String selPlugIn;
    private static final boolean DEBUG = false;

    /** This constructor is for using ManagePlugins without a GUI
     */
//        public ManagePlugins(JMConfig mainSettings) {
    public ManagePlugins() {

        // Ugly hack to get things working!
        super(new Frame(), "");

        settings = JMConfig.getInstance();

    }

    /**
     * Full-blown GUI for manage plug-ins.  This is now deprecated.
     * @deprecated 
     * @param frameParent 
     * @param mainSettings 
     */
    public ManagePlugins(Frame frameParent, JMConfig mainSettings) {

        // Create the dialog



        // super(frameParent, "JamochaMUD - " + resReader("ManagePlugins.title"), true);

        super(frameParent, java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("JamochaMUD_-_") + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("ManagePlugins.title"), true);

        this.settings = mainSettings;

        // this.enum = settings.getEnumerator(); // a pointer to our plugin enumerator

        // this.enum = EnumPlugIns.getInstance(settings); // a pointer to our plugin enumerator



        mpLayout = new GridBagLayout();

        constraints = new GridBagConstraints();



        // Set new layout



        setLayout(mpLayout);



        // Display the list of selected pictures

        // gLabel = new Label(resReader("activePlugins"));

        gLabel = new Label(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("activePlugins"));

        // constraints.gridwidth = 2;

        constraints.gridwidth = 3;

        constraints.gridheight = 1;

        constraints.gridx = 0;

        constraints.gridy = 0;

        constraints.weightx = 0;

        constraints.weighty = 0;

        constraints.insets = new Insets(7, 7, 2, 2);

        constraints.fill = GridBagConstraints.BOTH;

        constraints.anchor = GridBagConstraints.SOUTHWEST;

        mpLayout.setConstraints(gLabel, constraints);

        add(gLabel);



        // gLabel = new Label(resReader("inactivePlugins"));

        gLabel = new Label(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("inactivePlugins"));

        // constraints.gridwidth = 2;

        constraints.gridwidth = 3;

        constraints.gridheight = 1;

        // constraints.gridx = 2;

        constraints.gridx = 3;

        constraints.gridy = 0;

        constraints.weightx = 0;

        constraints.weighty = 0;

        constraints.insets = new Insets(7, 2, 2, 2);

        constraints.fill = GridBagConstraints.BOTH;

        constraints.anchor = GridBagConstraints.SOUTHWEST;

        mpLayout.setConstraints(gLabel, constraints);

        add(gLabel);



        // Add the 3 list items

        activeList = new List();

        // constraints.gridwidth = 2;

        constraints.gridwidth = 3;

        constraints.gridheight = 8;

        constraints.gridx = 0;

        constraints.gridy = 1;

        constraints.weightx = 3;

        constraints.weighty = 0;

        constraints.insets = new Insets(2, 7, 2, 7);

        constraints.fill = GridBagConstraints.BOTH;

        constraints.anchor = GridBagConstraints.CENTER;

        mpLayout.setConstraints(activeList, constraints);

        activeList.addItemListener(this);

        add(activeList);



        inactiveList = new List();

        // constraints.gridwidth = 2;

        constraints.gridwidth = 3;

        constraints.gridheight = 8;

        // constraints.gridx = 2;

        constraints.gridx = 3;

        constraints.gridy = 1;

        constraints.weightx = 3;

        constraints.weighty = 0;

//			constraints.insets = new Insets(2, 2, 2, 2);

        constraints.insets = new Insets(2, 2, 2, 7);

        constraints.fill = GridBagConstraints.BOTH;

        constraints.anchor = GridBagConstraints.CENTER;

        mpLayout.setConstraints(inactiveList, constraints);

        inactiveList.addItemListener(this);

        add(inactiveList);



        // Add the button for each column

        // gButton = new Button(resReader("removeWithArrows"));

        gButton = new Button(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("removeWithArrows"));

        gButton.setActionCommand(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("remove"));

        constraints.gridwidth = 2;

        constraints.gridheight = 1;

        constraints.gridx = 0;

        constraints.gridy = 9;

        constraints.weightx = 0;

        constraints.weighty = 0;

        constraints.insets = new Insets(7, 7, 2, 2);

        constraints.fill = GridBagConstraints.NONE;

        constraints.anchor = GridBagConstraints.CENTER;

        mpLayout.setConstraints(gButton, constraints);

        add(gButton);

        gButton.addActionListener(this);





        // gButton = new Button(resReader("addWithArrows"));

        gButton = new Button(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("addWithArrows"));

        gButton.setActionCommand(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("add"));

        constraints.gridwidth = 2;

        constraints.gridheight = 1;

        constraints.gridx = 2;

        constraints.gridy = 9;

        constraints.weightx = 0;

        constraints.weighty = 0;

        constraints.insets = new Insets(7, 2, 2, 2);

        constraints.fill = GridBagConstraints.NONE;

        constraints.anchor = GridBagConstraints.CENTER;

        mpLayout.setConstraints(gButton, constraints);

        add(gButton);

        gButton.addActionListener(this);



        // gButton = new Button(resReader("refresh"));

        gButton = new Button(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("refresh"));

        gButton.setActionCommand(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("refresh"));

        constraints.gridwidth = 2;

        constraints.gridheight = 1;

        constraints.gridx = 4;

        constraints.gridy = 9;

        constraints.weightx = 0;

        constraints.weighty = 0;

        constraints.insets = new Insets(7, 2, 2, 7);

        constraints.fill = GridBagConstraints.NONE;

        constraints.anchor = GridBagConstraints.CENTER;

        mpLayout.setConstraints(gButton, constraints);

        gButton.setEnabled(false);

        add(gButton);

        gButton.addActionListener(this);



        // Add the Okay and Cancel buttons

        // gButton = new Button(resReader("okay"));

        gButton = new Button(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("okay"));

        gButton.setActionCommand(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("okay"));

        constraints.gridwidth = 2;

        constraints.gridheight = 1;

        constraints.gridx = 0;

        constraints.gridy = 10;

        constraints.weightx = 0;

        constraints.weighty = 0;

        constraints.insets = new Insets(2, 7, 2, 2);

        constraints.fill = GridBagConstraints.NONE;

        constraints.anchor = GridBagConstraints.CENTER;

        mpLayout.setConstraints(gButton, constraints);

        add(gButton);

        gButton.addActionListener(this);



        // gButton = new Button(resReader("cancel"));

        gButton = new Button(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("cancel"));

        gButton.setActionCommand(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("cancel"));

        constraints.gridwidth = 2;

        constraints.gridheight = 1;

        constraints.gridx = 2;

        constraints.gridy = 10;

        constraints.weightx = 0;

        constraints.weighty = 0;

        constraints.insets = new Insets(2, 7, 2, 2);

        constraints.fill = GridBagConstraints.NONE;

        constraints.anchor = GridBagConstraints.CENTER;

        mpLayout.setConstraints(gButton, constraints);

        add(gButton);

        gButton.addActionListener(this);



        // gButton = new Button(resReader("properties"));

        // gButton.setActionCommand("properties");

        // propertiesB = new Button(resReader("properties"));

        propertiesB = new Button(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("properties"));

        propertiesB.setActionCommand(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("properties"));

        constraints.gridwidth = 2;

        constraints.gridheight = 1;

        constraints.gridx = 4;

        constraints.gridy = 10;

        constraints.weightx = 2;

        constraints.weighty = 0;

        constraints.insets = new Insets(2, 2, 2, 7);

        constraints.fill = GridBagConstraints.NONE;

        constraints.anchor = GridBagConstraints.CENTER;

        // mpLayout.setConstraints(gButton, constraints);

        mpLayout.setConstraints(propertiesB, constraints);

        // add(gButton);

        add(propertiesB);

        propertiesB.setEnabled(false);

        propertiesB.addActionListener(this);



        desc = new TextArea("", 4, 80, TextArea.SCROLLBARS_VERTICAL_ONLY);

        // constraints.gridwidth = GridBagConstraints.REMAINDER;

        // constraints.gridheight = GridBagConstraints.REMAINDER;

        constraints.gridwidth = 6;

        constraints.gridheight = GridBagConstraints.REMAINDER;

        constraints.gridx = 0;

        constraints.gridy = 11;

        constraints.weightx = 6;

        constraints.weighty = 1;

        constraints.insets = new Insets(2, 7, 7, 7);

        constraints.fill = GridBagConstraints.NONE;

        constraints.anchor = GridBagConstraints.CENTER;

        mpLayout.setConstraints(desc, constraints);

        add(desc);

        desc.setEditable(false);



        pack();

    }

    /**
     * This is a generic bit to access the
     * ResReader.class, for localization
     * (Multi-language support)
     */
//        private static String resReader(final String itemTarget) {
//            final ResReader reader = new ResReader();
//            // return reader.langString("JamochaMUDBundle", itemTarget);
//            return reader.langString(JMConfig.BUNDLEBASE, itemTarget);
//        }
    // This supports multi-line messages

    /*
    private Vector RBL(String itemTarget) {
    ResReader reader = new ResReader();
    // return reader.LangVector("JamochaMUDBundle", itemTarget);
    return reader.LangVector(JMConfig.BUNDLEBASE, itemTarget);
    }
     */
    public void actionPerformed(final ActionEvent event) {

        if (event != null) {
            final String arg = event.getActionCommand();

            processActionPerformed(arg);
        }
    }

    /**
     * 
     * @param event 
     */
    // public void actionPerformed(final ActionEvent event){
    public void processActionPerformed(final String arg) {

        // final String arg = event.getActionCommand();

        if (arg.equals(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("remove")) && (activeList.getSelectedItem() != null)) {
            // Removed selected from list
            // Check to see if an item is selected first
            // if (activeList.getSelectedItem() != null) {
            inactiveList.add(activeList.getSelectedItem());
            activeList.remove(activeList.getSelectedItem());
            // }
        }

        if (arg.equals(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("add")) && (inactiveList.getSelectedItem() != null)) {
            // Add selected from list
            // Check to see if an item is selected first
            // if (inactiveList.getSelectedItem() != null) {
            activeList.add(inactiveList.getSelectedItem());
            inactiveList.remove(inactiveList.getSelectedItem());
            // }
        }

        if (arg.equals(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("okay"))) {
            // Change active plugins

            // Write changes to the .plugins.rc file
            setPlugInStatus();

            // mpFrame.setVisible(false);
            this.setVisible(false);
            dispose();
        }

        if (arg.equals(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("cancel"))) {
            // Close dialogue, make no changes
            this.setVisible(false);
            dispose();
        }

        if (arg.equals(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("properties"))) {
            // Check to see if there is a selected plugin
            if (selPlugIn.equals("")) {
                return;
            }
            // Call the properties menu for the selected plugin
            try {
                // This is sloppy.  Fix this XXX!!
                final Object plugClass = EnumPlugIns.classByName(selPlugIn);
                ((PlugInterface) plugClass).plugInProperties();

                // Apply any changes we have to our configuration file, as we don't come back!!
                setPlugInStatus();

                this.dispose();

            } catch (ArrayIndexOutOfBoundsException exc) {
                System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("(ManagePlugins)_Out_of_bounds_exception"));
                exc.printStackTrace();
                // The array index was out of bounds
                // Chances are no plugin was selected
                // for properties to be viewed
            } catch (Exception exc) {
                System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("(ManagePlugins)_Plugin_Property_Exception_") + exc);
                exc.printStackTrace();
            }
        }

        if (arg.equals(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("refresh"))) {
            // Call the refresh plugins method
            jmRefreshPlugIns();
        }

    }

    @Override
    public void itemStateChanged(final ItemEvent iEvt) {
        if (iEvt != null) {
            processItemStateChanged(iEvt.getSource());
        }
    }

    /**
     * 
     * @param iEvt 
     */
    // public void itemStateChanged(final ItemEvent iEvt) {
    public void processItemStateChanged(Object eSource) {
        // String plugName;
        // int choice = 0;

        // Make certain only one item is selected at a time
        // if (iEvt.getSource() == activeList) {
        if (activeList != null) {
            if (eSource == activeList) {
                inactiveList.deselect(inactiveList.getSelectedIndex());
                selPlugIn = activeList.getItem(activeList.getSelectedIndex());
            } else {
                activeList.deselect(activeList.getSelectedIndex());
                selPlugIn = inactiveList.getItem(inactiveList.getSelectedIndex());
            }

            // Now that we have a selected item, we can continue
            final PlugInterface plugInClass = (PlugInterface) (EnumPlugIns.classByName(selPlugIn));

            // String description = EnumPlugIns.Description(selPlugIn);
            final String description = plugInClass.plugInDescription();

            // Check to see if the plug-in has properties and enable/disable our "properties" button
            if (plugInClass.hasProperties()) {
                propertiesB.setEnabled(true);
            } else {
                propertiesB.setEnabled(false);
            }

            // Set the description
            desc.setText(description);
            desc.setCaretPosition(0); // Just to make sure we can see the beginning
        }

    }

    /**
     * This reads in all the plugins and their settings (whether active/inactive)
     * and then formats them to be displayed in the appropriate columns
     */
    public void listPlugins() {

        // This will fill the 3 lists with the appropriate info

        String tempName;

        // Vector plugInStatus = settings.getPlugInStatus();

        // Vector plugInName = settings.getPlugInName();

        final Vector plugInStatus = settings.getJMVector(JMConfig.PLUGINSTATUS);

        final Vector plugInName = settings.getJMVector(JMConfig.PLUGINNAME);



        // First, we empty the existing lists

        if (activeList != null) {
            activeList.removeAll();
        }

        if (inactiveList != null) {
            inactiveList.removeAll();
        }

        // Now, loop through the names and fill the lists
        if (plugInName != null) {

            for (int i = 0; i < plugInName.size(); i++) {
                // tempName = (String)EnumPlugIns.plugInName.elementAt(i);
                tempName = (String) plugInName.elementAt(i);

                final String temp = ((String) plugInStatus.elementAt(i)).toLowerCase();
                if (temp.equals(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("true"))) {
                    // Plugin is active
                    activeList.add(tempName);
                } else {
                    // Plugin is inactive
                    inactiveList.add(tempName);
                }
            }
        }

    }

    /**
     * Write the changes to the plugins to the .plugins.rc file
     */
    private void setPlugInStatus() {
        // Make sure the plugInStatus vector is up to date
        String tempName;
        final Vector plugInName = new Vector(0, 1);
        final Vector plugInStatus = new Vector(0, 1);
        final Vector aListName = new Vector(0, 1);

        final Vector fullList = settings.getJMVector(JMConfig.PLUGINNAME);
        final int fullSize = fullList.size();
        final int activeSize = activeList.getItemCount();

        final EnumPlugIns tempEnum = EnumPlugIns.getInstance();

        // This seems like an awfully messy way of doing this!  Fix me XXX!!
        for (int i = 0; i < fullSize; i++) {
            tempName = (String) fullList.elementAt(i);
            plugInName.addElement(tempName);
            plugInStatus.addElement(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("false"));
            tempEnum.deactivate(i);

            for (int j = 0; j < activeSize; j++) {
                if (activeList.getItem(j).equals(tempName)) {
                    aListName.addElement(tempName);
                    plugInStatus.setElementAt(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("true"), i);
                    // EnumPlugIns.plugInStatus.setElementAt("true", i);
                    // EnumPlugIns.activate(i);
                    tempEnum.activate(i);
                }
            }

        }

        writePlugFile(aListName);

        // Now write the status to the hashtable
        settings.setJMValue(JMConfig.PLUGINNAME, plugInName);
        settings.setJMValue(JMConfig.PLUGINSTATUS, plugInStatus);
    }

    /**
     * Refresh the list of plugins to show any changes that may have
     * occured (adding, removing, etc).
     */
    private void jmRefreshPlugIns() {

        listPlugins();

    }

    /**
     * A method to allow other classes to enable/disable plugins
     * If our state is positive then we'll activate the plug-in, otherwise we deactivate
     * @param name 
     * @param state 
     */
    public void changePlugInState(final String name, final boolean state) {

        // Cycle through our list of plug-ins and add them back to the menu

        final Vector aListName = new Vector();

        final Vector pClasses = EnumPlugIns.plugInClass;

        String checkName;

        final int pcs = pClasses.size();

        PlugInterface plug;



        for (int i = 0; i < pcs; i++) {



            plug = (PlugInterface) (pClasses.elementAt(i));

            checkName = plug.plugInName();



            if (checkName.equals(name)) {

                if (state) {

                    if (DEBUG) {

                        System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("ManagePlugins_Calling_activate_on_") + plug.plugInName());

                    }

                    // (((PlugInterface)pClasses).elementAt(i)).activate();

                    plug.activate();

                } else {

                    if (DEBUG) {

                        System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("ManagePlugins_Calling_Deactivate_on_") + plug.plugInName());

                    }

                    // (((PlugInterface)pClasses).elementAt(i)).deactivate();

                    plug.deactivate();

                }

            }



            if (plug.isActive()) {

                aListName.addElement(plug.plugInName());

            }

        }



        // Rebuild our plug-ins menu

        // final MuckMain tempMain = settings.getMainWindowVariable();

        final MuckMain tempMain = MuckMain.getInstance();

        tempMain.rebuildPlugInMenu();

        writePlugFile(aListName);

    }

    /** This method writes out our list of active names to a file
     */
    private void writePlugFile(final Vector aListName) {

        // Now write the changes to the .plugins.rc file

        try {

            // FileOutputStream outputFile = new FileOutputStream(settings.getUserDirectory() + ".plugins.rc");

            final FileOutputStream outputFile = new FileOutputStream(settings.getJMString(JMConfig.USERDIRECTORY) + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString(".plugins.rc"));

            final ObjectOutputStream sStream = new ObjectOutputStream(outputFile);

            // sStream.writeObject(activeList);

            sStream.writeObject(aListName);

            sStream.flush();

        } catch (Exception exc) {

            System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Serialization_error_") + exc);

        }

    }
}

