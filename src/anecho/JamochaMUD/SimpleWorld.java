/*
 * This class is a simple, non-GUI object for storing world information
 */
package anecho.JamochaMUD;

/**
 *
 * @author jeffnik
 */
public class SimpleWorld {

    public SimpleWorld() {
        this("", "", 0, false, false, null);
    }

    public SimpleWorld(String name, String address, int port) {
        this(name, address, port, false, false, null);
    }

    public SimpleWorld(String name, String address, int port, boolean useSSL) {
        this(name, address, port, useSSL, false, null);
    }

    public SimpleWorld(String name, String address, int port, boolean useSSL, boolean overrideCP, String cp) {
        worldName = name;
        worldAddress = address;
        worldPort = port;
        secureSocket = useSSL;
        overrideCodepage = overrideCP;
        codePage = cp;
    }

    public String getCodePage() {
        return codePage;
    }

    public void setCodePage(final String codePage) {
        this.codePage = codePage;
    }

    public boolean isSSL() {
        return secureSocket;
    }

    public void setSSL(final boolean isSSL) {
        this.secureSocket = isSSL;
    }

    public boolean isOverrideCodepage() {
        return overrideCodepage;
    }

    public void setOverrideCodepage(final boolean overrideCodepage) {
        this.overrideCodepage = overrideCodepage;
    }

    public String getWorldAddress() {
        return worldAddress;
    }

    public void setWorldAddress(final String worldAddress) {
        this.worldAddress = worldAddress;
    }

    public String getWorldName() {
        return worldName;
    }

    public void setWorldName(final String worldName) {
        this.worldName = worldName;
    }

    public int getWorldPort() {
        return worldPort;
    }

    public void setWorldPort(final int worldPort) {
        this.worldPort = worldPort;
    }

    public String getConnectionString() {
        return connectionString;
    }

    public void setConnectionString(final String connectionString) {
        this.connectionString = connectionString;
    }

    public boolean isAutoConnect() {
        return autoConnect;
    }

    public void setAutoConnect(final boolean autoConnect) {
        this.autoConnect = autoConnect;
    }
    
    
    private String worldName;
    private String worldAddress;
    private int worldPort;
    private transient boolean secureSocket;
    private boolean overrideCodepage;
    private String codePage;
    private String connectionString;
    private boolean autoConnect;
}
