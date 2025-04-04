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
- **Kiali Traffic Graph**:
  Below is a screenshot of the Kiali dashboard showing the traffic flow between services in the `default` and `prod` namespaces for the `devsecops` application:
  
  ![Kiali Traffic Graph](https://raw.githubusercontent.com/suhailzap/DevSecOps/main/images/kiali-screenshot.png)
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