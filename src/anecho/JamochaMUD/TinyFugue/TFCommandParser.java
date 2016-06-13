/**
 * This section is for TinyFugue emulated &quot;/" commands like TinyFugue uses
 * $Id: TFCommandParser.java,v 1.6 2015/06/11 01:57:53 jeffnik Exp $
 */
package anecho.JamochaMUD.TinyFugue;

import anecho.JamochaMUD.*;
import java.util.StringTokenizer;
import java.util.Vector;
import net.sf.wraplog.AbstractLogger;
import net.sf.wraplog.NoneLogger;
import net.sf.wraplog.SystemLogger;

/**
 * This class processes user input and scans it for TinyFugue style "/
 * commands", or user defined definitions and variables. If any of these are
 * found, the class will process them, otherwise it lets the user input travel
 * through unchanged.
 */
final public class TFCommandParser {

    /**
     * The class that contains all the JamochaMUD user settings
     */
    final private transient JMConfig settings;
    /**
     * A String representing the ANSI escape code.
     */
    // private static final String ESC = java.util.ResourceBundle.getBundle(bundleStr).getString("\u001b");
    private static final String ESC = "\u001b";
    /**
     * A static final variable used to show (<CODE>true</CODE>)or hide
     * (<CODE>false</CODE>) debugging information.
     */
    private static final boolean DEBUG = false;
    /**
     * The singleton instance of this class
     */
    private static TFCommandParser _instance;
    /**
     * A string representation of the "path" the the correct language bundle
     */
    private static final String bundleStr = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString("anecho/JamochaMUD/JamochaMUDBundle");

    private static final String ESC1m = "[1m";
    private final AbstractLogger logger;

    /**
     * The constructor for this class.
     */
    // public TFCommandParser(JMConfig settings) {
    private TFCommandParser() {
        settings = JMConfig.getInstance();
        if (DEBUG) {
            logger = new SystemLogger();
        } else {
            logger = new NoneLogger();
        }

    }

    /**
     *
     * @return
     */
    public static TFCommandParser getInstance() {

        if (_instance == null) {
            _instance = new TFCommandParser();
        }

        return _instance;
    }

    /**
     * The String is received from the input window and processed in this
     * method. This method looks for any item that begins with a forward slash,
     * which then signals that additional processing is required.
     *
     * @param command Input received from the user to be processed
     * @return This boolean indicates whether our TFCommandParser class has has
     * processed the user input. <CODE>true</CODE> - indicates that the input
     * has been modified by this class and will not need to be handled any more.
     * <CODE>false</CODE> - indicates that the input does not have any special
     * definitions, commands, or variables and so should be left as-is and sent
     * off to the MU*.
     */
    public boolean command(final String command) {
        boolean response = false;       // return-code becomes true if we do process a command
        String workString, cmd, tempFlag;
        StringTokenizer tokens;
        final Vector options = new Vector(0, 1);        // contain any flags that may be present on the command
        final Vector args = new Vector(0, 1);

        workString = command.toLowerCase();

        final int space = workString.indexOf(' ');

        if (space < 0) {
            // Single word command
            cmd = workString;
        } else {
            cmd = workString.substring(0, space);
        }

        tokens = new StringTokenizer(command);

        final int total = tokens.countTokens();

        // Parse apart our arguments and options
        for (int i = 0; i < total; i++) {
            tempFlag = tokens.nextToken().trim();
            // if (tempFlag.startsWith("-")) {
            if (tempFlag.charAt(0) == '-') {
                options.addElement(tempFlag);
            } else {
                args.addElement(tempFlag);
            }
        }

        if (cmd.equals(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString("/bind"))) {
            bind(args, options);
            response = true;
        }

        if (cmd.equals(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString("/connect"))) {
            connectToWorld(args, options);
            response = true;
        }

        if (cmd.equals(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString("/dokey"))) {
            doKey(command);
            response = true;
        }

        if (cmd.equals(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString("/localecho"))) {
            localEcho(args);
            response = true;
        }

        if (cmd.equals(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString("/set"))) {
            setVariable(command, args, options);
            response = true;
        }

        if (cmd.equals(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString("/unset"))) {
            unsetVariable(command);
            response = true;
        }

        if (cmd.equals(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString("/def"))) {
            makeDefinition(command, args, options);
            response = true;
        }

        if (cmd.equals(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString("/undef"))) {
            // Undefine
            unsetDefinition(command);
            response = true;
        }

        if (cmd.equals(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString("/sh"))) {
            // call a shell command with the remaining text
            shellCommand(command);
            response = true;
        }

        if (cmd.equals(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString("/puppet"))) {
            puppetWindow(args);
            response = true;
        }

        if (!response) {
            response = userDefinedCommand(command,
                    options,
                    args);
        }

        logger.debug(java.util.ResourceBundle.getBundle(bundleStr).getString("JMTFCommands.Command_response_is:_") + response);

        return response;
    }

    /**
     * This command allows the binding of building commands to key-strokes
     *
     * @param userArgs
     * @param options
     */
    private void bind(final Vector userArgs, final Vector options) {
        // Fix Me XXX
        sendUserMessage(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString("/bind_has_not_yet_been_implemented."));
    }

    /**
     * This class will connect to a new MU* based on the keyboard input.
     * <I>Not yet implimented.</I>
     * Fix Me XXX
     *
     * @param userArgs not used.
     * @param options not used.
     */
    private void connectToWorld(final Vector userArgs, final Vector options) {
        if (userArgs.size() < 1 || options.size() < 1) {
            logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString("JMTFCommands.connectToWorld()_had_") + userArgs.size() + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString("_args_and_") + options.size() + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString("_options.__Returning."));

            return;
        }
        System.out.println(java.util.ResourceBundle.getBundle(bundleStr).getString("Connect_to_") + userArgs.elementAt(1) + java.util.ResourceBundle.getBundle(bundleStr).getString(":") + userArgs.elementAt(2));

        // Fix Me XXX
        sendUserMessage(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString("/connect_has_not_yet_been_implemented.__Please_use_the_Connection_->_Connect_to_a_new_MU*_menu_option."));
    }

    /**
     * This command allows the emulating of keystrokes that will be interpreted
     * by JamochaMUD. For example, ^PGUP will emulate pressing the CTRL key and
     * Page Up key.
     *
     * @param userArgs
     * @param options
     */
    private void doKey(final String command) {
        // Fix Me XXX
        sendUserMessage(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString("/dokey_has_not_yet_been_completely_implemented.\n"));
        final DoKey dk = new DoKey();
        dk.doKey(command);
    }

    /**
     * This method will allow the user to enable and disable local echo via the
     * text input.
     * <I>Not yet implemented.</I>
     *
     * @param userArgs not used.
     */
    private void localEcho(final Vector userArgs) {
        sendUserMessage(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString("This_command_has_not_yet_been_implemented."));
    }

    /**
     * This sets the value of a user defined variable, or displays all the set
     * variables if no additional arguments are given. A set command that gives
     * only the variable muName with no additional arguements returns the value
     * of that specific variable.
     *
     * @param command This String contains the entire command from the user.
     * This String will be parsed to determine if all variables are to be
     * listed, a single variable is to be listed, or the value of a variable is
     * to be set.
     * @param args unused
     * @param options unused
     */
    private void setVariable(final String command, final Vector args, final Vector options) {

        String name, value;
        final int eqSign = command.indexOf('=');

        if (command.length() <= eqSign) {
            sendUserMessage(java.util.ResourceBundle.getBundle(bundleStr).getString("Set_usage"));
            return;
        }

        // if (command.toLowerCase().trim().equals("/set")) {
        if (args.size() == 1) {
            // Display all of our set items
            // if (options.size() > 0) {
            if (options.isEmpty()) {
                displaySettings(settings.getAllVariables(), java.util.ResourceBundle.getBundle(bundleStr).getString("Variables"));
            } else {
                displaySettingsWindow(settings.getAllVariables());

            }
            return;
        }

        if (eqSign < 0 && command.indexOf(' ') > 0) {
            // Show the user the value of the given variable
            name = command.substring(4).trim();
            value = settings.getVariable(name);

            if (value == null) {
//                sendUserMessage(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString("Variable_") + ESC + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString("[1m") + name + ESC + "[0m is not set." + '\n');
//                sendUserMessage(java.util.ResourceBundle.getBundle(bundleStr).getString("Set_usage"));
                sendUserMessage(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString("Variable_") + ESC + '[' + "1m" + name + ESC + "[0m is not set." + '\n');
                sendUserMessage(java.util.ResourceBundle.getBundle(bundleStr).getString("Set_usage"));
            } else {
                sendUserMessage(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString("Variable_") + ESC + '[' + "1m" + name + ESC + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString("[0m_is_set_to_") + ESC + '[' + "1m" + value + ESC + "[0m" + '\n');
            }

            return;
        }

        name = command.substring(4, eqSign).trim();
        value = command.substring(eqSign + 1).trim();

        settings.addVariable(name, value);
        sendUserMessage(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString("Variable_") + ESC + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString("[1m") + name + ESC + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString("[0m_") + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString("is_now_set_to_") + ESC + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString("[1m") + value + ESC + "[0m" + '\n');

    }

    /**
     * This method removes a user-defined variable from JamochaMUD.
     *
     * @param command This string contains the /unset command plus the muName of
     * the variable to be deleted.
     */
    private void unsetVariable(final String command) {
        if (command.length() < 7) {
            sendUserMessage(java.util.ResourceBundle.getBundle(bundleStr).getString("Unset_usage"));
            return;
        }

        final String name = command.substring(6).trim();

        settings.removeVariable(name);
        sendUserMessage(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString("Variable_") + ESC + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString("[1m") + name + ESC + "[0m_has_been_unset." + '\n');

    }

    /**
     * This method is used to set user-defined definitions (which can contain
     * variables).
     *
     * @param command This String contains the command from the user, and will
     * be parsed to remove the /def directive and then set the definition to the
     * given value. eg: <CODE>/def foo Something something</CODE> will set the
     * definition of <I>foo</I> to be <I>Something something</I>.
     * @param args A vector that contains all the arguments beyond the initial
     * /def.
     * @param options This vector contains the definition itself from the user.
     */
    private void makeDefinition(final String command, final Vector args, final Vector options) {
        String name, value;
        int eqSign;

        // Initially we are going to ignore the args
        eqSign = command.indexOf('=');

        if (command.length() <= eqSign) {
            sendUserMessage(java.util.ResourceBundle.getBundle(bundleStr).getString("Define_usage"));
            return;
        }

        if (args.size() == 1) {
            // Display all the definitions
            // if (options.size() > 0) {
            if (options.isEmpty()) {
                displaySettings(settings.getAllDefinitions(), java.util.ResourceBundle.getBundle(bundleStr).getString("Definitions"));
            } else {
                displaySettingsWindow(settings.getAllDefinitions());

            }
            return;
        }

        if (eqSign < 0 && command.indexOf(' ') > 0) {

            name = command.substring(4).trim();
            value = settings.getDefinition(name);

            if (value == null) {
                sendUserMessage(java.util.ResourceBundle.getBundle(bundleStr).getString("Definition_") + ESC + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString("[1m") + name + ESC + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString("[0m_") + java.util.ResourceBundle.getBundle(bundleStr).getString("is_not_set") + '\n');
                sendUserMessage(java.util.ResourceBundle.getBundle(bundleStr).getString("Define_usage"));
            } else {
                sendUserMessage(java.util.ResourceBundle.getBundle(bundleStr).getString("Definition_") + ESC + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString("[1m") + name + ESC + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString("[0m_") + java.util.ResourceBundle.getBundle(bundleStr).getString("is_set_to_") + ESC + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString("[1m") + value + ESC + "[0m." + '\n');
            }

            return;
        }

        name = command.substring(5, eqSign).trim();
        value = command.substring(eqSign + 1);

        settings.addDefinition(name, value);

        sendUserMessage(java.util.ResourceBundle.getBundle(bundleStr).getString("Definition_") + ESC + java.util.ResourceBundle.getBundle(bundleStr).getString("[1m") + name + ESC + java.util.ResourceBundle.getBundle(bundleStr).getString("[0m_") + java.util.ResourceBundle.getBundle(bundleStr).getString("set_to_") + ESC + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString("[1m") + value + ESC + "[0m" + '\n');
    }

    /**
     * This method parses user input to expand any variables or act upon any
     * definitions that are supplied
     *
     * @param command The original input string received from the user.
     * @param options not used
     * @param args not used
     * @return This class returns <CODE>true</CODE> - indicates that the input
     * contained a definition and was processed by this method.
     * <CODE>false</CODE> - this string was not processed, and should be handled
     * without modification.
     */
    private boolean userDefinedCommand(final String command, final Vector options, final Vector args) {

        String name, value, temp;
        boolean expand = true;
        int start, end;

        final CHandler sender = CHandler.getInstance();

        temp = args.elementAt(0).toString();
        name = temp.substring(1);

        value = settings.getDefinition(name);

        if (value == null) {
            return false;
        }

        logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString("Splitting_our_string_into_required_new_lines."));

        // Check for multiple lines in our definition indicated by %;
        final String[] bits = value.split("\\%\\;");
        final int total = bits.length;

        logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString("Sending_out_") + total + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString("_lines_of_text_to_the_server."));

        for (int parseBits = 0; parseBits < total; parseBits++) {
            // Now parse our new value for any arguments that need to be translated
            value = bits[parseBits];
            // Always reset expand to true, or we'll fall through if a line inbetween does
            // not require substitution
            expand = true;

            // We're going after anything that looks like %1 or %{1-something}
            // We do have to work around the %;, which stand for line-breaks
            while (expand) {
                start = value.indexOf('%');

                if (start + 1 >= value.length()) {
                    // This is not a candidate for expansion, it is the last character in the line
                    start = -1;
                    expand = false;
                }

                if (value.charAt(start + 1) == ';') {
                    logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString("TFCommandParser.userDefinedCommand:_The_second_character_is_;_signifying_a_line-break."));

                    start = -1;
                    expand = false;
                }

                if (start > -1 && expand) {
                    String fillToken = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString("");
                    int replace;

                    if (value.charAt(start + 1) == '{') {
                        // Complex replacement
                        end = start + 2;
                        while (end < value.length() && Character.isDigit(value.charAt(end))) {
                            end++;
                        }

                        replace = Integer.parseInt(value.substring(start + 2, end));

                        // Now figure out what our default replacement token will be
                        if (value.charAt(end) == '-') {
                            final int startDash = end + 1;
                            final int endBracket = value.indexOf('}', end);

                            if (endBracket > -1 && endBracket <= value.length()) {

                                fillToken = value.substring(startDash, endBracket);
                                end = endBracket + 1;
                            } else {
                                end++;
                            }
                        } else {
                            replace = args.size() + 1;
                            end = start + 1;
                        }

                    } else {
                        end = start + 1;

                        while (end < value.length() && Character.isDigit(value.charAt(end))) {
                            end++;
                        }

                        // Figure out replacement
                        replace = Integer.parseInt(value.substring(start + 1, end));
                    }

                    String startS, endS, repValue;
                    if (start > 0) {
                        startS = value.substring(0, start);
                    } else {
                        startS = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString("");
                    }
                    if (end < value.length()) {
                        endS = value.substring(end);
                    } else {
                        endS = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString("");
                    }

                    if (replace < args.size()) {
                        repValue = (String) args.elementAt(replace);
                    } else {
                        repValue = fillToken;
                    }

                    value = startS + repValue + endS;

                } else {
                    expand = false;
                }
            }

            // Send the new value via CHandler to the default MU*
            sender.sendText(value);
        }

        return true;

    }

    /**
     * This method removes a user defined definition.
     *
     * @param command The String contains the /undef command in addition to the
     * muName of the definition to be removed from JamochaMUD.
     */
    private void unsetDefinition(final String command) {
        if (command.length() < 7) {
            sendUserMessage(java.util.ResourceBundle.getBundle(bundleStr).getString("Undefine_usage"));
            return;
        }

        final String name = command.substring(6).trim();

        settings.removeDefinition(name);
        // sendUserMessage(java.util.ResourceBundle.getBundle(bundleStr).getString("Definition_") + ESC + java.util.ResourceBundle.getBundle(bundleStr).getString("[1m") + muName + "" + ESC + java.util.ResourceBundle.getBundle(bundleStr).getString("[0m_") + java.util.ResourceBundle.getBundle(bundleStr).getString("has_been_undefined") + java.util.ResourceBundle.getBundle(bundleStr).getString("\n"));
        sendUserMessage(java.util.ResourceBundle.getBundle(bundleStr).getString("Definition_") + ESC + java.util.ResourceBundle.getBundle(bundleStr).getString("[1m") + name + ESC + java.util.ResourceBundle.getBundle(bundleStr).getString("[0m_") + java.util.ResourceBundle.getBundle(bundleStr).getString("has_been_undefined") + '\n');
    }

    /**
     * Writes a message back to the user (usually a confirmation on the active
     * MU* that is showing).
     *
     * @param message This string contains the text to be sent back to the user.
     */
    private void sendUserMessage(final String message) {
        // final CHandler connHandler = settings.getConnectionHandler();
        final CHandler connHandler = CHandler.getInstance();

        if (settings.getJMboolean(JMConfig.USESWING)) {
            anecho.gui.JMSwingText text;
            text = connHandler.getActiveMUDSwingText();
            text.append(message);
        } else {
            anecho.gui.JMText text;
            text = connHandler.getActiveMUDText();
            text.append(message);
        }

    }

    /**
     * Display a new dialogue that lists all of our definitions in the hashtable
     *
     * @param userDefs This hashtable contains all the user definitions, the key
     * being the command and the object being the definition itself.
     */
    private void displaySettingsWindow(final java.util.Hashtable userDefs) {
        String name, value, tempLine;
        int longLine = 0;

        final anecho.gui.SyncFrame defFrame = new anecho.gui.SyncFrame(java.util.ResourceBundle.getBundle(bundleStr).getString("JamochaMUD"));
        final java.awt.TextArea defs = new java.awt.TextArea(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString(""), 20, 20, java.awt.TextArea.SCROLLBARS_VERTICAL_ONLY);
        defs.setEditable(false);
        defFrame.add(defs);

        // Iternate through our hashtable and show all our settings:
        final java.util.Enumeration keys = userDefs.keys();

        while (keys.hasMoreElements()) {
            name = (String) keys.nextElement();
            value = userDefs.get(name).toString();
            tempLine = name + java.util.ResourceBundle.getBundle(bundleStr).getString("_=_") + value + '\n';
            // defs.append(muName + " = " + value + '\n');
            if (tempLine.length() > longLine) {
                longLine = tempLine.length();
            }
            defs.append(tempLine);
        }

        defs.setColumns(longLine);

        defFrame.setCloseState(true);
        defFrame.pack();
        defFrame.setVisible(true);
    }

    /**
     * Write the hashtable out to the current MU
     *
     *
     * @param userDefs This hashtable contains all the user definitions, the key
     * being the command and the object being the definition itself.
     */
    private void displaySettings(final java.util.Hashtable userDefs, final String title) {
        String name, value;

        // Determine is the Hashtable has any keys.  If there are no keys we
        // should give the user a message indicating this.
        if (userDefs.isEmpty()) {
            sendUserMessage(java.util.ResourceBundle.getBundle(bundleStr).getString("JamochaMUD_currently_has_no_") + title + ' ' + java.util.ResourceBundle.getBundle(bundleStr).getString("assigned") + '\n');
        } else {

            final java.util.Enumeration keys = userDefs.keys();

            while (keys.hasMoreElements()) {
                name = (String) keys.nextElement();
                value = userDefs.get(name).toString();
                sendUserMessage(ESC + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString("[1m") + name + ESC + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString("[0m_=_") + ESC + "[1m" + value + ESC + "[0m\n");
            }
        }
    }

    /**
     * Execute a shell command
     */
    private void shellCommand(final String command) {
        logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString("JMTFCommands.shellCommand()_has_been_called.__This_is_not_yet_implemented."));

    }

    /**
     * This command will create a new window for use of a puppet. The puppet
     * will be "connected" to the currently active MU*
     *
     * @param command Name of the puppet
     */
    private void puppetWindow(final Vector args) {

        final String puppetName;
        String puppetCommand = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString("");
        String puppetSendCommand = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString("");

        if (args.size() > 1) {
            puppetName = args.elementAt(1).toString();
        } else {
            // We didn't receive any arguments
            // Show the user the help, as they provided no arguments
            sendUserMessage(ESC + "[1m" + "Puppet window command usage: /puppet PuppetName [ Puppet Command from MU* [ Puppet Send Command ]]" + ESC + "[0m" + '\n');
            sendUserMessage(ESC + "[1m" + "Example: To create a window named \"Fred\" looking for MU* output \"Fred>\" and sending output to the MU* as \"rFred\" would_be:" + ESC + "[0m" + '\n');
            sendUserMessage(ESC + "[1m" + "         /puppet Fred Fred> rFred" + ESC + "[0m" + '\n');
            return;
        }

        if (args.size() > 2) {
            puppetCommand = args.elementAt(2).toString();
        }

        if (args.size() > 3) {
            // Assign the puppet send command
            puppetSendCommand = args.elementAt(3).toString();
        }

        final CHandler chandle = CHandler.getInstance();
        final MuSocket mSock = chandle.getActiveMUHandle();

        // Do not create the puppet window if it already exists
        // Or if this visible connection is already a puppet window
        // Perhaps bring existing window into focus instead.  Fix Me XXX
        if (!mSock.puppetExists(puppetName) && mSock.getCharacterType() == 0) {
            final PuppetSocket pSock = new PuppetSocket();

            final String muName = mSock.getMUName();
            final String address = mSock.getAddress();
            final String wholeName = puppetName + '@' + muName;
            final int port = mSock.getPort();

            pSock.setParentSocket(mSock);
            pSock.setAddress(address);
            pSock.setMUName(wholeName);
            pSock.setPuppetName(puppetName);
            pSock.setPuppetCommand(puppetCommand);
            pSock.setPuppetSendCommand(puppetSendCommand);

            mSock.addPuppet(puppetName, puppetCommand, pSock);

            chandle.addNewMU(wholeName, address, port, pSock);
        } else {
            if (mSock.puppetExists(puppetName)) {
                sendUserMessage(ESC + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString("[1mA_puppet_window_named_") + puppetName + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString("_already_exists.") + ESC + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString("[0m\n"));
            }
            if (mSock.getCharacterType() != 0) {
                sendUserMessage(ESC + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString("[1mA_puppet_window_may_not_have_an_additional_puppet_window.") + ESC + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString("[0m\n"));
            }

            logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString("JMTFCommands_failure_to_create_new_puppet."));
            logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString("Puppet_already_exists:_") + mSock.puppetExists(puppetName));
            logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString("Current_connection_type_should_be_0_") + mSock.getCharacterType());

        }
    }
}
