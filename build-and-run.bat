@echo off
echo ========================================================
echo   Lancement de PharmaHelp (Lome, Togo) - Mode Local
echo ========================================================

where java >nul 2>nul
if %errorlevel% neq 0 (
    echo [ERREUR] Java 21 n'est pas accessible dans votre variable PATH.
    pause
    exit /b 1
)

set MVN_CMD="C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2025.2.6.3\plugins\maven\lib\maven3\bin\mvn.cmd"
if not exist %MVN_CMD% (
    set MVN_CMD=mvn
)

echo [1/2] Compilation et verification de l'application...
call %MVN_CMD% clean package -DskipTests
if %errorlevel% neq 0 (
    echo [ERREUR] Echec de la compilation Maven.
    pause
    exit /b 1
)

echo [2/2] Demarrage de PharmaHelp sur http://localhost:8080 ...
echo Appuyez sur Ctrl+C pour arreter le serveur.
java -jar target\pharmahelp-1.0.0.jar
pause
