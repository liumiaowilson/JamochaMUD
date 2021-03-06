/*
 * SpellCheck.java
 *
 * Created on March 3, 2006, 11:42 PM
 */
package anecho.JamochaMUD.PrefPanels.SpellCheck;

import anecho.JamochaMUD.JMConfig;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import net.sf.wraplog.AbstractLogger;
import net.sf.wraplog.NoneLogger;
import net.sf.wraplog.SystemLogger;

/**
 *
 * @author jeffnik
 */
public class SpellCheck extends javax.swing.JPanel implements anecho.JamochaMUD.PrefPanels.PrefInterface {

    /**
     * Creates new form SpellCheck
     */
    public SpellCheck() {
        // initComponents();
        //if (DEBUG) {
        //    System.err.println("Creating new SpellCheck instance");
        //}

        if (DEBUG) {
            logger = new SystemLogger();
        } else {
            logger = new NoneLogger();
        }

        logger.debug("Creating new SpellCheck instance.");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        final javax.swing.JLabel jLabel1 = new javax.swing.JLabel();
        final javax.swing.JLabel jLabel2 = new javax.swing.JLabel();
        final javax.swing.JButton addLang = new javax.swing.JButton();
        enableCheck = new javax.swing.JCheckBox();
        langList = new javax.swing.JTextField();
        final javax.swing.JScrollPane jScrollPane1 = new javax.swing.JScrollPane();
        final javax.swing.JTextArea instructions = new javax.swing.JTextArea();
        downloadButton = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        jLabel1.setBackground(javax.swing.UIManager.getDefaults().getColor("InternalFrame.activeTitleBackground"));
        jLabel1.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/PrefPanels/SpellCheck/SpellCheckBundle"); // NOI18N
        jLabel1.setText(bundle.getString("Spell_Check")); // NOI18N
        jLabel1.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jLabel1, gridBagConstraints);

        jLabel2.setText("Dictionary file"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jLabel2, gridBagConstraints);

        addLang.setText("Install dictionary"); // NOI18N
        addLang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addLangActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(addLang, gridBagConstraints);

        enableCheck.setSelected(true);
        enableCheck.setText("Enable spell check"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(enableCheck, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(langList, gridBagConstraints);

        instructions.setColumns(20);
        instructions.setLineWrap(true);
        instructions.setRows(5);
        instructions.setText("JamochaMUD should be able to use any OpenOffice.org dictionary.  Simply navigate to http://wiki.services.openoffice.org/wiki/Dictionaries and download your chosen language file.  Onc you have downloaded the file, simply click \"Install Dictionary\" and point to the file.  JamochaMUD will do the rest!"); // NOI18N
        instructions.setCaretPosition(0);
        jScrollPane1.setViewportView(instructions);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jScrollPane1, gridBagConstraints);

        downloadButton.setText(bundle.getString("DOWNLOAD DICTIONARY FILE")); // NOI18N
        downloadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downloadButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(downloadButton, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    private void addLangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addLangActionPerformed
        locateDictionary();
    }//GEN-LAST:event_addLangActionPerformed

    private void downloadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downloadButtonActionPerformed
        fetchNewDictionary();
    }//GEN-LAST:event_downloadButtonActionPerformed

    private void fetchNewDictionary() {
        anecho.JamochaMUD.BrowserWrapper bw = anecho.JamochaMUD.BrowserWrapper.getInstance();

        bw.showURL("http://extensions.services.openoffice.org/dictionary");

    }

    /**
     *
     */
    @Override
    public void applySettings() {
        logger.debug("SpellCheck.applySettings() has been called.");
        

        final java.util.prefs.Preferences prefs = java.util.prefs.Preferences.userNodeForPackage(anecho.JamochaMUD.JMUD.class);
        dictFile = langList.getText();

        final JMConfig settings = JMConfig.getInstance();
        final anecho.JamochaMUD.DataIn dat = settings.getDataInVariable();

        if (!prefs.get(DICTLABEL, "").equals(dictFile)) {
            // The dictionary file has changed so make certain we get rid
            // of the previous instance
            logger.debug("Trying to remove previous instance of spellcheck");
            
            dat.setSpellCheck(false);
        }

        prefs.put(DICTLABEL, dictFile);

        final boolean autoCheck = enableCheck.isSelected();
        prefs.putBoolean(SPELLCHECKLABEL, autoCheck);

        // Tell any needed components about new spellcheck state
        dat.setSpellCheck(autoCheck);
    }

    @Override
    public JPanel loadPanel() {
        // Check to see if components have already been initialised
        if (langList == null) {
            logger.debug("SpellCheck.Loading new panel");
            
            // Initialise our components
            initComponents();

            // Setup the colours in our table
            loadSettings();
        } else {
            logger.debug("SpellCheck.Panel already loaded");
            
        }

        return this;
    }

    @Override
    public String getModuleName() {
//        final String retStr = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/PrefPanels/SpellCheck/SpellCheckBundle").getString("spell_check");
//        return retStr;
        return java.util.ResourceBundle.getBundle("anecho/JamochaMUD/PrefPanels/SpellCheck/SpellCheckBundle").getString("spell_check");
    }

    /**
     * Set up the UI with any existing settings
     */
    private void loadSettings() {
        final java.util.prefs.Preferences prefs = java.util.prefs.Preferences.userNodeForPackage(anecho.JamochaMUD.JMUD.class);

        boolean autoCheck = prefs.getBoolean(SPELLCHECKLABEL, false);

        dictFile = prefs.get(DICTLABEL, "");

        // if (dictFile != "") {
        if (!dictFile.equals("")) {
            langList.setText(dictFile);

            // Now, verify that the dictFile is still present, and if not we should create a
            // dialogue to let the user know.
            try {
                final File testFile = new File(dictFile);

                if (!testFile.exists()) {
                    dictNotFoundError();
                    autoCheck = false;
                }
            } catch (Exception noFile) {
                logger.debug("The dictionary file cannot be found.");
                
                dictNotFoundError();
                autoCheck = false;
            }
        }

        enableCheck.setSelected(autoCheck);
    }

    /**
     * Show a file dialog so that the user can locate a downloaded archive
     * containing a dictionary (ispell, myspell, or OpenOffice 3.x)
     */
    private void locateDictionary() {
        final JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/PrefPanels/SpellCheck/SpellCheckBundle").getString("Please_choose_a_dictionary_file"));
        chooser.setDialogType(JFileChooser.OPEN_DIALOG);

        // Set the file filter
        FileFilter mySpellFilter = new FileNameExtensionFilter(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/PrefPanels/SpellCheck/SpellCheckBundle").getString("MYSPELL_DICTIONARY"), "zip");
        FileFilter openOfficeFilter = new FileNameExtensionFilter(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/PrefPanels/SpellCheck/SpellCheckBundle").getString("OPENOFFICE_DICTIONARY"), new String[]{"zip", "oxt"});

        chooser.addChoosableFileFilter(mySpellFilter);
        chooser.addChoosableFileFilter(openOfficeFilter);

        // Show the dialogue
        final int returnVal = chooser.showDialog(anecho.JamochaMUD.MuckMain.getInstance().getMainFrame(), java.util.ResourceBundle.getBundle("anecho/JamochaMUD/PrefPanels/SpellCheck/SpellCheckBundle").getString("LOCATE_DICTIONARY"));

        if (returnVal == JFileChooser.CANCEL_OPTION) {
            // User has chosen to abort export
            return;
        }

        final File selFile = chooser.getSelectedFile();

        langList.setText(selFile.getAbsolutePath());
    }

    /**
     * Bring up an error dialog indicating the dictionary file can't be found.
     */
    private void dictNotFoundError() {
        // JOptionPane.showMessageDialog((JFrame) JMConfig.getInstance().getJMFrame(JMConfig.MUCKMAINFRAME),
        JOptionPane.showMessageDialog((JFrame) anecho.JamochaMUD.MuckMain.getInstance().getMainFrame(),
                java.util.ResourceBundle.getBundle("anecho/JamochaMUD/PrefPanels/SpellCheck/SpellCheckBundle").getString("Dictionary_file_not_found"),
                java.util.ResourceBundle.getBundle("anecho/JamochaMUD/PrefPanels/SpellCheck/SpellCheckBundle").getString("dictionary_file_error"),
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Checks to see if the version of Java available is current enough to use
     * this module
     */
    @Override
    public boolean checkVersion() {
        boolean retCode = true;

        final java.util.Properties systemData = System.getProperties();
        final double ver = anecho.gui.TextUtils.stringToDouble(systemData.getProperty("java.version"));
        if (ver < 1.5) {
            // Disable spellchecking
            enableCheck.setSelected(false);
            enableCheck.setEnabled(false);
            langList.setEnabled(false);
            retCode = false;
        }

        return retCode;
    }

    /**
     * This can be called to warn the user why this module does not function
     */
    @Override
    public void versionWarning() {
        // Check to see if Java is version 1.5 or higher.  If not, we
        // cannot use myspell libraries due to the use of generics which do not
        // existing in version 1.4 and below.
        // We can't use myspell as generics are not handled until Java 1.5.
        // An error message should probably be given to the user and spell-check
        // should be disabled
        final java.util.Properties systemData = System.getProperties();

        javax.swing.JOptionPane.showMessageDialog(null, 
                java.util.ResourceBundle.getBundle("anecho/JamochaMUD/PrefPanels/SpellCheck/SpellCheckBundle").getString("LIBRARY_VERSION_MESSAGE")
                + systemData.getProperty("java.version"), 
                java.util.ResourceBundle.getBundle("anecho/JamochaMUD/PrefPanels/SpellCheck/SpellCheckBundle").getString("JAVA_VERSION_FOR_SPELLCHECK"), 
                javax.swing.JOptionPane.WARNING_MESSAGE);

    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton downloadButton;
    private transient javax.swing.JCheckBox enableCheck;
    private transient javax.swing.JTextField langList;
    // End of variables declaration//GEN-END:variables
    /**
     * Enables or disables debugging output
     */
    private static final boolean DEBUG = false;
    /**
     *
     */
    private transient String dictFile;
    /**
     *
     */
    private static final String DICTLABEL = "DictFile";
    /**
     *
     */
    private static final String SPELLCHECKLABEL = "SpellCheckEnabled";

    private final AbstractLogger logger;
}
