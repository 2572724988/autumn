cls 
@ECHO OFF
color 0a 
TITLE Maven ���� For Windows 
GOTO MENU 
:MENU 
CLS
ECHO. 
ECHO. * * * * * Maven ���� For Windows  * * * * *  *
ECHO. * * 
ECHO. * 1 Install *
ECHO. * * 
ECHO. * 2 compile * 
ECHO. * * 
ECHO. * 3 deploy * 
ECHO. * * 
ECHO. * 4 clean * 
ECHO. * * 
ECHO. * 5 package * 
ECHO. * * 
ECHO. * 99 �� �� * 
ECHO. * * 
ECHO. * * * * * * * * * * * * * * * * * * * * * * * * 
ECHO. 
ECHO.�������������ţ� 
set /p ID= 
IF "%id%"=="1" GOTO Install 
IF "%id%"=="2" GOTO compile 
IF "%id%"=="3" GOTO deploy 
IF "%id%"=="4" GOTO clean 
IF "%id%"=="5" GOTO package
IF "%id%"=="99" EXIT 
PAUSE 
:Install 
ECHO. ��ʼ install
@call Install.bat
PAUSE 
GOTO MENU 
:compile 
ECHO. ��ʼ compile
@call compile.bat
PAUSE 
GOTO MENU
:deploy 
ECHO. ��ʼ deploy
@call deploy.bat
PAUSE 
GOTO MENU 
:clean 
ECHO. ��ʼ clean
@call clean.bat
PAUSE 
GOTO MENU 
:package 
ECHO. ��ʼ package
@call package.bat
PAUSE 
GOTO MENU 