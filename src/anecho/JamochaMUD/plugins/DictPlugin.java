/* This is a test plug-in.

 * It mainly contains stubs for the

 * functions the plug-ins look for.

 */

package anecho.JamochaMUD.plugins;

import anecho.JamochaMUD.EnumPlugIns;
import anecho.JamochaMUD.JMConfig;

import anecho.JamochaMUD.MuSocket;

public class DictPlugin implements PlugInterface {
    public static final String COMMAND = "#dict";
    
    private boolean isActive = true;
    
    public DictPlugin() {
    }
    
    public void setSettings(final JMConfig mainSettings) {
    }

    /**
     * 
     * This method returns the name of the plug-in
     * 
     * @return
     * 
     */

    public String plugInName() {
        return "DictPlugin";
    }

    public String plugInDescription() {
        return "Look up the words in the dictionary";
    }

    public String plugInType() {
        return EnumPlugIns.INPUT;
    }
    
    private void openUrl(String keyword) {
        String open_url = JMConfig.getInstance().getJMString(JMConfig.DICT_QUERY_URL);
        open_url = open_url.replace("${query}", keyword);
        
        try {
            Runtime.getRuntime().exec(open_url);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public String plugMain(final String jamochaString, final MuSocket mSock) {
        if(!this.isActive) {
            return jamochaString;
        }
        
        if(!jamochaString.startsWith(COMMAND)) {
            return jamochaString;
        }
        
        String [] items = jamochaString.split(" ");
        if(items.length == 2) {
            String keyword = items[1];
            openUrl(keyword);
        }
        
        return null;
    }

    public void plugInProperties() {
    }

    public void initialiseAtLoad() {
    }

    public void setAtHalt() {
    }

    public boolean haveConfig() {
        return false;
    }

    public boolean hasProperties() {
        return false;
    }

    public void activate() {
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }

    public boolean isActive() {
        return this.isActive;
    }
}
