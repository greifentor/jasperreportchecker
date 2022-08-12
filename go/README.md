# jasperreportchecker

A tool which checks jasperreports JRXML files for unused fields, parameters and variables.


# go

## Requirements

* Go: 1.18


## Build

Change into the project folder with a CLI and build the application via

```go build jrc.go jrxmlreader.go```

or just

```go build .```



## Run

Change into the project folder with a CLI and start the application either by

```go run jrc.go jrxmlreader.go {jrxmlFileName}```

or 

```go run . {jrxmlFileName}```

or 

```.\jrc.exe {jrxmlFileName}``` (Windows)

or 

```.\jrc {jrxmlFileName}``` (Linux)

in case the project is already build.

The unused fields, parameters or variables will be listed on the console. If no unused fields, parameters or variables are found
there will be a specific message.
