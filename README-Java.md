# jasperreportchecker

A tool which checks jasperreports JRXML files for unused fields, parameters and variables.


# Requirements

* Java: 11+
* Maven 3.5+


# Build

The project is to build via 

```mvn clean install```

from CLI.


# Start

Thr programm could be started via CLI

```java -jar target/jasperreportscleaner-0.1.0.jar -f fileName[,fileName]```

The version "0.1.0" may change.

The application will list unused fields, parameters and variables on the console.