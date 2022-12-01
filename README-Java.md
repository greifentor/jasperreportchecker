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
matching file names if there are some in the current directory. This argument list is not accepted by the application.
Instead of using the Powershell start the application via the simple (old) windows command line (german
"Eingabeaufforderung"). Use '"' for the value of the ``-p`` parameter.

```
java -jar target\jasperreportscleaner-1.0.0.jar check -d C:\workspace\document-project\ -p "*.jrxml"
```

### GUI

This command (```gui```) starts the application with a Swing GUI which allows to selected files for a check in a
comfortable way.

Passing VM argument ``-Djrc.language=DE`` allows to switch the language to German.

|Command     |Description|
|------------|-----------|
|Check       |Checks for unused fields, parameters and variables.|
|Find Orphans|Finds orphaned sub reports in a project.|
|Font Lister |Lists used fonts in alle documents of a project.|
|Usage       |Creates a PlantUML diagram with the report relations.|
|XML         |Creates a sample XML for a specified report (not 100% complete until now).|

All commands could be selected by the tabs when the application is started. Descriptions of application behavior are to find in the top field of any command panel.
