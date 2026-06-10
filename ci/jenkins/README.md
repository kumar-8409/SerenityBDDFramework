Jenkins integration for this project
=================================

This repository includes a `Jenkinsfile` (at the repository root) which defines a declarative Jenkins pipeline to build and run the Serenity BDD tests.

What was added
- `Jenkinsfile` — declarative pipeline that:
  - runs inside a Maven Docker image (`maven:3.8.8-openjdk-11`)
  - optionally starts a `selenium/standalone-chrome` container as a sidecar for browser-based tests
  - runs `mvn -B -DskipTests=false clean verify`
  - publishes JUnit results (Surefire/Failsafe) and archives Serenity / Cucumber reports

Prerequisites / recommended Jenkins plugins
- Jenkins (LTS recommended)
- Pipeline (workflow) plugins
- Docker Pipeline plugin (if you want to use the Docker agent)
- JUnit plugin (for test result publishing)
- HTML Publisher plugin (optional, to publish HTML reports in Jenkins UI)

Recommended job type
- Multibranch Pipeline or Organization Folder pointing at this Git repository. Jenkins will discover branches and use the `Jenkinsfile` in each branch.

Parameters
- `BROWSER` (string) — default `chrome`. Used by the Jenkinsfile to decide whether to start the selenium sidecar.
- `HEADLESS` (boolean) — default `true`. Present for informational use; configure your Serenity properties or system properties to honor this flag.

Notes about browser tests
- The pipeline runs inside a Maven container which does not include a browser. To run real browser tests you have options:
  1. Provide a Selenium Grid / standalone server as a sidecar (the `Jenkinsfile` contains code to start `selenium/standalone-chrome` automatically). In that case, ensure your Serenity configuration points at the Selenium server (e.g., set `webdriver.remote.url=http://localhost:4444/wd/hub` or appropriate property).
  2. Use a Jenkins agent that already has Chrome/Chromedriver installed and configured.
  3. Use a custom Docker image that contains both Maven and Chrome/Chromedriver.

Local reproduction using Docker
1) Start Selenium standalone chrome (optional):

   PowerShell (Windows):
   ```powershell
   docker run -d --shm-size=2g --name selenium-standalone -p 4444:4444 selenium/standalone-chrome:115.0
   ```

2) Run the build in a Maven container (maps current workspace and cache Maven local repository):

   PowerShell (Windows):
   ```powershell
   docker run --rm -v ${PWD}:/workspace -w /workspace -v ${env:USERPROFILE}/.m2:/root/.m2 maven:3.8.8-openjdk-11 mvn -B clean verify
   ```

   Adjust the Maven command and environment properties to point tests at the Selenium server if you started it.

Jenkins setup notes
- If you use the Docker agent approach from the `Jenkinsfile`, ensure the Jenkins machine is configured to run Docker containers and the Jenkins user has permission to use Docker.
- If Docker is not available on your Jenkins master, use a dedicated Docker-enabled agent or switch the Jenkinsfile to use a Jenkins tool-based agent (JDK + Maven) and ensure the node has Maven installed.

Publishing HTML reports in Jenkins
- The `Jenkinsfile` archives the Serenity HTML output under `target/site/**`. To show the Serenity HTML inside Jenkins you can install the HTML Publisher plugin and add a `publishHTML` step. Example (add inside the `post` section if plugin is available):

  publishHTML(target: [reportName: 'Serenity Reports', reportDir: 'target/site/serenity', reportFiles: 'index.html', keepAll: true, alwaysLinkToLastBuild: true])

Troubleshooting
- If tests fail because WebDriver cannot connect, check that the Selenium sidecar started and is reachable, or that your Jenkins agent has Chrome/Chromedriver installed.
- If build fails inside Docker due to insufficient shared memory (chrome crashes), increase `--shm-size` for the selenium container (the Jenkinsfile sets `--shm-size=2g`).

Customizing
- You can replace the Docker image in the `Jenkinsfile` with a custom image that bundles Maven + browser if you prefer a single-container approach.

If you'd like, I can:
- detect if your project already contains properties controlling remote webdriver (e.g. in `serenity.conf` or `pom.xml`) and adapt the pipeline to use them;
- add a second Jenkinsfile variant that uses a non-Docker Jenkins agent (tool-based) for environments where Docker isn't available.

