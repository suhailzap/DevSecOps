#!/bin/bash


echo "DEPLOYMENT_NAME: ${DEPLOYMENT_NAME}"
echo "CONTAINER_NAME: ${CONTAINER_NAME}"
echo "IMAGE_NAME: ${IMAGE_NAME}"

# Update the YAML with the new image
sed -i "s#replace#${IMAGE_NAME}#g" k8s_deployment_service.yaml
echo "Updated k8s_deployment_service.yaml:"
cat k8s_deployment_service.yaml

# Check if deployment exists
kubectl -n default get deployment "${DEPLOYMENT_NAME}" > /dev/null 2>&1

if [[ $? -ne 0 ]]; then
    echo "deployment ${DEPLOYMENT_NAME} doesn't exist"
    kubectl -n default apply -f k8s_deployment_service.yaml
else
    echo "deployment ${DEPLOYMENT_NAME} exists"
    echo "image name - ${IMAGE_NAME}"
    kubectl -n default set image deployment "${DEPLOYMENT_NAME}" "${CONTAINER_NAME}=${IMAGE_NAME}"
    kubectl -n default rollout restart deployment "${DEPLOYMENT_NAME}"
fi

# Apply the YAML to ensure consistency
kubectl -n default apply -f k8s_deployment_service.yaml

# Wait for rollout to complete
kubectl -n default rollout status deployment "${DEPLOYMENT_NAME}"