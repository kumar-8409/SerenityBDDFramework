pipeline {
    agent {
        docker {
            image 'maven:3.8.8-openjdk-11'
            args '-v /root/.m2:/root/.m2 -v /dev/shm:/dev/shm'
        }
    }

    // Build parameters - can be overridden from the job or multibranch pipeline UI
    parameters {
        string(name: 'BROWSER', defaultValue: 'chrome', description: 'Browser to run tests with (chrome|firefox)')
        booleanParam(name: 'HEADLESS', defaultValue: true, description: 'Run browser in headless mode')
    }

    environment {
        MAVEN_OPTS = '-Xmx1024m'
    }

    options {
        timestamps()
        ansiColor('xterm')
        buildDiscarder(logRotator(numToKeepStr: '10'))
        timeout(time: 60, unit: 'MINUTES')
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
                    // Start a Selenium standalone Chrome container as a sidecar so browser tests can run
                    // This container will be stopped in the `post` section. Adjust image tag as needed.
                    seleniumContainer = docker.image('selenium/standalone-chrome:115.0').run('-d --shm-size=2g')
                    echo "Started selenium container ${seleniumContainer.id}"
                }
            }
        }

        stage('Build & Test') {
            steps {
                // Use Maven to run the Serenity BDD tests. Override or provide additional properties as needed.
                sh 'mvn -B -DskipTests=false clean verify'
            }
        }
    }

    post {
        always {
            // Publish junit results (Surefire/Failsafe) if present
            junit allowEmptyResults: true, testResults: 'target/**/surefire-reports/*.xml, target/**/failsafe-reports/*.xml'

            // Archive Serenity / Cucumber reports and full target output
            archiveArtifacts artifacts: 'target/site/**, target/cucumber-reports/**, target/**/*.html, target/**/*.json', allowEmptyArchive: true

            // stop selenium sidecar if started
            script {
                try {
                    if (binding.hasVariable('seleniumContainer') && seleniumContainer) {
                        seleniumContainer.stop()
                        echo 'Stopped selenium container'
                    }
                } catch (err) {
                    echo "Failed to stop selenium container: ${err}"
                }
            }
        }
        success {
            echo 'Pipeline finished SUCCESS'
        }
        failure {
            echo 'Pipeline finished FAILURE'
        }
    }
}

