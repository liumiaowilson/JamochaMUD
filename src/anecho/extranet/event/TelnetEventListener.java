/** A custom listener class to alert objects to special IAC commands, such as ECHO */

package anecho.extranet.event;

/**
 * A TelnetEventListener can be registered by classes to listen for
 * TelnetEvents which may occur
 */
public interface TelnetEventListener
{
    /**
     * 
     * @param event 
     */
    void telnetMessageReceived( TelnetEvent event );
}
