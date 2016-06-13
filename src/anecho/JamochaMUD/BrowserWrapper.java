/*
 * BrowserWrapper.java
 *
 * Created on January 28, 2007, 4:31 PM
 *
 * This class acts as a wrapper around BrowserLauncher to make certain we only have
 * one instance at a time, and that and to any last-minute clean-up of the URLs
 * the are passed from JamochaMUD.
 *
 */

package anecho.JamochaMUD;

import edu.stanford.ejalbert.BrowserLauncher;



/**
 *
 * @author jeffnik
 */
public class BrowserWrapper {
    
    private static BrowserWrapper _instance;
    // private Thread launcherThread;
    private BrowserLauncher launcher;
    private static final boolean DEBUG = false;
    
    /** Creates a new instance of BrowserWrapper */
    private BrowserWrapper() {
        try {
            if (DEBUG) {
            net.sf.wraplog.SystemLogger sysLog = new net.sf.wraplog.SystemLogger();
            launcher = new BrowserLauncher(sysLog);
            } else {
                launcher = new BrowserLauncher(null);
            }
        } catch (Exception exc) {
            if (DEBUG) {
                System.err.println("BrowserWrapper error creating new BrowserLauncher." + exc);
            }
        }
    }
    
    public static synchronized BrowserWrapper getInstance() {
        if (_instance==null) {
            _instance = new BrowserWrapper();
        }
        return _instance;
        
    }
    
    public void showURL(String urlString) {
        
        // String cleanString = checkString(urlString);
        
        // cleanString = anecho.gui.TextUtils.cleanURL(cleanString);
        String cleanString = anecho.gui.TextUtils.cleanURL(urlString);
        cleanString = checkString(cleanString);
        
        try {
            if (DEBUG) {
                System.err.println("BrowserWrapper attempting to show URL: " + cleanString);
            }
            
            launcher.openURLinBrowser(cleanString);
        } catch (Exception exc) {
            if (DEBUG) {
                System.err.println("BrowserWrapper hit an error trying to open the URL");
                System.err.println(exc + "");
            }
        }
    }
    
    /** This will provide some sanity checking for the URL.
     * Mostly this will add missing "http://"'s, which are
     * a requirement for BrowserLauncher
     */
    private String checkString(String dirtyURL) {
        String urlString = dirtyURL;
        
        if (!dirtyURL.startsWith("http://")) {
            urlString = "http://" + dirtyURL;
        }
        
        return urlString;
    }
}
