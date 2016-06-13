/**
 * 
 */
package org.dts.spell.swing.finder;

import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Position;

import org.dts.spell.finder.Word;
import org.dts.spell.swing.utils.TextRange;

public class SynchronizedWordFinder extends DocumentWordFinder
{
	private DocumentWordFinder documentFinder ;
  private TextRange textRange ;

	public SynchronizedWordFinder(DocumentWordFinder documentFinder)
	{
    super(documentFinder.getCharSequence(), documentFinder.getTokenizer()) ;
		this.documentFinder = documentFinder ;
	}

  public DocumentWordFinder getWrapDocumentFinder()
  {
    return documentFinder ;
  }
  
  private Position createPosition(Word word, boolean begin)
  {
    Document doc = documentFinder.getDocument() ;
    
    if (null == word)
    {
      if (begin)
        return doc.getStartPosition() ;
      else
        return doc.getEndPosition() ;
    }
    else
    {
      try
      {
        if (begin)
          return doc.createPosition(word.getStart()) ;
        else
          return doc.createPosition(word.getEnd()) ;          
      }
      catch (BadLocationException e)
      {
        if (begin)
          return doc.getStartPosition() ;
        else
          return doc.getEndPosition() ;
      }
    }
  }
  
  public void setTextRange(TextRange textRange)
  {
    this.textRange = textRange ;
    final Document doc = documentFinder.getDocument() ;

    doc.render(new Runnable()
    {
      public void run()
      { 
        try
        {
          Word word = documentFinder.getPreviousWord(
              SynchronizedWordFinder.this.textRange.getBegin()) ;
          Position begin = createPosition(word, true) ;
          
          documentFinder.init(word) ;
          
          if (!SynchronizedWordFinder.this.textRange.isRemove())
            word = documentFinder.getNextWord(
                SynchronizedWordFinder.this.textRange.getEnd()) ;
          else
            word = documentFinder.getNextWord(
                SynchronizedWordFinder.this.textRange.getBegin()) ;
          
          Position end = createPosition(word, false) ;
          
          SynchronizedWordFinder.this.textRange = 
            new TextRange(begin, end) ;
        }
        catch (Exception ex)
        {
          ex.printStackTrace() ;
        }
      }
    }) ;
  }

  public String toString() 
  {
    return "rango_analizar_(" + textRange + ") : #" +
      getCharSequence().subSequence(
          textRange.getBegin(), textRange.getEnd()).toString() + "#" ;
  }
    
  public TextRange getTextRange()
  {
    return textRange ;
  }

	public Word current()
	{
		return documentFinder.current() ;
	}

  
  private class SynchronizedHasNext implements Runnable
  {
    private boolean hasNext ;     
    
    public void run()
    {
      if (documentFinder.hasCurrent())
      {
        Word word = current() ;
        
        if (textRange.compare(word.getEnd()) > 0)
          hasNext = false ;
        else
          hasNext = documentFinder.hasNext() ;
      }
      else
        hasNext = documentFinder.hasNext() ;  
    }
    
    public boolean hasNext()
    {
      return hasNext ;
    }
  }

	public boolean hasNext()
	{
    Document doc = documentFinder.getDocument() ;
    SynchronizedHasNext hasNext = new SynchronizedHasNext() ;
    
    doc.render(hasNext) ; 

    return hasNext.hasNext() ;
	}

	private class SynchronizedNext implements Runnable
	{
		private Word word ;			
		
		public void run()
		{
			word = documentFinder.next() ;
		}
		
		public Word getWord()
		{
			return word ;
		}
	}
	
	public Word next()
	{
		Document doc = documentFinder.getDocument() ;
		SynchronizedNext next = new SynchronizedNext() ;
		
		doc.render(next) ; 

		return next.getWord() ;
	}

	public void replace(final String newWord)
	{
		try
		{
			SwingUtilities.invokeAndWait(
					new Runnable()
					{
						public void run()
						{
							documentFinder.replace(newWord) ;							
						}
					}
			) ;
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public CharSequence getCharSequence()
	{
		return documentFinder.getCharSequence() ;
	}

	public Document getDocument()
	{
		return documentFinder.getDocument() ;
	}

	public DocumentCharSequence getDocumentCharSequence()
	{
		return documentFinder.getDocumentCharSequence() ;
	}

	private class SynchronizedWordAt implements Runnable
	{
		private Word word ;
    private int index ;
		
		public SynchronizedWordAt(int index)
		{
			this.index = index ;
		}
		
		public void run()
		{
			word = documentFinder.getWordAt(index) ;
		}
		
		public Word getWord()
		{
			return word ;
		}
	}
	
	public Word getWordAt(int index)
	{
		Document doc = documentFinder.getDocument() ;
		SynchronizedWordAt wordAt = new SynchronizedWordAt(index) ;
		
		doc.render(wordAt) ; 

		return wordAt.getWord() ;
	}

	public void quitDocument()
	{
		documentFinder.quitDocument() ;
	}

	public void setDocument(Document document)
	{
		documentFinder.setDocument(document) ;
	}
}
