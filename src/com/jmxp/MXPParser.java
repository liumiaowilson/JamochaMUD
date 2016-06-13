package com.jmxp;

import java.util.ArrayList;

import com.jmxp.MXPState.mxpMode;
import com.jmxp.structures.chunk;

public class MXPParser 
{
	public enum parserState {
	  pText,
	  pAnsiSeq,
	  pTag,
	  pComment,
	  pQuotedParam
	};

	private parserState pstate;
	private String str = "";
	private ArrayList<chunk> chunks = new ArrayList<chunk>();
	private char quoteChar;
	private boolean wasBackslashR;
	private MXPState state;
	private ElementManager elements;
	private ResultHandler results;
	
	
	/** constructor */
	public MXPParser (MXPState st, ElementManager elm, ResultHandler res )
	{
		  state = st;
		  elements = elm;
		  results = res;
		  
		  pstate = parserState.pText;
		  wasBackslashR = false;
		
	}

	public void simpleParse(String text) 
	{
		  if (text.isEmpty())
			    return;
		  chunk ch = new chunk();
		  pstate = parserState.pText;
		  str = "";
		  for ( int i =0; i < text.length(); i++ ) 
		  {			
			    char c = text.charAt(i);
			    switch (pstate) 
			    {
			      case pText: {
			        if (c == '<')
			        {
			          //end of text - got start of tag
			          if (!str.isEmpty())
			          {
			            ch.chk = chunk.chunkType.chunkText;
			            ch.text = str;
			            chunks.add(ch);
			            ch = new chunk();
			            str = "";
			          }
			          pstate = parserState.pTag;
			        }
			        else
			          str += c;  //add new character to the text... 
			        break;
			      }
			      case pTag: {
			        if (c == '>')
			        {
			          ch.chk = chunk.chunkType.chunkTag;
			          ch.text = str;
			          chunks.add(ch);
			          ch = new chunk();
			          str = "";
			          pstate = parserState.pText;
			        }
			        else
			        if ((c == '"') || (c == '\''))
			        {
			          pstate = parserState.pQuotedParam;
			          quoteChar = c;
			          str += c;
			        }
			        else
			          str += c;
			        break;
			      }
			      case pQuotedParam: {
			        if (c == quoteChar)
			        {
			          //quoted parameter ends... this simple approach will work correctly for correct
			          //tags, it may treat incorrect quotes as correct, but element manager will take care
			          //of that
			          pstate = parserState.pTag;
			          str += c;
			        }
			        else
			          str += c;
			        break;
			      }
			    };
			  }
			  //unfinished things...
			  if (pstate == parserState.pText)
			  {
			    ch.chk = chunk.chunkType.chunkText;
			    ch.text = str;
			    chunks.add(ch);
			    ch = new chunk();
			  }
			  if ((pstate == parserState.pTag) || (pstate == parserState.pQuotedParam))
			  {
			    ch.chk = chunk.chunkType.chunkError;
			    ch.text = "Tag definition contains unfinished tag <" + str;
			    chunks.add(ch);
			    ch = new chunk();
			  }
			  str = "";
		
	}

	public boolean hasNext() 
	{
		  return chunks.isEmpty() ? false : true;
	}

	public chunk getNext() 
	{
		  if (!hasNext())
		  {
		    chunk nochunk = new chunk();
		    nochunk.chk = chunk.chunkType.chunkNone;
		    return nochunk;
		  }
		  chunk ch = chunks.get(0);
		  chunks.remove(0);
		  return ch;
	  }

	public void parse(String text) throws Exception
	{
		  //WARNING: examine this function only at your own risk!
		  //it is advised to have a look at the simpleParse() function first - it's similar
		  //to this one, but much simpler...
		  if (text.isEmpty())
		    return;
		  for (int i = 0; i < text.length(); i++)
		  {
		    char c = text.charAt(i);
		    
		// Looks like number of brain-dead servers that send out \n\r is bigger than the
		// number of servers that send out \r alone - the latter maybe don't exist at
		// all. Hence, with this commented out, we can't handle the \r-only ones,
		// but \n\r works.
		/*
		    //handle \r not followed by \n - treated as a newline
		    if (wasBackslashR && (c != '\n'))
		    {
		      //"str" now certainly is empty, so we needn't care about that
		      //report new-line
		      elements->gotNewLine();
		      state->gotNewLine();
		    }
		*/
		    wasBackslashR = false;
		    
		    //we need current mode - parsing in LOCKED mode is limited
		    //mode is retrieved in every iteration to ensure that it's always up-to-date
		    mxpMode mode = state.getMXPMode();
		    switch (pstate) {
		      case pText: {
		        //tags not recognized in LOCKED mode...
		        if ((c == '\u001B') || ((mode != mxpMode.lockedMode) && 
		        		(c == '<')) || (c == '\n') || (c == '\r'))
		        {
		          //end of text - got newline / ANSI seq / start of tag
		          if (!str.isEmpty())
		          {
		            state.gotText(str,true);
		            str = "";
		          }
		          if (c == '\u001B')
		            pstate = parserState.pAnsiSeq;
		          if ((c == '<') && (mode != mxpMode.lockedMode))
		            pstate = parserState.pTag;
		          if (c == '\n')
		          {
		            //report new-line
		            elements.gotNewLine();
		            state.gotNewLine();
		          }
		          if (c == '\r')
		            wasBackslashR = true;
		        }
		        else
		          str += c;  //add new character to the text... 
		        break;
		      }
		      case pAnsiSeq: {
		        if ((c == '\u001B') || (c == '\n') || (c == '\r'))
		        {
		          //the same as in pTag section...
		          results.addToList (results.createError ("Received unfinished ANSI sequence!"));
		          str = "";
		          if (c == '\u001B')
		            pstate = parserState.pAnsiSeq;
		          if (c == '\n')
		          {
		            //report new-line
		            elements.gotNewLine();
		            state.gotNewLine();
		            pstate = parserState.pText;
		          }
		          if (c == '\r')
		          {
		            pstate = parserState.pText;
		            wasBackslashR = true;
		          }
		        }
		        else
		        if (((c >= 'a') && (c <= 'z')) || ((c >= 'A') && (c <= 'Z')))
		        {
		          //ANSI sequence ends...
		          if (c == 'z')  //line tag
		          {
		            if (str.isEmpty())
		            {
		              //invalid sequence
		              str = "\u001Bz";
		            }
		            else
		            {
		              //process this sequence
		              int len = str.length();
		              int num = 0;
		              for (int j = 1; j < len; j++)  //str[0] is '[', which is SKIPPED
		              {
		                char cc = str.charAt(j);
		                if (cc == ';')  //this shouldn't happen, but some MUD might want to use it...
		                {
		                  if ((num >= 0) && (num <= 99))  //ensure that number lies in correct range
		                  {
		                    state.gotLineTag (num);
		                    elements.gotLineTag (num);
		                  }
		                  else
		                    results.addToList (results.createError ("Received invalid line tag!"));
		                  num = 0;
		                }
		                else
		                  num = num * 10 + (cc - 48);  //48 is the code of '0'
		              }
		              //report last line tag (and usually the only one)
		              if ((num >= 0) && (num <= 99))  //ensure that number lies in correct range
		              {
		                state.gotLineTag (num);
		                elements.gotLineTag (num);
		              }
		              else
		                results.addToList (results.createError ("Received invalid line tag!"));
		              str = "";
		            }
		          }
		          else  //something else
		          {
		            //'\u001B' and c are not in the string - add them there
		            str = '\u001B' + str + c;
		          } 
		          pstate = parserState.pText;
		        }
		        else
		        if (c == '[')  //this one is valid, but only at the beginning
		        {
		          if (str.isEmpty())
		            str += c;
		          else
		          {
		            //'[' in the middle of ANSI seq => not an ANSI seq...
		            pstate = parserState.pText;
		            str = '\u001B' + str + c;
		          }
		        }
		        else
		        if ((c == ';') || ((c >= '0') && (c <= '9')))  //correct char, unless str is empty
		          if (!str.isEmpty())
		            str += c;  //here we go...
		          else
		          {
		            //ANSI seq must start with [ - therefore this is not an ANSI sequence after all
		            pstate = parserState.pText;
		            str += '\u001B';
		            str += c;
		          }
		        else
		        //incorrect character...
		        {
		          str = '\u001B' + str + c;
		          pstate = parserState.pText;
		        }
		        break;
		      }
		      case pTag: {
		        if (c == '>')
		        {
		          elements.gotTag (str);
		          str = "";
		          pstate = parserState.pText;
		        }
		        else
		        if ((c == '"') || (c == '\''))
		        {
		          pstate = parserState.pQuotedParam;
		          quoteChar = c;
		          str += c;
		        }
		        else if ((c == '\u001B') || (c == '\n') || (c == '\r'))
		        {
		          //handle incorrectly terminated tag and continue parsing...
		          results.addToList (results.createError ("Received unfinished tag <" + str));
		          str = "";
		          if (c == '\u001B')
		            pstate = parserState.pAnsiSeq;
		          if (c == '\n')
		          {
		            //report new-line
		            elements.gotNewLine();
		            state.gotNewLine();
		            pstate = parserState.pText;
		          }
		          if (c == '\r')
		          {
		            pstate = parserState.pText;
		            wasBackslashR = true;
		          }
		        }
		        else if (str.equals("!--"))  //comment
		        {
		          str += c;
		          pstate = parserState.pComment;
		        }
		        else
		          str += c;
		        break;
		      }
		      case pComment: {
		        if (c == '>')
		        {
		          int l = str.length();
		          if ((str.charAt(l-2) == '-') && (str.charAt(l-1) == '-')) //okay, comment ends
		          {
		            str = "";
		            pstate = parserState.pText;
		          }
		          else
		            str += c;
		        }
		        else if ((c == '\u001B') || (c == '\n') || (c == '\r'))
		        {
		          //handle incorrectly terminated comment and continue parsing...
		          results.addToList (results.createError ("Received an unfinished comment!"));
		          str = "";
		          if (c == '\u001B')
		            pstate = parserState.pAnsiSeq;
		          if (c == '\n')
		          {
		            //report new-line
		            elements.gotNewLine();
		            state.gotNewLine();
		            pstate = parserState.pText;
		          }
		          if (c == '\r')
		          {
		            pstate = parserState.pText;
		            wasBackslashR = true;
		          }
		        }
		        else
		          str += c;
		        break;
		      }
		      case pQuotedParam: {
		        if (c == quoteChar)
		        {
		          //quoted parameter ends... this simple approach will work correctly for correct
		          //tags, it may treat incorrect quotes as correct, but element manager will take care
		          //of that
		          pstate = parserState.pTag;
		          str += c;
		        }
		        else
		        if ((c == '\u001B') || (c == '\n') || (c == '\r'))
		        {
		          //the same as in pTag section...
		          results.addToList (results.createError ("Received unfinished tag <" + str));
		          str = "";
		          if (c == '\u001B')
		            pstate = parserState.pAnsiSeq;
		          if (c == '\n')
		          {
		            //report new-line
		            elements.gotNewLine();
		            state.gotNewLine();
		            pstate = parserState.pText;
		          }
		          if (c == '\r')
		          {
		            pstate = parserState.pText;
		            wasBackslashR = true;
		          }
		        }
		        else
		          str += c;
		        break;
		      }
		    };
		  }
		  //report remaining text, if any (needed to improve speed of text displaying and to handle
		  //prompts correctly)
		  if ((pstate == parserState.pText) && (!str.isEmpty()))
		  {
		    state.gotText(str,true);
		    str = "";
		  }
	}		
}
