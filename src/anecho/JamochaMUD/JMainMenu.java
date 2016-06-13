/**
 * JMainMenu.java, the main menu for JamochaMUD made of Swing components $Id:
 * JMainMenu.java,v 1.16 2014/05/20 02:20:28 jeffnik Exp $
 */

/* JamochaMUD, a Muck/Mud client program
 * Copyright (C) 1998-2009  Jeff Robinson
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * vesion 2, as published by the Free Software Foundation.
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

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import net.sf.wraplog.AbstractLogger;
import net.sf.wraplog.NoneLogger;
import net.sf.wraplog.SystemLogger;

/**
 * Create a menu for the Main Window
 */
public final class JMainMenu {

    /**
     * The singleton variable for this class
     */
    private static JMainMenu _instance;
    private transient JMenuBar tWMenuBar;
    private transient JCheckBoxMenuItem tWMacro, tWSyncWindowsItem, tWUseUnicodeItem, tWTFKeysItem, tWLocalEchoItem;
    private transient JCheckBoxMenuItem splitFramesItem;
    private transient JCheckBoxMenuItem tWAutoFocus, tWTimers, tWReleasePauseItem, tWAltFocus, tWAntiAliasItem, tWLowColourItem;
    private transient JMenu tWMUListMenu;
    private transient JMenuItem cTM, dFM, rTM, closeMU, addMU;
    private transient JMenuItem dumpOutputItem, quitItem;
    // private JMenuItem pingMUItem;
    private transient JMenuItem copyItem, pasteItem, findItem;
    private transient JMenuItem configItem;
    private transient JMenuItem coloursItem;
    // private transient JMenuItem externalProgramsItem;
    // private transient JMenuItem serverOptionsItem;
    // private JMenuItem contentsItem;
    private transient JMenuItem tfKeysItem, troubleshootingItem, aboutJamochaMUDItem;
    private transient JMenuItem installPlugIn, removePlugIn;
    private transient JMenuItem importConfigItem, exportConfigItem;
    private transient JMenuItem contentsItem;
    private transient JMenuItem whatsNewItem;
    // private transient JMConfig settings;
    protected static JMenu tWPlugInMenu;
    /**
     * Enable or disable debugging output
     */
    private static final boolean DEBUG = false;

    private final AbstractLogger logger;

    /**
     *
     */
    private JMainMenu() {
        if (DEBUG) {
            logger = new SystemLogger();
        } else {
            logger = new NoneLogger();
        }
    }

    // For lazy initialization
    /**
     * Creates a new instance of the JMainMenu
     *
     * @return
     */
    public static synchronized JMainMenu getInstance() {
        if (_instance == null) {
            _instance = new JMainMenu();
        }
        return _instance;
    }

    /**
     * mainProg
     *
     * @param parent
     * @param mainProg
     */
    public void buildMenu(final JFrame parent, final MuckMain mainProg) {
        // this.settings = settings;
        JMConfig settings = JMConfig.getInstance();
        tWMenuBar = new JMenuBar();

        parent.setJMenuBar(tWMenuBar);

        // MENU NOTE!!!
        // By using Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() JamochaMUD
        // will use the OS-correct key-mask... "CTRL" on many systems or "Command" on Mac
        final JMenu tWFileMenu = new JMenu(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("file"));
        tWFileMenu.setMnemonic(KeyEvent.VK_F);
        tWMenuBar.add(tWFileMenu);

        dumpOutputItem = new JMenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("dumpOutput"), new ImageIcon(JMainMenu.class.getResource(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("icons/22/filesave.png"))));
        dumpOutputItem.setMnemonic(KeyEvent.VK_D);
        dumpOutputItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

        // dumpOutputItem = buildMenuItem("dumpOutput",            // Name of item
        // "icons/22/filesave.png", // Item icon
        // KeyEvent.VK_D,           // Mnemonic
        // true,                    // Has an accelerator?
        // KeyEvent.VK_D,           // Accelerator key
        // true);                   // use CTRL?
        tWFileMenu.add(dumpOutputItem);
        // new MenuShortcut(KeyEvent.VK_D, false)));
        dumpOutputItem.setActionCommand(dumpOutputItem.getText());
        dumpOutputItem.addActionListener(mainProg);

        tWFileMenu.addSeparator();
        tWFileMenu.add(importConfigItem = new JMenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("import"), new ImageIcon(JMainMenu.class.getResource(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("icons/22/import.png")))));
        // importConfigItem.setMnemonic(KeyEvent.VK_Q);
        // importConfigItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        importConfigItem.setActionCommand(importConfigItem.getText());
        importConfigItem.addActionListener(mainProg);

        tWFileMenu.add(exportConfigItem = new JMenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("export"), new ImageIcon(JMainMenu.class.getResource(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("icons/22/export.png")))));
        // importConfigItem.setMnemonic(KeyEvent.VK_Q);
        // importConfigItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        exportConfigItem.setActionCommand(exportConfigItem.getText());
        exportConfigItem.addActionListener(mainProg);

        tWFileMenu.addSeparator();
        tWFileMenu.add(quitItem = new JMenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("quit"), new ImageIcon(JMainMenu.class.getResource(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("icons/22/exit.png")))));
        quitItem.setMnemonic(KeyEvent.VK_Q);
        quitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        quitItem.setActionCommand(quitItem.getText());
        quitItem.addActionListener(mainProg);

        tWFileMenu.addActionListener(mainProg);

        // Add the Edit menu items
        final JMenu tWEditMenu = new JMenu(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("edit"));
        tWEditMenu.setMnemonic(KeyEvent.VK_E);
        tWMenuBar.add(tWEditMenu);
        // I'm making it the right key for OS/2, damn't!
        // if (settings.getOSName().equals("OS/2")) {
        if ((settings.getJMString(JMConfig.OSNAME)).equals(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("OS/2"))) {
            // if (settings.getJMString(settings.OSNAME).equals("OS/2")) {
            copyItem = new JMenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("copyFromMainWindow"), new ImageIcon(JMainMenu.class.getResource(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("icons/22/editcopy.png"))));
            copyItem.setMnemonic(KeyEvent.VK_C);
            copyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        } else {
            copyItem = new JMenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("copyFromMainWindow"), new ImageIcon(JMainMenu.class.getResource(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("icons/22/editcopy.png"))));
            copyItem.setMnemonic(KeyEvent.VK_C);
            copyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        }
        copyItem.setActionCommand(copyItem.getText());
        copyItem.addActionListener(mainProg);
        tWEditMenu.add(copyItem);

        pasteItem = new JMenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("paste"), new ImageIcon(JMainMenu.class.getResource(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("icons/22/editpaste.png"))));
        pasteItem.setMnemonic(KeyEvent.VK_P);
        pasteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        pasteItem.setActionCommand(pasteItem.getText());
        pasteItem.addActionListener(mainProg);
        tWEditMenu.add(pasteItem);

        tWEditMenu.addSeparator();

        findItem = new JMenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("find"), new ImageIcon(JMainMenu.class.getResource(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("icons/22/find.png"))));
        findItem.setMnemonic(KeyEvent.VK_F);
        findItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        findItem.setActionCommand(findItem.getText());
        findItem.addActionListener(mainProg);
        findItem.setEnabled(false);
        tWEditMenu.add(findItem);
        tWEditMenu.addActionListener(mainProg);

        // Add CONNECTION menu items
        final JMenu tWConnectMenu = new JMenu(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("connection"));
        tWConnectMenu.setMnemonic(KeyEvent.VK_C);
        tWMenuBar.add(tWConnectMenu);
        cTM = new JMenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("connectToMU"), new ImageIcon(JMainMenu.class.getResource(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("icons/22/connect_established.png"))));
        cTM.setMnemonic(KeyEvent.VK_C);
        cTM.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        cTM.setActionCommand(cTM.getText());
        cTM.addActionListener(mainProg);
        tWConnectMenu.add(cTM);

        rTM = new JMenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("reconnectToMU"), new ImageIcon(JMainMenu.class.getResource(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("icons/22/reload.png"))));
        rTM.setMnemonic(KeyEvent.VK_R);
        rTM.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        rTM.setActionCommand(rTM.getText());
        rTM.addActionListener(mainProg);
        tWConnectMenu.add(rTM);

        dFM = new JMenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("disconnectFromMU"), new ImageIcon(JMainMenu.class.getResource(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("icons/22/connect_no.png"))));
        dFM.setMnemonic(KeyEvent.VK_D);
        dFM.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        dFM.setActionCommand(dFM.getText());
        dFM.addActionListener(mainProg);
        dFM.setEnabled(false);
        tWConnectMenu.add(dFM);

        closeMU = new JMenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("closeThisView"), new ImageIcon(JMainMenu.class.getResource(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("icons/22/round_stop.png"))));
        closeMU.setMnemonic(KeyEvent.VK_V);
        closeMU.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_0, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        closeMU.addActionListener(mainProg);
        closeMU.setEnabled(false);
        tWConnectMenu.add(closeMU);

        tWConnectMenu.addSeparator();

        // addMU = new JMenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("addToMUConnector"), new ImageIcon(JMainMenu.class.getResource("icons/22/round_stop.png")));
        addMU = new JMenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("addToMUConnector"));
        addMU.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        addMU.addActionListener(mainProg);
        tWConnectMenu.add(addMU);

        /*
         tWConnectMenu.add(pingMUItem = new JMenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("pingMU")));
         pingMUItem.addActionListener(mainProg);
         pingMUItem.setEnabled(false);
         tWConnectMenu.addActionListener(mainProg);
         */
        final JMenu tWOptionMenu = new JMenu(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("options"));
        tWOptionMenu.setMnemonic(KeyEvent.VK_O);
        tWMenuBar.add(tWOptionMenu);

        tWOptionMenu.add(configItem = new JMenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Configure_JamochaMUD"), new ImageIcon(JMainMenu.class.getResource(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("icons/22/configure.png")))));
        configItem.setMnemonic(KeyEvent.VK_C);
        // configItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        configItem.setActionCommand(configItem.getText());
        configItem.addActionListener(mainProg);

        // Add ****Edit Options**** Submenu
//        final JMenu tWEditOptionsMenu = new JMenu(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("editOptions"));
//        tWEditOptionsMenu.setMnemonic(KeyEvent.VK_E);
//        tWOptionMenu.add(tWEditOptionsMenu);
        // tWEditOptionsMenu.add(externalProgramsItem = new JMenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("externalPrograms"), new ImageIcon(JMainMenu.class.getResource(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("icons/22/configure.png")))));
        // externalProgramsItem.setMnemonic(KeyEvent.VK_E);
        // externalProgramsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        // externalProgramsItem.setActionCommand(externalProgramsItem.getText());
        // externalProgramsItem.addActionListener(mainProg);
        installPlugIn = new JMenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("installPlugin"), new ImageIcon(JMainMenu.class.getResource(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("icons/22/fileexport.png"))));
        installPlugIn.setMnemonic(KeyEvent.VK_I);
        installPlugIn.setActionCommand(installPlugIn.getText());
        installPlugIn.addActionListener(mainProg);

        removePlugIn = new JMenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("removePlugin"));
        removePlugIn.setMnemonic(KeyEvent.VK_R);
        removePlugIn.setEnabled(false);
        removePlugIn.setActionCommand(removePlugIn.getText());
        removePlugIn.addActionListener(mainProg);

//        tWEditOptionsMenu.add(serverOptionsItem = new JMenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("serverOptions"), new ImageIcon(JMainMenu.class.getResource(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("icons/22/configure.png")))));
//        serverOptionsItem.setMnemonic(KeyEvent.VK_S);
//        tWEditOptionsMenu.addActionListener(mainProg);
//        serverOptionsItem.addActionListener(mainProg);
        final JMenu tWDisplayOptionsMenu = new JMenu(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("displayOptions"));
        tWOptionMenu.add(tWDisplayOptionsMenu);

        tWDisplayOptionsMenu.add(coloursItem = new JMenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("fontsAndColours"), new ImageIcon(JMainMenu.class.getResource(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("icons/22/fonts.png")))));
        coloursItem.setMnemonic(KeyEvent.VK_F);
        coloursItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        coloursItem.setActionCommand(coloursItem.getText());
        coloursItem.addActionListener(mainProg);

        tWAntiAliasItem = new JCheckBoxMenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("antiAlias"), true);
        tWDisplayOptionsMenu.add(tWAntiAliasItem);
        tWAntiAliasItem.setState(settings.getJMboolean(JMConfig.ANTIALIAS));
        tWAntiAliasItem.addItemListener(mainProg);

        tWLowColourItem = new JCheckBoxMenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Low_colour_display"), false);
        tWDisplayOptionsMenu.add(tWLowColourItem);
        tWLowColourItem.setState(settings.getJMboolean(JMConfig.LOWCOLOUR));
        tWLowColourItem.addItemListener(mainProg);

        // Continue with normal option menu
        tWOptionMenu.addSeparator();

        tWMacro = new JCheckBoxMenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("showMacroBar"), false);
        tWOptionMenu.add(tWMacro);
        tWMacro.addItemListener(mainProg);
        tWMacro.setEnabled(false);

        tWTimers = new JCheckBoxMenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("showTimers"), false);
        tWTimers.setMnemonic(KeyEvent.VK_T);
        tWTimers.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        tWTimers.setActionCommand(tWTimers.getText());
        tWOptionMenu.add(tWTimers);
        tWTimers.addItemListener(mainProg);
        tWTimers.addActionListener(mainProg);
        // Fix mainProg XXX!!  Right now we have no option for timers at all
        tWTimers.setEnabled(false);

        splitFramesItem = new JCheckBoxMenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("splitFrames"), false);
        tWOptionMenu.add(splitFramesItem);

//        if (settings.getJMboolean(JMConfig.SPLITVIEW)) {
//            // viewStyle = SPLIT;
//            splitFramesItem.setState(true);
//        } else {
//            // viewStyle = COMBINED;
//        }
        splitFramesItem.setState(settings.getJMboolean(JMConfig.SPLITVIEW));

        splitFramesItem.addItemListener(mainProg);

        tWSyncWindowsItem = new JCheckBoxMenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("syncWindows"), true);
        tWOptionMenu.add(tWSyncWindowsItem);
        // Set this to false for now, as it breaks things in the current incarnation
        // Fix this XXX!
        // tWSyncWindowsItem.setState(settings.getJMboolean(JMConfig.SYNCWINDOWS));
        tWSyncWindowsItem.setState(false);
        tWSyncWindowsItem.setEnabled(false);
        tWSyncWindowsItem.addItemListener(mainProg);

        tWOptionMenu.addSeparator();

        tWAutoFocus = new JCheckBoxMenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("autoFocusInput"), true);
        tWOptionMenu.add(tWAutoFocus);
        tWAutoFocus.setState(settings.getJMboolean(JMConfig.AUTOFOCUSINPUT));
        tWAutoFocus.addItemListener(mainProg);

        tWAltFocus = new JCheckBoxMenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("altFocus"), true);
        tWOptionMenu.add(tWAltFocus);
        tWAltFocus.setState(settings.getJMboolean(JMConfig.ALTFOCUS));
        if (!splitFramesItem.getState()) {
            tWAltFocus.setEnabled(false);
        }
        tWAltFocus.addItemListener(mainProg);

        tWLocalEchoItem = new JCheckBoxMenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("localEcho"), true);
        tWOptionMenu.add(tWLocalEchoItem);
        tWLocalEchoItem.setState(settings.getJMboolean(JMConfig.LOCALECHO));
        tWLocalEchoItem.addItemListener(mainProg);

        tWReleasePauseItem = new JCheckBoxMenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("releasePause"), true);
        tWOptionMenu.add(tWReleasePauseItem);
        tWReleasePauseItem.setState(settings.getJMboolean(JMConfig.RELEASEPAUSE));
        tWReleasePauseItem.addItemListener(mainProg);

        tWTFKeysItem = new JCheckBoxMenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("tinyFugueKeys"), true);
        tWOptionMenu.add(tWTFKeysItem);
        tWTFKeysItem.setState(settings.getJMboolean(JMConfig.TFKEYEMU));
        tWTFKeysItem.addItemListener(mainProg);

        tWUseUnicodeItem = new JCheckBoxMenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("useUnicode"), false);
        tWOptionMenu.add(tWUseUnicodeItem);
        tWUseUnicodeItem.setState(settings.getJMboolean(JMConfig.USEUNICODE));
        tWUseUnicodeItem.addItemListener(mainProg);

        tWUseUnicodeItem = new JCheckBoxMenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("autoLogging"), false);
        tWOptionMenu.add(tWUseUnicodeItem);
        tWUseUnicodeItem.setState(settings.getJMboolean(JMConfig.AUTOLOGGING));
        tWUseUnicodeItem.addItemListener(mainProg);

        // Add the ActionListener to mainProg menu
        tWOptionMenu.addActionListener(mainProg);

        // Add the Plug-in menu items
        tWPlugInMenu = new JMenu(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("plugIn"));
        tWPlugInMenu.setMnemonic(KeyEvent.VK_P);
        tWMenuBar.add(tWPlugInMenu);
        tWPlugInMenu.addActionListener(mainProg);

        // List for our active MU*s
        tWMUListMenu = new JMenu(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("MUList"));
        tWMUListMenu.setMnemonic(KeyEvent.VK_M);
        tWMenuBar.add(tWMUListMenu);
        tWMUListMenu.addActionListener(mainProg);

        //Add HELP menu items
        final JMenu tWHelpMenu = new JMenu(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("help"));
        tWHelpMenu.setMnemonic(KeyEvent.VK_H);
        tWMenuBar.add(tWHelpMenu);

        tWHelpMenu.add(contentsItem = new JMenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("contents"), new ImageIcon(JMainMenu.class.getResource("icons/22/contents.png"))));
        contentsItem.setMnemonic(KeyEvent.VK_H);
        contentsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        contentsItem.setActionCommand(contentsItem.getText());
        contentsItem.addActionListener(mainProg);

        tWHelpMenu.add(troubleshootingItem = new JMenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("reportABug"), new ImageIcon(JMainMenu.class.getResource(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("icons/22/contents.png")))));
        troubleshootingItem.addActionListener(mainProg);
        tWHelpMenu.add(tfKeysItem = new JMenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("tinyFugueKeyCodes"), new ImageIcon(JMainMenu.class.getResource(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("icons/22/contents.png")))));
        tfKeysItem.addActionListener(mainProg);

        tWHelpMenu.add(whatsNewItem = new JMenuItem("What's new?"));
        whatsNewItem.addActionListener(mainProg);

        tWHelpMenu.addSeparator();
        tWHelpMenu.add(aboutJamochaMUDItem = new JMenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("aboutJamochaMUD"), new ImageIcon(JMainMenu.class.getResource(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("kehza.gif")))));
        aboutJamochaMUDItem.addActionListener(mainProg);
        tWHelpMenu.addActionListener(mainProg);

    }

    /**
     *
     * @param state
     */
    public synchronized void setCloseMUEnabled(final boolean state) {
        closeMU.setEnabled(state);
    }

    /**
     * This method removes all plug-in entries from the plug-ins menu
     */
    public void removeAllPlugins() {
        tWPlugInMenu.removeAll();
        tWPlugInMenu.add(installPlugIn);
        tWPlugInMenu.add(removePlugIn);
        tWPlugInMenu.addSeparator();
    }

    /**
     *
     * @param plug
     */
    public void addPlugin(final anecho.JamochaMUD.plugins.PlugInterface plug) {

        if (plug != null) {
            logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("JMainMenu.addPlugin:_") + plug);
            logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("JMainMenu_adding_plug-in:_") + plug.plugInName());

            final String name = plug.plugInName();
            final boolean state = plug.isActive();
            final boolean properties = plug.hasProperties();

            final JMenu tempName = new JMenu(name);
            if (state) {
                tempName.setIcon(new ImageIcon(JMainMenu.class.getResource(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("icons/22/Sphere_Green.png"))));
            } else {
                tempName.setIcon(new ImageIcon(JMainMenu.class.getResource(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("icons/22/Sphere_Red.png"))));
            }

            final JMenuItem prop = new JMenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("properties"));
            prop.setIcon(new ImageIcon(JMainMenu.class.getResource(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("icons/22/configure.png"))));

            if (properties) {
                prop.setActionCommand("plugin:" + name);
                prop.addActionListener(MuckMain.getInstance());
            } else {
                // No settings are available for this item
                prop.setEnabled(false);
            }

            final JMenuItem action = new JMenuItem();
            // Check to see if we should offer the enable or disable menu item
            if (state) {
                action.setText(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("disable"));
                action.setIcon(new ImageIcon(JMainMenu.class.getResource(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("icons/22/button_cancel.png"))));
                action.setActionCommand(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("disable:") + name);
            } else {
                action.setText(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("enable"));
                action.setIcon(new ImageIcon(JMainMenu.class.getResource(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("icons/22/button_ok.png"))));
                action.setActionCommand(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("enable:") + name);
            }

            // action.addActionListener(settings.getMainWindowVariable());
            action.addActionListener(MuckMain.getInstance());

            final JMenuItem desc = new JMenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("description"), new ImageIcon(JMainMenu.class.getResource(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("icons/22/info.png"))));
            desc.setActionCommand("description:" + name);
            // desc.addActionListener(settings.getMainWindowVariable());
            desc.addActionListener(MuckMain.getInstance());

            tempName.add(prop);
            tempName.add(action);
            tempName.addSeparator();
            tempName.add(desc);

            tWPlugInMenu.add(tempName);
        }
    }

    /**
     * This method allows the plug-in and menu items to be updated to reflect
     * the current state of the plug-in.
     *
     * @param state the new state of our plug-in
     * @param actCom the action-command we use to locate our menu item true
     * means the plug-in is active false means the plug-in is inactive
     */
    public void changePlugInState(final boolean state, final String actCom) {
        logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("JMainMenu.changePlugInState()_called."));

        /*
         if (state) {
         action.setText(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("disable"));
         action.setIcon(new ImageIcon(JMainMenu.class.getResource("icons/22/button_cancel.png")));
         action.setActionCommand("disable:" + name);
         } else {
         action.setText(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("enable"));
         action.setIcon(new ImageIcon(JMainMenu.class.getResource("icons/22/button_ok.png")));
         action.setActionCommand("enable:" + name);
         }
         */
    }

    /**
     *
     * @param mainProg
     */
    public void updateConnectionMenu(final MuckMain mainProg) {
        logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("JMainMenu.updateConnectionMenu:_entering_method"));

        // final MuckMain mainVar = settings.getMainWindowVariable();
        final MuckMain mainVar = MuckMain.getInstance();

        // First, check to see if we have any connections.
        // If not, then we don't need to do any more here
        logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("JMainMenu.updateConnectionMenu:_checking_total_connections"));

        // final CHandler connHandler = settings.getConnectionHandler();
        final CHandler connHandler = CHandler.getInstance();
        final int totalCon = connHandler.totalConnections();

        logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("JMainMenu.updateConnectionMenu:_total_connections_successful"));

        // if (connHandler.totalConnections() < 1) {
        if (totalCon < 1) {
            return;
        }

        // Clear the current list of MU*'s
        tWMUListMenu.removeActionListener(mainVar);
        tWMUListMenu.removeAll();

        // Add the two generic entries... the next and previous choices
        StringBuffer tempName;
        JMenuItem tempMenu;

        tempMenu = new JMenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("previousMU"), new ImageIcon(JMainMenu.class.getResource(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("icons/22/1leftarrow.png"))));
        tempMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        tempMenu.setActionCommand(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("previousMU"));
        tempMenu.addActionListener(mainProg);

        logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("JMainMenu.updateConnectionMenu:_checking_total_connections."));

        if (totalCon < 2) {
            tempMenu.setEnabled(false);
        }

        logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("JmainMenu.updateConnectionMenu:_completd_checking_total_connections."));

        tWMUListMenu.add(tempMenu);

        tempMenu = new JMenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("nextMU"), new ImageIcon(JMainMenu.class.getResource(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("icons/22/1rightarrow.png"))));
        tempMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        tempMenu.setActionCommand(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("nextMU"));
        tempMenu.addActionListener(mainProg);
        if (totalCon < 2) {
            tempMenu.setEnabled(false);
        }
        tWMUListMenu.add(tempMenu);

        tWMUListMenu.addSeparator();

        final int tlist = totalCon - 1;
        final int activeConnection = connHandler.getActiveMUIndex();

        // Loop through our connections and build a new menu
        for (int i = 0; i <= tlist; i++) {
            logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("JMainMenu:_Getting_muck_name_for_menu."));

            tempName = new StringBuffer(connHandler.getTitle(i));

            logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("JMainMenu:_Menu_name:_") + tempName);

            if (i == activeConnection) {
                // This is our active MU*
                // We'll just differentiate with a check beside it's name
                tWMUListMenu.add(new JCheckBoxMenuItem(tempName.toString(), true));
            } else {
                tempMenu = new JMenuItem(tempName.toString());
                tempMenu.addActionListener(mainProg);

                // Assign an accelerator to the connection
                if (i < 10) {
                    final char keyStroke = Integer.toString(i).charAt(0);
                    tempMenu.setAccelerator(KeyStroke.getKeyStroke(keyStroke, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

                }
//                switch (i) {
//                    case 0:
//                        // tempMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
//                        tempMenu.setAccelerator(KeyStroke.getKeyStroke('1', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
//                        break;
//                    case 1:
//                        tempMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
//                        break;
//                    case 2:
//                        tempMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
//                        break;
//                    case 3:
//                        tempMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
//                        break;
//                    case 4:
//                        tempMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_5, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
//                        break;
//                    case 5:
//                        tempMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_6, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
//                        break;
//                    case 6:
//                        tempMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_7, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
//                        break;
//                    case 7:
//                        tempMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_8, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
//                        break;
//                    case 8:
//                        tempMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_9, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
//                        break;
//                    case 9:
//                        tempMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_0, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
//                        break;
//                    default:
//                        break;
//                }

                tempMenu.setActionCommand(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("ChangeMU:") + i);
                tempMenu.addActionListener(mainVar);
                tWMUListMenu.add(tempMenu);
            }
        }

        tWMUListMenu.addActionListener(mainVar);
        logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("JMainMenu.updateConnectionMenu:_leaving_method"));

    }

    /**
     * Set the menus to reflect if we're connected (true) or not (false)
     *
     * @param state
     */
    public void setConnected(final boolean state) {

        // This changes the flags on the MuckMain menu to 'connected'
        // Connect to MU*
        cTM.setEnabled(true);

        // Disconnect from MU*
        dFM.setEnabled(state);

        // Reconnect to MU*
        rTM.setEnabled(!state);

    }

    /**
     *
     * @return
     */
    public boolean isAutoFocus() {

        return tWAutoFocus.getState();

    }

    /**
     *
     * @return
     */
    public boolean isReleasePause() {

        return tWReleasePauseItem.getState();

    }

    /**
     * Identifies whether the menu item indicates if the frames should be
     * synchronized
     *
     * @return <code>true</code> frames are synchronized <code>false</code>
     * frames are not synchronized
     */
    public boolean isSyncWindows() {
        return splitFramesItem.getState();
    }

    /**
     * Set whether the menu item for the menu should be synchronized or not
     *
     * @param state <code>true</code> - frames are to be synchronized
     * <code>false</code> - frames are to not be synchronized
     */
    public void setSyncWindows(final boolean state) {
        splitFramesItem.setState(state);
    }

}
