#!/usr/bin/env bash
set -euo pipefail

mkdir -p build
{
  echo "Deployment simulation"
  echo "Version: ${APP_VERSION:-local}"
  echo "Target environment: ${DEPLOY_ENV:-local}"
  echo "Notification channel: ${NOTIFY_CHANNEL:-console}"
} > build/deploy-info.txt
echo "Deployment simulation finished for ${DEPLOY_ENV:-local}."
