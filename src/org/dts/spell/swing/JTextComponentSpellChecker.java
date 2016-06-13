/*
 * Created on 26/02/2005
 *
 */
package org.dts.spell.swing;

import java.util.HashMap;
import java.util.Map;

import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

import org.dts.spell.SpellChecker;
import org.dts.spell.swing.event.ErrorMarkerListener;
import org.dts.spell.swing.event.UIErrorMarkerListener;
import org.dts.spell.swing.finder.DocumentWordFinder;

/**
 * @author DreamTangerine
 *
 */
public class JTextComponentSpellChecker
{
  private SpellChecker spellChecker ;
  private UIErrorMarkerListener uiErrorListener = new UIErrorMarkerListener() ;  
  private Map<JTextComponent, ErrorMarkerListener> errorMarkers = new HashMap<JTextComponent, ErrorMarkerListener>() ;
  private DocumentWordFinder defaultDocumentWordFinder = new DocumentWordFinder() ;
  private RealTimeSpellChecker realTimeSpellChecker = null ;
  
  
  public JTextComponentSpellChecker(SpellChecker checker)
  {
    spellChecker = checker ;
  }
  
  private ErrorMarkerListener getErrorMarkerListener(JTextComponent textComponent)
  {
    return errorMarkers.get(textComponent) ;
  }
  
  public void markErrors(JTextComponent textComponent)
  {
    markErrors(textComponent, defaultDocumentWordFinder) ;
  }
  
  /**
   * Mark current errors (not while you are typing) of the JTextComponent.
   * You must call unMarkErrors to free resources when you don't need the marks. 
   * 
   * @param textComponent
   * @param wordFinder
   */
  public void markErrors(JTextComponent textComponent, DocumentWordFinder wordFinder)
  {
    ErrorMarkerListener listener = getErrorMarkerListener(textComponent) ;
    
    if (null == listener)
    {
      listener = new ErrorMarkerListener() ; 
      errorMarkers.put(textComponent, listener) ;
    }
    
    Document doc = textComponent.getDocument() ;

    listener.setTextComponent(textComponent) ;
    wordFinder.setDocument(doc) ;
    
    spellChecker.check(wordFinder, listener) ;
    
    if (wordFinder == defaultDocumentWordFinder)
      wordFinder.quitDocument() ;
  }
  
  /**
   * Allow quit all marked error of the textComponent. You must call it when you
   * don't want the errors marks. You must first call markErrors or a 
   * IllegalArgumentException will be throw. 
   * 
   * @param textComponent
   */
  public void unMarkErrors(JTextComponent textComponent)
  {
    ErrorMarkerListener listener = getErrorMarkerListener(textComponent) ;
    
    if (null != listener)
    {
      listener.setTextComponent(null) ;      
      errorMarkers.remove(textComponent) ;
    }
    else
      throw new IllegalArgumentException() ;
  }
  
  public boolean spellCheck(JTextComponent textComponent)
  {
    return spellCheck(textComponent, defaultDocumentWordFinder) ;
  }
  
  
  /** 
   * Chek the current component showing de Graphical Dialog. 
   * If the realtime spell check is for textComponent, the realtime spell checking
   * is stoped and started when the spellcheck is started and finished respectively.  
   * 
   */
  public boolean spellCheck(
      JTextComponent textComponent, 
      DocumentWordFinder documentWordFinder)
  {
    Document doc = textComponent.getDocument() ;
    boolean isRealTime = 
      null != realTimeSpellChecker &&  
      realTimeSpellChecker.getDocumentWordFinder() == documentWordFinder ; 

    if (isRealTime)
      stopRealtimeMarkErrors() ;
    
    uiErrorListener.setTextComponent(textComponent) ;
    documentWordFinder.setDocument(doc) ;
    
    boolean result = spellChecker.check(documentWordFinder, uiErrorListener) ;
  
    uiErrorListener.quitTextComponent() ;
    
    if (!isRealTime)
      documentWordFinder.quitDocument() ;
    else
      startRealtimeMarkErrors(textComponent, documentWordFinder) ;
    
    return result ;
  }
  
  public void startRealtimeMarkErrors(JTextComponent textComponent)
  {
	  startRealtimeMarkErrors(textComponent, defaultDocumentWordFinder) ;
  }
  
  /**
   * Mark current errors (not while you are typing) of the JTextComponent.
   * You must call unMarkErrors to free resources when you don't need the marks. 
   * 
   * @param textComponent
   * @param wordFinder
   */
  public void startRealtimeMarkErrors(JTextComponent textComponent, DocumentWordFinder wordFinder)
  {
    Document doc = textComponent.getDocument() ;

    wordFinder.setDocument(doc) ;
    
    realTimeSpellChecker = new RealTimeSpellChecker(spellChecker, textComponent, wordFinder) ;   
    realTimeSpellChecker.start() ;
  }

  public void stopRealtimeMarkErrors()
  {
    realTimeSpellChecker.stop() ;
    realTimeSpellChecker = null ;
  }
  
}