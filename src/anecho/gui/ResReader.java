/**

 * ResReader.java

 * Contains functions to simplify reading

 * language resource bundles, taking the bulk

 * of the code out of the other classes

 * $Id: ResReader.java,v 1.3 2006/11/17 06:24:36 jeffnik Exp $
 */



/* JamochaMUD, a Muck/Mud client program

 * Copyright (C) 1998-2002  Jeff Robinson

 *

 * This program is free software; you can redistribute it and/or

 * modify it under the terms of the GNU General Public License

 * as published by the Free Software Foundation; either version 2

 * of the License, or (at your option) any later version. *

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



import java.util.ResourceBundle;

import java.util.MissingResourceException;

import java.util.Vector;

import java.util.StringTokenizer;



/**

 * ResReader.java

 * Contains functions to simplify reading

 * language resource bundles, taking the bulk

 * of the code out of the other classes

 * @version $Id: ResReader.java,v 1.3 2006/11/17 06:24:36 jeffnik Exp $
 * @author Jeff Robinson

 */

public class ResReader{

    

    private ResourceBundle bundle;

    private String finalString;

    // private Vector finalVector;

    private String resBundle;

    

    // This is our constructor.  There are many like it.

    // they are empty, too.

    /**

     * Cancel Button

     */

    public ResReader() {

        // I can't think of anything that would go in here right now.

    }

    

    /**

     *

     * @param bundleName

     */

    public ResReader(String bundleName) {

        this.resBundle = bundleName;

    }

    

        /* Methods for reading simple Resource bundles,

         * such as labels and short strings

         */

    /**

     * This method only works if we already have a bundle.  Return an internationalised String.

     * @deprecated There are better ways of handling this - 2005-12-28

     * @param className 

     * @return 

     */

    public String langString(final String className) {

        // String returnBundle = new String();

        String returnBundle = java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("");

        // String returnBundle = null;

        

        if (resBundle != null) {

            // return langString(resBundle, className);

            returnBundle = langString(resBundle, className);

        }

        

        // return null;

        return returnBundle;

    }

    

    /**

     * Return an internationalised String

     * @param bundleName 

     * @param className 

     * @return 

     * @deprecated There are better ways of handling this - 2005-12-28

     */

    public String langString(final String bundleName, final String className) {

        bundle = ResourceBundle.getBundle(bundleName);

        finalString = new String(extractString(bundle, className));

        return finalString.trim();

    }

    

    /**

     * Return an internationalised String

     * @param bundleName 

     * @param className 

     * @param itemName 

     * @return 

     * @deprecated There are better ways of handling this - 2005-12-28

     */

    public String langString(final String bundleName, final String className, final String itemName){

        // bundle = ResourceBundle.getBundle(bundleName);

        // finalString = new String(extractString(bundle, className + "." + itemName));

        // return finalString.trim();

        return langString(bundleName, className + java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString(".") + itemName);

        

    }

    

    /**

     * Return an internationalised String

     * @param bundleName 

     * @param className 

     * @param itemName 

     * @param subName 

     * @return 

     * @deprecated There are better ways of handling this - 2005-12-28

     */

    public String langString(final String bundleName, final String className, final String itemName, final String subName) {

        // bundle = ResourceBundle.getBundle(bundleName);

        // finalString = new String(extractString(bundle, className + "." + itemName + "." + subName));

        // return finalString.trim();

        return langString(bundleName, className + java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString(".") + itemName + java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString(".") + subName);

    }

    

    /**

     * Method for reading multiple-line

     * messages from resource bundles

     * @param bundleName

     * @param className

     * @param itemName

     * @return

     */

    public Vector langVector(final String bundleName, final String className, final String itemName) {

        // bundle = ResourceBundle.getBundle(bundleName);

        return langVector(bundleName, className + java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString(".") +itemName);

        // rBFinalVector(bundle, className, itemName);

    }

    

    /**

     *

     * @param bundleName

     * @param itemName

     * @return

     */

    public Vector langVector(final String bundleName, final String itemName) {

        bundle = ResourceBundle.getBundle(bundleName);

        return rBFinalVector(bundle, itemName);

    }

    

    /**

     * Return a multi-line message as an array of strings.

     * This method only works if we already have a language bundle

     * @param itemName

     * @return

     */

    public String[] langArray(final String itemName) {

        String retStr[];

        

//        if (resBundle != null) {
//            // return langArray(resBundle, itemName);
//            // retStr = langArray(resBundle, itemName);
//            retStr = langArray(resBundle, itemName);
//        } else {
//            retStr = new String[0];
//        }
        
        if (resBundle == null) {
            retStr = new String[0];

        } else {
            retStr = langArray(resBundle, itemName);
        }

        // return null;

        

        return retStr;

    }

    

    /**

     * Return a multi-line message as an array of strings

     * @param bundleName

     * @param itemName

     * @return

     */

    public String[] langArray(final String bundleName, final String itemName) {

        bundle = ResourceBundle.getBundle(bundleName);

        final Vector tempVec = rBFinalVector(bundle, itemName);

        final int bLen = tempVec.size();

        String retString[] = new String[bLen];

        

        for (int i = 0; i < bLen; i++) {

            retString[i] = (String)tempVec.elementAt(i);

        }

        

        return retString;

    }

    

    // Grab the string from the Resource Bundle

    private String extractString(final ResourceBundle bundle, final String item) {

        String extractString = java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("");

        

        try {

            extractString = bundle.getString(item);

        } catch (MissingResourceException e) {

            System.out.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("Missing_Resource_Exception_") + item);

            extractString = java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("Missing_resource:_") + item;

        }

        

        return extractString;

    }

    

    /**

     * This method is capable of grabbing/returning multi-line messages

     * in the form of a vector

     */

    private Vector rBFinalVector(final ResourceBundle bundle, final String itemName) {

        // This method grabs a multi-lined message

        String tempString = java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("");

        

        try {

            tempString = bundle.getString(itemName);

        } catch (MissingResourceException e) {

            System.out.println(java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("Missing_Resource_Exception_") + itemName);

            tempString = java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("Missing_Resource_Exception_") + itemName;

        }

        

        return rBStringToVector(tempString);

    }

    

    /**

     * Break up a long string into tokens, using || as the delimeter,

     * in that way, each token will be a single line of text in a long

     * message.  Not the best method, but does seem to work for now

     */

    private Vector rBStringToVector(final String tempString) {

        // Tokenize the string into an array

        final StringTokenizer tokenizer = new StringTokenizer(tempString, java.util.ResourceBundle.getBundle("anecho/gui/guiBundle").getString("||"));

        final Vector tokenVector = new Vector(0, 1);

        

        while (tokenizer.hasMoreTokens()) {

            tokenVector.addElement(tokenizer.nextToken());

        }

        

        return tokenVector;

    }

}

