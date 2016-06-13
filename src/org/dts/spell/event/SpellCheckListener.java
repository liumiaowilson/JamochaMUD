package org.dts.spell.event;

import java.util.EventListener;

/**
 * This is the event based listener interface.
 *
 * @author Jason Height (jheight@chariot.net.au)
 */
public interface SpellCheckListener extends EventListener
{
  /**
   * Se llama cuando se empieza a realizar una correccibad_char_replacedn ortogrbad_char_replacedfica.
   * Se puede cancelar la correcibad_char_replacedn llamando a 
   * <code>{@link org.dts.spell.event.SpellCheckEvent#cancel() SpellCheckEvent.cancel()}</code> 
   * 
   * @param event
   */
  public void beginChecking(SpellCheckEvent event) ;
  
  /**
   * Se llama cuando se ha detectado un error en la correccibad_char_replacedn ortogrbad_char_replacedfica.
   * Se puede cancelar la correcibad_char_replacedn llamando a 
   * <code>{@link org.dts.spell.event.SpellCheckEvent#cancel() SpellCheckEvent.cancel()}</code>
   *  
   * @param event
   */
  public void spellingError(SpellCheckEvent event) ;

  /**
   * Se ha dectectado un error en el uso de maybad_char_replacedsculas/minbad_char_replacedsculas, por ejemplo
   * porque la palabra estbad_char_replaced al principio de la frase y debe de ir en maybad_char_replacedsculas.
   * @param event
   */
  public void badCaseError(SpellCheckEvent event) ;
  
  
  /**
   * Se ha dectectado un error, al tener dos palabras iguales seguidas.
   * @param event
   */
  public void repeatWordError(SpellCheckEvent event) ;
  
  /**
   * Se llama cuando se termina una correccibad_char_replacedn ortogrbad_char_replacedfica.
   * Se puede cancelar la correcibad_char_replacedn llamando a 
   * <code>{@link org.dts.spell.event.SpellCheckEvent#cancel() SpellCheckEvent.cancel()}</code> 
   * 
   * @param event
   */
  public void endChecking(SpellCheckEvent event) ;  
}
