/**

 * PlugInstaller allows JamochaMUD users a convenient way to

 * add and remove plug-ins from JamochaMUD.

 * $Id: PlugInInstaller.java,v 1.6 2012/03/11 03:44:51 jeffnik Exp $

 */



/* JamochaMUD, a Muck/Mud client program

 * Copyright (C) 1998-2005 Jeff Robinson

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



package anecho.JamochaMUD;



import java.awt.*;

import java.awt.event.*;

import java.io.*;

import java.util.Enumeration;

import java.util.Vector;

import java.util.zip.*;



public class PlugInInstaller implements ActionListener {

    

    JMConfig settings;

    boolean userDir = false;

    boolean masterDir = false;

    boolean useSwing = false;

    boolean msgResponse = false; // Return message from AWT dialogue

    Dialog msg;

    private static final boolean DEBUG = false;

    

    String masterPath, userPath;

    int buffer = 2048;

    private static final int SUCCESS = 0;

    private static final int UNPACK_ERROR = 1;

    private static final int NO_MANIFEST_ERROR = 2;

    // FileOutputStream outStream;

    // BufferedOutputStream buffOutStream;

    

    /**

     * 

     */

//    public PlugInInstaller(JMConfig mainSettings) {

    public PlugInInstaller() {

        // settings = mainSettings;

        settings = JMConfig.getInstance();

        if (settings.getJMboolean(JMConfig.USESWING)) {

            useSwing = true;

        }

    }

    

    /** Display a file dialogue so a user may install a new plug-in.
     * To make things easier, we will require JamochaMUD plug-ins to
     * be packaged in plain old .zip files, but bearing the extension
     * .jpa (JamochaMUD Plug-in Archive).  This zipfile will contain
     * a MANIFEST file listing, and example of which follows with
     * comments in brackets (only NAME and PLUGCLASS are REQUIRED):
     * NAME: Test Plug-in (Name of the plug-in)
     * VERSION: 1.0 (Version of the plug-in)
     * PLUGCLASS: TestPlugIn (Name of the main Java class of the plug-in)
     * JVM: 1.1 (Minimum Java Virtual Machine needed to use the plug-in)
     * JMUDVER: 04-01-12 (Minimum version of JamochaMUD needed... numeric portion only)
     * LICENSE: TestPlugInDir\COPYING (location in the .jpa archive of the license file)
     * README: TestPlugInDir\readme.txt (any special instructions for the user)
     */
    public void install() {
        final Frame parent = MuckMain.getInstance().getMainFrame();
        
        String plugPath = null;
        
        if (useSwing) {
            plugPath = swingDialog(parent);
        } else {
            plugPath = awtDialog(parent);
        }
        
        masterPath = settings.getJMString(JMConfig.MASTERPLUGINDIR);
        userPath = settings.getJMString(JMConfig.USERPLUGINDIR);
        
        // if (installPath.getFile() != null) {
        if (plugPath != null) {
            // java.io.File installPath = new java.io.File(filePathName);
            // String plugPath;
            
            try {
                // String plugPath = installPath.getDirectory() + installPath.getFile();
                // plugPath = installPath.getPath() + installPath.pathSeparator + installPath.getName();
                
                // Select location(s) for plug-in installation
                if (!selectTargets()) {
                    // The user didn't select a directory so we'll leave
                    if (useSwing) {
                        javax.swing.JOptionPane.showMessageDialog(MuckMain.getInstance().getMainFrame(),
                                java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("An_installation_directory_was_not_selected._Installation_aborted."), java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Installation_aborted"), javax.swing.JOptionPane.ERROR_MESSAGE);
                        
                    } else {
                        final anecho.gui.OKBox successMsg = new anecho.gui.OKBox(MuckMain.getInstance().getMainFrame(), java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Installation_aborted"), true);
                        successMsg.setText(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("An_installation_directory_was_not_selected._Installation_aborted."));
                        successMsg.show();
                    }
                    return;
                }
                
                // if (!unpackPlugIn(plugPath)) {
                final int unpack = unpackPlugIn(plugPath);
                
                if (unpack != SUCCESS) {
                    String message = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Installation_aborted_message");
                    String title = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Installation_aborted");

                    switch (unpack) {
                            case UNPACK_ERROR:
                            title = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Unpack_error");
                            message = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Unpack_error");
                            break;
                        case NO_MANIFEST_ERROR:
                            title = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("No_MANIFEST_error");
                            // message = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("JamochaMUD_was_unable_to_locate_the_MANIFEST\nfile_in_this_plug-in_archive.\nEither_this_file_is_not_a_valid_JamochaMUD_plug-in\nor_the_MANIFEST_file_is_in_the_wrong_location\nin_the_archive.\n");
                            message = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("No_MANIFEST_error_message");
                            break;
                        default:
                            title =java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Installation_error");
                            message = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Plug-in_install_failed.");
                            break;
                    }
                            
                    if (useSwing) {
                        // javax.swing.JOptionPane.showMessageDialog(settings.getJMFrame(JMConfig.MUCKMAINFRAME),
                        //        "This plug-in was not successfully unpacked.\nInstallation aborted.\n");
                        javax.swing.JOptionPane.showMessageDialog(MuckMain.getInstance().getMainFrame(),
                                message, title, javax.swing.JOptionPane.ERROR_MESSAGE);
                        
                    } else {
                        // anecho.gui.OKBox successMsg = new anecho.gui.OKBox(settings.getJMFrame(JMConfig.MUCKMAINFRAME), "Package error", true);
                        // successMsg.setText("This plug-in was not successfully unpacked.\nInstallation aborted.\n");
                        final anecho.gui.OKBox successMsg = new anecho.gui.OKBox(MuckMain.getInstance().getMainFrame(), title, true);
                        successMsg.setText(message);
                        successMsg.show();
                    }
                    return;
                }
               
                // if (settings.getJMboolean(JMConfig.USESWING)) {
                if (useSwing) {
                    javax.swing.JOptionPane.showMessageDialog(MuckMain.getInstance().getMainFrame(),
                            java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Plug-in_installed_successfully._You_may_need_to_restart_JamochaMUD_to_use_the_plug-in."));
                } else {
                    // anecho.gui.OKBox successMsg = new anecho.gui.OKBox(settings.getJMFrame(JMConfig.MUCKMAINFRAME), "Success!", true, "Plug-in installed successfully.\nYou may need to restart JamochaMUD\nto use the plug-in.");
                    final anecho.gui.OKBox successMsg = new anecho.gui.OKBox(MuckMain.getInstance().getMainFrame(), java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Success!"), true);
                    // successMsg.setText("Plug-in installed successfully.\nYou may need to restart JamochaMUD\nto use the plug-in.");
                    successMsg.setText(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Plug-in_installed_successfully._You_may_need_to_restart_JamochaMUD_to_use_the_plug-in."));
                    successMsg.show();
                }
            } catch (Exception exc) {
                if (DEBUG) {
                    System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Exception") + exc);
                }
            }
            
        }
    }
    
    /** unpack and install the given plug-in */

    // private boolean unpackPlugIn(String plugLocation) {

    private int unpackPlugIn(final String plugLocation) {
        final int retVal = SUCCESS;
        // boolean unpackResult = false;
        ZipFile plugPack;
        ZipEntry entry, testEntry;
        final Vector entryList = new Vector();
        // FileInputStream inStream;
        InputStream inStream;
        String testDestName;
        
        try {
            plugPack = new ZipFile(plugLocation);
        } catch (Exception foo) {
            if (DEBUG) {
                System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Exception_opening_") + plugLocation);
                System.err.println(foo);
            }
            // return unpackResult;
            return UNPACK_ERROR;
        }
        
        final Enumeration plugList = plugPack.entries();
        
        while (plugList.hasMoreElements()) {
            // grab a zip file entry
            entry = (ZipEntry)plugList.nextElement();
            entryList.addElement(entry);
            // System.out.println(entry.getName());
        }
        
        final int totalEntries = entryList.size();
        
        if (DEBUG) {
            System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("There_are_") + totalEntries + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("_entries_in_our_plugPack."));
        }
        
        // Look for our MANIFEST file
        if (hasManifest(entryList)) {
            if (DEBUG) {
                System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("We_found_the_manifest_file!"));
            }
        } else {
            if (DEBUG) {
                System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("No_Manifest_file_was_found,_cannot_load."));
            }
            // return unpackResult;
            return NO_MANIFEST_ERROR;
        }
        
        File testFile;
        final String pathSep = System.getProperty(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("file.separator"));
        
        BufferedOutputStream dest = null;
        // FileInputStream fis = new FileInputStream();
        
        // Open up a stream to the plug-in archive
        // inStream = zipFile.getInputStream(plugLocation);
        // inStream = plugPack.getInputStream(plugLocation);
        
        for (int i = 0; i < totalEntries; i++) {
            testEntry = (ZipEntry)entryList.elementAt(i);
            
            if (testEntry.isDirectory()) {
                // Create a directory first
                if (DEBUG) {
                    System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Creating_directory:_") + testEntry.getName());
                    System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("installation_directory_not_selected") + userPath);
                    System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("at:_") + masterPath);
                }
                // This is not robust, just for demonstration purposes.
                
                if (userDir) {
                    testFile = new File(userPath + pathSep + testEntry.getName());
                    System.out.println(userPath + pathSep + testEntry.getName());
                    testFile.mkdirs();
                    continue;
                }
                if (masterDir) {
                    testFile = new File(masterPath + pathSep + testEntry.getName());
                    System.out.println(masterPath + pathSep + testEntry.getName());
                    testFile.mkdirs();
                    continue;
                }
            } else {
                if (!testEntry.getName().equalsIgnoreCase(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("manifest"))) {
                    // Uncompress the file
                    try {
                        // System.out.println("Creating file: " + testEntry.getName());
                        // System.out.println("at: " + userPath);
                        // System.out.println("at: " + masterPath);
                        
                        // System.out.println("Creating FileInputStream.");
                        inStream = plugPack.getInputStream(testEntry);
                        // System.out.println("Creating ZipInputStream.");
                        final ZipInputStream zis = new ZipInputStream(new BufferedInputStream(inStream));
                        
                        
                        if (userDir) {
                            // outStream = userPath + pathSep + testEntry.getName();
                            // testFile = new File(userPath + pathSep + testEntry.getName());
                            // testFile.mkdirs();
                            // outStream = new FileOutputStream(userPath + pathSep + testEntry.getName());
                            // buffOutStream = new BufferedOutputStream(outStream, buffer);                }
                            // unzipMethod(userPath + pathSep, testEntry);
                            // System.out.println("Extracting: " + testEntry);
                            int count;
                            final byte data[] = new byte[buffer];
                            // write the files to the disk
                            FileOutputStream fos;
                            
                            if (userDir) {
                                testDestName = userPath + pathSep + testEntry.getName();
                                // fos = new FileOutputStream(userPath + pathSep + testEntry.getName());
                                fos = new FileOutputStream(testDestName);
                            } else {
                                testDestName = masterPath + pathSep + testEntry.getName();
                                fos = new FileOutputStream(testDestName);
                            }
                            
                            dest = new BufferedOutputStream(fos, buffer);
                            while ((count = zis.read(data, 0, buffer))
                            != -1) {
                                dest.write(data, 0, count);
                            }
                            dest.flush();
                            dest.close();
                        }
                    } catch (Exception ze) {
                        System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("ZipInputStream_exception_") + ze);
                        continue;
                    }
                }
                
            }
        }
        
        try {
            plugPack.close();
        } catch (Exception e) {
            System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Exception_while_closing_plugPack:_") + e);
        }
        
        // unpackResult = true;
        
        // return unpackResult;
        return retVal;

    }

    

    /** Check the given entryList for a manifest file, returning &quot;true" if one exists */

    private boolean hasManifest(final Vector entryList) {

        boolean result = false;

        final int entries = entryList.size();

        

        for (int i = 0; i < entries; i++) {

            if (entryList.elementAt(i).toString().equalsIgnoreCase(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("manifest"))) {

                result = true;

            }

        }

        

        return result;

    }

    

    /** Create a dialogue and ask the user which directories to install to */

    private boolean selectTargets() {

        boolean selectResult = false;

        

        // GridbagLayout layout = new GridbagLayout();

        // if (settings.getJMboolean(JMConfig.USESWING)) {

        if (useSwing) {

            final javax.swing.JCheckBox userPathBox = new javax.swing.JCheckBox(userPath, true);

            final javax.swing.JCheckBox mPathBox = new javax.swing.JCheckBox(masterPath, false);

            final javax.swing.JPanel choicePanel = new javax.swing.JPanel(new GridLayout(3, 1));

            final javax.swing.JLabel choiceDesc = new javax.swing.JLabel(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Choose_the_directories_that_you_wish_to_have_the_plug-in_installed_to."));

            

            choicePanel.add(choiceDesc);

            choicePanel.add(userPathBox);

            choicePanel.add(mPathBox);

            

            // javax.swing.JOptionPane optionPane = new javax.swing.JOptionPane(choicePanel, javax.swing.JOptionPane.OK_CANCEL_OPTION);

            final int response = javax.swing.JOptionPane.showOptionDialog(MuckMain.getInstance().getMainFrame(),

                    choicePanel,

                    java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Choose_installation_location(s)"),

                    javax.swing.JOptionPane.OK_CANCEL_OPTION,

                    javax.swing.JOptionPane.QUESTION_MESSAGE,

                    null,

                    null,

                    null);

            

            if (response == javax.swing.JOptionPane.OK_OPTION) {

                selectResult = true;

                if (userPathBox.isSelected()) {

                    userDir = true;

                }

                if (mPathBox.isSelected()) {

                    masterDir = true;

                }

            }

            // optionPane.setVisible(true);

            

        } else {
            final Checkbox userPathBox = new Checkbox(userPath, true);
            final Checkbox mPathBox = new Checkbox(masterPath, false);
            
            final Button okayButton = new Button(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Okay"));
            final Button cancelButton = new Button(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Cancel"));
            okayButton.setActionCommand(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("OKAY"));
            okayButton.addActionListener(this);
            cancelButton.setActionCommand(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("CANCEL"));
            cancelButton.addActionListener(this);
            
            final Label choiceDesc = new Label(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Choose_the_directories_that_you_wish_to_have_the_plug-in_installed_to."));
            
            final Panel choicePanel = new Panel(new GridLayout(4, 1));
            final Panel buttonPanel = new Panel(new GridLayout(1, 2));
            
            buttonPanel.add(okayButton);
            buttonPanel.add(cancelButton);
            
            
            choicePanel.add(choiceDesc);
            choicePanel.add(userPathBox);
            choicePanel.add(mPathBox);
            choicePanel.add(buttonPanel);
            
            msg = new Dialog(MuckMain.getInstance().getMainFrame(),
                    java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Choose_installation_location(s)"),
                    true);
            
            msg.add(choicePanel);
            msg.pack();
            msg.setVisible(true);
            
            if (msgResponse) {
                selectResult = true;
                if (userPathBox.getState()) {
                    userDir = true;
                }
                if (mPathBox.getState()) {
                    masterDir = true;
                }
            }
            
        }
        
        return selectResult;
    }

    

    /**

     * This method is used solely for the AWT dialogue

     * @param buttonEvent 

     */

    public void actionPerformed(final ActionEvent buttonEvent) {

        // System.out.println("Event: " + buttonEvent);

        if (buttonEvent.getActionCommand().equals(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("OKAY"))) {

            msgResponse = true;

        }

        

        msg.setVisible(false);

        msg.dispose();

    }

    

    /**

     * private void unzipMethod(String destPath, Vector entryList) {

     * FileOutputStream outStream;

     * BufferedOutputStream buffOutStream;

     * while ((count = zin.read(data, 0, buffer)) != -1) {

     * System.out.println("Extracting: " + entryName);

     * outStream = new FileOutputStream(destPath + pathSep + entryName.getName());

     * buffOutStream = new BufferedOutputStream(outStream, buffer);

     * int count;

     * byte data[] = new byte[buffer];

     * //System.out.write(x);

     * buffOutStream.write(data, 0, count);

     * }

     * }

     */

    

    public void remove() {

    }

    

    // private java.io.File swingDialog(Frame parent) {

    private String swingDialog(final Frame parent) {

        java.io.File retFile;

        String plugPath = null;

        

        final javax.swing.JFileChooser plugChooser = new javax.swing.JFileChooser();

        // plugChooser.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);

        final int retVal = plugChooser.showOpenDialog(parent);

        

        if (retVal == javax.swing.JFileChooser.APPROVE_OPTION) {

            retFile = plugChooser.getSelectedFile();

            // plugPath = retFile.getPath() + retFile.pathSeparator + retFile.getName();

            plugPath = retFile.getPath();

        }

        

        // return retFile;

        return plugPath;

    }



    private String awtDialog(Frame parent) {

        // java.io.File retFile;

        String plugPath = null;

        

        final FileDialog installPath = new FileDialog(parent, java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Choose_.jpa_to_install"), FileDialog.LOAD);

        installPath.show();

        

        if (installPath.getFile() != null) {

            plugPath = installPath.getFile();

        }

        

        return plugPath;



    }

}