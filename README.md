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
This is done for all 3 microservices.

### Add load-balancer and mysql externally
The MySQL needed be configured with kubernetes instead of just having a docker image. 
It required two scripts which first claims the space and the creates the mysql service.
In order for the client UI to work properly, we had to add the URL of load-balancer for the storage for it manually.

### Setup dynatrace
Register for dynatrace monitoring and execute the generated .yaml-files to add dynatrace onto the project.
We also added the dynatrace-operator from the marketplace, though that should not be necessary.
We can see log and requests in dynatrace.

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

(Optional) 
1. Go to Service Detection -> Custom service detection -> Define Java services
2. Add name -> find entry point -> select process group -> select process -> select rest controller
3. Go to Applications & Microservices -> Frontend and select the application 
4. Go to the 3 dots and press edit 
5. Capturing -> Async web requests and SPAs and enable Capture XmlHttpRequest and Capture fetch requests

## Summary of lessons learned

### Kubernetes tutorial for MySQL does not fully work for GKE
Tutorial for PersistentVolume creation from kubernetes does not work for GKE
* GKE does not require the PersistentVolume section, only the PersistentVolumeClaim


### Dynatrace issues:

### Unscheduable pods from dynatrace
The pods were unscheduable according to GKE. We had to increase the number of nodes to get it working in our initial setup.

### Agent not injected
In our initial setup we had a NodeJs application. That way the application was not discovered as a service. Changing it to
a nginx with static files solved that issue. Additionaly the pods needed to be restarted or else the injections wouldn't happen.

### Fetch not traced
We expected Dynatrace to automatically check the requests made from the page and it took us some time to find out about the setting for it.

### Dynatrace log monitoring
Logs were not discovered at first, they also needed to be added in sources until they were shown.

### Service discovery
Arguably the biggest amout of time was spend trying to get the service discovery working which in the end still didnt fully work. According to the page there shouldnt be any extra rules required and it worked for the client after some docker changes.
We followed several tutorials from dynatrace on how to setup the monitoring correctly, which ultimately had either wrong version and thus different UI so they were not extremely helpful, or they did not work or solve the problem. 
In the end we manually added some custom service detection rules to see our services. However, doing so doesn't seem to work well with the distributed path tracing.
