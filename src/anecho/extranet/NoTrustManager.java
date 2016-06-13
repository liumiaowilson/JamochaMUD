/*
 * NoTrustManager.java
 *
 * Created on January 5, 2006, 6:58 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package anecho.extranet;

import javax.net.ssl.X509TrustManager;

/**
 *
 * @author jeffnik
 */
public class NoTrustManager implements X509TrustManager {
    
    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
        return null;
    }
    
    /**
     * This method determines whether the client is trusted.  This doesn't matter in a no-trust model.
     * @param certs
     * @param authType
     */
    public void checkClientTrusted(final java.security.cert.X509Certificate[] certs, final String authType) {
    }
    
    /**
     * This method checks if the server is trusted.  As this is a no-trust manager, we don't care!
     * @param certs
     * @param authType
     */
    public void checkServerTrusted(final java.security.cert.X509Certificate[] certs, final String authType) {
    }
    
}
