#!/bin/bash

# action-k8-deployment.sh

# Debug: Print environment variables
echo "DEPLOYMENT_NAME: ${DEPLOYMENT_NAME}"
echo "CONTAINER_NAME: ${CONTAINER_NAME}"
echo "IMAGE_NAME: ${IMAGE_NAME}"

# Replace 'replace' with the image name in the YAML
sed -i "s#replace#${IMAGE_NAME}#g" k8s_deployment_service.yaml

# Check if deployment exists
kubectl -n default get deployment "${DEPLOYMENT_NAME}" > /dev/null 2>&1

if [[ $? -ne 0 ]]; then
    echo "deployment ${DEPLOYMENT_NAME} doesn't exist"
    kubectl -n default apply -f k8s_deployment_service.yaml
else
    echo "deployment ${DEPLOYMENT_NAME} exists"
    echo "image name - ${IMAGE_NAME}"
    kubectl -n default set image deployment "${DEPLOYMENT_NAME}" "${CONTAINER_NAME}=${IMAGE_NAME}"
fi

# Apply the YAML again to ensure consistency
kubectl -n default apply -f k8s_deployment_service.yaml