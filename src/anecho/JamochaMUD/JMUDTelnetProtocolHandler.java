/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package anecho.JamochaMUD;

import java.awt.Dimension;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author jeffnik
 */
public class JMUDTelnetProtocolHandler extends anecho.extranet.TelnetProtocolHandler {

    protected String getTerminalType() {
        return "JamochaMUD " + AboutBox.VERNUM;
    }

    protected Dimension getWindowSize() {
        // Fix Me XXX.  This method should try and return a true representation
        return new Dimension(80, 25);
    }

    protected void setLocalEcho(final boolean echo) {
        /** EMPTY */
        // Fix Me XXX
    }

    protected void notifyEndOfRecord() {
        /** EMPTY */
        // Fix Me XXX
    }

    protected void write(final byte[] inByte) throws IOException {
        outStream.write(inByte);
    }

    /**
     * Set the output stream that is to be used by the "write" method
     * @param stream
     */
    public void setOutputStream(final DataOutputStream stream) {
        outStream = stream;
    }
    
    private DataOutputStream outStream;
}
