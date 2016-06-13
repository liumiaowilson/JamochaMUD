package com.jmxp;

import java.awt.Color;
import java.util.ArrayList;

import com.jmxp.libmxp.alignType;
import com.jmxp.structures.SendStruct;
import com.jmxp.structures.flagStruct;
import com.jmxp.structures.formatStruct;
import com.jmxp.structures.gaugeStruct;
import com.jmxp.structures.imageStruct;
import com.jmxp.structures.internalWindowStruct;
import com.jmxp.structures.linkStruct;
import com.jmxp.structures.moveStruct;
import com.jmxp.structures.relocateStruct;
import com.jmxp.structures.soundStruct;
import com.jmxp.structures.statStruct;
import com.jmxp.structures.varStruct;
import com.jmxp.structures.windowStruct;

public class ResultHandler 
{	
	/** 
	 * result that was most recently sent to the app 
	 */	
	private MXPResult returnedResult;
	private ArrayList<MXPResult> results = new ArrayList<MXPResult>();
	
	/** 
	 * constructor 
	 */	
	public ResultHandler()
	{
		returnedResult = null;
	}

	public MXPResult createError(String error) 
	{
		  MXPResult res = new MXPResult();
		  res.type = -1;
		  res.data = error;
		  return res;
	}
	
	public void addToList(MXPResult res)
	{
	  if (res!=null)
	  {
		  results.add(res);
	  }
	}

	public MXPResult createText(String t) 
	{
		  MXPResult res = new MXPResult();
		  res.type = 1;
		  res.data = t;
		  return res;
	}

	public MXPResult createWarning(String string) 
	{
		  MXPResult res = new MXPResult();
		  res.type = -2;
		  res.data = string;
		  return res;
	}

	public MXPResult createVariable(String varName, String varValue, boolean erase) 
	{
		MXPResult res = new MXPResult();
		res.type = 4;
		varStruct vs = new varStruct();
		vs.name = varName;
		vs.value = varValue;
		vs.erase = erase;
		res.data = vs;
		return res;
	}

	public void deleteResult(MXPResult res) 
	{
		if (res == null)
			return;
		res.data = null;
		res = null;
	}

	public MXPResult createSendThis(String text) 
	{
		  MXPResult res = new MXPResult();
		  res.type = 9;
		  res.data = text;
		  return res;
	}

	public MXPResult createFlag(boolean begin, String flag) 
	{
		MXPResult res = new MXPResult();
		res.type = 3;
		flagStruct fs = new flagStruct();
		fs.begin = begin;
		fs.name = flag;
		res.data = fs;
		return res;
	}

	public MXPResult createFormatting(int usemask, int curattrib,
			Color fgcolor, Color bgcolor, String font, int cursize) 
	{
		MXPResult res = new MXPResult();
		res.type = 5;
		formatStruct fs = new formatStruct();
		fs.usemask = usemask;
		fs.attributes = curattrib;
		fs.fg = fgcolor;
		fs.bg = bgcolor;
		fs.size = cursize;
		fs.font = font;
		res.data = fs;
		return res;		
	}

	public MXPResult createSetWindow(String curWindow) 
	{
		MXPResult res = new MXPResult();
		res.type = 15;
		res.data = curWindow;
		return res;
	}

	public MXPResult createLink(String name, String url, String text,
			String hint) 
	{
		  MXPResult res = new MXPResult();
		  res.type = 6;
		  linkStruct ls = new linkStruct();
		  ls.name = name;
		  ls.url = url;
		  ls.text = text;
		  ls.hint = hint;
		  res.data = ls;
		  return res;
	}

	public MXPResult createSendLink(String name, String cmd, String text,
			String hint, boolean prompt, boolean menu) 
	{
		  MXPResult res = new MXPResult();
		  res.type = 7;
		  SendStruct ss = new SendStruct();
		  ss.name = name;
		  ss.command = cmd;
		  ss.text = text;
		  ss.hint = hint;
		  ss.toprompt = prompt;
		  ss.ismenu = menu;
		  res.data = ss;
		  return res;
	}

	public MXPResult createExpire(String name) 
	{
		  MXPResult res = new MXPResult();
		  res.type = 8;
		  res.data = name;
		  return res;
		
	}

	public MXPResult createHorizLine() 
	{
		  MXPResult res = new MXPResult();
		  res.type = 10;
		  res.data = null;
		  return res;
	}

	public MXPResult createSound(boolean isSOUND, String fname, int vol, int count,
			int priority, boolean contifrereq, String type, String url) 
	{
		  MXPResult res = new MXPResult();
		  res.type = 11;
		  soundStruct ss = new soundStruct();
		  ss.fname = fname;
		  ss.type = type;
		  ss.url = url;
		  ss.isSOUND = isSOUND;
		  ss.vol = vol;
		  ss.repeats = count;
		  ss.priority = priority;
		  ss.continuemusic = contifrereq;
		  res.data = ss;
		  return res;
		
	}

	public MXPResult createGauge(String variable, String maxvariable,
			String caption, Color color) 
	{
		  MXPResult res = new MXPResult();
		  res.type = 22;
		  gaugeStruct gs = new gaugeStruct();
		  gs.variable = variable;
		  gs.maxvariable = maxvariable; 
		  gs.caption = caption;
		  gs.color = color;
		  res.data = gs;
		  return res;
	}

	public MXPResult createMoveCursor(int x, int y) 
	{
		  MXPResult res = new MXPResult();
		  res.type = 16;
		  moveStruct ms = new moveStruct();
		  ms.x = x;
		  ms.y = y;
		  res.data = ms;
		  return res;
	}

	public MXPResult createEraseText(boolean feof) 
	{
		  MXPResult res = new MXPResult();
		  res.type = 17;
		  res.data = feof;
		  return res;
		
	}

	public MXPResult createStat(String variable, String maxvariable, String caption) 
	{
		  MXPResult res = new MXPResult();
		  res.type = 23;
		  statStruct ss = new statStruct();
		  ss.variable = variable;
		  ss.maxvariable = maxvariable;
		  ss.caption = caption;
		  res.data = ss;
		  return res;
		
	}

	public MXPResult createInternalWindow(String nm, String tt, alignType at,
			boolean scrolling) 
	{
		  MXPResult res = new MXPResult();
		  res.type = 13;
		  internalWindowStruct ws = new internalWindowStruct();
		  ws.name = nm;
		  ws.align = at;
		  ws.scrolling = scrolling;
		  res.data = ws;
		  return res;		
	}

	public MXPResult createWindow(String nm, String tt, int left, int top,
			int width, int height, boolean scrolling, boolean floating)
	{
		  MXPResult res = new MXPResult();
		  res.type = 12;
		  windowStruct ws = new windowStruct();
		  ws.name = nm; 
		  ws.title = tt;
		  ws.left = left;
		  ws.top = top;
		  ws.width = width;
		  ws.height = height;
		  ws.scrolling = scrolling;
		  ws.floating = floating;
		  res.data = ws;
		  return res;
		
	}

	public MXPResult createCloseWindow(String nm) 
	{
		  MXPResult res = new MXPResult();
		  res.type = 14;
		  res.data = nm;
		  return res;		
	}

	public MXPResult createRelocate(String hostname, int port) 
	{
		  MXPResult res = new MXPResult();
		  res.type = 18;
		  relocateStruct rs = new relocateStruct();
		  rs.server = hostname;
		  rs.port = port;
		  res.data = rs;
		  return res;

	}

	public MXPResult createSendLogin(boolean username) 
	{
		  MXPResult res = new MXPResult();
		  res.type = 19;
		  res.data = (Boolean)username;
		  return res;
	}

	public MXPResult createImageMap(String lastcmd) 
	{
		  MXPResult res = new MXPResult();
		  res.type = 21;
		  res.data = lastcmd;
		  return res;		
	}

	public MXPResult createImage(String fname, String url, String type,
			int height, int width, int hspace, int vspace, alignType at)
	{
		  MXPResult res = new MXPResult();
		  res.type = 20;
		  imageStruct is = new imageStruct();
		  is.fname = fname;
		  is.url = url;
		  is.type = type;
		  is.height = height;
		  is.width = width;
		  is.hspace = hspace;
		  is.vspace = vspace;
		  is.align = at;
		  res.data = is;
		  return res;
		
	}

	public MXPResult createLineTag(int number) 
	{
		  MXPResult res = new MXPResult();
		  res.type = 2;
		  res.data = number;
		  return res;
		
	}

	public boolean haveResults() 
	{
		return results.size() > 0;		
	}

	public MXPResult nextResult() 
	{
		  returnedResult = results.get(0);
		  results.remove(0);
		  return returnedResult;
	}
	
}

