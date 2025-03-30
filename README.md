
---

# 🌟 DevSecOps Demo Project 🌟

Welcome to the **DevSecOps Demo Project**! This repository (`https://github.com/suhailzap/DevSecOps`) showcases a practical implementation of a secure DevOps pipeline deployed on Kubernetes, with a focus on integrating security at every step. Built by `suhailzap`, this project demonstrates how to evolve a simple pipeline into a robust **DevSecOps workflow** using modern tools and practices. 🚀

---

## 📋 Project Overview

This demo deploys a sample application (`suhailsap06/numeric-app`) to a Kubernetes cluster on Azure, secured with industry-standard tools. It’s a hands-on example of transitioning from **DevOps** to **DevSecOps**, complete with CI/CD automation via GitHub Actions.

### 🎯 Objectives
- Build a **simple DevOps pipeline** with 4 stages.
- Enhance it into a **DevSecOps pipeline** with security scans and checks.
- Deploy securely to **Kubernetes** (dev and prod environments).
- Showcase an **alternative CI/CD setup** compared to traditional tools like Jenkins.

---

## 🗂️ Project Structure

### ⚙️ Simple DevOps Pipeline
- **Setup**:
  - Azure VM deployment ☁️
  - Install: Docker 🐳, Kubernetes ☸️, Java/Maven ☕, Jenkins 🏭
- **Application**:
  - `suhailsap06/numeric-app` (a SpringBoot app with `/increment/99` endpoint)
- **Pipeline Stages (4)**:
  - Build 🛠️
  - Test ✅
  - Docker Build 📦
  - Kubernetes Deploy 🚢
- **Goal**: Deploy the app to `devsecops-k8.eastus.cloudapp.azure.com`.

### 🔐 DevSecOps Pipeline
- **Security Additions**:
  - Git Hooks (Talisman) 🛡️
  - Dependency Checks 📊
  - Image Scans (Trivy) 🖼️
  - Dockerfile Checks (OPA) 📜
  - Kubernetes Security (Kubesec) 🔍
  - SAST (SonarQube) 🐛
  - DAST (OWASP ZAP) ⚔️
- **Deployment**:
  - Deploy to **K8s Dev Namespace** 🚢
  - Rollout status check 🔄
- **Notifications**:
  - Basic Slack alerts 📢
- **Goal**: Secure the pipeline end-to-end.

### 🛡️ Kubernetes Security
- **Features**:
  - **Istio Service Mesh**: mTLS 🔐, Kiali 📊
  - **Monitoring**: Falco 🚨, Kube-scan 🔎
  - **Deployment**: Prod namespace 🌍, CIS benchmarks ✅
  - **Extras**: HELM 📦, advanced Slack notifications 🎨
- **Goal**: Harden Kubernetes for production.

---

## 🛠️ Prerequisites

To run this demo, you’ll need:
- **Basic Knowledge**:
  - Linux 🐧
  - Shell Scripting 📜
  - DevOps ⚙️
- **Tools**:
  - Docker 🐳
  - Kubernetes ☸️
  - Azure CLI ☁️
  - GitHub Actions 🔄

---

## 📦 Resources

- **Code**: See `/src/` for the `numeric-app` source (assumed structure).
- **Kubernetes Config**: Check `/k8s/k8s_deployment_service.yaml`.
- **Scripts**: Deployment script at `/action-k8-deployment.sh`.
- **Clone the Repo**:
  ```bash
  git clone https://github.com/suhailzap/DevSecOps.git
  ```

---

## 🚀 Getting Started

### 1️⃣ Set Up Your Environment
- **Azure VM**:
  ```bash
  az vm create --resource-group devsecops-rg --name devsecops-vm --image UbuntuLTS
  ```
- **Install Tools**:
  ```bash
  sudo apt update && sudo apt install -y docker.io kubectl
  ```

### 2️⃣ Build & Deploy Locally
- **Build Docker Image**:
  ```bash
  docker build -t suhailsap06/numeric-app:latest .
  docker push suhailsap06/numeric-app:latest
  ```
- **Deploy to Kubernetes**:
  ```bash
  kubectl apply -f k8s/k8s_deployment_service.yaml
  ```

### 3️⃣ Run the GitHub Actions Workflow
This project uses **GitHub Actions** for CI/CD (see `.github/workflows/devsecops.yml`):
- **Workflow Overview**:
  - Builds the app.
  - Runs tests.
  - Pushes the Docker image (`suhailsap06/numeric-app:<commit-sha>`).
  - Deploys to Kubernetes using `action-k8-deployment.sh`.
- **Key Script** (`action-k8-deployment.sh`):
  ```bash
  #!/bin/bash
  echo "DEPLOYMENT_NAME: ${DEPLOYMENT_NAME}"
  echo "IMAGE_NAME: ${IMAGE_NAME}"
  sed -i "s#replace#${IMAGE_NAME}#g" k8s_deployment_service.yaml
  kubectl -n default get deployment "${DEPLOYMENT_NAME}" > /dev/null 2>&1
  if [[ $? -ne 0 ]]; then
      echo "Deployment ${DEPLOYMENT_NAME} doesn't exist"
      kubectl -n default apply -f k8s_deployment_service.yaml
  else
      echo "Deployment ${DEPLOYMENT_NAME} exists"
      kubectl -n default set image deployment "${DEPLOYMENT_NAME}" "${CONTAINER_NAME}=${IMAGE_NAME}"
      kubectl -n default rollout restart deployment "${DEPLOYMENT_NAME}"
  fi
  kubectl -n default apply -f k8s_deployment_service.yaml
  kubectl -n default rollout status deployment "${DEPLOYMENT_NAME}"
  ```
- **Trigger**: Push to `main` branch.

### 4️⃣ Alternative CI/CD with Jenkins
- Instead of GitHub Actions, you could use **Jenkins**:
  - **Pipeline Example**:
    ```groovy
    pipeline {
        agent any
        stages {
            stage('Build') { steps { sh 'mvn clean package' } }
            stage('Docker Build') { steps { sh 'docker build -t suhailsap06/numeric-app:${BUILD_ID} .' } }
            stage('Push') { steps { sh 'docker push suhailsap06/numeric-app:${BUILD_ID}' } }
            stage('Deploy') { steps { sh 'bash action-k8-deployment.sh' } }
        }
    }
    ```
  - **Why GitHub Actions?**: Simpler setup, native to GitHub, no external VM required.

---

## 📊 Diagrams

### 1. DevSecOps Pipeline
**Description**: Flowchart of the pipeline (Build → Test → Secure → Deploy).
- **Mermaid Code**:
  ```mermaid
  graph TD
      A[Code Push] --> B[Build]
      B --> C[Test]
      C --> D[Security Scans]
      D --> E[Docker Build]
      E --> F[K8s Deploy]
      F --> G[Slack Notify]
  ```
- **Add to README**: Place this in a code block for live rendering.

### 2. Kubernetes Architecture
**Description**: Shows the app deployed with Istio and Falco.
- **Mermaid Code**:
  ```mermaid
  graph TD
      A[K8s Cluster] --> B[Dev Namespace]
      B --> C[devsecops Pod]
      C --> D[suhailsap06/numeric-app]
      A --> E[Istio]
      E --> F[mTLS]
      A --> G[Falco]
      G --> H[Slack Alerts]
  ```

---

## 🔍 DevOps vs DevSecOps

| **Aspect**         | **DevOps**             | **DevSecOps**            |
|---------------------|------------------------|--------------------------|
| **Focus**          | Speed & Delivery      | Security + Delivery     |
| **Security**       | After deployment      | Every stage             |
| **Pipeline**       | 4 stages              | 13 stages (with scans)  |
| **Tools**          | Docker, K8s           | Trivy, Istio, Falco     |

---

## 🔐 Security Highlights

- **Development**: Dependency checks 📊
- **Testing**: Trivy scans 🖼️, OWASP ZAP ⚔️
- **Production**: Falco monitoring 🚨, Istio mTLS 🔐

---

## ✅ Verify Deployment

- **Check Pods**:
  ```bash
  kubectl get pods -l app=devsecops
  ```
- **Test Endpoint**:
  ```bash
  curl http://devsecops-k8.eastus.cloudapp.azure.com/increment/99
  ```
- **Expected Output**: Incremented value (e.g., `100`).

---

## 🙌 Contributing

Fork this repo, tweak the pipeline, or add new security tools! Submit a PR or open an issue to collaborate. Let’s make DevSecOps shine! 🌈

---

## 📧 Contact

- **Author**: `suhailzap`
- **Repo**: [github.com/suhailzap/DevSecOps](https://github.com/suhailzap/DevSecOps)
- **Focus**: Kubernetes, DevOps, Security

Enjoy this demo, and happy securing! 🎉

---

### Notes
1. **Diagrams**: Add the Mermaid code directly in the README. For more complex visuals, create PNGs and upload to `/images/`.
2. **Repo Structure**: Assumes `/src/`, `/k8s/`, and `/action-k8-deployment.sh`. Adjust paths if different.
3. **Jenkins Alternative**: Highlights GitHub Actions as the primary CI/CD, with Jenkins as an optional alternative.
4. **Customization**: Replace placeholders (e.g., `<commit-sha>`) with dynamic variables like `${{ github.sha }}` in the workflow.

