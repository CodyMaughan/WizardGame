rem save the current directory
set olddir=%CD%
rem delete old directories
cd %3\historicalBuilds
forfiles /S /D -2 /c "cmd /c rd /s /q @path"
rem go to the main repository directory
cd %4
git pull -q origin master
rem go back to the original directory
chdir /d %olddir%
rem delete the .class files
del *.class
rem compile the code to create the .class files
javac %1.java
rem create the JAR file
jar -cvfe %2.jar %1 *.class resources
rem get the current date and time
@for /f "tokens=2 delims==" %%a in ('wmic OS Get localdatetime /value') do set "dt=%%a"
@set "YY=%dt:~2,2%" & set "YYYY=%dt:~0,4%" & set "MM=%dt:~4,2%" & set "DD=%dt:~6,2%"
@set "HH=%dt:~8,2%" & set "Min=%dt:~10,2%" & set "Sec=%dt:~12,2%"
@set "datestamp=%YYYY%%MM%%DD%" & set "timestamp=%HH%%Min%%Sec%"
@set "fullstamp=%YYYY%-%MM%-%DD%_%HH%-%Min%-%Sec%"
@echo Output directory = %3\historicalBuilds\%fullstamp%
rem create the directories for the jar file
md %3\historicalBuilds\%fullstamp%
md %3\masterBuild
rem copy the jar file to the directories
copy %2.jar %3\historicalBuilds\%fullstamp%
copy /Y %2.jar %3\masterBuild
@echo @java -jar %2.jar > %3\masterBuild\run_%2.bat

