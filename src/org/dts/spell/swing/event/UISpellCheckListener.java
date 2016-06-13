/*
 * Created on 24/02/2005
 *
 */
package org.dts.spell.swing.event;

import java.text.MessageFormat;

import javax.swing.JOptionPane;

import org.dts.spell.event.SpellCheckAdapter;
import org.dts.spell.event.SpellCheckEvent ;
import org.dts.spell.finder.Word;
import org.dts.spell.swing.JSpellDialog;
import org.dts.spell.swing.utils.ErrorMsgBox;
import org.dts.spell.swing.utils.Messages;

/**
 * @author DreamTangerine
 *  
 */
public class UISpellCheckListener extends SpellCheckAdapter
{
  /**
   * The current JSpellDialog
   */
  private JSpellDialog spellDialog ;

  /**
   * @return Returns the spellDialog.
   */
  public JSpellDialog getSpellDialog()
  {
    return spellDialog ;
  }

  /**
   * @param spellDialog
   *          The spellDialog to set.
   */
  public void setSpellDialog(JSpellDialog spellDialog)
  {
    this.spellDialog = spellDialog ;
  }

  /**
   *  
   */
  public UISpellCheckListener()
  {
    this(null) ;
  }

  /**
   * Create a UISpellCheckListener that show a JSpellDialog for each error that was 
   * found. Tou can pass a null JSpellDialog and the UISpellCheckListener will create
   * one for you. 
   * 
   * @param spellDialog The dialog to show it can be null.
   */
  public UISpellCheckListener(JSpellDialog spellDialog)
  {
    setSpellDialog(spellDialog) ;
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see org.dts.spell.event.SpellCheckListener#beginChecking(org.dts.spell.event.SpellCheckEvent)
   */
  public void beginChecking(SpellCheckEvent event)
  {
    if (getSpellDialog() == null)
      setSpellDialog(new JSpellDialog()) ;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.dts.spell.event.SpellCheckListener#spellingError(org.dts.spell.event.SpellCheckEvent)
   */
  public void spellingError(SpellCheckEvent event)
  {
    JSpellDialog dlg = getSpellDialog() ;
    
    if (!dlg.showDialog(event.getSpellChecker(), event.getWordFinder()))
      event.cancel() ;
  }

  public void badCaseError(SpellCheckEvent event)
  {
    Word word = event.getCurrentWord() ;
    String newWord = word.getStartSentenceWordCase() ;
    String msg = MessageFormat.format(
        Messages.getString("ERROR_CAPITALIZATION_STRING"), 
        new Object[] { word, newWord } ) ;

    int result = ErrorMsgBox.yesNoCancelMsg(Messages.getString("ERROR_CAPITALIZATION_TITLE_STRING"), msg) ;
    
    switch(result)
    {
      case JOptionPane.YES_OPTION :
        event.getWordFinder().replace(newWord) ;
        break ;
      
      case JOptionPane.NO_OPTION :
        // Nothing to do.
        break ;
   
      case JOptionPane.CANCEL_OPTION :
        event.cancel() ;
        break ;
    }
  }

  public void repeatWordError(SpellCheckEvent event)
  {
    Word word = event.getCurrentWord() ;
    String msg = MessageFormat.format(
        Messages.getString("ERROR_REPEAT_WORD_STRING"), 
        new Object[] { word } ) ;
    int result = ErrorMsgBox.yesNoCancelMsg(Messages.getString("ERROR_REPEAT_WORD_TITLE_STRING"), msg) ;
    
    switch(result)
    {
      case JOptionPane.YES_OPTION :
        event.getWordFinder().replace("") ;
        break ;
      
      case JOptionPane.NO_OPTION :
        // Nothing to do.
        break ;
   
      case JOptionPane.CANCEL_OPTION :
        event.cancel() ;
        break ;
    }
  }
}
