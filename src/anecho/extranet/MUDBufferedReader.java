/** MUDBufferedReader, a modified BufferedReader for JamochaMUD,
 * Portions rewritten by Jeff Robinson 2001/07/15
 * most code in this class is directly from the java.io.BufferedReader
 * that ships with Kaffe, by Transvirtual Technologies, Inc.  (See notice below)
 * 
 * Truth be told, there probably isn't very much of anything left of the original
 * Transvirtual class by now!  This will have to be properly vetted.
 */

/*
 * Java core library component.
 *
 * Copyright (checkChar) 1997, 1998
 *      Transvirtual Technologies, Inc.  All rights reserved.
 *
 * See the file "license.terms" for information on usage and redistribution
 * of this file.
 */
package anecho.extranet;

import anecho.extranet.event.TelnetEvent;
import anecho.extranet.event.TelnetEventListener;

import java.awt.Dimension;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
// import java.io.OutputStreamWriter;
import java.io.IOException;

import java.io.UnsupportedEncodingException;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

/**
 * MUDBufferedReader is a BufferedReader customised to deal
 * specifically with the output from MU*s.  Unlike other existing
 * classes, the MUDBufferedReader will give input back before
 * a EOL is received, as some MU*s do not use EOL.
 */
public class MUDBufferedReader extends InputStreamReader {

    /** The default buffer size */
    private static final int DEFAULTBUFFERSIZE = 8192;
    /** the input stream from the MU - this is output sent to the user */
    // private InputStreamReader inStreamReader;
    private transient InputStream inStreamReader;
    // private transient InputStreamReader checkStream;
    /** The output stream used for responding to the MU* */
    // private transient OutputStreamWriter outStream;
    /** An array of characters that are received from the inStreamReader */
    // private char[] fillBuff;
    private transient byte[] inbuf;
    /** position of next char in the inStream buffer */
    //private int pos;	// position of next char inStream buffer
    /** total length of valid chars in inStream buffer */
    //private int size;	// total length of valid chars inStream buffer
    //  invariant: 0 <= pos <= size <= fillBuff.length
    /** A vector containing buffered output strings */
    private transient Vector outputStrings;
    //private boolean markset;
    //private boolean markvalid;
    //private boolean setEORMark = false; // Do we receive EOR marks from the MU*?
    //private boolean isIAC = false;
    //private boolean isWILL = false;
    //private boolean isEOR = false;
    //private boolean isDO = false;
    //private boolean isDONT = false;
    //private boolean isWONT = false;
    //private boolean isSB = false;
    // private boolean isEB = false;
    //private boolean isTTYPE = false;
    //private boolean isSEND = false;
    /** Set whether debug information should be printed. */
    private static final boolean DEBUG = false;
    // private static final boolean DONEG = false; // Enable/disable telnet negotiation (for debugging)
    /** This variable is for the inflater that will (in the future) decompress MCCP2 data */
    private transient Inflater zlib;
    // Keep track of any programmatic choices of IAC responses
    //private boolean mbrEOR = true;     // Should be set "false" by default XXX
    /** Number of milliseconds we've waited since the last character */
    //private long promptSec = 0;
    /** Maximum wait-time for a new character */
    //private static final int PROMPT_USEC = 250;
    /** IAC */
    // private static final char IAC = (char) 255;   // IAC
    private static final byte IAC = (byte) 255;
    /** EOR End of record (line) */
    // private static final char EOR = (char) 239;       // EOR End of record (line)
    //private static final byte EOR = (byte) 239;       // EOR End of record (line)
    /** WILL */
    // private static final char WILL = (char) 251;        // WILL
    //private static final byte WILL = (byte) 251;        // WILL
    /** WONT */
    // private static final char WONT = (char) 252;  // WONT
    //private static final byte WONT = (byte) 252;  // WONT
    /** DO_CODE */
    // private static final char DO_CODE = (char) 253;    // DO_CODE
    //private static final byte DO_CODE = (byte) 253;    // DO_CODE
    /** DONT */
    // private static final char DONT = (char) 254; // DONT
    //private static final byte DONT = (byte) 254; // DONT
    /** Disconnected MU* ? */
    // private static final char DEAD = '\uffff';  // Disconnected MU*?
    //private static final byte DEAD = (byte) '\uffff';  // Disconnected MU*?
    /** NULL */
    // private static final char NULL = '\u0000';  // NULL
    //private static final byte NULL = '\u0000';  // NULL
    /** Carriage Return */
    // private static final String CR_CODE = "\r";    // Carriage Return
//    private static final byte[] CR_CODE = new byte[]{13,0};    // Carriage Return CR
//    private static final byte[] CRLF_CODE = new byte[]{13,10}; // CRLF
    //private static final byte CR_CODE = (byte) '\r';    // Carriage Return CR
    //private static final byte CRLF_CODE = (byte) '\n'; // CRLF
    /** Sub-begin */
    // private static final char SB_CODE = (char) 250;  // Sub-begin
    //private static final byte SB_CODE = (byte) 250;  // Sub-begin
    /** Sub-end */
    // private static final char SE_CODE = (char) 240;  // Sub-end
    //private static final byte SE_CODE = (byte) 240;  // Sub-end
    /** "Go Ahead" command */
    // private static final char GA_CODE = (char) 249;   // "Go Ahead" command
    //private static final byte GA_CODE = (byte) 249;   // "Go Ahead" command
    /** IS_CODE */
    // private static final char IS_CODE = (char) 0;     // IS_CODE
    //private static final byte IS_CODE = (byte) 0;     // IS_CODE
    // private static final char SEND = (char)12345;       // Fix this, the number we have clashes with ECHO
    /** MUD eXtension Protocol */
    // private static final char MXP = (char) 91;   // MUD eXtension Protocol
    //private static final byte MXP = (byte) 91;   // MUD eXtension Protocol
    /** Telnet option: binary mode */
    // private static final char TELOPT_BINARY= (char)0;  /* binary mode */
    /** Telnet option: echo text */
    // private static final char TELOPT_ECHO = (char) 1;  /* echo on/off */
    //private static final byte TELOPT_ECHO = (byte) 1;  /* echo on/off */
    /** Suppress Go-ahead */
    // private static final char TELOPT_SUPP = (char) 3; /* Suppress Go-Ahead */
    //private static final byte TELOPT_SUPP = (byte) 3; /* Suppress Go-Ahead */
    /** Telnet option: sga */
    // private static final char TELOPT_SGA   = (char)3;  /* supress go ahead */
    /** Telnet option: End Of Record */
    // private static final char TELOPT_EOR = (char) 25;  /* end of record */
    //private static final byte TELOPT_EOR = (byte) 25;  /* end of record */
    /** Telnet option: Negotiate About Window Size */
    // private static final char TELOPT_NAWS = (char) 31;  /* NA-WindowSize*/
    //private static final byte TELOPT_NAWS = (byte) 31;  /* NA-WindowSize*/
    /** Telnet option: Terminal Type */
    // private static final char TELOPT_TTYPE = (char) 24;  /* terminal type */
    //private static final byte TELOPT_TTYPE = (byte) 24;  /* terminal type */
    /** terminal speed - Clashes with "space" Fix Me XXX!! */
    // private static final char TELOPT_TSPEED = (char) 32;
    //private static final byte TELOPT_TSPEED = (byte) 32;
    /** What does this do?  Fix Me XXX */
    // private static final char TELOPT_XDISPLOC = (char) 35;       /* */
    //private static final byte TELOPT_XDISPLOC = (byte) 35;       /* */
    /** New Environment */
    // private static final char TELOPT_NEWENVIRONMENT = (char) 39;
    //private static final byte TELOPT_NEWENVIRONMENT = (byte) 39;
    /** compress data as inStream MCCP v1 specification */
    // private static final char MCCP_COMPRESS = (char) 85;
    //private static final byte MCCP_COMPRESS = (byte) 85;
    /** compress data as inStream MCCP v2 specification */
    // private static final char MCCP_COMPRESS2 = (char) 86;
    //private static final byte MCCP_COMPRESS2 = (byte) 86;
    /** Unknown */
    // private static final char UNKNOWN = (char) 112;
    // private static final byte UNKNOWN = (byte) 112;
    // Stuff for our listeners
    /** A vector representing all our listeners */
    private transient final Vector tListeners = new Vector();
    // private boolean compression = false;
    /** Whether MCCP is enabled */
    /** Used for trouble-shooting.  Compression does not
     * work yet, so should be set false for releases.
     */
    // private static final boolean ALLOW_COMPRESSION = false;
    /** The protocol handler */
    protected transient TelnetProtocolHandler handler;
    /** The codepage to use when returning Strings received from the server */
    private transient String codePage;

    /**
     * Create a new MUDBufferedReader using the given InputStream
     * @param inStream Our InputStream
     * @throws java.io.UnsupportedEncodingException This exception occurs when the provided encoding is incorrect or does not exist
     */
    public MUDBufferedReader(final InputStream inStream) throws UnsupportedEncodingException {
        this(inStream, DEFAULTBUFFERSIZE);
    }

    /**
     * Create a new MUDBufferedReader using the given InputStream and OutputStream
     * @param inStream The input stream that the user will receive from the server
     * @param outStream The output stream that will send data to the server
     * @throws java.io.UnsupportedEncodingException This exception occurs when the provided encoding is incorrect or does not exist
     */
    public MUDBufferedReader(final InputStream inStream, final DataOutputStream outStream) throws UnsupportedEncodingException {
        this(inStream, outStream, DEFAULTBUFFERSIZE);
    }

    /**
     * Create a new MUDBufferedReader using the given InputStream
     * and specified buffer size.
     * @param inStream The input stream that the user will receive from the server
     * @param buffSize
     * @throws java.io.UnsupportedEncodingException This exception occurs when the provided encoding is incorrect or does not exist
     */
    public MUDBufferedReader(final InputStream inStream, final int buffSize) throws UnsupportedEncodingException {
        this(inStream, null, buffSize);
    }

    /**
     * Create a new MUDBufferedReader using the given InputStream, OutputStream
     * and specified buffer size.
     * @param inStream The input stream that the user will receive from the server
     * @param outStream The output stream that will send data to the server
     * @param buffSize The user defined buffer size.
     * @throws java.io.UnsupportedEncodingException This exception occurs when the provided encoding is incorrect or does not exist
     */
    public MUDBufferedReader(final InputStream inStream, final DataOutputStream outStream, final int buffSize) throws UnsupportedEncodingException {
        this(inStream, outStream, buffSize, null, null);
    }

    /**
     * Create a new MUDBufferedReader using the given InputStream, OutputStream
     * and specified buffer size.
     * @param inStream The input stream that the user will receive from the server
     * @param outStream The output stream that will send data to the server
     * @param inHandler The telnet protocol handler to use when communicating with the server
     * @throws java.io.UnsupportedEncodingException This exception occurs when the provided encoding is incorrect or does not exist     
     */
    public MUDBufferedReader(final InputStream inStream, final DataOutputStream outStream, final TelnetProtocolHandler inHandler) throws UnsupportedEncodingException {
        this(inStream, outStream, DEFAULTBUFFERSIZE, inHandler, null);
    }

    /**
     * Create a new MUDBufferedReader using the given InputStream, OutputStream, 
     * TelnetProtocolHandler, and specific code page.
     * @param inStream The input stream that the user will receive from the server
     * @param outStream The output stream that will send data to the server
     * @param inHandler The telnet protocol handler to use when communicating with the server
     * @param codePage This is the code-page the be used when creating the InputStream
     * @throws java.io.UnsupportedEncodingException This exception occurs when the provided encoding is incorrect or does not exist     */
    public MUDBufferedReader(final InputStream inStream, final DataOutputStream outStream, final TelnetProtocolHandler inHandler, final String codePage) throws UnsupportedEncodingException {
        this(inStream, outStream, DEFAULTBUFFERSIZE, inHandler, codePage);
    }

    /**
     * Create a new MUDBufferedReader using the given InputStream, OutputStream, buffer size,
     * and TelnetProtocolHandler.
     * @param inStream The input stream that the user will receive from the server
     * @param outStream The output stream that will send data to the server
     * @param buffSize The user defined buffer size.
     * @param inHandler The telnet protocol handler to use when communicating with the server
     * @throws java.io.UnsupportedEncodingException This exception occurs when the provided encoding is incorrect or does not exist     */
    public MUDBufferedReader(final InputStream inStream, final DataOutputStream outStream, final int buffSize, final TelnetProtocolHandler inHandler) throws UnsupportedEncodingException {
        this(inStream, outStream, buffSize, inHandler, null);
    }

    /**
     * Create a new MUDBufferedReader using the given InputStream, OutputStream
     * and specified buffer size.
     * @param inStream The input stream that the user will receive from the server
     * @param outStream The output stream that will send data to the server
     * @param buffSize The user defined buffer size.
     * @param inHandler The telnet protocol handler to use when communicating with the server
     * @param codePage The codepage to use on the input received from the MU*
     * @throws java.io.UnsupportedEncodingException This exception occurs when the provided encoding is incorrect or does not exist
     */
    public MUDBufferedReader(final InputStream inStream, final DataOutputStream outStream, final int buffSize, final TelnetProtocolHandler inHandler, final String codePage) throws UnsupportedEncodingException {
        super(inStream);

        if (codePage == null) {
            // Assign the default system codepage
            this.codePage = new java.io.OutputStreamWriter(System.out).getEncoding();
            if (DEBUG) {
                System.err.println("MUDBufferedReader.constructor: codepage not supplied.  Setting default system codepage to: " + codePage);
            }
        } else {
            this.codePage = codePage;
            if (DEBUG) {
                System.err.println("MUDBufferedReader.constructor setting codepage to : " + codePage);
            }
        }

        if (buffSize <= 0) {
            throw new IllegalArgumentException("Buffer size <= 0");
        }

        if (DEBUG) {
            System.err.println("MUDBufferedReader is using codepage : " + this.getEncoding());
        }

        try {
            inStreamReader = inStream;
        } catch (Exception e) {
            System.err.println("MUDBufferedReader exception setting up inStreamReader:" + e);
        }

        // this.fillBuff = new char[buffSize];
        this.inbuf = new byte[buffSize];

        // Setup our special variables based on our code-page... they return different values
        // depending on the code-page that JamochaMUD is launched from!!!!
        if (DEBUG) {
            // System.err.println("MUDBufferedReader (constructor): InputStreamReader encoding: " + inStreamReader.getEncoding());
            System.err.println("Our char IAC is *" + IAC + "*");
        }


        outputStrings = new Vector();   // This vector should be given a starting size.  Fix Me XXX

        handler = inHandler;

        // If the handler hasn't been set, then we'll set up a generic one
        if (handler == null) {
            if (DEBUG) {
                System.err.println("MUDBufferedReader is using default TelnetProtocolHandler.");
            }
            // This part is for the new MUDTelnetProtocolHandler
            handler = new TelnetProtocolHandler() {

                /** get the current terminal type */
                public String getTerminalType() {
                    return "vt320";
                }

                /** get the current window size */
                public Dimension getWindowSize() {
                    return new Dimension(80, 25);
                }

                /** notify about local echo */
                public void setLocalEcho(final boolean echo) {
                /* EMPTY */
                }

                /** write data to our back end */
                public void write(final byte[] backBytes) throws IOException {
                    outStream.write(backBytes);
                }

                /** sent on IAC EOR (prompt terminator for remote access systems). */
                public void notifyEndOfRecord() {
                }
            };
        // End MUDTelnetProtocolHandler part
        }

    }

    /**
     * Close our *quot;socket" and reset our key variables
     * @throws java.io.IOException This exception is thrown if the socket no longer exists
     * (or perhaps is already closed).
     */
    public void close() throws IOException {
        if (DEBUG) {
            System.err.println("MUDBufferedReader.close called.");
        }

        synchronized (lock) {

            if (inStreamReader == null) {
                return;
            }

            // Close the input reader
            inStreamReader.close();

            // Release the buffer
            inbuf = null;

            // Release the input reader (lock is a reference too)
            inStreamReader = null;
            lock = this;
        }
    }

    /**
     * Allow a class to add a TelnetListener to this MUDBufferedReader.
     * @param telLis The listener
     */
    public synchronized void addTelnetEventListener(final TelnetEventListener telLis) {
        if (DEBUG) {
            System.err.println("MUDBufferedReader.addTelnetEventListener() called: " + telLis);
        }
        tListeners.addElement(telLis);
    }

    /**
     * Remove a given TelnetEventListener from this MUDBufferedReader.
     * @param telLis The listener to remove.
     */
    public synchronized void removeTelnetEventListener(final TelnetEventListener telLis) {
        if (tListeners != null) {
            tListeners.removeElement(telLis);
        }
    }

    /**
     * Send a message outStream to our TelnetListeners
     */
    private synchronized void sendTelnetMessage(final String message) {
        if (DEBUG) {
            System.err.println("MUDBufferedReader.sendTelnetMessage(): We have " + tListeners.size() + " listeners.");
            System.err.println("Sending out the message: " + message);
        }

        final TelnetEvent telEvt = new TelnetEvent(this, message);
        TelnetEventListener telLis;

        for (int i = 0; i < tListeners.size(); i++) {
            // final TelnetEventListener telLis = (TelnetEventListener) tListeners.elementAt(i);
            telLis = (TelnetEventListener) tListeners.elementAt(i);
//             telLis.telnetMessageReceived(new TelnetEvent(this, message));
            telLis.telnetMessageReceived(telEvt);
        }
    }

//    /**
//     * Set the telnet protocol handler to be used
//     * @param tHandler
//     */
//    public void setTelnetProtocolHandler(TelnetProtocolHandler tHandler) {
//        handler = tHandler;
//    }
//
//    /**
//     * Returns the given telnet protocol handler.
//     * @return
//     */
//    public TelnetProtocolHandler getTelnetProtocolHandler() {
//        return handler;
//    }
    /**
     * This method originates from the KMTextViewWorker class of the
     * KMush MUSH client written by Forrest Guice.
     * Decompress byte array using zlib. Return the uncompressed array as a
     * StringBuffer.
     * @param original a byte array consisting of compressed zlib data
     * @return an uncompressed StringBuffer
     */
    private String decompressStream(final byte[] original) {

        if (DEBUG) {
            System.err.println("MUDBufferedReader.decompressStream input: " + original);
        }

        if (zlib == null) {
            //    zlib = new Inflater(true);
            zlib = new Inflater();
        }
        zlib.setInput(original);

        if (DEBUG) {
            System.err.println("MUDBufferedReader.decompressStream input has been set.");
        }

        final StringBuffer retValue = new StringBuffer();
        boolean done = false;
        // boolean error = false;

        while (!done) {
            final byte[] buf = new byte[256];

            try {
                if (DEBUG) {
                    System.err.println("Inflating... estimate " + buf.length + " bytes");
                }
                final int bytesRead = zlib.inflate(buf);
                // int bytesRead = zlib.inflate(buf, 0, original.length);
                if (DEBUG) {
                    System.err.println("Appending...");
                }
                retValue.append(new String(buf, 0, bytesRead));
                System.err.println("MUDBufferedReader.decompressStream inc: " + retValue.toString());
                if (bytesRead < buf.length) {
                    done = true;
                }
            } catch (DataFormatException e) {
                if (DEBUG) {
                    System.err.println("MUDBufferedReader.decompressStream: MCCP: " + e);
                }
                // return null;
                // return the original string.  At least we'll have some feedback
                // retValue = null;
                retValue.append(new String(original, 0, original.length));
                break;
            }
        }

        if (DEBUG) {
            System.err.println("MUDBufferedReader.decompressStream output: " + retValue);
        }

        return retValue.toString();
    }

    /**
     * Read data from the socket and use telnet negotiation before returning
     * the data oldRead.
     * This method is originally from the Java Telnet Application (JTA)
     * from the file de.mud.telnet.TelnetWrapper.java
     * @param fillBuff the input buffer to oldRead in
     * @return the amount of bytes oldRead
     * @throws java.io.IOException This exception can be used to catch when the socket has closed
     */
    public int cleanRead(final byte[] fillBuff) throws IOException {
        /* process all already oldRead bytes */
        int processed;

        if (DEBUG) {
            System.err.println("MUDBufferedReader.cleanRead entered");
        }

        do {
            processed = handler.negotiate(fillBuff);
            if (processed > 0) {
                return processed;
            }
        } while (processed == 0);

        readNeg:
        while (processed <= 0) {
            do {
                processed = handler.negotiate(fillBuff);
                if (processed > 0) {
                    // return processed;
                    break readNeg;
                }
            } while (processed == 0);
            // processed = in.oldRead(fillBuff);
            processed = inStreamReader.read(fillBuff);
            if (processed < 0) {
                // return processed;
                break readNeg;
            }
            handler.inputfeed(fillBuff, processed);
            processed = handler.negotiate(fillBuff);

        }
        return processed;
    }

    /**
     * This method reads the buffer and divides it into separate strings.
     * These strings are then put in an internal buffer.  A string from the buffer
     * is returned each time cleanReadLine is called.  Once the buffer is empty
     * then we'll oldRead more from the socket.
     * 
     * There is probably a better way of doing this.  Fix Me XXX
     * 
     * @return A string of MU* output
     * @throws java.io.IOException
     */
    public String cleanReadLine() throws IOException {

        // String retVal = "";
        String retVal;
        String tempToken;

        if (outputStrings.isEmpty()) {
            // our output buffer is empty, so let's fill it up
            final int len = cleanRead(inbuf);

            if (len == -1) {
                // A length of -1 means that our socket has closed.
                close();
            }

            // There may be an error here that we want the \n character at the
            // end of the string, and not on a separate string.  Fix Me XXX
            if (len > 0) {
                final String testVal = new String(inbuf, 0, len);

                final StringTokenizer tokens = new StringTokenizer(testVal, "\n", true);

                while (tokens.hasMoreTokens()) {
                    tempToken = tokens.nextToken();

                    if ("\n".equals(tempToken) && !outputStrings.isEmpty()) {
                        // Replace the last element in outputStrings with
                        // the same element that includes a \n character
                        final int vecLen = outputStrings.size();
                        final String revamp = outputStrings.lastElement().toString().concat("\n");
                        outputStrings.setElementAt(revamp, vecLen - 1);
                    } else {
                        outputStrings.addElement(tempToken);
                    }
                }

            }
        }

        if (outputStrings.isEmpty()) {
            retVal = "";
        } else {
            retVal = outputStrings.firstElement().toString();

            // Remove the element from our array
            outputStrings.removeElementAt(0);

            // Pasted block.
            // Formerly below this in a separate !"".equals(retVal)
            try {
                retVal = new String(retVal.getBytes(), codePage);
            } catch (Exception exc) {
                System.err.println("Codepage conversion exception: " + exc);
            }
            // End of pasted block
        }

//        if (!"".equals(retVal)) {
//            if (DEBUG) {
//                System.err.println("MUDBufferedReader.cleanReadLine converting output to codepage " + codePage);
//            }
//            try {
//                retVal = new String(retVal.getBytes(), codePage);
//            } catch (Exception exc) {
//                System.err.println("Codepage conversion exception: " + exc);
//            }
//            if (DEBUG) {
//                System.err.println("MUDBufferedReader.cleanReadLine converting output to: " + retVal);
//            }
//        }

        return retVal;
    }

    /**
     * Set the codepage to convert received server output to
     * @param targetCodePage A string representing the codepage to be used
     */
    public void setCodepage(final String targetCodePage) {
        if (DEBUG) {
            System.err.println("MUDBufferedReader.setCodePage: set to " + targetCodePage);
        }
        codePage = targetCodePage;
    }

    /**
     * Return the codepage currently being used
     * @return A string representing the codepage currently being used
     */
    public String getCodePage() {
        return codePage;
    }

    /**
     * Resets the codepage used by this MUDBufferedReader to the system default
     */
    public void resetCodePage() {
        codePage = new java.io.OutputStreamWriter(System.out).getEncoding();
    }
}

