@echo off
setlocal
cd /d "%~dp0"
title Student Management API (port 8081)
echo =====================================================
echo   Student Profile and Skill Development Management
echo =====================================================
echo.

where java >nul 2>&1
if errorlevel 1 goto nojava

echo Checking MySQL on port 3306...
powershell -NoProfile -Command "if (Test-NetConnection -ComputerName 127.0.0.1 -Port 3306 -InformationLevel Quiet -WarningAction SilentlyContinue) { exit 0 } else { exit 1 }"
if errorlevel 1 goto nomysql
echo MySQL is running.
echo.

if exist "student-management.jar" goto run

echo First run: building the application. This needs internet and takes a few minutes.
set "MVN="
where mvn >nul 2>&1
if not errorlevel 1 set "MVN=mvn"
if defined MVN goto build

if exist ".maven\apache-maven-3.9.6\bin\mvn.cmd" goto havemaven
echo Maven not found. Downloading Maven...
powershell -NoProfile -ExecutionPolicy Bypass -Command "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri 'https://archive.apache.org/dist/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.zip' -OutFile 'maven.zip'; Expand-Archive -Path 'maven.zip' -DestinationPath '.maven' -Force"
if exist maven.zip del maven.zip
:havemaven
if not exist ".maven\apache-maven-3.9.6\bin\mvn.cmd" goto nomaven
set "MVN=.maven\apache-maven-3.9.6\bin\mvn.cmd"

:build
call "%MVN%" clean package -DskipTests
if errorlevel 1 goto buildfail
copy /Y "target\student-management.jar" "student-management.jar" >nul
if not exist "student-management.jar" goto buildfail
echo.
echo Build successful.
echo.

:run
echo Starting application on http://localhost:8081 ...
echo (Keep this window open. Press CTRL+C to stop.)
echo.
java -jar student-management.jar
echo.
echo Application stopped.
pause
exit /b 0

:nojava
echo ERROR: Java was not found. Install JDK 17 or newer and try again.
pause
exit /b 1

:nomysql
echo ERROR: MySQL is not running on port 3306.
echo Open the XAMPP Control Panel, click Start next to MySQL, then run START.bat again.
pause
exit /b 1

:nomaven
echo ERROR: Could not download Maven. Check your internet connection and try again.
pause
exit /b 1

:buildfail
echo.
echo ERROR: Build failed. Make sure you have JDK 17 or newer, not just a JRE, and internet access.
echo Copy the error text above and send it for help.
pause
exit /b 1
