/*
 * Created on 18/02/2005
 *
 */
package org.dts.spell.swing.finder;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class DocumentCharSequence implements CharSequence
{
  private Document document ;

  public DocumentCharSequence()
  {
    this(null) ;
  }
  
  public DocumentCharSequence(Document doc)
  {
    this.document = doc ;
  }

  public int length()
  {
    if (null == document)
      return 0 ;
    else
      return document.getLength() ;
  }

  // TODO : Optimizar con una cachbad_char_replaced    
  public char charAt(int index)
  {
    try
    {
      return document.getText(index, 1).charAt(0) ;
    }
    catch (BadLocationException e)
    {
      throw new IndexOutOfBoundsException(e.getLocalizedMessage()) ;
    }
  }

  public CharSequence subSequence(int start, int end)
  {
    return new DocumentFixedCharSequence(document, start, end) ;
  }
  
  public String toString()
  {
    try
    {
      if (null != document)
        return document.getText(0, length()) ;
      else
        return "" ;
    }
    catch (BadLocationException e)
    {
      throw new IndexOutOfBoundsException(e.getLocalizedMessage()) ;
    }
  }
  
  public Document getDocument()
  {
    return document ;
  }
  
  public void setDocument(Document doc) 
  {
    this.document = doc ;
  }
}