# GitHub Actions CI/CD Setup

## 🚀 CI/CD Pipeline Overview

Chúng tôi đã thiết lập 2 workflows:

### 1. **Development CI** (`dev-ci.yml`)

- Trigger: Push to `develop`, `feature/*`, `HVH/*` branches
- Chạy tests và build Docker image (không push)
- Kiểm tra code quality

### 2. **Production CI/CD** (`ci-cd.yml`)

- Trigger: Push to `main`/`master` branch
- Chạy full pipeline: Test → Build → Deploy → Security Scan

## 🔐 Required GitHub Secrets

Vào **Settings → Secrets and variables → Actions** và thêm các secrets sau:

### Docker Hub Integration:

```
DOCKER_HUB_USERNAME=your-dockerhub-username
DOCKER_HUB_ACCESS_TOKEN=your-dockerhub-access-token
```

### Server Deployment (Optional):

```
HOST=your-server-ip
USERNAME=your-server-username
PRIVATE_KEY=your-private-ssh-key
PORT=22
```

## 📋 Setup Steps

### 1. **Docker Hub Setup**

```bash
# 1. Tạo account trên hub.docker.com
# 2. Tạo Access Token:
#    - Docker Hub → Account Settings → Security → New Access Token
# 3. Copy token và username vào GitHub Secrets
```

### 2. **GitHub Repository Settings**

```bash
# 1. Enable Actions trong repository settings
# 2. Thêm các secrets cần thiết
# 3. Push code để trigger workflow
```

### 3. **Local Testing**

```bash
# Test Docker build locally
docker build -t busify:test .

# Test với compose
docker compose -f compose.test.yml up --build
```

## 🔄 Workflow Triggers

### Development:

- Push to `develop`, `feature/*`, `HVH/*` → Chạy tests + build
- Pull Request to `develop` → Chạy validation

### Production:

- Push to `main` → Full CI/CD pipeline
- Pull Request to `main` → Tests only

## 📊 Features

✅ **Automated Testing** với MySQL service
✅ **Docker Multi-platform Build** (AMD64 + ARM64)  
✅ **Maven Dependency Caching**
✅ **Security Scanning** với Trivy
✅ **Test Reports** integration
✅ **Automatic Deployment** to production server

## 🚨 Important Notes

1. **Branch Protection**: Nên enable branch protection cho `main` branch
2. **Environment Secrets**: Có thể setup environments (dev/staging/prod) với secrets riêng
3. **Database Migration**: Workflow sẽ chạy với test database riêng
4. **Resource Limits**: GitHub Actions có limits về compute time

## 🎯 Next Steps

1. Push workflows lên GitHub
2. Thêm required secrets
3. Create test branch và push để test workflow
4. Setup production server (nếu cần auto-deploy)
