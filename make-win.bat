@echo off
cls
set path=%path%;C:\Program Files\Java\jdk1.7.0_75\bin
echo --------------------------------------------------------------------------
echo ^| Compiling Main.java for Wolfenjar...
echo --------------------------------------------------------------------------
javac *.java
if ERRORLEVEL == 1 (
  echo --------------------------------------------------------------------------
  echo ^| Check errors.
  echo --------------------------------------------------------------------------
  goto end
) else (
  echo ^| No Errors found. Running .jar...
  echo --------------------------------------------------------------------------
  goto run
)

:run
java Main
goto end

:end
echo.
exit /B