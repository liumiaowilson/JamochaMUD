package org.dts.spell.swing.event;

import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

import org.dts.spell.event.SpellCheckAdapter;
import org.dts.spell.event.SpellCheckEvent;
import org.dts.spell.finder.Word;
import org.dts.spell.swing.finder.SynchronizedWordFinder;
import org.dts.spell.swing.utils.ErrorMarker;
import org.dts.spell.swing.utils.TextRange;

public class RealTimeSpellCheckerListener extends SpellCheckAdapter
{
	private ErrorMarker errorMarker ;
  
  public RealTimeSpellCheckerListener(JTextComponent textComponent)
  {
    errorMarker = new ErrorMarker(textComponent, false) ;
  }
	
	public void spellingError(SpellCheckEvent event)
	{
    markError(event) ;
  }
	
  public void badCaseError(SpellCheckEvent event)
  {
    markError(event) ;
  }  

  public void repeatWordError(SpellCheckEvent event)
  {
    markError(event) ;
  }
  
  private void markError(SpellCheckEvent event)
  {
    final Word badWord = event.getCurrentWord() ;
    
      try
      {
        SwingUtilities.invokeAndWait(
           new Runnable()
           {
             public void run()
             { 
               try
               {
                 errorMarker.markError(badWord.getStart(), badWord.getEnd()) ;
               }
               catch (BadLocationException e)
               {
                 // The user delete the word :(.
                 e.printStackTrace();
               } 
             }
           }) ;
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
  }
  
  public void beginChecking(SpellCheckEvent event)
  {
    SynchronizedWordFinder finder = (SynchronizedWordFinder) event.getWordFinder() ;
    final TextRange textRange = finder.getTextRange() ;
    
    try
    {
      SwingUtilities.invokeAndWait(
         new Runnable()
         {
           public void run()
           { 
             errorMarker.unMarkRange(textRange.getBegin(), textRange.getEnd()) ; 
           }
         }) ;
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  public void cleanAllErrors()
  {
    errorMarker.quitTextComponent() ;
  }
}