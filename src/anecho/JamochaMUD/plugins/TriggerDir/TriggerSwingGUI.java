/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * TriggerSwingGUI.java
 *
 * Created on Oct 23, 2010, 9:58:30 PM
 */
package anecho.JamochaMUD.plugins.TriggerDir;

import anecho.gui.JMappedComboBox;
import java.awt.Color;
import java.util.Vector;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import net.sf.wraplog.AbstractLogger;
import net.sf.wraplog.NoneLogger;
import net.sf.wraplog.SystemLogger;

/**
 *
 * @author jeffnik
 */
public class TriggerSwingGUI extends javax.swing.JDialog {

    /** Creates new form TriggerSwingGUI
     * @param parent
     * @param modal 
     */
    public TriggerSwingGUI(final java.awt.Frame parent, final boolean modal) {
        super(parent, modal);
        initComponents();

        setCustomRenderer();

        if (DEBUG) {
            logger = new SystemLogger();
        } else {
            logger = new NoneLogger();
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        buttonGroup1 = new javax.swing.ButtonGroup();
        currentRuleLabel = new javax.swing.JLabel();
        javax.swing.JPanel jPanel1 = new javax.swing.JPanel();
        javax.swing.JButton addRuleButton = new javax.swing.JButton();
        javax.swing.JButton removeRuleButton = new javax.swing.JButton();
        javax.swing.JButton okayButton = new javax.swing.JButton();
        javax.swing.JButton ruleSetCancel = new javax.swing.JButton();
        matchOnlyCheck = new javax.swing.JCheckBox();
        customColourCheck = new javax.swing.JCheckBox();
        customColour = new javax.swing.JTextField();
        colourButton = new javax.swing.JButton();
        ruleNameTF = new javax.swing.JTextField();
        triggerCheck = new javax.swing.JCheckBox();
        triggerTF = new javax.swing.JTextField();
        javax.swing.JButton applyConditionButton = new javax.swing.JButton();
        javax.swing.JButton revertButton = new javax.swing.JButton();
        ruleNameChoice = new javax.swing.JComboBox();
        highlightCheck = new javax.swing.JRadioButton();
        gagCheck = new javax.swing.JRadioButton();
        javax.swing.JLabel ruleComboLabel = new javax.swing.JLabel();
        conditionPanel = new javax.swing.JPanel();
        javax.swing.JScrollPane jScrollPane1 = new javax.swing.JScrollPane();
        ruleConditionTable = new javax.swing.JTable();
        javax.swing.JButton rowButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/plugins/TriggerDir/TriggerBundle"); // NOI18N
        setTitle(bundle.getString("title")); // NOI18N
        getContentPane().setLayout(new java.awt.GridBagLayout());

        currentRuleLabel.setText(bundle.getString("Rule_Name")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        getContentPane().add(currentRuleLabel, gridBagConstraints);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        addRuleButton.setText(bundle.getString("Add_A_Rule")); // NOI18N
        addRuleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addRuleButtonActionPerformed(evt);
            }
        });
        jPanel1.add(addRuleButton, new java.awt.GridBagConstraints());

        removeRuleButton.setText(bundle.getString("Delete_Rule")); // NOI18N
        removeRuleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeRuleButtonActionPerformed(evt);
            }
        });
        jPanel1.add(removeRuleButton, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 15, 0);
        getContentPane().add(jPanel1, gridBagConstraints);

        okayButton.setText(bundle.getString("Okay")); // NOI18N
        okayButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okayButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 10, 0);
        getContentPane().add(okayButton, gridBagConstraints);

        ruleSetCancel.setText(bundle.getString("Cancel")); // NOI18N
        ruleSetCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ruleSetCancelActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 10, 0);
        getContentPane().add(ruleSetCancel, gridBagConstraints);

        matchOnlyCheck.setText(bundle.getString("Highlight_matching_text_only")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 5, 0);
        getContentPane().add(matchOnlyCheck, gridBagConstraints);

        customColourCheck.setText(bundle.getString("colour")); // NOI18N
        customColourCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customColourCheckActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 5, 0);
        getContentPane().add(customColourCheck, gridBagConstraints);

        customColour.setColumns(10);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        getContentPane().add(customColour, gridBagConstraints);

        colourButton.setText(bundle.getString("Choose_colour")); // NOI18N
        colourButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colourButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        getContentPane().add(colourButton, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        getContentPane().add(ruleNameTF, gridBagConstraints);

        triggerCheck.setText(bundle.getString("Trigger_Command")); // NOI18N
        triggerCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                triggerCheckActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        getContentPane().add(triggerCheck, gridBagConstraints);

        triggerTF.setEnabled(false);
        triggerTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                triggerTFActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        getContentPane().add(triggerTF, gridBagConstraints);

        java.util.ResourceBundle bundle1 = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/plugins/TriggerDir/Bundle"); // NOI18N
        applyConditionButton.setText(bundle1.getString("TriggerSwingGUI.applyConditionButton.text")); // NOI18N
        applyConditionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyConditionButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        getContentPane().add(applyConditionButton, gridBagConstraints);

        revertButton.setText(bundle1.getString("TriggerSwingGUI.revertButton.text")); // NOI18N
        revertButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                revertButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        getContentPane().add(revertButton, gridBagConstraints);

        ruleNameChoice.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        ruleNameChoice.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ruleNameChoiceItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 5, 0);
        getContentPane().add(ruleNameChoice, gridBagConstraints);

        buttonGroup1.add(highlightCheck);
        highlightCheck.setSelected(true);
        highlightCheck.setText(bundle1.getString("TriggerSwingGUI.highlightCheck.text")); // NOI18N
        highlightCheck.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                highlightCheckStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(highlightCheck, gridBagConstraints);

        buttonGroup1.add(gagCheck);
        gagCheck.setText(bundle1.getString("TriggerSwingGUI.gagCheck.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(gagCheck, gridBagConstraints);

        ruleComboLabel.setText(bundle1.getString("TriggerSwingGUI.ruleComboLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 5, 0);
        getContentPane().add(ruleComboLabel, gridBagConstraints);

        conditionPanel.setLayout(new java.awt.GridBagLayout());

        ruleConditionTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "Condition", "Rule"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        ruleConditionTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ruleConditionTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(ruleConditionTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        conditionPanel.add(jScrollPane1, gridBagConstraints);

        rowButton.setText(bundle1.getString("TriggerSwingGUI.rowButton.text")); // NOI18N
        rowButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rowButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        conditionPanel.add(rowButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        getContentPane().add(conditionPanel, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void triggerTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_triggerTFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_triggerTFActionPerformed

    private void customColourCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customColourCheckActionPerformed
        changeColorState();
    }//GEN-LAST:event_customColourCheckActionPerformed

    private void triggerCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_triggerCheckActionPerformed
        changeCommandState();
    }//GEN-LAST:event_triggerCheckActionPerformed

    private void colourButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_colourButtonActionPerformed
        selectColour();
    }//GEN-LAST:event_colourButtonActionPerformed

    private void ruleSetCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ruleSetCancelActionPerformed
        cancelDialogue();
    }//GEN-LAST:event_ruleSetCancelActionPerformed

    private void ruleNameChoiceItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ruleNameChoiceItemStateChanged
        // Check to see if any changes have been made to the form
        // Fix Me XXX

        // Update the rule to show the new selection
        updateRuleConditions();
    }//GEN-LAST:event_ruleNameChoiceItemStateChanged

    private void highlightCheckStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_highlightCheckStateChanged
        changeHighlightState();
    }//GEN-LAST:event_highlightCheckStateChanged

    private void revertButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_revertButtonActionPerformed
        // Reverting the rule simply re-reads the rule and displays again (same as updateRuleCondition)
        updateRuleConditions();
    }//GEN-LAST:event_revertButtonActionPerformed

    private void applyConditionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyConditionButtonActionPerformed
        saveRuleChanges();
    }//GEN-LAST:event_applyConditionButtonActionPerformed

    private void okayButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okayButtonActionPerformed
        saveRules();
    }//GEN-LAST:event_okayButtonActionPerformed

    private void addRuleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addRuleButtonActionPerformed
        addRule();
    }//GEN-LAST:event_addRuleButtonActionPerformed

    private void removeRuleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeRuleButtonActionPerformed
        removeRule();
    }//GEN-LAST:event_removeRuleButtonActionPerformed

    private void rowButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rowButtonActionPerformed
        addTableRow();
    }//GEN-LAST:event_rowButtonActionPerformed

    private void ruleConditionTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ruleConditionTableMouseClicked
        triggerMouseEvent(evt);
    }//GEN-LAST:event_ruleConditionTableMouseClicked

    private void triggerMouseEvent(java.awt.event.MouseEvent evt) {
        if (DEBUG) {
            System.err.println("TriggerSwingGUI.triggerMosueEvent " + evt);

        }
    }

    /** Add an additional row to the options table
     * 
     */
    private void addTableRow() {
        final DefaultTableModel model = (DefaultTableModel) ruleConditionTable.getModel();
        final int rowCount = model.getRowCount();
        model.setRowCount(rowCount + 1);
    }

    private void addRule() {
        final Rule tempRule = new Rule();
        final String ruleName = "New Rule";

        tempRule.setRuleName(ruleName);
        triggers.addElement(tempRule);
        ruleNameChoice.addItem(ruleName);

        ruleNameChoice.setSelectedIndex(ruleNameChoice.getItemCount() - 1);

    }

    /**
     * Delete the currently selected rule
     */
    private void removeRule() {
        final int selected = ruleNameChoice.getSelectedIndex();

        if (selected > -1) {
            // Remove the rule from the vector
            triggers.removeElementAt(selected);

            // Remove the rule from the jcombobox
            ruleNameChoice.removeItemAt(selected);

            // Make certain the correct rule conditions show
            updateRuleConditions();
        }
    }

    /** Dispose of this dialogue with-out saving any changes */
    private void cancelDialogue() {
        this.setVisible(false);
        this.dispose();
    }

    private void selectColour() {
        String oldColour = customColour.getText();

        // Create a valid colour

        if (oldColour.length() < 6 || oldColour.length() > 7) {
            oldColour = "FFFFFF";
        } else {
            oldColour = oldColour.substring(1, 7);
        }

        int red, green, blue; //

        try {

            red = Integer.parseInt(oldColour.substring(0, 2), 16);
            green = Integer.parseInt(oldColour.substring(2, 4), 16);
            blue = Integer.parseInt(oldColour.substring(4, 6), 16);

        } catch (Exception colourErr) {

            // Not a valid number
            red = 255;
            green = 255;
            blue = 255;

        }

        final java.awt.Color baseColour = new java.awt.Color(red, green, blue);


        final Color result = javax.swing.JColorChooser.showDialog(this, "Please choose a colour", baseColour);

        // customColour.setText("#" + Integer.toHexString(result.getRGB() & 0x00ffffff));
        final StringBuffer strRes = new StringBuffer("#");
        strRes.append(translateHexColour(result.getRed()));
        strRes.append(translateHexColour(result.getGreen()));
        strRes.append(translateHexColour(result.getBlue()));
        customColour.setText(strRes.toString());

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
            // Append a leading "0" to single digit numbers.  This is to catch single-digit hex numbers
            retCol = '0' + retCol;
        }

        return retCol;

    }

    private void changeHighlightState() {
        final boolean state = highlightCheck.isSelected();

        matchOnlyCheck.setEnabled(state);
        customColourCheck.setEnabled(state);
        customColour.setEnabled(state);
        colourButton.setEnabled(state);

    }

    private void changeCommandState() {
        triggerTF.setEnabled(triggerCheck.isSelected());
    }

    private void changeColorState() {
        final boolean state = customColourCheck.isSelected();
        customColour.setEnabled(state);
        colourButton.setEnabled(state);
    }

    /** Load in the rules */
    private void loadRules() {
        // Read the rules into a vector

        // Add the rules to the combobox
        if (!triggers.isEmpty()) {
            final int totalRules = triggers.size();

            Rule tempRule;
            String ruleName;

            for (int i = 0; i < totalRules; i++) {
                tempRule = (Rule) triggers.get(i);
                ruleName = tempRule.getRuleName();

                if (DEBUG) {
                    System.err.println("TriggerSwingGUI.loadRules rule name: " + ruleName);
                }

                // ruleNameChoice.addItem(tempRule, tempRule.getRuleName());
                ruleNameChoice.addItem(ruleName);
            }
        }
    }

    /** Save the rules */
    private void saveRules() {
//        // Collect all the rules from combobox
//
//        triggers.removeAllElements();
//
//        int totalRules = ruleNameChoice.getComponentCount();
//
//        for (int i = 0; i < totalRules; i++) {
//            triggers.addElement(ruleNameChoice.getItemAt(i));
//        }

        updateRules = true;
        this.setVisible(false);
    }

    /** Returns a vector of the modified rules */
    public Vector getChangedRules() {

        // Triggers may need to be converted back to old-style
        final Vector oldstyleTriggers = new Vector();

        for (int i = 0; i < triggers.size(); i++) {
            // oldstyleTriggers.addElement(triggers.elementAt(i));
            System.err.println("TriggerSwingGUI.getChangedRules: " + triggers.elementAt(i));
            System.err.println(((Rule) triggers.elementAt(i)).convertToOldRule());
            oldstyleTriggers.addElement(((Rule) triggers.elementAt(i)).convertToOldRule());
        }

        return oldstyleTriggers;
    }

    /** Update the dialogue controls based on the currently selected rule */
    private void updateRuleGUI() {

        if (ruleNameChoice.getSelectedIndex() > -1) {
            // There is a selected rule, so we will update our rule options
            // Rule tempRule = (Rule) ruleNameChoice.getSelectedItem();

            final int index = ruleNameChoice.getSelectedIndex();
            final Rule tempRule = (Rule) triggers.elementAt(index);

            // Set the check-boxes
            highlightCheck.setSelected(tempRule.isHighlight());
            matchOnlyCheck.setSelected(tempRule.isMatchOnly());
            customColourCheck.setSelected(tempRule.isColour());
            gagCheck.setSelected(tempRule.isGag());
            triggerCheck.setSelected(tempRule.isTrigger());

            // Update the text fields
            customColour.setText(tempRule.getColourString());
            triggerTF.setText(tempRule.getTriggerString());

            // fill in the tables
            // String[] conditions = tempRule.getConditions();

//            int condCount = tempRule.conditionCount();
//
//            if (condCount > 0) {
//                TableModel model = ruleConditionTable.getModel();
//
//                String tempCond;
//
//                for (int i = 0; i < condCount; i++) {
//                    model.setValueAt(tempRule.getWordAt(i), i, 1);
//                    model.setValueAt(tempRule.getOptionAt(i), i, 2);
//                }
//            }

//            updateRuleConditions();

        }

    }

    /** This method builds an array of rules based on the old vector format
     * @param oldStyleRules An array of rules in the String format
     */
    public void setOldRules(final Vector oldStyleRules) {

        // Clear the triggers in the event there is anything there
        triggers.removeAllElements();
        ruleNameChoice.removeAllItems();

        Rule tempRule;

        if (!oldStyleRules.isEmpty()) {
            final int ruleCount = oldStyleRules.size();

            for (int i = 0; i < ruleCount; i++) {
                tempRule = new Rule();
                tempRule.convertFromOldRule((String) oldStyleRules.elementAt(i));

//                if (DEBUG) {
//                    System.err.println("TriggerSwingGUI.setOldRules rule name: " + tempRule.getRuleName());
//                    System.err.println("TriggerSwingGUI.setOldRules rule: " + tempRule);
//                }
                logger.info("TriggerSwingGUI.setOldRules rule name: " + tempRule.getRuleName());
                logger.info("TriggerSwingGUI.setOldRules rule: " + tempRule);


                // ruleNameChoice.addItem(tempRule.getRuleName(), tempRule);
                triggers.addElement(tempRule);
                ruleNameChoice.addItem(tempRule.getRuleName());
            }
        }
    }

    private void updateRuleConditions() {

        if (ruleNameChoice.getSelectedIndex() > -1) {
            logger.debug("Selected condition item: " + ruleNameChoice.getSelectedItem());

            final int index = ruleNameChoice.getSelectedIndex();
            // Rule tempRule = (Rule)ruleNameChoice.getSelectedMap();
            final Rule tempRule = (Rule) triggers.elementAt(index);

            // Set the editable rule name
            ruleNameTF.setText(tempRule.getRuleName());
            // Empty the table first
            final DefaultTableModel model = (DefaultTableModel) ruleConditionTable.getModel();
            final int rowCount = model.getRowCount();

            logger.debug("TriggerSwingGUI.updateRuleConditions: Table model has " + rowCount + " rows");

            for (int i = 0; i < rowCount; i++) {
                logger.debug("TriggerSwingGUI.updateRuleConditions: removing row " + i);
                model.removeRow(0);
            }

            // Add the appropriate amount of rows to the table model
            final int ruleLen = tempRule.conditionCount();
            model.setRowCount(ruleLen);

            logger.debug("Rule name: " + tempRule.getRuleName());
            logger.debug("Number of rule conditions:" + ruleLen);
            logger.debug("Old rule: " + tempRule.convertToOldRule());

            int optionIndex;
            String optionStr;
            javax.swing.JButton delB;
            final javax.swing.Icon icon = new javax.swing.ImageIcon(getClass().getResource("/anecho/JamochaMUD/icons/22/delete.png"));

            for (int i = 0; i < ruleLen; i++) {
                logger.debug("TriggerSwingGUI.updateRuleConditions option " + i + " option: " + tempRule.getOptionAt(i));
                logger.debug("TriggerSwingGUI.updateRuleConditions condition " + i + " condition: " + tempRule.getConditionAt(i));

                // Set the delete button
                delB = new javax.swing.JButton(icon);
                model.setValueAt(delB, i, 0);

                // Set the condition
                model.setValueAt(tempRule.getConditionAt(i), i, 1);

                // Set the value to be initially shown by the combo-box.  This is a work-around
                // As Java appears to treat this as a label unless the combobox is selected
                optionIndex = comboBox.getMapIndex(tempRule.getOptionAt(i));
                optionStr = comboBox.getItemAt(optionIndex).toString();
                model.setValueAt(optionStr, i, 2);


                logger.debug("Value of cell is " + model.getValueAt(i, 2));
                logger.debug("Setting map to " + tempRule.getOptionAt(i));

            }

            updateRuleGUI();

        }
    }

    private void setCustomRenderer() {
        if (DEBUG) {
            System.err.println("TriggerSwingGUI.TriggerSwingGUI: Setting up custom renderers.");
        }
        final String[] type = {"AND", "NOT", "MODIFIER"};
        final String[] localTrans = {"IS in this message", "is NOT in this message", "MODIFIES the output"};


        final TableColumn comboBoxColumn = ruleConditionTable.getColumnModel().getColumn(2);
        comboBox = new JMappedComboBox(localTrans, type);

        comboBoxColumn.setCellEditor(new DefaultCellEditor(comboBox));

        //Set up tool tips for the sport cells.
        final DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setToolTipText("Click for combo box");
        comboBoxColumn.setCellRenderer(renderer);


        if (DEBUG) {
            System.err.println("Completed setting up custom renderer");
        }

        final TableColumn deleteColumn = ruleConditionTable.getColumnModel().getColumn(0);
        // JButton delButton = new JButton(">Delete<");
        final javax.swing.Icon icon = new javax.swing.ImageIcon(getClass().getResource("/anecho/JamochaMUD/icons/22/delete.png"));

        JButton delButton = new JButton(icon);
        deleteColumn.setCellEditor(new DeleteCellEditor(delButton));
        deleteColumn.setCellRenderer(new anecho.gui.ButtonCellRenderer(delButton));

    }

    /** Write the current rule options into the main rules */
    private void saveRuleChanges() {
        // "Save" the form items into the proper rule
        // Rule saveRule = (Rule) ruleNameChoice.getSelectedItem();
        final int selectedIndex = ruleNameChoice.getSelectedIndex();
        final Rule saveRule = (Rule) triggers.elementAt(selectedIndex);
        saveRule.setRuleName(ruleNameTF.getText());
        saveRule.setColour(customColourCheck.isSelected());
        saveRule.setColourString(customColour.getText());
        saveRule.setGag(gagCheck.isSelected());
        saveRule.setHighlight(highlightCheck.isSelected());
        saveRule.setMatchOnly(matchOnlyCheck.isSelected());
        // saveRule.setMedia(this.);
        saveRule.setTrigger(triggerCheck.isSelected());
        saveRule.setTriggerString(triggerTF.getText());

        // Clear out the old conditions
        saveRule.removeAllConditions();

        // Save the conditions
        final DefaultTableModel model = (DefaultTableModel) ruleConditionTable.getModel();

        final int tableRows = model.getRowCount();

        String word;
        String condition;
        String conditionMap;

        for (int i = 0; i < tableRows; i++) {
            word = model.getValueAt(i, 1).toString();
            condition = model.getValueAt(i, 2).toString();

            // We'll only check to see if the "word" is empty.  Condition should never be empty
            // This is probably not a good assumption.  Fix me XXX
            if (!word.equals("")) {
                // int condInt = Integer.parseInt(comboBox.getMapAt(comboBox.getItemIndex(condition)).toString());
                // conditionMap = trans[condInt];
                conditionMap = comboBox.getMapAt(comboBox.getItemIndex(condition)).toString();

                // conditionMap = comboBox.getMapAt(comboBox.getItemIndex(condition)).toString();
                // conditionMap = comboBox.getItemAt(comboBox.getItemIndex(condition)).toString();

                if (DEBUG) {
                    System.err.println("TriggerSwingGUI.saveRuleChanges() writing condition, word: " + conditionMap + " : " + word);

                }

                saveRule.addCondition(conditionMap, word);

            }
        }

        if (DEBUG) {
            System.err.println("TriggerSwingGUI.saveRuleChanges has " + saveRule.conditionCount() + " conditions");
        }

        // Find a better way of doing this.  Fix Me XXX
        // Insert the revised rule
        ruleNameChoice.insertItemAt(saveRule.getRuleName(), selectedIndex + 1);
        triggers.insertElementAt(saveRule, selectedIndex + 1);
        // Delete the old version of the rule
        ruleNameChoice.removeItemAt(selectedIndex);
        triggers.removeElementAt(selectedIndex);


    }

    public boolean isUpdateRules() {
        return updateRules;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton colourButton;
    private javax.swing.JPanel conditionPanel;
    private javax.swing.JLabel currentRuleLabel;
    private javax.swing.JTextField customColour;
    private javax.swing.JCheckBox customColourCheck;
    private javax.swing.JRadioButton gagCheck;
    private javax.swing.JRadioButton highlightCheck;
    private javax.swing.JCheckBox matchOnlyCheck;
    private javax.swing.JTable ruleConditionTable;
    private javax.swing.JComboBox ruleNameChoice;
    private javax.swing.JTextField ruleNameTF;
    private javax.swing.JCheckBox triggerCheck;
    private javax.swing.JTextField triggerTF;
    // End of variables declaration//GEN-END:variables
    final private Vector triggers = new Vector();
    private static final boolean DEBUG = false;
    private JMappedComboBox comboBox;
    // private TableColumn comboBoxColumn;
    final static private int AND = 0;
    final static private int NOT = 1;
    final static private int MODIFIER = 2;
    final private String[] trans = {"AND", "NOT", "MODIFIER"};
    // This variable indicates to outside classes whether the changes to rules have been saved
    transient private boolean updateRules = false;
    private AbstractLogger logger;
}
