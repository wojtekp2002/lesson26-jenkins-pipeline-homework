#!/usr/bin/env bash
set -euo pipefail

mkdir -p build reports
python3 -m compileall -q app
{
  echo "Build completed"
  echo "Version: ${APP_VERSION:-local}"
  echo "Environment: ${DEPLOY_ENV:-local}"
  echo "Build number: ${BUILD_NUMBER:-manual}"
} > build/build-info.txt
