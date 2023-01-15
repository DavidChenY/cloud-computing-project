# Setup for calculator

## Instructions

### Building image

1. Build using gradle

```console
gradle build
```

2. Build a Docker image based on the Dockerfile:

```console
docker image build -f Dockerfile -t davidchen98/calculator ./
docker push davidchen98/calculator
```

### Set up the Cluster

 ```console
gcloud auth login
kubectl run calculator --image=davidchen98/calculator
kubectl apply -f deployment.yaml
kubectl apply -f service.yaml
kubectl describe service calculator
 ```
