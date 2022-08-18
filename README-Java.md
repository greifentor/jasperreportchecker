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

The program could be started via CLI

```java -jar target/jasperreportscleaner-1.0.0.jar COMMAND OPTIONS```

The version "1.0.0" may change.

The application will execute the passed command with the passed options.

## Commands

### Check

This command (```check```) starts a check for unused fields, parameters and variables in JRXML files
specified by the passed options.

#### Options

* ```-d``` DIRECTORY_NAME - sets a directory to search into for a pattern specified by the ```-p``` option. Is set to "." if not passed. Also sub directories will be processed.
* ```-f``` FILE_NAME[,FILE_NAME] - specifies matching file names to process.
* ```-p``` PATTERN - sets a pattern for search matching files (e. g. "*.JRXML").
* ```-snfm``` - suppresses any message for file which not contain any unused fields, parameters or variables.

All options could be used together with a single ```check``` command call and add matching files to a
single list of files to respect in the application run.

**Note:** There is a strange behavior with Windows Powershell: This CLI changes the pattern parameter to a list of 
matching file names if there are some in the current directory.