package anecho.JamochaMUD.plugins.TriggerDir;

import java.util.Vector;
import anecho.JamochaMUD.plugins.Trigger;
import java.util.Hashtable;
import net.sf.wraplog.AbstractLogger;
import net.sf.wraplog.SystemLogger;

/**
 *
 * This class is used to contain rules used by Triggers.
 * A rule is a set of conditions.  Each condition can be relatively
 * simple but when combined with other conditions will define the result
 * of a rule.
 * 
 * There are three descriptions for a rule:
 * Word - The text portion of the rule that JamochaMUD matches against
 * Option - specifying if a rule is type AND/NOT/MODIFIER etc.
 * Condition - A string containing both the Option and Word
 * 
 * A complete rule follows this format when saved to a text file
 * <code>$RULES:1$0:AND:[idle$COLOUR:#ffffff$MATCHONLY:true$LAUNCH:None$MEDIA:None$TRIGGER:None$NAME:Highlight idle messages$DONE:</code>
 * <code>$RULES:1</code> Indicates there is only 1 condition to this rule
 * <code>$0:</code> Indicates the position of the first (0-based numbering) rule
 * <code>AND:</code> the option for rule 0
 * <code>[idle</code> the string to match for rule 0
 * <code>$COLOUR:</code> the colour to use for this rule
 * <code>$MATCHONLY:</code> Highlight with $COLOUR: only the portion of the text that matches this rule
 * <code>$LAUNCH:</code> path of a program to launch based on a correct match
 * <code>$MEDIA:</code> path to a media file to play based on a correct match
 * <code>$TRIGGER:</code> Text to be sent back to the MU* based on a correct match
 * <code>$NAME:</code> The human-readable name of this plug-in
 * <code>$DONE:</code> This marks the end of the given rule
 * 
 * @author jeffnik
 * @version $Id: Rule.java,v 1.11 2012/03/11 03:44:51 jeffnik Exp $
 */
public class Rule {

    /**
     * The name of our rule
     */
    private transient String ruleName;
    /**
     * A vector containing all the conditions of this rule.  Each object in the
     * Vector will be a string array with 2 items... the condition string
     * itself and the condition's AND/OR/MODIFER rule
     */
    private Vector conditions;
    /** Is highlighting enabled */
    private boolean highlight = false;
    /** Is colouring enabled */
    private boolean colour = false;
    /** Is this rule set to match/colour only the portion that matches the rule */
    private boolean matchOnly = false;
    /** Is this rule a gag and not a highlight? */
    private boolean gag = false;
    /** Is media enabled, such as audible feedback */
    private boolean media = false;
    /** Is this a trigger (provides output based on the input), or starts another program */
    private boolean trigger = false;
    /** A string that contains the code representing our chosen colour */
    private transient String colourStr;
    /** A string that contains the path to our media */
    private transient String mediaStr;
    /** A string that contains the path to our trigger */
    private transient String triggerStr;
    /** A string used to contain a launch string (path to a program) */
    private transient String launchStr;
    /** The position in our array for the "option" */
    private static final int OPTION = 0;
    /** The position in our array for the "word" */
    private static final int WORD = 1;
    /** Allows the enabling and disabling out debugging output */
    private static final boolean DEBUG = false;
    /** The bundle that contains text translations for the triggers */
    private static final String TRIGGERBUNDLE = "anecho/JamochaMUD/plugins/TriggerDir/TriggerBundle";
    private AbstractLogger logger;

    /**
     * 
     */
    public Rule() {
        this(null);
    }

    /**
     * 
     * @param name
     */
    public Rule(String name) {

        logger = new SystemLogger();

        ruleName = name;

        conditions = new Vector();
    }

    /**
     * Set the name of this rule
     * @param name human-readable name of this rule
     */
    public void setRuleName(final String name) {
        ruleName = name;
    }

    /**
     * Returns the human-readable name of this rule
     * @return The human-readable name of this rule
     */
    public String getRuleName() {
//        if (DEBUG) {
//            System.err.println("Rule.getRuleName returns: " + ruleName);
//        }

        logger.debug("Rule.getRuleName returns: " + ruleName);

        String retStr = ruleName == null ? Trigger.NONE : ruleName;

        return retStr;
    }

    public String getLaunchString() {

        String retStr = launchStr == null ? Trigger.NONE : launchStr;

        return retStr;
    }

    public void setLaunchString(String launchStr) {
        this.launchStr = launchStr;
    }

    /**
     * This returns the number of conditions in this rule
     * @return an integer representing the number of conditions in this rule
     */
    public int conditionCount() {
        return conditions.size();
    }

    /**
     * Set the rule at the given index.  This will over-write any existing rule
     * @param index The index of the rule to set
     * @param cond 
     */
    public void insertConditionAt(final int index, final String cond) {
        final String[] tempCond = this.splitOptionWord(cond);
        conditions.insertElementAt(tempCond, index);
    }

    /**
     * Get the rule at the given index
     * @param index The index of the rule to return
     * @return A String representing the rule at the selected index
     */
    public String getConditionAt(final int index) {
        final String[] tempCond = (String[]) conditions.elementAt(index);

        return tempCond[WORD];
    }

//    /**
//     * Returns all the current rules as a String array
//     * @return A String array of all the current rules
//     */
//    public String[] getConditions() {
//        int condLen = conditions.size() - 1;
//        String[] retConds = new String[condLen];
//        String[] condStr;
//
//        for (int i = 0; i < condLen; i++) {
//            condStr = (String[]) conditions.elementAt(i);
//            retConds[i] = condStr[WORD];
//            
//            if (DEBUG) {
//                System.err.println("Rule.getConditions["+i+"] " + condStr[OPTION] + " " + condStr[WORD]);
//                System.err.println("Rule.getConditions["+i+"] " + retConds[i]);
//            }
//        }
//        
//        return retConds;
////         return new String[0];
//    }
//    /**
//     * Set the conditions based on one string.  This is generally how rules are
//     * stored, so this method will parse the string apart and build the conditions.
//     * @param conStr A String representing all the conditions
//     * Fix Me XXX
//     */
//    public void setConditions(final String conStr) {
//        // Find out how many conditions
//        // loop through the conditions and call addCondition
//    }
    /**
     * Adds a new rule to the end the existing list of rules
     * @param cond The string representing the new condition
     */
    public void addCondition(final String cond) {
        // Separate our word and option
        final String[] tempCond = this.splitOptionWord(cond);
        if (DEBUG) {
            System.err.println("Rule.addCondition: adding: " + tempCond[OPTION] + " " + tempCond[WORD]);
        }
        conditions.addElement(tempCond);
    }

    /**
     * Add a new rule to the end of the existing list of rules
     * @param option
     * @param word
     */
    public void addCondition(final String option, final String word) {
        final String[] tempCond = {option, word};
        conditions.addElement(tempCond);
    }

    /**
     * Returns the word at the given index
     * @param index
     * @return
     */
    public String getWordAt(final int index) {
        final String[] tempStr = (String[]) conditions.elementAt(index);
        if (DEBUG) {
            System.err.println("Rule.getWordAt returning " + tempStr[WORD]);
        }
        return tempStr[WORD];
    }

    /**
     * Returns the option at the given index
     * @param index
     * @return
     */
    public String getOptionAt(final int index) {
        final String[] tempStr = (String[]) conditions.elementAt(index);

        return tempStr[OPTION];

    }

    public String getTriggerString() {
        String retStr = triggerStr == null ? Trigger.NONE : triggerStr;

        return retStr;
    }

    public void setTriggerString(String triggerStr) {
        this.triggerStr = triggerStr;
    }

    /**
     * 
     * @param state
     */
    public void setHighlight(final boolean state) {
        highlight = state;
    }

    /**
     * 
     * @return
     */
    public boolean isHighlight() {
        return highlight;
    }

    /**
     * 
     * @param state
     */
    public void setColour(final boolean state) {
        colour = state;
    }

    /**
     * 
     * @return
     */
    public boolean isColour() {
        return colour;
    }

    /**
     * 
     * @param code
     */
    public void setColourString(final String code) {
        colourStr = code;
    }

    /**
     * 
     * @return
     */
    public String getColourString() {
        return colourStr;
    }

    /**
     * 
     * @param state
     */
    public void setMatchOnly(final boolean state) {
        matchOnly = state;
    }

    /**
     * 
     * @return
     */
    public boolean isMatchOnly() {
        return matchOnly;
    }

    /**
     * 
     * @param state
     */
    public void setGag(final boolean state) {
        gag = state;
    }

    /**
     * 
     * @return
     */
    public boolean isGag() {
        return gag;
    }

    /**
     * 
     * @param state
     */
    public void setMedia(final boolean state) {
        media = state;
    }

    /**
     * 
     * @return
     */
    public boolean isMedia() {
        return media;
    }

    /**
     * 
     * @param code
     */
    public void setMediaString(final String code) {
        mediaStr = code;
    }

    /**
     * 
     * @return
     */
    public String getMediaString() {

        String retStr = mediaStr == null ? Trigger.NONE : mediaStr;

        return retStr;
    }

    /**
     * 
     * @param state
     */
    public void setTrigger(final boolean state) {
        trigger = state;
    }

    /**
     * 
     * @return
     */
    public boolean isTrigger() {
        return trigger;
    }

    /** If we are given a combination of "option" (AND/NOT/MODIFIER) and "word"
     * this method will split the two and return the "word"
     * @param orig The combination of option and word to be split
     * @return A string representing only the "Word" portion of the original string
     */
    private String getWord(final String orig) {
        final int index = orig.indexOf(':');
        final String retStr = orig.substring(index + 1);

        return retStr;
    }

    /** If we are given a combination of "option" (AND/NOT/MODIFIER) and "word"
     * this method will split the two and return the "option"
     * @param orig The combination of option and word to be split
     * @return A string representing only the "option" portion of the original string
     */
    private String getOption(final String orig) {
        final int index = orig.indexOf(':');
        // final String retStr = orig.substring(0, index);
        final String retStr = index > -1 ? orig.substring(0, index) : "";

        return retStr;

    }

    /**
     * This method splits the word and option returning both parts
     * @param orig The original rule string
     * @return A String array representing the "option" and "word"
     */
    private String[] splitOptionWord(final String orig) {
        final String tempWord = getWord(orig);
        final String tempOption = getOption(orig);

        // Add the new array to our condition vector
        final String[] tempCond = {tempOption, tempWord};
        return tempCond;
    }

    /** convert standard colour names to hex codes */
    private String nameToCode(final String oldName) {

        // String retName = "#ffffff";
        String retName;

        final Hashtable colourTable = new Hashtable(11);
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

        if (colourTable.containsKey(oldName.toLowerCase())) {
            retName = colourTable.get(oldName.toLowerCase()).toString();
        } else {
            retName = "#ffffff";
        }

        return retName;

    }

    /**
     * Remove all conditions from this rule
     */
    public void removeAllConditions() {
        conditions.removeAllElements();
    }

    /**
     * Represent this rule as a string (often used for saving to file)
     * @return A string representation of this rule
     */
    public String convertToOldRule() {
        StringBuffer oldRule = new StringBuffer();
        int rules = this.conditionCount();

        oldRule.append(Trigger.RULES);
        // oldRule.append(rules + 1);
        oldRule.append(rules);

        for (int i = 0; i < rules; i++) {
            // oldRule.append("$" + i + ":" + (pullRule(theRules, "$" + i + ":")));
            oldRule.append('$');
            oldRule.append(i);
            oldRule.append(':');
            oldRule.append(this.getOptionAt(i));
            oldRule.append(':');
            oldRule.append(this.getWordAt(i));
        }

//        oldRule.append('$');
//        oldRule.append(rules);
//        oldRule.append(":AND:blank");

        oldRule.append(Trigger.COLOUR);
        oldRule.append(this.getColourString());
        oldRule.append(Trigger.LAUNCH);
        oldRule.append(this.getLaunchString());
        oldRule.append(Trigger.MEDIA);
        oldRule.append(this.getMediaString());
        oldRule.append(Trigger.NAME);
        oldRule.append(this.getRuleName());
        oldRule.append(Trigger.TRIGGERSTR);
        oldRule.append(this.getTriggerString());
        oldRule.append(Trigger.DONE);

        return oldRule.toString();
    }

    /** This method converts a single string rule to the information stored by the Rule class
     * @param oldRule A rule in the old string format
     */
    public void convertFromOldRule(final String oldRule) {
        // Make certain we empty out our conditions before we start
        conditions.removeAllElements();

        // Much of this method originally comes from TriggerGUI.java
        // First, we'll check to see if this is a GAG,
        // and if so, we'll disable half our options now

        setRuleName(pullRule(oldRule, Trigger.NAME));

//        if (DEBUG) {
//            System.err.println("Rule.convertOldRule rule name: " + getRuleName());
//        }
        logger.debug("Rule.convertOldRule rule name: " + getRuleName());

        if (pullRule(oldRule, Trigger.COLOUR).equalsIgnoreCase(Trigger.GAG)) {
            setHighlight(false);
            setGag(true);
            setColour(false);
        } else {
            // changeOptionStates(true);
            setHighlight(true);
            setGag(false);
            setColour(true);

            // Set colour information
            String cName = pullRule(oldRule, Trigger.COLOUR).toLowerCase();

            if (cName.charAt(0) == '#') {
                // customColour.setText(cName);
                setColourString(cName);
            } else {
                // This is support for "legacy" triggers that
                // used the colour name instead of code
                // customColour.setText(nameToCode(cName));
                setColourString(nameToCode(cName));
            }

            cName = pullRule(oldRule, Trigger.MATCHONLY).toLowerCase();

            if ("true".equals(cName)) {

                // matchOnlyCheck.setState(true);
                this.setMatchOnly(true);

            } else {
                setMatchOnly(false);
                // matchOnlyCheck.setState(false);
            }

        }

        // Fill in the media information
        final String text = pullRule(oldRule, Trigger.MEDIA);

        if (text.equals(Trigger.NONE)) {
            setMatchOnly(false);
        } else {
            setMatchOnly(true);
            setMediaString(text);
        }

        final String triggerText = pullRule(oldRule, Trigger.TRIGGERSTR);

        if (triggerText.equals(Trigger.NONE)) {
            setTrigger(false);
        } else {
            this.setTriggerString(triggerText);
            setTrigger(true);
        }


        setLaunchString(pullRule(oldRule, Trigger.LAUNCH));

        // Set-up the rule conditions
        final int numRules = Integer.parseInt(pullRule(oldRule, Trigger.RULES));

        String tmpStr;
        String cleanType;
        String tag;
        int secondColon;
        String[] tmpArray = new String[2];
        // String[] tmpArray;

        if (DEBUG) {
            System.err.println("Rule.convertOldRule number of conditions: " + numRules);
        }

        for (int i = 0; i < numRules; i++) {

            tmpArray = new String[2];
            tmpStr = pullRule(oldRule, Integer.toString(i));
            secondColon = tmpStr.indexOf(':', 1);

            cleanType = tmpStr.substring(1, secondColon);

            tag = tmpStr.substring(secondColon + 1);


            logger.debug("Rule.convertOldRule rule " + i + ": -> " + tmpStr);
            logger.debug("Rule.convertOldRule cleanType: " + cleanType);
            logger.debug("Rule.convertOldRule tag: " + tag);

            tmpArray[OPTION] = cleanType;
            tmpArray[WORD] = tag;

            conditions.addElement(tmpArray);

        }
    }

    /** Search through the string for the rule name,
     * and extract its information
     * Copied directly from TriggerGUI.java
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
}
