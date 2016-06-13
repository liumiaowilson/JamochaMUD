/*
 * Trigger.java
 *
 * Created on April 5, 2004, 6:22 PM
 * $Id: Trigger.java,v 1.30 2014/07/01 22:20:30 jeffnik Exp $
 */

/* JamochaMUD, a Muck/Mud client program
 * Copyright (C) 1998-2011  Jeff Robinson
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
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
package anecho.JamochaMUD.plugins;

import anecho.JamochaMUD.MuSocket;
import net.sf.wraplog.AbstractLogger;
import net.sf.wraplog.NoneLogger;
import net.sf.wraplog.SystemLogger;

/**
 * This plug-in allows users to set highlights and gags based on incoming text
 * from the connect MU*s.
 *
 * @author Jeff Robinson
 */
public class Trigger implements anecho.JamochaMUD.plugins.PlugInterface {

    /**
     * Creates a new instance of Trigger This method is here for the sake of
     * "proper" code
     */
    public Trigger() {

        if (DEBUG) {
            logger = new SystemLogger();
        } else {
            logger = new NoneLogger();
        }
    }

    @Override
    public void activate() {

        active = true;

    }

    /**
     * The method deactivates the plug-in
     */
    @Override
    public void deactivate() {

        active = false;

    }

    /**
     *
     *
     *
     * @return
     *
     */
    @Override
    public boolean isActive() {

        return active;

    }

    /**
     * This tells the main program whether the plug-in has user-accessible
     * properties.
     *
     * @return True indicates properties are available, false indicates none
     *
     */
    @Override
    public boolean hasProperties() {

        return true;

    }

    /**
     * This method initialises any variables needed by the plug-in audStream
     * soon audStream it is loaded.
     */
    @Override
    public void initialiseAtLoad() {

        andStr = java.util.ResourceBundle.getBundle(MAINBUNDLESTR).getString("and");
        andTrans = java.util.ResourceBundle.getBundle(BUNDLESTR).getString("AND");
        andTrans = andTrans.toLowerCase();

        notStr = java.util.ResourceBundle.getBundle(MAINBUNDLESTR).getString("not");
        notTrans = java.util.ResourceBundle.getBundle(BUNDLESTR).getString("NOT");
        notTrans = notTrans.toLowerCase();

        modStr = java.util.ResourceBundle.getBundle(MAINBUNDLESTR).getString("modifier");
        modTrans = java.util.ResourceBundle.getBundle(BUNDLESTR).getString("MODIFIER");
        modTrans = modTrans.toLowerCase();

        final anecho.JamochaMUD.JMConfig settings = anecho.JamochaMUD.JMConfig.getInstance();

        final String plugIns = settings.getJMString(anecho.JamochaMUD.JMConfig.USERPLUGINDIR);

        triggerDir = plugIns + pathSeparator + java.util.ResourceBundle.getBundle(MAINBUNDLESTR).getString("TriggerDir");

        // triggerFile = new java.io.File(triggerDir + pathSeparator + java.util.ResourceBundle.getBundle(MAINBUNDLESTR).getString(".trigger.rc"));
        initTriggerFile();

        readTriggers();

    }

    /**
     * This method returns a String with a brief description of the plug-in's
     * function.
     *
     * @return The description of the plug-in.
     *
     */
    @Override
    public String plugInDescription() {
        return java.util.ResourceBundle.getBundle(BUNDLESTR).getString("description");
    }

    /**
     * This returns the name of the plug-in for display to the user.
     *
     * @return The human-readable name of the plug-in
     */
    @Override
    public String plugInName() {
        return java.util.ResourceBundle.getBundle(BUNDLESTR).getString("Gags_and_Highlights");
    }

    /**
     * Displays properties for the plug-in that users can adjust.
     */
    @Override
    public void plugInProperties() {

        // Create our dialogues and slip them the new rules
        if (DEBUG) {
            System.err.println("Trigger.plugInProperties using rules size " + mainRules.size());
        }

        // This is not implemented yet, but should be soon.  Fix Me XXX
        // boolean swingVersion = anecho.JamochaMUD.JMConfig.getInstance().getJMboolean(anecho.JamochaMUD.JMConfig.USESWING);
        boolean swingVersion = false;

        if (swingVersion) {
            // if (anecho.JamochaMUD.JMConfig.getInstance().getJMboolean(anecho.JamochaMUD.JMConfig.USESWING)) {
            // Display the Swing version of the plug-in dialogue
            final anecho.JamochaMUD.plugins.TriggerDir.TriggerSwingGUI plugGUI = new anecho.JamochaMUD.plugins.TriggerDir.TriggerSwingGUI(anecho.JamochaMUD.JMConfig.getInstance().getJMFrame(anecho.JamochaMUD.JMConfig.MAINWINDOWVARIABLE), true);

            // We'll use the setOldRules method for now, but eventually we want all rules to use the Rules class
            plugGUI.setOldRules(mainRules);
            plugGUI.setVisible(true);

            if (plugGUI.isUpdateRules()) {
                mainRules = plugGUI.getChangedRules();
            }

            writeRulesToFile(mainRules);

            plugGUI.dispose();
        } else {
            // Display the AWT version of the plug-in dialogue
            final anecho.JamochaMUD.plugins.TriggerDir.TriggerGUI plugGUI = new anecho.JamochaMUD.plugins.TriggerDir.TriggerGUI(mainRules);
            plugGUI.setVisible(true);

            if (plugGUI.isChanged()) {
                // We have modified the existing rules, so we'll load the changes
                mainRules = plugGUI.getChangedRules();
                if (DEBUG) {
                    System.err.println("Trigger.plugInProperties(): mainRules is now " + mainRules.size() + " rules.");

                    for (int i = 0; i < mainRules.size(); i++) {
                        System.err.println("Rule " + i + ": " + mainRules.elementAt(i));
                    }
                }

                // write out the rules
                // writeRulesToFile(mainRules);
            }

            plugGUI.dispose();
        }
    }

    /**
     * Type of plug-in: input, output, or other.
     *
     * @return
     *
     */
    @Override
    public String plugInType() {

        // return java.util.ResourceBundle.getBundle(MAINBUNDLESTR).getString("Output");
        return anecho.JamochaMUD.EnumPlugIns.OUTPUT;

    }

    /**
     *
     * The main method of the plug-in.
     *
     * @param jamochaString The incoming String
     * @param mSock This is the socket this String belongs to
     * @return The new string after being processed for triggers, etc.
     *
     */
    @Override
    public String plugMain(final String jamochaString, final anecho.JamochaMUD.MuSocket mSock) {

        final String finalString = parseGags(jamochaString, mSock);

        return finalString;

    }

    /**
     * This is called when JamochaMUD stops, and can be used for any final
     * clean-up, file saving, etc.
     *
     */
    public void setAtHalt() {
        // Write out the rules to file
        writeRulesToFile(mainRules);

    }

    /**
     * Returns whether there is a (GUI) configuration available for the plug-in.
     *
     * @return <code>true</code> This plug-in has a configuration GUI
     *
     */
    @Override
    public boolean haveConfig() {

        return true;

    }

    /**
     *
     * @param settings
     *
     */
    public void setSettings(final anecho.JamochaMUD.JMConfig settings) {
//        
//        // This method has been deprecated and settings should be moved elsewhere.
//        // Fix Me XXX
//     
//        // this.settings = settings;
//        if (settings == null) {
//            settings = anecho.JamochaMUD.JMConfig.getInstance();
//        }
//        
//        plugIns = settings.getJMString(anecho.JamochaMUD.JMConfig.USERPLUGINDIR);
//        
//        triggerDir = plugIns + pathSeparator + java.util.ResourceBundle.getBundle(MAINBUNDLESTR).getString("TriggerDir");
//        
//        // triggerFile = new java.io.File(triggerDir + pathSeparator + java.util.ResourceBundle.getBundle(MAINBUNDLESTR).getString(".trigger.rc"));
//        initTriggerFile();
    }

    /**
     * This method initialises the triggerFile variable
     */
    private void initTriggerFile() {
        if (triggerFile == null) {
            triggerFile = new java.io.File(triggerDir + pathSeparator + ".trigger.rc");
        }
    }

    /**
     * Go through the rules and see if have anything that matches
     */
    private String parseGags(final String input, final MuSocket mSock) {
        String retString;

        if (mainRules == null || mainRules.size() < 1) {
            // We have no rules yet
            retString = input;
        } else {

//            StringBuffer grinder = new StringBuffer(input);
//
//            // Loop through all the rules...
//            int numRules = 0;
//            boolean gag, mod;
//            int ruleResult = NO_MATCH;
//
//            final int mRuleSize = mainRules.size();
//
//            // for (int set = 0; set < mainRules.size(); set++) {
//            for (int set = 0; set < mRuleSize; set++) {
//                gag = false; // Reset this for each rule, otherwise we can have "fall through"
//                mod = false; // Check if any rule "modifiers" have been successful
//
//                // How many rules are in this rule set?
//                numRules = Integer.parseInt(pullRule(mainRules.elementAt(set), RULES));
//
//                for (int j = 0; j < numRules; j++) {
//                    ruleResult = checkRule(input, set, j);
//
//                    if (ruleResult == TRUE_MATCH) {
//                        gag = true;
//                    }
//
//                    if (ruleResult == FALSE_MATCH) {
//                        gag = false;
//                        break;
//                    }
//
//                    if (ruleResult == MODIFIER_MATCH) {
//                        mod = true;
//                    }
//
//                }
//
//                // We'll see if this gag is appropriate...
//                if (gag) {
//                    grinder = new StringBuffer(applyGag(grinder, set));
//
//                    /* The colour needs some additional modifications */
//                    if (mod) {
//                        if (DEBUG) {
//                            System.err.println(java.util.ResourceBundle.getBundle(MAINBUNDLESTR).getString("Trigger_adding_mod_colouring."));
//                        }
//                        grinder.insert(0, "\u001b[3m");
//                        grinder.append("\u001b[0m");
//                    }
//
//                    // If audio is applicable, we'll call that audStream well!
//                    final String rule = pullRule(mainRules.elementAt(set), MEDIA);
//
//                    // if (!rule.equals(java.util.ResourceBundle.getBundle(MAINBUNDLESTR).getString(NONE))) {
//                    if (!java.util.ResourceBundle.getBundle(MAINBUNDLESTR).getString(NONE).equals(rule)) {
//                        // playMedia(pullRule(mainRules.elementAt(set), MEDIA));
//                        playMedia(rule);
//                    }
//
//                    // Check to see if there is a trigger as well.
//                    // final String trigRule = pullRule(mainRules.elementAt(set), TRIGGERSTR);
//                    final String trigRule = pullRule(mainRules.elementAt(set), TRIGGERSTR);
//
//                    // if (!trigRule.equals(java.util.ResourceBundle.getBundle(MAINBUNDLESTR).getString(NONE))) {
//                    if (!java.util.ResourceBundle.getBundle(MAINBUNDLESTR).getString(NONE).equals(trigRule)) {
//                        if (DEBUG) {
//                            System.err.println("Trigger.parseGags() reads TRIGGER as ***" + trigRule + "***");
//                        }
//                        sendCommand(trigRule, mSock);
//                    }
//                }
//
//            }
//
//            retString = grinder.toString();
            retString = grinderMethod(input, mSock);
        }

        if (DEBUG) {
            System.err.println("Trigger.parseGags() Completed trigger: " + retString);
        }

        return retString;

    }

    /**
     * This method returns the given string with any gags/highlights applied to
     * it.
     *
     * @param input
     * @param mSock
     * @return
     */
    private String grinderMethod(final String input, final MuSocket mSock) {
        if (DEBUG) {
            System.err.println("Trigger.grinderMethod: Entering grinder method.");
        }

        String retString = input;
        // StringBuffer grinder = new StringBuffer(input);

        // Loop through all the rules...
        int numRules = 0;
        boolean gag, mod;
        int ruleResult = NO_MATCH;

        final int mRuleSize = mainRules.size();

        // for (int set = 0; set < mainRules.size(); set++) {
        for (int set = 0; set < mRuleSize; set++) {
            gag = false; // Reset this for each rule, otherwise we can have "fall through"
            mod = false; // Check if any rule "modifiers" have been successful

            // How many rules are in this rule set?
            numRules = Integer.parseInt(pullRule(mainRules.elementAt(set), RULES));

            for (int j = 0; j < numRules; j++) {
                ruleResult = checkRule(input, set, j);

                if (ruleResult == TRUE_MATCH) {
                    gag = true;
                }

                if (ruleResult == FALSE_MATCH) {
                    gag = false;
                    break;
                }

                if (ruleResult == MODIFIER_MATCH) {
                    mod = true;
                }

            }

            // We'll see if this gag is appropriate...
            if (gag) {
                retString = applyRuleToInput(mSock, input, mod, set);
//                if (DEBUG) {
//                    System.err.println("Trigger.grinderMethod: entering 'true' gag");
//                }
//
//                grinder = new StringBuffer(applyGag(grinder, set));
//
//                /* The colour needs some additional modifications */
//                if (mod) {
//                    if (DEBUG) {
//                        System.err.println(java.util.ResourceBundle.getBundle(MAINBUNDLESTR).getString("Trigger_adding_mod_colouring."));
//                    }
//                    grinder.insert(0, "\u001b[3m");
//                    grinder.append("\u001b[0m");
//                }
//
//                // If audio is applicable, we'll call that audStream well!
//                final String rule = pullRule(mainRules.elementAt(set), MEDIA);
//
//                if (!java.util.ResourceBundle.getBundle(MAINBUNDLESTR).getString(NONE).equals(rule)) {
//                    playMedia(rule);
//                }
//
//                // Check to see if there is a trigger as well.
//                final String trigRule = pullRule(mainRules.elementAt(set), TRIGGERSTR);
//
//                if (DEBUG) {
//                    System.err.println("Trigger: trigRule: " + trigRule);
//                }
//
//                if (!java.util.ResourceBundle.getBundle(MAINBUNDLESTR).getString(NONE).equals(trigRule)) {
//                    if (DEBUG) {
//                        System.err.println("Trigger.parseGags() reads TRIGGER as ***" + trigRule + "***");
//                    }
//                    sendCommand(trigRule, mSock);
//                }
            }
//            else {
//                retString = input;
//            }

        }

        // retString = grinder.toString();
        return retString;
    }

    /**
     * This method applies the given rule to the provided input
     *
     * @param workLine The input to apply the rule to
     * @param mod Represents whether this is a "modification" rule
     * @param set The rule-set to apply to this input
     * @return A String of the input with appropriate gags, highlights, etc.
     * applied
     */
    private String applyRuleToInput(final MuSocket mSock, final String workLine, final boolean mod, final int set) {
        StringBuffer grinder = new StringBuffer(workLine);

        if (DEBUG) {
            System.err.println("Trigger.something entering.");
        }

        grinder = new StringBuffer(applyGag(grinder, set));

        /* The colour needs some additional modifications */
        if (mod) {
            if (DEBUG) {
                System.err.println(java.util.ResourceBundle.getBundle(MAINBUNDLESTR).getString("Trigger_adding_mod_colouring."));
            }
            grinder.insert(0, "\u001b[3m");
            grinder.append("\u001b[0m");
        }

        // If audio is applicable, we'll call that audStream well!
        final String rule = pullRule(mainRules.elementAt(set), MEDIA);

        // if (!rule.equals(java.util.ResourceBundle.getBundle(MAINBUNDLESTR).getString(NONE))) {
        if (!java.util.ResourceBundle.getBundle(MAINBUNDLESTR).getString(NONE).equals(rule)) {
            // playMedia(pullRule(mainRules.elementAt(set), MEDIA));
            playMedia(rule);
        }

        // Check to see if there is a trigger as well.
        // final String trigRule = pullRule(mainRules.elementAt(set), TRIGGERSTR);
        final String trigRule = pullRule(mainRules.elementAt(set), TRIGGERSTR);

        if (DEBUG) {
            System.err.println("Trigger: trigRule: " + trigRule);
        }

        // if (!trigRule.equals(java.util.ResourceBundle.getBundle(MAINBUNDLESTR).getString(NONE))) {
        if (!java.util.ResourceBundle.getBundle(MAINBUNDLESTR).getString(NONE).equals(trigRule)) {
            if (DEBUG) {
                System.err.println("Trigger.parseGags() reads TRIGGER as ***" + trigRule + "***");
            }
            sendCommand(trigRule, mSock);
        }

        return grinder.toString();
    }

    /**
     * Search through the string for the rule name, and extract its information
     */
    private String pullRule(final Object ruleObj, final String ruleName) {

        // Set-up the return value as NONE, in the event that we are missing
        // a property (which can happen if new properties are added to JamochaMUD
        // but an old trigger file is used).
        String retVal = NONE;

        final String rule = ruleObj.toString();

        int start, end;

        start = rule.indexOf(ruleName);

        if (start > -1) {
            // if "start" is -1 then the rule doesn't exist.  We want to keep this value
            // so that we can "fall through".
            start = start + ruleName.length();
        }

        // end = rule.indexOf(java.util.ResourceBundle.getBundle(MAINBUNDLESTR).getString("$"), start + 1);
        end = rule.indexOf('$', start + 1);

        if (DEBUG) {
            System.err.println("------------------------");
            System.err.println("Trigger.pullRule() ruleName: " + ruleName);
            System.err.println("Trigger.pullRule() rule: " + rule);
            System.err.println("Trigger.pullRule() start: " + start + " end: " + end);
            System.err.println("------------------------");
        }

        // if (end > 0) {
        if (start > -1) {
            if (DEBUG) {
                System.err.println("Trigger.pullRule() rule ->" + ruleName + "<- ->" + rule.substring(start, end) + "<-");
            }
            retVal = rule.substring(start, end);
        } else {
            if (DEBUG) {
                System.err.println("Trigger.pullRule() rule " + ruleName + " does not exist.  Returning 'None'");
            }

        }

        // return rule.substring(start, end);
        return retVal;

    }

    /**
     * Check the passed string to see if it matches one of our rules
     *
     * @param input The input string that the rule will be checked against
     * @param set The rule-set to check. A rule-set is a complete rule.
     * @param rule The specific rule within a set to check against. Usually a
     * condition.
     */
    private int checkRule(final String inString, final int set, final int rule) {

        final String input = inString.toLowerCase();
        final String chkRule = pullRule(mainRules.elementAt(set), '$' + Integer.toString(rule) + ':');
        final String matchType = transToType(chkRule, false);
        final String match = (chkRule.substring(chkRule.indexOf(':') + 1)).toLowerCase();
        int state = NO_MATCH;

//        if (DEBUG) {
//            System.err.println("Trigger.checkRule() Match: ->" + match + "<--");
//            System.err.println("Trigger.checkRule() matchType: ->" + matchType + "<--");
//            System.err.println("Trigger.checkRule() andStr : -->" + andStr + "<--");
//            System.err.println("Trigger.checkRule() Input -->" + input + "<--");
//        }
        logger.debug("Trigger.checkRule() Match: ->" + match + "<--");
        logger.debug("Trigger.checkRule() matchType: ->" + matchType + "<--");
        logger.debug("Trigger.checkRule() andStr : -->" + andStr + "<--");
        logger.debug("Trigger.checkRule() Input -->" + input + "<--");

        if (matchType.startsWith(andStr)) {
            if (input.contains(match)) {

                state = TRUE_MATCH;
//                if (DEBUG) {
//                    System.err.print("Trigger.checkRule() checkRule state: " + true);
//                }
                logger.debug("Trigger.checkRule() checkRule state: " + true);
            } else {
                // We didn't meet an "AND" condition, so this trigger fails
                state = FALSE_MATCH;
            }
        }

        if (matchType.startsWith(notStr) && (input.contains(match))) {
            state = FALSE_MATCH;
        }

        if (matchType.startsWith(modStr) && (input.contains(match))) {
            state = MODIFIER_MATCH;
        }

//        if (DEBUG) {
//            System.err.println("Trigger.checkRule() return state: " + state);
//        }
        logger.debug("Trigger.checkRule() return state: " + state);

        return state;

    }

    /**
     * Apply the Gag to the String we were passed from the main program
     */
    private String applyGag(StringBuffer sBuffer, final int set) {

        if (DEBUG) {
            System.err.println("Applying gag.");
        }

        String colour = pullRule(mainRules.elementAt(set), COLOUR);

        colour = colour.toLowerCase();

        String colourCode = "";

        // if (colour.equals(java.util.ResourceBundle.getBundle(BUNDLESTR).getString("gag"))) {
        if (java.util.ResourceBundle.getBundle(BUNDLESTR).getString("gag").equals(colour)) {
            return "";	// Not a colour, but a gag!
        }

        // Make the colour our colour name by default, change if necessary
        colourCode = nameToCode(colour);

        if (DEBUG) {
            System.err.println("Trigger.applyGag colourCode -->" + colourCode + "<--");
        }

        if (sBuffer.length() > 0 && sBuffer.charAt(0) != ESCAPE) {

            final String match = pullRule(mainRules.elementAt(set), MATCHONLY).toLowerCase();

            if (match.equals(java.util.ResourceBundle.getBundle(MAINBUNDLESTR).getString("true"))) {
                if (DEBUG) {
                    System.err.println("Trigger.ApplyGag(): Going to exact match text");
                }

                // Return a new StringBuffer with only the direct matches marked
                sBuffer = markMatchOnly(set, sBuffer, colourCode);

            } else {

                sBuffer.insert(0, colourCode);

                sBuffer.append(ESCAPE);
                sBuffer.append("[0m");

                if (DEBUG) {
                    System.err.println(java.util.ResourceBundle.getBundle(MAINBUNDLESTR).getString("Trigger.ApplyGag()_applying_highlight_to_entire_line"));
                }

            }
        }

        return sBuffer.toString();

    }

    /**
     * This method marks up a String buffer to match only specific portions
     *
     * @param set
     * @param sBuffer
     * @param colourCode
     * @return
     */
    private StringBuffer markMatchOnly(final int set, final StringBuffer sBuffer, final String colourCode) {
        int start = 0;
        int end = sBuffer.length();

        // Determine the new start and beginning
        final int numRules = Integer.parseInt(pullRule(mainRules.elementAt(set), RULES));
        int ruleResult;
        String input;
        String matchTerm;

        for (int j = 0; j < numRules; j++) {
            input = sBuffer.toString();
            ruleResult = checkRule(input, set, j);
            // matchTerm = this.pullRule(mainRules.elementAt(set), java.util.ResourceBundle.getBundle(MAINBUNDLESTR).getString("$") + j + java.util.ResourceBundle.getBundle(MAINBUNDLESTR).getString(":"));
            // matchTerm = this.pullRule(mainRules.elementAt(set), '$' + String.valueOf(j) + ':');
            matchTerm = this.pullRule(mainRules.elementAt(set), '$' + Integer.toString(j) + ':');
            matchTerm = matchTerm.substring(matchTerm.indexOf(':') + 1);

            if (DEBUG) {
                System.err.println(java.util.ResourceBundle.getBundle(MAINBUNDLESTR).getString("Trigger.ApplyGag()_checking_for:_") + matchTerm);
                System.err.println(java.util.ResourceBundle.getBundle(MAINBUNDLESTR).getString("Our_ruleRest_returned:_") + ruleResult);
            }

            if (ruleResult == TRUE_MATCH) {
                start = input.indexOf(matchTerm);
                end = start + matchTerm.length();

                if (start < 0) {
                    // We may have gotten here because of a case-sensitive issue
                    // This is an ugly fix.  Fix Me XXX
                    final String tempIn = input.toLowerCase();
                    final String tempMatch = matchTerm.toLowerCase();
                    start = tempIn.indexOf(tempMatch);
                    end = start + matchTerm.length();
                }

                if (DEBUG) {
                    System.err.println("Trigger.applyGAG -> TRUE_MATCH");
                    System.err.println("Trigger.applyGAG MatchTerm: " + matchTerm);
                    System.err.println("Trigger.applyGAG buffer: " + input);
                    System.err.println("Trigger.applyGAG start: " + start);
                    System.err.println("Trigger.applyGAG end:" + end);
                    System.err.println("Trigger.applyGAG length: " + sBuffer.length());
                }
                // We insert the "End" first so that the beginning doesn't
                // change our index number!!
                sBuffer.insert(end, ESCAPE + "[0m");
                sBuffer.insert(start, colourCode);

                if (DEBUG) {
                    System.err.println(java.util.ResourceBundle.getBundle(MAINBUNDLESTR).getString("Trigger.ApplyGag()_apply_matchTerm_") + matchTerm + java.util.ResourceBundle.getBundle(MAINBUNDLESTR).getString("_to_start:_") + start + java.util.ResourceBundle.getBundle(MAINBUNDLESTR).getString("_and_end:_") + end);
                }

            }

        }

        return sBuffer;

    }

    /**
     * Play media is triggered, so let's make some noise!
     */
    private void playMedia(final String audioFileName) {

        // This stuff is drawn from the Applet.AudioClip class.  Yuckyweird.
        // try {
        // AudioClip myClip = getAudioClip(new URL(getCodeBase(), "clip.au"));
        // } catch (Exception e) {
        // }
        try {

            sun.audio.AudioDataStream audioDataStream;

            final sun.audio.AudioPlayer audioPlayer = sun.audio.AudioPlayer.player;

            final java.io.FileInputStream fis = new java.io.FileInputStream(new java.io.File(audioFileName));

            final sun.audio.AudioStream audStream = new sun.audio.AudioStream(fis); // header plus audio data

            final sun.audio.AudioData audData = audStream.getData(); // audio data only, no header

            audioDataStream = new sun.audio.AudioDataStream(audData);

            audioPlayer.start(audioDataStream);

        } catch (Exception e) {

            System.out.println(java.util.ResourceBundle.getBundle(BUNDLESTR).getString("An_exception_occured_trying_to_play_this_file:"));

            System.out.println(e);

        }

    }

    /**
     * This method sends the given String to the appropriate MuSocket
     *
     * @param triggerStr
     * @param mSock
     */
    private void sendCommand(final String triggerStr, final MuSocket mSock) {
        if (DEBUG) {
            System.err.println("Trigger.sendCommand using string " + triggerStr);
            // System.exit(0);
        }

        // mSock.sendText(triggerStr);
        // anecho.JamochaMUD.JMConfig.getInstance().getDataInVariable().sen
        anecho.JamochaMUD.CHandler.getInstance().sendText(triggerStr, mSock);
        // mSock.write(triggerStr);

        if (DEBUG) {
            System.err.println("Trigger.runTrigger sent " + triggerStr);
        }

    }

    /**
     * Do a look-up from a language translation to its rule
     */
    private String transToType(final String trans, final boolean upper) {

        String retType = "";

        String lowerTrans = "";

        // final int cpos = trans.indexOf(java.util.ResourceBundle.getBundle(MAINBUNDLESTR).getString(":"));
        final int cpos = trans.indexOf(':');

        // Remove any markings if this is still part of our rule
        if (cpos > -1) {
            lowerTrans = trans.substring(0, cpos).toLowerCase();
        } else {
            lowerTrans = trans.toLowerCase();
        }

        // String match = (chkRule.substring(chkRule.indexOf(":") + 1)).toLowerCase();
        if (lowerTrans.equals(andTrans) || lowerTrans.equals(andStr)) {
            retType = andStr;
        }

        if (lowerTrans.equals(notTrans) || lowerTrans.equals(notStr)) {
            retType = notStr;
        }

        if (lowerTrans.equals(modTrans) || lowerTrans.equals(modStr)) {
            retType = modStr;
        }

        if (upper) {
            retType = retType.toUpperCase();
        }

        return retType;

    }

    /**
     * Do an "installation check", and read in triggers if they already exist.
     * If not, create the proper space
     */
    private void readTriggers() {
        // No trigger file, no need to continue
        // We'll open the .trigger.rc file and read in the rulesets
        java.io.RandomAccessFile reader;
        boolean loop = true;

        if (triggerFile == null) {
            initTriggerFile();
        }

//        if (DEBUG) {
//            System.err.println(java.util.ResourceBundle.getBundle(MAINBUNDLESTR).getString("Trying_to_read_in_triggers..."));
//            System.err.println(java.util.ResourceBundle.getBundle(MAINBUNDLESTR).getString("Trigger_File:_") + triggerFile.toString());
//        }
        logger.debug(java.util.ResourceBundle.getBundle(MAINBUNDLESTR).getString("Trying_to_read_in_triggers..."));
        logger.debug(java.util.ResourceBundle.getBundle(MAINBUNDLESTR).getString("Trigger_File:_") + triggerFile.toString());

        try {
            reader = new java.io.RandomAccessFile(triggerFile.toString(), java.util.ResourceBundle.getBundle(MAINBUNDLESTR).getString("r"));

            String line;
            final StringBuffer fullLine = new StringBuffer("");

            while (loop) {
                try {
                    line = reader.readLine();
                } catch (Exception e) {
                    // We're all out of lines
                    break;
                }

                if (line == null || line.trim().equals("")) {
                    loop = false;
                    break;
                }

                line.trim();
                fullLine.append(line);

//                if (DEBUG) {
//                    System.err.println(java.util.ResourceBundle.getBundle(MAINBUNDLESTR).getString("Read_rule:_") + line);
//                }
                logger.debug(java.util.ResourceBundle.getBundle(MAINBUNDLESTR).getString("Read_rule:_") + line);

                // if (line != null && line.indexOf(DONE) > 0) {
                    mainRules.addElement(fullLine.toString());
                    // fullLine = new StringBuffer("");
                    fullLine.setLength(0);
                // }

            }

            reader.close();

        } catch (Exception e) {
            // We can't find our trigger rules.  Chances are they will be created on the first-run
//            if (DEBUG) {
//                System.err.println("Trigger plugin could not access " + triggerFile + ", exception " + e);
//                e.printStackTrace();
//            }
            logger.debug("Trigger plugin could not access " + triggerFile + ", exception " + e);
            // logger.debug(e.printStackTrace());

        }

    }

    /**
     * Upon approval by the user, we'll write the rules back to the file
     *
     * @param newRules
     */
    public synchronized void writeRulesToFile(final java.util.Vector newRules) {

        // Rem'd out 2008-01-12.  Fix Me XXX
        // mainRules = newRules;
        if (triggerFile == null) {
            initTriggerFile();
        }

        if (triggerFile.exists()) {
            triggerFile.delete();
        }

        String writeOut;
        // We'll open the .trigger.rc file and read in the rulesets
        java.io.RandomAccessFile writer;

        try {
            writer = new java.io.RandomAccessFile(triggerFile.toString(), java.util.ResourceBundle.getBundle(MAINBUNDLESTR).getString("rw"));

            // final int mRuleSize = mainRules.size();
            final int mRuleSize = newRules.size();

            for (int i = 0; i < mRuleSize; i++) {
                // writeOut = mainRules.elementAt(i).toString();
                writeOut = newRules.elementAt(i).toString();
                writer.writeBytes(writeOut.trim() + '\n');

//                if (DEBUG) {
//                    System.err.println(java.util.ResourceBundle.getBundle(MAINBUNDLESTR).getString("Trigger.WriteRulesToFile:_") + writeOut.trim());
//                }
                logger.debug(java.util.ResourceBundle.getBundle(MAINBUNDLESTR).getString("Trigger.WriteRulesToFile:_") + writeOut.trim());

            }

            writer.close();

        } catch (Exception e) {
            System.err.println(java.util.ResourceBundle.getBundle(BUNDLESTR).getString("Some_sort've_error_while_writing_out_plugins") + e);
        }

        // Change the existing rules to the new rules
        mainRules = new java.util.Vector(0, 1);
        mainRules = newRules;
    }

    /**
     * This method converts a colour name to the appropriate escape code
     *
     * @param colour The original String of the colour
     * @return A string containing the ASCII colour escape
     */
    private String nameToCode(final String colour) {

        String colourCode = "";

        if (colour.charAt(0) == '#') {
            // colourCode = ESCAPE + java.util.ResourceBundle.getBundle(MAINBUNDLESTR).getString("[") + colour + java.util.ResourceBundle.getBundle(MAINBUNDLESTR).getString("m");
            colourCode = ESCAPE + "[" + colour + "m";
            if (DEBUG) {
                System.err.println("HMTL code.  Fix Me XXX");
            }
            // colourCode = ESCAPE + "[m";
        } else {

            // String[][] colList = new String[7][2];
            final String[][] colList = {
                {"black", "[30m"},
                {"red", "[31m"},
                {"green", "[32m"},
                {"yellow", "[33m"},
                {"blue", "[34m"},
                {"magenta", "[35m"},
                {"cyan", "[36m"},
                {"white", "[37m"}};

            final int listLen = colList.length;

            for (int i = 0; i < listLen; i++) {
                if (colour.equals(java.util.ResourceBundle.getBundle(BUNDLESTR).getString(colList[i][0]))) {
                    // if (colour.equalsIgnoreCase(colList[i][0])) {
                    colourCode = ESCAPE + colList[i][1];
                }
            }

//        if (colour.equals(java.util.ResourceBundle.getBundle(BUNDLESTR).getString("black"))) {
//            colourCode = ESCAPE + java.util.ResourceBundle.getBundle(MAINBUNDLESTR).getString("[30m");
//        }
//
//        if (colour.equals(java.util.ResourceBundle.getBundle(BUNDLESTR).getString("red"))) {
//            colourCode = ESCAPE + java.util.ResourceBundle.getBundle(MAINBUNDLESTR).getString("[31m");
//        }
//
//        if (colour.equals(java.util.ResourceBundle.getBundle(BUNDLESTR).getString("green"))) {
//            colourCode = ESCAPE + java.util.ResourceBundle.getBundle(MAINBUNDLESTR).getString("[32m");
//        }
//
//        if (colour.equals(java.util.ResourceBundle.getBundle(BUNDLESTR).getString("yellow"))) {
//            colourCode = ESCAPE + java.util.ResourceBundle.getBundle(MAINBUNDLESTR).getString("[33m");
//        }
//
//        if (colour.equals(java.util.ResourceBundle.getBundle(BUNDLESTR).getString("blue"))) {
//            colourCode = ESCAPE + java.util.ResourceBundle.getBundle(MAINBUNDLESTR).getString("[34m");
//        }
//
//        if (colour.equals(java.util.ResourceBundle.getBundle(BUNDLESTR).getString("magenta"))) {
//            colourCode = ESCAPE + java.util.ResourceBundle.getBundle(MAINBUNDLESTR).getString("[35m");
//        }
//
//        if (colour.equals(java.util.ResourceBundle.getBundle(BUNDLESTR).getString("cyan"))) {
//            colourCode = ESCAPE + java.util.ResourceBundle.getBundle(MAINBUNDLESTR).getString("[36m");
//        }
//
//        if (colour.equals(java.util.ResourceBundle.getBundle(BUNDLESTR).getString("white"))) {
//            colourCode = ESCAPE + java.util.ResourceBundle.getBundle(MAINBUNDLESTR).getString("[37m");
//        }
        }

        return colourCode;

    }
    /**
     * A Vector containing all the current rules
     */
    transient private java.util.Vector mainRules = new java.util.Vector(0, 1);
    /**
     * The path separator used by this operating system
     */
    transient private final String pathSeparator = java.io.File.separator;
    /**
     * the directory where we keep our trigger rules
     */
    transient private String triggerDir;
    /**
     * the file where we keep our trigger rules
     */
    transient private java.io.File triggerFile;
    public static final String RULES = "$RULES:";
    public static final String RULENAME = "$NAME:";
    public static final String COLOUR = "$COLOUR:";
    public static final String LAUNCH = "$LAUNCH:";
    public static final String MATCHONLY = "$MATCHONLY:";
    public static final String MEDIA = "$MEDIA:";
    public static final String NAME = "$NAME:";
    public static final String TRIGGERSTR = "$TRIGGER:";
    public static final String DONE = "$DONE:";
    public static final String MODIFIER = "MODIFIER";
    public static final String NOT = "NOT";
    public static final String AND = "AND";
    public static final String NONE = "None";
    public static final String GAG = "GAG";
    private static final int NO_MATCH = 0;
    private static final int TRUE_MATCH = 1;
    private static final int FALSE_MATCH = 2;
    private static final int MODIFIER_MATCH = 3;
    /**
     * This variable allows the enabling and disabling of debugging output
     */
    private static final boolean DEBUG = false;
    /**
     *
     */
    private static String andStr, andTrans;
    /**
     *
     */
    private static String notStr, notTrans;
    private static String modStr, modTrans;
    transient private boolean active = false;
    /**
     * The escape character
     */
    private static final char ESCAPE = '\u001b';
    /**
     * The basic JamochaMUD language bundle
     */
    private static final String MAINBUNDLESTR = "anecho/JamochaMUD/JamochaMUDBundle";
    /**
     * The language bundle specific to Gags and Highlights
     */
    private static final String BUNDLESTR = "anecho/JamochaMUD/plugins/TriggerDir/TriggerBundle";
    private final transient AbstractLogger logger;
}
