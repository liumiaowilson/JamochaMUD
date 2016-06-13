/*
 * Created on 27/12/2004
 *
 */
package org.dts.spell.dictionary;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.zip.ZipFile;

import org.dts.spell.dictionary.myspell.MySpell;
import org.dts.spell.dictionary.myspell.Utils;

/**
 * @author DreamTangerine
 *
 */
public class OpenOfficeSpellDictionary implements SpellDictionary
{
  private File personalDict ;
  private MySpell mySpell ;
  private boolean isLoaded = false ;
  private Future loader = null ;

  public OpenOfficeSpellDictionary(ZipFile zipFile) throws IOException
  {
    this(zipFile, true) ;
  }

  public OpenOfficeSpellDictionary(final ZipFile zipFile, boolean inBackground) throws IOException
  {
    if (inBackground)
    {
      final ExecutorService executor = Executors.newSingleThreadExecutor() ;
      
      loader = executor.submit(
          new Callable<Object>()
          {
            public Object call() throws Exception
            {
              initFromZipFile(zipFile) ;
              executor.shutdown() ; // we no need more the executor
              return null ;
            }
          }
      ) ;
    } 
    else
      initFromZipFile(zipFile) ;
  }

  public OpenOfficeSpellDictionary(InputStream zipStream, File personalDict) throws IOException
  {
    this(zipStream, personalDict, true) ;
  }
  
  // NOTE : If inBackground is true when the dict is loaded the zipStream is closed.
  public OpenOfficeSpellDictionary(
      final InputStream zipStream, 
      final File personalDict, 
      boolean inBackground) throws IOException
  {
    if (inBackground)
    {
      final ExecutorService executor = Executors.newSingleThreadExecutor() ;
      
      loader = executor.submit(
          new Callable<Object>()
          {
            public Object call() throws Exception
            {
              initFromStream(zipStream, personalDict) ;
              zipStream.close() ;
              executor.shutdown() ; // we no need more the executor
              return null ;
            }
          }
      ) ;
    } 
    else
      initFromStream(zipStream, personalDict) ;
  }
  
  public OpenOfficeSpellDictionary(File dictFile, File affFile) throws IOException
  {
    this(dictFile, affFile, true) ;
  }

  public OpenOfficeSpellDictionary(
      final File dictFile, 
      final File affFile, 
      boolean inBackground) throws IOException
  {
    if (inBackground)
    {
      final ExecutorService executor = Executors.newSingleThreadExecutor() ;
      
      loader = executor.submit(
          new Callable<Object>()
          {
            public Object call() throws Exception
            {
              initFromFiles(dictFile, affFile) ;
              executor.shutdown() ; // we no need more the executor
              return null ;
            }
          }
      ) ;
    } 
    else
      initFromFiles(dictFile, affFile) ;
  }
  
  
  private File extractRootFile(File file)
  {
    String name = file.getName() ;
    int index = name.lastIndexOf('.') ;
    String rootName ;
    
    if (index != -1)
      rootName = name.substring(0, index) ;
    else
      rootName = name ;
    
    return new File(file.getParent(), rootName) ;
  }
  
  private void initFromFiles(File dictFile, File affFile) throws IOException
  {
    personalDict = getPersonalWordsFile(extractRootFile(dictFile)) ;
    mySpell = new MySpell(affFile.getPath(), dictFile.getPath()) ;
    
    readPersonalWords(personalDict) ;
    
    notifyLoaded() ;
  }
  
  private void initFromZipFile(ZipFile zipFile) throws IOException
  {
    personalDict = getPersonalWordsFile(extractRootFile(new File(zipFile.getName()))) ;
    mySpell = new MySpell(zipFile) ;

    readPersonalWords(personalDict) ;
    
    notifyLoaded() ;    
  }

  private void initFromStream(InputStream zipStream, File personalDict)  throws IOException
  {
    this.personalDict = personalDict ;
    mySpell = new MySpell(zipStream) ;
    
    readPersonalWords(personalDict) ;

    notifyLoaded() ;
  }
  
  public void addWord(String word) throws SpellDictionaryException
  {
    waitToLoad() ;    
    
    PrintWriter pw = null ;
    
    word = word.trim() ;
    
    try
    {
	    pw = new PrintWriter(
	        new OutputStreamWriter(
	            new FileOutputStream(personalDict, true), 
                mySpell.get_dic_encoding())) ;

      mySpell.addCustomWord(word) ;
	    
	    pw.println(word) ;
    }
    catch(Exception ex)
    {
      throw new SpellDictionaryException(ex) ;
    }
    finally
    {
      try
      {
        Utils.close(pw) ;
      }
      catch (IOException e)
      {
        throw new SpellDictionaryException(e) ;
      }
    }
  }

  public boolean isCorrect(String word)
  {
    waitToLoad() ;
    
    return mySpell.spell(word) ;
  }

  public List<String> getSuggestions(String word)
  {
    waitToLoad() ;

    return mySpell.suggest(word) ;
  }
  
  private synchronized void waitToLoad()
  {
    try
    {
      if (!isLoaded)
      {
        if (null == loader || !loader.isDone())
          wait() ;
        else
          loader.get() ; // If were and excpetion this method rethrow it
      }
    }
    catch (Exception e)
    {
      throw new IllegalStateException(e) ;
    }
  }
  
  private synchronized void notifyLoaded()
  {
    isLoaded = true ;
    notify() ;
  }
  
  private File getPersonalWordsFile(File rootFile)
  {
    return new File(rootFile.getParent(), rootFile.getName() + ".per") ;
  }
  
  private void readPersonalWords(File personalFile) throws IOException
  {
    BufferedReader rd = null ;

    try
    {
      if (null != personalFile && personalFile.exists() && !personalFile.isDirectory())
      {
	      rd = new BufferedReader(
	          new InputStreamReader(
	              new FileInputStream(personalFile), mySpell.get_dic_encoding())) ;
	      
		    String line = rd.readLine() ;
		    
		    while (line != null)
		    {
		      mySpell.addCustomWord(line.trim()) ;
		      line = rd.readLine() ;
		    }
      }
    }
    finally
    {
      Utils.close(rd) ;
    }
  }
}
