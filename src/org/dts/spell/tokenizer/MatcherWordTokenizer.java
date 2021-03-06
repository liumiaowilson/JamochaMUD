/*
 * Created on 29/03/2005
 *
 */
package org.dts.spell.tokenizer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dts.spell.finder.Word;

/**
 * @author DreamTangerine
 *
 */
public class MatcherWordTokenizer extends AbstractWordTokenizer
{
  /**
   * Represent all the space chars.
   */
  public static final String SPACE_CHARS = "\\s" ;

  public MatcherWordTokenizer(Matcher matcher)
  {
    this.matcher = matcher ;
  }

  public MatcherWordTokenizer()
  {
    this(Pattern.compile("[^" + SPACE_CHARS + "]+").matcher("")) ;
  }

  /**
   * Esta funcibad_char_replacedn crea un <code>{@link java.util.regex.Matcher Matcher}</code>
   * para buscar las palabras dentro de la (
   * <code>{@link java.lang.CharSequence CharSequence}</code>) dada.
   * 
   * @param regexp
   *          La expresibad_char_replacedn regular a utilizar y que harbad_char_replaced que se salten los
   *          espacios.
   * 
   * @param flags
   *          Los <em>flags</em> que se utilizan en
   *          <code>{@link java.util.regex.Pattern#compile(java.lang.String, int) Pattern.compile}</code>.
   * 
   * @return El <code>{@link java.util.regex.Matcher Matcher}</code> para
   *         obtener las palabras.
   */
  public static MatcherWordTokenizer createMatcher(
      String regexp,
      int flags)
  {
    return new MatcherWordTokenizer(Pattern.compile(regexp, flags).matcher("")) ;
  }

  /**
   * Esta funcibad_char_replacedn crea un <code>{@link java.util.regex.Matcher Matcher}</code>
   * para buscar las palabras dentro de la (
   * <code>{@link java.lang.CharSequence CharSequence}</code>) dada.
   * 
   * @param regexp
   *          La expresibad_char_replacedn regular a utilizar y que harbad_char_replaced que se salten los
   *          espacios.
   * 
   * @return El <code>{@link java.util.regex.Matcher Matcher}</code> para
   *         obtener las palabras.
   */
  public static MatcherWordTokenizer createMatcher(String regexp)
  {
    return new MatcherWordTokenizer(Pattern.compile(regexp).matcher("")) ;
  }

  /**
   * Esta funcibad_char_replacedn crea un <code>{@link java.util.regex.Matcher Matcher}</code>
   * para buscar las palabras dentro de la (
   * <code>{@link java.lang.CharSequence CharSequence}</code>) dada. Haciendo
   * que el texto que se le pasa sea el texto a excluir, es decir la expresbad_char_replacedn
   * deberbad_char_replacedan de ser los caracteres que forman los espacios entre las palabras.
   * 
   * @param chars
   *          La expresibad_char_replacedn regular a utilizar y que indica cuales son los
   *          espacios entre las palabras. Es decir los carbad_char_replacedcteres que no forman
   *          parte de una palabra.
   * 
   * @return El <code>{@link java.util.regex.Matcher Matcher}</code> para
   *         obtener las palabras.
   * 
   * @see MatcherWordTokenizer#SPACE_CHARS SPACE_CHARS
   */

  public static MatcherWordTokenizer createExcludeMatcher(String chars)
  {
    return 
      new MatcherWordTokenizer(
        Pattern.compile("[^" + chars + "]+").matcher("")) ;
  }
  
  /**
   * The Matcher of word. Ideally it skips the spaces.
   */
  private Matcher matcher ;
  
  /**
   * @return Returns the matcher.
   */
  protected Matcher getMatcher()
  {
    return matcher ;
  }
  
  /**
   * Para localizar bien los empieces de frases delega en la funcibad_char_replacedn isEndOfSentence.
   * que por defecto indica que no es un empiece de frase. 
   */
  public Word nextWord(int index)
  {
    if (!matcher.find(index))
      return null ;
    else
    {
      int start = matcher.start() ;
      String text = matcher.group() ;
      
      return new Word(text, start, isStartOfSentence(getCharSequence(), start)) ;
    }
  }

  public Word currentWord(int index)
  {
    if (!matcher.find(index))
      return null ;
    else
    {
      int start = matcher.start() ;
      String text = matcher.group() ;
      
      --index ;
      
      while (index >= 0 && matcher.find(index) && start != matcher.start())
      {
        start = matcher.start() ;
        text = matcher.group() ;
        
        --index ;
      }
      
      return new Word(text, start, isStartOfSentence(getCharSequence(), start)) ;
    }
  }

  public void updateCharSequence(int start, int end, int cause)
  {
    getMatcher().reset(getCharSequence()) ;
  }
}
