/*
 * MuckInfo.java
 *
 * Created on January 5, 2006, 7:44 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package anecho.JamochaMUD;

/**
 * This class is used for storing information about a single MU* connection.
 * @author jeffnik
 */
public class MuckInfo {
    private int muPort;
    private String muName;
    private String muAddress;
    private boolean muSSL;
    
    /** Creates a new instance of MuckInfo */
    public MuckInfo() {
    }

    /**
     * 
     * @param name 
     */
    public void setName(String name) {
        muName = name;
    }
    
    /**
     * 
     * @return 
     */
    public String getName() {
        return muName;
    }
    
    /**
     * 
     * @param address 
     */
    public void setAddress(String address) {
        muAddress = address;
    }
    
    /**
     * 
     * @return 
     */
    public String getAddress() {
        return muAddress;
    }
    
    /**
     * 
     * @param port 
     */
    public void setPort(int port) {
        muPort = port;
    }
    
    /**
     * 
     * @return 
     */
    public int getPort() {
        return muPort;
    }
    
    /**
     * 
     * @param ssl 
     */
    public void setSSL(boolean ssl) {
        muSSL = ssl;
    }
    
    /**
     * 
     * @return 
     */
    public boolean getSSL() {
        return muSSL;
    }
}
