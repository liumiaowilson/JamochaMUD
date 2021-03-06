/*
 * WhatsNew.java
 *
 * Created on March 12, 2008, 9:14 PM
 */
package anecho.JamochaMUD;

/**
 *
 * This dialogue will show the most recent changes/fixes for this version of
 * JamochaMUD.
 *
 * @author jeffnik
 * @version $Id: WhatsNew.java,v 1.31 2015/08/30 22:43:32 jeffnik Exp $
 */
public class WhatsNew extends javax.swing.JDialog {

    /**
     * Creates new form WhatsNew
     *
     * @param parent The frame that will be parent to this dialogue
     * @param modal Indicate whether this dialogue should be modal or not
     */
    public WhatsNew(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        messageText.setText(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("JamochaMUD_version_") + AboutBox.VERNUM + ' ' + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("_released_") + AboutBox.BUILDNUM + "\n"
                + "Bugfix to stop crash during writing out of settings file.\n"
                + "\n"
                + "- 0000274: [Main Program] does not save settings (jeffnik) - resolved.\n"
                + "- 0000273: [User Interface] Double-click on \"World\" node (jeffnik) - resolved.\n"
                + "- 0000272: [User Interface] Double-click on world doesn't work (jeffnik) - closed.\n"
                + "- 0000270: [User Interface] JamochaMUD does not indicate failed connection (jeffnik) - closed.\n");
        messageText.setCaretPosition(0);

        final boolean selState = !JMConfig.getInstance().getJMboolean(JMConfig.SHOWNEW);
        dontShowCB.setSelected(selState);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        dontShowCB = new javax.swing.JCheckBox();
        final javax.swing.JButton okayButton = new javax.swing.JButton();
        final javax.swing.JLabel jLabel1 = new javax.swing.JLabel();
        final javax.swing.JScrollPane jScrollPane1 = new javax.swing.JScrollPane();
        messageText = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle"); // NOI18N
        setTitle(bundle.getString("What's_new!")); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeHandler(evt);
            }
        });
        getContentPane().setLayout(new java.awt.GridBagLayout());

        dontShowCB.setMnemonic(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("WhatsNew.DontShowCBMnemonic").charAt(0));
        dontShowCB.setText(bundle.getString("Don't_show_this_dialogue_again.")); // NOI18N
        dontShowCB.setToolTipText(bundle.getString("WhatsNew.DontShowCBToolTip")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        getContentPane().add(dontShowCB, gridBagConstraints);

        okayButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/anecho/JamochaMUD/icons/22/button_ok.png"))); // NOI18N
        okayButton.setMnemonic(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("WhatsNew.OkayButtonMnemonic").charAt(0));
        okayButton.setText(bundle.getString("Okay")); // NOI18N
        okayButton.setToolTipText(bundle.getString("WhatsNews.OkayButtonToolTip")); // NOI18N
        okayButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okayButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 5, 5);
        getContentPane().add(okayButton, gridBagConstraints);

        jLabel1.setLabelFor(messageText);
        jLabel1.setText(bundle.getString("WhatsNew.descLabel")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 2, 5);
        getContentPane().add(jLabel1, gridBagConstraints);

        messageText.setColumns(20);
        messageText.setEditable(false);
        messageText.setLineWrap(true);
        messageText.setRows(10);
        messageText.setToolTipText(bundle.getString("WhatsNew.MessageTextToolTip")); // NOI18N
        messageText.setWrapStyleWord(true);
        jScrollPane1.setViewportView(messageText);
        messageText.getAccessibleContext().setAccessibleName(bundle.getString("WhatsNew.MessageTextAccessibleName")); // NOI18N

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        getContentPane().add(jScrollPane1, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okayButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okayButtonActionPerformed
        closeAndSave();
    }//GEN-LAST:event_okayButtonActionPerformed

    private void closeHandler(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeHandler
        closeAndSave();
    }//GEN-LAST:event_closeHandler

    /**
     * Saves any required settings before closing this dialogue
     */
    private void closeAndSave() {
        if (DEBUG) {
            System.err.println("WhatsNew.closeAndSave called.");
        }

        this.setVisible(false);

        // Save any required settings
        final JMConfig settings = JMConfig.getInstance();
        boolean showNew = false;

        if (!dontShowCB.isSelected()) {
            showNew = true;
        }

        // Save whether this dialogue should automatically appear
        // when new versions are distributed
        settings.setJMboolean(JMConfig.SHOWNEW, String.valueOf(showNew));

        // Dispose of the dialogue
        this.dispose();
    }
    /**
     * Enabled and disables debugging output
     */
    private static final boolean DEBUG = false;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private transient javax.swing.JCheckBox dontShowCB;
    private transient javax.swing.JTextArea messageText;
    // End of variables declaration//GEN-END:variables
}
