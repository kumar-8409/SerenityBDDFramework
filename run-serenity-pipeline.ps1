param(
    [string]$Browser = "chrome",
    [bool]$Headless = $true
)

$ErrorActionPreference = "Continue"
$ProjectRoot = "d:\serenity-bdd-framework"
$MavenCmd = "C:\maven\bin\mvn.cmd"

Write-Host ""
Write-Host "╔════════════════════════════════════════════════════════════╗"
Write-Host "║   SERENITY BDD FRAMEWORK - JENKINS PIPELINE SIMULATOR     ║"
Write-Host "╚════════════════════════════════════════════════════════════╝"
Write-Host ""
Write-Host "[INFO] Pipeline Parameters:"
Write-Host "[INFO]   - BROWSER: $Browser"
Write-Host "[INFO]   - HEADLESS: $Headless"
Write-Host ""

# Stage 1: Checkout
Write-Host "[INFO] ========================================"
Write-Host "[INFO] STAGE: Checkout"
Write-Host "[INFO] ========================================"
Push-Location $ProjectRoot
$branch = & git symbolic-ref --short HEAD 2>$null
$commit = & git rev-parse --short HEAD 2>$null
Write-Host "[INFO] Branch: $branch"
Write-Host "[INFO] Commit: $commit"
Write-Host ""

# Stage 2: Selenium
Write-Host "[INFO] ========================================"
Write-Host "[INFO] STAGE: Start Selenium Container"
Write-Host "[INFO] ========================================"
Write-Host "[INFO] Browser: $Browser"
Write-Host "[INFO] Headless: $Headless"
Write-Host "[SUCCESS] Selenium preparation complete"
Write-Host ""

# Stage 3: Build and Test
Write-Host "[INFO] ========================================"
Write-Host "[INFO] STAGE: Build Test"
Write-Host "[INFO] ========================================"
Write-Host "[INFO] Running Maven clean verify..."
Write-Host ""

$startTime = Get-Date
& $MavenCmd -B -DskipTests=false clean verify
$exitCode = $LASTEXITCODE
$endTime = Get-Date
$duration = ($endTime - $startTime).TotalSeconds

Write-Host ""
Write-Host "[INFO] Build Duration: $duration seconds"
Write-Host ""

# Results
Write-Host "[INFO] ========================================"
Write-Host "[INFO] POST ACTIONS"
Write-Host "[INFO] ========================================"

$reportPath = "$ProjectRoot\target\site\serenity\index.html"
if (Test-Path $reportPath) {
    Write-Host "[SUCCESS] Serenity Report: $reportPath"
}

$failsafePath = "$ProjectRoot\target\failsafe-reports"
if (Test-Path $failsafePath) {
    Write-Host "[SUCCESS] Failsafe Reports: $failsafePath"
}

Write-Host ""
Write-Host "[INFO] ╔════════════════════════════════════════════════════════════╗"
Write-Host "[INFO] ║                   PIPELINE COMPLETED                       ║"
Write-Host "[INFO] ╚════════════════════════════════════════════════════════════╝"
Write-Host "[INFO] Total Pipeline Duration: $duration seconds"
Write-Host "[INFO] Pipeline Status: $(if ($exitCode -eq 0) { 'SUCCESS' } else { 'FAILURE' })"
Write-Host ""
Write-Host "[INFO] Next Steps:"
Write-Host "[INFO]   1. View Serenity Reports in browser"
Write-Host "[INFO]   2. Check test failures if any"
Write-Host "[INFO]   3. Deploy or proceed with next stages"
Write-Host ""

exit $exitCode
