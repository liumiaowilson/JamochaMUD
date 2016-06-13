package anecho.JamochaMUD;

/**
 * The PuppetSocket creates a class that acts in many ways like a MuSocket
 * but for the exception that it does not create its own MU* connection.
 * 
 * Instead, this socket will display output from and direct input to an
 * existing socket to be used by zombies/puppets.  The PuppetSocket will
 * appear like a separate connection to the MU* for most user-interface purposes.
 * 
 * @author Jeff Robinson
 * @version $Id: PuppetSocket.java,v 1.4 2008/08/06 12:48:01 jeffnik Exp $
 */
public class PuppetSocket extends MuSocket {

    /**
     * The public constructor used to set the characterType to 1 (PuppetSocket)
     */
    public PuppetSocket() {
        super();

        // Setting the character-type to 1 indicates this is a PuppetSocket
        characterType = 1;

    }

    public void run() {
        if (DEBUG) {
            System.err.println("PuppetSocket entering run method");
        }

        if ("".equals(puppetName)) {
            this.write("Attempting to connect to our puppet...");
        } else {
            this.write("Attempting to connect " + puppetName + " to the MU*");
        }

        if (DEBUG) {
            System.err.println("PuppetSocket exiting run method");
        }

    }

    /**
     * Returns the "parent" connection for this puppet
     * @return A MuSocket representing the parent connection for this puppet
     */
    public MuSocket getParentSocket() {
        return parentSocket;
    }

    /**
     * As a PuppetSocket is not really a separate connection to the MU*, we
     * must set the "parent" socket to use for communication
     * @param parentSocket The MuSocket connection used by this puppet
     */
    public void setParentSocket(final MuSocket parentSocket) {
        this.parentSocket = parentSocket;
    }

    /**
     * Returns the name of the puppet currently set for this view
     * @return A String of the puppet name set for this view
     */
    public String getPuppetName() {
        return puppetName;
    }

    /**
     * Set the name of the puppet using this view
     * @param puppetName The name of the puppet using this view
     */
    public void setPuppetName(final String puppetName) {
        this.puppetName = puppetName;
    }

    /**
     * Returns the command that JamochaMUD looks for to determine what goes to this puppet
     * @return The MU* puppet command
     */
    public String getPuppetCommand() {
        return puppetCommand;
    }

    /**
     * Sets the command that JamochaMUD looks for to determine what goes to this puppet
     * @param puppetCommand Command pre-pend
     */
    public void setPuppetCommand(final String puppetCommand) {
        this.puppetCommand = puppetCommand;
    }

    /**
     * Returns the command pre-pended to output to the MU* from this puppet
     * @return Command pre-pend
     */
    public String getPuppetSendCommand() {
        return puppetSendCommand;
    }

    /**
     * Set the command to pre-pend to output to the MU* from this puppet
     * @param puppetSendCommand
     */
    public void setPuppetSendCommand(final String puppetSendCommand) {
        this.puppetSendCommand = puppetSendCommand;
    }

    /** Send the text to the parent MuSocket.  As a PuppetSocket isn't really
     * a separate connection to the MU*, we have to send all output via the parent.
     * 
     * @param send The string to send to the parent MuSocket
     */
    public void sendText(final String send) {
        if ("".equals(puppetSendCommand)) {
            parentSocket.sendText(send);
        } else {
            parentSocket.sendText(puppetSendCommand + ' ' + send);
        }
    }
    
    public void disconnectCleanUp() {
        // We have been disconnected, so we should inform the user
        write("Disconnected from " + parentSocket.getMUName() + ".\n");
    }
    
    /** The name of the puppet using this view */
    private String puppetName;
    /** The command to watch for to know text is meant for this view */
    private String puppetCommand;
    /** The command to use to send text to a MU* */
    private String puppetSendCommand;
    /** This is the actual socket that this puppet window is listening to */
    private MuSocket parentSocket;
    /** Enable or disable debugging output */
    private static final boolean DEBUG = false;
}
