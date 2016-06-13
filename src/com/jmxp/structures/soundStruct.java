package com.jmxp.structures;

public class soundStruct 
{
	public boolean isSOUND;         //1 if SOUND, 0 if MUSIC
	public String fname, url;    //(fName and U params)
	public int vol;              //volume (V param)
	public int repeats;          //-1 for infinite (L param)
	public int priority;         //0-100; SOUND only (P param)
	public boolean continuemusic;   //continue without restarting if rerequested? MUSIC only (C param)
	public String type;           //sound/music type (T param)
}
