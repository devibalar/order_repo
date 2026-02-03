@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements.  See the NOTICE file
@REM distributed with this work for additional information
@REM regarding copyright ownership.  The ASF licenses this file
@REM to you under the Apache License, Version 2.0 (the
@REM "License"); you may not use this file except in compliance
@REM with the License.  You may obtain a copy of the License at
@REM
@REM    http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing,
@REM software distributed under the License is distributed on an
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM KIND, either express or implied.  See the License for the
@REM specific language governing permissions and limitations
@REM under the License.
@REM

@if "%DEBUG%" == "" @echo off
@setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%

@REM Resolve any "." and ".." in APP_HOME to make it shorter.
for %%i in ("%APP_HOME%") do set APP_HOME=%%~fi

@REM Add default JVM options here. You can also use JAVA_OPTS and MAVEN_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS="-Xmx64m" "-Xms64m"

@REM Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >nul 2>&1
if "%ERRORLEVEL%" == "0" goto execute

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=C:\Users\sajus\Documents\zulu17.64.15-ca-jdk17.0.18-win_x64\
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto execute

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:execute
@REM Setup the command line

set CLASSPATH=%APP_HOME%\maven\wrapper\maven-wrapper.jar

set WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain

@REM Execute Maven
"%JAVA_EXE%" ^
  %DEFAULT_JVM_OPTS% ^
  -classpath "%CLASSPATH%" ^
  "-Dmaven.home=%APP_HOME%\maven" ^
  "-Dmaven.multiModuleProjectDirectory=%APP_HOME%" ^
  %MAVEN_OPTS% %MAVEN_DEBUG_OPTS% ^
  -Dorg.slf4j.simpleLogger.defaultLogLevel=info ^
  "%WRAPPER_LAUNCHER%" %*

:end
@endlocal & goto errorlevel_exit

:errorlevel_exit
@if %ERRORLEVEL% neq 0 goto error
@endlocal
exit /b %ERRORLEVEL%

:error
@endlocal
@echo An error occurred during execution of Maven. Exit code: %1
exit /b %1
