# Jenkins Setup - Quick Start Guide

## Current Status
✓ **Jenkins is running on http://localhost:8080**

## Step 1: Access Jenkins Web Interface

1. Open your browser and go to: **http://localhost:8080**
2. Wait for Jenkins to fully initialize (this may take 2-5 minutes on first startup)
3. You should see the Jenkins Unlock page

## Step 2: Unlock Jenkins

1. Jenkins will display a message asking for the "Administrator password"
2. The password is stored at: `C:\jenkins\secrets\initialAdminPassword`
3. Copy and paste the password into the field
4. Click "Continue"

## Step 3: Install Suggested Plugins

1. Click "Install suggested plugins"
2. Wait for plugins to install (this takes 5-10 minutes)
3. Create a new admin user account
4. Save and continue

## Step 4: Create Pipeline Job

### Method A: Manual (UI)

1. Click "New Item" or "Create a job"
2. Enter job name: **SerenityBDDFramework**
3. Select **Pipeline**
4. Click **OK**
5. Scroll down to "Pipeline" section
6. Select: **Pipeline script from SCM**
7. SCM: **Git**
8. Repository URL: `https://github.com/kumar-8409/SerenityBDDFramework.git`
9. Branch: `*/main`
10. Script path: `Jenkinsfile`
11. Click **Save**

### Method B: Automated (Jenkins CLI)

Once Jenkins is ready, run this command:

```powershell
java -jar jenkins-cli.jar -s http://localhost:8080 create-job SerenityBDDFramework < job-config.xml
```

## Step 5: Configure Build Parameters

In the job configuration, add under "Build Parameters":

1. **BROWSER** (String parameter)
   - Default value: `chrome`
   - Description: `Browser to run tests with (chrome|firefox)`

2. **HEADLESS** (Boolean parameter)
   - Default value: `true`
   - Description: `Run browser in headless mode`

## Step 6: Run the Pipeline

1. Click on **SerenityBDDFramework** job
2. Click **"Build Now"**
3. You should see a new build starting in the Build History
4. Click on the build number to see the console output
5. Wait for the build to complete

## Step 7: View Test Reports

After the build completes:

1. Click **"Serenity Report"** (if available) to see detailed test results
2. Or navigate to: `d:\serenity-bdd-framework\target\site\serenity\index.html`
3. View test results, screenshots, and execution details

## Expected Results

- Build Duration: ~2 minutes
- Test Results: 
  - Total Tests: 10
  - Expected Pass Rate: 70-80%
- Reports Generated in `target/site/serenity/`

## Troubleshooting

### Jenkins Won't Start
**Problem**: Jenkins process exits or fails to initialize

**Solution**:
1. Check logs: `Get-Content C:\jenkins\jenkins.log -Tail 50`
2. Ensure port 8080 is not in use: `netstat -ano | findstr ":8080"`
3. Increase memory: `-Xmx1024m` in startup command
4. Check Java version: `java -version`

### Repository Access Denied
**Problem**: Jenkins cannot clone the GitHub repository

**Solution**:
1. Add GitHub credentials in Jenkins:
   - Manage Jenkins → Credentials → Add Credentials
   - Username: your GitHub username
   - Password: GitHub Personal Access Token
2. Update job to use credentials
3. Or use a public repository (SerenityBDDFramework is already public)

### Tests Fail with Timeout
**Problem**: Serenity tests timeout waiting for elements

**Solution**:
1. Check browser availability
2. Increase timeout in test configuration
3. Ensure Chrome/Firefox is installed on Jenkins host
4. Consider using Selenium Docker container

### Build Takes Too Long
**Problem**: Maven build is slow

**Solution**:
1. Jenkins caches Maven dependencies in `C:\jenkins\.m2`
2. First build is slower (downloads all dependencies)
3. Subsequent builds are faster
4. Increase memory: `-Xmx1024m`

## Environment Details

```
Jenkins Home: C:\jenkins
Jenkins URL: http://localhost:8080
Jenkins Port: 8080
Java Version: OpenJDK 21.0.11
OS: Windows 11
Repository: https://github.com/kumar-8409/SerenityBDDFramework
```

## Next Steps

1. **Configure GitHub Webhooks** (optional)
   - For automatic builds on push
   - GitHub Settings → Webhooks → Add webhook
   - Payload URL: `http://your-jenkins-url:8080/github-webhook/`

2. **Set Up Email Notifications** (optional)
   - Manage Jenkins → Configure System → Email notification
   - Configure SMTP server

3. **Integrate with Slack** (optional)
   - Install Slack plugin
   - Configure channel for build notifications

4. **Schedule Regular Builds** (optional)
   - Pipeline configuration → Build Triggers
   - Set cron schedule

## Useful Jenkins URLs

- Dashboard: http://localhost:8080/
- Job Page: http://localhost:8080/job/SerenityBDDFramework/
- Build Console: http://localhost:8080/job/SerenityBDDFramework/1/console
- Configuration: http://localhost:8080/job/SerenityBDDFramework/configure
- API: http://localhost:8080/api/json

## Getting Initial Admin Password

If you need the initial admin password:

```powershell
Get-Content C:\jenkins\secrets\initialAdminPassword
```

## Stopping Jenkins

To stop Jenkins:

```powershell
Get-Process java | Where-Object { $_.CommandLine -like "*jenkins*" } | Stop-Process
```

## Restarting Jenkins

```powershell
# Stop Jenkins (as above)
# Then restart with:
cd C:\jenkins
java -Xmx512m -jar jenkins.war --httpPort=8080
```

## Support Resources

- [Jenkins Documentation](https://www.jenkins.io/doc/)
- [Pipeline Syntax](https://www.jenkins.io/doc/book/pipeline/syntax/)
- [Serenity BDD](https://serenity-bdd.info/)
- [GitHub Integration](https://plugins.jenkins.io/github/)

---

**Ready to run tests?** Start with Step 1 above!
