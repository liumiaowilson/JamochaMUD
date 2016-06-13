/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package anecho.gui;

/**
 *
 * @author jeffnik
 */
public class FlashThread extends Thread {

    // length of time between changing
    long flashInterval;

    FlashThread(long interval) {
        flashInterval = interval;
    }

    public void run() {
        
    }
}
