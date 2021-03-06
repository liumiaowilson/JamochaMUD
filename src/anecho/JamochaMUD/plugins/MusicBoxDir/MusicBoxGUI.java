/*
 * MusicBoxGUI.java
 *
 * Created on November 11, 2004, 10:40 PM
 */

package anecho.JamochaMUD.plugins.MusicBoxDir;

/**
 *
 * @author  Jeff Robinson
 */
public class MusicBoxGUI extends javax.swing.JDialog {
    
    /**
     * Creates new form MusicBoxGUI
     * @param parent 
     * @param modal 
     */
    public MusicBoxGUI(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        fileLabel = new javax.swing.JLabel();
        playingTF = new javax.swing.JTextField();
        browseButton = new javax.swing.JButton();
        displayLabel = new javax.swing.JLabel();
        displayTF = new javax.swing.JTextField();
        okayButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        ctrlLabel = new javax.swing.JLabel();
        ctrlCB = new javax.swing.JCheckBox();
        shiftCB = new javax.swing.JCheckBox();
        altCB = new javax.swing.JCheckBox();
        ctrlTF = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        fileContents = new javax.swing.JTextArea();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setTitle(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/plugins/MusicBoxDir/MusicBoxBundle").getString("MusicBox_Properties"));
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        fileLabel.setText(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/plugins/MusicBoxDir/MusicBoxBundle").getString("Playing_file"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 3, 3);
        getContentPane().add(fileLabel, gridBagConstraints);

        playingTF.setToolTipText(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/plugins/MusicBoxDir/MusicBoxBundle").getString("The_directory_and_file_name_that_contain_the_song_information"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(7, 3, 3, 3);
        getContentPane().add(playingTF, gridBagConstraints);

        browseButton.setText(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/plugins/MusicBoxDir/MusicBoxBundle").getString("Browse..."));
        browseButton.setToolTipText(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/plugins/MusicBoxDir/MusicBoxBundle").getString("Browse_for_the_playing_file"));
        browseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(7, 3, 3, 7);
        getContentPane().add(browseButton, gridBagConstraints);

        displayLabel.setText(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/plugins/MusicBoxDir/MusicBoxBundle").getString("Display"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 7, 3, 3);
        getContentPane().add(displayLabel, gridBagConstraints);

        displayTF.setToolTipText(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/plugins/MusicBoxDir/MusicBoxBundle").getString("The_format_to_display_the_song_information"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 7);
        getContentPane().add(displayTF, gridBagConstraints);

        okayButton.setText(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/plugins/MusicBoxDir/MusicBoxBundle").getString("Okay"));
        okayButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okayButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 7);
        getContentPane().add(okayButton, gridBagConstraints);

        cancelButton.setText(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/plugins/MusicBoxDir/MusicBoxBundle").getString("Cancel"));
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 7);
        getContentPane().add(cancelButton, gridBagConstraints);

        ctrlLabel.setText(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/plugins/MusicBoxDir/MusicBoxBundle").getString("Control_Key:"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 7, 3, 3);
        getContentPane().add(ctrlLabel, gridBagConstraints);

        ctrlCB.setText(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/plugins/MusicBoxDir/MusicBoxBundle").getString("CTRL_+"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(ctrlCB, gridBagConstraints);

        shiftCB.setText(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/plugins/MusicBoxDir/MusicBoxBundle").getString("Shift_+"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(shiftCB, gridBagConstraints);

        altCB.setText(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/plugins/MusicBoxDir/MusicBoxBundle").getString("Alt_+"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(altCB, gridBagConstraints);

        ctrlTF.setToolTipText(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/plugins/MusicBoxDir/MusicBoxBundle").getString("The_character_that_will_control_this_plug-in_(along_with_the_other_control_keys)"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 7);
        getContentPane().add(ctrlTF, gridBagConstraints);

        fileContents.setEditable(false);
        fileContents.setLineWrap(true);
        fileContents.setRows(7);
        fileContents.setText(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/plugins/MusicBoxDir/MusicBoxBundle").getString("MusicBoxInstructions"));
        jScrollPane1.setViewportView(fileContents);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 7, 3, 7);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(jScrollPane1, gridBagConstraints);

        pack();
    }//GEN-END:initComponents

    private void browseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseButtonActionPerformed
        final javax.swing.JFileChooser jfc = new javax.swing.JFileChooser();
        
        final String tempPlay = playingTF.getText();
        if (!tempPlay.equals("")) {
            final java.io.File sFile = new java.io.File(tempPlay);
            jfc.setSelectedFile(sFile);
        }
        
        final int retVal = jfc.showOpenDialog(this);
        if (retVal == javax.swing.JFileChooser.APPROVE_OPTION) {
            final java.io.File pFile = jfc.getSelectedFile();
            playingTF.setText(pFile.getAbsolutePath());
        }
        
        // update the file sample
        // Fix Me XXX
    }//GEN-LAST:event_browseButtonActionPerformed

    private void okayButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okayButtonActionPerformed
        this.setVisible(false);
        this.dispose();
        okayStatus = true;       
    }//GEN-LAST:event_okayButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed
    
    /**
     * 
     * @return 
     */
    public String getPlayFile() {
        return playingTF.getText();
    }
   
    /**
     * 
     * @param playFile 
     */
    public void setPlayFile(final String playFile) {
        if (playFile != null) {
            playingTF.setText(playFile);
        }
    }
    
    /**
     * 
     * @return 
     */
    public String getDispLine() {
        return displayTF.getText();
    }
    
    /**
     * 
     * @param dispLine 
     */
    public void setDispLine(final String dispLine) {
        if (dispLine != null) {
            displayTF.setText(dispLine);
        }
    }
    
    /**
     * 
     * @return 
     */
    public boolean isOkay() {
        return okayStatus;
    }

    /**
     * 
     * @param ctrlKey 
     */
    public void setCTRLKey(final char ctrlKey) {
        ctrlTF.setText(ctrlKey + "");
    }

    /**
     * 
     * @return 
     */
    public char getCTRLKey() {
        final String ctrl = ctrlTF.getText().trim();
        char retVal;

        if (ctrl.length() > 0) {
            retVal = ctrl.charAt(0);
        } else {
            retVal = '\u0000';
        }

        return retVal;
    }

    /**
     * 
     * @param useCTRL 
     */
    public void setUseCTRL(final boolean useCTRL) {
        ctrlCB.setSelected(useCTRL);
    }

    /**
     * 
     * @return 
     */
    public boolean useCTRL() {
        return ctrlCB.isSelected();
    }

    /**
     * 
     * @param useSHIFT 
     */
    public void setUseSHIFT(final boolean useSHIFT) {
        shiftCB.setSelected(useSHIFT);
    }

    /**
     * 
     * @return 
     */
    public boolean useSHIFT() {
        return shiftCB.isSelected();
    }

    /**
     * 
     * @param useALT 
     */
    public void setUseALT(final boolean useALT) {
        altCB.setSelected(useALT);
    }

    /**
     * 
     * @return 
     */
    public boolean useALT() {
        return altCB.isSelected();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox altCB;
    private javax.swing.JButton browseButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JCheckBox ctrlCB;
    private javax.swing.JLabel ctrlLabel;
    private javax.swing.JTextField ctrlTF;
    private javax.swing.JLabel displayLabel;
    private javax.swing.JTextField displayTF;
    private javax.swing.JTextArea fileContents;
    private javax.swing.JLabel fileLabel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton okayButton;
    private javax.swing.JTextField playingTF;
    private javax.swing.JCheckBox shiftCB;
    // End of variables declaration//GEN-END:variables
    private transient boolean okayStatus = false;
}
