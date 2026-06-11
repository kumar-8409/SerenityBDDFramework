#!/usr/bin/env powershell
<#
.SYNOPSIS
    Automated Jenkins Pipeline Job Creator
.DESCRIPTION
    This script waits for Jenkins to be ready and then creates the SerenityBDD pipeline job
.EXAMPLE
    .\setup-jenkins-job.ps1
#>

param(
    [string]$JenkinsURL = "http://localhost:8080",
    [string]$JobName = "SerenityBDDFramework",
    [string]$GitRepository = "https://github.com/kumar-8409/SerenityBDDFramework.git"
)

$ErrorActionPreference = "Continue"

function Write-Status {
    param([string]$Message, [string]$Status = "INFO")
    $timestamp = Get-Date -Format "HH:mm:ss"
    Write-Host "[$timestamp][$Status] $Message"
}

Write-Host ""
Write-Host "╔════════════════════════════════════════════════════════════╗"
Write-Host "║         Jenkins Pipeline Job Automated Setup              ║"
Write-Host "╚════════════════════════════════════════════════════════════╝"
Write-Host ""

# Step 1: Wait for Jenkins to be ready
Write-Status "Checking Jenkins at $JenkinsURL..." "WAIT"

$maxAttempts = 60
$attempt = 0
$jenkinsReady = $false

while ($attempt -lt $maxAttempts) {
    $attempt++
    Write-Host -NoNewline "."
    
    try {
        $response = Invoke-WebRequest -Uri "$JenkinsURL/" -TimeoutSec 3 -ErrorAction SilentlyContinue
        if ($response.StatusCode -eq 200 -or $response.StatusCode -eq 403) {
            $jenkinsReady = $true
            break
        }
    } catch {
        # Still initializing
    }
    
    Start-Sleep -Seconds 2
}

Write-Host ""
Write-Host ""

if (-not $jenkinsReady) {
    Write-Status "Jenkins is not responding. Please check:" "ERROR"
    Write-Host "  1. Jenkins process: tasklist | findstr java"
    Write-Host "  2. Port 8080: netstat -ano | findstr :8080"
    Write-Host "  3. Jenkins logs: Get-Content C:\jenkins\jenkins.log -Tail 50"
    exit 1
}

Write-Status "✓ Jenkins is online" "SUCCESS"

# Step 2: Get initial admin password
Write-Host ""
Write-Status "Looking for initial admin password..." "INFO"

$secretFile = "C:\jenkins\secrets\initialAdminPassword"
$adminPassword = $null

if (Test-Path $secretFile) {
    $adminPassword = Get-Content $secretFile -Raw
    Write-Status "✓ Found admin password" "SUCCESS"
} else {
    Write-Status "⚠ Initial admin password not found yet" "WARNING"
    Write-Host "  This may be needed for Jenkins setup"
    Write-Host "  Location: $secretFile"
}

# Step 3: Create job configuration XML
Write-Host ""
Write-Status "Creating job configuration..." "INFO"

$jobConfigXml = @"
<?xml version='1.1' encoding='UTF-8'?>
<flow-definition plugin="workflow-job@1252.v29ee94b_4b_5f7">
  <actions/>
  <description>Serenity BDD Framework - Automated Testing Pipeline</description>
  <keepDependencies>false</keepDependencies>
  <properties>
    <hudson.model.ParametersDefinitionProperty>
      <parameterDefinitions>
        <hudson.model.StringParameterDefinition>
          <name>BROWSER</name>
          <description>Browser to run tests with (chrome|firefox)</description>
          <defaultValue>chrome</defaultValue>
          <trim>false</trim>
        </hudson.model.StringParameterDefinition>
        <hudson.model.BooleanParameterDefinition>
          <name>HEADLESS</name>
          <description>Run browser in headless mode</description>
          <defaultValue>true</defaultValue>
        </hudson.model.BooleanParameterDefinition>
      </parameterDefinitions>
    </hudson.model.ParametersDefinitionProperty>
  </properties>
  <definition class="org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition" plugin="workflow-cps@2795.v2b_5e9644b_07c">
    <scm class="hudson.plugins.git.GitSCM" plugin="git@5.2.0">
      <configVersion>2</configVersion>
      <userRemoteConfigs>
        <hudson.plugins.git.UserRemoteConfig>
          <url>$GitRepository</url>
          <credentialsId></credentialsId>
        </hudson.plugins.git.UserRemoteConfig>
      </userRemoteConfigs>
      <branches>
        <hudson.plugins.git.BranchSpec>
          <name>*/main</name>
        </hudson.plugins.git.BranchSpec>
      </branches>
      <doGenerateSubmoduleConfigurations>false</doGenerateSubmoduleConfigurations>
      <submoduleCfg class="java.util.ArrayList"/>
      <extensions/>
    </scm>
    <scriptPath>Jenkinsfile</scriptPath>
    <lightweight>false</lightweight>
  </definition>
  <triggers/>
  <disabled>false</disabled>
</flow-definition>
"@

$configFile = "C:\jenkins\job-config.xml"
$jobConfigXml | Out-File -FilePath $configFile -Encoding UTF8
Write-Status "✓ Job configuration created at: $configFile" "SUCCESS"

# Step 4: Attempt to create job via API or Jenkins CLI
Write-Host ""
Write-Status "Attempting to create job..." "INFO"

# Try using Jenkins API
try {
    $createJobUrl = "$JenkinsURL/createItem?name=$JobName"
    
    $headers = @{
        "Content-Type" = "application/xml"
    }
    
    $response = Invoke-WebRequest -Uri $createJobUrl `
        -Method POST `
        -Body $jobConfigXml `
        -Headers $headers `
        -TimeoutSec 10 `
        -ErrorAction SilentlyContinue
    
    if ($response.StatusCode -eq 200) {
        Write-Status "✓ Job created successfully via Jenkins API" "SUCCESS"
    } else {
        Write-Status "Job creation response: $($response.StatusCode)" "WARNING"
    }
} catch {
    Write-Status "Could not create job via API: $($_.Exception.Message)" "WARNING"
    Write-Status "You may need to create the job manually" "INFO"
}

# Step 5: Display summary and next steps
Write-Host ""
Write-Host "╔════════════════════════════════════════════════════════════╗"
Write-Host "║                    Setup Complete                         ║"
Write-Host "╚════════════════════════════════════════════════════════════╝"
Write-Host ""
Write-Status "Jenkins URL: $JenkinsURL" "INFO"
Write-Status "Job Name: $JobName" "INFO"
Write-Status "Repository: $GitRepository" "INFO"
Write-Host ""
Write-Host "NEXT STEPS:"
Write-Host ""
Write-Host "1. Open Jenkins: $JenkinsURL"
Write-Host ""
Write-Host "2. If this is first time:"
Write-Host "   - Unlock Jenkins using the initial admin password"
Write-Host "   - Install suggested plugins"
Write-Host "   - Create admin user account"
Write-Host ""
Write-Host "3. If job was not created automatically:"
Write-Host "   - Go to: $JenkinsURL/newJob"
Write-Host "   - Create Pipeline job manually"
Write-Host "   - Use configuration from: $configFile"
Write-Host ""
Write-Host "4. Run the pipeline:"
Write-Host "   - Click on $JobName"
Write-Host "   - Click 'Build Now'"
Write-Host "   - View console output"
Write-Host ""
Write-Host "5. View test reports:"
Write-Host "   - Reports: target/site/serenity/index.html"
Write-Host "   - Logs: $JenkinsURL/job/$JobName/lastBuild/console"
Write-Host ""
Write-Host "════════════════════════════════════════════════════════════"
Write-Host ""

Write-Status "Setup script completed" "SUCCESS"
