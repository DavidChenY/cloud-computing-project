# Monitoring with dynatrace

## Structure of microservices
Architecture Diagram
![architecture diagram](https://github.com/DavidChenY/cloud-computing-project/blob/main/architecture.png)

### Microservice 1 - Client
React UI which lets the user input 2 numbers and a mathematical operation. This is sent to the microservice 2 "storage".

### Microservice 2 - Storage
Spring Java backend which checks the MySQL database if the requested operation has already been computed with these numbers. If that's the case, the result is fetched from the database and returned to the "client". If not, the request is forwarded to the "calculator" and the received response then saved in the database and returned to the "client".

### Microservice 3 - Calculator
Spring Java backend which receives a mathematical operation with numbers and computes the result (or suspicious wrong results) depending on the input. The result is returned to the "storage".

## Summary of research

### Implement microservices / docker container
The 3 microservices are implemented and tested out locally if working as anticipated.
The .jar files are created and the docker image is built with exposed ports as desired.

### Deploy container on kubernetes
The docker container are pushed onto the docker-hub for kubernetes.
Kubernetes configurations are applied to add the docker container to the kubernetes infrastructure.
This is done for all 3 microserivces.

### Add load-balancer and mysql externally
In order for the client UI to work properly, we had to add a load-balancer for it.
A MySQL service had to be created specifically.

### Setup dynatrace
Registrate for dynatrace monitoring and execute the generated .yaml-files to add dynatrace onto the project.
We also added the dynatrace-operator from the marketplace, though that should not be necessary.

## Setup instructions

### Google Cloud

1. Create project and connect via gcloud auth login
2. Menu -> Kubernetes Engine -> Enable Kubernetes Engine API
3. Cluster -> Create cluster
4. Select "You manage your cluster: Create"
5. Give a name to the cluster
6. Go to default pool -> increase number of nodes if needed
7. Click on "Create"
8. Get connection command and connect to cluster via terminal
```console
gcloud container clusters get-credentials cluster-1 --zone us-central1-c --project cloudcomputingproject-375018
```

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
1. Get the storage-service load-balancer IP from Google Cloud
2. Put the IP into the http-commons.ts in the Base URL

3. Create the docker image and push it to docker hub
```console
cd ../client

docker image build -f Dockerfile -t davidchen98/client ./
docker push davidchen98/client
```

4. Create configurations for storage service
```console
kubectl apply -f deployment.yaml
kubectl apply -f service.yaml
```

5. Access the client page by the endpoint

### Dynatrace setup

1. Log in to dynatrace, start installation
2. Select Kubernetes
3. Add name, create tokens
4. enable skip ssl certificate, enable volume storage
ADD IMAGE
5. Copy .yaml file to the project folder
6. Copy and execute the commands listed below

 ```console
kubectl create namespace dynatrace
kubectl apply -f https://github.com/Dynatrace/dynatrace-operator/releases/download/v0.10.1/kubernetes.yaml
kubectl -n dynatrace wait pod --for=condition=ready --selector=app.kubernetes.io/name=dynatrace-operator,app.kubernetes.io/component=webhook --timeout=300s
kubectl apply -f dynakube.yaml
 ```

7. Errors in the workloads may occur. 
8. If that is the case wait a bit, or go to the cluster and increase node size of the default pool.
9. Wait and restart all pods

```console
kubectl rollout restart deployment calculator-service -n default
kubectl rollout restart deployment storage-service -n default
kubectl rollout restart deployment client -n default
```

10. Go to settings -> Log monitoring -> Log sources and storage
11. Select all host names and save changes
12. (Optional) Go to Service Detection -> Custom service detection -> Define Java services
13. Add name -> find entry point -> select process group -> select process -> select rest controller
14. Go to Applications & Microservices -> Frontend and select the application
15. Go to the 3 dots and press edit
16. Capturing -> Async web requests and SPAs and enable Capture XmlHttpRequest and Capture fetch requests

 ## Summary of lessons learned

 ### Dynatrace log monitoring
 The log monitoring in dynatrace is definitely not easy and out-of-the-box. We had a lot of issues as at first the whole services were not recognized. We followed several tutorials from dynatrace on how to setup the monitoring correctly, which ultimately had either wrong version and thus different UI so they were not extremely helpful, or they did not work or solve the problem. Ultimately the solution to a whole lot of problems was to rename our microservices to have the name "service" in their actual name.
