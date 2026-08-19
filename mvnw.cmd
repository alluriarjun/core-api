@ECHO OFF
SETLOCAL

SET "BASE_DIR=%~dp0"
SET "WRAPPER_PROPS=%BASE_DIR%.mvn\wrapper\maven-wrapper.properties"

FOR /F "tokens=1,* delims==" %%A IN ('FINDSTR /i "distributionUrl" "%WRAPPER_PROPS%"') DO (
    SET "DIST_URL=%%B"
)
SET "DIST_URL=%DIST_URL: =%"
FOR %%A IN ("%DIST_URL%") DO SET "DIST_FILENAME=%%~nxA"
SET "DIST_DIRNAME=%DIST_FILENAME:.zip=%"

IF "%MAVEN_USER_HOME%"=="" SET "MAVEN_USER_HOME=%USERPROFILE%\.m2"
SET "MAVEN_CACHE=%MAVEN_USER_HOME%\wrapper\dists\%DIST_DIRNAME%"

REM Find mvn.cmd anywhere in the cache subtree (handles zip structures that drop -bin suffix)
SET "MVN_CMD="
FOR /F "delims=" %%A IN ('DIR /B /S "%MAVEN_CACHE%\bin\mvn.cmd" 2^>NUL') DO (
    IF NOT DEFINED MVN_CMD SET "MVN_CMD=%%A"
)

IF NOT DEFINED MVN_CMD (
    ECHO Downloading Maven from %DIST_URL%...
    MKDIR "%MAVEN_CACHE%" 2>NUL
    POWERSHELL -Command "Invoke-WebRequest -Uri '%DIST_URL%' -OutFile '%TEMP%\%DIST_FILENAME%'"
    POWERSHELL -Command "Expand-Archive -Path '%TEMP%\%DIST_FILENAME%' -DestinationPath '%MAVEN_CACHE%' -Force"
    DEL /F /Q "%TEMP%\%DIST_FILENAME%"
    FOR /F "delims=" %%A IN ('DIR /B /S "%MAVEN_CACHE%\bin\mvn.cmd" 2^>NUL') DO (
        IF NOT DEFINED MVN_CMD SET "MVN_CMD=%%A"
    )
)

IF NOT DEFINED MVN_CMD (
    ECHO ERROR: Could not find or download Maven. >&2
    EXIT /B 1
)

CALL "%MVN_CMD%" %*
ENDLOCAL