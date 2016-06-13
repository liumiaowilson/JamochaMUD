/*
 * JMFancyTabbedPane.java
 *
 * Created on March 30, 2006, 9:16 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package anecho.gui;

import java.awt.Component;
import java.awt.SystemColor;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * This class is an implementation of JTabbedPane that allows the program to
 * easily change the colour of the "tab". Future work on this class will allow
 * for the tab to blink, which can be useful for notifying users of activity in
 * tabs not currently being viewed.
 *
 * @author J. Robinson
 */
public class JMFancyTabbedPane extends javax.swing.JTabbedPane implements ChangeListener {

    /**
     * Creates a new instance of JMFancyTabbedPane
     */
    public JMFancyTabbedPane() {
        super();
        this.addChangeListener(this);
    }

    /**
     * This constructor allows takes an index number to determine where (in
     * regard to other tabs) this tab is initially displayed.
     *
     * @param tabPlacement An index representing the location of this tab in
     * relation to existing tabs.
     */
    public JMFancyTabbedPane(int tabPlacement) {
        super(tabPlacement);
        this.addChangeListener(this);
    }

    /**
     * This constructor allows specification of the index of this tab as well as
     * the LayoutPolicy.
     *
     * @param tabPlacement An index indicating the positioning of this tab in
     * the existing lay-out.
     * @param tabLayoutPolicy The layout policy for the given tab.
     */
    public JMFancyTabbedPane(int tabPlacement, int tabLayoutPolicy) {
        super(tabPlacement, tabLayoutPolicy);
        this.addChangeListener(this);
    }

    /**
     * This class is used to turn the "flashing" feature of this tab on or off.
     *
     * @param comp The component that should be affected.
     * @param state <CODE>true</CODE> - start flashing of this tab
     * <CODE>false</CODE> - this tab should not be flashing
     */
    public void flashTab(final Component comp, final boolean state) {
        final int target = this.indexOfComponent(comp);

        // Check to make certain the component hasn't been removed by the time
        // we get to this method
        if (target > -1) {
            this.flashTab(target, state);
        }
    }

    /**
     * class Enable or disable tab flashing. Currently we'll just swap the
     * colours until we can set-up a proper way to blink them. Fix Me XXX
     *
     * @param target The tab that should be set to flash.
     * @param state <CODE>true</CODE> - flash this tab <CODE>false</CODE> - do
     * not flash this tab
     */
    public void flashTab(final int target, final boolean state) {
        if (target < 0 || target >= this.getTabCount()) {
            // Invalid target.
            return;
        }

        final SystemColor fgColor = SystemColor.controlText;
        final SystemColor bgColor = SystemColor.control;

        if (state) {

            // Start the blinking thread if not already started  Fix Me XXX
            // Reverse the colours
            if (DEBUG) {
                System.err.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("JMFancyTabbedPane_background:_") + fgColor);
                System.err.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("JMFancyTabbedPane_foreground:_") + bgColor);
            }
            this.setBackgroundAt(target, fgColor);
            this.setForegroundAt(target, bgColor);
        } else {

            // Stop the blinking thread.  Fix Me XXX
            // Revert the colours to normal
            if (DEBUG) {
                System.err.println("JMFancyTabbedPane.flashTab(int, boolean) setting:");
                System.err.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("JMFancyTabbedPane_background:_") + bgColor);
                System.err.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("JMFancyTabbedPane_foreground:_") + fgColor);
                System.err.println("Target: " + target);
            }

            this.setBackgroundAt(target, bgColor);
            this.setForegroundAt(target, fgColor);
        }

    }

    /**
     * Return whether the tab is actively flashing or not This method is
     * currently a stub.
     *
     * @param comp The component to query the state of
     * @return
     */
    public boolean isFlashing(final Component comp) {
        boolean flashing;

        final int target = this.indexOfComponent(comp);
        flashing = this.isFlashing(target);
        return flashing;
    }

    public boolean isFlashing(final int comp) {
        return false;
    }

    /**
     * This method is used for enabling and disabling automatic tab watching.
     *
     * @param state Enables or disablclasses tab watchingclass
     */
    public void setAutoWatchTab(final boolean state) {
        autoWatch = state;
    }

    /**
     * This method returns whether automatic tab watching is enabled
     *
     * @return
     */
    public boolean isAutoWatchTab() {
        return autoWatch;
    }

    public void stateChanged(final ChangeEvent evt) {
        if (DEBUG) {
            System.err.println("JMFancyTabbedPane.stateChanged " + evt);
        }

        if (autoWatch) {
            // Get current tab

            final int sel = this.getSelectedIndex();
            this.flashTab(sel, false);
        }
    }
    /**
     * Enables and disables debugging output
     */
    private static final boolean DEBUG = false;
    /**
     * This variable takes care of turning of "flashing" tabs. <code>true</code>
     * - Automatically turn off "flashing" if on the activated tab
     * <code>false</code> - Do not monitor flashing
     */
    private transient boolean autoWatch = true;
}
