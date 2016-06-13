/**
 * CHandler for JamochaMUD will act as our "proxy" between the core of
 * JamochaMUD and our different sockets, if we have simultaneous connections to
 * other MU*s $Id: CHandler.java,v 1.49 2015/08/30 22:43:32 jeffnik Exp $
 */

/* JamochaMUD, a Muck/Mud client program
 * Copyright (C) 1998-2014 Jeff Robinson
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

import anecho.JamochaMUD.TinyFugue.TFCommandParser;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import java.util.Vector;

import anecho.gui.JMText;
import anecho.gui.JMSwingText;
import anecho.JamochaMUD.legacy.MuckConnAWT;
import net.sf.wraplog.AbstractLogger;
import net.sf.wraplog.NoneLogger;
import net.sf.wraplog.SystemLogger;

/**
 * To allow an easier time of dealing with multiple connections, any input or
 * output from JamochaMUD will pass through the CHandler class. This class will
 * then determine which MU* the outbound traffic will go out (eg. the
 * &quot;active" MU* if from the DataIn window) or the appropriate window to
 * send incoming traffic based on the socket it is received from. CHandler now
 * extends the Container class so that it can act as it's own autonomous
 * "widget" coordinating views of MU*s and their actual connections.
 *
 * CHandler will &quot;hold" the actual MuSockets but for considering which
 * connection is visible/active, we will always ask our display object, be it
 * the JMTextPanel or the JTabbedPane.
 *
 * @version $Id: CHandler.java,v 1.49 2015/08/30 22:43:32 jeffnik Exp $
 */
public final class CHandler {

    /**
     * A list of our active sockets/MUs
     */
    final transient private Vector connections;
    /**
     * A pointer to our settings class. This probably isn't required anymore.
     * Fix Me XXX
     */
    final transient private JMConfig settings;          // a pointer to our settings
    /**
     * Whether we are using Swing or not
     */
    final transient private boolean useSwing;
    /**
     * A variable used to enable/disable debugging
     */
    private static final boolean DEBUG = false;
    /**
     * The private instance of this class for our use as a Singleton
     */
    private static CHandler _instance;

    private final AbstractLogger logger;

    /**
     * Our constructor. Since this should only be called once per invocation of
     * JamochaMUD, we will set up the &quot;frames" for our MU* client.
     */
    // public CHandler() {
    private CHandler() {

        settings = JMConfig.getInstance();

        connections = new Vector(0, 1);

        if (DEBUG) {
            logger = new SystemLogger();
        } else {
            logger = new NoneLogger();
        }

        useSwing = settings.getJMboolean(JMConfig.USESWING);

//        if (settings.getJMboolean(JMConfig.USESWING)) {
//            useSwing = true;
//        } else {
//            useSwing = false;
//        }
    }

    /**
     * Returns an instance of this class if it has already been instantiated. If
     * this class has not yet been instantiated, a new instance will be created.
     * This is to ensure that there is only one instance of this class occuring.
     *
     * @return returns the current instance of this class.
     */
    public static CHandler getInstance() {
        if (_instance == null) {
            _instance = new CHandler();
        }

        return _instance;
    }

    /**
     * Return the layout object that contains our MU* views
     *
     * @return Returns a component that houses JamochaMUD's output display.
     * @deprecated This method has been moved to MuckMain and broken into three
     * separate methods: getTextPanel(), getFancyTextPanel(), and
     * getLegacyTextPanel()
     */
    public Component getTextObject() {

        Component retComp = (Component) null;

        if (useSwing) {
            retComp = MuckMain.getInstance().getFancyTextPanel();
        } else {
            retComp = MuckMain.getInstance().getLegacyTextPanel();
        }

        return retComp;
    }

    /**
     * Sets the active MU* (the MU* visible to the user) based on the index
     * number provided.
     *
     * @param target The index number representing which MU* should be set
     * active.
     */
    public void setActiveMU(final int target) {

        logger.debug("CHandler.setActiveMU: " + target);

        if (target >= 0) {
            if (useSwing) {
                final anecho.gui.JMFancyTabbedPane tempPanel = (anecho.gui.JMFancyTabbedPane) getTextObject();

                try {
                    tempPanel.setSelectedIndex(target);  // Fix me!!
                } catch (Exception except) {
                    logger.debug("CHandler.setActiveMU exception: " + except);
                    return;
                }

            } else {
                final anecho.JamochaMUD.legacy.JMTabPanel tempPanel = (anecho.JamochaMUD.legacy.JMTabPanel) getTextObject();

                logger.debug("CHandler.setActiveMU() going to setSelectedIndex.");

                tempPanel.setSelectedIndex(target);  // Fix me!

                logger.debug("CHandler.setActiveMU() invalidating.");

                tempPanel.invalidate();

                logger.debug("CHandler.setActiveMU() validating.");

                tempPanel.validate();

                logger.debug("CHandler.setActiveMU() doLayout.");

                tempPanel.doLayout();

            }

            // Run through our list of MU*s and tell them whether they are
            // active or inactive  Fix Me XXX
            MuSocket tempMU;
            final int conns = connections.size();
            for (int i = 0; i < conns; i++) {
                tempMU = this.getMUHandle(i);
                if (i == target) {
                    tempMU.setActiveMU(true);

                    logger.debug("CHandler.setActiveMU(): " + tempMU.getMUName() + " set to true");

                } else {
                    tempMU.setActiveMU(false);
                    logger.debug("CHandler.setActiveMU(): " + tempMU.getMUName() + " set to false");

                }
            }

            logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("CHandler.setActiveMU()_complete."));

        } else {
            logger.debug("CHandler.setActiveMU() Skipped checking.");

        }

    }

    /**
     * This method displays the last MU* connected to that is still open.
     */
    public void showLatestMU() {

        if (connections.size() < 1) {
            // There are no windows left to show.
            logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("CHandler:_No_(additional)_connections_left_to_show."));
            return;
        }

logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Chandler.showLatestMU()"));
        
        if (useSwing) {
            final anecho.gui.JMFancyTabbedPane tempPanel = (anecho.gui.JMFancyTabbedPane) getTextObject();
            // tempPanel.setSelectedIndex(tempPanel.getTabCount() - 1);
            this.setActiveMU(tempPanel.getTabCount() - 1);
            tempPanel.invalidate();
            tempPanel.validate();
            tempPanel.doLayout();
        } else {
            final anecho.JamochaMUD.legacy.JMTabPanel tempPanel = (anecho.JamochaMUD.legacy.JMTabPanel) getTextObject();
            tempPanel.last();
            tempPanel.invalidate();
            tempPanel.validate();
            tempPanel.doLayout();
        }

    }

    /**
     * Add the text display component owned by the given MuSocket to our display
     * object. Once the component has been added, we set JamochaMUD to show it.
     *
     * @param name The human-readable name of the MU
     *
     * @param address The numeric or human-readable address for the MU* server
     * @param port The MU* server port to connect to
     * @param msock The MuSocket which is responsible for communicating between
     * JamochaMUD the new MU*.
     */
    // public synchronized void addNewMU(final String name, final String address, final int port, final MuSocket msock) {
    public synchronized void addNewMU(final String name, final String address, final int port, final MuSocket msock) {
            logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("CHandler.addNewMU_Adding_") + name + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("_tab."));
            logger.debug("CHandler.addNewMU() name: " + name);
            logger.debug("CHandler.addNewMU() address: " + address);
            logger.debug("CHandler.addNewMU() port: " + port);
            logger.debug("CHandler.addNewMU() MuSocket: " + msock);

        if (useSwing) {

            final JMSwingText tempText = msock.getSwingTextWindow();
            // final anecho.gui.JMFancyTabbedPane tempPane = (anecho.gui.JMFancyTabbedPane) getTextObject();
            final anecho.gui.JMFancyTabbedPane tempPane = MuckMain.getInstance().getFancyTextPanel();

logger.debug("CHandler.addNewMU() using Swing has tempText: " + tempText);
            
            tempPane.addTab(name, tempText);

            // Try forcing a standard palette load.  Otherwise colours do not seem to work.
            // We should try and move this to the JMSwingText component somehow.  Fix Me XXX
            tempText.setStandardPalette();

        } else {
            final JMText tempText = msock.getTextWindow();

            if (DEBUG) {
                logger.debug("CHandler.addNewMU() not using Swing has tempText: " + tempText);

                if (tempText == null) {
                    logger.debug("tempText is considered null.");
                }
            }

            final anecho.JamochaMUD.legacy.JMTabPanel tempTextObject = (anecho.JamochaMUD.legacy.JMTabPanel) this.getTextObject();
            // ((anecho.JamochaMUD.legacy.JMTabPanel) getTextObject()).addTab(name, tempText);
            tempTextObject.addTab(
                    name,
                    tempText);

        }

logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("showLatestMU."));

        updateConnectionList(msock);

        showLatestMU();
    }

    /**
     * @deprecated - The new function should pass a SimpleWorld object Create a
     * new MuSocket that will be set-up with the supplied information and start
     * its thread. Return the given MuSocket to the calling method. AddNewMU is
     * normally called after this if this is a brand new connection.
     * @return Returns a new MuSocket object that will be responsible for this
     * connection.
     * @param ssl Returns whether the connection is to be encrypted (SSL) or
     * plain text. <CODE>true</CODE> - encrypted connection <CODE>false</CODE> -
     * plain text connection
     * @param name The human-readable name for the new MU
     *
     * @param address The human-readable or numeric address for the MU* server
     * @param port The MU* server port to connect to
     * @param charType The type of character connetion (eg. character or puppet)
     */
    public MuSocket openSocket(final String name, final String address, final int port, final boolean ssl, final int charType) {

        final SimpleWorld connWorld = new SimpleWorld();

        // create a new MuSocket, and then populate it with the proper info
logger.debug("MuSocket openSocket(name, address, etc.) is deprecated.");
        
        // final MuSocket msock = new MuSocket();
//        final MuSocket msock;
//        if (charType == 0) {
//            msock = new MuSocket();
//        } else {
//            msock = new PuppetSocket();
//        }
//        msock.setMUName(name);
//        msock.setAddress(address);
//        msock.setPort(port);
//        msock.setSSL(ssl);
//
//        // We'll set a timestamp to this MU* to differentiate incase the
//        // user hooks up to the same MU* multiple times.
//        msock.setTimeStamp(System.currentTimeMillis());
        connWorld.setWorldName(name);
        connWorld.setWorldAddress(address);
        connWorld.setWorldPort(port);
        connWorld.setSSL(ssl);
        // Set our logging option
//        msock.setLogging(settings.getJMboolean(JMConfig.AUTOLOGGING));
//
//        // Now add the new Socket to the connections vector
//        updateConnectionList(msock);
//
//        // Now we'll start our socket-listening thread
//        /msock.start();

        // Update the connection menu on the main program?
        // Fix me XXX!
        // System.out.println("CHandler.openSocket returns: " + msock);
        // return msock;
        return openSocket(connWorld, charType);

    }

    /**
     * Create a new MuSocket that will be set-up with the supplied information
     * and start its thread. Return the given MuSocket to the calling method.
     * AddNewMU is normally called after this if this is a brand new connection.
     *
     * @return Returns a new MuSocket object that will be responsible for this
     * connection.
     * @param connWorld
     * @param charType The type of character connection (eg. character or
     * puppet)
     */
    public MuSocket openSocket(final SimpleWorld connWorld, final int charType) {

        logger.debug("CHandler.openSocket entered.");

        final MuSocket msock;
        if (charType == 0) {
            // msock = new MuSocket();
            msock = new MuSocket(connWorld);
        } else {
            // Does PuppetSocket need to be modified as well?  Fix me XXX
            msock = new PuppetSocket();
        }

        msock.setMUName(connWorld.getWorldName());
        msock.setAddress(connWorld.getWorldAddress());
        msock.setPort(connWorld.getWorldPort());
        msock.setSSL(connWorld.isSSL());

        // Set the code-page if it is different
        if (connWorld.isOverrideCodepage()) {
            logger.debug("CHandler.openSocket setting codepage.");
            
            msock.setCodePage(connWorld.getCodePage());
        }
        // We'll set a timestamp to this MU* to differentiate incase the
        // user hooks up to the same MU* multiple times.
        msock.setTimeStamp(System.currentTimeMillis());

        // Set our logging option
        msock.setLogging(settings.getJMboolean(JMConfig.AUTOLOGGING));

        // Now add the new Socket to the connections vector
        // connections.addElement(msock);
        updateConnectionList(msock);

        // Set the menu to be disconnected for a clean start
        logger.debug("CHandler.openSocket(World, int) setting menu to disconnected.");

        MuckMain window = MuckMain.getInstance();
        window.disconnectMenu();

        logger.debug("CHandler.openSocket(final SimpleWorld connWorld, final int charType): Starting the MuSocket thread.");
        // Now we'll start our socket-listening thread
        msock.start();

        logger.debug("CHandler.openSocket msock.start has been called.");
        // Update the connection menu on the main program?
        // Fix me XXX!
        logger.debug("CHandler.openSocket returns: " + msock);

        return msock;

    }

    /**
     *
     * @param msock
     */
    private synchronized void updateConnectionList(final MuSocket msock) {

        // If our connection list does not have this MU*
        // we'll add it to our list
        if (!connections.contains(msock)) {
            connections.addElement(msock);
        }
    }

    /**
     * Make the next MU* in the list the active MU*
     *
     * The process that makes the call to this method is responsible for
     * updating the display appropriately
     *
     */
    public void nextMU() {

        if (useSwing) {
            final anecho.gui.JMFancyTabbedPane tempPanel = (anecho.gui.JMFancyTabbedPane) getTextObject();
            int muNum = tempPanel.getSelectedIndex();
            muNum++;

            if (muNum > (tempPanel.getTabCount() - 1)) {
                muNum = 0;
            }

            tempPanel.setSelectedIndex(muNum);

        } else {

            final anecho.JamochaMUD.legacy.JMTabPanel tempPanel = (anecho.JamochaMUD.legacy.JMTabPanel) getTextObject();

            tempPanel.next();

        }

    }

    /**
     * Make the previous MU* in the list the active MU*.
     *
     * As with nextMU, the calling process is responsible
     *
     * for updating the display appropriately.
     *
     */
    public void previousMU() {

        if (useSwing) {
            final anecho.gui.JMFancyTabbedPane tempPanel = (anecho.gui.JMFancyTabbedPane) getTextObject();
            int muNum = tempPanel.getSelectedIndex();

            muNum--;

            if (muNum < 0) {
                muNum = tempPanel.getTabCount() - 1;
            }

            tempPanel.setSelectedIndex(muNum);

        } else {
            final anecho.JamochaMUD.legacy.JMTabPanel tempPanel = (anecho.JamochaMUD.legacy.JMTabPanel) getTextObject();
            tempPanel.previous();
        }

    }

    /**
     * Close the connection to the active MU*
     *
     * @param stamp Timestamp of MU* to be closed (as confirmation)
     *
     */
    public void closeActiveMU(final String stamp) {
        final MuSocket active = getActiveMUHandle();

        closeSocket(active);
    }

    /**
     *
     * Remove the active MU* from our list altogether
     *
     * @param stamp This is the time stamp used to verify that we are closing
     * the proper MU*.
     *
     */
    public void removeActiveMU(final String stamp) {

        // Disconnect the MU* if still connected
        try {
            if (isActiveMUDConnected()) {
                closeActiveMU(stamp);
            }
        } catch (Exception exc) {
            logger.debug("CHandler.removeActiveMU() exception " + exc);
        }

        // "destroy" any physical entities
        // uhm... somehow.  We could rely on garbage collection but
        // I'd feel better actually knowing it was gone.
        // Fix this XXX
        // remove the MU* from our hashtable
        final int aMU = getActiveMUIndex();

        connections.removeElementAt(aMU);

        // Update the active MU*
        final int index = getActiveMUIndex();
        setActiveMU(index);

    }

    /**
     * Remove the MU* indicated by the MuSocket and timeStamp
     *
     * @param mSock The MuSocket to close and remove
     */
    public void removeMU(final MuSocket mSock) {

        if (mSock == null) {
            return;
        }

        if (mSock.getCharacterType() == 1) {
            // This MuSocket is infact a PuppetSocket, so we'll need to remove
            // it from it's parents list of puppets
            final String pupName = ((PuppetSocket) mSock).getPuppetName();
            final MuSocket parSock = ((PuppetSocket) mSock).getParentSocket();
            parSock.removePuppet(pupName);
        }

        if (mSock.isConnectionActive()) {
logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("CHandler.removeMU():_Disconnecting_active_socket"));
            
            closeSocket(mSock);
        }

        int place;
        place = connections.indexOf(mSock);

        // Remove this from out layout as well
        if (useSwing) {
            final JMSwingText tempText = mSock.getSwingTextWindow();

            // final int index = ((anecho.gui.JMFancyTabbedPane) getTextObject()).indexOfComponent(tempText);
            final int index = MuckMain.getInstance().getFancyTextPanel().indexOfComponent(tempText);

logger.debug("CHandler.removeMU(): The index of our component is: " + index);
            

logger.debug("CHandler.removeMU(MuSocket) removing object at index " + index);
            
            // ((anecho.gui.JMFancyTabbedPane) getTextObject()).removeTabAt(index);
            MuckMain.getInstance().getFancyTextPanel().removeTabAt(index);

        } else {

            final JMText tempText = mSock.getTextWindow();

            ((anecho.JamochaMUD.legacy.JMTabPanel) getTextObject()).remove(tempText, place);

        }

logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("CHandler.removeMU():_Component_removed"));
        
        connections.removeElement(mSock);

logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("CHandler.removeMU():_Completed_successfully"));
        

        // Update the active MU*
        final int index = getActiveMUIndex();
        setActiveMU(index);
    }

    /**
     *
     * Close and &quot;destroy" an existing socket. This allows any of our
     * clases to shut things down
     *
     * @param socket The socket to be closed.
     *
     */
    private void closeSocket(final MuSocket socket) {

        // Clean up the existing socket
        if (socket != null) {
            socket.closeSocket();
        }

    }

    /**
     * Return the 'address' for the active JMText object
     *
     * @return Returns a variable representing the active MU*
     */
    public JMText getActiveMUDText() {

        final int aMU = getActiveMUIndex();

        final MuSocket msock;

        if (aMU > 0) {
            msock = (MuSocket) connections.elementAt(aMU);
        } else {
            // Return a blank (null) socket
            msock = new MuSocket();
        }

        return msock.getTextWindow();

    }

    /**
     *
     * This returns the active JMSwingText
     *
     * @return JMSwingText of the active MU*
     *
     */
    public JMSwingText getActiveMUDSwingText() {

        JMSwingText retText;

        // if (connections.size() > 0) {
        if (connections.isEmpty()) {
            retText = (JMSwingText) null;
        } else {
            final int aMU = getActiveMUIndex();
            final MuSocket msock = (MuSocket) connections.elementAt(aMU);
            // return msock.getSwingTextWindow();
            retText = msock.getSwingTextWindow();

        }

        return retText;

    }

    /**
     *
     * Return the active JMText component, verified with the passed timestamp
     *
     * @param stamp A timestamp to verify the component returned
     *
     * @return Returns a JMText component of the active MU*
     *
     */
    public JMText getActiveMUDText(final String stamp) {

        final int aMU = getActiveMUIndex();

        final MuSocket msock = (MuSocket) connections.elementAt(aMU);

        return msock.getTextWindow();

    }

    /**
     *
     * Return the index of the active MU
     *
     * @return Returns the index number of the active MU*
     *
     */
    public int getActiveMUIndex() {

        int index = 0;

        // With the new MuckMain method do we need to check for Swing?
        // Fix Me XXX
        if (useSwing) {
            // index = ((anecho.gui.JMFancyTabbedPane) getTextObject()).getSelectedIndex();
            index = MuckMain.getInstance().getFancyTextPanel().getSelectedIndex();

logger.debug("CHandler.getActiveMUIndex returns: " + index);
             logger.debug("CHandler.getActiveMUIndex has a total of " + connections.size() + " connections");
            
        } else {
            //index = ((anecho.JamochaMUD.legacy.JMTabPanel) getTextObject()).getSelectedIndex();
            index = MuckMain.getInstance().getLegacyTextPanel().getSelectedIndex();
        }

        return index;
    }

    /**
     *
     * Return the title of the MU based on the given index
     *
     * @param index Index representing the MU* we are to retrieve the title of.
     *
     * @return The title of the MU* represented by the given {@link index}
     *
     */
    public String getTitle(final int index) {

        String title;

        if (index < 0 || index > (connections.size() - 1)) {

logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("CHandler.getTitle_out_of_range."));

            title = "";

        } else {

            title = ((MuSocket) connections.elementAt(index)).getTitle();

        }

        return title;

    }

    /**
     *
     * Return the proper MuSocket for the requested mu
     *
     * @param muNum This variable represents the index of the MU* to retrieve
     *
     * @return MuSocket representing the MU* specified by our {@link muNum}
     *
     */
    // public synchronized MuSocket getMUHandle(final int muNum) {
    public MuSocket getMUHandle(final int muNum) {

        MuSocket retSock;

        if (muNum < connections.size() && muNum > -1) {

            retSock = (MuSocket) connections.elementAt(muNum);

        } else {

            retSock = (MuSocket) null;

        }

        return retSock;

    }

    /**
     *
     * Return the MuSocket for the currently &quot;active" MU*
     *
     * @return The MuSocket of the currently active MU*
     *
     */
    public synchronized MuSocket getActiveMUHandle() {

        MuSocket retSock;

        // Check to see if we have any connections yet
        // if (connections.size() > 0) {
//        if (!connections.isEmpty()) {
//            int aMU = 0;
//            if (useSwing) {
//                aMU = ((anecho.gui.JMFancyTabbedPane) getTextObject()).getSelectedIndex();
//            } else {
//                aMU = ((anecho.JamochaMUD.legacy.JMTabPanel) getTextObject()).getSelectedIndex();
//            }
//
//            retSock = (MuSocket) connections.elementAt(aMU);
//        } else {
//            retSock = (MuSocket) null;
//        }
        if (connections.isEmpty()) {
            retSock = (MuSocket) null;
        } else {
            int aMU = 0;
            if (useSwing) {
                aMU = ((anecho.gui.JMFancyTabbedPane) getTextObject()).getSelectedIndex();
            } else {
                aMU = ((anecho.JamochaMUD.legacy.JMTabPanel) getTextObject()).getSelectedIndex();
            }

            retSock = (MuSocket) connections.elementAt(aMU);

        }

        return retSock;

    }

    /**
     *
     * Return the connection status of the active MU*
     *
     * @return <CODE>true</CODE> - The active MU* is connected to a server
     *
     * <CODE>false</CODE> - The active MU* is not connected to a server
     * @throws java.lang.Exception if there are no current connections
     *
     */
    // public synchronized boolean isActiveMUDConnected() {
    public boolean isActiveMUDConnected() throws Exception {

        boolean active = false;

        if (connections.size() < 1) {
            throw new Exception("CHandler.isActiveMUDConnected has no connections.");
        }

        int aMU = 0;

        if (useSwing) {
            aMU = ((anecho.gui.JMFancyTabbedPane) getTextObject()).getSelectedIndex();
        } else {
            aMU = ((anecho.JamochaMUD.legacy.JMTabPanel) getTextObject()).getSelectedIndex();
        }

        if (aMU > connections.size() - 1) {

            logger.debug("CHandler.isActiveMUConnected()");
            logger.debug("aMU: " + aMU + " and our connection size: " + connections.size());

        } else {
            if (aMU >= 0 && aMU < connections.size()) {
                final MuSocket msock = (MuSocket) connections.elementAt(aMU);
                active = msock.isConnectionActive();
            }

        }

        return active;

    }

    /**
     * Send the text to the currently active MU*
     *
     * @param send
     */
    public void sendText(final String send) {
        int aMU = 0;

        if (useSwing) {
            // aMU = ((anecho.gui.JMFancyTabbedPane) getTextObject()).getSelectedIndex();
            aMU = MuckMain.getInstance().getFancyTextPanel().getSelectedIndex();

            logger.debug("CHandler.sendText() Swing tabCount is " + MuckMain.getInstance().getFancyTextPanel().getTabCount());

        } else {
            // aMU = ((anecho.JamochaMUD.legacy.JMTabPanel) getTextObject()).getSelectedIndex();
            aMU = MuckMain.getInstance().getLegacyTextPanel().getSelectedIndex();
        }

        if (aMU > -1 && aMU < connections.size()) {

            final MuSocket msock = (MuSocket) connections.elementAt(aMU);
            sendText(send, msock);
        } else {

            logger.debug("CHandler.sendText() cannot send text as aMU " + aMU + " is not within the size of our connections " + connections.size());

            // This is important enought that we want to tell the user
            if (useSwing) {
                javax.swing.JOptionPane.showMessageDialog(settings.getJMFrame(JMConfig.MAINWINDOW), "CHandler.sendText() cannot send text as aMU " + aMU + " is not within the size of our connections " + connections.size() + "\nYou should e-mail this details of this to the JamochaMUD author.");
            }
        }

    }

    /**
     *
     * Send a string to the supplied MU*, using the proper encoding method
     * (ASCII or Unicode)
     *
     *
     * @param inSend
     * @param msock
     *
     */
    public void sendText(final String inSend, final MuSocket msock) {

        String send = inSend;

        final TFCommandParser tfCommand = TFCommandParser.getInstance();

        // Section moved from DataIn - do some pre-processing of the input
        if (!(send.length() > 0 && send.charAt(0) == '/' && settings.getJMboolean(JMConfig.TFKEYEMU) && tfCommand.command(send))) {

            logger.debug("CHandler.sendText sending out " + send);

            send = translateMacros(send);
            // End of section moved from DataIn

        } else {
            // We'll see if this stops local commands from being sent to the MU*
            // In the DataIn class we simple changed "send" to an empty string.
            // This may be important to send an "ENTER" along to a MU*
            logger.debug("CHandler.sendText changing output to empty");

            send = "";
        }

        if (msock != null) {

            if (msock.getCharacterType() == 0) {
                // The view is an actual MU*, so send it to a MuSocket
                logger.debug("CHandler.Sending to a MuSocket.");

                msock.sendText(send);
            } else {
                // The view is actually a PuppetSocket, so cast it first
                logger.debug("CHandler.Sending to a PuppetSocket.");

                ((PuppetSocket) msock).sendText(send);
            }

        } else {

            logger.debug("CHandler.sendText(String, MuSocket) cannot send text as the MuSocket is null");

            // This is important enought that we want to tell the user
            if (useSwing) {
                javax.swing.JOptionPane.showMessageDialog(settings.getJMFrame(JMConfig.MAINWINDOW), "CHandler.sendText(String, MuSocket) cannot send text as the MuSocket is null.");
            }
        }

    }

    /**
     *
     * Change the font face and size on all the connections
     *
     * @param newPal This array represents the colour palette that is to be used
     * @param fontFace The Font to use for the display.
     * @param fgColour The foreground colour to use
     * @param bgColour The background colour to use
     */
    public void setAllAttribs(final Font fontFace, final Color fgColour, final Color bgColour, final Color[] newPal) {
        MuSocket msock;       // our temporary socket
//        JMText text;          // our temporary text object
//        final JMSwingText sText;

        final int total = connections.size();

        if (total < 1) {
            // There are no active MU*s
            return;
        }

        for (int i = 0; i < total; i++) {
            // Loop through our connections and change the fonts
            msock = (MuSocket) connections.elementAt(i);

            if (useSwing) {
                setSwingAttribs(msock, fontFace, fgColour, bgColour, newPal);
//                sText = msock.getSwingTextWindow();
//                if (fontFace != null) {
//                    sText.setFont(fontFace);
//                }
//
//                if (fgColour != null) {
//                    sText.setForeground(fgColour);
//                    sText.setBackground(bgColour);
//                }
//
//                if (newPal != null) {
//                    try {
//                        sText.setPalette(newPal);
//                    } catch (Exception exc) {
//                        if (DEBUG) {
//                            System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Exception_settings_new_custom_palette_from_CHandler."));
//                        }
//
//                    }
//
//                }

            } else {
                setAttribs(msock, fontFace, fgColour, bgColour);
//                text = msock.getTextWindow();
//
//                if (fontFace != null) {
//                    text.setFont(fontFace);
//                }
//
//                if (fgColour != null) {
//                    text.setForeground(fgColour);
//                    text.setBackground(bgColour);
//                }

            }

        }

    }

    /**
     * Set the attributes for the given MuSocket
     *
     * @param msock The MuSocket that is to have its attributes changed
     * @param fontFace The new Font to use for the given MuSocket
     * @param fgColour The new foreground colour to use for the given MuSocket
     * @param bgColour The new background colour to use for the given MuSocket
     */
    private void setAttribs(final MuSocket msock, final Font fontFace, final Color fgColour, final Color bgColour) {
        JMText text;          // our temporary text object

        text = msock.getTextWindow();

        if (fontFace != null) {
            text.setFont(fontFace);
        }

        if (fgColour != null) {
            text.setForeground(fgColour);
            text.setBackground(bgColour);
        }

    }

    /**
     *
     * @param msock
     * @param fontFace
     * @param fgColour
     * @param bgColour
     * @param newPal
     */
    private void setSwingAttribs(final MuSocket msock, final Font fontFace, final Color fgColour, final Color bgColour, final Color[] newPal) {
        JMSwingText sText;

        sText = msock.getSwingTextWindow();
        if (fontFace != null) {
            sText.setFont(fontFace);
        }

        if (fgColour != null) {
            sText.setForeground(fgColour);
            sText.setBackground(bgColour);
        }

        if (newPal != null) {
            try {
                sText.setPalette(newPal);
            } catch (Exception exc) {
                logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Exception_settings_new_custom_palette_from_CHandler."));
            }

        }

    }

    /**
     *
     * Activate or deactivate logging in all connections
     *
     * @param state <CODE>true</CODE> - enable logging
     *
     * <CODE>false</CODE> - disable logging
     *
     */
    public void setLogging(final boolean state) {

        MuSocket msock;       // our temporary socket

        final int total = connections.size();

        if (total < 1) {

            // There are no active MU*s
            return;

        }

        for (int i = 0; i < total; i++) {

            // Loop through our connections and change the fonts
            msock = (MuSocket) connections.elementAt(i);

            msock.setLogging(state);

        }

    }

    /**
     *
     * Set the foreground and background colours for all the MU*s.
     *
     * @param fgColour The foreground colour to use
     *
     * @param bgColour The background colour to use
     *
     */
    public void setAllColours(final Color fgColour, final Color bgColour) {

        setAllAttribs(null, fgColour, bgColour, null);

    }

    /**
     *
     * This method sets the custom colours to be used on all our MU* windows.
     *
     * @param newPal An array containing our new Colors.
     *
     */
    public void setCustomPalette(final Color[] newPal) {

        setAllAttribs(null, null, null, newPal);

    }

    /**
     *
     * Set the Font for all active MU*s
     *
     * @param newStyle The Font style to use
     *
     */
    public void setAllFonts(final Font newStyle) {

        setAllAttribs(newStyle, null, null, null);

    }

    /**
     * Connect to a MU* with the given connection information
     *
     * @param muName The human readable name of the MU* to connect to
     * @param address The URL or IP address of the MU* to connect to
     * @param port The port number of the MU* to connect to
     * @param charType The type of character (eg. character or puppet)
     * @param charName The name of the character
     */
    public synchronized void connectToNewMU(final String muName, final String address, final int port, final int charType, final String charName) {
        String tempName = muName;

        logger.debug("CHandler.connectToNewMU(String,int) called");

        // if (muName.equals("")) {
        if ("".equals(muName)) {
            // muName = "JamochaMUD";
            tempName = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("JAMOCHAMUD");
        }

        final SimpleWorld connWorld = new SimpleWorld(tempName, address, port, false);

        // openMUSocket(muName, address, port, false, null);
        // openMUSocket(tempName, address, port, false, "", null, charType, charName);
        this.openMUSocket(connWorld, charType, charName);
    }

    /**
     * Show MU* Connector and set-up a connection to a new MU*
     */
    public synchronized void connectToNewMU() {

        SimpleWorld connWorld;

        // Show our list of connections (the MuckConnector)
        logger.debug("CHandler.connectToNewMU() starting.");

        // Check first to make certain we don't already have a connection dialogue
        if (!settings.getJMboolean(JMConfig.MUCKCONNVISIBLE)) {
            logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("CHandler.connectToNewMU_creating_new_MuckConn."));

            // final String name, address;
            // int port;
            // final boolean ssl;
            // final String codePage;
            // String connStr = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("unset");
            String charName;
            int charType;

            if (settings.getJMboolean(JMConfig.USESWING)) {
                // Try using our new World Connector
                logger.debug("CHandler.connectToNewMU() calling AddEditWorld().");
                final AddEditWorld muChoice = new AddEditWorld();
                muChoice.setVisible(true);

                // Gather information about our chosen connection
                connWorld = muChoice.getSelectedSimpleWorld();

                // connStr = muChoice.getConnectionString();
                charType = muChoice.getCharacterType();
                charName = muChoice.getCharacterName();

            } else {
                final MuckConnAWT muChoice = new MuckConnAWT(settings);

                muChoice.setVisible(true);

                // Gather information about our chosen connection
                connWorld = new SimpleWorld();

                connWorld.setCodePage("");
                connWorld.setConnectionString(null);
                connWorld.setOverrideCodepage(false);
                connWorld.setSSL(false);
                connWorld.setWorldAddress(muChoice.getAddress());
                connWorld.setWorldName(muChoice.getName());
                connWorld.setWorldPort(muChoice.getPort());

                charType = 0;
                charName = null;
            }

            logger.debug("CHandler.connectToNewMU() now calling openMUSocket.");

            openMUSocket(connWorld, charType, charName);

        }

    }

    /**
     * Private method to open a new socket.
     */
    private void openMUSocket(final SimpleWorld openWorld, final int charType, final String charName) {

        logger.debug("CHandler.openMUSocket() called.");

        final String name = openWorld.getWorldName();
        final String address = openWorld.getWorldAddress();
        final int port = openWorld.getWorldPort();
        // final boolean ssl = openWorld.isSSL();
        final String codePage = openWorld.getCodePage();
        final String connStr = openWorld.getConnectionString();

        // Open a new socket if a MU* has been selected
        if (address != null && !(address.trim()).equals("")) {
            logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("CHandler.connectToNewMU_using_name:_") + name);

            final MuSocket msock = openSocket(openWorld, charType);

            if (connStr == null) {
                msock.setAutoConnect(false);
            } else {
                msock.setAutoConnect(true);
                msock.setConnectString(connStr);
            }

            msock.setCharacterName(charName);

            if (openWorld.isOverrideCodepage()) {
                logger.debug("CHandler.openMUSocket setting codepage to: " + codePage);

                msock.setCodePage(codePage);
            } else {
                logger.debug("CHandler.openMUSocket not changing default codepage.");

            }

            logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("CHandler.connectToNewMU_calling_addNewMU"));

            addNewMU(name, address, port, msock);

        } else {

            logger.debug("CHandler.connectToNewMU() didn't have a MU* to connect to.");

        }

        // Check to see if we need to make the main window visible
        // final JMConfig settings = JMConfig.getInstance();
        final java.awt.Frame mmFrame = MuckMain.getInstance().getMainFrame();
        if (!mmFrame.isVisible()) {
            mmFrame.setVisible(true);
        }

        settings.setJMValue(JMConfig.MUCKCONNVISIBLE, false);

        logger.debug("CHandler.openMUSocket() requesting focus");

        // Send the focus to our main program... just a nice thing to do
        mmFrame.requestFocus();

        // This has been moved the JMUD.java on 2010-11-13
//        final boolean isNew = settings.getJMboolean(JMConfig.ISNEW);
//        final boolean showNew = settings.getJMboolean(JMConfig.SHOWNEW);
//
//        if (DEBUG) {
//            System.err.println("CHandler.openMUSocket:");
//            System.err.println("isNew: " + isNew);
//            System.err.println("showNew: " + showNew);
//        }
//        if (useSwing && isNew && showNew) {
//            // Ask MuckMain to check the what's new box
//            // I'd like to move this check to MuckMain some how.  Fix Me XXX
//            final MuckMain muMain = MuckMain.getInstance();
//            muMain.showWhatsNew(false);
//            settings.setJMboolean(JMConfig.ISNEW, "false");
//        }
    }

    /**
     *
     * Get the total number of MU*s open regardless of their connection status
     *
     * @return The number of existing MuSockets
     *
     */
    // public synchronized int totalConnections() {
    public synchronized int totalConnections() {

        logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("CHandler.totalConnections():_entering_method"));

        int retSize = 0;

        logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("CHandler.totalConnections()_getting_connection_size."));

        // if (connections.size() > 0) {
        if (!connections.isEmpty()) {
            retSize = connections.size();
        }

        // public int totalConnections() {
        logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("CHandler.totalConnections()_returns:_") + retSize);

        // return connections.size();
        logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("CHandler.totalConnections():_leaving_method"));

        return retSize;

    }

    /**
     *
     * Determines if the given MU* is the currently active MU*.
     *
     * @return <CODE>true</CODE> - the provided MU* is the active MU*
     * <CODE>false</CODE> - the provided MU* is not the active MU
     *
     * @param mSock The MuSocket to check to see if it is current the active
     * MU*.
     *
     */
    public boolean isActiveMU(final MuSocket mSock) {

        boolean active = false;

        if (getActiveMUHandle().equals(mSock)) {
            active = true;
        }

        return active;

    }

    /**
     *
     * Return the title of the active MU*. The title may be different than the
     * actual MU* name depending on the status of the connection
     *
     * @return Return a String of the title of the active MU*.
     *
     */
    public synchronized String getActiveTitle() {

        String title;

        if (connections.size() < 1) {

            // We have no connections, thus no title
            title = "";

        } else {

            // MuSocket msock = (MuSocket)connections.elementAt(activeMU);
            // title = msock.getTitle();
            final int aMU = getActiveMUIndex();

            title = getTitle(aMU);

        }

        logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("getActiveTitle_returns:_") + title);

        return title;

    }

    /**
     *
     * Change the drawing type for the JMText... either
     *
     * single buffered (false) or double buffered (true)
     *
     * @param state <CODE>true</CODE> - set the display to double buffer
     *
     * <CODE>false</CODE> - set the display to work without buffering
     *
     */
    public void setDoubleBuffer(final boolean state) {

        MuSocket msock;

        for (int i = 0; i < connections.size(); i++) {

            msock = (MuSocket) connections.elementAt(i);

            if (msock != null) {
                (msock.getTextWindow()).setDoubleBuffer(state);
            }

        }

    }

    /**
     *
     * Change between aliased and antialised text in our
     *
     * output windows.
     *
     * @param state <CODE>true</CODE> - enable antialiasing
     *
     * <CODE>false</CODE> - disalbe antialiasing
     *
     */
    public void setAntiAliasing(final boolean state) {

        MuSocket msock;

        for (int i = 0; i < connections.size(); i++) {

            logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("CHandler.setAntiAliasing_setting_connection_") + i + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("_to_") + state);

            msock = (MuSocket) connections.elementAt(i);

            (msock.getSwingTextWindow()).setAntiAliasing(state);

        }

    }

    /**
     *
     * Set the MU* displays to operating in either "low colour" (8 colours,
     *
     * normal and bold) or "high colour" (16 colours).
     *
     * @param state <CODE>true</CODE> - set the display to low colour mode
     *
     * <CODE>false</CODE> - set display to high colour mode
     *
     */
    public void setLowColour(final boolean state) {

        MuSocket msock;

        for (int i = 0; i < connections.size(); i++) {
            logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("CHandler.setLowColour_setting_connection_") + i + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("_to_") + state);

            msock = (MuSocket) connections.elementAt(i);

            (msock.getSwingTextWindow()).setBoldNotBright(state);
        }

    }

    /**
     *
     * Return the ECHO state for the active MU*
     *
     * @return <CODE>true</CODE> - the active MU* echo mode is enabled
     *
     * <CODE>false</CODE> - the active MU* is not in echo mode
     *
     */
    public boolean isActiveMUEchoState() {

        final MuSocket active = getActiveMUHandle();
        boolean state = false;

        if (active != null) {
            state = active.isEchoState();
        }

        return state;

    }

    /**
     * Reconnect to the currently active (visible) MU*
     */
    public synchronized void reconnectToMU() {

        final MuSocket handle = getActiveMUHandle();

        if (handle == null) {

            logger.debug("CHandler.reconnectToMU() called but handle reads as null");

            // No MU*s have previously been connected to, so we can't reconnect
            return;
        }

        final String name = handle.getMUName();
        final String address = handle.getAddress();
        final int port = handle.getPort();
        // final boolean ssl = handle.isSSL();

        logger.debug("Chandler.reconnectToMU activeMU Handle is: " + handle);
        logger.debug("CHandler.reconnectToMU attempting connection to: " + name + " " + address + " " + port);
        logger.debug("CHandler.reconnectToMU calling OpenSocket");

        final SimpleWorld connWorld = handle.getWorld();

        // final MuSocket msock = openSocket(name, address, port);
        // We have hard-coded the character/MU type.  Fix Me XXX
        // final MuSocket msock = openSocket(name, address, port, ssl, 0);
        final MuSocket msock = openSocket(connWorld, 0);

        logger.debug("CHandler.reconnectToMU Adding the new MU*");

        addNewMU(name, address, port, msock);

        logger.debug("CHandler.reconnectToMU removing old MU*.");

        removeMU(handle);

        logger.debug("CHandler.reconnectToMU completed.");

    }

    /**
     * Return an array of all the MU*s. This can be handy if a change has to be
     * applied to all open connections
     *
     * @return returns a Vector containing all the current connections.
     */
    public Vector getAllMUs() {
        return connections;
    }

    /**
     * Do any required macro replacement in our string
     */
    private String translateMacros(final String macroIn) {

        String input = macroIn;

        boolean process = true;

        int start, end;

        String command, sString, eString, macro;

        /**
         * Create a loop to continuously process any (recursive) macros we find
         */
        // We need to be able to locate endless loops!  Fix this XXX
        while (process) {

            start = input.indexOf(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("${"));

            if (start > -1) {

                end = input.indexOf('}', start);

                if (end > -1 && end > start) {

                    command = input.substring(start + 2, end);

                    if (start > 0) {

                        sString = input.substring(0, start);

                    } else {

                        sString = "";

                    }

                    if (end + 1 < input.length()) {

                        eString = input.substring(end + 1);

                    } else {

                        eString = "";

                    }

                    macro = settings.getVariable(command);

                    if (macro == null) {

                        input = sString + command + eString;

                    } else {

                        input = sString + macro + eString;

                    }

                }

            } else {

                // No more macros to process
                process = false;

            }

        }

        return input;

    }
}
