package com.jmxp.structures;

import java.awt.Color;

public class formatStruct 
{
	public static final int USE_BOLD=0x01;
	public static final int USE_ITALICS=0x02;
	public static final int USE_UNDERLINE=0x04;
	public static final int USE_STRIKEOUT=0x08;
	public static final int USE_FG=0x10;
	public static final int USE_BG=0x20;
	public static final int USE_FONT=0x40;
	public static final int USE_SIZE=0x80;
	public static final int USE_ALL=0xFF;
	
	public int usemask;    //8-bit; which params should be applied
	public int attributes;
	public Color fg, bg;
	public String font; //if NULL and it should be applied => default font should be set
	public int size;
}
