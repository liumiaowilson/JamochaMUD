/* Launch JamochaMUD after setting the proper path */
'set CLASSPATH=%CLASSPATH%;F:\Programming'

/* Remember to pass along any arguments the user might've defined */
PARSE ARG arguments;
if (arguments <> '') THEN DO;
 arguments = ' '||arguments;
END

/* Check to see if JAVA2_USERHOME has to be set (for new versions of Java) */
testJavaHome = value('JAVA2_USERHOME',,'ENVIRONMENT');
testHome = value('HOME',,'ENVIRONMENT');
if (testJavaHome = '') THEN
DO
    if (testHome <> '') THEN
    DO
        'set JAVA2_USERHOME=%HOME%'
    END
END
/* Attempt to run JamochaMUD via javapm.exe */
/* If you're feeling adventurous, you can add -swing */
/* to the end of the command to use experimental Java2 support */
'E:\JAVA131\JRE\BIN\javaw.exe anecho.JamochaMUD.JMUD'||arguments
exit
