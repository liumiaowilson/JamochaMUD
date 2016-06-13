/* Setup the JamochaMUD Icon on the OS/2 desktop */
/* This script will create an icon on the user's desktop which */
/* in turn calls a REXX CMD file to set up the environment and */
/* launch JamochaMUD.  The big thing is to ensure that our     */
/* classpath is setup right in the .cmd file                   */

obID = '<JamochaMUDBeta>'	/* The object ID */
obID2 = '<JamochaMUDBetaSingle>'        /* Object ID for single user */
title = 'JamochaMUD v1.0'	/* Title for the desktop icon */
title2 = 'JamochaMUD v1.0 (Single User)'
startFile = 'anecho.JamochaMUD.JMUD' 		/* Name of the file JavaPM has to run */
iconFile = 'JMUD.ico'	/* JamochaMUD icon file */
cmdFile = 'JMUD.cmd'            /* The .cmd file to start JamochaMUD */

/* rc = RxFuncAdd("SysCreateObject", "RexxUtil", "SysCreateObject") */
/* rc = RxFuncAdd("SysSearchPath", "RexxUtil", "SysSearchPath") */

/* Load RexxUtil Library */
If RxFuncQuery('SysLoadFuncs') Then
    Do
        Call RxFuncAdd 'SysLoadFuncs', 'RexxUtil', 'SysLoadFuncs'
        Call RxFuncAdd 'SysIni','RexxUtil','SysIni'
        Call SysLoadFuncs
        End



/* Search for different versions of Java (the executables have different names */
/* These executables should be listed from least to most desirable for running JamochaMUD */
javaLocation = "";

progName.1 = "java.exe";
progName.2 = "jre.exe";
progName.3 = "javapm.exe";
progName.4 = "javaw.exe";
progName.0 = 4;

do i=1 to progName.0
    tempLoc = SysSearchPath('PATH', progName.i);
    if (tempLoc <> "") then do
        javaLocation = tempLoc;
    END
END

/* Check specifically for the 1.4.2 Innotek runtime */
tempValue = SysIni('USER', 'OS2 Kit for Java SDK','Path');
keyValue = STRIP(tempValue, 'B', '0'x);     /* Remove any extra spaces */
if (keyValue <> '') THEN DO
    tempJavaLocation = keyValue||"\bin\javaw.exe";
    Call SysFileTree tempJavaLocation, "files.", "F";
    if (files.0 == 1) THEN DO
        javaLocation = tempJavaLocation;
    END
    
END

/* Check to see if this script is being run from WarpIN or stand-alone
 * Warpin will pass the argument "warpin" where stand-alone will
 * have no argument
 */
if (arg() > 0) THEN DO
    /* Do a WarpIN install */
    /* JMUDLocation = directory()||'\anecho\JamochaMUD' */  /* The directory where JamochaMUD "lives" */
    JMUDLocation = arg(1)||'\anecho\JamochaMUD'  /* The directory where JamochaMUD "lives" */
    javaClassPath = arg(1);
    cd '..\..'
END
ELSE DO
    /* Do a stand-alone install */
    cd '..\..'
    JMUDLocation = directory()||'\anecho\JamochaMUD'  /* The directory where JamochaMUD "lives" */
    javaClassPath = directory() /* This is the directory that must be added to the CLASSPATH */
END

/* Erase any existing versions of JMUD.CMD if they already exist */
rc = SysFileDelete(JMUDLocation||'\JMUD.CMD');

/* Return to where we started */
cd JMUDLocation

/* say "Is this our classpath for anecho.*? "||javaClasspath */

If(javaLocation = "") Then
  Do
    Say "Unable to find a suitable Java environment (by myself)"
    Say ""
    Exit(1)
  End

/* Try to write out a startup .cmd file */
rc = LINEOUT(cmdFile, "/* Launch JamochaMUD after setting the proper path */", 1)
rc = LINEOUT(cmdFile, "'set CLASSPATH=%CLASSPATH%;"||javaClassPath||"'")
rc = LINEOUT(cmdFile, "");
rc = LINEOUT(cmdFile, "/* Remember to pass along any arguments the user might've defined */");
rc = LINEOUT(cmdFile, "PARSE ARG arguments;");
rc = LINEOUT(cmdFile, "if (arguments <> '') THEN DO;");
rc = LINEOUT(cmdFile, " arguments = ' '||arguments;");
rc = LINEOUT(cmdFile, "END");
rc = LINEOUT(cmdFile, "");
rc = LINEOUT(cmdFile, "/* Check to see if JAVA2_USERHOME has to be set (for new versions of Java) */");
rc = LINEOUT(cmdFile, "testJavaHome = value('JAVA2_USERHOME',,'ENVIRONMENT');");
rc = LINEOUT(cmdFile, "testHome = value('HOME',,'ENVIRONMENT');");
rc = LINEOUT(cmdFile, "if (testJavaHome = '') THEN");
rc = LINEOUT(cmdFile, "DO");
rc = LINEOUT(cmdFile, "    if (testHome <> '') THEN");
rc = LINEOUT(cmdFile, "    DO");
rc = LINEOUT(cmdFile, "        'set JAVA2_USERHOME=%HOME%'");
rc = LINEOUT(cmdFile, "    END");
rc = LINEOUT(cmdFile, "END");


/* This bit runs JamochaMUD */
rc = LINEOUT(cmdFile, "/* Attempt to run JamochaMUD via javapm.exe */")
rc = LINEOUT(cmdFile, "/* If you're feeling adventurous, you can add -swing */");
rc = LINEOUT(cmdFile, "/* to the end of the command to use experimental Java2 support */");
rc = LINEOUT(cmdFile, "'"||javaLocation||" "||startFile||"'||arguments");
/* Now close the file */
rc = LINEOUT(cmdFile, "exit");

/* Set up the parameters for the program */
/* params = "OBJECTID="||obID||";EXENAME=" || JMUDLocation || "\" || cmdFile || ";PROGTYPE=PM;STARTUPDIR=" || Directory() || ";PARAMETERS="||startFile||";ICONFILE="||Directory()||"\"||iconFile */
params = "OBJECTID="||obID||";EXENAME=" || JMUDLocation || "\" || cmdFile || ";PROGTYPE=PM;STARTUPDIR=" || Directory() || ";PARAMETERS= -nobf;ICONFILE="||Directory()||"\"||iconFile

/* Create the desktop icon */
rc = SysCreateObject("WPProgram", title, "<WP_DESKTOP>", params, "U");

If(rc = 0) Then
  Do
    Say "Unable to create the JamochaMUD program object"
    Say ""
  End

/* params = "OBJECTID="||obID2||";EXENAME=" || JMUDLocation || "\" || cmdFile || ";PROGTYPE=PM;STARTUPDIR=" || Directory() || ";PARAMETERS="||startFile||" -s;ICONFILE="||Directory()||"\"||iconFile */
params = "OBJECTID="||obID2||";EXENAME=" || JMUDLocation || "\" || cmdFile || ";PROGTYPE=PM;STARTUPDIR=" || Directory() || ";PARAMETERS= -s -nobg;ICONFILE="||Directory()||"\"||iconFile

/* Create the desktop icon */
rc = SysCreateObject("WPProgram", title2, "<WP_DESKTOP>", params, "U");

If(rc = 0) Then
  Do
    Say "Unable to create the JamochaMUD single-user program object"
    Say ""
  End

