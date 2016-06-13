/*
 * SimpleFilter.java
 *
 * Created on February 5, 2006, 4:20 AM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package anecho.util;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * SimpleFilter is a class used to create simple file filters
 * for use with a JFileChooser
 * @author Jeff Robinson
 */
public class SimpleFilter extends FileFilter {
    
    private String[] filters;
    private String description;
    private boolean allowDirs = true;
    
    /**
     * This method determines if a file or directory should be shown by the filter.
     * @param checkFile The file to be checked
     * @return <CODE>true</CODE> - this file or directory should be shown
     * <CODE>false</CODE> - this file or directory should not be shown
     */
    public boolean accept(final File checkFile) {
        boolean retVal = false;

        if (checkFile.isDirectory() && allowDirs) {
            retVal = true;
        } else {
            
            final String extension = getExtension(checkFile);
            
            final int fLen = filters.length;
            
            if (extension == null || filters == null || fLen < 1) {
                retVal = false;
            } else {
                // int fLen = filters.length;
                
                for (int i = 0; i < fLen; i++) {
                    if (extension.equalsIgnoreCase(filters[i])) {
                        retVal = true;
                        break;
                    }
                }
            }
            
        }
        
        return retVal;
    }
    
    //The description of this filter
    /**
     * Returns the human-readable description of this filter.
     * @return Returns a human-readable description of this filter.
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Set the human-readable description for this filter.
     * @param desc Description of the filter.
     */
    public void setDescription(final String desc) {
        description = desc;
    }
    
    /**
     * This method sets the different filters.
     * @param filts An array of strings representing the filters (eg. .jpg, .tif, .tiff, .html).
     */
    public void setFilters(final String[] filts) {
        filters = new String[filts.length];
        filters = filts;
    }
    
    private String getExtension(final File fileName) {
        String retVal = null;
        final String strName = fileName.getName();
        final int dot = strName.lastIndexOf(java.util.ResourceBundle.getBundle("anecho/util/UtilBundle").getString("."));
        
        if (dot > -1) {
            retVal = strName.substring(dot);
        }
        
        return retVal;
    }
    
    /**
     * Returns a boolean value
     * @param accept <code>true</code> - accept directories
     * <code>false</code> - do not accept directories
     */
    public void acceptDirs(final boolean accept) {
        allowDirs = accept;
    }
}
