/*****************************
**********************************************
*  The following java code is based off of the mxp library which
*  was written in c++ by Thomas Mecir and as such follows the
*  same license.
 *   Copyright (C) 2010 by slothmud.org                                     *
 *   splork@slothmud.org                     
 *  If you use this code, could you please add the following link
 * somewhere on your projects page
 *  <a href="http://www.slothmud.org">Slothmud - a multiplayer free online rpg game</a>         *
 *   His library can be found at http://www.kmuddy.com/libmxp/                                                                       *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU Library General Public License as       *
 *   published by the Free Software Foundation; either version 2 of the    *
 *   License, or (at your option) any later version.                       *
 *                                                                         *
 *   This program is distributed in the hope that it will be useful,       *
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *   GNU Library General Public License for more details.                  *
 ***************************************************************************/
package com.jmxp.structures;

public class chunk 
{
	public enum chunkType {
		  chunkNone,
		  chunkText,
		  chunkTag,
		  chunkError
		};
		
	  public chunkType chk;
	  public String text;
}