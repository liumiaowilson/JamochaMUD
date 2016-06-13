/** Create a Java 1.1.x compliant "tab panel" based on a simple card-layout.

 * This file is intended simply as a transition between Java 1.1.x and Java 2

 * GUI widgets.

 * $Id: JMTabPanel.java,v 1.3 2010/05/10 02:38:04 jeffnik Exp $

 */



package anecho.JamochaMUD.legacy;



import java.awt.Panel;

import java.awt.CardLayout;

import java.awt.Component;



import java.util.Vector;



public class JMTabPanel extends Panel {



    private CardLayout textCardLayout;

    private int activeTab = 0;    /* The integer representing the currently visible "Tab" */

    private Vector muList;

    private static final boolean DEBUG = false;



    /** Our magic constructor! */

    public JMTabPanel() {

        textCardLayout = new CardLayout();

        muList = new Vector();

        this.setSize(100, 100); // Without this kludge our object doesn't seem to show up consistently.

        this.setLayout(textCardLayout);

        this.doLayout();

    }



    /**

     * 

     * @param tabTitle 

     * @param icon 

     * @param item 

     * @param toolTip 

     */

    public void addTab(final String tabTitle, final Object icon, final Component item, final String toolTip) {

        this.add(item, tabTitle);

        muList.addElement(tabTitle);

        /*

        this.doLayout();

        this.validate();

        item.doLayout();

        item.validate();

        */



        if (DEBUG) {

            System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("JMTabPanel_added:_") + tabTitle);

            System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Vector:_") + muList);

        }

    }



    /**

     * 

     * @param tabTitle 

     * @param icon 

     * @param item 

     */

    public synchronized void addTab(final String tabTitle, final Object icon, final Component item) {

        this.addTab(tabTitle, icon, item, null);

    }



    /**

     * 

     * @param tabTitle 

     * @param item 

     */

    public synchronized void addTab(final String tabTitle, final Component item) {

        this.addTab(tabTitle, null, item, null);

    }



    /**
     * Remove the given component from our lay-out
     * @param item 
     * @param index 
     */
    public synchronized void remove(final Component item, final int index) {
        // Do some calculations to make certain we adjust our index  Fix me XXX
        if (DEBUG) {
            System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("removing_") + muList.elementAt(index) + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("_based_on_the_index."));
        }

        muList.removeElementAt(index);

        textCardLayout.previous(this);

        // Remove the component from the lay-out
        if (DEBUG) {
            System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Removing:_") + item);
        }

        this.remove(item);

        if (DEBUG) {
            System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("We're_removing_") + item + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("_that_was_at_location_") + index);
        }

        if (index <= activeTab) {
            activeTab--;
        }

        if (activeTab < 0) {
            activeTab = 0;
        }

        if (DEBUG) {
            System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Our_new_activeTab_is:_") + activeTab);
        }
        doLayout();
    }

    /**
     * 
     * @param index 
     */
    public synchronized void setSelectedIndex(final int index) {

        if (DEBUG) {

            System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("Index:_") + index + java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("___muList.size():_") + muList.size());

            System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("muList_is:_") + muList);

        }

        // Do some param checking here

        /*

        if (muList == null || muList.size() < 1) {

            if (DEBUG) {

                System.err.println("muList is null or empty.");

            }

            return;

        }



        if (index < 0 || index > muList.size() - 1) {

            if (DEBUG) {

                System.err.println("Cancelled... index less than zero or greater than muList.size()");

            }

            return;

        }

        */

        if (muList != null && muList.size() > 0 && index >= 0 && index < muList.size()) {

            // set the new active tab

            activeTab = index;



            // Now make the tab visible

            if (DEBUG) {

                System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("JMTabPanel.setSelectedIndex()_getting_name_of_item_to_show."));

            }

            final String tempName = (String)muList.elementAt(index);

            if (DEBUG) {

                System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("JMTabPanel.setSelectedIndex()_attempting_to_show:_") + tempName);

            }

            textCardLayout.show(this, tempName);

        }

        if (DEBUG) {

            System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("JMTabPanel.setSelectedIndex()_complete."));

        }

    }



    /**

     * 

     * @return 

     */

    public int getSelectedIndex() {

        return activeTab;

    }



    public synchronized void next() {

        if (DEBUG) {

            System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("JMTabPanel_->_next()"));

        }

        textCardLayout.next(this);

        activeTab++;

        if (activeTab >= muList.size()) {

            activeTab = 0;

        }



        // setSelectedIndex(activeTab);



    }



    public synchronized void previous() {

        if (DEBUG) {

            System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("JMTabPanel_->_previous()"));

        }

        textCardLayout.previous(this);

        activeTab--;

        if (activeTab < 0) {

            activeTab = muList.size() - 1;

        }



        // setSelectedIndex(activeTab);

    }



    public synchronized void last() {

        if (DEBUG) {

            System.err.println(java.util.ResourceBundle.getBundle("anecho/JamochaMUD/JamochaMUDBundle").getString("JMTabPanel_->_last()"));

        }

        activeTab = muList.size() - 1;

        // setSelectedIndex(activeTab);

        textCardLayout.last(this);

    }

}