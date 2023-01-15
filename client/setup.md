# Setup for client

## Instructions

### Building image

1. Build & push docker image

```console
docker image build -f Dockerfile -t davidchen98/client ./
docker push davidchen98/client
```

### Configure kubernetes

 ```console 
kubectl apply -f deployment.yaml
kubectl apply -f load-balancer.yaml
kubectl describe service load-balancer
 ```
