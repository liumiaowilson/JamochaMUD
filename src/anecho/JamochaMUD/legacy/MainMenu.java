/** $Id: MainMenu.java,v 1.5 2015/06/11 01:57:52 jeffnik Exp $

 */

package anecho.JamochaMUD.legacy;



import java.awt.CheckboxMenuItem;

import java.awt.Frame;

import java.awt.MenuBar;

import java.awt.Menu;

import java.awt.MenuItem;

import java.awt.MenuShortcut;



import java.awt.event.KeyEvent;



// import anecho.gui.ResReader;

import anecho.JamochaMUD.*;



/** Create a menu for the Main Window */

public class MainMenu {

    

    private static MainMenu _instance;  // We'll try playing with a Singleton!

    

    private MenuBar tWMenuBar;

    private CheckboxMenuItem tWMacro, tWSyncWindowsItem, tWUseUnicodeItem, tWTFKeysItem, tWLocalEchoItem, tWDoubleBufferItem, splitFramesItem;

    private CheckboxMenuItem tWAutoFocus, tWTimers, tWReleasePauseItem, tWAltFocus;

    private Menu tWMUListMenu;

    private MenuItem cTM, dFM, rTM, closeMU;

    private MenuItem dumpOutputItem, quitItem;

    private MenuItem pingMUItem;

    private MenuItem copyItem, pasteItem, findItem;

    private MenuItem coloursItem, externalProgramsItem, managePlugIn;

    // private MenuItem serverOptionsItem;

    private MenuItem installPlugIn, removePlugIn;

    private MenuItem contentsItem;

    // private MenuItem troubleshootingItem;

    // private MenuItem aboutJamochaMUDItem;

    private JMConfig settings;

    protected static Menu tWPlugInMenu;

    

    // private static final int COMBINED = 0;     // Combined frame style

    // private static final int SPLIT = 1;        // Split frame style

    

    // private int viewStyle;

    

    private static final boolean DEBUG = false;

    

    private MainMenu() {

    }

    

    // For lazy initialization

    /**

     * 

     * @return 

     */

    public static synchronized MainMenu getInstance() {

        if (_instance==null) {

            _instance = new MainMenu();

        }

        return _instance;

    }

    

    /**

     * 

     * @param parent 

     * @param mainProg 

     */

    // We can probably remove JMConfig from the method arguments now that we've implemented the singleton
    public void buildMenu(final Frame parent, final MuckMain mainProg, final JMConfig oldsettings) {

        // this.settings = settings;
        this.settings = JMConfig.getInstance();

        tWMenuBar = new MenuBar();

        

        parent.setMenuBar(tWMenuBar);

        

        final Menu tWFileMenu = new Menu(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("file"));

        tWMenuBar.add(tWFileMenu);

        // tWFileMenu.add(dumpOutputItem = new MenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("dumpOutput"), new MenuShortcut(KeyEvent.VK_D, false)));

        tWFileMenu.add(dumpOutputItem = new MenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("dumpOutput"), new MenuShortcut(KeyEvent.VK_L, false)));

        dumpOutputItem.setActionCommand(dumpOutputItem.getLabel());

        

        tWFileMenu.addSeparator();

        tWFileMenu.add(quitItem = new MenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("quit"), new MenuShortcut(KeyEvent.VK_Q, true)));

        quitItem.setActionCommand(quitItem.getLabel());

        tWFileMenu.addActionListener(mainProg);

        

        // Add the Edit menu items

        final Menu tWEditMenu = new Menu(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("edit"));

        tWMenuBar.add(tWEditMenu);

        // I'm making it the right key for OS/2, damn't!

        // if (settings.getOSName().equals("OS/2")) {

        if ((settings.getJMString(JMConfig.OSNAME)).equals(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("OS/2"))) {

            // if (settings.getJMString(settings.OSNAME).equals("OS/2")) {

            copyItem = new MenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("copyFromMainWindow"), new MenuShortcut(KeyEvent.VK_INSERT, false));

        } else {

            copyItem = new MenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("copyFromMainWindow"), new MenuShortcut(KeyEvent.VK_C, false));

        }

        copyItem.setActionCommand(copyItem.getLabel());

        tWEditMenu.add(copyItem);

        

        pasteItem = new MenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("paste"), new MenuShortcut(KeyEvent.VK_V, false));

        pasteItem.setActionCommand(pasteItem.getLabel());

        tWEditMenu.add(pasteItem);

        

        tWEditMenu.addSeparator();

        

        findItem = new MenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("find"), new MenuShortcut(KeyEvent.VK_F, false));

        findItem.setActionCommand(findItem.getLabel());

        findItem.setEnabled(false);

        tWEditMenu.add(findItem);

        tWEditMenu.addActionListener(mainProg);

        

        

        // Add CONNECTION menu items

        final Menu tWConnectMenu = new Menu(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("connection"));

        tWMenuBar.add(tWConnectMenu);

        cTM = new MenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("connectToMU"), new MenuShortcut(KeyEvent.VK_C, true)); // enable/disable functionality

        cTM.setActionCommand(cTM.getLabel());

        tWConnectMenu.add(cTM);

        

        rTM = new MenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("reconnectToMU"), new MenuShortcut(KeyEvent.VK_R, true));

        rTM.setActionCommand(rTM.getLabel());

        tWConnectMenu.add(rTM);

        

        dFM = new MenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("disconnectFromMU"), new MenuShortcut(KeyEvent.VK_D, true));  // enable/disable functionality

        dFM.setActionCommand(dFM.getLabel());

        dFM.setEnabled(false);

        tWConnectMenu.add(dFM);

        

        closeMU = new MenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("closeThisView"));

        closeMU.setEnabled(false);

        tWConnectMenu.add(closeMU);

        

        tWConnectMenu.add(pingMUItem = new MenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("pingMU")));

        pingMUItem.setEnabled(false);

        tWConnectMenu.addActionListener(mainProg);

        

        final Menu tWOptionMenu = new Menu(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("options"));

        tWMenuBar.add(tWOptionMenu);

        

        // Add ****Edit Options**** Submenu

        final Menu tWEditOptionsMenu = new Menu(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("editOptions"));

        tWOptionMenu.add(tWEditOptionsMenu);

        tWEditOptionsMenu.add(externalProgramsItem = new MenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("externalPrograms"), new MenuShortcut(KeyEvent.VK_E, false)));

        externalProgramsItem.setActionCommand(externalProgramsItem.getLabel());

        // externalProgramsItem.setEnabled(false);

        

        tWEditOptionsMenu.add(managePlugIn = new MenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("ManagePlugins.title"), new MenuShortcut(KeyEvent.VK_M, false)));

        managePlugIn.setActionCommand(managePlugIn.getLabel());

        // tWEditOptionsMenu.add(serverOptionsItem = new MenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("serverOptions")));

        tWEditOptionsMenu.add(new MenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("serverOptions")));

        tWEditOptionsMenu.addActionListener(mainProg);

        

        installPlugIn = new MenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("installPlugin"));

        installPlugIn.setActionCommand(installPlugIn.getLabel());

        

        removePlugIn = new MenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("removePlugin"));

        removePlugIn.setEnabled(false);

        removePlugIn.setActionCommand(removePlugIn.getLabel());

        

        tWOptionMenu.add(coloursItem = new MenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("fontsAndColours"), new MenuShortcut(KeyEvent.VK_F, true)));

        coloursItem.setActionCommand(coloursItem.getLabel());

        

        // Continue with normal option menu

        tWOptionMenu.addSeparator();

        

        tWMacro = new CheckboxMenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("showMacroBar"), false);

        tWOptionMenu.add(tWMacro);

        tWMacro.addItemListener(mainProg);

        tWMacro.setEnabled(false);

        

        tWTimers = new CheckboxMenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("showTimers"), false);

        tWTimers.setShortcut(new MenuShortcut(KeyEvent.VK_T, false));

        tWTimers.setActionCommand(tWTimers.getLabel());

        tWOptionMenu.add(tWTimers);

        tWTimers.addItemListener(mainProg);

        tWTimers.addActionListener(mainProg);

        // Fix mainProg XXX!!  Right now we have no option for timers at all

        tWTimers.setEnabled(false);

        

        splitFramesItem = new CheckboxMenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("splitFrames"), false);

        tWOptionMenu.add(splitFramesItem);

        // if (settings.getSplitView()) {

//        if (settings.getJMboolean(JMConfig.SPLITVIEW)) {
//
//            // viewStyle = SPLIT;
//
//            splitFramesItem.setState(true);
//
//        } else {
//
//            // viewStyle = COMBINED;
//
//        }

        splitFramesItem.setState(settings.getJMboolean(JMConfig.SPLITVIEW));
        
        splitFramesItem.addItemListener(mainProg);

        

        tWSyncWindowsItem = new CheckboxMenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("syncWindows"), true);

        tWOptionMenu.add(tWSyncWindowsItem);

        tWSyncWindowsItem.addItemListener(mainProg);

        // tWSyncWindowsItem.setState(settings.getSyncWindows());

        tWSyncWindowsItem.setState(settings.getJMboolean(JMConfig.SYNCWINDOWS));

        

        tWOptionMenu.addSeparator();

        

        tWAutoFocus = new CheckboxMenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("autoFocusInput"), true);

        tWOptionMenu.add(tWAutoFocus);

        tWAutoFocus.addItemListener(mainProg);

        // tWAutoFocus.setState(settings.getAutoFocusInput());

        tWAutoFocus.setState(settings.getJMboolean(settings.AUTOFOCUSINPUT));

        

        tWAltFocus = new CheckboxMenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("altFocus"), true);

        tWOptionMenu.add(tWAltFocus);

        tWAltFocus.addItemListener(mainProg);

        // tWAltFocus.setState(settings.getAltFocus());

        tWAltFocus.setState(settings.getJMboolean(JMConfig.ALTFOCUS));

        if (!splitFramesItem.getState()) {

            tWAltFocus.setEnabled(false);

        }

        

        tWLocalEchoItem = new CheckboxMenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("localEcho"), true);

        tWOptionMenu.add(tWLocalEchoItem);

        tWLocalEchoItem.addItemListener(mainProg);

        // tWLocalEchoItem.setState(settings.isLocalEchoEnabled());

        tWLocalEchoItem.setState(settings.getJMboolean(JMConfig.LOCALECHO));

        

        tWDoubleBufferItem = new CheckboxMenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("doubleBuffer"), true);

        tWOptionMenu.add(tWDoubleBufferItem);

        tWDoubleBufferItem.addItemListener(mainProg);

        // tWDoubleBufferItem.setState(settings.getDoubleBuffer());

        tWDoubleBufferItem.setState(settings.getJMboolean(JMConfig.DOUBLEBUFFER));

        

        tWReleasePauseItem = new CheckboxMenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("releasePause"), true);

        tWOptionMenu.add(tWReleasePauseItem);

        tWReleasePauseItem.addItemListener(mainProg);

        // tWReleasePauseItem.setState(settings.getReleasePause());

        tWReleasePauseItem.setState(settings.getJMboolean(JMConfig.RELEASEPAUSE));

        

        tWTFKeysItem = new CheckboxMenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("tinyFugueKeys"), true);

        tWOptionMenu.add(tWTFKeysItem);

        tWTFKeysItem.addItemListener(mainProg);

        // tWTFKeysItem.setState(settings.getTFKeyEmu());

        tWTFKeysItem.setState(settings.getJMboolean(settings.TFKEYEMU));

        

        tWUseUnicodeItem = new CheckboxMenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("useUnicode"), false);

        tWOptionMenu.add(tWUseUnicodeItem);

        tWUseUnicodeItem.addItemListener(mainProg);

        // tWUseUnicodeItem.setState(settings.getUseUnicode());

        tWUseUnicodeItem.setState(settings.getJMboolean(settings.USEUNICODE));

        

        // Add the ActionListener to mainProg menu

        tWOptionMenu.addActionListener(mainProg);

        

        

        // Add the Plug-in menu items

        tWPlugInMenu = new Menu(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("plugIn"));

        tWMenuBar.add(tWPlugInMenu);

        tWPlugInMenu.addActionListener(mainProg);

        

        // List for our active MU*s

        tWMUListMenu = new Menu(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("MUList"));

        tWMenuBar.add(tWMUListMenu);

        tWMUListMenu.addActionListener(mainProg);

        

        //Add HELP menu items

        final Menu tWHelpMenu = new Menu(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("help"));

        tWMenuBar.setHelpMenu(tWHelpMenu);

        tWHelpMenu.add(contentsItem = new MenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("contents"), new MenuShortcut(KeyEvent.VK_H)));

        contentsItem.setActionCommand(contentsItem.getLabel());

        // tWHelpMenu.add(troubleshootingItem = new MenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("reportABug")));

        tWHelpMenu.add(new MenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("reportABug")));

        tWHelpMenu.add(new MenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("tinyFugueKeyCodes")));

        // tWHelpMenu.add(troubleshootingItem = new MenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("troubleshooting")));

        tWHelpMenu.addSeparator();

        // tWHelpMenu.add(aboutJamochaMUDItem = new MenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("aboutJamochaMUD")));

        tWHelpMenu.add(new MenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("aboutJamochaMUD")));

        tWHelpMenu.addActionListener(mainProg);

        

    }

    

    /**

     * 

     * @param state 

     */

    public synchronized void setCloseMUEnabled(final boolean state) {

        closeMU.setEnabled(state);

    }

    

    public void removeAllPlugins() {

        tWPlugInMenu.removeAll();

        // tWPlugInMenu.add(managePlugIn);

        tWPlugInMenu.add(installPlugIn);

        tWPlugInMenu.add(removePlugIn);

        tWPlugInMenu.addSeparator();

    }

    

    // public void addPlugin(String name, boolean state) {

    /**

     * 

     * @param plug 

     */

    public void addPlugin(final anecho.JamochaMUD.plugins.PlugInterface plug) {

        

        if (DEBUG) {

            System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("MainMenu_adding_plug-in:_") + plug.plugInName());

        }

        

        final String name = plug.plugInName();

        final boolean state = plug.isActive();

        final boolean properties = plug.hasProperties();

        

        final Menu tempName = new Menu(name);

        /*

        if (state) {

            tempName.setIcon(new ImageIcon(JMainMenu.class.getResource("icons/22/connect_established.png")));

        } else {

            tempName.setIcon(new ImageIcon(JMainMenu.class.getResource("icons/22/connect_no.png")));

        }

         */

        

        final MenuItem prop = new MenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("properties"));

        // prop.setIcon(new ImageIcon(JMainMenu.class.getResource("icons/22/configure.png")));

        

        if (!properties) {

            // No settings are available for this item

            prop.setEnabled(false);

        } else {

            prop.setActionCommand(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("plugin:") + name);

            // prop.addActionListener(settings.getMainWindowVariable());

            prop.addActionListener(MuckMain.getInstance());

        }

        

        final MenuItem action = new MenuItem();

        // Check to see if we should offer the enable or disable menu item

        if (state) {

            action.setLabel(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("disable"));

            // action.setIcon(new ImageIcon(JMainMenu.class.getResource("icons/22/button_cancel.png")));

            action.setActionCommand(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("disable:") + name);

        } else {

            action.setLabel(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("enable"));

            // action.setIcon(new ImageIcon(JMainMenu.class.getResource("icons/22/button_ok.png")));

            action.setActionCommand(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("enable:") + name);

        }

        

        // action.addActionListener(settings.getMainWindowVariable());

        action.addActionListener(MuckMain.getInstance());

        

        final MenuItem desc = new MenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("description"));

        desc.setActionCommand(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("description:") + name);

        // desc.addActionListener(settings.getMainWindowVariable());

        desc.addActionListener(MuckMain.getInstance());

        

        tempName.add(prop);

        tempName.add(action);

        tempName.addSeparator();

        tempName.add(desc);

        

        tWPlugInMenu.add(tempName);

        

        /*

        MenuItem tempName = new MenuItem(name);

        tempName.setEnabled(state);

        tempName.setActionCommand("plugin:" + name);

        // tWPlugInMenu.add(name);

        tWPlugInMenu.add(tempName);

         */

    }

    

    public void updateConnectionMenu() {

        // final MuckMain mainVar = settings.getMainWindowVariable();

        final MuckMain mainVar = MuckMain.getInstance();

        

        // First, check to see if we have any connections.

        // If not, then we don't need to do any more here

        final CHandler connHandler = settings.getConnectionHandler();

        if (connHandler.totalConnections() < 1) {

            return;

        }

        

        // Clear the current list of MU*'s

        tWMUListMenu.removeActionListener(mainVar);

        tWMUListMenu.removeAll();

        

        // Add the two generic entries... the next and previous choices

        StringBuffer tempName;

        MenuItem tempMenu;

        MenuShortcut shortCut;

        

        tempMenu = new MenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("previousMU"));

        shortCut = new MenuShortcut(KeyEvent.VK_PAGE_DOWN, false);

        tempMenu.setShortcut(shortCut);

        tempMenu.setActionCommand(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("previousMU"));

        if (connHandler.totalConnections() < 2) {

            tempMenu.setEnabled(false);

        }

        tWMUListMenu.add(tempMenu);

        

        tempMenu = new MenuItem(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("nextMU"));

        shortCut = new MenuShortcut(KeyEvent.VK_PAGE_UP, false);

        tempMenu.setShortcut(shortCut);

        tempMenu.setActionCommand(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("nextMU"));

        if (connHandler.totalConnections() < 2) {

            tempMenu.setEnabled(false);

        }

        tWMUListMenu.add(tempMenu);

        

        tWMUListMenu.addSeparator();

        

        final int totConn = connHandler.totalConnections() - 1;

        final int activeConnection = connHandler.getActiveMUIndex();

        

        // Loop through our connections and build a new menu

        for (int i = 0; i <= totConn; i++) {

            tempName = new StringBuffer(connHandler.getTitle(i));

            if (i == activeConnection) {

                // This is our active MU*

                // We'll just differentiate with a check beside it's name

                tWMUListMenu.add(new CheckboxMenuItem(tempName.toString(), true));

            } else {

                tempMenu = new MenuItem(tempName.toString());

                switch(i) {

                    case 0:

                        shortCut = new MenuShortcut(KeyEvent.VK_1, false);

                        break;

                    case 1:

                        shortCut = new MenuShortcut(KeyEvent.VK_2, false);

                        break;

                    case 2:

                        shortCut = new MenuShortcut(KeyEvent.VK_3, false);

                        break;

                    case 3:

                        shortCut = new MenuShortcut(KeyEvent.VK_4, false);

                        break;

                    case 4:

                        shortCut = new MenuShortcut(KeyEvent.VK_5, false);

                        break;

                    case 5:

                        shortCut = new MenuShortcut(KeyEvent.VK_6, false);

                        break;

                    case 6:

                        shortCut = new MenuShortcut(KeyEvent.VK_7, false);

                        break;

                    case 7:

                        shortCut = new MenuShortcut(KeyEvent.VK_8, false);

                        break;

                    case 8:

                        shortCut = new MenuShortcut(KeyEvent.VK_9, false);

                        break;

                    case 9:

                        shortCut = new MenuShortcut(KeyEvent.VK_0, false);

                        break;

                    default:

                        break;

                        

                }

                

                tempMenu.setShortcut(shortCut);

                

                tempMenu.setActionCommand(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("ChangeMU:") + i);

                tempMenu.addActionListener(mainVar);

                tWMUListMenu.add(tempMenu);

            }

        }

        

        tWMUListMenu.addActionListener(mainVar);

        

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

    

//    private String RB(final String itemTarget) {

//        return reader.langString(anecho.JamochaMUD.JMConfig.BUNDLEBASE, itemTarget);

//    }

    

    /**

     * Set the menus to reflect if we're connected (true) or not (false)

     * @param state 

     */

    public void setConnected(final boolean state) {

        // This changes the flags on the MuckMain menu to 'connected'

        // Connect to MU*

        // Shouldn't the connect to MU* menu always be available?

        // cTM.setEnabled(!state);

        // Disconnect from MU*

        dFM.setEnabled(state);

        // Reconnect to MU*

        rTM.setEnabled(!state);

    }

    

}