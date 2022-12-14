
# Project proposal

## Definition
Exploring Dynatrace to observe an application consisting of 3 microservices

## Scope
### Structure
Three microservices:
* Microservice 1 - ui: Frontend with input fields
* Microservice 2 - db: Backend with database
* Microservice 3 - calculator: Backend for calculations

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
* Adzaga, Mateo
	* Implement Microservice ui
	* Set up dynatrace monitoring for kubernetes cluster
* Chen, David
	* Implement Microservice calculator
	* Create and deploy cluster on kubernetes
* Peham, Jakob
	* Implement Microservice db
	* Implement docker images

* Helping and checking in on others (all)
* Analyze dynatrace logs/traces (all)
