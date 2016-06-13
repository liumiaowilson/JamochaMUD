/*
 * Filename: pack.cmd
 *   Author: jeffnik
 *  Created: Mon Sep  9 2002
 *  Purpose: Package JamochaMUD into separate byte-code and sourcecode into various zips/tars
 *  Changes: Redone to create separate source and byte-code directories
 */
'@echo off'
call RxFuncAdd 'SysLoadFuncs', 'RexxUtil', 'SysLoadFuncs'
call SysLoadFuncs

/* Set up 'constants' */
beta = 'v1.0'; /* beta level of archive */
/* gzip = 'e:\utility\compress\gzip\' */	/* location of GZIP program */
gzip = '';
/* zip = 'e:\utility\compress\zip\'*/	/* location of ZIP program */
zip = '';
/* tar = 'e:\utility\compress\tar\' */	/* location of TAR program */
tar = '';
jar = 'f:\java142\bin\'                                /* location of Java JAR program */
wicPath = 'e:\bin\warpin'
VAInstallPath = 'e:\bin\java\vainstall' /* location of the VAInstall Java program */
IZPACK_HOME = 'E:\bin\java\IzPack';     /* location of the IzPack Java installer creator */
home = value('HOME',,'OS2ENVIRONMENT')
startDir = directory();			/* directory where we started */
'cd ..';
anechoBaseDir = directory();
'cd '||startDir;

/* Locate WIC.EXE if we need to */
fspec = SysSearchPath('PATH', 'WIC.EXE');
if (fspec == '') then do
    'path=%path%;'||wicPath;
end;

/* parse arg args */
call charout, "Enter version number for archives: >>"
args = LINEIN();
ver = args;

call charout ,"Enter destination drive for packages (eg: C:) >>"
args = LINEIN();

/* dir = directory()||'\..\_backup\'||args; */
base = args||'\JamochaMUD-'||beta||'-'||ver;
sedbase = args||'\\JamochaMUD-'||beta||'-'||ver;

/* package = base||'\anecho' */
src_package = base||'\source\anecho'
bin_package = base||'\binary\anecho'
/* dir = package||'\JamochaMUD'; */
src_dir = src_package||'\JamochaMUD';
bin_dir = bin_package||'\JamochaMUD';
packpath = 'anecho\JamochaMUD'
/* descFile = src_dir||'\zipdesc' */      		/* Zip description file */
descFile = base||'\zipdesc'

say "Creating JamochaMUD.sed file."
sedFile = "JamochaMUD.sed"
WPIVer = backtoforward(ver)
GUIWPIVer = backtoforward(dateToVersion(anechoBaseDir||"\gui"));
EXTRANETWPIVer = backtoforward(dateToVersion(anechoBaseDir||"\extranet"));
LEGACYWPIVer = backtoforward(dateToVersion(anechoBaseDir||"\legacy\JamochaMUD"));

'del '||sedFile
rc = LINEOUT(sedFile, "{", 1)
rc = LINEOUT(sedFile, "s/JMUDLevel/"||beta||"/g")
rc = LINEOUT(sedFile, "s/JMUDVersion/"||ver||"/g")
rc = LINEOUT(sedFile, "s/JMUDWPIVersion/"||WPIVer||"/g")
rc = LINEOUT(sedFile, "s/GUIWPIVersion/"||GUIWPIVer||"/g");
rc = LINEOUT(sedFile, "s/EXTRANETWPIVersion/"||EXTRANETWPIVer||"/g");
rc = LINEOUT(sedFile, "s/LEGACYWPIVersion/"||LEGACYWPIVer||"/g");
rc = LINEOUT(sedFile, "s/PACKARCHIVEPATH/"||sedbase||"/g");
rc = LINEOUT(sedFile, "s/DOTVersion/"||dashToDot(ver)||"/g");
rc = LINEOUT(sedFile, "s/JMUDUnderscoreVersion/"||dashToUnderScore(ver)||"/g");
rc = LINEOUT(sedFile, "}")
/* Now close the file */
rc = LINEOUT(sedFile)

/* Create the directory structures */
say "Creating directories...";
say "base: "||base;
'md 'base
'md 'base||'\source'
'md 'base||'\source\edu'
'md 'base||'\source\edu\stanford'
'md 'base||'\source\edu\stanford\ejalbert'
'md 'src_package
'md 'src_dir
'md 'src_dir||'\plugins'
'md 'src_dir||'\plugins\TriggerDir'
'md 'src_dir||'\plugins\MusicBoxDir'
'md 'src_dir||'\icons'
'md 'src_package||'\gui'
'md 'src_package||'\extranet'
'md 'src_package||'\extranet\event'
'md 'src_package||'\legacy'
'md 'src_package||'\legacy\JamochaMUD'

'md 'base||'\binary'
'md 'base||'\binary\edu'
'md 'base||'\binary\edu\stanford'
'md 'base||'\binary\edu\stanford\ejalbert'
'md 'bin_package
'md 'bin_dir
'md 'bin_dir||'\plugins'
'md 'bin_dir||'\plugins\TriggerDir'
'md 'bin_dir||'\plugins\MusicBoxDir'
'md 'bin_dir||'\icons'
'md 'bin_package||'\gui'
'md 'bin_package||'\extranet'
'md 'bin_package||'\extranet\event'
'md 'bin_package||'\legacy'
'md 'bin_package||'\legacy\JamochaMUD'

/* Copy the class and source files into the new directory */
say "Copying files to: "dir

'copy *.java 'src_dir
'copy *.class 'bin_dir
/* 'copy *.wis 'base */
'copy plugins\*.java 'src_dir||'\plugins'
'copy plugins\*.class 'bin_dir||'\plugins'
'copy plugins\TriggerDir\*.java 'src_dir||'\plugins\TriggerDir'
'copy plugins\TriggerDir\*.class 'bin_dir||'\plugins\TriggerDir'
'copy plugins\MusicBoxDir\*.java 'src_dir||'\plugins\MusicBoxDir'
'copy plugins\MusicBoxDir\*.class 'bin_dir||'\plugins\MusicBoxDir'
'copy ..\gui\*.java 'src_package||'\gui'
'copy ..\gui\*.class 'bin_package||'\gui'
/* 'copy ..\gui\*.properties 'package||'\gui' */
'copy ..\extranet\*.java 'src_package||'\extranet'
'copy ..\extranet\*.class 'bin_package||'\extranet'
'copy ..\extranet\event\*.java 'src_package||'\extranet\event'
'copy ..\extranet\event\*.class 'bin_package||'\extranet\event'
'copy ..\legacy\JamochaMUD\*.java 'src_package||'\legacy\JamochaMUD'
'copy ..\legacy\JamochaMUD\*.class 'bin_package||'\legacy\JamochaMUD'
'copy ..\..\edu\stanford\ejalbert\*.java 'base||'\source\edu\stanford\ejalbert'
'copy ..\..\edu\stanford\ejalbert\*.class 'base||'\binary\edu\stanford\ejalbert'


/* Copy some specific files (license, language bundle, etc.) */
'copy COPYING 'src_dir
'xcopy ..\JamochaMUD\*.properties 'src_package'\JamochaMUD /S'
'xcopy ..\JamochaMUD\*.form 'src_package'\JamochaMUD /S'
'xcopy ..\gui\*.properties 'src_package'\gui /S'
'xcopy ..\extranet\*.properties 'src_package'\extranet /S'
'xcopy ..\JamochaMUD\icons\*.txt 'src_package'\JamochaMUD\icons\ /S'
/* 'copy *.gif 'src_dir
'copy *.png 'src_dir
'copy *.ico 'src_dir
'copy *.gif 'src_dir
*/
'xcopy *.png 'src_dir' /S'
'xcopy *.ico 'src_dir' /S'
'xcopy read.me 'src_dir' /S'
/* 'copy zipdesc 'src_dir */
'copy zipdesc 'base
'copy os2setup.cmd' src_dir||'\unix_os2setup.cmd'
'copy jmud.bat' src_dir

'copy COPYING 'bin_dir
'xcopy ..\JamochaMUD\*.properties 'bin_package'\JamochaMUD /S'
'xcopy ..\gui\*.properties 'bin_package'\gui /S'
'xcopy ..\extranet\*.properties 'bin_package'\extranet /S'
'xcopy ..\JamochaMUD\icons\*.txt 'bin_package'\JamochaMUD\icons\ /S'
/*'copy *.gif 'bin_dir
'copy *.png 'bin_dir
'copy *.ico 'bin_dir */
'xcopy *.gif 'bin_dir' /S'
'xcopy *.png 'bin_dir' /S'
'xcopy *.ico 'bin_dir' /S'
'copy read.me 'bin_dir
/* 'copy zipdesc 'bin_dir */
'copy os2setup.cmd' bin_dir||'\unix_os2setup.cmd'
'copy jmud.bat' bin_dir
'copy Manifest' base;
'copy *.xml' base;


/* Update any files that have to reflect the current version number */
"sed -f JamochaMUD.sed JamochaMUD.txt > "||base||"\JamochaMUD-"||beta||"-"||ver||".txt"
"sed -f JamochaMUD.sed JamochaMUD-Source.txt > "||base||"\JamochaMUD-Source-"||beta||"-"||ver||".txt"
"sed -f JamochaMUD.sed JamochaMUD-wpi.txt > "||base||"\JamochaMUD-wpi-"||beta||"-"||ver||".txt"
"sed -f JamochaMUD.sed JamochaMUD.wis > "||base||"\JamochaMUD.wis"
"sed -f JamochaMUD.sed JamochaMUD.iss > "||base||"\JamochaMUD.iss"
/* "sed -f JamochaMUD.sed JamochaMUD_template.vai > "||base||"\JamochaMUD.vai" */
/* "sed -f JamochaMUD.sed JamochaMUD_filelist_template.txt > "||base||"\JamochaMUD_filelist.txt" */
"sed -f JamochaMUD.sed JamochaMUD-IzPack_template.xml > "||base||"\JamochaMUD-IzPack-"||beta||"-"||ver||".xml";

/* Now remove the test file if we accidentally copied it */
say "If you see an error between here:"

'del 'src_dir||'\Test.java'
'del 'bin_dir||'\Test.class'

/* Get rid of any plug-ins that we don't want as "standard" (because of prereqs, etc.) */
'del 'bin_dir||'\plugins\JazzySpell.class';

say ": and here, it is nothing to worry about."
say ""
say "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-=-=-=-="
say "Now creating ZIP of source..."

/* Now we'll create a ZIP file of the SOURCES from the 'clean' files */
call directory base

/* Kludgy cleanup of the os2setup.cmd */
'u2d '||src_dir||'\unix_os2setup.cmd '||src_dir||'\os2setup.cmd'
'u2d '||bin_dir||'\unix_os2setup.cmd '||bin_dir||'\os2setup.cmd'
'del '||src_dir||'\unix_os2setup.cmd'
'del '||bin_dir||'\unix_os2setup.cmd'

/* zip||'zip -q -r -9 -ll -z < '||descFile||' '||base||'\JamochaMUD-Source-'||beta||'-'||ver||' . -i \*.java -i \*.properties -i \*.cmd -i \*.bat' */
/* 'cmd.exe /c "cd source & '||zip||'zip -q -r -9 -ll -z < '||descFile||' '||base||'\JamochaMUD-Source-'||beta||'-'||ver||' . -i \*"'; */
'cmd.exe /c "cd source & '||zip||'zip -q -r -9 -z < '||descFile||' '||base||'\JamochaMUD-Source-'||beta||'-'||ver||' . -i \*"';

/* Now add the addition COPYing files, etc */

/* zip||'zip -q -g -9 '||base||'\JamochaMUD-Source-'||beta||'-'||ver||' '||packpath||'\COPYING '||packpath||'\kehza.gif '||packpath||'\read.me '||packpath||'\*.ico' */

/* Now we'll create a ZIP file of the BYTE-CODE from the 'clean' files */
say "Creating ZIP file of byte-code..."

/* zip||'zip -q -r -9 -z < '||descFile||' '||base||'\JamochaMUD-'||beta||'-'||ver||' . -i \*.class -i \*.properties -i \*.cmd -i \*.ico -i \*.bat' */
'cmd.exe /c "cd binary & '||zip||'zip -q -r -9 -z < '||descFile||' '||base||'\JamochaMUD-'||beta||'-'||ver||' . -i \*"';

/* Now add the addition COPYing files, etc */
/* zip||'zip -q -g -9 '||base||'\JamochaMUD-'||beta||'-'||ver||' '||packpath||'\COPYING '||packpath||'\kehza.gif '||packpath'\read.me' */

say ""
say "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-=-=-=-="
say ""
/* ===================================== */
/* Create a TAR file (gzip/tar) file of the source */
/* For the source only, we will uncompress our new ZIP, since */
/* it has the non-UNIX LF CR stripped from it, so only makes sense */
say "Creating TAR file of source-code..."

'cmd.exe /c "cd source & '||tar||'tar cf '||base||'\JamochaMUD-Source-'||beta||'-'||ver||'.tar  *.*"';
gzip||'gzip -9 -q -f JamochaMUD-Source-'||beta||'-'||ver||'.tar'

/* Create a TAR (gzip/tar) file of the source */
say ""
say ""
say "Creating TAR file of byte-code..."

'cmd.exe /c "cd binary & '||tar||'tar cf '||base||'\JamochaMUD-'||beta||'-'||ver||'.tar *.*"';
gzip||'gzip -9 -q -f JamochaMUD-'||beta||'-'||ver||'.tar'

say ""
say ""
say "Creating WPI archive of byte-code..."
'cmd.exe /c "cd binary & wic ..\JamochaMUD-wpi-'||beta||'-'||ver||'.wpi -a 1 -r anecho\gui\*.* 2 -r anecho\extranet\*.* 3 -r anecho\JamochaMUD\*.* 4 -r edu\stanford\ejalbert\*.* 5 -r anecho\legacy\JamochaMUD\*.* -s ..\JamochaMUD.wis'

say ""
say ""
say "Creating Java JAR file archive of byte-code..."
/* 'cmd.exe /c "cd binary & '||jar||'jar.exe cvmf ..\Manifest ..\JamochaMUD-'||beta||'-'||ver||'.jar anecho edu"'; */
/* Keep the name of the Jarfile the same for each version to allow easy upgrades */
'cmd.exe /c "cd binary & '||jar||'jar.exe cvmf ..\Manifest ..\JamochaMUD.jar anecho edu"';
say "Signing jar-file..."
'cmd.exe /c "cd binary & '||jar||'jarsigner.exe -keystore '||home||'\anechoKeys ..\JamochaMUD.jar jeffnik"';

say ""
say ""
/*
say "Creating VAInstall of JamochaMUD byte-code..."
'cmd.exe /c "cd binary & java -cp '||VAInstallPath||'\lib\jniregistry.jar;'||VAInstallPath||'\lib\vainstall.jar com.memoire.vainstall.VAInstall ..\JamochaMUD.vai"';
*/
say "Creating IzPack install of JamochaMUD...";
'cmd.exe /c "cd binary & java -jar '||IZPACK_HOME||'/lib/compiler.jar -HOME '||IZPACK_HOME||' '||base||'/JamochaMUD-IzPack-'||beta||'-'||ver||'.xml -b '||base;
say ""
say "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-=-=-=-="
say ""

say "Archiving complete.  A job well done!!"
call charout, "Press <ENTER> to finish"
pull enter

call directory startDir
/* For some reason, with (older versions of) X-workplace, this hangs the WPS */
rc = SysOpenObject(base, DEFAULT, TRUE)
exit

backtoforward: procedure
  arg pathname
  parse var pathname pathname'-'rest
  do while (rest <> "")
    pathname = pathname'\\'rest
    parse var pathname pathname'-'rest
  end
return pathname
    
dashToDot: procedure
    arg origVersion
    newVersion = TRANSLATE(origVersion, '.', '-');
return newVersion
    
dashToUnderScore: procedure
    arg origVersion
    newVersion = TRANSLATE(origVersion, '_', '-');
return newVersion

    /* convert a date to a WPI-usable version number by getting the date
     * of the latest modified file in the supplied directory.  (Actually,
     * we'll limit this to .class and .properties files to try to more
     * accurately reflect what the user gets).
     */
dateToVersion: procedure
    arg packDir;
    
    rc = SysFileTree(packDir||"\*.class","Files.","FSO");
    
    highestDate = 0;
    
    /* Sort through the dates until we find the highest one */
    do i = 1 to Files.0
        say Files.i;
        fileDateTime = Stream(Files.i, 'C', 'QUERY DATETIME');
        PARSE VAR fileDateTime fileDate fileDateTime;
        tempDate = translate('78612345', fileDate, '12345678');
        numDate = SPACE(translate(tempDate, '', '-'), 0);
        if (numDate > highestDate) then do
            highestDate = numDate;
            version = tempDate;
        end;
    end;
    
return version
