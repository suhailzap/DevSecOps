#!/bin/bash

# Get the NodePort for the service
PORT=$(kubectl -n default get svc ${serviceName} -o json | jq .spec.ports[].nodePort)

# Ensure the script has proper permissions (avoid chmod 777 for security)
chmod u+x $(pwd)/zap.sh
echo "Running as UID:GID $(id -u):$(id -g)"

# Pull the OWASP ZAP image explicitly with error handling
echo "Pulling OWASP ZAP image..."
docker pull owasp/zap2docker-weekly:latest || {
  echo "Failed to pull owasp/zap2docker-weekly:latest. Check Docker Hub access or network."
  exit 1
}

# Run OWASP ZAP scan
echo "Running OWASP ZAP scan on $applicationURL:$PORT/v3/api-docs..."
docker run -v $(pwd):/zap/wrk/:rw -t owasp/zap2docker-weekly:latest zap-api-scan.py -t http://$applicationURL:$PORT/v3/api-docs -f openapi -r zap_report.html

exit_code=$?

# Create report directory and move the report
sudo mkdir -p owasp-zap-report
if [ -f zap_report.html ]; then
  sudo mv zap_report.html owasp-zap-report/
else
  echo "Warning: zap_report.html not generated."
fi

echo "Exit Code: $exit_code"

if [ ${exit_code} -ne 0 ]; then
  echo "OWASP ZAP Report has either Low/Medium/High Risk. Please check the HTML Report"
  exit 1
else
  echo "OWASP ZAP did not report any Risk"
fi