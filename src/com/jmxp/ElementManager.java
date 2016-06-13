package com.jmxp;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.jmxp.MXPState.mxpMode;
import com.jmxp.structures.chunk;
import com.jmxp.structures.chunk.chunkType;

public class ElementManager 
{
	  /** list of all custom elements */
	  private HashMap<String, sElement> elements = new HashMap<String, sElement>();
	  private HashMap<String, sInternalElement> ielements = new HashMap<String, sInternalElement>();

	  /** line tags associated with elements */
	  private HashMap<Integer, String> lineTags = new HashMap<Integer, String>();

	  /** aliases for internal elements */
	  private HashMap<String, String> aliases = new HashMap<String, String>();

	  /** last line tag */
	  private int lastLineTag;

	  /** state class */
	  private MXPState state;
	  /** result handler */
	  private ResultHandler results;
	  /** entity manager */
	  private EntityManager entities;
	  /** expander of parameters in custom tags */
	  private EntityManager paramexpander;
	  /** parser of custom element definitions */
	  private MXPParser parser;


	  enum tagParserState {
	    tagBegin,
	    tagName,
	    tagParam,
	    tagParamValue,
	    tagQuotedParam,
	    tagAfterQuotedParam,
	    tagBetweenParams
	  };
	  
	  enum paramParserState {
		  parNone,
		  parName,
		  parValue,
		  parQuotedValue
		};

	  
	
	private class sElementPart 
	{
		  public boolean istag;
		  public String text;
	};

	/** one external element (defined by <!element>) */
	private class sElement 
	{
	  /** is it an open element? */
	  public boolean open;
	  /** is it an element with no closing tag? */
	  public boolean empty;
	  /** tag associated with this element */
	  int tag;
	  /** flag associated with this element */
	  public String flag = "";
	  /** list of all element contents */
	  public ArrayList<sElementPart> element = new ArrayList<sElementPart>();
	  /** list of element attributes in the right order */
	  public ArrayList<String> attlist = new ArrayList<String>();
	  /** default values for attributes */
	  public HashMap<String, String> attdefault = new HashMap<String, String>();
	  /** closing sequence */
	  public ArrayList<String> closingseq = new ArrayList<String>();
	};

	/** 
	 * one internal element 
	 */
	private class sInternalElement 
	{
	  /** is it an open element? */
	  public boolean open;
	  /** is it an element with no closing tag? */
	  public boolean empty;
	  /** list of element attributes in the right order */
	  public ArrayList<String> attlist = new ArrayList<String>();
	  /** default values for attributes; if there's an empty public String (but defined), then it's a flag */
	  public HashMap<String, String> attdefault = new HashMap<String, String>();
	};

	/** one parameter in one tag :-) */
	private class sParam {
	  public boolean flag;
	  public String name = "";
	  public String value = "";
	};
	
	/** constructor */
	public ElementManager (MXPState st, ResultHandler res, EntityManager enm)
	{
		  state = st;
		  results = res;
		  entities = enm;

		  paramexpander = new EntityManager (true);
		  parser = new MXPParser(null,null,null);
		  
		  reset();
		  createInternalElements();		
	}

	public void reset()
	{
	  lastLineTag = 0;
	  removeAll();
	}
	
	public void createInternalElements()
	{
	  //the list doesn't contain information on whether an argument is required or not
	  //processor of the tag implements this functionality

	  //create all internal elements
	  sInternalElement e;

	  //!element
	  e = new sInternalElement();
	  e.empty = true;
	  e.open = false;
	  e.attlist.add("name");  //this name is not in the spec!
	  e.attlist.add("definition");  //this name is not in the spec!
	  e.attlist.add("att");
	  e.attlist.add("tag");
	  e.attlist.add("flag");
	  e.attlist.add("open");
	  e.attlist.add("delete");
	  e.attlist.add("empty");
	  e.attdefault.put("open","");  //flags
	  e.attdefault.put("delete","");
	  e.attdefault.put("empty","");
	  ielements.put("!element", e);

	  //!attlist
	  e = new sInternalElement();
	  e.empty = true;
	  e.open = false;
	  e.attlist.add("name");  //this name is not in the spec!
	  e.attlist.add("att");
	  ielements.put("!attlist", e);

	  //!entity
	  e = new sInternalElement();
	  e.empty = true;
	  e.open = false;
	  e.attlist.add("name");  //this name is not in the spec!
	  e.attlist.add("value");  //this name is not in the spec!
	  e.attlist.add("desc");
	  e.attlist.add("private");
	  e.attlist.add("publish");
	  e.attlist.add("add");
	  e.attlist.add("delete");
	  e.attlist.add("remove");
	  e.attdefault.put("private", "");  //flags
	  e.attdefault.put("publish", "");
	  e.attdefault.put("delete", "");
	  e.attdefault.put("add", "");
	  e.attdefault.put("remove", "");
	  ielements.put("!entity", e);

	  //var
	  e = new sInternalElement();
	  e.empty = false;
	  e.open = false;
	  e.attlist.add("name");  //this name is not in the spec!
	  e.attlist.add("desc");
	  e.attlist.add("private");
	  e.attlist.add("publish");
	  e.attlist.add("add");
	  e.attlist.add("delete");
	  e.attlist.add("remove");
	  e.attdefault.put("private", "");  //flags
	  e.attdefault.put("publish", "");
	  e.attdefault.put("delete", "");
	  e.attdefault.put("add", "");
	  e.attdefault.put("remove", "");
	  ielements.put("var", e);

	  //b
	  e = new sInternalElement();
	  e.empty = false;
	  e.open = true;
	  ielements.put("b", e);

	  //i
	  e = new sInternalElement();
	  e.empty = false;
	  e.open = true;
	  ielements.put("i", e);

	  //u
	  e = new sInternalElement();
	  e.empty = false;
	  e.open = true;
	  ielements.put("u", e);

	  //s
	  e = new sInternalElement();
	  e.empty = false;
	  e.open = true;
	  ielements.put("s", e);

	  //c
	  e = new sInternalElement();
	  e.empty = false;
	  e.open = true;
	  e.attlist.add("fore");
	  e.attlist.add("back");
	  ielements.put("c", e);

	  //h
	  e = new sInternalElement();
	  e.empty = false;
	  e.open = true;
	  ielements.put("h", e);

	  //font
	  e = new sInternalElement();
	  e.empty = false;
	  e.open = true;
	  e.attlist.add("face");
	  e.attlist.add("size");
	  e.attlist.add("color");
	  e.attlist.add("back");
	  ielements.put("font", e);

	  //nobr
	  e = new sInternalElement();
	  e.empty = true;
	  e.open = false;
	  ielements.put("nobr", e);

	  //p
	  e = new sInternalElement();
	  e.empty = false;
	  e.open = false;
	  ielements.put("p", e);

	  //br
	  e = new sInternalElement();
	  e.empty = true;
	  e.open = false;
	  ielements.put("br", e);

	  //sbr
	  e = new sInternalElement();
	  e.empty = true;
	  e.open = false;
	  ielements.put("sbr", e);

	  //a
	  e = new sInternalElement();
	  e.empty = false;
	  e.open = false;
	  e.attlist.add("href");
	  e.attlist.add("hint");
	  e.attlist.add("expire");
	  ielements.put("a", e);

	  //send
	  e = new sInternalElement();
	  e.empty = false;
	  e.open = false;
	  e.attlist.add("href");
	  e.attlist.add("hint");
	  e.attlist.add("prompt");
	  e.attlist.add("expire");
	  e.attdefault.put("prompt", "");  //flags
	  ielements.put("send", e);

	  //expire
	  e = new sInternalElement();
	  e.empty = true;
	  e.open = false;
	  e.attlist.add("name");  //this name is not in the spec!
	  ielements.put("expire", e);

	  //version
	  e = new sInternalElement();
	  e.empty = true;
	  e.open = false;
	  ielements.put("version", e);

	  //support
	  e = new sInternalElement();
	  e.empty = true;
	  e.open = false;
	  ielements.put("support", e);

	  //h1
	  e = new sInternalElement();
	  e.empty = false;
	  e.open = false;
	  ielements.put("h1", e);

	  //h2
	  e = new sInternalElement();
	  e.empty = false;
	  e.open = false;
	  ielements.put("h2", e);

	  //h3
	  e = new sInternalElement();
	  e.empty = false;
	  e.open = false;
	  ielements.put("h3", e);

	  //h4
	  e = new sInternalElement();
	  e.empty = false;
	  e.open = false;
	  ielements.put("h4", e);

	  //h5
	  e = new sInternalElement();
	  e.empty = false;
	  e.open = false;
	  ielements.put("h5", e);

	  //h6
	  e = new sInternalElement();
	  e.empty = false;
	  e.open = false;
	  ielements.put("h6", e);

	  //hr
	  e = new sInternalElement();
	  e.empty = true;
	  e.open = false;
	  ielements.put("hr", e);

	  //small
	  e = new sInternalElement();
	  e.empty = false;
	  e.open = false;
	  ielements.put("small", e);

	  //tt
	  e = new sInternalElement();
	  e.empty = false;
	  e.open = false;
	  ielements.put("tt", e);

	  //sound
	  e = new sInternalElement();
	  e.empty = true;
	  e.open = false;
	  e.attlist.add("fname");
	  e.attlist.add("v");
	  e.attlist.add("l");
	  e.attlist.add("p");
	  e.attlist.add("t");
	  e.attlist.add("u");
	  e.attdefault.put("v", "100");
	  e.attdefault.put("l", "1");
	  e.attdefault.put("p", "50");
	  ielements.put("sound", e);

	  //music
	  e = new sInternalElement();
	  e.empty = true;
	  e.open = false;
	  e.attlist.add("fname");
	  e.attlist.add("v");
	  e.attlist.add("l");
	  e.attlist.add("c");
	  e.attlist.add("t");
	  e.attlist.add("u");
	  e.attdefault.put("v", "100");
	  e.attdefault.put("l", "1");
	  e.attdefault.put("c", "1");
	  ielements.put("music", e);

	  //gauge
	  e = new sInternalElement();
	  e.empty = true;
	  e.open = false;
	  e.attlist.add("entity");  //this name is not in the spec!
	  e.attlist.add("max");
	  e.attlist.add("caption");
	  e.attlist.add("color");
	  ielements.put("gauge", e);

	  //stat
	  e = new sInternalElement();
	  e.empty = true;
	  e.open = false;
	  e.attlist.add("entity");  //this name is not in the spec!
	  e.attlist.add("max");
	  e.attlist.add("caption");
	  ielements.put("stat", e);

	  //frame
	  e = new sInternalElement();
	  e.empty = true;
	  e.open = false;
	  e.attlist.add("name");
	  e.attlist.add("action");
	  e.attlist.add("title");
	  e.attlist.add("internal");
	  e.attlist.add("align");
	  e.attlist.add("left");
	  e.attlist.add("top");
	  e.attlist.add("width");
	  e.attlist.add("height");
	  e.attlist.add("scrolling");
	  e.attlist.add("floating");
	  e.attdefault.put("action", "open");
	  e.attdefault.put("align", "top");
	  e.attdefault.put("left", "0");
	  e.attdefault.put("top", "0");
	  e.attdefault.put("internal", "");  //flags
	  e.attdefault.put("scrolling", "");
	  e.attdefault.put("floating", "");
	  ielements.put("frame", e);

	  //dest
	  e = new sInternalElement();
	  e.empty = false;
	  e.open = false;
	  e.attlist.add("name");  //this name is not in the spec!
	  e.attlist.add("x");
	  e.attlist.add("y");
	  e.attlist.add("eol");
	  e.attlist.add("eof");
	  e.attdefault.put("eol", "");  //flags
	  e.attdefault.put("eof", "");
	  ielements.put("dest", e);

	  //relocate
	  e = new sInternalElement();
	  e.empty = true;
	  e.open = false;
	  e.attlist.add("name");  //this name is not in the spec!
	  e.attlist.add("port");  //this name is not in the spec!
	  ielements.put("relocate", e);

	  //user
	  e = new sInternalElement();
	  e.empty = true;
	  e.open = false;
	  ielements.put("user", e);

	  //password
	  e = new sInternalElement();
	  e.empty = true;
	  e.open = false;
	  ielements.put("password", e);

	  //image
	  e = new sInternalElement();
	  e.empty = true;
	  e.open = false;
	  e.attlist.add("fname");
	  e.attlist.add("url");
	  e.attlist.add("t");
	  e.attlist.add("h");
	  e.attlist.add("w");
	  e.attlist.add("hspace");
	  e.attlist.add("vspace");
	  e.attlist.add("align");
	  e.attlist.add("ismap");
	  e.attdefault.put("align", "top");
	  e.attdefault.put("ismap", "");  //flags
	  ielements.put("image", e);

	  //filter
	  e = new sInternalElement();
	  e.empty = true;
	  e.open = false;
	  e.attlist.add("src");
	  e.attlist.add("dest");
	  e.attlist.add("name");
	  ielements.put("filter", e);

	  //finally, define some aliases for internal elements
	  aliases.put("!el", "!element");
	  aliases.put("!at", "!attlist");
	  aliases.put("!en", "!entity");
	  aliases.put("v", "var");
	  aliases.put("bold", "b");
	  aliases.put("strong", "b");
	  aliases.put("italic", "i");
	  aliases.put("em", "i");
	  aliases.put("underline", "u");
	  aliases.put("strikeout", "s");
	  aliases.put("high", "h");
	  aliases.put("color", "c");
	  aliases.put("destination", "dest");
	}
	
	private void removeAll()
	{
	  //external elements only
	  Object names [] = elements.keySet().toArray();
	  for (Object object : names) 
	  {
		removeElement((String)object);
	  }
	}

	private void removeElement (String name)
	{
	  //external elements only
	  if (elements.containsKey(name))
	  {
	    sElement e = elements.get(name);
	    e.element.clear();
	    e.attlist.clear();
	    e.attdefault.clear();
	    e.closingseq.clear();
	    if (e.tag != 0 )
	      lineTags.remove(e.tag);
	    elements.remove(name);
	  }
	}

	  /** destructor */
	  protected void finalize() throws Throwable 
	  {
		  paramexpander = null;
		  parser = null;		  
		  removeAll ();
		  //internal elements
		  Collection<sInternalElement> values = ielements.values();
		  for (sInternalElement internalElement : values) 
		  {
			  internalElement.attlist.clear();
			  internalElement.attdefault.clear();
		  }
		  ielements.clear();
		  aliases.clear();
		  super.finalize();
	  }

	  /** set pointer to cMXPState - needed due to circular dependencies */
	  public void assignMXPState (MXPState st)
	  {
		  state = st;
	  }

	  /** 
	   * is this element defined? 
	   */
	  public boolean elementDefined (String name)
	  {
		  return ((elements.containsKey(name)) || (ielements.containsKey(name)) ||
			      (aliases.containsKey(name)));		  
	  }

	  /** 
	   * is it an internal tag? 
	   */
	  public boolean internalElement (String name)
	  {
	    return ((ielements.containsKey(name)) || (aliases.containsKey(name)));
	  }

	  /** 
	   * is it a custom element? (i.e. defined via <!element>) 
	   */
	  public boolean customElement (String name)
	  {
	    return (elements.containsKey(name));
	  }

	  /** 
	   * open element? 
	   */	  
	  public boolean openElement (String name)
	  {
	    if (!elementDefined (name))
	      return false;
	    if (internalElement (name))
	    {
	      String n = name;
	      if (aliases.containsKey(name))
	        n = aliases.get(name);
	      return ielements.get(n).open;
	    }
	    else
	      return elements.get(name).open;
	  }

	  /** 
	   * empty element? i.e. does it need a closing tag? 
	   */
	  public boolean emptyElement (String name)
	  {
	    if (!elementDefined (name))
	      return false;
	    if (internalElement (name))
	    {
	      String n = name;
	      if (aliases.containsKey(name))
	        n = aliases.get(name);
	      return ielements.get(n).empty;
	    }
	    else
	      return elements.get(name).empty;
	  }

	  public void gotTag (String tag) throws Exception
	  {
	    String tagname = "";
	    ArrayList<sParam> params = new ArrayList<sParam>();
	    sParam param = new sParam();
	    param.flag = false;
	    char quote = 0;
	    tagParserState pstate = tagParserState.tagBegin;
	    for (int i = 0; i<tag.length(); i++)
	    {
	      char ch = tag.charAt(i);
	      //process character
	      switch (pstate) {
	        case tagBegin: {
	          if (ch != ' ')
	          {
	            pstate = tagParserState.tagName;
	            tagname += ch;
	          }
	          break;
	        }
	        case tagName: {
	          if (ch == ' ')
	            pstate = tagParserState.tagBetweenParams;
	          else
	            tagname += ch;
	          break;
	        }
	        case tagParam: {
	          if (ch == '=')
	            pstate = tagParserState.tagParamValue;
	          else if (ch == ' ')
	          {
	            //one parameter, value only (it could also be a flag, we'll check that later)
	            param.value = param.name;
	            param.name = "";
	            //add a new parameter :-)
	            params.add(param);
	            param = new sParam();
	            param.value = "";
	            pstate = tagParserState.tagBetweenParams;
	          }
	          else
	            param.name += ch;
	          break;
	        }
	        case tagParamValue: {
	          if (ch == ' ')
	          {
	            //add a new parameter :-)
	            params.add(param);
	            param = new sParam();
	            param.name = "";
	            param.value = "";
	            pstate = tagParserState.tagBetweenParams;
	          }
	          else if (param.value.isEmpty() && ((ch == '\'') || (ch == '"')))
	          {
	            pstate = tagParserState.tagQuotedParam;
	            quote = ch;
	          }
	          else
	            param.value += ch;
	          break;
	        }
	        case tagQuotedParam: {
	          if (ch == quote)
	          {
	            //add a new parameter :-)
	            params.add(param);
	            param = new sParam();
	            param.name = "";
	            param.value = "";
	            pstate = tagParserState.tagAfterQuotedParam;
	          }
	          else
	            param.value += ch;
	          break;
	        }
	        case tagAfterQuotedParam: {
	          if (ch == ' ')    //ignore everything up to some space...
	            pstate = tagParserState.tagBetweenParams;
	          break;
	        }
	        case tagBetweenParams: {
	          if (ch != ' ')
	          {
	            if ((ch == '\'') || (ch == '"'))
	            {
	              pstate = tagParserState.tagQuotedParam;
	              param.name = "";
	              quote = ch;
	            }
	            else
	            {
	              pstate = tagParserState.tagParam;
	              param.name += ch;
	            }
	          }
	          break;
	        }
	      };
	    }

	    //last parameter...
	    switch (pstate) 
	    {
	      case tagBegin:
	        results.addToList (results.createError ("Received a tag with no body!"));
	        break;
	      case tagParam: {
	        param.value = param.name;
	        param.name = "";
	        params.add(param);
	        param = new sParam();
	        }
	        break;
	      case tagParamValue:
	        params.add(param);
	        param = new sParam();
	        break;
	      case tagQuotedParam:
	        results.addToList (results.createError ("Received tag " + tagname +
	            " with unfinished quoted parameter!"));
	        break;
	    };

	    //nothing more to do if the tag has no contents...
	    if (pstate == tagParserState.tagBegin) return;
	    
	    //convert tag name to lowercase
	    tagname = tagname.toLowerCase();
	    
	    //handle closing tag...
	    if (tagname.charAt(0) == '/')
	    {
	      if (!params.isEmpty())
	      {
	        results.addToList (results.createError ("Received closing tag " + tagname +
	            " with parametrs!"));
	      }
	      //remove that '/'
	      tagname = tagname.substring(1);
	      //and call closing tag processing
	      handleClosingTag(tagname);
	      return;
	    }
	    
	    //convert all parameter names to lower-case
	    for (sParam param2 : params) 
	    {
	    	param2.name = param2.name.toLowerCase();
		}
	    
	    //now we check the type of the tag and act accordingly
	    if (!elementDefined (tagname))
	    {
	      params.clear();
	      results.addToList (results.createError ("Received undefined tag " + tagname + "!"));
	      return;
	    }

	    mxpMode m = state.getMXPMode ();
	    //mode can be open or secure; locked mode is not possible here (or we're in a bug)
	    if (m == mxpMode.openMode)
	      //open mode - only open tags allowed
	      if (!openElement (tagname))
	      {
	      params.clear();
	        results.addToList (results.createError ("Received secure tag " + tagname +
	            " in open mode!"));
	        return;
	      }

	    if (internalElement (tagname))
	    {
	      //if the name is an alias for another tag, change the name
	      if (aliases.containsKey(tagname))
	        tagname = aliases.get(tagname);
	      //the <support> tag has to be handled separately :(
	      if (tagname.equals("support"))
	      {
	        processSupport(params);
	        return;
	      }
	      //identify all flags in the tag
	      identifyFlags(ielements.get(tagname).attdefault, params);
	      //correctly identify all parameters (assign names where necessary)
	      handleParams (tagname, params, ielements.get(tagname).attlist,
	    		  ielements.get(tagname).attdefault);
	      //separate out all the flags (flags are only valid for internal tags)
	      ArrayList<String> flags = new ArrayList<String>();
	      for ( int i =0; i < params.size();)
	      {
	    	  sParam item = params.get(i);
	    	  if (item.flag)
	    	  {
	    		  flags.add(item.name);
	    		  params.remove(i);
	    	  }
	    	  else
	    		  i++;
	      }
	      //okay, parsing done - send the tag for further processing
	      processInternalTag(tagname, params, flags);
	    }
	    else
	    {
	      handleParams (tagname, params, elements.get(tagname).attlist,
	    		  elements.get(tagname).attdefault);
	      processCustomTag(tagname, params);
	    }
	    
	    params.clear ();
	  }

	  private void handleClosingTag (String name)
	  {
	    String n = name.toLowerCase();
	    if (!elementDefined (n))
	    {
	      results.addToList (results.createError ("Received unknown closing tag </" + n + ">!"));
	      return;
	    }
	    if (emptyElement (n))
	    {
	      results.addToList (results.createError ("Received closing tag for tag " + n +
	          ", which doesn't need a closing tag!"));
	      return;
	    }
	    if (internalElement (n))
	    {
	      //if the name is an alias for another tag, change the name
	      if (aliases.containsKey(n))
	      {
	    	  n = aliases.get(n);
	      }
	      state.gotClosingTag(n);
	    }
	    else
	    {
	      //send closing flag, if needed
	      if (!elements.get(n).flag.isEmpty())
	        state.gotFlag (false, elements.get(n).flag);
	      
	      //expand the closing tag...
	      for (String item : elements.get(n).closingseq) 
	      {
	    	  handleClosingTag(item);
	      }
	    }
	  }

	  private void processSupport (List<sParam> params) throws Exception
	  {
	    List<String> pars = new ArrayList<String>();
	    for (sParam param : params) 
	    {
			pars.add(param.value);
		}
	    state.gotSUPPORT(pars);
	  }

	  private void identifyFlags (HashMap<String, String> attdefault, List<sParam> args)
	  {
		  for (sParam param : args) 
		  {
			  if ( param.name.isEmpty())
			  {
				  String s = param.value.toLowerCase();
				  if ( attdefault.containsKey(s) && (attdefault.get(s).isEmpty()))
				  {
					param.name = s;
					param.value = "";
					param.flag = true; 
				  }
			  }
		  }
	  }
	  
	  private void handleParams (String tagname, List<sParam> args,
			    List<String> attlist, HashMap<String, String> attdefault)
			{
			  //list<string>::const_iterator cur = attlist.begin();
			  //list<sParam>::iterator it;
		  	  int cur = 0;
			  for (sParam item : args) 
			  {				  
				//flag?
			    if (item.flag)
			    {
			      //only advance parameter iterator
			      cur++;
			    }
			    //not a flag
			    else
			    {
			      //empty name?
			      if (item.name.isEmpty())
			      {
			        //set the parameter name:
			        
			        //find the correct attribute name, skipping all flags
			        while (cur < attlist.size())
			        {
			          if ((attdefault.containsKey(attlist.get(cur))) 
			        		  && (attdefault.get(attlist.get(cur)).isEmpty()))  //flag
			            cur++;
			          else
			            break;  //okay, found the correct parameter
			        }
			        if (cur == attlist.size())    //ARGH! Parameter not found :(
			        {
			          results.addToList (results.createError ("Received too many parameters for tag " +
			              tagname + "!"));
			          continue;  //continue with the next parameter...
			        }
			      }
			      //non-empty name?
			      else
			      {
			        //set "cur" to the parameter following the given one
			        
			        //to speed things up a bit, first look if the iterator is pointing at the right parameter
			        // (we won't need to traverse the whole list, if it does)
			        if ((cur == attlist.size()) || (item.name != attlist.get(cur)))
			        {
			          int cur2 = cur;  //remember old "cur" value
			          for (cur = 0; cur < attlist.size(); cur++)
			            if (item.name.equals(attlist.get(cur)))
			              break;
			          if (cur == attlist.size())    //parameter name not found
			          {
			            //restore old iterator value
			            cur = cur2;
			            results.addToList (results.createError ("Received unknown parameter " +
			                item.name + " in tag " + tagname + "!"));
			            //clear name/value to avoid confusion in later stages
			            item.name = "";
			            item.value = "";
			            //proceed with next parameter
			            continue;
			          }
			          //if cur isn't attlist.end(), it's now set to the correct value...
			        }
			      }
			      
			      //things common to all non-flag parameters...
			      
			      //set parameter name
			      item.name = attlist.get(cur);
			      //if parameter value is empty, set it to default value (if any)
			      if (item.value.isEmpty() && (attdefault.containsKey(attlist.get(cur))))
			        item.value = attdefault.get(attlist.get(cur));
			      //advance parameter iterator
			      cur++;
			    }
			  }
			  
			  //finally, we add default parameter values to the beginning of the list... these shall get
			  //overridden by given values, if any (those shall be later in the list)
			  for (Object item : attdefault.keySet().toArray()) 
			  {
				if ( !attdefault.get(item).isEmpty())
				{
					sParam s = new sParam();
				    s.flag = false;
				    s.name = (String)item;
				    s.value = attdefault.get(item);
				    args.add(0, s);					
				}
			  }
			}

	  private void processInternalTag (String name2, List<sParam> params,
			    List<String> flags)
			{
			  //list<sParam>::const_iterator it;
			  //list<string>::const_iterator it2;
			  if (name2.equals("!element"))
			  {
			    String lname = "", definition = "", att = "", flag = "";
			    int tag = 0;
			    boolean fopen = false, fdelete = false, fempty = false;
			    for (sParam item : params) 
			    {
			      String s = item.name;
			      if (s.equals("name")) lname = item.value.toLowerCase();
			      if (s.equals("definition")) definition = item.value;
			      if (s.equals("att")) att = item.value;
			      if (s.equals("flag")) flag = item.value;
			      if (s.equals("tag")) tag = Integer.parseInt(item.value);
			    }
			    for (String string : flags) 
			    {
			      if (string.equals("open")) fopen = true;
			      if (string.equals("delete")) fdelete = true;
			      if (string.equals("empty")) fempty = true;
			    }
			    
			    if (lname.isEmpty())
			    {
			      results.addToList (results.createError (
			          "Received an <!element> tag with no element name!"));
			      return;
			    }
			    //definition can be empty, that's no problem...

			    //if we want to delete the tag...
			    if (fdelete)
			    {
			      //sanity check
			      if (!elements.containsKey(lname))
			      {
			        results.addToList (results.createWarning (
			            "Received request to remove an undefined tag " + lname + "!"));
			        return;
			      }
			      removeElement (lname);
			      return;
			    }
			    
			    //parse tag definition
			    parser.simpleParse(definition);
			    ArrayList<sElementPart> tagcontents = new ArrayList<sElementPart>();
			    while (parser.hasNext())
			    {
			      chunk ch = parser.getNext();
			      if (ch.chk == chunkType.chunkError)
			        results.addToList (results.createError (ch.text));
			      else
			      {
			        //create a new element part
			        sElementPart part = new sElementPart();
			        part.text = ch.text;
			        part.istag = (ch.chk == chunkType.chunkTag) ? true : false;
			        tagcontents.add(part);
			      }
			    }
			    
			    //parse attribute list
			    ArrayList<String> attlist = new ArrayList<String>();
			    HashMap<String, String> attdefault = new HashMap<String, String>();
			    processParamList (att, attlist, attdefault);
			    
			    //and do the real work
			    addElement(lname, tagcontents, attlist, attdefault, fopen, fempty, tag, flag);
			  }
			  else if (name2.equals("!attlist"))
			  {
			    String name = "", att = "";
			    for ( Iterator it = params.iterator(); it.hasNext();)
			    {
			    	sParam item = (sParam)it.next();
			    	String s = item.name;
			    	if (s.equals("name")) name = item.value;
			    	if (s.equals("att")) att = item.value;
			    }
			    
			    if (name.isEmpty())
			    {
			      results.addToList (results.createError (
			          "Received an <!attlist> tag with no element name!"));
			      return;
			    }

			    //parse attribute list
			    ArrayList<String> attlist =  new ArrayList<String>();
			    HashMap<String, String> attdefault = new HashMap<String, String>();
			    processParamList (att, attlist, attdefault);
			    
			    //and do the real work
			    setAttList (name, attlist, attdefault);
			  }
			  else if (name2.equals("!entity"))
			  {
			    String name = "", value = "", desc;
			    boolean fpriv = false, fpub = false, fadd = false, fdel = false, frem = false;
			    for (sParam item : params) 
			    {
			      String s = item.name;
			      if (s.equals("name")) name = item.value;
			      if (s.equals("value")) value = item.value;
			      if (s.equals("desc")) desc = item.value;
			    }
			    for (String item : flags) 
			    {
			      if (item.equals( "private")) fpriv = true;
			      if (item.equals( "publish")) fpub = true;
			      if (item.equals( "delete")) fdel = true;
			      if (item.equals( "add")) fadd = true;
			      if (item.equals( "remove")) frem = true;
			    }
			    
			    if (name.isEmpty())
			    {
			      results.addToList (results.createError (
			          "Received an <!entity> tag with no variable name!"));
			      return;
			    }
			    
			    //fpub is IGNORED...
			    //fadd and frem is IGNORED...
			    if (!(fadd) && !(frem))
			    {
			      if (fdel)
			      {
			        entities.deleteEntity (name);
			        if (!fpriv) //do not announce PRIVATE entities
			          state.gotVariable (name, "", true);
			      }
			      else
			      {
			        //we now have a new variable...
			        entities.addEntity (name, value);
			        if (!fpriv) //do not announce PRIVATE entities
			          state.gotVariable (name, value, false);
			      }
			    }
			    else
			      results.addToList (results.createWarning (
			          "Ignored <!ENTITY> tag with ADD or REMOVE flag."));
			  }
			  else if (name2.equals("var"))
			  {
			    //this is very similar to the !entity handler above...
			    
			    String name = "", desc = "";
			    boolean fpriv = false, fpub = false, fadd = false, fdel = false, frem = false;
			    for ( sParam item : params) 
			    {
			      String s = item.name;
			      if (s.equals("name")) name = item.value;
			      if (s.equals("desc")) desc = item.value;
			    }
			    for ( String item : flags)
			    {  
			      if (item.equals("private")) fpriv = true;
			      if (item.equals("publish")) fpub = true;
			      if (item.equals("add")) fadd = true;
			      if (item.equals("delete")) fdel = true;
			      if (item.equals("remove")) frem = true;
			    }
			    
			    if (name.isEmpty())
			    {
			      results.addToList (results.createError (
			          "Received an <var> tag with no variable name!"));
			      return;
			    }
			    
			    //fpriv and fpub is IGNORED...
			    //fadd and fdel is IGNORED...
			    if (!(fadd) && !(fdel))
			      state.gotVAR (name);
			    else
			      results.addToList (results.createWarning ("Ignored <VAR> tag with ADD or REMOVE flag."));
			  }
			  else if (name2.equals("b"))
			    state.gotBOLD();
			  else if (name2.equals("i"))
			    state.gotITALIC();
			  else if (name2.equals("u"))
			    state.gotUNDERLINE();
			  else if (name2.equals("s"))
			    state.gotSTRIKEOUT();
			  else if (name2.equals("c"))
			  {
			    String fore = "", back = "";
			    for (sParam item : params) 
			    {
			      String s = item.name;
			      if (s.equals("fore")) fore = item.value;
			      if (s.equals("back")) back = item.value;
			    }
			    Color fg = state.fgColor();
			    Color bg = state.bgColor();
			    if (!fore.isEmpty())
			      fg = MXPColors.self().getColor(fore);
			    if (!back.isEmpty())
			      bg = MXPColors.self().getColor(back);
			    state.gotCOLOR (fg, bg);
			  }
			  else if (name2.equals("h"))
			    state.gotHIGH();
			  else if (name2.equals("font"))
			  {
			    String face = "", fore="", back="";
			    int size = 0;
			    for ( sParam item : params) 
			    {
			      String s = item.name;
			      if (s.equals("face")) face = item.value;
			      if (s.equals("size")) size = Integer.parseInt(item.value);
			      if (s.equals("color")) fore = item.value;
			      if (s.equals("back")) back = item.value;
			    }
			    if (face.isEmpty())
			      face = state.fontFace();
			    if (size == 0)
			      size = state.fontSize();
			    Color fg = state.fgColor();
			    Color bg = state.bgColor();
			    if (!fore.isEmpty())
			      fg = MXPColors.self().getColor(fore);
			    if (!back.isEmpty())
			      bg = MXPColors.self().getColor(back);
			    state.gotFONT (face, size, fg, bg);
			  }
			  else if (name2.equals("p"))
			    state.gotP();
			  else if (name2.equals("br"))
			    state.gotBR();
			  else if (name2.equals("nobr"))
			    state.gotNOBR();
			  else if (name2.equals("sbr"))
			    state.gotSBR();
			  else if (name2.equals("a"))
			  {
			    String href = "", hint = "", expire = "";
			    for ( sParam item : params) 
			    {
			      String s = item.name;
			      if (s.equals("href")) href = item.value;
			      if (s.equals("hint")) hint = item.value;
			      if (s.equals("expire")) expire = item.value;
			    }
			    state.gotA (href, hint, expire);
			  }
			  else if (name2.equals("send"))
			  {
			    String href = "", hint = "", expire = "";
			    boolean prompt = false;
			    for ( sParam item : params) 
			    {
			      String s = item.name;
			      if (s.equals("href")) href = item.value;
			      if (s.equals("hint")) hint = item.value;
			      if (s.equals("expire")) expire = item.value;
			    }
			    for ( String item : flags)
			    {  
			      if (item.equals("prompt")) prompt = true;
			    }
			    state.gotSEND (href, hint, prompt, expire);
			  }
			  else if (name2.equals("expire"))
			  {
			    String name = "";
			    for ( sParam item : params) 
			    {
			      String s = item.name;
			      if (s.equals("name")) name = item.value;
			    }
			    //name can be empty - all named links shall then expire
			    state.gotEXPIRE(name);
			  }
			  else if (name2.equals("version"))
			    state.gotVERSION();
			  else if (name2.equals("h1"))
			    state.gotHtag (1);
			  else if (name2.equals("h2"))
			    state.gotHtag (2);
			  else if (name2.equals("h3"))
			    state.gotHtag (3);
			  else if (name2.equals("h4"))
			    state.gotHtag (4);
			  else if (name2.equals("h5"))
			    state.gotHtag (5);
			  else if (name2.equals("h6"))
			    state.gotHtag (6);
			  else if (name2.equals("hr"))
			    state.gotHR();
			  else if (name2.equals("small"))
			    state.gotSMALL();
			  else if (name2.equals("tt"))
			    state.gotTT();
			  else if (name2.equals("sound"))
			  {
			    String fname = "", t = "", u = "";
			    int v = 0, l = 0, p = 0;  //shall be overridden by defaults...
			    for ( sParam item : params) 
			    {
			      String s = item.name;
			      if (s.equals("fname")) fname = item.value;
			      if (s.equals("t")) t = item.value;
			      if (s.equals("u")) u = item.value;
			      if (s.equals("v")) v = Integer.parseInt(item.value);
			      if (s.equals("l")) l = Integer.parseInt(item.value);
			      if (s.equals("p")) p = Integer.parseInt(item.value);
			    }
			    if (fname.isEmpty())
			    {
			      results.addToList (results.createError ("Received SOUND tag with no file name!"));
			      return;
			    }
			    if ((v < 0) ||  (v > 100))
			    {
			      results.addToList (results.createWarning ("Ignoring incorrect V param for SOUND tag."));
			      v = 100;  //set default value
			    }
			    if ((l < -1) || (l > 100) || (l == 0))
			    {
			      results.addToList (results.createWarning ("Ignoring incorrect L param for SOUND tag."));
			      l = 1;  //set default value
			    }
			    if ((p < 0) ||  (p > 100))
			    {
			      results.addToList (results.createWarning ("Ignoring incorrect P param for SOUND tag."));
			      p = 50;  //set default value
			    }
			    state.gotSOUND (fname, v, l, p, t, u);
			  }
			  else if (name2.equals("music"))
			  {
			    String fname = "", t = "", u = "";
			    int v = 0, l = 0, c = 0;  //shall be overridden by defaults...
			    for ( sParam item : params) 
			    {
			      String s = item.name;
			      if (s.equals("fname")) fname = item.value;
			      if (s.equals("t")) t = item.value;
			      if (s.equals("u")) u = item.value;
			      if (s.equals("v")) v = Integer.parseInt(item.value);
			      if (s.equals("l")) l = Integer.parseInt(item.value);
			      if (s.equals("c")) c = Integer.parseInt(item.value);
			    }
			    if (fname.isEmpty())
			    {
			      results.addToList (results.createError ("Received MUSIC tag with no file name!"));
			      return;
			    }
			    if ((v < 0) ||  (v > 100))
			    {
			      results.addToList (results.createWarning ("Ignoring incorrect V param for MUSIC tag."));
			      v = 100;  //set default value
			    }
			    if ((l < -1) || (l > 100) || (l == 0))
			    {
			      results.addToList (results.createWarning ("Ignoring incorrect L param for MUSIC tag."));
			      l = 1;  //set default value
			    }
			    if ((c != 0) && (c != 1))
			    {
			      results.addToList (results.createWarning ("Ignoring incorrect C param for MUSIC tag."));
			      c = 1;  //set default value
			    }
			    state.gotMUSIC (fname, v, l, (c!=0), t, u);
			  }
			  else if (name2.equals("gauge"))
			  {
			    String entity = "", max = "", caption = "", color = "";
			    for ( sParam item : params) 
			    {
			      String s = item.name;
			      if (s.equals("entity")) entity = item.value;
			      if (s.equals("max")) max = item.value;
			      if (s.equals("caption")) caption = item.value;
			      if (s.equals("color")) color = item.value;
			    }
			    if (entity.isEmpty())
			    {
			      results.addToList (results.createError ("Received GAUGE with no entity name!"));
			      return;
			    }
			    Color c;
			    if (color.isEmpty()) color = "white";
			    c = MXPColors.self().getColor(color);
			    state.gotGAUGE (entity, max, caption, c);
			  }
			  else if (name2.equals("stat"))
			  {
			    String entity = "", max = "", caption = "";
			    for ( sParam item : params) 
			    {
			      String s = item.name;
			      if (s.equals("entity")) entity = item.value;
			      if (s.equals("max")) max = item.value;
			      if (s.equals("caption")) caption = item.value;
			    }
			    if (entity.isEmpty())
			    {
			      results.addToList (results.createError ("Received STAT with no entity name!"));
			      return;
			    }
			    state.gotSTAT (entity, max, caption);
			  }
			  else if (name2.equals("frame"))
			  {
			    String name = "", action = "", title = "", align = "";
			    int left = 0, top = 0, width = 0, height = 0;
			    boolean finternal = false, fscroll = false, ffloat = false;
			    for ( sParam item : params) 
			    {
			      String s = item.name;
			      if (s.equals("name")) name = item.value;
			      if (s.equals("action")) action = item.value;
			      if (s.equals("title")) title = item.value;
			      if (s.equals("align")) align = item.value;
			      if (s.equals("left")) left = state.computeCoord (item.value, true,false);
			      if (s.equals("top")) top = state.computeCoord (item.value, false,false);
			      if (s.equals("width")) width = state.computeCoord (item.value, true,false);
			      if (s.equals("height")) height = state.computeCoord (item.value, false,false);
			    }
			    for ( String item : flags)
			    {  
			      if (item.equals("internal")) finternal = true;
			      if (item.equals("scrolling")) fscroll = true;
			      if (item.equals("floating")) ffloat = true;
			    }
			    if (name.isEmpty())
			    {
			      results.addToList (results.createError ("Received FRAME tag with no frame name!"));
			      return;
			    }
			    state.gotFRAME (name, action, title, finternal, align, left, top, width, height,
			        fscroll, ffloat);
			  }
			  else if (name2.equals("dest"))
			  {
			    String name = "";
			    int x = 0, y = 0;
			    boolean feol = false, feof = false;
			    for ( sParam item : params) 
			    {
			      String s = item.name;
			      if (s.equals("name")) name = item.value;
			      if (s.equals("x")) x = Integer.parseInt(item.value);
			      if (s.equals("y")) y = Integer.parseInt(item.value);
			    }
			    for ( String item : flags)
			    {  
			      if (item.equals("eol")) feol = true;
			      if (item.equals("eof")) feof = true;
			    }
			    if (name.isEmpty())
			    {
			      results.addToList (results.createError ("Received DEST tag with no frame name!"));
			      return;
			    }
			    state.gotDEST (name, x, y, feol, feof);
			  }
			  else if (name2.equals("relocate"))
			  {
			    String name = "";
			    int port = 0;
			    for ( sParam item : params) 
			    {
			      String s = item.name;
			      if (s.equals("name")) name = item.value;
			      if (s.equals("port")) port = Integer.parseInt(item.value);
			    }
			    if (name.isEmpty())
			    {
			      results.addToList (results.createError ("Received RELOCATE tag with no server name!"));
			      return;
			    }
			    if (port == 0)
			    {
			      results.addToList (results.createError ("Received RELOCATE tag with no server port!"));
			      return;
			    }
			    state.gotRELOCATE (name, port);
			  }
			  else if (name2.equals("user"))
			    state.gotUSER();
			  else if (name2.equals("password"))
			    state.gotPASSWORD();
			  else if (name2.equals("image"))
			  {
			    String name="", url="", t="", align="";
			    int h = 0, w = 0, hspace = 0, vspace = 0;
			    boolean fismap = false;
			    for ( sParam item : params) 
			    {
			      String s = item.name;
			      if (s.equals("fname")) name = item.value;
			      if (s.equals("url")) url = item.value;
			      if (s.equals("t")) t = item.value;
			      if (s.equals("align")) align = item.value;
			      if (s.equals("h")) h = state.computeCoord (item.value, true, true);
			      if (s.equals("w")) w = state.computeCoord (item.value, false, true);
			      if (s.equals("hspace")) hspace = Integer.parseInt(item.value);;
			      if (s.equals("vspace")) vspace = Integer.parseInt(item.value);;
			    }
			    for ( String item : flags)
			    {  
			      if (item.equals("ismap")) fismap = true;
			    }
			    if (name.isEmpty())
			    {
			      results.addToList (results.createError ("Received IMAGE tag with no image name!"));
			      return;
			    }
			    state.gotIMAGE (name, url, t, h, w, hspace, vspace, align, fismap);
			  }
			  else if (name2.equals("filter"))
			  {
			/*
			    String src, dest, name;
			    for (it = params.begin(); it != params.end(); ++it)
			    {
			      String s = (*it).name;
			      if (s.equals("src") src = (*it).value;
			      if (s.equals("dest") dest = (*it).value;
			      if (s.equals("name") name = (*it).value;
			    }
			    state.gotFILTER (src, dest, name);
			*/
			    results.addToList (results.createWarning ("Ignoring unsupported FILTER tag."));
			  }
			}

	  private void setAttList(String name, ArrayList<String> attlist,
			HashMap<String, String> attdefault) 
	  {
		  //sanity check
		  if (!elements.containsKey(name))
		  {
		    results.addToList (results.createWarning ("Received attribute list for undefined tag " +
		        name + "!"));
		    return;
		  }
		  sElement e = elements.get(name);
		  e.attlist.clear();
		  e.attdefault.clear();
		  e.attlist = attlist;
		  e.attdefault = attdefault;		 
	  }

	private void addElement(String name, List<sElementPart> contents,
			ArrayList<String> attlist, HashMap<String, String> attdefault,
			boolean open, boolean empty, int tag, String flag) 
	  {
		  //sanity checks
		  if (elementDefined (name))
		  {
		    results.addToList (results.createError ("Multiple definition of element " + name + "!"));
		    return;
		  }

		  sElement e = new sElement();
		  e.open = open;
		  e.empty = empty;
		  if ((tag >= 20) && (tag <= 99))
		  {
		    e.tag = tag;
		    if (lineTags.containsKey(tag))
		      results.addToList (results.createError ("Element " + name +
		          " uses an already assigned line tag!"));
		    lineTags.put(tag, name);
		  }
		  else
		    e.tag = 0;
		  e.flag = flag;

		  //assign element contents, generating the list of closing tags
		  e.element.clear();
		  for (sElementPart ep : contents) 
		  {
		    if (ep.istag)
		    {
		      String tag1 = ep.text.split(" ")[0].toLowerCase();
		      if (elementDefined (tag1))
		      {
		        if (open && !(openElement (tag1)))
		        {
		          ep = null;
		          results.addToList (results.createError ("Definition of open " + name +
		              " tag contains secure tag " + tag1 + "!"));
		        }
		        else if (empty && !(emptyElement (tag1)))
		        {
		        	ep = null;
		          results.addToList (results.createError ("Definition of empty " + name +
		              " tag contains non-empty tag " + tag1 + "!"));
		        }
		        else
		        {
		          e.element.add (ep);
		          if (!emptyElement(tag1)) e.closingseq.add(0,tag1);
		        }
		      }
		      else
		      {
		        //element doesn't exist yet - we must believe that it's correct
		        e.element.add(ep);
		        if (!empty) e.closingseq.add(0,tag1);
		        results.addToList (results.createWarning ("Definition of element " + name +
		            " contains undefined element " + tag1 + "!"));
		      }
		    }
		    else
		      e.element.add(ep);
		  }

		  //assign the element definition
		  elements.put(name,e);

		  //set attribute list
		  setAttList (name, attlist, attdefault);		  
	  }

	private void processParamList(String params, ArrayList<String> attlist,
			HashMap<String, String> attdefault) 
	  {
		  //this is similar to the parser in gotTag(), but it's a bit simpler...
		  if (params==null) return;
		  String name = "", value = "";
		  char quote = 0;
		  paramParserState state = paramParserState.parNone;
		  for ( int i =0; i < params.length(); i++)
		  {
		    char ch = params.charAt(i);

		    //process character
		    switch (state) {
		      case parNone: {
		        if (ch != ' ')
		        {
		          state = paramParserState.parName;
		          name += ch;
		        }
		        break;
		      }
		      case parName: {
		        if (ch == '=')
		          state = paramParserState.parValue;
		        else if (ch == ' ')
		        {
		          //new parameter, no default value
		          attlist.add(name.toLowerCase());
		          name = "";
		          state = paramParserState.parNone;
		        }
		        else
		          name += ch;
		        break;
		      }
		      case parValue: {
		        if (ch == ' ')
		        {
		          //new parameter, with default value
		          attlist.add(name.toLowerCase());
		          attdefault.put(name, value);
		          name = "";
		          value = "";
		          state = paramParserState.parNone;
		        }
		        else if (value.isEmpty() && ((ch == '\'') || (ch == '"')))
		        {
		          state = paramParserState.parQuotedValue;
		          quote = ch;
		        }
		        else
		          value += ch;
		        break;
		      }
		      case parQuotedValue: {
		        if (ch == quote)
		        {
		          //new parameter, with default value
		          attlist.add(name.toLowerCase());
		          attdefault.put(name, value);
		          name = "";
		          value = "";
		          state = paramParserState.parNone;
		        }
		        else
		          value += ch;
		        break;
		      }
		    };
		  }

		  //last parameter...
		  switch (state) {
		    case parName: {
		      //new parameter, no default value
		      attlist.add(name.toLowerCase());
		    }
		    break;
		    case parValue: {
		      //new parameter, with default value
		      attlist.add(name.toLowerCase());
		      attdefault.put(name, value);
		      break;
		    }
		    case parQuotedValue:
		      results.addToList (results.createWarning (
		          "Received tag definition with unfinished quoted default parameter value!"));
		      //new parameter, with default value (unfinished, but hey...)
		      attlist.add(name.toLowerCase());
		      attdefault.put(name, value);
		    break;
		  };
		  
		  //everything done...
		  
	  }

	private void processCustomTag(String name, List<sParam> params) throws Exception
	  {
	    //generate a mapping with all parameter values
	    paramexpander.reset(false);
	    for (sParam param : params) 
	    {
		      //assign parameter value... default values and stuff were already expanded
		      paramexpander.addEntity (param.name, "'" + param.value + "'");			
		}	    
	    //process tag contents one by one
	    for (sElementPart sep : elements.get(name).element) 
	    {
	      String part = sep.text;
	      
	      //expand tag parameters first
	      part = paramexpander.expandEntities (part, true);
	      
	      //parameters are expanded, process this part
	      if (sep.istag)
	        //this part is another tag - expand it recursively
	        gotTag(part);
	      else
	        //this part is regular text
	        state.gotText(part,true);
	    }

	    if (!elements.get(name).flag.isEmpty())
	      state.gotFlag (true, elements.get(name).flag);
	  }

	void gotLineTag (int number) throws Exception
	{
	  if ((number < 20) || (number > 99))
	  {
	    lastLineTag = 0;
	    return;
	  }
	  if (!lineTags.containsKey(number))
	  {
	    lastLineTag = 0;
	    return;
	  }
	  String tag = lineTags.get(number);
	  lastLineTag = number;
	  //behave as if we've just gotten the appropriate tag
	  gotTag(tag);
	}

	public void gotNewLine ()
	{
	  if ((lastLineTag < 20) || (lastLineTag > 99))
	  {
	    lastLineTag = 0;
	    return;
	  }
	  if (!lineTags.containsKey(lastLineTag))
	  {
	    lastLineTag = 0;
	    return;
	  }
	  String tag = lineTags.get(lastLineTag);
	  lastLineTag = 0;
	  if (emptyElement (tag))
	    //no closing tag needed
	    return;
	  //okay, send out the appropriate closing tag
	  handleClosingTag (tag);
	}
	
}
