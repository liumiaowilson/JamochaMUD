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

import anecho.JamochaMUD.DataIn;
import anecho.JamochaMUD.JMConfig;

/**
 * This class will handle the parsing of /dokey commands
 * $Id: DoKey.java,v 1.2 2009/03/29 03:22:06 jeffnik Exp $
 * @author jeffnik
 */
public class DoKey {
    /**
     * This list of TF commands comes from http://www.zombii.org/tf-50b8-help/commands/dokey.html
     * Name		Default binding	  Action
     * ----		---------------	  --------
     * BSPC		(stty), ^H, ^?	  Backspace
     * BWORD		(stty), ^W	  Delete previous word
     * DLINE		(stty), ^U	  Delete entire line
     * REFRESH          (stty), ^R	  Refresh line
     * LNEXT		(stty), ^V	  Ignore any binding next key might have
     * UP		(none)		  Cursor up
     * DOWN		(none)		  Cursor down
     * RIGHT		key_right	  Cursor right
     * LEFT		key_left	  Cursor left
     * NEWLINE          ^J, ^M		  Execute current line
     * RECALLB          ^P		  Recall previous input line
     * RECALLF          ^N		  Recall next input line
     * RECALLBEG	^[<		  Recall first input line
     * RECALLEND	^[>		  Recall last input line
     * SEARCHB          ^[p		  Search backward in input history
     * SEARCHF          ^[n		  Search forward in input history
     * SOCKETB          ^[b		  Switch to previous socket
     * SOCKETF          ^[f		  Switch to next socket
     * DWORD		^[d		  Delete word
     * DCH		^D		  Delete character under cursor
     * REDRAW           ^L		  Redraw screen
     * CLEAR		^[^L		  Clear screen
     * HOME		^A		  Go to beginning of line
     * END		^E		  Go to end of line
     * WLEFT		^B		  Go left, to beginning of word
     * WRIGHT           ^F		  Go right, to end of word
     * DEOL		^K		  Delete from cursor to end of line
     * PAUSE		^S		  Pause screen
     * PAGE		key_tab		  Scroll 1 page forward ("more")
     * PAGEBACK         (none)		  Scroll 1 page backward ("more")
     * HPAGE		^X]		  Scroll half page forward ("more")
     * HPAGEBACK	^X[		  Scroll half page backward ("more")
     * PGDN		key_pgdn	  /dokey_hpage
     * PGUP		key_pgup	  /dokey_hpageback
     * LINE		^[^N		  Scroll forward 1 line ("more")
     * LINEBACK         ^[^P		  Scroll backward 1 line ("more")
     * FLUSH		^[j		  Jump to end of scroll buffer
     * SELFLUSH         ^[J		  Show lines with attributes,
     * and jump to end of buffer
     * @param command The command to be translated into a key combination
     */
    public void doKey(final String command) {
        di = JMConfig.getInstance().getDataInVariable();
        
        final int space = command.indexOf(' ');
        
        if (space < 0) {
            return;
        }
        
        final String upperCmd = command.substring(space).trim().toUpperCase();
        
        if (DEBUG) {
            System.err.println("DoKey has received the command: " + upperCmd);
        }
        // This seems like an inefficient way of doing this, so I'm open to suggestions
             if (upperCmd.equals("BSPC")) {
                 backspace();
             } else if (upperCmd.equals("BWORD")) {
                 deletePrevWord();
             } else if (upperCmd.equals("DLINE")) {
                 deleteEntireLine();
             } else if (upperCmd.equals("REFRESH")) {
                 // Fix Me XXX - not implemented yet
             } else if (upperCmd.equals("LNEXT")) {
                 // Fix Me XXX - not implemented yet
             } else if (upperCmd.equals("UP")) {
                 // Fix Me XXX - not implemented yet
             } else if (upperCmd.equals("DOWN")) {
                 // Fix Me XXX - not implemented yet
             } else if (upperCmd.equals("RIGHT")) {
                 // Fix Me XXX - not implemented yet
             } else if (upperCmd.equals("LEFT")) {
                 // Fix Me XXX - not implemented yet
             } else if (upperCmd.equals("NEWLINE")) {
                 newLine();
             } else if (upperCmd.equals("RECALLB")) {
                 recallBack();
             } else if (upperCmd.equals("RECALLF")) {
                 recallForward();
             } else if (upperCmd.equals("RECALLBEG")) {
                 recallBeginning();
             } else if (upperCmd.equals("RECALLEND")) {
                 recallEnd();
             } else if (upperCmd.equals("SEARCHB")) {
                 // Fix Me XXX - not implemented yet
             } else if (upperCmd.equals("SEARCHF")) {
                 // Fix Me XXX - not implemented yet
             } else if (upperCmd.equals("SOCKETB")) {
                 socketBack();
             } else if (upperCmd.equals("SOCKETF")) {
                 socketForward();
             } else if (upperCmd.equals("DWORD")) {
                 // Fix Me XXX - not implemented yet
             } else if (upperCmd.equals("DCH")) {
                 // Fix Me XXX - not implemented yet
             } else if (upperCmd.equals("REDRAW")) {
                 // Fix Me XXX - not implemented yet
             } else if (upperCmd.equals("CLEAR")) {
                 clear();
             } else if (upperCmd.equals("HOME")) {
                 home();
             } else if (upperCmd.equals("END")) {
                 end();
             } else if (upperCmd.equals("WLEFT")) {
                 wLeft();
             } else if (upperCmd.equals("WRIGHT")) {
                 wRight();
             } else if (upperCmd.equals("DEOL")) {
                 deleteToEOL();
             } else if (upperCmd.equals("PAUSE")) {
                 pause();
             } else if (upperCmd.equals("PAGE")) {
                 // Fix Me XXX - not implemented yet
             } else if (upperCmd.equals("PAGEBACK")) {
                 // Fix Me XXX - not implemented yet
             } else if (upperCmd.equals("HPAGE")) {
                 // Fix Me XXX - not implemented yet
             } else if (upperCmd.equals("HPAGEBACK")) {
                 // Fix Me XXX - not implemented yet
             } else if (upperCmd.equals("PGDN")) {
                 // Fix Me XXX - not implemented yet
             } else if (upperCmd.equals("PGUP")) {
                 // Fix Me XXX - not implemented yet
             } else if (upperCmd.equals("LINE")) {
                 // Fix Me XXX - not implemented yet
             } else if (upperCmd.equals("LINEBACK")) {
                 // Fix Me XXX - not implemented yet
             } else if (upperCmd.equals("FLUSH")) {
                 // Fix Me XXX - not implemented yet
             } else if (upperCmd.equals("SELFLUSH")) {
                 // Fix Me XXX - not implemented yet
             } else {
            // Fix Me XXX - We've fallen through!
             }

    }
    
    /** Send a back-space command to the input window */
    private void backspace() {
        
    }

    /** Delete the previous word */
    private void deletePrevWord() {
        JMTFKeys.erasePreviousWord();
    }

    /** Delete the entire line */
    private void deleteEntireLine() {
        JMTFKeys.eraseLine();
    }
    
    /** Send the current line (like hitting ENTER) */
    private void newLine() {
        // Do we have concerns that the displayed MU* might not be the one the script is called from?
        // Fix Me XXX
    }
    
    /** Recall the previous item in history */
    private void recallBack() {
        di.scrollHistory(DataIn.UP);
    }
    
    /** Recall the next item in history */
    private void recallForward() {
        di.scrollHistory(DataIn.DOWN);
    }
    
    /** Move back one connection */
    private void socketBack() {
        anecho.JamochaMUD.MuckMain.getInstance().advanceMU(anecho.JamochaMUD.MuckMain.PREVIOUS);
    }
    
    /** Move forward one connection */
    private void socketForward() {
        anecho.JamochaMUD.MuckMain.getInstance().advanceMU(anecho.JamochaMUD.MuckMain.NEXT);
    }
    
    /** Recall the first item in the history */
    private void recallBeginning() {
        di.historyBegin();
    }
    
    /** Recall the last item in the history */
    private void recallEnd() {
        di.historyEnd();
    }

    /**
     * Delete from the position of the cursor to the end of the line
     */
    private void deleteToEOL() {
        JMTFKeys.dEOL();
    }
    
    /**
     * Clear the current screen
     */
    private void clear() {
        
    }
    
    /**
     * Move the cursor to the beginning of the line
     */
    private void home() {
        
    }
    
    /**
     * Move the cursor to the end of the line
     */
    private void end() {
        
    }
    
    /**
     * Go left, to beginning of word
     */
    private void wLeft() {
        JMTFKeys.findWordStart();
    }
    
    /**
     * Go right, to end of word
     */
    private void wRight() {
        JMTFKeys.findWordEnd();
    }
    
    /**
     * Pause output
     */
    private void pause() {
        anecho.JamochaMUD.MuckMain.getInstance().pauseText();
    }
    
    /**
     * The DataIn variable, which we will probably be using often.
     */
    private transient DataIn di;
    
    /**
     * This variable enables and disables debugging output
     */
    private static final boolean DEBUG = false;
}
