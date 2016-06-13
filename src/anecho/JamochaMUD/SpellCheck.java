/*
 * This is a small class the enable/disable the spell-checker.
 * By separating this class from the main portion of JamochaMUD
 * it is possible to use JamochaMUD with Java 1.4 JVMs even though
 * the spell checker libraries require Java 1.5 or higher.
 * 
 * Of course, the older JVMs can't offer spell-checking in this method
 * but at the same time they let folks with older JVMs still use JamochaMUD.
 */

package anecho.JamochaMUD;

import java.util.zip.ZipFile;

/**
 *
 * @author jeffnik
 */
public class SpellCheck {

    private static final boolean DEBUG = false;
    
    /**
     * 
     * @param DICTTYPE
     * @param state
     * @param dataText
     * @param spellCheckObj
     * @return
     */
    public Object setSpellCheck(int DICTTYPE, boolean state, Object dataText, Object spellCheckObj) {
        
        JMConfig settings = JMConfig.getInstance();
        // Object spellCheckObj = new Object();
            final java.util.prefs.Preferences prefs = java.util.prefs.Preferences.userNodeForPackage(this.getClass());

        final String dictPath = prefs.get("DictFile", "");

//        if (DICTTYPE == ASPELL && DEBUG) {
//            System.err.println("JamochaMUD is attempting to use an Aspell dictionary via jazzy.");
//            System.err.println("This has been disabled at a source-code level.");
//        }

        if (DICTTYPE == DataIn.MYSPELL) {

            if (state) {
                if (DEBUG) {
                    System.err.println("Attempting to initiate myspell dictionary: " + dictPath);
                }

                try {
                    final org.dts.spell.dictionary.SpellDictionary dict = new org.dts.spell.dictionary.OpenOfficeSpellDictionary(new ZipFile(dictPath));
                    final org.dts.spell.SpellChecker checker = new org.dts.spell.SpellChecker(dict);
                    final org.dts.spell.swing.JTextComponentSpellChecker textSpellChecker = new org.dts.spell.swing.JTextComponentSpellChecker(checker);

                    spellCheckObj = textSpellChecker;

                    textSpellChecker.startRealtimeMarkErrors((anecho.gui.JMSwingEntry) dataText);
                } catch (Exception myspellExc) {
                    if (DEBUG) {
                        System.err.println("DataIn error trying to open mySpell dictionary:" + myspellExc);
                    }

                    javax.swing.JFrame baseFrame = (javax.swing.JFrame)MuckMain.getInstance().getMainFrame();
                    
                    // Should really give the user a warning, so that they are aware of what is happening
                    // Spell-checking should also be disabled so, that the warning doesn't occur again.
                    javax.swing.JOptionPane.showMessageDialog(baseFrame,
                            "JamochaMUD was unable to open\n" +
                            "your dictionary file.\n" +
                            "As of verison 2.0 of JamochaMUD\n" +
                            "your dictionary must be a\n" +
                            "MySpell (OpenOffice.org) dictionary.\n" +
                            "Spell-checking is currently disabled.",
                            "Spell-check disabled.",
                            javax.swing.JOptionPane.INFORMATION_MESSAGE);

                    settings.setJMboolean(JMConfig.SPELLCHECK, "false");
                }
            } else {
                // Deactivate spelling
                if (DEBUG) {
                    System.err.println("DataIn disabling mySpell spell checker");
                }

                final org.dts.spell.swing.JTextComponentSpellChecker textSpellChecker = (org.dts.spell.swing.JTextComponentSpellChecker) spellCheckObj;

                if (textSpellChecker != null) {
                    textSpellChecker.stopRealtimeMarkErrors();
                }
            }
        }
        
        return spellCheckObj;
    }
}
