/* PathWalker.java.  A plug-in to the JamochaMUD program by Jeff Robinson.
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
  * PathWalker plugin
  * A plugin to JamochaMUD that enables recording and saving movement
  * commands for later playback.  All PathWalker functionality is accessed
  * via the "#path" command, with the syntax
  * #path <subcommmand> [<options>]
  *
  * A list of available subcommands should be displayed with
  * #parse help
  */

package anecho.JamochaMUD.plugins;

import anecho.JamochaMUD.JMConfig;
import anecho.JamochaMUD.MuSocket;
import anecho.JamochaMUD.plugins.PathWalkerDir.*;
import java.util.HashMap;
import java.util.ResourceBundle;

public class PathWalker implements PlugInterface
{
  HashMap<String, Path> pathList;
  Path currentPath;
  private boolean isActive;
  private boolean isSusp; // flag to suspend recording

  static final String bundle = 
    "anecho/JamochaMUD/plugins/PathWalkerDir/PathWalkerBundle";

  // this is not a translatable string
  static final String aboutStr = "PathWalker plug-in for JamochaMUD " +
    "v0.2 (c) 2012 Ben Dehner";

  /**
   * useless constructor
   */
  public PathWalker()
  {
  }

  /**
   * Interface method to return plug-in name
   */
  @Override
  public String plugInName()
  {
      return "PathWalker";
  }

  /**
   * Interface method to return plug-in description
   */
  @Override
  public String plugInDescription()
  {
      return getResString( "pluginDescString" );
  }

  /**
   * Interface method to return plug-in type
   */
  @Override
  public String plugInType()
  {
     return anecho.JamochaMUD.EnumPlugIns.INPUT;
  }

  /**
   * primary interface method
   * @param jString input command
   * @param mSock mud connection
   * @return modified string written to gui and connection
   */
  @Override
  public String plugMain( String jString, MuSocket mSock)
  {
      if ( ! isActive ) return jString;
      if ( jString == null ) return null;  // hate NullPointerException

      saveStep( jString );
      if ( ! jString.startsWith("#path") ) return jString;

      // no otions supplied
      if ( jString.equalsIgnoreCase("#path") )
      {
         mSock.write(getResString("subCommandReqStr"), true);
         return null;
      }

      String [] cmd = parseCmd( jString );

      if (! cmd[0].equalsIgnoreCase("#path") ) return jString;

      if ( cmd.length < 2 )
      {
          mSock.write(getResString("subCommandReqStr"), true);
          return null;
      }

      CMD acmd;
      try { acmd  = CMD.getMatch( cmd[1], bundle ); }
      catch(Exception e)
      {
         System.out.println(e.toString());
         e.printStackTrace( System.out );
         acmd = CMD.NONE;
      }

      // If I was really fancy, I'd use reflection to look up a method
      // for each command
      switch (acmd)
      {  
         case ABOUT: aboutPaths(mSock); break;
         case NEW: newPath( mSock, cmd ); break;
         case SAVE: savePath( mSock, cmd, false ); break;
         case DELETE: deletePath( mSock, cmd ); break;
         case SUSPEND: setSuspend( true ); break;
         case RESUME: setSuspend( false ); break;
         case LIST: listPaths( mSock, cmd ); break;
         case STAT: showStat( mSock ); break;
         case HELP: showHelp( mSock ); break;
         case CANCEL: savePath( mSock, cmd, true ); break;
         case WALK: walkPath( mSock, cmd, false ); break;
         case REVERSE: walkPath( mSock, cmd, true ); break;
         case XLOAD: loadPaths(); break;
         case XWRITE: writePaths(); break;
         default: 
           mSock.write("\r\n" + getResString("unknowSubcmdStr"), true );
      }
   
      // if we get this far, return null, because we have usurped the
      // the input to be a probably invalid MUD command
      return null;
  }

  // get a resoure string; catch errors in case of an improperly
  // implemented resource file
  String getResString( String rname )
  {
    String txt =  null;
    try { txt = ResourceBundle.getBundle(bundle).getString(rname); }
    catch(java.util.MissingResourceException mre)
    { txt = "Language resource not found for " + rname; }
    return txt;
  }

  // hmm.  I wonder what this does ...
  void showHelp(MuSocket mSock)
  {
    String helpStr;
    String cmdStr;
    CMD [] cmdarray = CMD.values();
    mSock.write("\r\n");
    mSock.write( getResString("pathSyntaxStr") + "\r\n" );
    for ( int i = 1; i < cmdarray.length; i++ )
    {
      cmdStr = cmdarray[i].getText(bundle);
      helpStr = cmdarray[i].getHelp(bundle);
      if ( helpStr != null )
        mSock.write(cmdStr + ": " + helpStr + "\r\n");
    }
  }

  void aboutPaths(MuSocket mSock)
  {
    mSock.write("\r\n" + aboutStr + "\r\n");
  }

  void setSuspend( boolean newval )
  {
     if ( currentPath == null ) return;
     isSusp = newval;
  }

  void showStat( MuSocket mSock )
  {
     mSock.write( "\r\n" );
     if ( currentPath != null )
     {
        mSock.write(getResString("recordingStr") + 
          currentPath.getName() + "\r\n");
        if ( isSusp ) mSock.write(getResString( "isSuspendedStr") );
     }
     else
     {
       mSock.write(getResString("noActiveMacroStr"));
     }
  }

  // start a new path-recording macro
  void newPath(MuSocket mSock,  String [] args )
  {
     if ( currentPath != null )
     {
        mSock.write(getResString("alreadyRecordingStr") + " " +
          currentPath.getName());
        return;
     }
     isSusp = false;
     currentPath = new Path();
     if ( args.length > 2 )
     try { currentPath.setName( args[2] ); }
     catch (InvalidPathException ipe) 
     {
        mSock.write(getResString("invalidPathStr") );
        currentPath = null;
     }
     mSock.write("\r\n" + getResString("pathNewStr") + "\r\n");
  }

  void deletePath(MuSocket mSock, String [] args )
  {
     if (  args.length < 3 )
     {
       mSock.write("specifiy path to delete");
       return;
     }
     pathList.remove( args[2] );
     writePaths();
     mSock.write("\r\n" + getResString("pathDelStr") + "\r\n");
  }

  // send the commands to walk the walk
  void walkPath( MuSocket mSock, String [] args, boolean isReverse )
  {
     if ( args.length < 3 )
     {
       mSock.write(getResString("pathNameReqStr"), true);
       return;
     }
     Path p = pathList.get( args[2] );
     if ( p == null )
     {
       mSock.write(getResString("unknownPathStr") + "\r\n", true);
       return;
     }
     String [] pSteps = null;
     if ( isReverse )
       pSteps = p.getStepsReverse();
     else
       pSteps = p.getSteps();

     for (int i = 0; i < pSteps.length; i ++ )
     {
          mSock.sendText( pSteps[i] );
          mSock.write(pSteps[i] + "\r\n", true);
     }
  }

  // done recording; save the path to the internal list
  void savePath( MuSocket mSock, String [] args, boolean isCancel )
  {
     if ( currentPath == null )
     {
       mSock.write(getResString("noActiveMacroStr"), true);
       return;
     }

     if ( isCancel )
     {
       currentPath = null;
       return;
     }

     // additional arguments, so a name was specified
     if ( args.length > 2 )
     {
       try { currentPath.setName( args[2] ); }
       catch (InvalidPathException ipe ) 
       {
          mSock.write( getResString("invalidPathStr") );
          return;
       }
     }
     pathList.remove( currentPath.getName());
     pathList.put( currentPath.getName(), currentPath.getCopy() );
     currentPath = null;
     writePaths();
     mSock.write("\r\n" + getResString("pathSaveStr") + "\r\n");
  }

  // list currently known paths
  void listPaths( MuSocket mSock, String [] args )
  {
    java.util.Set<String> keySet = pathList.keySet();
    mSock.write( "\r\n" + getResString("pathListHeaderStr") + "\r\n");
    for (String akey: keySet)
    {
      mSock.write( akey + "\r\n" );
    }
  }

  // save a movement step, as applicable.  We decide here whether we're in a
  // state that we should try to save the command; if the plug-in isn't active
  // we should never get here in the first pace.  The path object itself will 
  // decide if the command is a movement command that should be saved.
  void saveStep( String jString )
  {
     if ( currentPath == null ) return; // not recording
     if ( isSusp ) return;              // recording is suspended
     currentPath.addStep( jString );
  }

  /**
   * Save the paths to an XML file.
   */
  void writePaths ( )
  {
     FileUtils.savePaths( pathList );
  }

  /*
   * parse the command options; this is horribly lazy handling of whitespace.
   */
  private String[] parseCmd( String jString)
  {
     if ( jString == null ) return null;
     String [] cmds = jString.split(" ");

     return cmds;
  }

  /**
   * Interface method for configuring properties; don't have any.
   */
  @Override
  public void plugInProperties()
  { boolean isBogus = true; }

  /**
   * Interface method to determine if we have configuration -- we don't.
   */
  @Override
  public boolean hasProperties()
  { return false; }

  /**
   * Interface method for initialization -- read in saved macros
   */
  @Override
  public void initialiseAtLoad()
  {
     currentPath = null;
     loadPaths();
  }

  // load the paths from an eternal file
  void loadPaths()
  {
     pathList = null;
     pathList = FileUtils.loadPaths();
  }

  /**
   * Interface method to activate the plug-in
   */
  @Override
  public void activate ()
  { isActive = true; }

  /**
   * interface method to clean up at shutdown -- save our paths.  Depending
   * on how the client is shutdown, this method may not get called.
   */
  @Override
  public void setAtHalt()
  {  writePaths(); }

  /**
   * Interface method to deactivate the plug-in
   */
  @Override
  public void deactivate()
  { isActive = false; }

  /**
   * Interface method to determine if we're active
   */
  @Override
  public boolean isActive()
  { return isActive; }

  /**
   * Interface method to determine if we have external config; we do.
   */
  @Override
  public boolean haveConfig()
  { return true; }

}
