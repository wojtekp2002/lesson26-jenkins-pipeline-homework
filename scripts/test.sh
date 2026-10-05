#!/usr/bin/env bash
set -euo pipefail

mkdir -p reports
python3 -m unittest discover -s app -p "test_*.py" | tee reports/test-output.txt
