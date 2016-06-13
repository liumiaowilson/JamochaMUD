/* Path.java.  A component of the PathWalker plug-in for the JamochaMUD
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

/** Encapsulate a sequence of movement commands, aka a "path".
  */

package anecho.JamochaMUD.plugins.PathWalkerDir;

public class Path
{
  static final int DIR_NONE = 0;
  static final int DIR_NORTH = 1;
  static final int DIR_EAST = 2;
  static final int DIR_SOUTH = 3;
  static final int DIR_WEST = 4;
  static final int DIR_UP = 5;
  static final int DIR_DOWN = 6;

  private String pathName;
  private String pathSteps;
  private int stepCount;

  /**
   * Constructor using a default path name "DefaultPath"
   */
  public Path()
  {
    pathSteps = "";
    pathName = "DefaultPath";
    stepCount = 0;
  }

  /**
   * Constructor assigning a path name.  Null path name not allowed
   */
  public Path ( String pName ) throws InvalidPathException
  {
    this();
    if ( isInvalidName( pName ) )
     throw new InvalidPathException("invalid path name");

    pathName = pName;
  }

  /**
    * Constructor from an XML node.  Used for loading the paths from the
    * saved configuration file.
    */
  public Path ( org.w3c.dom.Node n )
  {
      org.w3c.dom.NodeList nl = n.getChildNodes();
      String text;
      String nname;
      stepCount = 0;
      for ( int i = 0; i < nl.getLength(); i++)
      {
        nname = nl.item(i).getNodeName();
        text = nl.item(i).getTextContent();
        if ( nname.equalsIgnoreCase("name" ) )
          pathName = text;
        if ( nname.equalsIgnoreCase("steps" ) )
          pathSteps = text;
        if ( nname.equalsIgnoreCase("stepCount") )
        {
          try { stepCount = Integer.parseInt( text ); }
          catch (Exception e) {
            System.out.println("error parsing " + text);
            stepCount = 0;}
        }
      }
  }

  /**
   * Write out the path as an XML node in a supplied document.  Used
   * for generating the XML configuration file.
   */
  public void addNode(org.w3c.dom.Document d, org.w3c.dom.Node root)
  {
     org.w3c.dom.Element eparent;
     org.w3c.dom.Element echild;
     org.w3c.dom.Element edata;
     eparent = d.createElement("path");

     echild = d.createElement("name");
     echild.setTextContent( pathName );
     eparent.appendChild( echild );

     echild = d.createElement("steps");
     echild.setTextContent( pathSteps );
     eparent.appendChild( echild );

     echild = d.createElement("stepCount");
     echild.setTextContent( String.valueOf(stepCount) );
     eparent.appendChild( echild );

     root.appendChild( eparent );

  }

  /**
    * Set the "name" of the path.  A null name will throw InvalidPathException.
    */
  public void setName( String newName ) throws InvalidPathException
  {
      if ( isInvalidName( newName ) )
       throw new InvalidPathException("invalid path name");

      pathName = newName;
  }

  /**
    * get the name of the path
    */
  public String getName()
  { return pathName; }

  /**
    * Get the number of movement commands in the path
    */
  public int getCount()
  { return stepCount; }

  /**
   * Add a step to the current path macro.  Ignore the input if we
   * don't recognize a valid direction.  The direction must as follows:
   * short form: n,e,s,w,u,d
   * long form: north,east,south,west,up,down
   * @param dir: Direction
   */
  public void addStep( String dir )
  {
     int idir = parseCmd( dir );
     if ( idir > DIR_NONE )
     {
       addToPath( getCmdStr( idir ) );
       stepCount++;
     }
  }
  
  // internal method to add a path component; maintained as a
  // semi-colon seperated string
  private void addToPath(String step)
  {
     if (pathSteps.length() < 1 )
       pathSteps = step;
     else
       pathSteps = pathSteps + ";" + step;
  }

  // parse a movement command into a recognized value
  private static int parseCmd( String cmd )
  {
    if ( cmd == null ) return DIR_NONE;

    if (cmd.equalsIgnoreCase("n") || cmd.equalsIgnoreCase("north")) 
       return DIR_NORTH;
    if (cmd.equalsIgnoreCase("e") || cmd.equalsIgnoreCase("east")) 
       return DIR_EAST;
    if (cmd.equalsIgnoreCase("s") || cmd.equalsIgnoreCase("south")) 
       return DIR_SOUTH;
    if (cmd.equalsIgnoreCase("w") || cmd.equalsIgnoreCase("west")) 
       return DIR_WEST;
    if (cmd.equalsIgnoreCase("u") || cmd.equalsIgnoreCase("up")) 
       return DIR_UP;
    if (cmd.equalsIgnoreCase("d") || cmd.equalsIgnoreCase("down")) 
       return DIR_DOWN;

    return DIR_NONE;
  }

  // convert the numeric-form command back to the letter command
  static String getCmdStr( int pNum )
  {
    switch (pNum)
    { 
       case DIR_NORTH: return "n"; 
       case DIR_EAST: return "e";
       case DIR_SOUTH: return "s";
       case DIR_WEST: return "w"; 
       case DIR_UP: return "u"; 
       case DIR_DOWN: return "d"; 
       default: return "";
    }
  }

  /**
   * Return the path steps as an array.
   */
  public String [] getSteps()
  {
    return pathSteps.split(";");
  }

  /**
   * Return the paths reverse-direction steps.
   */
  public String[] getStepsReverse()
  {
     String [] steps = getSteps();
     if ( steps == null ) return null;

     int arrlen = steps.length;
     String [] revsteps = (String [])
      java.lang.reflect.Array.newInstance(String.class, arrlen );

     for ( int i = 0; i < arrlen; i ++ )
     {
         revsteps[arrlen - i - 1] = getReverseDir( steps[i] );
     }
     return revsteps;
  }

  private String getReverseDir( String dir )
  {
     if (dir.equalsIgnoreCase("n")) return "s";
     if (dir.equalsIgnoreCase("s")) return "n";
     if (dir.equalsIgnoreCase("e")) return "w";
     if (dir.equalsIgnoreCase("w")) return "e";
     if (dir.equalsIgnoreCase("u")) return "d";
     if (dir.equalsIgnoreCase("d")) return "u";

     return null;
  }

  @Override
  public String toString()
  { return pathName; }

  /**
    * Get a copy of this object
    */
  public Path getCopy()
  {
    Path p = new Path();
    p.pathName = pathName;
    p.pathSteps = pathSteps;
    p.stepCount = stepCount;
    return p;
  }

  @Override
  public int hashCode()
  {
    return pathName.hashCode();
  }

  // initially wanted to limit path names to alphanumeric; since the data
  // is saved in an XML format, no point in this restriction
  private static boolean isInvalidName( String pname )
  {
    if ( pname == null ) return true;
    return false;
  }
}
