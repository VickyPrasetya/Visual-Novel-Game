# Create PowerShell executable
$content = @"
# 24Hours Visual Novel Game Launcher
Write-Host "Starting 24Hours Visual Novel Game..." -ForegroundColor Green

# Set JavaFX path
`$javafxPath = "C:\DevTools\javafx-sdk-24.0.1\lib"

# Check if JavaFX exists
if (-not (Test-Path `$javafxPath)) {
    Write-Host "Error: JavaFX not found at `$javafxPath" -ForegroundColor Red
    Write-Host "Please install JavaFX SDK" -ForegroundColor Yellow
    pause
    exit 1
}

# Run the game
try {
    java --module-path "`$javafxPath" --add-modules javafx.controls,javafx.fxml,javafx.media,javafx.swing -jar VisualNovelGame.jar
} catch {
    Write-Host "Error running game: `$(`$_.Exception.Message)" -ForegroundColor Red
}

Write-Host "Game finished." -ForegroundColor Green
pause
"@

$content | Out-File -FilePath "24Hours.ps1" -Encoding UTF8

# Convert to EXE
try {
    ps2exe 24Hours.ps1 24Hours.exe
    Write-Host "Executable created: 24Hours.exe" -ForegroundColor Green
} catch {
    Write-Host "Error creating EXE. Using PowerShell script instead." -ForegroundColor Yellow
    Write-Host "Run: powershell -ExecutionPolicy Bypass -File 24Hours.ps1" -ForegroundColor Cyan
}
