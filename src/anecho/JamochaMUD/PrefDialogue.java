/*
 * PrefDialogue.java
 *
 * Created on November 20, 2005, 10:20 PM
 */

package anecho.JamochaMUD;

import anecho.JamochaMUD.PrefPanels.PrefInterface;

import java.util.Enumeration;
import javax.swing.tree.*;

import java.util.Hashtable;
import javax.swing.JPanel;


/**
 *
 * @author  jeffnik
 */
public class PrefDialogue extends javax.swing.JDialog {
    
    /**
     * The PrefDialogue creates a dialogue feature a &quot;tree" on the
     * left side of the dialogue listing available modules, and a panel
     * on the right side of the dialogue showing settings for the currently
     * selected module.  This dialogue has been designed to allow
     * additional modules to be easily added to display user-changable
     * settings.
     * @param parent The parent from of this dialogue.
     * @param modal Whether this dialogue should be modal or non-modal.
     */
    public PrefDialogue(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(parent);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        buildTree();
        optionTree = new javax.swing.JTree(optionRoot);
        optionCard = new javax.swing.JPanel();
        ScamFoxPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        buttonPanel = new javax.swing.JPanel();
        okayButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        applyButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle"); // NOI18N
        setTitle(bundle.getString("PrefDialogue.title_1")); // NOI18N
        getContentPane().setLayout(new java.awt.GridBagLayout());

        optionTree.setToolTipText(bundle.getString("PrefDialogueOptionTreeToolTip")); // NOI18N
        optionTree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                optionTreeValueChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(optionTree, gridBagConstraints);

        optionCard.setMinimumSize(new java.awt.Dimension(500, 400));
        optionCard.setLayout(new java.awt.CardLayout());

        ScamFoxPanel.setLayout(new java.awt.GridBagLayout());

        jLabel1.setBackground(javax.swing.UIManager.getDefaults().getColor("InternalFrame.activeTitleBackground"));
        jLabel1.setFont(new java.awt.Font("Dialog", 1, 18));
        jLabel1.setText(bundle.getString("PrefDialogue.jLabel1.text_1")); // NOI18N
        jLabel1.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        ScamFoxPanel.add(jLabel1, gridBagConstraints);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/anecho/JamochaMUD/JMUDSplash.png"))); // NOI18N
        jButton1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton1.setContentAreaFilled(false);
        jButton1.setFocusable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        ScamFoxPanel.add(jButton1, gridBagConstraints);

        optionCard.add(ScamFoxPanel, "card2");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(optionCard, gridBagConstraints);

        okayButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/anecho/JamochaMUD/icons/22/button_ok.png"))); // NOI18N
        okayButton.setMnemonic(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("PrefDialogueOkayButtonMnemonic").charAt(0));
        okayButton.setText(bundle.getString("PrefDialogue.okayButton.text_1")); // NOI18N
        okayButton.setToolTipText(bundle.getString("PrefDialogueOkayButtonToolTip")); // NOI18N
        okayButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okayButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(okayButton);

        cancelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/anecho/JamochaMUD/icons/22/button_cancel.png"))); // NOI18N
        cancelButton.setMnemonic(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("PrefDialogueCancelButtonMnemonic").charAt(0));
        cancelButton.setText(bundle.getString("PrefDialogue.cancelButton.text_1")); // NOI18N
        cancelButton.setToolTipText(bundle.getString("PrefDialogueCancelButtonToolTip")); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(cancelButton);

        applyButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/anecho/JamochaMUD/icons/22/edit.png"))); // NOI18N
        applyButton.setMnemonic(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("PrefDialogueApplyButtonMnemonic").charAt(0));
        applyButton.setText(bundle.getString("PrefDialogue.applyButton.text_1")); // NOI18N
        applyButton.setToolTipText(bundle.getString("PrefDialogueApplyButtonToolTip")); // NOI18N
        applyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(applyButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(buttonPanel, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void optionTreeValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_optionTreeValueChanged
        // There has been a selection on our tree, so change the corresponding panel
        changePanel(evt);
    }//GEN-LAST:event_optionTreeValueChanged
    
    private void applyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyButtonActionPerformed
        applyPref();
    }//GEN-LAST:event_applyButtonActionPerformed
    
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.setVisible(false);
        this.dispose();
        
    }//GEN-LAST:event_cancelButtonActionPerformed
    
    private void okayButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okayButtonActionPerformed
        // writePreferences();
        applyAllPrefs();
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_okayButtonActionPerformed
    
    /** Add all the nodes to our JTree of options */
    private void buildTree() {
        optionRoot = new DefaultMutableTreeNode("JamochaMUD options");
        
        // Loop through our PrefPanels and add nodes for them
        String tempName;
        DefaultMutableTreeNode tempNode;
        Object tempObj;
        
        prefPanelHash = new Hashtable();
        loadedPanels = new Hashtable();
        
        prefPanelHash.put("Spell Check", "anecho.JamochaMUD.PrefPanels.SpellCheck.SpellCheck");
        prefPanelHash.put("Output Colours", "anecho.JamochaMUD.PrefPanels.CustomColours.CustomColour");
        prefPanelHash.put("Command History", "anecho.JamochaMUD.PrefPanels.CommandHistory.CommandHistory");
        prefPanelHash.put("Logging Options", "anecho.JamochaMUD.PrefPanels.Logging.Logging");
        prefPanelHash.put("Socks Proxy", "anecho.JamochaMUD.PrefPanels.Socks.Socks");
        
        final Enumeration panelEnum = prefPanelHash.keys();
        
        // Go through our Enumeration and add each item to our tree
        while (panelEnum.hasMoreElements()) {
            tempObj = panelEnum.nextElement();
            tempName = tempObj.toString();
            if (DEBUG) {
                System.err.println("Our tempName is " + tempName);
            }
            tempNode = new DefaultMutableTreeNode(tempName);
            optionRoot.add(tempNode);
        }
    }
    
    /**
     * This changes the shown panel to the new selection
     * @param evt
     */
    private void changePanel(javax.swing.event.TreeSelectionEvent evt) {
        // Get the name of the node that was selected
        final DefaultMutableTreeNode node = (DefaultMutableTreeNode)optionTree.getLastSelectedPathComponent();
        
        if (node == null) {
            return;
        }
        
        final String panelName = node.toString();
        if (!prefPanelHash.containsKey(panelName)) {
            // Show the first card by default; our hastable doesn't contain the listed name
            final java.awt.CardLayout cards = (java.awt.CardLayout)optionCard.getLayout();
            cards.first(optionCard);
            return;
        }
        
        final String panelClass = prefPanelHash.get(panelName).toString();
        
        if (!loadedPanels.containsKey(panelName)) {
            // We haven't loaded this panel yet, so do so!
            if (DEBUG) {
                System.err.println("We don't have a key for this panel, so we'll try and load it.");
            }
            try {
                // final Object tempClass = (Object)Class.forName(panelClass).newInstance();
                final Object tempClass = Class.forName(panelClass).newInstance();
                final PrefInterface prefIn = (PrefInterface)tempClass;
                
                loadedPanels.put(panelName, prefIn);
                final JPanel tPan = prefIn.loadPanel();
                optionCard.add(tPan, panelName);
                this.pack();
                if (DEBUG) {
                    System.err.println("Successfully added " + panelName + " to optionCard.");
                    System.err.println("Our panel is " + tPan);
                }
                
                // Check for version
                if (!prefIn.checkVersion()) {
                    // We don't meet the running criteria.  Show the warning message
                    // to the user
                    prefIn.versionWarning();
                }
            } catch (Exception loadExc) {
                if (DEBUG) {
                    System.err.println("There was an exception trying to load panel " + loadExc);
                    loadExc.printStackTrace();
                }
            }
            
        } else {
            if (DEBUG) {
                System.err.println("We already have a key for this panel, so we should just be able to show it.");
            }
        }
        
        final java.awt.CardLayout cards = (java.awt.CardLayout)optionCard.getLayout();
        cards.show(optionCard, panelName);
    }
    
    /** Write out only the preference on the current page */
    private void applyPref() {
        // Determine the current panel based on our tree
        // Get the name of the node that was selected
        final DefaultMutableTreeNode node = (DefaultMutableTreeNode)optionTree.getLastSelectedPathComponent();
        
        if (node == null) {
            return;
        }
        
        final String panelName = node.toString();
        if (!prefPanelHash.containsKey(panelName)) {
            // A loadable panel in not currently selected, so there is nothing to apply
            return;
        }
        
        // Pull the reference to our panel from our loadedPanels Hashtable
        final PrefInterface prefIn = (PrefInterface)loadedPanels.get(panelName);
        // apply the settings to this panel
        prefIn.applySettings();
        
        
    }
    
    /** Apply the preferences for all the settings
     */
    private void applyAllPrefs() {
        // Loop through all the (loaded) panels and apply the settings
        // Obviously if a panel has not been loaded, its settings will not change!
        final int totPan = loadedPanels.size();
        
        if (totPan < 1) {
            return;
        }
        
        PrefInterface prefIn;
        String tempName;
        Object tempObj;
        
        final Enumeration panelEnum = loadedPanels.keys();
        
        if (DEBUG) {
            System.err.println("PrefDialogue beginning loop through panels to write out all settings.");
        }
        
        while (panelEnum.hasMoreElements()) {
            tempObj = panelEnum.nextElement();
            tempName = tempObj.toString();
            if (DEBUG) {
                System.err.println("Our tempName is " + tempName);
            }
            
            prefIn = (PrefInterface)loadedPanels.get(tempName);
            // apply the settings to this panel
            prefIn.applySettings();
            
        }
        
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ScamFoxPanel;
    private javax.swing.JButton applyButton;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton okayButton;
    private javax.swing.JPanel optionCard;
    private javax.swing.JTree optionTree;
    // End of variables declaration//GEN-END:variables
    private transient DefaultMutableTreeNode optionRoot;
    /** Enables or disables debugging output */
    private static final boolean DEBUG = false;
    /** A hashtable containing a list of the panels and their human-readable names */
    private transient Hashtable prefPanelHash;
    /** A hashtable containing a list of the current loaded (on demand) panels */
    private transient Hashtable loadedPanels;
    
}
