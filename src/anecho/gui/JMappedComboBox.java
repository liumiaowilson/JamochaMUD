/** JMappedComboBox.java is a GUI class that extends JComboBox
 * to allow a hidden "column" of information to go along with
 * the user-displayed information.  This is useful for keeping
 * "keys" to database entries or the like
 * Created on October 31, 2004, 12:51 PM
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 */
package anecho.gui;

import java.util.Vector;
import javax.swing.ComboBoxModel;

/**
 * JMappedComboBox.java is a GUI class that extends JComboBox
 * to allow a hidden "column" of information to go along with
 * the user-displayed information.  This is useful for keeping
 * "keys" to database entries or the like.
 * @author Jeff Robinson
 * @version $Id: JMappedComboBox.java,v 1.6 2011/05/31 02:47:29 jeffnik Exp $
 */
public class JMappedComboBox extends javax.swing.JComboBox implements java.io.Serializable {

    // private boolean useMap = false;     // Should our mapping feature be used?
    /** A vector containing the keys (maps) for our items */
    private transient Vector itemMap = new Vector(0);
    /** A variable used for enabling or disabling debugging information */
    private static final boolean DEBUG = false;
    /** the serial version UID required by java.io.Serializable */
    public static final long serialVersionUID = 4246891800861403013L;

    // Constructors
    /**
     * JMappedComboBox.java is a GUI class that extends JComboBox
     * to allow a hidden "column" of information to go along with
     * the user-displayed information.  This is useful for keeping
     * "keys" to database entries or the like
     */
    public JMappedComboBox() {
        super();
    }

    /**
     * JMappedComboBox.java is a GUI class that extends JComboBox
     * to allow a hidden "column" of information to go along with
     * the user-displayed information.  This is useful for keeping
     * "keys" to database entries or the like.
     * @param aModel The combobox model to use with this component.
     */
    public JMappedComboBox(ComboBoxModel aModel) {
        super(aModel);
    }

    /**
     * JMappedComboBox.java is a GUI class that extends JComboBox
     * to allow a hidden "column" of information to go along with
     * the user-displayed information.  This is useful for keeping
     * "keys" to database entries or the like.
     * @param items An array of items to be used to populate our combobox
     */
    public JMappedComboBox(Object[] items) {
        this(items, new Object[items.length]);
    }

    /**
     * JMappedComboBox.java is a GUI class that extends JComboBox
     * to allow a hidden "column" of information to go along with
     * the user-displayed information.  This is useful for keeping
     * "keys" to database entries or the like.
     * @param items A Vector of Objects to populate our combobox.
     */
    public JMappedComboBox(Vector items) {
        this(items, new Vector(items.size()));
    }

    /**
     * JMappedComboBox.java is a GUI class that extends JComboBox
     * to allow a hidden "column" of information to go along with
     * the user-displayed information.  This is useful for keeping
     * "keys" to database entries or the like.
     * @param items An Array of Objects to populate our combobox
     * @param maps An Array of "maps" to correlate with our {@link items}
     */
    public JMappedComboBox(Object[] items, Object[] maps) {
        super(items);
        itemMap.removeAllElements();

        final int mapSize = maps.length;

        for (int i = 0; i < mapSize; i++) {
            itemMap.addElement(maps[i]);
            if (DEBUG) {
                System.err.println("Adding: " + maps[i]);
            }
        }
    }

    /**
     * JMappedComboBox.java is a GUI class that extends JComboBox
     * to allow a hidden "column" of information to go along with
     * the user-displayed information.  This is useful for keeping
     * "keys" to database entries or the like.
     * @param items A Vector of items to populate our combobox
     * @param maps A Vector of "maps" to correspond to our {@link items}
     */
    public JMappedComboBox(Vector items, Vector maps) {
        super(items);

        itemMap.removeAllElements();
        itemMap = maps;
    }

    /**
     * This method adds a new item to the bottom of our combobox (with no associated map)
     * @param item The item to be added to our combobox
     */
    @Override
    public void addItem(final Object item) {
        this.addItem(item, null);
    }

    /**
     * This method will add an item and its associated map to the combobox.
     * @param item Item to be added to the combobox
     * @param map The map associated with our {@link item}
     */
    public void addItem(final Object item, final Object map) {
        super.addItem(item);
        itemMap.addElement(map);

        if (DEBUG) {
            System.err.println("JMappedComboBox.addItem(): Added item " + item + " with map: " + map);
        }
    }

    /**
     * Returns an object based on the object map provided
     * @param map
     * @return
     */
    public Object getMappedObject(final Object map) {
        Object retObj = new Object();

        if (itemMap.contains(map)) {
            int mapIndex = itemMap.indexOf(map);
            retObj = super.getItemAt(mapIndex);
        }

        return retObj;
    }

    /**
     * Return our map key based on the given index
     * @param index This integer indicates the (zero-based) index of an item
     * in our combobox to return
     * @return The Object at the selected index
     */
    public Object getMapAt(final int index) {
        Object retObj = new Object();

        if (index > 0 || index < itemMap.size()) {
            retObj = itemMap.elementAt(index);
        }

        return retObj;
    }

    /**
     * This method is used to determine if the current JMappedComboBox contains
     * for supplied item
     * @param item The Object to test for the existance of
     * @return <code>true</code> the given item is in the JMappedComboBox 
     * <code>false</code> the given item is not contained in the JMappedComboBox
     * 
     */
    public boolean containsItem(final Object item) {
        boolean retCode = false;

        // Iterate through the items already in the list
        final int len = this.getItemCount();
        // Object[] tempItem = new Object[len - 1];

        for (int i = 0; i < len; i++) {
            // tempItem[i] = this.getItemAt(i);
            if (this.getItemAt(i) == item) {
                retCode = true;
                break;
            }
        }

        return retCode;
    }

    /**
     * This method returns to map of the currently selected combobox item
     * @return An Object of the currently selected map of our combobox.
     */
    public Object getSelectedMap() {
        Object retObj = new Object();
        final int index = this.getSelectedIndex();

        if (index >= 0 && index < itemMap.size()) {
            retObj = itemMap.elementAt(index);
        } //else {
        // retObj = new Object();
        // retObj = null;
        // }

        return retObj;
    }

    /*
    public int getSelectedIntMap() {
    int retInt;
    int index = this.getSelectedIndex();
    Object retObj = itemMap.elementAt(index);
    retInt = Integer.parseInt((String)itemMap.elementAt(index));
    return retInt;
    }
     */
    /**
     * This method is used to add an item to our combobox at a specific location.
     * @param anObject The Object to be added to our combobox.
     * @param index The zero-based index of where to insert the new object
     */
    @Override
    public void insertItemAt(final Object anObject, final int index) {
        this.insertItemAt(anObject, index, null);
    }

    /**
     * This method is used to add an item to our combobox at a specific location.
     * @param anObject The Object to be added to our combobox
     * @param index The zero-based index of where to insert the new object
     * @param map The map object associated with the new item we are adding to
     * the combobox.
     */
    public void insertItemAt(final Object anObject, final int index, final Object map) {
        super.insertItemAt(anObject, index);
        itemMap.insertElementAt(map, index);
    }

    /**
     * Removes all items from the JMappedComboBox
     */
    @Override
    public void removeAllItems() {
        super.removeAllItems();
        itemMap.removeAllElements();
    }

    /**
     *  The method removes the given object from our combobox
     * @param anObject The object to be removed from our combobox.
     */
    @Override
    public void removeItem(final Object anObject) {
        // int index = this.get
        // This may be ugly where we have to iterate through the whole list
        // of the JComboBox to find the index of our object...!

        // Fix Me XXX
        final int count = this.getItemCount();

        for (int i = 0; i < count; i++) {
            if (this.getItemAt(i) == anObject) {
                // Remove both the item and the map
                this.removeItemAt(i);
                itemMap.removeElementAt(i);

                break;
            }
        }
    }

    /**
     * The method removes the object at the given index from our combobox
     * @param index The (zero-based) index of the object to remove
     */
    @Override
    public void removeItemAt(final int index) {
        super.removeItemAt(index);
        itemMap.removeElementAt(index);
    }

    /**
     * This method sets the map of the currently selected object.
     * @param map The map to be used by the currently selected item in our combobox.
     */
    /*
    public JMappedComboBox() {
    initComponents();
    }
     */
    public void setSelectedMap(final Object map) {
        int index = 0;
        final int mapItems = itemMap.size();

        if (mapItems < 1 || map == null) {
            // We don't have any items so we cannot set the selected map
            return;
        }

        final String strMap = map.toString();

        if (DEBUG) {
            System.err.println("JMappedComboBox.setSelectedMAP is looking to match " + strMap + " from a map of " + mapItems + "large.");
        }

        for (int i = 0; i < mapItems; i++) {
            // if (itemMap.elementAt(i) == map) {
            if (itemMap.elementAt(i) != null) {
                if (itemMap.elementAt(i).toString().equals(strMap)) {
                    if (DEBUG) {
                        System.err.println("JMappedComboBox has found a match to " + map);
                    }
                    index = i;
                }
            }
        }

        this.setSelectedIndex(index);

    }

    /**
     * Returns the index of the provided map based on the starting offset
     * @param map The "map" object to search for
     * @param offset The offset from the beginning of the map index to start searching
     * @return This returns the index of the "map" object if found.  If the map is not found this method returns -1.
     */
    public int getMapIndex(Object map, int offset) {
        int result;

        if (itemMap.contains(map)) {
            result = itemMap.indexOf(map, 0);
        } else {
            result = -1;
        }

        return result;

    }

    /**
     * Returns the index of the provided map starting from the beginning of the list of maps
     * @param map The "map" object to search for
     * @return This returns the index of the "map" object if found.  If the map is not found this method returns -1.
     */
    public int getMapIndex(Object map) {
//        int result;
//
//        if (itemMap.contains(map)) {
//            result = itemMap.indexOf(map);
//        } else {
//            result = -1;
//        }

        return getMapIndex(map, 0);
    }

    public int getItemIndex(Object item) {
        return getItemIndex(item, 0);
    }

    public int getItemIndex(Object item, int offset) {

        int retVal = -1;

        int itemCount = getItemCount();
        // Object[] items = new Object[itemCount];
        Object listItem;

        for (int i = offset; i < itemCount; i++) {
            // items[i] = getItemAt(i);
            listItem = getItemAt(i);
            if (listItem.equals(item)) {
                retVal = i;
                break;
            }
        }

        return retVal;

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    /*
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

    }
    // </editor-fold>//GEN-END:initComponents
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
