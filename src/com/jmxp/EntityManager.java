package com.jmxp;

import java.util.HashMap;

public class EntityManager 
{
	/** empty string, to speed up some things a little bit */
	private String empty_string;
	/** partial entity */
	private String partent;
	/** are we in an entity? */
	private boolean inEntity;
	private HashMap<String, String> entities = new HashMap<String, String>();
	
	private String ENTITY_NAMES[] = { 
			  "Aacute",
			  "aacute",
			  "Acirc",
			  "acirc",
			  "acute",
			  "AElig",
			  "aelig",
			  "Agrave",
			  "agrave",
			  "amp",
			  "apos",
			  "Aring",
			  "aring",
			  "Atilde",
			  "atilde",
			  "Auml",
			  "auml",
			  "brvbar",
			  "Ccedil",
			  "ccedil",
			  "cedil",
			  "cent",
			  "copy",
			  "curren",
			  "deg",
			  "divide",
			  "Eacute",
			  "eacute",
			  "Ecirc",
			  "ecirc",
			  "Egrave",
			  "egrave",
			  "ETH",
			  "eth",
			  "Euml",
			  "euml",
			  "frac12",
			  "frac14",
			  "frac34",
			  "gt",
			  "Iacute",
			  "iacute",
			  "Icirc",
			  "icirc",
			  "iexcl",
			  "Igrave",
			  "igrave",
			  "iquest",
			  "Iuml",
			  "iuml",
			  "laquo",
			  "lt",
			  "macr",
			  "micro",
			  "middot",
			  "nbsp",
			  "not",
			  "Ntilde",
			  "ntilde",
			  "Oacute",
			  "oacute",
			  "Ocirc",
			  "ocirc",
			  "Ograve",
			  "ograve",
			  "ordf",
			  "ordm",
			  "Oslash",
			  "oslash",
			  "Otilde",
			  "otilde",
			  "Ouml",
			  "ouml",
			  "para",
			  "plusmn",
			  "pound",
			  "quot",
			  "raquo",
			  "reg",
			  "sect",
			  "shy",
			  "sup1",
			  "sup2",
			  "sup3",
			  "szlig",
			  "THORN",
			  "thorn",
			  "times",
			  "Uacute",
			  "uacute",
			  "Ucirc",
			  "ucirc",
			  "Ugrave",
			  "ugrave",
			  "uml",
			  "Uuml",
			  "uuml",
			  "Yacute",
			  "yacute",
			  "yen",
			};

		private	int ENTITY_DEF[] = {
			  193,
			  225,
			  194,
			  226,
			  180,
			  198,
			  230,
			  192,
			  224,
			  38,
			  39,
			  197,
			  229,
			  195,
			  227,
			  196,
			  228,
			  166,
			  199,
			  231,
			  184,
			  162,
			  169,
			  164,
			  176,
			  247,
			  201,
			  233,
			  202,
			  234,
			  200,
			  232,
			  208,
			  240,
			  203,
			  235,
			  189,
			  188,
			  190,
			  62,
			  205,
			  237,
			  206,
			  238,
			  161,
			  204,
			  236,
			  191,
			  207,
			  239,
			  171,
			  60,
			  175,
			  181,
			  183,
			  160,
			  172,
			  209,
			  241,
			  211,
			  243,
			  212,
			  244,
			  210,
			  242,
			  170,
			  186,
			  216,
			  248,
			  213,
			  245,
			  214,
			  246,
			  182,
			  177,
			  163,
			  34,
			  187,
			  174,
			  167,
			  173,
			  185,
			  178,
			  179,
			  223,
			  222,
			  254,
			  215,
			  218,
			  250,
			  219,
			  251,
			  217,
			  249,
			  168,
			  220,
			  252,
			  221,
			  253,
			  165,
			};
	
	private static final int NUM_MXP_ENTITIES = 100;

	
	public EntityManager(boolean noStdEntities)
	{
		reset(noStdEntities);
	}

	//~cEntityManager ();

	/** 
	* add or update entity 
	*/
	public void addEntity (String name, String value)
	{
		if (name.equals("")) return;		
		//add or modify the entity
		entities.put(name, value);		
	}
	
	/** 
	 * delete entity 
	 */
	public void deleteEntity (String name)
	{
		entities.remove(name);	
	}
	
	public String entity(String name)
	{
		String result = entities.get(name);
		if ( result == null )
		{
			result = "";
		}
		return result;
	}
	
	public boolean exists(String name) 
	{ 
		return entities.containsKey(name); 
	};
	
	/** 
	 * expand entities in a string 
	 */
	public String expandEntities(String s, boolean finished)
	{
		  String s1 = "";
		  if (!partent.equals(""))  //some unfinished entity is waiting...
		  {
			  inEntity = true;
		  }

		  for (int i = 0; i < s.length(); i++ )
		  {
			  char ch = s.charAt(i);
		    if (inEntity)
		    {
		      
		      if (ch == ';')  //end of entity
		      {
		        inEntity = false;
		        if (partent.equals("")) //received &;
		        {
		          s1 += "&;";
		        }
		        else
		        if (partent.charAt(0) == '_')  //invalid entity name - IGNORED
		        {
		          partent = "";
		        }
		        else
		        if (partent.charAt(0) == '#')  //&#nnn; entity
		        {
		          //compute number
		          int n = 0;
		          //starting from second character
		          for (int j = 1; j < partent.length(); j++)
		          {
		            int x = partent.charAt(j) - 48;
		            if ((x < 0) || (x > 9)) //WRONG
		            {
		              n = 0;
		              break;
		            }
		            n = n * 10 + x;
		            if (n > 255)  //number too big!
		            {
		              n = 0;
		              break;
		            }
		          }
		          //verify number, IGNORE entity if it's wrong
		          if ((n >= 32) && (n <= 255))
		          {
		            s1 += (char) n;
		          }
		          partent = "";
		        }
		        else
		        {
		          //now we have correct entity name, let's expand it, if possible :)
		          if (entities.containsKey(partent))
		            s1 += entities.get(partent);
		          else
		            //keep the same string if the entity doesn't exist...
		            s1 += "&" + partent + ";";
		          partent = "";
		        }
		      }
		      else if (ch == '&')
		      //unterminated entity, new entity may start here
		      {
		        s1 += "&" + partent;
		        partent = "";
		        //isEntity remains set
		      }
		      else if ((partent.equals("") && correct1(ch)) || 
		    		  ((!partent.equals("")) && correctN(ch)))
		      {
		        partent += ch;
		      }
		      //this wasn't an entity after all
		      else
		      {
		        inEntity = false;
		        s1 += "&" + partent + ch;
		        partent = "";
		      }
		    }
		    else
		    {
		      if (ch == '&')
		        inEntity = true;
		      else
		        //copy without change
		        s1 += ch;
		    }
		  }
		  //string ends in an unterminated entity, but only if the string is finished
		  if (inEntity && finished)
		  {
		    s1 += "&" + partent;
		    partent = "";
		    inEntity = false;
		  }

		  //return the resulting string
		  return s1;
		
	}
	
	public boolean needMoreText()
	{
		return !partent.equals("");		
	}
	  
	void reset(boolean noStdEntities)
	{
		  partent = "";
		  entities.clear();
		  inEntity = false;
		  
		  if (noStdEntities)
		  {
		    return;
		  }
		  
		  
		  //restore standard HTML entities
		  for (int i = 0; i < NUM_MXP_ENTITIES; i++)
		  {
			  String s = "";
			  s += (char)ENTITY_DEF[i];
			  entities.put(ENTITY_NAMES[i], s);
		  }
	}
	
	//can this be the first letter of an entity?
	private boolean correct1 (char l)
	{
	  return (((l >= 'a') && (l <= 'z')) || ((l >= 'A') && (l <= 'Z')) || (l == '#'));
	}

	//can this be a letter of entity?
	private boolean correctN (char l)
	{
	  return (((l >= 'a') && (l <= 'z')) || ((l >= 'A') && (l <= 'Z')) || (l == '_') ||
	      ((l >= '0') && (l <= '9')));
	}
	
}
