/**
 * JMTFKeys emulates key controls of TinyFugue in JamochaMUD
 * $Id: JMTFKeys.java,v 1.1 2009/03/17 02:41:17 jeffnik Exp $
 */
/* JamochaMUD, a Muck/Mud client program
 * Copyright (C) 1998-2009 Jeff Robinson
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. *
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
package anecho.JamochaMUD.TinyFugue;

import anecho.JamochaMUD.*;
import java.awt.event.KeyEvent;

/**
 * 
 * @author Jeff Robinson
 */
final public class JMTFKeys {

    /** This method searches through the submitted string, checking for any
     * TinyFugue keystrokes.  If one is found, we can return 'true', so that the
     * rest of the event is not processed by DataIn.java
     * @version $Id: JMTFKeys.java,v 1.1 2009/03/17 02:41:17 jeffnik Exp $
     * @author Jeff Robinson
     */
    private static JMTFKeys _instance;  // We'll try playing with a Singleton!
    private static final boolean DEBUG = false;
    
    private JMTFKeys() {
    }

    // For lazy initialization
    /**
     * 
     * @return 
     */
    public static synchronized JMTFKeys getInstance() {
        if (_instance == null) {
            _instance = new JMTFKeys();
        }
        return _instance;

    }
    // We'll declare this variable here, as it is used so often
    /** The variable for the DataIn instance */
    private static DataIn typeHere;

    /**
     * 
     * @return 
     * @param keyStroke 
     */

    public static boolean jmTFKeyStroke(final int keyStroke) {

        if (DEBUG) {
            System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString("JMTFKeys.jmTFKeyStroke_entered."));
        }

        final JMConfig settings = JMConfig.getInstance();

        typeHere = settings.getDataInVariable();

        boolean retCode = true;

        switch (keyStroke) {

            case KeyEvent.VK_B:

                // Go left, to the beginning of the word

                findWordStart();

                break;

            case KeyEvent.VK_F:

                // Go right, to end of the word

                findWordEnd();

                break;

            case KeyEvent.VK_K:

                // Delete from cursor to end of line

                dEOL();

                break;

            case KeyEvent.VK_N:

                // Was 'Recall next input line', but now shows the pop-up
                typeHere.showPopup(null, -1, -1);

                break;

            case KeyEvent.VK_P:
                // Recall previous line
                previousLine();
                break;

            case KeyEvent.VK_Q:
                // resets the 'suspend' flag on the display (if true)
                // final CHandler target = settings.getConnectionHandler();
                final CHandler target = CHandler.getInstance();
                final MuSocket mSock = target.getActiveMUHandle();

                if (mSock.isPaused()) {

                    // This spools out any paused text

                    mSock.spoolText();

                }

                break;

            case KeyEvent.VK_S:

                // 'Suspends' the display.  (Halts scrollback)

                MuckMain.getInstance().pauseText();

                break;

            case KeyEvent.VK_T:

                // Transposes characters

                transposeChars();

                break;

            case KeyEvent.VK_U:

                // erase the entire line at the cursor's position

                eraseLine();

                break;

            case KeyEvent.VK_W:

                // erase the previous word

                erasePreviousWord();

                break;

            default:

                retCode = false;

        }

        return retCode;

    }

    /** Delete from the cursor to the end of the line */
    public static void dEOL() {
        if(typeHere == null) {
            typeHere = JMConfig.getInstance().getDataInVariable();
        }

        final String wText = typeHere.getText();
        typeHere.setText(wText.substring(0, typeHere.getCaretPosition()));

    }

    /** Erase the entire line at the cursor's position */
    public static void eraseLine() {
        if(typeHere == null) {
            typeHere = JMConfig.getInstance().getDataInVariable();
        }

        String text = typeHere.getText();
        typeHere.setText(text.substring(typeHere.getCaretPosition()));
        typeHere.setCaretPosition(0);
    }

    /** Erase the word proceeding the cursor */
    protected static void erasePreviousWord() {

        // Erase the word preceeding the cursor

        // String wText = new String(DataIn.dataText.getText());

        final String wText = typeHere.getText();



        // int cursor = DataIn.dataText.getCaretPosition();

        final int cursor = typeHere.getCaretPosition();



        // Do a check to see if the cursor is sitting at the 'zero' position

        if (cursor < 1) {

            return;

        }



        // Now search backwards from the cursor's position till we find the

        // first space back, and delete the area in between

        int start = cursor - 1;



        while (start >= 0 && wText.charAt(start) != ' ') {

            start--;

        }



        // Now that we have the beginning and end, we'll assemble a new String

        final StringBuffer nText = new StringBuffer(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString(""));



        if (start > 0) {

            nText.append(wText.substring(0, start));

        }



        if (cursor < wText.length()) {

            nText.append(wText.substring(cursor, wText.length()));

        }



        // Now set the text to this new String

        // DataIn.dataText.setText(nText.toString().trim());

        typeHere.setText(nText.toString().trim());



    }

    /** Find the beginning of the first word to the left of the cursor */
    public static void findWordStart() {

        // int cursor = DataIn.dataText.getCaretPosition() - 1;

        int cursor = typeHere.getCaretPosition() - 1;

        // String wText = DataIn.dataText.getText();

        final String wText = typeHere.getText();



        // Check to see if we're at the beginning of the line

        if (cursor < 1) {

            return;

        }



        while (cursor > 0 && wText.charAt(cursor) != ' ') {

            cursor--;

        }





        // DataIn.dataText.setCaretPosition(cursor);

        typeHere.setCaretPosition(cursor);



    }

    /** Find the end of the word to the right of the cursor */
    public static void findWordEnd() {

        // int cursor = DataIn.dataText.getCaretPosition();

        int cursor = typeHere.getCaretPosition();

        // String wText = DataIn.dataText.getText();

        final String wText = typeHere.getText();





        while (cursor < wText.length() && wText.charAt(cursor) != ' ') {

            cursor++;

        }



        if (cursor < wText.length()) {

            cursor++;

        }



        // DataIn.dataText.setCaretPosition(cursor);

        typeHere.setCaretPosition(cursor);



    }

    /** Recall the previously input line (if any)	*/
    private static void previousLine() {
        // DataIn.setFromHistory(-1);
        typeHere.setFromHistory(-1);
    }

    /** Find the current row that the cursor is on */
    private static int rowNumber(final int cursor) {

        // First, we'll locate the position of the cursor, and the

        // beginning and end points of its line

        // int width = DataIn.dataText.getColumns();

        final int width = typeHere.getColumns();



        int row = 1;



        while ((width * row) < cursor) {

            row++;

        }



        return (row - 1);

    }

    /** Transpose the character the caret is on with the one prior to it */
    private static void transposeChars() {

        // int cursor = DataIn.dataText.getCaretPosition();

        final int cursor = typeHere.getCaretPosition();

        // String wText = DataIn.dataText.getText();

        final String wText = typeHere.getText();



        // Check to see if the cursor is in a 'legal' position

        if (cursor < 1 || cursor == wText.length()) {

            return;

        }



        final StringBuffer fText = new StringBuffer(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/TinyFugue/TinyFugueBundle").getString(""));

        fText.append(wText.substring(0, cursor - 1));

        fText.append(wText.charAt(cursor));

        fText.append(wText.charAt(cursor - 1));

        fText.append(wText.substring(cursor + 1, wText.length()));



        // DataIn.dataText.setText(fText.toString().trim());

        // DataIn.dataText.setCaretPosition(cursor);

        typeHere.setText(fText.toString().trim());

        typeHere.setCaretPosition(cursor);



    }

    /* show a dialogue of our currently available commands
     */
    /**
     * 
     * @param parent 
     * @param useSwing 
     */
    public static void showCommands(final java.awt.Frame parent, final boolean useSwing) {
        final StringBuffer message = new StringBuffer();

        final String ctrlPlus = KeyEvent.getKeyText(KeyEvent.VK_CONTROL) + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("_+_");

        message.append("\n\n");
        message.append(ctrlPlus + KeyEvent.getKeyText(KeyEvent.VK_B) + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("_-_") + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("jump_left_to_beginning_of_word") + '\n');
        message.append(ctrlPlus + KeyEvent.getKeyText(KeyEvent.VK_F) + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("_-_") + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("jump_to_end_of_word") + '\n');
        message.append(ctrlPlus + KeyEvent.getKeyText(KeyEvent.VK_K) + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("_-_") + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("delete_from_the_cursor_to_end_of_line") + '\n');
        message.append(ctrlPlus + KeyEvent.getKeyText(KeyEvent.VK_N) + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("_-_") + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("show_command_history") + '\n');
        message.append(ctrlPlus + KeyEvent.getKeyText(KeyEvent.VK_P) + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("_-_") + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("recall_previous_command") + '\n');
        message.append(ctrlPlus + KeyEvent.getKeyText(KeyEvent.VK_Q) + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("_-_") + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("un-suspend_output") + '\n');
        message.append(ctrlPlus + KeyEvent.getKeyText(KeyEvent.VK_S) + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("_-_") + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Suspend_output") + '\n');
        message.append(ctrlPlus + KeyEvent.getKeyText(KeyEvent.VK_T) + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("_-_") + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Transpose_characters") + '\n');
        message.append(ctrlPlus + KeyEvent.getKeyText(KeyEvent.VK_U) + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("_-_") + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Erase_entire_line_at_cursor's_position") + '\n');
        message.append(ctrlPlus + KeyEvent.getKeyText(KeyEvent.VK_W) + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("_-_") + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Erase_previous_word") + '\n');

        if (useSwing) {
            javax.swing.JOptionPane.showMessageDialog(parent,
                    message,
                    java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("TinyFugue-style_commands"),
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
        } else {
            final anecho.gui.OKBox display = new anecho.gui.OKBox(parent);
            display.setTitle(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("TinyFugue-style_commands"));
            display.setModal(true);
            display.append(message.toString());
            display.showCentered();
        }
    }
}
