/*
 * Created on 24/02/2005
 *
 */
package org.dts.spell.examples;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.zip.ZipFile;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.text.PlainDocument;

import org.dts.spell.SpellChecker;
import org.dts.spell.dictionary.OpenOfficeSpellDictionary;
import org.dts.spell.dictionary.SpellDictionary;
import org.dts.spell.finder.Word;
import org.dts.spell.swing.JTextComponentSpellChecker;
import org.dts.spell.swing.finder.DocumentWordFinder;
import org.dts.spell.swing.utils.ErrorMsgBox;


/**
 * @author DreamTangerine
 *
 */
public class Example8
{
  private static Icon getIcon(String name)
  {
    return new ImageIcon(Example8.class.getResource(name)) ;
  }
  
  public static void main(String[] args)
  {
    try
    {
      // Allow paint while resize :D
      Toolkit.getDefaultToolkit().setDynamicLayout(true) ;
      
      // Don't delete background
      System.setProperty("sun.awt.noerasebackground", "true") ;
      
      SpellDictionary dict = new OpenOfficeSpellDictionary(new ZipFile(args[0])) ;
	    SpellChecker checker = new SpellChecker(dict) ;
	    
	    final JFrame frame = new JFrame("Check Speller") ;
	    final JTextArea textArea = new JTextArea() ;
	    
	    textArea.setWrapStyleWord(true) ;
	    textArea.setLineWrap(true) ;
	
      JPanel buttonPanel = new JPanel() ; 
      
      final JToggleButton checkRealTimeButton = new JToggleButton("Realtime Check", getIcon("images/stock_autospellcheck.png")) ;
	    final JTextComponentSpellChecker textSpellChecker = 
	      new JTextComponentSpellChecker(checker) ; 
	    
	    checkRealTimeButton.setMnemonic('C') ;
	    checkRealTimeButton.addActionListener(
	        new ActionListener()
	        {
            public void actionPerformed(ActionEvent e)
            {
              JToggleButton button = (JToggleButton) e.getSource() ;
              
              if (button.isSelected())
                textSpellChecker.startRealtimeMarkErrors(textArea) ;
              else
                textSpellChecker.stopRealtimeMarkErrors() ;
              
              textArea.requestFocusInWindow() ;              
            }
	        }
	    ) ;

      final JButton checkButton = new JButton("Check Spell", getIcon("images/stock_spellcheck.png")) ;
      
      checkButton.setMnemonic('S') ;
      checkButton.addActionListener(
          new ActionListener()
          {
            public void actionPerformed(ActionEvent e)
            {
              if (textSpellChecker.spellCheck(textArea))
                JOptionPane.showMessageDialog(textArea, "Text is OK") ;
              
              textArea.requestFocusInWindow() ;              
            }
          }
      ) ;
      
      
      
      JButton checkWord = new JButton("Get Word") ;
      
      checkWord.setMnemonic('W') ;
      checkWord.addActionListener(
          new ActionListener()
          {
            public void actionPerformed(ActionEvent e)
            {
              try
              {
                DocumentWordFinder wf = 
                  new DocumentWordFinder(textArea.getDocument()) ;
                
                int caretPos = textArea.getCaretPosition() ;
                
                Word wordAt = wf.getWordAt(caretPos) ;
                Word wordPrevious = wf.getPreviousWord(caretPos) ;
                Word wordNext = wf.getNextWord(caretPos) ;                

                JOptionPane.showMessageDialog(textArea, 
                    new String[] 
                    {  
                      "Caret Pos = " + caretPos,
                      "Previous : #" + wordPrevious + "#",
                      "AT : #" + wordAt + "#",            
                      "Next : #" + wordNext + "#"                      
                    }) ;
              }
              catch(Exception ex)
              {
                JOptionPane.showMessageDialog(textArea, ex.getLocalizedMessage()) ;
              }

              textArea.requestFocusInWindow() ;
            }
          }
      ) ;
      
      
      JButton newButton = new JButton("New", getIcon("images/stock_new-text.png")) ;

      newButton.setMnemonic('N') ;
      newButton.addActionListener(
          new ActionListener()
          {
            public void actionPerformed(ActionEvent e)
            {
              if (checkRealTimeButton.isSelected())
                textSpellChecker.stopRealtimeMarkErrors() ;
              
              textArea.setDocument(new PlainDocument()) ;
              frame.setTitle("Check Speller") ;
              System.gc() ;
              textArea.requestFocusInWindow() ;
              
              if (checkRealTimeButton.isSelected())
                textSpellChecker.startRealtimeMarkErrors(textArea) ;
            }
          }) ;
      
      JButton openButton = new JButton("Open...", getIcon("images/stock_open.png")) ;
      
      openButton.setMnemonic('O') ;
      openButton.addActionListener(
          new ActionListener()
          {
            public void actionPerformed(ActionEvent e)
            {
              JFileChooser fileChooser = new JFileChooser() ;
              
              if (fileChooser.showOpenDialog(textArea) == 
                  JFileChooser.APPROVE_OPTION)
              {
                if (checkRealTimeButton.isSelected())
                  textSpellChecker.stopRealtimeMarkErrors() ;
                
                BufferedReader reader = null ;
                
                try
                {
                  reader = new BufferedReader( 
                    new FileReader(fileChooser.getSelectedFile())) ;
                
                  textArea.read(reader, fileChooser.getSelectedFile()) ;
                  frame.setTitle("Check Speller - " + fileChooser.getSelectedFile()) ;
                }
                catch (IOException ex)
                {
                  JOptionPane.showMessageDialog(textArea, ex.getLocalizedMessage()) ;
                  ex.printStackTrace();
                }
                finally
                {
                  if (null != reader)
                    try
                    {
                      reader.close() ;
                    }
                    catch (IOException e1)
                    {
                      e1.printStackTrace();
                    }
                  
                  System.gc() ;
                  textArea.requestFocusInWindow() ;
                  
                  if (checkRealTimeButton.isSelected())
                    textSpellChecker.startRealtimeMarkErrors(textArea) ;
                }
              }
            }
          }) ;
      
      buttonPanel.add(openButton) ;
      buttonPanel.add(newButton) ;
      buttonPanel.add(checkButton) ;
      buttonPanel.add(checkRealTimeButton) ;
      buttonPanel.add(checkWord) ;
      
	    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE) ;
      frame.addWindowListener(
          new WindowAdapter()
          {
            public void windowClosing(WindowEvent e)
            {
              if (checkRealTimeButton.isSelected())
                textSpellChecker.stopRealtimeMarkErrors() ;
            }
          }) ;
      
	    frame.add(buttonPanel, BorderLayout.NORTH) ;
	    frame.add(new JScrollPane(textArea), BorderLayout.CENTER) ;

	    frame.setSize(640, 280) ;
	    frame.setVisible(true) ;
	    
	    textArea.requestFocusInWindow() ;
    }
    catch(Exception ex)
    {
      ex.printStackTrace() ;
      ErrorMsgBox.show(ex) ;
    }
  }
}
