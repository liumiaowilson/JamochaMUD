/*
 * Created on 04/03/2005
 *
 */
package org.dts.spell.swing.utils;

import javax.swing.text.Highlighter;

/**
 * @author DreamTangerine
 * NOTE : Tags are close at begin and open at end,for example the interval is [6, 11).
 */
public class TagList
{
  private static class TagNode
  {
    public TagNode(Highlighter.Highlight tag)
    {
      this(tag, null, null) ;
    }

    public TagNode(Highlighter.Highlight tag, TagNode next)
    {
      this(tag, null, next) ;
    }
    
    public TagNode(Highlighter.Highlight tag, TagNode previous, TagNode next)
    {
      this.tag = tag ;
      setNext(next) ;
      setPrevious(previous) ;
    }
    
    public TagNode getNext()
    {
      return next ;
    }
    
    public void setNext(TagNode node)
    {
      next = node ;
    }
    
    public TagNode getPrevious()
    {
      return prev ;
    }
    
    public void setPrevious(TagNode node)
    {
      prev = node ;
    }
    
    private TagNode next ;
    private TagNode prev ;

    public Highlighter.Highlight getTag()
    {
      return tag ;
    }

    public String toString()
    {
     return "(" + tag.getStartOffset() + "," + tag.getEndOffset() + ")" ; 
    }
    
    /**
     * The current Highlighter.Highlight. In fact is the previous iterator. That
     * is iterator.previous() == current.
     * 
     * <b>NOTE</b> : The use of Highlighter.Highlight is not documented in the
     * JDK. It known that highlighter.addHighlight return this class by the
     * inspect of the <em>SUN</em> code. So it may change in the future.
     */
    private Highlighter.Highlight tag ;
  }
  
  public TagList()
  {
    clear() ;
  }
  
  public boolean isEmpty()
  {
    return null == first ;
  }
  
  public void clear()
  {
    first = null ; 
    last = null ;
    current = null ;
  }

  /**
   * Search the TagNode where index should be from current to first node. 
   * It can be the pre or post node. 
   * If the index is before the first and returnNext is false null is returned.
   * 
   * @param index The index to search.
   * @param returnNext It is false the prenode is returned otherwise the postnode.
   * @return The node where index should be. It can be the pre or post node.
   */
  private TagNode findTagNodeDesc(int index, boolean returnNext)
  {
    Highlighter.Highlight curTag = current.getTag() ;
    TagNode aux = current ;
    TagNode prev = aux.getPrevious() ;
    boolean exit = false ;
    
    while (null != prev && !exit)
    {
      curTag = prev.getTag() ;
      
      if (HighlightUtils.isMinorOrEquals(curTag, index))
        exit = true ;
      else
      {
        aux = prev ;
        prev = aux.getPrevious() ;
      }
    }
    
    if (returnNext)
      return aux ;
    else
      return prev ;
  }
  
  /**
   * Search the TagNode where index should be from current to last node. 
   * The result can be the pre or post node position. 
   * If the index is past the last and returnNext is true null is returned.
   *
   * @param index The index to search.
   * @param returnNext It is false the prenode is returned otherwise the postnode.
   * @return The node where index should be. It can be the pre or post node.
   */
  private TagNode findTagNodeAsc(int index, boolean returnNext)
  {
    Highlighter.Highlight curTag = current.getTag() ;
    TagNode aux = current ;    
    TagNode next = aux.getNext() ;
    boolean exit = false ;
    
    while (null != next && !exit)
    {
      curTag = next.getTag() ;
      
      if (HighlightUtils.isMajor(curTag, index))
        exit = true ;
      else
      {
        aux = next ;
        next = aux.getNext() ;
      }
    }

    if (returnNext)
      return next ;
    else
      return aux ;
  }

  /**
   * Search the TagNode with that tag.
   * 
   * @param tag the tag to look for.
   * @return The TagNode or null if is not found.
   */
  private TagNode findTagNode(Highlighter.Highlight tag)
  {
    Highlighter.Highlight curTag = current.getTag() ;
      
    if (tag == curTag)
      return current ;
    else 
    {
      TagNode node ;
      
      if (HighlightUtils.isMinor(tag, curTag))      
        node = findTagNodeDesc(tag.getStartOffset(), true) ;
      else 
        node = findTagNodeAsc(tag.getEndOffset(), false) ;
      
      if (node.getTag() == tag)
        return node ;
      else
        return null ;
    }
  }

  private TagNode findPreviousNode(int index)
  {
    TagNode result ;
    
    if (HighlightUtils.isMajorOrEquals(current.getTag(), index)) 
      result = findTagNodeDesc(index, false) ;
    else
      result = findTagNodeAsc(index, false) ;
    
    while (null != result && HighlightUtils.isNullRange(result.getTag()))
      result = result.getPrevious() ; 
    
    return result ;
  }
  
  public void add(Object tag)
  {
    add((Highlighter.Highlight) tag) ;
  }
  
  private void add(Highlighter.Highlight tag)
  {
    if (current != null)
    {
      TagNode node ;
      Highlighter.Highlight curTag = current.getTag() ;
      
      if (HighlightUtils.isMinor(curTag, tag))
        node = findTagNodeAsc(tag.getStartOffset(), true) ;
      else
        node = findTagNodeDesc(tag.getStartOffset(), true) ;
        
      if (node == first)
      {
        current = new TagNode(tag, first) ;
        first.setPrevious(current) ;
        first = current ;
      }
      else if (null == node)
      {
        current = new TagNode(tag, last, null) ;
        last.setNext(current) ;
        last = current ;
      }
      else
      {
        current = new TagNode(tag, node.getPrevious(), node) ;
        current.getPrevious().setNext(current) ;
        node.setPrevious(current) ;
      }
    }
    else
    {
      first = new TagNode(tag) ;
      last = first ;
      current = first ;
    }
  }

  public void remove(Object tag)
  {
    remove((Highlighter.Highlight) tag) ;
  }

  private void remove(Highlighter.Highlight tag)
  {
    if (!isEmpty())
    {
      TagNode node = findTagNode(tag) ;
      
      if (null != node)
      {
        if (first == node)
        {
          if (last != node)
          {
            first = node.getNext() ;
            first.setPrevious(null) ;
            current = first ;
          }
          else
          {
            first = null ;
            last = null ;
            current = null ;          
          }
        }      
        else if (last == node)
        {
          last = node.getPrevious() ;
          last.setNext(null) ;
          current = last ;
        }
        else
        {
          TagNode prev = node.getPrevious() ;
          
          prev.setNext(node.getNext()) ;
          node.getNext().setPrevious(prev) ;
          current = prev ;
        }
      }
    }
  }

  private boolean isInside(int beginPos, int endPos)
  {
    if (isEmpty())
      return false ;
    else
    {
      return first.getTag().getStartOffset() <= endPos && 
              last.getTag().getEndOffset() >= beginPos ; 
    }
  }
  
  public void removeAll(Highlighter highlighter)
  {
    while (null != first)
    {
      highlighter.removeHighlight(first.getTag()) ;
      first = first.getNext() ; 
    }
    
    last = null ;
    current = null ;
  }
  
  private TagNode removeNullRange(TagNode begin, Highlighter highlighter)
  {
    // Delete until no null range
    while (null != begin && HighlightUtils.isNullRange(begin.getTag()))
    {
      highlighter.removeHighlight(begin.getTag()) ;
      begin = begin.getNext() ;
    }

    return begin ;
  }

  private TagNode removeRangeTo(TagNode begin, Highlighter highlighter, int endPos)
  {
    // Delete until no endPos
    while (null != begin && HighlightUtils.isMinorOrEquals(begin.getTag(), endPos))
    {
      highlighter.removeHighlight(begin.getTag()) ;
      begin = begin.getNext() ;
    }

    return begin ;
  }
  
  public void removeRange(int beginPos, int endPos, Highlighter highlighter)
  {
    if (isInside(beginPos, endPos))
    {
      if (HighlightUtils.isMajorOrEquals(first.getTag(), beginPos) && 
          HighlightUtils.isMinorOrEquals(last.getTag(), endPos))
        removeAll(highlighter) ;
      else
      {
        TagNode prev = findPreviousNode(beginPos) ;
        TagNode next ;
        
        if (null == prev)
        {
          // Delete the next to first node
          if (HighlightUtils.isInside(first.getTag(), beginPos - 1))
          {
            next = removeRangeTo(first.getNext(), highlighter, endPos) ;
            prev = first ;
          }
          else // Delete first node
          {
            next = removeRangeTo(first, highlighter, endPos) ;
            first = next ;
            first.setPrevious(null) ;
            prev = next ;
            next = prev.getNext() ;
          }
        }
        else
          next = removeRangeTo(prev.getNext(), highlighter, endPos) ;
           
        if (null != next)
        {
          prev.setNext(next) ;
          next.setPrevious(prev) ;
        }
        else
        {
          last = prev ;
          prev.setNext(null) ;
        }
        
        current = prev ;
      }
    }
  }
  
  public void removeNullRanges(int beginPos, int endPos, Highlighter highlighter)
  {
    if (isInside(beginPos, endPos))
    {
      if (HighlightUtils.isMajorOrEquals(first.getTag(), beginPos) && 
          HighlightUtils.isMinorOrEquals(last.getTag(), endPos))
        removeAll(highlighter) ;
      else
      {
        TagNode prev = findPreviousNode(beginPos) ;
        TagNode next ;
        
        if (null == prev)
        {
          // Delete the next to first node
          if (HighlightUtils.isInside(first.getTag(), beginPos - 1))
          {
            next = removeNullRange(first.getNext(), highlighter) ;
            prev = first ;
          }
          else // Delete first node
          {
            next = removeNullRange(first, highlighter) ;
            first = next ;
            first.setPrevious(null) ;
            prev = next ;
            next = prev.getNext() ;
          }
        }
        else
          next = removeNullRange(prev.getNext(), highlighter) ;
           
        if (null != next)
        {
          prev.setNext(next) ;
          next.setPrevious(prev) ;
        }
        else
        {
          last = prev ;
          prev.setNext(null) ;
        }
        
        current = prev ;
      }
    }
  }
  
  public void updateCurrent(int curPos)
  {
    if (!isEmpty())
    {
      if (HighlightUtils.isMajor(first.getTag(), curPos))
        current = first ;
      else if (HighlightUtils.isMinor(last.getTag(), curPos))
        current = last ;
      else
      {
        current = findPreviousNode(curPos) ;
        
        if (null == current)
          current = first ;
      }
    }
  }

  public String toString()
  {
    TagNode aux = first ;
    String result = "" ;
    
    while (aux != null)
    {
      result += aux + "\n" ;
      aux = aux.getNext() ;
    }
    
    result += "Current = " + current ;
      
    return result ;
  }
  
  private TagNode first ; 
  private TagNode last ;
  
  /**
   * Recent used node. It the recent used node, it update when add, remove o call
   * updateCurrent. It is a cache node.  
   */
  private TagNode current ;
}
