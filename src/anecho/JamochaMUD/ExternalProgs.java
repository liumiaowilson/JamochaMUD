/**
 * Allows the designation of external programs for
 * extension of JamochaMUD's capabilities
 * $Id: ExternalProgs.java,v 1.7 2011/05/31 02:48:07 jeffnik Exp $
 */

/* JamochaMUD, a Muck/Mud client program
 * Copyright (C) 1998-2004 Jeff Robinson
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

import java.awt.*;
import java.awt.event.*;

// import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

// import java.util.Vector;

// import anecho.gui.SyncFrame;
// import anecho.gui.ResReader;
// import anecho.gui.OKBox;

// import anecho.extranet.Bowsah;

/**
 * Allows the designation of external programs for
 * extension of JamochaMUD's capabilities
 * @version $Id: ExternalProgs.java,v 1.7 2011/05/31 02:48:07 jeffnik Exp $
 * @author Jeff Robinson
 */
public class ExternalProgs extends Dialog implements ActionListener, MouseListener{
    
    final private transient Button browser1B, browser2B, fTPClientB, eMailClientB, okayButton, cancelButton;
    // private Button pictureViewerB;
    private transient Checkbox browser1CB, browser2CB, fTPClientCB, eMailClientCB;
    // static private pictureViewerCB;
    // private transient FileDialog progDialogue;
    // private Label ePLabel;
    private transient TextField browser1, browser2, fTPClient, eMailClient;
    // private TextField pictureViewer;
    // private boolean returnText;
    
    // private static ExternalProgs eProg;
    private transient Frame ePFrame;
    
    // Private processes for the different 'browsers'
    static Process browser1P, browser2P, ftpP, eMailP, pictureViewerP;
    // private static Bowsah bp1;
    // private static boolean uniqueLaunch = false;		// launch a separate instance?
    private transient JMConfig settings;
    private static final boolean DEBUG = false;
    
    
    /**
     * 
     * @param frameParent 
     */
//    public ExternalProgs(Frame frameParent, JMConfig mainSettings){
    public ExternalProgs(final Frame frameParent){
        
        // super(frameParent, java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("JamochaMUD_-_") + resBundle("externalPrograms"), true);
        super(frameParent, java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("JamochaMUD_-_") + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("externalPrograms"), true);
        ePFrame = frameParent;
        // this.settings = mainSettings;
        settings = JMConfig.getInstance();
        
        // Set Gridbag layout
        final GridBagLayout ePLayout = new GridBagLayout();
        final GridBagConstraints constraints = new GridBagConstraints();
        this.setLayout(ePLayout);
        
        // Add dialogue components
        // Label ePLabel = new Label(resBundle("primaryWebBrowser"));
        Label ePLabel = new Label(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("primaryWebBrowser"));
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.NONE;
        ePLayout.setConstraints(ePLabel, constraints);
        add(ePLabel);
        
        // browser1CB = new Checkbox(resBundle("separateInstance"), false);
        browser1CB = new Checkbox(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("separateInstance"), false);
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        constraints.gridx = 3;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.NONE;
        ePLayout.setConstraints(browser1CB, constraints);
        // browser1CB.setState(settings.getBrowserInstance(JMConfig.BROWSER1));
        browser1CB.setState(settings.getJMboolean(JMConfig.BROWSERINSTANCE1));
        add(browser1CB);
        browser1CB.setState(false);
        browser1CB.setEnabled(false);
        
        browser1 = new TextField("", 30);
        constraints.gridwidth = 4;
        constraints.gridheight = 1;
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.fill = GridBagConstraints.BOTH;
        ePLayout.setConstraints(browser1, constraints);
        add(browser1);
        // browser1.setText(settings.getBrowser(JMConfig.BROWSER1));
        browser1.setText(settings.getJMString(JMConfig.BROWSER1));
        // System.out.println("Browser 1 set from Hashtable");
        
        // browser1B = new Button(resBundle("locate"));
        browser1B = new Button(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("locate"));
        browser1B.setActionCommand(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Browser1"));
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.gridx = 4;
        constraints.gridy = 1;
        constraints.fill = GridBagConstraints.BOTH;
        ePLayout.setConstraints(browser1B, constraints);
        browser1B.addActionListener(this);
        add(browser1B);
        
        // ePLabel = new Label(resBundle("secondaryWebBrowser"));
        ePLabel = new Label(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("secondaryWebBrowser"));
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.fill = GridBagConstraints.BOTH;
        ePLayout.setConstraints(ePLabel, constraints);
        add(ePLabel);
        
        // browser2CB = new Checkbox(resBundle("separateInstance"), false);
        browser2CB = new Checkbox(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("separateInstance"));
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        constraints.gridx = 3;
        constraints.gridy = 3;
        constraints.fill = GridBagConstraints.NONE;
        ePLayout.setConstraints(browser2CB, constraints);
        // browser2CB.setState(settings.getBrowserInstance(settings.BROWSER2));
        browser2CB.setState(settings.getJMboolean(JMConfig.BROWSERINSTANCE2));
        add(browser2CB);
        browser2CB.setState(false);
        browser2CB.setEnabled(false);
        
        browser2 = new TextField("", 30);
        constraints.gridwidth = 4;
        constraints.gridheight = 1;
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.fill = GridBagConstraints.BOTH;
        ePLayout.setConstraints(browser2, constraints);
        add(browser2);
        // browser2.setText(settings.getBrowser(settings.BROWSER2));
        browser2.setText(settings.getJMString(JMConfig.BROWSER2));
        browser2.setEnabled(false);
        
        // browser2B = new Button(resBundle("locate"));
        browser2B = new Button(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("locate"));
        browser2B.setActionCommand(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Browser2"));
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.gridx = 4;
        constraints.gridy = 4;
        constraints.fill = GridBagConstraints.BOTH;
        ePLayout.setConstraints(browser2B, constraints);
        browser2B.addActionListener(this);
        add(browser2B);
        browser2B.setEnabled(false);
        
        // ePLabel = new Label(resBundle("fTPClient"));
        ePLabel = new Label(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("fTPClient"));
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        constraints.gridx = 0;
        constraints.gridy = 7;
        constraints.fill = GridBagConstraints.BOTH;
        ePLayout.setConstraints(ePLabel, constraints);
        add(ePLabel);
        
        fTPClient = new TextField("", 30);
        constraints.gridwidth = 4;
        constraints.gridheight = 1;
        constraints.gridx = 0;
        constraints.gridy = 8;
        constraints.fill = GridBagConstraints.BOTH;
        ePLayout.setConstraints(fTPClient, constraints);
        add(fTPClient);
        // fTPClient.setText(settings.getBrowser(settings.FTPCLIENT));
        fTPClient.setText(settings.getJMString(JMConfig.FTPCLIENT));
        fTPClient.setEnabled(false);
        
        // fTPClientB = new Button(resBundle("locate"));
        fTPClientB = new Button(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("locate"));
        fTPClientB.setActionCommand(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("FTPClient"));
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.gridx = 4;
        constraints.gridy = 8;
        constraints.fill = GridBagConstraints.BOTH;
        ePLayout.setConstraints(fTPClientB, constraints);
        fTPClientB.addActionListener(this);
        add(fTPClientB);
        fTPClientB.setEnabled(false);
        
        // ePLabel = new Label(resBundle("eMailClient"));
        ePLabel = new Label(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("eMailClient"));
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        constraints.gridx = 0;
        constraints.gridy = 10;
        constraints.fill = GridBagConstraints.BOTH;
        ePLayout.setConstraints(ePLabel, constraints);
        add(ePLabel);
        
        eMailClient = new TextField("", 30);
        constraints.gridwidth = 4;
        constraints.gridheight = 1;
        constraints.gridx = 0;
        constraints.gridy = 11;
        constraints.fill = GridBagConstraints.BOTH;
        ePLayout.setConstraints(eMailClient, constraints);
        add(eMailClient);
        // eMailClient.setText(settings.getBrowser(settings.EMAILCLIENT));
        eMailClient.setText(settings.getJMString(JMConfig.EMAILCLIENT));
        eMailClient.setEnabled(false);
        
        // eMailClientB = new Button(resBundle("locate"));
        eMailClientB = new Button(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("locate"));
        eMailClientB.setActionCommand(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("E-mail"));
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.gridx = 4;
        constraints.gridy = 11;
        constraints.fill = GridBagConstraints.BOTH;
        ePLayout.setConstraints(eMailClientB, constraints);
        eMailClientB.addActionListener(this);
        add(eMailClientB);
        eMailClientB.setEnabled(false);
        
        // Add the 'okay' and 'cancel' buttons at the end.
        // okayButton = new Button(resBundle("okay"));
        okayButton = new Button(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("okay"));
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.gridx = 1;
        constraints.gridy = 12;
        constraints.fill = GridBagConstraints.BOTH;
        ePLayout.setConstraints(okayButton, constraints);
        okayButton.addActionListener(this);
        add(okayButton);
        
        // cancelButton = new Button(resBundle("cancel"));
        cancelButton = new Button(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("cancel"));
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.gridx = 3;
        constraints.gridy = 12;
        constraints.fill = GridBagConstraints.BOTH;
        ePLayout.setConstraints(cancelButton, constraints);
        cancelButton.addActionListener(this);
        add(cancelButton);
        
        
        // Set the location sometime, okay?
        // this.setLocation();
        setSize(350, 250);
        setResizable(false);
        pack();

    }
    

    /**
     * Call the file dialogue so that the user may choose
     * the appropriate program
     */
    private void showFileDialogue(final String title, final TextField affectedItem) {
        // Create the dialogue
        
        FileDialog progDialogue = new FileDialog(ePFrame, title);
        this.setVisible(false);
        progDialogue.setVisible(true);
        
        // Check to see if a file was selected
        try {
            final String directoryName = progDialogue.getDirectory();
            final String fileName = progDialogue.getFile();
            affectedItem.setText(directoryName + fileName);
        } catch (Exception e) {
            System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("ExternalProgs_(file_selected_exception)_") + e);
        }
        
        this.setVisible(true);
        
    }
    
    /**
     * 
     * @param event 
     */
    public void actionPerformed(final ActionEvent event){
        final String arg = event.getActionCommand();

        if (arg != null) {
            doActionPerformed(arg);
        }

        }

    /**
     * 
     * @param arg
     */
    public void doActionPerformed(final String arg) {
                    if (arg.equals(okayButton.getLabel())) {
            // Save new choices

            settings.setJMValue(JMConfig.BROWSER1, browser1.getText());
            settings.setJMValue(JMConfig.BROWSERINSTANCE1, browser1CB.getState());
            settings.setJMValue(JMConfig.BROWSER2, browser2.getText());
            settings.setJMValue(JMConfig.BROWSERINSTANCE2, browser2CB.getState());
            settings.setJMValue(JMConfig.FTPCLIENT, fTPClient.getText());
            settings.setJMValue(JMConfig.EMAILCLIENT, eMailClient.getText());

            // Fix this XXX
            // JMWriteRC.Content();
            setVisible(false);
            dispose();

        }
        

        if (arg.equals(cancelButton.getLabel())) {

            // discard any changes

            setVisible(false);

            dispose();

        }

        

        if (arg.equals(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Browser1"))) {

            // showFileDialogue(resBundle("primaryWebBrowser"), browser1);

            showFileDialogue(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("primaryWebBrowser"), browser1);

        }

        

        if (arg.equals(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Browser2"))) {

            // showFileDialogue(resBundle("secondaryWebBrowser"), browser2);

            showFileDialogue(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("secondaryWebBrowser"), browser2);

        }

        

        if (arg.equals(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("FTPClient"))) {

            // showFileDialogue(resBundle("fTPClient"), fTPClient);

            showFileDialogue(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("fTPClient"), fTPClient);

        }

        

        if (arg.equals(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("E-mail"))) {

            // showFileDialogue(resBundle("eMailClient"), eMailClient);

            showFileDialogue(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("eMailClient"), eMailClient);

        }

        

    }

    

    // Mouse events

    /**

     * 

     * @param e 

     */

    public void mousePressed(final MouseEvent evt) {}

    

    /**

     * 

     * @param e 

     */

    public void mouseReleased(final MouseEvent evt) {}

    

    /**

     * 

     * @param e 

     */

    public void mouseClicked(final MouseEvent evt) {}

    

    /**

     * 

     * @param e 

     */

    public void mouseEntered(final MouseEvent evt) {}

    

    /**

     * 

     * @param e 

     */

    public void mouseExited(final MouseEvent evt) {}

    

    /**

     * A method to launch external programs from JamochaMUD

     * @param workingFrame 

     * @param tentativeURL 

     */

    //	public static void launchProgram(CloseableFrame workingFrame, StringBuffer //tentativeURL) {

    //	public static void launchProgram(SyncFrame workingFrame, StringBuffer tentativeURL) {

    // public void launchProgram(SyncFrame workingFrame, StringBuffer tentativeURL) {

    public void launchProgram(final Frame workingFrame, final StringBuffer tentativeURL) {

        

        // Let's do some sanity checks first.

        // if (tentativeURL == null || tentativeURL.toString().equals("") || tentativeURL.toString().length() <= 0) {
        if (tentativeURL == null || tentativeURL.toString().equals("") || tentativeURL.length() <= 0) {
            return;
        }

        

        // First, determine if it is a valid URL

        String uRLToUse = "";

        uRLToUse = testURL(tentativeURL);

        

        // Now determine correct program to launch

        // String progName = "null";

        String progName;

        progName = whichProgram(uRLToUse.trim());

        

        // Check to see if the response is a valid type

        if (progName.toLowerCase().equals(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("null"))) {

            if (DEBUG) {

                System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("No_viewer_configured"));

            }

            

            // Well, this is no good...we should give the person an option to

            // either view our setup or at least inform them that nothing

            // has been setup to handle this request!

            

        } else {

            // Launch the selected program

            

            try {

                if (DEBUG) {

                    System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("ExternalProgs_attempting_to_launch_browser"));

                }

              // final anecho.extranet.Bowsah browser = new anecho.extranet.Bowsah(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("foo"));

              // browser.displayURL(uRLToUse.trim());

                

                /*

                         if (uniqueLaunch) {

                         Process p = Runtime.getRuntime().exec(progName + " " + uRLToUse.trim());

                         System.out.println("Unique launch.");

                         } else {

                         if (bp1 == null) {

                         bp1 = new Bowsah(progName, uRLToUse.trim());

                         System.out.println("Starting new 'browser'");

                         } else {

                         System.out.println("This would've fed information to an already executed program.");

                         bp1.DisplayURL(uRLToUse.trim());

                         }

                         }

                         */

//                if (progName != null && !progName.equals("")) {

//                    edu.stanford.ejalbert.BrowserLauncher.openURL(progName, uRLToUse.trim());

//                } else {

//                    edu.stanford.ejalbert.BrowserLauncher.openURL(uRLToUse.trim());

//                }

                

//            } catch (IOException ioe) {

//                // Most likely no browser is set, see if the default launches anything

//                System.out.println("ExternalProgs: Doing IOException");

//                // There was an IO Exception, most likely

//                // generated due to no program being

//                // assigned to this 'URL' type

//                Vector tempVector = new Vector(0, 1);

//                

//                // Fix this XXX

//                        /*                                OKBox.RunOkay(workingFrame, null, null, false, false, null, null, true, RBL("noProgramConfigured"), "Configure external programs?", 250, 200);

//                         

//                        if (((String)MuckConn.jmVars.get("Okay")).equals("true")) {

//                        // Yes was chosen, set the programs

//                        // Content(workingFrame);

//                        }

//                         */

//                

//                final OKBox check = new OKBox(workingFrame, resBundle("configureExternalProgram"), true);

//                tempVector = RBL("configureBrowser");

//                for (int i = 0; i < tempVector.size(); i++) {

//                    check.append((String)tempVector.elementAt(i));

//                }

//                

//                // check.show();

//                check.showCentered();

            } catch (Exception e) {

                System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("ExternalProgs_->_warning:_") + e);

            }

            

            // uniqueLaunch = false;	// Reset this variable for next time

            

        }

    }

    

    /**

     * Test the URL to see if it is in fact a *valid* URL

     * First, test for superfluous characters

     * such as . , " and perhaps others

     */

    private static String testURL(final StringBuffer tentativeURL) {

        // boolean testResult;

        

        // Test beginning of the string

        for (int i = 0; i<=tentativeURL.length(); i++) {

            

            // A result of true means that it is a valid character

            if (characterCheck(tentativeURL.charAt(i))) {

                // End loop

                break;

            } else {

                // The result is false, change that char

                // into a blank space

                tentativeURL.setCharAt(i, ' ');

            }

        }

        

        // Now check from the back end of the StringBuffer

        for (int i = (tentativeURL.length() - 1); i>=1; i--) {

            // Call a generic routine to test

            

            // A result of true means that it is a valid character

            if (characterCheck(tentativeURL.charAt(i))) {

                // End the loop

                break;

            } else {

                // The result if false, change that

                // char into a blank space

                tentativeURL.setCharAt(i, ' ');

            }

        }

        final String finalURL = tentativeURL.toString();

        finalURL.trim();

        

        // temporary return to allow compiling

        return finalURL;

        

    }

    

    /** Check characters to see if they are 'legal', either

     * a letter or a digit

     */

    private static boolean characterCheck(final char testChar) {

        boolean result = true;

        

        // See if the character is "legal"

        if (!Character.isLetterOrDigit(testChar)) {

            result = false;

        }

        return result;

    }

    

    /**

     * This class attempts to call the correct program by using the

     * file extension of the string passed to it.  Not perfect by

     * any stretch of the imagination

     */

    //	private static String whichProgram(String fileName) {

    private String whichProgram(final String fileName) {

        // private String whichProgram(String fileName) {

        String fileType = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("null");

        String browserPath = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("null");

        

        // Hopefully, this will be replaced with user

        // defined file-browser associations in the future

        

        // Check for tell-tale signs of which program to use

        // Do HTML first, being probably the most common

        if (fileName.toLowerCase().endsWith(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("html")) || fileName.toLowerCase().startsWith(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("www")) || fileName.toLowerCase().startsWith(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("http"))) {

            fileType = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("HTML");

        }

        

        // Check for E-mail address

        // if (fileName.indexOf(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("@")) >= 0) {
        if (fileName.indexOf('@') >= 0) {
            fileType = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("E-mail");

        }

        

        if (fileName.startsWith(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("ftp"))) {

            fileType = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("FTP");

        }

        

        if (fileName.endsWith(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("jpeg")) || fileName.endsWith(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("jpg")) || fileName.endsWith(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("gif"))) {

            fileType = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Image");

        }

        

        // Find a more efficient way of doing this, too?

        if (fileType.equals(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("HTML"))) {

            // browserPath = settings.getBrowser(settings.BROWSER1);

            browserPath = settings.getJMString(JMConfig.BROWSER1);

            if (browser1CB.getState()) {

                // uniqueLaunch = true;

            }

        }

        if (fileType.equals(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("E-mail"))) {

            try {

                // browserPath = settings.getBrowser(settings.BROWSER2);

                browserPath = settings.getJMString(JMConfig.BROWSER2);

                if (browser2CB.getState()) {

                    // uniqueLaunch = true;

                }

            } catch (Exception e) {

                System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("E-mail_exception:_") + e);

            }

        }

        if (fileType.equals(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("FTP"))) {

            // browserPath = settings.getBrowser(settings.FTPCLIENT);

            browserPath = settings.getJMString(JMConfig.FTPCLIENT);

            if (fTPClientCB.getState()) {

                // uniqueLaunch = true;

            }

        }

        if (fileType.equals(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Image"))) {

            // browserPath = settings.getBrowser(settings.EMAILCLIENT);

            browserPath = settings.getJMString(JMConfig.EMAILCLIENT);

            if (eMailClientCB.getState()) {

                // uniqueLaunch = true;

            }

        }

        

        

        // This returns fileType for now.  Later it will return

        // program location, etc.

        return browserPath;

    }

    

    /**

     * This method determines the proper way to pass a new

     * URL or something like that to the chosen "browser" that

     * already has a running instance

     * @param browser 

     * @param progName 

     * @param uRLToUse 

     */

    public static void usePreviousInstance(final Process browser, final String progName, final String uRLToUse) {

        //            browser.exec(uRLToUse);

        final OutputStream oStream = browser.getOutputStream();

        final OutputStreamWriter oSWriter = new OutputStreamWriter(oStream);

        

        try {

            oSWriter.write(uRLToUse);

        } catch (Exception e) {

            System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("We_caught_an_exception_when_we_went_to_write_to_the_Stream_(UsePreviousInstance)_") + e);

        }

    }

    

}

