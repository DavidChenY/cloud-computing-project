# Setup for calculator

## Instructions

### Building image

1. Build a Docker image based on the Dockerfile:
    ```console
    docker image build -f Dockerfile -t davidchen98/calculator ./
   
    docker images
    ```

### Set up

 ```console
kubectl apply -f mysql-pv.yaml
kubectl apply -f mysql-deployment.yaml

kubectl run calculator --image=davidchen98/calculator
kubectl apply -f service.yaml
kubectl describe service calculator
 ```


## Encountered problems
Tutorial for PersistentVolume creation from kubernetes does not work for GKE
* GKE does not require the PersistentVolume section, only the PersistentVolumeClaim
