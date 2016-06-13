/*
 * Created on 04/01/2005
 *
 */
package org.dts.spell ;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;

import org.dts.spell.dictionary.SpellDictionary ;
import org.dts.spell.event.ErrorCountListener ;
import org.dts.spell.event.FindSpellCheckErrorListener ;
import org.dts.spell.event.SpellCheckEvent ;
import org.dts.spell.event.SpellCheckListener ;
import org.dts.spell.finder.Word ;
import org.dts.spell.finder.WordFinder ;
import org.dts.spell.finder.CharSequenceWordFinder ;

/**
 * @author DreamTangerine
 *  
 */
public class SpellChecker
{
  private HashSet<String> ignore = new HashSet<String>() ;
  private HashMap<String, String> replace = new HashMap<String, String>() ;  

  private boolean skipNumbers = true ;
  private boolean ignoreUpperCaseWords = true ;
  private boolean caseSensitive = true ; 
  
  public SpellChecker(SpellDictionary dictionary)
  {
    this.dictionary = dictionary ;
  }

  private SpellDictionary dictionary ;

  /**
   * @param dictionary
   *          The dictionary to set.
   */
  public void setDictionary(SpellDictionary dictionary)
  {
    this.dictionary = dictionary ;
  }

  public SpellDictionary getDictionary()
  {
    return dictionary ;
  }

  /**
   * This method add teh word to the ignore table.
   * 
   * @param word
   */
  public void addIgnore(String word)
  {
    ignore.add(word.trim()) ;
  }
  
  public void resetIgnore()
  {
    ignore.clear() ;
  }
  
  public void setSkipNumbers(boolean skip)
  {
    skipNumbers = skip ;
  }
  
  public boolean isSkipNumbers()
  {
    return skipNumbers ;
  }
  
  /**
   * This method add a word to the replace table.
   * 
   * @param oldWord old word to replace.
   * @param newWord new word to replace
   */
  public void addReplace(String oldWord, String newWord)
  {
    replace.put(oldWord.trim(), newWord.trim()) ;
  }
  
  public void resetReplace()
  {
    replace.clear() ;  
  }
  
  public void setCaseSensitive(boolean sensitive)
  {
    caseSensitive = sensitive ;
  }
  
  public boolean isCaseSensitive()
  {
    return caseSensitive ;
  }
  
  public boolean isIgnoreUpperCaseWords()
  {
    return ignoreUpperCaseWords ;
  }
  
  public void setIgnoreUpperCaseWords(boolean ignore)
  {
    ignoreUpperCaseWords = ignore ;
  }
  
  private boolean checkCase(Word word)
  {
    if (isCaseSensitive())
    {
      if (word.isUpperCase())
      {
        if (!isIgnoreUpperCaseWords())
          return !dictionary.isCorrect(word.getText().toLowerCase()) ;
        else
          return true ;
      }
      else
      {
        return word.isCorrectFirstChar() || !dictionary.isCorrect(word.getText().toLowerCase()) ;
      }
    }
    else
      return true ;
  }
  
  /**
   * @return true si todo ha ido bien y no tiene errores.
   *  
   */
  public boolean isCorrect(CharSequence txt)
  {
    return isCorrect(new CharSequenceWordFinder(txt)) ;
  }

  public boolean isCorrect(WordFinder finder)
  {
    return check(finder, new FindSpellCheckErrorListener()) ;
  }

  public Word checkSpell(CharSequence txt)
  {
    return checkSpell(new CharSequenceWordFinder(txt)) ;
  }

  private static final FindSpellCheckErrorListener ERROR_FIND_LISTENER = new FindSpellCheckErrorListener() ;

  public Word checkSpell(WordFinder finder)
  {
    check(finder, ERROR_FIND_LISTENER) ;

    return ERROR_FIND_LISTENER.getInvalidWord() ;
  }

  private static final ErrorCountListener ERROR_COUNT_LISTENER = new ErrorCountListener() ;

  public int getErrorCount(CharSequence txt)
  {
    return getErrorCount(new CharSequenceWordFinder(txt)) ;
  }

  public int getErrorCount(WordFinder finder)
  {
    check(finder, ERROR_COUNT_LISTENER) ;

    return ERROR_COUNT_LISTENER.getErrorsCount() ;
  }
  
  private boolean isRepeat(Word word, Word last)
  {
    return  null != last && 
            word.getText().equalsIgnoreCase(last.getText()) && 
            !word.isStartOfSentence() ;
  }

  private boolean isNumber(Word word)
  {
    try
    {
      new BigDecimal(word.getText()) ;
      
      return true ;
    }
    catch (NumberFormatException ex)
    {
      return false ;
    }
  }
  
  private boolean canSkipWord(Word word)
  {
    // TODO : Skip internet address
    return isNumber(word) && isSkipNumbers() ;
  }
  
	private SpellCheckEvent checkCurrent(Word word, Word last, WordFinder finder, SpellCheckListener listener)
	{
    String wordText = word.getText() ;
    String newString = replace.get(wordText) ;
    SpellDictionary dict = getDictionary() ;		
		SpellCheckEvent event = null ;

    if (null != newString)
      finder.replace(newString) ;
    else if (!ignore.contains(wordText))
    {
      if (isRepeat(word, last))
      {
        event = new SpellCheckEvent(this, finder) ;        
        listener.repeatWordError(event) ;
      }
      else if (!canSkipWord(word))
      {
        if (!dict.isCorrect(wordText))
        {
          event = new SpellCheckEvent(this, finder) ;        
          listener.spellingError(event) ;
        }
        else if (!checkCase(word))
        {
          // TODO : Set the correct spell to the event.
          event = new SpellCheckEvent(this, finder) ;        
          listener.badCaseError(event) ;
        }
      }
    }
		
		return event ;
	}
	
  public boolean check(WordFinder finder, SpellCheckListener listener)
  {
    boolean result = true ;
		Word lastWord = null ;

    SpellCheckEvent event = new SpellCheckEvent(this, finder) ;
    
    listener.beginChecking(event) ;
    boolean exit = event.isCancel() ;    

    while (!exit && finder.hasNext())
    {
      Word word = finder.next() ;
			event = checkCurrent(word, lastWord, finder, listener) ;
			
			if (null != event)
			{
	      result = false ;
	      exit = event.isCancel() ;
			}
			
			lastWord = word ;
    }

    listener.endChecking(new SpellCheckEvent(this, finder)) ;
    return result ;
  }
}
