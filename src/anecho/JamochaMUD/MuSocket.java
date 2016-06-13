/**
 * A MuSocket is used to hold all information important to a single socket
 * connection.
 *
 * This class contains MXP parsing code supplied by Rick Robinson
 *
 * $Id: MuSocket.java,v 1.48 2012/02/25 02:42:41 jeffnik Exp $
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

import anecho.gui.JMText;
import anecho.gui.JMSwingText;

// import anecho.extranet.Socks5socket;
import anecho.extranet.MUDBufferedReader;
import anecho.extranet.event.TelnetEvent;
import anecho.extranet.event.TelnetEventListener;
import com.jmxp.MXPProcessor;
import com.jmxp.MXPResult;
import com.jmxp.libmxp;
import com.jmxp.structures.SendStruct;
import com.jmxp.structures.flagStruct;
import com.jmxp.structures.formatStruct;
import com.jmxp.structures.gaugeStruct;
import com.jmxp.structures.linkStruct;
import com.jmxp.structures.moveStruct;
import com.jmxp.structures.relocateStruct;
import com.jmxp.structures.soundStruct;
import com.jmxp.structures.statStruct;
import com.jmxp.structures.varStruct;
import com.jmxp.structures.windowStruct;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.io.DataOutputStream;
import java.io.IOException;

import java.net.ConnectException;
import java.net.InetAddress;

import java.net.NoRouteToHostException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.zip.Deflater;
import net.sf.wraplog.AbstractLogger;
import net.sf.wraplog.NoneLogger;
import net.sf.wraplog.SystemLogger;

/**
 *
 * @author jeffnik
 */
public class MuSocket extends Thread implements MouseListener, TelnetEventListener {

    /**
     * A time-stamp indicating when this connection was created
     */
    private long timeStamp;
    /**
     * Keep track of new activity if we're not the activeMU. Can the activity
     * and minLineCount variables be combined? Fix Me XXX
     */
    private transient int activity = 0;
    /**
     * the number of minimised lines
     */
    private transient int minLineCount = 0;
    /**
     * Is this connection the "active" one?
     */
    private transient boolean activeStatus = true;
    /**
     * The state of the connection
     */
    private transient boolean connected = false;
    /**
     * Has our output been paused?
     */
    private boolean paused;
    /**
     * MU*'retStr should always start with ECHO on
     */
    private transient boolean echo = true;
    /**
     * The text-area belonging to this connection
     */
    private transient JMText mainText;
    /**
     * The swing version of our text area
     */
    private transient JMSwingText mainSText;
    /**
     * The socket used for our connection
     */
    private transient Socket connSock;
    /**
     * input to us from the MU*
     */
    private transient MUDBufferedReader inStream;
    /**
     * for sending info to the MU*
     */
    private transient DataOutputStream outStream;
    /**
     * A vector to hold any text incase we are "paused"
     */
    private transient final Vector heldResponse;
    /**
     * Our connection handler. Should this now use the .getInstance(). Fix Me
     * XXX
     */
    private final transient CHandler connHandler;
    /**
     * Indicates whether this class should use AWT or Swing text areas
     */
    private transient boolean useSwing = false;
    /**
     * An auto-generated file name for logs specific to this session
     */
    private transient String logTitle;
    /**
     * indicate if auto-logging should be occurring
     */
    private boolean logging = false;
    /**
     * Indicates whether we should try to auto-login with the given character
     */
    private boolean autoConnect = false;
    /**
     * This will contain the string used for auto-connecting the character if
     * required
     */
    private transient String connectionStr = "";
    /**
     * The outputstream for our log file
     */
    private transient java.io.FileOutputStream logFile;
    /**
     * The Printwriter for our log file
     */
    private transient java.io.PrintWriter logWriter;
    /**
     * Enable or disable debugging output
     */
    private static final boolean DEBUG = false;
    /**
     * Whether MCCP is enabled or not
     */
    private transient boolean compression = false;
    /**
     * The deflater used for compression output should we be using MCCP2
     */
    private transient Deflater zlib;
    /**
     * The character type, used to keep track if this is a MU* or a puppet
     */
    protected transient int characterType;
    /**
     * A hashtable of puppet names and their PuppetSocket windows
     */
    final transient private Hashtable puppetTable = new Hashtable();
    /**
     * A String array of puppet names for quick access when doing input
     * comparison
     */
    private transient String[] puppetArray;
    /**
     * The name of the character that is connected to this MU* (if provided)
     */
    private transient String characterName;
    /**
     * The world represented by this connection
     */
    private SimpleWorld connWorld;
    /**
     * Enabling this variable causes MuSocket to parse everything as if MXP were
     * enabled
     */
    private boolean MXPTEST = true;
    /**
     * Variables created specifically by this connect (often via MXP)
     */
    private Hashtable localVars = new Hashtable();

    private final AbstractLogger logger;

    /**
     * This is a simple constructor to create a new MuSocket. If you are
     * creating a MuSocket intended as a new connection, you should instead
     * provide a world upon creation using the MuSocket(SimpleWorld)
     * constructor.
     */
    public MuSocket() {
        this(new SimpleWorld());
    }

    /**
     * This constructor creates a new MuSocket based on the provided connection
     * world. This is the recommended constructor to use when creating a new
     * connection
     *
     * @param connWorld
     */
    public MuSocket(SimpleWorld connWorld) {
        super();

        if (DEBUG) {
            logger = new SystemLogger();
        } else {
            logger = new NoneLogger();
        }
        final JMConfig settings = JMConfig.getInstance();

        if (settings.getJMboolean(JMConfig.USESWING)) {
            useSwing = true;
        }

        // Set up a new text area
        if (useSwing) {

            final java.awt.Color[] newPal = (java.awt.Color[]) settings.getJMObject(JMConfig.CUSTOMPALETTE);
            mainSText = new JMSwingText();
            mainSText.addMouseListener(this);
            mainSText.setForeground(settings.getJMColor(JMConfig.FOREGROUNDCOLOUR));
            mainSText.setBackground(settings.getJMColor(JMConfig.BACKGROUNDCOLOUR));
            mainSText.setFont(settings.getJMFont(JMConfig.FONTFACE));
            mainSText.setPaintBackground(settings.getJMboolean(JMConfig.BGPAINT));
            mainSText.setEditable(false);
            mainSText.addKeyListener(MuckMain.getInstance());
            mainSText.setAntiAliasing(settings.getJMboolean(JMConfig.ANTIALIAS));
            mainSText.setBoldNotBright(settings.getJMboolean(JMConfig.LOWCOLOUR));

            try {
                mainSText.setPalette(newPal);
            } catch (Exception exc) {
                if (DEBUG) {
                    System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Error_setting_custom_palette_from_MuSocket."));
                }
            }

        } else {
            mainText = new JMText("", 50, 80, JMText.SCROLLBARS_VERTICAL_ONLY);

            // Set the attributes of our text area
            mainText.setEditable(false);
            mainText.setDoubleBuffer(settings.getJMboolean(JMConfig.DOUBLEBUFFER));
            mainText.addMouseListener(this);
            mainText.setForeground(settings.getJMColor(JMConfig.FOREGROUNDCOLOUR));
            mainText.setBackground(settings.getJMColor(JMConfig.BACKGROUNDCOLOUR));
            mainText.setFont(settings.getJMFont(JMConfig.FONTFACE));

        }

        heldResponse = new Vector(0, 1);

        connHandler = CHandler.getInstance();

        // Setting the character-type to 1 indicates this is a MuSocket and not a subclass
        characterType = 0;

        // connWorld = new SimpleWorld();
        this.connWorld = connWorld;
    }

    /**
     *
     * @param world
     */
    public void setWorld(SimpleWorld world) {
        connWorld = world;

    }

    // Check for mouse actions
    /**
     *
     *
     *
     * @param mEvent
     *
     */
    public void mousePressed(final MouseEvent mEvent) {
        setPaused(true);
    }

    /**
     *
     *
     *
     * @param mEvent
     *
     */
    @Override
    public void mouseReleased(final MouseEvent mEvent) {
    }

    /**
     *
     *
     *
     * @param mEvent
     *
     */
    public void mouseClicked(final MouseEvent mEvent) {

        // Determine number of mouse clicks
        if (mEvent == null) {
            // This would most likely only happen during JUnit tests.
            return;
        }

        final int count = mEvent.getClickCount();

        try {
            anecho.gui.Hyperlink testLink = (anecho.gui.Hyperlink) mEvent.getComponent();

            // We've got a link and should launch it!
            if (DEBUG) {
                System.err.println("JMSwingText.mouseClicked() has a link: " + testLink.getAddress());
            }

            String linkAddress = testLink.getAddress();

            // We should extend the link class to handle this better.
            // Fix Me XXX
            if (linkAddress.startsWith("@")) {
                // This is a command and not a link
                int end = linkAddress.indexOf('|');

                if (end == -1) {
                    end = linkAddress.length();
                }

                // This grabs the first command string
                String command = linkAddress.substring(1, end);

                // Send the command to the MU*
                sendText(command);

            } else {
                final BrowserWrapper wrapper = BrowserWrapper.getInstance();
                wrapper.showURL(linkAddress);

                // deselect the text
                // Set the starting selection and the ending selection to the same index.
                // If we don't use the current starting index, we may accidentally scroll
                // the output which can be frustrating to the user.
                if (useSwing) {
                    mainSText.select(mainSText.getSelectionStart(), mainSText.getSelectionStart());
                } else {
                    mainText.select(mainText.getSelectionStart(), mainText.getSelectionStart());
                }
            }

            return;
        } catch (Exception notLink) {
            if (DEBUG) {
                System.err.println("Not a link, so we'll continue on.");
            }
        }

        // This will have to be refactored and probably removed.  Fix Me XXX
        if (count >= 2) {
            // Call outside routines (plug-ins) to handle the selected text
            mEvent.consume();
            spoolText();

            try {
                anecho.gui.Hyperlink testLink = (anecho.gui.Hyperlink) mEvent.getComponent();

                // We've got a link and should launch it!
                if (DEBUG) {
                    System.err.println("JMSwingText.mouseClicked() has a link: " + testLink.getAddress());
                }
            } catch (Exception notLink) {
                if (DEBUG) {
                    System.err.println("Not a link, so we'll continue on.");
                }
            }

            // grab the 'selected' URL
            String tempText;

            if (useSwing) {
                // We're going to have to do some manual manipulation of the text, as the
                // double-click on the JTextArea doesn't grab the whole token by itself.
                if (DEBUG) {
                    System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("MuSocket.mouseClicked_trying_to_get_information_from_mainSText()_") + mainSText);
                    System.err.println("MuSocket mouse event: " + mEvent);
                }

                // Grab the text via fullURL();
                tempText = fullURL();

            } else {
                tempText = (mainText.getSelectedText()).trim();
            }

            final StringBuffer tentativeURL = new StringBuffer(tempText);

            if (DEBUG) {
                System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("MuSocket.mouseClicked()_using_") + tentativeURL + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("_for_external_program."));
            }

            final BrowserWrapper wrapper = BrowserWrapper.getInstance();
            wrapper.showURL(tentativeURL.toString());

            // deselect the text
            // Set the starting selection and the ending selection to the same index.
            // If we don't use the current starting index, we may accidentally scroll
            // the output which can be frustrating to the user.            
            if (useSwing) {
                mainSText.select(mainSText.getSelectionStart(), mainSText.getSelectionStart());
            } else {
                mainText.select(mainText.getSelectionStart(), mainText.getSelectionStart());
            }

        } else {
            // A single click signals a pause
            // This will pause the output window,
            // the incoming lines held in queue
            setPaused(true);
        }
    }

    /**
     *
     *
     *
     * @param mEvent
     *
     */
    public void mouseEntered(final MouseEvent mEvent) {
    }

    /**
     *
     *
     *
     * @param mEvent
     *
     */
    public void mouseExited(final MouseEvent mEvent) {
    }

    /**
     *
     * The name of our connection
     *
     * @param newName
     *
     */
    public synchronized void setMUName(final String newName) {

        if (DEBUG) {
            System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("MuSocket.setTitle_setting_muckName_to:_") + newName);
        }

        // muckName = newName;
        connWorld.setWorldName(newName);
        setFrameTitles();

        if (DEBUG) {
            System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("MuSocket.setTitle_complete."));
        }

    }

    /**
     * getTitle does a small bit of book-keeping, returning a title based on
     * whether we're connected to the MU* or not. If we're not connected then
     * we'll return &quot;JamochaMUD" instead of the name
     *
     * @return Returns the correct status-based title for the connection
     *
     */
    public synchronized String getTitle() {

        String retStr;

        // Do a sanity check on the Title first
        // if (muckName == null) {
        if (connWorld.getWorldName() == null || connWorld.getWorldName().equals("")) {
            if (DEBUG) {
                // System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("MuSocket.getTitle_->_current_muckName_is_null_and_will_be_set_to_") + address);
                System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("MuSocket.getTitle_->_current_muckName_is_null_and_will_be_set_to_") + connWorld.getWorldAddress());
            }

            // muckName = address;
            connWorld.setWorldName(connWorld.getWorldAddress());

        }

        if (connected) {

            // retStr = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("JamochaMUD_(") + muckName + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString(")");
            retStr = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("JamochaMUD_(") + connWorld.getWorldName() + ')';
        } else {
            // retStr = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Not_Connected:_") + address;
            // retStr = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Not_Connected:_") + connWorld.getWorldAddress();
            retStr = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Not_Connected:_") + connWorld.getWorldName();
        }

        return retStr;

    }

    /**
     *
     * This returns the true name of the MU*, regardless of our connection
     *
     * @return Returns the MU* name regardless of connection status
     *
     */
    public String getMUName() {

        // return muckName;
        return connWorld.getWorldName();

    }

    /**
     * Call this to reset the frame'retStr title to the &quot;standard" title.
     * This is usually just the location
     */
    public synchronized void resetTitle() {

        setFrameTitles();

    }

    /**
     *
     * The Address of our connection
     *
     * @param address
     *
     */
    public void setAddress(final String address) {

        // this.address = address;
        connWorld.setWorldAddress(address);

    }

    /**
     *
     *
     *
     * @return
     *
     */
    public String getAddress() {

        // return address;
        return connWorld.getWorldAddress();

    }

    /**
     * The currentPort our connection will connect to
     *
     * @param port The port number to connect to
     */
    public void setPort(final int port) {

        // this.port = port;
        connWorld.setWorldPort(port);

    }

    /**
     *
     * Returns the port of this connection
     *
     * @return
     *
     */
    public int getPort() {

        // return port;
        return connWorld.getWorldPort();

    }

    /**
     * Enables or disabbles the use of SSL on this connection
     *
     * @param ssl
     */
    public void setSSL(final boolean ssl) {

        if (DEBUG) {

            System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Our_address_has_been_set_to_a_secure_socket."));

        }

        // this.ssl = ssl;
        connWorld.setSSL(ssl);

    }

    /**
     * Returns the SSL status of this connection
     *
     * @return <code>true</code> SSL is enabled <code>false</code> this is a
     * plain text connection
     */
    public boolean isSSL() {

        // return ssl;
        return connWorld.isSSL();

    }

    /**
     *
     * The timestamp of our connection, for internal use mostly
     *
     * @param time
     *
     */
    public void setTimeStamp(final long time) {

        this.timeStamp = time;

    }

    /**
     *
     * Returns the internal timestamp of this connection
     *
     * @return
     *
     */
    public synchronized long getTimeStamp() {

        return timeStamp;

    }

    /**
     * This is the main method for our thread. Veryvery important! Basically,
     * it'll be a big loop that only gets broken when our connection
     * disappears... either from the socket'retStr end or the user'retStr end
     */
    @Override
    public void run() {

        MXPProcessor proc;
        proc = new MXPProcessor("JamochaMUD", AboutBox.VERNUM);

        final String tempAddy = this.getAddress();

        // Give some visual notification that we're attempting a connection
        setVisuals(tempAddy);

        // Try to establish a connection
        final int currentPort = this.getPort();
        InetAddress serverAddy;
        JMUDTelnetProtocolHandler protHandler = new JMUDTelnetProtocolHandler();

        // We'll give sockPort and sockHost initial values to satisfy later conditions
        int sockPort = 0;
        String sockHost = "";
        final boolean socks = JMConfig.getInstance().getJMboolean(JMConfig.PROXY);

        // If using a socks connection we'll set up the proxy information
        if (socks) {
            sockHost = JMConfig.getInstance().getJMString(JMConfig.PROXYHOST);
            sockPort = JMConfig.getInstance().getJMint(JMConfig.PROXYPORT);
        }

        try {
            serverAddy = InetAddress.getByName(tempAddy);

            // JMUDTelnetProtocolHandler protHandler = new JMUDTelnetProtocolHandler();
            // protHandler = new JMUDTelnetProtocolHandler();
            // Set the socks proxy via the systems property method
            if (socks) {
                // Later, we can change this method depending on the JVM
                System.setProperty("socksProxyHost", sockHost);
                System.setProperty("socksProxyPort", Integer.toString(sockPort));
            } else {
                System.setProperty("socksProxyHost", "");
                System.setProperty("socksProxyPort", "");
            }

            if (isSSL()) {

                logger.debug("MuSocket.run() setting up secure connection");

                // We need to make a secure connection
                // Create a new TrustManager that will blindly trust certificates.
                // Most MU*'retStr have self-signed certs, and would otherwise never let us connect
                final javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[]{new anecho.extranet.NoTrustManager()};

                logger.debug("MuSocket.run() creating SSLContext");

                final javax.net.ssl.SSLContext sContext = javax.net.ssl.SSLContext.getInstance(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("SSL"));
                sContext.init(null, trustAllCerts, new java.security.SecureRandom());

                logger.debug("MuSocket.run() creating SSLSocketFactory.");

                final javax.net.ssl.SSLSocketFactory sslFact = sContext.getSocketFactory();

                logger.debug("MuSocket.run() Creating SSLSocket.");

                final javax.net.ssl.SSLSocket sslSock = (javax.net.ssl.SSLSocket) sslFact.createSocket(serverAddy, currentPort);

                logger.debug("MuSocket.run() starting SSL Handshake.");

                sslSock.startHandshake();

                outStream = new DataOutputStream(sslSock.getOutputStream());
                protHandler.setOutputStream(outStream);
                inStream = new MUDBufferedReader(sslSock.getInputStream(), outStream, protHandler);

                logger.debug("MuSocket.run() setting connSock from (Socket)sslSock");

                // We do this so that the SSL socket may be closed in "closeSocket()" method
                connSock = (Socket) sslSock;

            } else {
                logger.debug("MuSocket.run() setting up standard connection");

                // This sets up a standard plain text connection
                connSock = new Socket(serverAddy, currentPort);

            }

            // } // End of old SOCKS if statement
            if (outStream == null) {
                logger.debug("MuSocket.run() creating DataOutputStream");

                outStream = new DataOutputStream(connSock.getOutputStream());
            }

            if (inStream == null) {

                logger.debug("MuSocket.run() setting outputsream and creating MUDBufferedReader");

                protHandler.setOutputStream(outStream);

                inStream = new MUDBufferedReader(connSock.getInputStream(), outStream, protHandler);
            }

            // Set the codepage if required
            if (connWorld.isOverrideCodepage()) {
                if (DEBUG) {
                    System.err.println("MuSocket run() setting codepage.");
                }
                inStream.setCodepage(connWorld.getCodePage());
            } else {
                if (DEBUG) {
                    System.err.println("MuSocket run() is not changing codepage.");
                }
            }
            // inStream.setTelnetProtocolHandler(protHandler);

        } catch (NoRouteToHostException oops) {
            // No route to host, or operation timed out
            disconnectMenu();

            logger.debug("MuSocket.run() NoRouteToHostException " + oops);

            final String tempString = oops.toString();
            if (tempString.endsWith(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("unreachable"))) {
                // Host unreachable
                write(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("noRouteToHostException") + '\n');
            } else {
                write(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("operationTimedOutException") + '\n');
            }

            logger.debug("MuSocket.run() calling disconnectCleanUp()");

            disconnectCleanUp();

        } catch (UnknownHostException oops) {
            logger.debug("MuSocket.run() UnknownHostException.");
            disconnectMenu();
            write(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("unknownHostException") + '\n');
        } catch (ConnectException oops) {
            logger.debug("MuSocket.run() ConnectException.");
            disconnectMenu();
            write(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("connectException") + '\n');
        } catch (javax.net.ssl.SSLException oops) {
            logger.debug("MuSocket.run() SSLException.");
            disconnectMenu();
            if (isSSL()) {
                write("---> You have tried to connect to this world as an SSL connection, but the server reports it does not use SSL. <---\n");
            } else {
                write("---> You have tried to connect to this world as plain text connection, but the server reports it uses SSL. <---\n");
            }
        } catch (java.net.SocketException sockExp) {
            logger.debug("MuSocket.run() SocketException.");
            disconnectMenu();

            write("--> JamochaMUD can't connect to SOCKS proxy: Connection refused. <--" + '\n');
            write("--> If you are running JamochaMUD from WebStart then your web browser SOCKS settings should automatically be used." + '\n');
            write("--> If you are running JamochaMUD as a desktop application please check your SOCKS settings under the Options menu." + '\n');
            write("--> In addition to these steps, also make certain that your SOCKS proxy is accepting (non-http) connections. <--" + "\n");
        } catch (NoSuchAlgorithmException | KeyManagementException | IOException oops) {
            logger.debug("MuSocket.run() Fall-through exception.");
            disconnectMenu();

            logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("From_Net,_no_socket_") + oops);
            logger.debug("MuSocket.run() Fall-through exception", oops);

            write(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("exception") + '\n');

            // Fix this XXX
            // closeConnection();
        }

        // Create a MUDBufferedReader for our input stream from the MU*
        try {
            inStream.addTelnetEventListener(JMConfig.getInstance().getDataInVariable());
            inStream.addTelnetEventListener(this);

        } catch (Exception except) {

            logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("MuSocket.run_->_No_inStream") + except);
            logger.debug("MuSocket.run() Exception:", except);

            write("---> " + except + " <---\n");

            closeCleanUp();

            return;

        }

        // Okay, we're connected.  Do our little set-up stuff
        // Some specialised MU*'retStr require a form of authentication immediately
        // after connection.  This could be done here by adding something like:
        // sendText("foo");
        // Read from the socket until we get tired and fall down
        connected = true; // This notifies the program of true connection

        // Change the titles of our frames to show we're connected
        setFrameTitles();

        // Update the main window menus to reflect our connected state
        connectMenu();

        // Add auto-connection information here
        // Fix Me XXX!!
        if (autoConnect) {
            if (DEBUG) {
                System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("MuSocket.run()_attempting_autoConnect."));
            }

            if (!connectionStr.equals("")) {
                sendText(connectionStr);
            }

        }

        try {
            String fromMU;

            while (connected) {
                fromMU = inStream.cleanReadLine();

                // Run the output through the MXP processor if needed
                if (protHandler.isMXP() || MXPTEST) {
                    proc.processText(fromMU);

                    StringBuffer tempOut = new StringBuffer();
                    MXPResult mxpr;

                    while (proc.hasResult()) {
                        mxpr = proc.nextResult();
                        tempOut.append(parseMXP(mxpr));
                    }

                    fromMU = tempOut.toString();
                }

                if (DEBUG) {
                    System.err.println("MuSocket.run() fromMU: **" + fromMU + "**");
                }

                fromMU = EnumPlugIns.callPlugin(fromMU, EnumPlugIns.OUTPUT, this);

                if (DEBUG) {
                    System.err.println("MuSocket.run() fromMU (post processing): **" + fromMU + "**");
                }

                // Send the current line to our Text-field
                if (paused) {
                    heldResponse.addElement(fromMU);
                } else {

                    // Check for any URLs
                    fromMU = checkURL(fromMU);

                    // Check to see if this output should be sent to the current window
                    // or to a puppet window
                    final int pupSize = puppetTable.size();
                    if (DEBUG) {
                        System.err.println("MuSocket.run() puppetTable size is " + pupSize);
                    }

                    boolean pupWrite = false;

                    if (pupSize > 0) {
                        // final String tempFrom = fromMU.toLowerCase();
                        final String tempFrom = anecho.gui.TextUtils.stripEscapes(fromMU.toLowerCase(), false);

                        for (int i = 0; i < pupSize; i++) {
                            // Check for the array name with the ">" added
                            if (tempFrom.startsWith(puppetArray[i])) {
                                if (DEBUG) {
                                    System.err.println("MuSocket.run() to puppetSocket: " + tempFrom);
                                    System.err.println("puppetArray[" + i + "] " + puppetArray[i] + " matches " + tempFrom);
                                    if (puppetTable.containsKey(puppetArray[i])) {
                                        System.err.println("MuSocket.run puppetTable contains key.");
                                    } else {
                                        System.err.println("MuSocket.run puppetTable does not contain key.");
                                    }
                                }
                                final PuppetSocket pSock = (PuppetSocket) puppetTable.get(puppetArray[i]);
                                pSock.write(fromMU);
                                pupWrite = true;

                                // Should there be a return here?  Fix Me XXX
                            } else {
                                if (DEBUG) {
                                    System.err.println("MuSocket.run() determined that " + tempFrom + " does not start with " + puppetArray[i]);
                                }
                            }
                        }
                    }

                    if (!pupWrite) {
                        // This output was not written to a puppet, so we'll write it to our view
                        if (DEBUG) {
                            System.err.println("MuSocket.run() didn't find any puppets to write to, so sends to normal output");
                        }
                        write(fromMU);
                    }

                }

            }

        } catch (Exception exc) {

            if (DEBUG) {

                System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("MuSocket.run():_We've_fallen_out_of_our_while_loop.") + exc);

                exc.printStackTrace();

            }

            // Check to see if we somehow got disconnected.  (It can happen!)
            // sets out menu to the correct state as well as closes the socket
            write("---> " + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("connectionClosed") + "<---\n");

            closeSocket();

            connected = false;

            disconnectMenu();

            disconnectCleanUp();
        }

    }

    /**
     * This method checks to see if this MU* is currently active. If the MU* is
     * not active this method will either update JamochaMUD'retStr title if the
     * window is minimised or it will cause the tab to flash
     *
     * @param fromMU Input from the MU* used as the new title.
     */
    private void checkActivity(final String fromMU) {
        if (DEBUG) {
            System.err.println("MuSocket.checkActivity for " + getMUName() + " entered.");
        }

        // We'll notify the main window if needed
        if (isActiveMU()) {
            if (DEBUG) {
                System.err.println("MuSocket.checkActivity: Resetting activity count on " + getMUName());
            }

            activity = 0;
        } else {
            if (DEBUG) {
                System.err.println("MuSocket.checkActivity: " + getMUName() + " is an inactive mu. Our activity level is " + activity);
            }

            if (useSwing) {
                final anecho.gui.JMFancyTabbedPane ftp = MuckMain.getInstance().getFancyTextPanel();
                ftp.flashTab(mainSText, true);
            } else {

                if (activity < 2) {
                    // We don't want to spam the user with activity notices if they've already received one.
                    // Append an activity notice to the active window
                    if (DEBUG) {
                        System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Activity_on") + getMUName() + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString(".__Change_tab_colour_requested"));
                    }

                    connHandler.getActiveMUDText().append(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("activityOn") + getMUName() + '\n');
                }

            }

            activity++;

        }

        // Check to see if the main frame is minimised.  If so, we add
        // the new text to the title bar
        if (JMConfig.getInstance().getJMboolean(JMConfig.MAINWINDOWICONIFIED)) {
            if (isActiveMU()) {
                addTitleText(fromMU);
            }
        } else {
            minLineCount = 0;
        }

    }

    /**
     *
     * Return our text area to who-ever wants to know!
     *
     * @return
     *
     */
    public synchronized JMText getTextWindow() {

        return mainText;

    }

    /**
     * Return the swing version of our text window
     *
     * @return
     *
     */
    public synchronized JMSwingText getSwingTextWindow() {

        return mainSText;

    }

    /**
     *
     * Send text out to our MU*
     *
     * @param send
     *
     */
    public void sendText(final String send) {

        if (outStream != null) {

            final String finalSend = send + '\n';

            if (compression) {
                this.compressString(finalSend);
            } else {

                try {

                    if (JMConfig.getInstance().getJMboolean(JMConfig.USEUNICODE)) {

                        // Send the string as Unicode
                        //if (outStream != null) {
                        // Use only the \n, and not the \r - 2007-05-09
                        // outStream.writeChars(send + "\n");
                        outStream.writeChars(finalSend);

                        //}
                    } else {

                        // Send the string as regular ANSI text
                        //if (outStream != null) {
                        // outStream.writeBytes(send + "\r\n");
                        // Use only the \n, and not the \r - 2007-05-09
                        // outStream.writeBytes(send + "\n");
                        outStream.writeBytes(finalSend);

                        if (DEBUG) {
                            System.err.println("MuSocket.sendText() sending: *" + send + "* with returns.");
                        }

                        //}
                    }

                } catch (IOException except) {

                    // Most likely we aren't connected to the MU*
                    if (DEBUG) {
                        System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("MuSocket.sendText_exception:_") + except);
                        except.printStackTrace();
                    }

                }
            }
        }

    }

    /**
     * This will set up some of the initial indicators that we're trying to make
     * a connection
     */
    private void setVisuals(final String title) {
        write(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("attemptingConnectionTo") + " " + title + "...\n");
    }

    /**
     *
     */
    private void setFrameTitles() {

        // We should only be able to change the frame titles it this
        // is the active MU*
        if (connHandler == null) {
            return;
        }

        final MuSocket tempSock = connHandler.getActiveMUHandle();

        if (tempSock == this) {

            final MuckMain target = MuckMain.getInstance();

            target.setWindowTitle();

            final DataIn inWindow = JMConfig.getInstance().getDataInVariable();

            if (inWindow != null) {
                inWindow.setWindowTitle();
            }

        }

    }

    /**
     *
     * Return the status of our connection:<br>
     *
     * @return <CODE>true</CODE> - Connected
     *
     * <CODE>false</CODE> - Not connected
     *
     */
    public boolean isConnectionActive() {

        if (DEBUG) {

            System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("MuSocket.isConnectionActive()_returns:_") + connected);

        }

        return connected;

    }

    /**
     * Set the proper states of our windows, etc. 'cause, woe is us, we are
     * disconnected. Most often this would be called internally by our class
     *
     */
    private void disconnectMenu() {

        // This is where we actually disconnect the socket.
        // Is there a nicer way of saying good-bye?
        // Do we really need to close the socket?!  We should get here because the
        // socket is already closed!  Check this out! XXX
        // This guarantees that we've cleaned up after ourselves, too
        // Change the connection notice on the main window if applicable
        final MuckMain window = MuckMain.getInstance();

        window.checkDisconnectMenu();

    }

    /**
     * Allow other classes to close our socket
     */
    public void closeSocket() {

        /*
         if (DEBUG) {
         StringWriter sw = new StringWriter();
         new Throwable().printStackTrace(
         new PrintWriter( sw )
         );
         String callStack = sw.toString();
         System.out.println("Stack: " + callStack);
         }
         */
        try {

            if (connSock != null) {

                connSock.close();

            }

        } catch (IOException closeError) {

            // The only reason we should get here is if the socket was never opened
            // in the first place.  This can happen if we tried to open a bad address
            logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("MuSocket_hit_an_error_(.disconnectMenu):_") + closeError);

        }

        // Change the titles of the frames if applicable
        resetTitle();

    }

    /**
     * Set the proper states of our windows as we've probably just connected
     */
    private void connectMenu() {

        final MuckMain main = MuckMain.getInstance();
        main.setConnectionMenu();

    }

    /**
     *
     * Set the paused status of our text area
     *
     * @param option
     *
     */
    public void setPaused(final boolean option) {

        paused = option;

        if (option) {
            setFrameTitles();
        } else {
            spoolText();
        }

    }

    /**
     *
     * Return the fact that our connection is either paused -
     * <pre>true</pre><br>
     * or not paused -
     * <pre>false</pre><br>
     *
     * @return
     *
     */
    public synchronized boolean isPaused() {

        return paused;

    }

    /**
     *
     * Write any "held" text out to the user'retStr display.
     *
     */
    public synchronized void spoolText() {

        // We'll reset this variable, just in case
        paused = false;

        // Reset our frame titles
        resetTitle();

        // Do a little bounds checking first
        if (heldResponse.size() < 1) {
            /* System.out.println("MuSocket: spoolText, nothing to spool."); */
            return;
        }

        while (!heldResponse.isEmpty()) {

            write(heldResponse.elementAt(0).toString());

            if (DEBUG) {
                System.out.println(heldResponse.elementAt(0).toString());
            }

            heldResponse.removeElementAt(0);

        }

    }

    /**
     * Trim down the string we've received from the MU*, add the minimised line
     * count, and the set the new title
     */
    private synchronized void addTitleText(final String newText) {

        minLineCount++;

        final String str = stripEscapes(newText);

        // Trim the str down to a managable length
        String trimString;

        if (str.length() > 80) {
            trimString = str.substring(0, 80) + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("...");
        } else {
            trimString = str;
        }

        final MuckMain target = MuckMain.getInstance();

        target.setWindowTitle("(" + minLineCount + ") " + trimString);

    }

    /**
     * This method returns the value of a string once it has had any escape
     * characters stripped from it.
     */
    private String stripEscapes(final String token) {

        final StringBuffer workString = new StringBuffer("");

        boolean loop = true;

        int start = 0;

        int end = 0;

        while (loop) {

            end = token.indexOf('\u001b', start);

            if (end < start) {

                // There are no escapes left
                workString.append(token.substring(start));

                loop = false;

                break;

            }

            if (end > 0) {

                workString.append(token.substring(start, end));

            }

            // Now set the new 'start'
            start = token.indexOf('m', end) + 1;

            if (start <= end) {

                loop = false;

                System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Not_a_proper_ANSI_escape"));

                break;

            }

        }

        return workString.toString();

    }

    /**
     * Close the socket and clean up the connection menu
     */
    private void closeCleanUp() {

        logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("MuSocket.closeCleanUp()_calling_closeSocket()"));

        closeSocket();

        connected = false;

        logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("MuSocket.closeCleanUp()_calling_disconnectMenu()"));

        disconnectMenu();

        logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("MuSocket.closeCleanUp()_completed."));

    }

    /**
     * Returns
     * <pre>true</pre> if this connection is the currently active (visible to
     * the user) MU* connection.
     * <pre>False</pre> is returned otherwise.
     */
    private boolean isActiveMU() {

        return activeStatus;

    }

    /**
     *
     * Indicate that this MU active or inactive
     *
     * @param status
     *
     */
    public void setActiveMU(final boolean status) {

        activeStatus = status;

        if (DEBUG) {
            System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("active_state:_") + status + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("_for_") + getTitle());
        }

        // If this MU* is set active then we can reset our activity counter
        if (status) {
            if (DEBUG) {
                System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Setting_out_activity_monitor_to_0"));
            }

            activity = 0;

            if (useSwing) {
                // reset tab colour if appropriate Fix me XXX
                final anecho.gui.JMFancyTabbedPane ftp = MuckMain.getInstance().getFancyTextPanel();
                ftp.flashTab(mainSText, false);
            }

        }
    }

    /**
     *
     */
    public void validate() {

        System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("MuSocket.validate_called."));

        if (useSwing) {
            mainSText.validate();
        } else {
            mainText.validate();
        }

    }

    /**
     *
     * Return the state of our "echo"
     *
     * @return
     *
     */
    public boolean isEchoState() {

        return echo;

    }

    /**
     * Changes the echo state for this MU
     *
     *
     * @param state
     */
    public void setEchoState(final boolean state) {

        echo = state;

        System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("MuSocket_echo_state:_") + echo);

    }

    /**
     * Check the string for anything understood as a URL and underline it
     */
    private String checkURL(final String input) {

        String checkChunk, lCheckChunk;

        final StringBuffer incoming = new StringBuffer(input);

        final java.util.StringTokenizer tokens = new java.util.StringTokenizer(input);

        while (tokens.hasMoreTokens()) {

            checkChunk = tokens.nextToken();

            lCheckChunk
                    = checkChunk.toLowerCase();

            if (lCheckChunk.indexOf(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("www.")) >= 0 || lCheckChunk.indexOf(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("http://")) >= 0) {

                int match;

                match = (incoming.toString()).indexOf(checkChunk);

                // Fix ME XXX!!!
                // Using Swing we may want to change this to a "label" and insert it as a
                // component into our JMSwingText component
                // Added back-to-front so we don't have to do extra calculations
                if (!useSwing) {
                    // if (lCheckChunk.indexOf("http.") < 0) {
                    // Add the http:// so that our link conforms to the URL standard
                    // incoming.insert(match, "http://");
                    // }
                    // incoming.insert(match + checkChunk.length(), "</a>");
                    // incoming.insert(match, "<a href=\"" + lCheckChunk + "\">");
                    // } else {

                    incoming.insert(match + checkChunk.length(), java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("\u001b[24m"));
                    incoming.insert(match, java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("\u001b[4m"));

                }

            }

        }

        return incoming.toString();

    }

    /**
     * Write out given text to our text component with-out sending it to the
     * MU*.
     *
     * @param output The string to be written to the user output
     */
    public synchronized void write(final String output) {
        write(output, false);
    }

    /**
     * Write out given text to our text component with-out sending it to the
     * MU*.
     *
     * @param output The string to be written to the user output
     * @param skip Indicate if the string should be processed by the output
     * plug-ins
     */
    public synchronized void write(final String output, final boolean skip) {
        // public void write(final String output) {

        if (DEBUG) {
            System.err.println("MuSocket.write received string: " + output + " with skip: " + skip);
        }

        String mangledOut;

        if (skip) {
            mangledOut = output;
        } else {
            mangledOut = EnumPlugIns.callPlugin(output, EnumPlugIns.OUTPUT, this);
        }

        if (useSwing) {
            // mainSText.append(output);
            mainSText.append(mangledOut);
        } else {
            // mainText.append(output);
            mainText.append(mangledOut);
        }

        // if logging is enabled, this should be written to the log as well
        if (logging) {
            writeLog(output);
        }

        checkActivity(mangledOut);

    }

    /**
     * Enable / disable the ability for the MU* component to paint background
     * colours
     *
     * @param state <code>true</code> enables background colour painting
     * <code>false</code> disables background colour painting
     */
    public void enableBackgroundPainting(final boolean state) {
        if (mainSText != null) {
            mainSText.setPaintBackground(state);
        }

    }

    /**
     *
     * Custom telnet message receiver for our TelnetEventListener
     *
     * @param event
     *
     */
    public void telnetMessageReceived(final TelnetEvent event) {

        if (event == null) {
            // Most likely this would happen in the event of a JUnit test
            return;
        }

        if (DEBUG) {
            System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("MuSocket.telnetMessageReceived_has_received:_") + event);
            System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("With_our_message_being:_") + event.getMessage());
        }

        final String message = event.getMessage();

        if (message.equals(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("IAC_DO_TELOPT_ECHO"))) {
            // Turn off the local echo
            setEchoState(false);
        }

        if (message.equals(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("IAC_DONT_TELOPT_ECHO"))) {
            // Turn the local echo back on.
            setEchoState(true);
        }

        if (message.equals("IAC DO MCCP_COMPRESS2")) {
            if (DEBUG) {
                System.err.println("MuSocket: telnetEvent enabling compression");
            }
            // Enable MCCP2 Compression
            this.setMCCP(true);
        }

        if (message.equals("IAC DONT MCCP_COMPRESS2")) {
            if (DEBUG) {
                System.err.println("MuSocket: telnetEvent disabling compression");
            }
            setMCCP(false);
        }
    }

    /**
     *
     * Enable or disable auto-logging
     *
     * @param status
     *
     */
    public void setLogging(final boolean status) {
        logging = status;

        if (DEBUG) {
            System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Logging_has_been_set_to_") + status);
        }

        if (!status && logFile != null) {
            // Logging has been disabled.  Close our file handle
            try {
                logWriter.flush();
                logWriter.close();
            } catch (Exception exc) {
                if (DEBUG) {
                    System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Exception_trying_to_flush_and_close_logWriter_in_MuSocket.setLogging"));
                }
            }

            logFile = null;

        }
    }

    /**
     *
     * Return our auto-logging status
     *
     * @return
     *
     */
    public boolean isLogging() {

        return logging;

    }

    /**
     * Return a string that should be used as a file name for logging this
     * session If a title does not yet exist, we will create one Title format:
     * MU* address + timestamp + .txt
     */
    private String getLogFileTitle() {

        String workingTitle;

        if (logTitle == null) {

            // Assemble a date string that is human readable from our timestamp
            final java.util.Date workDate = new java.util.Date(timeStamp);
            final java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yy-MM-dd_H-mm-ss");

            // Create the title for our file and assign it to the logTitle variable
            final StringBuffer tempStr = new StringBuffer();
            // tempStr.append(muckName);
            tempStr.append(connWorld.getWorldName());
            tempStr.append('_');
            tempStr.append(formatter.format(workDate));
            tempStr.append(".txt");

            workingTitle = tempStr.toString();

            // Set up our logTitle
            logTitle = workingTitle;

            if (DEBUG) {
                System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Our_new_logTitle_is_") + logTitle);
            }

        } else {
            workingTitle = logTitle;
        }

        return workingTitle;

    }

    /**
     * Write our output to our log file
     *
     */
    private void writeLog(final String output) {

        final JMConfig settings = JMConfig.getInstance();

        if (logFile == null) {
            // Setup our variables for writing
            // We'll keep the append option to true, as we can't tell if the
            // user stopped logging and then perhaps started again.
            String logPath;
            String totalLogFile;
            // final String checkPath = settings.getJMString(JMConfig.LOGPATH);
            final String checkPath = settings.getJMString(JMConfig.AUTOLOGPATH);
            String checkFile = settings.getJMString(JMConfig.LOGFILENAMEFORMAT);

            if (checkPath.trim().equals("")) {
                // Use the standard logging directory
                logPath = settings.getJMString(JMConfig.USERDIRECTORY) + "logs";
            } else {
                // Use the custom logging directory
                logPath = checkPath;
            }

            // Can we use the getLogFileTitle method to do this instead?  Fix Me XXX
            if (checkFile.trim().equals("")) {
                // Use the standard logging file naming format
                totalLogFile = logPath + settings.getJMString(JMConfig.PATHSEPARATOR) + getLogFileTitle();
            } else {
                final String charName = getCharacterName();

                checkFile = checkFile.replaceAll("\\$MN", getMUName());
                if (charName == null) {
                    checkFile = checkFile.replaceAll("\\$MC", "Char_Name");
                } else {
                    checkFile = checkFile.replaceAll("\\$MC", charName);
                }

                if (checkFile.startsWith("SIMPLE:")) {
                    // re-assemble the simple format
                    // Break our string into tokens
                    String parts[] = checkFile.split("_JMUD_");
                    final StringBuffer tempFile = new StringBuffer();

                    final int partsLen = parts.length;

                    final java.util.Date workDate = new java.util.Date(timeStamp);

                    for (int z = 1; z < partsLen; z++) {
                        if (DEBUG) {
                            System.err.println("Part " + z + ": " + parts[z]);
                        }

                        parts[z] = parts[z].trim();

                        try {
                            final java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(parts[z]);
                            parts[z] = formatter.format(workDate);
                        } catch (Exception dfe) {
                            if (DEBUG) {
                                System.err.println("MuSocket.writeLog error parsing date format " + dfe);
                            }
                        }

                        if (!parts[z].equals("")) {
                            if (z > 1) {
                                tempFile.append('_');
                            }

                            tempFile.append(parts[z]);
                        }
                    }

                    totalLogFile = logPath + java.io.File.separator + tempFile.toString() + ".txt";

                } else {

//                    checkFile.replaceAll("\\$MN", getMUName());
//                    if (charName != null) {
//                        checkFile.replaceAll("\\$MC", charName);
//                    } else {
//                        checkFile.replaceAll("\\$MC", "Char_Name");
//                    }
                    if (DEBUG) {
                        System.err.println("Current checkFile before date parsing: " + checkFile);
                    }

                    // Assemble a date string that is human readable from our timestamp
                    final java.util.Date workDate = new java.util.Date(timeStamp);
                    // final java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yy-MM-dd_H-mm-ss");
                    final java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(checkFile);

                    //            tempStr.append(formatter.format(workDate));
                    // totalLogFile = logPath + java.io.File.pathSeparator + checkFile + ".txt";
                    totalLogFile = logPath + java.io.File.pathSeparator + formatter.format(workDate) + ".txt";
                }
            }

            if (DEBUG) {
                System.err.println("Our current logpath is: " + logPath);
                System.err.println("Our total logfile is: " + totalLogFile);
            }

            try {
                // Check to see if the directories need to be created
                if (!new java.io.File(logPath).exists()) {
                    if (DEBUG) {
                        System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Attempting_to_create_directory_for_logs"));
                    }

                    new java.io.File(logPath).mkdirs();

                    if (DEBUG) {
                        System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Successfully_created_log_directory."));
                    }

                }

                // Check to see if the logfile exists, and if so we'll increment the name by one
                if (new java.io.File(totalLogFile).exists()) {
                    int count = 1;
                    boolean existing = true;
                    while (existing) {
                        if (new java.io.File(totalLogFile + '_' + count).exists()) {
                            count++;
                        } else {
                            totalLogFile = totalLogFile + "_" + count;
                            existing = false;
                            break;
                        }
                    }

                }

                logFile = new java.io.FileOutputStream(totalLogFile, true);
                logWriter = new java.io.PrintWriter(logFile, true);

            } catch (Exception exc) {
                if (DEBUG) {
                    System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("MuSocket.writeLog:_") + exc);
                }
            }

        }

        final String cleanOut = anecho.gui.TextUtils.stripEscapes(output, false);

        if (DEBUG) {
            System.err.println("MuSocket.writeLog() writing out: **" + cleanOut + "**");
        }

        logWriter.print(cleanOut);
        logWriter.flush();

    }

    /**
     * Set the auto-connect status for the MU
     *
     *
     * @param status
     */
    public void setAutoConnect(final boolean status) {
        autoConnect = status;
    }

    /**
     * Indicates whether this connection is set to auto connect
     *
     * @return <code>true</code> auto-connect is enabled <code>false</code>
     * auto-connect is not enabled
     */
    public boolean isAutoConnect() {
        return autoConnect;
    }

    /**
     *
     * @param connStr
     */
    public void setConnectString(final String connStr) {
        connectionStr = connStr;
    }

    /**
     * Returns to string used to connect to this socket
     *
     * @return
     */
    public String getConnectString() {
        return connectionStr;
    }

    /**
     * This method tries to gather a complete URL based on what was
     * double-clicked. The "getSelectedText" method only returns a portion of
     * the URL.
     *
     * @return a String representing the complete URL
     */
    private String fullURL() {
        // Set our return string to the selected text just in case
        String retStr = mainSText.getSelectedText();

        int start = mainSText.getSelectionStart();
        int end = mainSText.getSelectionEnd();

        final String workStr = mainSText.getText();

        if (DEBUG) {
            System.err.println("MuSocket.fullURL original indexes (start, end)" + start + ", " + end);
            System.err.println("MuSocket.fullURL original selection: ->" + retStr + "<-");
            System.err.println("MuSocket.fullURL selection via start & end: ->" + workStr.substring(start, end) + "<-");
        }

        // Check for the index number work-around.
        // Certain versions of Java count the \n and \r tokens which results
        // in the wrong numbers for getSelectionStart and getSelectionEnd
        final int doubleCheck = workStr.indexOf(retStr, start);
        final int diff = doubleCheck - start;
        start = start + diff;
        end = end + diff;

        if (DEBUG) {
            System.err.println("MuSocket.fullURL suggests an adjustment of " + diff);
            System.err.println("MuSocket.fullURL diff'd start: " + start);
            System.err.println("MuSocket.fullURL diff'd end: " + end);
        }

        // Change the end value to the position of the next white-space.
        // This may end up getting some punctuation, but we'll take care
        // of that after.
        int testEnd = workStr.indexOf(' ', end);
        final int breakEnd = workStr.indexOf('\n', end);

        // Avoid wrapping on to a new line if there is a line-break
        if (breakEnd < testEnd) {
            testEnd = breakEnd;
            if (DEBUG) {
                System.err.println("MuSocket.fullURL() testEnd replaced with breakEnd.");
            }

        }

        if (testEnd > end) {
            end = testEnd;
            if (DEBUG) {
                System.err.println("MuSocket.fullURL() end replaced with testEnd.");
            }

        }

        if (testEnd < 0) {
            end = workStr.length();
        }

        // Change the start value to the position of white-space (or beginning
        // of the text.  We can't search backwards through the text as we cannot
        // always be certain if there are illegal characters.
        int testStart = workStr.lastIndexOf("http://", start);

        // Add 1 to avoid capturing the space as well
        final int spaceStart = workStr.lastIndexOf(' ', start) + 1;

        // This helps to determine the beginning of the URL if there is already
        // another URL on the same line.
        if (spaceStart < testStart) {
            testStart = spaceStart;
            if (DEBUG) {
                System.err.println("MuSocket.fullURL: Replaced testStart with spaceStart");
            }

        }

        if (testStart < start && testStart > -1) {
            start = testStart;
            if (DEBUG) {
                System.err.println("MuSocket.fullURL: Replaced start with testStart");
            }

        }

        retStr = workStr.substring(start, end);

        if (DEBUG) {
            System.err.println("MuSocket.fullURL() returns a string of " + retStr);
            System.err.println("MuSocket.fullURL() our new indexes are (start, end): " + start + ", " + end);
        }

        // Set the new selection based on what we've worked up here.
        mainSText.setSelectionEnd(end);
        mainSText.setSelectionStart(start);

        return retStr;

    }

    private void compressString(final String input) {
        try {
            final byte[] inBytes = input.getBytes();
            final byte[] compBytes = new byte[100];   // Fix Me XXX

            if (zlib == null) {
                zlib = new Deflater();
            }

            zlib.setInput(inBytes);
            zlib.finish();
            final int len = zlib.deflate(compBytes);

            for (int i = 0; i < len; i++) {
                outStream.writeByte(compBytes[i]);
            }
        } catch (IOException exc) {
            System.err.println("MuSocket.compressString error: " + exc);
        }
    }

    /**
     *
     * @param status
     */
    public void setMCCP(final boolean status) {
        compression = status;
    }

    /**
     *
     * @return
     */
    public boolean isMCCP() {
        return compression;
    }

    /**
     * Returns the character type of this connection. 0 for a MuSocket, 1 for a
     * PuppetSocket
     *
     * @return
     */
    public int getCharacterType() {
        return characterType;
    }

    /**
     * Adds a puppet to our list of "windows"
     *
     * @param puppetName Name of the puppet to add
     * @param puppetCommand The command (if applicable) to look for
     * @param pSock The window variable for puppet output
     */
    public void addPuppet(final String puppetName, final String puppetCommand, final PuppetSocket pSock) {
        // if (puppetCommand == null) {
        if ("".equals(puppetCommand)) {
            // No command was supplied, so we'll look for the name with a > appended
            // Should the ">" be added with creation of the puppet?  Fix Me XXX
            // puppetTable.put(puppetName.toLowerCase() + '>', pSock);
            puppetTable.put(puppetName.toLowerCase(), pSock);
        } else {
            puppetTable.put(puppetCommand.toLowerCase(), pSock);
        }

        buildPuppetArray();
    }

    /**
     * Removes a puppet from our list of "windows"
     *
     * @param puppetName The name of the puppet to remove
     */
    public void removePuppet(final String puppetName) {
        final String lowerName = getPuppetArrayFromName(puppetName).toLowerCase();

        if (puppetTable.containsKey(lowerName)) {
            puppetTable.remove(lowerName);
        }

        buildPuppetArray();
    }

    /**
     * This method checks to see if the given puppet exists
     *
     * @param puppetName The name of the puppet
     * @return <code>true</code> - This puppet exists <code>false</code> - This
     * puppet does not exist
     */
    public boolean puppetExists(final String puppetName) {
        boolean result = false;

        final String lowerName = getPuppetArrayFromName(puppetName).toLowerCase();
        if (DEBUG) {
            System.err.println("MuSocket.puppetExists received: " + puppetName);
            System.err.println("MuSocket.puppetExists searching for: *" + lowerName + "*");
        }

        if (!"".equals(lowerName) && puppetTable.containsKey(lowerName)) {
            result = true;
        }

        return result;
    }

    /**
     * Returns the Array look-up name based on the name supplied
     */
    private String getPuppetArrayFromName(final String searchName) {

        String tempStr;
        PuppetSocket pSock;
        String retStr = "";

        for (final Enumeration pupEnum = puppetTable.elements(); pupEnum.hasMoreElements();) {
            pSock = (PuppetSocket) pupEnum.nextElement();
            tempStr = pSock.getPuppetName();
            if (tempStr.equalsIgnoreCase(searchName)) {
                // We've found a matching puppet

                if (pSock.getPuppetCommand().equals("")) {
                    retStr = pSock.getPuppetName().toLowerCase();
                    if (DEBUG) {
                        System.err.println("MuSocket.getPuppetArrayFromName returns Name: " + retStr);
                    }
                } else {
                    retStr = pSock.getPuppetCommand().toLowerCase();
                    if (DEBUG) {
                        System.err.println("MuSocket.getPuppetArrayFromName returns Command: " + retStr);
                    }
                }

            }
        }

        return retStr;
    }

    /**
     * This method builds a String array of the puppet names. This should allow
     * for fast checking against our input strings.
     */
    private void buildPuppetArray() {
        final int arSize = puppetTable.size();

        if (DEBUG) {
            System.err.println("MuSocket.buildPuppetArray puppetTable size: " + arSize);
        }

        if (arSize < 1) {
            puppetArray = new String[0];
        } else {
            int count = 0;
            puppetArray = new String[arSize];

            PuppetSocket pSock;

            for (final Enumeration pupEnum = puppetTable.elements(); pupEnum.hasMoreElements();) {
                pSock = (PuppetSocket) pupEnum.nextElement();

                if (pSock.getPuppetCommand().equals("")) {
                    puppetArray[count] = pSock.getPuppetName().toLowerCase();
                } else {
                    puppetArray[count] = pSock.getPuppetCommand().toLowerCase();
                }

                if (DEBUG) {
                    System.err.println("PuppetArray[" + count + "] " + puppetArray[count]);
                }

                count++;
            }

        }

    }

    /**
     * This method is used to clean up any loose ends left over from the
     * connection
     *
     */
    public void disconnectCleanUp() {
        final int arSize = puppetTable.size();

        if (arSize > 0) {
            // Loop through and disconnect the puppets
            PuppetSocket pSock;

            for (final Enumeration pupEnum = puppetTable.elements(); pupEnum.hasMoreElements();) {
                pSock = (PuppetSocket) pupEnum.nextElement();
                pSock.disconnectCleanUp();
            }

        }
    }

    /**
     * Set the character name that is to be used for this MuSocket
     *
     * @param name
     */
    public void setCharacterName(final String name) {
        characterName = name;
    }

    /**
     * Return the character name currently set for this MuSocket
     *
     * @return
     */
    public String getCharacterName() {
        return characterName;
    }

    /**
     * The codepage used for text coming from the MU* serve
     *
     * @return
     */
    public String getCodePage() {
        // return codePage;
        return connWorld.getCodePage();
    }

    /**
     * Set the codepage to be used to convert text coming from the MU* server
     *
     * @param codePage
     */
    public void setCodePage(String codePage) {
        // this.codePage = codePage;
        connWorld.setCodePage(codePage);
    }

    /**
     * Return the world object for this connection.
     *
     * @return
     */
    public SimpleWorld getWorld() {
        return connWorld;
    }

    /**
     * Parse the output for the MXP code. This should probably be moved to a
     * separate class when up and running Fix Me XXX
     *
     * @param mxpr
     * @return
     */
    private String parseMXP(final MXPResult mxpr) {
        StringBuffer output = new StringBuffer();
        String origData = "";
        // String origData = (String)mxpr.data;

        switch (mxpr.type) {
            case 0: {
                // do nothing
                if (DEBUG) {
                    System.err.println("Nothing.\n");
                }
            }
            break;
            case 1: {
                // String retStr = (String) mxpr.data;
                origData = (String) mxpr.data;
                // i think if TRUE, output a newlines if not output regular text
                if (origData.equals("\r\n")) //newlines are always sent separately
                {
                    // output.append("\n");
                    output.append('\n');
                } else {
                    output.append(origData);
                }
            }
            break;
            case 2: {
                // not sure what this is used for
                if (DEBUG) {
                    // Fix Me XXX
//                    int t = (Integer) mxpr.data;
//                    output.append(origData + "Line tag: " + t + "\n");
                }
            }
            break;
            case 3: {
                flagStruct fs = (flagStruct) mxpr.data;
                // this tells ya the name of the flags receieved, like start room or start actor, basically server defined
                // elements, this should be hidden and not output anything to the screen
                // However, some of these things can be used for automappers,
                // In wintin.net, under mxp variables, these show room names, room descriptions, etc.  Ive sent you a screen shot of this also.
                // so these are a type of variable but can be more than one word
                // properties tags to be used by the server in !ELEMENT definitions.
                if (DEBUG) {
                    // Fix Me XXX
                    if (fs.begin) {
                        output.append("Start of flag: ").append(fs.name).append("\n");
                    } else {
                        output.append("End of flag: ").append(fs.name).append("\n");
                    }
                }
            }
            break;
            case 4: {
                final varStruct vs = (varStruct) mxpr.data;

                if (vs.erase) {
                    if (localVars.containsKey(vs.name)) {
                        localVars.remove(vs.name);
                    }
                } else {
                    localVars.put(vs.name, vs.value);
                }

                if (DEBUG) {
                    if (vs.erase) // if this is TRUE, need to delete/remove the global variable
                    {
                        System.err.println("Erase variable: " + vs.name + "\n");
                    } else {
                        System.err.println("Variable: " + vs.name + ", value: " + vs.value + "\n");
                    }
                }

            }
            break;
            case 5: {
                final formatStruct fs = (formatStruct) mxpr.data;
                // check to see if true, if so do what it says to inside the formatStruct
                int mask = fs.usemask;
                if ((mask & formatStruct.USE_BOLD) != 0) // if this is true, the output of text needs to be bolded
                {
                    // output.append("Formatting: "
                    // + (((fs.attributes & libmxp.Bold) != 0) ? "bold" : "not bold") + "\n");
                    if ((fs.attributes & libmxp.Bold) != 0) {
                        // Enable bold text
                        output.append('\u001b' + "[1m");
                    } else {
                        // Disable bold text
                        output.append('\u001b' + "[-1m");
                    }
                    output.append(origData);
                }
                if ((mask & formatStruct.USE_ITALICS) != 0) // if this is true, the output of text needs to be italic
                {
                    //output.append("Formatting: "
                    //        + (((fs.attributes & libmxp.Italic) != 0) ? "italics" : "not italics") + "\n");
                    if ((fs.attributes & libmxp.Italic) != 0) {
                        // Enable italic text
                        output.append('\u001b' + "[3m");
                    } else {
                        // Disable italic text
                        output.append('\u001b' + "[-3m");
                    }
                }
                if ((mask & formatStruct.USE_UNDERLINE) != 0) // if this is true, the output of text needs to be underlined
                {
//                    output.append("Formatting: "
//                            + (((fs.attributes & libmxp.Underline) != 0) ? "underline" : "not underline") + "\n");
                    if ((fs.attributes & libmxp.Underline) != 0) {
                        // enable underline text
                        output.append('\u001b' + "[4m");
                    } else {
                        // disable underline text
                        output.append('\u001b' + "[-4m");
                    }
                }
                if ((mask & formatStruct.USE_STRIKEOUT) != 0) // if this is true, the output of text needs to be strikeout
                {
//                    output.append("Formatting: "
//                            + (((fs.attributes & libmxp.Strikeout) != 0) ? "strikeout" : "not strikeout") + "\n");
                    if ((fs.attributes & libmxp.Strikeout) != 0) {
                        // enable strike-through text
                        output.append('\u001b' + "[9m");
                    } else {
                        // disable strike-through text
                        output.append('\u001b' + "[-9m");
                    }
                }
                if ((mask & formatStruct.USE_FG) != 0) //Set the color of the text.  If the background color is omitted, the current background color is used.
                // you probably should not allow this, just reply back do not support
                {
                    // output.append(String.format("Formatting: foreground color (%d, %d, %d)\n", fs.fg.getRed(),
                    //        fs.fg.getGreen(), fs.fg.getBlue()));
                    // java.awt.Color tempCol = new Color(fs.fg.getRed(), fs.fg.getGreen(), fs.fg.getBlue());
                    // java.awt.Color tempCol = new java.awt.Color(fs.fg.getRGB());

                    try {
                        // String tmpColStr = Integer.toHexString(fs.fg.getRGB() & 0x00ffffff);
                        final String tmpColStr = this.colorToHex(fs.fg);
                        output.append('\u001b' + "[#").append(tmpColStr).append('m');
                        if (DEBUG) {
                            System.err.println("MuSocket.parseMXP() setting foreground to :" + tmpColStr);
                        }
                    } catch (Exception exc) {
                        System.err.println("MuSocket.parseMXP() foreground exception: " + exc);
                    }
                }
                if ((mask & formatStruct.USE_BG) != 0) // changes color of the background
                // you probably should not allow this, just reply back do not support
                {
//                    output.append(String.format("Formatting: background color (%d, %d, %d)\n", fs.bg.getRed(),
//                            fs.bg.getGreen(), fs.bg.getBlue()));

                    try {
                        // String tmpColStr = Integer.toHexString(fs.bg.getRGB() & 0x00ffffff);
                        final String tmpColStr = this.colorToHex(fs.bg);
                        output.append('\u001b' + "[#;").append(tmpColStr).append('m');
                        if (DEBUG) {
                            System.err.println("MuSocket.parseMXP() setting background to :" + tmpColStr);
                        }
                    } catch (Exception exc) {
                        System.err.println("MuSocket.parseMXP() background exception: " + exc);
                    }

                }
                if ((mask & formatStruct.USE_FONT) != 0) // if this is true, use formatStruct.USE_FONT pointer as the style of font to use(might not allow this)
                {
                    // Fix Me XXX
                    if (DEBUG) {
                        // output.append(String.format("Formatting: font " + fs.font + "\n"));
                    }
                }
                if ((mask & formatStruct.USE_SIZE) != 0) // if this is TRUE, use formatStruct.USE_SIZE pointer to determine size of text to output(might not allow this)
                {
                    if (DEBUG) {
                        // Fix Me XXX
                        // output.append(String.format("Formatting: size %d\n", fs.size));
                    }
                }
            }
            break;
            case 6: {
                final linkStruct ls = (linkStruct) mxpr.data;
                // all data is kept in linkStruct
                // the client should recognize a URL either with http or with www
                // the text should be output in blue and underlined, when clicked it should open the browser and send a person to that website
                // the hint option is used if you want to add something such as (Click here to send your browser to URL)
//                output.append(String.format("URL link: name %s, URL %s, text %s, hint %s\n",
//                        ls.name, ls.url, ls.text, ls.hint));
                //output.append('\u001b' + "[!" + ls.name + ";!" + ls.url + ";!" + ls.text + ";!" + ls.hint + "!]");
                output.append('\u001b');
                output.append("[!");
                output.append(ls.name);
                output.append(";!");
                output.append(ls.url);
                output.append(";!");
                output.append(ls.text);
                output.append(";!");
                output.append(ls.hint);
                output.append("]");
            }
            break;
            case 7: {
                SendStruct ss = (SendStruct) mxpr.data;
                // this is an action link also, it sends commands through.
                // the text is normally output with blue text and underlined
                // if the server sends several hints, such as the following line:  they will be split up using the | bar
                // <SEND \"at %d %retStr|at %d look %retStr|at %d scan %retStr|at %d close %retStr %retStr\" hint=\"Go|Go|Look|Scan|Close\">%retStr</SEND>
                // I have attached a screen shot of what this looks like with wintin.net inside of slothmud

                if (DEBUG) {
                    // System.err.println(String.format("Send link: name %s, command %s, text %s, hint %s, to prompt: %s, menu: %s\n",
//                            ss.name, ss.command, ss.text,
//                            ss.hint, (ss.toprompt ? "yes" : "no"), (ss.ismenu ? "yes" : "no")));
                }
                // Currently we have this shoe-horned into a standard link, 
                // but we'll put the @ in front to represent a command
                output.append('\u001b');
                output.append("[!@");
                output.append(ss.command);
                output.append(";!");
                output.append(ss.text);
                output.append(";!");
                output.append(ss.hint);
                output.append(";!");
                output.append(ss.name);
                output.append(";!");
                output.append(ss.toprompt);
                output.append(";!");
                output.append(ss.ismenu);
                output.append("]");
            }
            break;
            case 8: {
                String c = (String) mxpr.data;
                //The <EXPIRE> tag is used to remove links previously displayed with the <A> or <SEND> tags.
                //For example, when moving to a new room, <SEND> links from the previous room description are no longer
                //valid and need to be removed.  To accomplish this, you create a Name for the tags that you want removed.
                //Then you specify this name in the <EXPIRE> tag.
                if (DEBUG) {
                    // Fix Me XXX
                    output.append("Expire: ").append(c).append("\n");
                }
            }
            break;
            case 9: {
                String c = (String) mxpr.data;
                if (DEBUG) {
                    System.err.println("Send this: " + c + "\n");
                }
            }
            break;
            case 10: {
                output.append("_______________________________________" + '\n');

            }
            break;
            case 11: {
                soundStruct ss = (soundStruct) mxpr.data;
                if (ss.isSOUND) {
                    if (DEBUG) {
                        // Fix Me XXX
                        // output.append(String.format("Sound: file %s, URL %s, volume %d, %d repeats, priority %d, type %s\n",
//                                ss.fname, ss.url, ss.vol, ss.repeats, ss.priority,
//                                ss.type));
                    }
                } else {
                    if (DEBUG) {
                        // Fix Me XXX
//                        output.append(String.format("Music: file %s, URL %s, volume %d, %d repeats, continue %s, type %s\n",
//                                ss.fname, ss.url, ss.vol, ss.repeats,
//                                (ss.continuemusic ? "yes" : "no"), ss.type));
                    }
                }
            }
            break;
            case 12: {
                windowStruct ws = (windowStruct) mxpr.data;
                if (DEBUG) {
                    // Fix Me XXX
                    // output.append(String.format("Create window: name %s, title %s, left %d, top %d, width %d, height %d, scrolling %s, floating %s\n",
//                            ws.name, ws.title, ws.left, ws.top, ws.width, ws.height,
//                            (ws.scrolling ? "yes" : "no"), (ws.floating ? "yes" : "no")));
                }
            }
            break;
            case 13: {
                if (DEBUG) {
//                    // Fix Me XXX
//                    internalWindowStruct ws = (internalWindowStruct) mxpr.data;
//                    String s = "none";
//                    switch (ws.align) {
//                        case Left:
//                            s = "left";
//                            break;
//                        case Right:
//                            s = "right";
//                            break;
//                        case Bottom:
//                            s = "bottom";
//                            break;
//                        case Top:
//                            s = "top";
//                            break;
//                        case Middle:
//                            s = "middle (invalid!)";
//                            break;
//                    }
//                    ;
//                    // output.append(String.format("Create internal window: name %s, title %s, align %s, scrolling %s\n", ws.name, ws.title, s, (ws.scrolling ? "yes" : "no")));
                }
            }
            break;
            case 14: {
                String s = (String) mxpr.data;
                if (DEBUG) {
                    // Fix Me XXX
                    // output.append(String.format("Close window: %s\n", s));
                }
            }
            break;
            case 15: {
                String s = (String) mxpr.data;
                if (DEBUG) {
                    // Fix Me XXX
                    // output.append(String.format("Set active window: %s\n", s));
                }
            }
            break;
            case 16: {
                moveStruct ms = (moveStruct) mxpr.data;
                if (DEBUG) {
                    // Fix Me XXX
                    // output.append(String.format("Move cursor: X=%d, Y=%d\n", ms.x, ms.y));
                }
            }
            break;
            case 17: {
                if (DEBUG) {
                    // Fix Me XXX
                    // output.append(String.format("Erase text: %s\n", ((mxpr.data != null) ? "rest of frame" : "rest of line")));
                }
            }
            break;
            case 18: {
                relocateStruct rs = (relocateStruct) mxpr.data;
                if (DEBUG) {
                    // Fix Me XXX
                    // output.append(String.format("Relocate: server %s, port %d\n", rs.server, rs.port));
                }
            }
            break;
            case 19: {
                if (DEBUG) {
                    // Fix Me XXX
                    // output.append(((Boolean) mxpr.data) ? "Send username\n" : "Send password\n");
                }
            }
            break;
            case 20: {
                if (DEBUG) {
                    // Fix Me XXX
//                    final imageStruct is = (imageStruct) mxpr.data;
//                    String s = "";
//                    switch (is.align) {
//                        case Left:
//                            s = "left";
//                            break;
//                        case Right:
//                            s = "right";
//                            break;
//                        case Bottom:
//                            s = "bottom";
//                            break;
//                        case Top:
//                            s = "top";
//                            break;
//                        case Middle:
//                            s = "middle";
//                            break;
//                    }
//                    ;
////                    output.append(String.format("Image: name %s, URL %s, type %s, height %d, width %d, hspace %d, vspace %d, align %s\n",
////                            is.fname, is.url, is.type, is.height,
////                            is.width, is.hspace, is.vspace, s));
                }
            }
            break;
            case 21: {
                if (DEBUG) {
                    // Fix Me XXX
                    String s = (String) mxpr.data;
                    // output.append(String.format("Image map: %s\n", s));
                }
            }
            break;
            case 22: {
                if (DEBUG) {
                    // Fix Me XXX
                    gaugeStruct gs = (gaugeStruct) mxpr.data;
//                    output.append(String.format("Gauge: variable %s, max.variable %s, caption %s, color (%d, %d, %d)\n",
//                            gs.variable, gs.maxvariable,
//                            gs.caption, gs.color.getRed(), gs.color.getGreen(), gs.color.getBlue()));
                }
            }
            break;
            case 23: {
                if (DEBUG) {
                    // Fix Me XXX
                    statStruct ss = (statStruct) mxpr.data;
//                    output.append(String.format("Status bar: variable %s, max.variable %s, caption %s\n",
//                            ss.variable, ss.maxvariable,
//                            ss.caption));
                }
            }
            break;
            case -1: {
                if (DEBUG) {
                    // Fix Me XXX
                    String s = (String) mxpr.data;
                    // output.append(String.format("Error: %s\n", s));
                }
            }
            break;
            case -2: {
                if (DEBUG) {
                    // Fix Me XXX
                    String s = (String) mxpr.data;
                    // output.append(String.format("Warning: %s\n", s));
                }
            }
            break;
            default: {
                output.append(origData);
            }
        }

        return output.toString();
    }

    /**
     *
     * @param intCol
     * @return
     */
    public String colorToHex(final java.awt.Color intCol) {

        String retStr = Integer.toHexString(intCol.getRGB() & 0xffffff);

        // Ensure that the return string is at least 6 characters long
        if (retStr.length() < 6) {
            retStr = "000000".substring(0, 6 - retStr.length()) + retStr;
        }

        return retStr;

    }
}
