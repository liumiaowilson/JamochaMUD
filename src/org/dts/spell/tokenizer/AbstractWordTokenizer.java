/*
 * Created on 29/03/2005
 *
 */
package org.dts.spell.tokenizer;

import org.dts.spell.finder.Word;

/**
 * @author DreamTangerine
 *
 */
public abstract class AbstractWordTokenizer implements WordTokenizer
{
  public abstract Word currentWord(int index) ;  

  /**
   * Implementacibad_char_replacedn por defecto. No es muy bad_char_replacedptima pero es genbad_char_replacedrica, se basa en
   * currentWord. Es casi obligado reescribirla.
   */
  public Word nextWord(int index)
  {
    int length = getCharSequence().length() ;
    Word orgWord = currentWord(index) ;
    Word current = orgWord ;
    Word result = null ;
    
    while (index < length && null == current)
      current = currentWord(++index) ;
    
    if (index < length && null != current)
    {
      if (current.equals(orgWord))
      {
        index = orgWord.getEnd() ;
        
        while (index < length && null == result)
        {
          if (null != current && !current.equals(orgWord))
            result = current ;
          else
            current = currentWord(++index) ;
        }
      }
      else
        result = current ;        
    }
    
    return result ;
  }

  /**
   * Implementacibad_char_replacedn por defecto. No es muy bad_char_replacedptima pero es genbad_char_replacedrica, se basa en
   * currentWord.
   */
  public Word previousWord(int index)
  {
    Word orgWord = currentWord(index) ;
    Word current = orgWord ;
    Word result = null ;    
    
    while (index > 0 && null == current)
      current = currentWord(--index) ;
    
    if (index > 0 && null != current)
    {
      if (current.equals(orgWord))
      {
        index = orgWord.getStart() ;
        
        while (index > 0 && null == result)
        {
          if (null != current && !current.equals(orgWord))
            result = current ;
          else
            current = currentWord(--index) ;
        }
      }
      else
        result = current ;        
    }
    
    return result ;
  }
  
  
  public CharSequence getCharSequence()
  {
    return charSequence ;
  }

  public void setCharSequence(CharSequence sequence)
  {
    if (sequence != charSequence)
    {
      charSequence = sequence ;
      
      updateCharSequence(0, 
          null != sequence ? sequence.length() : 0, 
          CHANGE_SEQUENCE) ;
    }
  }
  
  protected boolean isStartOfSentence(CharSequence sequence, int start)
  {
    boolean found = false ;
    --start ;
    
    while (start >= 0 && !found)
    {
      if (Character.isWhitespace(sequence.charAt(start)))
        --start ;
      else
        found = true ;
    }
    
    return start < 0 || sequence.charAt(start) == '.' ;
  }

  private CharSequence charSequence = "" ;
}
