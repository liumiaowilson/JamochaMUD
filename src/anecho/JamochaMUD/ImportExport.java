/*
 * ImportExport.java
 *
 * Created on January 14, 2006, 10:53 PM
 *
 * $Id: ImportExport.java,v 1.8 2014/05/17 21:01:45 jeffnik Exp $
 */

/* JamochaMUD, a Muck/Mud client program
 * Copyright (C) 1998-2014 Jeff Robinson
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

import anecho.util.FileUtils;
import anecho.util.SimpleFilter;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.prefs.BackingStoreException;
import java.util.prefs.InvalidPreferencesFormatException;
import java.util.prefs.Preferences;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import net.sf.wraplog.AbstractLogger;
import net.sf.wraplog.NoneLogger;
import net.sf.wraplog.SystemLogger;

/**
 * This class is used for the importing and exporting of JamochaMUD settings
 *
 * @author jeffnik
 */
public class ImportExport {

    private static final boolean DEBUG = false;
    private static final int BUFFER = 2048;
    private static final int SUCCESSFUL = 0;
    private static final int ARCHIVE_ERROR = 1;
    private static ImportExport ieObj;
    private static final String ARCEXT = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString(".jsa");
    private File prefFile;
    private boolean visualProgress = true;

    private final AbstractLogger logger;

    /**
     * Creates a new instance of ImportExport
     */
    private ImportExport() {
        File baseDir = getBaseDir();
        String sep = File.separator;
        prefFile = new File(baseDir + sep + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("JMUDPrefs_backup.xml"));

        if (DEBUG) {
            logger = new SystemLogger();
        } else {
            logger = new NoneLogger();
        }

    }

    /**
     * Returns an instance of ImportExport
     *
     * @return Returns a running instance of ImportExport.
     */
    public static ImportExport getInstance() {
        if (ieObj == null) {
            ieObj = new ImportExport();
        }
        return ieObj;
    }

    /**
     * Gather all our settings and export them to a file
     */
    public void exportSettings() {
        // Find out where to save the file when we are done
        if (DEBUG) {
            logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Entered_ImportExport.exportSettings"));
        }

        final JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Choose_a_name_and_location_to_save_your_settings."));
        chooser.setDialogType(JFileChooser.SAVE_DIALOG);

        final SimpleFilter filter = new SimpleFilter();
        filter.setFilters(new String[]{java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString(".jsa")});
        filter.setDescription(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("JamochaMUD_Settings_Archive_*.jsa"));

        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setFileFilter(filter);
        final int returnVal = chooser.showDialog(MuckMain.getInstance().getMainFrame(), java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Export"));

        if (returnVal == JFileChooser.CANCEL_OPTION) {
            // User has chosen to abort export
            return;
        }

        File selFile = chooser.getSelectedFile();

        // Check to see if we need to add the .jsa extension to the file name
        try {
            final String checkName = selFile.getCanonicalFile().toString();

            logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("The_Canonical_File_is_") + checkName);

            final int dot = checkName.lastIndexOf(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("."));
            if (dot < 0 || !checkName.substring(dot).equalsIgnoreCase(ARCEXT)) {
                selFile = new File(checkName + ARCEXT);
            }
        } catch (IOException exc) {

            logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("There_was_an_error_checking_for_the_.jsa_extension_") + exc);

        }

        if (JMConfig.getInstance().getJMboolean(JMConfig.DISKLESSCONFIG)) {
            logger.debug("ImportExport.exportSettings: Working in Diskless config and will not write out .jamocha.rc");
        } else {
        // Force a write to the .jamocha.rc file so that any changes since this start are saved
            logger.debug("ImportExport.exportSettings: writing out .jamocha.rc file");
            final JMWriteRC jwr = new JMWriteRC();
            jwr.writeRCToFile();
        }

        // Back-up our Java userprefs files into a separate archive (within the "base" dir)
        exportPrefFile();

        // Create a list of all the settings files
        final String[] fNames = gatherFiles();

        // zip the files, creating the archive inStrm the appropriate location
        zipFiles(selFile, fNames);

        // Delete the temporary PrefFile we created
        prefFile.delete();
    }

    /**
     * Import previously saved settings into JamochaMUD. JamochaMUD will need to
     * be shutdown before this can proceed, as we keep some specific variable
     * information inStrm our settings.
     */
    public void importSettings() {
        final java.awt.Frame muckMainFrame = MuckMain.getInstance().getMainFrame();
        final String message = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("impMessage");

        final int result = javax.swing.JOptionPane.showConfirmDialog(muckMainFrame,
                message,
                java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("importWarningTitle"),
                javax.swing.JOptionPane.YES_NO_OPTION,
                javax.swing.JOptionPane.QUESTION_MESSAGE);

        if (result == javax.swing.JOptionPane.NO_OPTION) {
            // The user does not want to restart at this time, so we cannot continue with the import
            return;
        }

        // Hiding the main frame removes some visual clutter and makes it more clear
        // to the user that JamochaMUD is working.
        muckMainFrame.setVisible(false);

        final JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Choose_a_the_settings_to_restore"));
        chooser.setDialogType(JFileChooser.OPEN_DIALOG);
        final SimpleFilter filter = new SimpleFilter();
        filter.setFilters(new String[]{ARCEXT});
        filter.setDescription(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("JamochaMUD_Settings_Archive"));
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setFileFilter(filter);
        final int returnVal = chooser.showDialog(MuckMain.getInstance().getMainFrame(), java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Import"));

        if (returnVal == JFileChooser.CANCEL_OPTION) {
            // User has chosen to abort export
            muckMainFrame.setVisible(true);
            return;
        }

        final File selFile = chooser.getSelectedFile();

        final int zipResult = unZipFiles(selFile);

        // Figure out our result codes here
        if (zipResult == SUCCESSFUL) {
            // We can only import the Pref File if the jsa was unzipped successfully
            // Import the Java pref file
            importPrefFile();
            // Tell the user we were successful and that they'll have to restart
            // JamochaMUD.

            logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Everything_went_perfectly_fine."));

            JOptionPane.showMessageDialog(null,
                    java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Import_successful_message"),
                    java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Import_successful"),
                    JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        } else {

            logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("There_was_a_failure_with_the_zip_file."));

            JOptionPane.showMessageDialog(null,
                    java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("An_error_has_occured_during_import\nYour_data_has_not_been_restored."),
                    java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Import_error"),
                    JOptionPane.ERROR_MESSAGE);

        }

        logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Importing_Pref_file."));

        muckMainFrame.setVisible(true);

        prefFile.delete();

    }

    /**
     * Gather the name and path of all settings files. If things are done
     * properly, all the settings files should be inStrm the
     * JMConfig.USERDIRECTORY
     */
    private String[] gatherFiles() {
        String[] fList;
        final File baseDir = getBaseDir();

        final FileUtils fileU = new FileUtils(baseDir.toString());
        try {
            fList = fileU.recursiveList(false);
        } catch (Exception exc) {
            logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("recursiveList_Exception_") + exc);
            fList = new String[0];
        }

        return fList;
    }

    /**
     * Zip the given files inStrm an archive and return a file handle for the
     * newly created zip archive
     */
    private void zipFiles(final File arcFile, final String[] fileList) {
        javax.swing.JFrame pFrame = (javax.swing.JFrame) MuckMain.getInstance().getMainFrame();
        pFrame.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));

        boolean showProg = visualProgress;

        javax.swing.ProgressMonitor epd = new javax.swing.ProgressMonitor(pFrame,
                "Creating archive...",
                "", 0, fileList.length);

        epd.setMillisToDecideToPopup(0);

        logger.debug("Total number of files: " + fileList.length);

        final File baseDir = getBaseDir();

        final String baseName = baseDir.toString() + File.separator;
        final int hackLen = baseName.length();

        // Create a BUFFER for reading the files
        final byte[] buf = new byte[BUFFER];

        try {
            // Create the ZIP file
            final String outFilename = arcFile.toString();
            try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outFilename))) {
                FileInputStream inStrm;
                
                // Compress the files
                for (int i = 0; i < fileList.length; i++) {
                    
                    if (showProg) {
                        epd.setProgress(i);
                    }
                    
                    logger.debug("ImportExport.zipFiles(): setting current task to " + i);
                    
                    inStrm = new FileInputStream(fileList[i]);
                    
                    // Add ZIP entry to output stream.
                    logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Hacked_entry_name_") + fileList[i].substring(hackLen));
                    
                    out.putNextEntry(new ZipEntry(fileList[i].substring(hackLen)));
                    
                    // Transfer bytes from the file to the ZIP file
                    int len;
                    while ((len = inStrm.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    
                    // Complete the entry
                    out.closeEntry();
                    inStrm.close();
                }
            }
        } catch (IOException exc) {

            logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("ImportExport_error_creating_zip_file_") + exc);

        }

        epd.close();

        pFrame.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));

    }

    /**
     * Unzip the files into the proper directory
     */
    private int unZipFiles(final File arcFile) {

        javax.swing.JFrame pFrame = (javax.swing.JFrame) MuckMain.getInstance().getMainFrame();
        javax.swing.ProgressMonitor epd = new javax.swing.ProgressMonitor(pFrame,
                "Unpacking archive...",
                "", 0, 100);

        int retVal = SUCCESSFUL;

        final File baseDir = getBaseDir();    // The base directory for unzipping the files

        final String pathSep = File.separator;

        try {
            BufferedOutputStream dest = null;
            final FileInputStream fis = new FileInputStream(arcFile);
            try (ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis))) {
                FileOutputStream fos;
                ZipEntry entry;
                byte data[];
                File testFile;
                int dotSpot;
                String testName;
                
                epd.setMaximum(fis.available());
                
                while ((entry = zis.getNextEntry()) != null) {
                    System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Extracting:_") + entry);
                    int count;
                    data = new byte[BUFFER];
                    // write the files to the disk
                    testName = baseDir.getCanonicalPath() + pathSep + entry.getName();
                    
                    logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Going_after_") + testName);
                    
                    dotSpot = testName.lastIndexOf(pathSep);
                    testFile = new File(testName.substring(0, dotSpot));
                    if (!testFile.exists()) {
                        
                        logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("We've_determined_that_") + testFile.toString() + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("_doesn't_exist."));
                        
                        testFile.mkdir();
                    }
                    fos = new FileOutputStream(baseDir.getCanonicalPath() + pathSep + entry.getName());
                    
                    dest = new BufferedOutputStream(fos, BUFFER);
                    while ((count = zis.read(data, 0, BUFFER)) != -1) {
                        dest.write(data, 0, count);
                    }
                    dest.flush();
                    dest.close();
                }
            }
        } catch (IOException e) {
            // return ARCHIVE_ERROR;
            retVal = ARCHIVE_ERROR;
            logger.debug("ImportExport.unZipFiles IOException " + e);
        }

        epd.close();

        // return SUCCESSFUL;
        return retVal;
    }

    private File getBaseDir() {
        final String userDir = JMConfig.getInstance().getJMString(JMConfig.USERDIRECTORY);
        final File baseDir = new File(userDir);
        return baseDir;

    }

    private void exportPrefFile() {

        final java.util.prefs.Preferences prefs = java.util.prefs.Preferences.userNodeForPackage(this.getClass());
        try {
            try (FileOutputStream out = new FileOutputStream(prefFile)) {
                prefs.exportNode(out);
                out.flush();
            }
        } catch (IOException | BackingStoreException exc) {

            logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("ImportExport.exportPrefFile()_error_") + exc);

        }
    }

    private void importPrefFile() {

        try {
            try (FileInputStream inStrm = new FileInputStream(prefFile)) {
                Preferences.importPreferences(inStrm);
            }
        } catch (IOException | InvalidPreferencesFormatException exc) {

            logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("ImportExport.importPrefFile()_error_") + exc);

        }

    }

}
