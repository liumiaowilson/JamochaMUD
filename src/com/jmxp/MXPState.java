package com.jmxp;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.jmxp.libmxp.alignType;
import com.jmxp.structures.SendStruct;
import com.jmxp.structures.flagStruct;
import com.jmxp.structures.formatStruct;
import com.jmxp.structures.linkStruct;

public class MXPState 
{
	enum mxpMode { openMode, secureMode, lockedMode};
	
	//initial LOCKED state?
	boolean initiallyLocked;
	//	currently implemented MXP version
	String mxpVersion;
	String clientName, clientVersion;	
	int Hattribs[] = new int[6];
	Color Hfg[] = new Color[6], Hbg[] = new Color[6];
	String Hfont [] = new String[6];
	int Hsize[] = new int[6];
	int defaultsize;
	String ttFont;
	/** list of existing frames */
	HashMap<String, Boolean> frames = new HashMap<String, Boolean>();
	//screen and font parameters
	int sX, sY;      // width/height of the screen
	int wX, wY;      // width/height of the output window
	int fX, fY;      // width/height of character X
	//user-defined values
	Color defaultfg, defaultbg;
	String defaultfont;
	int defaultattribs;
	Color gaugeColor;

	/** 
	 * current mode 
	 */
	private mxpMode mode;
	private ResultHandler results;
	private ElementManager elements;
	private EntityManager entities;
	/** temporary secure mode? */
	private boolean tempMode;
	/** did we just leave secure mode?*/
	private boolean wasSecureMode;
	//variables
	private boolean inVar;
	/** current default mode */
	private mxpMode defaultmode;
	private String varName, varValue;

	//links
	private boolean inLink, isALink;
	private String linkText;

	/** list of closing tags */
	private ArrayList<closingTag> closingTags = new ArrayList<closingTag>();

	//text attributes
	private boolean bold, italic, underline, strikeout;
	private Color fgcolor, bgcolor;
	private String curfont;
	private int cursize;
	
	//paragraphs
	private boolean inParagraph;  //in P tag; no method returns this
	private boolean ignoreNextNewLine;  //after NOBR; no method returns this
	private String lastcmd;
	private boolean gotmap;
	//current window
	private String curWindow, prevWindow;
	
	//SUPPORTS stuff...
	private boolean suplink, supgauge, supstatus, supsound, supframe, supimage, suprelocate;	

	private class closingTag 
	{
		  //tag name (lowercase)
		  String name;
		  //closing result, if there's exactly one
		  MXPResult closingresult;
		  //usually only zero or one element, but sometimes more :-)
		  List<MXPResult> closingresults;
	};
	
	public MXPState(ResultHandler resh, ElementManager elm,
			EntityManager enm, String package_name, String version) 
	{
		  results = resh;
		  elements = elm;
		  entities = enm;

		  //currently implemented MXP version
		  mxpVersion = "1.0";

		  //starting MXP mode is LOCKED, to prevent problems with non-MXP MUDs).
		  //This goes against the MXP protocol, therefore there's a setting that will keep the OPEN
		  //mode, if desired - see public API.
		  mode = mxpMode.lockedMode;
		  defaultmode = mxpMode.lockedMode;
		  initiallyLocked = true;
		  tempMode = false;
		  wasSecureMode = false;
		  
		  //some default values...
		  MXPColors colors = MXPColors.self();
		  defaultfg = colors.getColor("gray");
		  defaultbg = colors.getColor("black");
		  defaultfont = "Courier";
		  defaultsize = 12;
		  defaultattribs = 0;
		  //by default, all headers are written in the same font (Courier), they are bold and they
		  //differ in sizes...
		  for (int i = 0; i < 6; i++)
		  {
		    Hfont[i] = "Courier";
		    Hfg[i] = defaultfg;
		    Hbg[i] = defaultbg;
		    Hattribs[i] = libmxp.Bold;
		  }
		  Hsize[0] = 32;
		  Hsize[1] = 24;
		  Hsize[2] = 20;
		  Hsize[3] = 16;
		  Hsize[4] = 14;
		  Hsize[5] = 12;
		  ttFont = "Courier";
		  setDefaultGaugeColor (colors.getColor("white"));
		  //PACKAGE and VERSION are defined in config.h
		  clientName = package_name;
		  clientVersion = version;
		  //some default screen and font attributes...
		  fX = 16;
		  fY = 8;
		  sX = 800;
		  sY = 600;
		  
		  suplink = supgauge = supstatus = supframe = supimage = suprelocate = false;
		  
		  //params
		  reset();
	}

	private void reset() 
	{
		  bold = (defaultattribs & libmxp.Bold) != 0;
		  italic = (defaultattribs & libmxp.Italic) != 0;
		  underline = (defaultattribs & libmxp.Underline) != 0;
		  strikeout = (defaultattribs & libmxp.Strikeout) != 0;
		  fgcolor = defaultfg;
		  bgcolor = defaultbg;
		  curfont = defaultfont;
		  cursize = defaultsize;
		  inVar = false;
		  varValue = "";
		  inParagraph = false;
		  ignoreNextNewLine = false;
		  inLink = false;
		  isALink = false;
		  linkText = "";
		  gotmap = false;
		  curWindow = "";
		  prevWindow = "";
	}

	public void setDefaultGaugeColor(Color color) 
	{
		gaugeColor = color;
	}

	/** 
	 * return current mode 
	 */
	public mxpMode getMXPMode() 
	{
		  return mode;
	}

	public void gotSUPPORT(List<String> params) throws Exception 
	{
	  commonTagHandler();

	  if (!params.isEmpty())  //some parameters - this is not supported at the moment
	    results.addToList(results.createWarning (
	        "Received <support> with parameters, but this isn't supported yet..."));
	    
	  String res;
	  res = "\u001b[1z<SUPPORTS +!element +!attlist +!entity +var +b +i +u +s +c +h +font";
	  res += " +nobr +p +br +sbr +version +support +h1 +h2 +h3 +h4 +h5 +h6 +hr +small +tt";
	  if (suplink)
	    res += " +a +send +expire";
	  if (supgauge)
	    res += " +gauge";
	  if (supstatus)
	    res += " +status";
	  if (supsound)
	    res += " +sound +music";
	  if (supframe)
	    res += " +frame +dest";
	  if (supimage)
	    res += " +image";
	  if (suprelocate)
	    res += " +relocate +user +password";
	  res += ">\r\n";
	  results.addToList (results.createSendThis (res));
	  
	  commonAfterTagHandler();
	}

	/** stuff common for all tags */
	private void commonTagHandler() 
	{
		  //got a new tag - close outstanding entities, if any (unless we're in LOCKED mode)
		  if (mode != mxpMode.lockedMode)
		  {
		    String t = entities.expandEntities ("", true);
		    if (!(t.isEmpty()))
		      gotText(t, false);
		  }

		  //outstanding tags are closed, if we're going out of secure mode, unless a change back to secure
		  //mode occurs
		  if (wasSecureMode)
		  {
		    closeAllTags ();
		    wasSecureMode = false;
		  }

		  //error is reported, if we're inside VAR...
		  if (inVar)
		    results.addToList (results.createError ("Got a tag inside a variable!"));		
	}

	public void gotText (String text, boolean expandentities)
	{
	  if (text.isEmpty())
		  return;
	  //temp-secure mode . ERROR!
	  if (tempMode)
	  {
	    tempMode = false;
	    mode = defaultmode;
	    results.addToList (results.createError ("Temp-secure line tag not followed by a tag!"));
	  }

	  //outstanding tags are closed, if we're going out of secure mode, unless a change back to secure
	  //mode occurs
	  if (wasSecureMode)
	  {
	    closeAllTags ();
	    wasSecureMode = false;
	  }

	  //expand entities, if needed
	  String t;
	  if (expandentities && (mode != mxpMode.lockedMode))
	    t = entities.expandEntities (text, false);
	  else
	    t = text;

	  //special handling if we're in a variable or a link
	  if (inVar)
	    varValue += t;
	  if (inLink)
	    linkText += t;

	  //text can be sent is it's not a part of a link or of a variable
	  if (!(inVar || inLink))
	    //add text to the list of things to send
	    results.addToList (results.createText(t));
	}

	private void closeAllTags()
	{
	  if (closingTags.isEmpty())
	    return;

	  //process open tags one by one...
	  while (!closingTags.isEmpty())
	  {
	    //closingTags is a FIFO queue, tho technically it's a list
		  int last = closingTags.size()-1;
		  closingTag tag = closingTags.get(last);
		  closingTags.remove(last);
		  results.addToList (results.createWarning ("Had to auto-close tag " + tag.name + "."));	    
		  closeTag(tag);
	  }
	}
	
	private void closeTag(closingTag tag)
	{
	  //some tags need special handling...
	  if (tag.name.equals("p"))
	  {
	    inParagraph = false;
	    ignoreNextNewLine = false;
	    //also send a newline after end of paragraph... MXP docs say nothing about this :(
	    results.addToList (results.createText ("\r\n"));
	  }
	  if (tag.name.equals("var"))
	  {
	    tag.closingresult = null;
	    tag.closingresults = null;
	    results.addToList (results.createVariable (varName, varValue, false));
	    results.addToList (results.createText (varName + ": " + varValue));
	    entities.addEntity (varName, varValue);
	    inVar = false;
	    varName = "";
	    varValue = "";
	  }
	  if (tag.name.equals("a"))
	  {
	    if (inLink && isALink)
	    {
	      linkStruct ls = (linkStruct)tag.closingresult.data;
	      //assign text, using URL if no text given
	      String lt = linkText.isEmpty() ? (ls.url != null ? ls.url : "") : linkText;
	      lt = stripANSI (lt);
	      ls.text = lt;
	    }
	    else
	      //this should never happen
	      results.addToList (results.createError ("Received </A> tag, but I'm not in a link!"));
	    linkText = "";
	    inLink = false;
	    isALink = false;
	  }
	  if (tag.name.equals("send"))
	  {
	    if (gotmap)
	    {
	      //don't send this closing result
	      results.deleteResult (tag.closingresult);
	      tag.closingresult = null;
	      
	      if (!linkText.isEmpty())
	        results.addToList (results.createError
	            ("Received image map and a command in one SEND tag!"));
	    }
	    else if (inLink && (!isALink))
	    {
	      SendStruct ss = (SendStruct) tag.closingresult.data;
	      //assign text, also assign to command if none given
	      
	      //assign linkText to ss.text
	      linkText = stripANSI (linkText);
	      ss.text = linkText;
	      if (ss.hint != null )
	      {
	        //expand &text; in hint
	        String hint = ss.hint;

	        boolean found = true, havematch = false;
	        while (found)
	        {
	        	hint = hint.replaceFirst("&text;", linkText);
	        	if ( hint.indexOf("&text;") < 0 ) found = false; //no more matches
	        }
	        if (havematch)  //apply changes if needed
	        {
	          //assign hint to ss.hint
	          ss.hint = hint;
	        }
	      }
	      if (!ss.command.isEmpty()) 
	      {
	        String cmd = ss.command;
	        //also expand &text; in href
	        
	        boolean found = true, havematch = false;
	        while (found)
	        {
	        	if ( cmd.indexOf("&text;") > 0 )
	        	{
	        		cmd = cmd.replace("&text;", linkText);
	        		havematch = true;
	        	}
	        	else
	        		found = false;  //no more matches
	        }
	        if (havematch)  //apply changes if needed
	        {
	        	//assign cmd to ss.command
	        	ss.command = cmd;
	        }
	      }
	      else if (!linkText.isEmpty())
	      {
	    	  //assign linkText to ss.command
	    	  ss.command = linkText;
	      }
	    }
	    else
	      //this should never happen
	      results.addToList (results.createError ("Received </SEND> tag, but I'm not in a link!"));

	    linkText = "";
	    inLink = false;
	    isALink = false;
	    gotmap = false;
	  }

	  //handle applying/sending of closing results, is any
	  if (tag.closingresult!=null)
	  {
	    //apply result, reverting changes made by opening tag
	    applyResult(tag.closingresult);
	    //and send the changes to the client app
	    results.addToList (tag.closingresult);
	  }
	  if (tag.closingresults!=null)
	  {
	    //the same for remaining closing tags...
		for (MXPResult item : tag.closingresults) 
		{
			applyResult(item);
			results.addToList(item);
		}
	  }
	  //finally, the closing tag gets deleted
	  //note that this won't delete the results themselves - they will be deleted after
	  //they are processed by the client app
	  tag.closingresults = null;
	  tag = null;
	}

	private String stripANSI (String s)
	{
	  // first of all, find out whether there are any ANSI sequences
	  boolean ansi = false;
	  for (int i = 0; i < s.length(); ++i)
	    if (s.charAt(i) == 27) ansi = true;
	  if (!ansi) return s;

	  // there are ANSI sequences - have to get rid of them
	  String res = "";
	  ansi = false;
	  for (int i = 0; i < s.length(); ++i) {
	    if (!ansi) {
	      if (s.charAt(i) == 27)
	        ansi = true;
	      else
	        res += s.charAt(i);
	    } else {
	      // ANSI seq is ended by a-z,A-Z
	      if ( Character.isLetter(s.charAt(i)) )
	        ansi = false;
	    }
	  }
	  return res;
	}

	private void applyResult (MXPResult what)
	{
	  switch (what.type) {
	    case 5: {
	      formatStruct fs = (formatStruct) what.data;
	      int usemask = fs.usemask;
	      if ((usemask & formatStruct.USE_BOLD) != 0 )
	        bold = (fs.attributes & libmxp.Bold) != 0;
	      if ((usemask & formatStruct.USE_ITALICS) != 0 )
	        italic = (fs.attributes & libmxp.Italic) != 0;
	      if ((usemask & formatStruct.USE_UNDERLINE) != 0 )
	        underline = (fs.attributes & libmxp.Underline) != 0;
	      if ((usemask & formatStruct.USE_STRIKEOUT) != 0 )
	        strikeout = (fs.attributes & libmxp.Strikeout) != 0;
	      if ((usemask & formatStruct.USE_FG) != 0 )
	        fgcolor = fs.fg;
	      if ((usemask & formatStruct.USE_BG) != 0 )
	        bgcolor = fs.bg;
	      if ((usemask & formatStruct.USE_FONT) != 0 )
	        curfont = fs.font;
	      if ((usemask & formatStruct.USE_SIZE) != 0 )
	        cursize = fs.size;
	      break;
	    }
	    case 15: 
	    {
	      prevWindow = curWindow;
	      if (what.data != null)
	        curWindow = (String)what.data;
	      else
	        curWindow = "";
	      break;
	    }
	  };
	}

	private void commonAfterTagHandler()
	{
	  //secure mode for one tag?
	  if (tempMode)
	  {
	    tempMode = false;
	    //set mode back to default mode
	    mode = defaultmode;
	  }
	}

	public void gotClosingTag(String name) 
	{
		  String nm = name.toLowerCase();
		  //hack, to prevent an error from being reported when </var> or end-of-flag comes
		  //we cannot simply test for </var> and friends and disable it then, because
		  //we could have the var tag inside some element
		  boolean oldInVar = inVar;
		  inVar = false;
		  
		  commonTagHandler();
		  
		  //restore the inVar variable...
		  inVar = oldInVar;

		  boolean okay = false;
		  while (!okay)
		  {
		    if (closingTags.isEmpty())
		      break;  //last one closed...
		    //closingTags is a FIFO queue, tho technically it's a list
		    int last = closingTags.size()-1;
		    closingTag tag = closingTags.get(last);
		    closingTags.remove(last);

		    if (tag.name.equals(nm))
		      okay = true;  //good
		    else
		      results.addToList (results.createWarning ("Had to auto-close tag " + tag.name +
		          ", because closing tag </" + name + "> was received."));
		    
		    closeTag (tag);
		  }

		  if (!okay)
		    results.addToList (results.createError ("Received unpaired closing tag </" + name + ">."));

		  commonAfterTagHandler();
	}

	//we treat flag as another tag - this is needed to allow correct flag closing even if the //appropriate closing tag wasn't sent by the MUD (auto-closing of flag)
	public void gotFlag(boolean begin, String flag) 
	{
		  boolean setFlag = false;  //is this a set-variable flag?
		  String f = flag.toLowerCase();
		  if ( f.indexOf("set ") == 0 )
			  setFlag = true;

		  //disable inVar and remember old value, if this is a set-flag
		  //this is needed to prevent error report in commonTagHandler()
		  boolean oldInVar = inVar;
		  if (setFlag) inVar = false;

		  commonTagHandler();
		  
		  //restore inVar value
		  inVar = oldInVar;
		  
		  //no -> inform about the flag
		  if (begin)
		  {
		    MXPResult res = results.createFlag (true, flag);
		    MXPResult res2 = createClosingResult (res);
		    results.addToList (res);
		    addClosingTag ("flag", res2, null);
		    
		    //"set xxx" type of flag?
		    if (setFlag)
		    {
		      if (inVar)  //in variable already
		      {
		        results.addToList (results.createError
		            ("Got a set-flag, but I'm already in a variable definition!"));
		        return;
		      }
		      //we are now in a variable
		      inVar = true;
		      varName = f.substring(f.lastIndexOf(' ') + 1);  //last word
		      varValue = "";
		    }
		  }
		  else
		  {
		    //closing set-flag...
		    if (inVar && setFlag)
		    {
		      results.addToList (results.createVariable (varName, varValue, false));
		      //send variable value, but no varname as in </var>
		      results.addToList (results.createText (varValue));
		      entities.addEntity (varName, varValue);
		      inVar = false;
		      varName = "";
		      varValue = "";
		    }
		    gotClosingTag ("flag");
		  }
		  
		  //no commonAfterTagHandler() here - this ain't no real tag :D
	}

	//mxpResult handling
	MXPResult createClosingResult (MXPResult what)
	{
		MXPResult res = null;
		switch (what.type) 
		{
	    	case 3: 
	    	{
	    		flagStruct fs = (flagStruct) what.data;
	    		res = results.createFlag (false, fs.name);
	    		break;
	    	}
	    	case 5: 
	    	{
	    		formatStruct fs = (formatStruct)what.data;
	    		//usemask is the most relevant thing here - things not enabled there won't be applied,
	    		//so we can place anything there
	    		int usemask = fs.usemask;
	    		int curattrib = (bold?1:0) * libmxp.Bold + (italic?1:0) * libmxp.Italic +
	    			(underline?1:0) * libmxp.Underline + (strikeout?1:0) * libmxp.Strikeout;
	    		String font = "";
	    		if ( (usemask & formatStruct.USE_FONT) != 0 )
	    			font = curfont;
	    		res = results.createFormatting (usemask, curattrib, fgcolor, bgcolor, font, cursize);
	    		break;
	    	}
	    case 15: {
	      res = results.createSetWindow (curWindow);
	      break;
	    }
	  };
	  return res;
	}

	private void addClosingTag(String name, MXPResult res, List<MXPResult> res2)
	{
		closingTag ctag = new closingTag();
		ctag.name = name;
		ctag.closingresult = res;
		ctag.closingresults = res2;
		closingTags.add(ctag);
	}

	public void gotVariable(String name, String string, boolean b) 
	{
		  commonTagHandler();
		  //send the variable value
		  results.addToList(results.createVariable (name, string, b));
		  commonAfterTagHandler();		
	}

	/** 
	 * called upon the VAR tag 
	 */
	public void gotVAR(String name) 
	{
		  commonTagHandler();
		  if (inVar)
		  {
		    results.addToList (results.createError ("Nested VAR tags are not allowed!"));
		    commonAfterTagHandler();
		    return;
		  }

		  //we are now in a variable
		  inVar = true;
		  varName = name;
		  varValue = "";

		  //create a closing result; the variable name shall be updated when the tag will be closed
		  addClosingTag("var", null, null);

		  commonAfterTagHandler();		
	}

	public void gotBOLD() 
	{
		  commonTagHandler();
		  MXPResult res = results.createFormatting(formatStruct.USE_BOLD, libmxp.Bold, 
				  MXPColors.noColor(), MXPColors.noColor(), "", 0);
		  MXPResult res2 = createClosingResult (res);
		  applyResult (res);
		  results.addToList (res);
		  addClosingTag ("b", res2, null);

		  commonAfterTagHandler();
	}

	public void gotITALIC() 
	{
		  commonTagHandler();

		  MXPResult res = results.createFormatting (formatStruct.USE_ITALICS, libmxp.Italic, 
				  MXPColors.noColor(),  MXPColors.noColor(), "", 0);
		  MXPResult res2 = createClosingResult (res);
		  applyResult (res);
		  results.addToList (res);
		  addClosingTag ("i", res2, null);

		  commonAfterTagHandler();
	}

	public void gotUNDERLINE() 
	{
		  commonTagHandler();

		  MXPResult res = results.createFormatting (formatStruct.USE_UNDERLINE, libmxp.Underline, 
				  MXPColors.noColor(), MXPColors.noColor(), "", 0);
		  MXPResult res2 = createClosingResult (res);
		  applyResult (res);
		  results.addToList (res);
		  addClosingTag ("u", res2, null);

		  commonAfterTagHandler();
	}

	public void gotSTRIKEOUT() 
	{
		  commonTagHandler();

		  MXPResult res = results.createFormatting (formatStruct.USE_STRIKEOUT, libmxp.Strikeout, 
				  MXPColors.noColor(),
		      MXPColors.noColor(), "", 0);
		  MXPResult res2 = createClosingResult (res);
		  applyResult (res);
		  results.addToList (res);
		  addClosingTag ("s", res2, null);

		  commonAfterTagHandler();
	}

	public Color bgColor() 
	{
		  return bgcolor;
	}

	public Color fgColor() 
	{
		return fgcolor;	
	}

	public void gotCOLOR(Color fg, Color bg) 
	{
		  commonTagHandler();

		  MXPResult res = results.createFormatting (formatStruct.USE_FG | formatStruct.USE_BG,
				  0, fg, bg, "", 0);
		  MXPResult res2 = createClosingResult (res);
		  applyResult (res);
		  results.addToList (res);
		  addClosingTag ("c", res2, null);

		  commonAfterTagHandler();
	}

	public void gotHIGH() 
	{
		  commonTagHandler();

		  Color color = fgcolor;
		  //High color is computed by adding 128 to each attribute...
		  //This is a very primitive way of doing it, and it's probably insufficient. We'll see.
		  int r,g,b;
		  r = (color.getRed() < 128) ? (color.getRed() + 128) : 255;
		  g = (color.getGreen() < 128) ? (color.getGreen() + 128) : 255;
		  b = (color.getBlue() < 128) ? (color.getBlue() + 128) : 255;
		  color = new Color(r,g,b); 

		  MXPResult res = results.createFormatting (formatStruct.USE_FG, 0, 
				  color, MXPColors.noColor(), "", 0);
		  MXPResult res2 = createClosingResult (res);
		  applyResult (res);
		  results.addToList (res);
		  addClosingTag ("h", res2, null);

		  commonAfterTagHandler();
	}

	public String fontFace() 
	{
		return curfont;
	}

	public int fontSize() 
	{
		return cursize;
	}

	public void gotFONT(String face, int size, Color fg, Color bg) 
	{
		  commonTagHandler();

		  MXPResult res = results.createFormatting (formatStruct.USE_FG | formatStruct.USE_BG |
			  formatStruct.USE_FONT | formatStruct.USE_SIZE, 0, fg, bg,
		      face, size);
		  MXPResult res2 = createClosingResult (res);
		  applyResult (res);
		  results.addToList (res);
		  addClosingTag ("font", res2, null);

		  commonAfterTagHandler();
	}

	public void gotP()
	{
		  commonTagHandler();
		  //we're now in a paragraph
		  inParagraph = true;
		  addClosingTag ("p", null, null);		  
		  //no reporting to the client
		  commonAfterTagHandler();		
	}

	public void gotBR() {
		  commonTagHandler();
		  //inform the client that we got a newline (but no mode changes shall occur)
		  results.addToList (results.createText ("\r\n"));
		  commonAfterTagHandler();
	}

	public void gotNOBR() {
		  commonTagHandler();
		  //next new-line is to be ignored
		  ignoreNextNewLine = true;
		  //no reporting to client
		  commonAfterTagHandler();
	}

	public void gotSBR() {
		  commonTagHandler();
		  //soft-break is represented as 0x1F
		  results.addToList (results.createText ("\u001f"));
		  commonAfterTagHandler();
	}

	public void gotA(String href, String hint, String expire) {
		  commonTagHandler();
		  inLink = true;
		  isALink = true;
		  linkText = "";
		  MXPResult res = results.createLink (expire, href, "", hint);
		  addClosingTag ("a", res, null);
		  commonAfterTagHandler();
	}

	public void gotSEND(String command, String hint, boolean prompt, String expire) 
	{
		  commonTagHandler();

		  inLink = true;
		  isALink = false;
		  linkText = "";
		  gotmap = false;
		  String cmd = stripANSI (command);
		  lastcmd = cmd;
		  MXPResult res = results.createSendLink (expire, cmd, "", hint, prompt,
		      (command.indexOf("|") < 0) ? false : true);

		  addClosingTag ("send", res, null);

		  commonAfterTagHandler();
	}

	public void gotEXPIRE(String name) 
	{
		  commonTagHandler();
		  results.addToList (results.createExpire(name));
		  commonAfterTagHandler();
	}

	public void gotVERSION()
	{
		  commonTagHandler();

		  //this is to be sent...
		  results.addToList (results.createSendThis ("\u001b[1z<VERSION MXP=" + mxpVersion + " CLIENT=" +
		      clientName + " VERSION=" + clientVersion + ">\r\n"));

		  commonAfterTagHandler();
	}

	public void gotHtag(int which) 
	{
		  if ((which < 1) || (which > 6)) //BUG!!!
		  {
		    commonAfterTagHandler();
		    return;
		  }

		  commonTagHandler();

		  int idx = which - 1;
		  MXPResult res = results.createFormatting (formatStruct.USE_ALL, Hattribs[idx], Hfg[idx], Hbg[idx],
		      Hfont[idx], Hsize[idx]);
		  MXPResult res2 = createClosingResult (res);
		  applyResult (res);
		  results.addToList (res);
		  String ct = "h" + (idx+1);
		  addClosingTag (ct, res2, null);

		  commonAfterTagHandler();
	}

	public void gotHR() 
	{
		  commonTagHandler();

		  results.addToList (results.createHorizLine ());

		  commonAfterTagHandler();
	}

	public void gotSMALL() 
	{
		  commonTagHandler();

		  //SMALL means 3/4 of standard size :)
		  MXPResult res = results.createFormatting (formatStruct.USE_SIZE, 0, MXPColors.noColor(),
		    MXPColors.noColor(), "", defaultsize * 3/4);
		  MXPResult res2 = createClosingResult (res);
		  applyResult (res);
		  results.addToList (res);
		  addClosingTag ("small", res2, null);

		  commonAfterTagHandler();
	}

	public void gotTT() 
	{
		  commonTagHandler();

		  MXPResult res = results.createFormatting (formatStruct.USE_FONT, 0, 
				  MXPColors.noColor(), MXPColors.noColor(), ttFont, 0);
		  MXPResult res2 = createClosingResult (res);
		  applyResult (res);
		  results.addToList (res);
		  addClosingTag ("tt", res2, null);

		  commonAfterTagHandler();
	}

	public void gotSOUND(String fname, int vol, int count, int priority, String type, String url) 
	{
		  commonTagHandler();
		  results.addToList (results.createSound (true, fname, vol, count, priority, false, type, url));
		  commonAfterTagHandler();		
	}

	public void gotMUSIC(String fname, int vol, int count, boolean contifrereq, String type,
			String url) 
	{
		  commonTagHandler();
		  results.addToList (results.createSound (false, fname, vol, count, 0, contifrereq, type, url));
		  commonAfterTagHandler();		
	}

	public void gotGAUGE(String entity, String maxentity, String caption, Color color) 
	{
		  commonTagHandler();
		  results.addToList (results.createGauge (entity, maxentity, caption, color));
		  commonAfterTagHandler();		
	}

	public void gotDEST(String name, int x, int y, boolean feol, boolean feof) 
	{
		  commonTagHandler();
		  String nm = name.toLowerCase();
		  boolean nmExists = (frames.containsKey(nm));
		  
		  if (!nmExists)
		  {
		    results.addToList(results.createError(
		    		"Received a request to redirect to non-existing window " + nm));
		    return;
		  }
		  
		  MXPResult res = results.createSetWindow(name);
		  MXPResult res2 = createClosingResult (res);
		  applyResult (res);
		  results.addToList (res);

		  int _x = x;
		  int _y = y;
		  if ((y >= 0) && (x < 0)) _x = 0;
		  if ((_x >= 0) && (_y >= 0))
		    results.addToList (results.createMoveCursor (_x, _y));

		  List<MXPResult> ls = null;
		  //erase AFTER displaying text
		  if (feol || feof)
		  {
		    ls = new ArrayList<MXPResult>();
		    ls.add(res2);
		    res2 = results.createEraseText(feof);
		  }

		  //closing tag...
		  addClosingTag ("dest", res2, ls);

		  commonAfterTagHandler();
	  }

	public void gotSTAT(String entity, String max, String caption) 
	{
		  commonTagHandler();
		  results.addToList (results.createStat(entity, max, caption));
		  commonAfterTagHandler();
	  }

	public int computeCoord(String coord, boolean isX, boolean inWindow ) 
	{
		  int retval = Integer.parseInt(coord);
		  int len = coord.length();
		  char ch = coord.charAt(len - 1);
		  if (ch == 'c') retval *= (isX ? fX : fY);
		  if (ch == '%') retval = retval * (inWindow ? (isX ? wX : wY) : (isX ? sX : sY)) / 100;
		  return retval;
	}

	public void gotFRAME(String name, String action, String title,
			boolean internal, String align, int left, int top, int width,
			int height, boolean scrolling, boolean floating) {
		  commonTagHandler();

		  if (name.isEmpty())
		  {
		    results.addToList (results.createError ("Got FRAME tag without frame name!"));
		    commonAfterTagHandler();
		    return;
		  }

		  String nm = name.toLowerCase();
		  String act = action.toLowerCase();
		  String alg = align.toLowerCase();

		  String tt = title;
		  //name is the default title
		  if (tt.isEmpty())
		    tt = name;
		   
		  //align
		  libmxp.alignType at = libmxp.alignType.Top;
		  if (!align.isEmpty())
		  {
		    boolean alignok = false;
		    if (align.equals("left")) { at = libmxp.alignType.Left; alignok = true; }
		    if (align.equals("right")) { at = libmxp.alignType.Right; alignok = true; }
		    if (align.equals("top")) { at = libmxp.alignType.Top; alignok = true; }
		    if (align.equals("bottom")) { at = libmxp.alignType.Bottom; alignok = true; }
		    if (!alignok)
		      results.addToList (results.createError ("Received FRAME tag with unknown ALIGN option!"));
		  }

		  //does the list of frames contain frame with name nm?
		  boolean nmExists = frames.containsKey(nm);
		  
		  if (act.equals("open"))
		  {
		    if (nmExists)
		    {
		      results.addToList (results.createError ("Received request to create an existing frame!"));
		      commonAfterTagHandler();
		      return;
		    }
		    //cannot create _top or _previous
		    if ((nm.equals("_top")) || (nm.equals("_previous")))
		    {
		      results.addToList (results.createError ("Received request to create a frame with name " +
		          nm + ", which is invalid!"));
		      commonAfterTagHandler();
		      return;
		    }
		    if (internal)
		    {
		      //false for internal windows... value not used as of now, but it may be used later...
		      frames.put(nm, false);
		      results.addToList (results.createInternalWindow (nm, tt, at, scrolling));
		    }
		    else
		    {
		      //true for normal windows... value not used as of now, but it may be used later...
		      frames.put(nm, true);
		      results.addToList (results.createWindow (nm, tt, left, top, width, height,
		          scrolling, floating));
		    }
		  }
		  if (act.equals("close"))
		  {
		    if (nmExists)
		    {
		      frames.remove(nm);
		      results.addToList (results.createCloseWindow (nm));
		    }
		    else
		      results.addToList (results.createError
		          ("Received request to close a non-existing frame!"));
		  }
		  if (act.equals("redirect"))
		  {
		    //if the frame exists, or if the name is either _top or _previous, we redirect to that window
		    if ((nm.equals("_top")) || (nm.equals("_previous")) || nmExists)
		      redirectTo (nm);

		    else
		    {
		      //create that window
		      if (internal)
		      {
		        //false for internal windows... value not used as of now, but it may be used later...
		        frames.put(nm,false);
		        results.addToList (results.createInternalWindow (nm, tt, at, scrolling));
		      }
		      else
		      {
		        //true for normal windows... value not used as of now, but it may be used later...
		    	  frames.put(nm,true);
		        results.addToList (results.createWindow (nm, tt, left, top, width, height,
		            scrolling, floating));
		      }
		      //then redirect to it
		      redirectTo (nm);
		    }
		  }

		  commonAfterTagHandler();
	}

	private void redirectTo(String nm)
	{
		  nm = nm.toLowerCase();		  
		  String emptystring = "";
		  MXPResult res = null;
		  if (nm.equals("_top"))
		    res = results.createSetWindow(emptystring);
		  else
		  if (nm.equals("_previous"))
		    res = results.createSetWindow (prevWindow);
		  else
		    if (frames.containsKey(nm))
		      res = results.createSetWindow (nm);
		    else
		      res = results.createError ("Received request to redirect to non-existing window " + nm);
		  //apply result - will update info about previous window and so...
		  applyResult (res);
		  results.addToList (res);
	}

	public void gotRELOCATE(String hostname, int port) 
	{
		  commonTagHandler();
		  results.addToList (results.createRelocate (hostname, port));
		  commonAfterTagHandler();		
	}

	public void gotUSER() 
	{
		  commonTagHandler();
		  results.addToList (results.createSendLogin (true));
		  commonAfterTagHandler();		
	}

	public void gotPASSWORD() 
	{
		  commonTagHandler();
		  results.addToList (results.createSendLogin (false));
		  commonAfterTagHandler();		
	}

	public void gotIMAGE(String fname, String url, String type, int height, int width,
			int hspace, int vspace, String align, boolean ismap) 
	{
		  commonTagHandler();

		  //align
		  String alg = align.toLowerCase();
		  alignType at = alignType.Top;
		  if (!align.isEmpty())
		  {
		    boolean alignok = false;
		    if (align.equals("left")) { at = alignType.Left; alignok = true; }
		    if (align.equals("right")) { at = alignType.Right; alignok = true; }
		    if (align.equals("top")) { at = alignType.Top; alignok = true; }
		    if (align.equals("bottom")) { at = alignType.Bottom; alignok = true; }
		    if (align.equals("middle")) { at = alignType.Middle; alignok = true; }
		    if (!alignok)
		      results.addToList (results.createError ("Received IMAGE tag with unknown ALIGN option!"));
		  }

		  if (gotmap)
		    results.addToList (results.createError ("Received multiple image maps in one SEND tag!"));

		  if (ismap)
		  {
		    if (inLink && (!isALink))
		    {
		      results.addToList (results.createImageMap(lastcmd));
		      lastcmd = "";
		      gotmap = true;
		    }
		    else
		      results.addToList (results.createError ("Received an image map with no SEND tag!"));
		  }
		  results.addToList (results.createImage (fname, url, type, height, width, hspace, vspace, at));

		  commonAfterTagHandler();
	}

	public void gotNewLine() 
	{
		  //got a newline char - close outstanding entities, if any (unless we're in LOCKED mode)
		  if (mode != mxpMode.lockedMode)
		  {
		    String t = entities.expandEntities ("", true);
		    if (!t.isEmpty())
		      gotText (t, false);
		  }

		  //was temp-secure mode?
		  if (tempMode)
		  {
		    tempMode = false;
		    mode = defaultmode;
		    results.addToList (results.createError ("Temp-secure line tag followed by a newline!"));
		  }

		  //leaving secure mode?
		  wasSecureMode = false;
		  if ((mode == mxpMode.secureMode) && (defaultmode != mxpMode.secureMode))
		    wasSecureMode = true;

		  //ending line in OPEN mode - close all tags!
		  if (mode == mxpMode.openMode)
		    closeAllTags ();

		  //is we're in SECURE mode, some tags may need to be closed...

		  //line ended inside a link
		  if (inLink)
		  {
		    inLink = false;
		    isALink = false;
		    linkText = "";
		    results.addToList (results.createError ("Received an unterminated link!"));
		  }

		  if (inVar)
		  {
		    inVar = false;
		    results.addToList (results.createError ("Received an unterminated VAR tag!"));
		    varValue = "";
		  }

		  //should next newline be ignored?
		  if (ignoreNextNewLine)
		  {
		    ignoreNextNewLine = false;
		    return;
		  }

		  //if we're in a paragraph, don't report the new-line either
		  if (inParagraph)
		    return;

		  //set mode back to default mode
		  mode = defaultmode;
		  
		  //neither NOBR nor P - report newline
		  results.addToList (results.createText ("\r\n"));
	}

	public void gotLineTag(int number) 
	{
		  //got a line tag - close outstanding entities, if any (unless we're in LOCKED mode)
		  if (mode != mxpMode.lockedMode)
		  {
		    String t = entities.expandEntities ("", true);
		    if (!t.isEmpty())
		      gotText (t, false);
		  }

		  //leaving secure mode
		  if (wasSecureMode && (number != 1))
		    closeAllTags ();
		  wasSecureMode = false;

		  if (number < 0) return;
		  if (number > 99) return;
		  if (number >= 10)
		    results.addToList (results.createLineTag(number));
		  else
		  {
		    switch (number) {
		      case 0:
		        setMXPMode(mxpMode.openMode);
		        break;
		      case 1:
		        setMXPMode (mxpMode.secureMode);
		        break;
		      case 2:
		        setMXPMode (mxpMode.lockedMode);
		        break;
		      case 3:
		        closeAllTags ();
		        //default mode remains the same...
		        setMXPMode (mxpMode.openMode);
		        reset ();
		        break;
		      case 4:
		        setMXPMode (mxpMode.secureMode);
		        tempMode = true;
		        break;
		      case 5:
		        setMXPMode (mxpMode.openMode);
		        defaultmode = mxpMode.openMode;
		        break;
		      case 6:
		        setMXPMode (mxpMode.secureMode);
		        defaultmode = mxpMode.secureMode;
		        break;
		      case 7:
		        setMXPMode (mxpMode.lockedMode);
		        defaultmode = mxpMode.lockedMode;
		        break;
		      default:
		        results.addToList (results.createWarning ("Received unrecognized line tag."));
		        break;
		    };
		  }
		
	}

	private void setMXPMode(mxpMode m)
	{
		  mode = m;
		  tempMode = false;
		  wasSecureMode = false;
		  
		  //if we start in LOCKED mode and mode change occurs, we set default mode
		  //to OPEN, so that we are compatible with the spec...
		  if (initiallyLocked)
		  {
		    initiallyLocked = false;
		    defaultmode = mxpMode.openMode;
		  }
		
	}

	public void setDefaultText(String font, int size, boolean _bold,
			boolean _italic, boolean _underline, boolean _strikeout, Color fg,
			Color bg) 
	{
		  if (curfont == defaultfont) curfont = font;
		  defaultfont = font;

		  if (cursize == defaultsize) cursize = size;
		  defaultsize = size;

		  int curattrib = (bold?1:0) * libmxp.Bold + (italic?1:0) * libmxp.Italic +
		      (underline?1:0) * libmxp.Underline + (strikeout?1:0) * libmxp.Strikeout;
		  int newattribs = (_bold?1:0) * libmxp.Bold + (_italic?1:0) * libmxp.Italic +
		      (_underline?1:0) * libmxp.Underline + (_strikeout?1:0) * libmxp.Strikeout;
		  if (curattrib == defaultattribs)
		  {
		    bold = _bold;
		    italic = _italic;
		    underline = _underline;
		    strikeout = _strikeout;
		  }
		  defaultattribs = newattribs;

		  if (fgcolor == defaultfg) fgcolor = fg;
		  defaultfg = fg;
		  if (bgcolor == defaultbg) bgcolor = bg;
		  defaultbg = bg;
	}

	public void setHeaderParams(int which, String font, int size,
			boolean _bold, boolean _italic, boolean _underline,
			boolean _strikeout, Color fg, Color bg) 
	{
		  //invalid H-num?
		  if ((which < 1) || (which > 6))
		    return;

		  Hfont[which - 1] = font;

		  Hsize[which - 1] = size;

		  int newattribs = (_bold?1:0) * libmxp.Bold + (_italic?1:0) * libmxp.Italic +
		      (_underline?1:0) * libmxp.Underline + (_strikeout?1:0) * libmxp.Strikeout;
		  Hattribs[which - 1] = newattribs;

		  Hfg[which - 1] = fg;
		  Hbg[which - 1] = bg;
		
	}

	public void setNonProportFont(String font)
	{
		  ttFont = font;		
	}

	public void setClient(String name, String version)
	{
		clientName = name;
		clientVersion = version;
	}

	public void supportsLink (boolean supports)
	{
	  suplink = supports;
	}

	public void supportsGauge (boolean supports)
	{
	  supgauge = supports;
	}

	public void supportsStatus (boolean supports)
	{
	  supstatus = supports;
	}

	public void supportsSound (boolean supports)
	{
	  supsound = supports;
	}

	public void supportsFrame (boolean supports)
	{
	  supframe = supports;
	}

	public void supportsImage (boolean supports)
	{
	  supimage = supports;
	}

	public void supportsRelocate (boolean supports)
	{
	  suprelocate = supports;
	}

	public void switchToOpen ()
	{
	  mode = mxpMode.openMode;
	  defaultmode = mxpMode.openMode;
	  initiallyLocked = false;
	  //not we conform to MXP spec... use with care - only affects non-MXP MUDs, where it allows
	  //open tags - MUDs supporting MXP are NOT affected
	}
	
	public void setScreenProps (int sx, int sy, int wx, int wy, int fx, int fy)
	{
	  sX = sx;
	  sY = sy;
	  wX = wx;
	  wY = wy;
	  fX = fx;
	  fY = fy;
	}
	
}
