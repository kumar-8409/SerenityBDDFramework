# Jenkins Pipeline - Quick Access Guide

## ✅ Jenkins is Running!

**URL:** http://localhost:8080

## 🚀 Start Here

1. **Open Jenkins:** http://localhost:8080
2. **Complete setup wizard** (if first time)
3. **Create pipeline job** (see instructions below)
4. **Run tests** by clicking "Build Now"

## 📋 Create Pipeline Job (5 minutes)

### In Jenkins Web UI:

1. Click **"New Item"**
2. Name: `SerenityBDDFramework`
3. Type: **Pipeline**
4. Click **OK**
5. In Configuration:
   - Definition: **Pipeline script from SCM**
   - SCM: **Git**
   - Repository: `https://github.com/kumar-8409/SerenityBDDFramework.git`
   - Branch: `*/main`
   - Script Path: `Jenkinsfile`
6. Click **Save**

## ▶️ Run Tests

1. Go to: http://localhost:8080/job/SerenityBDDFramework
2. Click **"Build Now"**
3. Watch build in progress
4. View results after completion

## 📊 Expected Results

- **Duration:** ~2 minutes
- **Tests:** 10 total
- **Pass Rate:** ~70%
- **Reports:** Auto-generated in target/site/serenity/

## 📖 Documentation

| Document | Purpose |
|----------|---------|
| `JENKINS_QUICK_START.md` | Step-by-step setup (recommended first read) |
| `JENKINS_PIPELINE_SETUP.md` | Comprehensive guide with troubleshooting |
| `Jenkinsfile` | Pipeline definition |
| `jenkins-job-config.xml` | Job configuration (XML) |

## 🔗 Useful Links

| Link | Purpose |
|------|---------|
| http://localhost:8080 | Jenkins Dashboard |
| http://localhost:8080/newJob | Create new job |
| http://localhost:8080/job/SerenityBDDFramework | Your pipeline job |
| http://localhost:8080/manage | Jenkins settings |

## ⚙️ Configuration

| Setting | Value |
|---------|-------|
| Port | 8080 |
| Java | OpenJDK 21.0.11 |
| Maven | 3.8.8 |
| Repository | GitHub (public) |

## ❌ Troubleshooting

### Jenkins not loading?
```powershell
netstat -ano | findstr :8080
Get-Process java | Where-Object {$_.CommandLine -like "*jenkins*"}
```

### Need admin password?
```powershell
Get-Content C:\jenkins\secrets\initialAdminPassword
```

### Check logs?
```powershell
Get-Content C:\jenkins\jenkins.log -Tail 50
```

## 📞 Support

- Check `JENKINS_QUICK_START.md` for detailed steps
- Check `JENKINS_PIPELINE_SETUP.md` for troubleshooting
- Review Jenkins console output for build errors
- Check `target/failsafe-reports/` for test failures

---

**Ready?** → Open http://localhost:8080 now!
