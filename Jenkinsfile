pipeline {
  agent any

  environment {
    deploymentName = "devsecops"
    containerName = "devsecops-container"
    serviceName = "devsecops-svc"
    imageName = "suhailsap06/numeric-app:${GIT_COMMIT}"
    applicationURL = "devsecops-k8.eastus.cloudapp.azure.com"
    applicationURI = "/increment/99"
  }

  stages {
    stage('Build Artifact - Maven') {
      steps {
        sh "mvn clean package -DskipTests=true"
        archive 'target/*.jar'
      }
    }

    stage('Unit Tests - JUnit and Jacoco') {
      steps {
        sh "mvn test"
      }
    }

    stage('Mutation Tests - PIT') {
      steps {
        sh "mvn org.pitest:pitest-maven:mutationCoverage"
      }
    }

    stage('SonarQube - SAST') {
      steps {
        withSonarQubeEnv('SonarQube') {
          sh "mvn sonar:sonar \
                -Dsonar.projectKey=numeric-application \
                -Dsonar.host.url=http://devsecops-k8.eastus.cloudapp.azure.com:9000"
        }
        timeout(time: 2, unit: 'MINUTES') {
          script {
            waitForQualityGate abortPipeline: true
          }
        }
      }
    }

    stage('Vulnerability Scan - Docker') {
      steps {
        parallel(
          "Dependency Scan": {
            sh "mvn dependency-check:check"
            sh "ls -l target/dependency-check-report.xml || echo 'Dependency-Check report not found'"
          },
          "Trivy Scan": {
            sh "bash trivy-docker-image-scan.sh"
          },
          "OPA Conftest": {
            sh 'docker run --rm -v $(pwd):/project openpolicyagent/conftest test --policy opa-docker-security.rego Dockerfile'
          }
        )
      }
    }

    stage('Docker Build and Push') {
      steps {
        withDockerRegistry([credentialsId: 'dockerhub', url: '']) {
          sh 'printenv'
          sh 'sudo docker build -t suhailsap06/numeric-app:"$GIT_COMMIT" .'
          sh 'docker push suhailsap06/numeric-app:"$GIT_COMMIT"'
        }
      }
    }

    stage('Vulnerability Scan - Kubernetes') {
      steps {
        parallel(
          "OPA Scan": {
            sh 'docker run --rm -v $(pwd):/project openpolicyagent/conftest test --policy opa-k8s-security.rego k8s_deployment_service.yaml'
          },
          "Kubesec Scan": {
            sh "bash kubesec-scan.sh"
          },
          "Trivy Scan": {
            sh "bash trivy-k8s-scan.sh"
          }
        )
      }
    }

    stage('K8S Deployment - DEV') {
      steps {
        parallel(
          "Deployment": {
            withKubeConfig([credentialsId: 'kubeconfig']) {
              sh "bash k8s-deployment.sh"
            }
          },
          "Rollout Status": {
            withKubeConfig([credentialsId: 'kubeconfig']) {
              sh "bash k8s-deployment-rollout-status.sh"
            }
          }
        )
      }
    }

    stage('Integration Tests - DEV') {
      steps {
        script {
          try {
            withKubeConfig([credentialsId: 'kubeconfig']) {
              sh "bash integration-test.sh"
            }
          } catch (e) {
            withKubeConfig([credentialsId: 'kubeconfig']) {
              sh "kubectl -n default rollout undo deploy ${deploymentName}"
            }
            throw e
          }
        }
      }
    }

    stage('OWASP ZAP - DAST') {
      steps {
        withKubeConfig([credentialsId: 'kubeconfig']) {
          sh 'bash zap.sh'
        }
      }
    }

    stage('Prompt to PROD?') {
      steps {
        timeout(time: 2, unit: 'DAYS') {
          input message: 'Do you want to Approve the Deployment to Production Environment/Namespace?', ok: 'Approve'
        }
      }
    }

    stage('K8S CIS Benchmark') {
      steps {
        script {
          parallel(
            "Master": {
              sh "bash cis-master.sh"
            },
            "Etcd": {
              sh "bash cis-etcd.sh"
            },
            "Kubelet": {
              sh "bash cis-kubelet.sh"
            }
          )
        }
      }
    }

    stage('K8S Deployment - PROD') {
      steps {
        parallel(
          "Deployment": {
            withKubeConfig([credentialsId: 'kubeconfig']) {
              sh "sed -i 's#replace#${imageName}#g' k8s_PROD-deployment_service.yaml"
              sh "kubectl -n prod apply -f k8s_PROD-deployment_service.yaml"
            }
          },
          "Rollout Status": {
            withKubeConfig([credentialsId: 'kubeconfig']) {
              sh "bash k8s-PROD-deployment-rollout-status.sh"
            }
          }
        )
      }
    }

    stage('Verify PROD Deployment') {
      steps {
        withKubeConfig([credentialsId: 'kubeconfig']) {
          sh "kubectl -n prod get pods -l app=devsecops"
          sh "curl -s http://${applicationURL}:30997${applicationURI}"
        }
      }
    }
  }

  post {
    always {
      script {
        // Wrap each reporting step in a try-catch to prevent post block failures from marking the pipeline as failed
        try {
          junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true
        } catch (Exception e) {
          echo "Warning: Failed to publish JUnit test results. Error: ${e.message}"
        }

        try {
          jacoco execPattern: 'target/jacoco.exec'
        } catch (Exception e) {
          echo "Warning: Failed to publish JaCoCo report. Error: ${e.message}"
        }

        try {
          pitmutation mutationStatsFile: '**/target/pit-reports/**/mutations.xml'
        } catch (Exception e) {
          echo "Warning: Failed to publish PIT mutation report. Error: ${e.message}"
        }

        try {
          dependencyCheckPublisher pattern: 'target/dependency-check-report.xml', failedTotalCritical: 1, failedTotalHigh: 1
        } catch (Exception e) {
          echo "Warning: Failed to publish Dependency-Check report. Error: ${e.message}"
        }

        try {
          publishHTML([allowMissing: true, 
                       alwaysLinkToLastBuild: true, 
                       keepAll: true, 
                       reportDir: 'owasp-zap-report', 
                       reportFiles: 'zap_report.html', 
                       reportName: 'OWASP ZAP Report', 
                       reportTitles: 'OWASP ZAP Scan Results'])
        } catch (Exception e) {
          echo "Warning: Failed to publish OWASP ZAP report. Error: ${e.message}"
        }

        try {
          archiveArtifacts artifacts: 'owasp-zap-report/zap_report.html', allowEmptyArchive: true
        } catch (Exception e) {
          echo "Warning: Failed to archive OWASP ZAP report. Error: ${e.message}"
        }
      }
    }
    success {
      echo 'Pipeline completed successfully! Deployment to Production was successful.'
    }
    failure {
      echo 'Pipeline failed due to an error in the stages. Check the logs for details.'
    }
  }
}