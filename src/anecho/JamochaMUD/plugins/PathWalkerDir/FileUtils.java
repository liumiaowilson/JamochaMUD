/* FileUtils.java.  A component of the PathWalker plug-in for the JamochaMUD
 * program.
 * Copyright 2012 Ben Dehner
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses.
 *
 * Ben Dehner
 * b.dehner@cox.net
 *
 */

/**
  * Utility programs for reading/writing the save paths file.  This class
  * is mostly to streamline the primary PathWalker class.
  */

package anecho.JamochaMUD.plugins.PathWalkerDir;
import anecho.JamochaMUD.JMConfig;

public class FileUtils
{
   static final String SAVE_FILE = "PathWalker.xml";

   /**
     * Save the path list to an XML file
     */
   // mostly XML manipulation crap using suckweek JAXP.  I almost bundled
   // JDom (www.jdom.org) into this package.
   public static void savePaths( java.util.HashMap<String,Path> pathList )
   {
     java.io.File outfile = getSaveFile();
     org.w3c.dom.Node rootNode;

     try
     {
       javax.xml.parsers.DocumentBuilderFactory dbf =
        javax.xml.parsers.DocumentBuilderFactory.newInstance();
       javax.xml.parsers.DocumentBuilder db = dbf.newDocumentBuilder();
       org.w3c.dom.Document d = db.newDocument();
       rootNode = d.createElement("root");
       d.appendChild ( rootNode );
       for( Path aPath: pathList.values() )
       {
           aPath.addNode( d, rootNode );
       }
       javax.xml.transform.dom.DOMSource dsource =
         new javax.xml.transform.dom.DOMSource( d );

      javax.xml.transform.stream.StreamResult result =
         new javax.xml.transform.stream.StreamResult( outfile );
       javax.xml.transform.TransformerFactory tf =
         javax.xml.transform.TransformerFactory.newInstance();
       javax.xml.transform.Transformer t = tf.newTransformer();
       t.setOutputProperty(javax.xml.transform.OutputKeys.METHOD, "xml");
       t.setOutputProperty(javax.xml.transform.OutputKeys.STANDALONE, "yes");
       t.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
       t.transform( dsource, result );
     }
     catch(Exception e)
     { System.out.println(e.toString()); }

   }

   /**
     * read in the saved XML configuration file
     */
   // more XML crap.  Did I mention JAXP sucks?
   public static java.util.HashMap<String, Path> loadPaths()
   {
     java.io.File infile = getSaveFile();
     java.util.HashMap<String,Path> pathList =
       new java.util.HashMap<String,Path>();

     if ( ! infile.canRead() ) return pathList;

     javax.xml.parsers.DocumentBuilderFactory dbf =
      javax.xml.parsers.DocumentBuilderFactory.newInstance();
     try
     {
      javax.xml.parsers.DocumentBuilder db = dbf.newDocumentBuilder();
      org.w3c.dom.Document d = db.parse ( infile );
      org.w3c.dom.Node rootNode = d.getFirstChild();
      org.w3c.dom.NodeList children = rootNode.getChildNodes();
      Path aPath;
      String cname;
      for ( int i = 0; i < children.getLength(); i++)
      {
        cname = children.item(i).getNodeName();
        if ( (cname == null ) || ( ! cname.equalsIgnoreCase("path")) ) continue;
        aPath = new Path ( children.item(i) );
        pathList.put( aPath.getName(), aPath );
      }
     }
     catch(Exception e)
     {
        System.out.println(e.toString());
     }
 
     return pathList;
   }

   /**
     * Get the save file as a java.io.File object.
     */
   public static java.io.File getSaveFile()
   {
     JMConfig config = JMConfig.getInstance();
     String dirName = config.getJMString(JMConfig.USERPLUGINDIR) + 
       java.io.File.separator + "PathWalkerDir";
     java.io.File file = new java.io.File(dirName, SAVE_FILE);
     return file;
   }
}
