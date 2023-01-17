# Monitoring with dynatrace

## Setup instructions

### Google Cloud

1. Create project and connect via gcloud auth login
2. Menu -> Kubernetes Engine -> Enable Kubernetes Engine API
3. Cluster -> Create cluster
4. "You manage your cluster: Create"
5. Give name to cluster
6. Go to default pool -> increase number of nodes if needed
7. Create
8. Get connection command and connect to cluster via terminal
   gcloud container clusters get-credentials cluster-1 --zone us-central1-c --project cloudcomputingproject-375018

### Calculator service

1. Build the application, create the docker image and push it to docker hub
```console
cd calculator
gradle build

docker image build -f Dockerfile -t davidchen98/calculator ./
docker push davidchen98/calculator
```

2. Login to glcoud and apply kubernetes configurations
```console
kubectl apply -f deployment.yaml
kubectl apply -f service.yaml
```

### Storage service
1. Build the application, create the docker image and push it to docker hub
```console
cd ../storage
gradle build

docker image build -f Dockerfile -t davidchen98/storage ./
docker push davidchen98/storage
```

2. Create mysql volume and service
```console
kubectl apply -f mysql-pv.yaml
kubectl apply -f mysql-deployment.yaml
```

3. Create configurations for storage service
```console
kubectl apply -f deployment.yaml
kubectl apply -f service.yaml
```

### Client service
1. Create the docker image and push it to docker hub
```console
cd ../client

docker image build -f Dockerfile -t davidchen98/client ./
docker push davidchen98/client
```

2. Create configurations for storage service
```console
kubectl apply -f deployment.yaml
kubectl apply -f load-balancer.yaml
```

### Dynatrace setup

 ```console
kubectl create namespace dynatrace
kubectl apply -f https://github.com/Dynatrace/dynatrace-operator/releases/download/v0.10.1/kubernetes.yaml
kubectl -n dynatrace wait pod --for=condition=ready --selector=app.kubernetes.io/name=dynatrace-operator,app.kubernetes.io/component=webhook --timeout=300s
kubectl apply -f dynakube.yaml
 ```
