package main

import (
	"fmt"
	"log"
	"os"
	"strings"
)

func main() {
	log.Println("Started JasperReports Cleaner ================================================================================================")
	if len(os.Args) < 2 {
		log.Fatalln("Missing JRXML file name")
		return
	}
	var fileName = os.Args[1]
	var report = ReadJasperReport(fileName)
	content, error := os.ReadFile(fileName)
	if error != nil {
		log.Fatal(error)
	}
	unused := make([]string, 0)
	for _, field := range report.Fields {
		if !strings.Contains(string(content[:]), "$F{"+field.Name+"}") {
			unused = append(unused, "Field: "+field.Name)
		}
	}
	for _, parameter := range report.Parameters {
		if !strings.Contains(string(content[:]), "$P{"+parameter.Name+"}") {
			unused = append(unused, "Parameter: "+parameter.Name)
		}
	}
	for _, variable := range report.Variables {
		if !strings.Contains(string(content[:]), "$V{"+variable.Name+"}") {
			unused = append(unused, "Variable: "+variable.Name)
		}
	}
	if len(unused) > 0 {
		for _, s := range unused {
			fmt.Println(s)
		}
	} else {
		fmt.Println("No unused fields, parameters or variables!")
	}
	log.Println("Finished JasperReports Cleaner ===============================================================================================")
}
