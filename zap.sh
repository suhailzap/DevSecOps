#!/bin/bash

# Variables (ensure these are set before running, or pass them as arguments)
serviceName="${serviceName:-my-service}"  # Default value if not set
applicationURL="${applicationURL:-localhost}"  # Default value if not set

# Get the NodePort for the service
PORT=$(kubectl -n default get svc "${serviceName}" -o json | jq -r .spec.ports[0].nodePort)
if [ -z "$PORT" ] || [ "$PORT" == "null" ]; then
  echo "Error: Could not retrieve NodePort for service '${serviceName}'. Check service name or kubectl access."
  exit 1
fi

# Ensure the script has proper permissions (self-modify only if needed)
if [ ! -x "$0" ]; then
  chmod u+x "$0"
fi
echo "Running as UID:GID $(id -u):$(id -g)"

# Pull the OWASP ZAP image from Docker Hub with error handling
echo "Pulling OWASP ZAP image..."
docker pull zaproxy/zap-stable:latest || {
  echo "Failed to pull zaproxy/zap-stable:latest. Check Docker Hub access or network."
  exit 1
}

# Ensure the target URL is accessible (optional connectivity check)
echo "Checking if $applicationURL:$PORT is reachable..."
curl -s --connect-timeout 5 "http://$applicationURL:$PORT/v3/api-docs" >/dev/null || {
  echo "Warning: Could not reach http://$applicationURL:$PORT/v3/api-docs. Proceeding anyway..."
}

# Run OWASP ZAP scan and capture output
echo "Running OWASP ZAP scan on http://$applicationURL:$PORT/v3/api-docs..."
SCAN_OUTPUT=$(docker run -v "$(pwd)":/zap/wrk/:rw -t zaproxy/zap-stable:latest zap-api-scan.py \
  -t "http://$applicationURL:$PORT/v3/api-docs" \
  -f openapi \
  -r zap_report.html 2>&1)
EXIT_CODE=$?

# Print scan output for debugging
echo "$SCAN_OUTPUT"

# Parse the scan results for FAIL-NEW count
FAIL_COUNT=$(echo "$SCAN_OUTPUT" | grep -oP 'FAIL-NEW:\s*\K\d+' || echo "0")
WARN_COUNT=$(echo "$SCAN_OUTPUT" | grep -oP 'WARN-NEW:\s*\K\d+' || echo "0")

# Create report directory and move the report
mkdir -p owasp-zap-report
if [ -f zap_report.html ]; then
  mv zap_report.html owasp-zap-report/
  echo "Report moved to owasp-zap-report/zap_report.html"
else
  echo "Warning: zap_report.html not generated."
fi

echo "Exit Code: $EXIT_CODE"
echo "Failures Detected: $FAIL_COUNT"
echo "Warnings Detected: $WARN_COUNT"

# Interpret results: Fail only if there are FAIL-NEW entries
if [ "$FAIL_COUNT" -gt 0 ]; then
  echo "OWASP ZAP Report has detected $FAIL_COUNT failures. Please check the HTML Report in owasp-zap-report/"
  exit 1
elif [ "$WARN_COUNT" -gt 0 ]; then
  echo "OWASP ZAP detected $WARN_COUNT warnings but no failures. Proceeding as successful."
  exit 0
else
  echo "OWASP ZAP did not report any risks or warnings."
  exit 0
fi