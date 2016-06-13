/*
 * FileUtils.java
 *
 * Created on January 29, 2006, 9:29 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package anecho.util;

import java.io.File;
import java.util.Vector;

/**
 * This class contains some basic file utilities such as 
 * recursive directory listings.
 * @author Jeff Robinson
 */
public class FileUtils {
    
    /**
     * Creates a new instance of FileUtils
     * @param inputName The file or path name to perform a function on.
     */
    public FileUtils(String inputName) {
        fileName = inputName;
    }
    
    /**
     * Set the file or path to operate on.
     * @param inputName A string representing the file name or path
     */
    public void setFile(final String inputName) {
        fileName = inputName;
    }
    
    /**
     * This method will creative a recursive list of file names.
     *  If includeDirs is true, we will include the directories,
     *  otherwise the list will only contain files
     * @param includeDirs true - include the directory as a separate entity in the return list
     * @throws java.lang.Exception An exception will be thrown if the user specifies a file instead of a directory
     * @return A list of entities with represent all the files 
     * (and optionally directories) found recursively 
     * from our starting point.
     */
    public String[] recursiveList (final boolean includeDirs) throws Exception {
        String[] retList;
        String workBase;
        File tempFile;
        String fnStr;   // The full name + path of a file
        final String sep = File.separator;
        final Vector dirList = new Vector();  // A running list of directories to check
        final Vector fullList = new Vector(); // A complete list to be returned

        tempFile = new File(fileName);
        
        if (!tempFile.isDirectory()) {
            // You can't get a recursive list of a single file!
            // throw Exception;
            // Fix Me XXX
        }
        
        workBase = tempFile.getCanonicalPath();
        
        if (DEBUG) {
            System.err.println("Adding base file " + fileName);
            System.err.println("Workbase is " + workBase);
        }
        dirList.addElement(fileName);
        
        // while (dirList.size() > 0) {
        while (!dirList.isEmpty()) {
            tempFile = new File(dirList.elementAt(0).toString());
            workBase = tempFile.getCanonicalPath();
            
            final String[] fList = tempFile.list();
            for (int i = 0; i < fList.length; i++) {
                fnStr = workBase + sep + fList[i];
                if (new File(fnStr).isDirectory()) {
                    dirList.addElement(fnStr);
                    if (includeDirs) {
                        fullList.addElement(fnStr);
                        if (DEBUG) {
                            System.err.println("Adding DIRECTORY: " + fnStr);
                        }
                    }
                } else {
                    fullList.addElement(fnStr);
                    if (DEBUG) {
                        System.err.println("Adding FILE: " + fnStr);
                    }
                }
            }
            dirList.removeElementAt(0); // remove the first item from the list
        }

        // Convert our Vector to an array of strings
        retList = new String[fullList.size()];
        fullList.toArray(retList);
        
        return retList;
    }

    private transient String fileName;    // The name of the file we will operate on
    private static final boolean DEBUG = false;
}
