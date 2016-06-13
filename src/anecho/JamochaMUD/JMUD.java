/**
 * JMUD for JamochaMUD is the "main" section for JamochaMUD 
 * $Id: JMUD.java,v 1.34 2015/07/04 03:35:12 jeffnik Exp $
 */

/* JamochaMUD, a Muck/Mud client program
 * Copyright (C) 1998-2015 Jeff Robinson
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

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Rectangle;

import anecho.gui.JSyncFrame;
import anecho.gui.SyncFrame;
import anecho.gui.SyncFrameGroup;
import java.util.Hashtable;
import javax.swing.UnsupportedLookAndFeelException;
import net.sf.wraplog.AbstractLogger;
import net.sf.wraplog.NoneLogger;
import net.sf.wraplog.SystemLogger;

/**
 *
 * @author jeffnik
 */
public class JMUD {

    /**
     * This class will handle all our connections
     */
    public transient CHandler connections;
    /**
     * This class contains all our settings
     */
    public transient JMConfig settings;
    /**
     * Our main text window
     */
    public transient Frame mainFrame;
    /**
     *
     */
    public transient DataIn inputFrame;
    public transient SyncFrameGroup frameGroup; // Our SyncFrame group
    /**
     *
     * This object represents the splash screen (using Swing), which can be
     * accessed by other classes while loading. In this way plug-ins and the
     * like can pass status back to the user This object represents the splash
     * screen (using Swing), which can be accessed by other classes while
     * loading. In this way plug-ins and the /** Enables and disables debugging
     * output
     */
    private static final boolean DEBUG = false;

    private final AbstractLogger logger;
    private transient Object splash;

    /**
     * The main method for JamochaMUD. This is where it all begins!
     *
     *
     *
     * @param args
     */
    public static void main(final String[] args) {
        // Create a new instance of our program for your running pleasure!
        // final JMUD proggy = new JMUD(args);
        JMUD jmud = new JMUD(args);

        // Bye-bye
    }

    /**
     *
     * @param args
     */
    public JMUD(final String[] args) {

        if (DEBUG) {
            logger = new SystemLogger();
        } else {
            logger = new NoneLogger();
        }

        logger.debug("JMUD.JMUD has started.");

//        /* This is a hashtable of simple 2-part arguments */
//        final Hashtable simpleArgs = new Hashtable();
//        simpleArgs.put("-mudlist", JMConfig.EXTMUDLIST);
//        simpleArgs.put("-listtype", JMConfig.MUDLISTTYPE);
//        simpleArgs.put("-listtags", JMConfig.MANUALEXTLIST);
//        simpleArgs.put("-mudimage", JMConfig.EXTMUDIMAGE);
        // Create our "settings" container
        settings = JMConfig.getInstance();
        String startTitle = "";
        String startWorld = "";
        int startPort = 0;

        // Let's put ourselves in the settings, so we're accessable for a QUIT or somethin'
        settings.setJMCore(this);

        // Set some defaults
        settings.setJMValue(JMConfig.USESWING, true);  // Use Swing components by default
        settings.setJMValue(JMConfig.USESWINGENTRY, true);  // Java 1.2 and 1.3 requestFocus is "broken"
        settings.setJMValue(JMConfig.BGPAINT, true);    // Paint background on Swing text component

        // Check to see if we're forcing "single user mode" or not
        if (args.length > 0) {
            String tempArg = "";
            for (int i = 0; i < args.length; i++) {
                tempArg = args[i].toLowerCase();

                checkSingleArgs(tempArg);

//                if (tempArg.equals(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("-h")) || tempArg.equals(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("-?"))) {
//                    System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("JamochaMUD_") + AboutBox.FULLVERNUM);
//                    System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("A_Java_MUD/Muck_client,_(C)2008_Jeff_Robinson."));
//                    System.out.println();
//                    System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Current_accepted_arguments:"));
//                    System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("-h/-?_____This_screen"));
//                    System.out.println("-mudlist  Make a list of MU*s available from this address (include protocol such as http://)");
//                    System.out.println("-listype  The type of list imported by via -mudlist.  Types available: MUDLISTS (default)");
//                    System.out.println("-listtags Tags used to parse the list imported via -mudlist.  Tags must be separated by a colon and contain tags for World:Name:Address:Port (this is ignored if -mudlist is not specified)");
//                    System.out.println("-mudimage The fully qualified URL for the image to be used in the JamochaMUD connector");
//                    System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("-s________Force_single-user_mode"));
//                    System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("-swing____Use_Java2_Swing_components_(default)"));
//                    System.out.println("-t Assign a title to the MU* (only when launching a world from the command line)");
//                    System.out.println("-quietrc Do not prompt the user for creation of a .jamocha.rc file, create it silently.");
//                    System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("-noswing__Use_only_Java_1.1.x_AWT_components"));
//                    System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("-nosentry_Use_Swing_except_for_the_text-entry_area"));
//                    System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("-nobg_____Do_not_paint_text_background_colours_(default)"));
//                    System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("-bg_______Paint_text_background_colours"));
//                    System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("-v________Version_of_the_program"));
//
//                    System.out.println("[world:port] (e.g. muck.foo.com:1234");
//                    System.exit(0);
//                    // Throw an exception instead of calling System.exit, otherwise JUnit will fail on tests
//                    // throw new RuntimeException();
//                }
//
//                if (tempArg.equals("-s")) {
//                    // Force single user mode
//                    settings.setJMValue(JMConfig.SINGLEUSERMODE, true);
//                }
//
//                if (tempArg.equals("-v")) {
//                    System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("JamochaMUD_") + AboutBox.FULLVERNUM);
//                    System.exit(0);
//                    // Throw an exception instead of calling System.exit, otherwise JUnit will fail on tests
//                    // throw new RuntimeException();
//
//                }
//
//                if (tempArg.equals("-swing")) {
//                    settings.setJMValue(JMConfig.USESWING, true);
//                    System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("-swing_option_is_deprecated..._enabled_by_default."));
//                }
//
//                if (tempArg.equals("-noswing")) {
//                    settings.setJMValue(JMConfig.USESWING, false);
//                    settings.setJMValue(JMConfig.USESWINGENTRY, false);
//                    System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Disabling_use_of_Swing_components."));
//                }
//
//                if (tempArg.equals("-nosentry")) {
//                    settings.setJMValue(JMConfig.USESWINGENTRY, false);
//                    System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Using_AWT_for_the_text_entry_area."));
//                }
//
//                if (tempArg.equals("-sentry")) {
//                    settings.setJMValue(JMConfig.USESWINGENTRY, true);
//                    System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Forcing_Swing-based_text-entry_component."));
//                }
//
//                if (tempArg.equals("-nobg")) {
//                    settings.setJMValue(JMConfig.BGPAINT, false);
//                    System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Background_text_painting_disabled."));
//                }
//
//                if (tempArg.equals("-bg")) {
//                    settings.setJMValue(JMConfig.BGPAINT, true);
//                    if (tempArg.equals("-mudlist")) {
//                        // If we receive a -mudlist argument we'll automatically
//                        // populate the fields with a sane defaults.  These may be
//                        // overwritten below using the hashtable
//                        logger.debug("MUDCONNECTORLIST: **" + settings.getJMString(JMConfig.MUDCONNECTORLIST) + "**");
//                        logger.debug("EXTMUDLIST: **" + settings.getJMString(JMConfig.EXTMUDLIST) + "**");
//
//                        String mcl = settings.getJMString(JMConfig.MUDCONNECTORLIST);
//                        String eml = settings.getJMString(JMConfig.EXTMUDLIST);
//
//                        logger.debug("JMUD.JMUD: Setting MUDCONNETORLIST and others.");
//                        startTitle = args[i + 1];
//                    }
//
//                    // }
//                    logger.debug("------------------------");
//                    logger.debug("MUDCONNECTORLIST: " + settings.getJMString(JMConfig.MUDCONNECTORLIST));
//                    logger.debug("EXTMUDLIST: " + settings.getJMString(JMConfig.EXTMUDLIST));
//
//                }
                // Check our hashtable for matching arguments
                // Since these are matching pairs, we have to make certain there
                // are enough args[] for the command and argument
                checkMultiArgs(args, i);

//                logger.debug("Checking for key " + tempArg);
//                logger.debug("i: " + i + " and args.length: " + args.length);
//
//                if (simpleArgs.containsKey(tempArg) && i < (args.length - 1)) {
//
//                    settings.setJMValue(simpleArgs.get(tempArg).toString(), args[i + 1]);
//                    if (tempArg.equals("-quietrc")) {
//
//                        settings.setJMValue(JMConfig.QUIETRC, true);
//
//                        System.out.println("Creating .jamocha.rc file with-out user intervention.");
//                    }
//                    if (tempArg.equals("-mudlist")) {
//                        // If we receive a -mudlist argument we'll automatically
//                        // populate the fields with a sane defaults.  These may be
//                        // overwritten below using the hashtable
//                        logger.debug("MUDCONNECTORLIST: **" + settings.getJMString(JMConfig.MUDCONNECTORLIST) + "**");
//                        logger.debug("EXTMUDLIST: **" + settings.getJMString(JMConfig.EXTMUDLIST) + "**");
//
//                        // String mcl = settings.getJMString(JMConfig.MUDCONNECTORLIST);
//                        // String eml = settings.getJMString(JMConfig.EXTMUDLIST);
//                        // if (!settings.getJMString(JMConfig.MUDCONNECTORLIST).isEmpty() && settings.getJMString(JMConfig.EXTMUDLIST).isEmpty()) {
//                        // if (!mcl.isEmpty() && eml.isEmpty()) {
//                        // }catch (NumberFormatException portExc) {
//                        settings.setJMValue(JMConfig.EXTMUDLIST, "http://www.mudconnect.com/java/Telnet/javalist.db");
//                        settings.setJMValue(JMConfig.MUDLISTTYPE, "tab");
//                        settings.setJMValue(JMConfig.MANUALEXTLIST, "0123");
//                        // }
//
//                        logger.debug("------------------------");
//                        logger.debug("MUDCONNECTORLIST: " + settings.getJMString(JMConfig.MUDCONNECTORLIST));
//                        logger.debug("EXTMUDLIST: " + settings.getJMString(JMConfig.EXTMUDLIST));
//
//                    }
//
//                    // Since these are matching pairs, we have to make certain there
//                    // are enough args[] for the command and argument
//                    logger.debug("Checking for key " + tempArg);
//                    logger.debug("i: " + i + " and args.length: " + args.length);
//                    if (simpleArgs.containsKey(tempArg) && i < (args.length - 1)) {
//
//                        settings.setJMValue(simpleArgs.get(tempArg).toString(), args[i + 1]);
//                    }
//
//                }
                // Check to see if the last argument was a world to connect.
                // This will cause us to skip the MU* Connector altogether
                if (tempArg != null) {
                    // tempArg should be the last argument in our list
                    // }catch (ClassNotFoundException except) {
                    logger.debug("JMUD.class checking " + tempArg + " for world information.");
                }

                final int colonSpot = tempArg.indexOf(':');

                if (colonSpot > 1 && colonSpot < tempArg.length()) {
                    startWorld = tempArg.substring(0, colonSpot);
                    try {
                        startPort = Integer.parseInt(tempArg.substring(colonSpot + 1));
                    } catch (NumberFormatException portExc) {
                        logger.debug("Error getting startPort from " + tempArg);
                        logger.debug("Error: " + portExc);

                    }

                    logger.debug("JMUD starting world is " + startWorld + " " + startPort);

                }

            }

        }

        boolean useSwing = settings.getJMboolean(JMConfig.USESWING);

        if (useSwing) {
            try {
                // Class swingTest = Class.forName("javax.swing.JButton");
                Class.forName(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("javax.swing.JButton"));

                logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Swing_test_passed.."));

            } catch (ClassNotFoundException except) {
                logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Swing_is_not_currently_available,_switching_to_use_the_Java_AWT."));

                logger.debug("Error: " + except);

                settings.setJMValue(JMConfig.USESWING, false);
                settings.setJMValue(JMConfig.USESWINGENTRY, false);
                useSwing = false;
            }
        }

        // Create our SplashScreen
        if (useSwing) {
            splash = (Object) new anecho.gui.SplashScreen(null, java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Getting_system_information"), new javax.swing.ImageIcon(JMainMenu.class.getResource(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("JMUDSplash.png"))));
            ((anecho.gui.SplashScreen) splash).setVisible(true);
        }

        // Set Mac L&F features.  Mac L&F features need to be set before
        // the UIManager.setLookAndFeel is called to work completely
        String lcOSName = System.getProperty("os.name").toLowerCase();
        if (useSwing && lcOSName.startsWith("mac os x")) {
            // ((anecho.gui.SplashScreen) splash).updateMessage("Setting Mac look & feel...");
            splashMessage("Setting Mac look & feel...");
            setMacFeatures();
        }

        if (useSwing) {
            // Set the look and feel to system standard
            try {
                javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException uiExc) {
                if (DEBUG) {
                    logger.debug("JMUD.JMUD(): There was an error setting things to the native Look And Feel");
                }
            }
        }

        // Get our system information
        getSysInfo();

        // Do a sanity check on our user supplied arguments
        checkArguments();

        // Read in our "profile"
//        if (useSwing) {
//            ((anecho.gui.SplashScreen) splash).updateMessage(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Reading_JamochaMUD_settings..."));
//        }
        splashMessage(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Reading_JamochaMUD_settings..."));
        getSettings();

        frameGroup = new SyncFrameGroup();

//        if (useSwing) {
//            ((anecho.gui.SplashScreen) splash).updateMessage(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Loading_main_program..."));
//        }
        splashMessage(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Loading_main_program..."));

        final MuckMain mainProg = MuckMain.getInstance();
        mainFrame = mainProg.getMainFrame();
        // mainFrame = settings.getJMFrame(JMConfig.MUCKMAINFRAME);
        frameGroup.add(mainFrame);

        connections = CHandler.getInstance();

        inputFrame = new DataIn();
        settings.setDataInVariable(inputFrame);

        // Set the visual state of our components
        setupFrames();

        // Add our text-window to the main frame
        mainProg.setMainLayout();

//        if (useSwing) {
//            ((anecho.gui.SplashScreen) splash).updateMessage(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Loading_plug-ins..."));
//        }
        splashMessage(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Loading_plug-ins..."));

        logger.debug("JMUD.JMUD calling loadPlugins");
        loadPlugins();

        logger.debug("JMUD.JMUD setting up splash screen.");
        if (useSwing) {
            // splash = (Object) new anecho.gui.SplashScreen(null, java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Getting_system_information"), new javax.swing.ImageIcon(JMainMenu.class.getResource(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("JMUDSplash.png")))); 

            ((anecho.gui.SplashScreen) splash).setVisible(false);

        }

        // Test putting the "What's New" here. Fix Me XXX
        final boolean isNew = settings.getJMboolean(JMConfig.ISNEW);
        final boolean showNew = settings.getJMboolean(JMConfig.SHOWNEW);

        logger.debug("JMUD.JMUD checking isNew and showNew");
        if (useSwing && isNew && showNew) {
            // Ask MuckMain to check the what's new box
            // I'd like to move this check to MuckMain some how.  Fix Me XXX
//            final MuckMain muMain = MuckMain.getInstance();
//            muMain.showWhatsNew(true);
            settings.setJMValue(JMConfig.ISNEW, false);
            showIsNew();
        }

        /**
         * Read in our settings. We make this a separate method in hopes that it
         * will fall out of scope and destroy our JMParseRC class when it is
         * done...
         */
        if (startPort == 0) {
            // Connect to the MU* and show the standard MU* Connector
            //JMParseRC jmParseRC;
            //jmParseRC = new JMParseRC(splash);
            connections.connectToNewMU();
        } else {
            // Do a "World Check"
            /**
             * Gather system-specific information about the platform we're
             * running on
             */
            // Connect to the World with-out showing the MU* Connector
            SysInfo sysInfo = new SysInfo();
            connections.connectToNewMU(startTitle, startWorld, startPort, 0, null);
        }

        logger.debug("JMUD.JMUD finished JMUD.");
    }

    /**
     * Check the given argument to see if there are any single-work commands
     *
     * @param tempArg the argument to be checked for a command
     */
    private void checkSingleArgs(final String tempArg) {
        if (tempArg.equals("-h") || tempArg.equals("-?")) {
            System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("JamochaMUD_") + AboutBox.FULLVERNUM);
            System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("A_Java_MUD/Muck_client,_(C)2008_Jeff_Robinson."));
            System.out.println();
            System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Current_accepted_arguments:"));
            System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("-h/-?_____This_screen"));
            System.out.println("-mudlist  Make a list of MU*s available from this address (include protocol such as http://)");
            System.out.println("-listype  The type of list imported by via -mudlist.  Types available: MUDLISTS (default)");
            System.out.println("-listtags Tags used to parse the list imported via -mudlist.  Tags must be separated by a colon and contain tags for World:Name:Address:Port (this is ignored if -mudlist is not specified)");
            System.out.println("-mudimage The fully qualified URL for the image to be used in the JamochaMUD connector");
            System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("-s________Force_single-user_mode"));
            System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("-swing____Use_Java2_Swing_components_(default)"));
            System.out.println("-t Assign a title to the MU* (only when launching a world from the command line)");
            System.out.println("-quietrc Do not prompt the user for creation of a .jamocha.rc file, create it silently.");
            System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("-noswing__Use_only_Java_1.1.x_AWT_components"));
            System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("-nosentry_Use_Swing_except_for_the_text-entry_area"));
            System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("-nobg_____Do_not_paint_text_background_colours_(default)"));
            System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("-bg_______Paint_text_background_colours"));
            System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("-v________Version_of_the_program"));

            System.out.println("[world:port] (e.g. muck.foo.com:1234");
            // System.exit(0);
            exitSystem();
            // Throw an exception instead of calling System.exit, otherwise JUnit will fail on tests
            // throw new RuntimeException();
        }

        if (tempArg.equals("-s")) {
            // Force single user mode
            settings.setJMValue(JMConfig.SINGLEUSERMODE, true);
        }

        if (tempArg.equals("-v")) {
            System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("JamochaMUD_") + AboutBox.FULLVERNUM);
            // System.exit(0);
            exitSystem();
            // Throw an exception instead of calling System.exit, otherwise JUnit will fail on tests
            // throw new RuntimeException();

        }

        if (tempArg.equals("-swing")) {
            settings.setJMValue(JMConfig.USESWING, true);
            System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("-swing_option_is_deprecated..._enabled_by_default."));
        }

        if (tempArg.equals("-noswing")) {
            settings.setJMValue(JMConfig.USESWING, false);
            settings.setJMValue(JMConfig.USESWINGENTRY, false);
            System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Disabling_use_of_Swing_components."));
        }

        if (tempArg.equals("-nosentry")) {
            settings.setJMValue(JMConfig.USESWINGENTRY, false);
            System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Using_AWT_for_the_text_entry_area."));
        }

        if (tempArg.equals("-sentry")) {
            settings.setJMValue(JMConfig.USESWINGENTRY, true);
            System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Forcing_Swing-based_text-entry_component."));
        }

        if (tempArg.equals("-nobg")) {
            settings.setJMValue(JMConfig.BGPAINT, false);
            System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Background_text_painting_disabled."));
        }

        if (tempArg.equals("-bg")) {
            settings.setJMValue(JMConfig.BGPAINT, true);
        }

//            if (tempArg.equals("-mudlist")) {
//                        // If we receive a -mudlist argument we'll automatically
//                // populate the fields with a sane defaults.  These may be
//                // overwritten below using the hashtable
//                logger.debug("MUDCONNECTORLIST: **" + settings.getJMString(JMConfig.MUDCONNECTORLIST) + "**");
//                logger.debug("EXTMUDLIST: **" + settings.getJMString(JMConfig.EXTMUDLIST) + "**");
//
//                String mcl = settings.getJMString(JMConfig.MUDCONNECTORLIST);
//                String eml = settings.getJMString(JMConfig.EXTMUDLIST);
//
//                logger.debug("JMUD.JMUD: Setting MUDCONNETORLIST and others.");
//                startTitle = args[i + 1];
//            }
        // }
        logger.debug("------------------------");
        logger.debug("MUDCONNECTORLIST: " + settings.getJMString(JMConfig.MUDCONNECTORLIST));
        logger.debug("EXTMUDLIST: " + settings.getJMString(JMConfig.EXTMUDLIST));

    }

    /**
     * This method checks the given array for multi-part arguments
     *
     * @param args an array of the tokens to be parsed for arguments
     * @param i the starting point in the array to check for arguments
     */
    private void checkMultiArgs(final String[] args, final int i) {
        /* This is a hashtable of simple 2-part arguments */
        final Hashtable simpleArgs = new Hashtable();
        simpleArgs.put("-mudlist", JMConfig.EXTMUDLIST);
        simpleArgs.put("-listtype", JMConfig.MUDLISTTYPE);
        simpleArgs.put("-listtags", JMConfig.MANUALEXTLIST);
        simpleArgs.put("-mudimage", JMConfig.EXTMUDIMAGE);

        String tempArg = args[i].toLowerCase();
        logger.debug("Checking for key " + tempArg);
        logger.debug("i: " + i + " and args.length: " + args.length);

        if (simpleArgs.containsKey(tempArg) && i < (args.length - 1)) {

            settings.setJMValue(simpleArgs.get(tempArg).toString(), args[i + 1]);
            if (tempArg.equals("-quietrc")) {

                settings.setJMValue(JMConfig.QUIETRC, true);

                System.out.println("Creating .jamocha.rc file with-out user intervention.");
            }
            if (tempArg.equals("-mudlist")) {
                // If we receive a -mudlist argument we'll automatically
                // populate the fields with a sane defaults.  These may be
                // overwritten below using the hashtable
                logger.debug("MUDCONNECTORLIST: **" + settings.getJMString(JMConfig.MUDCONNECTORLIST) + "**");
                logger.debug("EXTMUDLIST: **" + settings.getJMString(JMConfig.EXTMUDLIST) + "**");

                // String mcl = settings.getJMString(JMConfig.MUDCONNECTORLIST);
                // String eml = settings.getJMString(JMConfig.EXTMUDLIST);
                // if (!settings.getJMString(JMConfig.MUDCONNECTORLIST).isEmpty() && settings.getJMString(JMConfig.EXTMUDLIST).isEmpty()) {
                // if (!mcl.isEmpty() && eml.isEmpty()) {
                // }catch (NumberFormatException portExc) {
                settings.setJMValue(JMConfig.EXTMUDLIST, "http://www.mudconnect.com/java/Telnet/javalist.db");
                settings.setJMValue(JMConfig.MUDLISTTYPE, "tab");
                settings.setJMValue(JMConfig.MANUALEXTLIST, "0123");
                // }

                logger.debug("------------------------");
                logger.debug("MUDCONNECTORLIST: " + settings.getJMString(JMConfig.MUDCONNECTORLIST));
                logger.debug("EXTMUDLIST: " + settings.getJMString(JMConfig.EXTMUDLIST));

            }

            // Since these are matching pairs, we have to make certain there
            // are enough args[] for the command and argument
            logger.debug("Checking for key " + tempArg);
            logger.debug("i: " + i + " and args.length: " + args.length);
            if (simpleArgs.containsKey(tempArg) && i < (args.length - 1)) {

                settings.setJMValue(simpleArgs.get(tempArg).toString(), args[i + 1]);
            }

        }

    }

    /**
     * Display the provided message on the splash screen
     *
     * @param message
     */
    private void splashMessage(final String message) {
        boolean useSwing = settings.getJMboolean(JMConfig.USESWING);

        if (useSwing) {
            ((anecho.gui.SplashScreen) splash).updateMessage(message);
        }

    }

    private void showIsNew() {
        // Ask MuckMain to check the what's new box
        // I'd like to move this check to MuckMain some how.  Fix Me XXX
        final MuckMain muMain = MuckMain.getInstance();
        muMain.showWhatsNew(true);
        // settings.setJMboolean(JMConfig.ISNEW, "false");
        settings.setJMValue(JMConfig.ISNEW, false);

    }

    /**
     * Setup the visual elements for the user
     */
    private void setupFrames() {
        logger.debug("JMUD.setupFrames() called.");
        settings.setJMValue(JMConfig.MUCKMAINTITLE, "JamochaMUD");

        // Set the size of our windows
        Rectangle tempRect;
        tempRect = settings.getJMRectangle(JMConfig.MAINWINDOW);

        if (tempRect == null) {
            mainFrame.setBounds(new Rectangle(0, 10, 600, 355));
        } else {
            mainFrame.setBounds(settings.getJMRectangle(JMConfig.MAINWINDOW));
        }

        tempRect = settings.getJMRectangle(JMConfig.DATABAR);

        // if (tempRect != null) {
        // inputFrame.setBounds(settings.getJMRectangle(JMConfig.DATABAR));
        // } else {
        // inputFrame.setBounds(new Rectangle(0, 385, 600, 60));
        // }
        if (tempRect == null) {
            inputFrame.setBounds(new Rectangle(0, 385, 600, 60));
        } else {
            inputFrame.setBounds(settings.getJMRectangle(JMConfig.DATABAR));
        }

        boolean sync = false;
        if (settings.getJMboolean(JMConfig.SPLITVIEW)) {
            sync = settings.getJMboolean(JMConfig.SYNCWINDOWS);
        }

        // Do this for now, but fix the setGroupSync function in SyncFrame/SyncGroup!!!
        // Fix me XXX
        // ((SyncFrame)mainFrame).setSyncFrameGroup(frameGroup);
        inputFrame.setSyncFrameGroup(frameGroup);
        // ((SyncFrame)mainFrame).setSync(sync);
        inputFrame.setSync(sync);

        // Create our area that will hold our output "windows"
        if (settings.getJMboolean(JMConfig.USESWING)) {
            if (DEBUG) {
                System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Swing_being_used_by_JMUD."));
            }
            ((JSyncFrame) mainFrame).setSyncFrameGroup(frameGroup);
            ((JSyncFrame) mainFrame).setSync(sync);
        } else {
            if (DEBUG) {
                System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("We're_not_using_swing_in_JMUD."));
            }
            // anecho.JamochaMUD.legacy.JMTabPanel textPanel = new anecho.JamochaMUD.legacy.JMTabPanel();
            ((SyncFrame) mainFrame).setSyncFrameGroup(frameGroup);
            ((SyncFrame) mainFrame).setSync(sync);
        }

        logger.debug("JMUD.setupFrames() finished.");
    }

    /**
     * Check to see if there are more arguments remaining in the given list
     *
     * @param args The list of arguments
     * @param pos The position in the list to start checking from
     * @return <code>true</code> There is an additional argument available
     * <code>false</code> There are no more arguments available
     */
    private boolean moreArgs(final String[] args, final int pos) {
        boolean retVal = false;

        if (pos + 1 < args.length && args[pos + 1] != null) {
            retVal = true;
        }

        return retVal;

    }

    /**
     * Read in our settings. We make this a separate method in hopes that it
     * will fall out of scope and destroy our JMParseRC class when it is done...
     */
    private void getSettings() {
        // We'll pass our 'settings' variable to get things set up correctly
        JMParseRC jmParseRC;
        jmParseRC = new JMParseRC(splash);
    }

    /**
     * Gather system-specific information about the platform we're running on.
     * Though we may not see the results in JMUD, this will load settings into
     * our main configuration file
     *
     * This probably should be a FIX ME XXX
     */
    private void getSysInfo() {
        SysInfo sysInfo = new SysInfo();
    }

    /**
     * This method calls the method to load all the JamochaMUD plug-ins
     */
    private void loadPlugins() {
        final EnumPlugIns plugEnum = EnumPlugIns.getInstance();
        settings.setPlugEnumerator(plugEnum);
        plugEnum.loadPlugIns(splash);
    }

    /**
     * Tell our plug-ins that we are shutting down, so they better save their
     * data!
     */
    private void haltPlugins() {
        // plugEnum.haltPlugIns();
        EnumPlugIns.getInstance().haltPlugIns();
    }

    /**
     * This method collects all the data needed to write a new .rc file of the
     * user's settings, and do any other necessary clean-up
     */
    public void quitJamochaMUD() {
        // Gather all our settings that remain.
        Rectangle test;

        test = mainFrame.getBounds();
        if (checkVisible(test)) {
            settings.setJMValue(JMConfig.MAINWINDOW, test);

        }

        test = inputFrame.getBounds();
        if (checkVisible(test)) {
            settings.setJMValue(JMConfig.DATABAR, test);
        }

        // Write out the .jamocha.rc file if JamochaMUD is not operating
        // in diskless mode
        if (!settings.getJMboolean(JMConfig.DISKLESSCONFIG)) {
            final JMWriteRC done = new JMWriteRC();

            done.writeRCToFile();
        }

        haltPlugins();

        // System.exit(0);
        exitSystem();
        // Throw an exception instead of calling System.exit, otherwise JUnit will fail on tests
        // throw new RuntimeException();

    }

    /**
     * Kill off JamochaMUD.  This puts this questionable code all in one place.
     */
    private void exitSystem() {
        System.exit(0);
    }
    /**
     * Check the bounds of our rectangle to see if it is visible or not. This is
     * basically a hack, but better'n nothing.
     *
     */
    private boolean checkVisible(final Rectangle testRect) {
        int xAxis, yAxis;   // Our location on the screen
        boolean retVal = true;
        xAxis = testRect.x;
        yAxis = testRect.y;

        if (xAxis < 0 || yAxis < 0) {
            // Our object is probably minimised, so use the settings from JMConfig
            retVal = false;
        }
        /**
         * Change the main title
         */
        // Our object is probably visible on-screen, so we'll return true
        return retVal;
    }

    /**
     * Change our fonts globally across all windows
     *
     * @param newStyle
     */
    public void setAllFonts(final Font newStyle) {
        // Change the font style of all our connections
        if (connections != null) {
            connections.setAllFonts(newStyle);
        }

        // Change the font style of the input bar
        if (inputFrame != null) {
            inputFrame.setFont(newStyle);
        }
    }

    /**
     * Change our colours globally across all windows
     *
     * @param foreground
     * @param background
     */
    public void setAllColours(final Color foreground, final Color background) {
        // Change the colours of our connections
        connections.setAllColours(foreground, background);

        // set the colour of our input frame
        inputFrame.setBackgroundColour(background);
        inputFrame.setForegroundColour(foreground);
    }

    /**
     * Change the synchronisation status of our windows
     *
     * @param sync
     */
    public void setAllSync(final boolean sync) {
        // Only change our Sync value if we're using split panes
        if (settings.getJMboolean(JMConfig.SPLITVIEW)) {
            settings.setJMValue(JMConfig.SYNCWINDOWS, sync);
            ((SyncFrame) mainFrame).setSync(sync);
            inputFrame.setSync(sync);
        }
    }

    /**
     * Change the main title
     */
    public void setMainWindowTitle() {
        // mainProg.setWindowTitle();
        MuckMain.getInstance().setWindowTitle();
        inputFrame.setWindowTitle();
    }

    /**
     * This method is used to make certain that variables have sane defaults
     * based on user supplied arguments
     */
    private void checkArguments() {
        // Check the external mud list arguments
        final String mudListLoc = settings.getJMString(JMConfig.EXTMUDLIST);

        // if (!mudListLoc.equals("")) {
        if (!"".equals(mudListLoc)) {
            // If we are supplied a location for a remote list then we must
            // do a sanity check on our list options
            final String mudListType = settings.getJMString(JMConfig.MUDLISTTYPE);
            final String mudManualList = settings.getJMString(JMConfig.MANUALEXTLIST);

            // if (mudListType.equals("") && mudManualList.equals("")) {
            if ("".equals(mudListType) && "".equals(mudManualList)) {
                // We weren't supplied a Mud List type or manual type, so set the Mud List to default
                settings.setJMValue(JMConfig.MUDLISTTYPE, "MUDLISTS");
            }
        }
    }

    /**
     * Set system properties that support special Mac look-and-feel items
     */
    private void setMacFeatures() {
        // Put the JamochaMUD name on the main apple screen menu bar
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "JamochaMUD");

        // Put the menu bar on the main apple screen menu bar
        System.setProperty("apple.laf.useScreenMenuBar", "true");

    }
}
