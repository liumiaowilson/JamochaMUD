package com.jmxp;

public class libmxp 
{
	public static int Bold = 0x01;
	public static int Italic = 0x02;
	public static int Underline = 0x04;
	public static int Strikeout = 0x08;

	//align type for internal windows and images (type Middle is only valid for images)
	public enum alignType {
		nop,
		Left,
		Right,
		Bottom,
		Top,
		Middle
	};
	
}
