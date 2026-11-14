# Script PowerShell pour compiler et lancer PharmaFind
Write-Host "========================================================" -ForegroundColor Cyan
Write-Host "   PharmaFind - Lancement Local (Lomé, Togo)" -ForegroundColor Cyan
Write-Host "========================================================" -ForegroundColor Cyan

$mvnPath = "C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2025.2.6.3\plugins\maven\lib\maven3\bin\mvn.cmd"
if (-not (Test-Path $mvnPath)) {
    $mvnPath = "mvn"
}

Write-Host "`n[1/2] Compilation Maven du projet..." -ForegroundColor Yellow
& $mvnPath clean package -DskipTests

if ($LASTEXITCODE -ne 0) {
    Write-Host "[ERREUR] Échec de la compilation Maven." -ForegroundColor Red
    exit 1
}

Write-Host "`n[2/2] Démarrage du serveur Spring Boot..." -ForegroundColor Green
Write-Host "Application accessible sur : http://localhost:8080" -ForegroundColor Green
Write-Host "Comptes de test inclus :" -ForegroundColor Yellow
Write-Host " - Admin : admin@pharmafind.tg / Admin123!" -ForegroundColor White
Write-Host " - Pharmacie Golfe : pharmacien.golfe@pharmafind.tg / Pharmacie123!" -ForegroundColor White
Write-Host " - Pharmacie Bé : pharmacien.be@pharmafind.tg / Pharmacie123!`n" -ForegroundColor White

java -jar target\pharmafind-1.0.0.jar
