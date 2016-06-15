/**
 * WriteRC, a replacement for WriteINI (January 17, 1999) Write .jamocha.rc from
 * all information available $Id: JMWriteRC.java,v 1.8 2012/02/25 02:42:40
 * jeffnik Exp $
 */

/* JamochaMUD, a Muck/Mud client program
 * Copyright (C) 1998-2008  Jeff Robinson
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

import java.awt.Color;
// import java.awt.Frame;
import java.awt.Font;
import java.awt.Rectangle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;

import java.util.Hashtable;
// import java.util.Properties;
import java.util.Vector;
import net.sf.wraplog.AbstractLogger;
import net.sf.wraplog.NoneLogger;
import net.sf.wraplog.SystemLogger;

/**
 * WriteRC, a replacement for WriteINI (January 17, 1999) Write .jamocha.rc and
 * from all from all information available
 *
 * @version $Id: JMWriteRC.java,v 1.10 2015/08/30 22:27:27 jeffnik Exp $
 * @author Jeff Robinson
 */
public class JMWriteRC {

    // private FileOutputStream outFile;
    // private transient PrintWriter out;
    private transient Vector newFile; // oldRC;
    private final transient JMConfig settings;
    private final transient String jmTempFile = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString(".jamocha.rc.temp");
    private final transient String empty = java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("empty");
    private static final transient String space = " ";
    private static final boolean DEBUG = false;
    private AbstractLogger logger;

    /**
     *
     */
    public JMWriteRC() {
        // public JMWriteRC(JMConfig mainSettings){
        // this.settings = mainSettings;
        this.settings = JMConfig.getInstance();

        if (DEBUG) {
            logger = new SystemLogger();
        } else {
            logger = new NoneLogger();
        }

    }

    public synchronized void writeRCToFile() {
        // jmSections = new Hashtable();

        // readLoop = true;
        // oldRC = new Vector(0, 1);
        newFile = new Vector(0, 1);

        // First make sure the vector is empty
        newFile.removeAllElements();

        // addObj the new information to the new vector
        jMBuildNewRC();

        // Delete the old temporary file if it exists
        try {
            // File orig = new File(settings.getUserDirectory() + jmTempFile);
            final File orig = new File(settings.getJMString(JMConfig.USERDIRECTORY) + jmTempFile);
            orig.delete();
        } catch (Exception exc) {
            System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Execption_deleting_old_temporary_file."));
        }

        // Now it is time to write this information to a new temp file
        try {
            // outFile = new FileOutputStream(settings.getUserDirectory() + jmTempFile, false);
            // outFile = new FileOutputStream(settings.getJMString(JMConfig.USERDIRECTORY) + jmTempFile, false);
            final FileOutputStream outFile = new FileOutputStream(settings.getJMString(JMConfig.USERDIRECTORY) + jmTempFile, false);
            // out = new PrintWriter(outFile, true);

            logger.debug("Writing out to file " + settings.getJMString(JMConfig.USERDIRECTORY) + jmTempFile);

            final PrintWriter out = new PrintWriter(outFile, true);

            // Loop through until all the information is written out
            for (int i = 0; i < newFile.size(); i++) {
                out.println(newFile.elementAt(i).toString());
            }
            outFile.close();
        } catch (Exception exc) {
            System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("JMWriteRC:_(outFile)_Exception_generated_:_") + exc);
            exc.printStackTrace();
        }

        // We've written the temporary file.
        // now copy this file onto the original after erasing the previous version
        try {
            // File delFile = new File(settings.getUserDirectory() + ".jamocha.rc");
            final File delFile = new File(settings.getJMString(JMConfig.USERDIRECTORY) + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString(".jamocha.rc"));
            delFile.delete();
        } catch (Exception exc) {
            System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Error_deleting_old_.jamocha.rc"));
        }

        try {

            // RandomAccessFile fromFile = new RandomAccessFile(settings.getUserDirectory() + jmTempFile, "r");
            final RandomAccessFile fromFile = new RandomAccessFile(settings.getJMString(JMConfig.USERDIRECTORY) + jmTempFile, java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("r"));

            // RandomAccessFile toFile = new RandomAccessFile(settings.getUserDirectory() + ".jamocha.rc", "rw");
            final RandomAccessFile toFile = new RandomAccessFile(settings.getJMString(JMConfig.USERDIRECTORY) + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString(".jamocha.rc"), java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("rw"));

            int fileData;

            // while((fileData = fromFile.read()) >= 0){
            fileData = fromFile.read();

            while (fileData >= 0) {

                toFile.write(fileData);

                fileData = fromFile.read();

            }

            try {

                toFile.close();

            } catch (Exception fcexc) {

                System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("JMWriteRC:_toFile.close_exception"));

            }

            try {

                fromFile.close();

            } catch (Exception fcexc) {

                System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("JMWriteRC:_fromFile.close_exception"));

            }

        } catch (Exception exc) {

            System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("JMWriteRC:_Renaming_file_exception_:_") + exc);

        }

        // We have successfully written the .rc file
        // and it is now safe to delete the temp file
        try {

            // File tempFile = new File(settings.getUserDirectory() + jmTempFile);
            final File tempFile = new File(settings.getJMString(JMConfig.USERDIRECTORY) + jmTempFile);

            tempFile.delete();

        } catch (Exception delexc) {

            System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Temp_file_delete_exception_") + delexc);

        }

        // Last but not least, lets serialise the macros
        serialiseMacros();

    }

    // These section will each write the data for their relevant area
    /**
     * Write the list of worlds out to .jamocha.rc
     */
    private void writeWorlds() {

//        Vector muckName = new Vector(0, 1);
//        Vector muckAddy = new Vector(0, 1);
//        Vector muckPort = new Vector(0, 1);
//        muckName = settings.getJMVector(JMConfig.MUCKNAME);
//        muckAddy = settings.getJMVector(JMConfig.MUCKADDY);
//        muckPort = settings.getJMVector(JMConfig.MUCKPORT);
        Vector muckList = new Vector(0, 1);
        muckList = settings.getJMVector(JMConfig.MUCKLIST);
        MuckInfo tempMU;

        // loop through until all worlds are gone
        // for (int i = 0; i < muckName.size(); i++) {
        for (int i = 0; i < muckList.size(); i++) {
            newFile.addElement(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("[") + JMConfig.WORLD + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("]"));
            // addObj all the world elements to the vector

            try {
                tempMU = (MuckInfo) muckList.elementAt(i);
                addObj(tempMU.getName());
                addObj(tempMU.getAddress());
                // addObj(tempMU.getPort() + "");
                addObj(Integer.toString(tempMU.getPort()));
                if (tempMU.getSSL()) {
                    addObj(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("true"));
                } else {
                    addObj(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("false"));
                }
//                addObj(muckName.elementAt(i));
//                addObj(muckAddy.elementAt(i));
//                addObj(muckPort.elementAt(i));

            } catch (Exception exc) {
                System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("JMWriteRC:_Exception_writing_worlds_") + exc);
            }

            newFile.addElement("");
        }

        // jmSections.put("MUWorlds", "false");
    }

    /**
     * Write the foreground colour of the text areas out to .jamocha.rc
     */
    private void writeForegroundColour() {

        // Get the colour of the font from the window
        // Color tempColour = MuckMain.mainText.getForeground();
        // Color tempColour = settings.getForegroundColour();
        final Color tempColour1 = settings.getJMColor(settings.FOREGROUNDCOLOUR);

        /*
         Color tempColour2 = settings.getJMColor(settings.BACKGROUNDCOLOUR);
         // Make certain our two colours are not the same!
         if (tempColour1 == tempColour2) {
         int red, green, blue;
         red = tempColour1.getRed();
         blue = tempColour1.getBlue();
         green = tempColour1.getGreen();
         red = red + 128;
         if (red > 255) {
         red = red - 255;
         }
         green = green + 128;
         if (green > 255) {
         green = green -255;
         }
         blue = blue + 128;
         if (blue > 255) {
         blue = blue - 255;
         }
         tempColour1 = new Color(red, green, blue);
         }
         */
        addObj(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("[") + JMConfig.FOREGROUNDCOLOUR + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("]"));

        addInt(tempColour1.getRed());

        addInt(tempColour1.getGreen());

        addInt(tempColour1.getBlue());

        addObj("");

        // jmSections.put("ForegroundColour", "false");
    }

    /**
     * Write out the text area's background colours to .jamocha.rc
     */
    private void writeBackgroundColour() {

        // Get the colour of the font from the window
        //Color tempColour = MuckMain.mainText.getBackground();
        // Color tempColour = settings.getBackgroundColour();
        final Color tempColour1 = settings.getJMColor(settings.BACKGROUNDCOLOUR);

        /*
         Color tempColour2 = settings.getJMColor(settings.FOREGROUNDCOLOUR);
         if (tempColour1 == tempColour2) {
         int red, green, blue;
         red = tempColour1.getRed();
         blue = tempColour1.getBlue();
         green = tempColour1.getGreen();
         red = red + 128;
         if (red > 255) {
         red = red - 255;
         }
         green = green + 128;
         if (green > 255) {
         green = green -255;
         }
         blue = blue + 128;
         if (blue > 255) {
         blue = blue - 255;
         }
         tempColour1 = new Color(red, green, blue);
         }
         */
        addObj(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("[") + JMConfig.BACKGROUNDCOLOUR + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("]"));

        addInt(tempColour1.getRed());

        addInt(tempColour1.getGreen());

        addInt(tempColour1.getBlue());

        addObj("");

        // jmSections.put("BackgroundColour", "false");
    }

    /**
     * Write out the dimension and location of the Main (output) window
     */
    private void writeMainWindow() {

        newFile.addElement(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("[") + JMConfig.MAINWINDOW + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("]"));

        // writeRect(settings.getMainWindow());
        writeRect(settings.getJMRectangle(JMConfig.MAINWINDOW));

        addObj("");

        // jmSections.put("MainWindow", "false");
    }

    /**
     * Write the size and location of the databar (input) window to .jamocha.rc
     */
    private void writeDataBar() {

        addObj(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("[") + JMConfig.DATABAR + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("]"));

        // writeRect(settings.getDataBar());
        writeRect(settings.getJMRectangle(JMConfig.DATABAR));

        addObj("");

        // jmSections.put("DataBar", "false");
    }

    /**
     * Write the size and position of the Macro Window out to .jamocha.rc
     */
    private void writeMacroWindow() {

        // These two lines will be correct regardless
        addObj(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("[") + JMConfig.MACROFRAME + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("]"));

        if (settings.getJMboolean(JMConfig.MACROVISIBLE)) {

            addObj(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("true"));

        } else {

            addObj(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("false"));

        }

        // writeRect(settings.getMacroFrame());
        final Rectangle tempRect = settings.getJMRectangle(JMConfig.MACROFRAME);

        if (tempRect != null) {

            writeRect(settings.getJMRectangle(JMConfig.MACROFRAME));

        }

        // MacroBar isn't visible, so use the
        // information provided from the
        // static variables
        addObj("");

        // jmSections.put("MacroWindow", "false");
    }

    /**
     * Write the style of font used in the text areas to .jamocha.rc
     */
    private void writeFontFace() {

        addObj(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("[") + JMConfig.FONTFACE + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("]"));

        // Font temp = settings.getFontFace();
        final Font temp = settings.getJMFont(settings.FONTFACE);

        addObj(temp.getName());

        addInt(temp.getStyle());

        addInt(temp.getSize());

        addObj("");

        // jmSections.put("FontFace", "false");
    }

    /**
     * Stub from the old version of .jamocha.rc, since macros are no longer
     * contained with the .jamocha.rc, instead having their own .plugins.rc
     */
    // private void WriteMacros() {
    // }
    /**
     * Write the browser locations to the .jamocha.rc file
     *
     * @param number
     */
    public void writeBrowser(final String number) {

        if (number.equals(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("1"))) {

            addObj(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("[") + JMConfig.BROWSER1 + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("]"));

            try {

                // String temp1 = settings.getBrowser("Browser1");
                final String temp1 = settings.getJMString(JMConfig.BROWSER1);

                if ("".equals(temp1)) {
                    addObj(empty);
                } else {

                    // String status = new String();
                    // if (settings.getBrowserInstance("Browser1")) {
                    // if (settings.getBrowserInstance(JMConfig.BROWSER1)) {
                    if (settings.getJMboolean(JMConfig.BROWSERINSTANCE1)) {

                        // status = "true";
                        addObj(temp1 + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("*true"));

                    } else {

                        // status = "false";
                        addObj(temp1 + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("*false"));

                    }

                    // addObj(temp1 + "*" + status);
                }

            } catch (Exception exc) {

                addObj(empty);

            }

        } else {

            addObj(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("[") + JMConfig.BROWSER2 + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("]"));

            try {

                // String temp1 = new String(settings.getBrowser("Browser2"));
                // String temp1 = new String(settings.getBrowser(JMConfig.BROWSER2));
                final String temp1 = new String(settings.getJMString(JMConfig.BROWSER2));

                if ("".equals(temp1)) {
                    // temp1 = new String(empty);
                    addObj(empty);
                } else {

                    // String status = new String();
                    // if (settings.getBrowserInstance("Browser2")) {
                    //if (settings.getBrowserInstance(JMConfig.BROWSER2)) {
                    if (settings.getJMboolean(JMConfig.BROWSERINSTANCE1)) {

                        // status = "true";
                        addObj(temp1 + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("*true"));

                    } else {

                        // status = "false";
                        addObj(temp1 + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("*false"));

                    }

                    // addObj(temp1 + "*" + status);
                }

            } catch (Exception exc) {

                addObj(empty);

            }

        }

        addObj("");

    }

    /**
     * Write the FTP Client location out to .jamocha.rc
     */
    public void writeFTPClient() {

        addObj(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("[") + JMConfig.FTPCLIENT + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("]"));

        // addObj the configured client
        try {

            // String temp1 = new String(settings.getBrowser("FTPClient"));
            // String temp1 = new String(settings.getBrowser(JMConfig.FTPCLIENT));
            String temp1 = settings.getJMString(JMConfig.FTPCLIENT);

            if ("".equals(temp1)) {
                temp1 = empty;
            }

            addObj(temp1);

        } catch (Exception exc) {

            addObj(empty);

        }

        addObj(space);

    }

    /**
     * Write the location of the E-mail client out to .jamocha.rc
     */
    public void writeEMailClient() {

        addObj(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("[") + JMConfig.EMAILCLIENT + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("]"));

        try {

            // String temp1 = settings.getBrowser("EMailClient");
            String temp1 = settings.getJMString(JMConfig.EMAILCLIENT);

            if ("".equals(temp1)) {
                temp1 = empty;
            }

            addObj(temp1);

        } catch (Exception exc) {

            addObj(empty);

        }

        addObj(space);

    }

    public void jMTimers() {

        addObj(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("[") + JMConfig.TIMERS + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("]"));

        // if (settings.getTimersVisible()) {
        if (settings.getJMboolean(JMConfig.TIMERSVISIBLE)) {

            addObj(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("true"));

            // writeRect(Timers.timerFrame.getBounds());
        } else {

            addObj(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("false"));

            // writeRect(settings.getTimers());
        }

        // writeRect(settings.getTimers());
        final Rectangle tempRect = settings.getJMRectangle(JMConfig.TIMERS);

        if (tempRect != null) {

            writeRect(tempRect);

        }

        addObj(space);

    }

    /*
     public void JMSyncWindows() {
     addObj("[SyncWindows]");
     // addObj(settings.getSyncWindows() + "");
     addObj(settings.getJMboolean(JMConfig.SYNCWINDOWS) + "");
     addObj(space);
     }
     */
    /*
     public void JMTFKeyEmu() {
     addObj("[TFKeyEmu]");
     // addObj(settings.getTFKeyEmu() + "");
     addObj(settings.getJMboolean(JMConfig.TFKEYEMU) + "");
     addObj(space);
     }
     */
    /**
     * Write the user's preference to using Unicode or ASCII to .jamocha.rc
     */

    /*
     public void JMUseUnicode() {
     addObj("[Unicode]");
     // addObj(settings.getUseUnicode() + "");
     addObj(settings.getJMboolean(JMConfig.USEUNICODE) + "");
     addObj(space);
     }
     */
    /**
     * Write the double-buffer setting
     */

    /*
     public void JMDoubleBuffer() {
     addObj("[DoubleBuffer]");
     //addObj(settings.getDoubleBuffer() + "");
     addObj(settings.getJMboolean(JMConfig.DOUBLEBUFFER) + "");
     addObj(space);
     }
     */
    /**
     * Write out the view state, split (true) or combined (false)
     */

    /*
     public void JMSplitView() {
     addObj("[SplitView]");
     // addObj(settings.getSplitView() + "");
     addObj(settings.getJMboolean(JMConfig.SPLITVIEW) + "");
     addObj(space);
     }
     */
    /**
     * Write out the &quot;Release pause on copy" option
     */

    /*
     public void JMReleasePause() {
     addObj("[ReleasePause]");
     // addObj(settings.getReleasePause() + "");
     addObj(settings.getJMboolean(JMConfig.RELEASEPAUSE) + "");
     addObj(space);
     }
     */
    /**
     * Write out the Alt-focus option
     */

    /*
     public void JMAltFocus() {
     addObj("[AltFocus]");
     // addObj(settings.getAltFocus() + "");
     addObj(settings.getJMboolean(JMConfig.ALTFOCUS) + "");
     addObj(space);
     }
     */
    /**
     * Serialise the user's macros to the .macros.rc file. This replaces the old
     * method of WriteMacros
     */
    public void serialiseMacros() {

        // This new method (as of 12/09/98) will serialise the macro
        // labels and their associated actions
        try {

            // FileOutputStream outputFile = new FileOutputStream(settings.getUserDirectory() + ".macros.rc");
            final FileOutputStream outputFile = new FileOutputStream(settings.getJMString(JMConfig.USERDIRECTORY) + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString(".macros.rc"));

            final ObjectOutputStream sStream = new ObjectOutputStream(outputFile);

            // sStream.writeObject(settings.getMacroLabels());
            // sStream.writeObject(settings.getMacroDefs());
            sStream.writeObject(settings.getJMStringArray(JMConfig.MACROLABELS));

            sStream.writeObject(settings.getJMStringArray(JMConfig.MACRODEFS));

            sStream.flush();

        } catch (Exception exc) {

            System.out.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("(JMWriteRC)_Macro_serialization_error_") + exc);

        }

    }

    /**
     * Write out the user's preference to Auto Focus Input (the MUD client
     * automagicall shifting focus to the input box (Databar) when they type
     */

    /*
     private void JMWriteAutoFocusInput() {
     addObj("[AutoFocusInput]");
     // addObj(settings.getAutoFocusInput() + "");
     addObj(settings.getJMboolean(settings.AUTOFOCUSINPUT) + "");
     addObj("");
     // jmSections.put("AutoFocusInput", "false");
     }
     */
    /**
     * Write out our Local Echo variable
     */

    /*
     private void JMWriteLocalEcho() {
     addObj("[LocalEcho]");
     // addObj(settings.isLocalEchoEnabled() + "");
     addObj(settings.getJMboolean(JMConfig.LOCALECHO) + "");
     addObj("");
     // jmSections.put(settings.LOCALECHO, "false");
     }
     */
    /**
     * Write out the proxy information to the .jamocha.rc file
     */
    private void jMWriteProxy() {

        addObj(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("[") + JMConfig.PROXY + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("]"));

        // if (settings.getProxy()) {
        if (settings.getJMboolean(JMConfig.PROXY)) {

            addObj(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("true"));

        } else {

            addObj(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("false"));

        }

        // if (settings.getProxyHost().equals("null") || settings.getProxyHost().equals("")) {
        if ((settings.getJMString(JMConfig.PROXYHOST)).equals(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("null")) || (settings.getJMString(JMConfig.PROXYHOST)).equals("")) {

            addObj(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("null"));

        } else {

            // addObj(settings.getProxyHost());
            addObj(settings.getJMString(JMConfig.PROXYHOST));

            // addObj(settings.getProxyPort() + "");
            addInt(settings.getJMint(JMConfig.PROXYPORT));

        }

        addObj("");

        // jmSections.put(settings.PROXY, "false");
    }

    /*
     private void JMWriteLastMU() {
     addObj("[LastMU]");
     // addObj(settings.getLastMU());
     addObj(settings.getJMString(JMConfig.LASTMU));
     addObj("");
     }
     private void JMWriteLogPath() {
     addObj("[LogPath]");
     addObj(settings.getJMString(JMConfig.LOGPATH));
     addObj("");
     }
     */
    /**
     * Creates the custom palette
     */
    private void jMWriteCustomPalette() {
        logger.debug("JMWriteRC.jMWriteCustomPalette.");

        // final java.util.prefs.Preferences prefs = java.util.prefs.Preferences.userNodeForPackage(this.getClass());
        // logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("JMWriteRC_writing_custom_palette_from_") + prefs);
        final Color[] newPal = (Color[]) settings.getJMObject(JMConfig.CUSTOMPALETTE);

        logger.debug("jMWriteCustomPalette now has colour palette: " + newPal);

        if (newPal == null) {
            logger.debug(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("JMWriteRC:_The_custom_palette_was_null"));
        } else {
            if (newPal.length < 16) {
                logger.debug("JMWrite.jMWriteCustomPalette does not have a palette with enough entries");
            } else {
                for (int i = 0; i < 16; i++) {
                    if (newPal[i] != null) {
                        logger.debug("JMWriteRC.jMWriteCustomPalette() writing out entry " + i);
                        logger.debug("JMWriteRC.jMWriteCustomPalette entry: " + newPal[i]);
                        logger.debug("JMWriteRC.jMWriteCustomPalette() writing out in RGB " + newPal[i].getRGB());
                    // prefs.putInt(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Palette") + i, newPal[i].getRGB());
                        // prefs.putInt(JMConfig.CUSTOMPALETTE + i, newPal[i].getRGB());
                        addObj('[' + JMConfig.CUSTOMPALETTE + i + ']');
                        addInt(newPal[i].getRGB());
                        addObj("");
                    } else {
                        logger.debug("JMWriteRC.jMWRiteCustomPalette() entry " + i + " is null");
                    }
                }
            }
        }
    }

    private void jMWriteWhatsNew() {
        final java.util.prefs.Preferences prefs = java.util.prefs.Preferences.userNodeForPackage(this.getClass());

        prefs.putBoolean(JMConfig.SHOWNEW, settings.getJMboolean(JMConfig.SHOWNEW));
        prefs.put("LastVersion", AboutBox.FULLVERNUM);
    }

    private void jMWriteEntry(final String title, final String value) {

        addObj(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("[") + title + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("]"));

        addObj(value);

        // jMWriteEntry("[LogPath]", settings.getJMString(JMConfig.LOGPATH));
        addObj("");

    }

    private void jMWriteVariables() {

        final Hashtable vars = settings.getAllVariables();

        String name, value;

        final java.util.Enumeration keys = vars.keys();

        while (keys.hasMoreElements()) {

            name = (String) keys.nextElement();

            value = vars.get(name).toString();

            addObj(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("/set_") + name + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("=") + value);

        }

    }

    private void jMWriteDefinitions() {

        final Hashtable vars = settings.getAllDefinitions();

        String name, value;

        final java.util.Enumeration keys = vars.keys();

        while (keys.hasMoreElements()) {

            name = (String) keys.nextElement();

            value = vars.get(name).toString();

            addObj(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("/def_") + name + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("=") + value);

        }

    }

    /**
     * Build the new .jamocha.rc file, first filling a vector with the necessary
     * information sections, and then writing it out to the actual file
     */
    private void jMBuildNewRC() {

        // Build the new .rc vector
        // First, empty the newFile vector
        newFile.removeAllElements();

        /* Version identifier */
        addObj(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("#_JamochaMUD_INI_file_version_1.1"));

        writeWorlds();

        writeForegroundColour();

        writeBackgroundColour();

        writeMainWindow();

        writeDataBar();

        writeMacroWindow();

        writeFontFace();

        jMWriteProxy();

        // addObj "constant" items
        jMTimers();

        // JMWriteAutoFocusInput();
        final String[] options = {
            JMConfig.AUTOFOCUSINPUT,
            JMConfig.AUTOLOGGING,
            JMConfig.LOCALECHO,
            JMConfig.SYNCWINDOWS,
            JMConfig.USEUNICODE,
            JMConfig.TFKEYEMU,
            JMConfig.DOUBLEBUFFER,
            JMConfig.SPLITVIEW,
            JMConfig.RELEASEPAUSE,
            JMConfig.ALTFOCUS,
            JMConfig.ANTIALIAS,
            JMConfig.LOWCOLOUR,
            JMConfig.DIVIDERLOCATION,
            JMConfig.AUTOLOGPATH,
            JMConfig.LOGFILENAMEFORMAT
        };

        final int len = options.length;

        // Write out the single-line entries
        for (int i = 0; i < len; i++) {
            jMWriteEntry(options[i], settings.getJMString(options[i]));
            if (DEBUG) {
                System.err.println("JMWriteRC writing " + options[i] + " -> " + settings.getJMString(options[i]));
            }
        }

//        jMWriteEntry(JMConfig.AUTOFOCUSINPUT, settings.getJMString(JMConfig.AUTOFOCUSINPUT));
//        jMWriteEntry(JMConfig.AUTOLOGGING, settings.getJMString(JMConfig.AUTOLOGGING));
//        jMWriteEntry(JMConfig.LOCALECHO, settings.getJMString(JMConfig.LOCALECHO));
//        jMWriteEntry(JMConfig.SYNCWINDOWS, settings.getJMString(JMConfig.SYNCWINDOWS));
//        jMWriteEntry(JMConfig.USEUNICODE, settings.getJMString(JMConfig.USEUNICODE));
//        jMWriteEntry(JMConfig.TFKEYEMU, settings.getJMString(JMConfig.TFKEYEMU));
//        jMWriteEntry(JMConfig.DOUBLEBUFFER, settings.getJMString(JMConfig.DOUBLEBUFFER));
//        jMWriteEntry(JMConfig.SPLITVIEW, settings.getJMString(JMConfig.SPLITVIEW));
//        jMWriteEntry(JMConfig.RELEASEPAUSE, settings.getJMString(JMConfig.RELEASEPAUSE));
//        jMWriteEntry(JMConfig.ALTFOCUS, settings.getJMString(JMConfig.ALTFOCUS));
//        jMWriteEntry(JMConfig.ANTIALIAS, settings.getJMString(JMConfig.ANTIALIAS));
//        jMWriteEntry(JMConfig.LOWCOLOUR, settings.getJMString(JMConfig.LOWCOLOUR));
//        jMWriteEntry(JMConfig.DIVIDERLOCATION, settings.getJMString(JMConfig.DIVIDERLOCATION));
//        jMWriteEntry(JMConfig.AUTOLOGPATH, settings.getJMString(JMConfig.AUTOLOGPATH));
//        jMWriteEntry(JMConfig.LOGFILENAMEFORMAT, settings.getJMString(JMConfig.LOGFILENAMEFORMAT));
        // now add the External programs
        writeBrowser(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("1"));
        writeBrowser(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("2"));
        writeFTPClient();
        writeEMailClient();

        // Some system defined variables
        jMWriteEntry(JMConfig.LASTMU, settings.getJMString(JMConfig.LASTMU));

        jMWriteEntry(JMConfig.LOGPATH, settings.getJMString(JMConfig.LOGPATH));

        jMWriteEntry(JMConfig.HISTORYLENGTH, settings.getJMString(JMConfig.HISTORYLENGTH));
        
        jMWriteEntry(JMConfig.COMMANDS_FILE, settings.getJMString(JMConfig.COMMANDS_FILE));
        
        jMWriteEntry(JMConfig.DICT_QUERY_URL, settings.getJMString(JMConfig.DICT_QUERY_URL));
        
        jMWriteEntry(JMConfig.PROMPT_STR, settings.getJMString(JMConfig.PROMPT_STR));

        // Now some user defined variables and definitions
        jMWriteVariables();

        jMWriteDefinitions();

        /**
         * Write out any Java 1.2-specific items
         */
        if (settings.getJMboolean(JMConfig.USESWING)) {

            jMWriteCustomPalette();
            jMWriteWhatsNew();

        }

    }

    /**
     * A method to add Objects (as strings) to the newFile vector
     */
    private void addObj(final Object newForVector) {

        logger.debug("JMWriteRC.addObj() received: " + newForVector);
        newFile.addElement(newForVector.toString());

    }

    /**
     * Any primitive integers must first be wrappered and then sent to
     * 'addObj(Object)'
     */
    private void addInt(final int convertInt) {
        // addObj(new Integer(convertInt));
        // addObj(Integer.valueOf(convertInt));
        addObj(convertInt);
    }

    /**
     * A common method to write 'rectangles' to the .rc file
     */
    private void writeRect(final Rectangle source) {
        addInt((source.getLocation()).x);
        addInt((source.getLocation()).y);
        addInt(source.getSize().width);
        addInt(source.getSize().height);
    }
}
