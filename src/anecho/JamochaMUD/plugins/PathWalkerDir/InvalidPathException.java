/* InvalidPathException.java.  A component of the PathWalker plug-in for the 
 * JamochaMUD program.
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
  * Indicate that a path name has invalid characters.
  */
package anecho.JamochaMUD.plugins.PathWalkerDir;

public class InvalidPathException extends Exception
{
  private static final long serialVersionUID = 0l;

  public InvalidPathException()
  { super(); }
  public InvalidPathException( String msg )
  { super( msg ); }
}
