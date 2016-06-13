/*
 * Created on 11/02/2005
 *
 */
package org.dts.spell.event ;

import org.dts.spell.finder.Word ;

/**
 * Esta clase busca el primer error en el texto y para la correccibad_char_replacedn ortogrbad_char_replacedfica.
 * Nos permite saber cual es la palabra que estaba mal escrita.
 * 
 * @author DreamTangerine
 *  
 */
public class FindSpellCheckErrorListener extends SpellCheckAdapter
{
  private Word badWord ;

  /**
   *  
   */
  public FindSpellCheckErrorListener()
  {
    super() ;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.dts.spell.event.SpellCheckListener#beginChecking(org.dts.spell.event.SpellCheckEvent)
   */
  public void beginChecking(SpellCheckEvent event)
  {
    badWord = null ;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.dts.spell.event.SpellCheckListener#spellingError(org.dts.spell.event.SpellCheckEvent)
   */
  public void spellingError(SpellCheckEvent event)
  {
    createError(event) ;
  }

  public void badCaseError(SpellCheckEvent event)
  {
    createError(event) ;
  }
  
  public void repeatWordError(SpellCheckEvent event)
  {
    createError(event) ;
  }  
  
  public Word getInvalidWord()
  {
    return badWord ;
  }
  
  public boolean hasError()
  {
    return badWord != null ;
  }
  
  private void createError(SpellCheckEvent event)
  {
    badWord = event.getCurrentWord() ;
    
    event.cancel() ;
  }
}
