package main

import (
	"encoding/xml"
    "fmt"
	"io/ioutil"
	"log"
    "os"
)

type JasperReport struct {
	XMLName      xml.Name     `xml:"jasperReport"`
	Fields       []Field      `xml:"field"`
	Parameters   []Parameter  `xml:"parameter"`
	Variables    []Variable   `xml:"variable"`
}

type Field struct {
	XMLName    xml.Name     `xml:"field"`
	Name       string       `xml:"name,attr"`
}

type Parameter struct {
	XMLName    xml.Name     `xml:"parameter"`
	Name       string       `xml:"name,attr"`
}

type Variable struct {
	XMLName    xml.Name     `xml:"variable"`
	Name       string       `xml:"name,attr"`
}

func ReadJasperReport(fileName string) *JasperReport {
	report := new(JasperReport)
	content, err := ioutil.ReadFile(fileName)
	if err != nil {
		log.Fatal(err)
	}
	data := string(content)
	if err := xml.Unmarshal([]byte(data), report); err != nil {
		panic(err)
	}
	return report
}


func main () {
	log.Println("Started JasperReports Cleaner ================================================================================================")
	if len(os.Args) < 2 {
		log.Fatalln("Missing JRXML file name")
		return
	}
	var report = ReadJasperReport(os.Args[1])
    for _, field := range report.Fields {
        fmt.Println("$F{" + field.Name + "}")
    }
    for _, parameter := range report.Parameters {
        fmt.Println("$P{" + parameter.Name + "}")
    }
    for _, variable := range report.Variables {
        fmt.Println("$V{" + variable.Name + "}")
    }
	log.Println("Finished JasperReports Cleaner ===============================================================================================")
}
