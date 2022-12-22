
# Project proposal

## Definition
Explore Dynatrace to observe an application consisting of 3 microservices running on a Kubernetes cluster.

## Scope
### Structure
Three microservices:
* Microservice 1 - ui: Frontend with input fields
* Microservice 2 - storage: Backend with database
* Microservice 3 - calculator: Backend for calculations

Architecture Diagram
![alt text](https://github.com/DavidChenY/cloud-computing-project/blob/main/architecture.png)

## Dynatrace
### Technology support
* React (Fetch API and 12, 14, 16, 18 in node.js)
* Java (full support)
* MySQL (Version 2.x)

All used technologies are supported by Dynatrace.
We will setup dynatrace for our cluster using following tutorial https://learn.dynatracelabs.com/workshops/kubernetes-gke/index.html?index=..%2F..index#0

Obtaining logs:
* Autodiscovery: 
Log files stored on the kubernetes node are automatically identified on the “/var/log/pods” folder by the OneAgent. For this to be possible we have to use docker, marked as important, writing logs to “stdout/stderr” streams and the log files in “/var/log/pods” have to be existing for at least a minute (as the files are analyzed every 60 seconds)
* Manual adding:
Alternatively we can log them manually, if the Autodiscovery doesn’t work for some reason. This is only used as a last resort.



### Functionality
The microservices interact in following ways:
* Microservice 1 allows the user to input certain numbers which get sent to Microservice 2.
* Microservice 2 stores the requested numbers and forwards these to Microservice 3.
* Microservice 3 calculates our special formula and returns the result back to Microservice 2.
* Microservice 2 stores the result to the respective numbers and returns to Microservice 1.
* Microservice 1 shows the input numbers and the result back to the user.
* The formula results in unexpected behavior when calculated with certain numbers.
* Dynatrace observes the microservices when in normal operation as well as when Microservice 3 becomes ‘unhealthy’ due to infinite calculation.

## Milestones
Implement microservices
Implement docker images for microservices.
Create and deploy clusters running on Google Kubernetes for microservices
Install and configure Dynatrace 
Check logs and traces of Dynatrace


## Responsibilities
* Adzaga, Mateo (k11776022)
	* Implement Microservice ui
	* Set up dynatrace monitoring for kubernetes cluster
* Chen, David (k11802918)
	* Implement Microservice calculator
	* Create and deploy cluster on kubernetes
* Peham, Jakob (k11740714)
	* Implement Microservice storage
	* Implement docker images

* Helping and checking in on others (all)
* Analyze dynatrace logs/traces (all)
