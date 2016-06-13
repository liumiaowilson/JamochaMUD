/* CMD.java.  A component of the PathWalker plug-in to the JamochaMUD program 
 * by Jeff Robinson.
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
 */

/**
  * An eumeration for the list of available commands.  The text used to
  * invoke a command is stored in the resource file.
  */

package anecho.JamochaMUD.plugins.PathWalkerDir;
import java.util.ResourceBundle;

public enum CMD
{
  NONE,
  ABOUT,
  NEW,
  SAVE,
  DELETE,
  SUSPEND,
  RESUME,
  LIST,
  STAT,
  HELP,
  CANCEL,
  WALK,
  REVERSE,
  XLOAD,
  XWRITE;
  
  /**
    * Match the input string to the proper command
    * @param input: input text
    * @param bundle: the name of the ResourceBundle where the command
    * text is stored
    */
  public static CMD getMatch(String input, String bundle)
  {
     if (input == null ) return NONE;

     CMD [] cmdList = CMD.values();    
     String [] alternates;
     String text;

     // start the iteration at i=1; NONE isn't really a command
     for (int i = 1; i < cmdList.length; i ++)
     {
       text = cmdList[i].getText( bundle );
       if ( text == null ) continue;

       // if the text contains a ":" it allows alternate short-forms
       if ( text.indexOf("|") > 0 )
       {
         alternates = text.split("\\|");
         for( int j = 0; j < alternates.length; j++)
         {
            if (alternates[j].equalsIgnoreCase( input )) return cmdList[i];
         }
       }
       else
       {
         if ( text.equalsIgnoreCase( input ) ) return cmdList[i];
       }
     }
     return NONE;
  }

  // the text used to run a command is stored as a resource CMDcmdStr. 
  // However, undocumented commands and/or botched Resource files may
  // not have the command sting defined for all commands.
  public String getText( String bundle )
  {
    String cmdResource = this + "cmdStr";
    String text = null;

    try 
    {
      text = ResourceBundle.getBundle( bundle ).getString(cmdResource);
    }
   catch (java.util.MissingResourceException mre )
   {
     text = this.toString();
   }
    return text;
  }

  // get the help for each command
  public String getHelp( String bundle )
  {
    String cmdResource = this + "hlpStr";
    String text = null;

    if (( this == NONE )  ||
      this.toString().startsWith("X")) return null;

    try 
    {
      text = ResourceBundle.getBundle( bundle ).getString(cmdResource);
    }
   catch (java.util.MissingResourceException mre )
   {
     text = "language help not available";
   }
    return text;
  }
}
 
