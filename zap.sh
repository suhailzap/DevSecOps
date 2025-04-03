# Ensure the target URL is accessible (optional connectivity check)
echo "Checking if $applicationURL:$PORT is reachable..."
curl -s --connect-timeout 5 "http://$applicationURL:$PORT/v3/api-docs" >/dev/null || {
  echo "Warning: Could not reach http://$applicationURL:$PORT/v3/api-docs. Proceeding anyway..."
}

# Run OWASP ZAP scan using the specified image
echo "Running OWASP ZAP scan on http://$applicationURL:$PORT/v3/api-docs..."
docker run -v $(pwd):/zap/wrk/:rw -t owasp/zap2docker-weekly zap-api-scan.py -t $applicationURL:$PORT/v3/api-docs -f openapi -c zap_rules -r zap_report.html
exit_code=$?  # Captures the exit code of the docker run command

# Create report directory and move the report
mkdir -p owasp-zap-report
if [ -f zap_report.html ]; then
  mv zap_report.html owasp-zap-report/
  echo "Report moved to owasp-zap-report/zap_report.html"
else
  echo "Warning: zap_report露

### Full Script Context
Here’s the full script again for clarity, with the added lines marked:

```bash
#!/bin/bash

# Exit on any error
set -e

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
docker pull owasp/zap2docker-weekly || {
  echo "Failed to pull owasp/zap2docker-weekly. Check Docker Hub access or network."
  exit 1
}

# Ensure the target URL is accessible (optional connectivity check)
echo "Checking if $applicationURL:$PORT is reachable..."
curl -s --connect-timeout 5 "http://$applicationURL:$PORT/v3/api-docs" >/dev/null || {
  echo "Warning: Could not reach http://$applicationURL:$PORT/v3/api-docs. Proceeding anyway..."
}

# Run OWASP ZAP scan using the specified image
echo "Running OWASP ZAP scan on http://$applicationURL:$PORT/v3/api-docs..."
docker run -v $(pwd):/zap/wrk/:rw -t owasp/zap2docker-weekly zap-api-scan.py -t $applicationURL:$PORT/v3/api-docs -f openapi -c zap_rules -r zap_report.html  # <--- Added line
exit_code=$?  # <--- Added line to capture exit code

# Create report directory and move the report
mkdir -p owasp-zap-report
if [ -f zap_report.html ]; then
  mv zap_report.html owasp-zap-report/
  echo "Report moved to owasp-zap-report/zap_report.html"
else
  echo "Warning: zap_report.html not generated."
fi

echo "Exit Code: $exit_code"

# Interpret exit code
if [ "$exit_code" -ne 0 ]; then
  echo "OWASP ZAP Report has either Low/Medium/High Risk. Please check the HTML Report in owasp-zap-report/"
  exit 1
else
  echo "OWASP ZAP did not report any Risk"
fi