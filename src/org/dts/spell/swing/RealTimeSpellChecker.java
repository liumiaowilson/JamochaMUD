package org.dts.spell.swing ;

import java.util.LinkedList;
import java.util.List;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent ;

import org.dts.spell.SpellChecker ;
import org.dts.spell.swing.event.RealTimeSpellCheckerListener;
import org.dts.spell.swing.finder.DocumentWordFinder ;
import org.dts.spell.swing.finder.SynchronizedWordFinder;
import org.dts.spell.swing.utils.TextRange;


public class RealTimeSpellChecker implements DocumentListener, Runnable
{
	private SpellChecker spellChecker ;

	private JTextComponent textComponent ;
	
	private SynchronizedWordFinder finder ;
	
	private List<TextRange> textRanges = new LinkedList<TextRange>() ;
	
	private boolean stopThread ;

	public RealTimeSpellChecker(SpellChecker checker, JTextComponent textComponent, DocumentWordFinder finder)
	{
		spellChecker = checker ;
		this.textComponent = textComponent ;
		this.finder = new SynchronizedWordFinder(finder) ;
	}
	
	public void start()
	{
    Document doc = textComponent.getDocument() ; 
    
    doc.addDocumentListener(this) ;
    textRanges.add(new TextRange(doc.getStartPosition(), doc.getEndPosition())) ;
		stopThread = false ;

    Thread th = new Thread(this) ;
    
    th.setPriority(Thread.MIN_PRIORITY) ;
    th.start() ;
	}
	
	public synchronized void stop()
	{
		textComponent.getDocument().removeDocumentListener(this) ;
		stopThread = true ;
    notify() ;
	}

  public synchronized void addTextRange(DocumentEvent e)
  {
    try
    {
      if (textRanges.size() > 0)
      {
        TextRange oldTextRange = textRanges.get(textRanges.size() - 1) ; 
        TextRange newTextRange = oldTextRange.compactWith(e) ;
      
        if (null != newTextRange)
          textRanges.add(newTextRange) ;
      }
      else
        textRanges.add(new TextRange(e)) ;          
      
      if (textRanges.size() > 0)
        notify() ;
    }
    catch(Exception ex)
    {
      ex.printStackTrace() ;
    }
  }
  
	public void insertUpdate(DocumentEvent e)
	{
    addTextRange(e) ;
	}

	public void removeUpdate(DocumentEvent e)
	{
    addTextRange(e) ;
	}

	public void changedUpdate(DocumentEvent e)
	{
    // Nothing to do when the style change.
	}

  /**
   * Return the current DocumentFinder.
   */
  public DocumentWordFinder getDocumentWordFinder()
  {
    return finder.getWrapDocumentFinder() ;
  }
  
  private void checkRange(TextRange range, RealTimeSpellCheckerListener listener)
  {
    finder.setTextRange(range) ;
    
    spellChecker.check(finder, listener) ;
  }
  
	public void run()
	{
		RealTimeSpellCheckerListener listener = new RealTimeSpellCheckerListener(textComponent) ; 
		
		while (true)
		{
      try
      {
        TextRange range ;
        
        synchronized(this)
        {
          if (textRanges.isEmpty())
            wait() ;
          
          // Must finish :D
          if (stopThread)
            break ;
          
          range = textRanges.remove(0) ;
        }
  
        checkRange(range, listener) ;
      }
      catch (InterruptedException e)
      {
        e.printStackTrace();
      }
		}
    
    listener.cleanAllErrors() ;
	}
}
