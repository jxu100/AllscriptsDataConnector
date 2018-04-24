# Allscripts Data Connector for MicroStrategy REST Data connection


## Description
This is a Spring Boot Application for calling Allscripts Unity APIs from MicroStrategy REST data connection

## Goal
* Import data from Allscripts through Unity APIs into MicroStrategy Intelligence Cubes.
* Support TouchWorks EMR and Practice Management Unity APIs
* See Allscripts API document https://developer.allscripts.com/APIReference


## Build and Installation

### Pre-requirements
* You'll need an Allscripts developer account and a Client application to access Allscripts APIs.
* java 1.8.0 +

### Build
* Download source code: GitHub [https://github.com/jxu100/AllscriptsDataConnector]
* Open src/main/resources/application.properties, validate sandbox url, and specify your app name, username and password.
* build: mvn clean install -- it will run the testing scripts for Allscripts sandbox
* use : mvn clean install -DskipTests to build without tests


### Run
```
java -jar AllscriptsDataConnector-1.0.0.jar
```
* recommend to run as a daemon process on your specific platform.

### Access Allscripts Data Connector from MicroStrategy

* On MicroStrategy Create new data source -> Data From URL, specify url as follows:
URL: http://localhost:5002/{emr}/{Action}?PatientID=?&Parameter1=?&Parameter2=?&Parameter3=?&Parameter4=?&Parameter5=?&Parameter6=?&Data=?
* {emr} is either tw for TouchWorks APIs or pm for Practice Management APIS
* {Action} is specified in Allscripts APIs. All parameters for corresponding action are specified in the Allscripts API document.
* Import data as a REST data source

## Author

**email: jeaxu@cisco.com**

**phone: +1 (650) 279-0043**
