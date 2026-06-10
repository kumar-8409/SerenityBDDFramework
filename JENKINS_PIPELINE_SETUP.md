# Jenkins Pipeline Setup Guide

## Overview
This document describes how to set up and configure a Jenkins pipeline for the Serenity BDD Framework.

## Pipeline Execution Summary

### Last Run (2026-06-10)
- **Status**: EXECUTED ✓
- **Duration**: 1 minute 42 seconds
- **Total Test Cases**: 10
- **Tests Passed**: 7
- **Tests Failed**: 2
- **Tests with Errors**: 1

### Test Results
```
SERENITY TESTS SUMMARY:
├── Test scenarios executed: 8
├── Total Test cases executed: 10
├── Tests passed: 7
├── Tests failed: 2
├── Tests with errors: 1
├── Total Duration: 41s 265ms
├── Fastest test: 3s 921ms
└── Slowest test: 9s 542ms
```

## Pipeline Architecture

The pipeline is defined in `Jenkinsfile` and consists of the following stages:

### Stage 1: Checkout
- Clones the repository from GitHub
- Branch: `main`
- Repository: `https://github.com/kumar-8409/SerenityBDDFramework.git`

### Stage 2: Start Selenium (Optional)
- Starts a Selenium standalone Chrome container
- Configured to run when `BROWSER=chrome`
- Uses Docker image: `selenium/standalone-chrome:115.0`
- Shared memory: 2GB

### Stage 3: Build & Test
- Runs Maven clean verify
- Executes all Serenity BDD tests
- Generates test reports
- Command: `mvn -B -DskipTests=false clean verify`

### Post Actions
- Publishes JUnit test results
- Archives Serenity reports
- Archives Cucumber reports
- Stops Selenium container (if started)

## Pipeline Parameters

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| BROWSER | String | chrome | Browser to run tests with (chrome\|firefox) |
| HEADLESS | Boolean | true | Run browser in headless mode |

## Installation Instructions

### Option 1: Using Jenkins Server (Docker)

#### Prerequisites
- Docker installed
- Docker Compose (optional)

#### Steps

1. **Start Jenkins with Docker**
```bash
docker run -d -p 8080:8080 -p 50000:50000 \
  -v jenkins_home:/var/jenkins_home \
  -v /var/run/docker.sock:/var/run/docker.sock \
  jenkins/jenkins:lts
```

2. **Initialize Jenkins**
   - Open `http://localhost:8080` in your browser
   - Get initial admin password:
   ```bash
   docker exec <container-id> cat /var/jenkins_home/secrets/initialAdminPassword
   ```

3. **Create Pipeline Job**
   - New Item → Pipeline
   - Name: `SerenityBDDFramework`
   - Definition: Pipeline script from SCM
   - SCM: Git
   - Repository URL: `https://github.com/kumar-8409/SerenityBDDFramework.git`
   - Script Path: `Jenkinsfile`

4. **Configure Build Parameters**
   - BROWSER: chrome (default)
   - HEADLESS: true (default)

5. **Run Pipeline**
   - Click "Build Now"
   - View console output and reports

### Option 2: Using Pipeline Simulator (Local Testing)

Run the pipeline simulator script locally for testing:

```powershell
cd d:\serenity-bdd-framework
Set-ExecutionPolicy -ExecutionPolicy Bypass -Scope Process -Force
.\run-serenity-pipeline.ps1 -Browser chrome -Headless $true
```

### Option 3: Traditional Jenkins Installation (Windows)

#### Prerequisites
- Java 11 or higher installed
- Maven 3.8+ installed
- Git installed

#### Steps

1. **Download and Install Jenkins**
   ```powershell
   $url = "https://get.jenkins.io/war-stable/2.452.3/jenkins.war"
   Invoke-WebRequest -Uri $url -OutFile "jenkins.war"
   ```

2. **Run Jenkins**
   ```powershell
   java -jar jenkins.war --httpPort=8080
   ```

3. **Access Jenkins**
   - Open `http://localhost:8080`
   - Complete the setup wizard
   - Install recommended plugins

4. **Create Pipeline Job**
   - New Item → Pipeline
   - Configure as described in Option 1, steps 3-5

## Jenkinsfile Content

The `Jenkinsfile` contains:

```groovy
pipeline {
    agent {
        docker {
            image 'maven:3.8.8-openjdk-11'
            args '-v /root/.m2:/root/.m2 -v /dev/shm:/dev/shm'
        }
    }

    parameters {
        string(name: 'BROWSER', defaultValue: 'chrome', description: 'Browser to run tests with')
        booleanParam(name: 'HEADLESS', defaultValue: true, description: 'Run browser in headless mode')
    }

    environment {
        MAVEN_OPTS = '-Xmx1024m'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Start Selenium (optional)') {
            when {
                expression { return params.BROWSER == 'chrome' }
            }
            steps {
                script {
                    seleniumContainer = docker.image('selenium/standalone-chrome:115.0').run(...)
                }
            }
        }

        stage('Build & Test') {
            steps {
                sh 'mvn -B -DskipTests=false clean verify'
            }
        }
    }

    post {
        always {
            junit allowEmptyResults: true, testResults: '...'
            archiveArtifacts artifacts: '...'
            // Stop Selenium container
        }
    }
}
```

## Test Reports Location

After pipeline execution, reports are available at:

- **Serenity Report**: `target/site/serenity/index.html`
- **Failsafe Reports**: `target/failsafe-reports/`
- **Surefire Reports**: `target/surefire-reports/`

## Prerequisites for Jenkins Execution

### Required Plugins
- Pipeline
- Git
- Docker Pipeline
- Junit Plugin
- Gradle Plugin (optional)
- Timestamper
- AnsiColor

### System Requirements
- CPU: 2+ cores
- RAM: 2GB minimum (4GB recommended)
- Disk: 10GB free space
- Docker (for containerized agents)

## Troubleshooting

### Issue: Tests Fail with Timeout
**Solution**: Increase Maven timeout in environment:
```groovy
environment {
    MAVEN_OPTS = '-Xmx1024m -XX:+UseG1GC'
}
```

### Issue: Selenium Connection Failed
**Solution**: Ensure Docker is running and Selenium container is available:
```bash
docker ps
docker pull selenium/standalone-chrome:115.0
```

### Issue: GitHub Access Denied
**Solution**: Configure GitHub credentials in Jenkins:
1. Manage Jenkins → Credentials
2. Add credentials (Personal Access Token or SSH key)
3. Configure job to use credentials

### Issue: Maven Build Out of Memory
**Solution**: Increase heap size:
```groovy
environment {
    MAVEN_OPTS = '-Xmx2048m'
}
```

## CI/CD Integration

### GitHub Actions Alternative
Create `.github/workflows/serenity-tests.yml`:
```yaml
name: Serenity BDD Tests
on: [push, pull_request]
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - uses: actions/setup-java@v2
        with:
          java-version: '11'
      - run: mvn -B clean verify
```

### GitLab CI Alternative
Create `.gitlab-ci.yml`:
```yaml
test:
  image: maven:3.8.8-openjdk-11
  script:
    - mvn -B clean verify
  artifacts:
    paths:
      - target/
```

## Monitoring and Metrics

### Key Metrics to Track
- Build Success Rate
- Test Pass Rate
- Build Duration Trend
- Test Execution Time

### Generate Reports
Reports are automatically generated by Serenity:
- HTML reports with screenshots
- JSON reports for integration
- Excel reports

## Next Steps

1. **Set up Jenkins Server**: Use Docker or traditional installation
2. **Configure Git Credentials**: For GitHub repository access
3. **Install Required Plugins**: Pipeline, Docker, Git, JUnit
4. **Create Pipeline Job**: Link to this repository's Jenkinsfile
5. **Set Build Parameters**: Configure BROWSER and HEADLESS options
6. **Run Initial Build**: Trigger build and verify reports
7. **Configure Webhooks**: For automatic builds on push

## Additional Resources

- [Jenkins Official Documentation](https://www.jenkins.io/doc/)
- [Serenity BDD Documentation](https://serenity-bdd.info/)
- [Jenkinsfile Reference](https://www.jenkins.io/doc/book/pipeline/jenkinsfile/)
- [Docker Hub - Jenkins](https://hub.docker.com/r/jenkins/jenkins)

## Support

For issues or questions:
1. Check Serenity reports in `target/site/serenity/`
2. Review Jenkins console output
3. Check Maven build logs
4. Refer to test failure reports in `target/failsafe-reports/`
