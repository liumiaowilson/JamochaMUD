/*
 * TriggerGUI.java
 *
 * Created on March 18, 2004, 9:49 PM
 * $Id: TriggerGUI.java,v 1.28 2014/07/01 22:20:30 jeffnik Exp $
 *
 * Actual parsing of the rule is done by the Trigger.java class.
 * This class is specifically for the creation and modification
 * of gags, highlights, and triggers.
 */
/* JamochaMUD, a Muck/Mud client program
 * Copyright (C) 1998-2008  Jeff Robinson
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
package anecho.JamochaMUD.plugins.TriggerDir;

// import anecho.JamochaMUD.JMConfig;
import anecho.JamochaMUD.JMConfig;
import java.util.Hashtable;
import anecho.JamochaMUD.plugins.Trigger;
import java.awt.Color;

/** The GUI to allow users to change the rules for their gags and highlights.
 *
 * @author Jeff Robinson
 *
 */
final public class TriggerGUI extends java.awt.Dialog {

    /** Creates new form Trigger
     * @param editableRules A vector of rules passed to the GUI
     * @return Returns the instance of our TriggerGUI class
     */
    // public static synchronized TriggerGUI getInstance(final java.util.Vector editableRules, final anecho.JamochaMUD.plugins.Trigger parentClass) {
    public static synchronized TriggerGUI getInstance(final java.util.Vector editableRules) {
        if (_instance == null) {
            // _instance = new TriggerGUI(editableRules, parentClass);
            _instance = new TriggerGUI(editableRules);
        }

        return _instance;
    }

    /**
     * This is the private initialisation method for TriggerGUI.  As we want
     * this class to be a Singleton, we have this method programmatically called
     * via the getInstance() method.
     * @param editableRules A vector containing the editable rules
     */
    public TriggerGUI(java.util.Vector editableRules) {
        // private TriggerGUI(anecho.JamochaMUD.plugins.Trigger parentClass) {
        // We need to keep track of this to update the rules when we're done
        // As we use a Frame, there is no other clearly visible way of getting
        // this done.
        super(anecho.JamochaMUD.JMConfig.getInstance().getJMFrame(anecho.JamochaMUD.JMConfig.MAINWINDOWVARIABLE), true);

        colourTable = new Hashtable(11);
        colourTable.put(java.util.ResourceBundle.getBundle(TRIGGERBUNDLE).getString("black"), "#000000");
        colourTable.put(java.util.ResourceBundle.getBundle(TRIGGERBUNDLE).getString("cyan"), "#00ffff");
        colourTable.put(java.util.ResourceBundle.getBundle(TRIGGERBUNDLE).getString("dark_gray"), "#a9a9a9");
        colourTable.put(java.util.ResourceBundle.getBundle(TRIGGERBUNDLE).getString("gray"), "#808080");
        colourTable.put(java.util.ResourceBundle.getBundle(TRIGGERBUNDLE).getString("green"), "#00ff00");
        colourTable.put(java.util.ResourceBundle.getBundle(TRIGGERBUNDLE).getString("light_gray"), "#dcdcdc");
        colourTable.put(java.util.ResourceBundle.getBundle(TRIGGERBUNDLE).getString("magenta"), "#ff00ff");
        colourTable.put(java.util.ResourceBundle.getBundle(TRIGGERBUNDLE).getString("orange"), "#ffa500");
        colourTable.put(java.util.ResourceBundle.getBundle(TRIGGERBUNDLE).getString("pink"), "#ffc0cb");
        colourTable.put(java.util.ResourceBundle.getBundle(TRIGGERBUNDLE).getString("red"), "#ff0000");
        colourTable.put(java.util.ResourceBundle.getBundle(TRIGGERBUNDLE).getString("yellow"), "#ffff00");


        int ers = 0;

        if (editableRules != null) {
            ers = editableRules.size();
            if (DEBUG) {
                System.err.println("TriggerGUI.TriggerGUI() editableRules size: " + ers);
            }
        }

        mainRules = new java.util.Vector(0, 1);
        originalRules = new java.util.Vector(0, 1);

        for (int i = 0; i < ers; i++) {
            mainRules.addElement(editableRules.elementAt(i));
            originalRules.addElement(editableRules.elementAt(i));
        }

        initComponents();
        setLocationRelativeTo(anecho.JamochaMUD.JMConfig.getInstance().getJMFrame(anecho.JamochaMUD.JMConfig.MAINWINDOWVARIABLE));


        // We'll keep all our schtuff here in lower-case to make faster comparisons
        andTrans = java.util.ResourceBundle.getBundle(TRIGGERBUNDLE).getString(Trigger.AND).toLowerCase();
        // andTrans = andTrans.toLowerCase();

        notTrans = java.util.ResourceBundle.getBundle(TRIGGERBUNDLE).getString(Trigger.NOT).toLowerCase();
        // notTrans = notTrans.toLowerCase();

        modTrans = java.util.ResourceBundle.getBundle(TRIGGERBUNDLE).getString(Trigger.MODIFIER).toLowerCase();
        // modTrans = modTrans.toLowerCase();

        ((java.awt.CardLayout) this.getLayout()).first(this);

        // Fill our forms with the proper information
        selectRule();

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     *
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        final java.awt.Panel ruleListPanel = new java.awt.Panel();
        final java.awt.Label label1 = new java.awt.Label();
        ruleNameChoice = new java.awt.Choice();
        final java.awt.Button editButton = new java.awt.Button();
        final java.awt.Button addRuleButton = new java.awt.Button();
        final java.awt.Button removeRuleButton = new java.awt.Button();
        final java.awt.Button saveRulesButton = new java.awt.Button();
        final java.awt.Button cancelButton = new java.awt.Button();
        final java.awt.Label label2 = new java.awt.Label();
        final java.awt.TextArea activationEx = new java.awt.TextArea();
        final java.awt.Panel ruleDetailsPanel = new java.awt.Panel();
        java.awt.CheckboxGroup typeGroup = new java.awt.CheckboxGroup();
        final java.awt.Label label3 = new java.awt.Label();
        ruleSetName = new java.awt.TextField();
        final java.awt.Label label4 = new java.awt.Label();
        ruleList = new java.awt.List();
        final java.awt.Button newRuleButton = new java.awt.Button();
        final java.awt.Button deleteRuleButton = new java.awt.Button();
        final java.awt.Button okayButton = new java.awt.Button();
        final java.awt.Button ruleSetCancel = new java.awt.Button();
        ruleOption = new java.awt.Choice();
        ruleWord = new java.awt.TextField();
        gagCheck = new java.awt.Checkbox();
        gagCheck.setCheckboxGroup(typeGroup);
        final java.awt.Checkbox highlightCheck = new java.awt.Checkbox();
        highlightCheck.setCheckboxGroup(typeGroup);
        customColourCheck = new java.awt.Checkbox();
        colourButton = new java.awt.Button();
        customColour = new java.awt.TextField();
        mediaCheck = new java.awt.Checkbox();
        mediaPath = new java.awt.TextField();
        mediaButton = new java.awt.Button();
        matchOnlyCheck = new java.awt.Checkbox();
        triggerCheck = new java.awt.Checkbox();
        triggerTF = new java.awt.TextField();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/plugins/TriggerDir/TriggerBundle"); // NOI18N
        setTitle(bundle.getString("Gags_and_Highlights")); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });
        setLayout(new java.awt.CardLayout());

        ruleListPanel.setName("ruleList"); // NOI18N
        ruleListPanel.setLayout(new java.awt.GridBagLayout());

        label1.setText(bundle.getString("Rule_Name")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 2);
        ruleListPanel.add(label1, gridBagConstraints);

        ruleNameChoice.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ruleNameChoiceItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(7, 2, 2, 2);
        ruleListPanel.add(ruleNameChoice, gridBagConstraints);

        editButton.setLabel(bundle.getString("editThisRule")); // NOI18N
        editButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                editButtonMouseClicked(evt);
            }
        });
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        ruleListPanel.add(editButton, gridBagConstraints);

        addRuleButton.setLabel(bundle.getString("Add_A_Rule")); // NOI18N
        addRuleButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addRuleButtonMouseClicked(evt);
            }
        });
        addRuleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addRuleButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 7, 2, 2);
        ruleListPanel.add(addRuleButton, gridBagConstraints);

        removeRuleButton.setLabel(bundle.getString("Remove_This_Rule")); // NOI18N
        removeRuleButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                removeRuleButtonMouseClicked(evt);
            }
        });
        removeRuleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeRuleButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        ruleListPanel.add(removeRuleButton, gridBagConstraints);

        saveRulesButton.setLabel(bundle.getString("Save_All_Rules")); // NOI18N
        saveRulesButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                saveRulesButtonMouseClicked(evt);
            }
        });
        saveRulesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveRulesButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 7, 7, 2);
        ruleListPanel.add(saveRulesButton, gridBagConstraints);

        cancelButton.setLabel(bundle.getString("Cancel_All_Changes")); // NOI18N
        cancelButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cancelButtonMouseClicked(evt);
            }
        });
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 2, 7, 7);
        ruleListPanel.add(cancelButton, gridBagConstraints);

        label2.setText(bundle.getString("This_rule_will_activate_if_a_line:")); // NOI18N
        label2.setVisible(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 7, 2, 7);
        ruleListPanel.add(label2, gridBagConstraints);

        activationEx.setEditable(false);
        activationEx.setVisible(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 7, 7, 7);
        ruleListPanel.add(activationEx, gridBagConstraints);

        add(ruleListPanel, "ruleList");

        ruleDetailsPanel.setName("ruleDetails"); // NOI18N
        ruleDetailsPanel.setLayout(new java.awt.GridBagLayout());

        label3.setText(bundle.getString("Rule_Name")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 2, 2);
        ruleDetailsPanel.add(label3, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 2, 2, 7);
        ruleDetailsPanel.add(ruleSetName, gridBagConstraints);

        label4.setText(bundle.getString("TriggerGUI.label4.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 7, 2, 7);
        ruleDetailsPanel.add(label4, gridBagConstraints);

        ruleList.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ruleListItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 7, 2, 7);
        ruleDetailsPanel.add(ruleList, gridBagConstraints);

        newRuleButton.setLabel(bundle.getString("Add_Rule")); // NOI18N
        newRuleButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                newRuleButtonMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 7, 2, 2);
        ruleDetailsPanel.add(newRuleButton, gridBagConstraints);

        deleteRuleButton.setLabel(bundle.getString("Delete_Rule")); // NOI18N
        deleteRuleButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                deleteRuleButtonMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        ruleDetailsPanel.add(deleteRuleButton, gridBagConstraints);

        okayButton.setLabel(bundle.getString("Okay")); // NOI18N
        okayButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okayButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 2);
        ruleDetailsPanel.add(okayButton, gridBagConstraints);

        ruleSetCancel.setLabel(bundle.getString("Cancel")); // NOI18N
        ruleSetCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ruleSetCancelActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(7, 2, 7, 7);
        ruleDetailsPanel.add(ruleSetCancel, gridBagConstraints);

        ruleOption.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ruleOptionItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 7, 2, 2);
        ruleDetailsPanel.add(ruleOption, gridBagConstraints);

        ruleWord.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ruleWordKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        ruleDetailsPanel.add(ruleWord, gridBagConstraints);

        gagCheck.setLabel(bundle.getString("Gag")); // NOI18N
        gagCheck.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                gagCheckItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 7, 2, 7);
        ruleDetailsPanel.add(gagCheck, gridBagConstraints);

        highlightCheck.setLabel(bundle.getString("Highlight")); // NOI18N
        highlightCheck.setState(true);
        highlightCheck.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                highlightCheckItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.25;
        gridBagConstraints.insets = new java.awt.Insets(2, 7, 2, 7);
        ruleDetailsPanel.add(highlightCheck, gridBagConstraints);

        customColourCheck.setLabel(bundle.getString("colour")); // NOI18N
        customColourCheck.setState(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.25;
        gridBagConstraints.insets = new java.awt.Insets(2, 7, 2, 2);
        ruleDetailsPanel.add(customColourCheck, gridBagConstraints);

        colourButton.setEnabled(false);
        colourButton.setLabel(bundle.getString("Choose_colour")); // NOI18N
        colourButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                colourButtonMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.25;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 7);
        ruleDetailsPanel.add(colourButton, gridBagConstraints);

        customColour.setColumns(6);
        customColour.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.25;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        ruleDetailsPanel.add(customColour, gridBagConstraints);

        mediaCheck.setLabel(bundle.getString("Media")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 7, 2, 2);
        ruleDetailsPanel.add(mediaCheck, gridBagConstraints);

        mediaPath.setColumns(6);
        mediaPath.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        ruleDetailsPanel.add(mediaPath, gridBagConstraints);

        mediaButton.setEnabled(false);
        mediaButton.setLabel(bundle.getString("Browse")); // NOI18N
        mediaButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mediaButtonMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 7);
        ruleDetailsPanel.add(mediaButton, gridBagConstraints);

        matchOnlyCheck.setLabel(bundle.getString("TriggerGUI.matchOnlyCheck.label")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 7, 2, 7);
        ruleDetailsPanel.add(matchOnlyCheck, gridBagConstraints);

        triggerCheck.setLabel(bundle.getString("TriggerGUI.triggerCheck.label")); // NOI18N
        triggerCheck.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                triggerCheckItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 7, 2, 7);
        ruleDetailsPanel.add(triggerCheck, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 7, 2, 7);
        ruleDetailsPanel.add(triggerTF, gridBagConstraints);

        add(ruleDetailsPanel, "ruleDetails");

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void triggerCheckItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_triggerCheckItemStateChanged
        // Toggle the state of the trigger text field based on the check box
        triggerTF.setEnabled(triggerCheck.getState());

    }//GEN-LAST:event_triggerCheckItemStateChanged

    private void ruleOptionItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ruleOptionItemStateChanged

        // Change the rule type

        updateRule();

    }//GEN-LAST:event_ruleOptionItemStateChanged

    private void ruleWordKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ruleWordKeyReleased

        // Add your handling code here:
        // help get rid of the need to hit "update" after typing new rules
        final int selected = ruleList.getSelectedIndex();

        updateRule();

        ruleList.select(selected);

    }//GEN-LAST:event_ruleWordKeyReleased

    private void ruleListItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ruleListItemStateChanged

        // Add your handling code here:
        // Update the rule options

        final int selected = ruleList.getSelectedIndex();
        showRuleWord(selected);

    }//GEN-LAST:event_ruleListItemStateChanged

    private void highlightCheckItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_highlightCheckItemStateChanged

        // Add your handling code here:

        changeOptionStates(true);

    }//GEN-LAST:event_highlightCheckItemStateChanged

    private void gagCheckItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_gagCheckItemStateChanged

        // Add your handling code here:
        final boolean state = gagCheck.getState();

        if (state) {
            changeOptionStates(false);
        }

    }//GEN-LAST:event_gagCheckItemStateChanged

    private void ruleNameChoiceItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ruleNameChoiceItemStateChanged
        // Add your handling code here:
        final String rTitle = ruleNameChoice.getSelectedItem();
        ruleSetName.setText(rTitle);
    }//GEN-LAST:event_ruleNameChoiceItemStateChanged

    private void okayButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okayButtonActionPerformed
        // Add your handling code here:
        if (DEBUG) {
            System.err.println("Rule has been accepted.");
        }

        writeNewRule();

        // Change editing status
        editing = !editing;

        setEditStatus(false);

    }//GEN-LAST:event_okayButtonActionPerformed

    private void ruleSetCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ruleSetCancelActionPerformed
        // Add your handling code here:
        editAddRule(ruleNameChoice.getSelectedIndex());

        if (editing) {
            editing = false;
        } else {
            editing = true;
        }

        setEditStatus(false);  // Fix this XXX - probably remove boolean from method

    }//GEN-LAST:event_ruleSetCancelActionPerformed

    private void mediaButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mediaButtonMouseClicked

        // Add your handling code here:

        selectFile(mediaPath, mediaPath.getText());

    }//GEN-LAST:event_mediaButtonMouseClicked

    private void colourButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colourButtonMouseClicked

        selectColour();
//        // Create our colour selector!!
//
//        String oldColour = customColour.getText();
//
//        // Create a valid colour
//
//        if (oldColour.length() < 6 || oldColour.length() > 7) {
//            oldColour = "FFFFFF";
//        } else {
//            oldColour = oldColour.substring(1, 7);
//        }
//
//        int red, green, blue; //
//
//        try {
//
//            red = Integer.parseInt(oldColour.substring(0, 2), 16);
//            green = Integer.parseInt(oldColour.substring(2, 4), 16);
//            blue = Integer.parseInt(oldColour.substring(4, 6), 16);
//
//        } catch (Exception colourErr) {
//
//            // Not a valid number
//            red = 255;
//            green = 255;
//            blue = 255;
//
//        }
//
//        final java.awt.Color baseColour = new java.awt.Color(red, green, blue);
//        final java.awt.Frame parentFrame = anecho.JamochaMUD.JMConfig.getInstance().getJMFrame(anecho.JamochaMUD.JMConfig.MAINWINDOWVARIABLE);
//
//        if (JMConfig.getInstance().getJMboolean(JMConfig.USESWING)) {
//
//            final Color result = javax.swing.JColorChooser.showDialog(this, "Please choose a colour", baseColour);
//
//            customColour.setText("#" + Integer.toHexString(result.getRGB() & 0x00ffffff));
//
//        } else {
//            final anecho.gui.ColourPicker chooser = new anecho.gui.ColourPicker(parentFrame, java.util.ResourceBundle.getBundle(TRIGGERBUNDLE).getString("Choose_a_colour"), baseColour);
//
//            // set the colour selector visible
//            chooser.show();
//
//            // update customColour textArea
//            if (chooser.getColour() != null) {
//                customColour.setText("#" + chooser.getHexColour());
//            }
//        }



    }//GEN-LAST:event_colourButtonMouseClicked

    private void selectColour() {
        // Create our colour selector!!

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

        } catch (NumberFormatException colourErr) {

            // Not a valid number
            red = 255;
            green = 255;
            blue = 255;

        }

        final java.awt.Color baseColour = new java.awt.Color(red, green, blue);

        if (JMConfig.getInstance().getJMboolean(JMConfig.USESWING)) {

            final Color result = javax.swing.JColorChooser.showDialog(this, "Please choose a colour", baseColour);

            // customColour.setText("#" + Integer.toHexString(result.getRGB() & 0x00ffffff));
            final StringBuffer strRes = new StringBuffer("#");
            strRes.append(translateHexColour(result.getRed()));
            strRes.append(translateHexColour(result.getGreen()));
            strRes.append(translateHexColour(result.getBlue()));
            customColour.setText(strRes.toString());


        } else {
            final java.awt.Frame parentFrame = anecho.JamochaMUD.JMConfig.getInstance().getJMFrame(anecho.JamochaMUD.JMConfig.MAINWINDOWVARIABLE);
            final anecho.gui.ColourPicker chooser = new anecho.gui.ColourPicker(parentFrame, java.util.ResourceBundle.getBundle(TRIGGERBUNDLE).getString("Choose_a_colour"), baseColour);

            // set the colour selector visible
            chooser.show();

            // update customColour textArea
            if (chooser.getColour() != null) {
                customColour.setText("#" + chooser.getHexColour());
            }
        }

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

    private void deleteRuleButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_deleteRuleButtonMouseClicked
        removeSubRule();
    }//GEN-LAST:event_deleteRuleButtonMouseClicked

    private void newRuleButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_newRuleButtonMouseClicked
        addNewSubRule();

    }//GEN-LAST:event_newRuleButtonMouseClicked

    private void cancelButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cancelButtonMouseClicked
//
//        this.setVisible(false);
//        approvedRules = false;

    }//GEN-LAST:event_cancelButtonMouseClicked

    private void saveRulesButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saveRulesButtonMouseClicked

//        saveRules();

    }//GEN-LAST:event_saveRulesButtonMouseClicked

    private void removeRuleButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_removeRuleButtonMouseClicked

        removeRule();

    }//GEN-LAST:event_removeRuleButtonMouseClicked

    private void addRuleButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addRuleButtonMouseClicked
//
//        addBlankRule();
//        setEditStatus(true);  // Fix this XXX - I don't think we need the boolean anymore

    }//GEN-LAST:event_addRuleButtonMouseClicked

    private void editButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_editButtonMouseClicked
//
//        if (DEBUG) {
//            System.err.println("ruleNameChoice index is: " + ruleNameChoice.getSelectedIndex());
//            System.err.println("ruleNameChoice: " + ruleNameChoice);
//        }
//
//        setEditStatus(true);  // Fix this XXX - I don't think we need to boolean anymore
//
//        if (DEBUG) {
//            System.err.println("ruleNameChoice index is: " + ruleNameChoice.getSelectedIndex());
//        }

    }//GEN-LAST:event_editButtonMouseClicked

    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm

        this.setVisible(false);

    }//GEN-LAST:event_exitForm

    private void removeRuleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeRuleButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_removeRuleButtonActionPerformed

    private void addRuleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addRuleButtonActionPerformed
        
        addBlankRule();
        setEditStatus(true);  // Fix this XXX - I don't think we need the boolean anymore

    }//GEN-LAST:event_addRuleButtonActionPerformed

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed

        if (DEBUG) {
            System.err.println("ruleNameChoice index is: " + ruleNameChoice.getSelectedIndex());
            System.err.println("ruleNameChoice: " + ruleNameChoice);
        }

        setEditStatus(true);  // Fix this XXX - I don't think we need to boolean anymore

        if (DEBUG) {
            System.err.println("ruleNameChoice index is: " + ruleNameChoice.getSelectedIndex());
        }

    }//GEN-LAST:event_editButtonActionPerformed

    private void saveRulesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveRulesButtonActionPerformed
        saveRules();

    }//GEN-LAST:event_saveRulesButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed

        this.setVisible(false);
        approvedRules = false;

    }//GEN-LAST:event_cancelButtonActionPerformed

    /**
     * Removes the currently selected master Rule
     */
    private void removeRule() {
        // if (ruleNameChoice.getItemCount() > 0) {
        // if (ruleNameChoice.getItem() != null) {
        if (ruleNameChoice.getSelectedItem() != null) {
            final int rule = ruleNameChoice.getSelectedIndex();

            ruleNameChoice.remove(rule);

            mainRules.removeElementAt(rule);

            // Make certain our rules show the proper info after removal
            editAddRule(ruleNameChoice.getSelectedIndex());

        }

    }

    /**
     * Removes the currently selected sub-rule from the list
     */
    private void removeSubRule() {

        if (ruleList.getSelectedItem() != null) {
            deleteSubRule(ruleList.getSelectedIndex());
        }

    }

    /**
     * Sets the original rules to the main rules, indicates rules have been approved
     * and dismisses this dialogue
     */
    private void saveRules() {
        // Add your handling code here:
        // We'll write out all the rules and clean up our frame

        if (DEBUG) {
            System.err.println("TriggerGUI.saveRules() Setting saveRules to true.");
        }

        originalRules = mainRules;
        this.setVisible(false);
        approvedRules = true;

    }

    /** Search through the string for the rule name,
     * and extract its information
     */
    private String pullRule(final Object ruleObj, final String ruleName) {

        // Set-up the return value as "None", in the event that we are missing
        // a property (which can happen if new properties are added to JamochaMUD
        // but an old trigger file is used).
        // String retVal = Trigger.NONE;
        String retVal;

        final String rule = ruleObj.toString();

        int start;
        int end;

        start = rule.indexOf(ruleName);

        if (start > -1) {
            // if "start" is -1 then the rule doesn't exist.  We want to keep this value
            // so that we can "fall through".
            start = start + ruleName.length();
        }

        end = rule.indexOf('$', start + 1);

        if (DEBUG) {
            System.err.println("------------------------");
            System.err.println("Trigger.pullRule() ruleName: " + ruleName);
            System.err.println("Trigger.pullRule() rule: " + rule);
            System.err.println("Trigger.pullRule() start: " + start + " end: " + end);
            System.err.println("------------------------");
        }

        if (start > -1) {
            if (DEBUG) {
                System.err.println("Trigger.pullRule() rule: " + ruleName + ": " + rule.substring(start, end));
            }
            retVal = rule.substring(start, end);
        } else {
            if (DEBUG) {
                System.err.println("Trigger.pullRule() rule " + ruleName + " does not exist.  Returning 'None'");
            }
            retVal = Trigger.NONE;
        }

        // return rule.substring(start, end);
        return retVal;

    }
//
//    /** Search through the string for the rule name,
//     * and extract its information
//     */
//    
//    private String pullRule(final Object ruleObj, final String ruleName) {
//        final String rule = ruleObj.toString();
//        int start, end;
//        start = rule.indexOf(ruleName);
//        start = start + ruleName.length();
//        end = rule.indexOf("$", start + 1);
//        
//        return rule.substring(start, end);
//        
//    }
//    /** Check the passed string to see if it matches one of our rules
//     * @param input The input string that the rule will be checked against
//     * @param set The rule-set to check.  A rule-set is a complete rule.
//     * @param rule The specific rule within a set to check against.  Usually a condition.
//     */
//    private int CheckRule(String inString, int set, int rule) {
//        String input = inString;
//        String chkRule = new String(pullRule(mainRules.elementAt(set), "$" + rule + ":"));
//        String matchType = transToType(chkRule, false);
//        // chkRule = chkRule.toLowerCase();
//        // chkRule = transToType(chkRule, false);
//        String match = (chkRule.substring(chkRule.indexOf(":") + 1)).toLowerCase();
//        input = input.toLowerCase();
//        int state = NO_MATCH;
//
//        // if (chkRule.startsWith(java.util.ResourceBundle.getBundle(TRIGGERBUNDLE).getString(AND))) {
//        // if (chkRule.startsWith(ANDSTR) || chkRule.startsWith(andTrans)) {
//        if (matchType.startsWith(ANDSTR)) {
//            // if (input.indexOf(match) > 0 || input.startsWith(match)) {
//            if (input.indexOf(match) > -1) {
//                state = TRUE_MATCH;
//            } else {
//                // We didn't meet an "AND" condition, so this trigger fails
//                state = FALSE_MATCH;
//            }
//        }
//
//        // if (chkRule.startsWith(java.util.ResourceBundle.getBundle(TRIGGERBUNDLE).getString("OR"))) {
//        //            if (matchType.startsWith(orStr)) {
//        //                // if (input.indexOf(match) > 0 || input.startsWith(match)) {
//        //                if (input.indexOf(match) > -1) {
//        //                    state = TRUE_MATCH;
//        //                }
//        //            }
//
//        // if (chkRule.startsWith(java.util.ResourceBundle.getBundle(TRIGGERBUNDLE).getString(NOT))) {
//        // if (chkRule.startsWith(NOTSTR) || chkRule.startsWith(notTrans)) {
//        if (matchType.startsWith(NOTSTR)) {
//            // if (input.indexOf(match) > 0 || input.startsWith(match)) {
//            if (input.indexOf(match) > -1) {
//                state = FALSE_MATCH;
//            }
//        }
//
//        // if (chkRule.startsWith(MODIFIER)) {
//        // if (chkRule.startsWith(MODSTR) || chkRule.startsWith(modTrans)) {
//        if (matchType.startsWith(MODSTR)) {
//            if (input.indexOf(match) > -1) {
//                state = MODIFIER_MATCH;
//            }
//        }
//        return state;
//    }
//    /** Apply the Gag to the String we were passed from the main program */
//    private String ApplyGag(StringBuffer sb, int set) {
//        String colour = pullRule(mainRules.elementAt(set), COLOUR);
//        colour = new String(colour.toLowerCase());
//        // String colourCode = new String();
//        String colourCode = "";
//        if (colour.equals(java.util.ResourceBundle.getBundle(TRIGGERBUNDLE).getString("gag"))) {
//            return "";	// Not a colour, but a gag!
//        }
//
//        // Make the colour our colour name by default, change if necessary
//
//        if (colour.startsWith("#")) {
//            colourCode = '\u001b' + "[" + colour + "m";
//        }
//
//        if (colour.equals(java.util.ResourceBundle.getBundle(TRIGGERBUNDLE).getString("black"))) {
//            colourCode = '\u001b' + "[30m";
//        }
//        if (colour.equals(java.util.ResourceBundle.getBundle(TRIGGERBUNDLE).getString("red"))) {
//            colourCode = '\u001b' + "[31m";
//        }
//        if (colour.equals(java.util.ResourceBundle.getBundle(TRIGGERBUNDLE).getString("green"))) {
//            colourCode = '\u001b' + "[32m";
//        }
//        if (colour.equals(java.util.ResourceBundle.getBundle(TRIGGERBUNDLE).getString("yellow"))) {
//            colourCode = '\u001b' + "[33m";
//        }
//        if (colour.equals(java.util.ResourceBundle.getBundle(TRIGGERBUNDLE).getString("blue"))) {
//            colourCode = '\u001b' + "[34m";
//        }
//        if (colour.equals(java.util.ResourceBundle.getBundle(TRIGGERBUNDLE).getString("magenta"))) {
//            colourCode = '\u001b' + "[35m";
//        }
//        if (colour.equals(java.util.ResourceBundle.getBundle(TRIGGERBUNDLE).getString("cyan"))) {
//            colourCode = '\u001b' + "[36m";
//        }
//        if (colour.equals(java.util.ResourceBundle.getBundle(TRIGGERBUNDLE).getString("white"))) {
//            colourCode = '\u001b' + "[37m";
//        }
//
//        if (sb.length() > 0 && sb.charAt(0) != '\u001b') {
//            sb.insert(0, colourCode);
//            sb.append('\u001b' + "[0m");
//        }
//
//        return sb.toString();
//    }
//    /** Play media is triggered, so let's make some noise! */
//    private void playMedia(String audioFileName) {
//        // This stuff is drawn from the Applet.AudioClip class.  Yuckyweird.
//        // try {
//        // AudioClip myClip = getAudioClip(new URL(getCodeBase(), "clip.au"));
//        // } catch (Exception e) {
//        // }
//        try {
//            sun.audio.AudioDataStream audioDataStream;
//            sun.audio.AudioPlayer audioPlayer = sun.audio.AudioPlayer.player;
//            java.io.FileInputStream fis = new java.io.FileInputStream( new java.io.File(audioFileName) );
//            sun.audio.AudioStream as = new sun.audio.AudioStream( fis ); // header plus audio data
//            sun.audio.AudioData ad = as.getData(); // audio data only, no header
//            audioDataStream = new sun.audio.AudioDataStream( ad );
//            audioPlayer.start( audioDataStream );
//        } catch (Exception e) {
//            System.out.println(java.util.ResourceBundle.getBundle(TRIGGERBUNDLE).getString("An_exception_occured_trying_to_play_this_file:"));
//            System.out.println(e);
//        }
//
//    }
//    /** Do an "installation check", and read in triggers
//     * if they already exist.  If not, create the proper space
//     */
//    private void ReadTriggers() {
//        // if (!triggerFile.exists()) return;
//        // No trigger file, no need to continue
//        // We'll open the .trigger.rc file and read in the rulesets
//        java.io.RandomAccessFile reader;
//        boolean loop = true;
//
//        if (DEBUG) {
//            System.err.println("Trying to read in triggers...");
//            System.err.println("Trigger File: " + triggerFile.toString());
//        }
//
//        try {
//            reader = new java.io.RandomAccessFile(triggerFile.toString(), "r");
//
//
//            String line;
//            StringBuffer fullLine = new StringBuffer("");
//
//            while (loop) {
//                try {
//                    line = reader.readLine();
//                } catch (Exception e) {
//                    // We're all out of lines
//                    break;
//                }
//
//                if (line == null || line.trim().equals("")) {
//                    loop = false;
//                    break;
//                }
//
//                line.trim();
//                fullLine.append(line);
//
//                if (DEBUG) {
//                    System.err.println("Read rule: " + line);
//                }
//
//
//                if (line != null && line.indexOf("$DONE") > 0) {
//                    mainRules.addElement(fullLine.toString());
//                    fullLine = new StringBuffer("");
//                }
//
//            }
//
//            reader.close();
//            setChoices();
//
//        } catch(Exception e) {
//            // We can't find our trigger rules.  Chances are they will be created on the first-run
//            if (DEBUG) {
//                System.out.println("Trigger plugin could not access " + triggerFile + ", exception " + e);
//                e.printStackTrace();
//            }
//        }
//
//        return;
//    }

    /** This displays a list of available rules, allowing the
     *
     * user to add, edit, or delete rules. */
    private void selectRule() {
        // Show our rules dialogue (adapted for NetBeans)
        // if (DEBUG) {
        //    System.err.println("Trigger.selectRule calling initComponents");
        //}

        // initComponents();
        setChoices();

        // Set all the rule options
        ruleOption.removeAll();
        ruleOption.add(java.util.ResourceBundle.getBundle(TRIGGERBUNDLE).getString(Trigger.AND));
        ruleOption.add(java.util.ResourceBundle.getBundle(TRIGGERBUNDLE).getString(Trigger.NOT));
        ruleOption.add(java.util.ResourceBundle.getBundle(TRIGGERBUNDLE).getString(Trigger.MODIFIER));

        // Add our colours to the list
        //colourSample.removeAll();
        // colourSample.add("custom");
        //colourSample.add("black");
        //colourSample.add("blue");
        //colourSample.add("cyan");
        //colourSample.add("dark gray");
        //colourSample.add("gray");
        //colourSample.add("green");
        //colourSample.add("light gray");
        //colourSample.add("magenta");
        //colourSample.add("orange");
        //colourSample.add("pink");
        //colourSample.add("red");
        //colourSample.add("white");
        //colourSample.add("yellow");

        // Make certain we're showing the proper rule information
        // if (ruleName != null) {
        editAddRule(ruleNameChoice.getSelectedIndex());
        // }
        setEditStatus(editing);

        // Show 'em what we're made of!
        // ruleFrame.setVisible(true);
        // this.setVisible(true);
    }

    /** read the new vector into the choices
     * for our main dialogue
     */
    private void setChoices() {
        if (DEBUG) {
            System.err.println("Trigger.SetChoices entered.  Clearing rules.");
        }

        ruleNameChoice.removeAll();
        String tempName;
        final int mRuleSize = mainRules.size();

        for (int i = 0; i < mRuleSize; i++) {
            tempName = pullRule(mainRules.elementAt(i), Trigger.RULENAME);

            if (DEBUG) {
                System.err.println("TriggerGUI.setChoice() tempName: " + tempName);
            }

            if (tempName != null) {
                ruleNameChoice.add(tempName);
            }

        }

        // Show information about our rule
        editAddRule(ruleNameChoice.getSelectedIndex());

    }

    /** Add a new rule or edit an existing rule
     * -1 indicates that we'll be dealing with a
     * new rule, otherwise we'll edit the rule
     * number passed by the int.
     */
    private void editAddRule(final int inRule) {
        // int rule = inRule;
        int rule;

        // There is a posibility that we don't have any rules yet
        if (mainRules == null) {
            mainRules = new java.util.Vector(0, 1);
        }

        if (DEBUG) {
            System.err.println("TriggerGUI.editAddRule() mainRules size: " + mainRules.size());
        }

        if (mainRules.isEmpty()) {
            // Create a new blank rule and set our pointer to that
            addBlankRule();
            rule = 0;
            // } else if (rule < 0) {
        } else if (inRule < 0) {
            rule = 0;
        } else {
            rule = inRule;
        }

        // if (rule >= 0) {

        theRules = new String((String) mainRules.elementAt(rule));

        ruleSetName.setText(pullRule(theRules, Trigger.RULENAME));

        setupRules(); // Fill in the appropriate rule areas

        // }

    }

    /** Toggle the portions of the dialogue to either allow
     * or disallow editing of the current rule
     * ***NOTE NOTE NOTE***
     * With the use of Netbeans and the Cardlayout this method
     * has now underwent some rather dramatic changes and no
     * long needs to be as kludgy!
     * 
     * @param editing
     */
    private void setEditStatus(final boolean editing) {

        if (editing) {

            ((java.awt.CardLayout) this.getLayout()).last(this);

            if (DEBUG) {

                System.err.println("Trying to show ruleDetails pane.");

            }

        } else {

            ((java.awt.CardLayout) this.getLayout()).first(this);

            if (DEBUG) {

                System.err.println("Trying to show ruleList pane.");

            }

        }



        if (editing) {

            if (DEBUG) {

                System.err.println(java.util.ResourceBundle.getBundle(TRIGGERBUNDLE).getString("Setting_up_rule_#") + ruleNameChoice.getSelectedIndex());

            }



            // Make certain all of our settings for this rule check out

            setupRules();

            editAddRule(ruleNameChoice.getSelectedIndex());

        }



    }

    /**
     * This creates a new blank rule that can later be edited
     */
    private void addBlankRule() {

        mainRules.addElement("$RULES:1$0:AND:Blank$COLOUR:red$LAUNCH:None$MEDIA:None$TRIGGER:None$NAME:Rule " + mainRules.size() + "$DONE");

        ruleNameChoice.add("Rule " + mainRules.size());

        ruleNameChoice.select(mainRules.size() - 1);

        editAddRule(mainRules.size() - 1);

    }

    /** This will read in a rule, breaking it up
     * for nice display in our little rule-mangler!
     */
    private void setupRules() {

        // First, we'll check to see if this is a GAG,
        // and if so, we'll disable half our options now
        // if (pullRule(theRules, Trigger.COLOUR).equalsIgnoreCase(java.util.ResourceBundle.getBundle(TRIGGERBUNDLE).getString("Trigger.GAG"))) {
        if (pullRule(theRules, Trigger.COLOUR).equalsIgnoreCase(Trigger.GAG)) {

            gagCheck.setState(true);
            changeOptionStates(false);
        } else {
            changeOptionStates(true);

            // Better safe than sorry, no?
            gagCheck.setState(false);

            // Set colour information
            String cName = pullRule(theRules, Trigger.COLOUR).toLowerCase();

            if (cName.charAt(0) == '#') {
                customColour.setText(cName);
            } else {
                // This is support for "legacy" triggers that
                // used the colour name instead of code
                customColour.setText(nameToCode(cName));
            }

            cName = pullRule(theRules, Trigger.MATCHONLY).toLowerCase();

            if (DEBUG) {
                System.err.println("TriggerGUI.SetupRules reads our MATCHONLY variable as *" + cName + "*");
            }

            // if (cName.equals("true")) {
            if ("true".equals(cName)) {
                if (DEBUG) {
                    System.err.println("TriggerGUI.SetupRules setting matchOnlyCheck to true");
                }

                matchOnlyCheck.setState(true);

            } else {
                if (DEBUG) {
                    System.err.println("TriggerGUI.SetupRules setting matchOnlyCheck to false");
                }
                matchOnlyCheck.setState(false);
            }

            // This is a custom colour
            customColour.setEnabled(true);
            colourButton.setEnabled(true);

        }

        // Fill in the media information
        final String text = pullRule(theRules, Trigger.MEDIA);

        if (text.equals(Trigger.NONE)) {
            mediaPath.setText("");
            mediaCheck.setState(false);
            setMediaState(false);
        } else {
            mediaPath.setText(text);
            mediaCheck.setState(true);
            setMediaState(true);
        }

        final String triggerText = pullRule(theRules, Trigger.TRIGGERSTR);
        // boolean tStatus = true;
        boolean tStatus;

        if (triggerText.equals(Trigger.NONE)) {
            triggerTF.setText("");
            tStatus = false;
        } else {
            triggerTF.setText(triggerText);
            tStatus = true;
        }

        triggerCheck.setState(tStatus);
        setTriggerState(tStatus);

        refreshRules();



        // For the convenience of the user, select the first rule

        ruleList.select(0);

        showRuleWord(0);



    }

    /** Toggle the state of the "media"-related widgets.  This
     * would generally be a simple matter but for the fact that
     * they are also controlled by the "GAG" checkbox
     */
    private void setMediaState(final boolean stat) {

        if (gagCheck.getState()) {
            mediaCheck.setEnabled(false);
            mediaPath.setEnabled(false);
            mediaButton.setEnabled(false);
        } else {
            mediaCheck.setEnabled(true);
            mediaPath.setEnabled(stat);
            mediaButton.setEnabled(stat);
        }

    }

    /**
     * This method enables or disables the textfield containing trigger commands
     * @param stat
     */
    private void setTriggerState(final boolean stat) {
        triggerTF.setEnabled(stat);
    }

    /** Parse the set of rules, writing it out
     *
     * to our list
     *
     */
    private void refreshRules() {

        // Get the number of rules that we're dealing with

        String ruleType, cleanType, tag;

        final int num = Integer.parseInt(pullRule(theRules, Trigger.RULES));

        if (num < 1) {

            return;// This shouldn't happen... but y'never know

        }



        ruleList.removeAll();

        for (int i = 0; i < num; i++) {

            ruleType = pullRule(theRules, "$" + i + ":");

            // cleanType = ruleType.substring(0, ruleType.indexOf(":"));
            cleanType = ruleType.substring(0, ruleType.indexOf(':'));

            // tag = (ruleType.substring(ruleType.indexOf(":") + 1));
            tag = ruleType.substring(ruleType.indexOf(':') + 1);





            // if (cleanType.toLowerCase().equals(ANDSTR)) {
            if (cleanType.equalsIgnoreCase(ANDSTR)) {

                cleanType = java.util.ResourceBundle.getBundle(TRIGGERBUNDLE).getString(Trigger.AND);

            }



            // if (cleanType.toLowerCase().equals(NOTSTR)) {
            if (cleanType.equalsIgnoreCase(NOTSTR)) {

                cleanType = java.util.ResourceBundle.getBundle(TRIGGERBUNDLE).getString(Trigger.NOT);

            }



            // if (cleanType.toLowerCase().equals(MODSTR)) {
            if (cleanType.equalsIgnoreCase(MODSTR)) {

                cleanType = java.util.ResourceBundle.getBundle(TRIGGERBUNDLE).getString(Trigger.MODIFIER);

            }



            ruleList.add(cleanType + " -> " + tag);

        }

    }

    /** Set up our ruleWord textArea and associated "choice"
     *
     */
    private void showRuleWord(final int selected) {

        String ruleType, tag, typeName;

        ruleType = pullRule(theRules, "$" + selected + ":");

        // tag = (ruleType.substring(ruleType.indexOf(':') + 1));
        tag = ruleType.substring(ruleType.indexOf(':') + 1);

        typeName = ruleType.substring(0, ruleType.indexOf(':'));

        if (DEBUG) {
            System.err.println("showRuleWord typeName: " + typeName);
        }

        ruleOption.select(typeToTrans(typeName));

        ruleWord.setText(tag);

    }

    /** Add an additional subrule to an existing rule */
    private void addNewSubRule() {
        // Half of this method looks the same as deleteSubRule.  Combine.  Fix Me XXX
        final int rules = Integer.parseInt(pullRule(theRules, Trigger.RULES));

        final StringBuffer workString = new StringBuffer();
        workString.append(Trigger.RULES);
        workString.append(rules + 1);

        for (int i = 0; i < rules; i++) {
            workString.append("$" + i + ":" + (pullRule(theRules, "$" + i + ":")));
        }

        workString.append('$');
        workString.append(rules);
        workString.append(":AND:blank");

        // Now, glean the rest of the info from out dialogues
        //        workString.append(Trigger.COLOUR);
        //        workString.append(pullRule(theRules, Trigger.COLOUR));
        //        workString.append(Trigger.LAUNCH);
        //        workString.append(pullRule(theRules, Trigger.LAUNCH));
        //        workString.append(Trigger.MEDIA);
        //        workString.append(pullRule(theRules, Trigger.MEDIA));
        //        workString.append(Trigger.NAME);
        //        workString.append(pullRule(theRules, Trigger.RULENAME));
        //        workString.append(Trigger.TRIGGERSTR);
        //        workString.append(pullRule(theRules, Trigger.TRIGGERSTR));
        //        workString.append(Trigger.DONE);

        workString.append(ruleFromDialog());

        theRules = workString.toString();

        refreshRules();

        ruleList.select(rules);

        showRuleWord(rules);

    }

    /** Delete a subrule from our current set of working rules. */
    private void deleteSubRule(final int ruleIndex) {

        // Part of this is identical to addNewSubRule.
        // Fix Me XXX
        int count = 0;

        final int rules = Integer.parseInt(pullRule(theRules, Trigger.RULES));

        final StringBuffer workString = new StringBuffer();
        workString.append(Trigger.RULES);
        workString.append(rules - 1);

        for (int i = 0; i < rules; i++) {
            if (i != ruleIndex) {
                workString.append("$" + count + ":" + (pullRule(theRules, "$" + i + ":")));
                count++;
            }
        }

        // Now, glean the rest of the info from out dialogues
//        workString.append(Trigger.COLOUR);
//        workString.append(pullRule(theRules, Trigger.COLOUR));
//        workString.append(Trigger.LAUNCH);
//        workString.append(pullRule(theRules, Trigger.LAUNCH));
//        workString.append(Trigger.MEDIA);
//        workString.append(pullRule(theRules, Trigger.MEDIA));
//        workString.append(Trigger.NAME);
//        workString.append(pullRule(theRules, Trigger.RULENAME));
//        workString.append(Trigger.TRIGGERSTR);
//        workString.append(pullRule(theRules, Trigger.TRIGGERSTR));
//        workString.append(Trigger.DONE);

        workString.append(ruleFromDialog());
        // Finished building the new rule, now we'll put it back in
        // into the "main array"

        theRules = workString.toString();

        refreshRules();

    }

    /**
     * Collects the "static" dialogue information for the COLOUR,
     * LAUNCH, MEDIA, NAME, TRIGGERSTR, and DONE.
     * @return A string representing the "static" portion of the sub-rule dialogue
     */
    private String ruleFromDialog() {
        final StringBuffer workString = new StringBuffer();

        workString.append(Trigger.COLOUR);
        workString.append(pullRule(theRules, Trigger.COLOUR));
        workString.append(Trigger.LAUNCH);
        workString.append(pullRule(theRules, Trigger.LAUNCH));
        workString.append(Trigger.MEDIA);
        workString.append(pullRule(theRules, Trigger.MEDIA));
        workString.append(Trigger.NAME);
        workString.append(pullRule(theRules, Trigger.RULENAME));
        workString.append(Trigger.TRIGGERSTR);
        workString.append(pullRule(theRules, Trigger.TRIGGERSTR));
        workString.append(Trigger.DONE);

        // theRules = workString.toString();
        return workString.toString();

    }

    /** Modify an existing rule */
    private void updateRule() {

        final int item = ruleList.getSelectedIndex();

        if (ruleList.getSelectedItem() == null || ruleWord.getText().equals("")) {
            return;
        }

        changeRule(item, ruleOption.getSelectedItem(), ruleWord.getText());
        refreshRules();

        ruleList.select(item);

    }

    /** Show a file-selection dialogue and the insert the results
     * from the selection into the proper location
     */
    private void selectFile(final java.awt.TextField text, final String oldPath) {

        final java.awt.FileDialog fileChooser = new java.awt.FileDialog(this, "Select a file", java.awt.FileDialog.LOAD);

        if (!"".equals(oldPath)) {
            fileChooser.setFile(oldPath);
        }

        fileChooser.setVisible(true);

        if (fileChooser.getFile() != null && !fileChooser.getFile().equals("")) {
            text.setText(fileChooser.getDirectory() + fileChooser.getFile());
        }

    }

    /** Modify a given rule.*/
    private void changeRule(final int item, final String option, final String ruleWord) {

        final int rules = Integer.parseInt(pullRule(theRules, Trigger.RULES));

        final StringBuffer workString = new StringBuffer(Trigger.RULES);
        workString.append(rules);

        for (int i = 0; i < rules; i++) {

            if (i == item) {
                // This is the rule we change
                workString.append("$" + i + ":" + transToType(option, true) + ":" + ruleWord);
            } else {
                workString.append("$" + i + ":" + (pullRule(theRules, "$" + i + ":")));
            }

        }

        workString.append(Trigger.COLOUR);
        workString.append(pullRule(theRules, Trigger.COLOUR));
        workString.append(Trigger.LAUNCH);
        workString.append(pullRule(theRules, Trigger.LAUNCH));
        workString.append(Trigger.MEDIA);
        workString.append(pullRule(theRules, Trigger.MEDIA));
        workString.append(Trigger.TRIGGERSTR);
        workString.append(pullRule(theRules, Trigger.TRIGGERSTR));
        workString.append(Trigger.NAME);
        workString.append(pullRule(theRules, Trigger.RULENAME));


        workString.append(Trigger.DONE);

        // Copy this as our new working rule, and then update the display
        theRules = workString.toString();

    }

    /** Write out our new rule in the proper format and then update the main window */
    private void writeNewRule() {

        final int rules = Integer.parseInt(pullRule(theRules, Trigger.RULES));
        final StringBuffer workString = new StringBuffer(Trigger.RULES);
        workString.append(rules);

        for (int i = 0; i < rules; i++) {
            workString.append("$" + i + ":" + (pullRule(theRules, "$" + i + ":")));
        }

        // Now, glean the rest of the info from out dialogues
        workString.append(Trigger.COLOUR);

        if (gagCheck.getState()) {
            workString.append("GAG");
        } else {
            workString.append(this.writeNewColourRule());
        }

        workString.append(Trigger.LAUNCH);
        workString.append(Trigger.NONE);
        workString.append(Trigger.MEDIA);
        workString.append(writeNewMediaRule());
        workString.append(writeNewTriggerRule());
        workString.append(Trigger.NAME);
        workString.append(ruleSetName.getText());
        workString.append(Trigger.DONE);

        // Finished building the new rule, now we'll put it back in
        // into the "main array"
        int ruleIndex = ruleNameChoice.getSelectedIndex();

        if (ruleIndex < 0) {
            if (DEBUG) {
                System.err.println("ruleNameChoice.getSelectedIndex() is less than zero.");
            }
            ruleIndex = 0;
        }

        if (DEBUG) {
            System.err.println("ruleNameChoice selectedIndex: " + ruleNameChoice.getSelectedIndex());
            System.err.println("workString: " + workString.toString());
        }

        mainRules.setElementAt(workString.toString(), ruleIndex);

        setChoices();

        ruleNameChoice.select(pullRule(workString.toString(), Trigger.RULENAME));

        editAddRule(ruleNameChoice.getSelectedIndex());

    }

    /**
     * Returns the trigger rule based the current sub-rule dialogue
     * @return A string representing the trigger rule
     */
    private String writeNewTriggerRule() {
        final StringBuffer workString = new StringBuffer();

        workString.append(Trigger.TRIGGERSTR);
        if (triggerCheck.getState() && !triggerTF.getText().equals("")) {
            workString.append(triggerTF.getText());
        } else {
            workString.append(Trigger.NONE);
        }

        return workString.toString();
    }

    /**
     * Writes a string representing the current state of the MEDIA section of this sub-rule
     * @return A string representing the MEDIA for this sub-rule
     */
    private String writeNewMediaRule() {
        final StringBuffer workString = new StringBuffer();

        if (mediaCheck.getState() && !mediaPath.getText().equals("")) {
            workString.append(mediaPath.getText());
        } else {
            workString.append(Trigger.NONE);
        }

        return workString.toString();
    }

    /**
     * Creates a string representing the current colour used in this sub-rule.
     * If the colour is empty, the method returns White (#FFFFFF).
     * @return A hex string displayed by this sub-rule
     */
    private String writeNewColourRule() {
        final StringBuffer workString = new StringBuffer();

        // Check whether we have a custom colour or not
        final String cusColour = customColour.getText();
        final int colLen = cusColour.length();

        if (colLen >= 6 && colLen <= 7) {
            // if (cusColour.length() == 6) {
            if (colLen == 6) {
                workString.append('#');
                workString.append(cusColour);
            } else {
                workString.append(cusColour);
            }
        } else {
            workString.append("#ffffff");
        }


        if (matchOnlyCheck.getState()) {
            workString.append(Trigger.MATCHONLY);
            workString.append("true");
        }

        return workString.toString();
    }

    /**
     * This method enables or disables multiple controls at once
     * @param state
     */
    private void changeOptionStates(final boolean state) {

        customColourCheck.setEnabled(state);

        customColour.setEnabled(state);

        colourButton.setEnabled(state);

        mediaCheck.setEnabled(state);

        mediaPath.setEnabled(state);

        mediaButton.setEnabled(state);

        matchOnlyCheck.setEnabled(state);

    }

    /** convert standard colour names to hex codes */
    private String nameToCode(final String oldName) {

        // String retName = "#ffffff";
        String retName;

        if (colourTable.containsKey(oldName.toLowerCase())) {
            retName = colourTable.get(oldName.toLowerCase()).toString();
        } else {
            retName = "#ffffff";
        }

        return retName;

    }

    /** Do a look-up from a rule to it's language translation */
    private String typeToTrans(final String rule) {

        // String retTrans = "";
        String retTrans;

        final String lowerRule = rule.toLowerCase();

        if (null != lowerRule) switch (lowerRule) {
            case ANDSTR:
                retTrans = java.util.ResourceBundle.getBundle(TRIGGERBUNDLE).getString(Trigger.AND);
                break;
            case MODSTR:
                retTrans = java.util.ResourceBundle.getBundle(TRIGGERBUNDLE).getString(Trigger.MODIFIER);
                break;
            default:
                retTrans = java.util.ResourceBundle.getBundle(TRIGGERBUNDLE).getString(Trigger.NOT);
                break;
        } else {
            retTrans = java.util.ResourceBundle.getBundle(TRIGGERBUNDLE).getString(Trigger.NOT);
        }

        return retTrans;

    }

    /** Do a look-up from a language translation to its rule */
    private String transToType(final String trans, final boolean upper) {

        // String retType = "";
        String retType;

        // String lowerTrans = "";
        String lowerTrans;

        final int cpos = trans.indexOf(':');

        // Remove any markings if this is still part of our rule
        if (cpos > -1) {
            lowerTrans = trans.substring(0, cpos).toLowerCase();
        } else {
            lowerTrans = trans.toLowerCase();
        }

//        if (lowerTrans.equals(andTrans) || lowerTrans.equals(ANDSTR)) {
//            retType = ANDSTR;
//        }
//
//        if (lowerTrans.equals(notTrans) || lowerTrans.equals(NOTSTR)) {
//            retType = NOTSTR;
//        }
//
//        if (lowerTrans.equals(modTrans) || lowerTrans.equals(MODSTR)) {
//            retType = MODSTR;
//        }

        // attempted to simplify the above and at least have retType be one of the above
        if (lowerTrans.equals(andTrans) || lowerTrans.equals(ANDSTR)) {
            retType = ANDSTR;
        } else if (lowerTrans.equals(notTrans) || lowerTrans.equals(NOTSTR)) {
            retType = NOTSTR;
        } else {
            retType = MODSTR;
        }

        if (upper) {
            retType = retType.toUpperCase();
        }

        return retType;

    }

    /**
     * Indicates whether the rules have been modified and the user wishes to use them
     * @return <code>true</code> - The rules have been modified and accepted by the user
     * <code>false</code> - The rules have not been modified or not accepted by the user
     */
    public boolean isChanged() {
        return approvedRules;
    }

    /**
     * Returns a vector of the rules based on whether they have been approved or not
     * @return
     */
    public java.util.Vector getChangedRules() {
        java.util.Vector retVec;

        if (approvedRules) {
            if (DEBUG) {
                System.err.println("TriggerGUI.getChangedRules using mainRules vector.");
            }
            retVec = mainRules;
        } else {
            if (DEBUG) {
                System.err.println("TriggerGUI.getChangedRules using originalRules vector.");
            }
            retVec = originalRules;
        }

        if (DEBUG) {
            System.err.println("TriggerGUI.getChangedRules retVec has size of " + retVec.size());
        }

        return retVec;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private transient java.awt.Button colourButton;
    private transient java.awt.TextField customColour;
    private transient java.awt.Checkbox customColourCheck;
    private transient java.awt.Checkbox gagCheck;
    private transient java.awt.Checkbox matchOnlyCheck;
    private transient java.awt.Button mediaButton;
    private transient java.awt.Checkbox mediaCheck;
    private transient java.awt.TextField mediaPath;
    private transient java.awt.List ruleList;
    private transient java.awt.Choice ruleNameChoice;
    private transient java.awt.Choice ruleOption;
    private transient java.awt.TextField ruleSetName;
    private transient java.awt.TextField ruleWord;
    private transient java.awt.Checkbox triggerCheck;
    private transient java.awt.TextField triggerTF;
    // End of variables declaration//GEN-END:variables
    /** The vector holding the main "working" rules */
    private transient java.util.Vector mainRules; // = new java.util.Vector(0, 1);
    /** These are the original rules which will be reverted to if the user cancels changes */
    private transient java.util.Vector originalRules; // = new java.util.Vector(0, 1);
    private transient boolean editing = false;
    private transient String theRules;
    /** Enables and disables debugging output */
    private static final boolean DEBUG = false;
    /** The string used by the system for the type "and" */
    private static final String ANDSTR = "and";
    /** The string used by the system for the type "not" */
    private static final String NOTSTR = "not";
    /** The string used by the system for the type "modifier" */
    private static final String MODSTR = "modifier";
    /** The human readable translation of the word "And" */
    private transient final String andTrans;
    /** The human readable translation of the word "Not" */
    private transient final String notTrans;
    /** The human readable translation of the word "Mod". */
    private transient final String modTrans;
    /** The bundle that contains text translations for the triggers */
    private static final String TRIGGERBUNDLE = "anecho/JamochaMUD/plugins/TriggerDir/TriggerBundle";
    /** The singleton for this class */
    private static TriggerGUI _instance;  // We'll try playing with a Singleton!
    private transient final Hashtable colourTable;  // a quick look-up table for colour names to codes
    /** True if rules have been accepted, false if the dialogue has been canceled */
    private transient boolean approvedRules = false;
}

