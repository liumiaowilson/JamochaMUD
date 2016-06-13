/**
 * 
 */
package org.dts.spell.swing.utils;

import javax.swing.event.DocumentEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Position;

public class TextRange
{
	private Position begin ;
  private Position end ;
  private boolean remove ;

  public TextRange(DocumentEvent e) throws BadLocationException
  {
    Document doc = e.getDocument() ;
    begin = doc.createPosition(e.getOffset()) ; 
    end = doc.createPosition(e.getOffset() + e.getLength()) ;

    remove = DocumentEvent.EventType.REMOVE == e.getType() ;
    
//    if (!remove)
//      System.out.println(
//          "INSERT Nuevo Rango = " + begin + ", " + end) ;/* + " " +; 
//          doc.getText(e.getOffset(), e.getOffset() + e.getLength())) ; */
//    else
//      System.out.println(
//          "DELETE Nuevo Rango = " + begin + ", " + end) ;
  }
  
	public TextRange(Position begin, Position end)
	{
		this.begin = begin ;
		this.end = end ;
	}

	/**
	 * Compare with the current text range.
	 * 
	 * @param index
	 *          index to compare
	 * @return less than 0 if is minor. 0 is inside of the range (inclusive).
	 *         more than 0 if is mayor.
	 */
	public int compare(int index)
	{
		if (begin.getOffset() > index)
			return -1 ;
		else if (end.getOffset() < index)
			return 1 ;
		else
			return 0 ;
	}

  public TextRange compactWith(DocumentEvent e) throws BadLocationException
  {
    TextRange result ;
    int newBegin = e.getOffset() ; 
    int newEnd = e.getOffset() + e.getLength() ;
    
    if (DocumentEvent.EventType.REMOVE == e.getType())
    {
      if (isInside(newBegin) || isInside(newEnd))
        result = null ;
      else
        result = new TextRange(e) ;
    }
    else
    {
      int currentBegin = getBegin() ;
      int currentEnd = getEnd() ;
      
      if (newEnd < currentBegin || newBegin > currentEnd)
        result = new TextRange(e) ;
      else
      {
        Document doc = e.getDocument() ;
        
        if (newBegin < currentBegin)
          begin = doc.createPosition(newBegin) ;
        
        if (newEnd > currentEnd)
          end = doc.createPosition(newEnd) ;
        
        // The TextRange is the same, only modify begin and/or end.
        result = null ;        
      }
    }
    
    return result ;
  }
  
	public boolean isInside(int index)
	{
		return compare(index) == 0 ;
	}
  
  public int getBegin()
  {
    return begin.getOffset() ;      
  }
  
  public int getEnd()
  {
    return end.getOffset() ;
  }
  
  public boolean isEmpty()
  {
    return getBegin() == getEnd() ;
  }
  
  public boolean isRemove()
  {
    return remove ;
  }
  
  public String toString()
  {
    return "Text_range = " + getBegin() + ", " + getEnd() ; 
  }
}